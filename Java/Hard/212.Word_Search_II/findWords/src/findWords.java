import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class findWords {

    /**
     * Given a 2D board and a list of words from the dictionary, find all words in the board.
     *
     * Each word must be constructed from letters of sequentially adjacent cell, where "adjacent" cells are those horizontally or vertically
     * neighboring. The same letter cell may not be used more than once in a word.
     *
     * Note:
     *
     * All inputs are consist of lowercase letters a-z.
     * The values of words are distinct.
     */
    public List<String> findWords(char[][] board, String[] words) {

    }


    @Test
    public void findWordsTest() {
        /**
         * Example:
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
        Set<String> actual = new HashSet<>(findWords(board, words));
        assertEquals(expected.size(), actual.size());
        for(String word : expected) {
            assertTrue(actual.contains(word));
        }

    }
}
