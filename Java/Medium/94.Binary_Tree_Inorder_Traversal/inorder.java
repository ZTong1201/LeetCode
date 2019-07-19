import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class inorder {
    /**
     * Given a binary tree, return the inorder traversal of its nodes' values.
     * Follow up: Recursive solution is trivial, could you do it iteratively?
     *
     * Approach 1: Stack
     *
     * Time: O(N) we need to visit all nodes once
     * Space: O(H), we need a stack to store nodes which may require up to the height of tree space, in the worst case, it will be O(N)
     */
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();
        TreeNode curr = root;
        while(curr != null || !stack.isEmpty()) {
            while(curr != null) {
                stack.push(curr);
                curr = curr.left;
            }
            curr = stack.pop();
            res.add(curr.val);
            curr = curr.right;
        }
        return res;
    }

    /**
     * Approach 2: Morris Traversal
     * Keep searching the predecessor for each node, and visit each predecessor twice to decide whether to go left or go right.
     * Add current node value to the result list when we visit the node the second time, which means we have traverse all the left subtree
     * for that given node
     *
     * Time: O(N), only the predecessor will be visited twice, other nodes will only be visited once
     * Space: O(1) if the output list doesn't account for space complexity, we only assign current node and the predecessor node during
     *       iteration
     */
    public List<Integer> morrisTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        TreeNode node = root;
        while(node != null) {
            //if node.left is null, which means we have visit all the left subtree for that node
            if(node.left == null) {
                //simply add current node value to the final list, and move to the right
                res.add(node.val);
                node = node.right;
            } else {
                //otherwise, find the predecessor of the current node
                TreeNode predecessor = node.left;
                while(predecessor.right != null && predecessor.right != node) {
                    predecessor = predecessor.right;
                }

                if(predecessor.right == null) { //visit the predecessor the first time
                    //simply assign current node to its right child
                    predecessor.right = node;
                    //then keep traverse the left subtree
                    node = node.left;
                } else {  //otherwise, we visit the predecessor the second time
                    //add current node value to the final list
                    res.add(node.val);
                    //recover the predecessor's right child back to null
                    predecessor.right = null;
                    //done left subtree, traverse the right subtree of current node
                    node = node.right;
                }
            }
        }
        return res;
    }

    @Test
    public void inorderTraversalTest() {
        /**
         * Example:
         * Input: [1,null,2,3]
         *    1
         *     \
         *      2
         *     /
         *    3
         *
         * Output: [1,3,2]
         */
        TreeNode tree = new TreeNode(1);
        tree.right = new TreeNode(2);
        tree.right.left = new TreeNode(3);
        List<Integer> res = inorderTraversal(tree);
        int[] expected = new int[]{1, 3, 2};
        int[] actual = listToArray(res, 3);
        assertArrayEquals(expected, actual);

    }

    @Test
    public void morrisTraversalTest() {
        /**
         * Example:
         * Input: [1,null,2,3]
         *    1
         *     \
         *      2
         *     /
         *    3
         *
         * Output: [1,3,2]
         */
        TreeNode tree = new TreeNode(1);
        tree.right = new TreeNode(2);
        tree.right.left = new TreeNode(3);
        List<Integer> res = morrisTraversal(tree);
        int[] expected = new int[]{1, 3, 2};
        int[] actual = listToArray(res, 3);
        assertArrayEquals(expected, actual);
    }

    private int[] listToArray(List<Integer> aList, int length) {
        int[] res = new int[length];
        for(int i = 0; i < length; i++) {
            res[i] = aList.get(i);
        }
        return res;
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
