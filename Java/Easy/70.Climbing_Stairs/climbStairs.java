import org.junit.Test;
import static org.junit.Assert.*;

public class climbStairs {

    /**
     * You are climbing a stair case. It takes n steps to reach to the top.
     *
     * Each time you can either climb 1 or 2 steps. In how many distinct ways can you climb to the top?
     *
     * Note: Given n will be a positive integer.
     *
     * Approach 1: Brute Force (Recursion)
     *
     * Time: O(2^n) recursion tree is of size 2^n
     * Space: O(n) in the worst case, the call stack requires O(n)
     */
    public int climbStairsRecursive(int n) {
        if(n <= 2) return n;
        return climbStairsRecursive(n - 1) + climbStairsRecursive(n - 2);
    }

    /**
     * Approach 2: Dynamic Programming
     *
     *
     * Time: O(n)
     * Space: O(n)
     */
    public int climbStairsDP(int n) {
        if(n <= 2) return n;
        int[] dp = new int[n];
        dp[0] = 1;
        dp[1] = 2;
        for(int i = 2; i < n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        return dp[n - 1];
    }

    /**
     * Approach 3: Dynamic Programming without extra space
     *
     * Time: O(n)
     * Space: O(1)
     */
    public int climbStairsDPTwoValues(int n) {
        if(n <= 2) return n;
        int f1 = 1;
        int f2 = 2;
        int res = 0;
        for(int i = 2; i < n; i++) {
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


    }
}
