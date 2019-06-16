import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class reverseWords {

    /**
     * Given an input string, reverse the string word by word.
     *
     * Note:
     *
     * A word is defined as a sequence of non-space characters.
     * Input string may contain leading or trailing spaces. However, your reversed string should not contain leading or trailing spaces.
     * You need to reduce multiple spaces between two words to a single space in the reversed string.
     *
     * Time: O(N)
     * Space: O(2*N), where N is the length of string
     *
     * IMPORTANT API: String.join(" ", iterables/array)
     *
     */
    public String reverseWords1(String s) {
        String[] words = s.split(" ");
        List<String> wordList = new LinkedList<>();
        for(int i = words.length - 1; i >= 0; i--) {
            if(!words[i].equals("")) {
                wordList.add(words[i]);
            }
        }
        String res = String.join(" ", wordList);
        return res;
    }

    /**
     * We can use a string builder and omit the word list memory allocation
     *
     * Time: O(N)
     * Space: O(N)
     *
     * IMPORTANT API: s.trim() return a string without any trailing and leading whitespaces.
     */
    public String reverseWords2(String s) {
        String[] words = s.trim().split(" ");
        StringBuilder res = new StringBuilder();
        for(int i = words.length - 1; i >= 0; i--) {
            if(!words[i].equals("")) {
                res.append(words[i]);
                if(i > 0) res.append(" ");
            }
        }
        return res.toString();
    }

    @Test
    public void reverseWords2Test() {
        /**
         * Example 1:
         * Input: "the sky is blue"
         * Output: "blue is sky the"
         */
        String string1 = "the sky is blue";
        String actual1 = reverseWords2(string1);
        String expected1 = "blue is sky the";
        assertEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: "  hello world!  "
         * Output: "world! hello"
         * Explanation: Your reversed string should not contain leading or trailing spaces.
         */
        String string2 = "  hello world!  ";
        String actual2 = reverseWords2(string2);
        String expected2 = "world! hello";
        assertEquals(expected2, actual2);
        /**
         * Example 3:
         * Input: "a good   example"
         * Output: "example good a"
         * Explanation: You need to reduce multiple spaces between two words to a single space in the reversed string.
         */
        String string3 = "a good   example";
        String actual3 = reverseWords2(string3);
        String expected3 = "example good a";
        assertEquals(expected3, actual3);
    }

    @Test
    public void reverseWords1Test() {
        /**
         * Example 1:
         * Input: "the sky is blue"
         * Output: "blue is sky the"
         */
        String string1 = "the sky is blue";
        String actual1 = reverseWords1(string1);
        String expected1 = "blue is sky the";
        assertEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: "  hello world!  "
         * Output: "world! hello"
         * Explanation: Your reversed string should not contain leading or trailing spaces.
         */
        String string2 = "  hello world!  ";
        String actual2 = reverseWords1(string2);
        String expected2 = "world! hello";
        assertEquals(expected2, actual2);
        /**
         * Example 3:
         * Input: "a good   example"
         * Output: "example good a"
         * Explanation: You need to reduce multiple spaces between two words to a single space in the reversed string.
         */
        String string3 = "a good   example";
        String actual3 = reverseWords1(string3);
        String expected3 = "example good a";
        assertEquals(expected3, actual3);
    }
}
