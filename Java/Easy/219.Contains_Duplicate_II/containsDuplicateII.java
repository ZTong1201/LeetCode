import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class containsDuplicateII {

    /**
     * Given an array of integers and an integer k, find out whether there are two distinct indices i and j in the array such that
     * nums[i] = nums[j] and the absolute difference between i and j is at most k.
     *
     * Approach 1: Hash Set
     * 此题可以用sliding window配合hash set来解。若在k个元素内的window内尚未发现重复元素，就将最先加入set的元素移除即可。
     *
     * Time: O(N)
     * Space: O(min(n, k))
     */
    public boolean containsNearbyDuplicateSet(int[] nums, int k) {
        Set<Integer> seen = new HashSet<>();
        for(int i = 0; i < nums.length; i++) {
            if(seen.contains(nums[i])) {
                return true;
            }
            seen.add(nums[i]);
            if(seen.size() > k) {
                seen.remove(nums[i - k]);
            }
        }
        return false;
    }

    /**
     * Approach 2: Hash Map:
     * 也可以直接利用hash map来记录每个元素出现的index，若该元素未出现过，则加入map中，若已出现过，则判断两index之差是否大于k，若小于k，则return true，大于
     * k更新该元素出现的index为当前index
     *
     * Time: O(n)
     * Space: O(n) 最坏情况，没有重复元素，需要将所有元素记录进map
     */
    public boolean containsNearbyDuplicateMap(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        for(int i = 0; i < nums.length; i++) {
            if(map.containsKey(nums[i])) {
                if(i - map.get(nums[i]) <= k) {
                    return true;
                }
            }
            map.put(nums[i], i);
        }
        return false;
    }

    @Test
    public void containsNearbyDuplicateTest() {
        /**
         * Example 1:
         * Input: nums = [1,2,3,1], k = 3
         * Output: true
         */
        int[] nums1 = new int[]{1, 2, 3, 1};
        assertTrue(containsNearbyDuplicateSet(nums1, 3));
        assertTrue(containsNearbyDuplicateMap(nums1, 3));
        /**
         * Example 2:
         * Input: nums = [1,0,1,1], k = 1
         * Output: true
         */
        int[] nums2 = new int[]{1, 0, 1, 1};
        assertTrue(containsNearbyDuplicateSet(nums2, 1));
        assertTrue(containsNearbyDuplicateMap(nums2, 1));
        /**
         * Example 3:
         * Input: nums = [1,2,3,1,2,3], k = 2
         * Output: false
         */
        int[] nums3 = new int[]{1, 2, 3, 1, 2, 3};
        assertFalse(containsNearbyDuplicateSet(nums3, 2));
        assertFalse(containsNearbyDuplicateMap(nums3, 2));
    }
}
