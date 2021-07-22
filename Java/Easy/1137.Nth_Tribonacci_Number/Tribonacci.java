import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Tribonacci {

    /**
     * The Tribonacci sequence Tn is defined as follows:
     * <p>
     * T0 = 0, T1 = 1, T2 = 1, and Tn+3 = Tn + Tn+1 + Tn+2 for n >= 0.
     * <p>
     * Given n, return the value of Tn.
     * <p>
     * Constraints:
     * <p>
     * 0 <= n <= 37
     * The answer is guaranteed to fit within a 32-bit integer, ie. answer <= 2^31 - 1.
     * <p>
     * Approach 1: Recursion + memorization
     * <p>
     * Time: O(n)
     * Space: O(n)
     */
    public int tribonacciMemorization(int n) {
        int[] memo = new int[n + 1];
        return dfs(memo, n);
    }

    private int dfs(int[] memo, int n) {
        if (n <= 2) return n == 0 ? 0 : 1;
        if (memo[n] != 0) return memo[n];
        memo[n] = dfs(memo, n - 1) + dfs(memo, n - 2) + dfs(memo, n - 3);
        return memo[n];
    }

    /**
     * Approach 2: Dynamic Programming (extra space)
     * <p>
     * Time: O(n)
     * Space: O(n)
     */
    public int tribonacciDpExtraSpace(int n) {
        if (n <= 2) return n == 0 ? 0 : 1;
        int[] dp = new int[n + 1];
        dp[1] = 1;
        dp[2] = 1;
        for (int i = 3; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2] + dp[i - 3];
        }
        return dp[n];
    }

    /**
     * Approach 3: DP (three variables)
     * <p>
     * Time: O(n)
     * Space: O(1)
     */
    public int tribonacciDpThreeVariables(int n) {
        if (n <= 2) return n == 0 ? 0 : 1;
        int f1 = 0, f2 = 1, f3 = 1;
        int res = 0;
        for (int i = 3; i <= n; i++) {
            res = f1 + f2 + f3;
            f1 = f2;
            f2 = f3;
            f3 = res;
        }
        return res;
    }

    @Test
    public void tribonacciTest() {
        /**
         * Example 1:
         * Input: n = 4
         * Output: 4
         * Explanation:
         * T_3 = 0 + 1 + 1 = 2
         * T_4 = 1 + 1 + 2 = 4
         */
        assertEquals(4, tribonacciMemorization(4));
        assertEquals(4, tribonacciDpExtraSpace(4));
        assertEquals(4, tribonacciDpThreeVariables(4));
        /**
         * Example 2:
         * Input: n = 25
         * Output: 1389537
         */
        assertEquals(1389537, tribonacciMemorization(25));
        assertEquals(1389537, tribonacciDpExtraSpace(25));
        assertEquals(1389537, tribonacciDpThreeVariables(25));
    }

}
