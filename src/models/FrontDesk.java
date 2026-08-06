package models;

/**
 * Represents a Front Desk staff member.
 * Front Desk staff can handle reservations and check ins/check outs but cannot manage strategies or reports.
 */
public class FrontDesk extends Staff {

    /**
     * Constructor for FrontDesk.
     * @param id Employee ID.
     * @param name Name of the employee.
     */
    public FrontDesk(String id, String name) {
        super(id, name, "Front Desk");
    }

    @Override
    public boolean hasPermission(String action) {
        if (action == null) {
            return false;
        }
        String act = action.toUpperCase();
        return act.equals("RESERVE_ROOM") ||
               act.equals("CHECK_IN") ||
               act.equals("CHECK_OUT") ||
               act.equals("VIEW_ROOMS") ||
               act.equals("MANAGE_BOOKINGS");
    }
}
