package roomitems;

/**
 * Represents an abstract Room Item that can be ordered by customers.
 * This class is part of the Factory pattern implementation for room service items.
 * All subclasses (Starter, MainCourse, Dessert, Beverage, ComboMeal) inherit from this class.
 */
public abstract class RoomItem {
    private String name;
    private double price;

    /**
     * Constructor for RoomItem.
     * @param name The name of the item.
     * @param price The price of the item.
     */
    protected RoomItem(String name, double price) {
        this.name = name;
        this.price = price;
    }

    /**
     * Gets the name of the room item.
     * @return The item name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the room item.
     * @param name The item name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the base price of the room item.
     * @return The item base price.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the base price of the room item.
     * @param price The item base price.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return String.format("%s - $%.2f", name, price);
    }
}
