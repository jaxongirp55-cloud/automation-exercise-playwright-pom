package roomitems;

/**
 * Represents a Combo Meal room item that packages multiple items together at a combined rate.
 * Extends the abstract RoomItem class.
 */
public class ComboMeal extends RoomItem {
    private String description;

    /**
     * Constructor for ComboMeal.
     * @param name The name of the combo meal.
     * @param price The discounted combo price.
     * @param description What is included in this combo meal.
     */
    public ComboMeal(String name, double price, String description) {
        super(name, price);
        this.description = description;
    }

    /**
     * Gets the description of what is included.
     * @return Description text.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the combo meal.
     * @param description Description text.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format("[Combo Meal] %s (%s)", super.toString(), description);
    }
}
