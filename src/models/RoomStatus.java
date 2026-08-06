package models;

/**
 * Represents the lifecycle states of a hotel room in the StayEase System.
 */
public enum RoomStatus {
    /**
     * Room is clean and available for reservation.
     */
    FREE,

    /**
     * Room is reserved for a future guest but not yet occupied.
     */
    RESERVED,

    /**
     * Room is currently occupied by checked-in guests.
     */
    OCCUPIED,

    /**
     * Guest has checked out or requested checkout; bill generation is pending.
     */
    AWAITING_BILL,

    /**
     * Guest has settled the bill; room requires housekeeping attention.
     */
    CLEARED
}
