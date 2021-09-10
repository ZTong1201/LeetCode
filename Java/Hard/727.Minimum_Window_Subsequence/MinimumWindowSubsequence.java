import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MinimumWindowSubsequence {

    /**
     * Given strings s1 and s2, return the minimum contiguous substring part of s1, so that s2 is a subsequence of the part.
     * <p>
     * If there is no such window in s1 that covers all characters in s2, return the empty string "". If there are multiple
     * such minimum-length windows, return the one with the left-most starting index.
     * <p>
     * Constraints:
     * <p>
     * 1 <= s1.length <= 2 * 10^4
     * 1 <= s2.length <= 100
     * s1 and s2 consist of lowercase English letters.
     * <p>
     * Approach 1: DP
     * Denote dp[i][j] = s indicates that the largest index in substring s1[s:j + 1] in which the substring s2[0:i] is a
     * subsequence.
     * Basically, the rows are characters in s2, whereas the columns are characters in s1. We need a 2-D array of size
     * (s1 + 1) * (s2 + 1). Why because index i indicates non-inclusive in the string, e.g. dp[0][0] means no character
     * is selected from both strings.
     * We have two scenarios:
     * 1. If s1[j] == s2[i], then we can reuse the index recorded in dp[i - 1][j - 1] meaning adding the current character
     * will include one more character of s2.
     * 2. If s1[j] != s2[i], then we just carry whatever we got in dp[i][j - 1]
     * <p>
     * When DP is done, we loop through the last row to find the shortest length of substring which has s2 as a subsequence.
     * <p>
     * Time: O(mn)
     * Space: O(mn)
     */
    public String minWindowDP(String s1, String s2) {
        int length1 = s1.length(), length2 = s2.length();
        // rows - characters in s2
        // columns - characters in s1
        int[][] dp = new int[length2 + 1][length1 + 1];
        // initialization
        // the empty string is a subsequence of any substrings with length 1
        for (int i = 0; i <= length1; i++) {
            dp[0][i] = i + 1;
        }

        for (int i = 1; i <= length2; i++) {
            for (int j = 1; j <= length1; j++) {
                // reuse the previous start index to which contains a short subsequence
                if (s1.charAt(j - 1) == s2.charAt(i - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // otherwise, the current character should be included anyway, get the start index from previous position
                    dp[i][j] = dp[i][j - 1];
                }
            }
        }

        String res = "";
        int minLength = Integer.MAX_VALUE;
        // iterate over the last line to find the shortest length with minimum start index
        for (int i = 1; i <= length1; i++) {
            // start index is 0 means the substring from 0 to current index doesn't contain s2 as a subsequence
            if (dp[length2][i] != 0 && i - dp[length2][i] + 1 < minLength) {
                minLength = i - dp[length2][i] + 1;
                // the start index should always be subtracted by 1
                res = s1.substring(dp[length2][i] - 1, i);
            }
        }
        return res;
    }

    /**
     * Approach 2: Two pointers
     * We can try to solve it in one pass. Essentially, for a given string, we start from the very beginning and trying to cover
     * all the characters in s2 (from left to right). However, we might have had a super long string with duplicate characters.
     * We then start from the very right boundary to the left and squeeze out some characters (shrink the window) to see
     * whether we can actually form a shorter string.
     * <p>
     * Time: O(s1) each character will be added and removed from the window once
     * Space: O(s1 + s2) convert string to array for easier manipulation
     */
    public String minWindowTwoPointers(String s1, String s2) {
        char[] s1CharArray = s1.toCharArray(), s2CharArray = s2.toCharArray();
        int s1Index = 0, s2Index = 0, start = 0, minLength = Integer.MAX_VALUE;

        // find the shortest substring in s1
        while (s1Index < s1CharArray.length) {
            // if we find a matched character
            if (s1CharArray[s1Index] == s2CharArray[s2Index]) {
                // we can jump to the next character in s2
                s2Index++;
                // if all characters have been found in s1 until s1Index
                if (s2Index == s2CharArray.length) {
                    // keep track of the current right boundary
                    int rightBound = s1Index + 1;

                    // decrement s2Index hence it's pointing to the last character of s2
                    s2Index--;
                    // start from right to left to see whether there is a shorter string
                    // contains s2 as a subsequence
                    // e.g. from the beginning we have "abcde" which contains "bde" as a subsequence
                    // however, "bcde" is the actual substring because character 'a' is redundant
                    while (s2Index >= 0) {
                        // if we find a same character, jump backward in s2
                        if (s1CharArray[s1Index] == s2CharArray[s2Index]) {
                            s2Index--;
                        }
                        // always jump back in s1
                        s1Index--;
                    }

                    // increment both pointers to jump back into the window
                    // where s1Index is pointing to the start index of a substring
                    // s2Index will be reinitialized to 0
                    s2Index++;
                    s1Index++;
                    // check if we find a shorter substring
                    if (rightBound - s1Index < minLength) {
                        // update the shortest length so far and update the current start index
                        minLength = rightBound - s1Index;
                        start = s1Index;
                    }
                }
            }
            // always jump to the next character in s1
            s1Index++;
        }
        // if min length has not been updated, which means such a substring doesn't exist
        return minLength == Integer.MAX_VALUE ? "" : s1.substring(start, start + minLength);
    }

    @Test
    public void minWindowTest() {
        /**
         * Example 1:
         * Input: s1 = "abcdebdde", s2 = "bde"
         * Output: "bcde"
         * Explanation:
         * "bcde" is the answer because it occurs before "bdde" which has the same length.
         * "deb" is not a smaller window because the elements of s2 in the window must occur in order.
         */
        assertEquals("bcde", minWindowDP("abcdebdde", "bde"));
        assertEquals("bcde", minWindowTwoPointers("abcdebdde", "bde"));
        /**
         * Example 2:
         * Input: s1 = "jmeqksfrsdcmsiwvaovztaqenprpvnbstl", s2 = "u"
         * Output: ""
         */
        assertEquals("", minWindowDP("jmeqksfrsdcmsiwvaovztaqenprpvnbstl", "u"));
        assertEquals("", minWindowTwoPointers("jmeqksfrsdcmsiwvaovztaqenprpvnbstl", "u"));
    }
}
