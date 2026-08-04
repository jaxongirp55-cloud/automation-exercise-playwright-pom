package utils;

/**
 * Handles displaying the unified console navigation structure for the terminal.
 *
 * Time/Space Complexity: O(1)
 */
public class ConsoleMenu {

    /**
     * Renders primary system operational routes.
     */
    public static void displayMainMenu() {
        System.out.println();
        System.out.println("========================================");
        System.out.println("     PRIMELOGIX MANAGEMENT SYSTEM       ");
        System.out.println("========================================");
        System.out.println(" 1  Graph Management (Warehouses & Hubs)");
        System.out.println(" 2  Shortest Route (Dijkstra's Path)");
        System.out.println(" 3  Minimum Network (Prim's MST)");
        System.out.println(" 4  Add Order");
        System.out.println(" 5  Process Orders (Priority Heap Queue)");
        System.out.println(" 6  Truck Queue (FIFO Terminals)");
        System.out.println(" 7  Package Stack (LIFO Storage)");
        System.out.println(" 8  Product Inventory (BST Management)");
        System.out.println(" 9  Product Search (Range Search & Find)");
        System.out.println(" 10 Session Manager (Chaining Hash Table)");
        System.out.println(" 11 Sort Sales (Manual Merge Sort)");
        System.out.println(" 12 Trie Search (Autocomplete / Prefix)");
        System.out.println(" 13 Display Reports (All System Summaries)");
        System.out.println(" 0  Exit");
        System.out.println("========================================");
    }
}
