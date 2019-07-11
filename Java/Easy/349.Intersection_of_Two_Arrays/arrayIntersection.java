import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class arrayIntersection {

    /**
     * Given two arrays, write a function to compute their intersection.
     *
     * Approach 1: Two Hash Sets
     * The simplest algorithm would be convert the first array to a set, and iterate over the second array to check whether the value
     * is in the previous set. If so, we add it to the final result. Since no duplicates allowed in final result, we use a second hash
     * set
     *
     * Time: O(m + n), we need to loop through two arrays to find the intersection
     * Space: O(m + n), in the worst case, (two arrays are completely different), we add all value into two sets
     */
    public int[] intersectionHashSet(int[] nums1, int[] nums2) {
        Set<Integer> set1 = new HashSet<>();
        Set<Integer> set2 = new HashSet<>();
        for(int num : nums1) set1.add(num);
        for(int num : nums2) {
            if(set1.contains(num)) set2.add(num);
        }
        int[] res = new int[set2.size()];
        int i = 0;
        for(int num : set2) {
            res[i] = num;
            i += 1;
        }
        return res;
    }

    /**
     * Approach 2: Binary search in shorter array.
     * Since we know that searching in a sorted array requires O(logn) time, we could actually sort the shorter array first, which requires
     * O(minLen*log(minLen)) time. And iterate over the longer array to check whether an element is in the shorter array or not.
     *
     * Time: O(mlogm + nlogm) assume m is the minimum length and n is the maximum length
     * Space: O(1) if the final result doesn't account
     */
    public int[] intersectionBinarySearch(int[] nums1, int[] nums2) {
        int len1 = nums1.length, len2 = nums2.length;
        Set<Integer> intersection = new HashSet<>();
        if(len1 >= len2) {
            Arrays.sort(nums2);
            for(int num : nums1) {
                if(binarySearch(num, nums2)) intersection.add(num);
            }
        } else {
            Arrays.sort(nums1);
            for(int num : nums2) {
                if(binarySearch(num, nums1)) intersection.add(num);
            }
        }
        int[] res = new int[intersection.size()];
        int i = 0;
        for(int num : intersection) {
            res[i] = num;
            i += 1;
        }
        return res;
    }


    private boolean binarySearch(int target, int[] nums) {
        int left = 0, right = nums.length - 1;
        while(left <= right) {
            int mid = left + (right - left) / 2;
            if(nums[mid] == target) return true;
            else if(nums[mid] > target) right = mid - 1;
            else left = mid + 1;
        }
        return false;
    }

    @Test
    public void intersectionHashSetTest() {
        /**
         * Example 1:
         * Input: nums1 = [1,2,2,1], nums2 = [2,2]
         * Output: [2]
         */
        int[] nums1 = new int[]{1, 2, 2, 1};
        int[] nums2 = new int[]{2, 2};
        int[] expected1 = new int[]{2};
        int[] actual1 = intersectionHashSet(nums1, nums2);
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: nums1 = [4,9,5], nums2 = [9,4,9,8,4]
         * Output: [9,4]
         */
        int[] nums3 = new int[]{4, 9, 5};
        int[] nums4 = new int[]{9, 4, 9, 8, 4};
        int[] expected2 = new int[]{4, 9};
        int[] actual2 = intersectionHashSet(nums3, nums4);
        assertArrayEquals(expected2, actual2);
    }

    @Test
    public void intersectionBinarySearchTest() {
        /**
         * Example 1:
         * Input: nums1 = [1,2,2,1], nums2 = [2,2]
         * Output: [2]
         */
        int[] nums1 = new int[]{1, 2, 2, 1};
        int[] nums2 = new int[]{2, 2};
        int[] expected1 = new int[]{2};
        int[] actual1 = intersectionBinarySearch(nums1, nums2);
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: nums1 = [4,9,5], nums2 = [9,4,9,8,4]
         * Output: [9,4]
         */
        int[] nums3 = new int[]{4, 9, 5};
        int[] nums4 = new int[]{9, 4, 9, 8, 4};
        int[] expected2 = new int[]{4, 9};
        int[] actual2 = intersectionBinarySearch(nums3, nums4);
        assertArrayEquals(expected2, actual2);
    }
}
