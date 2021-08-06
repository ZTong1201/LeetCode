import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class balancedBinaryTree {

    /**
     * Given a binary tree, determine if it is height-balanced.
     * <p>
     * For this problem, a height-balanced binary tree is defined as:
     * <p>
     * a binary tree in which the depth of the two subtrees of every node never differ by more than 1
     * <p>
     * Constraints:
     * <p>
     * The number of nodes in the tree is in the range [0, 5000].
     * -10^4 <= Node.val <= 10^4
     * <p>
     * Approach 1: Top down recursion
     * Time: O(nlogn) at each node p, it requires visiting all its left and right subtree, if the tree is actually height
     * balanced, the height will be O(logn). If in the worst case, the tree degrades to a linked list, we only need to traverse
     * the entire tree at the root node - which takes O(n) time
     * Space: O(n) for a completely unbalanced tree, O(logN) for a balanced tree.
     */
    public boolean isBalancedTopDown(TreeNode root) {
        if (root == null) return true;
        if (Math.abs(maxDepth(root.left) - maxDepth(root.right)) > 1) return false;
        return isBalancedTopDown(root.left) && isBalancedTopDown(root.right);
    }

    private int maxDepth(TreeNode root) {
        if (root == null) return 0;
        return Math.max(maxDepth(root.left), maxDepth(root.right)) + 1;
    }

    /**
     * Approach 2: Bottom up recursion
     * Note that we keep revisiting nodes in order to compute the height at each node. To avoid duplicate computation, we
     * can do the recursion call and start computing the height when it reaches the leaf nodes.
     * <p>
     * Time: O(n) now each node will only be visited once
     * Space: O(n) the recursion call stack requires O(n) if tree is completely unbalanced
     */
    private boolean balanced;

    public boolean isBalancedBottomUp(TreeNode root) {
        this.balanced = true;
        height(root);
        return this.balanced;
    }

    private int height(TreeNode root) {
        // base case
        if (root == null) return 0;
        // keep traversing the subtree until the leaf nodes
        int left = height(root.left);
        int right = height(root.right);
        // if the current node is not height balanced - return false immediately
        if (Math.abs(left - right) > 1) {
            this.balanced = false;
            // this value will not be used - just for early termination
            return -1;
        }
        return Math.max(left, right) + 1;
    }


    @Test
    public void balancedTreeTest() {
        /**
         * Example 1:
         * Input: root = [3,9,20,null,null,15,7]
         * Output: true
         *                3
         *               / \
         *              9  20
         *                /  \
         *               15   7
         */
        TreeNode testTree1 = new TreeNode(3);
        testTree1.left = new TreeNode(9);
        testTree1.right = new TreeNode(20);
        testTree1.right.left = new TreeNode(15);
        testTree1.right.right = new TreeNode(7);
        assertTrue(isBalancedTopDown(testTree1));
        assertTrue(isBalancedBottomUp(testTree1));
        /**
         * Example 2:
         * Input: root = [1,2,2,3,3,null,null,4,4]
         * Output: false
         *                     1
         *                    / \
         *                   2   2
         *                  / \
         *                 3   3
         *                / \
         *               4   4
         */
        TreeNode testTree2 = new TreeNode(1);
        testTree2.left = new TreeNode(2);
        testTree2.right = new TreeNode(2);
        testTree2.left.left = new TreeNode(3);
        testTree2.left.right = new TreeNode(3);
        testTree2.left.left.left = new TreeNode(4);
        testTree2.left.left.right = new TreeNode(4);
        assertFalse(isBalancedTopDown(testTree2));
        assertFalse(isBalancedBottomUp(testTree2));
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
