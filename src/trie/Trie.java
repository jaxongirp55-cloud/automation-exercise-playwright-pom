package trie;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Manually implemented Trie Data Structure.
 * Ideal for storing product catalogs or warehouse route codes, allowing instant O(L) searches.
 * Supports Insert, Delete, Search, Prefix Search, and Autocomplete.
 *
 * Time Complexity (Insert): O(L) where L is the length of the string.
 * Time Complexity (Search/Prefix Search): O(L)
 * Time Complexity (Delete): O(L)
 * Time Complexity (Autocomplete): O(P + V) where P is the length of prefix, V is nodes visited in subtree.
 * Space Complexity: O(N * L) where N is number of words inserted, L is average length.
 */
public class Trie {
    private final TrieNode root;

    public Trie() {
        this.root = new TrieNode();
    }

    /**
     * Inserts a key and its optional description into the Trie.
     * @param word Key string.
     * @param description Custom description associated with key.
     */
    public void insert(String word, String description) {
        if (word == null || word.trim().isEmpty()) return;
        TrieNode current = root;
        String normalized = word.toLowerCase().trim();

        for (int i = 0; i < normalized.length(); i++) {
            char ch = normalized.charAt(i);
            current = current.getChildren().computeIfAbsent(ch, k -> new TrieNode());
        }
        current.setEndOfWord(true);
        current.setDescription(description);
    }

    /**
     * Searches for exact matching key in the Trie.
     * @param word Key to look up.
     * @return Description text, or null if key does not exist.
     */
    public String search(String word) {
        if (word == null || word.trim().isEmpty()) return null;
        TrieNode node = searchNode(word);
        return (node != null && node.isEndOfWord()) ? node.getDescription() : null;
    }

    /**
     * Verifies if any keys in the Trie start with the specified prefix.
     * @param prefix Prefix query.
     * @return True if prefix match is found, false otherwise.
     */
    public boolean startsWith(String prefix) {
        if (prefix == null || prefix.trim().isEmpty()) return false;
        return searchNode(prefix) != null;
    }

    /**
     * Autocomplete feature returning all matching keys starting with a prefix.
     * @param prefix String prefix to autocomplete from.
     * @return List of autocompleted keywords.
     */
    public List<String> autocomplete(String prefix) {
        List<String> results = new ArrayList<>();
        if (prefix == null) return results;
        String normalized = prefix.toLowerCase().trim();
        TrieNode prefixNode = searchNode(normalized);
        if (prefixNode == null) {
            return results;
        }
        collectWords(prefixNode, normalized, results);
        return results;
    }

    /**
     * Deletes a word from the Trie.
     * @param word Key to delete.
     * @return True if deleted successfully.
     */
    public boolean delete(String word) {
        if (word == null || word.trim().isEmpty()) return false;
        return deleteRec(root, word.toLowerCase().trim(), 0);
    }

    private boolean deleteRec(TrieNode current, String word, int index) {
        if (index == word.length()) {
            if (!current.isEndOfWord()) {
                return false;
            }
            current.setEndOfWord(false);
            current.setDescription(null);
            return current.getChildren().isEmpty();
        }

        char ch = word.charAt(index);
        TrieNode node = current.getChildren().get(ch);
        if (node == null) {
            return false;
        }

        boolean shouldDeleteCurrentNode = deleteRec(node, word, index + 1);

        if (shouldDeleteCurrentNode) {
            current.getChildren().remove(ch);
            return current.getChildren().isEmpty() && !current.isEndOfWord();
        }
        return false;
    }

    private TrieNode searchNode(String prefix) {
        TrieNode current = root;
        String normalized = prefix.toLowerCase().trim();
        for (int i = 0; i < normalized.length(); i++) {
            char ch = normalized.charAt(i);
            TrieNode node = current.getChildren().get(ch);
            if (node == null) {
                return null;
            }
            current = node;
        }
        return current;
    }

    private void collectWords(TrieNode node, String prefix, List<String> results) {
        if (node.isEndOfWord()) {
            results.add(prefix + " -> " + node.getDescription());
        }
        for (Map.Entry<Character, TrieNode> entry : node.getChildren().entrySet()) {
            collectWords(entry.getValue(), prefix + entry.getKey(), results);
        }
    }
}
