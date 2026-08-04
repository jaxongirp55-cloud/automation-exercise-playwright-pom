package graph;

import java.util.*;

/**
 * Custom implementation of Dijkstra's algorithm to compute shortest path routes.
 * Displays Shortest Route, Visited Nodes, and Total Distance.
 * Handles disconnected graph layouts and invalid routes gracefully.
 *
 * Time Complexity: O((V + E) log V) where V = Vertices, E = Edges.
 * Space Complexity: O(V) to store parent pointers, distances, and state.
 */
public class Dijkstra {

    /**
     * Executes Dijkstra's algorithm from a starting node to a destination node.
     * @param graph The logistics graph
     * @param start The starting Warehouse node
     * @param end The destination Warehouse node
     */
    public static void computeShortestPath(Graph graph, Node start, Node end) {
        if (graph == null || start == null || end == null) {
            System.out.println("Error: Invalid inputs to Dijkstra Shortest Path algorithm.");
            return;
        }

        List<Node> allNodes = graph.getNodes();
        if (!allNodes.contains(start) || !allNodes.contains(end)) {
            System.out.println("Error: Start or destination node does not exist in the graph.");
            return;
        }

        // Keep track of shortest distances
        Map<Node, Double> distances = new HashMap<>();
        // Keep track of previous nodes to reconstruct path
        Map<Node, Node> parentMap = new HashMap<>();
        // Visited set in the order they are finalised
        List<Node> visitedNodes = new ArrayList<>();
        // Min-Priority queue using custom comparator
        PriorityQueue<NodeDistancePair> pq = new PriorityQueue<>(Comparator.comparingDouble(NodeDistancePair::getDistance));

        for (Node node : allNodes) {
            distances.put(node, Double.MAX_VALUE);
        }
        distances.put(start, 0.0);
        pq.add(new NodeDistancePair(start, 0.0));

        while (!pq.isEmpty()) {
            NodeDistancePair currentPair = pq.poll();
            Node u = currentPair.getNode();

            if (visitedNodes.contains(u)) continue;
            visitedNodes.add(u);

            // If we reached the end, we can stop early
            if (u.equals(end)) {
                break;
            }

            for (Edge edge : graph.getEdges(u)) {
                Node v = edge.getDestination();
                if (!visitedNodes.contains(v)) {
                    double weight = edge.getWeight();
                    double newDist = distances.get(u) + weight;
                    if (newDist < distances.get(v)) {
                        distances.put(v, newDist);
                        parentMap.put(v, u);
                        pq.add(new NodeDistancePair(v, newDist));
                    }
                }
            }
        }

        double totalDistance = distances.get(end);
        if (totalDistance == Double.MAX_VALUE) {
            System.out.println("\n===== ROUTE UNAVAILABLE =====");
            System.out.println("No physical route connects: " + start.getWarehouse().getName() + " and " + end.getWarehouse().getName());
            return;
        }

        // Reconstruct shortest path
        List<Node> path = new ArrayList<>();
        Node current = end;
        while (current != null) {
            path.add(0, current);
            current = parentMap.get(current);
        }

        // Display results cleanly
        System.out.println("\n===== SHORTEST ROUTE =====");
        System.out.println("Start       : " + start.getWarehouse().getName());
        System.out.println("Destination : " + end.getWarehouse().getName());
        System.out.printf("Distance    : %.2f km\n", totalDistance);
        System.out.println("\nRoute:");
        for (int i = 0; i < path.size(); i++) {
            System.out.println(path.get(i).getWarehouse().getName());
            if (i < path.size() - 1) {
                System.out.println("  ↓");
            }
        }

        System.out.println("\nVisited Nodes Order:");
        for (int i = 0; i < visitedNodes.size(); i++) {
            System.out.print(visitedNodes.get(i).getWarehouse().getName());
            if (i < visitedNodes.size() - 1) {
                System.out.print(" -> ");
            }
        }
        System.out.println();
    }

    /**
     * Helper pair model for priority queuing.
     */
    private static class NodeDistancePair {
        private final Node node;
        private final double distance;

        public NodeDistancePair(Node node, double distance) {
            this.node = node;
            this.distance = distance;
        }

        public Node getNode() {
            return node;
        }

        public double getDistance() {
            return distance;
        }
    }
}
