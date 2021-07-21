import org.junit.Test;

import java.util.ArrayDeque;
import java.util.Deque;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Given a string, determine if it is a palindrome, considering only alphanumeric characters and ignoring cases.
 * Note: For the purpose of this problem, we define empty string as valid palindrome.
 * Remember Character.isLetterOrDigit() (Character.isLetter()), Character.toLowerCase() API for this problem
 * Use deque to verify valid palindrome
 */
public class ValidPalindrome {

    /**
     * Approach 1: Deque
     * <p>
     * Time: O(n) need to visit the entire string
     * Space: O(n) an actual deque to store all alphanumeric values
     */
    public boolean isPalindromeDeque(String s) {
        Deque<Character> letterDeque = new ArrayDeque<>();
        for (int i = 0; i < s.length(); i++) {
            char letter = s.charAt(i);
            if (Character.isLetterOrDigit(letter)) {
                letterDeque.add(Character.toLowerCase(letter));
            }
        }
        while (letterDeque.size() > 1) {
            if (letterDeque.removeLast() != letterDeque.removeFirst()) return false;
        }
        return true;
    }

    /**
     * Approach 2: Two pointers
     * <p>
     * Time: O(n)
     * Space: O(1)
     */
    public boolean isPalindromeTwoPointers(String s) {
        int left = 0, right = s.length() - 1;
        s = s.toLowerCase();
        while (left < right) {
            // skip all non-alphanumeric characters
            while (left < right && !Character.isLetterOrDigit(s.charAt(left))) {
                left++;
            }
            while (left < right && !Character.isLetterOrDigit(s.charAt(right))) {
                right--;
            }
            if (s.charAt(left) != s.charAt(right)) return false;
            left++;
            right--;
        }
        return true;
    }

    @Test
    public void isPalindromeTest() {
        /**
         * Example 1:
         * Input: s = "A man, a plan, a canal: Panama"
         * Output: true
         * Explanation: "amanaplanacanalpanama" is a palindrome.
         */
        assertTrue(isPalindromeDeque("A man, a plan, a canal: Panama"));
        assertTrue(isPalindromeTwoPointers("A man, a plan, a canal: Panama"));
        /**
         * Example 2:
         * Input: s = "race a car"
         * Output: false
         * Explanation: "raceacar" is not a palindrome.
         */
        assertFalse(isPalindromeDeque("race a car"));
        assertFalse(isPalindromeTwoPointers("race a car"));
        /**
         * Example 3:
         * Input: s = "race car"
         * Output: true
         * Explanation: "racecar" is a palindrome.
         */
        assertTrue(isPalindromeDeque("race car"));
        assertTrue(isPalindromeTwoPointers("race car"));
    }
}
