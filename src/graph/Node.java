package graph;

/**
 * Represents a Node in the logistics graph, corresponding to a warehouse.
 * Implements encapsulation and follows SOLID design principles.
 *
 * Big-O Complexity:
 * - Space Complexity: O(1) for instance variables
 * - Time Complexity of standard methods: O(1)
 *
 * @author Senior Java Software Architect
 */
public class Node {
    private String name;

    /**
     * Constructor for creating a warehouse node.
     * @param name The unique name of the warehouse.
     */
    public Node(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Warehouse node name cannot be null or empty.");
        }
        this.name = name.trim();
    }

    /**
     * Gets the name of the warehouse.
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the warehouse.
     * @param name the new name.
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Warehouse node name cannot be null or empty.");
        }
        this.name = name.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;
        Node node = (Node) o;
        return name.equalsIgnoreCase(node.name);
    }

    @Override
    public int hashCode() {
        return name.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return name;
    }
}
