package orders;

/**
 * Manually implemented LIFO Stack representing cargo/packages stacked inside a delivery truck.
 * Designed with a singly linked list architecture without any standard Java collections.
 * Last package loaded is the first package unloaded.
 *
 * Time Complexity (Push): O(1)
 * Time Complexity (Pop): O(1)
 * Time Complexity (Peek): O(1)
 * Space Complexity: O(N) where N is number of stacked packages.
 */
public class PackageStack {

    private static class StackNode {
        private final String packageId;
        private StackNode next;

        public StackNode(String packageId) {
            this.packageId = packageId;
        }
    }

    private StackNode top;
    private int size;

    /**
     * Initializes an empty Cargo Stack.
     */
    public PackageStack() {
        this.top = null;
        this.size = 0;
    }

    /**
     * Pushes a package onto the loading stack (LIFO - Top).
     * @param packageId Unique package tracking code.
     */
    public void push(String packageId) {
        if (packageId == null || packageId.trim().isEmpty()) {
            throw new IllegalArgumentException("Package ID cannot be empty");
        }
        StackNode newNode = new StackNode(packageId);
        newNode.next = top;
        top = newNode;
        size++;
    }

    /**
     * Pops and returns the top package from the stack (LIFO - Pop).
     * @return Top package ID, or null if stack is empty.
     */
    public String pop() {
        if (isEmpty()) {
            return null;
        }
        String packageId = top.packageId;
        top = top.next;
        size--;
        return packageId;
    }

    /**
     * Peeks the top package from stack without removing it.
     * @return Top package ID, or null if empty.
     */
    public String peek() {
        if (isEmpty()) {
            return null;
        }
        return top.packageId;
    }

    /**
     * Verifies if stack is empty.
     * @return True if empty, false otherwise.
     */
    public boolean isEmpty() {
        return top == null;
    }

    /**
     * Returns total packages stacked.
     * @return Stack size.
     */
    public int size() {
        return size;
    }

    /**
     * Displays current stack status from top to bottom.
     */
    public void displayStack() {
        System.out.println("===== TRUCK CARGO CONTAINER (LIFO PACKAGES) =====");
        if (isEmpty()) {
            System.out.println("[Container is completely empty. Please load cargo]");
            return;
        }
        StackNode curr = top;
        System.out.println("  [ TOP / LAST LOADED ]");
        while (curr != null) {
            System.out.printf("   └─> Package Tracking ID: %s\n", curr.packageId);
            curr = curr.next;
        }
        System.out.println("  [ BOTTOM / FIRST LOADED ]");
    }
}
