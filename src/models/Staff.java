package models;

/**
 * Abstract class representing a generic Staff member in the hotel.
 * Provides basic staff parameters and outlines role-based access checks.
 */
public abstract class Staff {
    private final String staffId;
    private final String name;
    private final String role;

    /**
     * Constructs a base Staff member.
     *
     * @param staffId Unique Employee ID.
     * @param name    Full Employee Name.
     * @param role    Assigned Department or Role string.
     */
    protected Staff(String staffId, String name, String role) {
        this.staffId = staffId;
        this.name = name;
        this.role = role;
    }

    public String getStaffId() {
        return staffId;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    /**
     * Checks if this staff role possesses authority for Room Management workflows.
     *
     * @return True if permitted, otherwise false.
     */
    public abstract boolean canManageRooms();

    /**
     * Checks if this staff role possesses authority for Booking Management workflows.
     *
     * @return True if permitted, otherwise false.
     */
    public abstract boolean canManageBookings();

    /**
     * Checks if this staff role possesses authority for Queue operations.
     *
     * @return True if permitted, otherwise false.
     */
    public abstract boolean canManageQueue();

    /**
     * Checks if this staff role possesses authority to settle Bills.
     *
     * @return True if permitted, otherwise false.
     */
    public abstract boolean canManageBilling();

    /**
     * Checks if this staff role possesses authority to view History reports and Analytics.
     *
     * @return True if permitted, otherwise false.
     */
    public abstract boolean canViewReports();

    /**
     * Checks if this staff role possesses authority to configure dynamic Strategy rates.
     *
     * @return True if permitted, otherwise false.
     */
    public abstract boolean canModifyStrategy();

    @Override
    public String toString() {
        return "[" + role + "] " + name + " (ID: " + staffId + ")";
    }
}
