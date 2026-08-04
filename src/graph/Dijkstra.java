package graph;

import java.util.*;

/**
 * Implementation of Dijkstra's Single Source Shortest Path Algorithm on the Logistics Graph.
 * Computes shortest paths, distances, route chains, and records the exact traversal of visited nodes.
 *
 * Big-O Complexity:
 * - Time Complexity: O((V + E) * log V) using a PriorityQueue, where V is vertices and E is edges.
 * - Space Complexity: O(V) to store distances, parents, visited lists, and priority queue items.
 *
 * @author Senior Java Software Architect
 */
public class Dijkstra {

    /**
     * Finds the shortest route from a starting node to a destination node.
     * Calculates the path sequence, total distance, and list of visited nodes.
     *
     * @param graph The warehouse network graph.
     * @param startName Name of starting warehouse.
     * @param destName Name of destination warehouse.
     * @return ShortestPathResult containing the route, distance, and visited nodes.
     */
    public static ShortestPathResult findShortestPath(Graph graph, String startName, String destName) {
        if (graph == null) {
            throw new IllegalArgumentException("Graph cannot be null.");
        }
        Node start = graph.getNode(startName);
        Node destination = graph.getNode(destName);

        if (start == null || destination == null) {
            throw new NoSuchElementException("Start or destination warehouse node does not exist in the graph.");
        }

        // Initialize structures
        Map<Node, Integer> distances = new HashMap<>();
        Map<Node, Node> parents = new HashMap<>();
        Set<Node> settled = new HashSet<>();
        List<Node> visitedSequence = new ArrayList<>();

        // Priority Queue elements hold the Node and its currently estimated distance
        PriorityQueue<NodeDistance> pq = new PriorityQueue<>(Comparator.comparingInt(nd -> nd.distance));

        for (Node n : graph.getAllNodes()) {
            distances.put(n, Integer.MAX_VALUE);
        }

        distances.put(start, 0);
        pq.add(new NodeDistance(start, 0));

        while (!pq.isEmpty()) {
            NodeDistance currentDistancePair = pq.poll();
            Node currentNode = currentDistancePair.node;

            if (settled.contains(currentNode)) {
                continue;
            }

            settled.add(currentNode);
            visitedSequence.add(currentNode); // Record order of exploration (visited nodes)

            // If we reached the destination node, we can stop early
            if (currentNode.equals(destination)) {
                break;
            }

            for (Edge edge : graph.getEdges(currentNode)) {
                Node neighbor = edge.getDestination();
                if (!settled.contains(neighbor)) {
                    int edgeWeight = edge.getWeight();
                    int newDistance = distances.get(currentNode) + edgeWeight;

                    if (newDistance < distances.get(neighbor)) {
                        distances.put(neighbor, newDistance);
                        parents.put(neighbor, currentNode);
                        pq.add(new NodeDistance(neighbor, newDistance));
                    }
                }
            }
        }

        // Build route from destination to start using parent links
        List<Node> route = new ArrayList<>();
        int finalDistance = distances.get(destination);

        if (finalDistance != Integer.MAX_VALUE) {
            Node current = destination;
            while (current != null) {
                route.add(0, current); // Prepend to build correct order
                current = parents.get(current);
            }
        }

        return new ShortestPathResult(route, finalDistance, visitedSequence);
    }

    /**
     * Inner helper class representing a pair of a Node and its distance estimation.
     */
    private static class NodeDistance {
        Node node;
        int distance;

        NodeDistance(Node node, int distance) {
            this.node = node;
            this.distance = distance;
        }
    }

    /**
     * DTO containing the result of Dijkstra's algorithm.
     */
    public static class ShortestPathResult {
        private final List<Node> route;
        private final int totalDistance;
        private final List<Node> visitedNodes;

        public ShortestPathResult(List<Node> route, int totalDistance, List<Node> visitedNodes) {
            this.route = route;
            this.totalDistance = totalDistance;
            this.visitedNodes = visitedNodes;
        }

        public List<Node> getRoute() {
            return route;
        }

        public int getTotalDistance() {
            return totalDistance;
        }

        public List<Node> getVisitedNodes() {
            return visitedNodes;
        }

        /**
         * Outputs the route in the requested professional console format.
         */
        public void display() {
            System.out.println("\n===== SHORTEST ROUTE =====");
            if (route.isEmpty() || totalDistance == Integer.MAX_VALUE) {
                System.out.println("No path found between selected warehouses.");
                return;
            }
            System.out.println("Start: " + route.get(0).getName());
            System.out.println("Destination: " + route.get(route.size() - 1).getName());
            System.out.println("Shortest Distance: " + totalDistance + " km");
            System.out.println("\nRoute:");
            for (int i = 0; i < route.size(); i++) {
                System.out.print(route.get(i).getName());
                if (i < route.size() - 1) {
                    System.out.print("\n   ↓\n");
                }
            }
            System.out.println("\n");
            System.out.print("Visited Nodes (Dijkstra Traversal Order): ");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < visitedNodes.size(); i++) {
                sb.append(visitedNodes.get(i).getName());
                if (i < visitedNodes.size() - 1) {
                    sb.append(" -> ");
                }
            }
            System.out.println(sb.toString());
            System.out.println("==========================");
        }
    }
}
