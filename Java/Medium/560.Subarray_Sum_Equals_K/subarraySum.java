import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class subarraySum {

    /**
     * Given an array of integers and an integer k, you need to find the total number of continuous subarrays whose sum equals to k.
     *
     * Approach 1: Using cumulative sum
     * The naive approach will be inspecting all the subarrays and compute the sum of that subarray, if the sum is k, we increment the result.
     * Searching all the subarrays will require O(n^2), and compute the sum of a subarray can be O(n) in worst case. Hence, the time complexity
     * for this naive approach will be O(n^3).
     *
     * Fortunately, we can use extra space to record the cumulative sum, when we consider sum(i, j) from index i to j - 1, we can use precomputed
     * sum, where sum(i, j) = sum(0, j) - sum(0, i). By doing so, when we inspect each subarray, it is unnecessary to compute the sum by
     * traversing the whole subarray, we can actually retrieve sum(0, j) and sum(0, i) in O(1) space.
     * In this algorithm, sum[i] stores the cumulative sum from 0 to index i - 1. Hence in order to compute sum(i, j), we simply
     * compute sum[j + 1] - sum[i]
     *
     * Time: O(n^2), we have O(n^2) subarrays to check, however checking each sum only requires O(1) time
     * Space: O(n), we need another array to record the cumulative sum
     */
    public int subarraySumUsingCumsum(int[] nums, int k) {
        int[] sum = new int[nums.length + 1]; //need one more space, to set 0 at the front
        int count = 0;
        sum[0] = 0;
        //first, compute the cumulative sum
        for(int i = 1; i < sum.length; i++) sum[i] = sum[i - 1] + nums[i - 1];
        //check all subarrays
        for(int start = 0; start < nums.length; start++) {
            for(int end = start + 1; end <= nums.length; end++) {
                if(sum[end] - sum[start] == k) count += 1;
            }
        }
        return count;
    }

    /**
     * Approach 2: Without Space
     * Actually, we don't need the extra space. For each starting point, we can compute the cumulative sum start from the current starting
     * point ONLY. If at any time, the sum equals to k, we found a subarray. We do this for all the possible end points for every start
     * indices. The MOST IMPORTANT part for this algorithm is to initialize the sum equals to 0 for each iteration
     *
     *
     * Time: O(n^2), we still need to check O(n^2) subarrays
     * Space: O(1), only require constant space
     */
    public int subarraySumWithoutSpace(int[] nums, int k) {
        int count = 0;
        for(int start = 0; start < nums.length; start++) {
            //MOST IMPORTANT! always initialize the sum equals to 0 for each start indices
            int sum = 0;
            for(int end = start; end < nums.length; end++) {
                sum += nums[end];
                if(sum == k) count += 1;
            }
        }
        return count;
    }

    /**
     * Approach 3: Hash Map (BEST!)
     * If sum(0, j) equals to sum(0, i), which means the sum(i, j) is 0 in between. Similarly, if the difference between sum(0, j) and
     * sum(0, i) is k, which means sum(i, j) is k. We can use such knowledge to optimize our algorithm. We can still compute the cumulative
     * sum as we go through the entire array, however, instead of recording them in an array, we store them in a hash map, and record
     * the occurrences of each sum. If at each time we have currSum - k exists in the hash map, we know we can form a subarray in between
     * with the sum equals to k. The number of occurrences is the number of subarrays we can actually construct in between.
     *
     * The key point here is, we need to add a sum 0 with occurrence 1 in the hash map, because one single element is still a subarray.
     * We need to take it into account
     *
     * Time: O(n), we only go through the input array by one-pass
     * Space: O(n), we need a hash map to map from the number occurrences and the sum we seen
     */
    public int subarraySumHashMap(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, 1);
        int count = 0, sum = 0;
        for(int i = 0; i < nums.length; i++) {
            sum += nums[i];
            if(map.containsKey(sum - k)) count += map.get(sum - k);
            map.put(sum, map.getOrDefault(sum, 0) + 1);
        }
        return count;
    }

    @Test
    public void subarraySumHashMapTest() {
        /**
         * Example 1:
         * Input:nums = [1,1,1], k = 2
         * Output: 2
         */
        int[] nums1 = new int[]{1, 1, 1};
        assertEquals(2, subarraySumHashMap(nums1, 2));
        /**
         * Example 2:
         * Input:nums = [1], k = 1
         * Output: 1
         */
        int[] nums2 = new int[]{1};
        assertEquals(1, subarraySumHashMap(nums2, 1));
        /**
         * Example 3:
         * Input:nums = [1], k = 0
         * Output: 0
         */
        int[] nums3 = new int[]{1};
        assertEquals(0, subarraySumHashMap(nums3, 0));
        /**
         * Example 4:
         * Input:nums = [0,0,0,0,0,0,0,0,0,0], k = 0
         * Output: 55
         */
        int[] nums4 = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        assertEquals(55, subarraySumHashMap(nums4, 0));
    }


    @Test
    public void subarraySumUsingCumsumTest() {
        /**
         * Example 1:
         * Input:nums = [1,1,1], k = 2
         * Output: 2
         */
        int[] nums1 = new int[]{1, 1, 1};
        assertEquals(2, subarraySumUsingCumsum(nums1, 2));
        /**
         * Example 2:
         * Input:nums = [1], k = 1
         * Output: 1
         */
        int[] nums2 = new int[]{1};
        assertEquals(1, subarraySumUsingCumsum(nums2, 1));
        /**
         * Example 3:
         * Input:nums = [1], k = 0
         * Output: 0
         */
        int[] nums3 = new int[]{1};
        assertEquals(0, subarraySumUsingCumsum(nums3, 0));
        /**
         * Example 4:
         * Input:nums = [0,0,0,0,0,0,0,0,0,0], k = 0
         * Output: 55
         */
        int[] nums4 = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        assertEquals(55, subarraySumUsingCumsum(nums4, 0));
    }

    @Test
    public void subarraySumWithoutSpaceTest() {
        /**
         * Example 1:
         * Input:nums = [1,1,1], k = 2
         * Output: 2
         */
        int[] nums1 = new int[]{1, 1, 1};
        assertEquals(2, subarraySumWithoutSpace(nums1, 2));
        /**
         * Example 2:
         * Input:nums = [1], k = 1
         * Output: 1
         */
        int[] nums2 = new int[]{1};
        assertEquals(1, subarraySumWithoutSpace(nums2, 1));
        /**
         * Example 3:
         * Input:nums = [1], k = 0
         * Output: 0
         */
        int[] nums3 = new int[]{1};
        assertEquals(0, subarraySumWithoutSpace(nums3, 0));
        /**
         * Example 4:
         * Input:nums = [0,0,0,0,0,0,0,0,0,0], k = 0
         * Output: 55
         */
        int[] nums4 = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        assertEquals(55, subarraySumWithoutSpace(nums4, 0));
    }
}
