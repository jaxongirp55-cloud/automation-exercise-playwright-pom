package orders;

/**
 * Represents an Order within the logistics system.
 * Implements encapsulation and models priorities based on subscription or delivery tiers.
 *
 * Time Complexity: O(1) creation/access.
 * Space Complexity: O(1)
 */
public class Order {
    private String orderId;
    private String customerName;
    private PriorityLevel priority; // Premium, Next Day, Standard
    private String shippingType;
    private boolean hasSubscription;

    public enum PriorityLevel {
        PREMIUM(3),
        NEXT_DAY(2),
        STANDARD(1);

        private final int rank;

        PriorityLevel(int rank) {
            this.rank = rank;
        }

        public int getRank() {
            return rank;
        }
    }

    /**
     * Constructs a complete Order instance.
     * @param orderId Unique order id
     * @param customerName Customer name
     * @param priority Priority Tier (PREMIUM, NEXT_DAY, STANDARD)
     * @param shippingType Express, Ground, Air, etc.
     * @param hasSubscription Customer subscription status
     */
    public Order(String orderId, String customerName, PriorityLevel priority, String shippingType, boolean hasSubscription) {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("Order ID cannot be null or empty");
        }
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer Name cannot be null or empty");
        }
        if (priority == null) {
            throw new IllegalArgumentException("Priority Level cannot be null");
        }
        if (shippingType == null || shippingType.trim().isEmpty()) {
            throw new IllegalArgumentException("Shipping type cannot be null or empty");
        }
        this.orderId = orderId;
        this.customerName = customerName;
        this.priority = priority;
        this.shippingType = shippingType;
        this.hasSubscription = hasSubscription;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("Order ID cannot be empty");
        }
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer Name cannot be empty");
        }
        this.customerName = customerName;
    }

    public PriorityLevel getPriority() {
        return priority;
    }

    public void setPriority(PriorityLevel priority) {
        if (priority == null) {
            throw new IllegalArgumentException("Priority cannot be null");
        }
        this.priority = priority;
    }

    public String getShippingType() {
        return shippingType;
    }

    public void setShippingType(String shippingType) {
        if (shippingType == null || shippingType.trim().isEmpty()) {
            throw new IllegalArgumentException("Shipping type cannot be empty");
        }
        this.shippingType = shippingType;
    }

    public boolean isHasSubscription() {
        return hasSubscription;
    }

    public void setHasSubscription(boolean hasSubscription) {
        this.hasSubscription = hasSubscription;
    }

    @Override
    public String toString() {
        return "Order{" +
                "ID='" + orderId + '\'' +
                ", Customer='" + customerName + '\'' +
                ", Priority=" + priority +
                ", Shipping='" + shippingType + '\'' +
                ", VIP=" + hasSubscription +
                '}';
    }
}
