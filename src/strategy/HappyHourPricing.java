package strategy;

import models.Booking;

/**
 * Concrete strategy representing Happy Hour Pricing.
 * Applies a 20% discount to the booking subtotal.
 */
public class HappyHourPricing implements PricingStrategy {

    @Override
    public double calculateTotal(Booking booking) {
        if (booking == null) {
            return 0.0;
        }
        double rawSubtotal = booking.calculateSubtotal();
        return rawSubtotal * 0.80; // 20% discount
    }

    @Override
    public String getName() {
        return "Happy Hour Pricing (20% Discount)";
    }
}
