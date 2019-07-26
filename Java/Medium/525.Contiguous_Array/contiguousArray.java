import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class contiguousArray {

    /**
     * Given a binary array, find the maximum length of a contiguous subarray with equal number of 0 and 1.
     *
     * Approach 1: Hash Map
     *
     * We can treat 0 as -1, and as we iterate over the array, we compute the cumulative sum so far. Note that if at two different index
     * i and j, they have the same sum. By our definition, there must be equal size of 1's and 0's in between. Hence the length of this
     * subarrary will simply be j - i + 1; To achieve this, we initialize the map with key 0 and value -1, indicates that the empty array
     * has a sum of 0. As we pass through the array, we update the final result by taking the maximum
     *
     * Time: O(n) single one-pass
     * Space: O(n) in the worst case, for all 0's or all 1's
     */
    public int findMaxLengthHashMap(int[] nums) {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, -1);
        int res = 0, count = 0;
        for(int i = 0; i < nums.length; i++) {
            count += (nums[i] == 0 ? -1 : 1);
            if(map.containsKey(count)) {
                res = Math.max(res, i - map.get(count));
            } else {
                map.put(count, i);
            }
        }
        return res;
    }

    @Test
    public void findMaxLengthHashMapTest() {
        /**
         * Example 1:
         * Input: [0,1]
         * Output: 2
         * Explanation: [0, 1] is the longest contiguous subarray with equal number of 0 and 1.
         */
        int[] nums1 = new int[]{0, 1};
        assertEquals(2, findMaxLengthHashMap(nums1));
        /**
         * Example 2:
         * Input: [0,1,0]
         * Output: 2
         * Explanation: [0, 1] (or [1, 0]) is a longest contiguous subarray with equal number of 0 and 1.
         */
        int[] nums2 = new int[]{0, 1, 0};
        assertEquals(2, findMaxLengthHashMap(nums2));
        /**
         * Example 3:
         * Input: [1, 1, 1, 1, 0, 0, 1, 0]
         * Output: 6
         * Explanation: [1, 1, 0, 0, 1, 0] is the longest contiguous subarray with equal number of 0 and 1.
         */
        int[] nums3 = new int[]{1, 1, 1, 1, 0, 0, 1, 0};
        assertEquals(6, findMaxLengthHashMap(nums3));
    }

    /**
     * Approach 2: Using Array as Hash Map
     * Since computing hash code and extract value from the key can take time as the length of array increases, we can use an array
     * as a pseudo hash map for faster extraction. For an array of size n, the sum of the entire array is in range [-n, n], hence
     * 2*n + 1 possible values in total. We can build an array of size 2*n + 1 as the hash map, and we should be careful that the sum
     * can be negative, hence we need an offset n. (i.e. if the sum is -n, the corresponding index is -n + n = 0) We initialize the
     * entire array with all MIN_VALUE indicating we haven't visited any of them. As we compute the prefix sum, we have three case
     *
     * 1. If the current sum is 0, we update the current length
     * 2. If the current sum has been visited (i.e. sum[count + offset] != MIN_VALUE), we update the current length by subtract the
     *    previous index
     * 3. Otherwise, we add the index to the prefix sum
     *
     * Time: O(n) single one-pass
     * Space: O(2*n + 1) = O(n)
     */
    public int findMaxLengthArray(int[] nums) {
        int n = nums.length;
        //build an array of size 2 * n + 1 as a hash map
        int[] sum = new int[2*n + 1];
        //Initialize the array with MIN_VALUE indicating we haven't visited any of them
        Arrays.fill(sum, Integer.MIN_VALUE);

        int res = 0, count = 0;
        //loop through the entire array and compute the prefix sum
        for(int i = 0; i < n; i++) {
            //treat 0 as -1
            count += (nums[i] == 0 ? -1 : 1);
            //if current prefix sum is 0, update the length
            if(count == 0) {
                res = i + 1;
                //if the current sum is not 0, but have been visited
                //be careful about the offset!
            } else if(sum[count + n] != Integer.MIN_VALUE) {
                //still update the current length
                res = Math.max(res, i - sum[count + n]);
            } else {
                //otherwise, add current index to that prefix sum
                sum[count + n] = i;
            }
        }
        return res;
    }


    @Test
    public void findMaxLengthArrayTest() {
        /**
         * Example 1:
         * Input: [0,1]
         * Output: 2
         * Explanation: [0, 1] is the longest contiguous subarray with equal number of 0 and 1.
         */
        int[] nums1 = new int[]{0, 1};
        assertEquals(2, findMaxLengthArray(nums1));
        /**
         * Example 2:
         * Input: [0,1,0]
         * Output: 2
         * Explanation: [0, 1] (or [1, 0]) is a longest contiguous subarray with equal number of 0 and 1.
         */
        int[] nums2 = new int[]{0, 1, 0};
        assertEquals(2, findMaxLengthArray(nums2));
        /**
         * Example 3:
         * Input: [1, 1, 1, 1, 0, 0, 1, 0]
         * Output: 6
         * Explanation: [1, 1, 0, 0, 1, 0] is the longest contiguous subarray with equal number of 0 and 1.
         */
        int[] nums3 = new int[]{1, 1, 1, 1, 0, 0, 1, 0};
        assertEquals(6, findMaxLengthArray(nums3));
    }
}
