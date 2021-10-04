import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class FindOriginalArrayFromDoubledArray {

    /**
     * An integer array original is transformed into a doubled array changed by appending twice the value of every element
     * in original, and then randomly shuffling the resulting array.
     * <p>
     * Given an array changed, return original if changed is a doubled array. If changed is not a doubled array, return an
     * empty array. The elements in original may be returned in any order.
     * <p>
     * Constraints:
     * <p>
     * 1 <= changed.length <= 10^5
     * 0 <= changed[i] <= 10^5
     * <p>
     * Approach: Sort + Greedy
     * No matter how the changed array is shuffled, it's guaranteed that if there exists a pair in the original array that
     * nums[i] < nums[j], then 2 * nums[i] must < 2 * nums[j]. So if we sort the changed array, we make sure 2 * nums[i] will
     * be processed before 2 * nums[j]. Therefore, we can recover nums[i] without interfering with any values which are
     * greater than it. We can keep recovering numbers from the original array back and if all the numbers were found, we
     * return the recovered array, otherwise, return an empty array. There is an edge case that if the input length if an
     * odd number, then we must not be able to recover the original array.
     * <p>
     * Time: O(nlogn) where n is the size of the input array, we need to sort the array first, and iterate the entire array
     * to recover the original array
     * Space: O(n / 2)
     */
    public int[] findOriginalArray(int[] changed) {
        // we need to sort the entire array and start from the smallest to the largest
        Arrays.sort(changed);
        int n = changed.length;
        // if the changed length is an odd number, there is no way we can revert it back
        if (n % 2 != 0) return new int[0];
        // why double is used? because we need to divide each number by 2,
        // and it avoids 3 / 2 = 1
        Map<Double, Integer> numFrequency = new HashMap<>();
        int[] res = new int[n / 2];
        int index = 0;

        for (double number : changed) {
            // since we start from the smallest, then it is only possible we can find number / 2 in the map
            // but not number * 2
            double half = number / 2;
            if (numFrequency.containsKey(half)) {
                // if we can find half in the map, then it must be a number in the original array
                res[index++] = (int) half;
                // decrement the frequency of half, and remove it completely if the frequency is 0
                numFrequency.put(half, numFrequency.get(half) - 1);
                if (numFrequency.get(half) == 0) numFrequency.remove(half);
            } else {
                // otherwise, it's a candidate number in the original array
                // put it into the map and see whether we can find a doubled number of it
                numFrequency.put(number, numFrequency.getOrDefault(number, 0) + 1);
            }
        }
        // if all numbers are found, return the array, otherwise, return an empty one
        return (index == n / 2) ? res : new int[0];
    }

    @Test
    public void findOriginalArrayTest() {
        /**
         * Example 1:
         * Input: changed = [1,3,4,2,6,8]
         * Output: [1,3,4]
         * Explanation: One possible original array could be [1,3,4]:
         * - Twice the value of 1 is 1 * 2 = 2.
         * - Twice the value of 3 is 3 * 2 = 6.
         * - Twice the value of 4 is 4 * 2 = 8.
         * Other original arrays could be [4,3,1] or [3,1,4].
         */
        int[] expected1 = new int[]{1, 3, 4};
        int[] actual1 = findOriginalArray(new int[]{1, 3, 4, 2, 6, 8});
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: changed = [6,3,0,1]
         * Output: []
         * Explanation: changed is not a doubled array.
         */
        assertEquals(0, findOriginalArray(new int[]{6, 3, 0, 1}).length);
        /**
         * Example 3:
         * Input: changed = [1]
         * Output: []
         * Explanation: changed is not a doubled array.
         */
        assertEquals(0, findOriginalArray(new int[]{1}).length);
    }
}
