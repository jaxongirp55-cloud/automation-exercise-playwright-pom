package graph;

import java.util.*;

/**
 * Custom implementation of Prim's Minimum Spanning Tree (MST) Algorithm.
 * Solves minimum-cost setup problems for full interconnected logistical hubs.
 * Displays Selected Edges and Minimum Total cost.
 *
 * Time Complexity: O((V + E) log V) where V = Vertices, E = Edges.
 * Space Complexity: O(V + E) to manage PQ weights and spanning structures.
 */
public class PrimMST {

    /**
     * Executes Prim's Algorithm to generate and print MST of the logi-network.
     * @param graph Full logistics network graph.
     */
    public static void computeMST(Graph graph) {
        if (graph == null || graph.getNodes().isEmpty()) {
            System.out.println("Error: Graph is empty. Cannot compute MST.");
            return;
        }

        List<Node> allNodes = graph.getNodes();
        Set<Node> visited = new HashSet<>();
        List<Edge> mstEdges = new ArrayList<>();
        double minTotalCost = 0.0;

        // Min-Priority queue prioritizing lighter-weight graph edges
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingDouble(Edge::getWeight));

        // Start spanning from the first available node
        Node startNode = allNodes.get(0);
        visited.add(startNode);

        // Add all outgoing edges from the first node
        pq.addAll(graph.getEdges(startNode));

        while (!pq.isEmpty() && visited.size() < allNodes.size()) {
            Edge lightestEdge = pq.poll();
            Node destination = lightestEdge.getDestination();

            // If the destination node is already spanned, avoid cycle
            if (visited.contains(destination)) {
                continue;
            }

            visited.add(destination);
            mstEdges.add(lightestEdge);
            minTotalCost += lightestEdge.getWeight();

            // Add all outgoing edges from the newly spanned vertex
            for (Edge edge : graph.getEdges(destination)) {
                if (!visited.contains(edge.getDestination())) {
                    pq.add(edge);
                }
            }
        }

        System.out.println("\n===== MINIMUM NETWORK (PRIM'S MST) =====");
        if (visited.size() < allNodes.size()) {
            System.out.println("Warning: The graph is disconnected! Showing MST of the reachable partition.");
        }

        System.out.println("Selected Edges (MST Roads):");
        for (Edge edge : mstEdges) {
            System.out.printf("  %s <-> %s (%.2f km)\n",
                    edge.getSource().getWarehouse().getName(),
                    edge.getDestination().getWarehouse().getName(),
                    edge.getWeight());
        }
        System.out.printf("\nMinimum Total Interconnect Cost: %.2f km\n", minTotalCost);
    }
}
