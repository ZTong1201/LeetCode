import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class BalanceBST {

    /**
     * Given a binary search tree, return a balanced binary search tree with the same node values.
     * <p>
     * A binary search tree is balanced if and only if the depth of the two subtrees of every node never differ by more than 1.
     * <p>
     * If there is more than one answer, return any of them.
     * <p>
     * Constraints:
     * <p>
     * The number of nodes in the tree is between 1 and 10^4.
     * The tree nodes will have distinct values between 1 and 10^5.
     * <p>
     * Approach 1: Inorder traversal + construct BST from sorted array
     * Inorder traversal to get a sorted array (list), build BST using divider and conquer to guarantee height balance
     * <p>
     * Time: O(n)
     * Space: O(n)
     */
    public TreeNode balanceBSTInorder(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        inorder(root, res);
        return constructBST(res, 0, res.size() - 1);
    }

    private TreeNode constructBST(List<Integer> res, int start, int end) {
        if (start > end) return null;
        int mid = (end - start) / 2 + start;
        TreeNode root = new TreeNode(res.get(mid));
        root.left = constructBST(res, start, mid - 1);
        root.right = constructBST(res, mid + 1, end);
        return root;
    }

    private void inorder(TreeNode root, List<Integer> res) {
        if (root == null) return;
        inorder(root.left, res);
        res.add(root.val);
        inorder(root.right, res);
    }

    /**
     * Approach 2: DSW algorithm
     * This is the most efficient algorithm to balance a BST without using extra space.
     * 1. Convert the BST into a singly linked list by keep right rotating the current node if it has a left child (hence the
     * result list will be sorted), also count the number of total nodes (nodeCount)
     * 2. Compute the balanced height based on number of nodes - h = log2(nodeCount + 1)
     * 3. Compute the number of nodes needed to construct complete balanced tree with height h, which is n = 2^h - 1;
     * 4. Get the number of excess nodes = h - n
     * 5. From the beginning of the sorted list, left rotate (h - n) nodes
     * 6. Starting from m = n / 2, keep left rotating m nodes until m = 0, each time cut the number of nodes by half m = m / 2
     */
    public TreeNode balanceBSTDSW(TreeNode root) {
        // create a dummy root node - hence it's right child will always be the root of the tree
        TreeNode dummy = new TreeNode(0);
        dummy.right = root;
        int nodeCount = createLinkedListByRightRotation(dummy);

        // compute the balanced height
        int height = (int) (Math.log(nodeCount + 1) / Math.log(2));
        // compute the number of nodes needed to construct a complete balanced BST for that height;
        int numNodes = (int) Math.pow(2, height) - 1;
        // compute the number of excess nodes
        int excess = nodeCount - numNodes;

        // first left rotate excess nodes
        createBSTByLeftRotation(dummy, excess);
        // starting from numNodes / 2, keep left rotating until the tree is balanced
        for (int i = numNodes / 2; i > 0; i /= 2) {
            createBSTByLeftRotation(dummy, i);
        }
        return dummy.right;
    }

    private void createBSTByLeftRotation(TreeNode root, int numNodes) {
        // still, the real head is the right child of the dummy node
        TreeNode pseudoRoot = root.right;
        // left rotate some nodes
        while (numNodes-- > 0) {
            TreeNode oldRoot = pseudoRoot;
            pseudoRoot = pseudoRoot.right;

            // left rotate current node
            root.right = pseudoRoot;
            oldRoot.right = pseudoRoot.left;
            pseudoRoot.left = oldRoot;

            // move to the next node and keep left rotating
            root = pseudoRoot;
            pseudoRoot = pseudoRoot.right;
        }
    }

    private int createLinkedListByRightRotation(TreeNode root) {
        // keep track of the number of nodes while converting BST to list
        int nodeCount = 0;
        // assign a pointer as the pseudo root node
        TreeNode pseudoRoot = root.right;
        // the conversion stops when we hit the rightmost leaf node
        while (pseudoRoot != null) {
            // keep right rotating the current node if it has a left child
            if (pseudoRoot.left != null) {
                TreeNode oldRoot = pseudoRoot;
                pseudoRoot = pseudoRoot.left;

                // right rotate
                oldRoot.left = pseudoRoot.right;
                pseudoRoot.right = oldRoot;
                root.right = pseudoRoot;
                // after rotation the previous left child becomes the pseudo root node now
                // the rotation will resume if it also has a left child
            } else {
                // otherwise, the BST structure has been flattened
                // move to its right child (as if moving to the next node in linked list)
                // increment the count since we traversed one
                nodeCount++;
                root = pseudoRoot;
                pseudoRoot = pseudoRoot.right;
            }
        }
        return nodeCount;
    }


    @Test
    public void balanceBSTTest() {
        /**
         * Example:
         * Input: root = [1,null,2,null,3,null,4,null,null]
         * Output: [2,1,3,null,null,null,4]
         * Explanation: This is not the only correct answer, [3,1,4,null,2,null,null] is also correct.
         *            1
         *             \
         *              2
         *               \
         *                3
         *                 \
         *                  4
         * =>
         *         2
         *        / \
         *       1   3
         *            \
         *            4
         * or
         *         3
         *        / \
         *       1   4
         *        \
         *         2
         */
        TreeNode tree = new TreeNode(1);
        tree.right = new TreeNode(2);
        tree.right.right = new TreeNode(3);
        tree.right.right.right = new TreeNode(4);
        TreeNode balancedBSTInorder = balanceBSTInorder(tree);
        assertTrue(Math.abs(maxDepth(balancedBSTInorder.left) - maxDepth(balancedBSTInorder.right)) <= 1);
        TreeNode balancedBSTDSW = balanceBSTDSW(tree);
        assertTrue(Math.abs(maxDepth(balancedBSTDSW.left) - maxDepth(balancedBSTDSW.right)) <= 1);
    }

    private int maxDepth(TreeNode root) {
        if (root == null) return 0;
        return Math.max(maxDepth(root.left), maxDepth(root.right)) + 1;
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
