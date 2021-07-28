import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static org.junit.Assert.assertEquals;

public class RecoverBST {

    /**
     * You are given the root of a binary search tree (BST), where exactly two nodes of the tree were swapped by mistake.
     * Recover the tree without changing its structure.
     * <p>
     * Constraints:
     * <p>
     * The number of nodes in the tree is in the range [2, 1000].
     * -2^31 <= Node.val <= 2^31 - 1
     * <p>
     * Approach 1: Inorder traversal + sort an almost sorted array
     * Inorder traverse the entire tree and construct an almost sorted list with two items swapped.
     * Finding the two swapped positions
     * Traverse the tree again and recover the tree
     * <p>
     * Time: O(n) first inorder traversal and final traversal will take O(n) time, also finding two swapped positions may
     * also take up to O(n) time
     * Space: O(n) to keep all elements in the list
     */
    public void recoverTreeInorder(TreeNode root) {
        List<Integer> nums = new ArrayList<>();
        // first inorder traverse the entire tree and construct an almost sorted list
        inorder(root, nums);
        // find two swapped values in the list
        int[] swapped = findTwoSwapped(nums);
        // traverse the tree again to recover the BST
        recover(root, 2, swapped[0], swapped[1]);
    }

    private void recover(TreeNode root, int count, int x, int y) {
        if (root == null) return;
        // swap two values back
        root.val = (root.val == x) ? y : x;
        // decrement count
        count--;
        // the recovery is done once two values are swapped
        if (count == 0) return;
        recover(root.left, count, x, y);
        recover(root.right, count, x, y);
    }

    private int[] findTwoSwapped(List<Integer> nums) {
        int pos1 = -1, pos2 = -1;
        for (int i = 0; i < nums.size() - 1; i++) {
            if (nums.get(i + 1) < nums.get(i)) { // found a misplacement
                pos2 = i + 1;
                if (pos1 == -1) pos1 = i; // if another misplacement hasn't been found, assign it
                else break; // otherwise two positions are found, break
            }
        }
        return new int[]{nums.get(pos1), nums.get(pos2)};
    }

    private void inorder(TreeNode root, List<Integer> nums) {
        if (root == null) return;
        inorder(root.left, nums);
        nums.add(root.val);
        inorder(root.right, nums);
    }

    /**
     * Approach 2: Iteration
     * We can find the swapped nodes while inorder traversing the tree instead of storing all the values into a list
     * <p>
     * Time: O(N)
     * Space: O(H) H is the height of the tree to keep track of nodes until the leftmost child
     */
    public void recoverTreeIterative(TreeNode root) {
        Stack<TreeNode> stack = new Stack<>();
        TreeNode x = null, y = null, prev = null;
        while (!stack.isEmpty() || root != null) {
            // mimic recursion behavior until the leftmost child
            while (root != null) {
                stack.push(root);
                root = root.left;
            }
            // starting from the smallest node (leftmost child)
            TreeNode curr = stack.pop();
            // if we find a misplacement during inorder traversal
            if (prev != null && curr.val < prev.val) {
                // find the swapped nodes
                y = curr;
                if (x == null) x = prev;
                else break;
            }
            // if there is no misplacement
            // update the previous node and root node for next iteration
            prev = curr;
            root = curr.right;
        }
        swap(x, y);
    }

    private void swap(TreeNode x, TreeNode y) {
        int temp = x.val;
        x.val = y.val;
        y.val = temp;
    }

    /**
     * Approach 3: Recursion
     * We can use implicit call stack to replace the stack usage in the iterative approach.
     * <p>
     * Time: O(N)
     * Space: O(H) H is the height of the tree
     */
    private TreeNode x, y, prev;

    public void recoverTreeRecursive(TreeNode root) {
        x = null;
        y = null;
        prev = null;
        findTwoSwappedNodes(root);
        swap(x, y);
    }

    private void findTwoSwappedNodes(TreeNode root) {
        if (root == null) return;
        findTwoSwappedNodes(root.left);
        if (prev != null && root.val < prev.val) {
            y = root;
            if (x == null) x = prev;
            else return;
        }
        prev = root;
        findTwoSwappedNodes(root.right);
    }

    /**
     * Approach 4: Morris inorder traversal
     * <p>
     * Time: O(N)
     * Space: O(1)
     */
    public void recoverTreeMorris(TreeNode root) {
        x = null;
        y = null;
        prev = null;
        TreeNode node = root;
        while (node != null) {
            // if the left subtree has been completed visited
            // according to inorder traversal definition
            // we need to check whether there are any swapped nodes
            if (node.left == null) {
                findSwappedNodes(node);
                // keep traversing the right subtree
                node = node.right;
            } else {
                // find predecessor in the left subtree of current node
                TreeNode predecessor = node.left;
                while (predecessor.right != null && predecessor.right != node) {
                    predecessor = predecessor.right;
                }

                // if it's the first time we reach the predecessor
                // link it to the current root node and keep traversing the left subtree
                if (predecessor.right == null) {
                    predecessor.right = node;
                    node = node.left;
                } else {
                    // otherwise it's the second time back to the predecessor
                    // which means the left subtree of current root node has been complete
                    // do some inorder logic here - find swapped nodes
                    findSwappedNodes(node);
                    // unlink the root node from the predecessor
                    predecessor.right = null;
                    // keep traversing the right subtree of current root node
                    node = node.right;
                }
            }
        }
        // after targeting two swapped nodes, recover the BST
        swap(x, y);
    }

    private void findSwappedNodes(TreeNode node) {
        // given current node and previous node (supposedly in sorted order)
        // see whether we're able to find a misplacement
        if (prev != null && node.val < prev.val) {
            y = node;
            if (x == null) x = prev;
        }
        // update previous node
        prev = node;
    }

    @Test
    public void recoverTreeInorderTest() {
        /**
         * Example:
         * Input: root = [1,3,null,null,2]
         * Output: [3,1,null,null,2]
         * Explanation: 3 cannot be a left child of 1 because 3 > 1. Swapping 1 and 3 makes the BST valid.
         *        1                       3
         *       /                       /
         *      3            =>         1
         *       \                       \
         *        2                       2
         */
        TreeNode tree = new TreeNode(1);
        tree.left = new TreeNode(3);
        tree.left.right = new TreeNode(2);
        recoverTreeInorder(tree);
        assertEquals(3, tree.val);
        assertEquals(1, tree.left.val);
        assertEquals(2, tree.left.right.val);
    }

    @Test
    public void recoverTreeIterativeTest() {
        /**
         * Example:
         * Input: root = [1,3,null,null,2]
         * Output: [3,1,null,null,2]
         * Explanation: 3 cannot be a left child of 1 because 3 > 1. Swapping 1 and 3 makes the BST valid.
         *        1                       3
         *       /                       /
         *      3            =>         1
         *       \                       \
         *        2                       2
         */
        TreeNode tree = new TreeNode(1);
        tree.left = new TreeNode(3);
        tree.left.right = new TreeNode(2);
        recoverTreeIterative(tree);
        assertEquals(3, tree.val);
        assertEquals(1, tree.left.val);
        assertEquals(2, tree.left.right.val);
    }

    @Test
    public void recoverTreeRecursiveTest() {
        /**
         * Example:
         * Input: root = [1,3,null,null,2]
         * Output: [3,1,null,null,2]
         * Explanation: 3 cannot be a left child of 1 because 3 > 1. Swapping 1 and 3 makes the BST valid.
         *        1                       3
         *       /                       /
         *      3            =>         1
         *       \                       \
         *        2                       2
         */
        TreeNode tree = new TreeNode(1);
        tree.left = new TreeNode(3);
        tree.left.right = new TreeNode(2);
        recoverTreeRecursive(tree);
        assertEquals(3, tree.val);
        assertEquals(1, tree.left.val);
        assertEquals(2, tree.left.right.val);
    }

    @Test
    public void recoverTreeMorrisTest() {
        /**
         * Example:
         * Input: root = [1,3,null,null,2]
         * Output: [3,1,null,null,2]
         * Explanation: 3 cannot be a left child of 1 because 3 > 1. Swapping 1 and 3 makes the BST valid.
         *        1                       3
         *       /                       /
         *      3            =>         1
         *       \                       \
         *        2                       2
         */
        TreeNode tree = new TreeNode(1);
        tree.left = new TreeNode(3);
        tree.left.right = new TreeNode(2);
        recoverTreeMorris(tree);
        assertEquals(3, tree.val);
        assertEquals(1, tree.left.val);
        assertEquals(2, tree.left.right.val);
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
