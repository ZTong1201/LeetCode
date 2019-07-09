import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class levelOrderTraversal {

    /**
     * Given a binary tree, return the level order traversal of its nodes' values. (ie, from left to right, level by level).
     *
     * We can do it using iteration. For level order traversal, what we need is a queue to store all the nodes in that level,
     * add all the values into a level list. Before we move to the next level, we add it to the final list
     *
     * Time: O(N), we need to traverse all the nodes
     * Space: O(N), we need to store all the node values for output
     */
    public List<List<Integer>> levelOrderIterative(TreeNode root) {
        List<List<Integer>> res = new LinkedList<>();
        if(root == null) return res;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while(!queue.isEmpty()) {
            int length = queue.size();
            List<Integer> level = new LinkedList<>();
            for(int i = 0; i < length; i++) {
                TreeNode currNode = queue.poll();
                level.add(currNode.val);
                if(currNode.left != null) queue.add(currNode.left);
                if(currNode.right != null) queue.add(currNode.right);
            }
            res.add(level);
        }
        return res;
    }

    /**
     * We can also do it recursively. By using a helper function, we can trace which level we are actually in.
     * If the size of the result list equals to the level we record, we must add a new level in the result list.
     * Otherwise, we just add the node value to current level list
     *
     * Time: O(N), we need to traverse all the nodes
     * Space: O(N), we need to store all the node values for output
     */
    private List<List<Integer>> res;

    private void helper(TreeNode root, int level) {
        // if we exceed current level, we need a new level list
        if(res.size() == level) res.add(new LinkedList<>());
        // After that, we simply add node values into current level
        res.get(level).add(root.val);

        //check left and right child recursively
        if(root.left != null) helper(root.left, level + 1);
        if(root.right != null) helper(root.right, level + 1);

    }

    public List<List<Integer>> levelOrderRecursive(TreeNode root) {
        res = new LinkedList<>();
        if(root == null) return res;
        helper(root, 0);
        return res;
    }

    @Test
    public void levelOrderIterativeTest() {
        /**
         * Example 1:
         * Input:
         *     3
         *    / \
         *   9  20
         *     /  \
         *    15   7
         * Output:
         * [
         *   [3],
         *   [9,20],
         *   [15,7]
         * ]
         */
        TreeNode tree1 = new TreeNode(3);
        tree1.left = new TreeNode(9);
        tree1.right = new TreeNode(20);
        tree1.right.left = new TreeNode(15);
        tree1.right.right = new TreeNode(7);
        List<List<Integer>> expected1 = new LinkedList<>();
        expected1.add(new LinkedList<>(Arrays.asList(3)));
        expected1.add(new LinkedList<>(Arrays.asList(9, 20)));
        expected1.add(new LinkedList<>(Arrays.asList(15, 7)));
        List<List<Integer>> actual1 = levelOrderIterative(tree1);
        assertEquals(expected1, actual1);
        /**
         * Example 2:
         * Input:
         *     3
         *    / \
         *   9  20
         *   \ /  \
         *   2 15  7
         *     /
         *    6
         * Output:
         * [
         *   [3],
         *   [9,20],
         *   [2,15,7],
         *   [6]
         * ]
         */
        TreeNode tree2 = new TreeNode(3);
        tree2.left = new TreeNode(9);
        tree2.right = new TreeNode(20);
        tree2.right.left = new TreeNode(15);
        tree2.right.right = new TreeNode(7);
        tree2.left.right = new TreeNode(2);
        tree2.right.left.left = new TreeNode(6);
        List<List<Integer>> expected2 = new LinkedList<>();
        expected2.add(new LinkedList<>(Arrays.asList(3)));
        expected2.add(new LinkedList<>(Arrays.asList(9, 20)));
        expected2.add(new LinkedList<>(Arrays.asList(2, 15, 7)));
        expected2.add(new LinkedList<>(Arrays.asList(6)));
        List<List<Integer>> actual2 = levelOrderIterative(tree2);
        assertEquals(expected2, actual2);
    }

    @Test
    public void levelOrderRecursiveTest() {
        /**
         * Example 1:
         * Input:
         *     3
         *    / \
         *   9  20
         *     /  \
         *    15   7
         * Output:
         * [
         *   [3],
         *   [9,20],
         *   [15,7]
         * ]
         */
        TreeNode tree1 = new TreeNode(3);
        tree1.left = new TreeNode(9);
        tree1.right = new TreeNode(20);
        tree1.right.left = new TreeNode(15);
        tree1.right.right = new TreeNode(7);
        List<List<Integer>> expected1 = new LinkedList<>();
        expected1.add(new LinkedList<>(Arrays.asList(3)));
        expected1.add(new LinkedList<>(Arrays.asList(9, 20)));
        expected1.add(new LinkedList<>(Arrays.asList(15, 7)));
        List<List<Integer>> actual1 = levelOrderRecursive(tree1);
        assertEquals(expected1, actual1);
        /**
         * Example 2:
         * Input:
         *     3
         *    / \
         *   9  20
         *   \ /  \
         *   2 15  7
         *     /
         *    6
         * Output:
         * [
         *   [3],
         *   [9,20],
         *   [2,15,7],
         *   [6]
         * ]
         */
        TreeNode tree2 = new TreeNode(3);
        tree2.left = new TreeNode(9);
        tree2.right = new TreeNode(20);
        tree2.right.left = new TreeNode(15);
        tree2.right.right = new TreeNode(7);
        tree2.left.right = new TreeNode(2);
        tree2.right.left.left = new TreeNode(6);
        List<List<Integer>> expected2 = new LinkedList<>();
        expected2.add(new LinkedList<>(Arrays.asList(3)));
        expected2.add(new LinkedList<>(Arrays.asList(9, 20)));
        expected2.add(new LinkedList<>(Arrays.asList(2, 15, 7)));
        expected2.add(new LinkedList<>(Arrays.asList(6)));
        List<List<Integer>> actual2 = levelOrderRecursive(tree2);
        assertEquals(expected2, actual2);
    }

    private class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            this.val = x;
        }
    }
}
