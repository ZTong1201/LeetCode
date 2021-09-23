import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExpressionTreeTest {

    @Test
    public void expressionTreeTest() {
        /**
         * Example 1:
         * Input: s = ["3","4","+","2","*","7","/"]
         * Output: 2
         * Explanation: this expression evaluates to the above binary tree with expression ((3+4)*2)/7) = 14/7 = 2.
         *                         /
         *                       /   \
         *                      *     7
         *                     / \
         *                    +   2
         *                   / \
         *                  3   4
         */
        ExpressionTree expressionTree = new ExpressionTree();
        Node root = expressionTree.buildTree(new String[]{"3", "4", "+", "2", "*", "7", "/"});
        assertEquals(2, root.evaluate());
        /**
         * Example 2:
         * Input: s = ["4","5","2","7","+","-","*"]
         * Output: -16
         * Explanation: this expression evaluates to the above binary tree with expression 4*(5-(2+7)) = 4*(-4) = -16.
         *                             *
         *                           /   \
         *                          4     -
         *                               / \
         *                              5   +
         *                                 / \
         *                                2   7
         */
        root = expressionTree.buildTree(new String[]{"4", "5", "2", "7", "+", "-", "*"});
        assertEquals(-16, root.evaluate());
        /**
         * Example 3:
         * Input: s = ["4","2","+","3","5","1","-","*","+"]
         * Output: 18
         */
        root = expressionTree.buildTree(new String[]{"4", "2", "+", "3", "5", "1", "-", "*", "+"});
        assertEquals(18, root.evaluate());
        /**
         * Example 4:
         * Input: s = ["100","200","+","2","/","5","*","7","+"]
         * Output: 757
         */
        root = expressionTree.buildTree(new String[]{"100", "200", "+", "2", "/", "5", "*", "7", "+"});
        assertEquals(757, root.evaluate());
    }
}
