import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SearchRotatedArray {

    /**
     * There is an integer array nums sorted in ascending order (with distinct values).
     * <p>
     * Prior to being passed to your function, nums is rotated at an unknown pivot index k (0 <= k < nums.length)
     * such that the resulting array is [nums[k], nums[k+1], ..., nums[n-1], nums[0], nums[1], ..., nums[k-1]]
     * (0-indexed). For example, [0,1,2,4,5,6,7] might be rotated at pivot index 3 and become [4,5,6,7,0,1,2].
     * <p>
     * Given the array nums after the rotation and an integer target, return the index of target if it is
     * in nums, or -1 if it is not in nums.
     * <p>
     * You must write an algorithm with O(log n) runtime complexity.
     * <p>
     * Approach: binary search
     * <p>
     * Time: O(logn)
     * Space: O(1)
     */
    public int search(int[] nums, int target) {
        int left = 0, right = nums.length;
        while (left < right) {
            int mid = (right - left) / 2 + left;
            if (nums[mid] == target) return mid;
                // given a midpoint, one of the left and right sides must have been
                // rotated. It's easy to check whether the target would've been located
                // in the non-rotated side, i.e. left <= target < mid or mid < target <= right
                // narrow the search window to the sorted side if target might be there
                // otherwise search the rotated side

                // if the sorted side is on the left
            else if (nums[left] < nums[mid]) {
                // search the sorted side if left <= target < mid
                // use less than or equal in case there is only one item in the range
                if (target >= nums[left] && target < nums[mid]) {
                    right = mid;
                } else {
                    // otherwise search the rotated side
                    left = mid + 1;
                }
            } else {
                // search the sorted right side if mid < target <= right
                // CAREFUL: search区间为左闭右开，因此右侧最大index为right - 1
                if (target > nums[mid] && target <= nums[right - 1]) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }
        }
        return -1;
    }

    @Test
    public void searchTest() {
        /**
         * Example 1
         * Input: nums = [4,5,6,7,0,1,2], target = 0
         * Output: 4
         */
        assertEquals(4, search(new int[]{4, 5, 6, 7, 0, 1, 2}, 0));
        /**
         * Example 2:
         * Input: nums = [4,5,6,7,0,1,2], target = 3
         * Output: -1
         */
        assertEquals(-1, search(new int[]{4, 5, 6, 7, 0, 1, 2}, 3));
        /**
         * Example 3:
         * Input: nums = [3,1], target = 3
         * Output: 0
         */
        assertEquals(0, search(new int[]{3, 1}, 3));
        /**
         * Example 4:
         * Input: nums = [1,3,5], target = 5
         * Output: 2
         */
        assertEquals(2, search(new int[]{1, 3, 5}, 5));
    }

}
