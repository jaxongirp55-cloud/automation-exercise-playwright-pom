package roomitems;

/**
 * Concrete RoomItem representing hot/cold Beverages.
 */
public class Beverage extends RoomItem {

    /**
     * Constructs a Beverage menu item.
     *
     * @param itemId Unique ID.
     * @param name   Drink name.
     * @param price  Price of beverage.
     */
    public Beverage(String itemId, String name, double price) {
        super(itemId, name, price);
    }

    @Override
    public String getCategory() {
        return "Beverage";
    }
}
