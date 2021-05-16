import org.junit.Test;
import static org.junit.Assert.*;

public class constructBinaryTree {


    /**
     * Given an array where elements are sorted in ascending order, convert it to a height balanced BST.
     *
     * For this problem, a height-balanced binary tree is defined as a binary tree in which
     * the depth of the two subtrees of every node never differ by more than 1.
     *
     * The problem is basically a binary search problem. If we always insert the value locates at the middle,
     * the corresponding BST would be height-balanced.
     *
     * Time: O(N) since we must iterate over all the elements in the array
     * Space: O(logN) before we go to the right subtree of the root node, we have to move all the way down to the very left leaf,
     *     the call stack will store all the values in the left subtree, which approximate to the height of the tree (logN).
     */
    public TreeNode sortedArrayToBST(int[] nums) {
        return insertToBST(nums, 0, nums.length - 1);
    }

    private TreeNode insertToBST(int[] nums, int start, int end) {
        if(start > end) return null;
        int mid = start + (end - start) / 2;
        TreeNode res = new TreeNode(nums[mid]);
        res.left = insertToBST(nums, start, mid - 1);
        res.right = insertToBST(nums, mid + 1, end);
        return res;
    }

    @Test
    public void sortedArrayToBSTTest() {
        /**
         * Example1:
         * Input: [-10, -3, 0, 5, 9]
         * Output: 0
         *       /  \
         *    -10    5
         *       \    \
         *       -3   9
         */
        int[] test1 = new int[]{-10, -3, 0, 5, 9};
        TreeNode actual1 = sortedArrayToBST(test1);
        TreeNode expected1 = new TreeNode(0);
        expected1.left = new TreeNode(-10);
        expected1.left.right = new TreeNode(-3);
        expected1.right = new TreeNode(5);
        expected1.right.right = new TreeNode(9);
        assertTrue(testSameTree(expected1, actual1));
        /**
         * Example1:
         * Input: [-10, -3, 0, 5, 6, 9]
         * Output: 0
         *       /  \
         *    -10    6
         *       \   /\
         *       -3 5 9
         */
        int[] test2 = new int[]{-10, -3, 0, 5, 6, 9};
        TreeNode actual2 = sortedArrayToBST(test2);
        TreeNode expected2 = new TreeNode(0);
        expected2.right = new TreeNode(6);
        expected2.right.right = new TreeNode(9);
        expected2.right.left = new TreeNode(5);
        expected2.left = new TreeNode(-10);
        expected2.left.right = new TreeNode(-3);
        assertTrue(testSameTree(expected2, actual2));
    }

    private boolean testSameTree(TreeNode p, TreeNode q) {
        if(p == null && q == null) return true;
        if(p == null || q == null) return false;
        if(p.val != q.val) return false;
        return testSameTree(p.left, q.left) && testSameTree(p.right, q.right);
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
