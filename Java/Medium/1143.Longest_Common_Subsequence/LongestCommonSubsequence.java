import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LongestCommonSubsequence {

    /**
     * Given two strings text1 and text2, return the length of their longest common subsequence. If there is no common
     * subsequence, return 0.
     * <p>
     * A subsequence of a string is a new string generated from the original string with some characters (can be none)
     * deleted without changing the relative order of the remaining characters.
     * <p>
     * For example, "ace" is a subsequence of "abcde".
     * A common subsequence of two strings is a subsequence that is common to both strings.
     * <p>
     * Constraints:
     * <p>
     * 1 <= text1.length, text2.length <= 1000
     * text1 and text2 consist of only lowercase English characters.
     * <p>
     * Approach: DP
     * We can create a 2-D dp array which is of size len1 + 1 x len2 + 1, where dp[i][j] means the length of the longest common
     * subsequence between text1[0:i] and text2[0:j] (non-inclusive). How to make transition?
     * 1. if text1[i] == text2[j], then this character will definitely be part of the LCS, if we know the length of LCS
     * for text1[0:i - 1] and text2[0:j - 1] (say s), then the final length would be s + 1
     * 2. if they are not equal, then we cannot add either character into the LCS for now. The length of LCS will be
     * determined by length between text1[0:i] and text2[0:j - 1] or text1[0:i - 1] and text2[0:j]
     * <p>
     * Time: O(mn)
     * Space: O(mn)
     */
    public int longestCommonSubsequence(String text1, String text2) {
        int len1 = text1.length(), len2 = text2.length();
        int[][] lengthOfLCS = new int[len1 + 1][len2 + 1];

        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                char c1 = text1.charAt(i - 1);
                char c2 = text2.charAt(j - 1);

                if (c1 == c2) {
                    lengthOfLCS[i][j] = lengthOfLCS[i - 1][j - 1] + 1;
                } else {
                    lengthOfLCS[i][j] = Math.max(lengthOfLCS[i][j - 1], lengthOfLCS[i - 1][j]);
                }
            }
        }
        return lengthOfLCS[len1][len2];
    }

    @Test
    public void longestCommonSubsequenceTest() {
        /**
         * Example 1:
         * Input: text1 = "abcde", text2 = "ace"
         * Output: 3
         * Explanation: The longest common subsequence is "ace" and its length is 3.
         */
        assertEquals(3, longestCommonSubsequence("abcde", "ace"));
        /**
         * Example 2:
         * Input: text1 = "abc", text2 = "abc"
         * Output: 3
         * Explanation: The longest common subsequence is "abc" and its length is 3.
         */
        assertEquals(3, longestCommonSubsequence("abc", "abc"));
        /**
         * Example 3:
         * Input: text1 = "abc", text2 = "def"
         * Output: 0
         * Explanation: There is no such common subsequence, so the result is 0.
         */
        assertEquals(0, longestCommonSubsequence("abc", "def"));
    }
}
