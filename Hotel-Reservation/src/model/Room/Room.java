package model.Room;

import java.util.Objects;

public class Room implements IRoom {
    private String roomNumber;
    protected RoomType enumeration;
    private Double price;

    public Room(String roomNumber, RoomType enumeration, Double price) {
        this.roomNumber = roomNumber;
        this.enumeration = enumeration;
        this.price = price;
    }

    public String getRoomNumber() {
        return this.roomNumber;
    }

    public RoomType getRoomType() { return this.enumeration; }

    public Double getPrice() {
        return this.price;
    }

    @Override
    public String toString() {
        return "Room Number: " + this.roomNumber + " is " + "type " + this.enumeration + " and cost " + this.price;
    }
}
