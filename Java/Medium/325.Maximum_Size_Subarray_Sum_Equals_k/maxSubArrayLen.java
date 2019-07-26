import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class maxSubArrayLen {
    /**
     * Given an array nums and a target value k, find the maximum length of a subarray that sums to k.
     * If there isn't one, return 0 instead.
     *
     * Note:
     * The sum of the entire nums array is guaranteed to fit within the 32-bit signed integer range.
     *
     * Approach 1: Prefix Sum
     * The naive approach would be simply for every subarray (n(n - 1)/2 in total), check whether the sum equals to k. However, checking the
     * sum of an subarray can be O(n) in the worst case. Hence the brute force approach is O(n^3). We can reduce the sum extraction time
     * to O(1) using a prefix sum array. Note that sum[i] equals to the cumulative sum from index 0 to i - 1. Hence sum[j] - sum[i] will
     * equal to the sum between index i to j - 1. By doing so, we can check any subarray sum in O(1) time
     *
     * Time: O(n^2) total O(n^2) subarrays to be checked
     * Space: O(n) an array of size n + 1 to store prefix sum
     */
    public int maxSubArrayLenPrefixSum(int[] nums, int k) {
        int n = nums.length;
        int[] sum = new int[n + 1];
        for(int i = 1; i <= n; i++) {
            sum[i] = sum[i - 1] + nums[i - 1];
        }

        int res = 0;
        for(int i = 0; i < n; i++) {
            for(int j = i + 1; j <= n; j++) {
                if(sum[j] - sum[i] == k) {
                    res = Math.max(res, j - i);
                }
            }
        }
        return res;
    }

    @Test
    public void maxSubArrayLenPrefixSumTest() {
        /**
         * Example 1:
         * Input: nums = [1, -1, 5, -2, 3], k = 3
         * Output: 4
         * Explanation: The subarray [1, -1, 5, -2] sums to 3 and is the longest.
         */
        int[] nums1 = new int[]{1, -1, 5, -2, 3};
        assertEquals(4, maxSubArrayLenPrefixSum(nums1, 3));
        /**
         * Example 2:
         * Input: nums = [-2, -1, 2, 1], k = 1
         * Output: 2
         * Explanation: The subarray [-1, 2] sums to 1 and is the longest.
         */
        int[] nums2 = new int[]{-2, -1, 2, 1};
        assertEquals(2, maxSubArrayLenPrefixSum(nums2, 1));
    }

    /**
     * Follow Up: Can you do it in O(n) time?
     *
     * Approach 2: Hash Map
     *
     * Using a hash map, we can reduce the runtime to O(n). Since we have recorded all the prefix sum in a hash map, if we at that
     * some time, we find that currentSum - k is the hash map (i.e. based on such prefix sum, the sum in between will equal to k. It's
     * the same idea as sum[j] - sum[i]). Hence, we always keep the smallest index with a prefix sum value. And we need to initialize
     * the hash map with key 0 and value -1 in it.
     *
     * Time: O(n) single one-pass by computing prefix sum and search a target value in the hash map
     * Space: O(n) in the worst case. We have n different prefix sum and no result can be return.
     */
    public int maxSubArrayLenHashMap(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        //very important. Indicating an empty array has a sum 0
        map.put(0, -1);
        int sum = 0, res = 0;
        for(int i = 0; i < nums.length; i++) {
            //compute the prefix sum
            sum += nums[i];
            if(map.containsKey(sum - k)) {
                //if sum - k is in the hash map, which means the subarray in between equals to k
                //update the result
                res = Math.max(res, i - map.get(sum - k));
            }
            //always keep the smallest index corresponds to the prefix sum
            //hence, if the current prefix sum is not in the hash map, we then put it in the map
            map.putIfAbsent(sum, i);
        }
        return res;
    }

    @Test
    public void maxSubArrayLenHashMapTest() {
        /**
         * Example 1:
         * Input: nums = [1, -1, 5, -2, 3], k = 3
         * Output: 4
         * Explanation: The subarray [1, -1, 5, -2] sums to 3 and is the longest.
         */
        int[] nums1 = new int[]{1, -1, 5, -2, 3};
        assertEquals(4, maxSubArrayLenHashMap(nums1, 3));
        /**
         * Example 2:
         * Input: nums = [-2, -1, 2, 1], k = 1
         * Output: 2
         * Explanation: The subarray [-1, 2] sums to 1 and is the longest.
         */
        int[] nums2 = new int[]{-2, -1, 2, 1};
        assertEquals(2, maxSubArrayLenHashMap(nums2, 1));
    }
}
