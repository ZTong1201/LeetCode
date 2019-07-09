import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;

public class romanToInteger {

    /**
     * Roman numerals are represented by seven different symbols: I, V, X, L, C, D and M.
     *
     * Symbol       Value
     * I             1
     * V             5
     * X             10
     * L             50
     * C             100
     * D             500
     * M             1000
     * For example, two is written as II in Roman numeral, just two one's added together. Twelve is written as, XII, which is simply X + II.
     * The number twenty seven is written as XXVII, which is XX + V + II.
     *
     * Roman numerals are usually written largest to smallest from left to right. However, the numeral for four is not IIII.
     * Instead, the number four is written as IV. Because the one is before the five we subtract it making four.
     * The same principle applies to the number nine, which is written as IX. There are six instances where subtraction is used:
     *
     * I can be placed before V (5) and X (10) to make 4 and 9.
     * X can be placed before L (50) and C (100) to make 40 and 90.
     * C can be placed before D (500) and M (1000) to make 400 and 900.
     * Given a roman numeral, convert it to an integer. Input is guaranteed to be within the range from 1 to 3999.
     *
     * Approach: Single one-pass + hash map
     * The problem has stated clearly. Typically, we go through the string from left to right, and add them all up. However, since roman
     * numerals also has subtraction rules. When we traverse the string, we will record the mapping value of current roman letter and the
     * next
     *
     * 1.if curr >= next, simply add it
     * 2.if curr < next, simply subtract it
     *
     * Time: O(n) we traverse the string by one-pass
     * Space: O(1) only require constant space to record current and next value
     */
    public int romanToInt(String s) {
        Map<Character, Integer> map = new HashMap<>();
        map.put('I', 1);
        map.put('V', 5);
        map.put('X', 10);
        map.put('L', 50);
        map.put('C', 100);
        map.put('D', 500);
        map.put('M', 1000);
        int length = s.length();
        int res = 0;
        for(int i = 0; i < length - 1; i++) {
            int curr = map.get(s.charAt(i));
            int next = map.get(s.charAt(i + 1));
            if(curr >= next) res += curr;
            else res -= curr;
        }
        //remember to add the very last value to the result
        res += map.get(s.charAt(length - 1));
        return res;
    }

    @Test
    public void romanToIntTest() {
        /**
         * Example 1:
         * Input: "III"
         * Output: 3
         */
        assertEquals(3, romanToInt("III"));
        /**
         * Example 2:
         * Input: "IV"
         * Output: 4
         */
        assertEquals(4, romanToInt("IV"));
        /**
         * Example 3:
         * Input: "IX"
         * Output: 9
         */
        assertEquals(9, romanToInt("IX"));
        /**
         * Example 4:
         * Input: "LVIII"
         * Output: 58
         * Explanation: L = 50, V= 5, III = 3.
         */
        assertEquals(58, romanToInt("LVIII"));
        /**
         * Example 5:
         * Input: "MCMXCIV"
         * Output: 1994
         * Explanation: M = 1000, CM = 900, XC = 90 and IV = 4.
         */
        assertEquals(1994, romanToInt("MCMXCIV"));
    }
}
