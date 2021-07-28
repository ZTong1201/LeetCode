import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static org.junit.Assert.assertEquals;

public class KthSmallest {

    /**
     * Given the root of a binary search tree, and an integer k, return the kth (1-indexed) smallest element in the tree.
     * <p>
     * Constraints:
     * <p>
     * The number of nodes in the tree is n.
     * 1 <= k <= n <= 104
     * 0 <= Node.val <= 104
     * <p>
     * Approach 1: Recursion inorder traversal to a sorted list
     * <p>
     * Time: O(N) H is the height of the BST, O(logN) on average, O(N) in the worst case
     * Space: O(N) since all node values are stored in a sorted list
     */
    public int kthSmallestRecursive(TreeNode root, int k) {
        List<Integer> sortedList = new ArrayList<>();
        inorder(root, sortedList);
        return sortedList.get(k - 1);
    }

    private void inorder(TreeNode root, List<Integer> sortedList) {
        if (root == null) return;
        inorder(root.left, sortedList);
        sortedList.add(root.val);
        inorder(root.right, sortedList);
    }

    /**
     * Approach 2: Iterative inorder (stack)
     * <p>
     * Time: O(H + k) H is the height of the BST. We need to first go to the leftmost child in the BST and start
     * from there we will visit next k nodes.
     * Space: O(H)
     */
    public int kthSmallestIterative(TreeNode root, int k) {
        Stack<TreeNode> stack = new Stack<>();
        int index = 1;
        while (!stack.isEmpty() || root != null) {
            if (root != null) {
                // mimic the recursion behavior until the leftmost child
                stack.push(root);
                root = root.left;
            } else {
                // starting from the smallest value (leftmost node) to
                // traverse next k nodes
                TreeNode curr = stack.pop();
                if (index == k) return curr.val;
                index++;
                // traverse to the next node
                root = curr.right;
            }
        }
        // this return statement should never be reached if k is valid
        return -1;
    }

    /**
     * Follow up: Follow up: If the BST is modified often (i.e., we can do insert and delete operations) and you
     * need to find the kth smallest frequently, how would you optimize?
     * <p>
     * The search takes O(H + k) time and insert/delete in BST would take O(H) time, hence insert/delete + search
     * would take O(2H + k) in total. Since insert/delete has already been optimized and it couldn't be improved, so
     * the search is essentially to be improved. While keeping the basic BST structure, we can also add a linked list
     * structure to keep a sorted list (similar to LRU cache), then the search runtime would always be O(k) instead
     * of O(H + k), hence the total runtime is O(H + k) now. If the BST will be modified often, then the insert and
     * delete operation will finally dominate the time complexity - which is O(H)
     */


    @Test
    public void kthSmallestTest() {
        /**
         * Example 1:
         * Input: root = [3,1,4,null,2], k = 1
         * Output: 1
         *               3
         *              / \
         *             1   4
         *              \
         *               2
         */
        TreeNode tree1 = new TreeNode(3);
        tree1.left = new TreeNode(1);
        tree1.right = new TreeNode(4);
        tree1.left.right = new TreeNode(2);
        assertEquals(1, kthSmallestRecursive(tree1, 1));
        assertEquals(1, kthSmallestIterative(tree1, 1));
        /**
         * Example 2:
         * Input: root = [5,3,6,2,4,null,null,1], k = 3
         * Output: 3
         *                     5
         *                   /   \
         *                  3     6
         *                 / \
         *                2  4
         *               /
         *              1
         */
        TreeNode tree2 = new TreeNode(5);
        tree2.left = new TreeNode(3);
        tree2.right = new TreeNode(6);
        tree2.left.left = new TreeNode(2);
        tree2.left.right = new TreeNode(4);
        tree2.left.left.left = new TreeNode(1);
        assertEquals(3, kthSmallestRecursive(tree2, 3));
        assertEquals(3, kthSmallestIterative(tree2, 3));
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
