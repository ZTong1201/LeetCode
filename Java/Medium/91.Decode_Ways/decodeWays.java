import org.junit.Test;
import static org.junit.Assert.*;

public class decodeWays {

    /**
     * A message containing letters from A-Z is being encoded to numbers using the following mapping:
     *
     * 'A' -> 1
     * 'B' -> 2
     * ...
     * 'Z' -> 26
     * Given a non-empty string containing only digits, determine the total number of ways to decode it.
     *
     * Approach 1: Dynamic Programming with Extra Space
     *
     * Initialize an integer array to record how many ways of decoding at current location. The size of array is length + 1, which means
     * dp[i] is the number of decoding ways of substring from 0 to i - 1. Initialize dp[0] = 1, and dp[1] equals 1 if the first element of
     * string is not '0'. For a given index in the string, we need to look back two indices, that's the reason why we initialize dp[0] as 1,
     * because that is for index 1 in the string. If current char is not '0', there are at least dp[i - 1] ways to decode so far. If
     * the previous element along with current element can sum to a value >= 10 or <= 27. (If we have something like 09 or 27, which means
     * combining these two chars is invalid), there are also dp[i - 2] ways to decode here. For example, if we have xxxxxxx123, if we want
     * to know how many ways to decode at 3. First we know, there are at least the number of ways to decode 2 that we can decode 3. (simply
     * replace 3 by 'C'). Besides, we can treat 12 as a number and decode it as 'l'. Hence there are number of ways to decode prior to 12.
     * Hence, in the best case we have dp[i] = dp[i - 1] + dp[i - 2] ways.
     *
     * Time: O(n) single one-pass
     * Space: O(n) we need an integer array to store current number of ways to decode
     */
    public int numDecodingsDPWithSpace(String s) {
        int[] dp = new int[s.length() + 1];
        //initialize an empty string with 1
        dp[0] = 1;
        dp[1] = s.charAt(0) == '0' ? 0 : 1;

        for(int i = 2; i <= s.length(); i++) {
            if(s.charAt(i - 1) != '0') {
                //if previous char is not '0', we have at least dp[i - 1] ways to decode
                dp[i] += dp[i - 1];
            }

            int val = s.charAt(i - 1) - '0' + (s.charAt(i - 2) - '0') * 10; //the double-digit number before current index
            if(val >= 10 && val <= 26) {
                //make sure we don't have something like "09" or "27", which are invalid
                //we have dp[i - 2] more ways to decode by combining the previous two numbers as a single double-digit number
                dp[i] += dp[i - 2];
            }
        }
        return dp[dp.length - 1];
    }

    @Test
    public void numDecodingsDPWithSpaceTest() {
        /**
         * Example 1:
         * Input: "12"
         * Output: 2
         * Explanation: It could be decoded as "AB" (1 2) or "L" (12).
         */
        assertEquals(2, numDecodingsDPWithSpace("12"));
        /**
         * Example 2:
         * Input: "226"
         * Output: 3
         * Explanation: It could be decoded as "BZ" (2 26), "VF" (22 6), or "BBF" (2 2 6).
         */
        assertEquals(3, numDecodingsDPWithSpace("226"));
        /**
         * Example 3:
         * Input: "0"
         * Output: 0
         */
        assertEquals(0, numDecodingsDPWithSpace("0"));
        /**
         * Example 4:
         * Input: "101"
         * Output: 1
         */
        assertEquals(1, numDecodingsDPWithSpace("101"));
    }

    /**
     * Approach 2: DP without Extra Space
     *
     * We actually don't need the entire array. We only need two variables to store one-step ahead decoding ways and the two-step
     * ahead decoding ways. If the previous char is '0', (which means the double digit number % 10 is 0), we just have the number of ways
     * equals to two-step ahead only if the number is >= 10 or <= 26 (because 0 cannot be decoded). Otherwise, we have no way to decode
     * (set the result to 0). Remember to update the two-step ahead decoding ways to one-step ahead, because we move one step further.
     *
     * Time: O(n)
     * Space: O(1)
     */
    public int numDecodingsDPWithoutSpace(String s) {
        int first = 1;
        int second = s.charAt(0) == '0' ? 0 : 1;

        for(int i = 2; i <= s.length(); i++) {
            int val = s.charAt(i - 1) - '0' + (s.charAt(i - 2) - '0') * 10;
            int tmp = second;
            if(val % 10 == 0) second = 0;
            if(val >= 10 && val <= 26) {
                second += first;
            }
            first = tmp;
        }
        return second;
    }

    @Test
    public void numDecodingsDPWithoutSpaceTest() {
        /**
         * Example 1:
         * Input: "12"
         * Output: 2
         * Explanation: It could be decoded as "AB" (1 2) or "L" (12).
         */
        assertEquals(2, numDecodingsDPWithoutSpace("12"));
        /**
         * Example 2:
         * Input: "226"
         * Output: 3
         * Explanation: It could be decoded as "BZ" (2 26), "VF" (22 6), or "BBF" (2 2 6).
         */
        assertEquals(3, numDecodingsDPWithoutSpace("226"));
        /**
         * Example 3:
         * Input: "0"
         * Output: 0
         */
        assertEquals(0, numDecodingsDPWithoutSpace("0"));
        /**
         * Example 4:
         * Input: "101"
         * Output: 1
         */
        assertEquals(1, numDecodingsDPWithoutSpace("101"));
    }
}
