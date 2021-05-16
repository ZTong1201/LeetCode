import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class findMedian {

    /**
     * There are two sorted arrays nums1 and nums2 of size m and n respectively.
     *
     * Find the median of the two sorted arrays. The overall run time complexity should be O(log (m+n)).
     *
     * You may assume nums1 and nums2 cannot be both empty.
     */
    public double findMedianSortedArray(int[] nums1, int[] nums2) {

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
        assertEquals(2.0, findMedianSortedArray(nums1, nums2), 0);
        /**
         * Example 2:
         * nums1 = [1, 2]
         * nums2 = [3, 4]
         *
         * The median is (2 + 3)/2 = 2.5
         */
        int[] nums3 = new int[]{1, 2};
        int[] nums4 = new int[]{3, 4};
        assertEquals(2.5, findMedianSortedArray(nums3, nums4), 0);
    }
}
