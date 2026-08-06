package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a compiled itemized invoice for a room booking.
 * Calculates totals, applied taxes, tips, and splits billing details among guests.
 */
public class Bill {
    private final String billId;
    private final Booking booking;
    private final List<BillItem> lineItems;
    private double baseTotal;
    private final double taxRate; // e.g. 0.15 for 15% VAT
    private double tipAmount;
    private int splitCount;

    /**
     * Constructs a Bill invoice.
     *
     * @param billId  Unique billing index.
     * @param booking The associated booking.
     * @param taxRate Applied tax percentage.
     */
    public Bill(String billId, Booking booking, double taxRate) {
        this.billId = billId;
        this.booking = booking;
        this.lineItems = new ArrayList<>();
        this.baseTotal = 0.0;
        this.taxRate = taxRate;
        this.tipAmount = 0.0;
        this.splitCount = 1;
    }

    public String getBillId() {
        return billId;
    }

    public Booking getBooking() {
        return booking;
    }

    public List<BillItem> getLineItems() {
        return lineItems;
    }

    public double getBaseTotal() {
        return baseTotal;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public double getTipAmount() {
        return tipAmount;
    }

    public void setTipAmount(double tipAmount) {
        this.tipAmount = tipAmount;
    }

    public int getSplitCount() {
        return splitCount;
    }

    public void setSplitCount(int splitCount) {
        if (splitCount > 0) {
            this.splitCount = splitCount;
        }
    }

    /**
     * Appends a newline item to the invoice and updates base total.
     *
     * @param item The bill line item to add.
     */
    public void addLineItem(BillItem item) {
        lineItems.add(item);
        baseTotal += item.getAmount();
    }

    /**
     * Calculates the tax on the subtotal.
     *
     * @return Tax amount.
     */
    public double getTaxAmount() {
        return baseTotal * taxRate;
    }

    /**
     * Calculates final total including taxes and tips.
     *
     * @return Full billing total.
     */
    public double getFinalTotal() {
        return baseTotal + getTaxAmount() + tipAmount;
    }

    /**
     * Divides final total among the designated number of guests.
     *
     * @return Amount per individual.
     */
    public double getSplitAmount() {
        return getFinalTotal() / splitCount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("==================================================\n");
        sb.append(String.format("                STAYEASE HOTEL BILL - %s\n", billId));
        sb.append("==================================================\n");
        sb.append(String.format("Booking ID  : %s\n", booking.getBookingId()));
        sb.append(String.format("Guest Name  : %s\n", booking.getCustomer().getName()));
        sb.append(String.format("Room No     : %s (%s)\n", booking.getRoom().getRoomNumber(), booking.getRoom().getRoomType()));
        sb.append(String.format("Nights Stay : %d\n", booking.getNumberOfNights()));
        sb.append("--------------------------------------------------\n");
        for (BillItem item : lineItems) {
            sb.append(item.toString()).append("\n");
        }
        sb.append("--------------------------------------------------\n");
        sb.append(String.format("%-40s $%10.2f\n", "Subtotal:", baseTotal));
        sb.append(String.format("%-40s $%10.2f\n", "Tax (" + (taxRate * 100) + "%):", getTaxAmount()));
        sb.append(String.format("%-40s $%10.2f\n", "Optional Tip:", tipAmount));
        sb.append("==================================================\n");
        sb.append(String.format("%-40s $%10.2f\n", "FINAL TOTAL:", getFinalTotal()));
        if (splitCount > 1) {
            sb.append(String.format("%-40s $%10.2f\n", "Split Amount (" + splitCount + " ways):", getSplitAmount()));
        }
        sb.append("==================================================\n");
        return sb.toString();
    }
}
