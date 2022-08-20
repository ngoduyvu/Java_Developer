package service;

import model.Customer.Customer;
import model.Room.IRoom;
import model.Reservation.Reservation;
import java.util.Set;
import java.util.Date;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class ReservationService implements AddInterface {
    private static int DEFAULT_ADD_DAYS = 7;
    private static ReservationService SINGLETON = new ReservationService();
    private Set<IRoom> rooms;
    private Set<Reservation> reservations;

    private ReservationService() {
        this.rooms = new HashSet<>();
        this.reservations = new HashSet<>();
    }

    public static ReservationService getSingleton() {
        return SINGLETON;
    }

    public Collection<IRoom> getAllRooms() {
        return rooms;
    }
    public void addRoom(IRoom room) {
        for(IRoom existRoom: rooms) {
            if(existRoom.equals(room)) {
                rooms.remove(existRoom);
            }
        }
        rooms.add(room);
    }

    public IRoom getRoom(String roomNo) {
        for(IRoom room: rooms) {
            if(room.getRoomNumber().equals(roomNo)) {
                return room;
            }
        }
        return null;
    }

    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {
        if(reservations.isEmpty()) {
            return rooms;
        } else {
            return findAvailableRooms(checkInDate, checkOutDate);
        }
    }

    private Collection<IRoom> findAvailableRooms(Date checkInDate, Date checkOutDate) {
        List<IRoom> availableRooms = new ArrayList<>();

        for(Reservation reservation: reservations) {
            if(!reservation.getCheckInDate().after(checkInDate) || !reservation.getCheckOutDate().before(checkOutDate)) {
                for(IRoom room: rooms) {
                    if(!reservation.getRoom().equals(room)) {
                        if(checkRoomInReserve(room, checkInDate, checkOutDate) && (!availableRooms.contains(room))) {
                            availableRooms.add(room);
                        }
                    }
                }
            } else if(reservation.getCheckInDate().after(checkInDate) && reservation.getCheckOutDate().after(checkOutDate)) {
                for(IRoom room: rooms) {
                    if(!availableRooms.contains(room)) {
                        availableRooms.add(room);
                    }
                }
            }
        }
        return availableRooms;
    }

    private Boolean checkRoomInReserve(IRoom room, Date CheckIn, Date CheckOut) {
        for(Reservation reservation: reservations) {
            if(reservation.getRoom().equals(room)) {
                if (CheckIn.before(reservation.getCheckOutDate())
                        && CheckOut.after(reservation.getCheckInDate())) {
                    return false;
                }
            }
        }
        return true;
    }
    public Collection<IRoom> findAlternativeRooms(Date checkInDate, Date checkOutDate) {
        return findRooms(addDefaultDays(checkInDate), addDefaultDays(checkOutDate));
    }
    public Date addDefaultDays(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, DEFAULT_ADD_DAYS);
        return calendar.getTime();
    }

    public Reservation reserveRoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);
        reservations.add(reservation);
        return reservation;
    }

    public Collection<Reservation> getCustomersReservation(Customer customer) {
        List<Reservation> reservationsByCustomer = new ArrayList<>();
        for(Reservation reservation: reservations) {
            if(reservation.getCustomer().equals(customer)) {
                reservationsByCustomer.add(reservation);
            }
        }
        return reservationsByCustomer;
    }

    public void printAllReservation() {
        if(reservations.isEmpty()) {
            //System.out.println("No reservations found!");
            noReserve();
        } else {
            System.out.println(reservations + "\n");
        }
    }

}

interface AddInterface {
    default void noReserve()
    {
        System.out.println("No reservations found!");
    }
}
