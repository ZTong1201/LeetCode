import java.util.*;

public class BSTIteratorInorder {
    /**
     * Implement an iterator over a binary search tree (BST). Your iterator will be initialized with the root node of a BST.
     *
     * Calling next() will return the next smallest number in the BST.
     *
     * Note:
     *
     * next() and hasNext() should run in average O(1) time and uses O(h) memory, where h is the height of the tree.
     * You may assume that next() call will always be valid, that is, there will be at least a next smallest number in the BST when
     * next() is called.
     *
     * Approach 1: Inorder Traversal
     *
     * Inorder traversal a BST will obtain a sorted list. Store all the values in a sorted list and find the next value when next() method
     * is called. If current index beyond the list size - 1, hasNext() method return false
     *
     * Next(): O(1)
     * hasNext(): O(1)
     * Space: O(N) since we store all the values in a list
     */
    public BSTIteratorInorder() {}

    private List<Integer> values;
    private int index;

    public BSTIteratorInorder(TreeNode root) {
        this.values = new ArrayList<>();
        this.index = 0;
        inorder(root);
    }

    private void inorder(TreeNode root) {
        if(root == null) return;
        inorder(root.left);
        this.values.add(root.val);
        inorder(root.right);
    }


    /** @return the next smallest number */
    public int next() {
        return this.values.get(this.index++);
    }

    /** @return whether we have a next smallest number */
    public boolean hasNext() {
        return this.index < this.values.size();
    }
}
