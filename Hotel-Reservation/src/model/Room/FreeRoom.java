package model.Room;

import model.Room.RoomType;

public class FreeRoom extends Room{

    public FreeRoom(final String roomNumber, final RoomType enumeration) {
        super(roomNumber, enumeration, 0.0);
    }

    @Override
    public String toString() {
        return "FreeRoom:" + super.toString();
    }
}
