package roomitems;

/**
 * Concrete RoomItem representing Starter appetizers.
 */
public class Starter extends RoomItem {

    /**
     * Constructs a Starter menu item.
     *
     * @param itemId Unique ID.
     * @param name   Appetizer name.
     * @param price  Price of starter.
     */
    public Starter(String itemId, String name, double price) {
        super(itemId, name, price);
    }

    @Override
    public String getCategory() {
        return "Starter";
    }
}
