import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static org.junit.Assert.assertEquals;

public class NaryTreePreorderTraversal {

    /**
     * Given the root of an n-ary tree, return the preorder traversal of its nodes' values.
     * <p>
     * Nary-Tree input serialization is represented in their level order traversal. Each group of children is
     * separated by the null value (See examples)
     * <p>
     * Constraints:
     * <p>
     * The number of nodes in the tree is in the range [0, 10^4].
     * 0 <= Node.val <= 10^4
     * The height of the n-ary tree is less than or equal to 1000.
     * <p>
     * Follow up: Recursive solution is trivial, could you do it iteratively?
     * <p>
     * Approach 1: Recursion
     * <p>
     * Time: O(n)
     * Space: O(n) in the worst case
     */
    public List<Integer> preorderRecursive(Node root) {
        List<Integer> res = new ArrayList<>();
        preorder(root, res);
        return res;
    }

    private void preorder(Node root, List<Integer> res) {
        if (root == null) return;
        res.add(root.val);
        for (Node child : root.children) {
            preorder(child, res);
        }
    }

    /**
     * Approach 2: Iteration
     * <p>
     * Time: O(n)
     * Space: O(n) in the worst case
     */
    public List<Integer> preorderIterative(Node root) {
        List<Integer> res = new ArrayList<>();
        Stack<Node> stack = new Stack<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            Node curr = stack.pop();
            if (curr != null) {
                res.add(curr.val);
                for (int i = curr.children.size() - 1; i >= 0; i--) {
                    stack.push(curr.children.get(i));
                }
            }
        }
        return res;
    }

    @Test
    public void preorderTest() {
        /**
         * Example:
         * Input: root = [1,null,3,2,4,null,5,6]
         * Output: [1,3,5,6,2,4]
         *                      1
         *                    / | \
         *                   3  2  4
         *                  / \
         *                 5   6
         */
        List<Integer> expected = List.of(1, 3, 5, 6, 2, 4);
        Node root = new Node(1, List.of(new Node(3, List.of(new Node(5, List.of()), new Node(6, List.of()))),
                new Node(2, List.of()), new Node(4, List.of())));
        List<Integer> actualRecursive = preorderRecursive(root);
        assertEquals(expected.size(), actualRecursive.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actualRecursive.get(i));
        }
        List<Integer> actualIterative = preorderIterative(root);
        assertEquals(expected.size(), actualIterative.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actualIterative.get(i));
        }
    }

    private static class Node {
        public int val;
        public List<Node> children;

        public Node(int _val, List<Node> _children) {
            val = _val;
            children = _children;
        }
    }
}
