import org.junit.Test;
import static org.junit.Assert.*;

public class integerToRoman {

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
     *
     * For example, two is written as II in Roman numeral, just two one's added together. Twelve is written as, XII, which is simply X + II.
     * The number twenty seven is written as XXVII, which is XX + V + II.
     *
     * Roman numerals are usually written largest to smallest from left to right. However, the numeral for four is not IIII. Instead, the
     * number four is written as IV. Because the one is before the five we subtract it making four. The same principle applies to the
     * number nine, which is written as IX. There are six instances where subtraction is used
     *
     * I can be placed before V (5) and X (10) to make 4 and 9.
     * X can be placed before L (50) and C (100) to make 40 and 90.
     * C can be placed before D (500) and M (1000) to make 400 and 900.
     * Given an integer, convert it to a roman numeral. Input is guaranteed to be within the range from 1 to 3999.
     *
     * Approach: From biggest to smallest
     * 只需要所有可能的字符组合，即1000，900，500，400，100，90，50，40，10，9，5，4，1分别对应起来，然后对于输入数字，从大往小依次检查，若当前数字大于等于
     * 某个数，就添加该数字所对应字符，同时将该数一直从输入数字中减去，直到输入数字小于该数字，然后继续搜索更小的数字。直到最后输入数字减为0。
     *
     * Time: O(1)，因此输入数字保证了在1到3999之间，对于千位，最多需要循环3次，百位十位个位都是在为8的是否需要最多的循环，即将8拆成5 + 1 + 1 + 1，总共循环
     *       四次。因此需要循环次数最多的数字是3888，总共需要4 * 3 + 3 = 15次
     * Space: O(1)，如上述讨论，只需要将13种不同的字符组合记录下来即可
     */
    public String intToRoman(int num) {
        StringBuilder res = new StringBuilder();
        String[] roman = new String[]{"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        int[] values = new int[]{1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        //从大数往小数循环，不断减小输入数字直到为0
        int i = 0;
        while(num > 0) {
            while(num >= values[i]) {
                res.append(roman[i]);
                num -= values[i];
            }
            //当前数字已经全部从输入数字上减去，考虑更小的数字
            i++;
        }
        return res.toString();
    }

    @Test
    public void intToRomanTest() {
        /**
         * Example 1:
         * Input: 3
         * Output: "III"
         */
        assertEquals("III", intToRoman(3));
        /**
         * Example 2:
         * Input: 9
         * Output: "IX"
         */
        assertEquals("IX", intToRoman(9));
        /**
         * Example 3:
         * Input: 58
         * Output: "LVIII"
         * Explanation: L = 50, V = 5, III = 3.
         */
        assertEquals("LVIII", intToRoman(58));
        /**
         * Example 4:
         * Input: 1994
         * Output: "MCMXCIV"
         * Explanation: M = 1000, CM = 900, XC = 90 and IV = 4.
         */
        assertEquals("MCMXCIV", intToRoman(1994));
    }
}
