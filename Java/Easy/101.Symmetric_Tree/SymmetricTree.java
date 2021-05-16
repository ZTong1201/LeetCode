import org.junit.Test;
import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.Queue;

public class SymmetricTree {

    /**
     * Given a binary tree, check whether it is a mirror of itself (ie, symmetric around its center).
     * For example, this binary tree [1,2,2,3,4,4,3] is symmetric
     *
     * Use recursion.
     * Two trees are mirror of reflection if and only if
     * 1. They have the same root value
     * 2. The right subtree of each tree is a mirror reflection of the left subtree of each tree
     *
     * Use recursion (Depth-first search)
     * Time: O(N)
     * Space: O(N) for a completely unbalanced tree, O(logN) for a balanced tree (average case)
     */
    public boolean isSymmetricRecursive(TreeNode root) {
        if(root == null) return true;
        return isMirror(root.left, root.right);
    }

    private boolean isMirror(TreeNode p, TreeNode q) {
        if(p == null && q == null) return true;
        if(p == null || q == null) return false;
        if(p.val != q.val) return false;
        return isMirror(p.left, q.right) && isMirror(p.right, q.left);
    }

    /**
     * Use iteration (Breadth-first search)
     * Time: O(N)
     * Space: O(N) for a balanced tree
     */
    public boolean isSymmetricIterative(TreeNode root) {
        if(root == null) return true;
        Queue<TreeNode> nodes = new LinkedList<>();
        nodes.add(root.left);
        nodes.add(root.right);
        while(!nodes.isEmpty()) {
            TreeNode leftTree = nodes.poll();
            TreeNode rightTree = nodes.poll();
            if(leftTree == null && rightTree == null) continue;
            if(leftTree == null || rightTree == null) return false;
            if(leftTree.val != rightTree.val) return false;
            nodes.add(leftTree.left);
            nodes.add(rightTree.right);
            nodes.add(leftTree.right);
            nodes.add(rightTree.left);
        }
        return true;
    }


    @Test
    public void symmetricTreeTestRecursive() {
        /**
         * true
         *     1
         *    / \
         *   2   2
         *  / \ / \
         * 3  4 4  3
         */
        TreeNode testTree1 = new TreeNode(1);
        testTree1.left = new TreeNode(2);
        testTree1.right = new TreeNode(2);
        testTree1.left.left = new TreeNode(3);
        testTree1.left.right = new TreeNode(4);
        testTree1.right.left = new TreeNode(4);
        testTree1.right.right = new TreeNode(3);
        assertTrue(isSymmetricRecursive(testTree1));
        /**
         * false
         *     1
         *    / \
         *   2   2
         *    \   \
         *    3    3
         */
        TreeNode testTree2 = new TreeNode(1);
        testTree2.left = new TreeNode(2);
        testTree2.right = new TreeNode(2);
        testTree2.left.right = new TreeNode(3);
        testTree2.right.right = new TreeNode(3);
        assertFalse(isSymmetricRecursive(testTree2));
    }

    @Test
    public void symmetricTreeTestIterative() {
        /**
         * true
         *     1
         *    / \
         *   2   2
         *  / \ / \
         * 3  4 4  3
         */
        TreeNode testTree1 = new TreeNode(1);
        testTree1.left = new TreeNode(2);
        testTree1.right = new TreeNode(2);
        testTree1.left.left = new TreeNode(3);
        testTree1.left.right = new TreeNode(4);
        testTree1.right.left = new TreeNode(4);
        testTree1.right.right = new TreeNode(3);
        assertTrue(isSymmetricIterative(testTree1));
        /**
         * false
         *     1
         *    / \
         *   2   2
         *    \   \
         *    3    3
         */
        TreeNode testTree2 = new TreeNode(1);
        testTree2.left = new TreeNode(2);
        testTree2.right = new TreeNode(2);
        testTree2.left.right = new TreeNode(3);
        testTree2.right.right = new TreeNode(3);
        assertFalse(isSymmetricIterative(testTree2));
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
