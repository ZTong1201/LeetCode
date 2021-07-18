import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class validateBST {

    /**
     * Given a binary tree, determine if it is a valid binary search tree (BST).
     * <p>
     * Assume a BST is defined as follows:
     * <p>
     * The left subtree of a node contains only nodes with keys less than the node's key.
     * The right subtree of a node contains only nodes with keys greater than the node's key.
     * Both the left and right subtrees must also be binary search trees.
     * <p>
     * Approach 1: Depth-first search (Recursion)
     * We can check a given node value with its upper or lower bound if they are available to make sure that the current value
     * is at least larger than its lower bound or at most smaller than its upper bound.
     * <p>
     * Time: O(N) we visited all the nodes
     * Space: O(N) in the worst case, since the call stack require spaces up to the height of the tree
     */
    public boolean isValidBSTRecursive(TreeNode root) {
        return helper(root, null, null);
    }

    private boolean helper(TreeNode root, Integer lower, Integer upper) {
        if (root == null) return true;
        int val = root.val;
        if (lower != null && val <= lower)
            return false; //if there is a lower bound, and the current node value is smaller than the lower bound
        if (upper != null && val >= upper)
            return false; //if there is a upper bound, and the current node value is larger than the upper bound
        // recursively check the left and right subtrees
        // if checking left subtree, the root value is the upper bound
        // if checking right subtree, the root value is the lower bound
        return helper(root.left, lower, val) && helper(root.right, val, upper);
    }


    /**
     * Approach 2: Depth-first search (Iteration)
     * We can convert the above process to an iteration (using stack). The reason why we use DFS instead of BFS is that DFS is supposed
     * to be faster
     * <p>
     * Time: O(N) we visited all the nodes
     * Space: O(N) in the worst case, we still use DFS, the stack might store the entire tree in the worst case.
     */
    public boolean isValidBSTIterative(TreeNode root) {
        if (root == null) return true;
        Stack<TreeNode> nodes = new Stack<>();
        Stack<Integer> lower = new Stack<>();
        Stack<Integer> upper = new Stack<>();
        nodes.add(root);
        lower.add(null);
        upper.add(null);
        while (!nodes.isEmpty()) {
            TreeNode curr = nodes.pop();
            Integer low = lower.pop();
            Integer high = upper.pop();

            int val = curr.val;
            if (low != null && val <= low) return false;
            if (high != null && val >= high) return false;
            if (curr.left != null) {
                nodes.add(curr.left);
                lower.add(low);
                upper.add(val);
            }
            if (curr.right != null) {
                nodes.add(curr.right);
                lower.add(val);
                upper.add(high);
            }
        }
        return true;
    }

    /**
     * Approach 3: Inorder Traversal
     * <p>
     * A good property for a BST is that if we do an inorder traverse of the BST, the final list will be a sorted list.
     * We can simply inorder traverse the whole tree and check whether the resulting list is sorted and without duplicates.
     * <p>
     * Time: O(N), we need to visit all the nodes, and revisit all the values in the list.
     * Space: O(N), we need a list to store all the values
     */
    public boolean isValidBSTInorder1(TreeNode root) {
        List<Integer> values = new ArrayList<>();
        inorder(root, values);
        for (int i = 0; i < values.size() - 1; i++) {
            if (values.get(i + 1) <= values.get(i)) return false;
        }
        return true;
    }

    private void inorder(TreeNode root, List<Integer> nodes) {
        if (root == null) return;
        inorder(root.left, nodes);
        nodes.add(root.val);
        inorder(root.right, nodes);
    }


    /**
     * Approach 4: Inorder Traversal Without storing all the values.
     * <p>
     * Actually, it is unnecessary to store all the node values. We can simply check whether the newly visited value is larger than
     * its previous value. If not, return false, otherwise return true.
     * <p>
     * Time: O(N), we need to visit all the nodes
     * Space: O(N), in the worst case we need store N nodes in the stack
     */
    public boolean isValidBSTInorder2(TreeNode root) {
        Stack<TreeNode> stack = new Stack<>();
        double inorder = -Double.MAX_VALUE;
        while (!stack.isEmpty() || root != null) {
            while (root != null) {
                stack.push(root);
                root = root.left;
            }

            root = stack.pop();
            if (root.val <= inorder) return false;
            inorder = root.val;
            root = root.right;
        }
        return true;
    }

    /**
     * Approach 5: Inorder Traversal Without storing all the values (Recursive).
     * <p>
     * Actually, it is unnecessary to store all the node values. We can simply check whether the newly visited value is larger than
     * its previous value. If not, return false, otherwise return true.
     * <p>
     * Time: O(N), we need to visit all the nodes
     * Space: O(N), call stack
     */
    private Integer prev;

    public boolean isValidBSTInorder2Recursive(TreeNode root) {
        prev = null;
        return inorderRecursive(root);
    }

    private boolean inorderRecursive(TreeNode root) {
        if (root == null) return true;
        // recursively traverse the nodes until the leftmost leave
        if (!inorderRecursive(root.left)) return false;
        // do some check in between to achieve inorder traversal
        if (prev != null && root.val <= prev) return false;
        // update smallest number so far
        prev = root.val;
        // visit the right node to finish up inorder traversal
        return inorderRecursive(root.right);
    }

    @Test
    public void isValidBSTRecursiveTest() {
        /**
         * Example 1:
         *      1
         *     /
         *    1
         *
         * Input: [1,1]
         * Output: false
         */
        TreeNode tree1 = new TreeNode(1);
        tree1.left = new TreeNode(1);
        assertFalse(isValidBSTRecursive(tree1));
        /**
         * Example 2:
         *     2
         *    / \
         *   1   3
         *
         * Input: [2,1,3]
         * Output: true
         */
        TreeNode tree2 = new TreeNode(2);
        tree2.left = new TreeNode(1);
        tree2.right = new TreeNode(3);
        assertTrue(isValidBSTRecursive(tree2));
        /**
         * Example 3:
         *     5
         *    / \
         *   1   4
         *      / \
         *     3   6
         *
         * Input: [5,1,4,null,null,3,6]
         * Output: false
         * Explanation: The root node's value is 5 but its right child's value is 4.
         */
        TreeNode tree3 = new TreeNode(5);
        tree3.left = new TreeNode(1);
        tree3.right = new TreeNode(4);
        tree3.right.left = new TreeNode(3);
        tree3.right.right = new TreeNode(6);
        assertFalse(isValidBSTRecursive(tree3));
        /**
         * Example 4:
         *     10
         *    / \
         *   5   15
         *      / \
         *     6  20
         *
         * Input: [10,5,15,null,null,6,20]
         * Output: false
         * Explanation: The root node's value is 10 but its right subtree has value 6, which is less than 10.
         */
        TreeNode tree4 = new TreeNode(10);
        tree4.left = new TreeNode(5);
        tree4.right = new TreeNode(15);
        tree4.right.left = new TreeNode(6);
        tree4.right.right = new TreeNode(20);
        assertFalse(isValidBSTRecursive(tree4));
    }

    @Test
    public void isValidBSTIterativeTest() {
        /**
         * Example 1:
         *      1
         *     /
         *    1
         *
         * Input: [1,1]
         * Output: false
         */
        TreeNode tree1 = new TreeNode(1);
        tree1.left = new TreeNode(1);
        assertFalse(isValidBSTIterative(tree1));
        /**
         * Example 2:
         *     2
         *    / \
         *   1   3
         *
         * Input: [2,1,3]
         * Output: true
         */
        TreeNode tree2 = new TreeNode(2);
        tree2.left = new TreeNode(1);
        tree2.right = new TreeNode(3);
        assertTrue(isValidBSTIterative(tree2));
        /**
         * Example 3:
         *     5
         *    / \
         *   1   4
         *      / \
         *     3   6
         *
         * Input: [5,1,4,null,null,3,6]
         * Output: false
         * Explanation: The root node's value is 5 but its right child's value is 4.
         */
        TreeNode tree3 = new TreeNode(5);
        tree3.left = new TreeNode(1);
        tree3.right = new TreeNode(4);
        tree3.right.left = new TreeNode(3);
        tree3.right.right = new TreeNode(6);
        assertFalse(isValidBSTIterative(tree3));
        /**
         * Example 4:
         *     10
         *    / \
         *   5   15
         *      / \
         *     6  20
         *
         * Input: [10,5,15,null,null,6,20]
         * Output: false
         * Explanation: The root node's value is 10 but its right subtree has value 6, which is less than 10.
         */
        TreeNode tree4 = new TreeNode(10);
        tree4.left = new TreeNode(5);
        tree4.right = new TreeNode(15);
        tree4.right.left = new TreeNode(6);
        tree4.right.right = new TreeNode(20);
        assertFalse(isValidBSTIterative(tree4));
    }

    @Test
    public void isValidBSTInorder1Test() {
        /**
         * Example 1:
         *      1
         *     /
         *    1
         *
         * Input: [1,1]
         * Output: false
         */
        TreeNode tree1 = new TreeNode(1);
        tree1.left = new TreeNode(1);
        assertFalse(isValidBSTInorder1(tree1));
        /**
         * Example 2:
         *     2
         *    / \
         *   1   3
         *
         * Input: [2,1,3]
         * Output: true
         */
        TreeNode tree2 = new TreeNode(2);
        tree2.left = new TreeNode(1);
        tree2.right = new TreeNode(3);
        assertTrue(isValidBSTInorder1(tree2));
        /**
         * Example 3:
         *     5
         *    / \
         *   1   4
         *      / \
         *     3   6
         *
         * Input: [5,1,4,null,null,3,6]
         * Output: false
         * Explanation: The root node's value is 5 but its right child's value is 4.
         */
        TreeNode tree3 = new TreeNode(5);
        tree3.left = new TreeNode(1);
        tree3.right = new TreeNode(4);
        tree3.right.left = new TreeNode(3);
        tree3.right.right = new TreeNode(6);
        assertFalse(isValidBSTInorder1(tree3));
        /**
         * Example 4:
         *     10
         *    / \
         *   5   15
         *      / \
         *     6  20
         *
         * Input: [10,5,15,null,null,6,20]
         * Output: false
         * Explanation: The root node's value is 10 but its right subtree has value 6, which is less than 10.
         */
        TreeNode tree4 = new TreeNode(10);
        tree4.left = new TreeNode(5);
        tree4.right = new TreeNode(15);
        tree4.right.left = new TreeNode(6);
        tree4.right.right = new TreeNode(20);
        assertFalse(isValidBSTInorder1(tree4));
    }

    @Test
    public void isValidBSTInorder2Test() {
        /**
         * Example 1:
         *      1
         *     /
         *    1
         *
         * Input: [1,1]
         * Output: false
         */
        TreeNode tree1 = new TreeNode(1);
        tree1.left = new TreeNode(1);
        assertFalse(isValidBSTInorder2(tree1));
        /**
         * Example 2:
         *     2
         *    / \
         *   1   3
         *
         * Input: [2,1,3]
         * Output: true
         */
        TreeNode tree2 = new TreeNode(2);
        tree2.left = new TreeNode(1);
        tree2.right = new TreeNode(3);
        assertTrue(isValidBSTInorder2(tree2));
        /**
         * Example 3:
         *     5
         *    / \
         *   1   4
         *      / \
         *     3   6
         *
         * Input: [5,1,4,null,null,3,6]
         * Output: false
         * Explanation: The root node's value is 5 but its right child's value is 4.
         */
        TreeNode tree3 = new TreeNode(5);
        tree3.left = new TreeNode(1);
        tree3.right = new TreeNode(4);
        tree3.right.left = new TreeNode(3);
        tree3.right.right = new TreeNode(6);
        assertFalse(isValidBSTInorder2(tree3));
        /**
         * Example 4:
         *     10
         *    / \
         *   5   15
         *      / \
         *     6  20
         *
         * Input: [10,5,15,null,null,6,20]
         * Output: false
         * Explanation: The root node's value is 10 but its right subtree has value 6, which is less than 10.
         */
        TreeNode tree4 = new TreeNode(10);
        tree4.left = new TreeNode(5);
        tree4.right = new TreeNode(15);
        tree4.right.left = new TreeNode(6);
        tree4.right.right = new TreeNode(20);
        assertFalse(isValidBSTInorder2(tree4));
    }

    @Test
    public void isValidBSTInorder2RecursiveTest() {
        /**
         * Example 1:
         *      1
         *     /
         *    1
         *
         * Input: [1,1]
         * Output: false
         */
        TreeNode tree1 = new TreeNode(1);
        tree1.left = new TreeNode(1);
        assertFalse(isValidBSTInorder2Recursive(tree1));
        /**
         * Example 2:
         *     2
         *    / \
         *   1   3
         *
         * Input: [2,1,3]
         * Output: true
         */
        TreeNode tree2 = new TreeNode(2);
        tree2.left = new TreeNode(1);
        tree2.right = new TreeNode(3);
        assertTrue(isValidBSTInorder2Recursive(tree2));
        /**
         * Example 3:
         *     5
         *    / \
         *   1   4
         *      / \
         *     3   6
         *
         * Input: [5,1,4,null,null,3,6]
         * Output: false
         * Explanation: The root node's value is 5 but its right child's value is 4.
         */
        TreeNode tree3 = new TreeNode(5);
        tree3.left = new TreeNode(1);
        tree3.right = new TreeNode(4);
        tree3.right.left = new TreeNode(3);
        tree3.right.right = new TreeNode(6);
        assertFalse(isValidBSTInorder2Recursive(tree3));
        /**
         * Example 4:
         *     10
         *    / \
         *   5   15
         *      / \
         *     6  20
         *
         * Input: [10,5,15,null,null,6,20]
         * Output: false
         * Explanation: The root node's value is 10 but its right subtree has value 6, which is less than 10.
         */
        TreeNode tree4 = new TreeNode(10);
        tree4.left = new TreeNode(5);
        tree4.right = new TreeNode(15);
        tree4.right.left = new TreeNode(6);
        tree4.right.right = new TreeNode(20);
        assertFalse(isValidBSTInorder2Recursive(tree4));
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
