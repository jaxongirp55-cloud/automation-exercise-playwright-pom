package orders;

import java.util.NoSuchElementException;

/**
 * Manual FIFO (First-In, First-Out) Queue implementation for managing delivery Trucks.
 * Avoids built-in Java queue classes to demonstrate deep algorithmic competencies.
 * Implements a dynamic circular array queue for O(1) operations.
 *
 * Big-O Complexity:
 * - Time Complexity:
 *   - Enqueue (insert): O(1) amortized
 *   - Dequeue (poll): O(1)
 *   - Peek: O(1)
 *   - Display: O(N) where N is number of trucks
 * - Space Complexity: O(N) where N is number of trucks
 *
 * @author Senior Java Software Architect
 */
public class TruckQueue {
    private String[] queue;
    private int front;
    private int rear;
    private int size;
    private int capacity;

    /**
     * Initializes a circular Truck queue with initial default size.
     */
    public TruckQueue() {
        this.capacity = 8;
        this.queue = new String[capacity];
        this.front = 0;
        this.rear = -1;
        this.size = 0;
    }

    /**
     * Adds (enqueues) a Truck ID to the end of the queue.
     * @param truckId License plate or fleet identifier of the truck.
     */
    public void enqueue(String truckId) {
        if (truckId == null || truckId.trim().isEmpty()) {
            throw new IllegalArgumentException("Truck ID cannot be null or empty.");
        }
        ensureCapacity();
        rear = (rear + 1) % capacity;
        queue[rear] = truckId.trim();
        size++;
    }

    /**
     * Removes and returns (dispatches) the truck at the front of the queue.
     * @return Dispatch truck identifier.
     */
    public String dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Truck dispatch queue is empty.");
        }
        String truck = queue[front];
        queue[front] = null; // free reference
        front = (front + 1) % capacity;
        size--;
        return truck;
    }

    /**
     * Returns the truck currently at the front of the queue without removal.
     * @return Next truck for dispatch.
     */
    public String peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Truck queue is empty.");
        }
        return queue[front];
    }

    /**
     * Checks if the queue is empty.
     * @return true if size is zero, false otherwise.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns total elements.
     * @return count.
     */
    public int size() {
        return size;
    }

    private void ensureCapacity() {
        if (size == capacity) {
            int newCapacity = capacity * 2;
            String[] newQueue = new String[newCapacity];
            for (int i = 0; i < size; i++) {
                newQueue[i] = queue[(front + i) % capacity];
            }
            queue = newQueue;
            front = 0;
            rear = size - 1;
            capacity = newCapacity;
        }
    }

    /**
     * Outputs the current status of the Truck FIFO dispatch line.
     */
    public void displayQueue() {
        System.out.println("\n===== TRUCK DISPATCH LINE (FIFO QUEUE) =====");
        if (isEmpty()) {
            System.out.println("[No Trucks waiting in dispatch queue.]");
            return;
        }
        System.out.print("Front of Queue [Next to Dispatch]: ");
        for (int i = 0; i < size; i++) {
            System.out.print(queue[(front + i) % capacity]);
            if (i < size - 1) {
                System.out.print("  <--  ");
            }
        }
        System.out.println(" [Rear]");
        System.out.println("Total Trucks Ready: " + size);
        System.out.println("============================================");
    }
}
