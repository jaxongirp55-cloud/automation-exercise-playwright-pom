package inventory;

import java.util.ArrayList;
import java.util.List;

/**
 * Manual Binary Search Tree (BST) implementation for managing Product Inventory.
 * Key comparisons are made on the unique Product ID (alphabetic order).
 * Supports standard insert, search, traversal, range searching by price, and deletion.
 * No built-in Java TreeMap or TreeSet is utilized, satisfying Unit 26 assignments.
 *
 * Big-O Complexity:
 * - Time Complexity:
 *   - Insertion: Average O(log N), Worst Case O(N) if tree becomes highly skewed.
 *   - Search: Average O(log N), Worst Case O(N).
 *   - Deletion: Average O(log N), Worst Case O(N).
 *   - Traversals (InOrder, PreOrder, PostOrder): O(N) since every node is visited exactly once.
 *   - Range Search: O(N) maximum, but optimized on sorted property.
 * - Space Complexity:
 *   - Average: O(log N) recursive stack frames.
 *   - Worst: O(N) call stack for a highly unbalanced tree.
 *
 * @author Senior Java Software Architect
 */
public class BinarySearchTree {
    private BSTNode root;

    /**
     * Instantiates an empty BST.
     */
    public BinarySearchTree() {
        this.root = null;
    }

    /**
     * Inserts a product into the Binary Search Tree.
     * Throws an exception if a product with the same ID already exists to prevent duplicates.
     * @param product The product object to insert.
     */
    public void insert(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Cannot insert null product into the inventory tree.");
        }
        root = insertRec(root, product);
    }

    private BSTNode insertRec(BSTNode current, Product product) {
        if (current == null) {
            return new BSTNode(product);
        }

        int cmp = product.getId().compareToIgnoreCase(current.getProduct().getId());

        if (cmp < 0) {
            current.setLeft(insertRec(current.getLeft(), product));
        } else if (cmp > 0) {
            current.setRight(insertRec(current.getRight(), product));
        } else {
            // Equal IDs are forbidden duplicates
            throw new IllegalStateException("Duplicate Product ID detected: " + product.getId());
        }

        return current;
    }

    /**
     * Searches for a product by its ID (Case-insensitive).
     * @param id The Product ID key.
     * @return The Product if found, or null if it doesn't exist.
     */
    public Product search(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }
        BSTNode node = searchRec(root, id.trim());
        return node == null ? null : node.getProduct();
    }

    private BSTNode searchRec(BSTNode current, String id) {
        if (current == null) {
            return null;
        }

        int cmp = id.compareToIgnoreCase(current.getProduct().getId());

        if (cmp == 0) {
            return current;
        } else if (cmp < 0) {
            return searchRec(current.getLeft(), id);
        } else {
            return searchRec(current.getRight(), id);
        }
    }

    /**
     * Deletes a product from the inventory by its ID.
     * @param id Product ID to delete.
     */
    public void delete(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Product ID cannot be null or empty for deletion.");
        }
        if (search(id) == null) {
            throw new IllegalArgumentException("Product ID " + id + " does not exist in inventory.");
        }
        root = deleteRec(root, id.trim());
    }

    private BSTNode deleteRec(BSTNode current, String id) {
        if (current == null) {
            return null;
        }

        int cmp = id.compareToIgnoreCase(current.getProduct().getId());

        if (cmp < 0) {
            current.setLeft(deleteRec(current.getLeft(), id));
        } else if (cmp > 0) {
            current.setRight(deleteRec(current.getRight(), id));
        } else {
            // Node found! Prepare for deletion

            // Case 1: Leaf node (no children)
            if (current.getLeft() == null && current.getRight() == null) {
                return null;
            }

            // Case 2: One child
            if (current.getLeft() == null) {
                return current.getRight();
            } else if (current.getRight() == null) {
                return current.getLeft();
            }

            // Case 3: Two children
            // Find in-order successor (smallest in right subtree)
            BSTNode successor = findMin(current.getRight());
            // Replace values
            current.setProduct(successor.getProduct());
            // Delete successor
            current.setRight(deleteRec(current.getRight(), successor.getProduct().getId()));
        }

        return current;
    }

    private BSTNode findMin(BSTNode current) {
        while (current.getLeft() != null) {
            current = current.getLeft();
        }
        return current;
    }

    /**
     * Traverses and prints the BST using InOrder Traversal (Sorted by ID).
     */
    public void displayInOrder() {
        System.out.println("\n--- BST IN-ORDER TRAVERSAL (Sorted alphabetically by ID) ---");
        List<Product> list = new ArrayList<>();
        inOrderRec(root, list);
        if (list.isEmpty()) {
            System.out.println("[Inventory is empty.]");
        } else {
            for (Product p : list) {
                System.out.println("  " + p);
            }
        }
    }

    private void inOrderRec(BSTNode current, List<Product> list) {
        if (current != null) {
            inOrderRec(current.getLeft(), list);
            list.add(current.getProduct());
            inOrderRec(current.getRight(), list);
        }
    }

    /**
     * Traverses and prints the BST using PreOrder Traversal.
     */
    public void displayPreOrder() {
        System.out.println("\n--- BST PRE-ORDER TRAVERSAL ---");
        List<Product> list = new ArrayList<>();
        preOrderRec(root, list);
        if (list.isEmpty()) {
            System.out.println("[Inventory is empty.]");
        } else {
            for (Product p : list) {
                System.out.println("  " + p);
            }
        }
    }

    private void preOrderRec(BSTNode current, List<Product> list) {
        if (current != null) {
            list.add(current.getProduct());
            preOrderRec(current.getLeft(), list);
            preOrderRec(current.getRight(), list);
        }
    }

    /**
     * Traverses and prints the BST using PostOrder Traversal.
     */
    public void displayPostOrder() {
        System.out.println("\n--- BST POST-ORDER TRAVERSAL ---");
        List<Product> list = new ArrayList<>();
        postOrderRec(root, list);
        if (list.isEmpty()) {
            System.out.println("[Inventory is empty.]");
        } else {
            for (Product p : list) {
                System.out.println("  " + p);
            }
        }
    }

    private void postOrderRec(BSTNode current, List<Product> list) {
        if (current != null) {
            postOrderRec(current.getLeft(), list);
            postOrderRec(current.getRight(), list);
            list.add(current.getProduct());
        }
    }

    /**
     * Search products whose unit retail price falls inside a specific boundary [minPrice, maxPrice].
     * @param minPrice Minimum price.
     * @param maxPrice Maximum price.
     * @return List of matching products.
     */
    public List<Product> rangeSearch(double minPrice, double maxPrice) {
        List<Product> results = new ArrayList<>();
        rangeSearchRec(root, minPrice, maxPrice, results);
        return results;
    }

    private void rangeSearchRec(BSTNode current, double minPrice, double maxPrice, List<Product> results) {
        if (current == null) {
            return;
        }

        // Traversing fully to inspect every subtree (price is not the BST key, ID is)
        rangeSearchRec(current.getLeft(), minPrice, maxPrice, results);
        double price = current.getProduct().getPrice();
        if (price >= minPrice && price <= maxPrice) {
            results.add(current.getProduct());
        }
        rangeSearchRec(current.getRight(), minPrice, maxPrice, results);
    }
}
