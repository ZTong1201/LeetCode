import org.junit.Test;
import static org.junit.Assert.*;

public class nextPermutation {

    /**
     * Implement next permutation, which rearranges numbers into the lexicographically next greater permutation of numbers.
     *
     * If such arrangement is not possible, it must rearrange it as the lowest possible order (ie, sorted in ascending order).
     *
     * The replacement must be in-place and use only constant extra memory.
     *
     * Here are some examples. Inputs are in the left-hand column and its corresponding outputs are in the right-hand column.
     *
     * 1,2,3 → 1,3,2
     * 3,2,1 → 1,2,3
     * 1,1,5 → 1,5,1
     *
     * Approach 1: Single one-pass
     * This problem has two main parts. First, we need to find the correct index and replace it with the next larger element. In order
     * to do that, we need start from the end of the array, and check the first element when a[i] < a[i + 1] (strictly less than). The
     * index i is just the correct index we need to change value. Since after index i, we know that the original array is in a descending
     * order, we can still start from the end of the array, and find the next larger element in the subarray starting from index i. As
     * discussed before, the subarray is already in the descending order, hence the very first element who is larger than a[i] is the
     * desired element. As long as a[j] <= a[i], we keep decrementing the index until we reach index i. However, after swapping, this is
     * still not the next permutation, in order to find it, we need to reverse the subarray starting from index i + 1 to an ascending
     * order. This process will guarantee that the right subarray is in lexicographical order, and the entire array is the next permutation
     *
     * Time: O(n) one-pass algorithm, in the worst case, we go through the entire array twice
     * Space: O(1) in-place modification, no extra space required
     */
    public void nextPermutation(int[] nums) {
        int i = nums.length - 2;
        while(i >= 0 && nums[i] >= nums[i + 1]) { //find the first element that is strictly smaller than its next element
            i -= 1;
        }
        if(i >= 0) {  //if i >= 0, the entire array is not in the descending order, hence it has a next permutation
            int j = nums.length - 1;
            while(j >= i && nums[j] <= nums[i]) j -= 1; //find the next larger element than nums[i]
            swap(nums, i, j);                           //swap these two elements
        }
        reverse(nums, i + 1);                     //finally, reverse the subarray starting from i + 1 to a lexicographical order
    }

    private void swap(int[] nums, int a, int b) {
        int tmp = nums[a];
        nums[a] = nums[b];
        nums[b] = tmp;
    }

    private void reverse(int[] nums, int start) {
        int end = nums.length - 1;
        while(start < end) {
            swap(nums, start, end);
            start += 1;
            end -= 1;
        }
    }

    @Test
    public void nextPermutationTest() {
        /**
         * Example 1:
         * Input: 1,2,3
         * Output: 1,3,2
         */
        int[] nums1 = new int[]{1, 2, 3};
        int[] expected1 = new int[]{1, 3, 2};
        nextPermutation(nums1);
        assertArrayEquals(expected1, nums1);
        /**
         * Example 2:
         * Input: 3,2,1
         * Output: 1,2,3
         */
        int[] nums2 = new int[]{3, 2, 1};
        int[] expected2 = new int[]{1, 2, 3};
        nextPermutation(nums2);
        assertArrayEquals(expected2, nums2);
        /**
         * Example 3:
         * Input: 1,1,5
         * Output: 1,5,1
         */
        int[] nums3 = new int[]{1, 1, 5};
        int[] expected3 = new int[]{1, 5, 1};
        nextPermutation(nums3);
        assertArrayEquals(expected3, nums3);
        /**
         * Example 4:
         * Input: 1,5,8,4,7,6,5,3,1
         * Output: 1,5,8,5,1,3,4,6,7
         */
        int[] nums4 = new int[]{1, 5, 8, 4, 7, 6, 5, 3, 1};
        int[] expected4 = new int[]{1, 5, 8, 5, 1, 3, 4, 6, 7};
        nextPermutation(nums4);
        assertArrayEquals(expected4, nums4);
    }
}
