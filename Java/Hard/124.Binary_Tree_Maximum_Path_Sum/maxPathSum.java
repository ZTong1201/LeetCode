import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class maxPathSum {

    /**
     * Given a non-empty binary tree, find the maximum path sum.
     *
     * For this problem, a path is defined as any sequence of nodes from some starting node to any node in the tree along the
     * parent-child connections. The path must contain at least one node and does not need to go through the root.
     *
     * Approach 1: Brute Force
     * 因为题中给定的path不一定要通过root节点，因此可以对于每个节点，计算它左子树中path sum的最大值，和右子树的path sum最大值。若左右子树的path sum最大值
     * 和当前节点的值之和大于先前记录的maximum path sum，则更新最大值。因为对于每个节点都做了一遍左右子树遍历。因此其实是枚举了所有节点的path sum，取其中
     * 的最大值。
     *
     * Time: O(N^2) 对每个节点都需要遍历其左右子树所有节点计算最大值。所以对每个节点，最坏情况下都需要遍历O(N)个节点。
     * Space: O(H)，对每个节点都需要遍历至其最深的子树，因此call stack和为了遍历每个节点用的stack都需要O(H)空间
     */
    private int max;

    public int maxPathSumBruteForce(TreeNode root) {
        this.max = Integer.MIN_VALUE;
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        while(!stack.isEmpty()) {
            TreeNode curr = stack.pop();
            int[] leftMax = new int[]{Integer.MIN_VALUE};
            int[] rightMax = new int[]{Integer.MIN_VALUE};
            subtreeSum(curr.left, leftMax, 0);
            subtreeSum(curr.right, rightMax, 0);
            if(leftMax[0] + rightMax[0] + curr.val > this.max) {
                this.max = leftMax[0] + rightMax[0] + curr.val;
            }
            if(curr.left != null) stack.push(curr.left);
            if(curr.right != null) stack.push(curr.right);
        }
        return this.max;
    }

    private void subtreeSum(TreeNode root, int[] res, int sum) {
        if(sum > res[0]) {
            res[0] = sum;
        }
        if(root == null) {
            return;
        }
        subtreeSum(root.left, res, sum + root.val);
        subtreeSum(root.right, res, sum + root.val);
    }

    /**
     * Approach 2: Better Recursion
     * 其实不用对于每个节点都进行一次DFS。本质上，对于每个节点，当前节点记录的值应该是该节点对于再往上走的path sum的最大贡献。该最大贡献只能是三者其一，
     * 1.当前节点值加上其左孩子对path sum的最大贡献，说明该条path可以一直延伸到其左孩子
     * 2.当前节点值加上其右孩子对path sum的最大贡献，说明该条path可以一直延伸到其右孩子
     * 因为若sum最大的path还能往上延伸，其左右两条path只能有其中一个对该条path做出贡献，否则不符合path的定义
     * 3.当前节点值本身。（这种情况下，加上其左孩子或右孩子会减少当前sum，因此sum最大的path到当前节点就结束了）
     * 得到当前节点的最大贡献后，可以更新一次最终结果，因为上述三种情况都符合path的定义
     *
     * 除此之外，还有一种可能是该条path不会再往上延伸，而是以当前节点为parent节点，有一条path从其左孩子经过当前节点再到其右孩子。因此还有一种可能值为
     * 左孩子最大贡献 + 右孩子最大贡献 + 当前节点值。将其和最终结果进行比较，更新最终结果。
     *
     * Time: O(N) 对整个树做dfs，自leaf到root，每个节点只遍历了一遍
     * Space: O(H) 为了进行dfs，call stack需要O(H)的空间
     */
    private int res;

    public int maxPathSumRecursive(TreeNode root) {
        res = Integer.MIN_VALUE;
        dfs(root);
        return res;
    }

    //helper函数，返回每个节点对path的最大贡献，并更新最终结果
    private int dfs(TreeNode root) {
        //base case，若节点为null，最大贡献为0
        if(root == null) {
            return 0;
        }
        //递归访问左子树和右子树，返回的是每个节点对最终path最大贡献值
        int leftMax = dfs(root.left);
        int rightMax = dfs(root.right);
        //如上述讨论，每个节点的最大贡献值只能是三者之一，即当前节点值加上左孩子的最大贡献，或节点值加上右孩子的最大贡献，或该节点值本身
        int path = Math.max(root.val, root.val + Math.max(leftMax, rightMax));
        //更新当前最大path sum
        res = Math.max(res, path);
        //或者另一种可能是，最终的path sum不会经过更往上的节点，而是以当前节点为parent节点，延伸至其左右子树
        res = Math.max(res, root.val + leftMax + rightMax);
        //注意！每个节点还是要得到它对整个path的最大贡献值，上一步只是更新可能的最大结果
        return path;
    }

    @Test
    public void maxPathSumTest() {
        /**
         * Example 1:
         * Input: [1,2,3]
         *
         *        1
         *       / \
         *      2   3
         *
         * Output: 6
         */
        TreeNode tree1 = new TreeNode(1);
        tree1.left = new TreeNode(2);
        tree1.right = new TreeNode(3);
        assertEquals(6, maxPathSumBruteForce(tree1));
        /**
         * Example 2:
         * Input: [-10,9,20,null,null,15,7]
         *
         *    -10
         *    / \
         *   9  20
         *     /  \
         *    15   7
         *
         * Output: 42
         */
        TreeNode tree2 = new TreeNode(-10);
        tree2.left = new TreeNode(9);
        tree2.right = new TreeNode(20);
        tree2.right.left = new TreeNode(15);
        tree2.right.right = new TreeNode(7);
        assertEquals(42, maxPathSumBruteForce(tree2));
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
