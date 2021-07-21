import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SearchRotatedArrayII {

    /**
     * There is an integer array nums sorted in non-decreasing order (not necessarily with distinct values).
     * <p>
     * Before being passed to your function, nums is rotated at an unknown pivot index k (0 <= k < nums.length)
     * such that the resulting array is [nums[k], nums[k+1], ..., nums[n-1], nums[0], nums[1], ..., nums[k-1]]
     * (0-indexed). For example, [0,1,2,4,4,4,5,6,6,7] might be rotated at pivot index 5 and become [4,5,6,6,7,0,1,2,4,4].
     * <p>
     * Given the array nums after the rotation and an integer target, return true if target is in nums, or false
     * if it is not in nums.
     * <p>
     * You must decrease the overall operation steps as much as possible.
     * <p>
     * Constraints:
     * <p>
     * 1 <= nums.length <= 5000
     * -104 <= nums[i] <= 104
     * nums is guaranteed to be rotated at some pivot.
     * -104 <= target <= 104
     * <p>
     * Approach: binary search
     * <p>
     * Time: O(logn) on average, and O(n) in the worst case (all duplicate numbers in the array), the algorithm degrades
     * to linear scan
     * Space: O(1)
     */
    public boolean search(int[] nums, int target) {
        int left = 0, right = nums.length;
        while (left < right) {
            int mid = (right - left) / 2 + left;
            if (nums[mid] == target) return true;
                // if the left and right bounds are the same value
                // need to skip duplicates (if they're not equal to target)
            else if (nums[left] != target && nums[left] == nums[right - 1]) {
                // the algorithm degrades to linear scan in this scenario
                left++;
                right--;
            } else if (nums[left] <= nums[mid]) {
                // similar to LeetCode 33: https://leetcode.com/problems/search-in-rotated-sorted-array/
                if (target >= nums[left] && target < nums[mid]) right = mid;
                else left = mid + 1;
            } else {
                if (target > nums[mid] && target <= nums[right - 1]) left = mid + 1;
                else right = mid;
            }
        }
        return false;
    }

    @Test
    public void searchTest() {
        /**
         * Example 1:
         * Input: nums = [2,5,6,0,0,1,2], target = 0
         * Output: true
         */
        assertTrue(search(new int[]{2, 5, 6, 0, 0, 1, 2}, 0));
        /**
         * Example 2:
         * Input: nums = [2,5,6,0,0,1,2], target = 3
         * Output: false
         */
        assertFalse(search(new int[]{2, 5, 6, 0, 0, 1, 2}, 3));
        /**
         * Example 3:
         * Input: nums = [1,1,1,1,1,1,1,1,1,13,1,1,1,1,1,1,1,1,1,1,1,1], target = 13
         * Output: true
         */
        assertTrue(search(new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 13,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, 13));
        /**
         * Example 4:
         * Input: nums = [1,2,1], target = 1
         * Output: true
         */
        assertTrue(search(new int[]{1, 2, 1}, 1));
    }
}
