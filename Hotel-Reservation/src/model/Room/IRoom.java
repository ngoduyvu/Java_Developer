package model.Room;

import model.Room.RoomType;

public interface IRoom {
    public String getRoomNumber();
    public RoomType getRoomType();
    public Double getPrice();
}
