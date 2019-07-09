import org.junit.Test;
import static org.junit.Assert.*;

public class houseRobber {

    /**
     * You are a professional robber planning to rob houses along a street. Each house has a certain amount of money stashed,
     * the only constraint stopping you from robbing each of them is that adjacent houses have security system connected
     * and it will automatically contact the police if two adjacent houses were broken into on the same night.
     *
     * Given a list of non-negative integers representing the amount of money of each house,
     * determine the maximum amount of money you can rob tonight without alerting the police.
     *
     * Dynmaic Programming
     *
     * Time: O(N)
     * Space: O(1) no extra space
     */
    public int rob(int[] nums) {
        int prevMax = 0;
        int currMax = 0;
        for(int x : nums) {
            int temp = currMax;
            currMax = Math.max(prevMax + x, currMax);
            prevMax = temp;
        }
        return currMax;
    }

    @Test
    public void houseRobberTest() {
        /**
         * Input: [1,2,3,1]
         * Output: 4
         * Explanation: Rob house 1 (money = 1) and then rob house 3 (money = 3).
         *              Total amount you can rob = 1 + 3 = 4.
         */
        int[] test1 = new int[]{1, 2, 3, 1};
        assertEquals(4, rob(test1));
        /**
         * Input: [2,7,9,3,1]
         * Output: 12
         * Explanation: Rob house 1 (money = 2), rob house 3 (money = 9) and rob house 5 (money = 1).
         *              Total amount you can rob = 2 + 9 + 1 = 12.
         */
        int[] test2 = new int[]{2, 7, 9, 3, 1};
        assertEquals(12, rob(test2));
    }
}
