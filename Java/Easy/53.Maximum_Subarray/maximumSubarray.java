import org.junit.Test;
import static org.junit.Assert.*;

public class maximumSubarray {

    /**
     * Given an integer array nums, find the contiguous subarray (containing at least one number)
     * which has the largest sum and return its sum.
     *
     * We can solve this problem using greedy approach, i.e. pick the locally optimal move at each step, and that will
     * lead to the globally optimal solution
     *
     * For each iteration, we will update:
     * 1. current element
     * 2. current local maximum
     * 3. global maximum seen so far
     *
     * Time: O(N), we iterate over the array by one pass
     * Space: O(1), no extra space required
     */
    public int maxSubArrayGreedy(int[] nums) {
        int res = nums[0], currSum = nums[0];
        for(int i = 1; i < nums.length; i++) {
            currSum = Math.max(nums[i], currSum + nums[i]);
            res = Math.max(res, currSum);
        }
        return res;
    }

    /**
     * We can convert the above approach into a dynamic programming. Still, at each step, we'll update current local maximum
     * and the global maximum seen so far.
     *
     * Time: O(N)
     * Space: O(1)
     */
    public int maxSubArrayDP(int[] nums) {
        int res = nums[0];
        for(int i = 1; i < nums.length; i++) {
            if(nums[i - 1] > 0) nums[i] += nums[i - 1];
            res = Math.max(res, nums[i]);
        }
        return res;
    }

    /**
     * The problem can be solved by using divide and conquer. The solution template is as below:
     * 1. Define the base case(s).
     * 2. Split the problem into subproblems and solve them recursively.
     * 3. Merge the solutions for the subproblems to obtain the solution for the original problem.
     *
     * For this problem, we have a algorithm looks like this
     * 1. If n == 1, return a single element
     * 2. leftSum = maxSubArray for the left subarray, i.e. for the first n/2 elements (middle element at index (left+right)/2 always
     *    belongs to the left subarray)
     * 3. rightSum = maxSubArray for the right subarray, i.e. for the last n/2 elements
     * 4. crossSum = maxSubArray containing elements from both left and right subarrays (start form the midpoint of the left subarray to
     *    the midpoint of the right subarray) and hence crossing the middle elements at index (left + right) / 2.
     * 5. Merge the subproblems solutions, i.e. return max(leftSum, rightSum, crossSum)
     *
     * Time: O(NlogN), at each level, we merge all the subproblems cost approximately O(N) time, for a balanced division, the height
     *      will be logN, hence the overall runtime is O(NlogN)
     * Space: O(logN) to keep the recursion stack
     */
    private int crossSum(int[] nums, int left, int right, int mid) {
        if(left == right) return nums[left];

        int leftSum = Integer.MIN_VALUE;
        int currSum = 0;
        for(int i = mid; i >= left; i--) {
            currSum += nums[i];
            leftSum = Math.max(leftSum, currSum);
        }

        int rightSum = Integer.MIN_VALUE;
        currSum = 0;
        for(int i = mid + 1; i <= right; i++) {
            currSum += nums[i];
            rightSum = Math.max(rightSum, currSum);
        }

        return leftSum + rightSum;
    }

    private int helper(int[] nums, int left, int right) {
        if(left == right) return nums[left];

        int mid = left + (right - left) / 2;
        int leftSum = helper(nums, left, mid);
        int rightSum = helper(nums, mid + 1, right);
        int crossSum = crossSum(nums, left, right, mid);
        return Math.max(Math.max(leftSum, rightSum), crossSum);
    }

    public int maxSubArrayDivideAndConquer(int[] nums) {
        return helper(nums, 0, nums.length - 1);
    }

    @Test
    public void maxSubArrayTest() {
        /**
         * Input: [-2,1,-3,4,-1,2,1,-5,4],
         * Output: 6
         * Explanation: [4,-1,2,1] has the largest sum = 6.
         */
        int[] test1 = new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4};
        assertEquals(6, maxSubArrayGreedy(test1));
        assertEquals(6, maxSubArrayDP(test1));
        test1 = new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4};
        assertEquals(6, maxSubArrayDivideAndConquer(test1));
        /**
         * Input: [-2,1,-3,4,-1,2,1,-5,7],
         * Output: 8
         * Explanation: [4,-1,2,1,-5,7] has the largest sum = 8.
         */
        int[] test2 = new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 7};
        assertEquals(8, maxSubArrayGreedy(test2));
        assertEquals(8, maxSubArrayDP(test2));
        test2 = new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 7};
        assertEquals(8, maxSubArrayDivideAndConquer(test2));
    }
}
