package strategy;

/**
 * Concrete strategy representing Standard standard rack rates.
 * No modifications or discounts applied.
 */
public class StandardPricing implements PricingStrategy {

    @Override
    public double calculateTotal(double baseAmount) {
        return baseAmount;
    }

    @Override
    public String getName() {
        return "Standard Rack Rate (No Discount)";
    }

    @Override
    public double getDiscountRate() {
        return 0.0;
    }
}
