import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class evaluateRPN {

    /**
     * Evaluate the value of an arithmetic expression in Reverse Polish Notation.
     *
     * Valid operators are +, -, *, /. Each operand may be an integer or another expression.
     *
     * Note:
     *
     * Division between two integers should truncate toward zero.
     * The given RPN expression is always valid. That means the expression would always evaluate to a result and there won't be any
     * divide by zero operation.
     */
    public int evalRPN(String[] tokens) {

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
