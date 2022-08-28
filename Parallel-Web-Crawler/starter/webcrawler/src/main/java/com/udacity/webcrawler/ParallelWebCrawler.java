package com.udacity.webcrawler;

import com.udacity.webcrawler.json.CrawlResult;
import com.udacity.webcrawler.parser.PageParserFactory;
import com.udacity.webcrawler.parser.PageParser;

import javax.inject.Inject;
import javax.inject.Provider;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;
import java.util.regex.Pattern;

/**
 * A concrete implementation of {@link WebCrawler} that runs multiple threads on a
 * {@link ForkJoinPool} to fetch and process multiple web pages in parallel.
 */
final class ParallelWebCrawler implements WebCrawler {
  private final Clock clock;
  private final Duration timeout;
  private final int popularWordCount;
  private final ForkJoinPool pool;
  private final PageParserFactory parserFactory;
  private final int maxDepth;
  private final List<Pattern> ignoredUrls;

  @Inject
  ParallelWebCrawler(
          Clock clock,
          @Timeout Duration timeout,
          @PopularWordCount int popularWordCount,
          @MaxDepth int maxDepth,
          PageParserFactory parserFactory,
          @IgnoredUrls List<Pattern> ignoredUrls,
          @TargetParallelism int threadCount) {
    this.clock = clock;
    this.timeout = timeout;
    this.popularWordCount = popularWordCount;
    this.maxDepth = maxDepth;
    this.parserFactory = parserFactory;
    this.ignoredUrls = ignoredUrls;
    this.pool = new ForkJoinPool(Math.min(threadCount, getMaxParallelism()));
  }

  @Override
  public CrawlResult crawl(List<String> startingUrls) {
    Instant deadline = clock.instant().plus(timeout);
    ConcurrentMap<String, Integer> counts = new ConcurrentHashMap<>();
    ConcurrentSkipListSet<String> visited = new ConcurrentSkipListSet<>();
    for (String url : startingUrls) {
      pool.invoke(new internalCrawler(url, deadline, maxDepth, counts, visited, clock, parserFactory, ignoredUrls));
    }
    if (counts.isEmpty()) {
      return new CrawlResult.Builder()
              .setWordCounts(counts)
              .setUrlsVisited(visited.size())
              .build();
    }

    return new CrawlResult.Builder()
            .setWordCounts(WordCounts.sort(counts, popularWordCount))
            .setUrlsVisited(visited.size())
            .build();
  }

  public class internalCrawler extends RecursiveTask<Boolean> {
    private String url;
    private Instant deadline;
    private int maxDepth;
    private ConcurrentMap<String, Integer> counts;
    private ConcurrentSkipListSet<String> visitedUrls;
    private Clock clock;
    private PageParserFactory parserFactory;
    private List<Pattern> ignoredUrls;


    private internalCrawler(
            String url,
            Instant deadline,
            int maxDepth,
            ConcurrentMap<String, Integer> counts,
            ConcurrentSkipListSet<String> visitedUrls,
            Clock clock,
            PageParserFactory parserFactory,
            List<Pattern> ignoredUrls) {
      this.url = url;
      this.deadline = deadline;
      this.maxDepth = maxDepth;
      this.counts = counts;
      this.visitedUrls = visitedUrls;
      this.clock = clock;
      this.parserFactory = parserFactory;
      this.ignoredUrls = ignoredUrls;
    }

    @Override
    protected Boolean compute() {
      if (maxDepth == 0 || clock.instant().isAfter(deadline)) {
        return null;
      }
      for(Pattern pattern : ignoredUrls) {
        if(pattern.matcher(url).matches()) {
          return null;
        }
      }
      ReentrantLock lock = new ReentrantLock();
      try {
        lock.lock();
        if(!visitedUrls.add(url)) {
          return false;
        }
        visitedUrls.add(url);
        PageParser.Result result = parserFactory.get(url).parse();
        for(Map.Entry<String, Integer> e : result.getWordCounts().entrySet()) {
          if(counts.containsKey(e.getKey())) {
            counts.put(e.getKey(), e.getValue() + counts.get(e.getKey()));
          } else {
            counts.put(e.getKey(), e.getValue());
          }
        }
        List<internalCrawler> subtasks = new ArrayList<>();
        for(String link : result.getLinks()) {
          subtasks.add(new internalCrawler(link, deadline, maxDepth - 1, counts, visitedUrls, clock, parserFactory, ignoredUrls));
        }
        invokeAll(subtasks);
        return null;
      }
      catch(Exception exception) {
        exception.printStackTrace();
      }
      finally {
        lock.unlock();
      }
      return true;
    }
  }


  @Override
  public int getMaxParallelism() {
    return Runtime.getRuntime().availableProcessors();
  }
}
