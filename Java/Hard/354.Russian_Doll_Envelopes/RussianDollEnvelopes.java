import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class RussianDollEnvelopes {

    /**
     * You are given a 2D array of integers envelopes where envelopes[i] = [wi, hi] represents the width and the height of
     * an envelope.
     * <p>
     * One envelope can fit into another if and only if both the width and height of one envelope are greater than the other
     * envelope's width and height.
     * <p>
     * Return the maximum number of envelopes you can Russian doll (i.e., put one inside the other).
     * Note: You cannot rotate an envelope.
     * <p>
     * Constraints:
     * <p>
     * 1 <= envelopes.length <= 5000
     * envelopes[i].length == 2
     * 1 <= wi, hi <= 10^4
     * <p>
     * Approach 1: DP
     * We can first sort the envelopes by widths then by heights, now we can make sure we process the smallest envelopes
     * at first. It's pretty much the same to find the longest increasing subsequence in the array. Hence, we can use DP
     * to compute it in O(n^2) time.
     * Denote dp[i] as the longest Russian doll envelopes sequence ending with envelops[i]
     * Then we need to iterate over j in [0, i + 1], if that envelops can be fit into envelops[i], then
     * dp[i] = max(dp[i], dp[j] + 1)
     * To initialize the array, we set all dp[i] = 1, since each envelope itself is a Russian doll envelope.
     * <p>
     * Time: O(n^2)
     * Sorting takes O(nlogn) time, then for each index i in [0, n), we need to iterate over j in [0, i), which gives
     * n(n + 1)/2 pairs. Hence, the overall runtime will be O(n^2)
     * Space: O(n)
     */
    public int maxEnvelopesDP(int[][] envelopes) {
        // sort all envelops in ascending order by width, then by height
        Arrays.sort(envelopes, (a, b) -> {
            if (a[0] == b[0]) return a[1] - b[1];
            else return a[0] - b[0];
        });
        int n = envelopes.length;
        int[] dp = new int[n + 1];
        Arrays.fill(dp, 1);

        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                // update the longest Russian doll envelops if we find a strictly smaller one
                if (envelopes[j][0] < envelopes[i][0] && envelopes[j][1] < envelopes[i][1]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
        }

        int res = 0;
        // the longest sequence can be ended with any envelope
        for (int i = 0; i < n; i++) {
            res = Math.max(res, dp[i]);
        }
        return res;
    }

    /**
     * Approach: Binary Search
     * Still, the problem is to find the longest increasing subsequence (LeetCode 300)
     * https://leetcode.com/problems/longest-increasing-subsequence/
     * <p>
     * Then the optimal solution is to use binary search. Basically, for each element, we use binary search to find the
     * correct index to insert into. If the current element will be inserted after the entire array, which means we have
     * a longer LIS, otherwise, we just replace what's already in the array. The key part is to sort the envelope by width
     * first to make sure it's in ascending order, then we sort the height in descending order when there is a tie.
     * Why? For example, [1, 3], [1, 3], [1, 4], [1, 5]
     * We can only select one envelope because they also share the same width, hence they cannot form a longer Russian doll
     * sequence. Therefore, we only need to consider the sequence [5,4,3,3] which would give length of 1 LIS for each envelope.
     * <p>
     * Time: O(nlogn) We need to sort the entire envelopes array first. Then for each height, we use binary search to find the
     * correct insertion index. Both would take O(nlogn) time
     * Space: O(n)
     */
    public int maxEnvelopesBinarySearch(int[][] envelopes) {
        Arrays.sort(envelopes, (a, b) -> {
            // sort height in descending order if widths are the same
            if (a[0] == b[0]) return b[1] - a[1];
                // sort width in ascending order
            else return a[0] - b[0];
        });

        // add heights into a separate array hence we need to find the length of LIS in heights array
        int[] heights = new int[envelopes.length];
        for (int i = 0; i < envelopes.length; i++) {
            heights[i] = envelopes[i][1];
        }
        return lengthOfLIS(heights);
    }

    private int lengthOfLIS(int[] heights) {
        int n = heights.length;
        int[] dp = new int[n];
        int len = 0;

        for (int height : heights) {
            // find the correct insertion index for each height
            int index = binarySearch(dp, len, height);
            // insert the current value
            dp[index] = height;
            // if we're out of the range - we find a longer LIS
            if (index == len) {
                len++;
            }
        }
        return len;
    }

    private int binarySearch(int[] dp, int end, int target) {
        int left = 0, right = end;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (dp[mid] == target) {
                return mid;
            } else if (dp[mid] < target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }

    @Test
    public void maxEnvelopesTest() {
        /**
         * Example 1:
         * Input: envelopes = [[5,4],[6,4],[6,7],[2,3]]
         * Output: 3
         * Explanation: The maximum number of envelopes you can Russian doll is 3 ([2,3] => [5,4] => [6,7]).
         */
        assertEquals(3, maxEnvelopesDP(new int[][]{{5, 4}, {6, 4}, {6, 7}, {2, 3}}));
        assertEquals(3, maxEnvelopesBinarySearch(new int[][]{{5, 4}, {6, 4}, {6, 7}, {2, 3}}));
        /**
         * Example 2:
         * Input: envelopes = [[1,1],[1,1],[1,1]]
         * Output: 1
         */
        assertEquals(1, maxEnvelopesDP(new int[][]{{1, 1}, {1, 1}, {1, 1}}));
        assertEquals(1, maxEnvelopesBinarySearch(new int[][]{{1, 1}, {1, 1}, {1, 1}}));
        /**
         * Example 3:
         * Input: envelopes = [[1,3],[3,5],[6,7],[6,8],[8,4],[9,5]]
         * Output: 3
         */
        assertEquals(3, maxEnvelopesDP(new int[][]{{1, 3}, {3, 5}, {6, 7}, {6, 8}, {8, 4}, {9, 5}}));
        assertEquals(3, maxEnvelopesBinarySearch(new int[][]{{1, 3}, {3, 5}, {6, 7}, {6, 8}, {8, 4}, {9, 5}}));
        /**
         * Example 4:
         * Input: envelopes = [[10,8],[1,12],[6,15],[2,18]]
         * Output: 2
         */
        assertEquals(2, maxEnvelopesDP(new int[][]{{10, 8}, {1, 12}, {6, 15}, {2, 18}}));
        assertEquals(2, maxEnvelopesBinarySearch(new int[][]{{10, 8}, {1, 12}, {6, 15}, {2, 18}}));
    }
}
