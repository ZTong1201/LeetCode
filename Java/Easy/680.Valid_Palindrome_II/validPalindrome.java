import org.junit.Test;
import static org.junit.Assert.*;

public class validPalindrome {

    /**
     * Given a non-empty string s, you may delete at most one character. Judge whether you can make it a palindrome.
     *
     * Note:
     * The string will only contain lowercase characters a-z. The maximum length of the string is 50000.
     *
     * Approach: Two pointers
     * The brute force algorithm will be for each character, we remove one of the character after it and check whether the entire string
     * is a palindrome or not. It will cost O(n^2) runtime. To better handle this problem, we can assign two pointers at the front and the
     * end. If two characters are the same, we increment the left and decrement the right at the same time. If at any time two characters
     * are different, we remove one of them and check whether the string left is a palindrome or not.
     *
     * Time: O(n) iterate to find mismatched characters and checking palindrome will both be in O(n) time, since we loop the entire string
     *      by one pass
     * Space: O(n), to better manipulate characters, convert the entire string to an array
     */
    public boolean validPalindrome(String s) {
        char[] chars = s.toCharArray();
        //assign two pointers
        int left = 0, right = chars.length - 1;

        //we can first skip some matched characters
        while(left < right && chars[left] == chars[right]) {
            left++;
            right--;
        }

        //remove one of these two mismatched characters to check the left string is a palindrome or not
        //simply move the pointer to remove character
        return isPalindrome(chars, left + 1, right) || isPalindrome(chars, left, right - 1);
    }

    private boolean isPalindrome(char[] arr, int i, int j) {
        while(i < j) {
            if(arr[i] != arr[j]) {
                return false;
            }
            i++;
            j--;
        }
        return true;
    }


    @Test
    public void validPalindromeTest() {
        /**
         * Example 1:
         * Input: "aba"
         * Output: True
         */
        assertTrue(validPalindrome("aba"));
        /**
         * Example 2:
         * Input: "abca"
         * Output: True
         * Explanation: You could delete the character 'c'.
         */
        assertTrue(validPalindrome("abca"));
        /**
         * Example 3:
         * Input: "eccer"
         * Output: True
         * Explanation: You could delete the character 'r'.
         */
        assertTrue(validPalindrome("eccer"));
        /**
         * Example 4:
         * Input: "abc"
         * Output: False
         */
        assertFalse(validPalindrome("abc"));
    }
}
