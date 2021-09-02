import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HouseRobber {

    /**
     * You are a professional robber planning to rob houses along a street. Each house has a certain amount of money stashed,
     * the only constraint stopping you from robbing each of them is that adjacent houses have security system connected
     * and it will automatically contact the police if two adjacent houses were broken into on the same night.
     * <p>
     * Given a list of non-negative integers representing the amount of money of each house,
     * determine the maximum amount of money you can rob tonight without alerting the police.
     * <p>
     * Constraints:
     * <p>
     * 1 <= nums.length <= 100
     * 0 <= nums[i] <= 400
     * <p>
     * Approach 1: Bottom-up Dynamic Programming
     * Since the robber can skip any amount of houses to maximize the final result, the subsequent choices might impact
     * the previous decision. As a result, we need to calculate in the reverse way (bottom up), i.e. from the last house
     * to the first house. Why? Because if we move backward, we only want to maximize these two values
     * 1. Not robbing the current house, if maximum robbed money from house[i + 1] is larger
     * 2. Robbing current house if house[i + 2] + curr is larger
     * <p>
     * Time: O(n)
     * Space: O(n)
     */
    public int robDP(int[] nums) {
        int n = nums.length;
        int[] maxRobbedMoney = new int[n + 1];
        // the maximum is 0 since no house has been robbed
        maxRobbedMoney[n] = 0;
        // the maximum value is nums[n - 1] since there is only one house to be robbed
        maxRobbedMoney[n - 1] = nums[n - 1];

        // bottom up DP
        for (int i = n - 2; i >= 0; i--) {
            maxRobbedMoney[i] = Math.max(maxRobbedMoney[i + 1], maxRobbedMoney[i + 2] + nums[i]);
        }
        return maxRobbedMoney[0];
    }

    /**
     * Approach 2: Optimized DP
     * Notice that the calculation only depends on previous two states, we can reduce the usage of array and assign
     * two variables instead.
     * <p>
     * Time: O(n)
     * Space: O(1)
     */
    public int robDPWithoutSpace(int[] nums) {
        int n = nums.length;
        int maxRobbedMoney = nums[n - 1];
        int robNext = nums[n - 1], robNextPlusOne = 0;

        for (int i = n - 2; i >= 0; i--) {
            int current = Math.max(robNext, robNextPlusOne + nums[i]);
            maxRobbedMoney = Math.max(maxRobbedMoney, current);

            robNextPlusOne = robNext;
            robNext = current;
        }
        return maxRobbedMoney;
    }

    @Test
    public void houseRobberTest() {
        /**
         * Example 1:
         * Input: [1,2,3,1]
         * Output: 4
         * Explanation: Rob house 1 (money = 1) and then rob house 3 (money = 3).
         *              Total amount you can rob = 1 + 3 = 4.
         */
        assertEquals(4, robDP(new int[]{1, 2, 3, 1}));
        assertEquals(4, robDPWithoutSpace(new int[]{1, 2, 3, 1}));
        /**
         * Example 2:
         * Input: [2,7,9,3,1]
         * Output: 12
         * Explanation: Rob house 1 (money = 2), rob house 3 (money = 9) and rob house 5 (money = 1).
         *              Total amount you can rob = 2 + 9 + 1 = 12.
         */
        assertEquals(12, robDP(new int[]{2, 7, 9, 3, 1}));
        assertEquals(12, robDPWithoutSpace(new int[]{2, 7, 9, 3, 1}));
        /**
         * Example 3:
         * Input: [2,1,1,2]
         * Output: 4
         */
        assertEquals(4, robDP(new int[]{2, 1, 1, 2}));
        assertEquals(4, robDPWithoutSpace(new int[]{2, 1, 1, 2}));
    }
}
