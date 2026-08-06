package models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a booking transaction for a hotel room.
 * Tracks the room, customer, handling staff, ordered services/items, and transaction states.
 */
public class Booking {
    private String bookingId;
    private Room room;
    private Customer customer;
    private Staff staff;
    private List<BookingItem> bookingItems;
    private LocalDateTime timestamp;
    private boolean confirmed;
    private boolean checkedIn;
    private boolean checkedOut;

    /**
     * Constructor for Booking.
     * @param bookingId Unique identifier.
     * @param room The reserved room.
     * @param customer The guest placing the booking.
     * @param staff The staff member managing the process.
     */
    public Booking(String bookingId, Room room, Customer customer, Staff staff) {
        this.bookingId = bookingId;
        this.room = room;
        this.customer = customer;
        this.staff = staff;
        this.bookingItems = new ArrayList<>();
        this.timestamp = LocalDateTime.now();
        this.confirmed = false;
        this.checkedIn = false;
        this.checkedOut = false;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public List<BookingItem> getBookingItems() {
        return bookingItems;
    }

    /**
     * Adds an item to this booking. If item already exists, updates quantity.
     * @param item The BookingItem to add.
     */
    public void addBookingItem(BookingItem item) {
        for (BookingItem existing : bookingItems) {
            if (existing.getRoomItem().getName().equalsIgnoreCase(item.getRoomItem().getName())) {
                existing.setQuantity(existing.getQuantity() + item.getQuantity());
                return;
            }
        }
        bookingItems.add(item);
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public boolean isCheckedIn() {
        return checkedIn;
    }

    public void setCheckedIn(boolean checkedIn) {
        this.checkedIn = checkedIn;
    }

    public boolean isCheckedOut() {
        return checkedOut;
    }

    public void setCheckedOut(boolean checkedOut) {
        this.checkedOut = checkedOut;
    }

    /**
     * Calculates the subtotal of the room booking (room base price + all ordered items).
     * @return Subtotal amount.
     */
    public double calculateSubtotal() {
        double total = room.getBasePrice();
        for (BookingItem item : bookingItems) {
            total += item.getSubtotal();
        }
        return total;
    }

    @Override
    public String toString() {
        return String.format("Booking #%s | Room #%d | Guest: %s | Staff: %s | Confirmed: %b | Checked-In: %b | Checked-Out: %b | Base Price: $%.2f | Order Size: %d",
                bookingId, room.getRoomNumber(), customer.getName(), staff.getName(), confirmed, checkedIn, checkedOut, room.getBasePrice(), bookingItems.size());
    }
}
