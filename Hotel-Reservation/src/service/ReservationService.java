package service;

import model.Customer.Customer;
import model.Room.IRoom;
import model.Reservation.Reservation;
import java.util.Set;
import java.util.Date;
import java.util.HashSet;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

public class ReservationService {
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
//            if(reservation.getCheckInDate().before(checkOutDate)
//                    && reservation.getCheckOutDate().after(checkInDate)) {
            if(!reservation.getCheckInDate().after(checkInDate) && !reservation.getCheckOutDate().before(checkOutDate)) {
                for(IRoom room: rooms) {
                    if (!reservation.getRoom().equals(room)) {
                        availableRooms.add(room);
                    }
                }
            }
        }
        return availableRooms;
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
            System.out.println("No reservations found!");
        } else {
            System.out.println(reservations + "\n");
        }
    }
}
