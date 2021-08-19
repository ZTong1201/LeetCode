import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class IncreasingOrderSearchTree {

    private TreeNode ptr;

    public TreeNode increasingBSTInorder(TreeNode root) {
        /**
         * Given the root of a binary search tree, rearrange the tree in in-order so that the leftmost node in the tree is now
         * the root of the tree, and every node has no left child and only one right child.
         * <p>
         * Constraints:
         * <p>
         * The number of nodes in the given tree will be in the range [1, 100].
         * 0 <= Node.val <= 1000
         * <p>
         * Approach 1: Inorder + Relinking
         * While inorder traversing the BST, we can assign current node to the original dummy node, then set its left child
         * to null.
         *
         * Time: O(n)
         * Space: O(n)
         */
        TreeNode dummy = new TreeNode();
        ptr = dummy;
        inorder(root);
        return dummy.right;
    }

    private void inorder(TreeNode root) {
        if (root == null) return;
        inorder(root.left);
        root.left = null;
        ptr.right = root;
        ptr = ptr.right;
        inorder(root.right);
    }

    /**
     * Approach 2: Right Rotation
     * Basically, we can create a dummy whose right child is the original BST. Then we keep searching the right child as
     * if it's been flattened already. If a given node has a left child, then we do a right rotation by flipping its left
     * child as its parent.
     * <p>
     * Time: O(N)
     * Space: O(1)
     */
    public TreeNode increasingBSTRightRotation(TreeNode root) {
        TreeNode dummy = new TreeNode();
        // link the original tree as a right child of the dummy node
        dummy.right = root;
        // assign pointers for traversal
        TreeNode ptr = dummy, pseudoRoot = dummy.right;

        while (pseudoRoot != null) {
            // if the current root node has a left child
            // rotate its left child up
            if (pseudoRoot.left != null) {
                TreeNode oldRoot = pseudoRoot;
                pseudoRoot = pseudoRoot.left;

                // right rotate
                oldRoot.left = pseudoRoot.right;
                pseudoRoot.right = oldRoot;
                // now pseudoRoot (the previous left child) becomes the new root node
                // link it to the right of the dummy node (since rotation is not complete yet)
                ptr.right = pseudoRoot;
            } else {
                // otherwise, right rotation is done, the tree structure has been flattened
                // update the head of the dummy node - newly rotated nodes would've been assigned to its right child
                ptr = pseudoRoot;
                // and skip the node
                pseudoRoot = pseudoRoot.right;
            }
        }
        return dummy.right;
    }

    @Test
    public void increasingBSTRightRotationTest() {
        /**
         * Example 1:
         * Input: root = [5,3,6,2,4,null,8,1,null,null,null,7,9]
         * Output: [1,null,2,null,3,null,4,null,5,null,6,null,7,null,8,null,9]
         *                             5
         *                           /   \
         *                          3     6
         *                         / \     \
         *                        2  4      8
         *                       /         / \
         *                      1         7   9
         * =>
         *    1
         *     \
         *      2
         *       \
         *        3
         *         \
         *          4
         *           \
         *            5
         *             \
         *              6
         *               \
         *                7
         *                 \
         *                  8
         *                   \
         *                    9
         */
        TreeNode tree = new TreeNode(5);
        tree.left = new TreeNode(3);
        tree.right = new TreeNode(6);
        tree.left.left = new TreeNode(2);
        tree.left.right = new TreeNode(4);
        tree.right.right = new TreeNode(8);
        tree.left.left.left = new TreeNode(1);
        tree.right.right.left = new TreeNode(7);
        tree.right.right.right = new TreeNode(9);
        TreeNode actual = increasingBSTRightRotation(tree);
        TreeNode expected = new TreeNode(1);
        expected.right = new TreeNode(2);
        expected.right.right = new TreeNode(3);
        expected.right.right.right = new TreeNode(4);
        expected.right.right.right.right = new TreeNode(5);
        expected.right.right.right.right.right = new TreeNode(6);
        expected.right.right.right.right.right.right = new TreeNode(7);
        expected.right.right.right.right.right.right.right = new TreeNode(8);
        expected.right.right.right.right.right.right.right.right = new TreeNode(9);
        assertTrue(isSameTree(expected, actual));
    }

    @Test
    public void increasingBSTInorderTest() {
        /**
         * Example 1:
         * Input: root = [5,3,6,2,4,null,8,1,null,null,null,7,9]
         * Output: [1,null,2,null,3,null,4,null,5,null,6,null,7,null,8,null,9]
         *                             5
         *                           /   \
         *                          3     6
         *                         / \     \
         *                        2  4      8
         *                       /         / \
         *                      1         7   9
         * =>
         *    1
         *     \
         *      2
         *       \
         *        3
         *         \
         *          4
         *           \
         *            5
         *             \
         *              6
         *               \
         *                7
         *                 \
         *                  8
         *                   \
         *                    9
         */
        TreeNode tree = new TreeNode(5);
        tree.left = new TreeNode(3);
        tree.right = new TreeNode(6);
        tree.left.left = new TreeNode(2);
        tree.left.right = new TreeNode(4);
        tree.right.right = new TreeNode(8);
        tree.left.left.left = new TreeNode(1);
        tree.right.right.left = new TreeNode(7);
        tree.right.right.right = new TreeNode(9);
        TreeNode actual = increasingBSTInorder(tree);
        TreeNode expected = new TreeNode(1);
        expected.right = new TreeNode(2);
        expected.right.right = new TreeNode(3);
        expected.right.right.right = new TreeNode(4);
        expected.right.right.right.right = new TreeNode(5);
        expected.right.right.right.right.right = new TreeNode(6);
        expected.right.right.right.right.right.right = new TreeNode(7);
        expected.right.right.right.right.right.right.right = new TreeNode(8);
        expected.right.right.right.right.right.right.right.right = new TreeNode(9);
        assertTrue(isSameTree(expected, actual));
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

        TreeNode() {

        }

        TreeNode(int x) {
            this.val = x;
        }
    }
}
