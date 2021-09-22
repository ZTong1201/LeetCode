import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class longestPalindromeSubstring {

    /**
     * Given a string s, find the longest palindromic substring in s. You may assume that the maximum length of s is 1000.
     * <p>
     * The brute force approach would be for every substring (n(n - 1)/2 substrings), we check for whether it is a palindrome
     * Checking for palindrome is O(n), hence the overall runtime is O(n^3)
     * <p>
     * Approach 1: Dynamic Programming
     * We can actually solve this problem using dynamic programming, i.e. for O(n^2) substrings, we can check whether a substring
     * is a palindrome in O(1) time using previously checked information.
     * We can define P(i, j) by
     * <p>
     * P(i, j) = true if substring Si....Sj is palindrome
     * = false otherwise
     * <p>
     * Hence, P(i, j) = true if and only if s[i] == s[j] and P(i + 1, j - 1) is palindrome
     * <p>
     * <p>
     * Our initialization is simply:
     * P(i, i) = true, since each single element would be a palindrome
     * P(i, i + 1) = true if s[i] = s[i + 1] false otherwise.
     * <p>
     * Time: O(n^2) we still have O(n^2) substrings to check
     * Space: O(n^2) we need a 2-D boolean array to track whether P(i, j) is true or not.
     * For the sake of time reduction, we convert our string into a char array, since get element from an array is much faster (O(n))
     */
    public String longestPalindromeDP1(String s) {
        int start = 0; //record where the longest substring starts
        int length = 0; //record the length of the longest substring
        char[] characters = s.toCharArray();
        boolean[][] isPalindrome = new boolean[s.length()][s.length()];

        //Initialization
        for (int i = 0; i < s.length(); i++) {
            isPalindrome[i][i] = true;  //every single element is a palindrome
            start = i;
            length = 1;
        }
        for (int i = 0; i < s.length() - 1; i++) {
            //for all the substring with length of 2, if s[i] == s[i + 1], it is a palindrome
            if (characters[i] == characters[i + 1]) {
                isPalindrome[i][i + 1] = true;
                start = i;
                length = 2;
            }
        }

        //Next, we check substrings with length from 3 to s.length;
        for (int i = 3; i <= s.length(); i++) { // current length of substring
            for (int j = 0; j + i - 1 < s.length(); j++) {
                //P(i, j) = true if s[i] == s[j] and P(i + 1, j - 1) is true
                if (characters[j] == characters[j + i - 1] && isPalindrome[j + 1][j + i - 2]) {
                    isPalindrome[j][j + i - 1] = true;
                    start = j;
                    length = i;
                }
            }
        }
        return s.substring(start, start + length);
    }

    /**
     * Approach 2: DP without initialization
     * Denote dp[i][j] = true if s[i...j] is a palindrome
     * A cleaner way to implement DP, no need for explicit initialization, just check whether i - j <= 2 && s[j + 1][i - 1]
     * By doing so, if the current substring contains a single element, it automatically returns true.
     * <p>
     * Time: O(n^2)
     * Space: O(n^2)
     */
    public String longestPalindromeDP2(String s) {
        int length = s.length();
        String res = "";
        int maxLength = 0;
        boolean[][] isPalindrome = new boolean[length][length];

        for (int right = 0; right < length; right++) {
            for (int left = 0; left <= right; left++) {
                // MOST IMPORTANT STEP! We ONLY check substring from left to right
                // when s[left] == s[right], then s[left...right] is a palindrome if
                // the substring length is less than 3, e.g. "aba", "bb", "c" will always be a valid palindrome
                // or s[left + 1 ... right - 1] is already a palindrome
                isPalindrome[left][right] = s.charAt(left) == s.charAt(right) &&
                        (right - left <= 2 || isPalindrome[left + 1][right - 1]);
                if (isPalindrome[left][right] && (right - left + 1 > maxLength)) {
                    maxLength = right - left + 1;
                    res = s.substring(left, right + 1);
                }
            }
        }
        return res;
    }

    /**
     * Approach 3: Expand Around Center
     * We can actually reduce the required extra space. For a given index, we can start from the center and expand it towards
     * left and right sides. If anytime s[left] != s[right], the corresponding substring is not a palindrome. The tricky part
     * is that if we have an even length of substring (say "abbc"), there are two letters in the center. We need to first check
     * whether s[i] == s[i + 1] and treat s[i]s[i + 1] as the center and expand towards both sides. By doing so, we omit the 2-D
     * array in approach 1.
     * <p>
     * Time: O(n^2) still O(n^2) substrings to check
     * Space: O(1) omit the 2-D array, no extra space required
     */
    private String res;

    public String longestPalindromeExpandAroundCenter(String s) {
        res = "";
        for (int i = 0; i < s.length(); i++) {
            helper(s, i, i);
            helper(s, i, i + 1);
        }
        return res;
    }

    private void helper(String s, int left, int right) {
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            left -= 1;
            right += 1;
        }
        // after iteration, now left and right are not the same character
        // hence s[left + 1, right) will be a palindrome, check whether it has a larger length
        if (right - left > res.length()) res = s.substring(left + 1, right);
    }

    /**
     * Approach 4: Manacher's Algorithm
     * We can solve this problem in O(n) runtime. Inspired by expanding around center, we can actually update length at a given index
     * in this rule:
     * 1. If i > rightmost boundary (R), then lengths[i] = min(R - i, its mirror element (lengths[2*C - i]))
     * 2. We try to expand the length to see whether we can obtain longer palindromes. Node that we don't need to check little by little,
     * Based on potential longest length, we can start from the current longest palindrome
     * 3. If i + lengths[i] > rightmost boundary (R), we outreach the boundary, so we need to update the center C to i and R to i + lengths[i]
     * <p>
     * Meanwhile, we can record the max palindrome length and the center of longest substring during the update.
     * However, in order to correctly implement this algorithm, we need to add a hash sign '#' between two characters and add two other
     * different characters at the front and the end (e.g. '$', '@')
     * <p>
     * The longest palindrome in our modified string occurs between maxCenter - maxLength and maxCenter + maxLength.
     * We can divide the indexes by two to retrieve indexes in the original string.
     * <p>
     * Time: O(2n) = O(n), we actually ONLY update the center and the right boundary during iteration, we never recalculate any information
     * we obtained before.
     * Space: O(2*2n) = O(n), we need a new character array to store '#' and original characters in the previous string, and another
     * integer array to store longest length at that index.
     */
    public String longestPalindromeManacher(String s) {
        char[] chars = new char[2 * s.length() + 3];
        chars[0] = '$';
        chars[1] = '#';
        int index = 2;
        for (char c : s.toCharArray()) {
            chars[index++] = c;
            chars[index++] = '#';
        }
        chars[index++] = '@'; // now, index = chars.length
        int[] lengths = new int[index];
        int maxLength = 0, maxIndex = 0, center = 0, rightBoundary = 0;
        for (int i = 1; i < index - 1; i++) {
            // check whether current index < rightmost boundary
            // If so, lengths[i] = min(R - i, its mirror element)
            if (i < rightBoundary) lengths[i] = Math.min(rightBoundary - i, lengths[2 * center - i]);
            // try to expand current substring
            while (chars[i + (1 + lengths[i])] == chars[i - (1 + lengths[i])]) lengths[i] += 1;
            // If after expanding, i + lengths[i] > right boundary
            // update the center and the right boundary
            if (i + lengths[i] > rightBoundary) {
                center = i;
                rightBoundary = i + lengths[i];
            }
            // record maxLength and the center of longest palindrome substring
            if (lengths[i] > maxLength) {
                maxIndex = i;
                maxLength = lengths[i];
            }
        }
        // convert maxIndex back to the original string
        return s.substring((maxIndex - maxLength) / 2, (maxIndex + maxLength) / 2);
    }

    @Test
    public void longestPalindromeTest() {
        /**
         * Example 1:
         * Input: "babad"
         * Output: "bab"
         * Note: "aba" is also a valid answer.
         */
        Set<String> expectedSet1 = new HashSet<>(Arrays.asList("bab", "aba"));
        assertTrue(expectedSet1.contains(longestPalindromeDP1("babad")));
        assertTrue(expectedSet1.contains(longestPalindromeDP2("babad")));
        assertTrue(expectedSet1.contains(longestPalindromeExpandAroundCenter("babad")));
        assertTrue(expectedSet1.contains(longestPalindromeManacher("babad")));
        /**
         * Example 2:
         * Input: "cbbd"
         * Output: "bb"
         */
        assertEquals("bb", longestPalindromeDP1("cbbd"));
        assertEquals("bb", longestPalindromeDP2("cbbd"));
        assertEquals("bb", longestPalindromeExpandAroundCenter("cbbd"));
        assertEquals("bb", longestPalindromeManacher("cbbd"));
        /**
         * Example 3:
         * Input: "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
         * Output: "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
         */
        assertEquals("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", longestPalindromeDP1("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));
        assertEquals("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", longestPalindromeDP2("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));
        assertEquals("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", longestPalindromeExpandAroundCenter("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));
        assertEquals("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", longestPalindromeManacher("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));
    }

}
