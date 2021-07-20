import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class searchRange {

    /**
     * Given an array of integers nums sorted in ascending order, find the starting and ending position of a given
     * target value.
     * <p>
     * Your algorithm's runtime complexity must be in the order of O(log n).
     * <p>
     * If the target is not found in the array, return [-1, -1].
     * <p>
     * Constraints:
     * <p>
     * 0 <= nums.length <= 10^5
     * -10^9 <= nums[i] <= 10^9
     * nums is a non-decreasing array.
     * -10^9 <= target <= 10^9
     * <p>
     * Approach 1: Linear Scan
     * First pass starts from the front of the array find the first position, second pass starts from the end of the
     * array, find the last.
     * <p>
     * Time: O(n)
     * Space: O(1)
     */
    public int[] searchRangeLinearScan(int[] nums, int target) {
        int[] res = new int[]{-1, -1};
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == target) {
                res[0] = i;
                break;
            }
        }

        //if res[0] == -1, we didn't find the target value
        if (res[0] == -1) return res;

        //otherwise, we search for the last position
        for (int j = nums.length - 1; j >= 0; j--) {
            if (nums[j] == target) {
                res[1] = j;
                break;
            }
        }
        return res;
    }

    @Test
    public void searchRangeLinearScanTest() {
        /**
         * Example 1:
         * Input: nums = [5,7,7,8,8,10], target = 8
         * Output: [3,4]
         */
        int[] nums1 = new int[]{5, 7, 7, 8, 8, 10};
        int[] expected1 = searchRangeLinearScan(nums1, 8);
        int[] actual1 = new int[]{3, 4};
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: nums = [5,7,7,8,8,10], target = 6
         * Output: [-1,-1]
         */
        int[] nums2 = new int[]{5, 7, 7, 8, 8, 10};
        int[] expected2 = searchRangeLinearScan(nums2, 6);
        int[] actual2 = new int[]{-1, -1};
        assertArrayEquals(expected2, actual2);
    }


    /**
     * Approach 2: Binary Search
     * Basically the algorithm involves two binary search scan to find the leftmost & rightmost bound of target. If
     * target doesn't exist in the nums array at all, return -1. When searching for the leftmost bound, if
     * nums[mid] == target, there are two possibilities: 1. mid is already the leftmost bound when mid hits the
     * left boundary of current search range or it doesn't hit the left boundary but nums[mid - 1] != target,
     * 2. mid is still not the leftmost bound and since we're seeking the left bound, narrow the search range to
     * the left half.
     * <p>
     * When searching for the rightmost bound, the logic is essentially the same - 1. mid is the right bound if mid
     * hits the right boundary or nums[mid + 1] != target 2. keep searching the right half
     * <p>
     * Time: O(logn)
     * Space: O(1)
     */
    public int[] searchRangeBinarySearch(int[] nums, int target) {
        // find the leftmost bound
        int firstOccurrence = findBound(nums, target, true);
        // if target doesn't exist in the nums array, -1 will be returned
        if (firstOccurrence == -1) return new int[]{-1, -1};

        // find the rightmost bound
        int lastOccurrence = findBound(nums, target, false);
        return new int[]{firstOccurrence, lastOccurrence};
    }

    private int findBound(int[] nums, int target, boolean isFirst) {
        // 左闭右开区间
        int left = 0, right = nums.length;
        while (left < right) {
            int mid = (right - left) / 2 + left;
            if (nums[mid] == target) {
                if (isFirst) {
                    // 判断mid是否已经是left bound
                    if (mid == left || nums[mid - 1] != target) {
                        return mid;
                    }
                    // 向左继续寻找first occurrence
                    right = mid;
                } else {
                    // 判断mid是否已经是right bound
                    // 因为是左闭右开区间，最右index为right - 1
                    if (mid == right - 1 || nums[mid + 1] != target) {
                        return mid;
                    }
                    // 向右继续寻找last occurrence
                    left = mid + 1;
                }
            } else if (nums[mid] < target) { // normal binary search
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        // target不在nums array中，返回-1
        return -1;
    }

    @Test
    public void searchRangeBinarySearchTest() {
        /**
         * Example 1:
         * Input: nums = [5,7,7,8,8,10], target = 8
         * Output: [3,4]
         */
        int[] nums1 = new int[]{5, 7, 7, 8, 8, 10};
        int[] expected1 = searchRangeBinarySearch(nums1, 8);
        int[] actual1 = new int[]{3, 4};
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: nums = [5,7,7,8,8,10], target = 6
         * Output: [-1,-1]
         */
        int[] nums2 = new int[]{5, 7, 7, 8, 8, 10};
        int[] expected2 = searchRangeBinarySearch(nums2, 6);
        int[] actual2 = new int[]{-1, -1};
        assertArrayEquals(expected2, actual2);
    }
}
