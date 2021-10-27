import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class SubArraySumsDivisibleByK {

    /**
     * Given an integer array nums and an integer k, return the number of non-empty subarrays that have a sum divisible by k.
     * <p>
     * A subarray is a contiguous part of an array.
     * <p>
     * Constraints:
     * <p>
     * 1 <= nums.length <= 3 * 10^4
     * -10^4 <= nums[i] <= 10^4
     * 2 <= k <= 10^4
     * <p>
     * Approach: Prefix sum + hash map
     * The key part of this problem is that given a prefix sum of the array. If sum[0...i] % k == sum[0...j] % k, given i < j,
     * then sum[i...j] % k == 0. In math, we have (si - sj) % k == (si % k - sj % k) % k == 0 % k = k.
     * Another tricky part is that the remainder can be negative, how to deal with the negative numbers? Basically, remainder
     * means you have x surplus of an integer times of k, for example, 9 % 5 = 4, means if you divide 9 only has one share
     * of 5, and the rest of them is 4. On the other hand, we can also say 9 is short of 1 (remainder -1) in order to have
     * two shares of 5.
     * Therefore, remainder -1 and remainder 4 can be treated as the same condition, so when there is a negative remainder,
     * we will always add it by 5 to make it positive.
     * Then the algorithm will be taking the prefix sum on the fly and take the remainder, keep track of the frequency of each
     * remainder, and the number of desired arrays will be the frequency of the same remainder before index i.
     * <p>
     * Time: O(n)
     * Space: O(n)
     */
    public int subarraysDivByK(int[] nums, int k) {
        int sum = 0, count = 0;
        Map<Integer, Integer> remainders = new HashMap<>();
        // need to put remainder 0 with frequency 1 into the map since an empty subarray has a remainder 0
        remainders.put(0, 1);

        for (int num : nums) {
            // compute the prefix sum and take the remainder on the fly
            sum = (sum + num) % k;
            // if the remainder is negative, make it positive
            if (sum < 0) sum += k;

            // add the count with the same remainder
            count += remainders.getOrDefault(sum, 0);
            // increment the count
            remainders.put(sum, remainders.getOrDefault(sum, 0) + 1);
        }
        return count;
    }

    @Test
    public void subarraysDivByKTest() {
        /**
         * Example 1:
         * Input: nums = [4,5,0,-2,-3,1], k = 5
         * Output: 7
         * Explanation: There are 7 subarrays with a sum divisible by k = 5:
         * [4, 5, 0, -2, -3, 1], [5], [5, 0], [5, 0, -2, -3], [0], [0, -2, -3], [-2, -3]
         */
        assertEquals(7, subarraysDivByK(new int[]{4, 5, 0, -2, -3, 1}, 5));
        /**
         * Example 2:
         * Input: nums = [5], k = 9
         * Output: 0
         */
        assertEquals(0, subarraysDivByK(new int[]{5}, 9));
    }
}
