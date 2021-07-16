import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class countSmallerMergeSort {

    private int[] count;

    /**
     * Approach 1: Merge Sort
     * Merge Sort的过程中会将两个array中的数字重排，如果有数字在merge过程中从right sub-array被移动到left sub-array中，说明对于left
     * sub-array中的有些元素，在原array中其右侧有比它更小的元素。所以只需在merge sort过程中记录是否有更小的元素被移动到更大的元素左边的情况
     * 即可。为了更好地判断，需要额外的array记录原array数字的index，在排序过程中，index也会被随之排序。通过index找回原array的数字，以此
     * 判断是否有更小的元素被移动至left sub-array
     * <p>
     * Time: O(nlogn) 本质是merge sort
     * Space: O(n) 需要额外空间存放index
     */
    public List<Integer> countSmallerMergeSort(int[] nums) {
        List<Integer> result = new ArrayList<>();
        count = new int[nums.length];
        int[] indexes = new int[nums.length];
        // initialize index array
        for (int i = 0; i < nums.length; i++) {
            indexes[i] = i;
        }
        mergeSortAndCount(nums, indexes, 0, nums.length - 1);
        for (int num : count) {
            result.add(num);
        }
        return result;
    }

    private void mergeSortAndCount(int[] nums, int[] indexes, int start, int end) {
        if (start >= end) return;
        int mid = (end - start) / 2 + start;
        // do a normal merge sort
        mergeSortAndCount(nums, indexes, start, mid);
        mergeSortAndCount(nums, indexes, mid + 1, end);
        // count smaller items while merging
        merge(nums, indexes, start, end);
    }

    private void merge(int[] nums, int[] indexes, int start, int end) {
        // need a new index array to arrange original indexes to correct places
        // while merging two arrays, the length will be the current left.length + right.length
        // which is end - start + 1
        int[] new_indexes = new int[end - start + 1];
        int mid = (end - start) / 2 + start;
        // the left array is of range [start, mid]
        int left_index = start;
        // the right array is of range [mid + 1, end]
        int right_index = mid + 1;

        int right_count = 0;
        int sort_index = 0;
        // normal merge actions
        while (left_index <= mid && right_index <= end) {
            // if there is an element in the right array that are smaller
            // than the one in the left array, increment the count and place
            // its original index to the new index array
            if (nums[indexes[left_index]] > nums[indexes[right_index]]) {
                right_count++;
                new_indexes[sort_index++] = indexes[right_index++];
            } else {
                // otherwise, all smaller elements have been counted for that item
                // assign current right_count value to the count array
                // also put the index into correct place
                count[indexes[left_index]] += right_count;
                new_indexes[sort_index++] = indexes[left_index++];
            }
        }
        // now one of the array has been completed merge
        // if there are any outstanding elements in the left sub-array
        // except for arranging the correct indexes, it's also necessary to
        // assign the count of smaller numbers
        while (left_index <= mid) {
            count[indexes[left_index]] += right_count;
            new_indexes[sort_index++] = indexes[left_index++];
        }
        // if there are any outstanding elements in the right sub-array
        // only need to sort the indexes - no need for count update
        while (right_index <= end) {
            new_indexes[sort_index++] = indexes[right_index++];
        }
        // now the merge is complete, need to copy the correct sorted indexes
        // into original index array
        for (int i = start; i <= end; i++) {
            indexes[i] = new_indexes[i - start];
        }
    }

    @Test
    public void countSmallerMergeSortTest() {
        /**
         * Input: [5,2,6,1]
         * Output: [2,1,1,0]
         * Explanation:
         * To the right of 5 there are 2 smaller elements (2 and 1).
         * To the right of 2 there is only 1 smaller element (1).
         * To the right of 6 there is 1 smaller element (1).
         * To the right of 1 there is 0 smaller element.
         */
        int[] nums = new int[]{5, 2, 6, 1};
        List<Integer> actual = countSmallerMergeSort(nums);
        List<Integer> expected = new ArrayList<>(Arrays.asList(2, 1, 1, 0));
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }
}
