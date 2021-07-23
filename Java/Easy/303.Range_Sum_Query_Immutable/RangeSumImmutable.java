import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RangeSumImmutable {

    /**
     * Given an integer array nums, find the sum of the elements between indices i and j (i ≤ j), inclusive.
     * <p>
     * Note:
     * You may assume that the array does not change.
     * There are many calls to sumRange function.
     * <p>
     * Approach 1: Prefix Sum
     * 暴力解法为直接算给定range的和，每次时间为O(N)，因此若call很多次，则需要O(MN)时间，当M和N很大时，会很耗时。可以使用prefix sum，先将到每个index为止
     * 前面所有元素的和记录下来。第一遍初始化会需要O(N)时间，但之后在query range的时候，只需返回sum[j + 1] - sum[i]即可，为O(1)时间。
     * <p>
     * <p>
     * Time: O(N + M)，初始化需要O(N)时间，每次call query需要O(1)时间，call M次需要O(M)时间
     * Space: O(N) 需要一个数组记录prefix sum
     */
    private static class NumArray {

        private final int[] preSum;

        public NumArray(int[] nums) {
            this.preSum = new int[nums.length + 1];
            for (int i = 1; i <= nums.length; i++) {
                this.preSum[i] = this.preSum[i - 1] + nums[i - 1];
            }
        }

        public int sumRange(int left, int right) {
            return preSum[right + 1] - preSum[left];
        }

    }

    @Test
    public void numArrayTest() {
        /**
         * Given nums = [-2, 0, 3, -5, 2, -1]
         *
         * sumRange(0, 2) -> 1
         * sumRange(2, 5) -> -1
         * sumRange(0, 5) -> -3
         */
        int[] nums = new int[]{-2, 0, 3, -5, 2, -1};
        NumArray numArray = new NumArray(nums);
        assertEquals(1, numArray.sumRange(0, 2));
        assertEquals(-1, numArray.sumRange(2, 5));
        assertEquals(-3, numArray.sumRange(0, 5));
    }
}
