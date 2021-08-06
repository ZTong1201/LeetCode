import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;

import static org.junit.Assert.assertEquals;

public class MinimumDepth {

    /**
     * Given a binary tree, find its minimum depth.
     * <p>
     * The minimum depth is the number of nodes along the shortest path from the root node down to the nearest leaf node.
     * <p>
     * Note: A leaf is a node with no children.
     * <p>
     * Constraints:
     * <p>
     * The number of nodes in the tree is in the range [0, 10^5].
     * -1000 <= Node.val <= 1000
     * <p>
     * Approach 1: BFS (level order traversal)
     * Time: O(N)
     * Space: O(N) for a balanced tree
     */

    public int minDepthBFS(TreeNode root) {
        int res = 0;
        if (root == null) return res;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            // get the size of queue to visit current level
            int size = queue.size();
            // hit a new level to be visited - increment the depth
            res++;
            // level order traversal
            for (int i = 0; i < size; i++) {
                TreeNode curr = queue.poll();
                if (curr != null) {
                    // early termination if we reach a leaf node
                    if (curr.left == null && curr.right == null) return res;
                    // otherwise, add left & right children into the queue
                    queue.add(curr.left);
                    queue.add(curr.right);
                }
            }
        }
        // this return statement will never be reached
        return -1;
    }

    /**
     * Approach 2: Depth-first search - recursion
     * <p>
     * Time: O(N)
     * Space: O(N) for a completely unbalanced tree, O(logN) for a balanced tree;
     */
    public int minDepthDFS(TreeNode root) {
        if (root == null) return 0;
        if (root.left == null) return minDepthDFS(root.right) + 1;
        if (root.right == null) return minDepthDFS(root.left) + 1;
        return Math.min(minDepthDFS(root.left), minDepthDFS(root.right)) + 1;
    }

    @Test
    public void minDepthTestIterative() {
        /**
         * Example 1:
         * Input: root = [3,9,20,null,null,15,7]
         * Output: 2
         *                3
         *               / \
         *              9   20
         *             / \
         *            15  7
         */
        TreeNode testTree1 = new TreeNode(3);
        testTree1.left = new TreeNode(9);
        testTree1.right = new TreeNode(20);
        testTree1.right.left = new TreeNode(15);
        testTree1.right.right = new TreeNode(7);
        assertEquals(2, minDepthBFS(testTree1));
        assertEquals(2, minDepthDFS(testTree1));
        /**
         * Input: root = [1,2]
         * Output: 2
         *              1
         *             /
         *            2
         */
        TreeNode testTree2 = new TreeNode(1);
        testTree2.left = new TreeNode(2);
        assertEquals(2, minDepthBFS(testTree2));
        assertEquals(2, minDepthDFS(testTree2));
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
