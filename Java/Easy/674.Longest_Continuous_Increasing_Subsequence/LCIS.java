import org.junit.Test;
import static org.junit.Assert.*;

public class LCIS {

    /**
     * Given an unsorted array of integers, find the length of longest continuous increasing subsequence (subarray).
     *
     * Approach: Sliding window
     * If the current subsequence is increasing, keep sliding the window to the right, and update current maximum length. Otherwise, put
     * the left side to current index, and reslide the window to the right
     *
     *
     * Time: O(N) single one-pass
     * Space: O(1)
     */
    public int findLengthOfLCIS(int[] nums) {
        int res = 0, anchor = 0; //anchor is the left boundary of the window
        for(int i = 0; i < nums.length; i++) {
            if(i > 0 && nums[i - 1] >= nums[i]) {
                //if current subsequence is decreasing
                //update the anchor
                anchor = i;
            }
            //update current maximum length
            res = Math.max(res, i - anchor + 1);
        }
        return res;
    }

    @Test
    public void findLengthOfLCISTest() {
        /**
         * Example 1:
         * Input: [1,3,5,4,7]
         * Output: 3
         * Explanation: The longest continuous increasing subsequence is [1,3,5], its length is 3.
         * Even though [1,3,5,7] is also an increasing subsequence, it's not a continuous one where 5 and 7 are separated by 4.
         */
        int[] nums1 = new int[]{1, 3, 5, 4, 7};
        assertEquals(3, findLengthOfLCIS(nums1));
        /**
         * Example 2:
         * Input: [2,2,2,2,2]
         * Output: 1
         * Explanation: The longest continuous increasing subsequence is [2], its length is 1.
         */
        int[] nums2 = new int[]{2, 2, 2, 2, 2};
        assertEquals(1, findLengthOfLCIS(nums2));
    }
}
