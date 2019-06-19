import org.junit.Test;
import static org.junit.Assert.*;

public class twoSumII {

    /**
     * Given an array of integers that is already sorted in ascending order,
     * find two numbers such that they add up to a specific target number.
     *
     * The function twoSum should return indices of the two numbers such that they add up to the target,
     * where index1 must be less than index2.
     *
     * Note:
     * 1. Your returned answers (both index1 and index2) are not zero-based.
     * 2. You may assume that each input would have exactly one solution and you may not use the same element twice.
     *
     * Two pointers
     * If the array is sorted, we can simply find the target sum by using two pointers. We assign two pointers at the front and at
     * the end. If the two numbers our pointers point to sum to the target, we find our target pair. If the sum is smaller, we move
     * the front pointer one step to the right. Similarly, if the sum is larger than target, we move the end pointer on step to the left.
     * The only thing we need to care about is overflow. Instead of summing two numbers in the array, we can compare numbers[i] with
     * target - numbers[j] to avoid overflow.
     *
     * Time: O(N), using two pointers, we actually traverse the array by one pass
     * Space: O(1), only assign two pointers, no extra space required
     */
    public int[] twoSum(int[] numbers, int target) {
        int i = 0;
        int j = numbers.length - 1;
        while(i < j) {
            if(numbers[i] == target - numbers[j]) break;
            else if(numbers[i] > target - numbers[j]) j -=1;
            else i += 1;
        }
        return new int[]{i + 1, j + 1};
    }

    @Test
    public void twoSumTest() {
        /**
         * Example 1:
         * Input: numbers = [2,7,11,15], target = 9
         * Output: [1,2]
         * Explanation: The sum of 2 and 7 is 9. Therefore index1 = 1, index2 = 2.
         */
        int[] numbers1 = new int[]{2, 7, 11, 15};
        int[] actual1 = twoSum(numbers1, 9);
        int[] expected1 = new int[]{1, 2};
        assertArrayEquals(expected1, actual1);
        /**
         * Example 1:
         * Input: numbers = [2,3,5,11,15], target = 16
         * Output: [3, 4]
         * Explanation: The sum of 16 and 5 is 11. Therefore index1 = 3, index2 = 4.
         */
        int[] numbers2 = new int[]{2, 3, 5, 11, 15};
        int[] actual2 = twoSum(numbers2, 16);
        int[] expected2 = new int[]{3, 4};
        assertArrayEquals(expected2, actual2);
    }
}
