import org.junit.Test;
import static org.junit.Assert.*;

public class addStrings {

    /**
     * Given two non-negative integers num1 and num2 represented as string, return the sum of num1 and num2.
     *
     * Note:
     *
     * The length of both num1 and num2 is < 5100.
     * Both num1 and num2 contains only digits 0-9.
     * Both num1 and num2 does not contain any leading zero.
     * You must not use any built-in BigInteger library or convert the inputs to integer directly.
     *
     * Approach: Direct Summation
     * 只要倒序地对两个字符串进行遍历，计算当前位置的值即可，记得两数字相加可能需要进位，因此在计算当前位置时，需要考虑上一位的进位，同时计算完当前位置后，也需要
     * 更新进位。若遍历超出了某一字符串的边界，从那之后该字符串中得到的新值一直是0。
     * 循环终止条件为，两字符串都被遍历完，而且最后的进位不是1。
     *
     * Time: O(m + n) 需要遍历两字符串
     * Space: O(max(m, n))，最后的字符串长度为max(m, n) + 1
     */
    public String addStrings(String num1, String num2) {
        StringBuilder res = new StringBuilder();
        int len1 = num1.length();
        int len2 = num2.length();
        int i = len1 - 1, j = len2 - 1;
        int sum = 0;
        while(i >= 0 || j >= 0 || sum == 1) {
            int val1 = i >= 0 ? num1.charAt(i) - '0' : 0;
            int val2 = j >= 0 ? num2.charAt(j) - '0' : 0;
            sum = val1 + val2 + sum;
            res.append(sum % 10);
            sum /= 10;
            i--;
            j--;
        }
        return res.reverse().toString();
    }

    @Test
    public void addStringsTest() {
        /**
         * Example 1:
         * Input:
         * num1 = "99", num2 = "1"
         * Output: "100"
         */
        assertEquals("100", addStrings("99", "1"));
        /**
         * Example 2:
         * Input:
         * num1 = "1234", num2 = "4321"
         * Output: "5555"
         */
        assertEquals("5555", addStrings("1234", "4321"));
        /**
         * Example 3:
         * Input:
         * num1 = "1000002", num2 = "999"
         */
        assertEquals("1001001", addStrings("1000002", "999"));
    }
}
