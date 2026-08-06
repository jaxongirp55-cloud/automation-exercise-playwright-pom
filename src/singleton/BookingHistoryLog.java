package singleton;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Singleton class that maintains a persistent historical log of all completed hotel transactions.
 * Follows the Singleton Design Pattern.
 */
public class BookingHistoryLog {

    // Nested class to represent each completed transaction log entry
    public static class LogEntry {
        private String bookingId;
        private int roomNumber;
        private String staffId;
        private List<String> items; // List of item names/descriptions
        private LocalDateTime timestamp;
        private double total;

        /**
         * Constructor for LogEntry.
         */
        public LogEntry(String bookingId, int roomNumber, String staffId, List<String> items, LocalDateTime timestamp, double total) {
            this.bookingId = bookingId;
            this.roomNumber = roomNumber;
            this.staffId = staffId;
            this.items = new ArrayList<>(items);
            this.timestamp = timestamp;
            this.total = total;
        }

        public String getBookingId() {
            return bookingId;
        }

        public int getRoomNumber() {
            return roomNumber;
        }

        public String getStaffId() {
            return staffId;
        }

        public List<String> getItems() {
            return items;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public double getTotal() {
            return total;
        }

        @Override
        public String toString() {
            return String.format("LogEntry[BookingID=%s, Room=%d, Staff=%s, Items=%s, Date=%s, Total=$%.2f]",
                    bookingId, roomNumber, staffId, items, timestamp.toLocalDate(), total);
        }
    }

    private List<LogEntry> entries;

    // Private constructor to prevent direct instantiation
    private BookingHistoryLog() {
        this.entries = new ArrayList<>();
    }

    // Bill Pugh Singleton Implementation for thread safety and lazy loading
    private static class SingletonHelper {
        private static final BookingHistoryLog INSTANCE = new BookingHistoryLog();
    }

    /**
     * Gets the single instance of BookingHistoryLog.
     * @return The BookingHistoryLog instance.
     */
    public static BookingHistoryLog getInstance() {
        return SingletonHelper.INSTANCE;
    }

    /**
     * Adds a historical entry to the log.
     * @param entry The log entry to record.
     */
    public synchronized void addEntry(LogEntry entry) {
        if (entry != null) {
            entries.add(entry);
        }
    }

    /**
     * Gets all log entries.
     * @return List of all entries.
     */
    public synchronized List<LogEntry> getEntries() {
        return new ArrayList<>(entries);
    }

    /**
     * Clears all log entries. (Useful for demo generation / testing)
     */
    public synchronized void clearLog() {
        entries.clear();
    }

    /**
     * Searches completed transactions by room number.
     * @param roomNumber The room number to find.
     * @return List of matching LogEntries.
     */
    public synchronized List<LogEntry> searchByRoom(int roomNumber) {
        List<LogEntry> results = new ArrayList<>();
        for (LogEntry entry : entries) {
            if (entry.getRoomNumber() == roomNumber) {
                results.add(entry);
            }
        }
        return results;
    }

    /**
     * Searches completed transactions by date.
     * @param date The date to find.
     * @return List of matching LogEntries.
     */
    public synchronized List<LogEntry> searchByDate(LocalDate date) {
        List<LogEntry> results = new ArrayList<>();
        if (date == null) {
            return results;
        }
        for (LogEntry entry : entries) {
            if (entry.getTimestamp().toLocalDate().equals(date)) {
                results.add(entry);
            }
        }
        return results;
    }

    /**
     * Finds and returns the most ordered item across all historical transactions.
     * Calculates the frequency of occurrences of items in the logs.
     * @return The name of the most ordered item, or a message indicating no items have been ordered.
     */
    public synchronized String getMostOrderedItem() {
        if (entries.isEmpty()) {
            return "No transactions logged yet.";
        }

        Map<String, Integer> itemCounts = new HashMap<>();
        for (LogEntry entry : entries) {
            for (String item : entry.getItems()) {
                // Strip quantity details if stored as "Item x Qty"
                String cleanItem = item.split(" x ")[0].trim();
                itemCounts.put(cleanItem, itemCounts.getOrDefault(cleanItem, 0) + 1);
            }
        }

        if (itemCounts.isEmpty()) {
            return "No individual menu items ordered in history.";
        }

        String mostOrdered = null;
        int maxCount = -1;
        for (Map.Entry<String, Integer> entry : itemCounts.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostOrdered = entry.getKey();
            }
        }

        return String.format("%s (Ordered %d times)", mostOrdered, maxCount);
    }
}
