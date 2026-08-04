package orders;

/**
 * Represents a Customer Order within PrimeLogix Logistics & Management System.
 * Fully encapsulated with appropriate SOLID principles and Java doc block.
 *
 * Big-O Complexity:
 * - Space Complexity: O(1) for instance fields.
 * - Time Complexity: O(1) for all field access.
 *
 * @author Senior Java Software Architect
 */
public class Order {
    private final String orderId;
    private final String customerName;
    private final PriorityLevel priority; // Premium, Next Day, Standard
    private final String shippingType;
    private final boolean isSubscribed;   // VIP subscriber status

    /**
     * Enumerates the order priority options.
     */
    public enum PriorityLevel {
        PREMIUM(3),
        NEXT_DAY(2),
        STANDARD(1);

        private final int value;

        PriorityLevel(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * Constructor initializing order requirements.
     * @param orderId Unique Identifier for the order.
     * @param customerName Customer placing the order.
     * @param priority Priority standard (Premium, Next Day, Standard).
     * @param shippingType Transport channel type (e.g. Air, Ground, Express).
     * @param isSubscribed Customer VIP status.
     */
    public Order(String orderId, String customerName, PriorityLevel priority, String shippingType, boolean isSubscribed) {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("Order ID cannot be null or empty.");
        }
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be null or empty.");
        }
        if (priority == null) {
            throw new IllegalArgumentException("Priority level cannot be null.");
        }
        this.orderId = orderId.trim();
        this.customerName = customerName.trim();
        this.priority = priority;
        this.shippingType = shippingType != null ? shippingType.trim() : "Standard Ground";
        this.isSubscribed = isSubscribed;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public PriorityLevel getPriority() {
        return priority;
    }

    public String getShippingType() {
        return shippingType;
    }

    public boolean isSubscribed() {
        return isSubscribed;
    }

    /**
     * Compares priorities of two orders.
     * Premium is processed before Next Day, which is processed before Standard.
     * If priorities are identical, Subscribed users are prioritized.
     *
     * @param other The other order to compare.
     * @return Negative if this is higher priority, positive if lower, zero if equal.
     */
    public int comparePriorityTo(Order other) {
        if (other == null) return -1;

        // Higher numeric priority value goes first
        int priorityCompare = Integer.compare(other.priority.getValue(), this.priority.getValue());
        if (priorityCompare != 0) {
            return priorityCompare;
        }

        // If priorities are equal, VIP subscribers go first
        if (this.isSubscribed && !other.isSubscribed) {
            return -1;
        } else if (!this.isSubscribed && other.isSubscribed) {
            return 1;
        }

        // Natural ordering fallback: older order ID (assuming sequential/alphabetic ID order)
        return this.orderId.compareTo(other.orderId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return orderId.equalsIgnoreCase(order.orderId);
    }

    @Override
    public int hashCode() {
        return orderId.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return String.format("Order[ID=%s, Customer=%s, Priority=%s, Shipping=%s, VIP=%b]",
                orderId, customerName, priority.name(), shippingType, isSubscribed);
    }
}
