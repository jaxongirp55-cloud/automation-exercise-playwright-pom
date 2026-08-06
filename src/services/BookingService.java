package services;

import models.*;
import roomitems.RoomItem;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class that coordinates customer bookings, room selections,
 * item orders, and booking modification or cancellation workflows.
 */
public class BookingService {
    private final List<Booking> activeBookings;

    /**
     * Constructs a new BookingService.
     */
    public BookingService() {
        this.activeBookings = new ArrayList<>();
    }

    /**
     * Instantiates a new Booking request.
     * Checks room availability and validates inputs.
     *
     * @param operator       Staff member initiating the booking.
     * @param bookingId      Unique booking identifier.
     * @param customer       The customer placing the booking.
     * @param room           The requested hotel room.
     * @param numberOfNights Number of nights for the stay.
     * @return Generated Booking instance.
     */
    public Booking createBooking(Staff operator, String bookingId, Customer customer, Room room, int numberOfNights) {
        if (!operator.canManageBookings()) {
            throw new SecurityException("Access Denied: Staff ID " + operator.getStaffId() + " cannot create bookings.");
        }
        if (bookingId == null || bookingId.trim().isEmpty()) {
            throw new IllegalArgumentException("Booking ID cannot be empty.");
        }
        if (getBooking(bookingId) != null) {
            throw new IllegalArgumentException("Duplicate Booking: Booking ID " + bookingId + " already exists.");
        }
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null.");
        }
        if (room == null) {
            throw new IllegalArgumentException("Room cannot be null.");
        }
        if (room.getStatus() != RoomStatus.FREE) {
            throw new IllegalStateException("Room " + room.getRoomNumber() + " is already occupied or reserved (Current status: " + room.getStatus() + ").");
        }
        if (numberOfNights <= 0) {
            throw new IllegalArgumentException("Nights of stay must be greater than zero.");
        }

        return new Booking(bookingId.trim(), customer, room, operator, numberOfNights);
    }

    /**
     * Adds an ordered menu item to an active booking.
     *
     * @param booking  The target booking.
     * @param item     The room menu item to order.
     * @param quantity The quantity ordered.
     */
    public void addItemToBooking(Booking booking, RoomItem item, int quantity) {
        if (booking == null) {
            throw new IllegalArgumentException("Booking cannot be null.");
        }
        if (item == null) {
            throw new IllegalArgumentException("Room item cannot be null.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Item quantity must be greater than zero.");
        }

        // Add item to booking
        booking.addItem(new BookingItem(item, quantity));
    }

    /**
     * Modifies the nights of stay for a given booking.
     *
     * @param booking        The target booking.
     * @param numberOfNights The updated number of nights.
     */
    public void modifyBookingNights(Booking booking, int numberOfNights) {
        if (booking == null) {
            throw new IllegalArgumentException("Booking cannot be null.");
        }
        if (numberOfNights <= 0) {
            throw new IllegalArgumentException("Nights must be greater than zero.");
        }
        booking.setNumberOfNights(numberOfNights);
    }

    /**
     * Obtains an active booking by its ID.
     *
     * @param bookingId The booking designation.
     * @return Booking object, or null if not found.
     */
    public Booking getBooking(String bookingId) {
        if (bookingId == null) return null;
        for (Booking b : activeBookings) {
            if (b.getBookingId().equalsIgnoreCase(bookingId.trim())) {
                return b;
            }
        }
        return null;
    }

    /**
     * Returns all globally active bookings.
     *
     * @return List of bookings.
     */
    public List<Booking> getActiveBookings() {
        return activeBookings;
    }

    /**
     * Confirms the booking manually if bypassing the Command queue.
     *
     * @param booking Target booking.
     */
    public void confirmBooking(Booking booking) {
        if (booking == null) {
            throw new IllegalArgumentException("Booking cannot be null.");
        }
        booking.setConfirmed(true);
        booking.getRoom().setStatus(RoomStatus.RESERVED);
        if (!activeBookings.contains(booking)) {
            activeBookings.add(booking);
        }
    }

    /**
     * Removes booking from active list (e.g. after cancellation or completion).
     *
     * @param booking Target booking.
     */
    public void removeActiveBooking(Booking booking) {
        if (booking != null) {
            activeBookings.remove(booking);
        }
    }
}
