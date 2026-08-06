package utils;

import command.*;
import factory.RoomItemFactory;
import models.*;
import roomitems.RoomItem;
import services.*;
import singleton.BookingHistoryLog;
import strategy.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Console Menu handler representing the Presentation and Interaction layer.
 * Coordinates system services, validators, and command/strategy behaviors.
 */
public class ConsoleMenu {
    private final RoomService roomService;
    private final BookingService bookingService;
    private final BillingService billingService;
    private final ReceptionQueue receptionQueue;
    private final List<Customer> customers;
    private final List<Staff> staffMembers;
    private final List<RoomItem> menuItems;
    private PricingStrategy currentStrategy;
    private Staff currentOperator;

    /**
     * Constructs a ConsoleMenu interface.
     */
    public ConsoleMenu(RoomService roomService, BookingService bookingService, BillingService billingService) {
        this.roomService = roomService;
        this.bookingService = bookingService;
        this.billingService = billingService;
        this.receptionQueue = new ReceptionQueue();
        this.customers = new ArrayList<>();
        this.staffMembers = new ArrayList<>();
        this.menuItems = new ArrayList<>();
        this.currentStrategy = new StandardPricing();
        this.currentOperator = null;
    }

    public List<Customer> getCustomers() { return customers; }
    public List<Staff> getStaffMembers() { return staffMembers; }
    public List<RoomItem> getMenuItems() { return menuItems; }
    public PricingStrategy getCurrentStrategy() { return currentStrategy; }
    public void setCurrentStrategy(PricingStrategy strategy) { this.currentStrategy = strategy; }
    public Staff getCurrentOperator() { return currentOperator; }
    public void setCurrentOperator(Staff operator) { this.currentOperator = operator; }

    /**
     * Entrypoint of the primary user loop.
     *
     * @param scanner Input Scanner.
     */
    public void run(Scanner scanner) {
        // Enforce employee login/switch initially if no operator loaded
        if (currentOperator == null && !staffMembers.isEmpty()) {
            currentOperator = staffMembers.get(0); // auto-login default Manager
        }

        while (true) {
            displayHeader();
            System.out.println(" 1  Room Management");
            System.out.println(" 2  Booking Management");
            System.out.println(" 3  Reception Queue");
            System.out.println(" 4  Billing");
            System.out.println(" 5  Booking History");
            System.out.println(" 6  Pricing Strategy");
            System.out.println(" 7  Staff Management");
            System.out.println(" 8  Reports");
            System.out.println(" 9  Generate Demo Data");
            System.out.println(" 0  Exit");
            System.out.println("==================================================");

            int choice = InputValidator.readInteger(scanner, "Enter menu choice (0-9): ", 0, 9);
            System.out.println();

            switch (choice) {
                case 1:
                    handleRoomMenu(scanner);
                    break;
                case 2:
                    handleBookingMenu(scanner);
                    break;
                case 3:
                    handleQueueMenu(scanner);
                    break;
                case 4:
                    handleBillingMenu(scanner);
                    break;
                case 5:
                    handleHistoryMenu(scanner);
                    break;
                case 6:
                    handleStrategyMenu(scanner);
                    break;
                case 7:
                    handleStaffMenu(scanner);
                    break;
                case 8:
                    handleReportsMenu(scanner);
                    break;
                case 9:
                    handleGenerateDemoData();
                    break;
                case 0:
                    System.out.println("Thank you for using StayEase Hotel Booking System. Goodbye!");
                    return;
            }
        }
    }

    private void displayHeader() {
        System.out.println("\n==================================================");
        System.out.println("      StayEase Hotel Management System (SRMS)");
        System.out.println("==================================================");
        System.out.println("Current Operator: " + (currentOperator != null ? currentOperator.toString() : "None"));
        System.out.println("Current Strategy: " + currentStrategy.getName());
        System.out.println("==================================================");
    }

    // ==========================================
    // 1. ROOM MANAGEMENT SUBMENU
    // ==========================================
    private void handleRoomMenu(Scanner scanner) {
        while (true) {
            System.out.println("--- Room Management ---");
            System.out.println("1. Create New Room");
            System.out.println("2. Reserve Room (Manual)");
            System.out.println("3. Check In Guest");
            System.out.println("4. Check Out Guest");
            System.out.println("5. Clear/Prepare Room (Housekeeping)");
            System.out.println("6. Display All Rooms");
            System.out.println("0. Back to Main Menu");
            int sub = InputValidator.readInteger(scanner, "Select option (0-6): ", 0, 6);
            System.out.println();

            try {
                switch (sub) {
                    case 1:
                        String num = InputValidator.readString(scanner, "Enter Room Number: ");
                        String type = InputValidator.readString(scanner, "Enter Room Type (e.g. Standard, Deluxe, Suite): ");
                        double price = InputValidator.readDouble(scanner, "Enter Base Room Price ($): ", 1.0, 10000.0);
                        roomService.createRoom(currentOperator, num, type, price);
                        System.out.println("Room " + num + " registered successfully.");
                        break;
                    case 2:
                        String rNum = InputValidator.readString(scanner, "Enter Room Number: ");
                        roomService.reserveRoom(rNum);
                        System.out.println("Room " + rNum + " is now RESERVED.");
                        break;
                    case 3:
                        String cNum = InputValidator.readString(scanner, "Enter Room Number to Check In: ");
                        roomService.checkInRoom(currentOperator, cNum);
                        System.out.println("Room " + cNum + " status changed to OCCUPIED. Welcome!");
                        break;
                    case 4:
                        String coNum = InputValidator.readString(scanner, "Enter Room Number to Check Out: ");
                        roomService.checkOutRoom(currentOperator, coNum);
                        System.out.println("Room " + coNum + " status changed to AWAITING_BILL. Please generate checkout invoice.");
                        break;
                    case 5:
                        String clNum = InputValidator.readString(scanner, "Enter Room Number to Clear: ");
                        roomService.clearRoom(currentOperator, clNum);
                        System.out.println("Room " + clNum + " prepped by housekeeping and returned to FREE.");
                        break;
                    case 6:
                        displayRooms();
                        break;
                    case 0:
                        return;
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            System.out.println();
        }
    }

    private void displayRooms() {
        List<Room> list = roomService.getAllRooms();
        if (list.isEmpty()) {
            System.out.println("No rooms registered in the system.");
            return;
        }
        System.out.println(String.format("%-12s %-18s %-12s %-12s", "Room Number", "Room Type", "Base Price", "Status"));
        System.out.println("-----------------------------------------------------------------");
        for (Room r : list) {
            System.out.println(String.format("%-12s %-18s $%-11.2f %-12s", r.getRoomNumber(), r.getRoomType(), r.getBasePrice(), r.getStatus()));
        }
    }

    // ==========================================
    // 2. BOOKING MANAGEMENT SUBMENU
    // ==========================================
    private void handleBookingMenu(Scanner scanner) {
        while (true) {
            System.out.println("--- Booking Management ---");
            System.out.println("1. Create New Booking");
            System.out.println("2. Add Menu/Room Items to Booking");
            System.out.println("3. Modify Booking Nights");
            System.out.println("4. Cancel Booking (Queue Command)");
            System.out.println("5. Confirm Booking Immediately");
            System.out.println("6. Display Active Bookings");
            System.out.println("0. Back to Main Menu");
            int sub = InputValidator.readInteger(scanner, "Select option (0-6): ", 0, 6);
            System.out.println();

            try {
                switch (sub) {
                    case 1:
                        handleCreateBooking(scanner);
                        break;
                    case 2:
                        handleAddItemsToBooking(scanner);
                        break;
                    case 3:
                        handleModifyBooking(scanner);
                        break;
                    case 4:
                        handleCancelBooking(scanner);
                        break;
                    case 5:
                        handleConfirmBooking(scanner);
                        break;
                    case 6:
                        displayActiveBookings();
                        break;
                    case 0:
                        return;
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            System.out.println();
        }
    }

    private void handleCreateBooking(Scanner scanner) {
        if (customers.isEmpty()) {
            System.out.println("No customers exist. Please add a customer first under Reports/Demo.");
            return;
        }
        System.out.println("Select Customer:");
        for (int i = 0; i < customers.size(); i++) {
            System.out.println((i + 1) + ". " + customers.get(i));
        }
        int custIdx = InputValidator.readInteger(scanner, "Choose customer: ", 1, customers.size()) - 1;
        Customer cust = customers.get(custIdx);

        List<Room> freeRooms = new ArrayList<>();
        for (Room r : roomService.getAllRooms()) {
            if (r.getStatus() == RoomStatus.FREE) {
                freeRooms.add(r);
            }
        }
        if (freeRooms.isEmpty()) {
            System.out.println("No FREE rooms available for booking.");
            return;
        }

        System.out.println("\nSelect Available Room:");
        for (int i = 0; i < freeRooms.size(); i++) {
            System.out.println((i + 1) + ". " + freeRooms.get(i));
        }
        int rmIdx = InputValidator.readInteger(scanner, "Choose room: ", 1, freeRooms.size()) - 1;
        Room rm = freeRooms.get(rmIdx);

        String bId = InputValidator.readString(scanner, "Enter New Booking ID (e.g. B001): ");
        int nights = InputValidator.readInteger(scanner, "Enter Number of Nights: ", 1, 365);

        Booking b = bookingService.createBooking(currentOperator, bId, cust, rm, nights);

        System.out.println("\nWould you like to enqueue this in the Reception Queue or confirm immediately?");
        System.out.println("1. Enqueue in Reception Queue (Command Pattern)");
        System.out.println("2. Confirm Immediately");
        int flow = InputValidator.readInteger(scanner, "Choice (1-2): ", 1, 2);

        if (flow == 1) {
            receptionQueue.enqueue(new PrepareBookingCommand(b, bookingService.getActiveBookings()));
            System.out.println("Booking successfully enqueued in the Reception Queue as a PrepareBookingCommand.");
        } else {
            bookingService.confirmBooking(b);
            System.out.println("Booking confirmed immediately. Room status is RESERVED.");
        }
    }

    private void handleAddItemsToBooking(Scanner scanner) {
        List<Booking> active = bookingService.getActiveBookings();
        if (active.isEmpty()) {
            System.out.println("No active bookings to add items to.");
            return;
        }
        System.out.println("Select Booking:");
        for (int i = 0; i < active.size(); i++) {
            System.out.println((i + 1) + ". " + active.get(i));
        }
        int bIdx = InputValidator.readInteger(scanner, "Choose booking: ", 1, active.size()) - 1;
        Booking b = active.get(bIdx);

        if (menuItems.isEmpty()) {
            System.out.println("No menu items registered. Please load Demo Data.");
            return;
        }

        while (true) {
            System.out.println("\nSelect Room Item/Service to Order:");
            for (int i = 0; i < menuItems.size(); i++) {
                System.out.println(String.format("%3d. %-45s", (i + 1), menuItems.get(i).toString()));
            }
            System.out.println("  0. Finish Adding Items");
            int mIdx = InputValidator.readInteger(scanner, "Select item: ", 0, menuItems.size());
            if (mIdx == 0) break;

            RoomItem item = menuItems.get(mIdx - 1);
            int qty = InputValidator.readInteger(scanner, "Quantity: ", 1, 100);

            bookingService.addItemToBooking(b, item, qty);
            System.out.println(qty + " x " + item.getName() + " added to Booking ID " + b.getBookingId() + ".");
        }
    }

    private void handleModifyBooking(Scanner scanner) {
        List<Booking> active = bookingService.getActiveBookings();
        if (active.isEmpty()) {
            System.out.println("No active bookings to modify.");
            return;
        }
        System.out.println("Select Booking:");
        for (int i = 0; i < active.size(); i++) {
            System.out.println((i + 1) + ". " + active.get(i));
        }
        int bIdx = InputValidator.readInteger(scanner, "Choose booking: ", 1, active.size()) - 1;
        Booking b = active.get(bIdx);

        int nights = InputValidator.readInteger(scanner, "Enter New Number of Nights: ", 1, 365);
        bookingService.modifyBookingNights(b, nights);
        System.out.println("Booking " + b.getBookingId() + " successfully updated to " + nights + " nights.");
    }

    private void handleCancelBooking(Scanner scanner) {
        List<Booking> active = bookingService.getActiveBookings();
        if (active.isEmpty()) {
            System.out.println("No active bookings to cancel.");
            return;
        }
        System.out.println("Select Booking to Cancel:");
        for (int i = 0; i < active.size(); i++) {
            System.out.println((i + 1) + ". " + active.get(i));
        }
        int bIdx = InputValidator.readInteger(scanner, "Choose booking: ", 1, active.size()) - 1;
        Booking b = active.get(bIdx);

        System.out.println("1. Enqueue Cancellation in Queue (Command Pattern)");
        System.out.println("2. Cancel Immediately");
        int cancelChoice = InputValidator.readInteger(scanner, "Choice (1-2): ", 1, 2);

        if (cancelChoice == 1) {
            receptionQueue.enqueue(new CancelBookingCommand(b, active));
            System.out.println("Cancellation enqueued as CancelBookingCommand.");
        } else {
            b.setCancelled(true);
            b.getRoom().setStatus(RoomStatus.FREE);
            System.out.println("Booking cancelled immediately. Room returned to FREE.");
        }
    }

    private void handleConfirmBooking(Scanner scanner) {
        List<Booking> active = bookingService.getActiveBookings();
        System.out.println("Unconfirmed Bookings:");
        // Here we can find if there are custom unconfirmed bookings
        // For simplicity, we let the user manually confirm any active booking or toggle its status.
        if (active.isEmpty()) {
            System.out.println("No bookings to confirm.");
            return;
        }
        for (int i = 0; i < active.size(); i++) {
            System.out.println((i + 1) + ". " + active.get(i));
        }
        int bIdx = InputValidator.readInteger(scanner, "Choose booking to confirm: ", 1, active.size()) - 1;
        bookingService.confirmBooking(active.get(bIdx));
        System.out.println("Booking confirmed successfully.");
    }

    private void displayActiveBookings() {
        List<Booking> active = bookingService.getActiveBookings();
        if (active.isEmpty()) {
            System.out.println("No active bookings.");
            return;
        }
        System.out.println(String.format("%-12s %-15s %-10s %-8s %-12s %-10s", "Booking ID", "Customer", "Room", "Nights", "Confirmed", "Cancelled"));
        System.out.println("-------------------------------------------------------------------------");
        for (Booking b : active) {
            System.out.println(String.format("%-12s %-15s %-10s %-8d %-12s %-10s",
                    b.getBookingId(),
                    b.getCustomer().getName(),
                    b.getRoom().getRoomNumber(),
                    b.getNumberOfNights(),
                    b.isConfirmed(),
                    b.isCancelled()));
        }
    }

    // ==========================================
    // 3. RECEPTION QUEUE SUBMENU
    // ==========================================
    private void handleQueueMenu(Scanner scanner) {
        while (true) {
            System.out.println("--- Reception Queue ---");
            System.out.println("Pending Commands in Queue: " + receptionQueue.getPendingCount());
            System.out.println("Executed Commands on Undo Stack: " + receptionQueue.getUndoCount());
            System.out.println("-------------------------------------");
            System.out.println("1. View Pending Commands");
            System.out.println("2. Process/Execute Next Command");
            System.out.println("3. Undo Last Executed Command (LIFO)");
            System.out.println("4. Clear Queue & History");
            System.out.println("0. Back to Main Menu");
            int sub = InputValidator.readInteger(scanner, "Select option (0-4): ", 0, 4);
            System.out.println();

            switch (sub) {
                case 1:
                    Command[] pending = receptionQueue.getPendingCommands();
                    if (pending.length == 0) {
                        System.out.println("The Reception Queue is empty.");
                    } else {
                        System.out.println("Pending Queue Requests (FIFO):");
                        for (int i = 0; i < pending.length; i++) {
                            System.out.println((i + 1) + ". " + pending[i].getDescription());
                        }
                    }
                    break;
                case 2:
                    String execMsg = receptionQueue.processNext();
                    if (execMsg != null) {
                        System.out.println("Success: Executed Command -> " + execMsg);
                    } else {
                        System.out.println("Queue is empty. Nothing to process.");
                    }
                    break;
                case 3:
                    String undoMsg = receptionQueue.undoLast();
                    if (undoMsg != null) {
                        System.out.println("Success: Undone Command -> " + undoMsg);
                    } else {
                        System.out.println("Undo stack is empty. No commands to roll back.");
                    }
                    break;
                case 4:
                    receptionQueue.clear();
                    System.out.println("Reception queue and undo history cleared.");
                    break;
                case 0:
                    return;
            }
            System.out.println();
        }
    }

    // ==========================================
    // 4. BILLING SUBMENU
    // ==========================================
    private void handleBillingMenu(Scanner scanner) {
        while (true) {
            System.out.println("--- Billing & Invoice Settlement ---");
            System.out.println("1. Generate Room Checkout Invoice");
            System.out.println("2. Settle Room Invoice");
            System.out.println("0. Back to Main Menu");
            int sub = InputValidator.readInteger(scanner, "Select option (0-2): ", 0, 2);
            System.out.println();

            try {
                switch (sub) {
                    case 1:
                        String bId = InputValidator.readString(scanner, "Enter Booking ID: ");
                        Bill bill = billingService.generateBill(bId, bookingService, currentStrategy);

                        double tip = InputValidator.readDouble(scanner, "Enter Optional Tip ($): ", 0.0, 10000.0);
                        bill.setTipAmount(tip);

                        boolean split = InputValidator.readBoolean(scanner, "Split bill among multiple guests? (y/n): ");
                        if (split) {
                            int ways = InputValidator.readInteger(scanner, "Number of ways to split: ", 2, 100);
                            bill.setSplitCount(ways);
                        }

                        System.out.println("\nGenerated Invoice Details:");
                        System.out.println(bill);
                        break;
                    case 2:
                        String bIdSettle = InputValidator.readString(scanner, "Enter Booking ID to settle: ");
                        Bill billSettle = billingService.generateBill(bIdSettle, bookingService, currentStrategy);

                        double tipSettle = InputValidator.readDouble(scanner, "Enter Optional Tip ($): ", 0.0, 10000.0);
                        billSettle.setTipAmount(tipSettle);

                        boolean splitS = InputValidator.readBoolean(scanner, "Split bill? (y/n): ");
                        if (splitS) {
                            int ways = InputValidator.readInteger(scanner, "Split ways: ", 2, 100);
                            billSettle.setSplitCount(ways);
                        }

                        System.out.println("\nFinal Settle Preview:");
                        System.out.println(billSettle);

                        boolean confirm = InputValidator.readBoolean(scanner, "Confirm Payment & Check out room? (y/n): ");
                        if (confirm) {
                            // First, make sure the room is transitioned to AWAITING_BILL if it was in OCCUPIED
                            Room r = billSettle.getBooking().getRoom();
                            if (r.getStatus() == RoomStatus.OCCUPIED) {
                                r.setStatus(RoomStatus.AWAITING_BILL);
                            }
                            billingService.settleBill(currentOperator, billSettle, roomService, bookingService);
                            System.out.println("Bill invoice settled. Transaction logged in Singleton History.");
                            System.out.println("Room " + r.getRoomNumber() + " is now CLEARED and needs housekeeping attention.");
                        } else {
                            System.out.println("Invoice settlement aborted.");
                        }
                        break;
                    case 0:
                        return;
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            System.out.println();
        }
    }

    // ==========================================
    // 5. BOOKING HISTORY SUBMENU
    // ==========================================
    private void handleHistoryMenu(Scanner scanner) {
        BookingHistoryLog log = BookingHistoryLog.getInstance();
        while (true) {
            System.out.println("--- Booking History Log ---");
            System.out.println("1. View All Historical Records");
            System.out.println("2. Search by Room Number");
            System.out.println("3. Search by Date (yyyy-MM-dd)");
            System.out.println("4. Display Most Ordered Room Item");
            System.out.println("0. Back to Main Menu");
            int sub = InputValidator.readInteger(scanner, "Select option (0-4): ", 0, 4);
            System.out.println();

            switch (sub) {
                case 1:
                    List<BookingHistoryLog.HistoryRecord> records = log.getAllRecords();
                    if (records.isEmpty()) {
                        System.out.println("No booking history records compiled yet.");
                    } else {
                        System.out.println("Complete Historical Invoice Log:");
                        for (BookingHistoryLog.HistoryRecord r : records) {
                            System.out.println(r);
                        }
                    }
                    break;
                case 2:
                    String rm = InputValidator.readString(scanner, "Enter Room Number: ");
                    List<BookingHistoryLog.HistoryRecord> rmRecs = log.searchByRoom(rm);
                    if (rmRecs.isEmpty()) {
                        System.out.println("No records found for room " + rm + ".");
                    } else {
                        System.out.println("Search Results for Room " + rm + ":");
                        for (BookingHistoryLog.HistoryRecord r : rmRecs) {
                            System.out.println(r);
                        }
                    }
                    break;
                case 3:
                    String date = InputValidator.readDate(scanner, "Enter Date (yyyy-MM-dd): ");
                    List<BookingHistoryLog.HistoryRecord> dateRecs = log.searchByDate(date);
                    if (dateRecs.isEmpty()) {
                        System.out.println("No records found on date " + date + ".");
                    } else {
                        System.out.println("Search Results for Date " + date + ":");
                        for (BookingHistoryLog.HistoryRecord r : dateRecs) {
                            System.out.println(r);
                        }
                    }
                    break;
                case 4:
                    System.out.println("Most Ordered Room Item metric: ");
                    System.out.println(log.getMostOrderedItem());
                    break;
                case 0:
                    return;
            }
            System.out.println();
        }
    }

    // ==========================================
    // 6. PRICING STRATEGY SUBMENU
    // ==========================================
    private void handleStrategyMenu(Scanner scanner) {
        System.out.println("--- Configure Pricing Strategy ---");
        System.out.println("Active Pricing Strategy: " + currentStrategy.getName());
        System.out.println("-------------------------------------");
        System.out.println("1. Standard Rack Pricing (No Discounts)");
        System.out.println("2. Happy Hour Pricing (20% Discount across entire invoice)");
        System.out.println("3. Loyalty Program Pricing (10% Discount + Free Drink Credit)");
        System.out.println("0. Cancel");

        int choice = InputValidator.readInteger(scanner, "Choose strategy (0-3): ", 0, 3);
        System.out.println();

        if (choice == 1) {
            currentStrategy = new StandardPricing();
            System.out.println("Pricing strategy successfully updated to standard rack rates.");
        } else if (choice == 2) {
            currentStrategy = new HappyHourPricing();
            System.out.println("Pricing strategy successfully updated to Happy Hour pricing.");
        } else if (choice == 3) {
            currentStrategy = new LoyaltyPricing();
            System.out.println("Pricing strategy successfully updated to Loyalty rewards pricing.");
        }
    }

    // ==========================================
    // 7. STAFF MANAGEMENT SUBMENU
    // ==========================================
    private void handleStaffMenu(Scanner scanner) {
        while (true) {
            System.out.println("--- Staff Management ---");
            System.out.println("Current Logged-in Operator: " + (currentOperator != null ? currentOperator.toString() : "None"));
            System.out.println("-------------------------------------");
            System.out.println("1. Switch/Log in Active Operator");
            System.out.println("2. Register New Staff Employee");
            System.out.println("3. List All Employees");
            System.out.println("0. Back to Main Menu");
            int sub = InputValidator.readInteger(scanner, "Select option (0-3): ", 0, 3);
            System.out.println();

            switch (sub) {
                case 1:
                    if (staffMembers.isEmpty()) {
                        System.out.println("No staff members exist. Please load Demo Data.");
                        break;
                    }
                    System.out.println("Choose employee to switch to:");
                    for (int i = 0; i < staffMembers.size(); i++) {
                        System.out.println((i + 1) + ". " + staffMembers.get(i));
                    }
                    int sIdx = InputValidator.readInteger(scanner, "Choice: ", 1, staffMembers.size()) - 1;
                    currentOperator = staffMembers.get(sIdx);
                    System.out.println("Active Operator successfully updated to: " + currentOperator);
                    break;
                case 2:
                    String id = InputValidator.readString(scanner, "Enter Employee ID (e.g. S005): ");
                    // check duplicates
                    boolean duplicate = false;
                    for (Staff s : staffMembers) {
                        if (s.getStaffId().equalsIgnoreCase(id.trim())) {
                            duplicate = true;
                            break;
                        }
                    }
                    if (duplicate) {
                        System.out.println("Error: Employee ID " + id + " already exists.");
                        break;
                    }
                    String name = InputValidator.readString(scanner, "Enter Employee Full Name: ");
                    System.out.println("Choose Role:");
                    System.out.println("1. Manager");
                    System.out.println("2. Receptionist");
                    System.out.println("3. Front Desk");
                    System.out.println("4. Head Housekeeping");
                    int roleChoice = InputValidator.readInteger(scanner, "Choice (1-4): ", 1, 4);

                    Staff newStaff;
                    if (roleChoice == 1) {
                        newStaff = new Manager(id, name);
                    } else if (roleChoice == 2) {
                        newStaff = new Receptionist(id, name);
                    } else if (roleChoice == 3) {
                        newStaff = new FrontDesk(id, name);
                    } else {
                        newStaff = new HeadHousekeeping(id, name);
                    }
                    staffMembers.add(newStaff);
                    System.out.println("New employee successfully registered: " + newStaff);
                    break;
                case 3:
                    if (staffMembers.isEmpty()) {
                        System.out.println("No staff members registered.");
                    } else {
                        System.out.println("Registered Staff Directory:");
                        for (Staff s : staffMembers) {
                            System.out.println(s);
                        }
                    }
                    break;
                case 0:
                    return;
            }
            System.out.println();
        }
    }

    // ==========================================
    // 8. REPORTS SUBMENU
    // ==========================================
    private void handleReportsMenu(Scanner scanner) {
        while (true) {
            System.out.println("--- Management Analytics Reports ---");
            System.out.println("1. Room Status Categorized Report");
            System.out.println("2. Customer Directory & Loyalty Summary");
            System.out.println("3. Cumulative Revenue Report");
            System.out.println("4. Most Ordered Room Menu Item");
            System.out.println("0. Back to Main Menu");
            int sub = InputValidator.readInteger(scanner, "Select option (0-4): ", 0, 4);
            System.out.println();

            switch (sub) {
                case 1:
                    System.out.println("--- Categorized Room Status Report ---");
                    Map<RoomStatus, List<Room>> map = new EnumMap<>(RoomStatus.class);
                    for (RoomStatus rStatus : RoomStatus.values()) {
                        map.put(rStatus, new ArrayList<>());
                    }
                    for (Room r : roomService.getAllRooms()) {
                        map.get(r.getStatus()).add(r);
                    }
                    for (Map.Entry<RoomStatus, List<Room>> entry : map.entrySet()) {
                        System.out.println("[" + entry.getKey() + "] Rooms count: " + entry.getValue().size());
                        for (Room r : entry.getValue()) {
                            System.out.println("  - Room " + r.getRoomNumber() + " (" + r.getRoomType() + ") - Base: $" + r.getBasePrice());
                        }
                    }
                    break;
                case 2:
                    System.out.println("--- Registered Customer Registry & Loyalty Info ---");
                    if (customers.isEmpty()) {
                        System.out.println("No customer profiles stored.");
                    } else {
                        System.out.println(String.format("%-10s %-20s %-25s %-10s", "Cust ID", "Name", "Email", "Loyalty"));
                        System.out.println("-------------------------------------------------------------------------");
                        for (Customer c : customers) {
                            System.out.println(String.format("%-10s %-20s %-25s %-10s", c.getCustomerId(), c.getName(), c.getEmail(), c.isLoyalCustomer() ? "YES" : "NO"));
                        }
                    }
                    break;
                case 3:
                    System.out.println("--- Financial Revenue Performance Report ---");
                    double revenue = BookingHistoryLog.getInstance().getTotalRevenue();
                    System.out.println("Total Historical Revenue Logged : $" + String.format("%.2f", revenue));
                    System.out.println("Total Settlement Log Entries    : " + BookingHistoryLog.getInstance().getAllRecords().size());
                    break;
                case 4:
                    System.out.println("--- Menu Item Performance analytics ---");
                    System.out.println("Most Demanded Item: " + BookingHistoryLog.getInstance().getMostOrderedItem());
                    break;
                case 0:
                    return;
            }
            System.out.println();
        }
    }

    // ==========================================
    // 9. AUTOMATIC DEMO DATA GENERATION
    // ==========================================
    public void handleGenerateDemoData() {
        System.out.println("Generating production-quality automated demo datasets...");

        // Ensure clean state to prevent duplicates
        roomService.clearRooms();
        customers.clear();
        staffMembers.clear();
        menuItems.clear();
        bookingService.getActiveBookings().clear();
        receptionQueue.clear();
        BookingHistoryLog.getInstance().clearLog();

        // 1. Generate 10 Staff Members
        staffMembers.add(new Manager("S001", "Charles Xavier"));
        staffMembers.add(new Receptionist("S002", "Emma Frost"));
        staffMembers.add(new FrontDesk("S003", "Logan Howlett"));
        staffMembers.add(new HeadHousekeeping("S004", "Ororo Munroe"));
        staffMembers.add(new Manager("S005", "Jean Grey"));
        staffMembers.add(new Receptionist("S006", "Scott Summers"));
        staffMembers.add(new FrontDesk("S007", "Hank McCoy"));
        staffMembers.add(new HeadHousekeeping("S008", "Kurt Wagner"));
        staffMembers.add(new Receptionist("S009", "Bobby Drake"));
        staffMembers.add(new FrontDesk("S010", "Remy LeBeau"));

        // Set default operator
        currentOperator = staffMembers.get(0);

        // 2. Generate 20 Rooms
        try {
            roomService.createRoom(currentOperator, "101", "Standard King", 120.00);
            roomService.createRoom(currentOperator, "102", "Standard Double", 140.00);
            roomService.createRoom(currentOperator, "103", "Deluxe Suite", 220.00);
            roomService.createRoom(currentOperator, "104", "Standard King", 120.00);
            roomService.createRoom(currentOperator, "105", "Standard Double", 140.00);
            roomService.createRoom(currentOperator, "201", "Deluxe Suite", 220.00);
            roomService.createRoom(currentOperator, "202", "Executive Suite", 350.00);
            roomService.createRoom(currentOperator, "203", "Presidential Suite", 850.00);
            roomService.createRoom(currentOperator, "204", "Deluxe Suite", 220.00);
            roomService.createRoom(currentOperator, "205", "Executive Suite", 350.00);
            roomService.createRoom(currentOperator, "301", "Standard King", 130.00);
            roomService.createRoom(currentOperator, "302", "Standard Double", 150.00);
            roomService.createRoom(currentOperator, "303", "Deluxe Suite", 240.00);
            roomService.createRoom(currentOperator, "304", "Executive Suite", 380.00);
            roomService.createRoom(currentOperator, "305", "Presidential Suite", 900.00);
            roomService.createRoom(currentOperator, "401", "Standard King", 130.00);
            roomService.createRoom(currentOperator, "402", "Standard Double", 150.00);
            roomService.createRoom(currentOperator, "403", "Deluxe Suite", 240.00);
            roomService.createRoom(currentOperator, "404", "Executive Suite", 380.00);
            roomService.createRoom(currentOperator, "405", "Penthouse Suite", 1500.00);
        } catch (Exception e) {
            System.out.println("Warning registering rooms: " + e.getMessage());
        }

        // 3. Generate 15 Customers
        customers.add(new Customer("C001", "Tony Stark", "tony@starkindustries.com", "+1-555-0100", true));
        customers.add(new Customer("C002", "Steve Rogers", "cap@avengers.org", "+1-555-0120", false));
        customers.add(new Customer("C003", "Bruce Banner", "hulk@smash.com", "+1-555-0130", false));
        customers.add(new Customer("C004", "Natasha Romanoff", "widow@shield.gov", "+1-555-0140", true));
        customers.add(new Customer("C005", "Thor Odinson", "thor@asgard.org", "+1-555-0150", true));
        customers.add(new Customer("C006", "Clint Barton", "hawkeye@avengers.org", "+1-555-0160", false));
        customers.add(new Customer("C007", "Peter Parker", "spidey@dailybugle.com", "+1-555-0170", false));
        customers.add(new Customer("C008", "Wanda Maximoff", "scarlet@magic.com", "+1-555-0180", true));
        customers.add(new Customer("C009", "Vision", "vision@synthezoid.net", "+1-555-0190", true));
        customers.add(new Customer("C010", "Sam Wilson", "falcon@cap.com", "+1-555-0200", false));
        customers.add(new Customer("C011", "Bucky Barnes", "soldier@winter.com", "+1-555-0210", false));
        customers.add(new Customer("C012", "Stephen Strange", "doctor@sanctum.org", "+1-555-0220", true));
        customers.add(new Customer("C013", "T'Challa", "panther@wakanda.gov", "+1-555-0230", true));
        customers.add(new Customer("C014", "Carol Danvers", "marvel@space.com", "+1-555-0240", false));
        customers.add(new Customer("C015", "Scott Lang", "antman@pym.com", "+1-555-0250", true));

        // 4. Generate 30 Menu Items via RoomItemFactory
        menuItems.add(RoomItemFactory.createItem("Starter", "M101", "Caprese Salad with Pesto", 14.50));
        menuItems.add(RoomItemFactory.createItem("Starter", "M102", "Truffle Fries with Garlic Aioli", 12.00));
        menuItems.add(RoomItemFactory.createItem("Starter", "M103", "Shrimp Cocktail with Spicy Sauce", 18.00));
        menuItems.add(RoomItemFactory.createItem("Starter", "M104", "French Onion Soup Gratinee", 11.50));
        menuItems.add(RoomItemFactory.createItem("Starter", "M105", "Stuffed Mushrooms with Gorgonzola", 13.00));
        menuItems.add(RoomItemFactory.createItem("Starter", "M106", "Crispy Calamari with Lemon Pepper", 16.50));

        menuItems.add(RoomItemFactory.createItem("Main Course", "M201", "Ribeye Steak with Butter & Asparagus", 45.00));
        menuItems.add(RoomItemFactory.createItem("Main Course", "M202", "Pan-Seared Atlantic Salmon", 38.00));
        menuItems.add(RoomItemFactory.createItem("Main Course", "M203", "Wild Mushroom Risotto with Truffle Oil", 29.50));
        menuItems.add(RoomItemFactory.createItem("Main Course", "M204", "Free-Range Chicken Breast with Lemon Herb", 27.00));
        menuItems.add(RoomItemFactory.createItem("Main Course", "M205", "Lobster Thermidor Classic", 65.00));
        menuItems.add(RoomItemFactory.createItem("Main Course", "M206", "Handmade Fettuccine Carbonara", 26.00));

        menuItems.add(RoomItemFactory.createItem("Dessert", "M301", "Madagascar Vanilla Crème Brûlée", 10.50));
        menuItems.add(RoomItemFactory.createItem("Dessert", "M302", "Warm Chocolate Lava Cake", 12.00));
        menuItems.add(RoomItemFactory.createItem("Dessert", "M303", "New York Style Cheesecake with Berry Coulis", 11.00));
        menuItems.add(RoomItemFactory.createItem("Dessert", "M304", "Tiramisu della Casa", 10.50));
        menuItems.add(RoomItemFactory.createItem("Dessert", "M305", "Assorted Artisanal Cheese Platter", 19.00));
        menuItems.add(RoomItemFactory.createItem("Dessert", "M306", "Fresh Seasonal Fruit Bowl with Mint", 9.00));

        menuItems.add(RoomItemFactory.createItem("Beverage", "M401", "Espresso Macchiato", 4.50));
        menuItems.add(RoomItemFactory.createItem("Beverage", "M402", "San Pellegrino Sparkling (750ml)", 6.50));
        menuItems.add(RoomItemFactory.createItem("Beverage", "M403", "Craft IPA Local Selection", 8.00));
        menuItems.add(RoomItemFactory.createItem("Beverage", "M404", "Cabernet Sauvignon Premium Glass", 14.00));
        menuItems.add(RoomItemFactory.createItem("Beverage", "M405", "Freshly Squeezed Orange Juice", 6.00));
        menuItems.add(RoomItemFactory.createItem("Beverage", "M406", "Imperial Earl Grey Tea", 5.00));

        menuItems.add(RoomItemFactory.createItem("Combo Meal", "M501", "Gourmet Burger & Beer combo", 24.00));
        menuItems.add(RoomItemFactory.createItem("Combo Meal", "M502", "Continental Breakfast package", 20.00));
        menuItems.add(RoomItemFactory.createItem("Combo Meal", "M503", "English Afternoon Tea package for two", 42.00));
        menuItems.add(RoomItemFactory.createItem("Combo Meal", "M504", "Steak & Red Wine luxury bundle", 55.00));
        menuItems.add(RoomItemFactory.createItem("Combo Meal", "M505", "Healthy Lifestyle Breakfast bundle", 22.00));
        menuItems.add(RoomItemFactory.createItem("Combo Meal", "M506", "Midnight Snack bundle", 18.00));

        // 5. Generate 20 historical transactions directly in BookingHistoryLog Singleton
        // This will pre-fill the charts and reports beautifully!
        BookingHistoryLog historyLog = BookingHistoryLog.getInstance();
        Random rand = new Random();

        for (int i = 1; i <= 20; i++) {
            String bId = "B1" + String.format("%02d", i);
            String rNum = roomService.getAllRooms().get((i + 3) % roomService.getAllRooms().size()).getRoomNumber();
            String sId = staffMembers.get(i % staffMembers.size()).getStaffId();

            // Choose a customer
            Customer c = customers.get(i % customers.size());

            // Generate list of pre-ordered items for the report
            List<String> items = new ArrayList<>();
            items.add("Lodging: Room " + rNum + " x" + (1 + (i % 4)) + " nights");
            items.add(menuItems.get(i % menuItems.size()).getName());
            items.add(menuItems.get((i + 5) % menuItems.size()).getName());

            // Assign total bills between $150 and $1200
            double total = 150.0 + (i * 45.75);

            // Stagger date timestamps to provide nice date search records
            LocalDateTime dateTimestamp = LocalDateTime.now().minusDays(20 - i);
            historyLog.addRecord(bId, rNum, sId, items, dateTimestamp, total);
        }

        System.out.println("Demo Data Generation complete!");
        System.out.println("Created: 10 Employees, 20 Rooms, 15 Customers, 30 Premium Menu Items, and 20 Historical Invoices.");
    }
}
