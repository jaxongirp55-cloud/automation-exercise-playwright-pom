package inventory;

/**
 * Node structure for the Binary Search Tree, wrapping a Product.
 * Products are sorted within the BST by Price.
 *
 * Time Complexity (Creation): O(1)
 * Space Complexity: O(1)
 */
public class BSTNode {
    private Product product;
    private BSTNode left;
    private BSTNode right;

    /**
     * Constructs a BST Node.
     * @param product The product element.
     */
    public BSTNode(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
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
            throw new IllegalArgumentException("Product cannot be null");
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
