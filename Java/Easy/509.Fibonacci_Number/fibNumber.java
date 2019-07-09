import org.junit.Test;
import static org.junit.Assert.*;

public class fibNumber {
    /**
     * The Fibonacci numbers, commonly denoted F(n) form a sequence, called the Fibonacci sequence,
     * such that each number is the sum of the two preceding ones, starting from 0 and 1. That is,
     *
     * F(0) = 0,   F(1) = 1
     * F(N) = F(N - 1) + F(N - 2), for N > 1.
     * Given N, calculate F(N).
     *
     * Just like 70.Climbing Stairs, we can have three simple algorithms.
     * Approach 1: Recursion
     *
     * Time: O(2^n) size of recursion tree is O(2^n)
     * Space: O(n) in the worst case, the call stack will require O(n)
     */
    public int fibRecusive(int N) {
        if(N <= 1) return N;
        return fibRecusive(N - 1) + fibRecusive(N - 2);
    }

    /**
     * Dynamic programming
     *
     * Time: O(N)
     * Space: O(N) requires an extra array to store previous stages
     */
    public int fibDP(int N) {
        if(N <= 1) return N;
        int[] dp = new int[N + 1];
        dp[0] = 0;
        dp[1] = 1;
        for(int i = 2; i < N + 1; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        return dp[N];
    }

    /**
     * DP without extra space
     *
     * Time: O(N)
     * Space: O(1) assigning two values is enough, require constant space
     */
    public int fibDPTwoValues(int N) {
        if(N <= 1) return N;
        int f0 = 0;
        int f1 = 1;
        int res = 0;
        for(int i = 2; i < N + 1; i++) {
            res = f0 + f1;
            f0 = f1;
            f1 = res;
        }
        return res;
    }

    @Test
    public void fibTest() {
        assertEquals(1, fibRecusive(2));
        assertEquals(5, fibRecusive(5));
        assertEquals(55, fibRecusive(10));
        assertEquals(1, fibDP(2));
        assertEquals(5, fibDP(5));
        assertEquals(55, fibDP(10));
        assertEquals(1, fibDPTwoValues(2));
        assertEquals(5, fibDPTwoValues(5));
        assertEquals(55, fibDPTwoValues(10));
    }
}
