import org.junit.Test;
import static org.junit.Assert.*;

public class balancedBinaryTree {

    /**
     * Given a binary tree, determine if it is height-balanced.
     *
     * For this problem, a height-balanced binary tree is defined as:
     *
     * a binary tree in which the depth of the two subtrees of every node never differ by more than 1
     *
     *
     * Use Recursion and a helper function
     * Time: O(N).
     * Space: O(N) for a completely unbalanced tree, O(logN) for a balanced tree.
     */
    public boolean isBalanced(TreeNode root) {
        if(root == null) return true;
        if(Math.abs(maxDepth(root.left) - maxDepth(root.right)) > 1) return false;
        return isBalanced(root.left) && isBalanced(root.right);
    }

    private int maxDepth(TreeNode root) {
        if(root == null) return 0;
        return Math.max(maxDepth(root.left), maxDepth(root.right)) + 1;
    }


    @Test
    public void balancedTreeTest() {
        TreeNode testTree1 = new TreeNode(3);
        testTree1.left = new TreeNode(9);
        testTree1.right = new TreeNode(20);
        testTree1.right.left = new TreeNode(15);
        testTree1.right.right = new TreeNode(7);
        assertTrue(isBalanced(testTree1));
        TreeNode testTree2 = new TreeNode(1);
        testTree2.left = new TreeNode(2);
        testTree2.right = new TreeNode(2);
        testTree2.left.left = new TreeNode(3);
        testTree2.left.right = new TreeNode(3);
        testTree2.left.left.left = new TreeNode(4);
        testTree2.left.left.right = new TreeNode(4);
        assertFalse(isBalanced(testTree2));
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
