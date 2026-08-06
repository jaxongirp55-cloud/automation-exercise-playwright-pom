package roomitems;

/**
 * Represents a Starter dish room item.
 * Extends the abstract RoomItem class.
 */
public class Starter extends RoomItem {

    /**
     * Constructor for Starter.
     * @param name The name of the starter item.
     * @param price The price of the starter item.
     */
    public Starter(String name, double price) {
        super(name, price);
    }

    @Override
    public String toString() {
        return "[Starter] " + super.toString();
    }
}
