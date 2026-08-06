package roomitems;

/**
 * Concrete RoomItem representing sweet Desserts.
 */
public class Dessert extends RoomItem {

    /**
     * Constructs a Dessert menu item.
     *
     * @param itemId Unique ID.
     * @param name   Dessert name.
     * @param price  Price of dessert.
     */
    public Dessert(String itemId, String name, double price) {
        super(itemId, name, price);
    }

    @Override
    public String getCategory() {
        return "Dessert";
    }
}
