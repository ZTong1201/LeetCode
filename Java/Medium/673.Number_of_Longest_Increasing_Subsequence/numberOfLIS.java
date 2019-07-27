import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;

public class numberOfLIS {

    /**
     * Given an unsorted array of integers, find the number of longest increasing subsequence.
     *
     * Note: Length of the given array will be not exceed 2000 and the answer is guaranteed to be fit in 32-bit signed int.
     *
     * Approach 1: Dynamic Programming
     *
     * As in leetcode 300, we might use an array to dynamically update the length of LIS at each index. By maintaining such information,
     * we can actually dynamically update the counts of LIS so far.  For each pair that nums[later] > nums[before].
     * If current length equals to previous length + 1, which means we can form a longer LIS by adding current element. How many ways to form
     * such LISs? The answer is the number of ways to form LIS with previous length. If current length is smaller than previous length + 1,
     * which means we can update the length of LIS (since we can add current element, remember we have nums[later] > nums[before]). The counts
     * will be simply equal to the number of ways forming LIS with previous length.
     *
     * Meanwhile, we need to update the length of LIS so far, if at each index, the length we compute equals to the max length, we can update
     * the final counts. Otherwise, we meet a larger length, we update the length, and modify the final count to the current count
     *
     * Time: O(n^2) two nested loops to look back from the start at each index
     * Space: O(2n) = O(n) two arrays of size n to store length of LIS so far and counts of LIS so far.
     */
    public int findNumberOfLISDP(int[] nums) {
        int length = nums.length;
        //array to record lengths of LIS so far
        int[] lengths = new int[length];
        //array to record number of ways to form that length of LIS
        int[] counts = new int[length];
        //we need to initialize two arrays with 1's
        //indicating that each single element is a LIS of length 1 and the count will be 1
        Arrays.fill(lengths, 1);
        Arrays.fill(counts, 1);
        int maxLen = 0, res = 0;
        //iterate over the entire array
        for(int i = 0; i < length; i++) {
            //look back from the start to update lengths and counts
            for(int j = 0; j < i; j++) {
                //only consider nums[after] > nums[before] pairs, since we need increasing sequence
                if(nums[i] > nums[j]) {
                    //if we can add current element to previous subsequence and form a longer LIS
                    if(lengths[i] == lengths[j] + 1) {
                        //we add the number of ways to form LIS with previous length
                        counts[i] += counts[j];
                    } else if(lengths[i] < lengths[j] + 1) {
                        //otherwise, we update the length of LIS, since we can add one more element
                        //the count will be just the number of ways to form previous LIS
                        counts[i] = counts[j];
                        lengths[i] = lengths[j] + 1;
                    }
                }
            }
            //we need to update the maxLen and the counts
            //if current LIS is the longest
            if(maxLen == lengths[i]) {
                //update the final result
                res += counts[i];
            } else if(maxLen < lengths[i]) { //otherwise, we find a longer LIS
                //update the maxLen and reset the count
                res = counts[i];
                maxLen = lengths[i];
            }
        }
        return res;
    }

    @Test
    public void findNumberOfLISTest() {
        /**
         * Example 1:
         * Input: [1,3,5,4,7]
         * Output: 2
         * Explanation: The two longest increasing subsequence are [1, 3, 4, 7] and [1, 3, 5, 7].
         */
        int[] nums1 = new int[]{1, 3, 5, 4, 7};
        assertEquals(2, findNumberOfLISDP(nums1));
        /**
         * Example 2:
         * Input: [2,2,2,2,2]
         * Output: 5
         * Explanation: The length of longest continuous increasing subsequence is 1, and there are 5 subsequences' length is 1, so output 5.
         */
        int[] nums2 = new int[]{2, 2, 2, 2, 2};
        assertEquals(5, findNumberOfLISDP(nums2));
    }
}
