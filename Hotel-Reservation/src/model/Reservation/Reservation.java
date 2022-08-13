package model.Reservation;

import model.Customer.Customer;
import model.Room.IRoom;

import java.util.Date;
import java.util.Objects;

public class Reservation {
    private Customer customer;
    private IRoom room;
    private Date checkInDate;
    private Date checkOutDate;

    public Reservation(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public Customer getCustomer() { return this.customer; }

    public IRoom getRoom() { return this.room; }

    public Date getCheckInDate() { return this.checkInDate; }

    public Date getCheckOutDate() { return this.checkOutDate; }

    @Override
    public String toString() {
        return "Customer: " + this.customer.toString() +
                "\nRoom: " + this.room.toString() +
                "\nCheck-In Date: " + this.checkInDate +
                "\nCheck-Out Date: " + this.checkOutDate;
    }

}
