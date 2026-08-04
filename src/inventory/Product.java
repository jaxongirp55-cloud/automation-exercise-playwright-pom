package inventory;

/**
 * Represents a Product in the warehouse inventory system.
 * Handled via a manual Binary Search Tree.
 *
 * Time Complexity (Creation): O(1)
 * Space Complexity: O(1)
 */
public class Product {
    private String id;
    private String name;
    private double price;
    private String category;

    /**
     * Constructs a complete Product instance.
     * @param id Unique product ID
     * @param name Name of the product
     * @param price Value price of product
     * @param category Department category
     */
    public Product(String id, String name, double price, String category) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Product ID cannot be empty");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be empty");
        }
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Product ID cannot be empty");
        }
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be empty");
        }
        this.category = category;
    }

    @Override
    public String toString() {
        return String.format("Product [ID=%s, Name=%-20s, Price=$%.2f, Category=%s]", id, name, price, category);
    }
}
