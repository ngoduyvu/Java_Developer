package api;

import model.Customer.Customer;
import model.Reservation.Reservation;
import model.Room.IRoom;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.Date;

public class HotelResource {
    private static HotelResource SINGLETON = new HotelResource();
    private CustomerService customerService = CustomerService.getSINGLETON();
    private ReservationService reservationService = ReservationService.getSingleton();

    private HotelResource() {}

    public static HotelResource getSINGLETON()  { return SINGLETON; }

    public Customer getCustomer(String email) {
        return customerService.getCustomer(email);
    }

    public void createCustomer(String email, String firstName, String lastName) {
        customerService.addCustomer(email, firstName, lastName);
    }

    public IRoom getRoom(String roomNo) {
        return reservationService.getRoom(roomNo);
    }
    public Collection<IRoom> findRoom(Date checkInDate, Date checkOutDate) {
        return reservationService.findRooms(checkInDate, checkOutDate);
    }

    public Reservation bookRoom(String email, IRoom room, Date checkInDate, Date checkOutDate) {
        return reservationService.reserveRoom(getCustomer(email), room, checkInDate, checkOutDate);
    }

    public Collection<Reservation> getCustomersReservations(String email) {
        return reservationService.getCustomersReservation(getCustomer(email));
    }
}
