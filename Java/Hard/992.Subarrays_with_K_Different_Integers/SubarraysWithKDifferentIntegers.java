import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SubarraysWithKDifferentIntegers {

    /**
     * Given an integer array nums and an integer k, return the number of good subarrays of nums.
     * <p>
     * A good array is an array where the number of different integers in that array is exactly k.
     * <p>
     * For example, [1,2,3,1,2] has 3 different integers: 1, 2, and 3.
     * A subarray is a contiguous part of an array.
     * <p>
     * Constraints:
     * <p>
     * 1 <= nums.length <= 2 * 10^4
     * 1 <= nums[i], k <= nums.length
     * <p>
     * Approach 1: Brute Force
     * Start at each number, enumerate all the subarrays with exactly k integers. When there are more than K integers in the
     * subarray already, early terminate the enumeration.
     * <p>
     * Time: O(n^2) in the worst, we need to traverse the rest of the array at each index
     * Space: O(n) need a boolean array to keep track of seen numbers
     */
    public int subarraysWithKDistinctBruteForce(int[] nums, int k) {
        int length = nums.length, res = 0;

        for (int i = 0; i < length; i++) {
            // given 1 <= nums[i], k <= nums.length
            // the possible number set includes values in [1, nums.length]
            boolean[] seen = new boolean[nums.length + 1];
            // count the number of distinct integers in the subarray
            int numCount = 0;
            for (int j = i; j < length; j++) {
                if (!seen[nums[j]]) {
                    // add new integer to the hash table
                    seen[nums[j]] = true;
                    // and increment the count
                    numCount++;
                }
                // found a subarray when the count equals to k
                if (numCount == k) res++;
                    // early termination - already violate the number of integers in the subarrary
                    // not necessary to keep searching
                else if (numCount > k) break;
            }
        }
        return res;
    }

    /**
     * Approach 2: Sliding window
     * We could use sliding window approach to get the number of distinct subarrays with at most K integers. Essentially,
     * we'll decrement the number K if we meet a new integer. We keep expanding the window size if we have surplus for the
     * K, and if at any time K is less than 0, we shrink the window by pushing out existing integers. Then we keep adding
     * the window size into the final result.
     * The final result will be atMost(k) - atMost(k - 1)
     * <p>
     * Time: O(n) we will traverse the array one time to compute atMost(k) for any k
     * Space: O(n) and an integer array to count the number of appearances for each number
     */
    public int subarraysWithKDistinctSlidingWindow(int[] nums, int k) {
        return atMostK(nums, k) - atMostK(nums, k - 1);
    }

    private int atMostK(int[] nums, int k) {
        int[] count = new int[nums.length + 1];
        // i is the left bound of the window
        int res = 0, i = 0;
        for (int j = 0; j < nums.length; j++) {
            // decrement k since we meet a new integer
            if (count[nums[j]] == 0) k--;
            // always increment the count for that integer
            count[nums[j]]++;

            // if we have more than k distinct integers in the subarray
            // try to shrink the window and push out one integer
            while (k < 0) {
                // push out the integer at the left bound of the window
                count[nums[i]]--;
                // if the integer has been pushed from the subarray completely
                // increment the k back
                if (count[nums[i]] == 0) k++;
                // shrink the window afterwards by sliding the left bound
                i++;
            }
            // add the current window length to the res
            res += j - i + 1;
        }
        return res;
    }

    @Test
    public void subarraysWithKDistinctTest() {
        /**
         * Example 1:
         * Input: nums = [1,2,1,2,3], k = 2
         * Output: 7
         * Explanation: Subarrays formed with exactly 2 different integers:
         * [1,2], [2,1], [1,2], [2,3], [1,2,1], [2,1,2], [1,2,1,2]
         */
        assertEquals(7, subarraysWithKDistinctBruteForce(new int[]{1, 2, 1, 2, 3}, 2));
        assertEquals(7, subarraysWithKDistinctSlidingWindow(new int[]{1, 2, 1, 2, 3}, 2));
        /**
         * Example 3:
         * Input: nums = [1,2,1,3,4], k = 3
         * Output: 3
         * Explanation: Subarrays formed with exactly 3 different integers: [1,2,1,3], [2,1,3], [1,3,4].
         */
        assertEquals(3, subarraysWithKDistinctBruteForce(new int[]{1, 2, 1, 3, 4}, 3));
        assertEquals(3, subarraysWithKDistinctSlidingWindow(new int[]{1, 2, 1, 3, 4}, 3));
    }
}
