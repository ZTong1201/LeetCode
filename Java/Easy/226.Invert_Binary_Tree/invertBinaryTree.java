import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class invertBinaryTree {

    /**
     * Invert a binary tree.
     *
     * Approach 1: DFS (Recursion)
     * 1. The inverse of an empty is an empty tree
     * 2. The inverse of a tree with root r and left subtree LEFT and right subtree RIGHT, is a tree with root r whose left subtree
     * is the inverse of RIGHT, and whose right subtree is the inverse of LEFT.
     *
     * Time: O(N) we have to visit all the nodes in the tree in order to invert a tree
     * Space: O(logN) for a completely balanced tree, since the call stack will up to the height of a tree. In worst case, it should be O(N)
     */
    public TreeNode invertTreeRecursive(TreeNode root) {
        if(root == null) return null;
        TreeNode right = invertTreeRecursive(root.right);
        TreeNode left = invertTreeRecursive(root.left);
        root.left = right;
        root.right = left;
        return root;
    }

    /**
     * Approach 2: BFS (Iteration)
     * We can invert a tree using Breadth-First Search
     *
     * Time: O(N), we still need to visit all the nodes in the tree
     * Space: O(N), we need a queue to contain all the nodes in a given level, for a full binary tree, the leaf level will have around n / 2
     *       nodes.
     */
    public TreeNode invertTreeIterative(TreeNode root) {
        if(root == null) return null;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while(!queue.isEmpty()) {
            TreeNode curr = queue.poll();
            TreeNode temp = curr.left;
            curr.left = curr.right;
            curr.right = temp;
            if(curr.left != null) queue.add(curr.left);
            if(curr.right != null) queue.add(curr.right);
        }
        return root;
    }

    @Test
    public void invertTreeRecursiveTest() {
        /**
         * Example:
         * Input:
         *      4
         *    /   \
         *   2     7
         *  / \   / \
         * 1   3 6   9
         * Output:
         *      4
         *    /   \
         *   7     2
         *  / \   / \
         * 9   6 3   1
         */
        TreeNode tree = new TreeNode(4);
        tree.left = new TreeNode(2);
        tree.right = new TreeNode(7);
        tree.left.left = new TreeNode(1);
        tree.left.right = new TreeNode(3);
        tree.right.left = new TreeNode(6);
        tree.right.right = new TreeNode(9);
        TreeNode expectedTree = new TreeNode(4);
        expectedTree.left = new TreeNode(7);
        expectedTree.right = new TreeNode(2);
        expectedTree.left.left = new TreeNode(9);
        expectedTree.left.right = new TreeNode(6);
        expectedTree.right.left = new TreeNode(3);
        expectedTree.right.right = new TreeNode(1);
        List<Integer> expected = new ArrayList<>();
        preorder(expectedTree, expected);
        TreeNode actualTree = invertTreeRecursive(tree);
        List<Integer> actual = new ArrayList<>();
        preorder(actualTree, actual);
        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    public void invertTreeIterativeTest() {
        /**
         * Example:
         * Input:
         *      4
         *    /   \
         *   2     7
         *  / \   / \
         * 1   3 6   9
         * Output:
         *      4
         *    /   \
         *   7     2
         *  / \   / \
         * 9   6 3   1
         */
        TreeNode tree = new TreeNode(4);
        tree.left = new TreeNode(2);
        tree.right = new TreeNode(7);
        tree.left.left = new TreeNode(1);
        tree.left.right = new TreeNode(3);
        tree.right.left = new TreeNode(6);
        tree.right.right = new TreeNode(9);
        TreeNode expectedTree = new TreeNode(4);
        expectedTree.left = new TreeNode(7);
        expectedTree.right = new TreeNode(2);
        expectedTree.left.left = new TreeNode(9);
        expectedTree.left.right = new TreeNode(6);
        expectedTree.right.left = new TreeNode(3);
        expectedTree.right.right = new TreeNode(1);
        List<Integer> expected = new ArrayList<>();
        preorder(expectedTree, expected);
        TreeNode actualTree = invertTreeIterative(tree);
        List<Integer> actual = new ArrayList<>();
        preorder(actualTree, actual);
        assertArrayEquals(expected.toArray(), actual.toArray());
    }


    private void preorder(TreeNode root, List<Integer> list) {
        if(root == null) return;
        list.add(root.val);
        preorder(root.left, list);
        preorder(root.right, list);
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
