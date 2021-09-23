import java.util.Stack;

public class ExpressionTree {

    /**
     * Given the postfix tokens of an arithmetic expression, build and return the binary expression tree that represents
     * this expression.
     * <p>
     * Postfix notation is a notation for writing arithmetic expressions in which the operands (numbers) appear before their
     * operators. For example, the postfix tokens of the expression 4*(5-(7+2)) are represented in the array postfix =
     * ["4","5","7","2","+","-","*"].
     * <p>
     * The class Node is an interface you should use to implement the binary expression tree. The returned tree will be
     * tested using the evaluate function, which is supposed to evaluate the tree's value. You should not remove the Node
     * class; however, you can modify it as you wish, and you can define other classes to implement it if needed.
     * <p>
     * A binary expression tree is a kind of binary tree used to represent arithmetic expressions. Each node of a binary
     * expression tree has either zero or two children. Leaf nodes (nodes with 0 children) correspond to operands (numbers),
     * and internal nodes (nodes with two children) correspond to the operators '+' (addition), '-' (subtraction), '*'
     * (multiplication), and '/' (division).
     * <p>
     * It's guaranteed that no subtree will yield a value that exceeds 10^9 in absolute value, and all the operations are
     * valid (i.e., no division by zero).
     * <p>
     * Follow up: Could you design the expression tree such that it is more modular? For example, is your design able to
     * support additional operators without making changes to your existing evaluate implementation?
     * <p>
     * Constraints:
     * <p>
     * 1 <= s.length < 100
     * s.length is odd.
     * s consists of numbers and the characters '+', '-', '*', and '/'.
     * If s[i] is a number, its integer representation is no more than 105.
     * It is guaranteed that s is a valid expression.
     * The absolute value of the result and intermediate values will not exceed 109.
     * It is guaranteed that no expression will include division by zero.
     * <p>
     * Approach: Stack
     * Basically, in order to build the expression tree. We need to keep adding nodes into the stack
     * 1. If it's a number, then create an operand node and push it to the stack
     * 2. If it's an operator, we need to create an operator node, where left and right children will be popped from the stack
     * e.g. [3, 4, '+']
     * stack = [node(3)]
     * stack = [node(3), node(4)]
     * stack = [node(+)], where node(+).left = node(3), node(+).right = node(4)
     * To resolve the follow-up question, we can define an operator abstract class to extend the Node class. Then we implement
     * each operator separately to handle different operators, add, minus, etc.
     * The evaluate method in the operand node will return the value directly.
     * The evaluate method in the operator node will return the corresponding value after calculation the result from
     * its left and right children.
     */
    Node buildTree(String[] postfix) {
        // keep adding nodes into the stack
        Stack<Node> stack = new Stack<>();
        for (String curr : postfix) {
            // if the current string is an operator, add the corresponding node into the stack
            // and assign the left and right children from the stack
            if (!Character.isDigit(curr.charAt(0))) {
                Operator operator = null;
                switch (curr) {
                    case "+": {
                        operator = new Add();
                        break;
                    }
                    case "-": {
                        operator = new Minus();
                        break;
                    }
                    case "*": {
                        operator = new Multiply();
                        break;
                    }
                    case "/": {
                        operator = new Division();
                        break;
                    }
                }
                // assign the right child first, then the left child
                // because stack is last in first out
                operator.right = stack.pop();
                operator.left = stack.pop();

                // then add the current node into the stack
                stack.push(operator);
            } else {
                // otherwise, we encounter a number, simply add that into the stack
                int num = Integer.parseInt(curr);
                stack.push(new Operand(num));
            }
        }
        // finally, the expression tree root node will be left in the stack
        return stack.pop();
    }
}
