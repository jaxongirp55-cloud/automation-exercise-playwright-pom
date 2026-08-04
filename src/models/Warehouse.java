package models;

/**
 * Represents a physical Warehouse in the logistics network.
 * Satisfies OOP encapsulation with private fields, constructor, getters, setters, and toString.
 *
 * Time Complexity (Creation/Access): O(1)
 * Space Complexity: O(1)
 */
public class Warehouse {
    private String id;
    private String name;
    private String location;

    /**
     * Constructs a Warehouse instance.
     * @param id Unique identifier of the warehouse
     * @param name Name of the warehouse
     * @param location Geographical location or city of the warehouse
     */
    public Warehouse(String id, String name, String location) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Warehouse ID cannot be null or empty");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Warehouse Name cannot be null or empty");
        }
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("Warehouse Location cannot be null or empty");
        }
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Warehouse ID cannot be null or empty");
        }
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Warehouse Name cannot be null or empty");
        }
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("Warehouse Location cannot be null or empty");
        }
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Warehouse warehouse = (Warehouse) o;
        return id.equals(warehouse.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Warehouse{" +
                "ID='" + id + '\'' +
                ", Name='" + name + '\'' +
                ", Location='" + location + '\'' +
                '}';
    }
}
