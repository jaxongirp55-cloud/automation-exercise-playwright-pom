package models;

import roomitems.RoomItem;

/**
 * Represents a quantity of a RoomItem added to a room booking.
 */
public class BookingItem {
    private RoomItem roomItem;
    private int quantity;

    /**
     * Constructor for BookingItem.
     * @param roomItem The RoomItem being ordered.
     * @param quantity The amount ordered.
     */
    public BookingItem(RoomItem roomItem, int quantity) {
        this.roomItem = roomItem;
        this.quantity = quantity;
    }

    public RoomItem getRoomItem() {
        return roomItem;
    }

    public void setRoomItem(RoomItem roomItem) {
        this.roomItem = roomItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Calculates the subtotal for this item order.
     * @return Price times quantity.
     */
    public double getSubtotal() {
        return roomItem.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return String.format("%s x %d (Subtotal: $%.2f)", roomItem, quantity, getSubtotal());
    }
}
