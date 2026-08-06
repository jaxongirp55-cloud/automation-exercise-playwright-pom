package models;

/**
 * Represents a Hotel Manager staff member.
 * Managers have full access permissions for all management actions.
 */
public class Manager extends Staff {

    /**
     * Constructor for Manager.
     * @param id Employee ID.
     * @param name Name of the manager.
     */
    public Manager(String id, String name) {
        super(id, name, "Manager");
    }

    @Override
    public boolean hasPermission(String action) {
        // Manager can perform any action in the system
        return true;
    }
}
