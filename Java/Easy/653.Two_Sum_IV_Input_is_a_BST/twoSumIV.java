import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class twoSumIV {


    /**
     * Given a Binary Search Tree and a target number, return true if there exist two elements in the BST
     * such that their sum is equal to the given target.
     *
     * Approach 1: DFS + HashSet
     * Just like in two sum, at each node, we check whether target - node.val has appeared before, if that is true, we found a pair
     * which sum to the target. We need a hash set to store values seen so far.
     *
     * Time: O(N), in the worst case, we have to traverse all the nodes in the tree.
     * Space: O(N), we need a hash set to store values seen so far
     */
    public boolean findTargetDFS(TreeNode root, int k) {
        Set<Integer> nodeSeen = new HashSet<>();
        return find(root, k, nodeSeen);
    }

    private boolean find(TreeNode root, int k, Set<Integer> set) {
        if(root == null) return false;
        if(set.contains(k - root.val)) return true;
        set.add(root.val);
        return find(root.left, k, set) || find(root.right, k, set);
    }

    /**
     * Approach 2: BFS + HashSet
     * We can always traverse every tree node using DFS and BFS, just like in approach 1, we need a hash set to store values seen so far.
     * However, this time we iterate tree nodes in a level order.
     *
     * Time: O(N) we still need to visit all the nodes in the tree, in the worst case
     * Space: O(N) still need a hash set to store values seen so far
     */
    public boolean findTargetBFS(TreeNode root, int k) {
        if(root == null) return false;
        Set<Integer> nodeSeen = new HashSet<>();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while(!queue.isEmpty()) {
            TreeNode curr = queue.poll();
            if(nodeSeen.contains(k - curr.val)) return true;
            nodeSeen.add(curr.val);
            if(curr.left != null) queue.add(curr.left);
            if(curr.right != null) queue.add(curr.right);
        }
        return false;
    }

    /**
     * Approach 3: Inorder + Two Pointers
     * The first two approaches work well for all kinds of tree. However, we do not take advantage of the binary search tree property.
     * If we inorder traverse a BST, we will get a sorted list. Then we can use two pointers to find the target sum easily.
     *
     * Time: O(N), we need first do an inorder traverse to obtain a sorted list, then use two pointers to iterate over the resulting list
     * Space: O(N), it requires a sorted list to store all the values in the BST
     */
    public boolean findTargetInorder(TreeNode root, int k) {
        List<Integer> sorted = new ArrayList<>();
        inorder(root, sorted);
        int i = 0;
        int j = sorted.size() - 1;
        while(i < j) {
            if(sorted.get(i) == k - sorted.get(j)) return true;
            else if(sorted.get(i) > k - sorted.get(j)) j -= 1;
            else i += 1;
        }
        return false;
    }

    private void inorder(TreeNode root, List<Integer> list) {
        if(root == null) return;
        inorder(root.left, list);
        list.add(root.val);
        inorder(root.right, list);
    }


    @Test
    public void findTargetInorderTest() {
        /**
         * Example 1:
         * Input:
         *     5
         *    / \
         *   3   6
         *  / \   \
         * 2   4   7
         *
         * Target = 9
         *
         * Output: True
         */
        TreeNode tree = new TreeNode(5);
        tree.left = new TreeNode(3);
        tree.right = new TreeNode(6);
        tree.left.left = new TreeNode(2);
        tree.left.right = new TreeNode(4);
        tree.right.right = new TreeNode(7);
        assertTrue(findTargetInorder(tree, 9));
        /**
         * Input:
         *     5
         *    / \
         *   3   6
         *  / \   \
         * 2   4   7
         *
         * Target = 28
         *
         * Output: False
         */
        assertFalse(findTargetInorder(tree, 28));
    }

    @Test
    public void findTargetBFSTest() {
        /**
         * Example 1:
         * Input:
         *     5
         *    / \
         *   3   6
         *  / \   \
         * 2   4   7
         *
         * Target = 9
         *
         * Output: True
         */
        TreeNode tree = new TreeNode(5);
        tree.left = new TreeNode(3);
        tree.right = new TreeNode(6);
        tree.left.left = new TreeNode(2);
        tree.left.right = new TreeNode(4);
        tree.right.right = new TreeNode(7);
        assertTrue(findTargetBFS(tree, 9));
        /**
         * Input:
         *     5
         *    / \
         *   3   6
         *  / \   \
         * 2   4   7
         *
         * Target = 28
         *
         * Output: False
         */
        assertFalse(findTargetBFS(tree, 28));
    }


    @Test
    public void findTargetDFSTest() {
        /**
         * Example 1:
         * Input:
         *     5
         *    / \
         *   3   6
         *  / \   \
         * 2   4   7
         *
         * Target = 9
         *
         * Output: True
         */
        TreeNode tree = new TreeNode(5);
        tree.left = new TreeNode(3);
        tree.right = new TreeNode(6);
        tree.left.left = new TreeNode(2);
        tree.left.right = new TreeNode(4);
        tree.right.right = new TreeNode(7);
        assertTrue(findTargetDFS(tree, 9));
        /**
         * Input:
         *     5
         *    / \
         *   3   6
         *  / \   \
         * 2   4   7
         *
         * Target = 28
         *
         * Output: False
         */
        assertFalse(findTargetDFS(tree, 28));
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
