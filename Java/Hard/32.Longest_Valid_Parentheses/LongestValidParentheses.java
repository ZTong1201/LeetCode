import org.junit.Test;

import java.util.Stack;

import static org.junit.Assert.assertEquals;

public class LongestValidParentheses {

    /**
     * Given a string containing just the characters '(' and ')', find the length of the longest valid (well-formed)
     * parentheses substring.
     * <p>
     * Constraints:
     * <p>
     * 0 <= s.length <= 3 * 10^4
     * s[i] is '(', or ')'.
     * <p>
     * Approach: DP
     * The brute force approach would be for each substring (O(n^2) in total), we check whether it's a valid parentheses string,
     * it will take up to O(n) runtime hence make it O(n^3). We could use DP to optimize the algorithm. Basically, denote
     * dp[i] as the longest length of substring which has valid parentheses ending at index i.
     * Note that if s[i] == '(', dp[i] will always be 0 since it's not possible to form valid parentheses with ending '('.
     * If s[i] == ')', there are two possibilities.
     * 1. s[i - 1] == '(', hence we can add two more characters into the previous valid substring until i - 1, i.e.
     * dp[i] = dp[i - 2] + 2. (if i - 2 is a valid index)
     * 2. s[i - 1] == ')', this is tricky cuz we may have a long substring as '....))' which could be valid as well.
     * Hence, we want to check whether there is a '(' matching with s[i], essentially, if that match exists, the index
     * of that '(' will be i - 1 - dp[i - 1]. If s[i - 1 - dp[i - 1]] == '(' and s[i] = ')', then we know at least
     * from i - 1 - dp[i - 1] to i is a valid substring, that length of valid substring in between is stored in dp[i - 1]
     * already. Also, we might have something like this s = "()()(())", the final length at index 7 will be
     * dp[6] = 2 which corresponds to s[5-6] = "()"
     * + 2 because we can add index 4 and index 7 to form a longer substring
     * + dp[3] = 4, cuz there is a substring of length 4 which is also valid, and we can bond them together.
     * therefore,
     * dp[i] = dp[i - 1] + dp[i - dp[i - 1] - 2] + 2 if s[i - 1] == ')'
     * <p>
     * Time: O(n) we will traverse the entire string once
     * Space: O(n)
     */
    public int longestValidParenthesesDP(String s) {
        int maxLength = 0, length = s.length();
        int[] dp = new int[length];

        for (int i = 1; i < length; i++) {
            // we can only update the length when substring is ending with ')'
            if (s.charAt(i) == ')') {
                // if we can close out with the previous '('
                if (s.charAt(i - 1) == '(') {
                    // then we can add 2 more characters into the previous valid substring
                    dp[i] = ((i - 2 >= 0) ? dp[i - 2] : 0) + 2;
                } else if (i - 1 - dp[i - 1] >= 0 && s.charAt(i - 1 - dp[i - 1]) == '(') {
                    // if the previous character is also a ')', then we check whether we can close it with a previous '('
                    // if we can, add 2 more characters into the current valid substring and see whether there is a possibility
                    // to bond with previous substring
                    dp[i] = dp[i - 1] + (i - dp[i - 1] - 2 >= 0 ? dp[i - dp[i - 1] - 2] : 0) + 2;
                }
            }
            maxLength = Math.max(maxLength, dp[i]);
        }
        return maxLength;
    }

    /**
     * Approach 2: Stack
     * We could always consider using stack to solve parentheses matching problems. Essentially, we push the index into
     * the stack if reaching a '(', and popping an index from the stack if reaching a ')'. Essentially, we close out an
     * open parenthesis. To avoid popping from an empty stack in case we have ")(((", we could insert index -1 into the
     * queue. Once we close a pair of parentheses, if we still have something in the stack, that is the index where
     * parentheses haven't been closed, therefore, the current index i - stack.peek() will be the length of a valid substring.
     * However, if the stack is empty, for example, we have "())", the stack will be empty when reaching the second right
     * parenthesis. In such a case, we also add that index into the stack, because this right parenthesis will never be
     * closed in the future. It will serve as a rightmost boundary and a longer substring can only form starting from there.
     * <p>
     * Time: O(n) each character will be pushed and popped into the stack at most once, which takes O(n) time
     * Space: O(n)
     */
    public int longestValidParenthesesStack(String s) {
        Stack<Integer> index = new Stack<>();
        // add -1 as a placeholder in case the first character in s is ')'
        index.push(-1);
        int maxLength = 0;

        for (int i = 0; i < s.length(); i++) {
            char curr = s.charAt(i);
            // always insert current index into the stack if it's a left parenthesis
            if (curr == '(') {
                index.push(i);
            } else {
                // otherwise, close out an open parenthesis
                index.pop();
                // if the stack is empty, add current index into the stack as a right boundary
                if (index.isEmpty()) {
                    index.push(i);
                } else {
                    // otherwise, compute the maximum length
                    maxLength = Math.max(maxLength, i - index.peek());
                }
            }
        }
        return maxLength;
    }

    /**
     * Approach 3: Two passes
     * We can keep track of the number of left parentheses and right parentheses while going through the entire string.
     * When traversing from the left to the right, if at any time left == right, then the current length will be 2 * right.
     * However, if right > left, we cannot form a longer valid substring from the very beginning, we set both left and right
     * to 0. We need one more pass from the right to the left, and this time the left will be 2 * left if left == right. When
     * left > right, we reset both counters again.
     * <p>
     * Time: O(n)
     * Space: O(1)
     */
    public int longestValidParenthesesTwoPasses(String s) {
        int left = 0, right = 0;
        int maxLength = 0;

        // first pass, left to right
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                left++;
            } else {
                right++;
            }

            if (left == right) {
                maxLength = Math.max(maxLength, 2 * right);
            } else if (right > left) {
                left = right = 0;
            }
        }

        left = right = 0;
        // second pass, right to left
        for (int i = s.length() - 1; i >= 0; i--) {
            if (s.charAt(i) == '(') {
                left++;
            } else {
                right++;
            }

            if (left == right) {
                maxLength = Math.max(maxLength, 2 * left);
            } else if (left > right) {
                left = right = 0;
            }
        }
        return maxLength;
    }

    @Test
    public void longestValidParenthesesTest() {
        /**
         * Example 1:
         * Input: s = "(()"
         * Output: 2
         * Explanation: The longest valid parentheses substring is "()".
         */
        assertEquals(2, longestValidParenthesesDP("(()"));
        assertEquals(2, longestValidParenthesesStack("(()"));
        assertEquals(2, longestValidParenthesesTwoPasses("(()"));
        /**
         * Example 2:
         * Input: s = ")()())"
         * Output: 4
         * Explanation: The longest valid parentheses substring is "()()".
         */
        assertEquals(4, longestValidParenthesesDP(")()())"));
        assertEquals(4, longestValidParenthesesStack(")()())"));
        assertEquals(4, longestValidParenthesesTwoPasses(")()())"));
        /**
         * Example 3:
         * Input: s = ")()()(()))"
         * Output: 8
         * Explanation: The longest valid parentheses substring is "()()(())".
         */
        assertEquals(8, longestValidParenthesesDP(")()()(()))"));
        assertEquals(8, longestValidParenthesesStack(")()()(()))"));
        assertEquals(8, longestValidParenthesesTwoPasses(")()()(()))"));
    }
}
