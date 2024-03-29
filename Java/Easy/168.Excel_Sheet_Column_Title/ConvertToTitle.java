import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConvertToTitle {

    /**
     * Given a positive integer, return its corresponding column title as appear in an Excel sheet.
     * <p>
     * For example:
     * <p>
     * 1 -> A
     * 2 -> B
     * 3 -> C
     * ...
     * 26 -> Z
     * 27 -> AA
     * 28 -> AB
     * ...
     * <p>
     * It is still a 26-based conversion problem. However, when n = 26, 26 / 26 = 1, and 26 % 26 = 0, which is not desired, since 26
     * corresponds to 'Z'. We can subtract the input value by 1 to guarantee that we have 0-based indices. For example, when input is 1,
     * we subtract by 1 then obtain 0, 0 % 26 = 0, which corresponds to 0 + 'A' = 'A'. However, the problem will reappear when moving to
     * next decimals. For instance, n = 27, we first subtract 1 and get 26, 26 % 26  = 0 we get 'A'. 26 / 26 = 1, then 1 % 26 = 1, which is
     * 'B', but we require "AA". Therefore, we must subtract 1 initially (to ensure 0-based indices) when we move to a new decimal
     * and insert it at the front of the string.
     * <p>
     * Constraints:
     * <p>
     * 1 <= columnNumber <= 2^31 - 1
     * <p>
     * Time: O(log26n) we need to divide the input value by 26 for log26n times to reach 0
     * Space: O(log26n) we need a string builder to store final result
     */
    public String convertToTitle(int columnNumber) {
        StringBuilder res = new StringBuilder();
        while (columnNumber != 0) {
            columnNumber -= 1;                            //subtract by 1
            int remainder = columnNumber % 26;            //take the remainder
            char c = (char) (remainder + 'A'); // get the desired letter
            // or use res.append(c), then reverse the res in final return line.
            res.insert(0, c);
            columnNumber /= 26;
        }
        return res.toString();  //res.reverse().toString() if use append
    }


    @Test
    public void convertToTitleTest() {
        /**
         * Example 1:
         * Input: 1
         * Output: "A"
         */
        assertEquals("A", convertToTitle(1));
        /**
         * Example 2:
         * Input: 28
         * Output: "AB"
         */
        assertEquals("AB", convertToTitle(28));
        /**
         * Example 3:
         * Input: 701
         * Output: "ZY"
         */
        assertEquals("ZY", convertToTitle(701));
        /**
         * Example 4:
         * Input: columnNumber = 2147483647
         * Output: "FXSHRXW"
         */
        assertEquals("FXSHRXW", convertToTitle(2147483647));
    }
}
