import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SearchSuggestionsSystem {

    /**
     * Given an array of strings products and a string searchWord. We want to design a system that suggests at most three
     * product names from products after each character of searchWord is typed. Suggested products should have common prefix
     * with the searchWord. If there are more than three products with a common prefix return the three lexicographically
     * minimums products.
     * <p>
     * Return list of lists of the suggested products after each character of searchWord is typed.
     * <p>
     * Approach: Trie + DFS
     * Since we only have lower-case English letters in the input, it could be convenient to use Trie for prefix search.
     * In addition, the nodes in trie at the same level are already sorted ('a' -> 'z'). So if we stop traversing we there are
     * already 3 suggestion - then they will be guaranteed to be the smallest lexicographical order.
     * <p>
     * Time: O(M * N * L) where N is the length of input array, L is the average length of product. Hence, we'll have N * L
     * nodes in the trie. M is the length of search word, we need to search M times in the trie to find suggestions.
     * Space: O(N * L) there are N * L nodes in the array
     */
    public List<List<String>> suggestedProducts(String[] products, String searchWord) {
        TrieNode head = new TrieNode();
        // insert product into the trie
        for (String product : products) {
            insert(head, product);
        }

        List<List<String>> res = new ArrayList<>();
        for (int i = 0; i < searchWord.length(); i++) {
            res.add(startsWith(head, searchWord.substring(0, i + 1)));
        }
        return res;
    }

    private void dfs(TrieNode head, List<String> suggestions) {
        // base case - stop traversing when
        // 1. cannot keep searching
        // 2. already found 3 suggestions
        if (head == null || suggestions.size() == 3) return;
        // only add word into the list
        if (head.getWord() != null) {
            suggestions.add(head.getWord());
        }

        // search all possible characters from 'a' to 'z'
        for (TrieNode child : head.getChildren()) {
            dfs(child, suggestions);
        }
    }

    private List<String> startsWith(TrieNode head, String prefix) {
        List<String> suggestions = new ArrayList<>();
        for (int i = 0; i < prefix.length(); i++) {
            char curr = prefix.charAt(i);
            // if the prefix doesn't event exist in the trie
            // return the empty list
            if (!head.containsKey(curr)) return suggestions;
            // otherwise, move to the next node
            head = head.get(curr);
        }
        // the prefix has been found in the trie
        // subsequently, we need to start from the current node and add at most 3 products into the list
        dfs(head, suggestions);
        return suggestions;
    }

    private void insert(TrieNode head, String product) {
        for (int i = 0; i < product.length(); i++) {
            char curr = product.charAt(i);
            if (!head.containsKey(curr)) head.put(curr);
            head = head.get(curr);
        }
        head.setWord(product);
    }

    private static class TrieNode {
        private final TrieNode[] children;
        private String word;

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
    public void suggestedProductsTest() {
        /**
         * Example 1:
         * Input: products = ["mobile","mouse","moneypot","monitor","mousepad"], searchWord = "mouse"
         * Output: [
         * ["mobile","moneypot","monitor"],
         * ["mobile","moneypot","monitor"],
         * ["mouse","mousepad"],
         * ["mouse","mousepad"],
         * ["mouse","mousepad"]
         * ]
         * Explanation: products sorted lexicographically = ["mobile","moneypot","monitor","mouse","mousepad"]
         * After typing m and mo all products match and we show user ["mobile","moneypot","monitor"]
         * After typing mou, mous and mouse the system suggests ["mouse","mousepad"]
         */
        List<List<String>> expected1 = List.of(List.of("mobile", "moneypot", "monitor"), List.of("mobile", "moneypot", "monitor"),
                List.of("mouse", "mousepad"), List.of("mouse", "mousepad"), List.of("mouse", "mousepad"));
        List<List<String>> actual1 = suggestedProducts(new String[]{"mobile", "mouse", "moneypot", "monitor", "mousepad"}, "mouse");
        assertEquals(expected1.size(), actual1.size());
        for (int i = 0; i < expected1.size(); i++) {
            assertEquals(expected1.get(i), actual1.get(i));
        }
        /**
         * Example 2:
         * Input: products = ["havana"], searchWord = "havana"
         * Output: [["havana"],["havana"],["havana"],["havana"],["havana"],["havana"]]
         */
        List<List<String>> expected2 = List.of(List.of("havana"), List.of("havana"),
                List.of("havana"), List.of("havana"), List.of("havana"), List.of("havana"));
        List<List<String>> actual2 = suggestedProducts(new String[]{"havana"}, "havana");
        assertEquals(expected2.size(), actual2.size());
        for (int i = 0; i < expected2.size(); i++) {
            assertEquals(expected2.get(i), actual2.get(i));
        }
        /**
         * Example 3:
         * Input: products = ["bags","baggage","banner","box","cloths"], searchWord = "bags"
         * Output: [["baggage","bags","banner"],["baggage","bags","banner"],["baggage","bags"],["bags"]]
         */
        List<List<String>> expected3 = List.of(List.of("baggage", "bags", "banner"), List.of("baggage", "bags", "banner"),
                List.of("baggage", "bags"), List.of("bags"));
        List<List<String>> actual3 = suggestedProducts(new String[]{"bags", "baggage", "banner", "box", "cloths"}, "bags");
        assertEquals(expected3.size(), actual3.size());
        for (int i = 0; i < expected3.size(); i++) {
            assertEquals(expected3.get(i), actual3.get(i));
        }
    }
}
