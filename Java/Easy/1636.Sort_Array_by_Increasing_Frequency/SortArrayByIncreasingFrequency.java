import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.Assert.assertArrayEquals;

public class SortArrayByIncreasingFrequency {

    /**
     * Given an array of integers nums, sort the array in increasing order based on the frequency of the values. If multiple
     * values have the same frequency, sort them in decreasing order.
     * <p>
     * Return the sorted array.
     * <p>
     * Constraints:
     * <p>
     * 1 <= nums.length <= 100
     * -100 <= nums[i] <= 100
     * <p>
     * Approach: Comparator + sort
     * <p>
     * Time: O(nlogn)
     * Space: O(n) or O(logn)
     */
    public int[] frequencySort(int[] nums) {
        Map<Integer, Integer> numFrequency = new HashMap<>();
        List<Integer> numList = new ArrayList<>();

        for (int num : nums) {
            numList.add(num);
            numFrequency.put(num, numFrequency.getOrDefault(num, 0) + 1);
        }

        numList.sort((a, b) -> {
            if (Objects.equals(numFrequency.get(a), numFrequency.get(b))) {
                return Integer.compare(b, a);
            }
            return Integer.compare(numFrequency.get(a), numFrequency.get(b));
        });

        int[] res = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            res[i] = numList.get(i);
        }
        return res;
    }

    @Test
    public void frequencySortTest() {
        /**
         * Example 1:
         * Input: nums = [1,1,2,2,2,3]
         * Output: [3,1,1,2,2,2]
         * Explanation: '3' has a frequency of 1, '1' has a frequency of 2, and '2' has a frequency of 3.
         */
        int[] expected1 = new int[]{3, 1, 1, 2, 2, 2};
        int[] actual1 = frequencySort(new int[]{1, 1, 2, 2, 2, 3});
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: nums = [2,3,1,3,2]
         * Output: [1,3,3,2,2]
         * Explanation: '2' and '3' both have a frequency of 2, so they are sorted in decreasing order.
         */
        int[] expected2 = new int[]{1, 3, 3, 2, 2};
        int[] actual2 = frequencySort(new int[]{2, 3, 1, 3, 2});
        assertArrayEquals(expected2, actual2);
        /**
         * Example 3:
         * Input: nums = [-1,1,-6,4,5,-6,1,4,1]
         * Output: [5,-1,4,4,-6,-6,1,1,1]
         */
        int[] expected3 = new int[]{5, -1, 4, 4, -6, -6, 1, 1, 1};
        int[] actual3 = frequencySort(new int[]{-1, 1, -6, 4, 5, -6, 1, 4, 1});
        assertArrayEquals(expected3, actual3);
    }
}
