package services;

import models.*;
import singleton.BookingHistoryLog;
import strategy.PricingStrategy;
import strategy.StandardPricing;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service class that manages itemized billing, pricing strategies, tips, and financial transaction logging.
 */
public class BillingService {
    private final Map<String, Bill> bills;
    private final BookingService bookingService;
    private PricingStrategy pricingStrategy;

    /**
     * Constructor for BillingService.
     * @param bookingService The BookingService instance to fetch booking information.
     */
    public BillingService(BookingService bookingService) {
        this.bills = new ConcurrentHashMap<>();
        this.bookingService = bookingService;
        this.pricingStrategy = new StandardPricing(); // Default to Standard Pricing
    }

    /**
     * Sets the active pricing strategy dynamically at runtime.
     * @param strategy The concrete pricing strategy to use.
     */
    public void setPricingStrategy(PricingStrategy strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("Pricing strategy cannot be null.");
        }
        this.pricingStrategy = strategy;
    }

    /**
     * Gets the current pricing strategy.
     * @return The active strategy.
     */
    public PricingStrategy getPricingStrategy() {
        return pricingStrategy;
    }

    /**
     * Generates a new bill for a booking.
     *
     * @param billId The unique identifier for the bill.
     * @param bookingId The booking to bill.
     * @param tip Gratuity left by the guest.
     * @return The generated Bill.
     */
    public Bill generateBill(String billId, String bookingId, double tip) {
        if (billId == null || billId.trim().isEmpty()) {
            throw new IllegalArgumentException("Bill ID cannot be empty.");
        }
        if (bills.containsKey(billId)) {
            throw new IllegalArgumentException("Duplicate bill error: ID '" + billId + "' already exists.");
        }
        if (tip < 0) {
            throw new IllegalArgumentException("Tip cannot be negative.");
        }

        Booking booking = bookingService.getBooking(bookingId);
        if (booking == null) {
            throw new IllegalArgumentException("Booking ID '" + bookingId + "' does not exist.");
        }

        if (booking.getRoom().getStatus() != RoomStatus.AWAITING_BILL) {
            throw new IllegalArgumentException("Invalid bill generation: Room is in " + booking.getRoom().getStatus() + " status, not AWAITING_BILL.");
        }

        Bill bill = new Bill(billId, booking, pricingStrategy, tip);
        bills.put(billId, bill);
        return bill;
    }

    /**
     * Records payment for a bill and transitions states.
     * Transition: Room status goes from AWAITING_BILL -> CLEARED.
     * Additionally logs the finalized transaction into the BookingHistoryLog singleton.
     *
     * @param billId The ID of the bill being paid.
     * @param staffId The ID of the staff receiving the payment.
     */
    public void processPayment(String billId, String staffId) {
        Bill bill = bills.get(billId);
        if (bill == null) {
            throw new IllegalArgumentException("Bill ID '" + billId + "' does not exist.");
        }
        if (bill.isPaid()) {
            throw new IllegalArgumentException("This bill has already been paid.");
        }

        Staff staff = bookingService.getStaff(staffId);
        if (staff == null) {
            throw new IllegalArgumentException("Staff ID '" + staffId + "' does not exist.");
        }

        if (!staff.hasPermission("MANAGE_BILLING")) {
            throw new SecurityException("Access Denied: " + staff + " does not have permission to process payments.");
        }

        // Mark paid
        bill.setPaid(true);

        // Transition Room: AWAITING_BILL -> CLEARED
        Room room = bill.getBooking().getRoom();
        room.setStatus(RoomStatus.CLEARED);

        // Prepare items list for history log entry
        List<String> itemsHistory = new ArrayList<>();
        itemsHistory.add("Room Base: " + room.getRoomType());
        for (BillItem item : bill.getBillItems()) {
            if (!item.getName().startsWith("Room Charge")) {
                itemsHistory.add(item.getName() + " x " + item.getQuantity());
            }
        }

        // Add Log Entry to Singleton
        BookingHistoryLog.LogEntry entry = new BookingHistoryLog.LogEntry(
                bill.getBooking().getBookingId(),
                room.getRoomNumber(),
                staff.getId(),
                itemsHistory,
                LocalDateTime.now(),
                bill.getFinalTotal()
        );
        BookingHistoryLog.getInstance().addEntry(entry);
    }

    public Bill getBill(String billId) {
        return bills.get(billId);
    }

    public List<Bill> getAllBills() {
        return new ArrayList<>(bills.values());
    }

    /**
     * Calculates the total revenue earned across all completed (paid) bills.
     * Also integrates with BookingHistoryLog to provide overall revenue stats.
     * @return Sum of totals of paid bills + logged history totals.
     */
    public double calculateTotalRevenue() {
        double total = 0.0;
        // Total from active paid bills in memory
        for (Bill bill : bills.values()) {
            if (bill.isPaid()) {
                total += bill.getFinalTotal();
            }
        }
        // Add historical entries from the Singleton log (excluding any duplicates already in active memory)
        Set<String> activePaidIds = new HashSet<>();
        for (Bill b : bills.values()) {
            if (b.isPaid()) {
                activePaidIds.add(b.getBooking().getBookingId());
            }
        }

        for (BookingHistoryLog.LogEntry log : BookingHistoryLog.getInstance().getEntries()) {
            if (!activePaidIds.contains(log.getBookingId())) {
                total += log.getTotal();
            }
        }

        return total;
    }

    public void clearAllBills() {
        bills.clear();
    }
}
