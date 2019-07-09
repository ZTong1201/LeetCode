import org.junit.Test;
import static org.junit.Assert.*;

public class titleToNumber {

    /**
     * Given a column title as appear in an Excel sheet, return its corresponding column number.
     *
     * For example:
     *
     *     A -> 1
     *     B -> 2
     *     C -> 3
     *     ...
     *     Z -> 26
     *     AA -> 27
     *     AB -> 28
     *     ...
     *
     * It is actually a 26-based calculation. Just the same as any bases, convert the letter to the corresponding number (A: 1, B: 2....)
     * and sum them up;
     *
     * Time: O(n) we need to traverse the whole string
     * Space: O(1) no extra space required
     */
    public int titleToNumber(String s) {
        int res = 0;
        int length = s.length();
        for(int i = 0; i < length; i++) {
            int curr = s.charAt(i) - 'A' + 1;
            res *= 26;
            res += curr;
        }
        return res;
    }

    @Test
    public void titleToNumberTest() {
        /**
         * Example 1:
         * Input: "A"
         * Output: 1
         */
        assertEquals(1, titleToNumber("A"));
        /**
         * Example 2:
         * Input: "AB"
         * Output: 28
         */
        assertEquals(28, titleToNumber("AB"));
        /**
         * Example 3:
         * Input: "ZY"
         * Output: 701
         */
        assertEquals(701, titleToNumber("ZY"));
    }
}
