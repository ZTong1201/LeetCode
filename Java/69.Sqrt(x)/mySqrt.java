import org.junit.Test;
import static org.junit.Assert.*;

public class mySqrt {

    /**
     * Implement int sqrt(int x).
     *
     * Compute and return the square root of x, where x is guaranteed to be a non-negative integer.
     *
     * Since the return type is an integer, the decimal digits are truncated and only the integer part of the result is returned.
     *
     * Approach 1: Binary Search:
     * We can use binary search to solve this problem in roughly O(logn) time. Since we only care about the integer part of the result,
     * when mid * mid <= x and (mid + 1) * (mid + 1) > x, we found the result. The only thing we need to care about is integer overflow.
     * If the given x is larger, the square of middle value can be larger than the max value of integer. We can use a long to store
     * these values and cast them by back to integer when returning.
     *
     * Time: O(logn) since we are using a divide and conquer approach
     * Space: O(1), we require constant space to store values
     */
    public int mySqrtBinarySearch(int x) {
        long low = 0;
        long high = x;
        while(low <= high) {
            long mid = (high + low) / 2;
            long val1 = mid * mid;
            long val2 = (mid + 1) * (mid + 1);
            if(val1 <= x && val2 > x) return (int) mid;
            else if(val1 < x) low = mid + 1;
            else high = mid - 1;
        }
        return -1;
    }

    /**
     * Approach 2: Newton's Method
     *
     * For a given function, we can use Newton's method to compute the root of that function. In our case we have f(x) = x^2 - a,
     *
     * x(n + 1) = x(n) - f(xn)/f'(xn)
     *
     * f(xn) = x^2 - a, f'(xn) = 2x
     *
     * x(n + 1) = x(n) - (x(n)^2 - a) / (2x(n)) = x(n)/2 + a/2x(n) = (x(n) + a/x(n)) / 2
     *
     * we can keep iterating until we get the desired decimal. Or in our case, we can start from x = a, and at any time x(n + 1)^2 <= a,
     * the desired result is x(n + 1)
     *
     * Time: Since Newton's Method has a quadratic convergence rate. For large x, the time complexity will no worse than O(logn)
     * Space: O(1) requires constant space
     */
    public int mySqrtNewton(int x) {
        long res = x;
        while(res * res > x) {
            res = (res + x / res) / 2;
        }
        return (int) res;
    }


    @Test
    public void mySqrtTest() {
        /**
         * Example 1:
         * Input: 4
         * Output: 2
         */
        assertEquals(2, mySqrtBinarySearch(4));
        assertEquals(2, mySqrtNewton(4));

        /**
         * Example 2:
         * Input: 8
         * Output: 2
         * Explanation: The square root of 8 is 2.82842..., and since
         *              the decimal part is truncated, 2 is returned.
         */
        assertEquals(2, mySqrtBinarySearch(8));
        assertEquals(2, mySqrtNewton(8));
        /**
         * Example 3:
         * Input: 2147395599
         * Output: 46339
         */
        assertEquals(46339, mySqrtBinarySearch(2147395599));
        assertEquals(46339, mySqrtNewton(2147395599));
    }
}
