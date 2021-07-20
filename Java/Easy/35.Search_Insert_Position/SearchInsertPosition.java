import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SearchInsertPosition {

    /**
     * Given a sorted array of distinct integers and a target value, return the index if the target is found.
     * If not, return the index where it would be if it were inserted in order.
     * <p>
     * You must write an algorithm with O(log n) runtime complexity.
     * <p>
     * Constraints:
     * <p>
     * 1 <= nums.length <= 10^4
     * -10^4 <= nums[i] <= 10^4
     * nums contains distinct values sorted in ascending order.
     * -10^4 <= target <= 10^4
     * Approach: binary search
     * <p>
     * Time: O(logn)
     * Space: O(1)
     */
    public int searchInsert(int[] nums, int target) {
        // 选择左闭右开区间
        int left = 0, right = nums.length;
        // 终止条件为[left, left)区间内没有元素
        while (left < right) {
            int mid = (right - left) / 2 + left;
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] < target) {
                // 选择右半区间[mid + 1, right)
                left = mid + 1;
            } else {
                // 选择左半区间[left, mid)
                right = mid;
            }
        }
        return left;
    }

    @Test
    public void searchInsertTest() {
        /**
         * Example 1:
         * Input: nums = [1,3,5,6], target = 5
         * Output: 2
         */
        assertEquals(2, searchInsert(new int[]{1, 3, 5, 6}, 5));
        /**
         * Example 2:
         * Input: nums = [1,3,5,6], target = 2
         * Output: 1
         */
        assertEquals(1, searchInsert(new int[]{1, 3, 5, 6}, 2));
        /**
         * Example 3:
         * Input: nums = [1,3,5,6], target = 7
         * Output: 4
         */
        assertEquals(4, searchInsert(new int[]{1, 3, 5, 6}, 7));
        /**
         * Example 4:
         * Input: nums = [1,3,5,6], target = 0
         * Output: 0
         */
        assertEquals(0, searchInsert(new int[]{1, 3, 5, 6}, 0));
    }

}
