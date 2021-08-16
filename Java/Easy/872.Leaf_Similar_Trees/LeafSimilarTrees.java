import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LeafSimilarTrees {

    /**
     * Consider all the leaves of a binary tree, from left to right order, the values of those leaves form a leaf value sequence.
     * <p>
     * Two binary trees are considered leaf-similar if their leaf value sequence is the same.
     * <p>
     * Return true if and only if the two given trees with head nodes root1 and root2 are leaf-similar.
     * <p>
     * Constraints:
     * <p>
     * The number of nodes in each tree will be in the range [1, 200].
     * Both of the given trees will have values in the range [0, 200].
     * <p>
     * Approach: DFS
     * Traverse each tree individually and insert only the leaf nodes into the result list, return true if two list are identical
     * <p>
     * Time: O(H1 + H2)
     * Space: O(H1 + H2)
     */
    public boolean leafSimilar(TreeNode root1, TreeNode root2) {
        List<Integer> leaves1 = new ArrayList<>();
        List<Integer> leaves2 = new ArrayList<>();
        findLeaves(root1, leaves1);
        findLeaves(root2, leaves2);
        return leaves1.equals(leaves2);
    }

    private void findLeaves(TreeNode root, List<Integer> res) {
        if (root == null) return;
        if (root.left == null && root.right == null) res.add(root.val);
        findLeaves(root.left, res);
        findLeaves(root.right, res);
    }

    @Test
    public void leafSimilarTest() {
        /**
         * Input: root1 = [3,5,1,6,2,9,8,null,null,7,4], root2 = [3,5,1,6,7,4,2,null,null,null,null,null,null,9,8]
         * Output: true
         * root1:
         *                       3
         *                      / \
         *                     5   1
         *                    / \ / \
         *                   6  2 9  8
         *                     / \
         *                    7   4
         * root2:
         *                        3
         *                       / \
         *                      5   1
         *                     / \ / \
         *                    6  7 4  2
         *                           / \
         *                          9   8
         */
        TreeNode root1 = new TreeNode(3);
        root1.left = new TreeNode(5);
        root1.right = new TreeNode(1);
        root1.left.left = new TreeNode(6);
        root1.left.right = new TreeNode(2);
        root1.right.left = new TreeNode(9);
        root1.right.right = new TreeNode(8);
        root1.left.right.left = new TreeNode(7);
        root1.left.right.right = new TreeNode(4);
        TreeNode root2 = new TreeNode(3);
        root2.left = new TreeNode(5);
        root2.right = new TreeNode(1);
        root2.left.left = new TreeNode(6);
        root2.left.right = new TreeNode(7);
        root2.right.left = new TreeNode(4);
        root2.right.right = new TreeNode(2);
        root2.right.right.left = new TreeNode(9);
        root2.right.right.right = new TreeNode(8);
        assertTrue(leafSimilar(root1, root2));
        /**
         * Example 2:
         * Input: root1 = [1], root2 = [2]
         * Output: false
         */
        assertFalse(leafSimilar(new TreeNode(1), new TreeNode(2)));
        /**
         * Example 3:
         * Input: root1 = [1,2,3], root2 = [1,3,2]
         * Output: false
         */
        TreeNode root3 = new TreeNode(1);
        root3.left = new TreeNode(2);
        root3.right = new TreeNode(3);
        TreeNode root4 = new TreeNode(1);
        root4.left = new TreeNode(3);
        root4.right = new TreeNode(2);
        assertFalse(leafSimilar(root3, root4));
    }

    private static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        public TreeNode(int x) {
            this.val = x;
        }
    }
}
