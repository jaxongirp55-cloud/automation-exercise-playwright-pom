package models;

import roomitems.RoomItem;

/**
 * Represents an individual menu item or service requested as part of a Room Booking.
 * Captures the item and the quantity ordered.
 */
public class BookingItem {
    private final RoomItem item;
    private final int quantity;

    /**
     * Constructs a BookingItem.
     *
     * @param item     The room menu/service item.
     * @param quantity The quantity of the item ordered.
     */
    public BookingItem(RoomItem item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public RoomItem getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    /**
     * Calculates the total cost of this booking item based on unit price and quantity.
     *
     * @return Calculated subtotal of this item.
     */
    public double getSubtotal() {
        return item.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return item.getName() + " x" + quantity + " (Subtotal: $" + String.format("%.2f", getSubtotal()) + ")";
    }
}
