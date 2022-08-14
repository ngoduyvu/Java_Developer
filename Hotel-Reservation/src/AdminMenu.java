import api.AdminResource;
import model.Customer.Customer;
import model.Room.IRoom;
import model.Room.Room;
import model.Room.RoomType;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class AdminMenu {
    private static AdminResource adminResource = AdminResource.getSINGLETON();

    public static void adminMenu() {
        boolean end = false;
        String choice;
        Scanner input = new Scanner(System.in);

        while(!end) {
            do {
                printAdminMenu();
                choice = input.next().trim();

                switch(choice) {
                    case "1":
                        showAllCustomers();
                        break;
                    case "2":
                        showAllRooms();
                        break;
                    case "3":
                        showAllReservations();
                        break;
                    case "4":
                        addRoom();
                        break;
                    case "5":
                        MainMenu mainMenu = new MainMenu();
                        mainMenu.mainMenu();
                        end = true;
                        break;
                    default:
                        System.out.println("Invalid Input.\n");
                }

            } while (!choice.equals("1") && !choice.equals("2") && !choice.equals("3") && !choice.equals("4") && !choice.equals("5") && !choice.equals("6") && end);
        }
    }


    private static void printAdminMenu() {
        System.out.println("Admin Menu");
        System.out.println("--------------------------------------------");
        System.out.println("1. See all Customers");
        System.out.println("2. See all Rooms");
        System.out.println("3. See all Reservations");
        System.out.println("4. Add a Room");
        System.out.println("5. Back to Main Menu");
        System.out.println("--------------------------------------------");
        System.out.println("Please select a number for the menu option");
    }

    private static void showAllCustomers() {
        Collection<Customer> customers = adminResource.getAllCustomers();

        if(customers.isEmpty()) {
            System.out.println("No customers found!");
        } else {
            for(Customer customer : customers) {
                System.out.println(customer.toString());
            }
        }
    }

    private static void showAllRooms() {
        Collection<IRoom> rooms = adminResource.getAllRooms();

        if(rooms.isEmpty()) {
            System.out.println("No rooms found.");
        } else {
            for(IRoom room : rooms) {
                System.out.println(room.toString());
            }
        }
    }

    private static void showAllReservations() {
        adminResource.displayAllReservations();
    }

    private static void addRoom() {
        String addRoom = "Y";
        List<IRoom> rooms = new ArrayList<>();
        Scanner input = new Scanner(System.in);
        boolean update;

        do {
            update = true;
            System.out.println("Enter room number: ");
            String roomNumber = input.nextLine();
            if(checkRoomExist(roomNumber)) {
                System.out.println("Room already exist!");
                update = checkAnswerUpdate(input);

            }
            if(update) {
                System.out.println("Enter price per night: ");
                Double roomPrice = checkRoomPrice(input);

                System.out.println("Enter room type: 1 - Single bed, 2 - Double bed");
                RoomType roomType = checkRoomType(input);
                IRoom room = new Room(roomNumber, roomType, roomPrice);
                adminResource.addRoom(Collections.singletonList(room));
                System.out.println("Room added successfully!");
            }
            do {
                System.out.println("Would you like to add another room? Y/N");
                addRoom = input.next().toLowerCase().trim();
            } while(!addRoom.equals("Y") && !addRoom.equals("y") && !addRoom.equals("N") && !addRoom.equals("n"));
            input.nextLine();
        } while(addRoom.equals("Y") || addRoom.equals("y"));
    }

    private static Boolean checkRoomExist(String roomNumber) {
        Collection<IRoom> rooms = adminResource.getAllRooms();
        for (IRoom room: rooms) {
            if(roomNumber.equals(room.getRoomNumber())) {
                return true;
            }
        }
        return false;
    }

    private static Boolean checkAnswerUpdate(Scanner input) {
        System.out.println("You want to update it? Y/N");
        String answer = input.nextLine();

        if(answer.equals("Y") || answer.equals("y")) {
            return true;
        } else if(answer.equals("N") || answer.equals("n")) {
            return false;
        } else {
            System.out.println("Invalid input!");
            return checkAnswerUpdate(input);
        }

    }
    private static RoomType checkRoomType(Scanner input) {
        String type = input.nextLine();
        try {
            return RoomType.checkRoomType(type);
        } catch (IllegalArgumentException exp) {
            System.out.println("Invalid room type! Enter room type: 1 - Single bed, 2 - Double bed");
            return checkRoomType(input);
        }
    }

    private static double checkRoomPrice(Scanner input) {
        String price = input.nextLine();
        try {
            return Double.parseDouble(price);
        } catch (NumberFormatException exp) {
            System.out.println("Invalid room price! Please enter correct value.");
            return checkRoomPrice(input);
        }
    }
}
