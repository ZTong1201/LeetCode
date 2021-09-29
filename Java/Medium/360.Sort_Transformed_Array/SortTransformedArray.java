import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class SortTransformedArray {

    /**
     * Given a sorted integer array nums and three integers a, b and c, apply a quadratic function of the form
     * f(x) = ax^2 + bx + c to each element nums[i] in the array, and return the array in a sorted order.
     * <p>
     * Constraints:
     * <p>
     * 1 <= nums.length <= 200
     * -100 <= nums[i], a, b, c <= 100
     * nums is sorted in ascending order.
     * <p>
     * <p>
     * Follow up: Could you solve it in O(n) time?
     * <p>
     * Approach: Math + Two Pointers
     * Transform each number and resort the array approach is trivial. We can actually do it in O(n) by taking advantage of
     * the quadratic function property. Basically, when a > 0, the values will be larger at two ends and smaller in the center,
     * when a < 0, it's the opposite scenario. Therefore, we could always start from the both ends and shrink the window until
     * they meet at the center. If a > 0, we will assign a larger value to the right of the result list, if a < 0, we assign
     * a smaller value to the left of the list. What about a == 0? It will be the same, since we will always insert either
     * the largest to the right or the smallest to the left. We can combine it with a > 0. (Combining it with a < 0 will also
     * be the same)
     * <p>
     * Time: O(n)
     * Space: O(1)
     */
    public int[] sortTransformedArray(int[] nums, int a, int b, int c) {
        int n = nums.length;
        int[] res = new int[n];

        int left = 0, right = n - 1;
        // always insert the largest value to the right when a >= 0
        // otherwise, insert the smallest value to the left
        int start_index = (a >= 0) ? n - 1 : 0;
        // still need to process when there is one element left in the array
        while (left <= right) {
            int leftValue = transform(nums[left], a, b, c), rightValue = transform(nums[right], a, b, c);
            if (a >= 0) {
                // find the largest value from both ends and insert it into the right
                if (leftValue >= rightValue) {
                    res[start_index] = leftValue;
                    left++;
                } else {
                    res[start_index] = rightValue;
                    right--;
                }
                start_index--;
            } else {
                // otherwise, find the smallest value from both ends and insert it into the left
                if (leftValue >= rightValue) {
                    res[start_index] = rightValue;
                    right--;
                } else {
                    res[start_index] = leftValue;
                    left++;
                }
                start_index++;
            }
        }
        return res;
    }

    private int transform(int num, int a, int b, int c) {
        return a * num * num + b * num + c;
    }

    @Test
    public void sortTransformedArrayTest() {
        /**
         * Example 1:
         * Input: nums = [-4,-2,2,4], a = 1, b = 3, c = 5
         * Output: [3,9,15,33]
         */
        int[] expected1 = new int[]{3, 9, 15, 33};
        int[] actual1 = sortTransformedArray(new int[]{-4, -2, 2, 4}, 1, 3, 5);
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: nums = [-4,-2,2,4], a = -1, b = 3, c = 5
         * Output: [-23,-5,1,7]
         */
        int[] expected2 = new int[]{-23, -5, 1, 7};
        int[] actual2 = sortTransformedArray(new int[]{-4, -2, 2, 4}, -1, 3, 5);
        assertArrayEquals(expected2, actual2);
        /**
         * Example 3:
         * Input: nums = [-4,-2,2,4], a = 0, b = 3, c = 5
         * Output: [-7,-1,11,17]
         */
        int[] expected3 = new int[]{-7, -1, 11, 17};
        int[] actual3 = sortTransformedArray(new int[]{-4, -2, 2, 4}, 0, 3, 5);
        assertArrayEquals(expected3, actual3);
    }
}
