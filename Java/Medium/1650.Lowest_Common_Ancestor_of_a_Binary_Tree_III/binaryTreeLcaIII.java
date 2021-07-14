import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class binaryTreeLcaIII {

    /**
     * Given two nodes of a binary tree p and q, return their lowest common ancestor (LCA).
     *
     * Each node will have a reference to its parent node. The definition for Node is below:
     *
     * class Node {
     *     public int val;
     *     public Node left;
     *     public Node right;
     *     public Node parent;
     * }
     * According to the definition of LCA on Wikipedia: "The lowest common ancestor of two nodes p and q
     * in a tree T is the lowest node that has both p and q as descendants (where we allow a node to be
     * a descendant of itself)."
     */

    /**
     * Approach 1: Hash Set
     * Store the path from p to the root.
     * Traverse the path from q to the root, the first common point of the two paths is the LCA.
     * <p>
     * Time: O(N)
     * Space: O(N)
     */
    public Node lowestCommonAncestorHashSet(Node p, Node q) {
        Set<Node> parents = new HashSet<>();
        while (p != null) {
            parents.add(p);
            p = p.parent;
        }
        while (!parents.contains(q)) {
            q = q.parent;
        }
        return q;
    }

    /**
     * Two pointers (similar to LeetCode 160: https://leetcode.com/problems/intersection-of-two-linked-lists/)
     * The path between p (or q) and the root can be treated as two linked lists. Hence the problem converts finding
     * the intersection of two lists. Two pointers will meet at the intersection point and they both travel len(A) +
     * len(B) - commonLen(A, B)
     * <p>
     * Time: O(N)
     * Space: O(1)
     */
    public Node lowestCommonAncestorTwoPointers(Node p, Node q) {
        Node p_ptr = p;
        Node q_ptr = q;
        while (p_ptr != q_ptr) {
            p_ptr = p_ptr == null ? q : p_ptr.parent;
            q_ptr = q_ptr == null ? p : q_ptr.parent;
        }
        return p_ptr;
    }

    @Test
    public void lowestCommonAncestorTest() {
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
        Node tree = new Node(3);
        tree.left = new Node(5, tree);
        tree.right = new Node(1, tree);
        tree.left.left = new Node(6, tree.left);
        tree.left.right = new Node(2, tree.left);
        tree.left.right.left = new Node(7, tree.left.right);
        tree.left.right.right = new Node(4, tree.left.right);
        tree.right.left = new Node(0, tree.right);
        tree.right.right = new Node(8, tree.right);
        Node p = tree.left;
        Node q = tree.right;
        Node actual_hashSet = lowestCommonAncestorHashSet(p, q);
        Node actual_twoPointers = lowestCommonAncestorTwoPointers(p, q);
        assertEquals(tree, actual_hashSet);
        assertEquals(tree, actual_twoPointers);
    }


    private class Node {
        public int val;
        public Node left;
        public Node right;
        public Node parent;

        Node(int x) {
            this.val = x;
        }

        Node(int x, Node parent) {
            this.val = x;
            this.parent = parent;
        }
    }
}
