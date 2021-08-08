import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class findMedian {

    /**
     * There are two sorted arrays nums1 and nums2 of size m and n respectively.
     * <p>
     * Find the median of the two sorted arrays. The overall run time complexity should be O(log (m+n)).
     * <p>
     * You may assume nums1 and nums2 cannot be both empty.
     * <p>
     * Constraints:
     * <p>
     * nums1.length == m
     * nums2.length == n
     * 0 <= m <= 1000
     * 0 <= n <= 1000
     * 1 <= m + n <= 2000
     * -106 <= nums1[i], nums2[i] <= 106
     * <p>
     * Approach 1: One pass
     * Basically, we traverse two sorted arrays until hitting the midpoint (m + n) / 2. We keep two variables to keep track
     * of the largest value seen so far because they might be candidates of the median. If at any time, we reach the end of
     * nums1 (or nums2) but hasn't hit the middle point, we stop traversing that array and only move pointers in another.
     * <p>
     * Time: O(m + n)
     * Space: O(1)
     */
    public double findMedianSortedArrayOnePass(int[] nums1, int[] nums2) {
        // two pointers to traverse two arrays
        int index1 = 0, index2 = 0;
        int median1 = 0, median2 = 0;
        // we will find the median until reaching the midpoint
        for (int i = 0; i <= (nums1.length + nums2.length) / 2; i++) {
            // update median1 to be the value from last run
            // since we might take average in the end
            median1 = median2;
            // if we reach the end of array 1
            // stop traversing it and move to array 2
            if (index1 == nums1.length) {
                median2 = nums2[index2];
                index2++;
            } else if (index2 == nums2.length) { // same thing holds when the array 2 has been completely traversed
                median2 = nums1[index1];
                index1++;
            } else if (nums1[index1] < nums2[index2]) {
                // if both arrays have elements to be visited
                // move the smaller value
                median2 = nums1[index1];
                index1++;
            } else {
                median2 = nums2[index2];
                index2++;
            }
        }

        // after hitting the midpoint, take the average if the total length is even
        if ((nums1.length + nums2.length) % 2 == 0) {
            return (median1 + median2) / 2.0;
        }

        // otherwise, the middle value will be directly returned
        return median2;
    }

    /**
     * Approach 2: Binary search
     * Finding median is actually to partition a sorted array into two equally-sized parts (the first half could be 1 size
     * larger if the array is of odd length). Hence, we can do binary search on the array of smaller size, and partition
     * the second array accordingly to make sure the "combined" array is split into two halves with the same size.
     * For example:
     * x -> x1 x2 x3 x4
     * y -> y1 y2 y3 y4 y5 y6
     * we split x into two halves
     * x1 x2 | x3 x4
     * In order to make sure both sides have the same number of elements, we need to partition the y array
     * as this:
     * y1 y2 y3 | y4 y5 y6
     * Therefore, we have
     * x1 x2    | x3 x4
     * y1 y2 y3 | y4 y5 y6
     * now, only (x2, x3, y3, y4) matter to determine the median. Since we know x2 <= x3 and y3 <= y4 (original arrays are
     * sorted), then if x2 <= y4 && y3 <= x3, we know we have partitioned the "combined" array at the right place. The median
     * will be
     * max(x2, y3) + min(x3, y4) / 2 if the combined array is of even length
     * max(x2, y3) if the array is of odd length
     * <p>
     * If x2 > y4, then we're too much partition to the right side, we need to search the left half in x to find a new partition
     * point. Otherwise, we search the right half.
     * Note that, if the input array is empty, or we have partitioned the array to something like this: {} | x1 x2 x3 x4
     * then we assign -inf to the left side as its maximum value. We'll also assign +inf to the right side if the right half
     * is empty.
     * <p>
     * Time: O(log(min(m, n)) since we guarantee to always do binary search in the smaller array
     * Space: O(1)
     */
    public double findMedianSortedArrayBinarySearch(int[] nums1, int[] nums2) {
        // make sure nums1 is always the array of a smaller size
        if (nums1.length > nums2.length) return findMedianSortedArrayBinarySearch(nums2, nums1);
        int x = nums1.length, y = nums2.length;
        int low = 0, high = x;
        // run binary search on x
        // 左闭右闭区间 - 因为可以有一侧是empty array
        while (low <= high) {
            // find the partition point for array x
            int partitionX = (high - low) / 2 + low;
            // find partition point for array y, plus 1 to handle both odd and even length cases
            int partitionY = (x + y + 1) / 2 - partitionX;

            // find maximum in the left half and minimum in the right half on array x
            int maxLeftX = (partitionX == 0) ? Integer.MIN_VALUE : nums1[partitionX - 1];
            int minRightX = (partitionX == x) ? Integer.MAX_VALUE : nums1[partitionX];

            // find same values on array y
            int maxLeftY = (partitionY == 0) ? Integer.MIN_VALUE : nums2[partitionY - 1];
            int minRightY = (partitionY == y) ? Integer.MAX_VALUE : nums2[partitionY];

            // if we have partitioned at the correct place
            if (maxLeftX <= minRightY && maxLeftY <= minRightX) {
                if ((x + y) % 2 == 0) {
                    // return average if the "combined" array is of an even length
                    return (Math.max(maxLeftX, maxLeftY) + Math.min(minRightX, minRightY)) / 2.0;
                } else {
                    // otherwise, return the maximum value from the left half since it's of an odd length
                    return Math.max(maxLeftX, maxLeftY);
                }
            } else if (maxLeftX > minRightY) {
                // we're partitioning too far from the right
                // need to search the left half on array x
                high = partitionX - 1;
            } else {
                // otherwise, we're too far from the left, search the right half
                low = partitionX + 1;
            }
        }
        // this return statement would've never been reached
        return -1;
    }

    @Test
    public void findMedianSortedArrayTest() {
        /**
         * Example 1:
         * nums1 = [1, 3]
         * nums2 = [2]
         *
         * The median is 2.0
         */
        int[] nums1 = new int[]{1, 3};
        int[] nums2 = new int[]{2};
        assertEquals(2.0, findMedianSortedArrayOnePass(nums1, nums2), 0);
        assertEquals(2.0, findMedianSortedArrayBinarySearch(nums1, nums2), 0);
        /**
         * Example 2:
         * nums1 = [1, 2]
         * nums2 = [3, 4]
         *
         * The median is (2 + 3)/2 = 2.5
         */
        int[] nums3 = new int[]{1, 2};
        int[] nums4 = new int[]{3, 4};
        assertEquals(2.5, findMedianSortedArrayOnePass(nums3, nums4), 0);
        assertEquals(2.5, findMedianSortedArrayBinarySearch(nums3, nums4), 0);
    }
}
