package models;

/**
 * Concrete Staff extension for FrontDesk role.
 * Possesses access to check-ins, queuing, and room updates but limited reporting/strategy control.
 */
public class FrontDesk extends Staff {

    /**
     * Constructs a FrontDesk employee.
     *
     * @param staffId Unique ID.
     * @param name    Full name.
     */
    public FrontDesk(String staffId, String name) {
        super(staffId, name, "FrontDesk");
    }

    @Override
    public boolean canManageRooms() {
        return true;
    }

    @Override
    public boolean canManageBookings() {
        return true;
    }

    @Override
    public boolean canManageQueue() {
        return true;
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
