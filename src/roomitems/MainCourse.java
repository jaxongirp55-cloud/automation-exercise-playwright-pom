package roomitems;

/**
 * Represents a Main Course dish room item.
 * Extends the abstract RoomItem class.
 */
public class MainCourse extends RoomItem {

    /**
     * Constructor for MainCourse.
     * @param name The name of the main course item.
     * @param price The price of the main course item.
     */
    public MainCourse(String name, double price) {
        super(name, price);
    }

    @Override
    public String toString() {
        return "[Main Course] " + super.toString();
    }
}
