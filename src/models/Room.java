package models;

/**
 * Represents a Hotel Room within the StayEase management system.
 * Keeps track of room identification, room type, pricing, and current status.
 */
public class Room {
    private final String roomNumber;
    private final String roomType;
    private final double basePrice;
    private RoomStatus status;

    /**
     * Constructs a new Room instance.
     *
     * @param roomNumber Unique room number string.
     * @param roomType   Type of room (e.g., Deluxe, Suite, Standard).
     * @param basePrice  Base nightly rate of the room.
     */
    public Room(String roomNumber, String roomType, double basePrice) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.basePrice = basePrice;
        this.status = RoomStatus.FREE;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public RoomStatus getStatus() {
        return status;
    }

    /**
     * Updates the status of the room according to the room lifecycle.
     *
     * @param status The new status of the room.
     */
    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Room{" +
                "Room No='" + roomNumber + '\'' +
                ", Type='" + roomType + '\'' +
                ", Base Price=" + basePrice +
                ", Status=" + status +
                '}';
    }
}
