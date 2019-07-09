import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class mergeSortedArray {

    /**
     * Given two sorted integer arrays nums1 and nums2, merge nums2 into nums1 as one sorted array.
     *
     * Note:
     *
     * The number of elements initialized in nums1 and nums2 are m and n respectively.
     * You may assume that nums1 has enough space (size that is greater or equal to m + n) to hold additional elements from nums2.
     *
     * Approach 1: merge and sort
     * Since the nums1 array guarantees to have enough space, we can simply add all elements from nums2 to nums1, then sort
     * the entire array
     *
     * Time: (m + n)log(m + n)
     * Space: O(1) no extra space required
     */
    public void mergeWithSorting(int[] nums1, int m, int[] nums2, int n) {
        for(int i = 0 ; i < n; i++) {
            nums1[i + m] = nums2[i];
        }
        //Or simply we can do arraycopy
        //System.arraycopy(nums2, 0, nums1, m, n);
        Arrays.sort(nums1);
    }

    /**
     * Approach 2: Two pointers/from the start
     * We can reduce the running time to O(m + n) by using two pointers. We can move the element from the start of each array and put
     * them at the front of the nums1 array. Since we put elements at the front, we need an extra space to store elements of nums1 array.
     * If either of the array has been copied completely, we stop merging and directly copy all remaining elements from the rest array to
     * the resulting array.
     *
     *
     * Time: O(m + n) since we traverse both array once
     * Space: O(m) we need to copy the elements from nums1 array
     */
    public void mergeTwoPointers1(int[] nums1, int m, int[] nums2, int n) {
        int[] nums1_copy = new int[m];
        System.arraycopy(nums1, 0, nums1_copy, 0, m);
        int p1 = 0, p2 = 0, p = 0;
        while(p1 < m && p2 < n) {
            if(nums1_copy[p1] <= nums2[p2]) {
                nums1[p] = nums1_copy[p1];
                p1 += 1;
            } else {
                nums1[p] = nums2[p2];
                p2 += 1;
            }
            p += 1;
        }
        if(p1 < m) System.arraycopy(nums1_copy, p1, nums1, p1 + p2, m - p1);
        if(p2 < n) System.arraycopy(nums2, p2, nums1, p1 + p2, n - p2);
    }

    /**
     * Approach 3: Two pointers/from the end
     * We can actually start moving element from the end of each array. By doing so, it doesn't require any extra space.
     * Meanwhile, the only edge case needs to be cared is when nums2 has remaining elements. Since if we move all the elements
     * from nums2 array, the rest of elements in nums1 array are in the desired order.
     *
     * Time: O(m + n)
     * Space: O(1) no extra space required
     */
    public void mergeTwoPointers2(int[] nums1, int m, int[] nums2, int n) {
        int p1 = m - 1, p2 = n - 1, p = m + n - 1;
        while(p1 >= 0 && p2 >= 0) {
            if(nums2[p2] >= nums1[p1]) {
                nums1[p] = nums2[p2];
                p2 -= 1;
            } else {
                nums1[p] = nums1[p1];
                p1 -= 1;
            }
            p -= 1;
        }
        System.arraycopy(nums2, 0, nums1, 0, p2 + 1);
    }



    @Test
    public void mergeTwoPointers2Test() {
        /**
         * Input:
         * nums1 = [1,2,3,0,0,0], m = 3
         * nums2 = [2,5,6],       n = 3
         *
         * Output: [1,2,2,3,5,6]
         */
        int[] nums1 = new int[]{1, 2, 3, 0, 0, 0};
        int m = 3;
        int[] nums2 = new int[]{2, 5, 6};
        int n = 3;
        int[] expected = new int[]{1, 2, 2, 3, 5, 6};
        mergeTwoPointers2(nums1, m, nums2, n);
        int[] actual = nums1;
        assertArrayEquals(expected, actual);
    }

    @Test
    public void mergeTwoPointers1Test() {
        /**
         * Input:
         * nums1 = [1,2,3,0,0,0], m = 3
         * nums2 = [2,5,6],       n = 3
         *
         * Output: [1,2,2,3,5,6]
         */
        int[] nums1 = new int[]{1, 2, 3, 0, 0, 0};
        int m = 3;
        int[] nums2 = new int[]{2, 5, 6};
        int n = 3;
        int[] expected = new int[]{1, 2, 2, 3, 5, 6};
        mergeTwoPointers1(nums1, m, nums2, n);
        int[] actual = nums1;
        assertArrayEquals(expected, actual);
    }


    @Test
    public void mergeWithSortingTest() {
        /**
         * Input:
         * nums1 = [1,2,3,0,0,0], m = 3
         * nums2 = [2,5,6],       n = 3
         *
         * Output: [1,2,2,3,5,6]
         */
        int[] nums1 = new int[]{1, 2, 3, 0, 0, 0};
        int m = 3;
        int[] nums2 = new int[]{2, 5, 6};
        int n = 3;
        int[] expected = new int[]{1, 2, 2, 3, 5, 6};
        mergeWithSorting(nums1, m, nums2, n);
        int[] actual = nums1;
        assertArrayEquals(expected, actual);
    }
}
