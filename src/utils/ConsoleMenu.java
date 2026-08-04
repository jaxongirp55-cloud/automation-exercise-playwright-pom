package utils;

/**
 * Formats beautifully designed terminal interface menus.
 *
 * @author Senior Java Software Architect
 */
public class ConsoleMenu {

    /**
     * Prints the primary application banner and standard operations menu.
     */
    public static void printMainMenu() {
        System.out.println();
        System.out.println("=========================================================");
        System.out.println("          PRIMELOGIX LOGISTICS & MANAGEMENT SYSTEM       ");
        System.out.println("           BTEC HND Unit 26: Data Structures & Algorithms ");
        System.out.println("=========================================================");
        System.out.println("  1.  Graph Management (Add Warehouse, Create Roads)");
        System.out.println("  2.  Shortest Route (Dijkstra algorithm)");
        System.out.println("  3.  Minimum Spanning Network (Prim's MST algorithm)");
        System.out.println("  4.  Add Customer Order (Enqueue to Priority Queue)");
        System.out.println("  5.  Process Pending Orders (Highest Priority First)");
        System.out.println("  6.  Truck Dispatch Manager (FIFO Queue Operations)");
        System.out.println("  7.  Package Load Manager (LIFO Stack Operations)");
        System.out.println("  8.  Product Inventory Tree (Insert/Delete/View BST)");
        System.out.println("  9.  Product Search & Range Filter");
        System.out.println("  10. Session Manager (In-Memory Chained Hash Table)");
        System.out.println("  11. Sort Daily Sales Data (Manual Merge Sort)");
        System.out.println("  12. Trie Autocomplete Search (Prefix Tree Suggestions)");
        System.out.println("  13. Display Consolidated System Reports");
        System.out.println("  0.  Exit System cleanly");
        System.out.println("=========================================================");
    }

    /**
     * Formats a page header dynamically.
     * @param title Title of the current subsystem module.
     */
    public static void printHeader(String title) {
        System.out.println();
        System.out.println("=========================================================");
        System.out.println("  " + title.toUpperCase());
        System.out.println("=========================================================");
    }
}
