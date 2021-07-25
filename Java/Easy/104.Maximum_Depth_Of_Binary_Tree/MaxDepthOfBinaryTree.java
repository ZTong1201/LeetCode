import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;

import static org.junit.Assert.assertEquals;

public class MaxDepthOfBinaryTree {
    /**
     * Given a binary tree, find its maximum depth.
     * <p>
     * The maximum depth is the number of nodes along the longest path
     * from the root node down to the farthest leaf node.
     * <p>
     * Note: A leaf is a node with no children.
     * <p>
     * Solve it in a recursive way.
     * Time: O(n)
     * Space: worst case: O(n), average case: O(logN)
     */
    public int maxDepthRecursive(TreeNode root) {
        if (root == null) return 0;
        return Math.max(maxDepthRecursive(root.left), maxDepthRecursive(root.right)) + 1;
    }


    /**
     * Use iteration to compute max depth (BFS)
     * <p>
     * Time: O(n)
     * Space: O(n)
     */
    public int maxDepthIterative(TreeNode root) {
        int depth = 0;
        if (root == null) return depth;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode curr = queue.poll();
                if (curr.left != null) queue.add(curr.left);
                if (curr.right != null) queue.add(curr.right);
            }
            depth++;
        }
        return depth;
    }

    @Test
    public void testMaxDepthRecursive() {
        TreeNode testTree1 = new TreeNode(3);
        assertEquals(1, maxDepthRecursive(testTree1));
        assertEquals(0, maxDepthRecursive(null));
        TreeNode testTree2 = new TreeNode(3);
        testTree2.left = new TreeNode(9);
        testTree2.right = new TreeNode(20);
        testTree2.right.left = new TreeNode(15);
        testTree2.right.right = new TreeNode(7);
        assertEquals(3, maxDepthRecursive(testTree2));
    }

    @Test
    public void testMaxDepthIterative() {
        TreeNode testTree1 = new TreeNode(3);
        assertEquals(1, maxDepthIterative(testTree1));
        assertEquals(0, maxDepthIterative(null));
        TreeNode testTree2 = new TreeNode(3);
        testTree2.left = new TreeNode(9);
        testTree2.right = new TreeNode(20);
        testTree2.right.left = new TreeNode(15);
        testTree2.right.right = new TreeNode(7);
        assertEquals(3, maxDepthIterative(testTree2));
    }


    private static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }
}
