import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MinimumNumberOfIncrements {

    /**
     * Given an array of positive integers target and an array initial of same size with all zeros.
     * <p>
     * Return the minimum number of operations to form a target array from initial if you are allowed to do the following
     * operation:
     * Choose any subarray from initial and increment each value by one.
     * <p>
     * The answer is guaranteed to fit within the range of a 32-bit signed integer.
     * <p>
     * Constraints:
     * <p>
     * 1 <= target.length <= 10^5
     * 1 <= target[i] <= 10^5
     * <p>
     * Approach: Greedy
     * Considering the number operations in a greedy way starting from the left until the end of the array. First, in order
     * to make the first value from 0 to target[0], we need target[0] operations, that's the starting point. While moving
     * to the right, since we're allowed to increment a sub-array, which means we can carry the number of operations from
     * the previous value to get the same value. Here are two scenarios to be considered:
     * 1. target[i] <= operations carried over, which means we don't need extra operations, this index will be incremented
     * to the correct value as a sub-array along with its left neighbor
     * 2. target[i] > operations carried over, which means we still cannot achieve a desired value after incrementing the number
     * of operations as a sub-array. Therefore, we need to increment (target[i] - operations) more times to get the desired
     * value, and this value can be carried over to its right neighbor.
     * <p>
     * Time: O(n)
     * Space: O(n)
     */
    public int minNumberOperations(int[] target) {
        int totalOperations = target[0];
        int operationsCanBeReused = target[0];

        for (int i = 1; i < target.length; i++) {
            // only need to add more operations when more increments are needed
            if (target[i] > operationsCanBeReused) {
                totalOperations += target[i] - operationsCanBeReused;
            }
            // reassign the number of operations can be carried over
            operationsCanBeReused = target[i];
        }
        return totalOperations;
    }

    @Test
    public void minNumberOperationsTest() {
        /**
         * Example 1:
         * Input: target = [1,2,3,2,1]
         * Output: 3
         * Explanation: We need at least 3 operations to form the target array from the initial array.
         * [0,0,0,0,0] increment 1 from index 0 to 4 (inclusive).
         * [1,1,1,1,1] increment 1 from index 1 to 3 (inclusive).
         * [1,2,2,2,1] increment 1 at index 2.
         * [1,2,3,2,1] target array is formed.
         */
        assertEquals(3, minNumberOperations(new int[]{1, 2, 3, 2, 1}));
        /**
         * Example 2:
         * Input: target = [3,1,1,2]
         * Output: 4
         * Explanation: (initial)[0,0,0,0] -> [1,1,1,1] -> [1,1,1,2] -> [2,1,1,2] -> [3,1,1,2] (target).
         */
        assertEquals(4, minNumberOperations(new int[]{3, 1, 1, 2}));
        /**
         * Example 3:
         * Input: target = [3,1,5,4,2]
         * Output: 7
         * Explanation: (initial)[0,0,0,0,0] -> [1,1,1,1,1] -> [2,1,1,1,1] -> [3,1,1,1,1]
         *                                   -> [3,1,2,2,2] -> [3,1,3,3,2] -> [3,1,4,4,2] -> [3,1,5,4,2] (target).
         */
        assertEquals(7, minNumberOperations(new int[]{3, 1, 5, 4, 2}));
        /**
         * Example 4:
         * Input: target = [1,1,1,1]
         * Output: 1
         */
        assertEquals(1, minNumberOperations(new int[]{1, 1, 1, 1}));
    }
}
