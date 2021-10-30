import org.junit.Test;

import java.util.Arrays;
import java.util.Stack;

import static org.junit.Assert.assertEquals;

public class ShortestUnsortedContinuousSubArray {

    /**
     * Given an integer array nums, you need to find one continuous subarray that if you only sort this subarray in ascending
     * order, then the whole array will be sorted in ascending order.
     * <p>
     * Return the shortest such subarray and output its length.
     * <p>
     * Constraints:
     * <p>
     * 1 <= nums.length <= 10^4
     * -10^5 <= nums[i] <= 10^5
     * <p>
     * Approach 1: Sorting
     * The input array is almost sorted, there is only one sub-array that is unsorted. Therefore, the objective of this problem
     * is to find the desired left and right boundaries of the sub-array. How to find the boundary? Basically, we can sort the
     * input array, which clearly should've been the correct position for each element. Then, we can compare all elements at
     * each index, and find the left- and right-most indexes where the element is not the same. The sub-array in between will
     * be the desired segment. One edge case would be, if there is no such sub-array, the left and right boundaries will be at
     * the same index.
     * <p>
     * Time: O(nlogn) since we need to sort the input array
     * Space: O(n) need to clone the input array
     */
    public int findUnsortedSubArraySorting(int[] nums) {
        int[] clonedArray = nums.clone();
        // sort the input array
        Arrays.sort(clonedArray);
        int left = 0, right = nums.length - 1;

        // try to find the left and right boundaries where the elements are different with the desired sorted array
        while (left < right && nums[left] == clonedArray[left]) left++;
        while (left < right && nums[right] == clonedArray[right]) right--;

        return (left == right) ? 0 : right - left + 1;
    }

    /**
     * Follow up: Can you solve it in O(n) time complexity?
     * <p>
     * Approach 2: Stack
     * Essentially, we're trying to the leftmost boundary where the left element is larger than the right element. For the
     * rightmost boundary, the right element is smaller than the left element. Therefore, we can use stack to store a non-
     * increasing or non-decreasing sequence. When there is a misplacement in the stack, we pop that from the stack and
     * update the left- & right-most boundaries on the fly.
     * <p>
     * Time: O(n)
     * Space: O(n)
     */
    public int findUnsortedSubArrayStack(int[] nums) {
        Stack<Integer> stack = new Stack<>();
        int n = nums.length;
        int left = n, right = 0;

        // first pass - from left to right, find the leftmost boundary
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && nums[stack.peek()] > nums[i]) {
                left = Math.min(left, stack.pop());
            }
            stack.push(i);
        }

        // clear out the stack
        stack.clear();

        // second pass - from right to left, find the rightmost boundary
        for (int i = n - 1; i >= 0; i--) {
            while (!stack.isEmpty() && nums[stack.peek()] < nums[i]) {
                right = Math.max(right, stack.pop());
            }
            stack.push(i);
        }

        return (right > left) ? right - left + 1 : 0;
    }

    @Test
    public void findUnsortedSubArrayTest() {
        /**
         * Example 1:
         * Input: nums = [2,6,4,8,10,9,15]
         * Output: 5
         * Explanation: You need to sort [6, 4, 8, 10, 9] in ascending order to make the whole array sorted in ascending order.
         */
        assertEquals(5, findUnsortedSubArraySorting(new int[]{2, 6, 4, 8, 10, 9, 15}));
        assertEquals(5, findUnsortedSubArrayStack(new int[]{2, 6, 4, 8, 10, 9, 15}));
        /**
         * Example 2:
         * Input: nums = [1,2,3,4]
         * Output: 0
         */
        assertEquals(0, findUnsortedSubArraySorting(new int[]{1, 2, 3, 4}));
        assertEquals(0, findUnsortedSubArrayStack(new int[]{1, 2, 3, 4}));
        /**
         * Example 3:
         * Input: nums = [1]
         * Output: 0
         */
        assertEquals(0, findUnsortedSubArraySorting(new int[]{1}));
        assertEquals(0, findUnsortedSubArrayStack(new int[]{1}));
    }
}
