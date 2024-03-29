import org.junit.Test;

import java.util.Stack;

import static org.junit.Assert.assertEquals;

public class evaluateRPN {

    /**
     * Evaluate the value of an arithmetic expression in Reverse Polish Notation.
     * <p>
     * Valid operators are +, -, *, and /. Each operand may be an integer or another expression.
     * <p>
     * Note that division between two integers should truncate toward zero.
     * <p>
     * It is guaranteed that the given RPN expression is always valid. That means the expression would always evaluate to
     * a result, and there will not be any division by zero operation.
     * <p>
     * Constraints:
     * <p>
     * 1 <= tokens.length <= 10^4
     * tokens[i] is either an operator: "+", "-", "*", or "/", or an integer in the range [-200, 200].
     * <p>
     * Approach: Stack
     * <p>
     * Time: O(n)
     * Space: O(n)
     */
    public int evalRPN(String[] tokens) {
        Stack<Integer> stack = new Stack<>();

        for (String token : tokens) {
            switch (token) {
                case "+": {
                    stack.push(stack.pop() + stack.pop());
                    break;
                }
                case "-": {
                    int num1 = stack.pop(), num2 = stack.pop();
                    stack.push(num2 - num1);
                    break;
                }
                case "*": {
                    stack.push(stack.pop() * stack.pop());
                    break;
                }
                case "/": {
                    int num1 = stack.pop(), num2 = stack.pop();
                    stack.push(num2 / num1);
                    break;
                }
                default: {
                    stack.push(Integer.parseInt(token));
                }
            }
        }
        return stack.pop();
    }

    @Test
    public void evalRPNTest() {
        /**
         * Example 1:
         * Input: ["2", "1", "+", "3", "*"]
         * Output: 9
         * Explanation: ((2 + 1) * 3) = 9
         */
        String[] tokens1 = new String[]{"2", "1", "+", "3", "*"};
        assertEquals(9, evalRPN(tokens1));
        /**
         * Example 2:
         * Input: ["4", "13", "5", "/", "+"]
         * Output: 6
         * Explanation: (4 + (13 / 5)) = 6
         */
        String[] tokens2 = new String[]{"4", "13", "5", "/", "+"};
        assertEquals(6, evalRPN(tokens2));
        /**
         * Example 3:
         * Input: ["10", "6", "9", "3", "+", "-11", "*", "/", "*", "17", "+", "5", "+"]
         * Output: 22
         * Explanation:
         *   ((10 * (6 / ((9 + 3) * -11))) + 17) + 5
         * = ((10 * (6 / (12 * -11))) + 17) + 5
         * = ((10 * (6 / -132)) + 17) + 5
         * = ((10 * 0) + 17) + 5
         * = (0 + 17) + 5
         * = 17 + 5
         * = 22
         */
        String[] tokens3 = new String[]{"10", "6", "9", "3", "+", "-11", "*", "/", "*", "17", "+", "5", "+"};
        assertEquals(22, evalRPN(tokens3));
    }
}
