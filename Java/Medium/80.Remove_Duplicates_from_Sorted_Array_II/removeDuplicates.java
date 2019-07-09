import org.junit.Test;
import static org.junit.Assert.*;

public class removeDuplicates {

    /**
     * Given a sorted array nums, remove the duplicates in-place such that duplicates appeared at most twice and return the new length.
     *
     * Do not allocate extra space for another array, you must do this by modifying the input array in-place with O(1) extra memory.
     *
     * Confused why the returned value is an integer but your answer is an array?
     *
     * Note that the input array is passed in by reference, which means modification to the input array will be known to the caller as well.
     *
     * Two pointers
     *
     * Since the problem requires O(1) extras space, we need two pointers (fast and slow) to record the index of array we locate. Here's the update rule:
     * 1. If the nums[fast] == nums[slow], we have repeated values, keep counting the number of current value.
     * 2. If the count <= 2, we move slow pointer one step, and assign nums[fast] to nums[slow], otherwise we need to fix slow pointer at
     *    its index, and only move fast pointer
     * 3. If nums[fast] != nums[slow], which means we have a new value. We can simply move slow one step ahead, assign nums[fast] to it,
     *    reassign the count to 1, and move the fast pointer
     *
     *
     * Time: O(N) each pointer will at most visit each element once
     * Space: O(1), only assign pointers and a count value, constant space
     */
    public int removeDuplicatesTwoPointers1(int[] nums) {
        int length = nums.length;
        if(length < 3) return length; //If we only have less three elements, the array is good. No modification required
        int count = 1;
        int slow = 0;
        for(int fast = 1; fast < length; fast++) {
            if(nums[fast] == nums[slow]) { // if meet a repeated value
                count += 1;                // update the count
                if(count <= 2) {           // if the count <= 2, move slow pointer, and assign fast value to it. Otherwise, do nothing
                    slow += 1;
                    nums[slow] = nums[fast];
                }
            } else {                       // if meet a new value
                slow += 1;                 // move the slow pointer and assign fast value to it
                nums[slow] = nums[fast];
                count = 1;                 // reassign count to 1
            }
        }
        return slow + 1;                   // in the end, the slow pointer is at the desired index. Since we want a length, we return slow + 1
    }

    /**
     * We can actually omit the count value in the algorithm. By doing so, we have to initialize the two pointers from index 1 and 2.
     * Then we can check whether a given value occurs more than two times using nums[j] == nums[i] && nums[j] == nums[i - 1]
     *
     * Time: O(n)
     * Space: O(1)
     */
    public int removeDuplicatesTwoPointers2Test(int[] nums) {
        int length = nums.length;
        if(length < 3) return length;
        int i = 1;
        int j = 2;
        while(j < length) {
            if(nums[j] == nums[i] && nums[j] == nums[i - 1]) {
                j += 1;
            } else {
                i += 1;
                nums[i] = nums[j];
                j += 1;
            }
        }
        return i + 1;
    }

    @Test
    public void removeDuplicatesTwoPointers1Test() {
        /**
         * Example 1:
         * Given nums = [1,1,1,2,2,3],
         *
         * Your function should return length = 5, with the first five elements of nums being 1, 1, 2, 2 and 3 respectively.
         *
         * It doesn't matter what you leave beyond the returned length.
         */
        int[] nums1 = new int[]{1, 1, 1, 2, 2, 3};
        int[] expected1 = new int[]{1, 1, 2, 2, 3};
        int length1 = removeDuplicatesTwoPointers1(nums1);
        int[] actual1 = new int[length1];
        System.arraycopy(nums1, 0, actual1, 0, length1);
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Given nums = [0,0,1,1,1,1,2,3,3],
         *
         * Your function should return length = 7, with the first seven elements of nums being modified to 0, 0, 1, 1, 2, 3 and 3 respectively.
         *
         * It doesn't matter what values are set beyond the returned length.
         */
        int[] nums2 = new int[]{0, 0, 1, 1, 1, 1, 2, 3, 3};
        int[] expected2 = new int[]{0, 0, 1, 1, 2, 3, 3};
        int length2 = removeDuplicatesTwoPointers1(nums2);
        int[] actual2 = new int[length2];
        System.arraycopy(nums2, 0, actual2, 0, length2);
        assertArrayEquals(expected2, actual2);
    }

    @Test
    public void removeDuplicatesTwoPointers2Test() {
        /**
         * Example 1:
         * Given nums = [1,1,1,2,2,3],
         *
         * Your function should return length = 5, with the first five elements of nums being 1, 1, 2, 2 and 3 respectively.
         *
         * It doesn't matter what you leave beyond the returned length.
         */
        int[] nums1 = new int[]{1, 1, 1, 2, 2, 3};
        int[] expected1 = new int[]{1, 1, 2, 2, 3};
        int length1 = removeDuplicatesTwoPointers2Test(nums1);
        int[] actual1 = new int[length1];
        System.arraycopy(nums1, 0, actual1, 0, length1);
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Given nums = [0,0,1,1,1,1,2,3,3],
         *
         * Your function should return length = 7, with the first seven elements of nums being modified to 0, 0, 1, 1, 2, 3 and 3 respectively.
         *
         * It doesn't matter what values are set beyond the returned length.
         */
        int[] nums2 = new int[]{0, 0, 1, 1, 1, 1, 2, 3, 3};
        int[] expected2 = new int[]{0, 0, 1, 1, 2, 3, 3};
        int length2 = removeDuplicatesTwoPointers2Test(nums2);
        int[] actual2 = new int[length2];
        System.arraycopy(nums2, 0, actual2, 0, length2);
        assertArrayEquals(expected2, actual2);
    }
}
