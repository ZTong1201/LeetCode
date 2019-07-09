import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class deleteNodeBST {

    /**
     * Given a root node reference of a BST and a key, delete the node with the given key in the BST.
     * Return the root node reference (possibly updated) of the BST.
     *
     * Basically, the deletion can be divided into two stages:
     *
     * 1. Search for a node to remove.
     * 2. If the node is found, delete the node.
     *
     * Note: Time complexity should be O(height of tree).
     *
     * A Binary Search Tree (BST) has the property that any nodes in the left subtree is less than the current node, and any nodes
     * in the right subtree is great than the current node. We have to maintain this property after deletion. Hence, we have algorithm
     * looks like this:
     *
     * 1. If the deleting node is a leaf node, we simply remove it (set it to null)
     * 2. If the deleting node is not a leaf node and it has a right child, we can assign current node to its successor and recursively
     *    remove its successor. Successor is the smallest possible value in the right subtree of current node, move that value to the
     *    current node would maintain the binary search tree property
     * 3. If the deleting node is not a leaf node and it has a left child, we can assign current node to its predecessor and recursively
     *    remove its predecessor. Predecessor is the largest possible value in the left subtree of current node.
     *
     * Based on the binary search property, if current value is larger than the target value, we search its left subtree, and if current
     * value is smaller thant the target value, we simply search its right subtree.
     *
     * Time: O(H), where H is the height of BST. H = logN if the BST is completely balanced, it can degenerated to N if it is completely
     *      unbalanced. Overall, it should be logN
     * Space: O(H), the recursion call stack will cost O(H) space.
     */
    public TreeNode deleteNode(TreeNode root, int key) {
        if(root == null) return root;
        if(root.val < key) root.right = deleteNode(root.right, key);
        else if(root.val > key) root.left = deleteNode(root.left, key);
        else {
            if(root.left == null && root.right == null) return null;
            if(root.right != null) {
                root.val = findSuccessor(root);
                root.right = deleteNode(root.right, root.val);
            } else {
                root.val = findPredecessor(root);
                root.left = deleteNode(root.left, root.val);
            }
        }
        return root;
    }

    private int findPredecessor(TreeNode root) {
        root = root.left;
        while(root.right != null) {
            root = root.right;
        }
        return root.val;
    }

    private int findSuccessor(TreeNode root) {
        root = root.right;
        while(root.left != null) {
            root = root.left;
        }
        return root.val;
    }

    private int[] treeArray;

    @Test
    public void deleteNodeTest() {
        /**
         * Example 1:
         * root = [5, 3, 6, 2, 4, null, 7]
         * key = 3
         *
         *     5
         *    / \
         *   3   6
         *  / \   \
         * 2   4   7
         *
         * Given key to delete is 3. So we find the node with value 3 and delete it.
         *
         * One valid answer is [5,4,6,2,null,null,7], shown in the following BST.
         *
         *     5
         *    / \
         *   4   6
         *  /     \
         * 2       7
         *
         * Another valid answer is [5,2,6,null,4,null,7].
         *
         *     5
         *    / \
         *   2   6
         *    \   \
         *     4   7
         *
         * Based on the algorithm above, it should always return
         *
         *     5
         *    / \
         *   4   6
         *  /     \
         * 2       7
         *
         */
        TreeNode tree1 = new TreeNode(5);
        tree1.left = new TreeNode(3);
        tree1.right = new TreeNode(6);
        tree1.left.left = new TreeNode(2);
        tree1.left.right = new TreeNode(4);
        tree1.right.right = new TreeNode(7);
        List<Integer> actual1 = new ArrayList<>();
        List<Integer> expected1 = new ArrayList<>();
        TreeNode actualTree1 = deleteNode(tree1, 3);
        treeNodeToList(actual1, actualTree1);
        TreeNode treeExpected1 = new TreeNode(5);
        treeExpected1.left = new TreeNode(4);
        treeExpected1.right = new TreeNode(6);
        treeExpected1.left.left = new TreeNode(2);
        treeExpected1.right.right = new TreeNode(7);
        treeNodeToList(expected1, treeExpected1);
        for(int i = 0; i < actual1.size(); i++) {
            assertEquals(expected1.get(i), actual1.get(i));
        }
        /**
         * Example 2:
         * root = [1, null, 2]
         * key = 2
         *    1
         *     \
         *      2
         *
         * Given key to delete is 2. So we find the node with value 3 and delete it.
         * The output should be [1]
         *    1
         */
        TreeNode tree2 = new TreeNode(1);
        tree1.right = new TreeNode(2);
        List<Integer> actual2 = new ArrayList<>();
        List<Integer> expected2 = new ArrayList<>();
        TreeNode actualTree2 = deleteNode(tree2, 2);
        treeNodeToList(actual2, actualTree2);
        TreeNode treeExpected2 = new TreeNode(1);
        treeNodeToList(expected2, treeExpected2);
        for(int i = 0; i < actual2.size(); i++) {
            assertEquals(expected2.get(i), actual2.get(i));
        }
    }

    private void treeNodeToList(List<Integer> alist, TreeNode root) {
        if(root == null) return;
        alist.add(root.val);
        treeNodeToList(alist, root.left);
        treeNodeToList(alist, root.right);
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
