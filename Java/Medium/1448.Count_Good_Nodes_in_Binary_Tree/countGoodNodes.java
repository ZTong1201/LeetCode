import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class countGoodNodes {

    /**
     * Use DFS (Depth First Search) to traverse the tree, and constantly keep track of the current path maximum.
     */
    private int result = 0;

    public int goodNodes(TreeNode root) {
        traverseTree(root, root.val);
        return result;
    }

    private void traverseTree(TreeNode root, int currentMax) {
        if (root == null) return;
        if (root.val >= currentMax) {
            result++;
            currentMax = root.val;
        }
        traverseTree(root.left, currentMax);
        traverseTree(root.right, currentMax);
    }

    @Test
    public void countGoodNodesTest() {
        /**
         * Input: root = [3,1,4,3,null,1,5]
         * Output: 4
         * Explanation: Nodes in blue are good.
         * Root Node (3) is always a good node.
         * Node 4 -> (3,4) is the maximum value in the path starting from the root.
         * Node 5 -> (3,4,5) is the maximum value in the path
         * Node 3 -> (3,1,3) is the maximum value in the path.
         *               3
         *              / \
         *             1   4
         *            /   / \
         *           3   1   4
         */
        TreeNode tree = new TreeNode(3, new TreeNode(1), new TreeNode(4));
        tree.left.left = new TreeNode(3);
        tree.right.left = new TreeNode(1);
        tree.right.right = new TreeNode(4);
        assertEquals(4, goodNodes(tree));
    }

    private class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {
        }

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }
}
