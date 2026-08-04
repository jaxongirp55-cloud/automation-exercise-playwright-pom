import graph.Dijkstra;
import graph.Graph;
import graph.Node;
import graph.PrimMST;
import inventory.BinarySearchTree;
import inventory.HashTableManager;
import inventory.Product;
import models.UserSession;
import models.Warehouse;
import orders.Order;
import orders.PackageStack;
import orders.PriorityOrderQueue;
import orders.TruckQueue;
import sorting.MergeSort;
import trie.Trie;
import utils.ConsoleMenu;
import utils.InputValidator;

import java.util.*;

/**
 * PrimeLogix Logistics & Management System Main Application.
 * Bootstraps the platform, populates mock demonstration data automatically, and
 * launches the interactive professional console loop.
 *
 * Satisfies all requirements of Pearson BTEC HND Unit 26: Data Structures & Algorithms.
 *
 * Big-O Summary of the entire Logistics System:
 * - Graph Operations: Adjacency list storage O(V + E). Dijkstra O((V+E) log V). Prim MST O((V+E) log V).
 * - Priority Queue: Binary Heap insertion/extraction O(log N).
 * - FIFO Truck Queue: Circular dynamic array O(1).
 * - LIFO Package Stack: Dynamic array O(1).
 * - Product BST: Balanced BST average insertion/deletion/searching O(log N).
 * - User Sessions HashTable: Chaining lookup O(1) average.
 * - Merge Sort: Divide and conquer stable sorting O(N log N).
 * - Autocomplete Trie: Prefix traversal and sub-trie recursive search O(L + S).
 *
 * @author Senior Java Software Architect
 */
public class Main {

    // Global in-memory data structures
    private static final Graph logisticsGraph = new Graph();
    private static final PriorityOrderQueue orderPriorityQueue = new PriorityOrderQueue();
    private static final TruckQueue truckFIFOQueue = new TruckQueue();
    private static final PackageStack packageLIFOStack = new PackageStack();
    private static final BinarySearchTree productBST = new BinarySearchTree();
    private static final HashTableManager<String, UserSession> sessionTable = new HashTableManager<>();
    private static final Trie trieProductSearch = new Trie();
    private static final List<MergeSort.SaleTransaction> dailyTransactions = new ArrayList<>();

    public static void main(String[] args) {
        // Step 1: Pre-populate robust demonstration data automatically
        initializeDemoData();

        // Step 2: Main console menu loop
        boolean running = true;
        while (running) {
            try {
                ConsoleMenu.printMainMenu();
                int choice = InputValidator.readIntInRange("Select operation (0-13): ", 0, 13);

                switch (choice) {
                    case 1:
                        manageGraph();
                        break;
                    case 2:
                        calculateShortestRoute();
                        break;
                    case 3:
                        calculateMinimumNetwork();
                        break;
                    case 4:
                        addNewOrder();
                        break;
                    case 5:
                        processOrders();
                        break;
                    case 6:
                        manageTruckQueue();
                        break;
                    case 7:
                        managePackageStack();
                        break;
                    case 8:
                        manageProductInventory();
                        break;
                    case 9:
                        searchAndFilterProducts();
                        break;
                    case 10:
                        manageSessionTable();
                        break;
                    case 11:
                        sortDailySales();
                        break;
                    case 12:
                        autocompleteTrieSearch();
                        break;
                    case 13:
                        displayConsolidatedReports();
                        break;
                    case 0:
                        running = false;
                        System.out.println("\nShutting down PrimeLogix Logistics & Management System. Goodbye!");
                        break;
                    default:
                        System.out.println("Error: Unrecognized menu selection.");
                }
            } catch (Exception e) {
                System.out.println("\n[ERROR EXCEPTION TRIGGERED]: " + e.getMessage());
                System.out.println("Returning to main menu...");
            }
        }
    }

    /**
     * Instantiates robust initial sample values for seamless test displays.
     */
    private static void initializeDemoData() {
        System.out.println(">> Initializing PrimeLogix Logistics System Data...");

        // 1. Populate Graph (Warehouses & Roads)
        logisticsGraph.connect("London", "Paris", 340);
        logisticsGraph.connect("Paris", "Berlin", 1050);
        logisticsGraph.connect("London", "Amsterdam", 540);
        logisticsGraph.connect("Amsterdam", "Berlin", 650);
        logisticsGraph.connect("Berlin", "Munich", 580);
        logisticsGraph.connect("Paris", "Munich", 840);
        logisticsGraph.connect("Munich", "Rome", 920);

        // 2. Populate Order Queue (Priority Queue)
        orderPriorityQueue.enqueue(new Order("ORD101", "Alice Johnson", Order.PriorityLevel.STANDARD, "Standard Ground", false));
        orderPriorityQueue.enqueue(new Order("ORD102", "Robert Smith", Order.PriorityLevel.PREMIUM, "Express Delivery", true));
        orderPriorityQueue.enqueue(new Order("ORD103", "Emma Davis", Order.PriorityLevel.NEXT_DAY, "Next-Day Air", false));
        orderPriorityQueue.enqueue(new Order("ORD104", "David Wilson", Order.PriorityLevel.PREMIUM, "Same-Day Courier", false));

        // 3. Populate Truck Queue (FIFO)
        truckFIFOQueue.enqueue("TRK-FL100");
        truckFIFOQueue.enqueue("TRK-FL101");
        truckFIFOQueue.enqueue("TRK-FL102");

        // 4. Populate Package Stack (LIFO)
        packageLIFOStack.push("PKG-901-A");
        packageLIFOStack.push("PKG-902-B");
        packageLIFOStack.push("PKG-903-C");

        // 5. Populate Product Inventory (BST & Trie)
        addProductToInventory(new Product("PROD01", "iPhone 15 Pro", 1199.99, "Electronics"));
        addProductToInventory(new Product("PROD02", "Sony Headphones WH-1000XM5", 349.99, "Electronics"));
        addProductToInventory(new Product("PROD03", "Ergonomic Office Chair", 499.00, "Furniture"));
        addProductToInventory(new Product("PROD04", "Mechanical Gaming Keyboard", 150.00, "Electronics"));
        addProductToInventory(new Product("PROD05", "MacBook Pro M3 Max", 2499.99, "Electronics"));
        addProductToInventory(new Product("PROD06", "Standing Desk Dual Motor", 720.00, "Furniture"));

        // 6. Populate Sessions (HashTableManager)
        sessionTable.put("SESS-001", new UserSession("SESS-001", "manager_luke", "Logistics Administrator"));
        sessionTable.put("SESS-002", new UserSession("SESS-002", "clerk_sarah", "Warehouse Operations Clerk"));
        sessionTable.put("SESS-003", new UserSession("SESS-003", "admin_jack", "System Superuser"));

        // 7. Populate Daily Sales Data (For MergeSort demo)
        dailyTransactions.add(new MergeSort.SaleTransaction("2026-02-01", 12500.50));
        dailyTransactions.add(new MergeSort.SaleTransaction("2026-02-02", 8400.20));
        dailyTransactions.add(new MergeSort.SaleTransaction("2026-02-03", 24500.10));
        dailyTransactions.add(new MergeSort.SaleTransaction("2026-02-04", 19200.75));
        dailyTransactions.add(new MergeSort.SaleTransaction("2026-02-05", 15000.00));

        System.out.println(">> Demo datasets prepared successfully! Total records:");
        System.out.printf("   - Warehouses (Vertices): %d\n", logisticsGraph.getAllNodes().size());
        System.out.printf("   - Pending Priority Orders: %d\n", orderPriorityQueue.size());
        System.out.printf("   - Dispatch Queue Trucks: %d\n", truckFIFOQueue.size());
        System.out.printf("   - Package Stack: %d\n", packageLIFOStack.size());
        System.out.printf("   - Product Inventory Tree: %d\n", sessionTable.size());
        System.out.println("---------------------------------------------------------");
    }

    private static void addProductToInventory(Product p) {
        productBST.insert(p);
        trieProductSearch.insert(p.getName());
    }

    // ==========================================
    // 1. GRAPH MANAGEMENT
    // ==========================================
    private static void manageGraph() {
        ConsoleMenu.printHeader("Graph Management");
        System.out.println("1. View Current Warehouse Network");
        System.out.println("2. Add New Warehouse Vertex");
        System.out.println("3. Connect Warehouses with Weighted Road Edge");
        int subChoice = InputValidator.readIntInRange("Choose sub-option: ", 1, 3);

        if (subChoice == 1) {
            logisticsGraph.displayGraph();
        } else if (subChoice == 2) {
            String name = InputValidator.readString("Enter name of new Warehouse: ");
            boolean added = logisticsGraph.addNode(new Node(name));
            if (added) {
                System.out.println("Success: Warehouse node [" + name + "] added.");
            } else {
                System.out.println("Error: A warehouse with that name already exists.");
            }
        } else {
            String src = InputValidator.readString("Enter Source Warehouse: ");
            String dest = InputValidator.readString("Enter Destination Warehouse: ");
            int weight = InputValidator.readIntInRange("Enter Road Connection Distance (km): ", 1, 10000);

            logisticsGraph.connect(src, dest, weight);
            System.out.println("Success: Bidirectional road link added: " + src + " <--> " + dest + " (" + weight + " km)");
        }
    }

    // ==========================================
    // 2. DIJKSTRA SHORTEST PATH
    // ==========================================
    private static void calculateShortestRoute() {
        ConsoleMenu.printHeader("Shortest Route Calculation (Dijkstra)");
        String start = InputValidator.readString("Enter Departure Warehouse Name: ");
        String destination = InputValidator.readString("Enter Destination Warehouse Name: ");

        try {
            Dijkstra.ShortestPathResult result = Dijkstra.findShortestPath(logisticsGraph, start, destination);
            result.display();
        } catch (NoSuchElementException e) {
            System.out.println("[Graph Error]: " + e.getMessage());
        }
    }

    // ==========================================
    // 3. PRIM'S MINIMUM SPANNING TREE (MST)
    // ==========================================
    private static void calculateMinimumNetwork() {
        ConsoleMenu.printHeader("Minimum Network Optimization (Prim's MST)");
        System.out.println("Evaluating optimal cost-saving backbone for road connections...");
        PrimMST.MSTResult result = PrimMST.findMST(logisticsGraph);
        result.display();
    }

    // ==========================================
    // 4. ADD CUSTOMER ORDER
    // ==========================================
    private static void addNewOrder() {
        ConsoleMenu.printHeader("Add Customer Order");
        String id = InputValidator.readString("Enter unique Order ID: ");
        if (orderPriorityQueue.containsOrder(id)) {
            System.out.println("Error: An order with ID " + id + " is already in the priority queue.");
            return;
        }
        String client = InputValidator.readString("Enter Customer Name: ");

        System.out.println("Select Priority Rank:");
        System.out.println("  1. Premium (Highest)");
        System.out.println("  2. Next Day (Medium)");
        System.out.println("  3. Standard (Regular)");
        int level = InputValidator.readIntInRange("Select Level (1-3): ", 1, 3);
        Order.PriorityLevel priority = Order.PriorityLevel.STANDARD;
        if (level == 1) priority = Order.PriorityLevel.PREMIUM;
        else if (level == 2) priority = Order.PriorityLevel.NEXT_DAY;

        String shipping = InputValidator.readString("Enter Shipping Transport Type (e.g. Flight Air, Freight Ground): ");
        boolean isVip = InputValidator.readBoolean("Is the client a VIP Prime Member?");

        Order order = new Order(id, client, priority, shipping, isVip);
        orderPriorityQueue.enqueue(order);
        System.out.println("\n[SUCCESS]: New customer order successfully logged in heap priority storage.");
        System.out.println(order);
    }

    // ==========================================
    // 5. PROCESS ORDERS
    // ==========================================
    private static void processOrders() {
        ConsoleMenu.printHeader("Process Order Priority Queue");
        orderPriorityQueue.displayQueue();

        if (orderPriorityQueue.isEmpty()) {
            return;
        }

        boolean proceed = InputValidator.readBoolean("Dispatch highest priority order now?");
        if (proceed) {
            Order processed = orderPriorityQueue.dequeue();
            System.out.println("\n===== ORDER DISPATCH REPORT =====");
            System.out.println("Now Dispatching:");
            System.out.println("  ID:           " + processed.getOrderId());
            System.out.println("  Client Name:  " + processed.getCustomerName());
            System.out.println("  Priority:     " + processed.getPriority());
            System.out.println("  VIP Subscriber: " + processed.isSubscribed());
            System.out.println("  Shipping Channel: " + processed.getShippingType());
            System.out.println("=================================");

            // Link to loading stack
            boolean loadPackage = InputValidator.readBoolean("Do you want to stage a matching package on the Loading Stack?");
            if (loadPackage) {
                String pkgId = "PKG-" + processed.getOrderId().substring(Math.max(0, processed.getOrderId().length() - 4)) + "-STK";
                packageLIFOStack.push(pkgId);
                System.out.println("Placed package [" + pkgId + "] onto truck load stack.");
            }
        }
    }

    // ==========================================
    // 6. TRUCK QUEUE (FIFO)
    // ==========================================
    private static void manageTruckQueue() {
        ConsoleMenu.printHeader("Truck Dispatch Manager (FIFO Queue)");
        truckFIFOQueue.displayQueue();

        System.out.println("\nOperations:");
        System.out.println("1. Register (Enqueue) New Truck");
        System.out.println("2. Dispatch (Dequeue) Leading Truck");
        System.out.println("3. Inspect Front Truck (Peek)");
        int sub = InputValidator.readIntInRange("Select action: ", 1, 3);

        if (sub == 1) {
            String tId = InputValidator.readString("Enter Fleet Truck Registration ID: ");
            truckFIFOQueue.enqueue(tId);
            System.out.println("Success: Truck [" + tId + "] registered at the back of queue.");
        } else if (sub == 2) {
            if (truckFIFOQueue.isEmpty()) {
                System.out.println("Error: Empty queue! No trucks available for dispatch.");
            } else {
                String disp = truckFIFOQueue.dequeue();
                System.out.println("\n[DISPATCH ALERT]: Truck [" + disp + "] departed warehouse docks.");
            }
        } else {
            if (truckFIFOQueue.isEmpty()) {
                System.out.println("Error: Empty queue!");
            } else {
                System.out.println("Truck ready at dock: " + truckFIFOQueue.peek());
            }
        }
    }

    // ==========================================
    // 7. PACKAGE LOADING STACK (LIFO)
    // ==========================================
    private static void managePackageStack() {
        ConsoleMenu.printHeader("Package Loading Stack (LIFO)");
        packageLIFOStack.displayStack();

        System.out.println("\nOperations:");
        System.out.println("1. Load Package (Push)");
        System.out.println("2. Discharge Package (Pop)");
        System.out.println("3. Inspect Top Package (Peek)");
        int sub = InputValidator.readIntInRange("Select action: ", 1, 3);

        if (sub == 1) {
            String pkgId = InputValidator.readString("Enter Package Barcode/ID: ");
            packageLIFOStack.push(pkgId);
            System.out.println("Success: Package [" + pkgId + "] stacked onto loading deck.");
        } else if (sub == 2) {
            if (packageLIFOStack.isEmpty()) {
                System.out.println("Error: Stack empty! No packages loaded.");
            } else {
                String discharged = packageLIFOStack.pop();
                System.out.println("Discharged package from stack: " + discharged);
            }
        } else {
            if (packageLIFOStack.isEmpty()) {
                System.out.println("Error: Stack empty!");
            } else {
                System.out.println("Top loaded package: " + packageLIFOStack.peek());
            }
        }
    }

    // ==========================================
    // 8. PRODUCT INVENTORY MANAGEMENT (BST)
    // ==========================================
    private static void manageProductInventory() {
        ConsoleMenu.printHeader("Product Inventory Tree (BST)");
        System.out.println("1. Display Inventory In-Order (Sorted by Product ID)");
        System.out.println("2. Display Inventory Pre-Order");
        System.out.println("3. Display Inventory Post-Order");
        System.out.println("4. Register New Product in System");
        System.out.println("5. Remove Product from Catalog");
        int sub = InputValidator.readIntInRange("Select action: ", 1, 5);

        switch (sub) {
            case 1:
                productBST.displayInOrder();
                break;
            case 2:
                productBST.displayPreOrder();
                break;
            case 3:
                productBST.displayPostOrder();
                break;
            case 4:
                String id = InputValidator.readString("Enter new unique Product ID: ");
                if (productBST.search(id) != null) {
                    System.out.println("Error: Duplicate Product ID registered.");
                    return;
                }
                String name = InputValidator.readString("Enter Product Label Name: ");
                double price = InputValidator.readDouble("Enter Product Retail Unit Price ($): ");
                String category = InputValidator.readString("Enter Product Segment Category: ");

                Product p = new Product(id, name, price, category);
                addProductToInventory(p);
                System.out.println("Success: Product registered in in-memory catalog databases & prefix trees.");
                break;
            case 5:
                String delId = InputValidator.readString("Enter product ID to remove: ");
                try {
                    Product toDel = productBST.search(delId);
                    if (toDel != null) {
                        productBST.delete(delId);
                        trieProductSearch.delete(toDel.getName());
                        System.out.println("Success: Product [" + delId + "] successfully deleted.");
                    } else {
                        System.out.println("Error: Product not found.");
                    }
                } catch (Exception e) {
                    System.out.println("Deletion error: " + e.getMessage());
                }
                break;
        }
    }

    // ==========================================
    // 9. RANGE SEARCH AND PRODUCT RETRIEVAL
    // ==========================================
    private static void searchAndFilterProducts() {
        ConsoleMenu.printHeader("Product Search & Range Filter");
        System.out.println("1. Lookup exact Product by ID");
        System.out.println("2. Search Product by Price Range Filter");
        int sub = InputValidator.readIntInRange("Select: ", 1, 2);

        if (sub == 1) {
            String pId = InputValidator.readString("Enter Product ID to query: ");
            Product p = productBST.search(pId);
            if (p != null) {
                System.out.println("\n===== MATCH FOUND =====");
                System.out.println(p);
                System.out.println("=======================");
            } else {
                System.out.println("Error: No matching product registered under the ID: " + pId);
            }
        } else {
            double min = InputValidator.readDouble("Enter Minimum Catalog Price Boundary ($): ");
            double max = InputValidator.readDouble("Enter Maximum Catalog Price Boundary ($): ");

            List<Product> matches = productBST.rangeSearch(min, max);
            System.out.printf("\n===== INVENTORY PRICE SEARCH RESULTS ($%.2f to $%.2f) =====\n", min, max);
            if (matches.isEmpty()) {
                System.out.println("[No matching items registered in selected bracket.]");
            } else {
                for (Product match : matches) {
                    System.out.println("  " + match);
                }
            }
            System.out.println("==========================================================");
        }
    }

    // ==========================================
    // 10. SESSION MANAGER (HASH TABLE MANAGER)
    // ==========================================
    private static void manageSessionTable() {
        ConsoleMenu.printHeader("Session Manager (HashTableManager)");
        sessionTable.displayTable();

        System.out.println("\nSub-Operations:");
        System.out.println("1. Authenticate New Session (Put)");
        System.out.println("2. Invalidate Session ID (Remove)");
        System.out.println("3. Query Session Verification (Get)");
        int sub = InputValidator.readIntInRange("Select action: ", 1, 3);

        if (sub == 1) {
            String sId = InputValidator.readString("Enter Session ID Code (e.g. SESS-XYZ): ");
            if (sessionTable.containsKey(sId)) {
                System.out.println("Error: Session ID code is already assigned.");
                return;
            }
            String user = InputValidator.readString("Enter Employee Username: ");
            String role = InputValidator.readString("Enter Authorized Corporate Role: ");
            UserSession session = new UserSession(sId, user, role);
            sessionTable.put(sId, session);
            System.out.println("Success: UserSession verified and loaded into active Hash Tables.");
        } else if (sub == 2) {
            String sId = InputValidator.readString("Enter Session ID to invalidate: ");
            UserSession rm = sessionTable.remove(sId);
            if (rm != null) {
                System.out.println("Success: Logged off session: " + rm);
            } else {
                System.out.println("Error: Session ID target was not found in registered active states.");
            }
        } else {
            String sId = InputValidator.readString("Enter Session ID: ");
            UserSession search = sessionTable.get(sId);
            if (search != null) {
                System.out.println("\n===== SESSION VERIFIED =====");
                System.out.println(search);
                System.out.println("============================");
            } else {
                System.out.println("Error: No active sessions registered with token ID: " + sId);
            }
        }
    }

    // ==========================================
    // 11. SORT DAILY SALES DATA (MERGE SORT)
    // ==========================================
    private static void sortDailySales() {
        ConsoleMenu.printHeader("Sort Daily Sales (Merge Sort)");

        MergeSort.SaleTransaction[] arr = dailyTransactions.toArray(new MergeSort.SaleTransaction[0]);

        System.out.println("Daily Transactions List BEFORE Merge Sort (Random/Insertion order):");
        for (MergeSort.SaleTransaction st : arr) {
            System.out.println("  " + st);
        }

        // Apply Custom Merge Sort
        MergeSort.sort(arr);

        System.out.println("\nDaily Transactions List AFTER Merge Sort (Ascending by Revenue value):");
        for (MergeSort.SaleTransaction st : arr) {
            System.out.println("  " + st);
        }
    }

    // ==========================================
    // 12. TRIE AUTOCOMPLETE SEARCH
    // ==========================================
    private static void autocompleteTrieSearch() {
        ConsoleMenu.printHeader("Trie Autocomplete Product Suggestion");
        String prefix = InputValidator.readString("Enter starting letters to query autocomplete catalog: ");

        List<String> list = trieProductSearch.autocomplete(prefix);
        System.out.println("\nAutocomplete Results Matching prefix \"" + prefix + "\":");
        if (list.isEmpty()) {
            System.out.println("  [No matching item suggestions detected in Prefix Tree.]");
        } else {
            for (String suggest : list) {
                System.out.println("  ↳ " + suggest);
            }
        }
    }

    // ==========================================
    // 13. DISPLAY CONSOLIDATED SYSTEM REPORTS
    // ==========================================
    private static void displayConsolidatedReports() {
        ConsoleMenu.printHeader("Consolidated System Metrics Report");
        System.out.println("Date/Time: " + new Date());
        System.out.println("---------------------------------------------------------");

        System.out.println("\n1. NETWORK BACKBONE STATUS:");
        System.out.printf("   Total Connected Warehouse Hubs: %d\n", logisticsGraph.getAllNodes().size());

        System.out.println("\n2. PENDING ORDER VOLUME IN HEAP STORAGE:");
        orderPriorityQueue.displayQueue();

        System.out.println("\n3. LOGISTICS FLEET DISPATCH LINE (FIFO):");
        truckFIFOQueue.displayQueue();

        System.out.println("\n4. STORAGE STACK LOAD LEVEL (LIFO):");
        packageLIFOStack.displayStack();

        System.out.println("\n5. SYSTEM SESSIONS SECURITY REPORT (HASH MAP):");
        List<UserSession> activeSessions = sessionTable.getAllValues();
        System.out.printf("   Total Active Authenticated Sessions: %d\n", activeSessions.size());
        for (UserSession s : activeSessions) {
            System.out.println("     - " + s);
        }
        System.out.println("=========================================================");
    }
}
