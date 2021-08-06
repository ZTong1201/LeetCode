import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SubtreeOfAnotherTree {

    /**
     * Given the roots of two binary trees root and subRoot, return true if there is a subtree of root with the same
     * structure and node values of subRoot and false otherwise.
     * <p>
     * A subtree of a binary tree is a tree that consists of a node in tree and all of this node's descendants.
     * The tree could also be considered as a subtree of itself.
     * <p>
     * Constraints:
     * <p>
     * The number of nodes in the root tree is in the range [1, 2000].
     * The number of nodes in the subRoot tree is in the range [1, 1000].
     * -10^4 <= root.val <= 10^4
     * -10^4 <= subRoot.val <= 10^4
     * <p>
     * Approach: DFS
     * <p>
     * Time: O(m * n) in the worst case, at each node of tree root, we need to traverse n more nodes to make sure whether
     * subRoot is a sub-tree of root.
     * Space: O(n)
     */
    public boolean isSubtree(TreeNode root, TreeNode subRoot) {
        // base case
        if (root == null) return subRoot == null;
        // check if the tree is a subtree of itself
        if (isSameTree(root, subRoot)) return true;
        // otherwise, check whether the subRoot is a sub-tree of its left or right child
        return isSubtree(root.left, subRoot) || isSubtree(root.right, subRoot);
    }

    private boolean isSameTree(TreeNode p, TreeNode q) {
        if (p == null && q == null) return true;
        if (p == null || q == null) return false;
        if (p.val != q.val) return false;
        return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
    }

    @Test
    public void isSubtreeTest() {
        /**
         * Example 1:
         * Input: root = [3,4,5,1,2], subRoot = [4,1,2]
         * Output: true
         * root:                3
         *                     / \
         *                    4   5
         *                   / \
         *                  1   2
         * subRoot:             4
         *                     / \
         *                    1   2
         */
        TreeNode root1 = new TreeNode(3);
        root1.left = new TreeNode(4);
        root1.right = new TreeNode(5);
        root1.left.left = new TreeNode(1);
        root1.left.right = new TreeNode(2);
        TreeNode subRoot1 = new TreeNode(4);
        subRoot1.left = new TreeNode(1);
        subRoot1.right = new TreeNode(2);
        assertTrue(isSubtree(root1, subRoot1));
        /**
         * Example 2:
         * Input: root = [3,4,5,1,2,null,null,null,null,0], subRoot = [4,1,2]
         * Output: false
         * root:                   3
         *                        / \
         *                       4   5
         *                      / \
         *                     1   2
         *                        /
         *                       0
         * subRoot:               4
         *                       / \
         *                      1   2
         */
        TreeNode root2 = new TreeNode(3);
        root2.left = new TreeNode(4);
        root2.right = new TreeNode(5);
        root2.left.left = new TreeNode(1);
        root2.left.right = new TreeNode(2);
        root2.left.right.left = new TreeNode(0);
        TreeNode subRoot2 = new TreeNode(4);
        subRoot2.left = new TreeNode(1);
        subRoot2.right = new TreeNode(2);
        assertFalse(isSubtree(root2, subRoot2));
    }

    private static class TreeNode {
        private int val;
        private TreeNode left;
        private TreeNode right;

        TreeNode(int x) {
            this.val = x;
        }
    }
}
