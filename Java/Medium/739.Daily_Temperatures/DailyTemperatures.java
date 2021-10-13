import org.junit.Test;

import java.util.Stack;

import static org.junit.Assert.assertArrayEquals;

public class DailyTemperatures {

    /**
     * Given an array of integers temperatures represents the daily temperatures, return an array answer such that answer[i]
     * is the number of days you have to wait after the ith day to get a warmer temperature. If there is no future day for
     * which this is possible, keep answer[i] == 0 instead.
     * <p>
     * Constraints:
     * <p>
     * 1 <= temperatures.length <= 10^5
     * 30 <= temperatures[i] <= 100
     * <p>
     * Approach: Monotonic Stack
     * <p>
     * The problem is essentially finding the next greater element in the array, hence we can keep a monotonic stack in which
     * the elements are in a non-decreasing order. When a new element comes in, we will remove all elements which are smaller
     * than it and hence the new element will be the next greater element for them. However, since the answer needed is the
     * number of waiting days, we can actually keep track of the index in the stack, and the desired result is the difference
     * between two indexes.
     * <p>
     * Time: O(n) each element will be pushed and popped at most once, which takes O(1) time for a stack data structure
     * Space: O(n)
     */
    public int[] dailyTemperatures(int[] temperatures) {
        Stack<Integer> stack = new Stack<>();
        int n = temperatures.length;
        int[] res = new int[n];

        for (int i = 0; i < n; i++) {
            // remove smaller temperatures from the stack and update the result by taking the difference
            while (!stack.isEmpty() && temperatures[stack.peek()] < temperatures[i]) {
                int prevIndex = stack.pop();
                res[prevIndex] = i - prevIndex;
            }
            // always add the current temperature index into the stack
            stack.push(i);
        }
        return res;
    }

    @Test
    public void dailyTemperaturesTest() {
        /**
         * Example 1:
         * Input: temperatures = [73,74,75,71,69,72,76,73]
         * Output: [1,1,4,2,1,1,0,0]
         */
        int[] expected1 = new int[]{1, 1, 4, 2, 1, 1, 0, 0};
        int[] actual1 = dailyTemperatures(new int[]{73, 74, 75, 71, 69, 72, 76, 73});
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: temperatures = [30,40,50,60]
         * Output: [1,1,1,0]
         */
        int[] expected2 = new int[]{1, 1, 1, 0};
        int[] actual2 = dailyTemperatures(new int[]{30, 40, 50, 60});
        assertArrayEquals(expected2, actual2);
        /**
         * Example 3:
         * Input: temperatures = [30,60,90]
         * Output: [1,1,0]
         */
        int[] expected3 = new int[]{1, 1, 0};
        int[] actual3 = dailyTemperatures(new int[]{30, 60, 90});
        assertArrayEquals(expected3, actual3);
    }
}
