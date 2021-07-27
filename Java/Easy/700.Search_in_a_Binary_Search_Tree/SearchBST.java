import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SearchBST {

    /**
     * You are given the root of a binary search tree (BST) and an integer val.
     * <p>
     * Find the node in the BST that the node's value equals val and return the subtree rooted with that node.
     * If such a node does not exist, return null.
     * <p>
     * Constraints:
     * <p>
     * The number of nodes in the tree is in the range [1, 5000].
     * 1 <= Node.val <= 107
     * root is a binary search tree.
     * 1 <= val <= 107
     * <p>
     * Approach 1: Recursion
     * <p>
     * Time: O(H) H is the height of the BST. O(logn) on average and O(n) in the worst case
     * Space: O(H) for the recursion call stack
     */
    public TreeNode searchBSTRecursive(TreeNode root, int val) {
        if (root == null || root.val == val) return root;
        return root.val > val ? searchBSTRecursive(root.left, val) : searchBSTRecursive(root.right, val);
    }

    /**
     * Approach 2: Iteration
     * <p>
     * Time: O(H)
     * Space: O(1)
     */
    public TreeNode searchBSTIterative(TreeNode root, int val) {
        while (root != null && root.val != val) {
            root = root.val > val ? root.left : root.right;
        }
        return root;
    }

    @Test
    public void searchBSTTest() {
        /**
         * Example 1:
         * Input: root = [4,2,7,1,3], val = 2
         * Output: [2,1,3]
         *                 4
         *                / \
         *               2   7
         *              / \
         *             1   3
         */
        TreeNode tree = new TreeNode(4);
        tree.left = new TreeNode(2);
        tree.right = new TreeNode(7);
        tree.left.left = new TreeNode(1);
        tree.left.right = new TreeNode(3);
        TreeNode expected = tree.left;
        TreeNode actualRecursive = searchBSTRecursive(tree, 2);
        TreeNode actualIterative = searchBSTIterative(tree, 2);
        assertEquals(expected, actualRecursive);
        assertEquals(expected, actualIterative);
        /**
         * Example 2:
         * Input: root = [4,2,7,1,3], val = 5
         * Output: []
         */
        assertNull(searchBSTRecursive(tree, 5));
        assertNull(searchBSTIterative(tree, 5));
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
