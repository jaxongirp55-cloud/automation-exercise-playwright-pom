package roomitems;

/**
 * Concrete RoomItem representing nested multi-course Combo Meals.
 */
public class ComboMeal extends RoomItem {

    /**
     * Constructs a ComboMeal menu item.
     *
     * @param itemId Unique ID.
     * @param name   Combo package description.
     * @param price  Price of package meal.
     */
    public ComboMeal(String itemId, String name, double price) {
        super(itemId, name, price);
    }

    @Override
    public String getCategory() {
        return "Combo Meal";
    }
}
