import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class HouseRobberIII {

    /**
     * The thief has found himself a new place for his thievery again. There is only one entrance to this area, called root.
     * <p>
     * Besides the root, each house has one and only one parent house. After a tour, the smart thief realized that all houses
     * in this place form a binary tree. It will automatically contact the police if two directly-linked houses were broken
     * into on the same night.
     * <p>
     * Given the root of the binary tree, return the maximum amount of money the thief can rob without alerting the police.
     * <p>
     * Constraints:
     * <p>
     * The number of nodes in the tree is in the range [1, 10^4].
     * 0 <= Node.val <= 10^4
     * <p>
     * Approach: Recursion + memorization
     * Basically, at a given node, we just need to decide whether we're going to rob the current node or not.
     * How to make the optimal decision? If we know the maximum money we can get without robbing the children and the maximum
     * money we can get with robbing the children. Then we just simply need to decide which one is greater
     * rob current + not rob the children & rob the children + not rob current
     * <p>
     * Therefore, we can use a hash map to keep track of the max result without robbing the node, and our recursion funtion
     * will return the max result at each node. We need to handle the edge case when the node is null, we return 0 in that
     * scenario.
     * <p>
     * Time: O(n) we need to visit every node to find the result
     * Space: O(n)
     */
    private Map<TreeNode, Integer> notRobAtNode;

    public int rob(TreeNode root) {
        notRobAtNode = new HashMap<>();
        notRobAtNode.put(null, 0);
        return robMaxMoney(root);
    }

    private int robMaxMoney(TreeNode root) {
        // base case
        if (root == null) return 0;

        // recursively compute the maximum money we can rob from both children
        int left = robMaxMoney(root.left);
        int right = robMaxMoney(root.right);

        // keep track of the maximum money we can rob without robbing the current node
        int notRobResult = left + right;
        notRobAtNode.put(root, notRobResult);

        // the maximum money will be one of the following
        // rob current + not robbing the children
        // not rob current + the max result we can rob from both children regardless of the children are robbed or not
        return Math.max(root.val + notRobAtNode.get(root.left) + notRobAtNode.get(root.right), notRobResult);
    }

    @Test
    public void robTest() {
        /**
         * Example 1:
         * Input: root = [3,2,3,null,3,null,1]
         * Output: 7
         * Explanation: Maximum amount of money the thief can rob = 3 + 3 + 1 = 7.
         *                         3
         *                        / \
         *                       2   3
         *                        \   \
         *                         3   1
         */
        TreeNode root1 = new TreeNode(3);
        root1.left = new TreeNode(2);
        root1.right = new TreeNode(3);
        root1.left.right = new TreeNode(3);
        root1.right.right = new TreeNode(1);
        assertEquals(7, rob(root1));
        /**
         * Example 2:
         * Input: root = [3,4,5,1,3,null,1]
         * Output: 9
         * Explanation: Maximum amount of money the thief can rob = 4 + 5 = 9.
         *                          3
         *                         / \
         *                        4   5
         *                       / \   \
         *                      1   3   1
         */
        TreeNode root2 = new TreeNode(3);
        root2.left = new TreeNode(4);
        root2.right = new TreeNode(5);
        root2.left.left = new TreeNode(1);
        root2.left.right = new TreeNode(3);
        root2.right.right = new TreeNode(1);
        assertEquals(9, rob(root2));
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
