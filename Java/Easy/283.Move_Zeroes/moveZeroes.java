import org.junit.Test;
import static org.junit.Assert.*;

public class moveZeroes {

    /**
     * Given an array nums, write a function to move all 0's to the end of it while maintaining the relative order of the non-zero elements.
     * Note:
     *
     * You must do this in-place without making a copy of the array.
     * Minimize the total number of operations.
     *
     * Approach 1: Brute Force
     *
     * The easiest way is checking each index, if at any time, nums[i] is 0, we traverse its right part and find the first element
     * and swap these two elements. If we reach the end of the array, we simply have moved all zeros to the end.
     *
     * Time: O(n^2) in the worst case, for each index we have to move all the way to end of the array which will at most cost O(n). Overall
     *      it is O(n^2)
     * Space: O(1) no extra space required
     * Operations: O(n) in the worst case when we have arrays like this [0,1.....,1,1].
     */
    public void moveZeroesBruteForce(int[] nums) {
        for(int i = 0; i < nums.length - 1; i++) {
            if(nums[i] == 0) {
                for(int j = i + 1; j < nums.length; j++) {
                    if(nums[j] != 0) {
                        swap(nums, i, j);
                        break;
                    }
                    //if at any time we reach the end of the array, we are done
                    if(j == nums.length - 1) return;
                }
            }
        }
    }


    private void swap(int[] nums, int a, int b) {
        int tmp = nums[a];
        nums[a] = nums[b];
        nums[b] = tmp;
    }

    @Test
    public void moveZeroesBruteForceTest() {
        /**
         * Example 1:
         * Input: [0,1,0,3,12]
         * Output: [1,3,12,0,0]
         */
        int[] actual = new int[]{0, 1, 0, 3, 12};
        int[] expected = new int[]{1, 3, 12, 0, 0};
        moveZeroesBruteForce(actual);
        assertArrayEquals(expected, actual);
    }

    /**
     * Approach 2: Two Pointers (Overwrite)
     *
     * We can actually use two pointers to solve this problem, the slow pointer points to a current zero element, the fast pointer keep
     * moving until it reaches a non-zero element, then we overwrite slow element (which is 0) by the fast element. If fast reaches the end
     * of array, we have moved all the zeroes to the front. The left will be overwrite whatever left between slow and fast by 0's.
     *
     * Time: O(n), we traverse the array by one-pass
     * Space: O(1)
     * Operations: Always be O(n)
     */
    public void moveZeroesOverwrite(int[] nums) {
        int slow = 0;

        //i is just the fast pointer
        for(int i = 0; i < nums.length; i++) {
            if(nums[i] != 0) {
                nums[slow++] = nums[i];
            }
        }

        for(int j = slow; j < nums.length; j++) {
            nums[j] = 0;
        }
    }

    @Test
    public void moveZeroesOverwrite() {
        /**
         * Example 1:
         * Input: [0,1,0,3,12]
         * Output: [1,3,12,0,0]
         */
        int[] actual = new int[]{0, 1, 0, 3, 12};
        int[] expected = new int[]{1, 3, 12, 0, 0};
        moveZeroesOverwrite(actual);
        assertArrayEquals(expected, actual);
    }

    /**
     * Approach 3: Two pointers (Swap)
     *
     * If we have something like [0,0,...,0,1], it will be crazy to reassign 0 from index 1 to the end since they are almost all 0's.
     * We can actually do swap. If current element is non-zero, we swap the element with the slow element (which is 0), and increment
     * both pointers
     *
     * Time: O(n)
     * Space: O(1)
     * Operations: O(n) in the worst case. Overall, it is just the number of non-zero element left behind
     */
    public void moveZeroesSwap(int[] nums) {
        for(int slow = 0, fast = 0; fast < nums.length; fast++) {
            if(nums[fast] != 0) {
                swap(nums, slow, fast);
                slow += 1;
            }
        }
    }

    @Test
    public void moveZeroesSwapTest() {
        /**
         * Example 1:
         * Input: [0,1,0,3,12]
         * Output: [1,3,12,0,0]
         */
        int[] actual = new int[]{0, 1, 0, 3, 12};
        int[] expected = new int[]{1, 3, 12, 0, 0};
        moveZeroesSwap(actual);
        assertArrayEquals(expected, actual);
    }

}
