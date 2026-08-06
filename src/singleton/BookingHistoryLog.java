package singleton;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Singleton Pattern implementation to log and search booking completion history.
 * Thread-safe implementation utilizing double-checked locking.
 */
public class BookingHistoryLog {
    private static volatile BookingHistoryLog instance;
    private final List<HistoryRecord> records;

    /**
     * Private constructor to enforce Singleton property.
     */
    private BookingHistoryLog() {
        this.records = new ArrayList<>();
    }

    /**
     * Thread-safe double-checked locking getter for the singleton instance.
     *
     * @return The single BookingHistoryLog instance.
     */
    public static BookingHistoryLog getInstance() {
        if (instance == null) {
            synchronized (BookingHistoryLog.class) {
                if (instance == null) {
                    instance = new BookingHistoryLog();
                }
            }
        }
        return instance;
    }

    /**
     * Adds a record to the history log.
     *
     * @param bookingId  Unique ID of the booking.
     * @param roomNumber Room designation.
     * @param staffId    Employee that handled the booking.
     * @param items      List of room items ordered.
     * @param timestamp  Date and time of transaction.
     * @param total      Final billing total.
     */
    public synchronized void addRecord(String bookingId, String roomNumber, String staffId, List<String> items, LocalDateTime timestamp, double total) {
        records.add(new HistoryRecord(bookingId, roomNumber, staffId, items, timestamp, total));
    }

    /**
     * Retrieves all recorded history items.
     *
     * @return List of HistoryRecord elements.
     */
    public synchronized List<HistoryRecord> getAllRecords() {
        return new ArrayList<>(records);
    }

    /**
     * Searches booking records by room number.
     *
     * @param roomNumber The room designation to query.
     * @return List of matching HistoryRecord objects.
     */
    public synchronized List<HistoryRecord> searchByRoom(String roomNumber) {
        List<HistoryRecord> result = new ArrayList<>();
        if (roomNumber == null) return result;
        for (HistoryRecord record : records) {
            if (record.getRoomNumber().equalsIgnoreCase(roomNumber.trim())) {
                result.add(record);
            }
        }
        return result;
    }

    /**
     * Searches booking records by date string.
     *
     * @param dateStr Date formatted as "yyyy-MM-dd".
     * @return List of matching HistoryRecord objects.
     */
    public synchronized List<HistoryRecord> searchByDate(String dateStr) {
        List<HistoryRecord> result = new ArrayList<>();
        if (dateStr == null) return result;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (HistoryRecord record : records) {
            String recordDate = record.getTimestamp().format(dtf);
            if (recordDate.equals(dateStr.trim())) {
                result.add(record);
            }
        }
        return result;
    }

    /**
     * Identifies the most ordered item across all records.
     *
     * @return Description of the item with highest ordered quantity.
     */
    public synchronized String getMostOrderedItem() {
        if (records.isEmpty()) {
            return "No items ordered yet.";
        }
        Map<String, Integer> itemCounts = new HashMap<>();
        for (HistoryRecord record : records) {
            for (String item : record.getItems()) {
                itemCounts.put(item, itemCounts.getOrDefault(item, 0) + 1);
            }
        }
        String mostOrdered = null;
        int maxCount = 0;
        for (Map.Entry<String, Integer> entry : itemCounts.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostOrdered = entry.getKey();
            }
        }
        return mostOrdered != null ? mostOrdered + " (ordered " + maxCount + " times)" : "None";
    }

    /**
     * Calculates the cumulative total revenue recorded.
     *
     * @return Sum of all totals in history.
     */
    public synchronized double getTotalRevenue() {
        double revenue = 0.0;
        for (HistoryRecord r : records) {
            revenue += r.getTotal();
        }
        return revenue;
    }

    /**
     * Clear all records in the log (useful for resetting/demoing).
     */
    public synchronized void clearLog() {
        records.clear();
    }

    /**
     * Inner class representing a static immutable historical log record.
     */
    public static class HistoryRecord {
        private final String bookingId;
        private final String roomNumber;
        private final String staffId;
        private final List<String> items;
        private final LocalDateTime timestamp;
        private final double total;

        public HistoryRecord(String bookingId, String roomNumber, String staffId, List<String> items, LocalDateTime timestamp, double total) {
            this.bookingId = bookingId;
            this.roomNumber = roomNumber;
            this.staffId = staffId;
            this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
            this.timestamp = timestamp;
            this.total = total;
        }

        public String getBookingId() {
            return bookingId;
        }

        public String getRoomNumber() {
            return roomNumber;
        }

        public String getStaffId() {
            return staffId;
        }

        public List<String> getItems() {
            return new ArrayList<>(items);
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public double getTotal() {
            return total;
        }

        @Override
        public String toString() {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return "HistoryRecord{" +
                    "Booking ID='" + bookingId + '\'' +
                    ", Room='" + roomNumber + '\'' +
                    ", Staff ID='" + staffId + '\'' +
                    ", Items=" + items +
                    ", Date=" + timestamp.format(dtf) +
                    ", Total=$" + String.format("%.2f", total) +
                    '}';
        }
    }
}
