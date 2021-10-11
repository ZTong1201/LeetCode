import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class PathSumIII {

    /**
     * Given the root of a binary tree and an integer targetSum, return the number of paths where the sum of the values along
     * the path equals targetSum.
     * <p>
     * The path does not need to start or end at the root or a leaf, but it must go downwards (i.e., traveling only from
     * parent nodes to child nodes).
     * <p>
     * Constraints:
     * <p>
     * The number of nodes in the tree is in the range [0, 1000].
     * -10^9 <= Node.val <= 10^9
     * -1000 <= targetSum <= 1000
     * <p>
     * Approach: Prefix Sum
     * Since the path needs to go downwards, we actually only have two scenarios
     * 1. The path starts from the root node
     * 2. The path doesn't start from the root node, however, it is still a sub-path of root -> leaf path.
     * Therefore, we can still keep track of the path sum (from root to leaf) on the fly, and if at any time the sum equals
     * to the target, it is scenario 1, we can increment our count.
     * Or, the problem becomes the two sum problem. Denote the previous stored path sum as prevSum, clearly, currently
     * the currSum doesn't equal to the target sum. However, if currSum - prevSum = targetSum, it means we have a path starts
     * from a middle node and going downwards equals to the target. If there are multiple prevSum, then we will have
     * multiple paths.
     * One pitfall is that the path cannot connect both the left and the right children. Therefore, we need to decrement
     * the frequency of currSum before moving from the left subtree to the right subtree.
     * <p>
     * Time: O(n) we visit each node once
     * Space: O(n) there will be at most n distinct sums as we go through n nodes
     */
    private int count;
    private Map<Integer, Integer> prefixSum;

    public int pathSum(TreeNode root, int targetSum) {
        count = 0;
        prefixSum = new HashMap<>();
        findAllPathSums(root, 0, targetSum);
        return count;
    }

    private void findAllPathSums(TreeNode root, int currSum, int targetSum) {
        if (root == null) return;

        // compute the current path sum on the fly
        currSum += root.val;
        // scenario 1: if the path sum from the root node equals to target
        if (currSum == targetSum) {
            count++;
        }

        // scenario 2: if the path in between equals to the target
        // need to find whether there is a prefix sum equals to currSum - targetSum
        count += prefixSum.getOrDefault(currSum - targetSum, 0);

        // add currSum into the prefix sum map for further usage
        prefixSum.put(currSum, prefixSum.getOrDefault(currSum, 0) + 1);

        // recursively visit left and right subtrees
        findAllPathSums(root.left, currSum, targetSum);
        findAllPathSums(root.right, currSum, targetSum);

        // when one subtree is done - need to remove the currSum
        // since the path connecting both subtrees are not considered as a valid path
        prefixSum.put(currSum, prefixSum.getOrDefault(currSum, 0) - 1);
    }

    @Test
    public void pathSumTest() {
        /**
         * Example 1:
         * Input: root = [10,5,-3,3,2,null,11,3,-2,null,1], targetSum = 8
         * Output: 3
         * Explanation: The paths that sum to 8 are shown.
         *                              10
         *                            /    \
         *                           5     -3
         *                         /  \      \
         *                        3    2      11
         *                       / \    \
         *                      3  -2    1
         * Paths:
         * -3 -> 11
         * 5 -> 2 -> 1
         * 5 -> 3
         */
        TreeNode root1 = new TreeNode(10);
        root1.left = new TreeNode(5);
        root1.right = new TreeNode(-3);
        root1.left.left = new TreeNode(3);
        root1.left.right = new TreeNode(2);
        root1.right.right = new TreeNode(11);
        root1.left.left.left = new TreeNode(3);
        root1.left.left.right = new TreeNode(-2);
        root1.left.right.right = new TreeNode(1);
        assertEquals(3, pathSum(root1, 8));
        /**
         * Example 2:
         * Input: root = [5,4,8,11,null,13,4,7,2,null,null,5,1], targetSum = 22
         * Output: 3
         *                            5
         *                          /   \
         *                         4     8
         *                       /      /  \
         *                      11     13   4
         *                     /  \        /  \
         *                    7    2      5    1
         * Paths:
         * 5 -> 4 -> 11 -> 2
         * 4 -> 11 -> 7
         * 5 -> 8 -> 4 -> 5
         */
        TreeNode root2 = new TreeNode(5);
        root2.left = new TreeNode(4);
        root2.right = new TreeNode(8);
        root2.left.left = new TreeNode(11);
        root2.right.left = new TreeNode(13);
        root2.right.right = new TreeNode(4);
        root2.left.left.left = new TreeNode(7);
        root2.left.left.right = new TreeNode(2);
        root2.right.right.left = new TreeNode(5);
        root2.right.right.right = new TreeNode(1);
        assertEquals(3, pathSum(root2, 22));
    }

    private static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        public TreeNode(int val) {
            this.val = val;
        }
    }
}
