package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the logistics network of warehouses and connections.
 * Uses an Adjacency List representation internally via a Map of Nodes to lists of Edges.
 * Provides APIs to add warehouses, connect them, and display the network graph.
 *
 * Big-O Complexity:
 * - Space Complexity: O(V + E) where V is the number of vertices (warehouses) and E is the number of edges (roads).
 * - Time Complexity:
 *   - Add Node (Warehouse): O(1) average time using a HashMap.
 *   - Add Edge (Connect): O(1) average time.
 *   - Display Graph: O(V + E) to iterate over all vertices and their adjacent lists.
 *
 * @author Senior Java Software Architect
 */
public class Graph {
    private final Map<Node, List<Edge>> adjacencyList;

    /**
     * Initializes an empty Logistics Graph with an adjacency list.
     */
    public Graph() {
        this.adjacencyList = new HashMap<>();
    }

    /**
     * Adds a warehouse node to the graph if it does not already exist.
     * @param node The node to add.
     * @return true if added, false if it was already present.
     */
    public boolean addNode(Node node) {
        if (node == null) {
            throw new IllegalArgumentException("Cannot add a null Node to the graph.");
        }
        if (!adjacencyList.containsKey(node)) {
            adjacencyList.put(node, new ArrayList<>());
            return true;
        }
        return false;
    }

    /**
     * Adds a weighted connection (edge) between two warehouses.
     * Since roads are bidirectional in logistics networks, this connects both directions.
     * @param sourceName Name of the source warehouse.
     * @param destName Name of the destination warehouse.
     * @param weight Distance or cost between warehouses.
     */
    public void connect(String sourceName, String destName, int weight) {
        if (sourceName == null || destName == null) {
            throw new IllegalArgumentException("Warehouse names cannot be null for connections.");
        }
        Node source = findOrCreateNode(sourceName);
        Node destination = findOrCreateNode(destName);

        // Avoid adding duplicate edges in the same direction
        List<Edge> sourceEdges = adjacencyList.get(source);
        boolean exists = false;
        for (Edge e : sourceEdges) {
            if (e.getDestination().equals(destination)) {
                e.setWeight(weight); // update distance if edge already exists
                exists = true;
                break;
            }
        }
        if (!exists) {
            sourceEdges.add(new Edge(source, destination, weight));
        }

        // Connect the other way as well for bidirectional road networks
        List<Edge> destEdges = adjacencyList.get(destination);
        boolean destExists = false;
        for (Edge e : destEdges) {
            if (e.getDestination().equals(source)) {
                e.setWeight(weight); // update distance if edge already exists
                destExists = true;
                break;
            }
        }
        if (!destExists) {
            destEdges.add(new Edge(destination, source, weight));
        }
    }

    /**
     * Finds a Node by name or creates it if it does not exist.
     * @param name The name of the warehouse node.
     * @return The Node instance.
     */
    public Node findOrCreateNode(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Warehouse name cannot be empty.");
        }
        Node query = new Node(name);
        for (Node n : adjacencyList.keySet()) {
            if (n.equals(query)) {
                return n;
            }
        }
        // If not found, add it
        addNode(query);
        return query;
    }

    /**
     * Finds a Node by name. Returns null if it doesn't exist.
     * @param name Name of warehouse.
     * @return The node or null.
     */
    public Node getNode(String name) {
        if (name == null) return null;
        Node query = new Node(name);
        for (Node n : adjacencyList.keySet()) {
            if (n.equals(query)) {
                return n;
            }
        }
        return null;
    }

    /**
     * Gets the list of connections (Edges) for a given node.
     * @param node The source node.
     * @return List of edges connected to the node, or an empty list.
     */
    public List<Edge> getEdges(Node node) {
        return adjacencyList.getOrDefault(node, new ArrayList<>());
    }

    /**
     * Gets all nodes present in the graph.
     * @return A list of all unique warehouse nodes.
     */
    public List<Node> getAllNodes() {
        return new ArrayList<>(adjacencyList.keySet());
    }

    /**
     * Gets the raw adjacency list map.
     * @return adjacency list map.
     */
    public Map<Node, List<Edge>> getAdjacencyList() {
        return adjacencyList;
    }

    /**
     * Displays the complete structure of the graph in a professional format.
     */
    public void displayGraph() {
        System.out.println("\n===== WAREHOUSE NETWORK GRAPH (ADJACENCY LIST) =====");
        if (adjacencyList.isEmpty()) {
            System.out.println("[Empty Network - No Warehouses registered.]");
            return;
        }
        for (Map.Entry<Node, List<Edge>> entry : adjacencyList.entrySet()) {
            System.out.print("Warehouse [" + entry.getKey().getName() + "] matches roads to: ");
            List<Edge> edges = entry.getValue();
            if (edges.isEmpty()) {
                System.out.println("No outgoing roads.");
            } else {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < edges.size(); i++) {
                    Edge edge = edges.get(i);
                    sb.append(edge.getDestination().getName()).append(" (").append(edge.getWeight()).append(" km)");
                    if (i < edges.size() - 1) {
                        sb.append(", ");
                    }
                }
                System.out.println(sb.toString());
            }
        }
        System.out.println("====================================================");
    }
}
