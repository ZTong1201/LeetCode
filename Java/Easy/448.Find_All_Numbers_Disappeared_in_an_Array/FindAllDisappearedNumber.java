import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FindAllDisappearedNumber {

    /**
     * Given an array nums of n integers where nums[i] is in the range [1, n], return an array of all the integers in the
     * range [1, n] that do not appear in nums.
     * <p>
     * Constraints:
     * <p>
     * n == nums.length
     * 1 <= n <= 10^5
     * 1 <= nums[i] <= n
     * <p>
     * Approach 1: Hash Map
     * Count the appearance of each number and record into a hash map (can be an array of size n). Then loop through the map
     * again to find 0 occurrence numbers.
     * <p>
     * Time: O(n)
     * Space: O(n)
     */
    public List<Integer> findDisappearedNumbersHashMap(int[] nums) {
        int n = nums.length;
        int[] count = new int[n];
        for (int num : nums) {
            count[num - 1]++;
        }

        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (count[i] == 0) {
                res.add(i + 1);
            }
        }
        return res;
    }

    /**
     * Approach 2: Mark Correct Index with Inplace Modification
     * Actually, we can mark the present number in the array since they're guaranteed to be in range [1, n]. For a given number,
     * e.g. 3, we mark the corresponding value at that index as negative -> nums[3] = -nums[3] if it's not being marked. The
     * idea behind this would be if the array is sorted, then 1 should be at index 0, 2 at index 1, etc. Therefore, if any index
     * i has a positive number, then which means (i + 1) doesn't exist in the array
     * <p>
     * Time: O(n)
     * Space: O(1) we're modifying the array inplace
     */
    public List<Integer> findDisappearedNumbersInplace(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            // find the correct index for the number
            int correctIndex = Math.abs(nums[i]) - 1;

            // if that number hasn't been marked - mark it
            if (nums[correctIndex] > 0) {
                nums[correctIndex] = -nums[correctIndex];
            }
        }

        List<Integer> res = new ArrayList<>();
        // find the index at which there is a positive value
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] > 0) {
                res.add(i + 1);
            }
        }
        return res;
    }

    @Test
    public void findDisappearedNumbersTest() {
        /**
         * Example 1:
         * Input: nums = [4,3,2,7,8,2,3,1]
         * Output: [5,6]
         */
        List<Integer> expected1 = List.of(5, 6);
        List<Integer> actualHashMap1 = findDisappearedNumbersHashMap(new int[]{4, 3, 2, 7, 8, 2, 3, 1});
        List<Integer> actualInplace1 = findDisappearedNumbersInplace(new int[]{4, 3, 2, 7, 8, 2, 3, 1});
        assertEquals(expected1, actualHashMap1);
        assertEquals(expected1, actualInplace1);
        /**
         * Example 2:
         * Input: nums = [1,1]
         * Output: [2]
         */
        List<Integer> expected2 = List.of(2);
        List<Integer> actualHashMap2 = findDisappearedNumbersHashMap(new int[]{1, 1});
        List<Integer> actualInplace2 = findDisappearedNumbersInplace(new int[]{1, 1});
        assertEquals(expected2, actualHashMap2);
        assertEquals(expected2, actualInplace2);
    }
}
