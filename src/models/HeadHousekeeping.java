package models;

/**
 * Represents the Head of Housekeeping staff member.
 * Head Housekeeping can view rooms and clear dirty rooms.
 */
public class HeadHousekeeping extends Staff {

    /**
     * Constructor for HeadHousekeeping.
     * @param id Employee ID.
     * @param name Name of the employee.
     */
    public HeadHousekeeping(String id, String name) {
        super(id, name, "Head Housekeeping");
    }

    @Override
    public boolean hasPermission(String action) {
        if (action == null) {
            return false;
        }
        String act = action.toUpperCase();
        return act.equals("CLEAR_ROOM") ||
               act.equals("VIEW_ROOMS");
    }
}
