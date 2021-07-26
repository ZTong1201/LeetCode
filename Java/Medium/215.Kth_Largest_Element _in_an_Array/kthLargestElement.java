import org.junit.Test;

import java.util.Arrays;
import java.util.PriorityQueue;

import static org.junit.Assert.assertEquals;

public class kthLargestElement {
    /**
     * Find the kth largest element in an unsorted array.
     * Note that it is the kth largest element in the sorted order, not the kth distinct element.
     * <p>
     * Method 1: Sort the array and retrieve the (n - k)-th element in the array
     * <p>
     * Time: O(NlogN) for sorting
     * Space: O(1) no extra space needed
     */
    public int findKthLargestSoring(int[] nums, int k) {
        Arrays.sort(nums);
        return nums[nums.length - k];
    }

    /**
     * Method 2: create a minHeap with size k, when the size of minHeap is larger than k, remove the smallest element
     * <p>
     * Time: O(Nlogk) since minHeap is of size k, and insertion/deletion in a minHeap cost O(logk)
     * Space: O(k) or we could say O(1) cause it costs constant space to store elements
     */
    public int findKthLargestMinHeap(int[] nums, int k) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        for (int i : nums) {
            minHeap.add(i);
            if (minHeap.size() > k) minHeap.poll();
        }
        return minHeap.poll();
    }

    /**
     * Method 3: Quick select (find k-th largest == find (n - k)-th smallest)
     * Find a pivot value, and add values which are smaller than the pivot value to its left, and values which are greater or equal to
     * the pivot value to its right. When the correct index of the pivot value equals to n - k, then we are done. Otherwise, we search
     * the correct side to find the (n - k)-th element. (i.e. if pivot_index < n - k, search the right side, otherwise search the left side)
     * <p>
     * Time: O(N^2) in the worst case, if array is sorted or array contains all same values.
     * On average, we can randomly pick the pivot value to partition the array evenly, then the runtime would be O(N)
     * Space: O(1) no extra space needed, since we swap elements in-place
     */

    private void swap(int[] nums, int start, int end) {
        int temp = nums[start];
        nums[start] = nums[end];
        nums[end] = temp;
    }

    private int partition(int[] nums, int start, int end) {
        // always select the mid value to bring some randomization
        int mid = (end - start) / 2 + start;
        int pivot_value = nums[mid];
        // this would be the correct index for the pivot value after partitioning
        int correct_index = start;
        // swap the pivot value to the end
        swap(nums, mid, end);

        for (int i = start; i < end; i++) {
            if (nums[i] < pivot_value) {
                // keep swapping values which are smaller than the pivot value to its left
                swap(nums, correct_index, i);
                // found one smaller than, increment the correct index by 1
                correct_index += 1;
            }
        }
        // swap the pivot value to its correct index
        swap(nums, correct_index, end);
        // return the pivot index since it's been correctly placed
        return correct_index;
    }

    private int quickSelect(int[] nums, int start, int end, int kth_smallest) {
        if (start == end) return nums[start]; // if array only contains one element, return it

        int pivot_index = partition(nums, start, end);

        if (pivot_index == kth_smallest) return nums[kth_smallest];
            // if pivot_index < n - k, search the right side
        else if (pivot_index < kth_smallest) return quickSelect(nums, pivot_index + 1, end, kth_smallest);
        // otherwise search the left side
        return quickSelect(nums, start, pivot_index - 1, kth_smallest);
    }

    public int findKthLargestQuickSelect(int[] nums, int k) {
        return quickSelect(nums, 0, nums.length - 1, nums.length - k);
    }

    @Test
    public void findKthLargestQuickSelectTest() {
        /**
         * Example 1:
         * Input: [3,2,1,5,6,4] and k = 2
         * Output: 5
         */
        int[] test1 = new int[]{3, 2, 1, 5, 6, 4};
        assertEquals(5, findKthLargestQuickSelect(test1, 2));
        /**
         * Example 2:
         * Input: [3,2,3,1,2,4,5,5,6] and k = 4
         * Output: 4
         */
        int[] test2 = new int[]{3, 2, 3, 1, 2, 4, 5, 5, 6};
        assertEquals(4, findKthLargestQuickSelect(test2, 4));
    }

    @Test
    public void findKthLargestSortingTest() {
        /**
         * Example 1:
         * Input: [3,2,1,5,6,4] and k = 2
         * Output: 5
         */
        int[] test1 = new int[]{3, 2, 1, 5, 6, 4};
        assertEquals(5, findKthLargestSoring(test1, 2));
        /**
         * Example 2:
         * Input: [3,2,3,1,2,4,5,5,6] and k = 4
         * Output: 4
         */
        int[] test2 = new int[]{3, 2, 3, 1, 2, 4, 5, 5, 6};
        assertEquals(4, findKthLargestSoring(test2, 4));
    }

    @Test
    public void findKthLargestMinHeapTest() {
        /**
         * Example 1:
         * Input: [3,2,1,5,6,4] and k = 2
         * Output: 5
         */
        int[] test1 = new int[]{3, 2, 1, 5, 6, 4};
        assertEquals(5, findKthLargestMinHeap(test1, 2));
        /**
         * Example 2:
         * Input: [3,2,3,1,2,4,5,5,6] and k = 4
         * Output: 4
         */
        int[] test2 = new int[]{3, 2, 3, 1, 2, 4, 5, 5, 6};
        assertEquals(4, findKthLargestMinHeap(test2, 4));
    }
}
