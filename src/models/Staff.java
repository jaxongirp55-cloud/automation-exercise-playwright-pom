package models;

/**
 * Base abstract class representing hotel staff.
 * Implements role-based permissions modeling.
 */
public abstract class Staff {
    private String id;
    private String name;
    private String role;

    /**
     * Constructor for Staff.
     * @param id Unique employee ID.
     * @param name Name of the employee.
     * @param role Role designation.
     */
    protected Staff(String id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Checks if this staff member has permission to perform a specific action.
     *
     * @param action The action keyword (e.g. "CREATE_ROOM", "RESERVE", "CHECK_IN", "CHECK_OUT", "CLEAR_ROOM", "VIEW_REPORTS", "CHANGE_STRATEGY").
     * @return true if permission is granted, false otherwise.
     */
    public abstract boolean hasPermission(String action);

    @Override
    public String toString() {
        return String.format("%s [ID: %s, Name: %s]", role, id, name);
    }
}
