import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class postorder {
    /**
     * Given a binary tree, return the postorder traversal of its nodes' values.
     * Follow up: Recursive solution is trivial, could you do it iteratively?
     *
     * Approach 1: Stack
     * Adding node in stacks will be from top -> bottom, and from left -> right. However, postorder traversal will return values
     * from bottom -> top, and from left -> right, the output list will be reverted at the end of loop
     *
     * Time: O(N) we need to visit all nodes once
     * Space: O(H), we need a stack to store nodes which may require up to the height of tree space, in the worst case, it will be O(N)
     */
    public List<Integer> postorderTraversal(TreeNode root) {
        LinkedList<Integer> res = new LinkedList<>();//use linked list to add value at the front
        if(root == null) return res;
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        while(!stack.isEmpty()) {
            TreeNode curr = stack.pop();
            res.addFirst(curr.val);
            if(curr.left != null) stack.push(curr.left);
            if(curr.right != null) stack.push(curr.right);
        }
        return res;
    }

    /**
     * Approach 2: Morris Traversal
     * 1.先建立一个临时结点dummy，并令其左孩子为根结点root，将当前结点设置为dummy；
     * 2.如果当前结点的左孩子为空，则将其右孩子作为当前结点；
     * 3.如果当前结点的左孩子不为空，则找到其在中序遍历中的前驱结点
     *   3.1如果前驱结点的右孩子为空，将它的右孩子设置为当前结点，将当前结点更新为当前结点的左孩子；
     *   3.2如果前驱结点的右孩子为当前结点，倒序输出从当前结点的左孩子到该前驱结点这条路径上所有的结点。将前驱结点的右孩子设置为空，将当前结点更新为当前结点的右孩子。
     * 4.重复以上过程，直到当前结点为空。
     *
     *
     * Time: O(N), only the predecessor will be visited twice, other nodes will only be visited once
     * Space: O(1) if the output list doesn't account for space complexity, we only assign current node and the predecessor node during
     *       iteration
     */
    public List<Integer> morrisTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        TreeNode curr = new TreeNode(0);
        curr.left = root;
        while(curr != null) {
            if(curr.left == null) {
                curr = curr.right;
            } else {
                TreeNode predecessor = curr.left;
                while(predecessor.right != null && predecessor.right != curr) {
                    predecessor = predecessor.right;
                }

                if(predecessor.right == null) {
                    predecessor.right = curr;
                    curr = curr.left;
                } else {
                    addReverse(curr.left, predecessor, res);
                    predecessor.right = null;
                    curr = curr.right;
                }
            }
        }
        return res;
    }

    private void reverseOrder(TreeNode start, TreeNode end) {
        if(start == end) return;
        TreeNode x = start;
        TreeNode y = x.right;
        while(x != end) {
            TreeNode temp = y.right;
            y.right = x;
            x = y;
            y = temp;
        }
    }

    private void addReverse(TreeNode curr, TreeNode prev, List<Integer> res) {
        reverseOrder(curr, prev);

        TreeNode temp = prev;
        while(true) {
            res.add(temp.val);
            if(temp == curr) break;
            temp = temp.right;
        }

        reverseOrder(prev, curr);
    }

    @Test
    public void postorderTraversalTest() {
        /**
         * Example:
         * Input: [1,null,2,3]
         *    1
         *     \
         *      2
         *     /
         *    3
         *
         * Output: [3,2,1]
         */
        TreeNode tree = new TreeNode(1);
        tree.right = new TreeNode(2);
        tree.right.left = new TreeNode(3);
        List<Integer> res = postorderTraversal(tree);
        int[] expected = new int[]{3, 2, 1};
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
         * Output: [3,2,1]
         */
        TreeNode tree = new TreeNode(1);
        tree.right = new TreeNode(2);
        tree.right.left = new TreeNode(3);
        List<Integer> res = morrisTraversal(tree);
        int[] expected = new int[]{3, 2, 1};
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
