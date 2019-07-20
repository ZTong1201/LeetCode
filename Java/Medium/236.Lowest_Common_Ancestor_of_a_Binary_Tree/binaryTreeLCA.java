import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class binaryTreeLCA {

    /**
     * Given a binary tree, find the lowest common ancestor (LCA) of two given nodes in the tree.
     *
     * According to the definition of LCA on Wikipedia: “The lowest common ancestor is defined between two nodes p and q as the lowest node
     * in T that has both p and q as descendants (where we allow a node to be a descendant of itself).”
     *
     * Approach 1: Hash Map
     * Mapping from a node to its parent node as we visited all the nodes. If occasionally two nodes have a same parent node, that is the
     * desired LCA. Otherwise, keep finding all the parent node for one node and record them in a set. If at any time, the other node's
     * parent node in the set, that is the LCA.
     *
     * Time: O(N + 2H), since we visit all the nodes first, which cost O(n), and then finding all the parent node for a given node would be
     *       O(H), where H is the height of the tree. In the worst case, it would be O(3N) = O(N)
     * Space: O(N + H), hash map require O(N) space, the set requires O(H) space, in the worst case it requires O(2N) = O(N)
     */
    public TreeNode lowestCommonAncestorHashMap(TreeNode root, TreeNode p, TreeNode q) {
        Map<TreeNode, TreeNode> childToParent = new HashMap<>();
        //The root node doesn't have a parent node, so assign null to it
        childToParent.put(root, null);
        dfs(childToParent, root);
        if(childToParent.get(p) == childToParent.get(q)) {
            return childToParent.get(p);
        }
        Set<TreeNode> parents = new HashSet<>();
        if(p != null) {
            parents.add(p);
            p = childToParent.get(p);
        }
        while(!parents.contains(q)) {
            q = childToParent.get(q);
        }
        return q;
    }

    private void dfs(Map<TreeNode, TreeNode> map, TreeNode root) {
        if(root == null) return;
        if(root.left != null) {
            map.put(root.left, root);
        }
        if(root.right != null) {
            map.put(root.right, root);
        }
        dfs(map, root.left);
        dfs(map, root.right);
    }

    /**
     * Approach 2: Iteration
     * Implement DFS using iteration (with stack), can stop searching when we find p and q to save time and space
     *
     * Time: O(N)
     * Space: O(N)
     */
    public TreeNode lowestCommonAncestorIterative(TreeNode root, TreeNode p, TreeNode q) {
        Stack<TreeNode> stack = new Stack<>();
        Map<TreeNode, TreeNode> parent = new HashMap<>();
        stack.push(root);
        parent.put(root, null);

        //Iterate until we found both p and q
        while(!parent.containsKey(p) || !parent.containsKey(q)) {
            TreeNode curr = stack.pop();

            if(curr.left != null) {
                parent.put(curr.left, curr);
                stack.push(curr.left);
            }
            if(curr.right != null) {
                parent.put(curr.right, curr);
                stack.push(curr.right);
            }
        }

        Set<TreeNode> ancestors = new HashSet<>();
        while(p != null) {
            ancestors.add(p);
            p = parent.get(p);
        }

        while(!ancestors.contains(q)) {
            q = parent.get(q);
        }
        return q;
    }

    /**
     * Approach 3: Recursion
     * Need three boolean variables left, right and self corresponding to given the current node (root), whether p or q is in the left
     * subtree, the right subtree or just itself. If any two of these three variables are true, the current node is the LCA.
     *
     * Time: O(N) in the worst case, we visited all nodes
     * Space: O(H), where H is the height of the tree, the call stack requires up to O(H) space, in the worst case, it is O(N)
     */
    public TreeNode lowestCommonAncestorRecursive(TreeNode root, TreeNode p, TreeNode q) {
        //use a array to avoid global variables
        TreeNode[] res = new TreeNode[1];
        helper(res, root, p, q);
        return res[0];
    }

    private boolean helper(TreeNode[] res, TreeNode root, TreeNode p, TreeNode q) {
        //base case, if we reach a null node without finding p or q, return false;
        if(root == null) {
            return false;
        }
        boolean left = helper(res, root.left, p, q); //check left subtree
        boolean right = helper(res, root.right, p, q);//check right subtree
        boolean self = (root == p) || (root == q); //check itself
        //if two of them are true, the current root node is the LCA
        if(self && left || self && right || left && right) {
            res[0] = root;
        }
        return self || left || right; //if we found p or q, when backtrack, that subtree will always return true
    }

    /**
     * Approach 4: Directly search for LCA (Recursion)
     *
     * Time: O(N)
     * Space: O(H)
     */
    public TreeNode lowestCommonAncestorDirect(TreeNode root, TreeNode p, TreeNode q) {
        //base case, if the current root is null, return null
        if(root == null) {
            return null;
        }
        //if we found p or q, return that node
        if(root == p || root == q) {
            return root;
        }

        //keep searching left subtree and the right subtree for p and q
        TreeNode left = lowestCommonAncestorDirect(root.left, p, q);
        TreeNode right = lowestCommonAncestorDirect(root.right, p, q);
        //if left and right are both not null, we found LCA, return the current node
        if(left != null && right != null) {
            return root;
        }

        //otherwise, if one part is null, return the other part for further searching
        return left == null ? right : left;
    }

    @Test
    public void lowestCommonAncestorDirectTest() {
        /**
         * Example 1:
         * Input: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 1
         *                          3
         *                        /   \
         *                       5    1
         *                      / \  / \
         *                     6  2 0   8
         *                       / \
         *                      7  4
         * Output: 3
         * Explanation: The LCA of nodes 5 and 1 is 3.
         */
        TreeNode tree = new TreeNode(3);
        tree.left = new TreeNode(5);
        tree.right = new TreeNode(1);
        tree.left.left = new TreeNode(6);
        tree.left.right = new TreeNode(2);
        tree.right.left = new TreeNode(0);
        tree.right.right = new TreeNode(8);
        tree.left.right.left = new TreeNode(7);
        tree.left.right.right = new TreeNode(4);
        TreeNode p1 = tree.left;
        TreeNode q1 = tree.right;
        TreeNode expected1 = tree;
        TreeNode actual1 = lowestCommonAncestorDirect(tree, p1, q1);
        assertEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 4
         *                          3
         *                        /   \
         *                       5    1
         *                      / \  / \
         *                     6  2 0   8
         *                       / \
         *                      7  4
         * Output: 5
         * Explanation: The LCA of nodes 5 and 4 is 5.
         */
        TreeNode p2 = tree.left;
        TreeNode q2 = tree.left.right.right;
        TreeNode expected2 = tree.left;
        TreeNode actual2 = lowestCommonAncestorDirect(tree, p2, q2);
        assertEquals(expected2, actual2);
    }

    @Test
    public void lowestCommonAncestorRecursiveTest() {
        /**
         * Example 1:
         * Input: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 1
         *                          3
         *                        /   \
         *                       5    1
         *                      / \  / \
         *                     6  2 0   8
         *                       / \
         *                      7  4
         * Output: 3
         * Explanation: The LCA of nodes 5 and 1 is 3.
         */
        TreeNode tree = new TreeNode(3);
        tree.left = new TreeNode(5);
        tree.right = new TreeNode(1);
        tree.left.left = new TreeNode(6);
        tree.left.right = new TreeNode(2);
        tree.right.left = new TreeNode(0);
        tree.right.right = new TreeNode(8);
        tree.left.right.left = new TreeNode(7);
        tree.left.right.right = new TreeNode(4);
        TreeNode p1 = tree.left;
        TreeNode q1 = tree.right;
        TreeNode expected1 = tree;
        TreeNode actual1 = lowestCommonAncestorRecursive(tree, p1, q1);
        assertEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 4
         *                          3
         *                        /   \
         *                       5    1
         *                      / \  / \
         *                     6  2 0   8
         *                       / \
         *                      7  4
         * Output: 5
         * Explanation: The LCA of nodes 5 and 4 is 5.
         */
        TreeNode p2 = tree.left;
        TreeNode q2 = tree.left.right.right;
        TreeNode expected2 = tree.left;
        TreeNode actual2 = lowestCommonAncestorRecursive(tree, p2, q2);
        assertEquals(expected2, actual2);
    }

    @Test
    public void lowestCommonAncestorIterativeTest() {
        /**
         * Example 1:
         * Input: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 1
         *                          3
         *                        /   \
         *                       5    1
         *                      / \  / \
         *                     6  2 0   8
         *                       / \
         *                      7  4
         * Output: 3
         * Explanation: The LCA of nodes 5 and 1 is 3.
         */
        TreeNode tree = new TreeNode(3);
        tree.left = new TreeNode(5);
        tree.right = new TreeNode(1);
        tree.left.left = new TreeNode(6);
        tree.left.right = new TreeNode(2);
        tree.right.left = new TreeNode(0);
        tree.right.right = new TreeNode(8);
        tree.left.right.left = new TreeNode(7);
        tree.left.right.right = new TreeNode(4);
        TreeNode p1 = tree.left;
        TreeNode q1 = tree.right;
        TreeNode expected1 = tree;
        TreeNode actual1 = lowestCommonAncestorIterative(tree, p1, q1);
        assertEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 4
         *                          3
         *                        /   \
         *                       5    1
         *                      / \  / \
         *                     6  2 0   8
         *                       / \
         *                      7  4
         * Output: 5
         * Explanation: The LCA of nodes 5 and 4 is 5.
         */
        TreeNode p2 = tree.left;
        TreeNode q2 = tree.left.right.right;
        TreeNode expected2 = tree.left;
        TreeNode actual2 = lowestCommonAncestorIterative(tree, p2, q2);
        assertEquals(expected2, actual2);
    }

    @Test
    public void lowestCommonAncestorHashMapTest() {
        /**
         * Example 1:
         * Input: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 1
         *                          3
         *                        /   \
         *                       5    1
         *                      / \  / \
         *                     6  2 0   8
         *                       / \
         *                      7  4
         * Output: 3
         * Explanation: The LCA of nodes 5 and 1 is 3.
         */
        TreeNode tree = new TreeNode(3);
        tree.left = new TreeNode(5);
        tree.right = new TreeNode(1);
        tree.left.left = new TreeNode(6);
        tree.left.right = new TreeNode(2);
        tree.right.left = new TreeNode(0);
        tree.right.right = new TreeNode(8);
        tree.left.right.left = new TreeNode(7);
        tree.left.right.right = new TreeNode(4);
        TreeNode p1 = tree.left;
        TreeNode q1 = tree.right;
        TreeNode expected1 = tree;
        TreeNode actual1 = lowestCommonAncestorHashMap(tree, p1, q1);
        assertEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 4
         *                          3
         *                        /   \
         *                       5    1
         *                      / \  / \
         *                     6  2 0   8
         *                       / \
         *                      7  4
         * Output: 5
         * Explanation: The LCA of nodes 5 and 4 is 5.
         */
        TreeNode p2 = tree.left;
        TreeNode q2 = tree.left.right.right;
        TreeNode expected2 = tree.left;
        TreeNode actual2 = lowestCommonAncestorHashMap(tree, p2, q2);
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
