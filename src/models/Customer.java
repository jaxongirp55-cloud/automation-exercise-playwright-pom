package models;

/**
 * Represents a Customer of the StayEase Hotel Room Booking Management System.
 * Stores customer identifying details and contact information.
 */
public class Customer {
    private final String customerId;
    private final String name;
    private final String email;
    private final String phoneNumber;
    private final boolean isLoyalCustomer;

    /**
     * Constructs a new Customer instance.
     *
     * @param customerId      Unique ID of the customer.
     * @param name            Full name of the customer.
     * @param email           Email address of the customer.
     * @param phoneNumber     Phone number of the customer.
     * @param isLoyalCustomer Boolean indicating if the customer belongs to the loyalty program.
     */
    public Customer(String customerId, String name, String email, String phoneNumber, boolean isLoyalCustomer) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.isLoyalCustomer = isLoyalCustomer;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isLoyalCustomer() {
        return isLoyalCustomer;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "ID='" + customerId + '\'' +
                ", Name='" + name + '\'' +
                ", Email='" + email + '\'' +
                ", Phone='" + phoneNumber + '\'' +
                ", Loyal=" + isLoyalCustomer +
                '}';
    }
}
