import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class zigzagLevelOrderTraversal {


    /**
     * Given a binary tree, return the zigzag level order traversal of its nodes' values.
     * (ie, from left to right, then right to left for the next level and alternate between).
     *
     * We can do it iteratively, simply assign a value to record current level, if current level is an odd number,
     * we add values from right to left. Otherwise, we add values from left to right.
     * Very similar to leetcode 102.
     *
     * Time: O(N), we traverse all the nodes once
     * Space: O(N), we at most store N/2 nodes (the bottom level) in the queue
     */
    public List<List<Integer>> zigzagLevelOrderIterative(TreeNode root) {
        List<List<Integer>> res = new LinkedList<>();
        if(root == null) return res;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        int level = 0;
        while(!queue.isEmpty()) {
            int length = queue.size();
            List<Integer> levelList = new LinkedList<>();
            Deque<Integer> levelDeque = new ArrayDeque<>();
            for(int i = 0; i < length; i++) {
                TreeNode currNode = queue.poll();
                if(currNode.left != null) queue.add(currNode.left);
                if(currNode.right != null) queue.add(currNode.right);
                if(level % 2 == 0) levelDeque.addLast(currNode.val);
                else levelDeque.addFirst(currNode.val);
            }
            while(!levelDeque.isEmpty()) levelList.add(levelDeque.removeFirst());
            res.add(levelList);
            level += 1;
        }
        return res;
    }

    /**
     * Similarly, we can convert the whole process into a recursion. The trick is we can cast the original list as
     * a Linked List. By doing so, we can implement addFirst in O(1) time
     *
     * Time: O(N)
     * Space: O(N)
     */
    private List<List<Integer>> res;

    private void helper(TreeNode root, int level) {
        if(root == null) return;

        if(res.size() == level) res.add(new LinkedList<>());

        // if level is an even number, add values in original way
        if(level % 2 == 0) res.get(level).add(root.val);
        // if level is an odd number, cast level list as a LinkedList, and add values from the front
        else ((LinkedList) res.get(level)).addFirst(root.val);

        if(root.left != null) helper(root.left, level + 1);
        if(root.right != null) helper(root.right, level + 1);
    }

    public List<List<Integer>> zigzagLevelOrderRecursive(TreeNode root) {
        res = new LinkedList<>();
        helper(root, 0);
        return res;
    }

    @Test
    public void zigzagLevelOrderIterativeTest() {
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
         *   [20,9],
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
        expected1.add(new LinkedList<>(Arrays.asList(20, 9)));
        expected1.add(new LinkedList<>(Arrays.asList(15, 7)));
        List<List<Integer>> actual1 = zigzagLevelOrderIterative(tree1);
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
         *   [20,9],
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
        expected2.add(new LinkedList<>(Arrays.asList(20, 9)));
        expected2.add(new LinkedList<>(Arrays.asList(2, 15, 7)));
        expected2.add(new LinkedList<>(Arrays.asList(6)));
        List<List<Integer>> actual2 = zigzagLevelOrderIterative(tree2);
        assertEquals(expected2, actual2);
    }

    @Test
    public void zigzagLevelOrderRecursiveTest() {
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
         *   [20,9],
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
        expected1.add(new LinkedList<>(Arrays.asList(20, 9)));
        expected1.add(new LinkedList<>(Arrays.asList(15, 7)));
        List<List<Integer>> actual1 = zigzagLevelOrderRecursive(tree1);
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
         *   [20,9],
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
        expected2.add(new LinkedList<>(Arrays.asList(20, 9)));
        expected2.add(new LinkedList<>(Arrays.asList(2, 15, 7)));
        expected2.add(new LinkedList<>(Arrays.asList(6)));
        List<List<Integer>> actual2 = zigzagLevelOrderRecursive(tree2);
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
