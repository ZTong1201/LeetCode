import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UnivaluedBinaryTree {

    /**
     * A binary tree is uni-valued if every node in the tree has the same value.
     * <p>
     * Given the root of a binary tree, return true if the given tree is uni-valued, or false otherwise.
     * <p>
     * Constraints:
     * <p>
     * The number of nodes in the tree is in the range [1, 100].
     * 0 <= Node.val < 100
     * <p>
     * Approach: Recursion
     * Base case: if the tree is empty -> return true;
     * if the tree is not empty, and it has a left child. If the left child value is not equal to the root value -> return false
     * Also check for its right child.
     * The tree is a uni-valued tree if and only if it's left and right children are uni-valued trees
     */
    public boolean isUnivalTree(TreeNode root) {
        // base case
        if (root == null) return true;
        // check the left child
        if (root.left != null && root.left.val != root.val) return false;
        if (root.right != null && root.right.val != root.val) return false;
        return isUnivalTree(root.left) && isUnivalTree(root.right);
    }

    @Test
    public void isUnivalTreeTest() {
        /**
         * Example 1:
         * Input: root = [1,1,1,1,1,null,1]
         * Output: true
         *                   1
         *                  / \
         *                 1   1
         *                / \   \
         *               1  1    1
         */
        TreeNode root1 = new TreeNode(1);
        root1.left = new TreeNode(1);
        root1.right = new TreeNode(1);
        root1.left.left = new TreeNode(1);
        root1.left.right = new TreeNode(1);
        root1.right.right = new TreeNode(1);
        assertTrue(isUnivalTree(root1));
        /**
         * Example 2:
         * Input: root = [2,2,2,5,2]
         * Output: false
         *                   2
         *                  / \
         *                 2   2
         *                / \
         *               5   2
         */
        TreeNode root2 = new TreeNode(2);
        root2.left = new TreeNode(2);
        root2.right = new TreeNode(2);
        root2.left.left = new TreeNode(5);
        root2.left.right = new TreeNode(2);
        assertFalse(isUnivalTree(root2));
    }

    private static class TreeNode {
        private int val;
        private TreeNode left;
        private TreeNode right;

        public TreeNode(int x) {
            this.val = x;
        }
    }
}
