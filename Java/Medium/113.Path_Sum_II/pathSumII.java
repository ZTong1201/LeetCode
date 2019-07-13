import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class pathSumII {

    /**
     * Given a binary tree and a sum, find all root-to-leaf paths where each path's sum equals the given sum.
     *
     * Note: A leaf is a node with no children.
     *
     * Approach: Depth-first Search (Backtracking)
     * Since we are finding all the path sum from root to the leaf, the DFS would be a proper approach. We can keep adding node value into
     * a path list, and subtracting the target sum with current node value. If when we reach a leaf node, and current target sum equals
     * to the value of leaf node, we find a desired path. Then we copy the entire path list and add it to the result list. When we done
     * searching for one subtree, we pop the last element out, i.e. we backtrack to the previous state and keep searching the right tree.
     *
     *
     * Time: O(n), we traverse all the node once using DFS
     * Space: O(H), (if the result list doesn't account for space complexity) where H is the height of the tree.
     */
    public List<List<Integer>> pathSum(TreeNode root, int sum) {
        List<List<Integer>> res = new ArrayList<>();
        List<Integer> path = new ArrayList<>();
        dfs(root, res, path, sum);
        return res;
    }

    private void dfs(TreeNode root, List<List<Integer>> res, List<Integer> path, int sum) {
        //add current node value to the path list
        path.add(root.val);
        if(root.left == null && root.right == null) {  //if we reach a leaf node
            if(sum == root.val) {  //if we find a path equals to target sum
                res.add(new ArrayList<>(path));
            }
        } else {   //otherwise, it is an inner node, we need to keep searching its left and right subtree by subtracting current node value
            //from the target sum. However, we only consider non-null nodes
            if(root.left != null) dfs(root.left, res, path, sum - root.val);
            if(root.right != null) dfs(root.right, res, path, sum - root.val);
        }
        path.remove(path.size() - 1);  //we backtrack to the previous state and restart searching
    }

    @Test
    public void pathSumTest() {
        /**
         * Example 1:
         * Given the below binary tree and sum = 22,
         *
         *       5
         *      / \
         *     4   8
         *    /   / \
         *   11  13  4
         *  /  \    / \
         * 7    2  5   1
         *
         * Return:
         *
         * [
         *    [5,4,11,2],
         *    [5,8,4,5]
         * ]
         */
        TreeNode tree1 = new TreeNode(5);
        tree1.left = new TreeNode(4);
        tree1.right = new TreeNode(8);
        tree1.left.left = new TreeNode(11);
        tree1.right.left = new TreeNode(13);
        tree1.right.right = new TreeNode(4);
        tree1.left.left.left = new TreeNode(7);
        tree1.left.left.right = new TreeNode(2);
        tree1.right.right.left = new TreeNode(5);
        tree1.right.right.right = new TreeNode(1);
        List<List<Integer>> actual1 = pathSum(tree1, 22);
        List<List<Integer>> expected1 = new ArrayList<>();
        expected1.add(new ArrayList<>(Arrays.asList(5, 4, 11, 2)));
        expected1.add(new ArrayList<>(Arrays.asList(5, 8, 4, 5)));
        assertEquals(expected1.size(), actual1.size());
        for(int i = 0; i < expected1.size(); i++) {
            assertEquals(expected1.get(i).size(), actual1.get(i).size());
            for(int j = 0; j < expected1.get(i).size(); j++) {
                assertEquals(expected1.get(i).get(j), actual1.get(i).get(j));
            }
        }
        /**
         * Example 2:
         *
         * Given the below binary tree and sum = 1
         *
         *       1
         *      /
         *     2
         *
         *  Return:
         *
         *  []
         */
        TreeNode tree2 = new TreeNode(1);
        tree2.left = new TreeNode(2);
        List<List<Integer>> actual2 = pathSum(tree2, 1);
        List<List<Integer>> expected2 = new ArrayList<>();
        assertEquals(expected2, actual2);
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
