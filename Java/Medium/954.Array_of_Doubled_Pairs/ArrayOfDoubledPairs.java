import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ArrayOfDoubledPairs {

    /**
     * Given an integer array of even length arr, return true if it is possible to reorder arr such that
     * arr[2 * i + 1] = 2 * arr[2 * i] for every 0 <= i < len(arr) / 2, or false otherwise.
     * <p>
     * Constraints:
     * <p>
     * 2 <= arr.length <= 3 * 10^4
     * arr.length is even.
     * -10^5 <= arr[i] <= 10^5
     * <p>
     * Approach: Greedy
     * Basically, we try to pair up the smallest double pairs at each step, and eventually we should be able to make n / 2
     * doubled pairs. The naive algorithm will be sorted the array from the smallest to the largest, and given a value (num),
     * try to pair it up with a previous value. How? similar to two sum, we add the needed value to form a pair into a hash
     * map and when that needed value is reached, we can identify it in O(1) time. For example, if the given value is 1,
     * then put {2: 1} into the map. If 2 appears, we decrement the frequency from the map, and whenever the frequency becomes
     * 0, we remove it from the map. i.e.
     * 1. If the value (num) exists in the map, we formed a pair -> decrement the frequency and remove it if necessary
     * 2. If num doesn't exist, increment the frequency for 2 * num.
     * If the map is empty in the end, which means we paired everything up.
     * One pitfall is that, if the values are negative numbers, e.g. [-4, -2], it can actually form a doubled pair. However,
     * 2 times a negative value will give an even smaller value instead of a larger one. e.g. if we meet -4, and -4 * 2 = -8
     * is not what we need, we need -4 / 2 = -2. Therefore, if the number is negative, instead of putting 2 * num, we put
     * num / 2 into the map. And there is also an edge case that e.g. -5 / 2 = -2 if we use integer instead of double.
     * <p>
     * Time: O(nlogn) we need to sort the input array, which will dominate the time complexity eventually
     * Space: O(n)
     */
    public boolean canReorderDoubled(int[] arr) {
        // sort the array for greedy algorithm
        Arrays.sort(arr);
        Map<Double, Integer> numNeededForDoubledPairs = new HashMap<>();

        // use double-precision instead of the integer to avoid 5 / 2 = 2
        for (double num : arr) {
            // if the current number can pair up with a previous value
            // form the pair
            if (numNeededForDoubledPairs.containsKey(num)) {
                numNeededForDoubledPairs.put(num, numNeededForDoubledPairs.get(num) - 1);
                // if the previous values have been used up - cannot form new pairs, remove it from the map
                if (numNeededForDoubledPairs.get(num) == 0) {
                    numNeededForDoubledPairs.remove(num);
                }
            } else if (num < 0) {
                // otherwise, the current value cannot form a pair with a smaller value
                // it needs to be paired up with a larger value
                // put the needed number into the map
                // if the current number is negative - put num / 2 into the map
                numNeededForDoubledPairs.put(num / 2, numNeededForDoubledPairs.getOrDefault(num / 2, 0) + 1);
            } else {
                // if the current number is positive - put 2 * num into the map
                numNeededForDoubledPairs.put(2 * num, numNeededForDoubledPairs.getOrDefault(2 * num, 0) + 1);
            }
        }
        // if the map is empty - all numbers have been paired - return true
        // otherwise, return false
        return numNeededForDoubledPairs.isEmpty();
    }

    @Test
    public void canReorderDoubledTest() {
        /**
         * Example 1:
         * Input: arr = [3,1,3,6]
         * Output: false
         */
        assertFalse(canReorderDoubled(new int[]{3, 1, 3, 6}));
        /**
         * Example 2:
         * Input: arr = [2,1,2,6]
         * Output: false
         */
        assertFalse(canReorderDoubled(new int[]{2, 1, 2, 6}));
        /**
         * Example 3:
         * Input: arr = [4,-2,2,-4]
         * Output: true
         * Explanation: We can take two groups, [-2,-4] and [2,4] to form [-2,-4,2,4] or [2,4,-2,-4].
         */
        assertTrue(canReorderDoubled(new int[]{4, -2, 2, -4}));
        /**
         * Example 4:
         * Input: arr = [1,2,4,16,8,4]
         * Output: false
         */
        assertFalse(canReorderDoubled(new int[]{1, 2, 4, 16, 8, 4}));
    }
}
