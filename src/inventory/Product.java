package inventory;

/**
 * Represents a Product entity in the logistics inventory system.
 * Highly encapsulated with SOLID standards.
 *
 * Big-O Complexity:
 * - Space Complexity: O(1)
 * - Time Complexity: O(1)
 *
 * @author Senior Java Software Architect
 */
public class Product {
    private final String id;
    private final String name;
    private final double price;
    private final String category;

    /**
     * Product constructor.
     * @param id Unique Product identifier.
     * @param name Descriptive item name.
     * @param price Standard unit retail value.
     * @param category Product category group.
     */
    public Product(String id, String name, double price, String category) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Product ID cannot be null or empty.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty.");
        }
        if (price < 0.0) {
            throw new IllegalArgumentException("Product price cannot be negative.");
        }
        this.id = id.trim();
        this.name = name.trim();
        this.price = price;
        this.category = category != null ? category.trim() : "General";
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return id.equalsIgnoreCase(product.id);
    }

    @Override
    public int hashCode() {
        return id.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return String.format("Product[ID=%s, Name=%s, Price=$%.2f, Category=%s]",
                id, name, price, category);
    }
}
