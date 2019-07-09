import org.junit.Test;
import static org.junit.Assert.*;

public class treeDiameter {


    /**
     * Given a binary tree, you need to compute the length of the diameter of the tree.
     * The diameter of a binary tree is the length of the longest path between any two nodes in a tree.
     * This path may or may not pass through the root.
     *
     *
     * Depth-first Search (DFS)
     * For a given tree node, the diameter (longest path) will be one of these three choices,
     * 1. longest path in the left subtree
     * 2. longest path in the right subtree
     * 3. longest path pass through the root node = longest path in the left subtree + longest path in the right subtree
     *
     * Hence, for each node, we can use DFS to find the depth of its left subtree and right subtree, update the current longest
     * path we seen so far. Finally return that result.
     *
     * Time: O(n) we need to visit each node once
     * Space: O(n) in the worst case. The implicit size of call stack will equal to the height of the tree. For a completely unbalanced
     *       tree, the height is N.
     */
    private int res;

    public int diameterOfBinaryTree(TreeNode root) {
        res = 0;
        depth(root);
        return res;
    }

    private int depth(TreeNode root) {
        if(root == null) return 0;
        int left = depth(root.left);
        int right = depth(root.right);
        res = Math.max(res, left + right);
        return Math.max(left, right) + 1;
    }

    @Test
    public void diameterOfBinaryTreeTest() {
        /**
         * Example 1:
         *           1
         *          / \
         *         2   3
         *        / \
         *       4   5
         * Return 3, which is the length of the path [4,2,1,3] or [5,2,1,3].
         *
         * Note: The length of path between two nodes is represented by the number of edges between them.
         */
        TreeNode tree = new TreeNode(1);
        tree.left = new TreeNode(2);
        tree.right = new TreeNode(3);
        tree.left.left = new TreeNode(4);
        tree.left.right = new TreeNode(5);
        assertEquals(3, diameterOfBinaryTree(tree));
        /**
         * Example 1:
         *           1
         *          / \
         *         2   3
         *        / \
         *       4   5
         *      /     \
         *     6       7
         *      \     /
         *      8    9
         * Return 6, which is the length of the path [8, 6, 4, 2, 5, 7, 9]. the path pass through the root [8, 6, 4, 2, 1, 3]
         * has length 5, which is less than 6.
         */
        TreeNode tree2 = new TreeNode(1);
        tree2.left = new TreeNode(2);
        tree2.right = new TreeNode(3);
        tree2.left.left = new TreeNode(4);
        tree2.left.right = new TreeNode(5);
        tree2.left.left.left = new TreeNode(6);
        tree2.left.right.right = new TreeNode(7);
        tree2.left.left.left.right = new TreeNode(8);
        tree2.left.right.right.left = new TreeNode(9);
        assertEquals(6, diameterOfBinaryTree(tree2));
    }


    private class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            this.val = x;
        }
    }
}
