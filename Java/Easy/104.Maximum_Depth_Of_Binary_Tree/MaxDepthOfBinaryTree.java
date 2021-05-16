import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class MaxDepthOfBinaryTree {
    /**
     * Given a binary tree, find its maximum depth.
     *
     * The maximum depth is the number of nodes along the longest path
     * from the root node down to the farthest leaf node.
     *
     * Note: A leaf is a node with no children.
     *
     * Solve it in a recursive way.
     * Time: O(n)
     * Space: worst case: O(n), average case: O(logN)
     */
    public int maxDepthRecursive(TreeNode root) {
        if(root == null) return 0;
        return Math.max(maxDepthRecursive(root.left), maxDepthRecursive(root.right)) + 1;
    }


    /**
     * Use iteration to compute max depth
     *
     * Time: O(n)
     * Space: O(n)
     */
    public int maxDepthIterative(TreeNode root) {
        if(root == null) return 0;
        Stack<TreeNode> nodes = new Stack<>();
        Stack<Integer> depths = new Stack<>();
        nodes.push(root);
        depths.push(1);
        int depth = 0, curr_depth = 0;
        while(!nodes.isEmpty()) {
            TreeNode currNode = nodes.pop();
            curr_depth = depths.pop();
            if(currNode != null) {
                depth = Math.max(depth, curr_depth);
                nodes.push(currNode.left);
                nodes.push(currNode.right);
                depths.push(curr_depth + 1);
                depths.push(curr_depth + 1);
            }
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
