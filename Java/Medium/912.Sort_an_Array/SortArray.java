import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class SortArray {

    /**
     * Given an array of integers nums, sort the array in ascending order.
     * <p>
     * Approach 1: Quick Sort
     * Key is partition. Find a pivot value and loop through the sub-array to arrange all items which are smaller than the
     * pivot value to its left side and larger elements to its right. Keep partitioning the left and right side until it
     * hits the edge case.
     * <p>
     * Time: O(nlogn)
     * Space: O(logn)
     */
    public int[] sortArrayQuickSort(int[] nums) {
        quickSort(nums, 0, nums.length - 1);
        return nums;
    }

    private void quickSort(int[] nums, int start, int end) {
        // base case
        if (start >= end) return;

        // find an arbitrary pivot value in the array
        int pivot = nums[(end - start) / 2 + start];

        // partition
        int left = start, right = end;
        while (left <= right) {
            // skip elements that are already smaller than the pivot from the left
            while (nums[left] < pivot) left++;
            // also skip elements that are already larger than the pivot from the right
            while (nums[right] > pivot) right--;

            // if left and right haven't crossed, which means the pivot place is still not in its correct place
            if (left <= right) {
                // swap the smaller values to the left (or the larger value to the right)
                int temp = nums[left];
                nums[left] = nums[right];
                nums[right] = temp;
                // keep arranging elements
                left++;
                right--;
            }
        }
        // once partition is done, the pivot value should have been at the right place
        // divide and conquer to partition the left and the right sub-array
        // now everything ahead of "right" index is smaller than the pivot value
        quickSort(nums, start, right);
        // and everything after "left" index is larger than the pivot value
        quickSort(nums, left, end);
    }

    /**
     * Approach 2: Merge Sort
     * Keep dividing the array into two equally-sized sub-arrays until it only have one element. Then add an implementation
     * for merging two sorted arrays.
     * <p>
     * Time: O(nlogn)
     * Space: O(n) need additional space for new array
     */
    public int[] sortArrayMergeSort(int[] nums) {
        return mergeSort(nums, 0, nums.length - 1);
    }

    private int[] mergeSort(int[] nums, int start, int end) {
        // base case, return a new array of a single element
        if (start == end) {
            return new int[]{nums[start]};
        }

        // keep dividing the entire array into sub-arrays
        int mid = (end - start) / 2 + start;
        int[] left = mergeSort(nums, start, mid);
        int[] right = mergeSort(nums, mid + 1, end);
        // return merged array
        return mergeTwoSortedArrays(left, right);
    }

    private int[] mergeTwoSortedArrays(int[] left, int[] right) {
        int[] res = new int[left.length + right.length];
        int i = 0, j = 0, k = 0;
        while (i < left.length && j < right.length) {
            // keep adding the smaller element from two arrays to the result array
            // until reaching the end of either array
            if (left[i] <= right[j]) {
                res[k++] = left[i++];
            } else {
                res[k++] = right[j++];
            }
        }
        // adding the rest of elements to the end of result (if any)
        while (i < left.length) {
            res[k++] = left[i++];
        }
        while (j < right.length) {
            res[k++] = right[j++];
        }
        return res;
    }

    @Test
    public void sortArrayQuickSortTest() {
        /**
         * Example 1:
         * Input: nums = [5,2,3,1]
         * Output: [1,2,3,5]
         */
        int[] array1 = new int[]{5, 2, 3, 1};
        int[] expectedArray1 = new int[]{1, 2, 3, 5};
        int[] actualArray1 = sortArrayQuickSort(array1);
        assertArrayEquals(expectedArray1, actualArray1);
        /**
         * Example 2:
         * Input: nums = [5,1,1,2,0,0]
         * Output: [0,0,1,1,2,5]
         */
        int[] array2 = new int[]{5, 1, 1, 2, 0, 0};
        int[] expectedArray2 = new int[]{0, 0, 1, 1, 2, 5};
        int[] actualArray2 = sortArrayQuickSort(array2);
        assertArrayEquals(expectedArray2, actualArray2);
    }

    @Test
    public void sortArrayMergeSortTest() {
        /**
         * Example 1:
         * Input: nums = [5,2,3,1]
         * Output: [1,2,3,5]
         */
        int[] array1 = new int[]{5, 2, 3, 1};
        int[] expectedArray1 = new int[]{1, 2, 3, 5};
        int[] actualArray1 = sortArrayMergeSort(array1);
        assertArrayEquals(expectedArray1, actualArray1);
        /**
         * Example 2:
         * Input: nums = [5,1,1,2,0,0]
         * Output: [0,0,1,1,2,5]
         */
        int[] array2 = new int[]{5, 1, 1, 2, 0, 0};
        int[] expectedArray2 = new int[]{0, 0, 1, 1, 2, 5};
        int[] actualArray2 = sortArrayMergeSort(array2);
        assertArrayEquals(expectedArray2, actualArray2);
    }
}
