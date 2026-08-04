package orders;

import java.util.EmptyStackException;

/**
 * Manual LIFO (Last-In, First-Out) Stack implementation for loading packages into a Delivery Truck.
 * Demonstrates manual node chaining or dynamic array manipulation.
 * Here we use an array-based dynamic stack representation for O(1) push/pop.
 *
 * Big-O Complexity:
 * - Time Complexity:
 *   - Push: O(1) amortized
 *   - Pop: O(1)
 *   - Peek: O(1)
 *   - Display: O(N) where N is number of packages
 * - Space Complexity: O(N) where N is number of packages
 *
 * @author Senior Java Software Architect
 */
public class PackageStack {
    private String[] stack;
    private int top;
    private int capacity;

    /**
     * Initializes an empty Package Stack with standard initial capacity.
     */
    public PackageStack() {
        this.capacity = 8;
        this.stack = new String[capacity];
        this.top = -1;
    }

    /**
     * Pushes a package onto the loading stack.
     * @param packageId Unique barcode of package.
     */
    public void push(String packageId) {
        if (packageId == null || packageId.trim().isEmpty()) {
            throw new IllegalArgumentException("Package ID / Name cannot be null or empty.");
        }
        ensureCapacity();
        stack[++top] = packageId.trim();
    }

    /**
     * Removes and returns the package from the top of the stack.
     * @return package code or name.
     */
    public String pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        String packageId = stack[top];
        stack[top--] = null; // facilitate GC
        return packageId;
    }

    /**
     * Inspects the package at the top of the stack without removing it.
     * @return package code or name.
     */
    public String peek() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return stack[top];
    }

    /**
     * Checks if the stack has no packages.
     * @return true if empty.
     */
    public boolean isEmpty() {
        return top == -1;
    }

    /**
     * Returns total packages loaded in stack.
     * @return count.
     */
    public int size() {
        return top + 1;
    }

    private void ensureCapacity() {
        if (top == capacity - 1) {
            capacity *= 2;
            String[] temp = new String[capacity];
            System.arraycopy(stack, 0, temp, 0, stack.length);
            stack = temp;
        }
    }

    /**
     * Displays stack visually in standard console representations.
     */
    public void displayStack() {
        System.out.println("\n===== LIFO PACKAGE LOADING STACK =====");
        if (isEmpty()) {
            System.out.println("[Stack is empty. No packages loaded yet.]");
            return;
        }
        System.out.println("Top of Stack (Last package in, first package out/discharged):");
        for (int i = top; i >= 0; i--) {
            System.out.println("  [ | " + stack[i] + " | ]");
            if (i > 0) {
                System.out.println("       ↓");
            }
        }
        System.out.println("--------------------------------------");
        System.out.println("Total Packages Loaded: " + size());
        System.out.println("=======================================");
    }
}
