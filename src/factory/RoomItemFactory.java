package factory;

import roomitems.*;

/**
 * Factory class to create instances of RoomItem subclasses.
 * This class implements the Factory design pattern.
 */
public class RoomItemFactory {

    /**
     * Creates a RoomItem object based on the item type and properties.
     *
     * @param type The type of room item (e.g., "starter", "main", "dessert", "beverage", "combo").
     * @param name The name of the item.
     * @param price The base price of the item.
     * @param extra Optional extra argument (Boolean for Beverage alcoholic, String for ComboMeal description, or null otherwise).
     * @return The concrete RoomItem instance, or null if type is unrecognized.
     */
    public static RoomItem createItem(String type, String name, double price, Object extra) {
        if (type == null) {
            return null;
        }

        String cleanType = type.trim().toLowerCase();
        switch (cleanType) {
            case "starter":
                return new Starter(name, price);
            case "main":
            case "maincourse":
                return new MainCourse(name, price);
            case "dessert":
                return new Dessert(name, price);
            case "beverage":
                boolean alcoholic = false;
                if (extra instanceof Boolean) {
                    alcoholic = (Boolean) extra;
                }
                return new Beverage(name, price, alcoholic);
            case "combo":
            case "combomeal":
                String description = "Delicious combo meal selection";
                if (extra instanceof String) {
                    description = (String) extra;
                }
                return new ComboMeal(name, price, description);
            default:
                return null;
        }
    }
}
