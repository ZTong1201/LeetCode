import org.junit.Test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import static org.junit.Assert.assertTrue;

public class pathSum {

    /**
     * Given a binary tree and a sum, determine if the tree has a root-to-leaf path such that
     * adding up all the values along the path equals the given sum.
     * <p>
     * Note: A leaf is a node with no children.
     * <p>
     * <p>
     * Time: O(N) since we traverse all the nodes once
     * Space: call stack costs O(N) for a completely unbalanced tree, O(logN) for a balanced tree;
     * We also need a set to store all the path sum (which equals to the number of leaves in the tree)
     * O(1) for a completely imbalanced tree, and O(N) for a balanced tree since we need to store N/2 leaves
     * at the bottom level.
     */
    private Set<Integer> pathSumSet;
    
    public boolean hasPathSum1(TreeNode root, int sum) {
        pathSumSet = new HashSet<>();
        allPathSum(root, 0);
        return pathSumSet.contains(sum);
    }

    private void allPathSum(TreeNode root, int sum) {
        if (root == null) return;
        if (root.left == null && root.right == null) {
            sum += root.val;
            pathSumSet.add(sum);
        } else {
            allPathSum(root.left, sum + root.val);
            allPathSum(root.right, sum + root.val);
        }
    }

    /**
     * Actually we can return true as long as we find the target value in the tree
     * <p>
     * Time: still be O(N) since in the worst case we need to traverse all the nodes
     * Space: call stack costs O(N) for a completely unbalanced tree, O(logN) for a balanced tree;
     */
    public boolean hasPathSum2(TreeNode root, int targetSum) {
        if (root == null) return false;
        // if it's a leaf node - check whether subtracting current root value can make target sum 0
        if (root.left == null && root.right == null) return (targetSum == root.val);
        // check paths in both left & right subtree
        return hasPathSum2(root.left, targetSum - root.val) || hasPathSum2(root.right, targetSum - root.val);
    }

    /**
     * We can also implement this Depth-first search in a iterative way
     * <p>
     * Time: O(N)
     * Space: O(N) for a completely unbalanced tree, O(logN) for a balanced tree
     */
    public boolean hasPathSum2Iterative(TreeNode root, int sum) {
        if (root == null) return false;
        LinkedList<TreeNode> nodeStack = new LinkedList<>();
        LinkedList<Integer> sumStack = new LinkedList<>();
        nodeStack.add(root);
        sumStack.add(sum - root.val);
        while (!nodeStack.isEmpty()) {
            TreeNode currNode = nodeStack.pollLast();
            int currSum = sumStack.pollLast();
            if (currNode.left == null && currNode.right == null && currSum == 0) return true;
            if (currNode.left != null) {
                nodeStack.add(currNode.left);
                sumStack.add(currSum - currNode.left.val);
            }
            if (currNode.right != null) {
                nodeStack.add(currNode.right);
                sumStack.add(currSum - currNode.right.val);
            }
        }
        return false;
    }


    @Test
    public void hasPathSum1Test() {
        /**
         * Example:
         * Input:
         *       5
         *      / \
         *     4   8
         *    /   / \
         *   11  13  4
         *  /  \      \
         * 7    2      1
         * sum = 22
         * Output: true
         */
        TreeNode tree1 = new TreeNode(5);
        tree1.left = new TreeNode(4);
        tree1.right = new TreeNode(8);
        tree1.left.left = new TreeNode(11);
        tree1.left.left.left = new TreeNode(7);
        tree1.left.left.right = new TreeNode(2);
        tree1.right.left = new TreeNode(13);
        tree1.right.right = new TreeNode(4);
        tree1.right.right.right = new TreeNode(1);
        assertTrue(hasPathSum1(tree1, 22));
    }

    @Test
    public void hasPathSum2Test() {
        /**
         * Example:
         * Input:
         *       5
         *      / \
         *     4   8
         *    /   / \
         *   11  13  4
         *  /  \      \
         * 7    2      1
         * sum = 22
         * Output: true
         */
        TreeNode tree1 = new TreeNode(5);
        tree1.left = new TreeNode(4);
        tree1.right = new TreeNode(8);
        tree1.left.left = new TreeNode(11);
        tree1.left.left.left = new TreeNode(7);
        tree1.left.left.right = new TreeNode(2);
        tree1.right.left = new TreeNode(13);
        tree1.right.right = new TreeNode(4);
        tree1.right.right.right = new TreeNode(1);
        assertTrue(hasPathSum2(tree1, 22));
    }

    @Test
    public void hasPathSum2IterativeTest() {
        /**
         * Example:
         * Input:
         *       5
         *      / \
         *     4   8
         *    /   / \
         *   11  13  4
         *  /  \      \
         * 7    2      1
         * sum = 22
         * Output: true
         */
        TreeNode tree1 = new TreeNode(5);
        tree1.left = new TreeNode(4);
        tree1.right = new TreeNode(8);
        tree1.left.left = new TreeNode(11);
        tree1.left.left.left = new TreeNode(7);
        tree1.left.left.right = new TreeNode(2);
        tree1.right.left = new TreeNode(13);
        tree1.right.right = new TreeNode(4);
        tree1.right.right.right = new TreeNode(1);
        assertTrue(hasPathSum2Iterative(tree1, 22));
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
