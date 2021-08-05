import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import static org.junit.Assert.assertEquals;

public class LongestWord {

    /**
     * Given an array of strings words representing an English Dictionary, return the longest word in words that can be
     * built one character at a time by other words in words.
     * <p>
     * If there is more than one possible answer, return the longest word with the smallest lexicographical order. If there
     * is no answer, return the empty string.
     * <p>
     * Constraints:
     * <p>
     * 1 <= words.length <= 1000
     * 1 <= words[i].length <= 30
     * words[i] consists of lowercase English letters.
     * <p>
     * Approach 1: Sorting + Hash Set
     * Sort the entire array to make sure the first correct result has the smallest lexicographical order. For each word
     * in the array, enumerate all possible prefixes and check whether it exist in the hash set. We cannot form the world
     * one character at time if any prefixes doesn't exist. If all prefixes exist - update the result to current word if and
     * only if it has a bigger length.
     * <p>
     * Time: O(nlogn + sum(wi^2)) the sorting algorithm takes O(nlogn) time, for each word in the array, we also need to
     * enumerate all possible prefixes to see whether it exists in the hash set already.
     * Space: O(n)
     */
    public String longestWordHashSet(String[] words) {
        Arrays.sort(words);
        Set<String> prefix = new HashSet<>();
        // add an empty string to the set since it's the root of any strings
        prefix.add("");
        String res = "";
        for (String word : words) {
            // create a string builder to enumerate all possible prefixes
            StringBuilder sb = new StringBuilder();
            for (char c : word.toCharArray()) {
                // if the prefix doesn't exist, it's not the result we're looking for
                if (!prefix.contains(sb.toString())) break;
                // append a new character only if the current prefix already exists
                sb.append(c);
            }
            // only update the result when it can be formed one character at a time from other word,
            // and it has a larger length (sorting guarantees the smallest lexicographical order when of same length)
            if (word.length() == sb.toString().length() && word.length() > res.length()) res = word;
            prefix.add(word);
        }
        return res;
    }

    /**
     * Approach 2: Trie + DFS
     * <p>
     * Time: O(N * L) in the worst case we need to create new nodes for every character when building the trie, and we might
     * as well visit all nodes to find the longest word
     * Space: O(N * L)
     */
    private TrieNode head;

    public String longestWordTrie(String[] words) {
        head = new TrieNode();
        insert(words);
        return dfs();
    }

    private String dfs() {
        String res = "";
        Stack<TrieNode> stack = new Stack<>();
        stack.push(head);

        while (!stack.isEmpty()) {
            TrieNode curr = stack.pop();
            String word = curr.getWord();

            // only update result when current node is the end of a word
            if (word != null) {
                // update word if a longer string is found, or it has a smaller lexicographical order
                if (word.length() > res.length() || word.length() == res.length() && word.compareTo(res) < 0) {
                    res = word;
                }
            }
            // add non-null child who is also the end of a word to the stack
            for (TrieNode child : curr.getChildren()) {
                if (child != null && child.getWord() != null) stack.push(child);
            }
        }
        return res;
    }

    private void insert(String[] words) {
        for (String word : words) {
            TrieNode ptr = head;
            for (char c : word.toCharArray()) {
                if (!ptr.containsKey(c)) ptr.put(c);
                ptr = ptr.get(c);
            }
            ptr.setWord(word);
        }
    }

    private static class TrieNode {
        private String word;
        private TrieNode[] children;

        public TrieNode() {
            this.children = new TrieNode[26];
        }

        public TrieNode[] getChildren() {
            return this.children;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public String getWord() {
            return this.word;
        }

        public boolean containsKey(char c) {
            return this.children[c - 'a'] != null;
        }

        public void put(char c) {
            this.children[c - 'a'] = new TrieNode();
        }

        public TrieNode get(char c) {
            return this.children[c - 'a'];
        }
    }

    @Test
    public void longestWordTest() {
        /**
         * Example 1:
         * Input: words = ["w","wo","wor","worl","world"]
         * Output: "world"
         * Explanation: The word "world" can be built one character at a time by "w", "wo", "wor", and "worl".
         */
        assertEquals("world", longestWordHashSet(new String[]{"w", "wo", "wor", "worl", "world"}));
        assertEquals("world", longestWordTrie(new String[]{"w", "wo", "wor", "worl", "world"}));
        /**
         * Example 2:
         * Input: words = ["a","banana","app","appl","ap","apply","apple"]
         * Output: "apple"
         * Explanation: Both "apply" and "apple" can be built from other words in the dictionary. However, "apple"
         * is lexicographically smaller than "apply".
         */
        assertEquals("apple", longestWordHashSet(new String[]{"a", "banana", "app", "appl", "ap", "apply", "apple"}));
        assertEquals("apple", longestWordTrie(new String[]{"a", "banana", "app", "appl", "ap", "apply", "apple"}));
    }
}
