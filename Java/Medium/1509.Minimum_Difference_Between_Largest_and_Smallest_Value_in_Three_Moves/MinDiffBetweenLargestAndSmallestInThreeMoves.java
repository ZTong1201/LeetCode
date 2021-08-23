import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class MinDiffBetweenLargestAndSmallestInThreeMoves {

    /**
     * Given an array nums, you are allowed to choose one element of nums and change it by any value in one move.
     * <p>
     * Return the minimum difference between the largest and smallest value of nums after performing at most 3 moves.
     * <p>
     * Constraints:
     * <p>
     * 1 <= nums.length <= 10^5
     * -10^9 <= nums[i] <= 10^9
     * <p>
     * Approach: Greedy
     * Since only the minimum between the largest and the smallest matters for the result. We can basically remove 3 elements
     * from the set of 3 smallest and 6 largest numbers, and find the minimum difference between new smallest or largest items.
     * Essentially, we only have these 4 options to search:
     * 1. remove 0 from 3 smallest and remove all 3 largest
     * 2. remove 1 from 3 smallest and remove 1 from 3 largest
     * 3. remove 2 from 3 smallest and remove 1 from 3 largest
     * 4. remove all 3 smallest and remove 0 from 3 largest
     * The easiest way to get the indexes of smallest and largest value is to sort the array beforehand.
     * <p>
     * Time: O(nlogn) the sorting algorithm will dominate the runtime
     * Space: O(1) or O(logn) depends on the sorting algorithm being used.
     */
    public int minDifference(int[] nums) {
        int n = nums.length;
        // if we have less than or equal to 4 elements in the array, we can always get 0 min diff
        if (n <= 4) return 0;
        int res = Integer.MAX_VALUE;
        // the array must be sorted beforehand
        Arrays.sort(nums);
        // enumerate all 4 options
        for (int i = 0; i <= 3; i++) {
            // get the min difference from new smallest and largest
            // after removing i elements from the front and (3 - i) elements from the end
            res = Math.min(res, nums[n - 1 - (3 - i)] - nums[i]);
        }
        return res;
    }

    @Test
    public void minDifferenceTest() {
        /**
         * Example 1:
         * Input: nums = [5,3,2,4]
         * Output: 0
         * Explanation: Change the array [5,3,2,4] to [2,2,2,2].
         * The difference between the maximum and minimum is 2-2 = 0.
         */
        assertEquals(0, minDifference(new int[]{5, 3, 2, 4}));
        /**
         * Example 2:
         * Input: nums = [1,5,0,10,14]
         * Output: 1
         * Explanation: Change the array [1,5,0,10,14] to [1,1,0,1,1].
         * The difference between the maximum and minimum is 1-0 = 1.
         */
        assertEquals(1, minDifference(new int[]{1, 5, 0, 10, 14}));
        /**
         * Example 3:
         * Input: nums = [6,6,0,1,1,4,6]
         * Output: 2
         */
        assertEquals(2, minDifference(new int[]{6, 6, 0, 1, 1, 4, 6}));
        /**
         * Example 4:
         * Input: nums = [1,5,6,14,15]
         * Output: 1
         */
        assertEquals(1, minDifference(new int[]{1, 5, 6, 14, 15}));
        /**
         * Example 5:
         * Input: nums = [82,81,95,75,20]
         * Output: 1
         */
        assertEquals(1, minDifference(new int[]{82, 81, 95, 75, 20}));
    }
}
