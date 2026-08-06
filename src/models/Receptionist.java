package models;

/**
 * Represents a Hotel Receptionist staff member.
 * Receptionists can perform bookings, check ins, check outs, and handle billing.
 */
public class Receptionist extends Staff {

    /**
     * Constructor for Receptionist.
     * @param id Employee ID.
     * @param name Name of the receptionist.
     */
    public Receptionist(String id, String name) {
        super(id, name, "Receptionist");
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
               act.equals("MANAGE_BOOKINGS") ||
               act.equals("MANAGE_BILLING") ||
               act.equals("VIEW_ROOMS") ||
               act.equals("CHANGE_STRATEGY");
    }
}
