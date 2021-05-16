import org.junit.Test;
import static org.junit.Assert.*;

public class numArray {

    /**
     * Given an integer array nums, find the sum of the elements between indices i and j (i ≤ j), inclusive.
     *
     * Note:
     * You may assume that the array does not change.
     * There are many calls to sumRange function.
     *
     * Approach 1: Prefix Sum
     * 暴力解法为直接算给定range的和，每次时间为O(N)，因此若call很多次，则需要O(MN)时间，当M和N很大时，会很耗时。可以使用prefix sum，先将到每个index为止
     * 前面所有元素的和记录下来。第一遍初始化会需要O(N)时间，但之后在query range的时候，只需返回sum[j + 1] - sum[i]即可，为O(1)时间。
     *
     *
     * Time: O(N + M)，初始化需要O(N)时间，每次call query需要O(1)时间，call M次需要O(M)时间
     * Space: O(N) 需要一个数组记录prefix sum
     */
    private final int[] preSum;

    public numArray(int[] nums) {
        this.preSum = new int[nums.length + 1];
        for(int i = 1; i <= nums.length; i++) {
            this.preSum[i] = this.preSum[i - 1] + nums[i - 1];
        }
    }

    public int sumRange(int i, int j) {
        return preSum[j + 1] - preSum[i];
    }
}
