package api;

import model.Customer.Customer;
import model.Room.IRoom;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.List;
public class AdminResource {

    private static AdminResource SINGLETON = new AdminResource();
    private CustomerService customerService = CustomerService.getSINGLETON();
    private ReservationService reservationService = ReservationService.getSingleton();

    private AdminResource() {}

    public static AdminResource getSINGLETON()  { return SINGLETON; }

    public Customer getCustomer(String email) {
        return customerService.getCustomer(email);
    }

    public Collection<Customer> getAllCustomers() {
        return customerService.getAllCustomer();
    }

    public Collection<IRoom> getAllRooms() {
        return reservationService.getAllRooms();
    }

    public void addRoom(List<IRoom> rooms) {
        for (IRoom room: rooms) {
            reservationService.addRoom(room);
        }
    }

    public void displayAllReservations() {
        reservationService.printAllReservation();
    }
}
