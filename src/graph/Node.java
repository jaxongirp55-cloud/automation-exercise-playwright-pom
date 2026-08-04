package graph;

import models.Warehouse;

/**
 * Represents a Vertex/Node in the Graph containing a Warehouse object.
 *
 * Time Complexity (Creation): O(1)
 * Space Complexity: O(1)
 */
public class Node {
    private Warehouse warehouse;

    /**
     * Constructs a Node containing a physical Warehouse.
     * @param warehouse Physical warehouse
     */
    public Node(Warehouse warehouse) {
        if (warehouse == null) {
            throw new IllegalArgumentException("Node warehouse cannot be null");
        }
        this.warehouse = warehouse;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        if (warehouse == null) {
            throw new IllegalArgumentException("Warehouse cannot be null");
        }
        this.warehouse = warehouse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return warehouse.equals(node.warehouse);
    }

    @Override
    public int hashCode() {
        return warehouse.hashCode();
    }

    @Override
    public String toString() {
        return warehouse.getName() + " (" + warehouse.getLocation() + ")";
    }
}
