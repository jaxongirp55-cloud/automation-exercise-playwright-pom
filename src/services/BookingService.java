package services;

import factory.RoomItemFactory;
import models.*;
import roomitems.*;
import singleton.BookingHistoryLog;
import strategy.StandardPricing;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service class that manages booking states, customers, staff, and room service menu items in-memory.
 */
public class BookingService {
    private final Map<String, Booking> bookings;
    private final Map<String, Customer> customers;
    private final Map<String, Staff> staffMap;
    private final List<RoomItem> menuItems;
    private final RoomService roomService;

    /**
     * Constructor for BookingService.
     * @param roomService The RoomService instance used to look up and transition rooms.
     */
    public BookingService(RoomService roomService) {
        this.bookings = new ConcurrentHashMap<>();
        this.customers = new ConcurrentHashMap<>();
        this.staffMap = new ConcurrentHashMap<>();
        this.menuItems = Collections.synchronizedList(new ArrayList<>());
        this.roomService = roomService;
    }

    // --- CUSTOMER MANAGEMENT ---

    public void addCustomer(Customer customer) {
        if (customer == null || customer.getId() == null) {
            throw new IllegalArgumentException("Customer cannot be null and must have a valid ID.");
        }
        if (customers.containsKey(customer.getId())) {
            throw new IllegalArgumentException("Duplicate customer error: ID '" + customer.getId() + "' already exists.");
        }
        customers.put(customer.getId(), customer);
    }

    public Customer getCustomer(String id) {
        return customers.get(id);
    }

    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers.values());
    }

    // --- STAFF MANAGEMENT ---

    public void addStaff(Staff staff) {
        if (staff == null || staff.getId() == null) {
            throw new IllegalArgumentException("Staff cannot be null and must have a valid ID.");
        }
        if (staffMap.containsKey(staff.getId())) {
            throw new IllegalArgumentException("Duplicate staff error: ID '" + staff.getId() + "' already exists.");
        }
        staffMap.put(staff.getId(), staff);
    }

    public Staff getStaff(String id) {
        return staffMap.get(id);
    }

    public List<Staff> getAllStaff() {
        return new ArrayList<>(staffMap.values());
    }

    // --- MENU ITEM MANAGEMENT ---

    public void addMenuItem(RoomItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Menu item cannot be null.");
        }
        // Check duplicates by name
        for (RoomItem existing : menuItems) {
            if (existing.getName().equalsIgnoreCase(item.getName())) {
                throw new IllegalArgumentException("Duplicate menu item error: '" + item.getName() + "' already exists.");
            }
        }
        menuItems.add(item);
    }

    public List<RoomItem> getMenuItems() {
        return new ArrayList<>(menuItems);
    }

    // --- BOOKING OPERATIONS ---

    /**
     * Creates a new booking.
     * Validates that room is free, and customer and staff are valid.
     */
    public Booking createBooking(String bookingId, int roomNumber, String customerId, String staffId) {
        if (bookingId == null || bookingId.trim().isEmpty()) {
            throw new IllegalArgumentException("Booking ID cannot be empty.");
        }
        if (bookings.containsKey(bookingId)) {
            throw new IllegalArgumentException("Duplicate booking error: ID '" + bookingId + "' already exists.");
        }

        Room room = roomService.getRoom(roomNumber);
        if (room == null) {
            throw new IllegalArgumentException("Room #" + roomNumber + " does not exist.");
        }
        if (room.getStatus() != RoomStatus.FREE) {
            throw new IllegalArgumentException("Room already occupied error: Room #" + roomNumber + " is currently " + room.getStatus());
        }

        Customer customer = customers.get(customerId);
        if (customer == null) {
            throw new IllegalArgumentException("Customer ID '" + customerId + "' does not exist.");
        }

        Staff staff = staffMap.get(staffId);
        if (staff == null) {
            throw new IllegalArgumentException("Staff ID '" + staffId + "' does not exist.");
        }

        // Check staff permissions
        if (!staff.hasPermission("RESERVE_ROOM")) {
            throw new SecurityException("Access Denied: " + staff + " does not have permission to reserve rooms.");
        }

        Booking booking = new Booking(bookingId, room, customer, staff);
        // Reserve the room
        room.setStatus(RoomStatus.RESERVED);
        booking.setConfirmed(true);
        bookings.put(bookingId, booking);

        return booking;
    }

    /**
     * Direct addition for undo stack.
     */
    public void addBookingDirectly(Booking booking) {
        if (booking != null) {
            bookings.put(booking.getBookingId(), booking);
        }
    }

    /**
     * Direct removal for undo stack.
     */
    public void removeBookingDirectly(String bookingId) {
        bookings.remove(bookingId);
    }

    /**
     * Checks in a guest for a reserved booking.
     * Transition: RESERVED -> OCCUPIED
     */
    public void checkIn(String bookingId, String staffId) {
        Booking booking = bookings.get(bookingId);
        if (booking == null) {
            throw new IllegalArgumentException("Booking ID '" + bookingId + "' does not exist.");
        }

        Staff staff = staffMap.get(staffId);
        if (staff == null) {
            throw new IllegalArgumentException("Staff ID '" + staffId + "' does not exist.");
        }

        if (!staff.hasPermission("CHECK_IN")) {
            throw new SecurityException("Access Denied: " + staff + " does not have permission to check in guests.");
        }

        if (booking.isCheckedIn()) {
            throw new IllegalArgumentException("Booking #" + bookingId + " is already checked-in.");
        }

        Room room = booking.getRoom();
        if (room.getStatus() != RoomStatus.RESERVED) {
            throw new IllegalArgumentException("Cannot check-in: Room status is " + room.getStatus() + " instead of RESERVED.");
        }

        room.setStatus(RoomStatus.OCCUPIED);
        booking.setCheckedIn(true);
    }

    /**
     * Checks out a guest.
     * Transition: OCCUPIED -> AWAITING_BILL
     */
    public void checkOut(String bookingId, String staffId) {
        Booking booking = bookings.get(bookingId);
        if (booking == null) {
            throw new IllegalArgumentException("Booking ID '" + bookingId + "' does not exist.");
        }

        Staff staff = staffMap.get(staffId);
        if (staff == null) {
            throw new IllegalArgumentException("Staff ID '" + staffId + "' does not exist.");
        }

        if (!staff.hasPermission("CHECK_OUT")) {
            throw new SecurityException("Access Denied: " + staff + " does not have permission to check out guests.");
        }

        if (!booking.isCheckedIn()) {
            throw new IllegalArgumentException("Cannot check-out: Guest has not checked in yet.");
        }
        if (booking.isCheckedOut()) {
            throw new IllegalArgumentException("Booking #" + bookingId + " is already checked-out.");
        }

        Room room = booking.getRoom();
        if (room.getStatus() != RoomStatus.OCCUPIED) {
            throw new IllegalArgumentException("Cannot check-out: Room is not currently OCCUPIED.");
        }

        room.setStatus(RoomStatus.AWAITING_BILL);
        booking.setCheckedOut(true);
    }

    /**
     * Adds an item to a booking.
     */
    public void addItemToBooking(String bookingId, String itemName, int quantity) {
        Booking booking = bookings.get(bookingId);
        if (booking == null) {
            throw new IllegalArgumentException("Booking ID '" + bookingId + "' does not exist.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }

        // Find item in menu
        RoomItem menuMatch = null;
        for (RoomItem item : menuItems) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                menuMatch = item;
                break;
            }
        }

        if (menuMatch == null) {
            throw new IllegalArgumentException("Invalid menu choice: '" + itemName + "' is not on the hotel service menu.");
        }

        BookingItem bookingItem = new BookingItem(menuMatch, quantity);
        booking.addBookingItem(bookingItem);
    }

    /**
     * Modifies/removes an item from the booking or updates quantity.
     */
    public void updateBookingItemQuantity(String bookingId, String itemName, int newQuantity) {
        Booking booking = bookings.get(bookingId);
        if (booking == null) {
            throw new IllegalArgumentException("Booking ID '" + bookingId + "' does not exist.");
        }

        BookingItem match = null;
        for (BookingItem item : booking.getBookingItems()) {
            if (item.getRoomItem().getName().equalsIgnoreCase(itemName)) {
                match = item;
                break;
            }
        }

        if (match == null) {
            throw new IllegalArgumentException("Item '" + itemName + "' is not in this booking.");
        }

        if (newQuantity <= 0) {
            booking.getBookingItems().remove(match);
        } else {
            match.setQuantity(newQuantity);
        }
    }

    /**
     * Cancels a booking directly.
     * Transitions room status back to FREE.
     */
    public void cancelBooking(String bookingId, String staffId) {
        Booking booking = bookings.get(bookingId);
        if (booking == null) {
            throw new IllegalArgumentException("Booking ID '" + bookingId + "' does not exist.");
        }

        Staff staff = staffMap.get(staffId);
        if (staff == null) {
            throw new IllegalArgumentException("Staff ID '" + staffId + "' does not exist.");
        }

        if (!staff.hasPermission("MANAGE_BOOKINGS")) {
            throw new SecurityException("Access Denied: " + staff + " does not have permission to cancel bookings.");
        }

        if (booking.isCheckedOut()) {
            throw new IllegalArgumentException("Cannot cancel booking: Guest has already checked out.");
        }

        booking.getRoom().setStatus(RoomStatus.FREE);
        booking.setConfirmed(false);
        bookings.remove(bookingId);
    }

    public Booking getBooking(String bookingId) {
        return bookings.get(bookingId);
    }

    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookings.values());
    }

    /**
     * Checks if booking has items. Throws exception if empty.
     */
    public void validateNotEmpty(String bookingId) {
        Booking booking = bookings.get(bookingId);
        if (booking == null) {
            throw new IllegalArgumentException("Booking does not exist.");
        }
        if (booking.getBookingItems().isEmpty()) {
            throw new IllegalArgumentException("Empty booking error: Booking must contain at least one item order.");
        }
    }

    public void clearAllBookings() {
        bookings.clear();
        customers.clear();
        staffMap.clear();
        menuItems.clear();
    }

    // --- AUTOMATIC DEMO DATA GENERATION ---

    /**
     * Populates database memory with realistic demo data.
     * Generates: 20 Rooms, 15 Customers, 10 Staff, 30 Menu Items, 20 Bookings (with complete histories).
     */
    public void generateDemoData() {
        clearAllBookings();
        roomService.clearAllRooms();
        BookingHistoryLog.getInstance().clearLog();

        // 1. Generate 20 Rooms (Numbers 101-110 Standard, 111-115 Deluxe, 116-120 Suite)
        for (int i = 1; i <= 10; i++) {
            roomService.createRoom(100 + i, "Standard Single", 80.00);
        }
        for (int i = 11; i <= 15; i++) {
            roomService.createRoom(100 + i, "Deluxe Double", 150.00);
        }
        for (int i = 16; i <= 20; i++) {
            roomService.createRoom(100 + i, "Executive Suite", 300.00);
        }

        // 2. Generate 15 Customers (Mix of loyalty and standard)
        String[] customerNames = {
            "Alice Smith", "Bob Jones", "Charlie Brown", "Diana Prince", "Edward Elric",
            "Fiona Gallagher", "George Clark", "Hannah Abbott", "Ian Malcolm", "Julia Roberts",
            "Kevin Hart", "Laura Croft", "Michael Jordan", "Nancy Drew", "Oliver Twist"
        };
        for (int i = 0; i < customerNames.length; i++) {
            String id = "C" + (i + 1);
            boolean loyalty = (i % 2 == 0); // Alternate loyalty status
            addCustomer(new Customer(id, customerNames[i],
                    customerNames[i].toLowerCase().replace(" ", "") + "@mail.com",
                    "+1-555-010" + i, loyalty));
        }

        // 3. Generate 10 Staff (1 Manager, 4 Receptionists, 3 Front Desk, 2 Head Housekeeping)
        addStaff(new Manager("S1", "Sarah Connor"));
        addStaff(new Receptionist("S2", "John Connor"));
        addStaff(new Receptionist("S3", "Jack Sparrow"));
        addStaff(new Receptionist("S4", "Peter Parker"));
        addStaff(new Receptionist("S5", "Clark Kent"));
        addStaff(new FrontDesk("S6", "Bruce Wayne"));
        addStaff(new FrontDesk("S7", "Barry Allen"));
        addStaff(new FrontDesk("S8", "Hal Jordan"));
        addStaff(new HeadHousekeeping("S9", "Wanda Maximoff"));
        addStaff(new HeadHousekeeping("S10", "Natasha Romanoff"));

        // 4. Generate 30 Menu Items (using Factory)
        // Starters (6 items)
        addMenuItem(RoomItemFactory.createItem("starter", "Garlic Bread", 6.50, null));
        addMenuItem(RoomItemFactory.createItem("starter", "Mozzarella Sticks", 8.00, null));
        addMenuItem(RoomItemFactory.createItem("starter", "Chicken Wings", 10.50, null));
        addMenuItem(RoomItemFactory.createItem("starter", "Spring Rolls", 7.00, null));
        addMenuItem(RoomItemFactory.createItem("starter", "Tomato Soup", 6.00, null));
        addMenuItem(RoomItemFactory.createItem("starter", "Bruschetta", 9.00, null));

        // Main Courses (8 items)
        addMenuItem(RoomItemFactory.createItem("main", "Ribeye Steak", 32.00, null));
        addMenuItem(RoomItemFactory.createItem("main", "Grilled Salmon", 26.50, null));
        addMenuItem(RoomItemFactory.createItem("main", "Fettuccine Alfredo", 18.00, null));
        addMenuItem(RoomItemFactory.createItem("main", "Cheeseburger & Fries", 16.00, null));
        addMenuItem(RoomItemFactory.createItem("main", "Margherita Pizza", 15.00, null));
        addMenuItem(RoomItemFactory.createItem("main", "Chicken Tikka Masala", 20.00, null));
        addMenuItem(RoomItemFactory.createItem("main", "Vegan Buddha Bowl", 17.50, null));
        addMenuItem(RoomItemFactory.createItem("main", "Club Sandwich", 14.00, null));

        // Desserts (6 items)
        addMenuItem(RoomItemFactory.createItem("dessert", "Chocolate Lava Cake", 8.50, null));
        addMenuItem(RoomItemFactory.createItem("dessert", "New York Cheesecake", 9.00, null));
        addMenuItem(RoomItemFactory.createItem("dessert", "Apple Pie", 7.50, null));
        addMenuItem(RoomItemFactory.createItem("dessert", "Tiramisu", 9.50, null));
        addMenuItem(RoomItemFactory.createItem("dessert", "Ice Cream Sundae", 6.50, null));
        addMenuItem(RoomItemFactory.createItem("dessert", "Crème Brûlée", 10.00, null));

        // Beverages (6 items - 3 Alcoholic, 3 Non-Alcoholic)
        addMenuItem(RoomItemFactory.createItem("beverage", "Coca-Cola", 3.00, false));
        addMenuItem(RoomItemFactory.createItem("beverage", "Fresh Orange Juice", 4.50, false));
        addMenuItem(RoomItemFactory.createItem("beverage", "Mineral Water", 2.50, false));
        addMenuItem(RoomItemFactory.createItem("beverage", "Red Wine (Glass)", 12.00, true));
        addMenuItem(RoomItemFactory.createItem("beverage", "Draft Beer", 7.00, true));
        addMenuItem(RoomItemFactory.createItem("beverage", "Classic Martini", 14.00, true));

        // Combo Meals (4 items)
        addMenuItem(RoomItemFactory.createItem("combo", "Steak & Wine Special", 40.00, "Ribeye Steak, Green Salad, and Glass of Red Wine"));
        addMenuItem(RoomItemFactory.createItem("combo", "Burger & Brew", 20.00, "Cheeseburger, Fries, and Draft Beer"));
        addMenuItem(RoomItemFactory.createItem("combo", "Italian Feast", 30.00, "Margherita Pizza, Bruschetta, and Coca-Cola"));
        addMenuItem(RoomItemFactory.createItem("combo", "Breakfast in Bed", 18.00, "Pancakes, Scrambled Eggs, Bacon, and Fresh Orange Juice"));

        // 5. Generate 20 Bookings across rooms, with some completing payment to create realistic log entries.
        // We will finalize 12 bookings as past/logged entries, 3 as active OCCUPIED, 3 as RESERVED, 2 as AWAITING_BILL
        Random random = new Random();
        for (int i = 1; i <= 20; i++) {
            String bId = "B" + i;
            int roomNo = 100 + i; // Rooms 101 to 120
            String custId = "C" + ((i % 15) + 1);
            String staffId = "S" + ((i % 5) + 1); // Select receptionists / manager S1-S5

            Room r = roomService.getRoom(roomNo);
            Customer c = getCustomer(custId);
            Staff s = getStaff(staffId);

            Booking booking = new Booking(bId, r, c, s);

            // Add some items
            int itemIndex1 = (i * 3) % menuItems.size();
            int itemIndex2 = (i * 7) % menuItems.size();
            booking.addBookingItem(new BookingItem(menuItems.get(itemIndex1), 1 + (i % 3)));
            booking.addBookingItem(new BookingItem(menuItems.get(itemIndex2), 1));

            if (i <= 12) {
                // Historically finalized and logged (Payment received, room returned to FREE)
                booking.setConfirmed(true);
                booking.setCheckedIn(true);
                booking.setCheckedOut(true);
                r.setStatus(RoomStatus.FREE); // Complete and checked out

                // Add to bookings map
                bookings.put(bId, booking);

                // Create a Bill and Log it
                double subTotal = booking.calculateSubtotal();
                double discount = c.isLoyaltyMember() ? subTotal * 0.1 : 0.0;
                double finalAmount = (subTotal - discount) * 1.10 + 10.00; // subtotal - disc + tax + tip

                List<String> orderedItemNames = new ArrayList<>();
                orderedItemNames.add("Room: " + r.getRoomType());
                for (BookingItem bItem : booking.getBookingItems()) {
                    orderedItemNames.add(bItem.getRoomItem().getName() + " x " + bItem.getQuantity());
                }

                // Log entry
                BookingHistoryLog.LogEntry logEntry = new BookingHistoryLog.LogEntry(
                        bId,
                        roomNo,
                        staffId,
                        orderedItemNames,
                        LocalDateTime.now().minusDays(15 - i), // sequential dates
                        finalAmount
                );
                BookingHistoryLog.getInstance().addEntry(logEntry);
            }
            else if (i <= 15) {
                // OCCUPIED rooms
                booking.setConfirmed(true);
                booking.setCheckedIn(true);
                r.setStatus(RoomStatus.OCCUPIED);
                bookings.put(bId, booking);
            }
            else if (i <= 18) {
                // RESERVED rooms
                booking.setConfirmed(true);
                r.setStatus(RoomStatus.RESERVED);
                bookings.put(bId, booking);
            }
            else {
                // AWAITING_BILL rooms
                booking.setConfirmed(true);
                booking.setCheckedIn(true);
                booking.setCheckedOut(true);
                r.setStatus(RoomStatus.AWAITING_BILL);
                bookings.put(bId, booking);
            }
        }
    }
}
