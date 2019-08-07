import org.junit.Test;
import static org.junit.Assert.*;

public class maxProduct {

    /**
     * Given an integer array nums, find the contiguous subarray within an array (containing at least one number)
     * which has the largest product.
     *
     * Approach: Dynamic Programming
     * 用DP解此题的关键是记录到当前index的最大值和最小值，当前的最大值只可能是如下三种情况之一
     * 1.该元素本身
     * 2.前一位的最大值为正数，且当前元素也为正，最大值为二者乘积
     * 3.前一位的最小值为负值，且当前元素也为负，二者相乘可能形成最大值
     * 最小值同理。最一步只需更新最大值和最小值，将最大值与最终结果比较，更新最终结果即可
     *
     * Time: O(n) one-pass
     * Space: O(1) 只用了四个变量，空间为定值
     */
    public int maxProduct(int[] nums) {
        if(nums == null || nums.length == 0) return 0;
        int res = nums[0];
        int currMax = nums[0];
        int currMin = nums[0];
        for(int i = 1; i < nums.length; i++) {
            int oldMax = currMax;
            currMax = Math.max(Math.max(nums[i], nums[i] * currMax), currMin * nums[i]);
            currMin = Math.min(Math.min(nums[i], nums[i] * currMin), oldMax * nums[i]);
            res = Math.max(res, currMax);
        }
        return res;
    }


    @Test
    public void maxProductTest() {
        /**
         * Example 1:
         * Input: [2,3,-2,4]
         * Output: 6
         * Explanation: [2,3] has the largest product 6.
         */
        int[] nums1 = new int[]{2, 3, -2 ,4};
        assertEquals(6, maxProduct(nums1));
        /**
         * Example 2:
         * Input: [-2,0,-1]
         * Output: 0
         * Explanation: The result cannot be 2, because [-2,-1] is not a subarray.
         */
        int[] nums2 = new int[]{-2, 0, -1};
        assertEquals(0, maxProduct(nums2));
        /**
         * Example 3:
         * Input: [-1,0,-2,2]
         * Output: 2
         */
        int[] nums3 = new int[]{-1, 0, -2, 2};
        assertEquals(2, maxProduct(nums3));
        /**
         * Example 4:
         * Input: [-2,3,-4]
         * Output: 24
         */
        int[] nums4 = new int[]{-2, 3, -4};
        assertEquals(24, maxProduct(nums4));
    }
}
