import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class inorderSuccessor {
    /**
     * Given a binary search tree and a node in it, find the in-order successor of that node in the BST.
     *
     * The successor of a node p is the node with the smallest key greater than p.val.
     *
     * Note:
     *
     * If the given node has no in-order successor in the tree, return null.
     * It's guaranteed that the values of the tree are unique.
     *
     * Approach 1: Inorder traversal
     * Inorder traverse a BST will obtain a sorted list. Hence, the successor of a node will locate right next to that node. If the index
     * is beyond the list, return null
     *
     * Time: O(N) visit all the nodes once
     * Space: O(N + H) the inorder traverse call stack requires O(H) space, the final result list requires O(N) to store all values.
     */
    private int index;

    public TreeNode inorderSuccessorWithList(TreeNode root, TreeNode p) {
        this.index = 0;
        List<TreeNode> nodes = new ArrayList<>();
        inorder(root, p, nodes);
        return index < nodes.size() ? nodes.get(index) : null;
    }

    private void inorder(TreeNode root, TreeNode p, List<TreeNode> nodes) {
        if(root == null) return;
        inorder(root.left, p, nodes);
        nodes.add(root);
        if(root == p) this.index = nodes.size();
        inorder(root.right, p, nodes);
    }

    @Test
    public void inorderSuccessorWithListTest() {
        /**
         * Example 1
         * Input: root = [2,1,3], p = 1
         *           2
         *          / \
         *         1  3
         * Output: 2
         * Explanation: 1's in-order successor node is 2. Note that both p and the return value is of TreeNode type.
         */
        TreeNode tree1 = new TreeNode(2);
        tree1.left = new TreeNode(1);
        tree1.right = new TreeNode(3);
        TreeNode p1 = tree1.left;
        TreeNode expected1 = tree1;
        TreeNode actual1 = inorderSuccessorWithList(tree1, p1);
        assertEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: root = [5,3,6,2,4,null,null,1], p = 6
         *                5
         *              /  \
         *             3    6
         *            / \
         *           2   4
         *          /
         *         1
         * Output: null
         * Explanation: There is no in-order successor of the current node, so the answer is null.
         */
        TreeNode tree2 = new TreeNode(5);
        tree2.left = new TreeNode(3);
        tree2.right = new TreeNode(6);
        tree2.left.left = new TreeNode(2);
        tree2.left.right = new TreeNode(4);
        tree2.left.left.left = new TreeNode(1);
        TreeNode p2 = tree2.right;
        TreeNode actual2 = inorderSuccessorWithList(tree2, p2);
        assertEquals(null, actual2);
    }

    /**
     * Approach 2: Recursion
     * If the given node has a right subtree, then search the successor in that subtree, otherwise, the successor will just be its parent.
     *
     * Time: O(N) visit all the nodes
     * Space: O(H) the call stack requires up to O(H) space
     */
    private boolean found; //record whether we found node p
    private TreeNode successor; //record the successor

    public TreeNode inorderSuccessorRecursive(TreeNode root, TreeNode p) {
        this.found = false;
        this.successor = null;
        inorder(root, p);
        return this.successor;
    }

    private void inorder(TreeNode root, TreeNode p) {
        //base case, if root is null, return nothing
        if(root == null) return;

        //inorder traversal the BST
        inorder(root.left, p);
        //if we found the target node p, we can search its successor, as we go to the right subtree of that node
        //otherwise it will be the parent node
        if(this.found) {
            this.successor = root;
            this.found = false; //remember to assign found back to false, since we have found the successor
        }
        if(root == p) {
            this.found = true;
        }

        inorder(root.right, p);
    }

    @Test
    public void inorderSuccessorRecursiveTest() {
        /**
         * Example 1
         * Input: root = [2,1,3], p = 1
         *           2
         *          / \
         *         1  3
         * Output: 2
         * Explanation: 1's in-order successor node is 2. Note that both p and the return value is of TreeNode type.
         */
        TreeNode tree1 = new TreeNode(2);
        tree1.left = new TreeNode(1);
        tree1.right = new TreeNode(3);
        TreeNode p1 = tree1.left;
        TreeNode expected1 = tree1;
        TreeNode actual1 = inorderSuccessorRecursive(tree1, p1);
        assertEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: root = [5,3,6,2,4,null,null,1], p = 6
         *                5
         *              /  \
         *             3    6
         *            / \
         *           2   4
         *          /
         *         1
         * Output: null
         * Explanation: There is no in-order successor of the current node, so the answer is null.
         */
        TreeNode tree2 = new TreeNode(5);
        tree2.left = new TreeNode(3);
        tree2.right = new TreeNode(6);
        tree2.left.left = new TreeNode(2);
        tree2.left.right = new TreeNode(4);
        tree2.left.left.left = new TreeNode(1);
        TreeNode p2 = tree2.right;
        TreeNode actual2 = inorderSuccessorRecursive(tree2, p2);
        assertEquals(null, actual2);
    }

    /**
     * Approach 3: Iteration
     * Convert the above process into an iteration. During iteration, we need to first found the target node p. Using the BST property,
     * if current node val is less than the value of p, search the right subtree, otherwise, search the left subtree, meanwhile, we need
     * to record its parent node since it is the potential successor.
     *
     * When we found the node p, if it doesn't have a right child, simply return the previous stored parent node. Otherwise, we need to
     * search to the leftmost leaf node of its right subtree.
     *
     * Time: O(N)
     * Space: O(H)
     */

    public TreeNode inorderSuccessorIterative(TreeNode root, TreeNode p) {
        TreeNode successor = null;
        while(root != null && root != p) {
            if(root.val > p.val) {
                //search the left and record the parent node
                successor = root;
                root = root.left;
            } else {
                //simply search the right
                root = root.right;
            }
        }
        //if the target doesn't have a right child, the parent node is its successor
        if(root.right == null) {
            return successor;
        }
        //otherwise, find the leftmost leaf node in its right subtree
        root = root.right;
        while(root.left != null) {
            root = root.left;
        }
        return root;
    }

    @Test
    public void inorderSuccessorIterativeTest() {
        /**
         * Example 1
         * Input: root = [2,1,3], p = 1
         *           2
         *          / \
         *         1  3
         * Output: 2
         * Explanation: 1's in-order successor node is 2. Note that both p and the return value is of TreeNode type.
         */
        TreeNode tree1 = new TreeNode(2);
        tree1.left = new TreeNode(1);
        tree1.right = new TreeNode(3);
        TreeNode p1 = tree1.left;
        TreeNode expected1 = tree1;
        TreeNode actual1 = inorderSuccessorIterative(tree1, p1);
        assertEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: root = [5,3,6,2,4,null,null,1], p = 6
         *                5
         *              /  \
         *             3    6
         *            / \
         *           2   4
         *          /
         *         1
         * Output: null
         * Explanation: There is no in-order successor of the current node, so the answer is null.
         */
        TreeNode tree2 = new TreeNode(5);
        tree2.left = new TreeNode(3);
        tree2.right = new TreeNode(6);
        tree2.left.left = new TreeNode(2);
        tree2.left.right = new TreeNode(4);
        tree2.left.left.left = new TreeNode(1);
        TreeNode p2 = tree2.right;
        TreeNode actual2 = inorderSuccessorIterative(tree2, p2);
        assertEquals(null, actual2);
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
