package graph;

import models.Warehouse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Custom graph implementation using an adjacency list.
 * Supports warehouse node additions, connections, and graph querying.
 *
 * Time Complexity (Add Vertex): O(1)
 * Time Complexity (Connect Vertex): O(1)
 * Time Complexity (Display Graph): O(V + E) where V is vertices, E is edges.
 * Space Complexity: O(V + E) for storing adjacency lists.
 */
public class Graph {
    private final Map<Node, List<Edge>> adjacencyList;

    /**
     * Instantiates an empty Adjacency List graph.
     */
    public Graph() {
        this.adjacencyList = new HashMap<>();
    }

    /**
     * Adds a warehouse vertex to the graph.
     * @param warehouse Physical warehouse node to add
     * @return The created Node instance
     */
    public Node addWarehouse(Warehouse warehouse) {
        if (warehouse == null) {
            throw new IllegalArgumentException("Cannot add null warehouse");
        }
        Node node = new Node(warehouse);
        if (!adjacencyList.containsKey(node)) {
            adjacencyList.put(node, new ArrayList<>());
        }
        return node;
    }

    /**
     * Connects two warehouses bi-directionally with a weighted road.
     * @param u Source warehouse Node
     * @param v Destination warehouse Node
     * @param distance Distance in km (edge weight)
     */
    public void connectWarehouses(Node u, Node v, double distance) {
        if (u == null || v == null) {
            throw new IllegalArgumentException("Cannot connect null nodes");
        }
        if (!adjacencyList.containsKey(u) || !adjacencyList.containsKey(v)) {
            throw new IllegalArgumentException("Both warehouses must be added to the graph first");
        }
        adjacencyList.get(u).add(new Edge(u, v, distance));
        adjacencyList.get(v).add(new Edge(v, u, distance));
    }

    /**
     * Gets all nodes currently registered in the graph.
     * @return List of Node vertices
     */
    public List<Node> getNodes() {
        return new ArrayList<>(adjacencyList.keySet());
    }

    /**
     * Finds a Node by warehouse ID or name.
     * @param identifier ID or Name to look up
     * @return Matched Node, or null if not found
     */
    public Node findNode(String identifier) {
        if (identifier == null) return null;
        for (Node node : adjacencyList.keySet()) {
            if (node.getWarehouse().getId().equalsIgnoreCase(identifier) ||
                node.getWarehouse().getName().equalsIgnoreCase(identifier)) {
                return node;
            }
        }
        return null;
    }

    /**
     * Returns adjacency list of edges leaving the source node.
     * @param node Node of interest
     * @return List of Edge associations
     */
    public List<Edge> getEdges(Node node) {
        return adjacencyList.getOrDefault(node, new ArrayList<>());
    }

    /**
     * Outputs graph layout visualization representation.
     */
    public void displayGraph() {
        System.out.println("===== LOGISTICS NETWORK GRAPH =====");
        if (adjacencyList.isEmpty()) {
            System.out.println("Empty network graph.");
            return;
        }
        for (Map.Entry<Node, List<Edge>> entry : adjacencyList.entrySet()) {
            System.out.print(entry.getKey() + " connects to: ");
            List<Edge> edges = entry.getValue();
            if (edges.isEmpty()) {
                System.out.print("[No connections]");
            } else {
                for (int i = 0; i < edges.size(); i++) {
                    Edge edge = edges.get(i);
                    System.out.print(edge.getDestination().getWarehouse().getName() + " (" + edge.getWeight() + "km)");
                    if (i < edges.size() - 1) {
                        System.out.print(", ");
                    }
                }
            }
            System.out.println();
        }
    }
}
