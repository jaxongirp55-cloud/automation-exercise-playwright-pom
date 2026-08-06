package utils;

import command.*;
import models.*;
import roomitems.RoomItem;
import services.*;
import singleton.BookingHistoryLog;
import strategy.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Driver console menu interface that presents options and handles
 * the workflow logic for StayEase Hotel Management System (SRMS).
 */
public class ConsoleMenu {
    private final RoomService roomService;
    private final BookingService bookingService;
    private final BillingService billingService;
    private final ReceptionQueue receptionQueue;
    private final Scanner scanner;

    /**
     * Constructor for ConsoleMenu.
     */
    public ConsoleMenu(RoomService roomService, BookingService bookingService,
                       BillingService billingService, ReceptionQueue receptionQueue) {
        this.roomService = roomService;
        this.bookingService = bookingService;
        this.billingService = billingService;
        this.receptionQueue = receptionQueue;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Starts the interactive application loop.
     */
    public void start() {
        System.out.println("=================================================");
        System.out.println("  WELCOME TO STAYEASE HOTEL ROOM BOOKING SYSTEM  ");
        System.out.println("=================================================");

        // Auto-generate starting demo data to make evaluation easy
        try {
            bookingService.generateDemoData();
            System.out.println("Demo data generated successfully (20 Rooms, 15 Customers, 10 Staff, 30 Items).");
        } catch (Exception e) {
            System.out.println("Notice: Default demo data failed to auto-load. Manual generation is available.");
        }

        while (true) {
            displayMainMenu();
            int choice = InputValidator.readInteger(scanner, "Enter choice (0-9): ", 0, 9);
            System.out.println();
            switch (choice) {
                case 1:
                    handleRoomManagement();
                    break;
                case 2:
                    handleBookingManagement();
                    break;
                case 3:
                    handleReceptionQueue();
                    break;
                case 4:
                    handleBilling();
                    break;
                case 5:
                    handleBookingHistoryLog();
                    break;
                case 6:
                    handlePricingStrategy();
                    break;
                case 7:
                    handleStaffManagement();
                    break;
                case 8:
                    handleReports();
                    break;
                case 9:
                    handleGenerateDemoData();
                    break;
                case 0:
                    System.out.println("Thank you for using StayEase Hotel Room Booking Management System. Goodbye!");
                    return;
            }
        }
    }

    private void displayMainMenu() {
        System.out.println("\n==================================");
        System.out.println("StayEase Hotel Management System  ");
        System.out.println("==================================");
        System.out.println("1 Room Management");
        System.out.println("2 Booking Management");
        System.out.println("3 Reception Queue");
        System.out.println("4 Billing");
        System.out.println("5 Booking History");
        System.out.println("6 Pricing Strategy");
        System.out.println("7 Staff Management");
        System.out.println("8 Reports");
        System.out.println("9 Generate Demo Data");
        System.out.println("0 Exit");
        System.out.println("==================================");
    }

    // --- 1. ROOM MANAGEMENT ---
    private void handleRoomManagement() {
        while (true) {
            System.out.println("\n--- Room Management Submenu ---");
            System.out.println("1. Create/Add Room");
            System.out.println("2. Display All Rooms");
            System.out.println("3. Housekeeping: Clear Room");
            System.out.println("4. Return to Main Menu");
            int choice = InputValidator.readInteger(scanner, "Enter option (1-4): ", 1, 4);
            System.out.println();

            switch (choice) {
                case 1:
                    executeCreateRoom();
                    break;
                case 2:
                    displayAllRooms();
                    break;
                case 3:
                    executeClearRoom();
                    break;
                case 4:
                    return;
            }
        }
    }

    private void executeCreateRoom() {
        System.out.println("--- Create New Room ---");
        String staffId = InputValidator.readString(scanner, "Enter Staff ID authorizing creation: ");
        Staff staff = bookingService.getStaff(staffId);
        if (staff == null) {
            System.out.println("Error: Invalid Staff ID. Authorized staff member needed.");
            return;
        }
        if (!staff.hasPermission("CREATE_ROOM")) {
            System.out.println("Access Denied: Staff role '" + staff.getRole() + "' does not have permission CREATE_ROOM.");
            return;
        }

        int number = InputValidator.readInteger(scanner, "Enter Room Number: ", 1, 9999);
        String type = InputValidator.readString(scanner, "Enter Room Type (e.g., Standard Single, Deluxe Double, Executive Suite): ");
        double rate = InputValidator.readDouble(scanner, "Enter Base Room Price per Night ($): ", 0, 100000);

        try {
            Room r = roomService.createRoom(number, type, rate);
            System.out.println("Success! Created room successfully: " + r);
        } catch (Exception e) {
            System.out.println("Validation Error: " + e.getMessage());
        }
    }

    private void displayAllRooms() {
        System.out.println("--- Hotel Room Grid Status ---");
        List<Room> list = roomService.getAllRooms();
        if (list.isEmpty()) {
            System.out.println("No rooms registered in StayEase system yet.");
            return;
        }
        for (Room r : list) {
            System.out.println(r);
        }
    }

    private void executeClearRoom() {
        System.out.println("--- Housekeeping: Clear Room ---");
        String staffId = InputValidator.readString(scanner, "Enter Staff ID performing clearance: ");
        Staff staff = bookingService.getStaff(staffId);
        if (staff == null) {
            System.out.println("Error: Staff ID not found.");
            return;
        }
        if (!staff.hasPermission("CLEAR_ROOM")) {
            System.out.println("Access Denied: Staff role '" + staff.getRole() + "' does not have permission CLEAR_ROOM.");
            return;
        }

        int number = InputValidator.readInteger(scanner, "Enter Room Number to Clear: ", 1, 9999);
        try {
            Room r = roomService.getRoom(number);
            if (r == null) {
                System.out.println("Error: Room #" + number + " does not exist.");
                return;
            }
            RoomStatus oldStatus = r.getStatus();
            roomService.clearRoom(number);
            System.out.println("Room #" + number + " cleared. Transitioned: " + oldStatus + " -> " + r.getStatus());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // --- 2. BOOKING MANAGEMENT ---
    private void handleBookingManagement() {
        while (true) {
            System.out.println("\n--- Booking Management Submenu ---");
            System.out.println("1. Create/Reserve Booking (via Command Queue)");
            System.out.println("2. Add Room Service Items to Booking");
            System.out.println("3. Modify Booking Item Quantities");
            System.out.println("4. Cancel Booking (via Command Queue)");
            System.out.println("5. Check-In Guest");
            System.out.println("6. Check-Out Guest");
            System.out.println("7. Display All Current Bookings");
            System.out.println("8. Return to Main Menu");
            int choice = InputValidator.readInteger(scanner, "Enter option (1-8): ", 1, 8);
            System.out.println();

            switch (choice) {
                case 1:
                    executeCreateBookingCommand();
                    break;
                case 2:
                    executeAddRoomItems();
                    break;
                case 3:
                    executeModifyBookingItems();
                    break;
                case 4:
                    executeCancelBookingCommand();
                    break;
                case 5:
                    executeCheckIn();
                    break;
                case 6:
                    executeCheckOut();
                    break;
                case 7:
                    displayAllBookings();
                    break;
                case 8:
                    return;
            }
        }
    }

    private void executeCreateBookingCommand() {
        System.out.println("--- Prepare New Booking Request ---");
        String staffId = InputValidator.readString(scanner, "Enter Staff ID handling reservation: ");
        Staff staff = bookingService.getStaff(staffId);
        if (staff == null) {
            System.out.println("Error: Staff ID not found.");
            return;
        }
        if (!staff.hasPermission("RESERVE_ROOM")) {
            System.out.println("Access Denied: This staff role cannot reserve rooms.");
            return;
        }

        String bId = InputValidator.readString(scanner, "Enter New Booking ID (e.g. B101): ");
        if (bookingService.getBooking(bId) != null) {
            System.out.println("Error: Duplicate Booking ID! Booking " + bId + " already exists.");
            return;
        }

        int roomNo = InputValidator.readInteger(scanner, "Enter Room Number to book: ", 1, 9999);
        Room room = roomService.getRoom(roomNo);
        if (room == null) {
            System.out.println("Error: Room #" + roomNo + " does not exist.");
            return;
        }
        if (room.getStatus() != RoomStatus.FREE) {
            System.out.println("Error: Room already occupied or reserved! Status: " + room.getStatus());
            return;
        }

        String custId = InputValidator.readString(scanner, "Enter Customer ID placing the booking: ");
        Customer customer = bookingService.getCustomer(custId);
        if (customer == null) {
            System.out.println("Customer not found. Let's register a new customer first!");
            String name = InputValidator.readString(scanner, "Enter Guest Name: ");
            String email = InputValidator.readEmail(scanner, "Enter Guest Email: ");
            String phone = InputValidator.readPhone(scanner, "Enter Guest Phone: ");
            boolean loyalty = InputValidator.readInteger(scanner, "Is guest a Loyalty Member? (1=Yes, 2=No): ", 1, 2) == 1;
            customer = new Customer(custId, name, email, phone, loyalty);
            bookingService.addCustomer(customer);
            System.out.println("Customer registered: " + customer);
        }

        // Create booking object
        Booking booking = new Booking(bId, room, customer, staff);

        // Wrap in PrepareBookingCommand
        Command cmd = new PrepareBookingCommand(bookingService, booking);

        // Enqueue to command queue
        receptionQueue.enqueue(cmd);
        System.out.println("Success! Your booking request has been added to the Reception Queue.");
        System.out.println("Command description: " + cmd.getDescription());
        System.out.println("Please remember to 'Process Next Command' in the 'Reception Queue' submenu to finalize it.");
    }

    private void executeAddRoomItems() {
        System.out.println("--- Add Room Service Items ---");
        String bId = InputValidator.readString(scanner, "Enter Booking ID: ");
        Booking booking = bookingService.getBooking(bId);
        if (booking == null) {
            System.out.println("Error: Booking not found.");
            return;
        }

        // Display Menu Items
        System.out.println("\n=== Available Room Service Menu ===");
        List<RoomItem> items = bookingService.getMenuItems();
        if (items.isEmpty()) {
            System.out.println("No menu items registered.");
            return;
        }
        for (int i = 0; i < items.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, items.get(i));
        }

        int itemChoice = InputValidator.readInteger(scanner, "Select menu item (number): ", 1, items.size());
        int qty = InputValidator.readInteger(scanner, "Enter quantity: ", 1, 100);

        RoomItem selectedItem = items.get(itemChoice - 1);
        try {
            bookingService.addItemToBooking(bId, selectedItem.getName(), qty);
            System.out.printf("Success! Added %d x %s to Booking #%s.%n", qty, selectedItem.getName(), bId);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void executeModifyBookingItems() {
        System.out.println("--- Modify Booking Ordered Items ---");
        String bId = InputValidator.readString(scanner, "Enter Booking ID: ");
        Booking booking = bookingService.getBooking(bId);
        if (booking == null) {
            System.out.println("Error: Booking not found.");
            return;
        }

        List<BookingItem> items = booking.getBookingItems();
        if (items.isEmpty()) {
            System.out.println("No items have been ordered for this booking yet.");
            return;
        }

        System.out.println("\nOrdered items for Booking #" + bId + ":");
        for (int i = 0; i < items.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, items.get(i));
        }

        int itemChoice = InputValidator.readInteger(scanner, "Select item to modify (number): ", 1, items.size());
        int newQty = InputValidator.readInteger(scanner, "Enter new quantity (Enter 0 to completely delete/remove): ", 0, 100);

        BookingItem targetItem = items.get(itemChoice - 1);
        try {
            bookingService.updateBookingItemQuantity(bId, targetItem.getRoomItem().getName(), newQty);
            if (newQty == 0) {
                System.out.println("Success! Removed item '" + targetItem.getRoomItem().getName() + "' from order.");
            } else {
                System.out.println("Success! Updated quantity to " + newQty + " for '" + targetItem.getRoomItem().getName() + "'.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void executeCancelBookingCommand() {
        System.out.println("--- Prepare Cancel Booking Request ---");
        String staffId = InputValidator.readString(scanner, "Enter Staff ID authorizing cancellation: ");
        Staff staff = bookingService.getStaff(staffId);
        if (staff == null) {
            System.out.println("Error: Staff ID not found.");
            return;
        }
        if (!staff.hasPermission("MANAGE_BOOKINGS")) {
            System.out.println("Access Denied: Authorized staff member needed.");
            return;
        }

        String bId = InputValidator.readString(scanner, "Enter Booking ID to cancel: ");
        Booking booking = bookingService.getBooking(bId);
        if (booking == null) {
            System.out.println("Error: Booking ID '" + bId + "' does not exist.");
            return;
        }

        // Wrap in CancelBookingCommand
        Command cmd = new CancelBookingCommand(bookingService, booking);

        // Enqueue to command queue
        receptionQueue.enqueue(cmd);
        System.out.println("Success! Cancellation request has been enqueued to the Reception Queue.");
        System.out.println("Command description: " + cmd.getDescription());
        System.out.println("Please remember to 'Process Next Command' in the 'Reception Queue' submenu to execute it.");
    }

    private void executeCheckIn() {
        System.out.println("--- Guest Check-In ---");
        String staffId = InputValidator.readString(scanner, "Enter Staff ID executing check-in: ");
        String bId = InputValidator.readString(scanner, "Enter Booking ID: ");
        try {
            bookingService.checkIn(bId, staffId);
            System.out.println("Success! Guest checked-in. Room status transitioned to OCCUPIED.");
        } catch (Exception e) {
            System.out.println("Check-in Error: " + e.getMessage());
        }
    }

    private void executeCheckOut() {
        System.out.println("--- Guest Check-Out ---");
        String staffId = InputValidator.readString(scanner, "Enter Staff ID executing check-out: ");
        String bId = InputValidator.readString(scanner, "Enter Booking ID: ");
        try {
            bookingService.checkOut(bId, staffId);
            System.out.println("Success! Guest checked-out. Room status transitioned to AWAITING_BILL.");
        } catch (Exception e) {
            System.out.println("Check-out Error: " + e.getMessage());
        }
    }

    private void displayAllBookings() {
        System.out.println("--- Current Active Bookings ---");
        List<Booking> list = bookingService.getAllBookings();
        if (list.isEmpty()) {
            System.out.println("No active bookings recorded.");
            return;
        }
        for (Booking b : list) {
            System.out.println(b);
        }
    }

    // --- 3. RECEPTION QUEUE ---
    private void handleReceptionQueue() {
        while (true) {
            System.out.println("\n--- Reception Queue Submenu (Command Pattern) ---");
            System.out.println("1. Display Pending Command Queue");
            System.out.println("2. Process/Execute Next Command");
            System.out.println("3. Undo Last Executed Command");
            System.out.println("4. Display Command History");
            System.out.println("5. Return to Main Menu");
            int choice = InputValidator.readInteger(scanner, "Enter option (1-5): ", 1, 5);
            System.out.println();

            switch (choice) {
                case 1:
                    displayPendingQueue();
                    break;
                case 2:
                    executeProcessNextQueue();
                    break;
                case 3:
                    executeUndoLastQueue();
                    break;
                case 4:
                    displayCommandHistory();
                    break;
                case 5:
                    return;
            }
        }
    }

    private void displayPendingQueue() {
        System.out.println("--- Pending Reception Queue Commands ---");
        List<Command> pending = receptionQueue.getPendingCommands();
        if (pending.isEmpty()) {
            System.out.println("Queue is empty. No pending reservation or cancellation commands.");
            return;
        }
        int index = 1;
        for (Command cmd : pending) {
            System.out.printf("[%d] %s%n", index++, cmd.getDescription());
        }
    }

    private void executeProcessNextQueue() {
        System.out.println("--- Processing Next Command ---");
        boolean success = receptionQueue.processNext();
        if (success) {
            System.out.println("Success! The command at head of queue has been executed.");
        } else {
            System.out.println("Notice: No pending commands in the queue to process.");
        }
    }

    private void executeUndoLastQueue() {
        System.out.println("--- Undoing Last Executed Command ---");
        boolean success = receptionQueue.undoLast();
        if (success) {
            System.out.println("Success! The last executed command has been successfully undone.");
        } else {
            System.out.println("Notice: No executed command history available to undo.");
        }
    }

    private void displayCommandHistory() {
        System.out.println("--- Command Execution History ---");
        List<Command> history = receptionQueue.getHistory();
        if (history.isEmpty()) {
            System.out.println("No commands have been processed yet.");
            return;
        }
        int index = 1;
        for (Command cmd : history) {
            System.out.printf("[%d] %s (Processed)%n", index++, cmd.getDescription());
        }
    }

    // --- 4. BILLING ---
    private void handleBilling() {
        while (true) {
            System.out.println("\n--- Billing Submenu ---");
            System.out.println("1. Generate Itemized Bill");
            System.out.println("2. Handle Tips on Bill");
            System.out.println("3. Split Bill");
            System.out.println("4. Settle / Process Payment");
            System.out.println("5. Display Bill Details / Receipt");
            System.out.println("6. Return to Main Menu");
            int choice = InputValidator.readInteger(scanner, "Enter option (1-6): ", 1, 6);
            System.out.println();

            switch (choice) {
                case 1:
                    executeGenerateBill();
                    break;
                case 2:
                    executeHandleTip();
                    break;
                case 3:
                    executeSplitBill();
                    break;
                case 4:
                    executeProcessPayment();
                    break;
                case 5:
                    executeDisplayBill();
                    break;
                case 6:
                    return;
            }
        }
    }

    private void executeGenerateBill() {
        System.out.println("--- Generate Itemized Bill ---");
        String billId = InputValidator.readString(scanner, "Enter New Bill ID (e.g. BILL501): ");
        if (billingService.getBill(billId) != null) {
            System.out.println("Error: Duplicate Bill ID '" + billId + "'.");
            return;
        }

        String bId = InputValidator.readString(scanner, "Enter Booking ID (Room must be AWAITING_BILL): ");
        double tip = InputValidator.readDouble(scanner, "Enter custom tip amount ($) (Enter 0 for none): ", 0, 100000);

        try {
            Bill b = billingService.generateBill(billId, bId, tip);
            System.out.println("Success! Generated itemized bill using pricing strategy: " + billingService.getPricingStrategy().getName());
            System.out.println(b.generateReceipt());
        } catch (Exception e) {
            System.out.println("Billing Error: " + e.getMessage());
        }
    }

    private void executeHandleTip() {
        System.out.println("--- Add/Edit Gratuity Tip ---");
        String billId = InputValidator.readString(scanner, "Enter Bill ID: ");
        Bill bill = billingService.getBill(billId);
        if (bill == null) {
            System.out.println("Error: Bill ID '" + billId + "' does not exist.");
            return;
        }
        if (bill.isPaid()) {
            System.out.println("Error: Cannot adjust tip. This bill is already finalized and paid.");
            return;
        }

        double tip = InputValidator.readDouble(scanner, "Enter new tip amount ($): ", 0, 100000);
        bill.setTip(tip);
        System.out.println("Success! Tip updated. New Grand Total: $" + String.format("%.2f", bill.getFinalTotal()));
    }

    private void executeSplitBill() {
        System.out.println("--- Split Bill ---");
        String billId = InputValidator.readString(scanner, "Enter Bill ID: ");
        Bill bill = billingService.getBill(billId);
        if (bill == null) {
            System.out.println("Error: Bill not found.");
            return;
        }

        int people = InputValidator.readInteger(scanner, "Number of guests splitting: ", 1, 100);
        try {
            List<Double> splits = bill.splitBill(people);
            System.out.printf("Total Amount to split: $%.2f%n", bill.getFinalTotal());
            System.out.printf("Divided equally among %d parties:%n", people);
            for (int i = 0; i < splits.size(); i++) {
                System.out.printf("  Party %d: $%.2f%n", i + 1, splits.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void executeProcessPayment() {
        System.out.println("--- Settle / Process Payment ---");
        String billId = InputValidator.readString(scanner, "Enter Bill ID: ");
        String staffId = InputValidator.readString(scanner, "Enter Staff ID processing settlement: ");
        try {
            billingService.processPayment(billId, staffId);
            System.out.println("Success! Bill paid in full. Room marked CLEARED. History entry logged.");
        } catch (Exception e) {
            System.out.println("Payment Processing Error: " + e.getMessage());
        }
    }

    private void executeDisplayBill() {
        System.out.println("--- Display Bill details ---");
        String billId = InputValidator.readString(scanner, "Enter Bill ID: ");
        Bill b = billingService.getBill(billId);
        if (b == null) {
            System.out.println("Error: Bill not found.");
            return;
        }
        System.out.println(b.generateReceipt());
    }

    // --- 5. BOOKING HISTORY ---
    private void handleBookingHistoryLog() {
        while (true) {
            System.out.println("\n--- Booking History Submenu ---");
            System.out.println("1. Search Logs by Room Number");
            System.out.println("2. Search Logs by Date");
            System.out.println("3. Show Most Ordered Item in History");
            System.out.println("4. Display All Completed Logs");
            System.out.println("5. Return to Main Menu");
            int choice = InputValidator.readInteger(scanner, "Enter option (1-5): ", 1, 5);
            System.out.println();

            switch (choice) {
                case 1:
                    executeSearchByRoom();
                    break;
                case 2:
                    executeSearchByDate();
                    break;
                case 3:
                    executeShowMostOrderedItem();
                    break;
                case 4:
                    displayAllHistoryLogs();
                    break;
                case 5:
                    return;
            }
        }
    }

    private void executeSearchByRoom() {
        System.out.println("--- Search Completed Logs by Room ---");
        int num = InputValidator.readInteger(scanner, "Enter Room Number: ", 1, 9999);
        List<BookingHistoryLog.LogEntry> results = BookingHistoryLog.getInstance().searchByRoom(num);
        if (results.isEmpty()) {
            System.out.println("No historical records found for Room #" + num);
        } else {
            System.out.printf("Found %d completed entries for Room #%d:%n", results.size(), num);
            for (BookingHistoryLog.LogEntry e : results) {
                System.out.println(e);
            }
        }
    }

    private void executeSearchByDate() {
        System.out.println("--- Search Completed Logs by Date ---");
        String dateString = InputValidator.readString(scanner, "Enter date (YYYY-MM-DD): ");
        try {
            LocalDate date = LocalDate.parse(dateString);
            List<BookingHistoryLog.LogEntry> results = BookingHistoryLog.getInstance().searchByDate(date);
            if (results.isEmpty()) {
                System.out.println("No historical records found on date: " + date);
            } else {
                System.out.printf("Found %d completed entries on date %s:%n", results.size(), date);
                for (BookingHistoryLog.LogEntry e : results) {
                    System.out.println(e);
                }
            }
        } catch (DateTimeParseException e) {
            System.out.println("Invalid format. Please enter date as YYYY-MM-DD.");
        }
    }

    private void executeShowMostOrderedItem() {
        System.out.println("--- Most Ordered Menu Item in History ---");
        String item = BookingHistoryLog.getInstance().getMostOrderedItem();
        System.out.println("Result: " + item);
    }

    private void displayAllHistoryLogs() {
        System.out.println("--- Historical Transaction Log (Singleton Store) ---");
        List<BookingHistoryLog.LogEntry> logs = BookingHistoryLog.getInstance().getEntries();
        if (logs.isEmpty()) {
            System.out.println("No historical bookings logged.");
            return;
        }
        for (BookingHistoryLog.LogEntry e : logs) {
            System.out.println(e);
        }
    }

    // --- 6. PRICING STRATEGY ---
    private void handlePricingStrategy() {
        while (true) {
            System.out.println("\n--- Pricing Strategy Submenu (Strategy Pattern) ---");
            System.out.println("1. Standard Pricing (No Discount)");
            System.out.println("2. Happy Hour Pricing (20% Discount)");
            System.out.println("3. Loyalty Pricing (10% Discount + Free Drink for Loyalty Members)");
            System.out.println("4. Display Current Active Strategy");
            System.out.println("5. Return to Main Menu");
            int choice = InputValidator.readInteger(scanner, "Enter option (1-5): ", 1, 5);
            System.out.println();

            switch (choice) {
                case 1:
                    billingService.setPricingStrategy(new StandardPricing());
                    System.out.println("Strategy changed successfully: " + billingService.getPricingStrategy().getName());
                    break;
                case 2:
                    billingService.setPricingStrategy(new HappyHourPricing());
                    System.out.println("Strategy changed successfully: " + billingService.getPricingStrategy().getName());
                    break;
                case 3:
                    billingService.setPricingStrategy(new LoyaltyPricing());
                    System.out.println("Strategy changed successfully: " + billingService.getPricingStrategy().getName());
                    break;
                case 4:
                    System.out.println("Current active pricing strategy is: " + billingService.getPricingStrategy().getName());
                    break;
                case 5:
                    return;
            }
        }
    }

    // --- 7. STAFF MANAGEMENT ---
    private void handleStaffManagement() {
        while (true) {
            System.out.println("\n--- Staff Management Submenu ---");
            System.out.println("1. Create/Add New Staff Member");
            System.out.println("2. List All Active Staff");
            System.out.println("3. Return to Main Menu");
            int choice = InputValidator.readInteger(scanner, "Enter option (1-3): ", 1, 3);
            System.out.println();

            switch (choice) {
                case 1:
                    executeAddStaff();
                    break;
                case 2:
                    displayAllStaff();
                    break;
                case 3:
                    return;
            }
        }
    }

    private void executeAddStaff() {
        System.out.println("--- Add New Staff Member ---");
        String id = InputValidator.readString(scanner, "Enter Employee ID (e.g., S11): ");
        if (bookingService.getStaff(id) != null) {
            System.out.println("Error: Duplicate staff ID '" + id + "' already exists.");
            return;
        }

        String name = InputValidator.readString(scanner, "Enter Employee Name: ");
        System.out.println("Select Role:");
        System.out.println("1. Manager");
        System.out.println("2. Receptionist");
        System.out.println("3. Front Desk");
        System.out.println("4. Head Housekeeping");
        int roleChoice = InputValidator.readInteger(scanner, "Enter choice (1-4): ", 1, 4);

        Staff staff = null;
        switch (roleChoice) {
            case 1:
                staff = new Manager(id, name);
                break;
            case 2:
                staff = new Receptionist(id, name);
                break;
            case 3:
                staff = new FrontDesk(id, name);
                break;
            case 4:
                staff = new HeadHousekeeping(id, name);
                break;
        }

        try {
            bookingService.addStaff(staff);
            System.out.println("Success! Staff member created and added to database: " + staff);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void displayAllStaff() {
        System.out.println("--- Hotel Active Staff list ---");
        List<Staff> list = bookingService.getAllStaff();
        if (list.isEmpty()) {
            System.out.println("No staff members registered.");
            return;
        }
        for (Staff s : list) {
            System.out.println(s);
        }
    }

    // --- 8. REPORTS ---
    private void handleReports() {
        while (true) {
            System.out.println("\n--- Administrative Reports Submenu ---");
            System.out.println("1. Room Status Report");
            System.out.println("2. Booking History Report");
            System.out.println("3. Revenue Report");
            System.out.println("4. Top Ordered Item");
            System.out.println("5. Customer Report");
            System.out.println("6. Return to Main Menu");
            int choice = InputValidator.readInteger(scanner, "Enter option (1-6): ", 1, 6);
            System.out.println();

            switch (choice) {
                case 1:
                    displayRoomStatusReport();
                    break;
                case 2:
                    displayAllHistoryLogs(); // Reuse the log display
                    break;
                case 3:
                    displayRevenueReport();
                    break;
                case 4:
                    executeShowMostOrderedItem(); // Reuse most ordered item calculation
                    break;
                case 5:
                    displayCustomerReport();
                    break;
                case 6:
                    return;
            }
        }
    }

    private void displayRoomStatusReport() {
        System.out.println("--- Room Occupancy & Status Report ---");
        List<Room> rooms = roomService.getAllRooms();
        if (rooms.isEmpty()) {
            System.out.println("No rooms registered.");
            return;
        }

        int free = 0, reserved = 0, occupied = 0, awaiting = 0, cleared = 0;
        for (Room r : rooms) {
            switch (r.getStatus()) {
                case FREE: free++; break;
                case RESERVED: reserved++; break;
                case OCCUPIED: occupied++; break;
                case AWAITING_BILL: awaiting++; break;
                case CLEARED: cleared++; break;
            }
        }

        System.out.printf("Total Rooms: %d%n", rooms.size());
        System.out.printf("  FREE          : %d%n", free);
        System.out.printf("  RESERVED      : %d%n", reserved);
        System.out.printf("  OCCUPIED      : %d%n", occupied);
        System.out.printf("  AWAITING_BILL : %d%n", awaiting);
        System.out.printf("  CLEARED       : %d%n", cleared);
        System.out.println("-------------------------");
    }

    private void displayRevenueReport() {
        System.out.println("--- Revenue Audit Report ---");
        double activeRev = 0.0;
        int activePaidCount = 0;
        for (Bill b : billingService.getAllBills()) {
            if (b.isPaid()) {
                activeRev += b.getFinalTotal();
                activePaidCount++;
            }
        }

        double historyRev = 0.0;
        List<BookingHistoryLog.LogEntry> history = BookingHistoryLog.getInstance().getEntries();
        for (BookingHistoryLog.LogEntry e : history) {
            historyRev += e.getTotal();
        }

        double totalRevenue = billingService.calculateTotalRevenue();

        System.out.printf("Total Settle Invoices (In-Memory)  : %d | Revenue: $%.2f%n", activePaidCount, activeRev);
        System.out.printf("Historical Completed Bookings (Log) : %d | Revenue: $%.2f%n", history.size(), historyRev);
        System.out.println("------------------------------------------------------------------");
        System.out.printf("TOTAL SYSTEM COLLECTED REVENUE     : $%.2f%n", totalRevenue);
        System.out.println("------------------------------------------------------------------");
    }

    private void displayCustomerReport() {
        System.out.println("--- Customer Account Audit Report ---");
        List<Customer> list = bookingService.getAllCustomers();
        if (list.isEmpty()) {
            System.out.println("No customers registered in database.");
            return;
        }
        System.out.printf("%-6s %-20s %-25s %-12s %s%n", "ID", "Name", "Email", "Phone", "Loyalty Status");
        System.out.println("------------------------------------------------------------------------------");
        for (Customer c : list) {
            System.out.printf("%-6s %-20s %-25s %-12s %s%n",
                    c.getId(), c.getName(), c.getEmail(), c.getPhone(), c.isLoyaltyMember() ? "LOYALTY MEMBER" : "STANDARD");
        }
    }

    // --- 9. GENERATE DEMO DATA ---
    private void handleGenerateDemoData() {
        System.out.println("--- Re-generating Database Demo Data ---");
        try {
            bookingService.generateDemoData();
            System.out.println("Success! All previous in-memory state has been reset.");
            System.out.println("Pre-loaded:");
            System.out.println(" - 20 Rooms (Standard Single #101-#110, Deluxe Double #111-#115, Executive Suite #116-#120)");
            System.out.println(" - 15 Customers (with realistic details)");
            System.out.println(" - 10 Staff Members (Manager Connor, 4 Receptionists, 3 Front Desk, 2 Housekeeping)");
            System.out.println(" - 30 Room Service Menu Items (Starters, Mains, Desserts, Drinks, Combos)");
            System.out.println(" - 20 Bookings pre-processed (12 already completed and written to historical log)");
        } catch (Exception e) {
            System.out.println("Error generating demo data: " + e.getMessage());
        }
    }
}
