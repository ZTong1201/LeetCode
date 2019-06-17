import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class majorityElement {

    /**
     * Given an array of size n, find the majority element. The majority element is the element that appears more than ⌊ n/2 ⌋ times.
     *
     * You may assume that the array is non-empty and the majority element always exist in the array.
     *
     * Approach 1: simply sort the array, the majority element will locate at the middle of the array
     *
     * Time: O(NlogN) for sorting
     * Space: O(1) if sorted in-place
     */
    public int majorityElementSorting(int[] nums) {
        Arrays.sort(nums);
        return nums[nums.length / 2];
    }

    /**
     * Approach 2: Hash Map
     * We can build a hash map to record the frequency of each num, if any number appears more than n / 2 times, it is just the majority
     * element.
     *
     * Time: O(N) we iterate over the array by one pass
     * Space: O(N) in the worst case, we will find the majority element after finishing the iteration. We may have to store
     *      N - ⌊ N/2 ⌋ elements in the hash map, which is approximately N/2 in the worst case
     */
    public int majorityElementHashMap(int[] nums) {
        Map<Integer, Integer> numCount = new HashMap<>();
        int res = 0;
        for(int num : nums) {
            numCount.put(num, numCount.getOrDefault(num, 0) + 1);
            if(numCount.get(num) > nums.length / 2) {
                res = num;
                break;
            }
        }
        return res;
    }

    /**
     * Approach 3: Boyer-Moore Voting Algorithm
     * We maintain a count value, increment it whenever we see an instance of our current candidate for majority element and decrement it
     * whenever we see an instance of any other values. If anytime the count equals to 0, we simply forget the previous values up to the
     * current index and consider the current number as the new candidate of majority element.
     *
     * Time: O(N) we iterate over the array by one pass
     * Space: O(1) no extra space required
     */
    public int majorityElementBoyerMoore(int[] nums) {
        int count = 0;
        int res = 0;
        for(int num : nums) {
            if(count == 0) {
                res = num;
                count += 1;
            } else if(res == num) {
                count += 1;
            } else {
                count -= 1;
            }
        }
        return res;
    }

    @Test
    public void majorityElementBoyerMooreTest() {
        int[] nums1 = new int[]{3, 2, 3};
        assertEquals(3, majorityElementBoyerMoore(nums1));
        int[] nums2 = new int[]{2, 2, 1, 1, 1, 2, 2};
        assertEquals(2, majorityElementBoyerMoore(nums2));
    }

    @Test
    public void majorityElementHashMapTest() {
        int[] nums1 = new int[]{3, 2, 3};
        assertEquals(3, majorityElementHashMap(nums1));
        int[] nums2 = new int[]{2, 2, 1, 1, 1, 2, 2};
        assertEquals(2, majorityElementHashMap(nums2));
    }

    @Test
    public void majorityElementSortingTest() {
        int[] nums1 = new int[]{3, 2, 3};
        assertEquals(3, majorityElementSorting(nums1));
        int[] nums2 = new int[]{2, 2, 1, 1, 1, 2, 2};
        assertEquals(2, majorityElementSorting(nums2));
    }
}
