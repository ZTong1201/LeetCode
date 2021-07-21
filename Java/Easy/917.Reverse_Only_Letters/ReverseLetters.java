import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ReverseLetters {

    /**
     * Given a string s, return the "reversed" string where all characters that are not a letter stay in the same place,
     * and all letters reverse their positions.
     * <p>
     * Note:
     * <p>
     * s.length <= 100
     * 33 <= s[i].ASCIIcode <= 122
     * s doesn't contain \ or "
     * <p>
     * Approach: Two pointers
     * <p>
     * Time: O(n)
     * Space: O(n) since string is immutable
     */
    public String reverseOnlyLetters(String s) {
        char[] chars = s.toCharArray();
        int left = 0, right = chars.length - 1;
        while (left < right) {
            while (left < right && !Character.isLetter(chars[left])) left++;
            while (left < right && !Character.isLetter(chars[right])) right--;
            swap(chars, left, right);
            left++;
            right--;
        }
        return new String(chars);
    }

    private void swap(char[] chars, int i, int j) {
        char temp = chars[i];
        chars[i] = chars[j];
        chars[j] = temp;
    }

    @Test
    public void reverseOnlyLettersTest() {
        /**
         * Example 1:
         * Input: s = "ab-cd"
         * Output: "dc-ba"
         */
        assertEquals("dc-ba", reverseOnlyLetters("ab-cd"));
        /**
         * Example 2:
         * Input: s = "a-bC-dEf-ghIj"
         * Output: "j-Ih-gfE-dCba"
         */
        assertEquals("j-Ih-gfE-dCba", reverseOnlyLetters("a-bC-dEf-ghIj"));
        /**
         * Example 3:
         * Input: s = "Test1ng-Leet=code-Q!"
         * Output: "Qedo1ct-eeLg=ntse-T!"
         */
        assertEquals("Qedo1ct-eeLg=ntse-T!", reverseOnlyLetters("Test1ng-Leet=code-Q!"));
    }
}
