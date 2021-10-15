import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LongestConsecutiveSequenceII {

    /**
     * Given the root of a binary tree, return the length of the longest consecutive path in the tree.
     * <p>
     * A consecutive path is a path where the values of the consecutive nodes in the path differ by one. This path can be
     * either increasing or decreasing.
     * <p>
     * For example, [1,2,3,4] and [4,3,2,1] are both considered valid, but the path [1,2,4,3] is not valid.
     * On the other hand, the path can be in the child-Parent-child order, where not necessarily be parent-child order.
     * <p>
     * Constraints:
     * <p>
     * The number of nodes in the tree is in the range [1, 3 * 10^4].
     * -3 * 10^4 <= Node.val <= 3 * 10^4
     * <p>
     * Approach: Postorder traversal (Recursion)
     * Basically, at a given node, we want to know the length of increasing and decreasing sequence coming from its left
     * and right children. Here, increasing means the root value - child value is 1, and decreasing means the child value
     * - root value is 1. The max length at current node is the length of increasing + length of decreasing - 1 (minus one
     * because we count the current node twice).
     * The base case will be if the tree is empty, then we return 0 for both sequences.
     * <p>
     * Time: O(n) need to traverse the entire tree
     * Space: O(n)
     */
    private int maxLength;

    public int longestConsecutive(TreeNode root) {
        maxLength = 0;
        findLongestConsecutivePathLength(root);
        return maxLength;
    }

    private int[] findLongestConsecutivePathLength(TreeNode root) {
        if (root == null) return new int[]{0, 0};

        // initialize both lengths of sequence as 1
        int lengthOfIncreasingSequence = 1, lengthOfDecreasingSequence = 1;
        // compute the length in the left subtree
        if (root.left != null) {
            int[] left = findLongestConsecutivePathLength(root.left);
            // increment the length based on the difference
            if (root.val - root.left.val == 1) {
                lengthOfDecreasingSequence = left[1] + 1;
            } else if (root.left.val - root.val == 1) {
                lengthOfIncreasingSequence = left[0] + 1;
            }
        }

        // compute the length in the right subtree
        if (root.right != null) {
            int[] right = findLongestConsecutivePathLength(root.right);
            // take the maximum length of sequence
            if (root.val - root.right.val == 1) {
                lengthOfDecreasingSequence = Math.max(lengthOfDecreasingSequence, right[1] + 1);
            } else if (root.right.val - root.val == 1) {
                lengthOfIncreasingSequence = Math.max(lengthOfIncreasingSequence, right[0] + 1);
            }
        }

        // after searching both subtrees - get the max length by grouping them together
        maxLength = Math.max(maxLength, lengthOfIncreasingSequence + lengthOfDecreasingSequence - 1);
        // return the max length of both sequences in an array
        return new int[]{lengthOfIncreasingSequence, lengthOfDecreasingSequence};
    }

    @Test
    public void longestConsecutiveTest() {
        /**
         * Example 1:
         * Input: root = [1,2,3]
         * Output: 2
         * Explanation: The longest consecutive path is [1, 2] or [2, 1].
         *                   1
         *                  / \
         *                 2   3
         */
        TreeNode root1 = new TreeNode(1);
        root1.left = new TreeNode(2);
        root1.right = new TreeNode(3);
        assertEquals(2, longestConsecutive(root1));
        /**
         * Example 2:
         * Input: root = [2,1,3]
         * Output: 3
         * Explanation: The longest consecutive path is [1, 2, 3] or [3, 2, 1].
         *                2
         *               / \
         *              1   3
         */
        TreeNode root2 = new TreeNode(2);
        root2.left = new TreeNode(1);
        root2.right = new TreeNode(3);
        assertEquals(3, longestConsecutive(root2));
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
