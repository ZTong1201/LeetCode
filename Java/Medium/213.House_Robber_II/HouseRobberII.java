import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HouseRobberII {

    /**
     * You are a professional robber planning to rob houses along a street. Each house has a certain amount of money stashed.
     * All houses at this place are arranged in a circle. That means the first house is the neighbor of the last one.
     * Meanwhile, adjacent houses have a security system connected, and it will automatically contact the police if two
     * adjacent houses were broken into on the same night.
     * <p>
     * Given an integer array nums representing the amount of money of each house, return the maximum amount of money you
     * can rob tonight without alerting the police.
     * <p>
     * Constraints:
     * <p>
     * 1 <= nums.length <= 100
     * 0 <= nums[i] <= 1000
     * <p>
     * Approach: Two-pass DP
     * The problem is similar to LeetCode 198: https://leetcode.com/problems/house-robber/
     * The only difference now is the first and the last houses cannot be robbed at the same time. Therefore, the easiest
     * way to resolve this problem is not robbing the last house first, then run a normal DP between [0, n - 2] because
     * when the last house is not robbed, it doesn't affect the decision whether the first house is robbed. Same thing holds
     * for the first house, we then run a normal DP between [1, n - 1] to indicate the first house will never be robbed.
     * The maximum value during these two runs will be the final result.
     * <p>
     * Time: O(n)
     * Space: O(n)
     */
    public int rob(int[] nums) {
        int n = nums.length;
        int[] maxMoneyRobbed = new int[n + 1];
        int res = 0;

        // first run - we carve out the last house and compute max money can be robbed at each house
        for (int i = n - 2; i >= 0; i--) {
            maxMoneyRobbed[i] = Math.max(maxMoneyRobbed[i + 2] + nums[i], maxMoneyRobbed[i + 1]);
            // also record the maximum value on the fly
            res = Math.max(res, maxMoneyRobbed[i]);
        }

        // second run - carve out the first house
        // rob the last house
        maxMoneyRobbed[n - 1] = nums[n - 1];
        res = Math.max(res, maxMoneyRobbed[n - 1]);

        // and dynamically compute the max money robbed until hosue 1
        for (int i = n - 2; i >= 1; i--) {
            maxMoneyRobbed[i] = Math.max(maxMoneyRobbed[i + 2] + nums[i], maxMoneyRobbed[i + 1]);
            // also record the maximum value on the fly
            res = Math.max(res, maxMoneyRobbed[i]);
        }
        return res;
    }

    @Test
    public void robTest() {
        /**
         * Example 1:
         * Input: nums = [2,3,2]
         * Output: 3
         * Explanation: You cannot rob house 1 (money = 2) and then rob house 3 (money = 2), because they are adjacent houses.
         */
        assertEquals(3, rob(new int[]{2, 3, 2}));
        /**
         * Example 2:
         * Input: nums = [1,2,3,1]
         * Output: 4
         * Explanation: Rob house 1 (money = 1) and then rob house 3 (money = 3).
         * Total amount you can rob = 1 + 3 = 4.
         */
        assertEquals(4, rob(new int[]{1, 2, 3, 1}));
        /**
         * Example 3:
         * Input: nums = [1,2,3]
         * Output: 3
         */
        assertEquals(3, rob(new int[]{1, 2, 3}));
    }
}
