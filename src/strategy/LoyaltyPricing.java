package strategy;

import models.Booking;
import models.BookingItem;
import roomitems.Beverage;

/**
 * Concrete strategy representing Loyalty Pricing.
 * Applies a 10% discount to the subtotal.
 * If the customer is a loyalty member and has ordered a beverage, the price of the first beverage is fully waived.
 */
public class LoyaltyPricing implements PricingStrategy {

    @Override
    public double calculateTotal(Booking booking) {
        if (booking == null) {
            return 0.0;
        }

        double subtotal = booking.calculateSubtotal();
        double drinkDeduction = 0.0;

        // Check if the guest is a loyalty member
        if (booking.getCustomer() != null && booking.getCustomer().isLoyaltyMember()) {
            // Find the first beverage item to make it free
            for (BookingItem item : booking.getBookingItems()) {
                if (item.getRoomItem() instanceof Beverage) {
                    // Waive the price of one beverage unit
                    drinkDeduction = item.getRoomItem().getPrice();
                    break;
                }
            }
        }

        // Apply deduction first, then apply a 10% discount on the remainder
        double netAmount = subtotal - drinkDeduction;
        if (netAmount < 0) {
            netAmount = 0;
        }

        return netAmount * 0.90; // 10% discount
    }

    @Override
    public String getName() {
        return "Loyalty Pricing (10% Discount + Free Drink for Loyalty Members)";
    }
}
