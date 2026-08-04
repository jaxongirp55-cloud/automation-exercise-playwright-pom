package orders;

/**
 * Manually implemented FIFO Queue representing delivery trucks queued at the warehouse terminal.
 * Designed with a singly linked list architecture without Java Collections.
 *
 * Time Complexity (Enqueue): O(1)
 * Time Complexity (Dequeue/Dispatch): O(1)
 * Time Complexity (Peek): O(1)
 * Space Complexity: O(N) where N is number of trucks.
 */
public class TruckQueue {

    private static class QueueNode {
        private final String truckId;
        private QueueNode next;

        public QueueNode(String truckId) {
            this.truckId = truckId;
        }
    }

    private QueueNode head;
    private QueueNode tail;
    private int size;

    /**
     * Initializes an empty truck terminal FIFO queue.
     */
    public TruckQueue() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    /**
     * Enqueues a truck ID to the back of the queue (FIFO - In).
     * @param truckId Unique identifier for the logistics truck.
     */
    public void enqueue(String truckId) {
        if (truckId == null || truckId.trim().isEmpty()) {
            throw new IllegalArgumentException("Truck ID cannot be empty");
        }
        QueueNode newNode = new QueueNode(truckId);
        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    /**
     * Dispatches the truck at the front of the queue (FIFO - Out).
     * @return Dispatched truck ID, or null if queue is empty.
     */
    public String dequeue() {
        if (head == null) {
            return null;
        }
        String truckId = head.truckId;
        head = head.next;
        if (head == null) {
            tail = null;
        }
        size--;
        return truckId;
    }

    /**
     * Views the truck at the front of the queue without dispatching.
     * @return Front truck ID, or null if empty.
     */
    public String peek() {
        if (head == null) {
            return null;
        }
        return head.truckId;
    }

    /**
     * Verifies if queue has no trucks.
     * @return True if empty, false otherwise.
     */
    public boolean isEmpty() {
        return head == null;
    }

    /**
     * Returns total queued trucks.
     * @return Truck count.
     */
    public int size() {
        return size;
    }

    /**
     * Displays all trucks in queue line.
     */
    public void displayQueue() {
        System.out.println("===== TRUCK LOADING TERMINAL (FIFO) =====");
        if (isEmpty()) {
            System.out.println("[No trucks currently in the loading bay]");
            return;
        }
        QueueNode curr = head;
        int position = 1;
        while (curr != null) {
            System.out.printf("  Terminal Slot %d: Truck ID [%s]\n", position++, curr.truckId);
            curr = curr.next;
        }
    }
}
