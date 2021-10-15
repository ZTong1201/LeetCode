import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LongestConsecutiveSequence {

    /**
     * Given the root of a binary tree, return the length of the longest consecutive sequence path.
     * <p>
     * The path refers to any sequence of nodes from some starting node to any node in the tree along the parent-child
     * connections. The longest consecutive path needs to be from parent to child (cannot be the reverse).
     * <p>
     * Constraints:
     * <p>
     * The number of nodes in the tree is in the range [1, 3 * 10^4].
     * -3 * 10^4 <= Node.val <= 3 * 10^4
     * <p>
     * Approach: Preorder traversal (Recursion)
     * Since the path can only be parent-child connection, we can use dfs to find the longest consecutive path at each node.
     * Basically, we have these scenarios:
     * 1. Base case, if the tree is null, return the current longest length.
     * 2. If the current node value can form a consecutive path with its parent, we increment the longest length
     * 3. If not, then we should try to seek a new consecutive path, hence the current longest length should be reset to 1
     * 4. At a given node, the final longest length should be the maximum of current recorded longest length, and longest
     * length being calculated recursively from its left and right children.
     * <p>
     * Time: O(n) we need to visit all nodes in the tree
     * Space: O(n) in the tree is extremely unbalanced
     */
    public int longestConsecutive(TreeNode root) {
        // initially, the root node doesn't have a parent, hence pass in a null pointer
        return findLongestConsecutivePathLength(root, null, 0);
    }

    private int findLongestConsecutivePathLength(TreeNode root, TreeNode parent, int currLength) {
        // base case - return the current length if tree is empty
        if (root == null) return currLength;
        // check whether we're able to construct a longer path
        // otherwise, the current length needs to be reset to 1
        currLength = (parent != null && root.val - parent.val == 1) ? currLength + 1 : 1;
        // return the maximum length in the end
        return Math.max(currLength, Math.max(findLongestConsecutivePathLength(root.left, root, currLength),
                findLongestConsecutivePathLength(root.right, root, currLength)));
    }

    @Test
    public void longestConsecutiveTest() {
        /**
         * Example 1:
         * Input: root = [1,null,3,2,4,null,null,null,5]
         * Output: 3
         * Explanation: Longest consecutive sequence path is 3-4-5, so return 3.
         *                           1
         *                            \
         *                             3
         *                            / \
         *                           2   4
         *                                \
         *                                 5
         */
        TreeNode root1 = new TreeNode(1);
        root1.right = new TreeNode(3);
        root1.right.left = new TreeNode(2);
        root1.right.right = new TreeNode(4);
        root1.right.right.right = new TreeNode(5);
        assertEquals(3, longestConsecutive(root1));
        /**
         * Example 2:
         * Input: root = [2,null,3,2,null,1]
         * Output: 2
         * Explanation: Longest consecutive sequence path is 2-3, not 3-2-1, so return 2.
         *                         2
         *                          \
         *                           3
         *                          /
         *                         2
         *                        /
         *                       1
         */
        TreeNode root2 = new TreeNode(2);
        root2.right = new TreeNode(3);
        root2.right.left = new TreeNode(2);
        root2.right.left.left = new TreeNode(1);
        assertEquals(2, longestConsecutive(root2));
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
