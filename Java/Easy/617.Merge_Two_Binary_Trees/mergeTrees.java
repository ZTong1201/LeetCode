import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class mergeTrees {

    /**
     * Given two binary trees and imagine that when you put one of them to cover the other, some nodes of the two trees are
     * overlapped while the others are not.
     *
     * You need to merge them into a new binary tree. The merge rule is that if two nodes overlap, then sum node values up as
     * the new value of the merged node. Otherwise, the NOT null node will be used as the node of new tree.
     *
     * Note: The merging process must start from the root nodes of both trees.
     *
     * Approach 1: Depth-First Search (Recursion)
     * We can solve this problem using DFS, the easiest way to implement a DFS is using recursion. We can modify the tree on tree1 recursively,
     * if tree1 is not null yet tree2 is null, we keep this tree1 node, otherwise, we assign the tree2 node to tree1. If at the same position,
     * both tree1 and tree2 are not null, we add their value up.
     *
     * Time: O(n), we visited all the nodes once
     * Space: O(h), the call stack will require space up to the height of tree, it will be O(n) in the worst case
     */
    public TreeNode mergeTreesRecursive(TreeNode t1, TreeNode t2) {
        if(t1 == null && t2 == null) return null;
        if(t1 == null || t2 == null) return t1 == null ? t2 : t1;
        t1.val += t2.val;
        t1.left = mergeTreesRecursive(t1.left, t2.left);
        t1.right = mergeTreesRecursive(t1.right, t2.right);
        return t1;
    }

    /**
     * Approach 2: Depth-First Search (Iteration)
     * We can convert the above process into an iteration. By using a queue, we can actually implement DFS iteratively. The only thing
     * need to be cared is that, if we want to assign node from tree2 to tree1, we need to do it at the parent node of that node. Hence,
     * for a given node, we need to check whether any of the two children is null or not.
     */
    public TreeNode mergeTreesIterative(TreeNode t1, TreeNode t2) {
        Stack<TreeNode> tree1 = new Stack<>();
        Stack<TreeNode> tree2 = new Stack<>();
        tree1.push(t1);
        tree2.push(t2);
        while(!tree1.isEmpty() || !tree2.isEmpty()) {
            TreeNode root1 = tree1.isEmpty() ? null : tree1.pop();
            TreeNode root2 = tree2.isEmpty() ? null : tree2.pop();
            //if both nodes are null, do nothing
            if(root1 == null && root2 == null) continue;
            //as long as the node of tree1 is not null, we check tree2 node
            if(root1 != null) {
                //if tree2 node is null, we do nothing, since we only retain the node of tree1
                if(root2 == null) continue;

                //if tree2 node is not null
                if(root2 != null) {
                    //simply add their values
                    root1.val += root2.val;

                    //then check for both children
                    if(root1.left == null) { //if the left child of tree1 node is not null
                        //simply assign tree2's left child to it
                        root1.left = root2.left;
                    } else {
                        //otherwise, push left children into their stack for further search
                        tree1.push(root1.left);
                        tree2.push(root2.left);
                    }

                    //same for the right child
                    if(root1.right == null) {
                        //assign tree2's right child to tree1 as long as tree1's right child is null
                        root1.right = root2.right;
                    } else {
                        //otherwise, push right children in the stack
                        tree1.push(root1.right);
                        tree2.push(root2.right);
                    }
                }
            }
        }
        return t1;
    }


    @Test
    public void mergeTreesRecursiveTest() {
        /**
         * Input:
         * 	Tree 1                     Tree 2
         *           1                         2
         *          / \                       / \
         *         3   2                     1   3
         *        /                           \   \
         *       5                             4   7
         * Output:
         * Merged tree:
         * 	     3
         * 	    / \
         * 	   4   5
         * 	  / \   \
         * 	 5   4   7
         */
        TreeNode tree1 = new TreeNode(1);
        tree1.left = new TreeNode(3);
        tree1.right = new TreeNode(2);
        tree1.left.left = new TreeNode(5);
        TreeNode tree2 = new TreeNode(2);
        tree2.left = new TreeNode(1);
        tree2.right = new TreeNode(3);
        tree2.left.right = new TreeNode(4);
        tree2.right.right = new TreeNode(7);
        TreeNode merged = mergeTreesIterative(tree1, tree2);
        List<Integer> aList = new ArrayList<>();
        preorder(merged, aList);
        int[] actual = listToArray(aList, 6);
        int[] expected = new int[]{3, 4, 5, 4, 5, 7};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void mergeTreesIterativeTest() {
        /**
         * Input:
         * 	Tree 1                     Tree 2
         *           1                         2
         *          / \                       / \
         *         3   2                     1   3
         *        /                           \   \
         *       5                             4   7
         * Output:
         * Merged tree:
         * 	     3
         * 	    / \
         * 	   4   5
         * 	  / \   \
         * 	 5   4   7
         */
        TreeNode tree1 = new TreeNode(1);
        tree1.left = new TreeNode(3);
        tree1.right = new TreeNode(2);
        tree1.left.left = new TreeNode(5);
        TreeNode tree2 = new TreeNode(2);
        tree2.left = new TreeNode(1);
        tree2.right = new TreeNode(3);
        tree2.left.right = new TreeNode(4);
        tree2.right.right = new TreeNode(7);
        TreeNode merged = mergeTreesRecursive(tree1, tree2);
        List<Integer> aList = new ArrayList<>();
        preorder(merged, aList);
        int[] actual = listToArray(aList, 6);
        int[] expected = new int[]{3, 4, 5, 4, 5, 7};
        assertArrayEquals(expected, actual);
    }

    private void preorder(TreeNode root, List<Integer> res) {
        if(root != null) {
            res.add(root.val);
            preorder(root.left, res);
            preorder(root.right, res);
        }
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
