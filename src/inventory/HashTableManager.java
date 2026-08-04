package inventory;

import models.UserSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom hash table manager designed with Chaining (Linked List Buckets) for collision resolution.
 * Manages active user sessions inside the application.
 *
 * Time Complexity (Insert): O(1) average, O(N) worst-case (all hash to one bucket).
 * Time Complexity (Search): O(1) average, O(N) worst-case.
 * Time Complexity (Delete): O(1) average, O(N) worst-case.
 * Space Complexity: O(M + N) where M is bucket capacity, N is session count.
 */
public class HashTableManager {

    private static class HashNode {
        private final String key;
        private UserSession val;
        private HashNode next;

        public HashNode(String key, UserSession val) {
            this.key = key;
            this.val = val;
        }
    }

    private final HashNode[] buckets;
    private final int capacity;
    private int size;

    /**
     * Initializes the hash table manager with a prime number capacity to minimize collisions.
     */
    public HashTableManager() {
        this.capacity = 31; // Prime number size
        this.buckets = new HashNode[capacity];
        this.size = 0;
    }

    private int getBucketIndex(String key) {
        int hashCode = key.hashCode();
        int index = hashCode % capacity;
        // Resolve potential negative modulo outputs in Java
        return index < 0 ? index + capacity : index;
    }

    /**
     * Inserts or updates a user session.
     * @param session The active user session.
     */
    public void insert(UserSession session) {
        if (session == null) {
            throw new IllegalArgumentException("Cannot insert null session");
        }
        String key = session.getSessionId();
        int bucketIndex = getBucketIndex(key);
        HashNode head = buckets[bucketIndex];

        // Check if key already exists to perform update
        while (head != null) {
            if (head.key.equals(key)) {
                head.val = session;
                return;
            }
            head = head.next;
        }

        // Insert new session node to the head of the bucket list (O(1) insertion)
        size++;
        head = buckets[bucketIndex];
        HashNode newNode = new HashNode(key, session);
        newNode.next = head;
        buckets[bucketIndex] = newNode;
    }

    /**
     * Searches for a session by its unique ID.
     * @param sessionId Session ID query.
     * @return UserSession if found, otherwise null.
     */
    public UserSession search(String sessionId) {
        if (sessionId == null) return null;
        int bucketIndex = getBucketIndex(sessionId);
        HashNode head = buckets[bucketIndex];

        while (head != null) {
            if (head.key.equals(sessionId)) {
                return head.val;
            }
            head = head.next;
        }
        return null;
    }

    /**
     * Deletes a user session by ID.
     * @param sessionId Session ID to clear.
     * @return True if session was found and deleted, false otherwise.
     */
    public boolean delete(String sessionId) {
        if (sessionId == null) return false;
        int bucketIndex = getBucketIndex(sessionId);
        HashNode head = buckets[bucketIndex];
        HashNode prev = null;

        while (head != null) {
            if (head.key.equals(sessionId)) {
                break;
            }
            prev = head;
            head = head.next;
        }

        if (head == null) {
            return false;
        }

        size--;
        if (prev != null) {
            prev.next = head.next;
        } else {
            buckets[bucketIndex] = head.next;
        }
        return true;
    }

    /**
     * Returns all registered active sessions.
     * @return List of active UserSessions.
     */
    public List<UserSession> getAllSessions() {
        List<UserSession> list = new ArrayList<>();
        for (HashNode bucket : buckets) {
            HashNode head = bucket;
            while (head != null) {
                list.add(head.val);
                head = head.next;
            }
        }
        return list;
    }

    /**
     * Displays bucket mapping layout and chained elements.
     */
    public void displayHashTable() {
        System.out.println("===== USER SESSION HASH TABLE (CHAINING METADATA) =====");
        boolean empty = true;
        for (int i = 0; i < capacity; i++) {
            HashNode head = buckets[i];
            if (head != null) {
                empty = false;
                System.out.print("  Bucket [" + i + "]: ");
                while (head != null) {
                    System.out.print("[" + head.key + " -> " + head.val.getUsername() + " (" + head.val.getRole() + ")]");
                    head = head.next;
                    if (head != null) {
                        System.out.print(" -> ");
                    }
                }
                System.out.println();
            }
        }
        if (empty) {
            System.out.println("  [Hash Table is completely empty]");
        }
    }

    public int getSize() {
        return size;
    }
}
