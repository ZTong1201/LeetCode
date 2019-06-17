import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class longestPalindromeSubstring {

    /**
     * Given a string s, find the longest palindromic substring in s. You may assume that the maximum length of s is 1000.
     *
     * The brute force approach would be for every substring (n(n - 1)/2 substrings), we check for whether it is a palindrome
     * Checking for palindrome is O(n), hence the overall runtime is O(n^3)
     *
     * Approach 1: Dynamic Programming
     * We can actually solve this problem using dynamic programming, i.e. for O(n^2) substrings, we can check whether a substring
     * is a palindrome in O(1) time using previously checked information.
     * We can define P(i, j) by
     *
     * P(i, j) = true if substring Si....Sj is palindrome
     *         = false otherwise
     *
     * Hence, P(i, j) = true if and only if s[i] == s[j] and P(i + 1, j - 1) is palindrome
     *
     *
     * Our initialization is simply:
     * P(i, i) = true, since each single element would be a palindrome
     * P(i, i + 1) = true if s[i] = s[i + 1] false otherwise.
     *
     * Time: O(n^2) we still have O(n^2) substrings to check
     * Space: O(n^2) we need a 2-D boolean array to track whether P(i, j) is true or not.
     * For the sake of time reduction, we convert our string into a char array, since get element from an array is much faster (O(n))
     */
    public String longestPalindromeDP1(String s) {
        if(s == null || s.length() <= 1) return s;
        int start = 0; //record where the longest substring starts
        int length = 0; //record the length of longest substring
        char[] characters = s.toCharArray();
        boolean[][] isPalindrome = new boolean[s.length()][s.length()];

        //Initialization
        for(int i = 0; i < s.length(); i++) {
            isPalindrome[i][i] = true;  //every single element is a palindrome
            start = i;
            length = 1;
        }
        for(int i = 0; i < s.length() - 1; i++) {
            //for all the substring with length of 2, if s[i] == s[i + 1], it is a palindrome
            if(characters[i] == characters[i + 1]) {
                isPalindrome[i][i + 1] = true;
                start = i;
                length = 2;
            }
        }

        //Next, we check substrings with length from 3 to s.length;
        for(int i = 3; i <= s.length(); i++) { // current length of substring
            for(int j = 0; j + i - 1 < s.length(); j++) {
                //P(i, j) = true if s[i] == s[j] and P(i + 1, j - 1) is true
                if(characters[j] == characters[j + i - 1] && isPalindrome[j + 1][j + i - 2]) {
                    isPalindrome[j][j + i - 1] = true;
                    start = j;
                    length = i;
                }
            }
        }
        return s.substring(start, start + length);
    }

    // A cleaner way to implement DP, no need for explicit initialization, just check whether i - j <= 2 && s[j + 1][i - 1]
    // By doing so, if the current substring contains a single element, it automatically return true.
    public String longestPalindromeDP2(String s) {
        if(s == null || s.length() <= 1) return s;
        String res= "";
        int maxLength = 0;
        boolean[][] isPalindrome = new boolean[s.length()][s.length()];
        for(int i = 0; i < s.length(); i++) {
            for(int j = 0; j <= i; j++) { //MOST IMPORTANT STEP! We ONLY check substring from j to i
                isPalindrome[j][i] = s.charAt(j) == s.charAt(i) && (i - j <= 2 || isPalindrome[j + 1][i - 1]);
                if(isPalindrome[j][i]) {
                    if(i - j + 1 > maxLength) {
                        maxLength = i - j + 1;
                        res = s.substring(j, i + 1);
                    }
                }
            }
        }
        return res;
    }

    /**
     * Approach 2: Expand Around Center
     * We can actually reduce the required extra space. For a given index, we can start from the center and expand it towards
     * left and right sides. If anytime s[left] != s[right], the corresponding substring is not a palindrome. The tricky part
     * is that if we have an even length of substring (say "abbc"), there are two letters in the center. We need to first check
     * whether s[i] == s[i + 1] and treat s[i]s[i + 1] as the center and expand towards both sides. By doing so, we omit the 2-D
     * array in approach 1.
     *
     * Time: O(n^2) still O(n^2) substrings to check
     * Space: O(1) omit the 2-D array, no extra space required
     */
    private String res;

    public String longestPalindromeExpandAroundCenter(String s) {
        res = "";
        if(s == null || s.length() <= 1) return s;
        for(int i = 0; i < s.length(); i++) {
            helper(s, i, i);
            helper(s, i, i + 1);
        }
        return res;
    }

    private void helper(String s, int left, int right) {
        while(left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            left -= 1;
            right += 1;
        }
        String curr = s.substring(left + 1, right);
        if(curr.length() > res.length()) res = curr;
    }

    /**
     * Approach 3: Manacher's Algorithm
     * We can solve this problem in O(n) runtime. Inspired by expanding around center, we can actually update length at a given index
     * in this rule:
     * 1. If i > rightmost boundary (R), then lengths[i] = min(R - i, its mirror element (lengths[2*C - i]))
     * 2. We try to expand the length to see whether we can obtain longer palindromes. Node that we don't need to check little by little,
     *    Based on potential longest length, we can start from the current longest palindrome
     * 3. If i + lengths[i] > rightmost boundary (R), we outreach the boundary, so we need to update the center C to i and R to i + lengths[i]
     *
     * Meanwhile, we can record the max palindrome length and the center of longest substring during the update.
     * However, in order to correctly implement this algorithm, we need to add a hash sign '#' between two characters and add two other
     * different characters at the front and the end (e.g. '$', '@')
     *
     * The longest palindrome in our modified string occurs between maxCenter - maxLength and maxCenter + maxLength.
     * We can divide the indexes by two to retrieve indexes in the original string.
     *
     * Time: O(2n) = O(n), we actually ONLY update the center and the right boundary during iteration, we never recalculate any information
     *      we obtained before.
     * Space: O(2*2n) = O(n), we need a new character array to store '#' and original characters in the previous string, and another
     *       integer array to store longest length at that index.
     */
    public String longestPalindromeManacher(String s) {
        char[] chars = new char[2 * s.length() + 3];
        chars[0] = '$';
        chars[1] = '#';
        int index = 2;
        for(char c : s.toCharArray()) {
            chars[index++] = c;
            chars[index++] = '#';
        }
        chars[index++] = '@'; // now, index = chars.length
        int[] lengths = new int[index];
        int maxLength = 0, maxIndex = 0, center = 0, rightBoundary = 0;
        for(int i = 1; i < index - 1; i++) {
            // check whether current index < rightmost boundary
            // If so, lengths[i] = min(R - i, its mirror element)
            if(i < rightBoundary) lengths[i] = Math.min(rightBoundary - i, lengths[2 * center - i]);
            // try to expand current substring
            while(chars[i + (1 + lengths[i])] == chars[i - (1 + lengths[i])]) lengths[i] += 1;
            // If after expanding, i + lengths[i] > right boundary
            // update the center and the right boundary
            if(i + lengths[i] > rightBoundary) {
                center = i;
                rightBoundary = i + lengths[i];
            }
            // record maxLength and the center of longest palindrome substring
            if(lengths[i] > maxLength) {
                maxIndex = i;
                maxLength = lengths[i];
            }
        }
        // convert maxIndex back to the original string
        return s.substring((maxIndex - maxLength) / 2, (maxIndex + maxLength) / 2);
    }

    @Test
    public void longestPalindromeManacherTest() {
        /**
         * Example 1:
         * Input: "babad"
         * Output: "bab"
         * Note: "aba" is also a valid answer.
         */
        String s1 = "babad";
        Set<String> expectedSet1 = new HashSet<>(Arrays.asList("bab", "aba"));
        String actual1 = longestPalindromeManacher(s1);
        assertTrue(expectedSet1.contains(actual1));
        /**
         * Example 2:
         * Input: "cbbd"
         * Output: "bb"
         */
        String s2 = "cbbd";
        String expected2 = "bb";
        String actual2 = longestPalindromeManacher(s2);
        assertEquals(expected2, actual2);
        /**
         * Example 3:
         * Input: "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
         * Output: "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
         */
        String s3 = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        String expected3 = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        String actual3 = longestPalindromeManacher(s3);
        assertEquals(expected3, actual3);
    }


    @Test
    public void longestPalindromeExpandAroundCenterTest() {
        /**
         * Example 1:
         * Input: "babad"
         * Output: "bab"
         * Note: "aba" is also a valid answer.
         */
        String s1 = "babad";
        Set<String> expectedSet1 = new HashSet<>(Arrays.asList("bab", "aba"));
        String actual1 = longestPalindromeExpandAroundCenter(s1);
        assertTrue(expectedSet1.contains(actual1));
        /**
         * Example 2:
         * Input: "cbbd"
         * Output: "bb"
         */
        String s2 = "cbbd";
        String expected2 = "bb";
        String actual2 = longestPalindromeExpandAroundCenter(s2);
        assertEquals(expected2, actual2);
        /**
         * Example 3:
         * Input: "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
         * Output: "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
         */
        String s3 = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        String expected3 = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        String actual3 = longestPalindromeExpandAroundCenter(s3);
        assertEquals(expected3, actual3);
    }

    @Test
    public void longestPalindromeDPTest() {
        /**
         * Example 1:
         * Input: "babad"
         * Output: "bab"
         * Note: "aba" is also a valid answer.
         */
        String s1 = "babad";
        Set<String> expectedSet1 = new HashSet<>(Arrays.asList("bab", "aba"));
        String actual1 = longestPalindromeDP1(s1);
        String actual11 = longestPalindromeDP2(s1);
        assertTrue(expectedSet1.contains(actual1));
        assertTrue(expectedSet1.contains(actual11));
        /**
         * Example 2:
         * Input: "cbbd"
         * Output: "bb"
         */
        String s2 = "cbbd";
        String expected2 = "bb";
        String actual2 = longestPalindromeDP1(s2);
        String actual22 = longestPalindromeDP2(s2);
        assertEquals(expected2, actual2);
        assertEquals(expected2, actual22);
        /**
         * Example 3:
         * Input: "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
         * Output: "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
         */
        String s3 = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        String expected3 = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        String actual3 = longestPalindromeDP1(s3);
        String actual33 = longestPalindromeDP2(s3);
        assertEquals(expected3, actual3);
        assertEquals(expected3, actual33);
    }

}
