package strategy;

import models.Booking;

/**
 * Concrete strategy representing Standard Pricing.
 * No discounts are applied to the booking subtotal.
 */
public class StandardPricing implements PricingStrategy {

    @Override
    public double calculateTotal(Booking booking) {
        if (booking == null) {
            return 0.0;
        }
        return booking.calculateSubtotal();
    }

    @Override
    public String getName() {
        return "Standard Pricing (No Discount)";
    }
}
