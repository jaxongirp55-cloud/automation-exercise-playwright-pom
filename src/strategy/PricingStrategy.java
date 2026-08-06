package strategy;

import models.Booking;

/**
 * Strategy interface for calculating the booking total.
 * Implements the Strategy Design Pattern, allowing the pricing algorithm
 * to be changed dynamically at runtime.
 */
public interface PricingStrategy {

    /**
     * Calculates the booking total after applying specific pricing algorithms.
     * @param booking The booking for which to calculate the price.
     * @return The final strategy-calculated total.
     */
    double calculateTotal(Booking booking);

    /**
     * Gets the descriptive name of the pricing strategy.
     * @return Strategy name.
     */
    String getName();
}
