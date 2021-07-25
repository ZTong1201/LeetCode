import org.junit.Test;

import java.util.Stack;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SameTree {

    /**
     * Given two binary trees, write a function to check if they are the same or not.
     * <p>
     * Two binary trees are considered the same if they are structurally identical and the nodes have the same value.
     * <p>
     * Use Recursion!
     * Time: O(N)
     * Space: worst case: O(N) for completely unbalanced tree, average case: O(logN)
     */
    public boolean isSameTreeRecursive(TreeNode p, TreeNode q) {
        if (p == null && q == null) return true;
        if (p == null || q == null) return false;
        if (p.val != q.val) return false;
        return isSameTreeRecursive(p.left, q.left) && isSameTreeRecursive(p.right, q.right);
    }

    /**
     * Use Iteration
     * Time: O(N)
     * Space: O(N) for a completely balanced tree
     */
    public boolean isSameTreeIterative(TreeNode p, TreeNode q) {
        Stack<TreeNode> pNodes = new Stack<>();
        Stack<TreeNode> qNodes = new Stack<>();
        pNodes.push(p);
        qNodes.push(q);

        while (!pNodes.isEmpty() || !qNodes.isEmpty()) {
            TreeNode currP = pNodes.pop();
            TreeNode currQ = qNodes.pop();
            if (currP == null && currQ == null) continue;
            if (currP == null || currQ == null) return false;
            if (currP.val != currQ.val) return false;
            pNodes.push(currP.left);
            pNodes.push(currP.right);
            qNodes.push(currQ.left);
            qNodes.push(currQ.right);
        }
        return true;
    }


    @Test
    public void sameTreeTestRecursive() {
        TreeNode testp1 = new TreeNode(1);
        testp1.left = new TreeNode(2);
        testp1.right = new TreeNode(3);
        TreeNode testq1 = new TreeNode(1);
        testq1.left = new TreeNode(2);
        testq1.right = new TreeNode(3);
        assertTrue(isSameTreeRecursive(testp1, testq1));
        TreeNode testp2 = new TreeNode(1);
        testp2.left = new TreeNode(2);
        TreeNode testq2 = new TreeNode(1);
        testq2.right = new TreeNode(2);
        assertFalse(isSameTreeRecursive(testp2, testq2));
        TreeNode testp3 = new TreeNode(1);
        testp3.left = new TreeNode(2);
        testp3.right = new TreeNode(1);
        TreeNode testq3 = new TreeNode(1);
        testq3.left = new TreeNode(1);
        testq3.right = new TreeNode(2);
        assertFalse(isSameTreeRecursive(testp3, testq3));
    }

    @Test
    public void sameTreeTestIterative() {
        TreeNode testp1 = new TreeNode(1);
        testp1.left = new TreeNode(2);
        testp1.right = new TreeNode(3);
        TreeNode testq1 = new TreeNode(1);
        testq1.left = new TreeNode(2);
        testq1.right = new TreeNode(3);
        assertTrue(isSameTreeIterative(testp1, testq1));
        TreeNode testp2 = new TreeNode(1);
        testp2.left = new TreeNode(2);
        TreeNode testq2 = new TreeNode(1);
        testq2.right = new TreeNode(2);
        assertFalse(isSameTreeIterative(testp2, testq2));
        TreeNode testp3 = new TreeNode(1);
        testp3.left = new TreeNode(2);
        testp3.right = new TreeNode(1);
        TreeNode testq3 = new TreeNode(1);
        testq3.left = new TreeNode(1);
        testq3.right = new TreeNode(2);
        assertFalse(isSameTreeIterative(testp3, testq3));
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
