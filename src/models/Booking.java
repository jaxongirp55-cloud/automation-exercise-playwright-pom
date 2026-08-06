package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a comprehensive Hotel Room Booking.
 * Aggregates information regarding Customer, Room, associated staff members,
 * ordered menu/room items, and pricing characteristics.
 */
public class Booking {
    private final String bookingId;
    private final Customer customer;
    private final Room room;
    private final Staff receptionist;
    private final List<BookingItem> items;
    private boolean isConfirmed;
    private boolean isCancelled;
    private int numberOfNights;

    /**
     * Constructs a Booking.
     *
     * @param bookingId      Unique booking identifier.
     * @param customer       The customer placing the booking.
     * @param room           The room allocated.
     * @param receptionist   The receptionist or front desk staff managing this booking.
     * @param numberOfNights Number of nights for the stay.
     */
    public Booking(String bookingId, Customer customer, Room room, Staff receptionist, int numberOfNights) {
        this.bookingId = bookingId;
        this.customer = customer;
        this.room = room;
        this.receptionist = receptionist;
        this.items = new ArrayList<>();
        this.isConfirmed = false;
        this.isCancelled = false;
        this.numberOfNights = numberOfNights;
    }

    public String getBookingId() {
        return bookingId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Room getRoom() {
        return room;
    }

    public Staff getReceptionist() {
        return receptionist;
    }

    public List<BookingItem> getItems() {
        return items;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.isConfirmed = confirmed;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.isCancelled = cancelled;
    }

    public int getNumberOfNights() {
        return numberOfNights;
    }

    public void setNumberOfNights(int numberOfNights) {
        this.numberOfNights = numberOfNights;
    }

    /**
     * Adds an item to the booking items list.
     *
     * @param item The BookingItem to include.
     */
    public void addItem(BookingItem item) {
        this.items.add(item);
    }

    /**
     * Calculates room lodging cost based on nights and base price.
     *
     * @return Nightly lodging cost.
     */
    public double getLodgingCost() {
        return room.getBasePrice() * numberOfNights;
    }

    /**
     * Calculates the cumulative subtotal of all additional ordered room items.
     *
     * @return Accumulated subtotal.
     */
    public double getAdditionalItemsCost() {
        double total = 0.0;
        for (BookingItem item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "ID='" + bookingId + '\'' +
                ", Customer=" + customer.getName() +
                ", Room=" + room.getRoomNumber() +
                ", Nights=" + numberOfNights +
                ", Active Items Count=" + items.size() +
                ", Confirmed=" + isConfirmed +
                ", Cancelled=" + isCancelled +
                '}';
    }
}
