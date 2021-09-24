import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import static org.junit.Assert.assertArrayEquals;

public class NextGreaterElementI {

    /**
     * The next greater element of some element x in an array is the first greater element that is to the right of x in the
     * same array.
     * <p>
     * You are given two distinct 0-indexed integer arrays nums1 and nums2, where nums1 is a subset of nums2.
     * <p>
     * For each 0 <= i < nums1.length, find the index j such that nums1[i] == nums2[j] and determine the next greater element
     * of nums2[j] in nums2. If there is no next greater element, then the answer for this query is -1.
     * <p>
     * Return an array ans of length nums1.length such that ans[i] is the next greater element as described above.
     * <p>
     * Constraints:
     * <p>
     * 1 <= nums1.length <= nums2.length <= 1000
     * 0 <= nums1[i], nums2[i] <= 10^4
     * All integers in nums1 and nums2 are unique.
     * All the integers of nums1 also appear in nums2.
     * <p>
     * <p>
     * Follow up: Could you find an O(nums1.length + nums2.length) solution?
     * <p>
     * Approach: Monotonic Stack
     * The brute force approach will be for each element, we loop through the rest of the array and find the first greater
     * element, which will take O(n^2) runtime in total. However, we could do some preprocessing on the original array
     * to compute the next greater element at each position. How? By maintaining a stack that the order is decreasing.
     * Why? Cuz if the elements in the stack is in the decreasing order, then if a new element breaks the order, it will be
     * the first greater element for the top value in the stack, and we can remove it from the stack and keep popping new
     * elements until the stack is empty or a bigger element in the stack has been found.
     * For example, given [1,3,4,2]
     * put 1
     * stack = [1], map = {}
     * 3 > 1, stack = [], map = {1: 3}
     * add 3
     * stack = [3]
     * 4 > 3, stack = [], map = {1: 3, 3: 4};
     * add 4
     * stack = [4]
     * 2 < 4, stack = [4, 2]
     * after the iteration, if the stack is not empty, which means there is not next greater element for them.
     * Popping everything from the stack and mark the next greater element as -1
     * <p>
     * Time: O(n) while traversing the array, each element will be pushed and popped once, which takes O(1) time. Hence, it
     * takes O(n) in total.
     * Space: O(n)
     */
    public int[] nextGreaterElement(int[] nums1, int[] nums2) {
        Stack<Integer> stack = new Stack<>();
        Map<Integer, Integer> nextGreaterNum = new HashMap<>();

        // preprocess
        for (int num : nums2) {
            // while the stack is not empty, and we find the first greater element for the top value in the stack
            // add it into the map and keep comparing
            while (!stack.isEmpty() && stack.peek() < num) {
                nextGreaterNum.put(stack.pop(), num);
            }
            // always push the element to the correct position
            stack.push(num);
        }

        // if there are any elements left in the stack, they don't have next greater element
        // pop them out and assign -1 to them
        while (!stack.isEmpty()) {
            nextGreaterNum.put(stack.pop(), -1);
        }

        int[] res = new int[nums1.length];
        for (int i = 0; i < nums1.length; i++) {
            res[i] = nextGreaterNum.get(nums1[i]);
        }
        return res;
    }

    @Test
    public void nextGreaterElementTest() {
        /**
         * Example 1:
         * Input: nums1 = [4,1,2], nums2 = [1,3,4,2]
         * Output: [-1,3,-1]
         * Explanation: The next greater element for each value of nums1 is as follows:
         * - 4 is underlined in nums2 = [1,3,4,2]. There is no next greater element, so the answer is -1.
         * - 1 is underlined in nums2 = [1,3,4,2]. The next greater element is 3.
         * - 2 is underlined in nums2 = [1,3,4,2]. There is no next greater element, so the answer is -1.
         */
        int[] expected1 = new int[]{-1, 3, -1};
        int[] actual1 = nextGreaterElement(new int[]{4, 1, 2}, new int[]{1, 3, 4, 2});
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: nums1 = [2,4], nums2 = [1,2,3,4]
         * Output: [3,-1]
         * Explanation: The next greater element for each value of nums1 is as follows:
         * - 2 is underlined in nums2 = [1,2,3,4]. The next greater element is 3.
         * - 4 is underlined in nums2 = [1,2,3,4]. There is no next greater element, so the answer is -1.
         */
        int[] expected2 = new int[]{3, -1};
        int[] actual2 = nextGreaterElement(new int[]{2, 4}, new int[]{1, 2, 3, 4});
        assertArrayEquals(expected2, actual2);
    }
}
