package inventory;

/**
 * Node within the custom Binary Search Tree.
 * Holds a Product value, and references to left and right children.
 *
 * Big-O Complexity:
 * - Space Complexity: O(1) for pointers.
 * - Time Complexity: O(1).
 *
 * @author Senior Java Software Architect
 */
public class BSTNode {
    private Product product;
    private BSTNode left;
    private BSTNode right;

    /**
     * Constructs a node containing a single Product.
     * @param product Product data.
     */
    public BSTNode(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Node product data cannot be null.");
        }
        this.product = product;
        this.left = null;
        this.right = null;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Node product data cannot be null.");
        }
        this.product = product;
    }

    public BSTNode getLeft() {
        return left;
    }

    public void setLeft(BSTNode left) {
        this.left = left;
    }

    public BSTNode getRight() {
        return right;
    }

    public void setRight(BSTNode right) {
        this.right = right;
    }
}
