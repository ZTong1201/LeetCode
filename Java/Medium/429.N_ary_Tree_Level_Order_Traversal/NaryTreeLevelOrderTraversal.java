import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static org.junit.Assert.assertEquals;

public class NaryTreeLevelOrderTraversal {

    /**
     * Given an n-ary tree, return the level order traversal of its nodes' values.
     * <p>
     * Nary-Tree input serialization is represented in their level order traversal, each group of children is separated
     * by the null value (See examples).
     * <p>
     * Approach 1: Iterative (queue)
     * <p>
     * Time: O(n) need to visit all nodes
     * Space: O(n) since the queue will contain two layers of nodes - for a balanced tree, this will be proportional to N
     */
    public List<List<Integer>> levelOrderIterative(Node root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) return res;
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            List<Integer> level = new ArrayList<>();
            // The size of queue will be the number of
            // nodes at current level
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                Node curr = queue.poll();
                level.add(curr.val);
                // add all children in the queue
                queue.addAll(curr.children);
            }
            res.add(level);
        }
        return res;
    }

    /**
     * Approach 2: Recursive
     * Usually level order traversal is not feasible by recursion. However, we can append node values to desired
     * level list whose order has been pre-determined already.
     * <p>
     * Time: O(n)
     * Space: O(n) in the worst case, O(logn) on average
     */
    public List<List<Integer>> levelOrderRecursive(Node root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) return res;
        traverseNodes(root, 0, res);
        return res;
    }

    private void traverseNodes(Node root, int level, List<List<Integer>> res) {
        // base case, if reach a new level, create a list as a placeholder
        if (res.size() <= level) {
            res.add(new ArrayList<>());
        }

        // add node value into the level list
        res.get(level).add(root.val);
        // traverse all the child nodes for current node
        for (Node child : root.children) {
            // the level value needs to be incremented accordingly
            traverseNodes(child, level + 1, res);
        }
    }

    @Test
    public void levelOrderTest() {
        /**
         * Example:
         * Input: root = [1,null,3,2,4,null,5,6]
         * Output: [[1],[3,2,4],[5,6]]
         *                        1
         *                     /  |  \
         *                    3   2   4
         *                   / \
         *                  5  6
         */
        List<List<Integer>> expected = List.of(List.of(1), List.of(3, 2, 4), List.of(5, 6));
        Node root = new Node(1, List.of(new Node(3, List.of(new Node(5, List.of()), new Node(6, List.of()))),
                new Node(2, List.of()), new Node(4, List.of())));
        List<List<Integer>> actualIterative = levelOrderIterative(root);
        assertEquals(expected.size(), actualIterative.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).size(), actualIterative.get(i).size());
            for (int j = 0; j < expected.get(i).size(); j++) {
                assertEquals(expected.get(i).get(j), actualIterative.get(i).get(j));
            }
        }
        List<List<Integer>> actualRecursive = levelOrderRecursive(root);
        assertEquals(expected.size(), actualRecursive.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).size(), actualRecursive.get(i).size());
            for (int j = 0; j < expected.get(i).size(); j++) {
                assertEquals(expected.get(i).get(j), actualRecursive.get(i).get(j));
            }
        }
    }

    private static class Node {
        public int val;
        public List<Node> children;

        public Node(int _val) {
            val = _val;
        }

        public Node(int _val, List<Node> _children) {
            val = _val;
            children = _children;
        }
    }

}
