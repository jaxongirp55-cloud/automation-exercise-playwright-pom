package models;

/**
 * Represents a hotel room.
 * Tracks room attributes, including number, type, base price, and current status.
 */
public class Room {
    private int roomNumber;
    private String roomType;
    private double basePrice;
    private RoomStatus status;

    /**
     * Constructor for Room.
     * @param roomNumber The room's identifier.
     * @param roomType The classification of the room (e.g., Single, Double, Suite).
     * @param basePrice The base price per night.
     */
    public Room(int roomNumber, String roomType, double basePrice) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.basePrice = basePrice;
        this.status = RoomStatus.FREE; // Rooms start as FREE
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("Room #%d [%s] - Base Price: $%.2f | Status: %s",
                roomNumber, roomType, basePrice, status);
    }
}
