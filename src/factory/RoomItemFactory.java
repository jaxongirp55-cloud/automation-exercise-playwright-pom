package factory;

import roomitems.*;

/**
 * Factory pattern implementation to instantiate various types of RoomItems.
 * Encapsulates the instantiation logic of RoomItem concrete objects.
 */
public class RoomItemFactory {

    /**
     * Creates a concrete RoomItem object according to category type.
     *
     * @param category The type of item (Starter, MainCourse, Dessert, Beverage, ComboMeal).
     * @param itemId   Unique identification ID.
     * @param name     Label name of menu option.
     * @param price    Calculated cost.
     * @return Generated concrete RoomItem or throws IllegalArgumentException.
     */
    public static RoomItem createItem(String category, String itemId, String name, double price) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null.");
        }

        switch (category.trim().toLowerCase()) {
            case "starter":
                return new Starter(itemId, name, price);
            case "maincourse":
            case "main course":
                return new MainCourse(itemId, name, price);
            case "dessert":
                return new Dessert(itemId, name, price);
            case "beverage":
                return new Beverage(itemId, name, price);
            case "combomeal":
            case "combo meal":
                return new ComboMeal(itemId, name, price);
            default:
                throw new IllegalArgumentException("Unknown room item category: " + category);
        }
    }
}
