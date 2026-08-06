package models;

/**
 * Represents a line item on an invoice.
 */
public class BillItem {
    private String name;
    private double unitPrice;
    private int quantity;

    /**
     * Constructor for BillItem.
     * @param name The name of the item or service.
     * @param unitPrice The unit cost of the item.
     * @param quantity The amount purchased.
     */
    public BillItem(String name, double unitPrice, int quantity) {
        this.name = name;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Subtotal for this itemized entry.
     * @return Unit price times quantity.
     */
    public double getSubtotal() {
        return unitPrice * quantity;
    }

    @Override
    public String toString() {
        return String.format("%-25s %3d x $%-7.2f $%-8.2f", name, quantity, unitPrice, getSubtotal());
    }
}
