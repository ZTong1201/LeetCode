import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class editDistance {

    /**
     * Given two words word1 and word2, find the minimum number of operations required to convert word1 to word2.
     * <p>
     * You have the following 3 operations permitted on a word:
     * <p>
     * Insert a character
     * Delete a character
     * Replace a character
     */
    public int minDistance(String word1, String word2) {
        return 0;
    }


    @Test
    public void minDistanceTest() {
        /**
         * Example 1:
         * Input: word1 = "horse", word2 = "ros"
         * Output: 3
         * Explanation:
         * horse -> rorse (replace 'h' with 'r')
         * rorse -> rose (remove 'r')
         * rose -> ros (remove 'e')
         */
        assertEquals(3, minDistance("horse", "ros"));
        /**
         * Example 2:
         * Input: word1 = "intention", word2 = "execution"
         * Output: 5
         * Explanation:
         * intention -> inention (remove 't')
         * inention -> enention (replace 'i' with 'e')
         * enention -> exention (replace 'n' with 'x')
         * exention -> exection (replace 'n' with 'c')
         * exection -> execution (insert 'u')
         */
        assertEquals(5, minDistance("intention", "execution"));
    }
}
