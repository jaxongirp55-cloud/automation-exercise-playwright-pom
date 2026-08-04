package trie;

import java.util.HashMap;
import java.util.Map;

/**
 * Node within the custom Trie structure.
 * Encapsulates child mapping and end-of-word boolean tracking.
 *
 * Big-O Complexity:
 * - Space Complexity: O(1) per node.
 * - Time Complexity: O(1) to access children.
 *
 * @author Senior Java Software Architect
 */
public class TrieNode {
    private final Map<Character, TrieNode> children;
    private boolean isEndOfWord;

    /**
     * Instantiates an empty Trie node.
     */
    public TrieNode() {
        this.children = new HashMap<>();
        this.isEndOfWord = false;
    }

    public Map<Character, TrieNode> getChildren() {
        return children;
    }

    public boolean isEndOfWord() {
        return isEndOfWord;
    }

    public void setEndOfWord(boolean isEndOfWord) {
        this.isEndOfWord = isEndOfWord;
    }
}
