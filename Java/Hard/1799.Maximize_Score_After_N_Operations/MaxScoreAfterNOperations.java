import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MaxScoreAfterNOperations {

    /**
     * You are given nums, an array of positive integers of size 2 * n. You must perform n operations on this array.
     * <p>
     * In the ith operation (1-indexed), you will:
     * <p>
     * Choose two elements, x and y.
     * Receive a score of i * gcd(x, y).
     * Remove x and y from nums.
     * Return the maximum score you can receive after performing n operations.
     * <p>
     * The function gcd(x, y) is the greatest common divisor of x and y.
     * <p>
     * Constraints:
     * <p>
     * 1 <= n <= 7
     * nums.length == 2 * n
     * 1 <= nums[i] <= 10^6
     * <p>
     * Approach: Bitmask DP
     * Basically, we need to enumerate all possible combinations of selection in order to find the maximum score. When
     * we have 2 * n numbers in the array, we should find all two pairs (i, j), and the maximum score will be
     * 1 * gcd(i, j) + maxScore(nums with i and j removed). Therefore, essentially there will be a subset of numbers left
     * in the array at a certain operations, we can use bitmask to identify which elements have been used.
     * We need a dp array of size 2^(2n) * (n / 2 + 1). Because each number can be selected or not after certain operations,
     * so there will be 2 options for each number, and we have 2n numbers in total. We need n operations to select all the
     * numbers and since operation is 1-indexes, that's why we need n / 2 + 1 spaces.
     * dp[mask][k] means the maximum score we can obtain after k operations with some numbers selected from the array.
     * Being selected means the bit value is 0 at certain position.
     * Initially the mask is 2^n means no number hasn't been selected yet and operation number is 1.
     * We will run DFS until mask is 0 meaning all numbers have been selected (order doesn't matter) and operation number is n.
     * <p>
     * Time: O(n^2 * 2^n) for each mask, we need to list all possible pairs to find the maximum score
     * Space: O(n * 2^n) need a 2-D dp array
     */
    public int maxScore(int[] nums) {
        int n = nums.length;
        // 1 << n = 2^n
        int[][] dp = new int[1 << n][n / 2 + 1];
        // start from nothing has been selected
        return dfs(dp, nums, (1 << n) - 1, 1);
    }

    private int dfs(int[][] dp, int[] nums, int mask, int operation) {
        // base case - after all numbers have been selected, the score is always 0
        if (mask == 0) return 0;
        // if the answer has been computed for current mask and after certain operations - return it
        if (dp[mask][operation] > 0) return dp[mask][operation];
        // enumerate all possible pairs
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                // need to make sure both numbers at i and j haven't been selected (0 means selected, 1 means unselected)
                if (((mask & (1 << i)) != 0) && ((mask & (1 << j)) != 0)) {
                    // if the pair is not selected - recursively compute the maximum score
                    // which is k * gcd(nums[i], nums[j]) + maxScore(nums after i, j removed)
                    // use mask ^ (1 << i) to change the bit at position i to 0, means nums[i] is selected
                    dp[mask][operation] = Math.max(dp[mask][operation], operation * gcd(nums[i], nums[j]) +
                            dfs(dp, nums, mask ^ (1 << i) ^ (1 << j), operation + 1));
                }
            }
        }
        return dp[mask][operation];
    }

    private int gcd(int x, int y) {
        if (x % y == 0) return y;
        return gcd(y, x % y);
    }

    @Test
    public void maxScoreTest() {
        /**
         * Example 1:
         * Input: nums = [1,2]
         * Output: 1
         * Explanation: The optimal choice of operations is:
         * (1 * gcd(1, 2)) = 1
         */
        assertEquals(1, maxScore(new int[]{1, 2}));
        /**
         * Example 2:
         * Input: nums = [3,4,6,8]
         * Output: 11
         * Explanation: The optimal choice of operations is:
         * (1 * gcd(3, 6)) + (2 * gcd(4, 8)) = 3 + 8 = 11
         */
        assertEquals(11, maxScore(new int[]{3, 4, 6, 8}));
        /**
         * Example 3:
         * Input: nums = [1,2,3,4,5,6]
         * Output: 14
         * Explanation: The optimal choice of operations is:
         * (1 * gcd(1, 5)) + (2 * gcd(2, 4)) + (3 * gcd(3, 6)) = 1 + 4 + 9 = 14
         */
        assertEquals(14, maxScore(new int[]{1, 2, 3, 4, 5, 6}));
    }
}
