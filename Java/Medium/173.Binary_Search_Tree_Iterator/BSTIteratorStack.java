import java.util.Stack;

public class BSTIteratorStack {
    /**
     * Implement an iterator over a binary search tree (BST). Your iterator will be initialized with the root node of a BST.
     * <p>
     * Calling next() will return the next smallest number in the BST.
     * <p>
     * Note:
     * <p>
     * next() and hasNext() should run in average O(1) time and uses O(h) memory, where h is the height of the tree.
     * You may assume that next() call will always be valid, that is, there will be at least a next smallest number in the BST when
     * next() is called.
     * <p>
     * Approach 1: Controlled Recursion
     * Naive approach works well, however, the space complexity is not O(h), in order to realize O(h) space complexity, we need to only store
     * nodes as we go deepest the left subtree. In other words, we retain the right subtree for later traversal, since the most left node
     * in the left subtree will always be smaller than the current node and its right subtree. Hence we only require O(h) space to to contain
     * nodes up to the height of tree size.
     * <p>
     * next(): amortized O(1) actually, each node is pushed and popped to and from the stack once during the iteration. For
     * N nodes, we have 2 * N * O(1) runtime in total O(2N) / N = O(1) on average.
     * hasNext(): O(1)
     * Space: O(h)
     */
    private final Stack<TreeNode> stack;

    public BSTIteratorStack(TreeNode root) {
        this.stack = new Stack<>();
        searchLeft(root);
    }

    private void searchLeft(TreeNode root) {
        while (root != null) {
            this.stack.push(root);
            root = root.left;
        }
    }

    public int next() {
        TreeNode curr = this.stack.pop();
        if (curr.right != null) {
            searchLeft(curr.right);
        }
        return curr.val;
    }

    public boolean hasNext() {
        return !this.stack.isEmpty();
    }
}
