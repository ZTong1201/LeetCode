import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class validParentheses {

    /**
     * Given a string containing just the characters '(', ')', '{', '}', '[' and ']', determine if the input string is valid.
     *
     * An input string is valid if:
     *
     * Open brackets must be closed by the same type of brackets.
     * Open brackets must be closed in the correct order.
     * Note that an empty string is also considered valid.
     *
     * Stack:
     * We can use a stack data structure to solve this problem neatly. First we need a map to contain left parentheses with values of
     * its corresponding right parentheses. We iterate over the string, if at any time we see a left parenthesis, we add its matched
     * right parenthesis in the stack, indicating that we need to find this right parenthesis somewhere. If at any time we see a right
     * parenthesis, we need to check two things:
     * 1. If the stack is empty, which means there is no left parenthesis seen before, the string is invalid
     * 2. If the stack is not empty, we need to see whether the right parenthesis at the top of the stack matches what we'd like to find
     *    If it is, pop the top element from the stack. Otherwise, the string is invalid.
     * In the end, we just check whether the stack is empty or not.
     *
     * Time: O(N), we need to iterate over the string to check whether it is valid or not
     * Space: O(N), in the worst case (e.g. "((((((((((((((("), we need to push every element in the stack
     */
    public boolean isValid(String s) {
        Map<Character, Character> map = new HashMap<>();
        map.put('(', ')');
        map.put('[', ']');
        map.put('{', '}');
        Stack<Character> stack = new Stack<>();
        int length = s.length();
        for(int i = 0; i < length; i++) {
            char c = s.charAt(i);
            if(stack.isEmpty() && !map.containsKey(c)) return false;
            if(map.containsKey(c)) stack.push(map.get(c));
            else if(stack.peek() != c) return false;
            else stack.pop();
        }
        return stack.isEmpty();
    }

    @Test
    public void isValidTest() {
        /**
         * Example 1:
         * Input: "()"
         * Output: true
         */
        String s1 = "()";
        assertTrue(isValid(s1));
        /**
         * Example 2:
         * Input: "()[]{}"
         * Output: true
         */
        String s2 = "()[]{}";
        assertTrue(isValid(s2));
        /**
         * Example 3:
         * Input: "(]"
         * Output: false
         */
        String s3 = "(]";
        assertFalse(isValid(s3));
        /**
         * Example 4:
         * Input: "([)]"
         * Output: false
         */
        String s4 = "([)]";
        assertFalse(isValid(s4));
        /**
         * Example 5:
         * Input: "{[]}"
         * Output: true
         */
        String s5 = "{[]}";
        assertTrue(isValid(s5));

    }
}
