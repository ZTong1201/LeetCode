import java.util.*;

public class BSTIteratorControlledRecursion {
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
     * Approach 1: Controlled Recursion
     * Naive approach works well, however, the space complexity is not O(h), in order to realize O(h) space complexity, we need to only store
     * nodes as we go deepest the left subtree. In other words, we retain the right subtree for later traversal, since the most left node
     * in the left subtree will always be smaller than the current node and its right subtree. Hence we only require O(h) space to to contain
     * nodes up to the height of tree size.
     *
     * When we call next() method, we pop from the stack and return its value, which will be O(1). However, if the node has a right subtree,
     * we need to search the right subtree until reaching its leftmost node. This will cost up to O(n) time in the worst case. However, not
     * all of the nodes have a right child. Besides, only when we have a completely unbalanced tree, it will require O(n) to get the smallest
     * value for only the root node. Hence, the runtime would be amortized O(1) time
     * hasNext(): O(1)
     * Space: O(h)
     */
    private Stack<TreeNode> stack;

    public BSTIteratorControlledRecursion() {}

    public BSTIteratorControlledRecursion(TreeNode root) {
        this.stack = new Stack<>();
        searchLeft(root);
    }

    private void searchLeft(TreeNode root) {
        while(root != null) {
            this.stack.push(root);
            root = root.left;
        }
    }

    public int next() {
        TreeNode curr = this.stack.pop();
        if(curr.right != null) {
            searchLeft(curr.right);
        }
        return curr.val;
    }

    public boolean hasNext() {
        return !this.stack.isEmpty();
    }
}
