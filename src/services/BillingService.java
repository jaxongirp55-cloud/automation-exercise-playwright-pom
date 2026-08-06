package services;

import models.*;
import roomitems.RoomItem;
import singleton.BookingHistoryLog;
import strategy.LoyaltyPricing;
import strategy.PricingStrategy;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class that handles invoice generation, dynamic strategy discount calculations,
 * taxes, tips, bill splitting, and logging payments in the Booking History singleton.
 */
public class BillingService {
    private final double taxRate; // e.g. 0.15 for 15% VAT

    /**
     * Constructs a BillingService.
     *
     * @param taxRate Decimal percentage tax rate (e.g., 0.15 for 15%).
     */
    public BillingService(double taxRate) {
        if (taxRate < 0) {
            throw new IllegalArgumentException("Tax rate cannot be negative.");
        }
        this.taxRate = taxRate;
    }

    /**
     * Generates an itemized Bill invoice for a Booking using a dynamic pricing strategy.
     *
     * @param bookingId       Target booking ID.
     * @param bookingService  The BookingService instance.
     * @param pricingStrategy The strategy to compute prices.
     * @return Prepared itemized Bill object.
     */
    public Bill generateBill(String bookingId, BookingService bookingService, PricingStrategy pricingStrategy) {
        Booking booking = bookingService.getBooking(bookingId);
        if (booking == null) {
            throw new IllegalArgumentException("Invalid Bill: Booking ID " + bookingId + " not found.");
        }
        if (!booking.isConfirmed() || booking.isCancelled()) {
            throw new IllegalStateException("Cannot bill a booking that is not confirmed or is cancelled.");
        }

        Bill bill = new Bill("INV-" + booking.getBookingId(), booking, taxRate);

        // 1. Calculate Lodging Subtotal
        double rawLodging = booking.getLodgingCost();
        double discountedLodging = pricingStrategy.calculateTotal(rawLodging);
        bill.addLineItem(new BillItem("Room Lodging: " + booking.getRoom().getRoomType() + " (" + booking.getNumberOfNights() + " nights @ $" + booking.getRoom().getBasePrice() + ")", rawLodging));

        // Apply lodging strategy discount line item if any discount exists
        if (pricingStrategy.getDiscountRate() > 0) {
            double discountAmount = rawLodging - discountedLodging;
            bill.addLineItem(new BillItem("  - [" + pricingStrategy.getName() + " Room Discount]", -discountAmount));
        }

        // 2. Add Room Menu Items
        double rawItemsCost = 0.0;
        for (BookingItem bookingItem : booking.getItems()) {
            double itemCost = bookingItem.getSubtotal();
            rawItemsCost += itemCost;
            bill.addLineItem(new BillItem("Menu: " + bookingItem.getItem().getName() + " (Qty: " + bookingItem.getQuantity() + ")", itemCost));
        }

        // Apply strategy discount to menu items as well if applicable
        if (pricingStrategy.getDiscountRate() > 0 && rawItemsCost > 0) {
            double discountedItemsCost = pricingStrategy.calculateTotal(rawItemsCost);
            double itemsDiscount = rawItemsCost - discountedItemsCost;
            bill.addLineItem(new BillItem("  - [" + pricingStrategy.getName() + " Menu Discount]", -itemsDiscount));
        }

        // 3. Loyalty Strategy Special: Free Drink!
        // If strategy is LoyaltyPricing and they ordered a beverage, give a 100% credit for the highest priced beverage.
        if (pricingStrategy instanceof LoyaltyPricing && rawItemsCost > 0) {
            RoomItem freeBeverage = null;
            for (BookingItem bookingItem : booking.getItems()) {
                if (bookingItem.getItem().getCategory().equalsIgnoreCase("Beverage")) {
                    if (freeBeverage == null || bookingItem.getItem().getPrice() > freeBeverage.getPrice()) {
                        freeBeverage = bookingItem.getItem();
                    }
                }
            }
            if (freeBeverage != null) {
                bill.addLineItem(new BillItem("  - [Loyalty Free Drink Credit: " + freeBeverage.getName() + "]", -freeBeverage.getPrice()));
            }
        }

        return bill;
    }

    /**
     * Finalizes and registers the bill settlement.
     * Transitions room status from AWAITING_BILL to CLEARED, logs transaction
     * into Singleton History, and removes booking from active service tracking.
     *
     * @param operator       Staff member settling the invoice.
     * @param bill           The finalized bill.
     * @param roomService    The RoomService instance.
     * @param bookingService The BookingService instance.
     */
    public void settleBill(Staff operator, Bill bill, RoomService roomService, BookingService bookingService) {
        if (!operator.canManageBilling()) {
            throw new SecurityException("Access Denied: Staff ID " + operator.getStaffId() + " cannot settle billing invoices.");
        }
        if (bill == null) {
            throw new IllegalArgumentException("Bill cannot be null.");
        }

        Booking booking = bill.getBooking();
        Room room = booking.getRoom();

        if (room.getStatus() != RoomStatus.AWAITING_BILL && room.getStatus() != RoomStatus.OCCUPIED) {
            throw new IllegalStateException("Room " + room.getRoomNumber() + " is not awaiting billing. Status: " + room.getStatus());
        }

        // Perform Room State Transition to CLEARED
        room.setStatus(RoomStatus.CLEARED);

        // Prep historical items log
        List<String> itemsLogged = new ArrayList<>();
        itemsLogged.add("Lodging: " + room.getRoomType() + " x" + booking.getNumberOfNights() + " nights");
        for (BookingItem bi : booking.getItems()) {
            itemsLogged.add(bi.getItem().getName() + " (x" + bi.getQuantity() + ")");
        }

        // Register record in BookingHistoryLog Singleton
        BookingHistoryLog historyLog = BookingHistoryLog.getInstance();
        historyLog.addRecord(
                booking.getBookingId(),
                room.getRoomNumber(),
                operator.getStaffId(),
                itemsLogged,
                LocalDateTime.now(),
                bill.getFinalTotal()
        );

        // De-register from active bookings
        bookingService.removeActiveBooking(booking);
    }
}
