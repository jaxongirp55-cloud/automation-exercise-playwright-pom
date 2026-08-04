package orders;

import java.util.ArrayList;
import java.util.List;

/**
 * Manually implemented Priority Queue for Orders utilizing a Max-Heap array implementation.
 * Ensures PREMIUM orders are processed first, then NEXT_DAY, then STANDARD.
 * Ties are broken by chronological arrival order.
 *
 * Time Complexity (Enqueue): O(log N)
 * Time Complexity (Dequeue/Poll): O(log N)
 * Time Complexity (Peek): O(1)
 * Space Complexity: O(N) where N is number of stored orders.
 */
public class PriorityOrderQueue {
    private final List<HeapEntry> heap;

    private static class HeapEntry {
        private final Order order;
        private final long sequenceNumber; // Chronological tie breaker

        public HeapEntry(Order order, long sequenceNumber) {
            this.order = order;
            this.sequenceNumber = sequenceNumber;
        }

        public Order getOrder() {
            return order;
        }

        public long getSequenceNumber() {
            return sequenceNumber;
        }
    }

    private long sequenceCounter = 0;

    /**
     * Initializes an empty manual PriorityQueue.
     */
    public PriorityOrderQueue() {
        this.heap = new ArrayList<>();
    }

    /**
     * Enqueues an order into the queue based on its priority.
     * @param order The order to enqueue.
     */
    public void enqueue(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Cannot enqueue null order");
        }
        heap.add(new HeapEntry(order, sequenceCounter++));
        siftUp(heap.size() - 1);
    }

    /**
     * Dequeues the highest priority order.
     * @return Highest priority order, or null if empty.
     */
    public Order dequeue() {
        if (heap.isEmpty()) {
            return null;
        }
        HeapEntry root = heap.get(0);
        HeapEntry last = heap.remove(heap.size() - 1);
        if (!heap.isEmpty()) {
            heap.set(0, last);
            siftDown(0);
        }
        return root.getOrder();
    }

    /**
     * Peeks at the highest priority order without removing it.
     * @return Highest priority order, or null if empty.
     */
    public Order peek() {
        if (heap.isEmpty()) {
            return null;
        }
        return heap.get(0).getOrder();
    }

    /**
     * Checks if the queue is empty.
     * @return True if empty, false otherwise.
     */
    public boolean isEmpty() {
        return heap.isEmpty();
    }

    /**
     * Returns the size of the queue.
     * @return Number of items in queue.
     */
    public int size() {
        return heap.size();
    }

    /**
     * Displays all orders in queue in raw heap array representation.
     */
    public void displayQueue() {
        System.out.println("===== PENDING PRIORITY ORDER QUEUE =====");
        if (heap.isEmpty()) {
            System.out.println("[Queue is empty]");
            return;
        }
        // Note: For visualization, we clone and dequeue elements to show sorted order
        List<HeapEntry> tempHeap = new ArrayList<>(heap);
        long tempCounter = this.sequenceCounter;

        // We'll perform a soft sort just for displaying so we don't destroy original queue
        List<Order> sortedList = new ArrayList<>();
        while (!this.isEmpty()) {
            sortedList.add(this.dequeue());
        }

        // Restore original state
        for (Order o : sortedList) {
            this.enqueue(o);
        }

        for (int i = 0; i < sortedList.size(); i++) {
            System.out.println((i + 1) + ". " + sortedList.get(i));
        }
    }

    private void siftUp(int index) {
        while (index > 0) {
            int parentIdx = (index - 1) / 2;
            if (compare(heap.get(index), heap.get(parentIdx)) > 0) {
                swap(index, parentIdx);
                index = parentIdx;
            } else {
                break;
            }
        }
    }

    private void siftDown(int index) {
        int size = heap.size();
        while (index * 2 + 1 < size) {
            int leftChild = index * 2 + 1;
            int rightChild = leftChild + 1;
            int candidate = leftChild;

            if (rightChild < size && compare(heap.get(rightChild), heap.get(leftChild)) > 0) {
                candidate = rightChild;
            }

            if (compare(heap.get(candidate), heap.get(index)) > 0) {
                swap(index, candidate);
                index = candidate;
            } else {
                break;
            }
        }
    }

    private void swap(int i, int j) {
        HeapEntry temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    /**
     * Compares two HeapEntries. Higher priority ranks first.
     * If priorities are identical, the sequence counter determines entry ordering (FIFO).
     */
    private int compare(HeapEntry a, HeapEntry b) {
        int priorityDiff = Integer.compare(a.getOrder().getPriority().getRank(), b.getOrder().getPriority().getRank());
        if (priorityDiff != 0) {
            return priorityDiff;
        }
        // Higher sequence number means entered later -> lower rank
        return Long.compare(b.getSequenceNumber(), a.getSequenceNumber());
    }
}
