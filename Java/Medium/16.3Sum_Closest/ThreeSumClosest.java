import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class ThreeSumClosest {

    /**
     * Given an array nums of n integers and an integer target, find three integers in nums such that the sum is closest
     * to target. Return the sum of the three integers. You may assume that each input would have exactly one solution.
     * <p>
     * Constraints:
     * <p>
     * 3 <= nums.length <= 10^3
     * -10^3 <= nums[i] <= 10^3
     * -10^4 <= target <= 10^4
     * <p>
     * Approach: Sorting + Two Pointers
     * We can convert the problem into finding the smallest absolute difference with the target value. It's pretty much like
     * the 3 sum (LeetCode 15) problem: https://leetcode.com/problems/3sum/. However, instead of finding the exact sum,
     * we compute the sum of triplet and update the minimum absolute difference on the fly. If the difference is 0, returns
     * the target directly, otherwise we will return target - minDiff in the end. Since we're intended to find the exact sum,
     * the two pointers will be moved to get close to the target value (i.e. move the left pointer if the current sum is smaller,
     * move the right pointer otherwise)
     * <p>
     * <p>
     * Time: O(n^2) for each element, we need to use two pointers to visit all the elements after it in the worst case
     * Space: O(1) or O(n) depends upon which sorting algorithm is used
     */
    public int threeSumClosest(int[] nums, int target) {
        // the array must be sorted first to use the non-decreasing property
        Arrays.sort(nums);
        // the goal is to find the minimum absolute difference
        int minDiff = Integer.MAX_VALUE;
        for (int i = 0; i < nums.length; i++) {
            int left = i + 1, right = nums.length - 1;
            while (left < right) {
                // compute the sum of current triplet
                int sum = nums[i] + nums[left] + nums[right];
                // early termination if it equals to target already
                if (sum == target) return target;
                // otherwise, update the minimum absolute difference
                if (Math.abs(target - sum) < Math.abs(minDiff)) {
                    minDiff = target - sum;
                }
                // move pointers to get close to the target value
                if (sum < target) left++;
                else right--;
            }
        }
        // return the closest sum
        return target - minDiff;
    }

    @Test
    public void threeSumClosestTest() {
        /**
         * Example 1:
         * Input: nums = [-1,2,1,-4], target = 1
         * Output: 2
         * Explanation: The sum that is closest to the target is 2. (-1 + 2 + 1 = 2).
         */
        assertEquals(2, threeSumClosest(new int[]{-1, 2, 1, -4}, 1));
        /**
         * Example 2:
         * Input: nums = [1,1,1,0], target = -100
         * Output: 2
         */
        assertEquals(2, threeSumClosest(new int[]{1, 1, 1, 0}, -100));
    }
}
