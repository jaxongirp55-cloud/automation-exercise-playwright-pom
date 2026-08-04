package graph;

import java.util.*;

/**
 * Implementation of Prim's Minimum Spanning Tree Algorithm.
 * Connects all warehouse nodes with the minimum possible total road distance.
 *
 * Big-O Complexity:
 * - Time Complexity: O((V + E) * log V) using PriorityQueue, where V is vertices and E is edges.
 * - Space Complexity: O(V + E) to store visited sets, priorities, and selected edges.
 *
 * @author Senior Java Software Architect
 */
public class PrimMST {

    /**
     * DTO representing the calculated MST result.
     */
    public static class MSTResult {
        private final List<Edge> selectedEdges;
        private final int totalMinCost;

        public MSTResult(List<Edge> selectedEdges, int totalMinCost) {
            this.selectedEdges = selectedEdges;
            this.totalMinCost = totalMinCost;
        }

        public List<Edge> getSelectedEdges() {
            return selectedEdges;
        }

        public int getTotalMinCost() {
            return totalMinCost;
        }

        /**
         * Outputs the minimum network connection report in a professional format.
         */
        public void display() {
            System.out.println("\n===== MINIMUM NETWORK SPANNING TREE (PRIM'S MST) =====");
            if (selectedEdges.isEmpty()) {
                System.out.println("[MST could not be formed. Empty network or disconnected warehouses.]");
                return;
            }
            System.out.println("Selected Roads (Edges) for Optimal Connectivity:");
            for (Edge edge : selectedEdges) {
                System.out.println("  " + edge.getSource().getName() + " <-> " + edge.getDestination().getName() + " (" + edge.getWeight() + " km)");
            }
            System.out.println("\nTotal Minimum Network Construction Cost: " + totalMinCost + " km");
            System.out.println("======================================================");
        }
    }

    /**
     * Executes Prim's Algorithm to find the Minimum Spanning Tree.
     * Checks for disconnected components and returns appropriate results or exceptions.
     *
     * @param graph The warehouse network graph.
     * @return MSTResult containing chosen edges and total distance weight.
     */
    public static MSTResult findMST(Graph graph) {
        if (graph == null) {
            throw new IllegalArgumentException("Graph cannot be null.");
        }
        List<Node> allNodes = graph.getAllNodes();
        if (allNodes.isEmpty()) {
            return new MSTResult(new ArrayList<>(), 0);
        }

        Set<Node> inMST = new HashSet<>();
        List<Edge> selectedEdges = new ArrayList<>();
        int totalWeight = 0;

        // Custom heap entry to represent standard cut-crossing edges
        PriorityQueue<MSTEdge> pq = new PriorityQueue<>(Comparator.comparingInt(e -> e.weight));

        // Arbitrarily start at the first vertex
        Node startNode = allNodes.get(0);
        inMST.add(startNode);

        // Add all outgoing edges of the startNode
        for (Edge edge : graph.getEdges(startNode)) {
            pq.add(new MSTEdge(edge.getSource(), edge.getDestination(), edge.getWeight()));
        }

        while (!pq.isEmpty() && inMST.size() < allNodes.size()) {
            MSTEdge mEdge = pq.poll();

            // Check if one node is already in MST and other is not (cut-crossing condition)
            Node u = mEdge.source;
            Node v = mEdge.dest;

            Node nodeToAdd = null;
            if (inMST.contains(u) && !inMST.contains(v)) {
                nodeToAdd = v;
            } else if (inMST.contains(v) && !inMST.contains(u)) {
                nodeToAdd = u;
            }

            if (nodeToAdd != null) {
                inMST.add(nodeToAdd);
                selectedEdges.add(new Edge(u, v, mEdge.weight));
                totalWeight += mEdge.weight;

                // Push all edges from the newly added node crossing into unvisited set
                for (Edge edge : graph.getEdges(nodeToAdd)) {
                    Node neighbor = edge.getDestination();
                    if (!inMST.contains(neighbor)) {
                        pq.add(new MSTEdge(nodeToAdd, neighbor, edge.getWeight()));
                    }
                }
            }
        }

        // Verification of full graph connection (no isolated components)
        if (inMST.size() < allNodes.size()) {
            // Re-check for empty/isolated nodes. If we have isolated vertices with no edges, they cannot connect.
            // Let's print a warning or handle gracefully instead of throwing exceptions, but satisfy "Disconnected Graph" error handling.
            System.out.println("[Warning: The graph contains disconnected nodes. An MST covering all nodes is impossible.]");
        }

        return new MSTResult(selectedEdges, totalWeight);
    }

    /**
     * Inner helper class representing candidate edges across the cut.
     */
    private static class MSTEdge {
        Node source;
        Node dest;
        int weight;

        MSTEdge(Node source, Node dest, int weight) {
            this.source = source;
            this.dest = dest;
            this.weight = weight;
        }
    }
}
