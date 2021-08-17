import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class BinaryTreePruning {

    /**
     * Given the root of a binary tree, return the same tree where every subtree (of the given tree) not containing a 1
     * has been removed.
     * <p>
     * A subtree of a node is node plus every node that is a descendant of node.
     * <p>
     * Constraints:
     * <p>
     * The number of nodes in the tree is in the range [1, 200].
     * Node.val is either 0 or 1.
     * <p>
     * Approach: Postorder + prune
     * Since a deeper pruning might have an effect on an upper level node, postorder traversal is the most suitable approach.
     * At a given node, as long as its left and right children are null (no matter it is actually a leaf node, or it becomes
     * a leaf node after pruning), we set it to null if it's 0. Recursively pruning the left & right subtrees will get the
     * desired result.
     * <p>
     * Time: O(N)
     * Space: O(H)
     */
    public TreeNode pruneTree(TreeNode root) {
        // base case
        if (root == null) return null;
        // using postorder - hence need to traverse to the very end
        root.left = pruneTree(root.left);
        root.right = pruneTree(root.right);
        // after left & right has been pruned already, check whether current node needs to be pruned
        if (root.left == null && root.right == null && root.val == 0) return null;
        return root;
    }

    @Test
    public void pruneTreeTest() {
        /**
         * Example 1:
         * Input: root = [1,null,0,0,1]
         * Output: [1,null,0,null,1]
         * Explanation:
         * Only the red nodes satisfy the property "every subtree not containing a 1".
         * The diagram on the right represents the answer.
         *                   1
         *                    \
         *                     0
         *                    / \
         *                   0   1
         *  =>
         *                   1
         *                    \
         *                     0
         *                      \
         *                       1
         */
        TreeNode root1 = new TreeNode(1);
        root1.right = new TreeNode(0);
        root1.right.left = new TreeNode(0);
        root1.right.right = new TreeNode(1);
        TreeNode expected1 = new TreeNode(1);
        expected1.right = new TreeNode(0);
        expected1.right.right = new TreeNode(1);
        TreeNode actual1 = pruneTree(root1);
        assertTrue(isSameTree(expected1, actual1));
        /**
         * Example 2:
         * Input: root = [1,0,1,0,0,0,1]
         * Output: [1,null,1,null,1]
         *                      1
         *                     / \
         *                    0   1
         *                   / \ / \
         *                  0  0 0  1
         * =>
         *                     1
         *                      \
         *                       1
         *                        \
         *                         1
         */
        TreeNode root2 = new TreeNode(1);
        root2.left = new TreeNode(0);
        root2.right = new TreeNode(1);
        root2.left.left = new TreeNode(0);
        root2.left.right = new TreeNode(0);
        root2.right.left = new TreeNode(0);
        root2.right.right = new TreeNode(1);
        TreeNode expected2 = new TreeNode(1);
        expected2.right = new TreeNode(1);
        expected2.right.right = new TreeNode(1);
        TreeNode actual2 = pruneTree(root2);
        assertTrue(isSameTree(expected2, actual2));
        /**
         * Example 3:
         * Input: root = [1,1,0,1,1,0,1,0]
         * Output: [1,1,0,1,1,null,1]
         *                            1
         *                          /   \
         *                         1     0
         *                        / \   /  \
         *                       1  1  0   1
         *                      /
         *                     0
         * =>
         *                            1
         *                           / \
         *                          1   0
         *                         / \   \
         *                        1  1    1
         */
        TreeNode root3 = new TreeNode(1);
        root3.left = new TreeNode(1);
        root3.right = new TreeNode(0);
        root3.left.left = new TreeNode(1);
        root3.left.right = new TreeNode(1);
        root3.left.left.left = new TreeNode(0);
        root3.right.left = new TreeNode(0);
        root3.right.right = new TreeNode(1);
        TreeNode expected3 = new TreeNode(1);
        expected3.left = new TreeNode(1);
        expected3.right = new TreeNode(0);
        expected3.left.left = new TreeNode(1);
        expected3.left.right = new TreeNode(1);
        expected3.right.right = new TreeNode(1);
        TreeNode actual3 = pruneTree(root3);
        assertTrue(isSameTree(expected3, actual3));
    }

    private boolean isSameTree(TreeNode p, TreeNode q) {
        if (p == null && q == null) return true;
        if (p == null || q == null) return false;
        if (p.val != q.val) return false;
        return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
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
