import org.junit.Test;
import static org.junit.Assert.*;

public class removeDuplicatesArray {
    /**
     * Given a sorted array nums, remove the duplicates in-place such that each element appear only once and return the new length.
     *
     * Do not allocate extra space for another array, you must do this by modifying the input array in-place with O(1) extra memory.
     *
     * Clarification:
     *
     * Confused why the returned value is an integer but your answer is an array?
     *
     * Note that the input array is passed in by reference, which means modification to the input array will be known to the caller as well.
     *
     * Internally you can think of this:
     *
     * // nums is passed in by reference. (i.e., without making a copy)
     * int len = removeDuplicates(nums);
     *
     * // any modification to nums in your function would be known by the caller.
     * // using the length returned by your function, it prints the first len elements.
     * for (int i = 0; i < len; i++) {
     *     print(nums[i]);
     * }
     *
     * Approach 1: Swap Seat
     * We can modify first k elements when we find duplicates by swapping non-duplicates to a correct position. By doing so, all duplicate
     * elements will move the positions beyond the returning length.
     *
     * Time: O(N), we remove duplicates by one pass
     * Space: O(1), modify input array in-place, no extra memory required
     */
    public int removeDuplicatesSwap(int[] nums) {
        if(nums.length == 0) return 0;
        int prevValue = nums[0];
        int changeIndex = 1;
        for(int i = 1; i < nums.length; i++) {
            if(nums[i] != prevValue) {
                prevValue = nums[i];
                swap(nums, changeIndex, i);
                changeIndex += 1;
            }
        }
        return changeIndex;
    }

    private void swap(int[] nums, int a, int b) {
        int temp = nums[a];
        nums[a] = nums[b];
        nums[b] = temp;
    }

    /**
     * Approach 2: Two Pointers
     * Actually, it is not necessary to keep original elements in the list. It doesn't matter what we left beyond the returning length.
     * Consequently, we can handle this task using two pointers, a fast one (j) and a slow one (i). when we found nums[j] != nums[i],
     * we simply increment the slow pointer by 1 (point to the correct position) and change the value of nums[i].
     *
     * Time: O(N) still one pass
     * Space: O(1) do it in-place, no extra space
     */
    public int removeDuplicatesTwoPointers(int[] nums) {
        if(nums.length == 0) return 0;
        int i = 0;
        for(int j = 1; j < nums.length; j++) {
            if(nums[j] != nums[i]) {
                i += 1;
                nums[i] = nums[j];
            }
        }
        return i + 1;
    }

    @Test
    public void removeDuplicatesTwoPointersTest() {
        /**
         * Example 1:
         * Given nums = [1,1,2],
         *
         * Your function should return length = 2, with the first two elements of nums being 1 and 2 respectively.
         */
        int[] nums1 = new int[]{1, 1, 2};
        int[] expected1 = new int[]{1, 2};
        int length1 = removeDuplicatesTwoPointers(nums1);
        int[] actual1 = arrayTrimming(nums1, length1);
        assertArrayEquals(expected1, actual1);
        /**
         * Given nums = [0,0,1,1,1,2,2,3,3,4],
         *
         * Your function should return length = 5, with the first five elements of nums being modified to 0, 1, 2, 3, and 4 respectively.
         */
        int[] nums2 = new int[]{0, 0, 1, 1, 1, 2, 2, 3, 3, 4};
        int[] expected2 = new int[]{0, 1, 2, 3, 4};
        int length2 = removeDuplicatesTwoPointers(nums2);
        int[] actual2 = arrayTrimming(nums2, length2);
        assertArrayEquals(expected2, actual2);
    }

    @Test
    public void removeDuplicatesSwapTest() {
        /**
         * Example 1:
         * Given nums = [1,1,2],
         *
         * Your function should return length = 2, with the first two elements of nums being 1 and 2 respectively.
         */
        int[] nums1 = new int[]{1, 1, 2};
        int[] expected1 = new int[]{1, 2};
        int length1 = removeDuplicatesSwap(nums1);
        int[] actual1 = arrayTrimming(nums1, length1);
        assertArrayEquals(expected1, actual1);
        /**
         * Given nums = [0,0,1,1,1,2,2,3,3,4],
         *
         * Your function should return length = 5, with the first five elements of nums being modified to 0, 1, 2, 3, and 4 respectively.
         */
        int[] nums2 = new int[]{0, 0, 1, 1, 1, 2, 2, 3, 3, 4};
        int[] expected2 = new int[]{0, 1, 2, 3, 4};
        int length2 = removeDuplicatesSwap(nums2);
        int[] actual2 = arrayTrimming(nums2, length2);
        assertArrayEquals(expected2, actual2);
    }

    private int[] arrayTrimming(int[] nums, int length) {
        int[] res = new int[length];
        for(int i = 0; i < length; i++) {
            res[i] = nums[i];
        }
        return res;
    }

}
