package trie;

import java.util.HashMap;
import java.util.Map;

/**
 * Node representation in a Trie structure.
 *
 * Time Complexity (Creation): O(1)
 * Space Complexity: O(1) character container.
 */
public class TrieNode {
    private final Map<Character, TrieNode> children;
    private boolean isEndOfWord;
    private String description; // metadata associated with the search key

    public TrieNode() {
        this.children = new HashMap<>();
        this.isEndOfWord = false;
        this.description = null;
    }

    public Map<Character, TrieNode> getChildren() {
        return children;
    }

    public boolean isEndOfWord() {
        return isEndOfWord;
    }

    public void setEndOfWord(boolean endOfWord) {
        isEndOfWord = endOfWord;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
