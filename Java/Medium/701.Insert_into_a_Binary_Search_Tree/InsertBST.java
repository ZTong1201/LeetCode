import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InsertBST {

    /**
     * You are given the root node of a binary search tree (BST) and a value to insert into the tree. Return the root
     * node of the BST after the insertion. It is guaranteed that the new value does not exist in the original BST.
     * <p>
     * Notice that there may exist multiple valid ways for the insertion, as long as the tree remains a BST after insertion.
     * You can return any of them.
     * <p>
     * Constraints:
     * <p>
     * The number of nodes in the tree will be in the range [0, 104].
     * -108 <= Node.val <= 108
     * All the values Node.val are unique.
     * -108 <= val <= 108
     * It's guaranteed that val does not exist in the original BST.
     * <p>
     * Approach 1: Recursion
     * <p>
     * Time: O(H), O(logn) on average, O(n) in the worst case
     * Space: O(H)
     */
    public TreeNode insertIntoBSTRecursive(TreeNode root, int val) {
        if (root == null) return new TreeNode(val);
        if (root.val > val) root.left = insertIntoBSTRecursive(root.left, val);
        else root.right = insertIntoBSTRecursive(root.right, val);
        return root;
    }


    /**
     * Approach 2: Iteration
     * <p>
     * Time: O(H)
     * Space: O(1)
     */
    public TreeNode insertIntoBSTIterative(TreeNode root, int val) {
        // since the root node will always be returned
        // create a pointer to traverse the tree
        TreeNode node = root;
        while (node != null) {
            // insert into the left subtree
            if (node.val > val) {
                if (node.left == null) {
                    // insert directly
                    node.left = new TreeNode(val);
                    return root;
                } else node = node.left; // or keep traversing until the correct position
            } else {
                if (node.right == null) {
                    node.right = new TreeNode(val);
                    return root;
                } else node = node.right;
            }
        }
        // edge case, the tree itself is null, construct a new BST
        return new TreeNode(val);
    }

    @Test
    public void insertIntoBSTRecursiveTest() {
        /**
         * Example:
         * Input: root = [4,2,7,1,3], val = 5
         * Output: [4,2,7,1,3,5]
         *                 4
         *                / \
         *               2   7
         *              / \
         *             1  3
         * after insertion =>
         *                 4
         *                / \
         *               2   7
         *              / \ /
         *             1  3 5
         */
        TreeNode tree = new TreeNode(4);
        tree.left = new TreeNode(2);
        tree.right = new TreeNode(7);
        tree.left.left = new TreeNode(1);
        tree.left.right = new TreeNode(3);
        TreeNode actual = insertIntoBSTRecursive(tree, 5);
        assertEquals(5, tree.right.left.val);
    }

    @Test
    public void insertIntoBSTIterativeTest() {
        /**
         * Example:
         * Input: root = [4,2,7,1,3], val = 5
         * Output: [4,2,7,1,3,5]
         *                 4
         *                / \
         *               2   7
         *              / \
         *             1  3
         * after insertion =>
         *                 4
         *                / \
         *               2   7
         *              / \ /
         *             1  3 5
         */
        TreeNode tree = new TreeNode(4);
        tree.left = new TreeNode(2);
        tree.right = new TreeNode(7);
        tree.left.left = new TreeNode(1);
        tree.left.right = new TreeNode(3);
        TreeNode actual = insertIntoBSTIterative(tree, 5);
        assertEquals(5, tree.right.left.val);
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
