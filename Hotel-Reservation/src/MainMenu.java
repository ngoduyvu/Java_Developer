import api.HotelResource;
import model.Room.IRoom;
import model.Reservation.Reservation;

import java.util.Collection;
import java.util.Date;
import java.util.Calendar;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class MainMenu {

    private static HotelResource hotelResource = HotelResource.getSINGLETON();
    // Scanner input = new Scanner(System.in);

    public static void mainMenu() {
        boolean end = false;
        String choice;
        Scanner input = new Scanner(System.in);
        while(!end) {
            do {
                printMainMenu();
                choice = input.next().trim();

                switch(choice) {
                    case "1":
                        findAndReserveRoom();
                        break;
                    case "2":
                        seeMyReservation();
                        break;
                    case "3":
                        createAccount();
                        break;
                    case "4":
                        AdminMenu.adminMenu();
                        break;
                    case "5":
                        end = true;
                        System.out.println("Exit\n");
                        break;
                    default:
                        System.out.println("Invalid Input.\n");
                }

            } while (!choice.equals("1") && !choice.equals("2") && !choice.equals("3") && !choice.equals("4") && !choice.equals("5") && end != true);
        }
    }

    private static void printMainMenu() {
        System.out.println("Welcome to the Hotel Reservation Application");
        System.out.println("--------------------------------------------");
        System.out.println("1. Find and reserve a room");
        System.out.println("2. See my reservations");
        System.out.println("3. Create an account");
        System.out.println("4. Admin");
        System.out.println("5. Exit");
        System.out.println("--------------------------------------------");
        System.out.println("Please select a number for the menu option");
    }

    private static void findAndReserveRoom() {
        Calendar calendar = Calendar.getInstance();

        Scanner input = new Scanner(System.in);
        System.out.println("Enter Check-In Date mm/dd/yyyy: ");
        Date checkIn = getInputDate(input);
        System.out.println("Enter Check-Out Date mm/dd/yyyy: ");
        Date checkOut = getInputDate(input);

        if ((checkIn != null && checkOut != null) && (checkIn.before(checkOut))) {
            Collection<IRoom> rooms = hotelResource.findRoom(checkIn, checkOut);

            if(!rooms.isEmpty()) {
                reserveRoom(input, checkIn, checkOut, rooms);
            } else {
                System.out.println("No rooms found!");
            }

        } else {
            System.out.println("Check-In and Check-Out Date need to be correct!\n");
        }
    }

    private static void reserveRoom(Scanner input, Date checkIn, Date checkOut, Collection<IRoom> rooms) {
        System.out.println("Would you like to book a room? Y/N");
        String answerBookRoom = input.nextLine();

        if(answerBookRoom.equals("Y") || answerBookRoom.equals("y")) {
            System.out.println("Do you have an account with us? Y/N");
            String answerAccount = input.nextLine();

            if(answerAccount.equals("Y") || answerAccount.equals("y")) {
                System.out.println("Enter Email email@domain.com: ");
                String email = input.nextLine();

                if(hotelResource.getCustomer(email) != null) {
                    System.out.println("What room number would you like to reserve?");
                    rooms.forEach(System.out::println);
                    String roomNumber = input.nextLine();

                    if(rooms.stream().anyMatch(room -> room.getRoomNumber().equals(roomNumber))) {
                        IRoom room = hotelResource.getRoom(roomNumber);
                        Reservation reservation = hotelResource.bookRoom(email, room, checkIn, checkOut);
                    } else {
                        System.out.println("Sorry, this room is not available. Please book another room!");
                    }
                    printMainMenu();
                } else {
                    System.out.println("Account not found.\nYou need to create a new account!");
                }
            }
        } else if(answerBookRoom.equals("N") || answerBookRoom.equals("n")) {
            printMainMenu();
        } else {
            System.out.println("Please answer Y or N");
            reserveRoom(input, checkIn, checkOut, rooms);
        }
    }
    private static void seeMyReservation() {
        Scanner input = new Scanner(System.in);

        System.out.println("Enter Email email@domain.com: ");
        String email = input.nextLine();

        Collection<Reservation> reservations = hotelResource.getCustomersReservations(email);
        if(reservations.isEmpty()) {
            System.out.println("You do not have any reservation!");
        } else {
            for(Reservation reservation : reservations) {
                System.out.println(reservation);
            }
        }

    }
    private static void createAccount() {
        Scanner input = new Scanner(System.in);

        System.out.println("Enter Email email@domain.com: ");
        String email = input.nextLine();
        System.out.println("First Name: ");
        String firstName = input.nextLine();
        System.out.println("Last Name: ");
        String lastName = input.nextLine();

        try {
            hotelResource.createCustomer(email, firstName, lastName);
            System.out.println("New Account created successfully!");
        } catch(IllegalArgumentException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }
    private static Date getInputDate(Scanner input) {
        String DATE_PATTERN = "MM/dd/yyyy";
        try {
            return new SimpleDateFormat(DATE_PATTERN).parse(input.nextLine());
        } catch(ParseException ex) {
            System.out.println("Error: Invalid date.\n");
        }
        return null;
    }
}
