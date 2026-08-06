package models;

import strategy.PricingStrategy;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a customer's final invoice.
 * Handles itemization, tax calculations, tip allocations, split payments, and discounts.
 */
public class Bill {
    private String billId;
    private Booking booking;
    private List<BillItem> billItems;
    private double baseSubtotal;     // sum of items + room price
    private double discount;         // discount applied by strategy
    private double discountedSubtotal; // subtotal after discount
    private double tax;              // calculated tax
    private double tip;              // custom tip
    private double finalTotal;       // discountedSubtotal + tax + tip
    private boolean paid;

    private static final double TAX_RATE = 0.10; // 10% tax rate

    /**
     * Constructor for Bill.
     * @param billId The unique bill identifier.
     * @param booking The associated room booking.
     * @param strategy The pricing strategy applied to calculate discounts.
     * @param tip The gratuity left by the guest.
     */
    public Bill(String billId, Booking booking, PricingStrategy strategy, double tip) {
        this.billId = billId;
        this.booking = booking;
        this.billItems = new ArrayList<>();
        this.tip = tip;
        this.paid = false;

        // Itemize from booking
        populateBillItems();

        // Calculate totals using selected strategy
        calculateTotals(strategy);
    }

    private void populateBillItems() {
        // Add Room Charge
        billItems.add(new BillItem("Room Charge (" + booking.getRoom().getRoomType() + ")",
                booking.getRoom().getBasePrice(), 1));

        // Add other ordered items
        for (BookingItem item : booking.getBookingItems()) {
            billItems.add(new BillItem(item.getRoomItem().getName(),
                    item.getRoomItem().getPrice(), item.getQuantity()));
        }
    }

    /**
     * Calculates final pricing breakdown using the strategy, tax, and tip.
     * @param strategy The strategy to determine net total.
     */
    public void calculateTotals(PricingStrategy strategy) {
        // Total base cost of all services before strategy discounts
        this.baseSubtotal = booking.calculateSubtotal();

        // Final subtotal calculated by pricing strategy
        double strategyTotal = strategy.calculateTotal(booking);

        // Discount is the difference between base price and strategy price
        this.discount = Math.max(0, baseSubtotal - strategyTotal);
        this.discountedSubtotal = strategyTotal;

        // Tax is calculated on the discounted subtotal
        this.tax = this.discountedSubtotal * TAX_RATE;

        // Grand total
        this.finalTotal = this.discountedSubtotal + this.tax + this.tip;
    }

    public String getBillId() {
        return billId;
    }

    public Booking getBooking() {
        return booking;
    }

    public List<BillItem> getBillItems() {
        return billItems;
    }

    public double getBaseSubtotal() {
        return baseSubtotal;
    }

    public double getDiscount() {
        return discount;
    }

    public double getDiscountedSubtotal() {
        return discountedSubtotal;
    }

    public double getTax() {
        return tax;
    }

    public double getTip() {
        return tip;
    }

    public void setTip(double tip) {
        this.tip = tip;
        // Recalculate final total
        this.finalTotal = this.discountedSubtotal + this.tax + this.tip;
    }

    public double getFinalTotal() {
        return finalTotal;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    /**
     * Splits the final total equally among a specified number of people.
     * @param people Number of parties splitting the bill.
     * @return List containing the equal split amounts.
     * @throws IllegalArgumentException If people count is less than 1.
     */
    public List<Double> splitBill(int people) {
        if (people <= 0) {
            throw new IllegalArgumentException("Number of people to split with must be greater than 0.");
        }
        List<Double> splits = new ArrayList<>();
        double amountPerPerson = finalTotal / people;
        for (int i = 0; i < people; i++) {
            splits.add(amountPerPerson);
        }
        return splits;
    }

    /**
     * Prints an itemized receipt representation of the bill.
     * @return A multi-line receipt string.
     */
    public String generateReceipt() {
        StringBuilder sb = new StringBuilder();
        sb.append("=========================================\n");
        sb.append("              STAYEASE HOTEL             \n");
        sb.append("                 RECEIPT                 \n");
        sb.append("=========================================\n");
        sb.append(String.format("Bill ID    : %s\n", billId));
        sb.append(String.format("Booking ID : %s\n", booking.getBookingId()));
        sb.append(String.format("Room #     : %d (%s)\n", booking.getRoom().getRoomNumber(), booking.getRoom().getRoomType()));
        sb.append(String.format("Guest Name : %s\n", booking.getCustomer().getName()));
        sb.append(String.format("Staff ID   : %s\n", booking.getStaff().getId()));
        sb.append("-----------------------------------------\n");
        sb.append(String.format("%-25s %3s   %-7s %-8s\n", "Item Description", "Qty", "Price", "Subtotal"));
        sb.append("-----------------------------------------\n");
        for (BillItem item : billItems) {
            sb.append(item.toString()).append("\n");
        }
        sb.append("-----------------------------------------\n");
        sb.append(String.format("%-32s $%-8.2f\n", "Subtotal:", baseSubtotal));
        if (discount > 0) {
            sb.append(String.format("%-32s-$%-8.2f\n", "Discount:", discount));
            sb.append(String.format("%-32s $%-8.2f\n", "Discounted Subtotal:", discountedSubtotal));
        }
        sb.append(String.format("%-32s $%-8.2f (%.0f%%)\n", "Tax:", tax, TAX_RATE * 100));
        sb.append(String.format("%-32s $%-8.2f\n", "Tip (Gratuity):", tip));
        sb.append("=========================================\n");
        sb.append(String.format("%-32s $%-8.2f\n", "GRAND TOTAL:", finalTotal));
        sb.append("=========================================\n");
        sb.append(String.format("Payment Status: %s\n", paid ? "PAID" : "UNPAID"));
        sb.append("=========================================\n");
        return sb.toString();
    }

    @Override
    public String toString() {
        return String.format("Bill[ID=%s, BookingID=%s, Total=$%.2f, Paid=%b]",
                billId, booking.getBookingId(), finalTotal, paid);
    }
}
