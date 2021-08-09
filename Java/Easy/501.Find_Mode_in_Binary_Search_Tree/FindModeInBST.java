import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;

public class FindModeInBST {

    /**
     * Given the root of a binary search tree (BST) with duplicates, return all the mode(s) (i.e., the most frequently
     * occurred element) in it.
     * <p>
     * If the tree has more than one mode, return them in any order.
     * <p>
     * Assume a BST is defined as follows:
     * <p>
     * The left subtree of a node contains only nodes with keys less than or equal to the node's key.
     * The right subtree of a node contains only nodes with keys greater than or equal to the node's key.
     * Both the left and right subtrees must also be binary search trees.
     * <p>
     * Constraints:
     * <p>
     * The number of nodes in the tree is in the range [1, 10^4].
     * -10^5 <= Node.val <= 10^5
     * <p>
     * Approach 1: Hash Map
     * While traversing the tree, keep a map to record <num, freq> for each unique element and update the maximum frequency as
     * well. After traversal, search in the map to find all elements whose frequency equals to the maximum.
     * <p>
     * Time: O(n)
     * Space: O(n)
     */
    private int maxFreq;

    public int[] findModeHashMap(TreeNode root) {
        maxFreq = 0;
        Map<Integer, Integer> map = new HashMap<>();
        preorder(root, map);
        List<Integer> res = new ArrayList<>();
        for (int num : map.keySet()) {
            if (map.get(num) == maxFreq) res.add(num);
        }
        int[] modes = new int[res.size()];
        for (int i = 0; i < modes.length; i++) {
            modes[i] = res.get(i);
        }
        return modes;
    }

    private void preorder(TreeNode root, Map<Integer, Integer> map) {
        if (root == null) return;
        map.put(root.val, map.getOrDefault(root.val, 0) + 1);
        maxFreq = Math.max(maxFreq, map.get(root.val));
        preorder(root.left, map);
        preorder(root.right, map);
    }

    /**
     * Approach 2: Inorder traversal
     * The above approach doesn't take advantage of the BST property. Essentially, for a BST with duplicate values, we could
     * get a non-decreasing array if inorder traversing the tree. That's being said, all the duplicate values will be grouped
     * together. In order to further optimize the memory performance, we could use a pointer "prev" to keep track of the node
     * before current node being visited. If its value equals to the previous node's, we increment the count, otherwise, we
     * update the mode list.
     * <p>
     * Time: O(n)
     * Space: O(1) if the recursion call stack is not taken into account
     */
    private int count, maxCount;
    private TreeNode prev;

    public int[] findModeInorder(TreeNode root) {
        // initialize count as 1 since any element will appear at least once
        count = 1;
        maxCount = 0;
        List<Integer> res = new ArrayList<>();
        // inorder traverse the BST
        inorder(root, res);
        int[] modes = new int[res.size()];
        for (int i = 0; i < modes.length; i++) {
            modes[i] = res.get(i);
        }
        return modes;
    }

    private void inorder(TreeNode root, List<Integer> res) {
        if (root == null) return;
        inorder(root.left, res);
        if (prev != null) {
            // if we have duplicate elements, increment the count
            if (root.val == prev.val) count++;
                // otherwise, re-initialize the count
            else count = 1;
        }
        // update modes if we have a greater count
        if (count > maxCount) {
            maxCount = count;
            res.clear();
            res.add(root.val);
        } else if (count == maxCount) {
            // append new element to the current list
            // since we might have multiple modes
            res.add(root.val);
        }
        // update previous node
        prev = root;
        inorder(root.right, res);
    }

    @Test
    public void findModeTest() {
        /**
         * Example 1:
         * Input: root = [1,null,2,2]
         * Output: [2]
         *                   1
         *                    \
         *                     2
         *                    /
         *                   2
         */
        TreeNode tree1 = new TreeNode(1);
        tree1.right = new TreeNode(2);
        tree1.right.left = new TreeNode(2);
        int[] expected1 = new int[]{2};
        int[] actualHashMap1 = findModeHashMap(tree1);
        int[] actualInorder1 = findModeInorder(tree1);
        assertArrayEquals(expected1, actualHashMap1);
        assertArrayEquals(expected1, actualInorder1);
        /**
         * Example 2:
         * Input: root = [1,null,2]
         * Output: [1,2]
         *                 1
         *                  \
         *                   2
         */
        TreeNode tree2 = new TreeNode(1);
        tree2.right = new TreeNode(2);
        int[] expected2 = new int[]{1, 2};
        int[] actualHashMap2 = findModeHashMap(tree2);
        Arrays.sort(actualHashMap2);
        int[] actualInorder2 = findModeInorder(tree2);
        Arrays.sort(actualInorder2);
        assertArrayEquals(expected2, actualHashMap2);
        assertArrayEquals(expected2, actualInorder2);
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
