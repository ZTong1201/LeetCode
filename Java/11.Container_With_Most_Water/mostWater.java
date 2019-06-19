import org.junit.Test;
import static org.junit.Assert.*;

public class mostWater {

    /**
     * Given n non-negative integers a1, a2, ..., an , where each represents a point at coordinate (i, ai).
     * n vertical lines are drawn such that the two endpoints of line i is at (i, ai) and (i, 0).
     * Find two lines, which together with x-axis forms a container, such that the container contains the most water.
     *
     * Note: You may not slant the container and n is at least 2.
     *
     * Two pointers
     * We should notice that the maximum area is determined by the magnitude of the smallest height we can obtain. Given a smaller height
     * of one of the two pointers, we want to extend the length as long as possible. This will give us the maximum area for that
     * particular shorter height. Hence, if at each step, we always move the shorter height to its next step. We keep update the
     * maximum area obtained so far and finally return the largest we can get.
     *
     * Time: O(N), we iterate over the array by one pass
     * Space: O(1), we only assign pointers and keep track of the maximum area seen so far. Hence it only requires a constant size of space
     */
    public int maxArea(int[] height) {
        int left = 0;
        int right = height.length - 1;
        int res = 0;
        while(left < right) {
            res = Math.max(res, Math.min(height[left], height[right]) * (right - left));
            if(height[left] <= height[right]) left += 1;
            else right -= 1;
        }
        return res;
    }

    @Test
    public void maxAreaTest() {
        /**
         * Example 1:
         * Input: [1,8,6,2,5,4,8,3,7]
         * Output: 49
         */
        int[] height1 = new int[]{1,8,6,2,5,4,8,3,7};
        assertEquals(49, maxArea(height1));
        /**
         * Example 2:
         * Input: [1,3,2,5,25,24,5]
         * Output: 24
         */
        int[] height2 = new int[]{1,3,2,5,25,24,5};
        assertEquals(24, maxArea(height2));
    }
}
