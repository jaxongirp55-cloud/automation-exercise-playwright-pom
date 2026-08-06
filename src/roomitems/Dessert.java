package roomitems;

/**
 * Represents a Dessert dish room item.
 * Extends the abstract RoomItem class.
 */
public class Dessert extends RoomItem {

    /**
     * Constructor for Dessert.
     * @param name The name of the dessert item.
     * @param price The price of the dessert item.
     */
    public Dessert(String name, double price) {
        super(name, price);
    }

    @Override
    public String toString() {
        return "[Dessert] " + super.toString();
    }
}
