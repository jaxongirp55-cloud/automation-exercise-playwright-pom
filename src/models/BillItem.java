package models;

/**
 * Represents an individual line-item printed on a billing invoice.
 */
public class BillItem {
    private final String description;
    private final double amount;

    /**
     * Constructs a BillItem.
     *
     * @param description Descriptive label.
     * @param amount      Cost or rate of item.
     */
    public BillItem(String description, double amount) {
        this.description = description;
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return String.format("%-40s $%10.2f", description, amount);
    }
}
