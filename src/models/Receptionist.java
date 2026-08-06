package models;

/**
 * Concrete Staff extension for Receptionist role.
 * Primarily handles customer interactions, reservations, and general billing.
 */
public class Receptionist extends Staff {

    /**
     * Constructs a Receptionist employee.
     *
     * @param staffId Unique ID.
     * @param name    Full name.
     */
    public Receptionist(String staffId, String name) {
        super(staffId, name, "Receptionist");
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
        return false;
    }

    @Override
    public boolean canModifyStrategy() {
        return false;
    }
}
