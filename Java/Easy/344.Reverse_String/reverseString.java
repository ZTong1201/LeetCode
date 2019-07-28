import org.junit.Test;
import static org.junit.Assert.*;

public class reverseString {

    /**
     * Write a function that reverses a string. The input string is given as an array of characters char[].
     *
     * Do not allocate extra space for another array, you must do this by modifying the input array in-place with O(1) extra memory.
     *
     * You may assume all the characters consist of printable ascii characters.
     *
     * Time: O(n/2) = O(n)
     * Space: O(1)
     */
    public void reverseString1(char[] s) {
        for(int i = 0; i < s.length / 2; i++) {
            char tmp = s[i];
            s[i] = s[s.length - 1 - i];
            s[s.length - 1 - i] = tmp;
        }
    }


    @Test
    public void reverseString1Test() {
        /**
         * Example 1:
         * Input: ["h","e","l","l","o"]
         * Output: ["o","l","l","e","h"]
         */
        char[] actual1 = new char[]{'h', 'e', 'l', 'l', 'o'};
        char[] expected1 = new char[]{'o', 'l', 'l', 'e', 'h'};
        reverseString1(actual1);
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: ["H","a","n","n","a","h"]
         * Output: ["h","a","n","n","a","H"]
         */
        char[] actual2 = new char[]{'H', 'a', 'n', 'n', 'a', 'h'};
        char[] expected2 = new char[]{'h', 'a', 'n', 'n', 'a', 'H'};
        reverseString1(actual2);
        assertArrayEquals(expected2, actual2);
    }

    /**
     * Approach 2: Two Pointers
     *
     * Time: O(n)
     * Space: O(1)
     */
    public void reverseString2(char[] s) {
        int left = 0, right = s.length - 1;
        while(left <= right) {
            char tmp = s[left];
            s[left] = s[right];
            s[right] = tmp;
            left++;
            right--;
        }
    }

    @Test
    public void reverseString2Test() {
        /**
         * Example 1:
         * Input: ["h","e","l","l","o"]
         * Output: ["o","l","l","e","h"]
         */
        char[] actual1 = new char[]{'h', 'e', 'l', 'l', 'o'};
        char[] expected1 = new char[]{'o', 'l', 'l', 'e', 'h'};
        reverseString2(actual1);
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: ["H","a","n","n","a","h"]
         * Output: ["h","a","n","n","a","H"]
         */
        char[] actual2 = new char[]{'H', 'a', 'n', 'n', 'a', 'h'};
        char[] expected2 = new char[]{'h', 'a', 'n', 'n', 'a', 'H'};
        reverseString2(actual2);
        assertArrayEquals(expected2, actual2);
    }
}
