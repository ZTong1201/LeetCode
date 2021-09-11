import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StrobogrammaticNumber {

    /**
     * Given a string num which represents an integer, return true if num is a strobogrammatic number.
     * <p>
     * A strobogrammatic number is a number that looks the same when rotated 180 degrees (looked at upside down).
     * <p>
     * Constraints:
     * <p>
     * 1 <= num.length <= 50
     * num consists of only digits.
     * num does not contain any leading zeros except for zero itself.
     * <p>
     * Approach 1: Rotated Copy
     * We can put all strobogrammatic numbers into a map, e.g. "0" -> "0", "1" -> "1". However, we need clarify that
     * whether "2" and "5" are valid strobogrammatic numbers since it might differ under different circumstance. If assuming
     * "2" and "5" are not valid, we have
     * "0" -> "0"
     * "1" -> "1"
     * "6" -> "9"
     * "8" -> "8"
     * "9" -> "6"
     * after rotating. Then we construct the rotated string according to the requirement and see whether it still equals to the
     * original string. If we encounter a digit which is not in the above list, we directly return false.
     * <p>
     * Time: O(n) traverse the entire string in the reverse way
     * Space: O(n) need to construct output string
     */
    public boolean isStrobogrammaticRotatedCopy(String num) {
        Map<Character, Character> strobogrammaticNumbers = Map.of('0', '0', '1', '1', '6', '9',
                '8', '8', '9', '6');

        StringBuilder rotatedNumber = new StringBuilder();
        for (int i = num.length() - 1; i >= 0; i--) {
            char digit = num.charAt(i);
            if (!strobogrammaticNumbers.containsKey(digit)) return false;
            rotatedNumber.append(strobogrammaticNumbers.get(digit));
        }
        return num.equals(rotatedNumber.toString());
    }

    /**
     * Approach 2: Two pointers
     * Notice that not the digit itself is rotated 180 degrees, the string is also constructed in a reverse order, i.e.
     * the first character becomes the last character, the second becomes the second last, etc. Therefore, we can use
     * two points to visit the string from the front and the rear at the same time. If the input string is actually a
     * strobogrammatic number, then the digit on the left will equal to the digit on the right after rotating 180 degrees.
     * By doing so, we only need to keep two pointers which gives O(1) extra space.
     * <p>
     * Time: O(n)
     * Space: O(1)
     */
    public boolean isStrobogrammaticTwoPointers(String num) {
        Map<Character, Character> strobogrammaticNumbers = Map.of('0', '0', '1', '1', '6', '9',
                '8', '8', '9', '6');

        int left = 0, right = num.length() - 1;

        // still need to check even if the length of string is 1
        while (left <= right) {
            char leftChar = num.charAt(left), rightChar = num.charAt(right);
            if (!strobogrammaticNumbers.containsKey(leftChar) || strobogrammaticNumbers.get(leftChar) != rightChar)
                return false;
            left++;
            right--;
        }
        return true;
    }

    @Test
    public void isStrobogrammaticTest() {
        /**
         * Example 1:
         * Input: num = "69"
         * Output: true
         */
        assertTrue(isStrobogrammaticRotatedCopy("69"));
        assertTrue(isStrobogrammaticTwoPointers("69"));
        /**
         * Example 2:
         * Input: num = "88"
         * Output: true
         */
        assertTrue(isStrobogrammaticRotatedCopy("88"));
        assertTrue(isStrobogrammaticTwoPointers("88"));
        /**
         * Example 3:
         * Input: num = "962"
         * Output: false
         */
        assertFalse(isStrobogrammaticRotatedCopy("962"));
        assertFalse(isStrobogrammaticTwoPointers("962"));
        /**
         * Example 4:
         * Input: num = "1"
         * Output: true
         */
        assertTrue(isStrobogrammaticRotatedCopy("1"));
        assertTrue(isStrobogrammaticTwoPointers("1"));
    }
}
