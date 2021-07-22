import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MinCostClimbingStairs {

    /**
     * You are given an integer array cost where cost[i] is the cost of ith step on a staircase.
     * Once you pay the cost, you can either climb one or two steps.
     * <p>
     * You can either start from the step with index 0, or the step with index 1.
     * <p>
     * Return the minimum cost to reach the top of the floor.
     * <p>
     * Constraints:
     * <p>
     * 2 <= cost.length <= 1000
     * 0 <= cost[i] <= 999
     * <p>
     * Approach 1: In-place dynamic programming
     * Modify the current cost array with the equation dp[i] = min(dp[i - 1], dp[i -2]) + cost[i]. Note that it's possible
     * to skip the last index (climb two steps from index length - 2), hence the final result will be min(dp[n - 1], dp[n - 2])
     * <p>
     * Time: O(n)
     * Space: O(1) in place
     */
    public int minCostClimbingStairsDpInPlace(int[] cost) {
        int n = cost.length;
        for (int i = 2; i < n; i++) {
            cost[i] += Math.min(cost[i - 1], cost[i - 2]);
        }
        return Math.min(cost[n - 1], cost[n - 2]);
    }

    /**
     * Approach 2: DP (two variables)
     * If the original cost array is immutable, then we can declare two variables to keep track of the min cost until
     * reaching n - 1 and n - 2
     * <p>
     * Time: O(n)
     * Space: O(1)
     */
    public int minCostClimbingStairsDpTwoVariables(int[] cost) {
        int downTwo = cost[0], downOne = cost[1];
        for (int i = 2; i < cost.length; i++) {
            int temp = downOne;
            // now downOne = dp[i - 1], downTwo = dp[i - 2]
            downOne = Math.min(downOne, downTwo) + cost[i];
            downTwo = temp;
        }
        return Math.min(downOne, downTwo);
    }

    @Test
    public void minCostClimbingStairsTest() {
        /**
         * Example 1:
         * Input: cost = [10,15,20]
         * Output: 15
         * Explanation: Cheapest is: start on cost[1], pay that cost, and go to the top
         */
        assertEquals(15, minCostClimbingStairsDpInPlace(new int[]{10, 15, 20}));
        assertEquals(15, minCostClimbingStairsDpTwoVariables(new int[]{10, 15, 20}));
        /**
         * Example 2:
         * Input: cost = [1,100,1,1,1,100,1,1,100,1]
         * Output: 6
         * Explanation: Cheapest is: start on cost[0], and only step on 1s, skipping cost[3].
         */
        assertEquals(6, minCostClimbingStairsDpInPlace(new int[]{1, 100, 1, 1, 1, 100, 1, 1, 100, 1}));
        assertEquals(6, minCostClimbingStairsDpTwoVariables(new int[]{1, 100, 1, 1, 1, 100, 1, 1, 100, 1}));
    }

}
