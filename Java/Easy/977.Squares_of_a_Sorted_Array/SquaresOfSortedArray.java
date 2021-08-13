import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class SquaresOfSortedArray {

    /**
     * Given an integer array nums sorted in non-decreasing order, return an array of the squares of each number sorted in
     * non-decreasing order.
     * <p>
     * Constraints:
     * <p>
     * 1 <= nums.length <= 10^4
     * -10^4 <= nums[i] <= 10^4
     * nums is sorted in non-decreasing order.
     * <p>
     * Follow up: Squaring each element and sorting the new array is very trivial, could you find an O(n) solution using a
     * different approach?
     * <p>
     * Approach: Two pointers
     * For a sorted array with both negative and positive numbers, e.g. [-3, -2, -1, 4, 5, 6], we would like to traverse the
     * negative part in the reverse order (hence we get [1, 4, 9]) and the positive part in the forward direction (hence
     * we get [16, 25, 36]). Therefore, if we have two pointers starting from the left and the right bound, and always append
     * the number with a larger square value to the result array, the merged array will be in a sorted order.
     * <p>
     * Time: O(n) one-pass
     * Space: O(1)
     */
    public int[] sortedSquares(int[] nums) {
        int n = nums.length;
        int[] res = new int[n];
        // left: traverse the negative part (if any) from the largest square value -> the smallest
        // right traverse the positive part (if any) also from the largest to the smallest
        int left = 0, right = n - 1;

        // since the largest square value is calculated first - append to the array in the reverse order
        for (int i = n - 1; i >= 0; i--) {
            int square;
            if (Math.abs(nums[left]) < Math.abs(nums[right])) {
                square = Math.abs(nums[right]);
                right--;
            } else {
                square = Math.abs(nums[left]);
                left++;
            }
            res[i] = square * square;
        }
        return res;
    }

    @Test
    public void sortedSquaresTest() {
        /**
         * Example 1:
         * Input: nums = [-4,-1,0,3,10]
         * Output: [0,1,9,16,100]
         * Explanation: After squaring, the array becomes [16,1,0,9,100].
         * After sorting, it becomes [0,1,9,16,100].
         */
        int[] expected1 = new int[]{0, 1, 9, 16, 100};
        int[] actual1 = sortedSquares(new int[]{-4, -1, 0, 3, 10});
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: nums = [-7,-3,2,3,11]
         * Output: [4,9,9,49,121]
         */
        int[] expected2 = new int[]{4, 9, 9, 49, 121};
        int[] actual2 = sortedSquares(new int[]{-7, -3, 2, 3, 11});
        assertArrayEquals(expected2, actual2);
    }
}
