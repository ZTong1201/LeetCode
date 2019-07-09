import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class happyNumber {

    /**
     * Write an algorithm to determine if a number is "happy".
     *
     * A happy number is a number defined by the following process: Starting with any positive integer,
     * replace the number by the sum of the squares of its digits, and repeat the process until the number equals 1 (where it will stay),
     * or it loops endlessly in a cycle which does not include 1. Those numbers for which this process ends in 1 are happy numbers.
     *
     * Approach 1: Hash set
     * We simply do the process iteratively. When we find a 1, then we are done, simply return true. If not, it will loop endlessly
     * as stated in the problem. Hence, we can use a hash set to store the number seen so far. If at any time, we get a number that has
     * already been recorded, we know we are in an endlessly loop, and it is not a happy number.
     *
     * Time: O(1), the steps of iterations will be a constant no matter what input is
     * Space: O(1)
     */
    public boolean isHappy(int n) {
        Set<Integer> seen = new HashSet<>();
        while(n != 1) {
            seen.add(n);
            int sum = 0;
            while(n != 0) {
                sum += Math.pow(n % 10, 2);
                n /= 10;
            }
            n = sum;
            if(seen.contains(n)) return false;
        }
        return true;
    }

    /**
     * Approach 2: tricks
     * Surprisingly, when we meet the value 4, it will start an endless loop so that is is not a happy number
     */
    public boolean isHappy2(int n) {
        while(n != 1) {
            if(n == 4) return false;
            int sum = 0;
            while(n != 0) {
                sum += Math.pow(n % 10, 2);
                n /= 10;
            }
            n = sum;
        }
        return true;
    }

    @Test
    public void isHappyTest() {
        /**
         * Example 1:
         * Input: 19
         * Output: true
         * Explanation:
         * 12 + 92 = 82
         * 82 + 22 = 68
         * 62 + 82 = 100
         * 12 + 02 + 02 = 1
         */
        assertTrue(isHappy(19));
        assertTrue(isHappy2(19));
        /**
         * Example 2:
         * Input: 6
         * Output: false
         */
        assertFalse(isHappy(6));
        assertFalse(isHappy2(6));
        /**
         * Example 3:
         * Input: 4
         * Output: false
         */
        assertFalse(isHappy(4));
        assertFalse(isHappy2(4));
    }
}
