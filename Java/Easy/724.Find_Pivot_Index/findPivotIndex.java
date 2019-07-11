import org.junit.Test;
import static org.junit.Assert.*;

public class findPivotIndex {

    /**
     * Given an array of integers nums, write a method that returns the "pivot" index of this array.
     *
     * We define the pivot index as the index where the sum of the numbers to the left of the index is equal to the sum of the numbers to the right of the index.
     *
     * If no such index exists, we should return -1. If there are multiple pivot indexes, you should return the left-most pivot index.
     *
     *
     * Approach: Prefix Sum
     * This is a similar question as problem 560. Subarray Sum Equals K. In order to achieve the optimal running time, we need to compute
     * prefix sum, i.e. the cumulative sum. Hence, at each index, the left sum (does not include nums[i]) equals to prefix sum
     * P[i] = nums[0] + nums[1] + ... nums[i + 1]. Meanwhile, the right sum equals to P[P.length - 1] - P[i] - nums[i]
     * (a.k.a the total sum minus the left sum minus nums[i]). In other words, we need to omit nums[i] for both left and right sum
     * If for any index, we have left sum == right sum, we return that index. Otherwise, return -1.
     *
     * Time: O(n), we traverse the entire array twice. One for computing the total sum, the other is to compute left and right sum separately
     *      to find pivot index
     * Space: O(1), only require a constant space to store left and total sum
     */
    public int pivotIndex(int[] nums) {
        int sum = 0;
        for(int num : nums) sum += num;
        int leftSum = 0;
        for(int i = 0; i < nums.length; i++) {
            if(leftSum == sum - leftSum - nums[i]) return i;
            leftSum += nums[i];
        }
        return -1;
    }


    @Test
    public void pivotIndexTest() {
        /**
         * Example 1:
         * Input:
         * nums = [1, 7, 3, 6, 5, 6]
         * Output: 3
         * Explanation:
         * The sum of the numbers to the left of index 3 (nums[3] = 6) is equal to the sum of numbers to the right of index 3.
         * Also, 3 is the first index where this occurs.
         */
        int[] nums1 = new int[]{1, 7, 3, 6, 5, 6};
        assertEquals(3, pivotIndex(nums1));
        /**
         * Example 2:
         * Input:
         * nums = [1, 2, 3]
         * Output: -1
         * Explanation:
         * There is no index that satisfies the conditions in the problem statement.
         */
        int[] nums2 = new int[]{1, 2, 3};
        assertEquals(-1, pivotIndex(nums2));
        /**
         * Example 3:
         * Input:
         * nums = [-1, -1, -1, 0, 1, 1]
         * Output: 0
         * Explanation:
         * We need to treat the left sum of index 0 as 0. Hence the left sum and right sum of index 0 are both 0.
         */
        int[] nums3 = new int[]{-1, -1, -1, 0, 1, 1};
        assertEquals(0, pivotIndex(nums3));
    }
}
