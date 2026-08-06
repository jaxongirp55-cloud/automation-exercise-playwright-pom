package services;

import models.Room;
import models.RoomStatus;
import models.Staff;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class that manages the complete lifecycle of hotel rooms.
 * Handles room registrations, status transitions, and role-based permissions.
 */
public class RoomService {
    private final List<Room> rooms;

    /**
     * Constructs a new RoomService instance.
     */
    public RoomService() {
        this.rooms = new ArrayList<>();
    }

    /**
     * Registers a new Room in the system.
     * Prevents duplicate room numbers.
     *
     * @param operator   Staff member executing the request.
     * @param roomNumber Unique room number designation.
     * @param roomType   Type of the room (e.g., Suite, Standard).
     * @param basePrice  Base price per night.
     * @throws IllegalArgumentException if staff permissions are invalid or room already exists.
     */
    public void createRoom(Staff operator, String roomNumber, String roomType, double basePrice) {
        if (!operator.canManageRooms()) {
            throw new SecurityException("Access Denied: Staff ID " + operator.getStaffId() + " cannot manage rooms.");
        }
        if (roomNumber == null || roomNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Room number cannot be empty.");
        }
        if (getRoom(roomNumber) != null) {
            throw new IllegalArgumentException("Duplicate Room: Room " + roomNumber + " already exists.");
        }
        if (basePrice <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero.");
        }

        rooms.add(new Room(roomNumber.trim(), roomType, basePrice));
    }

    /**
     * Finds a Room by room number.
     *
     * @param roomNumber The room designation.
     * @return Room object, or null if not found.
     */
    public Room getRoom(String roomNumber) {
        if (roomNumber == null) return null;
        for (Room r : rooms) {
            if (r.getRoomNumber().equalsIgnoreCase(roomNumber.trim())) {
                return r;
            }
        }
        return null;
    }

    /**
     * Returns a list of all registered rooms.
     *
     * @return List of rooms.
     */
    public List<Room> getAllRooms() {
        return new ArrayList<>(rooms);
    }

    /**
     * Clears all registered rooms.
     */
    public void clearRooms() {
        rooms.clear();
    }

    /**
     * Transitions a room to RESERVED state.
     *
     * @param roomNumber The room number.
     */
    public void reserveRoom(String roomNumber) {
        Room room = getRoom(roomNumber);
        if (room == null) {
            throw new IllegalArgumentException("Room " + roomNumber + " does not exist.");
        }
        if (room.getStatus() != RoomStatus.FREE) {
            throw new IllegalStateException("Room " + roomNumber + " is not FREE (Current status: " + room.getStatus() + ").");
        }
        room.setStatus(RoomStatus.RESERVED);
    }

    /**
     * Transitions a room from RESERVED to OCCUPIED state upon guest check-in.
     *
     * @param operator   Staff member performing check-in.
     * @param roomNumber The room designation.
     */
    public void checkInRoom(Staff operator, String roomNumber) {
        if (!operator.canManageBookings()) {
            throw new SecurityException("Access Denied: Staff ID " + operator.getStaffId() + " cannot check-in guests.");
        }
        Room room = getRoom(roomNumber);
        if (room == null) {
            throw new IllegalArgumentException("Room " + roomNumber + " does not exist.");
        }
        if (room.getStatus() != RoomStatus.RESERVED) {
            throw new IllegalStateException("Cannot check-in to Room " + roomNumber + " because it is not RESERVED (Current status: " + room.getStatus() + ").");
        }
        room.setStatus(RoomStatus.OCCUPIED);
    }

    /**
     * Transitions a room from OCCUPIED to AWAITING_BILL state.
     *
     * @param operator   Staff member performing checkout.
     * @param roomNumber The room designation.
     */
    public void checkOutRoom(Staff operator, String roomNumber) {
        if (!operator.canManageBookings()) {
            throw new SecurityException("Access Denied: Staff ID " + operator.getStaffId() + " cannot check-out guests.");
        }
        Room room = getRoom(roomNumber);
        if (room == null) {
            throw new IllegalArgumentException("Room " + roomNumber + " does not exist.");
        }
        if (room.getStatus() != RoomStatus.OCCUPIED) {
            throw new IllegalStateException("Cannot check-out Room " + roomNumber + " because it is not OCCUPIED (Current status: " + room.getStatus() + ").");
        }
        room.setStatus(RoomStatus.AWAITING_BILL);
    }

    /**
     * Transitions room from CLEARED to FREE after housekeeping has prepped it.
     * Alternatively, can handle cleaning of rooms from other dirty states.
     *
     * @param operator   Staff member clearing the room (e.g., HeadHousekeeping).
     * @param roomNumber The room designation.
     */
    public void clearRoom(Staff operator, String roomNumber) {
        if (!operator.canManageRooms()) {
            throw new SecurityException("Access Denied: Staff ID " + operator.getStaffId() + " is unauthorized to clear rooms.");
        }
        Room room = getRoom(roomNumber);
        if (room == null) {
            throw new IllegalArgumentException("Room " + roomNumber + " does not exist.");
        }
        if (room.getStatus() != RoomStatus.CLEARED && room.getStatus() != RoomStatus.AWAITING_BILL) {
            throw new IllegalStateException("Room " + roomNumber + " does not require clearing (Current status: " + room.getStatus() + ").");
        }
        room.setStatus(RoomStatus.FREE);
    }

    /**
     * Forcefully updates room status (e.g. for administrative corrections).
     *
     * @param operator   Staff member performing update.
     * @param roomNumber The room designation.
     * @param newStatus  Target status state.
     */
    public void updateRoomStatus(Staff operator, String roomNumber, RoomStatus newStatus) {
        if (!operator.canManageRooms()) {
            throw new SecurityException("Access Denied: Staff ID " + operator.getStaffId() + " is unauthorized to alter room status.");
        }
        Room room = getRoom(roomNumber);
        if (room == null) {
            throw new IllegalArgumentException("Room " + roomNumber + " does not exist.");
        }
        room.setStatus(newStatus);
    }
}
