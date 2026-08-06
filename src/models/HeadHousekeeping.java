package models;

/**
 * Concrete Staff extension for Head Housekeeping role.
 * Responsibilities focused entirely on clearing and prepping Rooms.
 */
public class HeadHousekeeping extends Staff {

    /**
     * Constructs a Head Housekeeping employee.
     *
     * @param staffId Unique ID.
     * @param name    Full name.
     */
    public HeadHousekeeping(String staffId, String name) {
        super(staffId, name, "Head Housekeeping");
    }

    @Override
    public boolean canManageRooms() {
        // Can clear rooms
        return true;
    }

    @Override
    public boolean canManageBookings() {
        return false;
    }

    @Override
    public boolean canManageQueue() {
        return false;
    }

    @Override
    public boolean canManageBilling() {
        return false;
    }

    @Override
    public boolean canViewReports() {
        return false;
    }

    @Override
    public boolean canModifyStrategy() {
        return false;
    }
}
