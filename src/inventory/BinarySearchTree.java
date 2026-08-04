package inventory;

import java.util.ArrayList;
import java.util.List;

/**
 * Manually implemented Binary Search Tree (BST) for high-performance Product Inventory Management.
 * Handled and indexed dynamically by Product Price (as key).
 * Supports Insert, Delete, Search, Traversals (In-order, Pre-order, Post-order), and Range-Search.
 *
 * Time Complexity (Insert/Delete/Search): O(log N) average, O(N) worst-case (skewed).
 * Space Complexity: O(N) for nodes representation.
 */
public class BinarySearchTree {
    private BSTNode root;

    public BinarySearchTree() {
        this.root = null;
    }

    /**
     * Inserts a product node into the tree based on Price.
     * @param product Product to insert.
     */
    public void insert(Product product) {
        if (product == null) return;
        root = insertRec(root, product);
    }

    private BSTNode insertRec(BSTNode root, Product product) {
        if (root == null) {
            return new BSTNode(product);
        }
        // Comparison based on Price
        if (product.getPrice() < root.getProduct().getPrice()) {
            root.setLeft(insertRec(root.getLeft(), product));
        } else {
            // Equal prices are put in the right subtree
            root.setRight(insertRec(root.getRight(), product));
        }
        return root;
    }

    /**
     * Searches for a product by exact Price.
     * @param price Price query.
     * @return Product if found, otherwise null.
     */
    public Product search(double price) {
        BSTNode result = searchRec(root, price);
        return result != null ? result.getProduct() : null;
    }

    private BSTNode searchRec(BSTNode root, double price) {
        if (root == null || Math.abs(root.getProduct().getPrice() - price) < 0.0001) {
            return root;
        }
        if (price < root.getProduct().getPrice()) {
            return searchRec(root.getLeft(), price);
        }
        return searchRec(root.getRight(), price);
    }

    /**
     * Deletes a product with the matching exact Price.
     * @param price Target price to delete.
     */
    public void delete(double price) {
        root = deleteRec(root, price);
    }

    private BSTNode deleteRec(BSTNode root, double price) {
        if (root == null) {
            return null;
        }

        if (price < root.getProduct().getPrice()) {
            root.setLeft(deleteRec(root.getLeft(), price));
        } else if (price > root.getProduct().getPrice()) {
            root.setRight(deleteRec(root.getRight(), price));
        } else {
            // Node to delete found!
            if (root.getLeft() == null) {
                return root.getRight();
            } else if (root.getRight() == null) {
                return root.getLeft();
            }

            // Node with two children: Get the inorder successor (smallest in the right subtree)
            root.setProduct(minValue(root.getRight()));

            // Delete the inorder successor
            root.setRight(deleteRec(root.getRight(), root.getProduct().getPrice()));
        }
        return root;
    }

    private Product minValue(BSTNode root) {
        Product minv = root.getProduct();
        while (root.getLeft() != null) {
            minv = root.getLeft().getProduct();
            root = root.getLeft();
        }
        return minv;
    }

    /**
     * Helper to route traversals from console commands.
     * @param mode 1 = InOrder, 2 = PreOrder, 3 = PostOrder
     */
    public void orderBookDisplay(int mode) {
        if (mode == 1) {
            inorder();
        } else if (mode == 2) {
            preorder();
        } else if (mode == 3) {
            postorder();
        }
    }

    /**
     * Inorder traversal (Left, Root, Right). Yields sorted order.
     */
    public void inorder() {
        inorderRec(root);
        System.out.println();
    }

    private void inorderRec(BSTNode root) {
        if (root != null) {
            inorderRec(root.getLeft());
            System.out.println("  " + root.getProduct());
            inorderRec(root.getRight());
        }
    }

    /**
     * Preorder traversal (Root, Left, Right).
     */
    public void preorder() {
        preorderRec(root);
        System.out.println();
    }

    private void preorderRec(BSTNode root) {
        if (root != null) {
            System.out.println("  " + root.getProduct());
            preorderRec(root.getLeft());
            preorderRec(root.getRight());
        }
    }

    /**
     * Postorder traversal (Left, Right, Root).
     */
    public void postorder() {
        postorderRec(root);
        System.out.println();
    }

    private void postorderRec(BSTNode root) {
        if (root != null) {
            postorderRec(root.getLeft());
            postorderRec(root.getRight());
            System.out.println("  " + root.getProduct());
        }
    }

    /**
     * Performs a range search of products with prices within [min, max].
     * @param min Minimum boundary price.
     * @param max Maximum boundary price.
     * @return List of products within that range.
     */
    public List<Product> rangeSearch(double min, double max) {
        List<Product> results = new ArrayList<>();
        rangeSearchRec(root, min, max, results);
        return results;
    }

    private void rangeSearchRec(BSTNode root, double min, double max, List<Product> results) {
        if (root == null) {
            return;
        }

        double currentPrice = root.getProduct().getPrice();

        // If current node's price is greater than min, we go left
        if (currentPrice > min) {
            rangeSearchRec(root.getLeft(), min, max, results);
        }

        // If current node is within range, add to results
        if (currentPrice >= min && currentPrice <= max) {
            results.add(root.getProduct());
        }

        // If current node's price is less than max, we go right
        if (currentPrice < max) {
            rangeSearchRec(root.getRight(), min, max, results);
        }
    }
}
