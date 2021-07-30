import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FindPeakElement {

    /**
     * A peak element is an element that is strictly greater than its neighbors.
     * <p>
     * Given an integer array nums, find a peak element, and return its index. If the array contains multiple peaks,
     * return the index to any of the peaks.
     * <p>
     * You may imagine that nums[-1] = nums[n] = -∞.
     * <p>
     * You must write an algorithm that runs in O(log n) time.
     * <p>
     * Constraints:
     * <p>
     * 1 <= nums.length <= 1000
     * -2^31 <= nums[i] <= 2^31 - 1
     * nums[i] != nums[i + 1] for all valid i.
     * <p>
     * Approach: Binary search
     * Since nums[i] != nums[i + 1], then there is guaranteed we don't have a (sub-array) look like this: [1,1,1]. In scenario
     * like this, we cannot discard any halves to execute binary search. Therefore, based on the problem statement, the peak
     * element would always exist. We can simply search the higher element part in each round to finally target the peak.
     * <p>
     * Time: O(logn)
     * Space: O(1)
     */
    public int findPeakElement(int[] nums) {
        // 左闭右开区间
        // 如果在[0, length - 2]index区间内未找到peak，那么peak一定是最后一个element
        // 此时left = length - 1
        int left = 0, right = nums.length - 1;
        while (left < right) {
            int mid = (right - left) / 2 + left;
            // 搜索值更大的区间
            if (nums[mid] < nums[mid + 1]) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }

    @Test
    public void findPeakElementTest() {
        /**
         * Example 1:
         * Input: nums = [1,2,3,1]
         * Output: 2
         * Explanation: 3 is a peak element and your function should return the index number 2.
         */
        assertEquals(2, findPeakElement(new int[]{1, 2, 3, 1}));
        /**
         * Example 2:
         * Input: nums = [1,2,1,3,5,6,4]
         * Output: 5
         * Explanation: Your function can return either index number 1 where the peak element is 2, or index number 5
         * where the peak element is 6.
         */
        assertEquals(5, findPeakElement(new int[]{1, 2, 1, 3, 5, 6, 4}));
    }
}
