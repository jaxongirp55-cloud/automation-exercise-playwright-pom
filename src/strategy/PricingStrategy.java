package strategy;

/**
 * Strategy pattern interface defining dynamic pricing calculations.
 * Allows the billing structure to adapt or switch rates dynamically at runtime.
 */
public interface PricingStrategy {

    /**
     * Calculates the adjusted total amount after applying strategy rules.
     *
     * @param baseAmount The original subtotal base.
     * @return Adjusted calculated total.
     */
    double calculateTotal(double baseAmount);

    /**
     * Retrieves the display name of this strategy.
     *
     * @return String description name.
     */
    String getName();

    /**
     * Gets the discount fraction applied.
     *
     * @return Discount percentage fraction.
     */
    double getDiscountRate();
}
