import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UniqueBSTII {

    /**
     * Given an integer n, return all the structurally unique BST's (binary search trees), which has exactly n nodes of
     * unique values from 1 to n. Return the answer in any order.
     * <p>
     * Constraints:
     * <p>
     * 1 <= n <= 8
     * <p>
     * Approach: Recursion
     * Basically, we will enumerate all the possible solutions by making every num i from [1, n] as the root value. In order
     * to keep the BST structure, if the root value is i, then we know [1, i - 1] must be placed in its left subtree, and
     * [i + 1, n] must be placed in the right subtree. By taking advantage of the recursion, when working on root value i,
     * we could assume all possible solutions have been found for root value in [1, i - 1]. Therefore, we can make all
     * possible combinations between left & right subtree structures and assign them to the current root value to generate
     * a new BST and append it to the final list.
     * <p>
     * Time:
     * Space:
     */
    public List<TreeNode> generateTrees(int n) {
        return buildTrees(1, n);
    }

    private List<TreeNode> buildTrees(int start, int end) {
        List<TreeNode> res = new ArrayList<>();
        // base case, if there is no element in the range, we cannot construct any trees (an empty tree)
        // add null into the list
        if (start > end) {
            res.add(null);
            return res;
        }

        for (int i = start; i <= end; i++) {
            // get all the structures to form BST from [start, i - 1] as its left subtree
            List<TreeNode> leftTrees = buildTrees(start, i - 1);
            // get all the structures to form BST from [i + 1, end] as its right subtree
            List<TreeNode> rightTrees = buildTrees(i + 1, end);

            for (TreeNode left : leftTrees) {
                for (TreeNode right : rightTrees) {
                    // add both left & right subtrees to the root node to build a brand new BST
                    TreeNode root = new TreeNode(i);
                    root.left = left;
                    root.right = right;
                    res.add(root);
                }
            }
        }
        return res;
    }

    @Test
    public void generateTreesTest() {
        /**
         * Example 1:
         * Input: n = 3
         * Output: [[1,null,2,null,3],[1,null,3,2],[2,1,3],[3,1,null,null,2],[3,2,null,1]]
         */
        List<TreeNode> expected1 = List.of(new TreeNode(1, null, new TreeNode(2, null, new TreeNode(3))),
                new TreeNode(1, null, new TreeNode(3, new TreeNode(2), null)),
                new TreeNode(2, new TreeNode(1), new TreeNode(3)),
                new TreeNode(3, new TreeNode(1, null, new TreeNode(2)), null),
                new TreeNode(3, new TreeNode(2, new TreeNode(1), null), null));
        List<TreeNode> actual1 = generateTrees(3);
        assertEquals(expected1.size(), actual1.size());
        for (int i = 0; i < expected1.size(); i++) {
            assertTrue(isSameTree(expected1.get(i), actual1.get(i)));
        }
        /**
         * Example 2:
         * Input: n = 1
         * Output: [[1]]
         */
        List<TreeNode> expected2 = List.of(new TreeNode(1));
        List<TreeNode> actual2 = generateTrees(1);
        assertEquals(expected2.size(), actual2.size());
        for (int i = 0; i < expected2.size(); i++) {
            assertTrue(isSameTree(expected2.get(i), actual2.get(i)));
        }
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

        TreeNode(int x, TreeNode left, TreeNode right) {
            this.val = x;
            this.left = left;
            this.right = right;
        }
    }
}
