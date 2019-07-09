import org.junit.Test;
import static org.junit.Assert.*;

public class lowestCommonAncestorBST {

    /**
     * Given a binary search tree (BST), find the lowest common ancestor (LCA) of two given nodes in the BST.
     *
     * According to the definition of LCA on Wikipedia: “The lowest common ancestor is defined between two nodes p and q as the
     * lowest node in T that has both p and q as descendants (where we allow a node to be a descendant of itself).”
     *
     * Note:
     *
     * All of the nodes' values will be unique.
     * p and q are different and both values will exist in the BST.
     *
     * Approach 1: Recursion
     * In order to find the LCA in a BST, we can take advantage of the BST property.
     * 1. If the value of node p and value of node q locate at two sides of the parent node, which means the parent node is just the LCA
     * 2. If both values are larger than the parent value, the LCA must locate in the right subtree of the parent node
     * 3. If both values are smaller than the parent value, the LCA must locate in the left subtree of the parent node
     *
     * Time: O(N), in the worst case, we must traverse all the nodes in the BST
     * Space: O(N), for a completely unbalanced tree, the recursion call stack would be N
     */
    public TreeNode lowestCommonAncestorRecursive(TreeNode root, TreeNode p, TreeNode q) {
        //get parent value
        int parentVal = root.val;
        //get node p value
        int pVal = p.val;
        //get node q value
        int qVal = q.val;

        //if both values are larger than the parent value, the LCA locates in the right subtree, so we search right tree only
        if(pVal > parentVal && qVal > parentVal) return lowestCommonAncestorRecursive(root.right, p, q);
        //if both values are smaller than the parent value, the LCA locates in the left subtree, so we search left tree only
        else if(pVal < parentVal && qVal < parentVal) return lowestCommonAncestorRecursive(root.left, p, q);
        //if two values locate at both sides, then the parent node is simply the LCA
        else return root;
    }

    /**
     * Approach 2: Iteration
     * We can convert the above process into an iteration. However, we don't actually need a queue or a stack to store
     * nodes to be visited. The essence of the problem is to find the split point of two nodes.
     *
     * Time: O(N), we must traverse all the nodes in the BST
     * Space: O(1), no extra space needed, since we either go to the left subtree or the right subtree or find the parent node
     */
    public TreeNode lowestCommonAncestorIterative(TreeNode root, TreeNode p, TreeNode q) {
        int pVal = p.val;
        int qVal = q.val;
        //start from the root node
        while(root != null) {
            int parentVal = root.val;
            //if both values are larger than the parent value, search the right subtree
            if(pVal > parentVal && qVal > parentVal) root = root.right;
            //if both values are smaller than the parent value, search the left subtree
            else if(pVal < parentVal && qVal < parentVal) root = root.left;
            //else, we find the LCA
            else break;
        }
        return root;
    }

    @Test
    public void lowestCommonAncestorRecursiveTest() {
        /**
         * Example 1:
         * Input:
         *                6
         *               / \
         *              2   8
         *             / \ / \
         *            0  4 7  9
         *              / \
         *             3  5
         * root = 6, p = 2, q = 8;
         * Output: 6
         */
        TreeNode tree = new TreeNode(6);
        tree.left = new TreeNode(2);
        tree.right = new TreeNode(8);
        tree.left.left = new TreeNode(0);
        tree.left.right = new TreeNode(4);
        tree.right.left = new TreeNode(7);
        tree.right.right = new TreeNode(9);
        tree.left.right.left = new TreeNode(3);
        tree.left.right.right = new TreeNode(5);
        TreeNode p1 = tree.left;
        TreeNode q1 = tree.right;
        TreeNode actual1 = lowestCommonAncestorRecursive(tree, p1, q1);
        TreeNode expected1 = tree;
        assertEquals(expected1, actual1);
        /**
         * Example 2:
         * Input:
         *                6
         *               / \
         *              2   8
         *             / \ / \
         *            0  4 7  9
         *              / \
         *             3  5
         * root = 6, p = 2, q = 4;
         * Output: 2
         */
        TreeNode p2 = tree.left;
        TreeNode q2 = tree.left.right;
        TreeNode actual2 = lowestCommonAncestorRecursive(tree, p2, q2);
        TreeNode expected2 = tree.left;
        assertEquals(expected2, actual2);
        /**
         * Example 3:
         * Input:
         *                6
         *               / \
         *              2   8
         *             / \ / \
         *            0  4 7  9
         *              / \
         *             3  5
         * root = 6, p = 0, q = 5;
         * Output: 2
         */
        TreeNode p3 = tree.left.left;
        TreeNode q3 = tree.left.right.right;
        TreeNode actual3 = lowestCommonAncestorRecursive(tree, p3, q3);
        TreeNode expected3 = tree.left;
        assertEquals(expected3, actual3);
    }

    @Test
    public void lowestCommonAncestorIterativeTest() {
        /**
         * Example 1:
         * Input:
         *                6
         *               / \
         *              2   8
         *             / \ / \
         *            0  4 7  9
         *              / \
         *             3  5
         * root = 6, p = 2, q = 8;
         * Output: 6
         */
        TreeNode tree = new TreeNode(6);
        tree.left = new TreeNode(2);
        tree.right = new TreeNode(8);
        tree.left.left = new TreeNode(0);
        tree.left.right = new TreeNode(4);
        tree.right.left = new TreeNode(7);
        tree.right.right = new TreeNode(9);
        tree.left.right.left = new TreeNode(3);
        tree.left.right.right = new TreeNode(5);
        TreeNode p1 = tree.left;
        TreeNode q1 = tree.right;
        TreeNode actual1 = lowestCommonAncestorIterative(tree, p1, q1);
        TreeNode expected1 = tree;
        assertEquals(expected1, actual1);
        /**
         * Example 2:
         * Input:
         *                6
         *               / \
         *              2   8
         *             / \ / \
         *            0  4 7  9
         *              / \
         *             3  5
         * root = 6, p = 2, q = 4;
         * Output: 2
         */
        TreeNode p2 = tree.left;
        TreeNode q2 = tree.left.right;
        TreeNode actual2 = lowestCommonAncestorIterative(tree, p2, q2);
        TreeNode expected2 = tree.left;
        assertEquals(expected2, actual2);
        /**
         * Example 3:
         * Input:
         *                6
         *               / \
         *              2   8
         *             / \ / \
         *            0  4 7  9
         *              / \
         *             3  5
         * root = 6, p = 0, q = 5;
         * Output: 2
         */
        TreeNode p3 = tree.left.left;
        TreeNode q3 = tree.left.right.right;
        TreeNode actual3 = lowestCommonAncestorIterative(tree, p3, q3);
        TreeNode expected3 = tree.left;
        assertEquals(expected3, actual3);
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
