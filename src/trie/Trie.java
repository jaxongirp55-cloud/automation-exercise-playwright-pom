package trie;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Manual Trie (Prefix Tree) implementation to satisfy BTEC assignment for autocompletion.
 * Supports insert, prefix checking, autocomplete suggestions, and deletion of products/words.
 *
 * Big-O Complexity:
 * - Time Complexity:
 *   - Insertion: O(L) where L is the length of the string.
 *   - Search Prefix / Word: O(L).
 *   - Deletion: O(L).
 *   - Autocomplete: O(P + S) where P is length of prefix, S is total characters in matching sub-trie.
 * - Space Complexity:
 *   - Overall space: O(K * L) where K is number of inserted words, L is average length.
 *   - Search/Insert call stack: O(1) iterative. Deletion stack: O(L) recursive.
 *
 * @author Senior Java Software Architect
 */
public class Trie {
    private final TrieNode root;

    /**
     * Initializes an empty Trie structure.
     */
    public Trie() {
        this.root = new TrieNode();
    }

    /**
     * Inserts a word/product name into the Trie.
     * @param word Word to insert.
     */
    public void insert(String word) {
        if (word == null || word.trim().isEmpty()) {
            return;
        }
        String cleanWord = word.trim().toLowerCase();
        TrieNode current = root;

        for (int i = 0; i < cleanWord.length(); i++) {
            char ch = cleanWord.charAt(i);
            current.getChildren().putIfAbsent(ch, new TrieNode());
            current = current.getChildren().get(ch);
        }
        current.setEndOfWord(true);
    }

    /**
     * Checks if a specific prefix exists in the Trie.
     * @param prefix Prefix to search.
     * @return true if prefix found, false otherwise.
     */
    public boolean startsWith(String prefix) {
        if (prefix == null || prefix.trim().isEmpty()) {
            return false;
        }
        String cleanPrefix = prefix.trim().toLowerCase();
        TrieNode current = root;

        for (int i = 0; i < cleanPrefix.length(); i++) {
            char ch = cleanPrefix.charAt(i);
            TrieNode node = current.getChildren().get(ch);
            if (node == null) {
                return false;
            }
            current = node;
        }
        return true;
    }

    /**
     * Checks if exact word is registered in Trie.
     * @param word Word to match.
     * @return true if registered.
     */
    public boolean search(String word) {
        if (word == null || word.trim().isEmpty()) {
            return false;
        }
        String cleanWord = word.trim().toLowerCase();
        TrieNode current = root;

        for (int i = 0; i < cleanWord.length(); i++) {
            char ch = cleanWord.charAt(i);
            TrieNode node = current.getChildren().get(ch);
            if (node == null) {
                return false;
            }
            current = node;
        }
        return current.isEndOfWord();
    }

    /**
     * Autocomplete suggestions for a given input prefix.
     * Searches matching node and extracts all trailing registered words recursively.
     *
     * @param prefix Prefix query.
     * @return List of matching strings.
     */
    public List<String> autocomplete(String prefix) {
        List<String> results = new ArrayList<>();
        if (prefix == null || prefix.trim().isEmpty()) {
            return results;
        }
        String cleanPrefix = prefix.trim().toLowerCase();
        TrieNode current = root;

        // Traverse down to the end node of prefix
        for (int i = 0; i < cleanPrefix.length(); i++) {
            char ch = cleanPrefix.charAt(i);
            TrieNode node = current.getChildren().get(ch);
            if (node == null) {
                return results; // empty list
            }
            current = node;
        }

        // Recursive gather
        gatherWords(current, cleanPrefix, results);
        return results;
    }

    private void gatherWords(TrieNode node, String currentPrefix, List<String> results) {
        if (node.isEndOfWord()) {
            results.add(currentPrefix);
        }
        for (Map.Entry<Character, TrieNode> child : node.getChildren().entrySet()) {
            gatherWords(child.getValue(), currentPrefix + child.getKey(), results);
        }
    }

    /**
     * Removes a word/product name from the Trie structure.
     * Handles cleaning up of orphaned character nodes gracefully.
     *
     * @param word Word to delete.
     * @return true if successfully found and deleted, false otherwise.
     */
    public boolean delete(String word) {
        if (word == null || word.trim().isEmpty()) {
            return false;
        }
        String cleanWord = word.trim().toLowerCase();
        return deleteRec(root, cleanWord, 0);
    }

    private boolean deleteRec(TrieNode current, String word, int index) {
        if (index == word.length()) {
            // Reached last node. Must be actual end of word.
            if (!current.isEndOfWord()) {
                return false;
            }
            current.setEndOfWord(false);
            // If node has no other branches, it can be deleted from parent
            return current.getChildren().isEmpty();
        }

        char ch = word.charAt(index);
        TrieNode node = current.getChildren().get(ch);
        if (node == null) {
            return false;
        }

        boolean shouldDeleteChild = deleteRec(node, word, index + 1);

        if (shouldDeleteChild) {
            current.getChildren().remove(ch);
            // Return true if current node can also be cleaned up (not part of another word, not end of word)
            return current.getChildren().isEmpty() && !current.isEndOfWord();
        }

        return false;
    }
}
