import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TrimBST {

    /**
     * Given the root of a binary search tree and the lowest and highest boundaries as low and high, trim the tree so that
     * all its elements lies in [low, high]. Trimming the tree should not change the relative structure of the elements that
     * will remain in the tree (i.e., any node's descendant should remain a descendant). It can be proven that there is a
     * unique answer.
     * <p>
     * Return the root of the trimmed binary search tree. Note that the root may change depending on the given bounds.
     * <p>
     * Constraints:
     * <p>
     * The number of nodes in the tree in the range [1, 10^4].
     * 0 <= Node.val <= 10^4
     * The value of each node in the tree is unique.
     * root is guaranteed to be a valid binary search tree.
     * 0 <= low <= high <= 10^4
     * <p>
     * Approach: DFS
     * Assume trim(node) will trim the left & right subtree at the node, we can trim the entire tree recursively. We
     * can further prune the search space by taking advantage of the BST property.
     * 1. If root.val > high, which means the entire right subtree will be trimmed, and it's only necessary to return
     * trimmed left subtree - which is trim(root.left)
     * 2. If root.val < low, which means the entire left subtree will be trimmed, return trim(root.right)
     * 3. Otherwise, we need to trim both left & right subtree and assign trimmed tree to that node
     * <p>
     * Time: O(N)
     * Space: O(H)
     */
    public TreeNode trimBST(TreeNode root, int low, int high) {
        // base case
        if (root == null) return null;
        // if current node is larger than high - the entire right subtree will be trimmed
        // only need to keep trimming the left subtree
        if (root.val > high) return trimBST(root.left, low, high);
        // similarly, the entire left subtree is trimmed if current node is smaller than low
        if (root.val < low) return trimBST(root.right, low, high);
        // otherwise - trim both subtrees and assign trimmed trees to the current node
        root.left = trimBST(root.left, low, high);
        root.right = trimBST(root.right, low, high);
        return root;
    }

    @Test
    public void trimBSTTest() {
        /**
         * Example 1:
         * Input: root = [1,0,2], low = 1, high = 2
         * Output: [1,null,2]
         *                      1
         *                     / \
         *                    0   2
         * =>
         *                      1
         *                       \
         *                        2
         */
        TreeNode root1 = new TreeNode(1);
        root1.left = new TreeNode(0);
        root1.right = new TreeNode(2);
        TreeNode expected1 = new TreeNode(1);
        expected1.right = new TreeNode(2);
        TreeNode actual1 = trimBST(root1, 1, 2);
        assertTrue(isSameTree(expected1, actual1));
        /**
         * Example 2:
         * Input: root = [3,0,4,null,2,null,null,1], low = 1, high = 3
         * Output: [3,2,null,1]
         *                        3
         *                       / \
         *                      0   4
         *                       \
         *                        2
         *                       /
         *                      1
         * =>
         *                        3
         *                       /
         *                      2
         *                     /
         *                    1
         */
        TreeNode root2 = new TreeNode(3);
        root2.left = new TreeNode(0);
        root2.right = new TreeNode(4);
        root2.left.right = new TreeNode(2);
        root2.left.right.left = new TreeNode(1);
        TreeNode expected2 = new TreeNode(3);
        expected2.left = new TreeNode(2);
        expected2.left.left = new TreeNode(1);
        TreeNode actual2 = trimBST(root2, 1, 3);
        assertTrue(isSameTree(expected2, actual2));
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
