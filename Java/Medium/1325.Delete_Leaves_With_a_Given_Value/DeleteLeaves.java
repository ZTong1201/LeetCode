import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class DeleteLeaves {

    /**
     * Given a binary tree root and an integer target, delete all the leaf nodes with value target.
     * <p>
     * Note that once you delete a leaf node with value target, if it's parent node becomes a leaf node and has the value
     * target, it should also be deleted (you need to continue doing that until you can't).
     * <p>
     * Constraints:
     * <p>
     * 1 <= target <= 1000
     * The given binary tree will have between 1 and 3000 nodes.
     * Each node's value is between [1, 1000].
     * <p>
     * Approach: Postorder + pruning
     * This is a similar question to LeetCode 814: https://leetcode.com/problems/binary-tree-pruning/
     * <p>
     * Time: O(N)
     * Space: O(H)
     */
    public TreeNode removeLeafNodes(TreeNode root, int target) {
        if (root == null) return null;
        root.left = removeLeafNodes(root.left, target);
        root.right = removeLeafNodes(root.right, target);
        if (root.left == null && root.right == null && root.val == target) return null;
        return root;
    }

    @Test
    public void removeLeafNodesTest() {
        /**
         * Example 1:
         * Input: root = [1,2,3,2,null,2,4], target = 2
         * Output: [1,null,3,null,4]
         * Explanation: Leaf nodes in green with value (target = 2) are removed (Picture in left).
         * After removing, new nodes become leaf nodes with value (target = 2) (Picture in center).
         *                                1
         *                               / \
         *                              2   3
         *                             /   /  \
         *                            2   2    4
         * =>
         *                                1
         *                                 \
         *                                  3
         *                                   \
         *                                    4
         */
        TreeNode root1 = new TreeNode(1);
        root1.left = new TreeNode(2);
        root1.right = new TreeNode(3);
        root1.left.left = new TreeNode(2);
        root1.right.left = new TreeNode(2);
        root1.right.right = new TreeNode(4);
        TreeNode expected1 = new TreeNode(1);
        expected1.right = new TreeNode(3);
        expected1.right.right = new TreeNode(4);
        TreeNode actual1 = removeLeafNodes(root1, 2);
        assertTrue(isSameTree(expected1, actual1));
        /**
         * Example 2:
         * Input: root = [1,3,3,3,2], target = 3
         * Output: [1,3,null,null,2]
         *                     1
         *                    / \
         *                   3   3
         *                  / \
         *                 3   2
         * =>
         *                     1
         *                    /
         *                   3
         *                    \
         *                     2
         */
        TreeNode root2 = new TreeNode(1);
        root2.left = new TreeNode(3);
        root2.right = new TreeNode(3);
        root2.left.left = new TreeNode(3);
        root2.left.right = new TreeNode(2);
        TreeNode expected2 = new TreeNode(1);
        expected2.left = new TreeNode(3);
        expected2.left.right = new TreeNode(2);
        TreeNode actual2 = removeLeafNodes(root2, 3);
        assertTrue(isSameTree(expected2, actual2));
        /**
         * Example 3:
         * Input: root = [1,2,null,2,null,2], target = 2
         * Output: [1]
         * Explanation: Leaf nodes in green with value (target = 2) are removed at each step.
         *                         1
         *                        /
         *                       2
         *                      /
         *                     2
         *                    /
         *                   2
         * =>
         *                        1
         */
        TreeNode root3 = new TreeNode(1);
        root3.left = new TreeNode(2);
        root3.left.left = new TreeNode(2);
        root3.left.left.left = new TreeNode(2);
        TreeNode expected3 = new TreeNode(1);
        TreeNode actual3 = removeLeafNodes(root3, 2);
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
