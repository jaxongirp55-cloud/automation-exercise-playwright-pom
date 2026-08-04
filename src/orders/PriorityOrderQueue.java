package orders;

import java.util.ArrayList;
import java.util.List;

/**
 * Manual Priority Queue implementation utilizing a Binary Max-Heap array.
 * Demonstrates advanced understanding of heap algorithms, resizing, and sift operations.
 * Processes high-priority orders (Premium > Next Day > Standard, with VIP subscribers taking precedence).
 *
 * Big-O Complexity:
 * - Time Complexity:
 *   - Enqueue (insert): O(log N)
 *   - Dequeue (poll): O(log N)
 *   - Peek: O(1)
 *   - Size / isEmpty: O(1)
 * - Space Complexity: O(N) where N is the number of stored orders.
 *
 * @author Senior Java Software Architect
 */
public class PriorityOrderQueue {
    private Order[] heap;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    /**
     * Instantiates an empty Priority Queue with default capacity.
     */
    public PriorityOrderQueue() {
        this.heap = new Order[DEFAULT_CAPACITY];
        this.size = 0;
    }

    /**
     * Inserts an order into the Priority Queue.
     * @param order The order to enqueue.
     */
    public void enqueue(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Cannot enqueue a null order.");
        }
        // Duplicate check
        if (containsOrder(order.getOrderId())) {
            throw new IllegalStateException("Duplicate Order ID detected: " + order.getOrderId());
        }

        ensureCapacity();
        heap[size] = order;
        siftUp(size);
        size++;
    }

    /**
     * Removes and returns the highest priority order from the queue.
     * @return The highest priority Order.
     * @throws IllegalStateException if the queue is empty.
     */
    public Order dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Order processing queue is empty.");
        }
        Order result = heap[0];
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;

        if (size > 0) {
            siftDown(0);
        }
        return result;
    }

    /**
     * Views, but does not remove, the highest priority order from the queue.
     * @return The highest priority Order.
     * @throws IllegalStateException if the queue is empty.
     */
    public Order peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Order processing queue is empty.");
        }
        return heap[0];
    }

    /**
     * Checks if the queue is empty.
     * @return true if size is zero, false otherwise.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the number of items in the queue.
     * @return size.
     */
    public int size() {
        return size;
    }

    /**
     * Helper to verify if an order ID is already registered in the queue.
     * @param id The Order ID.
     * @return true if found, false otherwise.
     */
    public boolean containsOrder(String id) {
        if (id == null) return false;
        for (int i = 0; i < size; i++) {
            if (heap[i].getOrderId().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Custom list retrieval to display all registered orders sorted by actual priority.
     * Returns a copy of the priority orders without modifying the internal heap.
     *
     * Big-O: O(N log N) to extract.
     * @return Sorted list of current orders.
     */
    public List<Order> getSortedOrders() {
        // Clone heap elements
        PriorityOrderQueue tempQueue = new PriorityOrderQueue();
        for (int i = 0; i < size; i++) {
            // bypassing contains check since we know these are already verified unique
            tempQueue.heap[tempQueue.size] = this.heap[i];
            tempQueue.size++;
        }
        // Build heap structure on temp
        for (int i = (tempQueue.size / 2) - 1; i >= 0; i--) {
            tempQueue.siftDown(i);
        }

        List<Order> sortedList = new ArrayList<>();
        while (!tempQueue.isEmpty()) {
            sortedList.add(tempQueue.dequeue());
        }
        return sortedList;
    }

    // Binary Max-Heap Sifting Helpers
    private void siftUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            // If current element is higher priority (comparePriorityTo < 0) than parent, swap
            if (heap[index].comparePriorityTo(heap[parentIndex]) < 0) {
                swap(index, parentIndex);
                index = parentIndex;
            } else {
                break;
            }
        }
    }

    private void siftDown(int index) {
        while (index < size / 2) {
            int leftChild = 2 * index + 1;
            int rightChild = leftChild + 1;
            int highestPriorityChild = leftChild;

            if (rightChild < size && heap[rightChild].comparePriorityTo(heap[leftChild]) < 0) {
                highestPriorityChild = rightChild;
            }

            if (heap[highestPriorityChild].comparePriorityTo(heap[index]) < 0) {
                swap(index, highestPriorityChild);
                index = highestPriorityChild;
            } else {
                break;
            }
        }
    }

    private void swap(int i, int j) {
        Order temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    private void ensureCapacity() {
        if (size == heap.length) {
            Order[] temp = new Order[heap.length * 2];
            System.arraycopy(heap, 0, temp, 0, heap.length);
            heap = temp;
        }
    }

    /**
     * Displays all current orders inside the queue.
     */
    public void displayQueue() {
        System.out.println("\n===== ACTIVE PRIORITY ORDER QUEUE =====");
        if (isEmpty()) {
            System.out.println("[Queue is empty. No orders currently pending processing.]");
            return;
        }
        List<Order> sorted = getSortedOrders();
        int rank = 1;
        for (Order o : sorted) {
            System.out.printf("%2d. [%-10s] %-15s | Priority: %-9s | VIP: %-5b | Route Strategy: %-15s\n",
                    rank++, o.getOrderId(), o.getCustomerName(), o.getPriority(), o.isSubscribed(), o.getShippingType());
        }
        System.out.println("=======================================");
    }
}
