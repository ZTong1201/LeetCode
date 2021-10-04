import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConcatenationOfBinaryNumbers {

    /**
     * Given an integer n, return the decimal value of the binary string formed by concatenating the binary representations
     * of 1 to n in order, modulo 10^9 + 7.
     * <p>
     * Constraints:
     * <p>
     * 1 <= n <= 10^5
     * <p>
     * Approach 1: Change to binary string
     * We can change the input number into a binary string using a stack or some built-in methods. Then for each binary
     * value the result will be updated to result = result * 2 + (0 or 1). We need to take mod after each calculation in case
     * it overflows.
     * <p>
     * Time: O(nlogn) for each number, we need take O(logn) operations to get the binary string.
     * Space: O(logn) for binary string
     */
    public int concatenatedBinaryString(int n) {
        final int MOD = 1_000_000_007;
        int result = 0;
        for (int i = 1; i <= n; i++) {
            // convert the number into a binary string
            String binary = Integer.toBinaryString(i);
            // update the result - take the mod on the fly
            for (int j = 0; j < binary.length(); j++) {
                result = (result * 2 + binary.charAt(j) - '0') % MOD;
            }
        }
        return result;
    }

    /**
     * Approach 2: Math
     * Actually, we don't need to convert the number into a binary string. Note that we basically move the previous result
     * to the left by the length of the binary representation of the new number. For example, once we concatenate 1 and 2
     * to get "110" = 6, next time we want to add 3 = "11", we essentially shift "110" to the left by 2 bits and get
     * "11000" = 24 then plus 3 and get 27. How to get the length of the bit representation? for a given number i, it equals
     * to log2(i) + 1. Therefore, instead of getting the binary string, we compute the length of that string using math
     * and shift the previous result to the left and add the new number. Still, we will take the mod on the fly in case
     * it overflows.
     * <p>
     * Time: O(n)
     * Space: O(1)
     */
    public int concatenatedBinaryMath(int n) {
        final int MOD = 1_000_000_007;
        long result = 0;
        for (int i = 1; i <= n; i++) {
            // get the length of binary string
            int length = (int) (Math.log(i) / Math.log(2)) + 1;
            // update the result - take the mod on the fly
            result = ((result << length) + i) % MOD;
        }
        return (int) result;
    }

    /**
     * Approach 3: Bit operation
     * We can further speed up the previous approach by using pure bit operation. Note that when the length of binary string
     * will be incremented? When we encounter a power of 2, e.g. length is 0 for 0, length is 1 for 2, length is 2 for 4,
     * length is 3 for 8, etc. Therefore, we can initiate the length as 0 and whenever a power of 2 is met, increment the
     * length. How to test power of 2? If x & (x - 1) is 0, then x must be a power of 2. For example, x = 4, the binary
     * string is "100" and x - 1 = 3 = "11" "100" & "11" = "000", hence 4 is a power of 2. In addition, for multiplication,
     * we simply shift the number to the left. Afterwards, the right side of the binary representation will become 0 after
     * shifting, hence we can use | operation to add the new number to those positions, i.e. (result << length) | i
     * is equivalent to result * 2^length + i
     * <p>
     * Time: O(n)
     * Space: O(1)
     */
    public int concatenatedBinaryBitOperation(int n) {
        final int MOD = 1_000_000_007;
        long result = 0;
        // starting from length 0
        int length = 0;
        for (int i = 1; i <= n; i++) {
            // increment the length of binary representation if it's a power of 2
            if ((i & (i - 1)) == 0) length++;
            // result = result * 2^length + i
            result = ((result << length) | i) % MOD;
        }
        return (int) result;
    }

    @Test
    public void concatenatedBinaryTest() {
        /**
         * Example 1:
         * Input: n = 1
         * Output: 1
         * Explanation: "1" in binary corresponds to the decimal value 1.
         */
        assertEquals(1, concatenatedBinaryString(1));
        assertEquals(1, concatenatedBinaryMath(1));
        assertEquals(1, concatenatedBinaryBitOperation(1));
        /**
         * Example 2:
         * Input: n = 3
         * Output: 27
         * Explanation: In binary, 1, 2, and 3 corresponds to "1", "10", and "11".
         * After concatenating them, we have "11011", which corresponds to the decimal value 27.
         */
        assertEquals(27, concatenatedBinaryString(3));
        assertEquals(27, concatenatedBinaryMath(3));
        assertEquals(27, concatenatedBinaryBitOperation(3));
        /**
         * Example 3:
         * Input: n = 12
         * Output: 505379714
         * Explanation: The concatenation results in "1101110010111011110001001101010111100".
         * The decimal value of that is 118505380540.
         * After modulo 10^9 + 7, the result is 505379714.
         */
        assertEquals(505379714, concatenatedBinaryString(12));
        assertEquals(505379714, concatenatedBinaryMath(12));
        assertEquals(505379714, concatenatedBinaryBitOperation(12));
    }
}
