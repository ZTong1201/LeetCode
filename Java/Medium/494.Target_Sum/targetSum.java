import org.junit.Test;
import static org.junit.Assert.*;

public class targetSum {

    /**
     * You are given a list of non-negative integers, a1, a2, ..., an, and a target, S. Now you have 2 symbols + and -.
     * For each integer, you should choose one from + and - as its new symbol.
     *
     * Find out how many ways to assign symbols to make sum of integers equal to target S.
     *
     * Note:
     * The length of the given array is positive and will not exceed 20.
     * The sum of elements in the given array will not exceed 1000.
     * Your output answer is guaranteed to be fitted in a 32-bit integer.
     *
     * Approach 1: Depth-First Search
     *
     * We can build a recursion tree like this
     *                                     root
     *                                   /      \
     *                                 +a1      -a1
     *                                /   \    /   \
     *                              +a2  -a2  +a2  -a2
     *                             ....................
     *
     * When we reach a leaf node (index == nums.length), we check whether the current sum equals to target, if it is, update the result.
     * We can either assign a global variable to maintain results or assign an array of size 1 to store the result value
     *
     * Time: O(2^(n+1)) The recursion tree contains 2^(n + 1) nodes, we have to visit all the nodes to compute the result
     * Space: O(n), The height of tree is n, the recursion call stack require space up to the height of the recursion tree
     */

    public int findTargetSumWaysDFS(int[] nums, int S) {
        int[] res = new int[1];
        //since the sum of the entire array can never be larger than 1000, if we search for a target larger than 1000, simply return 0
        if(S > 1000) return res[0];
        dfs(nums, 0, 0, S, res);
        return res[0];
    }

    private void dfs(int[] nums, int index, int currSum, int S, int[] res) {
        //if we reach a leaf node
        if(index == nums.length) {
            //check whether current sum equals to target sum
            if(currSum == S) {
                //if it is, increment the result
                res[0] += 1;
            }
            //always return since we have reached the leaf node
            return;
        }
        //search left subtree (+ ai)
        dfs(nums, index + 1, currSum + nums[index], S, res);
        //search right subtree (- ai)
        dfs(nums, index + 1, currSum - nums[index], S, res);
    }


    @Test
    public void findTargetSumWaysDFSTest() {
        /**
         * Example:
         * Input: nums is [1, 1, 1, 1, 1], S is 3.
         * Output: 5
         * Explanation:
         *
         * -1+1+1+1+1 = 3
         * +1-1+1+1+1 = 3
         * +1+1-1+1+1 = 3
         * +1+1+1-1+1 = 3
         * +1+1+1+1-1 = 3
         *
         * There are 5 ways to assign symbols to make the sum of nums be target 3.
         */
        int[] nums = new int[]{1, 1, 1, 1, 1};
        assertEquals(5, findTargetSumWaysDFS(nums, 3));
    }

    /**
     * Approach 2: Dynamic Programming
     *
     * The basic idea is if the sum of the entire array (denoted by sum), using all the elements, the sum will be in range [-sum, sum]
     * there are 2 * sum + 1 (including 0) distinct values. We can use dynamic programming to reduce the work load, since
     * 2 * sum + 1 << S. We can denote dp[i][j] as # of ways sum up to j using nums[0~i]. However, since S can be negative yet the index
     * cannot be negative, we can store -sum at index sum, and sum at index 2*sum.
     *
     * By using dp, as we loop through the element of array (0 to n - 1), we should also loop through previous sum j we can achieve (which
     * means dp[i][j] !=  0), hence the values of sum can be j + nums[i + 1] and j - nums[j - 1] when we adding one more element.
     * We should care about the boundary condition as we loop through. In the end, the desired location is stored at dp[n][S + sum]
     * (since S indicates the number of ways sum up to -S)
     *
     * Time: O(n * sum), since there are (2 * sum + 1) * n total cells to be filled, the overall runtime is O(n*sum)
     * Space: O(n * sum), still, we need a 2-D array to store all the values we need.
     */
    public int findTargetSumWaysDP(int[] nums, int S) {
        //first compute the sum of the array
        int sum = 0;
        for(int num : nums) sum += num;
        //if S larger than the array sum or less than the negative sum, we cannot not sum up to S
        if(S > sum || S < -sum) return 0;
        //need a 2-D array to store values
        int[][] dp = new int[nums.length + 1][2 * sum + 1];
        //initialize that if we negate all the elements, we can form -S in only one way
        dp[0][sum] = 1;

        //loop through the entire array
        for(int i = 0; i < nums.length; i++) {
            //then loop through all the sum value we can form using the elements
            //care about boundaries
            for(int j = nums[i]; j < 2 * sum + 1 - nums[i]; j++) {
                //i + 1 means we add one more element
                //j - nums[i] means we can form j - nums[i] by adding nums[i], which equals to dp[i][j]
                dp[i + 1][j - nums[i]] += dp[i][j];
                //j - nums[i] means we can form j - nums[i] by adding nums[i], which equals to dp[i][j] as well
                dp[i + 1][j + nums[i]] += dp[i][j];
            }
        }
        return dp[nums.length][S + sum];
    }

    @Test
    public void findTargetSumWaysDPTest() {
        /**
         * Example:
         * Input: nums is [1, 1, 1, 1, 1], S is 3.
         * Output: 5
         * Explanation:
         *
         * -1+1+1+1+1 = 3
         * +1-1+1+1+1 = 3
         * +1+1-1+1+1 = 3
         * +1+1+1-1+1 = 3
         * +1+1+1+1-1 = 3
         *
         * There are 5 ways to assign symbols to make the sum of nums be target 3.
         */
        int[] nums = new int[]{1, 1, 1, 1, 1};
        assertEquals(5, findTargetSumWaysDP(nums, 3));
    }


    /**
     * Approach 3: Subset Sum (KnapSack problem)
     *
     * We can convert the problem into this: Splitting all the elements into two mutually exclusive sets P and N. which means in subset
     * P we add all of the values, whereas we subtract all the values in subset N. Hence, we have
     *
     * sum(P) - sum(N) = S  is the target sum we care about
     *
     * sum(P) - sum(N) + sum(P) + sum(N) = S + sum(P) + sum(N)
     * => 2 * sum(P) = S + sum(a)   since the union of P and N is the entire array
     * => sum(P) = (S + sum(a)) / 2
     * Therefore, the problem is now that we try to find a subset of the array which sum up to (S + sum(a)) / 2. It is only possible
     * when (S + sum(a)) is an even number, otherwise, it won't be feasible.
     *
     * This is now a knapsack problem.
     * Hence we can scan j for dp[i]
     * dp[i][j] = dp[i - 1][j] + dp[i - 1][j - ai], which means we can either add it into the subset or leave it outside of the subset.
     * If we leave it out of the subset, we have dp[i - 1][j] of ways to form the subset, otherwise, we need to know how many ways
     * we can form j - ai and add ai in we will also obtain j.
     *
     * Actually, we can convert this into a 1-D DP. Since for the iteration of i, we just search for a different size of subarray, we
     * can always make the result size as the same as (S + sum(a)) / 2. Besides, we need to update in the reverse order, since the newly
     * updated value won't influence the upcoming value. If using 1-D DP, the dp[i - 1][j] is actually store in dp[j], and dp[i - 1][j - ai]
     * is stored in dp[j - ai], hence we can increment dp[j] by dp[j - ai] to update the new dp[i][j]
     *
     * Time: O(n * sum) even if we use a 1-D array, the basic idea is the same, we need first loop through each index in the array and then
     *      loop through the 1-D array which is of size sum + 1
     * Space: O(sum) we need a 1-D array to store ways to sum up to j
     */
    public int findTargetSumWaysSubsetSum(int[] nums, int S) {
        int sum = 0;
        for(int num : nums) sum += num;
        //if target sum out of range or (S + sum) is an odd number, we cannot sum up to S
        if(S > sum || S < -sum || (S + sum) % 2 == 1) return 0;

        int target = (S + sum) / 2; //set new target value we care about
        int[] dp = new int[target + 1]; //assign a 1-D array to record number of ways sum up to j
        //initialize index 0 with 1, which indicates that there is one way to sum up to 0 (not adding anything)
        dp[0] = 1;

        //we still need to loop through all the elements in the array
        for(int i = 0; i < nums.length; i++) {
            //then loop through the dp array in reverse order
            //we need j >= nums[i], since the left boundary of index will always be 0
            for(int j = target; j >= nums[i]; j--) {
                dp[j] += dp[j - nums[i]];
            }
        }
        return dp[target];
    }

    @Test
    public void findTargetSumWaysSubsetSumTest() {
        /**
         * Example:
         * Input: nums is [1, 1, 1, 1, 1], S is 3.
         * Output: 5
         * Explanation:
         *
         * -1+1+1+1+1 = 3
         * +1-1+1+1+1 = 3
         * +1+1-1+1+1 = 3
         * +1+1+1-1+1 = 3
         * +1+1+1+1-1 = 3
         *
         * There are 5 ways to assign symbols to make the sum of nums be target 3.
         */
        int[] nums = new int[]{1, 1, 1, 1, 1};
        assertEquals(5, findTargetSumWaysSubsetSum(nums, 3));
    }
}
