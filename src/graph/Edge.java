package graph;

/**
 * Represents a weighted directed or undirected connection (edge) between two graph nodes.
 *
 * Time Complexity (Creation): O(1)
 * Space Complexity: O(1)
 */
public class Edge {
    private Node source;
    private Node destination;
    private double weight;

    /**
     * Constructs a weighted connection edge.
     * @param source Source node
     * @param destination Destination node
     * @param weight Edge distance/cost
     */
    public Edge(Node source, Node destination, double weight) {
        if (source == null || destination == null) {
            throw new IllegalArgumentException("Edge source and destination cannot be null");
        }
        if (weight < 0) {
            throw new IllegalArgumentException("Edge weight cannot be negative");
        }
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    public Node getSource() {
        return source;
    }

    public void setSource(Node source) {
        this.source = source;
    }

    public Node getDestination() {
        return destination;
    }

    public void setDestination(Node destination) {
        this.destination = destination;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        if (weight < 0) {
            throw new IllegalArgumentException("Weight cannot be negative");
        }
        this.weight = weight;
    }

    @Override
    public String toString() {
        return source.getWarehouse().getName() + " -> " + destination.getWarehouse().getName() + " (" + weight + " km)";
    }
}
