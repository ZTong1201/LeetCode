import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AllPossibleFullBinaryTrees {

    /**
     * Given an integer n, return a list of all possible full binary trees with n nodes. Each node of each tree in the
     * answer must have Node.val == 0.
     * <p>
     * Each element of the answer is the root node of one possible tree. You may return the final list of trees in any order.
     * <p>
     * A full binary tree is a binary tree where each node has exactly 0 or 2 children.
     * <p>
     * Constraints:
     * <p>
     * 1 <= n <= 20
     * <p>
     * Approach: Recursion + Memorization
     * First notice that we can only build a fully binary tree with an odd number of nodes. This would help us to prune the
     * search space. Basically, we just need to run DFS to get the list of fully binary trees for both the left and the
     * right sides. For example, if we have N nodes, then there will be (N - 1) nodes in the left and right subtrees. We can
     * enumerate all the possible combinations, e.g. (1, N - 2), (2, N - 3) etc. to build new trees. While running the DFS,
     * we can use a hash map to keep track of all possible full trees for each number of nodes. This will avoid duplicate
     * tree construction, and we can obtain all the trees in O(1) runtime. The base case will be when the number of nodes
     * is 1, we will return a standalone root node with value 0.
     * <p>
     * Time: O(n^2 * 2^n) basically, for a given number n, we will have about (n/2) odd numbers, and hence we will have
     * O(n^2) combinations of number of nodes in left and right trees. The number of full binary trees given n nodes will
     * be given by the Catalan number, which is approximately bounded by O(2^n). Hence, in total we have O(n^2 * 2^n)
     * Space: O(2^n) we need to store all the full binary trees constructions
     */
    private List<TreeNode>[] fullTrees;

    public List<TreeNode> allPossibleFBT(int n) {
        // use array of lists as a hash map to keep track of all full binary trees for each n
        fullTrees = new ArrayList[n + 1];
        // build all full binary trees for n nodes
        return buildTrees(n);
    }

    private List<TreeNode> buildTrees(int nodesLeft) {
        // if we have constructed all full binary trees for the current number of nodes, return it
        if (fullTrees[nodesLeft] != null) return fullTrees[nodesLeft];
        // otherwise, we build all full binary trees
        List<TreeNode> res = new ArrayList<>();
        // base case, if there is only one node left, construct the single node
        if (nodesLeft == 1) {
            res.add(new TreeNode(0));
        } else if ((nodesLeft % 2) != 0) {
            // otherwise, we can only build full trees when the number of nodes is odd
            for (int nodesInLeftTree = 1; nodesInLeftTree <= nodesLeft; nodesInLeftTree++) {
                // try to build trees in the left tree with i - 1 nodes
                List<TreeNode> leftTrees = buildTrees(nodesInLeftTree - 1);
                // similarly, build trees in the right tree with n - 1 - (i - 1) = n - i nodes
                // why always minus 1? because we need one node for the root node
                List<TreeNode> rightTrees = buildTrees(nodesLeft - nodesInLeftTree);

                // build trees
                for (TreeNode left : leftTrees) {
                    for (TreeNode right : rightTrees) {
                        TreeNode root = new TreeNode(0);
                        root.left = left;
                        root.right = right;
                        res.add(root);
                    }
                }
            }
        }
        // memorize the result to avoid duplicate calculation
        fullTrees[nodesLeft] = res;
        return res;
    }

    @Test
    public void allPossibleFBTTest() {
        /**
         * Example 1:
         * Input: n = 7
         * Output: [[0,0,0,null,null,0,0,null,null,0,0],
         * [0,0,0,null,null,0,0,0,0],
         * [0,0,0,0,0,0,0],
         * [0,0,0,0,0,null,null,null,null,0,0],
         * [0,0,0,0,0,null,null,0,0]]
         */
        List<TreeNode> expected1 = List.of(
                new TreeNode(0, new TreeNode(0), new TreeNode(0, new TreeNode(0), new TreeNode(0, new TreeNode(0), new TreeNode(0)))),
                new TreeNode(0, new TreeNode(0), new TreeNode(0, new TreeNode(0, new TreeNode(0), new TreeNode(0)), new TreeNode(0))),
                new TreeNode(0, new TreeNode(0, new TreeNode(0), new TreeNode(0)), new TreeNode(0, new TreeNode(0), new TreeNode(0))),
                new TreeNode(0, new TreeNode(0, new TreeNode(0), new TreeNode(0, new TreeNode(0), new TreeNode(0))), new TreeNode(0)),
                new TreeNode(0, new TreeNode(0, new TreeNode(0, new TreeNode(0), new TreeNode(0)), new TreeNode(0)), new TreeNode(0)));
        List<TreeNode> actual1 = allPossibleFBT(7);
        assertEquals(expected1.size(), actual1.size());
        for (int i = 0; i < expected1.size(); i++) {
            assertTrue(isSameTree(expected1.get(i), actual1.get(i)));
        }
        /**
         * Example 2:
         * Input: n = 3
         * Output: [[0,0,0]]
         */
        List<TreeNode> expected2 = List.of(new TreeNode(0, new TreeNode(0), new TreeNode(0)));
        List<TreeNode> actual2 = allPossibleFBT(3);
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

        public TreeNode(int x) {
            this.val = x;
        }

        public TreeNode(int x, TreeNode left, TreeNode right) {
            this.val = x;
            this.left = left;
            this.right = right;
        }
    }
}
