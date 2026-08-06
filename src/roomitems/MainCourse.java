package roomitems;

/**
 * Concrete RoomItem representing Main Course menu options.
 */
public class MainCourse extends RoomItem {

    /**
     * Constructs a MainCourse menu item.
     *
     * @param itemId Unique ID.
     * @param name   Course title.
     * @param price  Price of main course.
     */
    public MainCourse(String itemId, String name, double price) {
        super(itemId, name, price);
    }

    @Override
    public String getCategory() {
        return "Main Course";
    }
}
