import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FindLeavesOfBinaryTree {

    /**
     * Given the root of a binary tree, collect a tree's nodes as if you were doing this:
     * <p>
     * Collect all the leaf nodes.
     * Remove all the leaf nodes.
     * Repeat until the tree is empty.
     * <p>
     * Constraints:
     * <p>
     * The number of nodes in the tree is in the range [1, 100].
     * -100 <= Node.val <= 100
     * <p>
     * Approach 1: DFS + node removal
     * Basically, we can mimic the behavior to recursively delete the leaf nodes until the tree is empty. In order to safely
     * delete the leaf nodes without interfering with the current iteration, we should consider a preorder traversal to
     * add the leaf node into the list the first time it was hit. For example, give tree [1, 2, 3], we should add [2, 3]
     * into the list and the tree will become [1] after it. Now node 1 will become a leaf node too, but we should add it
     * in the next iteration.
     * <p>
     * Time: O(N^2) after each iteration, we remove the leaf nodes from the tree. If the tree is balanced, then we remove
     * approximately N / 2 nodes from the tree and the tree height is about logn. However, in the worst case, the tree is
     * extremely unbalanced, we can only remove 1 node at a time and the height is n, so we need O(n) time for each iteration,
     * the total iteration will be n, so overall runtime will be O(n^2)
     * Space: O(n)
     */
    public List<List<Integer>> findLeavesWithRemoval(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        // keep adding leaf nodes into the list until the tree is empty
        while (root != null) {
            List<Integer> level = new ArrayList<>();
            // we need to find and remove all leaf nodes of current tree and add them into the list
            // the tree needs to be updated too
            root = findAndRemoveLeavesPreorder(root, level);
            res.add(new ArrayList<>(level));
        }
        return res;
    }

    private TreeNode findAndRemoveLeavesPreorder(TreeNode root, List<Integer> level) {
        // base case
        if (root == null) return null;
        // if it's a leaf node - add it to the current level list
        // also need to kill that node
        if (root.left == null && root.right == null) {
            level.add(root.val);
            return null;
        }
        // need to assign pruned children to each root node
        root.left = findAndRemoveLeavesPreorder(root.left, level);
        root.right = findAndRemoveLeavesPreorder(root.right, level);
        return root;
    }

    /**
     * Approach 2: DFS + height calculation
     * If we're not allowed to modify the input tree, then the previous approach won't be acceptable. Basically, we're
     * grouping nodes by their heights. The leaf node will have height 0, and their parents will have height 1. Therefore,
     * once the original leaf nodes were removed from the tree, their parents' height will become 0. We can compute the
     * height of each node on the fly, and add node values to the correct index based on their heights. One pitfall is that
     * we need to first add an empty list at the desired index/height as a placeholder before adding node values.
     * <p>
     * Time: O(n) we only traverse the tree once
     * Space: O(n)
     */
    public List<List<Integer>> findLeavesHeightCalculation(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        getHeightForEachNode(root, res);
        return res;
    }

    private int getHeightForEachNode(TreeNode root, List<List<Integer>> res) {
        // base case - null nodes have height -1 hence we won't add them into the list
        if (root == null) return -1;
        // recursively calculate the height of each node
        int height = 1 + Math.max(getHeightForEachNode(root.left, res), getHeightForEachNode(root.right, res));

        // if we reach a larger height - we need add an empty list as a placeholder
        if (height == res.size()) {
            res.add(new ArrayList<>());
        }

        // add the node value into the correct index/height group
        res.get(height).add(root.val);
        return height;
    }

    @Test
    public void findLeavesWithRemovalTest() {
        /**
         * Example 1:
         * Input: root = [1,2,3,4,5]
         * Output: [[4,5,3],[2],[1]]
         * Explanation:
         * [[3,5,4],[2],[1]] and [[3,4,5],[2],[1]] are also considered correct answers since per each level it does not
         * matter the order on which elements are returned.
         *                    1
         *                   / \
         *                  2   3
         *                 / \
         *                4   5
         *  =>
         *                    1
         *                   /
         *                  2
         *  =>
         *                   1
         *  =>
         *                  null
         */
        TreeNode root1 = new TreeNode(1);
        root1.left = new TreeNode(2);
        root1.right = new TreeNode(3);
        root1.left.left = new TreeNode(4);
        root1.left.right = new TreeNode(5);
        List<List<Integer>> expected1 = List.of(List.of(4, 5, 3), List.of(2), List.of(1));
        List<List<Integer>> actual1 = findLeavesWithRemoval(root1);
        assertEquals(expected1.size(), actual1.size());
        for (int i = 0; i < expected1.size(); i++) {
            assertEquals(expected1.get(i), actual1.get(i));
        }
        /**
         * Example 2:
         * Input: root = [1]
         * Output: [[1]]
         */
        TreeNode root2 = new TreeNode(1);
        List<List<Integer>> expected2 = List.of(List.of(1));
        List<List<Integer>> actual2 = findLeavesWithRemoval(root2);
        assertEquals(expected2.size(), actual2.size());
        for (int i = 0; i < expected2.size(); i++) {
            assertEquals(expected2.get(i), actual2.get(i));
        }
    }

    @Test
    public void findLeavesHeightCalculationTest() {
        /**
         * Example 1:
         * Input: root = [1,2,3,4,5]
         * Output: [[4,5,3],[2],[1]]
         * Explanation:
         * [[3,5,4],[2],[1]] and [[3,4,5],[2],[1]] are also considered correct answers since per each level it does not
         * matter the order on which elements are returned.
         *                    1
         *                   / \
         *                  2   3
         *                 / \
         *                4   5
         *  =>
         *                    1
         *                   /
         *                  2
         *  =>
         *                   1
         *  =>
         *                  null
         */
        TreeNode root1 = new TreeNode(1);
        root1.left = new TreeNode(2);
        root1.right = new TreeNode(3);
        root1.left.left = new TreeNode(4);
        root1.left.right = new TreeNode(5);
        List<List<Integer>> expected1 = List.of(List.of(4, 5, 3), List.of(2), List.of(1));
        List<List<Integer>> actual1 = findLeavesHeightCalculation(root1);
        assertEquals(expected1.size(), actual1.size());
        for (int i = 0; i < expected1.size(); i++) {
            assertEquals(expected1.get(i), actual1.get(i));
        }
        /**
         * Example 2:
         * Input: root = [1]
         * Output: [[1]]
         */
        TreeNode root2 = new TreeNode(1);
        List<List<Integer>> expected2 = List.of(List.of(1));
        List<List<Integer>> actual2 = findLeavesHeightCalculation(root2);
        assertEquals(expected2.size(), actual2.size());
        for (int i = 0; i < expected2.size(); i++) {
            assertEquals(expected2.get(i), actual2.get(i));
        }
    }

    private static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        public TreeNode(int x) {
            this.val = x;
        }
    }
}
