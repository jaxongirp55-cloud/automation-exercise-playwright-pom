package models;

/**
 * Concrete Staff extension for Manager role.
 * Possesses overarching administrative privileges across all operations.
 */
public class Manager extends Staff {

    /**
     * Constructs a Manager employee.
     *
     * @param staffId Unique ID.
     * @param name    Full name.
     */
    public Manager(String staffId, String name) {
        super(staffId, name, "Manager");
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
        return true;
    }

    @Override
    public boolean canViewReports() {
        return true;
    }

    @Override
    public boolean canModifyStrategy() {
        return true;
    }
}
