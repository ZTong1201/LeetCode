import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class LongestSubsequence {

    /**
     * Given an integer array arr and an integer difference, return the length of the longest subsequence in arr
     * which is an arithmetic sequence such that the difference between adjacent elements in the subsequence
     * equals difference.
     * <p>
     * A subsequence is a sequence that can be derived from arr by deleting some or no elements without changing
     * the order of the remaining elements.
     * <p>
     * Constraints:
     * <p>
     * 1 <= arr.length <= 105
     * -104 <= arr[i], difference <= 104
     * <p>
     * Approach: HashMap + DP
     * At index i, in order to know the longest subsequence until now, we need to know the length of longest subsequence
     * until arr[i] - difference, then dp[i] = dp[arr[i] - difference] + 1. The easiest to retrieve such information is
     * by using a hash map.
     * <p>
     * Time: O(n)
     * Space: O(n)
     */
    public int longestSubsequence(int[] arr, int difference) {
        Map<Integer, Integer> dp = new HashMap<>();
        int res = 1;
        for (int i = 0; i < arr.length; i++) {
            // it's always required to put new key-value pairs into the map
            // no matter it's being updated or newly created
            dp.put(arr[i], dp.getOrDefault(arr[i] - difference, 0) + 1);
            res = Math.max(res, dp.get(arr[i]));
        }
        return res;
    }

    @Test
    public void longestSubsequenceTest() {
        /**
         * Example 1:
         * Input: arr = [1,2,3,4], difference = 1
         * Output: 4
         * Explanation: The longest arithmetic subsequence is [1,2,3,4].
         */
        assertEquals(4, longestSubsequence(new int[]{1, 2, 3, 4}, 1));
        /**
         * Example 2:
         * Input: arr = [1,3,5,7], difference = 1
         * Output: 1
         * Explanation: The longest arithmetic subsequence is any single element.
         */
        assertEquals(1, longestSubsequence(new int[]{1, 3, 5, 7}, 1));
        /**
         * Example 3:
         * Input: arr = [1,5,7,8,5,3,4,2,1], difference = -2
         * Output: 4
         * Explanation: The longest arithmetic subsequence is [7,5,3,1].
         */
        assertEquals(4, longestSubsequence(new int[]{1, 5, 7, 8, 5, 3, 4, 2, 1}, -2));
    }
}
