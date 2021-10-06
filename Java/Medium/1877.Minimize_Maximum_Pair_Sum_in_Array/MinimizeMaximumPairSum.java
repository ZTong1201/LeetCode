import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class MinimizeMaximumPairSum {

    /**
     * The pair sum of a pair (a,b) is equal to a + b. The maximum pair sum is the largest pair sum in a list of pairs.
     * <p>
     * For example, if we have pairs (1,5), (2,3), and (4,4), the maximum pair sum would be max(1+5, 2+3, 4+4) =
     * max(6, 5, 8) = 8.
     * Given an array nums of even length n, pair up the elements of nums into n / 2 pairs such that:
     * <p>
     * Each element of nums is in exactly one pair, and
     * The maximum pair sum is minimized.
     * Return the minimized maximum pair sum after optimally pairing up the elements.
     * <p>
     * Constraints:
     * <p>
     * n == nums.length
     * 2 <= n <= 10^5
     * n is even.
     * 1 <= nums[i] <= 10^5
     * <p>
     * Approach: Sort + Greedy
     * Essentially, given a list of numbers in range [xmin, xmax], the optimal pair at current state would always be pairing
     * up xmin and xmax. Why?
     * 1. If we don't choose xmin but pick a random number xi, where xi > xmin, apparently xi + xmax > xmin + xmax. The pair
     * sum is not minimized.
     * 2. If we don't choost xmax but pick a random number xi, where xi < xmax, it's true that we can obtain a smaller pair
     * sum in this case. However, this leaves a sub-list of numbers which are all strictly larger than xmin, hence moving
     * forward, whatever we pick to pair up with xmax, it will be greater than xmin + xmax again. Still, the max sum
     * is not minimized
     * <p>
     * Time: O(nlogn) the algorithm will be dominated by the sorting algorithm
     * Space: O(1) depend upon which sorting algorithm is used
     */
    public int minPairSum(int[] nums) {
        Arrays.sort(nums);
        int left = 0, right = nums.length - 1;
        int minMaxSum = 0;

        while (left < right) {
            // always pair up the current min and max - and update the maximum pair sum
            minMaxSum = Math.max(minMaxSum, nums[left] + nums[right]);
            left++;
            right--;
        }
        return minMaxSum;
    }

    @Test
    public void minPairSumTest() {
        /**
         * Example 1:
         * Input: nums = [3,5,2,3]
         * Output: 7
         * Explanation: The elements can be paired up into pairs (3,3) and (5,2).
         * The maximum pair sum is max(3+3, 5+2) = max(6, 7) = 7.
         */
        assertEquals(7, minPairSum(new int[]{3, 5, 2, 3}));
        /**
         * Example 2:
         * Input: nums = [3,5,4,2,4,6]
         * Output: 8
         * Explanation: The elements can be paired up into pairs (3,5), (4,4), and (6,2).
         * The maximum pair sum is max(3+5, 4+4, 6+2) = max(8, 8, 8) = 8.
         */
        assertEquals(8, minPairSum(new int[]{3, 5, 4, 2, 4, 6}));
    }
}
