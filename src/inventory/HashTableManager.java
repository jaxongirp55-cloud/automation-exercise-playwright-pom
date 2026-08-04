package inventory;

/**
 * Custom HashMap implementation for managing User Sessions or Product Tracking.
 * Resolves collision overheads using Linked List Chaining.
 * Implements clean SOLID coding standards with zero reliance on java.util.HashMap.
 *
 * Big-O Complexity:
 * - Time Complexity:
 *   - Put (insert): O(1) average, O(N) worst case if resizing or bad hashing occurs.
 *   - Get (search): O(1) average, O(N) worst case.
 *   - Remove (delete): O(1) average, O(N) worst case.
 * - Space Complexity: O(M + N) where M is bucket count, N is size.
 *
 * @author Senior Java Software Architect
 */
public class HashTableManager<K, V> {

    /**
     * Entry Node holding key-value mappings.
     */
    public static class Entry<K, V> {
        private final K key;
        private V value;
        private Entry<K, V> next;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public Entry<K, V> getNext() {
            return next;
        }

        public void setNext(Entry<K, V> next) {
            this.next = next;
        }
    }

    private Entry<K, V>[] table;
    private int capacity;
    private int size;
    private static final double LOAD_FACTOR_THRESHOLD = 0.75;

    @SuppressWarnings("unchecked")
    public HashTableManager(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be positive.");
        }
        this.capacity = initialCapacity;
        this.table = new Entry[capacity];
        this.size = 0;
    }

    public HashTableManager() {
        this(16);
    }

    /**
     * Calculates bucket array mapping via hash code function.
     */
    private int hash(K key) {
        if (key == null) return 0;
        return Math.abs(key.hashCode() % capacity);
    }

    /**
     * Inserts or updates a key-value mapping.
     * @param key Entry mapping key.
     * @param value Entry mapping value.
     */
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Null keys are not supported in HashTableManager.");
        }
        int bucketIndex = hash(key);
        Entry<K, V> head = table[bucketIndex];

        // Check if key already exists, then update value
        while (head != null) {
            if (head.getKey().equals(key)) {
                head.setValue(value);
                return;
            }
            head = head.getNext();
        }

        // Key doesn't exist, insert new entry at bucket head
        Entry<K, V> newEntry = new Entry<>(key, value);
        newEntry.setNext(table[bucketIndex]);
        table[bucketIndex] = newEntry;
        size++;

        // Verify if resize is needed
        if ((double) size / capacity >= LOAD_FACTOR_THRESHOLD) {
            resize();
        }
    }

    /**
     * Searches and retrieves value associated with key.
     * @param key Key to search.
     * @return Value if exists, or null.
     */
    public V get(K key) {
        if (key == null) return null;
        int bucketIndex = hash(key);
        Entry<K, V> current = table[bucketIndex];
        while (current != null) {
            if (current.getKey().equals(key)) {
                return current.getValue();
            }
            current = current.getNext();
        }
        return null;
    }

    /**
     * Removes key-value mapping if present.
     * @param key Key to delete.
     * @return Associated value that was removed, or null if key is missing.
     */
    public V remove(K key) {
        if (key == null) return null;
        int bucketIndex = hash(key);
        Entry<K, V> current = table[bucketIndex];
        Entry<K, V> prev = null;

        while (current != null) {
            if (current.getKey().equals(key)) {
                if (prev == null) {
                    table[bucketIndex] = current.getNext();
                } else {
                    prev.setNext(current.getNext());
                }
                size--;
                return current.getValue();
            }
            prev = current;
            current = current.getNext();
        }
        return null;
    }

    /**
     * Returns true if HashTable contains key.
     * @param key The search key.
     * @return true if registered.
     */
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    /**
     * Resizes internal bucket array to double the size.
     */
    @SuppressWarnings("unchecked")
    private void resize() {
        int oldCapacity = capacity;
        Entry<K, V>[] oldTable = table;

        capacity = oldCapacity * 2;
        table = new Entry[capacity];
        size = 0;

        for (int i = 0; i < oldCapacity; i++) {
            Entry<K, V> entry = oldTable[i];
            while (entry != null) {
                put(entry.getKey(), entry.getValue());
                entry = entry.getNext();
            }
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Prints complete internal state of HashTable showing bucket links.
     */
    public void displayTable() {
        System.out.println("\n===== IN-MEMORY HASH TABLE STATUS =====");
        if (isEmpty()) {
            System.out.println("[Hash Table is empty.]");
            return;
        }
        for (int i = 0; i < capacity; i++) {
            Entry<K, V> entry = table[i];
            if (entry != null) {
                System.out.print("Bucket " + i + ": ");
                while (entry != null) {
                    System.out.print("[" + entry.getKey() + " => " + entry.getValue() + "]");
                    entry = entry.getNext();
                    if (entry != null) {
                        System.out.print(" -> ");
                    }
                }
                System.out.println();
            }
        }
        System.out.println("========================================");
    }

    /**
     * Retrieves all active values.
     * @return List of values inside Hash Table.
     */
    public java.util.List<V> getAllValues() {
        java.util.List<V> values = new java.util.ArrayList<>();
        for (int i = 0; i < capacity; i++) {
            Entry<K, V> entry = table[i];
            while (entry != null) {
                values.add(entry.getValue());
                entry = entry.getNext();
            }
        }
        return values;
    }
}
