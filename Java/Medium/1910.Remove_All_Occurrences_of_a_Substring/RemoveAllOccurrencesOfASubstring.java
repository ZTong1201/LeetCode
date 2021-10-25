import org.junit.Test;

import java.util.Stack;

import static org.junit.Assert.assertEquals;

public class RemoveAllOccurrencesOfASubstring {

    /**
     * Given two strings s and part, perform the following operation on s until all occurrences of the substring part are removed:
     * <p>
     * Find the leftmost occurrence of the substring part and remove it from s.
     * Return s after removing all occurrences of part.
     * <p>
     * A substring is a contiguous sequence of characters in a string.
     * <p>
     * Constraints:
     * <p>
     * 1 <= s.length <= 1000
     * 1 <= part.length <= 1000
     * s and part consists of lowercase English letters.
     * <p>
     * Approach: Stack
     * If built-in string helper methods are not allowed, we can use stack to solve this problem. Basically, we keep pushing
     * characters into the stack, and when the size of stack >= the length of part, we can check whether the top value of the
     * stack equals to the last character in part. Then we retrospectively pop characters from the stack and see whether we
     * can have a match with the part. If we have a match, then we've already removed those characters. If not, we need add
     * those popped characters back to the stack and keep iterating over the input string.
     * <p>
     * Time: O(m * n) where n is the length of s, and m is the length of part
     * Space: O(m + n) we need a stack of O(n) to store all characters of s, and also intermediate string builder to carry
     * the current pattern, which may up to O(m)
     */
    public String removeOccurrences(String s, String part) {
        Stack<Character> stack = new Stack<>();
        int sLen = s.length(), partLength = part.length();

        for (int i = 0; i < sLen; i++) {
            // always append the current character into the stack
            stack.push(s.charAt(i));

            // and check whether we can remove part from s
            if (stack.size() >= partLength) {
                // need a string builder to store the current pattern
                StringBuilder currPattern = new StringBuilder();
                // traverse the part string backward
                for (int j = partLength - 1; j >= 0; j--) {
                    // if the characters are not fully matched, then we didn't find an entire part
                    // add those removed characters back to the stack
                    if (part.charAt(j) != stack.peek()) {
                        for (int k = 0; k < currPattern.length(); k++) {
                            stack.push(currPattern.charAt(k));
                        }
                        // stop the iteration since we cannot further remove anything
                        break;
                    } else {
                        // if the top character in the stack matches with the character in part
                        // temporarily pop that character from stack and see we can finally remove it
                        currPattern.insert(0, stack.pop());
                    }
                }
            }
        }

        // construct new string from the stack
        StringBuilder newString = new StringBuilder();
        while (!stack.isEmpty()) {
            newString.insert(0, stack.pop());
        }
        return newString.toString();
    }

    @Test
    public void removeOccurrencesTest() {
        /**
         * Example 1:
         * Input: s = "daabcbaabcbc", part = "abc"
         * Output: "dab"
         * Explanation: The following operations are done:
         * - s = "daabcbaabcbc", remove "abc" starting at index 2, so s = "dabaabcbc".
         * - s = "dabaabcbc", remove "abc" starting at index 4, so s = "dababc".
         * - s = "dababc", remove "abc" starting at index 3, so s = "dab".
         * Now s has no occurrences of "abc".
         */
        assertEquals("dab", removeOccurrences("daabcbaabcbc", "abc"));
        /**
         * Example 2:
         * Input: s = "axxxxyyyyb", part = "xy"
         * Output: "ab"
         * Explanation: The following operations are done:
         * - s = "axxxxyyyyb", remove "xy" starting at index 4 so s = "axxxyyyb".
         * - s = "axxxyyyb", remove "xy" starting at index 3 so s = "axxyyb".
         * - s = "axxyyb", remove "xy" starting at index 2 so s = "axyb".
         * - s = "axyb", remove "xy" starting at index 1 so s = "ab".
         * Now s has no occurrences of "xy".
         */
        assertEquals("ab", removeOccurrences("axxxxyyyyb", "xy"));
    }
}
