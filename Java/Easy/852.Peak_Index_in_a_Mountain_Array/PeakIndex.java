import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PeakIndex {

    /**
     * Let's call an array arr a mountain if the following properties hold:
     * <p>
     * arr.length >= 3
     * There exists some i with 0 < i < arr.length - 1 such that:
     * arr[0] < arr[1] < ... arr[i-1] < arr[i]
     * arr[i] > arr[i+1] > ... > arr[arr.length - 1]
     * <p>
     * Given an integer array arr that is guaranteed to be a mountain, return any i such that
     * arr[0] < arr[1] < ... arr[i - 1] < arr[i] > arr[i + 1] > ... > arr[arr.length - 1].
     * <p>
     * Constraints:
     * <p>
     * 3 <= arr.length <= 10^4
     * 0 <= arr[i] <= 10^6
     * arr is guaranteed to be a mountain array.
     * <p>
     * Approach: Binary search
     * Similar question to LeetCode 162: https://leetcode.com/problems/find-peak-element/
     * The array is guaranteed to be a mountain, hence we can take advantage of binary search to always discard to smaller half.
     * <p>
     * Time: O(logn)
     * Space: O(1)
     */
    public int peakIndexInMountainArray(int[] arr) {
        int left = 0, right = arr.length - 1;
        while (left < right) {
            int mid = (right - left) / 2 + left;
            if (arr[mid] < arr[mid + 1]) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }

    @Test
    public void peakIndexInMountainArrayTest() {
        /**
         * Example 1:
         * Input: arr = [0,1,0]
         * Output: 1
         */
        assertEquals(1, peakIndexInMountainArray(new int[]{0, 1, 0}));
        /**
         * Example 2:
         * Input: arr = [0,2,1,0]
         * Output: 1
         */
        assertEquals(1, peakIndexInMountainArray(new int[]{0, 2, 1, 0}));
        /**
         * Example 3:
         * Input: arr = [0,10,5,2]
         * Output: 1
         */
        assertEquals(1, peakIndexInMountainArray(new int[]{0, 10, 5, 2}));
        /**
         * Example 4:
         * Input: arr = [3,4,5,1]
         * Output: 2
         */
        assertEquals(2, peakIndexInMountainArray(new int[]{3, 4, 5, 1}));
        /**
         * Example 5:
         * Input: arr = [24,69,100,99,79,78,67,36,26,19]
         * Output: 2
         */
        assertEquals(2, peakIndexInMountainArray(new int[]{24, 69, 100, 99, 79, 78, 67, 36, 26, 19}));
    }
}
