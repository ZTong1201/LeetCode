import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class containsDuplicate {

    /**
     * Given an array of integers, find if the array contains any duplicates.
     *
     * Your function should return true if any value appears at least twice in the array,
     * and it should return false if every element is distinct.
     *
     * Approach 1: Sorting
     * 排序后，只需查看是否有相邻元素相等即可
     *
     * Time: O(nlogn)
     * Space: O(1) 取决于排序的算法
     */
    public boolean containsDuplicateSorting(int[] nums) {
        Arrays.sort(nums);
        for(int i = 0; i < nums.length - 1; i++) {
            if(nums[i] == nums[i + 1]) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void containsDuplicateSortingTest() {
        /**
         * Example 1:
         * Input: [1,2,3,1]
         * Output: true
         */
        int[] nums1 = new int[]{1, 2, 3, 1};
        assertTrue(containsDuplicateSorting(nums1));
        /**
         * Example 2:
         * Input: [1,2,3,4]
         * Output: false
         */
        int[] nums2 = new int[]{1, 2, 3, 4};
        assertFalse(containsDuplicateSorting(nums2));
        /**
         * Example 3:
         * Input: [1,1,1,3,3,4,3,2,4,2]
         * Output: true
         */
        int[] nums3 = new int[]{1, 1, 1, 3, 3, 4, 3, 2, 4, 2};
        assertTrue(containsDuplicateSorting(nums3));
    }

    /**
     * Approach 2: Hash Table
     * 用一个set记录所有出现过的元素，若某元素再此出现则说明重复
     *
     * Time: O(n)
     * Space: O(n)
     */
    public boolean containsDuplicateHashTable(int[] nums) {
        Set<Integer> seen = new HashSet<>();
        for(int num : nums) {
            if(seen.contains(num)) {
                return true;
            }
            seen.add(num);
        }
        return false;
    }


    @Test
    public void containsDuplicateHashTableTest() {
        /**
         * Example 1:
         * Input: [1,2,3,1]
         * Output: true
         */
        int[] nums1 = new int[]{1, 2, 3, 1};
        assertTrue(containsDuplicateHashTable(nums1));
        /**
         * Example 2:
         * Input: [1,2,3,4]
         * Output: false
         */
        int[] nums2 = new int[]{1, 2, 3, 4};
        assertFalse(containsDuplicateHashTable(nums2));
        /**
         * Example 3:
         * Input: [1,1,1,3,3,4,3,2,4,2]
         * Output: true
         */
        int[] nums3 = new int[]{1, 1, 1, 3, 3, 4, 3, 2, 4, 2};
        assertTrue(containsDuplicateHashTable(nums3));
    }
}
