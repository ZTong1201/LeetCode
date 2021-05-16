import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class ContinuousSubarraySum {

    /**
     * Given a list of non-negative numbers and a target integer k, write a function to check if the array has a continuous subarray of
     * size at least 2 that sums up to a multiple of k, that is, sums up to n*k where n is also an integer.
     *
     * Note:
     *
     * The length of the array won't exceed 10,000.
     * You may assume the sum of all the numbers is in the range of a signed 32-bit integer.
     *
     * Approach 1: Prefix Sum (same with 560)
     * sum[i] records the prefix sum from index 0 to i - 1, hence if we want to check sum(i, j) simply subtract sum[j] by sum[i]
     * The only thing we need to note here is the subarray of size at least 2. Therefore, for a given start index i, we check from
     * index i + 2
     * Corner case: k = 0, since 0 is indivisible, we need to handle it separately, if k = 0, only we have a subarray sum equals to 0
     *             return true.
     *
     * Time: O(n^2), we need to check O(n^2) subarrays in the worst case to find the answer (especially when return false)
     *       check sum(i, j) is O(1) time
     * Space: O(n), need an array to store prefix sum
     */
    public boolean checkSubarraySumPreSum(int[] nums, int k) {
        int[] sum = new int[nums.length + 1];
        for(int i = 1; i < sum.length; i++) {
            sum[i] = sum[i - 1] + nums[i - 1];
        }

        for(int start = 0; start < nums.length; start++) {
            for(int end = start + 2; end <= nums.length; end++) {
                if(k == 0) {
                    if((sum[end] - sum[start]) == 0) return true;
                } else if((sum[end] - sum[start]) % k == 0) return true;
            }
        }
        return false;
    }

    @Test
    public void checkSubarraySumPreSumTest() {
        /**
         * Example 1:
         * Input: [23, 2, 4, 6, 7],  k=6
         * Output: True
         * Explanation: Because [2, 4] is a continuous subarray of size 2 and sums up to 6.
         */
        int[] nums1 = new int[]{23, 2, 4, 6, 7};
        assertTrue(checkSubarraySumPreSum(nums1, 6));
        /**
         * Example 2:
         * Input: [23, 2, 6, 4, 7],  k=6
         * Output: True
         * Explanation: Because [23, 2, 6, 4, 7] is an continuous subarray of size 5 and sums up to 42.
         */
        int[] nums2 = new int[]{23, 2, 6, 4, 7};
        assertTrue(checkSubarraySumPreSum(nums2, 6));
        /**
         * Example 3:
         * Input: [23, 2, 4, 6, 7],  k=0
         * Output: False
         * Explanation: Because no continuous subarray of size larger than 2 and sums up to 0.
         */
        int[] nums3 = new int[]{23, 2, 4, 6, 7};
        assertFalse(checkSubarraySumPreSum(nums3, 0));
        /**
         * Example 4:
         * Input: [0, 0], k=0
         * Output: True
         * Explanation: Because [0, 0] is an continuous subarray of size 2 and sums up to 0.
         */
        int[] nums4 = new int[]{0, 0};
        assertTrue(checkSubarraySumPreSum(nums4, 0));
    }

    /**
     * Approach 2: Hash Map
     * We can use a hash map to store pair of preSum % k and its index. The idea behind the scenes is that if the differenc between two
     * values equals to nk, then they will have the same remainder. When we find a remainder that is already stored in the hash map,
     * we only check the difference between two indices is larger than 1.
     *
     * To handle the corner case, we just initially put a 0 with index -1 in the hash map.
     *
     * Time: O(n), just one loop to construct the hash map and check subarray sums
     * Space: O(min(n, k)), the hash map will contain min(n, k) pairs at most
     */
    public boolean checkSubarraySumHashMap(int[] nums, int k) {
        Map<Integer, Integer> remainders = new HashMap<>();
        //initialize a 0 with index -1
        remainders.put(0, -1);
        int sum = 0;
        for(int i = 0; i < nums.length; i++) {
            sum += nums[i];
            if(k != 0) {
                sum = sum % k;
            }
            if(remainders.containsKey(sum)) {
                if(i - remainders.get(sum) > 1) {
                    return true;
                }
            } else {
                remainders.put(sum, i);
            }
        }
        return false;
    }


    @Test
    public void checkSubarraySumHashMapTest() {
        /**
         * Example 1:
         * Input: [23, 2, 4, 6, 7],  k=6
         * Output: True
         * Explanation: Because [2, 4] is a continuous subarray of size 2 and sums up to 6.
         */
        int[] nums1 = new int[]{23, 2, 4, 6, 7};
        assertTrue(checkSubarraySumHashMap(nums1, 6));
        /**
         * Example 2:
         * Input: [23, 2, 6, 4, 7],  k=6
         * Output: True
         * Explanation: Because [23, 2, 6, 4, 7] is an continuous subarray of size 5 and sums up to 42.
         */
        int[] nums2 = new int[]{23, 2, 6, 4, 7};
        assertTrue(checkSubarraySumHashMap(nums2, 6));
        /**
         * Example 3:
         * Input: [23, 2, 4, 6, 7],  k=0
         * Output: False
         * Explanation: Because no continuous subarray of size larger than 2 and sums up to 0.
         */
        int[] nums3 = new int[]{23, 2, 4, 6, 7};
        assertFalse(checkSubarraySumHashMap(nums3, 0));
        /**
         * Example 4:
         * Input: [0, 0], k=0
         * Output: True
         * Explanation: Because [0, 0] is an continuous subarray of size 2 and sums up to 0.
         */
        int[] nums4 = new int[]{0, 0};
        assertTrue(checkSubarraySumHashMap(nums4, 0));
    }

}
