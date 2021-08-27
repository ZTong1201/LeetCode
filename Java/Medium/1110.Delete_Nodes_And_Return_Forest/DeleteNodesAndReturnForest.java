import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DeleteNodesAndReturnForest {

    /**
     * Given the root of a binary tree, each node in the tree has a distinct value.
     * <p>
     * After deleting all nodes with a value in to_delete, we are left with a forest (a disjoint union of trees).
     * <p>
     * Return the roots of the trees in the remaining forest. You may return the result in any order.
     * <p>
     * Constraints:
     * <p>
     * The number of nodes in the given tree is at most 1000.
     * Each node has a distinct value between 1 and 1000.
     * to_delete.length <= 1000
     * to_delete contains distinct values between 1 and 1000.
     * <p>
     * Approach: Postorder
     * Since the node deletion at a lower height might impact the result at a higher height, we need to execute the node
     * deletion in a postorder way. Once its left and right children are visited (either remain as is or being chopped off some
     * nodes), the current node can be safely deleted (setting it to null in the original tree).
     * Before the current is removed from the children, it will leave some disjoint subtress in the forest (if any). We add
     * its non-null left and right children into the final result. Note that after the whole process is done, we may have to
     * add the pruned tree into the forest if the root value is not being deleted.
     * <p>
     * Time: O(n)
     * Space: O(n + h) h is the height of the tree for recursion call stack
     */
    private List<TreeNode> forest;
    private Set<Integer> nodeValuesToBeDeleted;

    public List<TreeNode> delNodes(TreeNode root, int[] to_delete) {
        nodeValuesToBeDeleted = new HashSet<>();
        forest = new ArrayList<>();
        for (int value : to_delete) {
            nodeValuesToBeDeleted.add(value);
        }
        // return the pruned tree after deleting all nodes
        TreeNode prunedRoot = postorderPrune(root);
        // if the root value is not removed, the pruned tree has been added to the list
        if (root != null) forest.add(prunedRoot);
        return forest;
    }

    private TreeNode postorderPrune(TreeNode root) {
        if (root == null) return null;
        // delete nodes from both subtrees
        root.left = postorderPrune(root.left);
        root.right = postorderPrune(root.right);
        // after both subtrees are pruned - the current node can be safely deleted
        if (nodeValuesToBeDeleted.contains(root.val)) {
            // add non-null child to the forest
            if (root.left != null) forest.add(root.left);
            if (root.right != null) forest.add(root.right);
            // safe deletion by returning null
            return null;
        }
        // otherwise, return the current node as is
        return root;
    }

    @Test
    public void delNodesTest() {
        /**
         * Example 1:
         * Input: root = [1,2,3,4,5,6,7], to_delete = [3,5]
         * Output: [[1,2,null,4],[6],[7]]
         *                         1
         *                        / \
         *                       2   3
         *                      / \ / \
         *                     4  5 6  7
         * =>
         *         1       6       7
         *        /
         *       2
         *      /
         *     4
         */
        TreeNode root1 = new TreeNode(1);
        root1.left = new TreeNode(2);
        root1.right = new TreeNode(3);
        root1.left.left = new TreeNode(4);
        root1.left.right = new TreeNode(5);
        root1.right.left = new TreeNode(6);
        root1.right.right = new TreeNode(7);
        List<TreeNode> expected1 = List.of(new TreeNode(6), new TreeNode(7),
                new TreeNode(1, new TreeNode(2, new TreeNode(4), null), null));
        List<TreeNode> actual1 = delNodes(root1, new int[]{3, 5});
        assertEquals(expected1.size(), actual1.size());
        for (int i = 0; i < expected1.size(); i++) {
            assertTrue(isSameTree(expected1.get(i), actual1.get(i)));
        }
        /**
         * Example 2:
         * Input: root = [1,2,4,null,3], to_delete = [3]
         * Output: [[1,2,4]]
         *                   1
         *                  / \
         *                 2   4
         *                  \
         *                   3
         * =>
         *                   1
         *                  / \
         *                 2   4
         */
        TreeNode root2 = new TreeNode(1);
        root2.left = new TreeNode(2);
        root2.right = new TreeNode(4);
        root2.left.right = new TreeNode(3);
        List<TreeNode> expected2 = List.of(new TreeNode(1, new TreeNode(2), new TreeNode(4)));
        List<TreeNode> actual2 = delNodes(root2, new int[]{3});
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
