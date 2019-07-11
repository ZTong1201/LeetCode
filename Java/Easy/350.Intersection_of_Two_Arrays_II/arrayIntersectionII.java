import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class arrayIntersectionII {

    /**
     * Given two arrays, write a function to compute their intersection.
     * Note:
     *
     * Each element in the result should appear as many times as it shows in both arrays.
     * The result can be in any order.
     *
     * Follow up:
     *
     * What if the given array is already sorted? How would you optimize your algorithm?
     * What if nums1's size is small compared to nums2's size? Which algorithm is better?
     * What if elements of nums2 are stored on disk, and the memory is limited such that you cannot load all elements into the memory at once?
     *
     * Approach 1: Hash Map
     * Since in this problem, we need to return all values appear in both arrays, i.e. we allow duplicates. Hence, we need to use a hash
     * map to record the frequency of values in the first array. Then we iterate over the second array to check whether there is a same
     * value left in hash map, if so, we add this common value to our final list, and decrement the frequency by 1
     *
     * Time: O(m + n), we need to loop through all the values in both array to find the intersection
     * Space: O(m), in the worst case, the first array has no duplicates, we need a hash map to record their appearance
     */
    public int[] intersectHashMap(int[] nums1, int[] nums2) {
        Map<Integer, Integer> freqMap = new HashMap<>();
        for(int num : nums1) freqMap.put(num, freqMap.getOrDefault(num, 0) + 1);

        List<Integer> intersection = new ArrayList<>();
        for(int num : nums2) {
            if(freqMap.containsKey(num) && freqMap.get(num) > 0) {
                intersection.add(num);
                freqMap.put(num, freqMap.get(num) - 1);
            }
        }
        int length = intersection.size();
        int[] res = new int[length];
        for(int i = 0; i < length; i++) {
            res[i] = intersection.get(i);
        }
        return res;
    }

    /**
     * Approach 2: Two Pointers (better algorithm if input arrays are sorted)
     * If the input arrays are already sorted, it is not necessary to traverse all the values and record their frequency to find the intersection
     * We can assign two pointers at the start of each array. If at any time, the pointers point to a same value, we add this common value
     * into a final list. Otherwise, we keep moving the pointer with a smaller value.
     *
     * Time: O(min(n, m)), since the arrays are sorted. When we done searching the shorter array, the intersection is determined.
     * Space: O(1), if the final result list doesn't account, we only assign two pointers, which require a constant space
     */
    public int[] intersectTwoPointers(int[] nums1, int[] nums2) {
        Arrays.sort(nums1);
        Arrays.sort(nums2);
        List<Integer> intersection = new ArrayList<>();
        int p1 = 0, p2 = 0;
        int len1 = nums1.length, len2 = nums2.length;
        while(p1 < len1 && p2 < len2) {   //if we reach the end of the shorter array, we done searching
            int num1 = nums1[p1];
            int num2 = nums2[p2];
            if(num1 == num2) {
                intersection.add(num1);
                p1 += 1;
                p2 += 1;
            }
            else if(num1 < num2) p1 += 1;
            else p2 += 1;
        }
        int[] res = new int[intersection.size()];
        for(int i = 0; i < res.length; i++) {
            res[i] = intersection.get(i);
        }
        return res;
    }


    @Test
    public void intersectTwoPointersTest() {
        /**
         * Example 1:
         * Input: nums1 = [1,2,2,1], nums2 = [2,2]
         * Output: [2,2]
         */
        int[] nums1 = new int[]{1, 2, 2, 1};
        int[] nums2 = new int[]{2, 2};
        int[] expected1 = new int[]{2, 2};
        int[] actual1 = intersectTwoPointers(nums1, nums2);
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: nums1 = [4,9,5], nums2 = [9,4,9,8,4]
         * Output: [4,9] (or [9, 4])
         */
        int[] nums3 = new int[]{4, 9, 5};
        int[] nums4 = new int[]{9, 4, 9, 8, 4};
        int[] expected2 = new int[]{4, 9};
        int[] actual2 = intersectTwoPointers(nums3, nums4);
        assertArrayEquals(expected2, actual2);
    }

    @Test
    public void intersectHashMapTest() {
        /**
         * Example 1:
         * Input: nums1 = [1,2,2,1], nums2 = [2,2]
         * Output: [2,2]
         */
        int[] nums1 = new int[]{1, 2, 2, 1};
        int[] nums2 = new int[]{2, 2};
        int[] expected1 = new int[]{2, 2};
        int[] actual1 = intersectHashMap(nums1, nums2);
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: nums1 = [4,9,5], nums2 = [9,4,9,8,4]
         * Output: [4,9] (or [9, 4])
         */
        int[] nums3 = new int[]{4, 9, 5};
        int[] nums4 = new int[]{9, 4, 9, 8, 4};
        int[] expected2 = new int[]{9, 4};
        int[] actual2 = intersectHashMap(nums3, nums4);
        assertArrayEquals(expected2, actual2);
    }
}
