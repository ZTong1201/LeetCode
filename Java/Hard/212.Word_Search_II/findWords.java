import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class findWords {

    /**
     * Given a 2D board and a list of words from the dictionary, find all words in the board.
     * <p>
     * Each word must be constructed from letters of sequentially adjacent cell, where "adjacent" cells are those horizontally or vertically
     * neighboring. The same letter cell may not be used more than once in a word.
     * <p>
     * Note:
     * <p>
     * All inputs are consist of lowercase letters a-z.
     * The values of words are distinct.
     * <p>
     * Approach 1: Backtrack in Board
     * Similar to LeetCode 79: https://leetcode.com/problems/word-search/
     * We search each word in the grid and if it exists, add it to the final list. Note that there might be duplicate
     * words in the grid, hence we can keep a set to avoid duplicate.
     * <p>
     * Time: O(mns) where grid is of size mxn, and the length of words array is s. For each word, we might need to search
     * the entire grid in the worst case.
     * Space: O(min(m, n)) for the call stack
     */
    public List<String> findWordsBacktrackInBoard(char[][] board, String[] words) {
        Set<String> res = new HashSet<>();
        for (String word : words) {
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[0].length; j++) {
                    // only start search the current character equals to the start letter
                    if (board[i][j] == word.charAt(0) && search(board, 0, word, i, j)) {
                        res.add(word);
                    }
                }
            }
        }
        return new ArrayList<>(res);
    }

    private boolean search(char[][] board, int index, String word, int i, int j) {
        // base case
        // 1 - search outside the grid
        // 2 - reach a visited cell ('.')
        // 3 - cannot form the word
        if (i < 0 || j < 0 || i >= board.length || j >= board[0].length ||
                board[i][j] == '.' || board[i][j] != word.charAt(index)) return false;
        // if all characters were found - return true;
        if (index == word.length() - 1) return true;
        // mark the cell as visited
        char currChar = board[i][j];
        board[i][j] = '.';

        // search 4 neighbors
        boolean found = search(board, index + 1, word, i + 1, j) || search(board, index + 1, word, i - 1, j)
                || search(board, index + 1, word, i, j + 1) || search(board, index + 1, word, i, j - 1);

        // backtrack
        board[i][j] = currChar;
        return found;
    }

    /**
     * Approach 2: Backtrack in Trie
     * The search in approach 1 will be very time-consuming if the number of words to be searched is really large. Since
     * the size of the grid itself is relatively small, we would consider put all words into a trie and search the grid
     * characters in that trie.
     * How?
     * We insert words into the trie and mark them as word. Then traverse each cell in the grid, if we encounter a character
     * which is a starting letter of a word, we start searching from there. Each time, we search its 4 neighbors and if any
     * of them corresponds to a next character node - we keep searching on the route until a whole word was found - we
     * mark the word as null after searching to avoid duplicates.
     * <p>
     * Time: O(4*3^(L - 1)) where L is the maximum length of input words. The actual time complexity is very tricky to
     * compute. However, we can think about this. If there is a starting letter, we need first search its 4 neighbors,
     * after that, its neighbor will have 3 neighbors to be searched further (we don't want to circle back to lead to
     * cycle). We keep doing this for the rest of length, which is L - 1. Hence the upper bound should be O(4 * 3^(L - 1))
     * Space: O(n * L) where n is the number of input words. Suppose there is no overlap between any two words, we would
     * have n branches for n words and each word will have up to L nodes.
     */
    private List<String> res;

    public List<String> findWordsBacktrackInTrie(char[][] board, String[] words) {
        // add all words in the trie
        TrieNode head = new TrieNode();
        for (String word : words) {
            insert(word, head);
        }

        res = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                // only search if the start letter is the same
                if (head.containsKey(board[i][j])) {
                    search(board, i, j, head);
                }
            }
        }
        return res;
    }

    private void search(char[][] board, int i, int j, TrieNode root) {
        // base case
        if (i < 0 || j < 0 || i >= board.length || j >= board[0].length ||
                board[i][j] == '.' || !root.containsKey(board[i][j])) return;

        // the current character exists in the trie
        // move to the next node for further search
        char currChar = board[i][j];
        root = root.get(currChar);
        // if we find a word - add it into the list
        if (root.getWord() != null) {
            res.add(root.getWord());
            // also set the word to null to avoid duplicate visit
            root.setWord(null);
        }

        // mark the cell as visited
        board[i][j] = '.';

        // search 4 neighbors
        search(board, i + 1, j, root);
        search(board, i - 1, j, root);
        search(board, i, j + 1, root);
        search(board, i, j - 1, root);

        // backtrack
        board[i][j] = currChar;
    }

    private void insert(String word, TrieNode root) {
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (!root.containsKey(c)) root.put(c);
            root = root.get(c);
        }
        root.setWord(word);
    }


    @Test
    public void findWordsTest() {
        /**
         * Example 1:
         * Input:
         * board = [
         *   ['o','a','a','n'],
         *   ['e','t','a','e'],
         *   ['i','h','k','r'],
         *   ['i','f','l','v']
         * ]
         * words = ["oath","pea","eat","rain"]
         *
         * Output: ["eat","oath"]
         */
        char[][] board = new char[][]{
                {'o', 'a', 'a', 'n'},
                {'e', 't', 'a', 'e'},
                {'i', 'h', 'k', 'r'},
                {'i', 'f', 'l', 'v'}};
        String[] words = new String[]{"oath", "pea", "eat", "rain"};
        Set<String> expected = new HashSet<>(Arrays.asList("eat", "oath"));
        Set<String> actual1 = new HashSet<>(findWordsBacktrackInBoard(board, words));
        assertEquals(expected.size(), actual1.size());
        for (String word : expected) {
            assertTrue(actual1.contains(word));
        }
        Set<String> actual11 = new HashSet<>(findWordsBacktrackInTrie(board, words));
        assertEquals(expected.size(), actual11.size());
        for (String word : expected) {
            assertTrue(actual11.contains(word));
        }
        /**
         * Example 2:
         * Input: board = [["a","b"],["c","d"]], words = ["abcb"]
         * Output: []
         */
        List<String> actual21 = findWordsBacktrackInBoard(new char[][]{{'a', 'b'}, {'c', 'd'}}, new String[]{"abcd"});
        assertTrue(actual21.isEmpty());
        List<String> actual22 = findWordsBacktrackInTrie(new char[][]{{'a', 'b'}, {'c', 'd'}}, new String[]{"abcd"});
        assertTrue(actual21.isEmpty());
        assertTrue(actual22.isEmpty());
    }

    private static class TrieNode {
        private final TrieNode[] children;
        private String word;

        public TrieNode() {
            children = new TrieNode[26];
        }

        public TrieNode get(char c) {
            return children[c - 'a'];
        }

        public boolean containsKey(char c) {
            return children[c - 'a'] != null;
        }

        public void put(char c) {
            children[c - 'a'] = new TrieNode();
        }

        public void setWord(String word) {
            this.word = word;
        }

        public String getWord() {
            return word;
        }
    }
}
