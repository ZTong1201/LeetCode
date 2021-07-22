import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class climbStairs {

    /**
     * You are climbing a stair case. It takes n steps to reach to the top.
     * <p>
     * Each time you can either climb 1 or 2 steps. In how many distinct ways can you climb to the top?
     * <p>
     * Note: Given n will be a positive integer.
     * <p>
     * Approach 1: Brute Force (Recursion)
     * <p>
     * Time: O(2^n) recursion tree is of size 2^n
     * Space: O(n) in the worst case, the call stack requires O(n)
     */
    public int climbStairsRecursive(int n) {
        if (n <= 1) return 1;
        return climbStairsRecursive(n - 1) + climbStairsRecursive(n - 2);
    }

    /**
     * Approach 2: Recursion with memorization
     * <p>
     * Time: O(n)
     * Space: O(n) need extra space to store visited nodes
     */
    public int climbStairsMemorization(int n) {
        int[] memo = new int[n + 1];
        return climb(memo, n);
    }

    private int climb(int[] memo, int n) {
        if (n <= 1) return 1;
        // if memo[n] is not 0, which means it's been visited
        // return the memorized value
        if (memo[n] != 0) return memo[n];
        // update memo array
        memo[n] = climb(memo, n - 1) + climb(memo, n - 2);
        // return the last value in the array
        return memo[n];
    }

    /**
     * Approach 3: Dynamic Programming
     * <p>
     * <p>
     * Time: O(n)
     * Space: O(n)
     */
    public int climbStairsDP(int n) {
        int[] dp = new int[n + 1];
        dp[0] = 1;
        dp[1] = 1;
        for (int i = 2; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        return dp[n];
    }

    /**
     * Approach 4: Dynamic Programming without extra space
     * <p>
     * Time: O(n)
     * Space: O(1)
     */
    public int climbStairsDPTwoValues(int n) {
        if (n <= 1) return n;
        int f1 = 1;
        int f2 = 1;
        int res = 0;
        for (int i = 2; i <= n; i++) {
            res = f1 + f2;
            f1 = f2;
            f2 = res;
        }
        return res;
    }

    @Test
    public void climbStairsTest() {
        assertEquals(2, climbStairsRecursive(2));
        assertEquals(3, climbStairsRecursive(3));
        assertEquals(8, climbStairsRecursive(5));
        assertEquals(2, climbStairsDP(2));
        assertEquals(3, climbStairsDP(3));
        assertEquals(8, climbStairsDP(5));
        assertEquals(2, climbStairsDPTwoValues(2));
        assertEquals(3, climbStairsDPTwoValues(3));
        assertEquals(8, climbStairsDPTwoValues(5));
        assertEquals(2, climbStairsMemorization(2));
        assertEquals(3, climbStairsMemorization(3));
        assertEquals(8, climbStairsMemorization(5));
    }
}
