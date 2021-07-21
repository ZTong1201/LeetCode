import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LongPressedName {

    /**
     * Your friend is typing his name into a keyboard. Sometimes, when typing a character c, the key might get long
     * pressed, and the character will be typed 1 or more times.
     * <p>
     * You examine the typed characters of the keyboard. Return True if it is possible that it was your friends name,
     * with some characters (possibly none) being long pressed.
     * <p>
     * Constraints:
     * <p>
     * 1 <= name.length <= 1000
     * 1 <= typed.length <= 1000
     * name and typed contain only lowercase English letters.
     * <p>
     * Approach: Two pointer
     * Traverse both strings, if at any point, the two characters are different -> return false
     * For duplicate characters, when the two characters are the same, compute the counts of same
     * consecutive characters in two strings. Since the letters can be long pressed, then the count in the typed string
     * should be greater than or equal to the count in the original name string.
     * <p>
     * Time: O(max(m, n))
     * Space: O(1)
     */
    public boolean isLongPressedName(String name, String typed) {
        int m = name.length(), n = typed.length();
        int i = 0, j = 0;
        while (i < m && j < n) {
            char c1 = name.charAt(i), c2 = typed.charAt(j);
            if (c1 != c2) return false;

            int count1 = 0, count2 = 0;
            // count consecutive characters in the name string
            while (i < m && name.charAt(i) == c1) {
                count1++;
                i++;
            }

            // count consecutive characters in the typed string
            while (j < n && typed.charAt(j) == c2) {
                count2++;
                j++;
            }

            // count2 cannot be strictly less than count1
            if (count2 < count1) return false;
        }

        // make sure there is no outstanding characters in both strings
        return i == m && j == n;
    }

    @Test
    public void isLongPressedNameTest() {
        /**
         * Example 1:
         * Input: name = "alex", typed = "aaleex"
         * Output: true
         * Explanation: 'a' and 'e' in 'alex' were long pressed.
         */
        assertTrue(isLongPressedName("alex", "aaleex"));
        /**
         * Example 2:
         * Input: name = "saeed", typed = "ssaaedd"
         * Output: false
         * Explanation: 'e' must have been pressed twice, but it wasn't in the typed output.
         */
        assertFalse(isLongPressedName("saeed", "ssaaedd"));
        /**
         * Example 3:
         * Input: name = "leelee", typed = "lleeelee"
         * Output: true
         */
        assertTrue(isLongPressedName("leelee", "lleeelee"));
        /**
         * Example 4:
         * Input: name = "laiden", typed = "laiden"
         * Output: true
         * Explanation: It's not necessary to long press any character.
         */
        assertTrue(isLongPressedName("laiden", "laiden"));
    }
}
