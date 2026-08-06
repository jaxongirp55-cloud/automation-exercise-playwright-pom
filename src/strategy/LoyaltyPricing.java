package strategy;

/**
 * Concrete strategy representing Loyalty rewards program rates.
 * Applies 10% discount on prices and awards a free beverage.
 */
public class LoyaltyPricing implements PricingStrategy {

    @Override
    public double calculateTotal(double baseAmount) {
        return baseAmount * 0.90; // 10% Discount
    }

    @Override
    public String getName() {
        return "Loyalty Member Rate (10% Discount + Free Drink Voucher)";
    }

    @Override
    public double getDiscountRate() {
        return 0.10;
    }
}
