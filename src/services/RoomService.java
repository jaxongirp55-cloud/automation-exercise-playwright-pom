package services;

import models.Room;
import models.RoomStatus;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service class that manages hotel rooms and their lifecycle transitions in-memory.
 */
public class RoomService {
    private final Map<Integer, Room> rooms;

    /**
     * Constructor for RoomService.
     */
    public RoomService() {
        this.rooms = new ConcurrentHashMap<>();
    }

    /**
     * Creates a new room and registers it.
     * Throws an exception if the room number already exists.
     *
     * @param roomNumber Unique room number.
     * @param roomType Type of room (e.g. Single, Double, Suite).
     * @param basePrice Cost per night.
     * @return The created Room object.
     * @throws IllegalArgumentException If the room number already exists or input is invalid.
     */
    public Room createRoom(int roomNumber, String roomType, double basePrice) {
        if (roomNumber <= 0) {
            throw new IllegalArgumentException("Room number must be greater than zero.");
        }
        if (basePrice < 0) {
            throw new IllegalArgumentException("Room base price cannot be negative.");
        }
        if (roomType == null || roomType.trim().isEmpty()) {
            throw new IllegalArgumentException("Room type cannot be empty.");
        }
        if (rooms.containsKey(roomNumber)) {
            throw new IllegalArgumentException("Duplicate room error: Room #" + roomNumber + " already exists.");
        }

        Room newRoom = new Room(roomNumber, roomType, basePrice);
        rooms.put(roomNumber, newRoom);
        return newRoom;
    }

    /**
     * Finds a room by its number.
     * @param roomNumber Room number.
     * @return The Room object, or null if not found.
     */
    public Room getRoom(int roomNumber) {
        return rooms.get(roomNumber);
    }

    /**
     * Returns a list of all rooms in the hotel, sorted by room number.
     * @return Sorted list of all rooms.
     */
    public List<Room> getAllRooms() {
        List<Room> list = new ArrayList<>(rooms.values());
        list.sort((r1, r2) -> Integer.compare(r1.getRoomNumber(), r2.getRoomNumber()));
        return list;
    }

    /**
     * Clears a room's state.
     * Transition: AWAITING_BILL / CLEARED / OCCUPIED -> CLEARED -> FREE.
     * Housekeeping clears dirty/occupied rooms back to FREE.
     *
     * @param roomNumber The room number to clear.
     * @throws IllegalArgumentException If room doesn't exist.
     */
    public void clearRoom(int roomNumber) {
        Room room = rooms.get(roomNumber);
        if (room == null) {
            throw new IllegalArgumentException("Room #" + roomNumber + " does not exist.");
        }

        RoomStatus current = room.getStatus();
        if (current == RoomStatus.FREE) {
            throw new IllegalArgumentException("Room #" + roomNumber + " is already FREE.");
        }

        // Standard sequence: occupied -> checkout (awaiting bill) -> payment (cleared) -> housekeeping clear (free)
        if (current == RoomStatus.CLEARED) {
            room.setStatus(RoomStatus.FREE);
        } else {
            // Forcefully clear by Head Housekeeping or manager
            room.setStatus(RoomStatus.CLEARED);
        }
    }

    /**
     * Helper to load predefined rooms into the map.
     * @param room The room to add directly.
     */
    public void addRoomDirectly(Room room) {
        if (room != null) {
            rooms.put(room.getRoomNumber(), room);
        }
    }

    /**
     * Clears all in-memory rooms.
     */
    public void clearAllRooms() {
        rooms.clear();
    }
}
