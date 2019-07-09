import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;


public class majorityElements {

    /**
     * Given an integer array of size n, find all elements that appear more than ⌊ n/3 ⌋ times.
     *
     * Approach 1: Hash Map
     * The simplest way would be iterate over the whole array and record the corresponding frequency of each number. If any of the number
     * occurs more than N / 3 times (where N is the length of the array), we push the number into a result set.
     *
     * Time: O(N), we iterate over the array by one pass
     * Space: O(N), in the worst case (no majority elements), we need to put N values in the hash map
     */
    public List<Integer> majorityElementHashMap(int[] nums) {
        Map<Integer, Integer> numCount = new HashMap<>();
        Set<Integer> res = new HashSet<>();
        for(int num : nums) {
            numCount.put(num, numCount.getOrDefault(num, 0) + 1);
            if(numCount.get(num) > nums.length / 3) res.add(num);
        }
        return new ArrayList<>(res);
    }

    /**
     * Note: The algorithm should run in linear time and in O(1) space.
     * Approach 2: Generalized Boyer-Moore Majority Voting Algorithm
     * In order to reduce the space complexity into O(1), we need to implement generalized Boyer-Moore majority voting algorithm. There
     * are at most 3 elements with frequency n / 3 in a given array. Hence the major element whose frequency is larger than n / 3 is at
     * most 2. In this case, we can implement a similar algorithm to keep tracking two candidate numbers, just like in original majority
     * voting algorithm. Since Boyer-Moore will always find a value in the array no matter whether there is a majority element in the array.
     * We need a second pass to check whether the candidate number appears actually more than n / 3 times. If so, we add it to a final list.
     *
     * Time: O(2N) = O(N), we iterate the whole array twice, each cost O(N) time.
     * Space: O(1), no extra space required
     */
    public List<Integer> majorityElementBoyerMoore(int[] nums) {
        Set<Integer> res = new HashSet<>();
        int count1 = 0, count2 = 0;
        int candidate1 = 0, candidate2 = 0;
        // First pass, to find candidate numbers
        for(int num : nums) {
            if(candidate1 == num) {
                count1 += 1;
            } else if(candidate2 == num) {
                count2 += 1;
            } else if(count1 == 0) {
                candidate1 = num;
                count1 += 1;
            } else if(count2 == 0) {
                candidate2 = num;
                count2 += 1;
            } else {
                count1 -= 1;
                count2 -= 1;
            }
        }
        count1 = 0;
        count2 = 0;
        // Second pass, to check the frequency is actually larger than n / 3 times
        for(int num : nums) {
            if(num == candidate1) count1 += 1;
            if(num == candidate2) count2 += 1;
        }
        if(count1 > nums.length / 3) res.add(candidate1);
        if(count2 > nums.length / 3) res.add(candidate2);
        return new ArrayList<>(res);
    }


    @Test
    public void majorityElementBoyerMooreTest() {
        /**
         * Example 1:
         * Input: [3,2,3]
         * Output: [3]
         */
        int[] nums1 = new int[]{3, 2, 3};
        List<Integer> expected1 = new LinkedList<>(Arrays.asList(3));
        List<Integer> actual1 = majorityElementBoyerMoore(nums1);
        assertEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: [1,1,1,3,3,2,2,2]
         * Output: [1,2]
         */
        int[] nums2 = new int[]{1, 1, 1, 3, 3, 2, 2, 2};
        List<Integer> expected2 = new LinkedList<>(Arrays.asList(1, 2));
        List<Integer> actual2 = majorityElementBoyerMoore(nums2);
        assertEquals(expected2, actual2);
        /**
         * Example 3:
         * Input: [1,2,3]
         * Output: [] there is no major element in this case
         */
        int[] nums3 = new int[]{1 ,2 ,3};
        List<Integer> expected3 = new LinkedList<>();
        List<Integer> actual3 = majorityElementBoyerMoore(nums3);
        assertEquals(expected3, actual3);
    }


    @Test
    public void majorityElementHashMapTest() {
        /**
         * Example 1:
         * Input: [3,2,3]
         * Output: [3]
         */
        int[] nums1 = new int[]{3, 2, 3};
        List<Integer> expected1 = new LinkedList<>(Arrays.asList(3));
        List<Integer> actual1 = majorityElementHashMap(nums1);
        assertEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: [1,1,1,3,3,2,2,2]
         * Output: [1,2]
         */
        int[] nums2 = new int[]{1, 1, 1, 3, 3, 2, 2, 2};
        List<Integer> expected2 = new LinkedList<>(Arrays.asList(1, 2));
        List<Integer> actual2 = majorityElementHashMap(nums2);
        assertEquals(expected2, actual2);
        /**
         * Example 3:
         * Input: [1,2,3]
         * Output: [] there is no major element in this case
         */
        int[] nums3 = new int[]{1 ,2 ,3};
        List<Integer> expected3 = new LinkedList<>();
        List<Integer> actual3 = majorityElementHashMap(nums3);
        assertEquals(expected3, actual3);
    }
}
