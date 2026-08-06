package models;

/**
 * Enum representing the current physical and transaction lifecycle state of a hotel room.
 */
public enum RoomStatus {
    FREE,
    RESERVED,
    OCCUPIED,
    AWAITING_BILL,
    CLEARED
}
