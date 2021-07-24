import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static org.junit.Assert.assertArrayEquals;

public class preorder {

    /**
     * Given a binary tree, return the preorder traversal of its nodes' values.
     * Follow up: Recursive solution is trivial, could you do it iteratively?
     * <p>
     * Approach 1: Stack
     * <p>
     * Time: O(N) we need to visit all nodes once
     * Space: O(H), we need a stack to store nodes which may require up to the height of tree space, in the worst case, it will be O(N)
     */
    public List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode curr = stack.pop();
            if (curr != null) {
                res.add(curr.val);
                stack.push(curr.right);
                stack.push(curr.left);
            }
        }
        return res;
    }

    /**
     * Approach 2: Morris Traversal
     * Keep searching the predecessor for each node, and visit each predecessor twice to decide whether to go left or go right.
     * <p>
     * Time: O(N), only the predecessor will be visited twice, other nodes will only be visited once
     * Space: O(1) if the output list doesn't account for space complexity, we only assign current node and the predecessor node during
     * iteration
     */
    public List<Integer> morrisTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        TreeNode node = root;
        while (node != null) {
            if (node.left == null) {
                res.add(node.val);
                node = node.right;
            } else {
                TreeNode predecessor = node.left;
                while (predecessor.right != null && predecessor.right != node) {
                    predecessor = predecessor.right;
                }

                //if the predecessor.right is null, it is the first time we visit the predecessor
                //add current node value to the list, assign the current node to predecessor.right
                //and visit the left child
                if (predecessor.right == null) {
                    res.add(node.val);
                    predecessor.right = node;
                    node = node.left;
                } else {
                    //otherwise, it is the second visit, we should assign predecessor.right to null
                    //and we now done the left part of current node, then visit the right child
                    predecessor.right = null;
                    node = node.right;
                }
            }
        }
        return res;
    }

    @Test
    public void preorderTraversalTest() {
        /**
         * Example:
         * Input: [1,null,2,3]
         *    1
         *     \
         *      2
         *     /
         *    3
         *
         * Output: [1,2,3]
         */
        TreeNode tree = new TreeNode(1);
        tree.right = new TreeNode(2);
        tree.right.left = new TreeNode(3);
        List<Integer> res = preorderTraversal(tree);
        int[] expected = new int[]{1, 2, 3};
        int[] actual = listToArray(res, 3);
        assertArrayEquals(expected, actual);

    }

    @Test
    public void morrisTraversalTest() {
        /**
         * Example:
         * Input: [1,null,2,3]
         *    1
         *     \
         *      2
         *     /
         *    3
         *
         * Output: [1,2,3]
         */
        TreeNode tree = new TreeNode(1);
        tree.right = new TreeNode(2);
        tree.right.left = new TreeNode(3);
        List<Integer> res = morrisTraversal(tree);
        int[] expected = new int[]{1, 2, 3};
        int[] actual = listToArray(res, 3);
        assertArrayEquals(expected, actual);
    }

    private int[] listToArray(List<Integer> aList, int length) {
        int[] res = new int[length];
        for (int i = 0; i < length; i++) {
            res[i] = aList.get(i);
        }
        return res;
    }

    private static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            this.val = x;
        }
    }
}
