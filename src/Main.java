import graph.*;
import orders.*;
import inventory.*;
import sorting.*;
import trie.*;
import models.*;
import utils.*;

import java.util.ArrayList;
import java.util.List;

/**
 * PrimeLogix Logistics & Management System Main Orchestrator.
 * Designed to satisfy Pearon BTEC HND Unit 26: Data Structures & Algorithms.
 *
 * Bootstraps automatic test datasets including:
 *   - Warehouses in major European Hubs (London, Paris, Berlin, Brussels, Amsterdam).
 *   - Products in a BST.
 *   - Orders in a Priority Queue.
 *   - Sessions in a Custom Chaining Hash Table.
 *   - Sales records to demonstrate Merge Sort.
 *   - Trie structure for autocomplete search.
 */
public class Main {
    private static final Graph logisticsGraph = new Graph();
    private static final PriorityOrderQueue orderQueue = new PriorityOrderQueue();
    private static final TruckQueue truckQueue = new TruckQueue();
    private static final PackageStack cargoStack = new PackageStack();
    private static final BinarySearchTree inventoryBST = new BinarySearchTree();
    private static final HashTableManager sessionTable = new HashTableManager();
    private static final List<MergeSort.SalesRecord> salesRecords = new ArrayList<>();
    private static final Trie autocompleteTrie = new Trie();

    public static void main(String[] args) {
        bootstrapData();

        boolean running = true;
        while (running) {
            ConsoleMenu.displayMainMenu();
            int choice = InputValidator.readInt("Enter menu option [0-13]: ");

            try {
                switch (choice) {
                    case 1:
                        handleGraphManagement();
                        break;
                    case 2:
                        handleShortestRoute();
                        break;
                    case 3:
                        handleMinimumNetwork();
                        break;
                    case 4:
                        handleAddOrder();
                        break;
                    case 5:
                        handleProcessOrders();
                        break;
                    case 6:
                        handleTruckQueue();
                        break;
                    case 7:
                        handlePackageStack();
                        break;
                    case 8:
                        handleProductInventory();
                        break;
                    case 9:
                        handleProductSearch();
                        break;
                    case 10:
                        handleSessionManager();
                        break;
                    case 11:
                        handleSortSales();
                        break;
                    case 12:
                        handleTrieSearch();
                        break;
                    case 13:
                        handleDisplayReports();
                        break;
                    case 0:
                        System.out.println("Thank you for using PrimeLogix Logistics & Management System. Goodbye!");
                        running = false;
                        break;
                    default:
                        System.out.println("Error: Option must be between 0 and 13.");
                }
            } catch (Exception e) {
                System.out.println("An unexpected system exception occurred: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Seeds initial records to immediately showcase each manual structure on startup.
     */
    private static void bootstrapData() {
        System.out.println("Bootstrapping PrimeLogix System Data...");

        // 1. Warehouses (Graph)
        Node london = logisticsGraph.addWarehouse(new Warehouse("W01", "London", "UK"));
        Node paris = logisticsGraph.addWarehouse(new Warehouse("W02", "Paris", "France"));
        Node berlin = logisticsGraph.addWarehouse(new Warehouse("W03", "Berlin", "Germany"));
        Node brussels = logisticsGraph.addWarehouse(new Warehouse("W04", "Brussels", "Belgium"));
        Node amsterdam = logisticsGraph.addWarehouse(new Warehouse("W05", "Amsterdam", "Netherlands"));

        // Connections (Weighted Edges)
        logisticsGraph.connectWarehouses(london, paris, 340.0);
        logisticsGraph.connectWarehouses(london, brussels, 320.0);
        logisticsGraph.connectWarehouses(paris, brussels, 260.0);
        logisticsGraph.connectWarehouses(paris, berlin, 880.0);
        logisticsGraph.connectWarehouses(brussels, amsterdam, 210.0);
        logisticsGraph.connectWarehouses(amsterdam, berlin, 650.0);

        // 2. Orders (Priority Queue)
        orderQueue.enqueue(new Order("ORD901", "Acme Industries", Order.PriorityLevel.STANDARD, "Ground", false));
        orderQueue.enqueue(new Order("ORD902", "TechCorp UK", Order.PriorityLevel.PREMIUM, "Express Air", true));
        orderQueue.enqueue(new Order("ORD903", "Berlin Retailers", Order.PriorityLevel.NEXT_DAY, "Express Ground", false));
        orderQueue.enqueue(new Order("ORD904", "Global Trade Ltd", Order.PriorityLevel.PREMIUM, "Express Air", true));

        // 3. Truck Queue (FIFO)
        truckQueue.enqueue("TRK-707");
        truckQueue.enqueue("TRK-808");
        truckQueue.enqueue("TRK-909");

        // 4. Package Stack (LIFO)
        cargoStack.push("PKG-101");
        cargoStack.push("PKG-102");
        cargoStack.push("PKG-103");

        // 5. Products (BST)
        inventoryBST.insert(new Product("P101", "Enterprise Router", 1200.0, "Networking"));
        inventoryBST.insert(new Product("P102", "Smart Tablet", 450.0, "Electronics"));
        inventoryBST.insert(new Product("P103", "High-End Server", 2500.0, "Networking"));
        inventoryBST.insert(new Product("P104", "Office Printer", 300.0, "Office Supplies"));
        inventoryBST.insert(new Product("P105", "Mechanical Keyboard", 120.0, "Peripherals"));
        inventoryBST.insert(new Product("P106", "Curved Monitor", 550.0, "Peripherals"));

        // 6. User Sessions (Hash Table)
        sessionTable.insert(new UserSession("SESS-001", "alice_mgr", "MANAGER"));
        sessionTable.insert(new UserSession("SESS-002", "bob_operator", "OPERATOR"));
        sessionTable.insert(new UserSession("SESS-003", "clara_admin", "ADMIN"));

        // 7. Sales Records (Merge Sort)
        salesRecords.add(new MergeSort.SalesRecord("TX01", "London Logistics", 15200.0));
        salesRecords.add(new MergeSort.SalesRecord("TX02", "Paris Hub LLC", 8900.0));
        salesRecords.add(new MergeSort.SalesRecord("TX03", "Berlin Distribution", 23400.0));
        salesRecords.add(new MergeSort.SalesRecord("TX04", "Brussels Express", 12100.0));
        salesRecords.add(new MergeSort.SalesRecord("TX05", "Amsterdam Core", 31000.0));

        // 8. Trie Entries (Autocomplete)
        autocompleteTrie.insert("shipping", "Standard postal and freight shipping services");
        autocompleteTrie.insert("shipment", "A batch of cargo packages dispatched under unique IDs");
        autocompleteTrie.insert("shipper", "Logistics vendor dispatching freight");
        autocompleteTrie.insert("warehouse", "Physical warehouse facilities for temporary inventories");
        autocompleteTrie.insert("warfare", "Military operational logistics context");
        autocompleteTrie.insert("workspace", "Digital management session space");

        System.out.println("Bootstrap complete!");
    }

    private static void handleGraphManagement() {
        System.out.println("\n===== GRAPH MANAGEMENT =====");
        System.out.println("1. Add Warehouse");
        System.out.println("2. Connect Warehouses");
        System.out.println("3. Display Logistics Network");
        int sub = InputValidator.readInt("Select Option: ");

        if (sub == 1) {
            String id = InputValidator.readString("Enter Warehouse ID (e.g., W06): ");
            if (logisticsGraph.findNode(id) != null) {
                System.out.println("Error: A warehouse with ID or Name '" + id + "' already exists.");
                return;
            }
            String name = InputValidator.readString("Enter Warehouse Name (e.g., Madrid): ");
            if (logisticsGraph.findNode(name) != null) {
                System.out.println("Error: A warehouse with ID or Name '" + name + "' already exists.");
                return;
            }
            String loc = InputValidator.readString("Enter Location/Country: ");
            logisticsGraph.addWarehouse(new Warehouse(id, name, loc));
            System.out.println("Warehouse '" + name + "' successfully integrated into the network graph!");
        } else if (sub == 2) {
            String uName = InputValidator.readString("Enter Source Warehouse ID/Name: ");
            String vName = InputValidator.readString("Enter Destination Warehouse ID/Name: ");
            Node u = logisticsGraph.findNode(uName);
            Node v = logisticsGraph.findNode(vName);
            if (u == null || v == null) {
                System.out.println("Error: One or both warehouses do not exist in the graph database.");
                return;
            }
            double dist = InputValidator.readDouble("Enter Road Distance (km): ");
            logisticsGraph.connectWarehouses(u, v, dist);
            System.out.println("Warehouses successfully connected with weighted edge!");
        } else if (sub == 3) {
            logisticsGraph.displayGraph();
        } else {
            System.out.println("Invalid Option.");
        }
    }

    private static void handleShortestRoute() {
        System.out.println("\n===== ROUTE PLANNING (DIJKSTRA) =====");
        String startName = InputValidator.readString("Enter Starting Warehouse ID/Name: ");
        String endName = InputValidator.readString("Enter Destination Warehouse ID/Name: ");
        Node start = logisticsGraph.findNode(startName);
        Node end = logisticsGraph.findNode(endName);

        if (start == null || end == null) {
            System.out.println("Error: Starting or ending point not registered in system.");
            return;
        }
        Dijkstra.computeShortestPath(logisticsGraph, start, end);
    }

    private static void handleMinimumNetwork() {
        System.out.println("\n===== OPTIMIZING LOGISTIC BACKBONE ROAD SYSTEM (PRIM'S MST) =====");
        PrimMST.computeMST(logisticsGraph);
    }

    private static void handleAddOrder() {
        System.out.println("\n===== ADD INCOMING CUSTOMER ORDER =====");
        String id = InputValidator.readString("Enter Order ID: ");
        String customer = InputValidator.readString("Enter Customer Name: ");
        System.out.println("Select Priority Level:");
        System.out.println("  1. Premium Priority");
        System.out.println("  2. Next Day Delivery");
        System.out.println("  3. Standard Shipping");
        int prioChoice = InputValidator.readInt("Select Option [1-3]: ");
        Order.PriorityLevel level;
        if (prioChoice == 1) level = Order.PriorityLevel.PREMIUM;
        else if (prioChoice == 2) level = Order.PriorityLevel.NEXT_DAY;
        else level = Order.PriorityLevel.STANDARD;

        String shipType = InputValidator.readString("Enter Shipping Logistics Mode (e.g., Air Freight, Ground Cargo): ");
        boolean vip = InputValidator.readBoolean("Does the customer hold a Platinum/VIP Subscription?");

        Order order = new Order(id, customer, level, shipType, vip);
        orderQueue.enqueue(order);
        System.out.println("Order successfully scheduled into Priority Max-Heap Queue!");
    }

    private static void handleProcessOrders() {
        System.out.println("\n===== DISPATCH PROCESSOR (PRIORITY QUEUE) =====");
        System.out.println("1. Peek Next Urgent Order");
        System.out.println("2. Process & Dispatch Highest Priority Order");
        System.out.println("3. Display All Active Orders Queue");
        int choice = InputValidator.readInt("Select Queue Operation: ");

        if (choice == 1) {
            Order next = orderQueue.peek();
            if (next == null) {
                System.out.println("Notification: Priority Queue is completely empty. No pending orders!");
            } else {
                System.out.println("Highest Priority Order in Queue: " + next);
            }
        } else if (choice == 2) {
            Order processed = orderQueue.dequeue();
            if (processed == null) {
                System.out.println("Notification: Empty queue. No orders left to process!");
            } else {
                System.out.println("Dispatched Order: " + processed);
                // Automatically generate a tracking code and push onto Truck stack as loading action
                String pkgId = "PKG-" + processed.getOrderId();
                cargoStack.push(pkgId);
                System.out.println("System Action: Generated and loaded package tracking ID '" + pkgId + "' to delivery truck stack.");
            }
        } else if (choice == 3) {
            orderQueue.displayQueue();
        } else {
            System.out.println("Invalid option.");
        }
    }

    private static void handleTruckQueue() {
        System.out.println("\n===== TERMINAL BAY TRUCK LINE (FIFO QUEUE) =====");
        System.out.println("1. Queue New Delivery Truck (Enqueue)");
        System.out.println("2. Dispatch Front Delivery Truck (Dequeue)");
        System.out.println("3. Peek Next Truck to Load");
        System.out.println("4. Display All Queued Trucks");
        int choice = InputValidator.readInt("Select FIFO Operation: ");

        if (choice == 1) {
            String trkId = InputValidator.readString("Enter Truck ID (e.g., TRK-999): ");
            truckQueue.enqueue(trkId);
            System.out.println("Truck " + trkId + " joined terminal queue line.");
        } else if (choice == 2) {
            String dispatched = truckQueue.dequeue();
            if (dispatched == null) {
                System.out.println("Error: Truck line is completely empty!");
            } else {
                System.out.println("Success: Dispatched Truck [" + dispatched + "] from the terminal bay.");
            }
        } else if (choice == 3) {
            String next = truckQueue.peek();
            if (next == null) {
                System.out.println("No trucks waiting.");
            } else {
                System.out.println("Next Truck in FIFO queue: " + next);
            }
        } else if (choice == 4) {
            truckQueue.displayQueue();
        } else {
            System.out.println("Invalid option.");
        }
    }

    private static void handlePackageStack() {
        System.out.println("\n===== CARGO LOADING TERMINAL (LIFO STACK) =====");
        System.out.println("1. Load Package to Stack (Push)");
        System.out.println("2. Unload Top Package from Stack (Pop)");
        System.out.println("3. Peek Top Loaded Package");
        System.out.println("4. Display Loaded Cargo Container");
        int choice = InputValidator.readInt("Select LIFO Operation: ");

        if (choice == 1) {
            String pkgId = InputValidator.readString("Enter Package Tracking ID: ");
            cargoStack.push(pkgId);
            System.out.println("Success: Loaded package [" + pkgId + "] into the cargo bay stack.");
        } else if (choice == 2) {
            String unloaded = cargoStack.pop();
            if (unloaded == null) {
                System.out.println("Error: Cargo stack container is empty!");
            } else {
                System.out.println("Success: Unloaded top package [" + unloaded + "] from container.");
            }
        } else if (choice == 3) {
            String top = cargoStack.peek();
            if (top == null) {
                System.out.println("Cargo container is empty.");
            } else {
                System.out.println("Top Package in container stack: " + top);
            }
        } else if (choice == 4) {
            cargoStack.displayStack();
        } else {
            System.out.println("Invalid option.");
        }
    }

    private static void handleProductInventory() {
        System.out.println("\n===== PRODUCT CATALOG (BINARY SEARCH TREE) =====");
        System.out.println("1. Insert New Product");
        System.out.println("2. Delete Product by Price");
        System.out.println("3. Search Product by Exact Price");
        System.out.println("4. Print In-Order Traversal (Sorted)");
        System.out.println("5. Print Pre-Order Traversal");
        System.out.println("6. Print Post-Order Traversal");
        int choice = InputValidator.readInt("Select BST Operation: ");

        if (choice == 1) {
            String id = InputValidator.readString("Enter Product ID: ");
            String name = InputValidator.readString("Enter Product Name: ");
            double price = InputValidator.readDouble("Enter Product Unit Price ($): ");
            String category = InputValidator.readString("Enter Department Category: ");
            inventoryBST.insert(new Product(id, name, price, category));
            System.out.println("Product catalog successfully updated and indexed by price!");
        } else if (choice == 2) {
            double price = InputValidator.readDouble("Enter Target Price to Delete ($): ");
            Product exist = inventoryBST.search(price);
            if (exist == null) {
                System.out.println("Error: No product registered at price $" + price);
            } else {
                inventoryBST.delete(price);
                System.out.println("Success: Deleted product: " + exist);
            }
        } else if (choice == 3) {
            double price = InputValidator.readDouble("Enter Price query ($): ");
            Product found = inventoryBST.search(price);
            if (found == null) {
                System.out.println("No matching product found with price $" + price);
            } else {
                System.out.println("Product Matched: " + found);
            }
        } else if (choice == 4) {
            System.out.println("=== In-Order (Sorted Price Index) ===");
            inventoryBST.orderBookDisplay(1);
        } else if (choice == 5) {
            System.out.println("=== Pre-Order Traversal ===");
            inventoryBST.orderBookDisplay(2);
        } else if (choice == 6) {
            System.out.println("=== Post-Order Traversal ===");
            inventoryBST.orderBookDisplay(3);
        } else {
            System.out.println("Invalid option.");
        }
    }

    private static void handleProductSearch() {
        System.out.println("\n===== RANGE QUERY AND FILTERING =====");
        double min = InputValidator.readDouble("Enter Minimum Target Price ($): ");
        double max = InputValidator.readDouble("Enter Maximum Target Price ($): ");

        if (min > max) {
            System.out.println("Error: Minimum range cannot exceed maximum range.");
            return;
        }

        List<Product> results = inventoryBST.rangeSearch(min, max);
        System.out.println("\n===== PRODUCTS WITHIN PRICE RANGE [$" + min + " - $" + max + "] =====");
        if (results.isEmpty()) {
            System.out.println("No products found within range.");
        } else {
            for (Product p : results) {
                System.out.println("  " + p);
            }
        }
    }

    private static void handleSessionManager() {
        System.out.println("\n===== ACTIVE USER SESSIONS (CHAINING HASH TABLE) =====");
        System.out.println("1. Create/Insert User Session");
        System.out.println("2. Terminate/Delete Session");
        System.out.println("3. Search Session metadata");
        System.out.println("4. Display Chaining Hash Table Buckets");
        int choice = InputValidator.readInt("Select Hash Table Operation: ");

        if (choice == 1) {
            String sid = InputValidator.readString("Enter Session ID (e.g., SESS-999): ");
            String user = InputValidator.readString("Enter Username: ");
            String role = InputValidator.readString("Enter Security Access Role (e.g. ADMIN, OPERATOR): ");
            sessionTable.insert(new UserSession(sid, user, role));
            System.out.println("Session registered successfully in custom chaining hash map.");
        } else if (choice == 2) {
            String sid = InputValidator.readString("Enter Session ID to Terminate: ");
            boolean deleted = sessionTable.delete(sid);
            if (deleted) {
                System.out.println("Session terminated successfully.");
            } else {
                System.out.println("Error: Session ID not found.");
            }
        } else if (choice == 3) {
            String sid = InputValidator.readString("Enter Session ID query: ");
            UserSession session = sessionTable.search(sid);
            if (session == null) {
                System.out.println("Session not found.");
            } else {
                System.out.println("Session Records: " + session);
            }
        } else if (choice == 4) {
            sessionTable.displayHashTable();
        } else {
            System.out.println("Invalid Option.");
        }
    }

    private static void handleSortSales() {
        System.out.println("\n===== SORT HISTORICAL REGIONAL SALES (MANUAL MERGE SORT) =====");
        System.out.println("Before Sorting (Original Transaction Sequence):");
        for (MergeSort.SalesRecord sr : salesRecords) {
            System.out.println("  " + sr);
        }

        // Execute manual merge sort algorithm
        MergeSort.sort(salesRecords);

        System.out.println("\nAfter Sorting (Descending Sale Value):");
        for (MergeSort.SalesRecord sr : salesRecords) {
            System.out.println("  " + sr);
        }
    }

    private static void handleTrieSearch() {
        System.out.println("\n===== INSTANT TRIE AUTOCOMPLETE =====");
        System.out.println("1. Add Keyword dictionary key");
        System.out.println("2. Delete Keyword key");
        System.out.println("3. Prefix Search Autocomplete");
        int choice = InputValidator.readInt("Select Trie Operation: ");

        if (choice == 1) {
            String word = InputValidator.readString("Enter keyword (e.g., shipment): ");
            String desc = InputValidator.readString("Enter description: ");
            autocompleteTrie.insert(word, desc);
            System.out.println("Key inserted.");
        } else if (choice == 2) {
            String word = InputValidator.readString("Enter keyword to delete: ");
            boolean deleted = autocompleteTrie.delete(word);
            if (deleted) {
                System.out.println("Successfully removed from vocabulary.");
            } else {
                System.out.println("Word not found.");
            }
        } else if (choice == 3) {
            String prefix = InputValidator.readString("Enter partial keyword prefix: ");
            List<String> results = autocompleteTrie.autocomplete(prefix);
            System.out.println("\n===== MATCHING DICTIONARY ENTRIES =====");
            if (results.isEmpty()) {
                System.out.println("[No matching dictionary entries for '" + prefix + "']");
            } else {
                for (String r : results) {
                    System.out.println("  " + r);
                }
            }
        } else {
            System.out.println("Invalid option.");
        }
    }

    private static void handleDisplayReports() {
        System.out.println("\n========================================================");
        System.out.println("          PRIMELOGIX LOGISTIC SYSTEM REPORT SUMMARY     ");
        System.out.println("========================================================");
        System.out.println("Active Logistics Hubs: " + logisticsGraph.getNodes().size());
        System.out.println("Pending Priority Orders: " + orderQueue.size());
        System.out.println("Queued Terminal Trucks: " + truckQueue.size());
        System.out.println("Stacked Cargo Packages: " + cargoStack.size());
        System.out.println("Active User Sessions: " + sessionTable.getSize());
        System.out.println("========================================================");
    }
}
