import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class removeParentheses {


    /**
     * Remove the minimum number of invalid parentheses in order to make the input string valid. Return all possible results.
     * <p>
     * Note: The input string may contain letters other than the parentheses ( and ).
     * <p>
     * Approach 1: Simple Backtrack
     * <p>
     * Since we don't have a better strategy to solve this problem, we can actually search for all the possible results. For a given
     * parenthesis (either '(' or ')'), we can either add or remove it from the final expression and check whether it can form a valid
     * expression. Note that we may have other letters in the string, such letter will not influence validness of expression, hence we always
     * add them into our final result. To check whether the final expression is valid or not, we keep track of the count of left and right
     * brackets. If they are the same, the expression is valid. Meanwhile, since the problem requires the minimum number of removal, we also
     * need a global variable to record the number of minimum removal. We only add potential expression if current number of removal is less
     * than the minimum.
     * <p>
     * The algorithm can be easily implemented by using backtrack. The only thing we need to care about that is after backtracking (i.e.
     * remove the current parenthesis), we need to keep searching for the next parenthesis by incrementing the current number of removal.
     * <p>
     * Time: O(2^n) if the worst case, for each parenthesis, we need to either include it or remove it and check whether the final answer
     * is valid or not
     * Space: O(n) the call stack will require O(n) space in the worst case
     */
    // we need a global variable to record the minimum number of removal
    private int minRemoval;

    public List<String> removeInvalidParenthesesBacktrack(String s) {
        this.minRemoval = Integer.MAX_VALUE;
        // need a set to avoid duplicates
        Set<String> res = new HashSet<>();
        dfs(s, 0, 0, 0, res, new StringBuilder(), 0);
        return new ArrayList<>(res);
    }

    /**
     * Backtrack function to either add or remove current parenthesis
     *
     * @param s           the invalid string
     * @param index       the current index at input string
     * @param leftCount   the number of left parenthesis in the current expression
     * @param rightCount  the number of right parenthesis in the current expression
     * @param res         a final set containing all the valid expressions
     * @param validExpr   a string builder to contain intermediate expression
     * @param currRemoval current number of parentheses removal
     */
    private void dfs(String s, int index, int leftCount, int rightCount, Set<String> res, StringBuilder validExpr, int currRemoval) {
        // if we reach the end of the string
        // we need to check whether we have valid expression or not
        if (index == s.length()) {
            // check whether the current expression is valid or not
            if (leftCount == rightCount) {
                // only adding expression when current number of removal is smaller (or equal to) the global minimum
                if (currRemoval <= this.minRemoval) {
                    if (currRemoval < this.minRemoval) {
                        // if it is smaller, we update the global minimum and clear all previous expressions in the set
                        res.clear();
                        this.minRemoval = currRemoval;
                    }
                    // in either case, add current valid expression
                    res.add(validExpr.toString());
                }
            }
        } else { // otherwise, we keep searching for valid expressions
            int length = validExpr.length();
            char curr = s.charAt(index);

            // if current char is neither a left parenthesis nor a right parenthesis
            // simply add it and keep searching the next position
            if (curr != '(' && curr != ')') {
                validExpr.append(curr);
                dfs(s, index + 1, leftCount, rightCount, res, validExpr, currRemoval);
                validExpr.deleteCharAt(length);
            } else {
                // otherwise, we do the backtrack stuff

                // first, add current parenthesis
                validExpr.append(curr);
                // search the next position
                if (curr == '(') {
                    dfs(s, index + 1, leftCount + 1, rightCount, res, validExpr, currRemoval);
                } else if (rightCount < leftCount) {
                    // we can prune the recursion tree, since if we already have more right parenthesis, the current
                    // expression is invalid
                    dfs(s, index + 1, leftCount, rightCount + 1, res, validExpr, currRemoval);
                }
                // backtrack to previous stage by deleting current parenthesis
                validExpr.deleteCharAt(length);
                // restart searching for valid expressions
                dfs(s, index + 1, leftCount, rightCount, res, validExpr, currRemoval + 1);
            }
        }
    }

    @Test
    public void removeInvalidParenthesesBacktrackTest() {
        /**
         * Example 1:
         * Input: "()())()"
         * Output: ["()()()", "(())()"]
         */
        List<String> actual1 = removeInvalidParenthesesBacktrack("()())()");
        Set<String> expected1 = new HashSet<>(Arrays.asList("()()()", "(())()"));
        assertEquals(expected1.size(), actual1.size());
        for (int i = 0; i < actual1.size(); i++) {
            assertTrue(expected1.contains(actual1.get(i)));
        }
        /**q
         * Example 2:
         * Input: "(a)())()"
         * Output: ["(a)()()", "(a())()"]
         */
        List<String> actual2 = removeInvalidParenthesesBacktrack("(a)())()");
        Set<String> expected2 = new HashSet<>(Arrays.asList("(a)()()", "(a())()"));
        assertEquals(expected2.size(), actual2.size());
        for (int i = 0; i < actual2.size(); i++) {
            assertTrue(expected2.contains(actual2.get(i)));
        }
        /**
         * Example 3:
         * Input: ")("
         * Output: [""]
         */
        List<String> actual3 = removeInvalidParenthesesBacktrack(")(");
        Set<String> expected3 = new HashSet<>(Arrays.asList(""));
        assertEquals(expected3.size(), actual3.size());
        for (int i = 0; i < actual3.size(); i++) {
            assertTrue(expected3.contains(actual3.get(i)));
        }
    }

    /**
     * Approach 2: Limited Backtracking
     * <p>
     * We can prune the recursion tree further by precomputing the number of removal for left and right parentheses. If we know how many
     * misplaced parentheses in the input string, when we have already remove that amount of parenthesis, we will never discard parenthesis
     * This approach will tremendously cut off the number of nodes we should visit. Meanwhile, we don't need a global variable anymore.
     * We can check whether the number to be removed parentheses is 0 or not.
     * <p>
     * Time: O(2^n) in the worst case. suppose we have "((((((((((", we still have to check all the possible choices (either adding or deleting
     * each parenthesis to get the final result.
     * Space: O(n) the call stack will require O(n) space in the worst case
     */
    private Set<String> res;
    private char[] chars;

    public List<String> removeInvalidParenthesesLimitedBacktrack(String s) {
        chars = s.toCharArray();
        res = new HashSet<>();
        // compute number of misplaced parentheses
        int left = 0, right = 0;
        for (int i = 0; i < s.length(); i++) {
            // if it is a left parenthesis, we have a possible misplaced left parenthesis
            if (s.charAt(i) == '(') {
                left++;
            }
            // if it is a right parenthesis
            // we check whether we can close out a previous open left parenthesis
            // if yes, we decrement a misplaced left parenthesis
            // if not, we actually have a misplaced right parenthesis now
            if (s.charAt(i) == ')') {
                if (left > 0) left--;
                else right++;
            }
        }
        // call the helper function
        helper(0, 0, 0, new StringBuilder(), left, right);
        return new ArrayList<>(res);
    }

    /**
     * The algorithm is basically the same. For adding elements, we don't have any difference. We only need to check whether we can
     * delete any parentheses when discarding. Besides, checking validness is just check whether left == 0 && right == 0
     */
    private void helper(int start, int leftCount, int rightCount, StringBuilder sb, int leftRemoval, int rightRemoval) {
        // base case - when we traverse the entire string
        if (start == chars.length) {
            // if all the parentheses are balanced - add current string to the set
            if (leftRemoval == 0 && rightRemoval == 0) {
                res.add(sb.toString());
            }
            return;
        }
        char curr = chars[start];

        // always first add current character
        sb.append(curr);

        // keep searching by recursively calling the function
        if (curr != '(' && curr != ')') {
            helper(start + 1, leftCount, rightCount, sb, leftRemoval, rightRemoval);
        } else if (curr == '(') {
            helper(start + 1, leftCount + 1, rightCount, sb, leftRemoval, rightRemoval);
        } else if (rightCount < leftCount) {
            // early termination - if the count of right parenthesis is larger than its left counterpart
            // there is no way we can balance subsequently
            helper(start + 1, leftCount, rightCount + 1, sb, leftRemoval, rightRemoval);
        }

        // backtrack to previous stage by deleting current character
        sb.deleteCharAt(sb.length() - 1);

        // if current character is a parenthesis, and we still can delete more parentheses, then restart searching
        if (curr == '(' && leftRemoval > 0) {
            helper(start + 1, leftCount, rightCount, sb, leftRemoval - 1, rightRemoval);
        } else if (curr == ')' && rightRemoval > 0) {
            helper(start + 1, leftCount, rightCount, sb, leftRemoval, rightRemoval - 1);
        }
    }

    @Test
    public void removeInvalidParenthesesLimitedBacktrackTest() {
        /**
         * Example 1:
         * Input: "()())()"
         * Output: ["()()()", "(())()"]
         */
        List<String> actual1 = removeInvalidParenthesesLimitedBacktrack("()())()");
        Set<String> expected1 = new HashSet<>(Arrays.asList("()()()", "(())()"));
        assertEquals(expected1.size(), actual1.size());
        for (int i = 0; i < actual1.size(); i++) {
            assertTrue(expected1.contains(actual1.get(i)));
        }
        /**q
         * Example 2:
         * Input: "(a)())()"
         * Output: ["(a)()()", "(a())()"]
         */
        List<String> actual2 = removeInvalidParenthesesLimitedBacktrack("(a)())()");
        Set<String> expected2 = new HashSet<>(Arrays.asList("(a)()()", "(a())()"));
        assertEquals(expected2.size(), actual2.size());
        for (int i = 0; i < actual2.size(); i++) {
            assertTrue(expected2.contains(actual2.get(i)));
        }
        /**
         * Example 3:
         * Input: ")("
         * Output: [""]
         */
        List<String> actual3 = removeInvalidParenthesesLimitedBacktrack(")(");
        Set<String> expected3 = new HashSet<>(Arrays.asList(""));
        assertEquals(expected3.size(), actual3.size());
        for (int i = 0; i < actual3.size(); i++) {
            assertTrue(expected3.contains(actual3.get(i)));
        }
    }
}
