import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BinarySearch {

    /**
     * Given an array of integers nums which is sorted in ascending order, and an integer target,
     * write a function to search target in nums. If target exists, then return its index. Otherwise, return -1.
     * <p>
     * You must write an algorithm with O(log n) runtime complexity.
     * <p>
     * Constraints:
     * <p>
     * 1 <= nums.length <= 104
     * -104 < nums[i], target < 104
     * All the integers in nums are unique.
     * nums is sorted in ascending order.
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
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return -1;
    }

    @Test
    public void searchTest() {
        /**
         * Example 1:
         * Input: nums = [-1,0,3,5,9,12], target = 9
         * Output: 4
         * Explanation: 9 exists in nums and its index is 4
         */
        assertEquals(4, search(new int[]{-1, 0, 3, 5, 9, 12}, 9));
        /**
         * Example 2
         * Input: nums = [-1,0,3,5,9,12], target = 2
         * Output: -1
         * Explanation: 2 does not exist in nums so return -1
         */
        assertEquals(-1, search(new int[]{-1, 0, 3, 5, 9, 12}, 2));
    }
}
