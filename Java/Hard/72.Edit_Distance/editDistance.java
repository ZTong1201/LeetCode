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
     * <p>
     * Constraints:
     * <p>
     * 0 <= word1.length, word2.length <= 500
     * word1 and word2 consist of lowercase English letters.
     * <p>
     * Approach: DP
     * Use a 2-D dp array to where dp[i][j] denotes the minimum edit distance between word1[0:i] and word1[0:j].
     * 1. If word1[i] == word2[j], then we don't need to change anything here, the min edit distance so far would be the edit
     * distance from dp[i - 1][j - 1]
     * 2. If word1[i] != word2[j], we want to get the minimum edit distance from dp[i - 1][j - 1], dp[i][j - 1] and dp[i - 1][j]
     * and increment one on the minimum (because we either need to delete the current character, replace it or insert a new char
     * at i to make word1[i] == word2[j])
     * <p>
     * Time: O(mn)
     * Space: O(mn)
     */
    public int minDistance(String word1, String word2) {
        int len1 = word1.length(), len2 = word2.length();
        // need one more row and one more column to handle empty scenarios
        int[][] minEditDistance = new int[len1 + 1][len2 + 1];
        // if word1 is an empty string, then the edit distance between word1 and word2.substring(i) will always be i
        // cuz we need to insert one character at a time to make it equal to word2
        for (int i = 1; i <= len2; i++) {
            minEditDistance[0][i] = i;
        }
        // same thing happens when word2 is an empty string - need to delete one character at a time
        for (int i = 1; i <= len1; i++) {
            minEditDistance[i][0] = i;
        }

        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                // since minimum will be taken - assign the value as +inf first
                minEditDistance[i][j] = Integer.MAX_VALUE;
                char c1 = word1.charAt(i - 1);
                char c2 = word2.charAt(j - 1);

                if (c1 == c2) {
                    minEditDistance[i][j] = minEditDistance[i - 1][j - 1];
                } else {
                    // plus one since we need to choose one of the following
                    // delete, insert, replace to make word1[i] == word2[j]
                    minEditDistance[i][j] = Math.min(minEditDistance[i - 1][j - 1], Math.min(
                            minEditDistance[i - 1][j], minEditDistance[i][j - 1])) + 1;
                }
            }
        }
        return minEditDistance[len1][len2];
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
