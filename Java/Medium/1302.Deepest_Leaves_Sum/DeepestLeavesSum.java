import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;

import static org.junit.Assert.assertEquals;

public class DeepestLeavesSum {

    /**
     * Given the root of a binary tree, return the sum of values of its deepest leaves.
     * <p>
     * Constraints:
     * <p>
     * The number of nodes in the tree is in the range [1, 104].
     * 1 <= Node.val <= 100
     * <p>
     * Approach 1: BFS (level order traversal)
     * <p>
     * Time: O(n)
     * Space: O(n) if the tree is balanced
     */
    public int deepestLeavesSumBFS(TreeNode root) {
        int res = 0;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            int levelSum = 0, size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode curr = queue.poll();
                // since it's guaranteed there is at least one node in the tree,
                // and we never add null nodes into the queue
                // hence no null check is needed here
                levelSum += curr.val;
                if (curr.left != null) queue.add(curr.left);
                if (curr.right != null) queue.add(curr.right);
            }
            // update the final to current level sum
            res = levelSum;
        }
        return res;
    }

    /**
     * Approach 2: DFS - recursion
     * We basically traverse the tree twice, computing the maximum depth in the first traversal and find the leaf node
     * whose depth is the max depth and add it to the final result.
     * <p>
     * Time: O(n) each traversal will require O(n) time
     * Space: O(H) where H is the height of the tree
     */
    private int res;

    public int deepestLeavesSumDFS(TreeNode root) {
        res = 0;
        int maxDepth = depth(root);
        dfs(root, 1, maxDepth);
        return res;
    }

    private void dfs(TreeNode root, int currDepth, int maxDepth) {
        if (root == null) return;
        // add the deepest leaf node value to the final result
        if (currDepth == maxDepth) res += root.val;
        // traverse the left and right subtrees to find the deepest leaves nodes
        dfs(root.left, currDepth + 1, maxDepth);
        dfs(root.right, currDepth + 1, maxDepth);
    }

    private int depth(TreeNode root) {
        if (root == null) return 0;
        return Math.max(depth(root.left), depth(root.right)) + 1;
    }

    @Test
    public void deepestLeavesSumTest() {
        /**
         * Example 1:
         * Input: root = [1,2,3,4,5,null,6,7,null,null,null,null,8]
         * Output: 15
         *                             1
         *                           /   \
         *                          2     3
         *                        /   \    \
         *                       4    5     6
         *                      /            \
         *                     7              8
         */
        TreeNode root1 = new TreeNode(1);
        root1.left = new TreeNode(2);
        root1.right = new TreeNode(3);
        root1.left.left = new TreeNode(4);
        root1.left.right = new TreeNode(5);
        root1.right.right = new TreeNode(6);
        root1.left.left.left = new TreeNode(7);
        root1.right.right.right = new TreeNode(8);
        assertEquals(15, deepestLeavesSumBFS(root1));
        assertEquals(15, deepestLeavesSumDFS(root1));
        /**
         * Example 2:
         * Input: root = [6,7,8,2,7,1,3,9,null,1,4,null,null,null,5]
         * Output: 19
         *                              6
         *                            /   \
         *                           7     8
         *                         /  \   /  \
         *                        2   7  1    3
         *                       /   / \       \
         *                      9   1   4       5
         */
        TreeNode root2 = new TreeNode(6);
        root2.left = new TreeNode(7);
        root2.right = new TreeNode(8);
        root2.left.left = new TreeNode(2);
        root2.left.right = new TreeNode(7);
        root2.right.left = new TreeNode(1);
        root2.right.right = new TreeNode(3);
        root2.left.left.left = new TreeNode(9);
        root2.left.right.left = new TreeNode(1);
        root2.left.right.right = new TreeNode(4);
        root2.right.right.right = new TreeNode(5);
        assertEquals(19, deepestLeavesSumBFS(root2));
        assertEquals(19, deepestLeavesSumDFS(root2));
    }

    private static class TreeNode {
        private int val;
        private TreeNode left;
        private TreeNode right;

        public TreeNode(int x) {
            this.val = x;
        }
    }
}
