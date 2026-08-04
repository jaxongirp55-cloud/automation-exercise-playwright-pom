package graph;

/**
 * Represents a weighted road connection (Edge) between two warehouses (Nodes) in the logistics graph.
 *
 * Big-O Complexity:
 * - Space Complexity: O(1) for instance variables
 * - Time Complexity: O(1) for standard getter/setter operations
 *
 * @author Senior Java Software Architect
 */
public class Edge {
    private Node source;
    private Node destination;
    private int weight;

    /**
     * Constructor for a weighted connection between two warehouses.
     * @param source The starting warehouse.
     * @param destination The ending warehouse.
     * @param weight The distance or cost of the connection (must be positive).
     */
    public Edge(Node source, Node destination, int weight) {
        if (source == null || destination == null) {
            throw new IllegalArgumentException("Source and destination nodes cannot be null.");
        }
        if (weight <= 0) {
            throw new IllegalArgumentException("Edge weight/distance must be a positive integer.");
        }
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    /**
     * Gets the source node.
     * @return source node.
     */
    public Node getSource() {
        return source;
    }

    /**
     * Sets the source node.
     * @param source the source node.
     */
    public void setSource(Node source) {
        if (source == null) {
            throw new IllegalArgumentException("Source node cannot be null.");
        }
        this.source = source;
    }

    /**
     * Gets the destination node.
     * @return destination node.
     */
    public Node getDestination() {
        return destination;
    }

    /**
     * Sets the destination node.
     * @param destination the destination node.
     */
    public void setDestination(Node destination) {
        if (destination == null) {
            throw new IllegalArgumentException("Destination node cannot be null.");
        }
        this.destination = destination;
    }

    /**
     * Gets the weight / distance of this edge.
     * @return weight as integer.
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Sets the weight / distance of this edge.
     * @param weight weight value.
     */
    public void setWeight(int weight) {
        if (weight <= 0) {
            throw new IllegalArgumentException("Edge weight must be a positive integer.");
        }
        this.weight = weight;
    }

    @Override
    public String toString() {
        return source.getName() + " -> " + destination.getName() + " (" + weight + " km)";
    }
}
