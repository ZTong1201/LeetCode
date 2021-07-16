import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class findMinimumII {

    /**
     * Suppose an array of length n sorted in ascending order is rotated between 1 and n times.
     * For example, the array nums = [0,1,4,4,5,6,7] might become:
     * <p>
     * [4,5,6,7,0,1,4] if it was rotated 4 times.
     * [0,1,4,4,5,6,7] if it was rotated 7 times.
     * Notice that rotating an array [a[0], a[1], a[2], ..., a[n-1]] 1 time results in the array [a[n-1], a[0], a[1], a[2], ..., a[n-2]].
     * <p>
     * Given the sorted rotated array nums that may contain duplicates, return the minimum element of this array.
     * <p>
     * You must decrease the overall operation steps as much as possible.
     * <p>
     * Binary Search
     * The only difference between 154 and 153 is that if the value at the midpoint equals to both the left and the right
     * boundary values -> it's not clear in which the actual minimum value will locate, e.g. [7,7,7,1,7]
     * In scenario like this, the algorithm should be downgraded to linear scan instead of aggressively jump the left or
     * right boundary to the midpoint.
     * <p>
     * Time: O(logn) on average, if the list contains all same value, the time complexity degrades to O(n)
     * Space: O(1)
     */
    public int findMinBinarySearch(int[] nums) {
        int left = 0, right = nums.length - 1;
        while (left < right) {
            int mid = left + (right - left) / 2;
            //若中点值与最左端和最右端的值都相等，两端各缩减1个index -> 算法此时退化为linear scan
            if (nums[mid] == nums[right] && nums[mid] == nums[left]) {
                left += 1;
                right -= 1;
            } else if (nums[mid] > nums[right]) { // 除以上特殊情况，二分查找仍适用
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return nums[left];
    }

    @Test
    public void findMinTest() {
        /**
         * Example 1:
         * Input: [1,3,5]
         * Output: 1
         */
        int[] nums1 = new int[]{1, 3, 5};
        assertEquals(1, findMinBinarySearch(nums1));
        /**
         * Example 2:
         * Input: [2,2,2,0,1]
         * Output: 0
         */
        int[] nums2 = new int[]{2, 2, 2, 0, 1};
        assertEquals(0, findMinBinarySearch(nums2));
    }
}
