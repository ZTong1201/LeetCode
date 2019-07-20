import org.junit.Test;
import static org.junit.Assert.*;

public class myPow {

    /**
     * Implement pow(x, n), which calculates x raised to the power n (x^n).
     *
     * -100.0 < x < 100.0
     * n is a 32-bit signed integer, within the range [−2^31, 2^31 − 1]
     *
     * Approach 1: Fast Power
     * If we know x^n and we want compute x^2n, no need to multiply x for n more times. Simply (x^n)^2 is enough. We can use this idea
     * to compute the power, as we record the previous computed power x^(n/2). Several cases need to be cared about
     *
     * 1. If n < 0, we convert x = 1 / x, and n = -n to compute the power
     * 2. when n = 0, is the base case, return 1
     * 3. If n is an even number, (x^n/2)^2 will give us x^n. However, if n is an odd number, (x^n/2)^2 = x^(n - 1), we need to multiply one
     * more x
     * 4. since n is in range [−2^31, 2^31 − 1], if n equals to the minimum value of integer, when we negate it, we will have integer
     * overflow, hence we need a long type to store n.
     *
     * Time: O(logN) since we split the problem into subproblems by dividing n by 2, we will have at most logN operations
     * Space: O(logN) the call stack requires O(logN) time
     */
    public double myPow(double x, int n) {
        long N = n;
        if(N < 0) {
            x = 1 / x;
            N = -N;
        }
        return fastPow(x, N);
    }

    private double fastPow(double x, long n) {
        if(n == 0) return 1;
        double half = fastPow(x, n / 2);
        if(n % 2 == 0) {
            return half * half;
        } else {
            return half * half * x;
        }
    }

    @Test
    public void myPowTest() {
        /**
         * Example 1:
         * Input: 2.00000, 10
         * Output: 1024.00000
         */
        assertEquals(1024, myPow(2.0, 10), 0.1);
        /**
         * Input: 2.10000, 3
         * Output: 9.26100
         */
        assertEquals(9.261, myPow(2.1, 3), 0.1);
        /**
         * Input: 2.00000, -2
         * Output: 0.25000
         * Explanation: 2-2 = 1/22 = 1/4 = 0.25
         */
        assertEquals(0.25, myPow(2, -2), 0.1);

    }

    /**
     * Approach 2: Fast Power Iteration
     * If we denote n to binary representation bnbn-1...b2b1, it would be more clear. If bi equals to 1, we need to multiply the
     * result by x^(2^i). We can also compute x^(2^i) by multiply x^(2^(i - 1)) * x^(2^(i - 1)). In other words, no matter bi equals to
     * 0 or 1, we keep multiplying the previous product to obtain desired product value for index i. Only if bi = 1 should we multiply
     * the product to our final result.
     * Since when obtaining binary representation, we keep dividing n by 2 until it hits 0. Hence, the binary number will be of length
     * at most logN
     *
     * Time: O(logN)
     * Space: O(1), only assign to values to record final result and current product we have for index i
     */
    public double myPowIterative(double x, int n) {
        long N = n;
        if(N < 0) {
            x = 1 / x;
            N = -N;
        }

        double res = 1;
        //initialize with x^1 = x
        double currentProduct = x;
        for(long i = N; i > 0; i /= 2) {
            if((i % 2) == 1) {
                res = res * currentProduct;
            }
            currentProduct = currentProduct * currentProduct;
        }
        return res;
    }

    @Test
    public void myPowIterativeTest() {
        /**
         * Example 1:
         * Input: 2.00000, 10
         * Output: 1024.00000
         */
        assertEquals(1024, myPowIterative(2.0, 10), 0.1);
        /**
         * Input: 2.10000, 3
         * Output: 9.26100
         */
        assertEquals(9.261, myPowIterative(2.1, 3), 0.1);
        /**
         * Input: 2.00000, -2
         * Output: 0.25000
         * Explanation: 2-2 = 1/22 = 1/4 = 0.25
         */
        assertEquals(0.25, myPowIterative(2, -2), 0.1);

    }
}
