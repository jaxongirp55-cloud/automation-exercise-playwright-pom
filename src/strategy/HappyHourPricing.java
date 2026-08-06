package strategy;

/**
 * Concrete strategy representing Happy Hour special.
 * Applies a flat 20% discount on prices.
 */
public class HappyHourPricing implements PricingStrategy {

    @Override
    public double calculateTotal(double baseAmount) {
        return baseAmount * 0.80; // 20% Discount
    }

    @Override
    public String getName() {
        return "Happy Hour Rate (20% Discount)";
    }

    @Override
    public double getDiscountRate() {
        return 0.20;
    }
}
