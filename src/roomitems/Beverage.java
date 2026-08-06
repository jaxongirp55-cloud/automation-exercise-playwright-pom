package roomitems;

/**
 * Represents a Beverage room item.
 * Extends the abstract RoomItem class.
 */
public class Beverage extends RoomItem {
    private boolean alcoholic;

    /**
     * Constructor for Beverage.
     * @param name The name of the beverage.
     * @param price The price of the beverage.
     * @param alcoholic Whether the beverage contains alcohol.
     */
    public Beverage(String name, double price, boolean alcoholic) {
        super(name, price);
        this.alcoholic = alcoholic;
    }

    /**
     * Checks if the beverage is alcoholic.
     * @return true if alcoholic, false otherwise.
     */
    public boolean isAlcoholic() {
        return alcoholic;
    }

    /**
     * Sets whether the beverage is alcoholic.
     * @param alcoholic true if alcoholic, false otherwise.
     */
    public void setAlcoholic(boolean alcoholic) {
        this.alcoholic = alcoholic;
    }

    @Override
    public String toString() {
        return String.format("[Beverage] %s %s", super.toString(), alcoholic ? "(Alcoholic)" : "(Non-alcoholic)");
    }
}
