import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class SplitBST {

    /**
     * Given the root of a binary search tree (BST) and an integer target, split the tree into two subtrees where one subtree
     * has nodes that are all smaller or equal to the target value, while the other subtree has all nodes that are greater
     * than the target value. It Is not necessarily the case that the tree contains a node with the value target.
     * <p>
     * Additionally, most of the structure of the original tree should remain. Formally, for any child c with parent p in the
     * original tree, if they are both in the same subtree after the split, then node c should still have the parent p.
     * <p>
     * Return an array of the two roots of the two subtrees.
     * <p>
     * Constraints:
     * <p>
     * The number of nodes in the tree is in the range [1, 50].
     * 0 <= Node.val, target <= 1000
     * <p>
     * Approach: Recursion
     * Basically, the BST always needs to be split at a given node if that node value <= target. Because all nodes whose
     * value is less than or equal to the target value should be in one single BST. According to the BST property, if the
     * node value <= target, which means that node + the entire left subtree should be <= target. Hence, we can always split
     * at the current node into [node, node.right]. However, there might have been some nodes whose value is also less
     * than or equal to target, we need to recursively split the right subtree. After the split is done, as defined above
     * res[0] will always be the root of a tree whose values are all <= target, but still > node.val. (Why? Because the result
     * is return from node's right subtree). Therefore, we need to assign node's right child to res[0] which is a new BST
     * whose node values are all <= target.
     * On the contrary, if node.val > target, the node + its entire right subtree should be placed in res[1], the left subtree
     * might need to be split. Similar to the above step, now we need to assign res[1] (split from its left subtree) as a
     * left child of current node.
     * The base case would be we cannot split anything if the root node is null, then return [null, null]
     * <p>
     * Time: O(n) in the worst case we might need to visit the entire BST
     * Space: O(H) the recursion call stack will take up to O(H) space
     */
    public TreeNode[] splitBST(TreeNode root, int target) {
        // base case
        if (root == null) return new TreeNode[]{null, null};
        // always need to detach the right subtree from current node
        // if the value is less than or equal to target
        if (root.val <= target) {
            TreeNode[] rightSplit = splitBST(root.right, target);
            // group all nodes whose value is less than or equal to target together
            root.right = rightSplit[0];
            rightSplit[0] = root;
            return rightSplit;
        } else {
            // otherwise, we might need to split the left subtree
            TreeNode[] leftSplit = splitBST(root.left, target);
            // group all nodes whose value is greater than target together
            root.left = leftSplit[1];
            leftSplit[1] = root;
            return leftSplit;
        }
    }

    @Test
    public void splitBSTTest() {
        /**
         * Example 1:
         * Input: root = [4,2,6,1,3,5,7], target = 2
         * Output: [[2,1],[4,3,6,null,null,5,7]]
         *                     4
         *                   /   \
         *                  2     6
         *                 / \   / \
         *                1  3  5   7
         * =>
         *          2                         4
         *         /                         / \
         *        1                         3   6
         *                                     / \
         *                                    5   7
         */
        TreeNode root1 = new TreeNode(4);
        root1.left = new TreeNode(2);
        root1.right = new TreeNode(6);
        root1.left.left = new TreeNode(1);
        root1.left.right = new TreeNode(3);
        root1.right.left = new TreeNode(5);
        root1.right.right = new TreeNode(7);
        TreeNode[] expected1 = new TreeNode[2];
        TreeNode left1 = new TreeNode(2);
        left1.left = new TreeNode(1);
        expected1[0] = left1;
        TreeNode right1 = new TreeNode(4);
        right1.left = new TreeNode(3);
        right1.right = new TreeNode(6);
        right1.right.left = new TreeNode(5);
        right1.right.right = new TreeNode(7);
        expected1[1] = right1;
        TreeNode[] actual1 = splitBST(root1, 2);
        assertTrue(isSameTree(expected1[0], actual1[0]));
        assertTrue(isSameTree(expected1[1], actual1[1]));
        /**
         * Example 2:
         * Input: root = [1], target = 1
         * Output: [[1],[]]
         */
        TreeNode root2 = new TreeNode(1);
        TreeNode[] expected2 = new TreeNode[]{root2, null};
        TreeNode[] actual2 = splitBST(root2, 1);
        assertTrue(isSameTree(expected2[0], actual2[0]));
        assertTrue(isSameTree(expected2[1], actual2[1]));
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

        TreeNode(int x) {
            this.val = x;
        }
    }
}
