package roomitems;

/**
 * Abstract class representing an item or service that can be ordered to a hotel room.
 * Demonstrates abstraction, encapsulation, and inheritance across different room menu classes.
 */
public abstract class RoomItem {
    private final String itemId;
    private final String name;
    private final double price;

    /**
     * Constructs a base RoomItem.
     *
     * @param itemId Unique identifier of the room item.
     * @param name   Label name.
     * @param price  Subtotal price rate.
     */
    protected RoomItem(String itemId, String name, double price) {
        this.itemId = itemId;
        this.name = name;
        this.price = price;
    }

    public String getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    /**
     * Obtains the category description of this item.
     *
     * @return Item classification name.
     */
    public abstract String getCategory();

    @Override
    public String toString() {
        return "[" + getCategory() + "] " + name + " ($" + String.format("%.2f", price) + ")";
    }
}
