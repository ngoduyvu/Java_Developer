package menu;

import api.AdminResource;
import model.Customer.Customer;
import model.Room.IRoom;
import model.Room.Room;
import model.Room.RoomType;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class AdminMenu {
    private AdminResource adminResource = AdminResource.getSINGLETON();

    public void adminMenu() {
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


    private void printAdminMenu() {
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

    private void showAllCustomers() {
        Collection<Customer> customers = adminResource.getAllCustomers();

        if(customers.isEmpty()) {
            System.out.println("No customers found!");
        } else {
            for(Customer customer : customers) {
                System.out.println(customer.toString());
            }
        }
    }

    private void showAllRooms() {
        Collection<IRoom> rooms = adminResource.getAllRooms();

        if(rooms.isEmpty()) {
            System.out.println("No rooms found.");
        } else {
            for(IRoom room : rooms) {
                System.out.println(room.toString());
            }
        }
    }

    private void showAllReservations() {
        adminResource.displayAllReservations();
    }

    private void addRoom() {
        String addRoom = "Y";
        List<IRoom> rooms = new ArrayList<>();
        Scanner input = new Scanner(System.in);

        do {
            System.out.println("Enter room number: ");
            String roomNumber = input.nextLine();
            System.out.println("Enter price per night: ");
            Double roomPrice = input.nextDouble();

            System.out.println("Enter room type: 1 - Single bed, 2 - Double bed");
            RoomType roomType = checkRoomType(input);
            Room room = new Room(roomNumber, roomType, roomPrice);
            //adminResource.addRoom(Collections.singletonList(room));
            System.out.println("Room added successfully!");
        } while(addRoom.equals("Y") || addRoom.equals("y"));
    }

    private RoomType checkRoomType(Scanner input) {
        try {
            return RoomType.checkRoomType(input.nextLine());
        } catch (IllegalArgumentException exp) {
            System.out.println("Invalid room type! Enter room type: 1 - Single bed, 2 - Double bed");
            return checkRoomType(input);
        }
    }
}
