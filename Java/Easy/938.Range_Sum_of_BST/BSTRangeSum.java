import org.junit.Test;

import java.util.Stack;

import static org.junit.Assert.assertEquals;

public class BSTRangeSum {

    /**
     * Given the root node of a binary search tree and two integers low and high, return the sum of values of all nodes
     * with a value in the inclusive range [low, high].
     * <p>
     * Constraints:
     * <p>
     * The number of nodes in the tree is in the range [1, 2 * 10^4].
     * 1 <= Node.val <= 10^5
     * 1 <= low <= high <= 10^5
     * All Node.val are unique.
     * <p>
     * Approach 1: DFS (recursion)
     * Traverse the tree to add values belong to [low, high] range to the final answer. Since it's a BST, we can take advantage
     * of the BST property to (potentially) discard a sub-tree. For example, if the current node value is greater than the lower
     * bound, we should move to the left sub-tree in order to get a smaller value which also belongs to this range.
     * <p>
     * Time: O(n)
     * Space: O(H)
     */
    private int res;

    public int rangeSumBSTDfsRecursive(TreeNode root, int low, int high) {
        res = 0;
        dfs(root, low, high);
        return res;
    }

    private void dfs(TreeNode root, int low, int high) {
        if (root == null) return;
        if (root.val >= low && root.val <= high) res += root.val;
        // in order to find a smaller value in this range, search the left sub-tree
        if (root.val > low) dfs(root.left, low, high);
        // search the right sub-tree for a greater value
        if (root.val < high) dfs(root.right, low, high);
    }

    /**
     * Approach 2: DFS (iteration)
     * We can convert the above recursion into an iteration by explicitly using a stack.
     * <p>
     * Time: O(n)
     * Space: O(n) the stack will contain no more than two levels of nodes, if the BST is balanced, the stack will be
     * of size O(n/2) = O(n)
     */
    public int rangeSumBSTDfsIterative(TreeNode root, int low, int high) {
        Stack<TreeNode> stack = new Stack<>();
        int res = 0;
        stack.push(root);

        while (!stack.isEmpty()) {
            TreeNode curr = stack.pop();
            if (curr != null) {
                if (curr.val >= low && curr.val <= high) res += curr.val;
                if (curr.val > low) stack.push(curr.left);
                if (curr.val < high) stack.push(curr.right);
            }
        }
        return res;
    }

    /**
     * Approach 3: Inorder traversal
     * We can take advantage of the BST property even further - inorder traversing a BST will return a sorted array. Hence, if
     * we encounter a node whose value is larger than the upper bound, then we can early terminate the algorithm.
     * <p>
     * Time: O(n)
     * Space: O(H) for recursion call stack
     */
    public int rangeSumBSTDfsInorder(TreeNode root, int low, int high) {
        res = 0;
        inorder(root, low, high);
        return res;
    }

    private void inorder(TreeNode root, int low, int high) {
        if (root == null) return;
        inorder(root.left, low, high);
        // early termination is the root value is greater than the upper bound
        if (root.val > high) return;
        // add node values to the res if it belongs to the range [low, high]
        if (root.val >= low) res += root.val;
        inorder(root.right, low, high);
    }

    @Test
    public void rangeSumBSTTest() {
        /**
         * Example 1:
         * Input: root = [10,5,15,3,7,null,18], low = 7, high = 15
         * Output: 32
         * Explanation: Nodes 7, 10, and 15 are in the range [7, 15]. 7 + 10 + 15 = 32.
         *                              10
         *                             /  \
         *                            5    15
         *                           / \     \
         *                          3   7    18
         */
        TreeNode tree1 = new TreeNode(10);
        tree1.left = new TreeNode(5);
        tree1.right = new TreeNode(15);
        tree1.left.left = new TreeNode(3);
        tree1.left.right = new TreeNode(7);
        tree1.right.right = new TreeNode(18);
        assertEquals(32, rangeSumBSTDfsRecursive(tree1, 7, 15));
        assertEquals(32, rangeSumBSTDfsIterative(tree1, 7, 15));
        assertEquals(32, rangeSumBSTDfsInorder(tree1, 7, 15));
        /**
         * Example 2:
         * Input: root = [10,5,15,3,7,13,18,1,null,6], low = 6, high = 10
         * Output: 23
         * Explanation: Nodes 6, 7, and 10 are in the range [6, 10]. 6 + 7 + 10 = 23.
         *                                    10
         *                                  /    \
         *                                 5     15
         *                               /  \   /  \
         *                              3   7  13  18
         *                             /   /
         *                            1   6
         */
        TreeNode tree2 = new TreeNode(10);
        tree2.left = new TreeNode(5);
        tree2.right = new TreeNode(15);
        tree2.left.left = new TreeNode(3);
        tree2.left.right = new TreeNode(7);
        tree2.right.left = new TreeNode(13);
        tree2.right.right = new TreeNode(18);
        tree2.left.left.left = new TreeNode(1);
        tree2.left.right.left = new TreeNode(6);
        assertEquals(23, rangeSumBSTDfsRecursive(tree2, 6, 10));
        assertEquals(23, rangeSumBSTDfsIterative(tree2, 6, 10));
        assertEquals(23, rangeSumBSTDfsInorder(tree2, 6, 10));
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
