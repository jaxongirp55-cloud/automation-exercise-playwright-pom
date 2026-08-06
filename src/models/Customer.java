package models;

/**
 * Represents a customer of the hotel.
 */
public class Customer {
    private String id;
    private String name;
    private String email;
    private String phone;
    private boolean loyaltyMember;

    /**
     * Constructor for Customer.
     * @param id Unique identifier.
     * @param name Full name.
     * @param email Contact email.
     * @param phone Contact phone number.
     * @param loyaltyMember Whether the customer is part of the loyalty program.
     */
    public Customer(String id, String name, String email, String phone, boolean loyaltyMember) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.loyaltyMember = loyaltyMember;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isLoyaltyMember() {
        return loyaltyMember;
    }

    public void setLoyaltyMember(boolean loyaltyMember) {
        this.loyaltyMember = loyaltyMember;
    }

    @Override
    public String toString() {
        return String.format("Customer[ID=%s, Name=%s, Email=%s, Phone=%s, Loyalty=%b]",
                id, name, email, phone, loyaltyMember);
    }
}
