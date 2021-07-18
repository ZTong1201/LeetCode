import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class minDifference {

    /**
     * Given the root of a Binary Search Tree (BST), return the minimum absolute difference between the
     * values of any two different nodes in the tree.
     * <p>
     * Note: This question is the same as 783: https://leetcode.com/problems/minimum-distance-between-bst-nodes/
     * <p>
     * Essence: inorder traversing a BST will return a sorted array, the minimum absolute difference would've been gotten
     * from two adjacent numbers.
     * <p>
     * Approach 1: Write into a sorted list
     * <p>
     * Time: O(n)
     * Space: O(n)
     */
    private List<Integer> values;

    public int getMinimumDifferenceWriteToList(TreeNode root) {
        int res = Integer.MAX_VALUE;
        values = new ArrayList<>();
        inorderAndWriteToList(root);
        for (int i = 0; i < values.size() - 1; i++) {
            res = Math.min(res, values.get(i + 1) - values.get(i));
        }
        return res;
    }

    private void inorderAndWriteToList(TreeNode root) {
        if (root == null) return;
        inorderAndWriteToList(root.left);
        values.add(root.val);
        inorderAndWriteToList(root.right);
    }

    /**
     * Approach 2: Get minimum on the fly
     * It's only necessary to store the previous value while visiting a current node and update minimum & previous values simultaneously.
     * <p>
     * Time: O(n)
     * Space: O(n) recursion call stack
     */
    private int res;
    private Integer prev;

    public int getMinimumDifferenceOnTheFly(TreeNode root) {
        res = Integer.MAX_VALUE;
        prev = null;
        inorderAndUpdateMinimum(root);
        return res;
    }

    private void inorderAndUpdateMinimum(TreeNode root) {
        if (root == null) return;
        inorderAndUpdateMinimum(root.left);
        // update the minimum difference on the fly
        if (prev != null) res = Math.min(res, root.val - prev);
        // also update the previous value for next recursive call
        prev = root.val;
        inorderAndUpdateMinimum(root.right);
    }

    @Test
    public void getMinimumDifferenceTest() {
        /**
         * Example 1:
         *                1
         *               / \
         *              0  48
         *                /  \
         *               12  49
         * Input: root = [1,0,48,null,null,12,49]
         * Output: 1
         */
        TreeNode tree = new TreeNode(1, new TreeNode(0), new TreeNode(48, new TreeNode(12), new TreeNode(49)));
        assertEquals(1, getMinimumDifferenceWriteToList(tree));
        assertEquals(1, getMinimumDifferenceOnTheFly(tree));
    }


    private static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            this.val = x;
        }

        TreeNode(int x, TreeNode left, TreeNode right) {
            this.val = x;
            this.left = left;
            this.right = right;
        }
    }
}
