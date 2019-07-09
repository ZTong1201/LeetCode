import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class MinimumDepth {

    /**
     * Given a binary tree, find its minimum depth.
     *
     * The minimum depth is the number of nodes along the shortest path from the root node down to the nearest leaf node.
     *
     * Note: A leaf is a node with no children.
     *
     * Use Iteration (Breadth-first search)
     * Time: O(N);
     * Space: O(N) for a balanced tree
     */

    public int minDepthIterative(TreeNode root) {
        if(root == null) return 0;
        Queue<TreeNode> nodes = new LinkedList<>();
        Queue<Integer> depths = new LinkedList<>();
        nodes.add(root);
        depths.add(1);
        int minDepth = Integer.MAX_VALUE;
        while(!nodes.isEmpty()) {
            TreeNode currNode = nodes.remove();
            int currDepth = depths.remove();
            if(currNode.left == null && currNode.right == null) {
                if(currDepth < minDepth) {
                    minDepth = currDepth;
                    break;
                }
            }
            if(currNode.left != null) {
                nodes.add(currNode.left);
                depths.add(currDepth + 1);
            }
            if(currNode.right != null) {
                nodes.add(currNode.right);
                depths.add(currDepth + 1);
            }
        }
        return minDepth;
    }

    /**
     * Can solve this problem using recursion (Depth-first search)
     *
     * Time: O(N)
     * Space: O(N) for a completely unbalanced tree, O(logN) for a balanced tree;
     */
    public int minDepthRecursive(TreeNode root) {
        if(root == null) return 0;
        if(root.left == null) return minDepthRecursive(root.right) + 1;
        if(root.right == null) return minDepthRecursive(root.left) + 1;
        return Math.min(minDepthRecursive(root.left), minDepthRecursive(root.right)) + 1;
    }

    @Test
    public void minDepthTestIterative() {
        TreeNode testTree1 = new TreeNode(3);
        testTree1.left = new TreeNode(9);
        testTree1.right = new TreeNode(20);
        testTree1.right.left = new TreeNode(15);
        testTree1.right.right = new TreeNode(7);
        assertEquals(2, minDepthIterative(testTree1));
        TreeNode testTree2 = new TreeNode(1);
        testTree2.left = new TreeNode(2);
        assertEquals(2, minDepthIterative(testTree2));
    }

    @Test
    public void minDepthTestRecursive() {
        TreeNode testTree1 = new TreeNode(3);
        testTree1.left = new TreeNode(9);
        testTree1.right = new TreeNode(20);
        testTree1.right.left = new TreeNode(15);
        testTree1.right.right = new TreeNode(7);
        assertEquals(2, minDepthRecursive(testTree1));
        TreeNode testTree2 = new TreeNode(1);
        testTree2.left = new TreeNode(2);
        assertEquals(2, minDepthRecursive(testTree2));
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
