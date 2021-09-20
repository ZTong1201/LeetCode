import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AllNodesDistanceK {

    /**
     * Given the root of a binary tree, the value of a target node target, and an integer k, return an array of the values
     * of all nodes that have a distance k from the target node.
     * <p>
     * You can return the answer in any order.
     * <p>
     * Constraints:
     * <p>
     * The number of nodes in the tree is in the range [1, 500].
     * 0 <= Node.val <= 500
     * All the values Node.val are unique.
     * target is the value of one of the nodes in the tree.
     * 0 <= k <= 1000
     * <p>
     * Approach: BFS
     * Basically, if we treat the tree as a connected graph, we can do BFS starting from the target node and when the distance
     * equals to k, we add all the node values into the result list. However, it's easy to do BFS downward, but there is no
     * way to visit nodes upward starting from the target node. Therefore, we could consider use a hash map to add mapping
     * between current node to its parent. By doing so, we can both move upward and downward from any given node.
     * <p>
     * Time: O(n) we need to traverse the entire tree first to build the map. Then we might still have to visit the entire
     * tree to find all nodes with distance K. Both traversals take O(n) time.
     * Space: O(n)
     */
    public List<Integer> distanceK(TreeNode root, TreeNode target, int k) {
        Map<TreeNode, TreeNode> parentMap = new HashMap<>();
        buildParentMap(root, null, parentMap);

        // run BFS starting from queue
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(target);
        Set<TreeNode> visited = new HashSet<>();
        List<Integer> res = new ArrayList<>();
        int distance = 0;

        while (!queue.isEmpty()) {
            int size = queue.size();

            for (int i = 0; i < size; i++) {
                TreeNode curr = queue.poll();

                // avoid duplicate visits
                if (!visited.contains(curr)) {
                    visited.add(curr);

                    // if the current node has distance k
                    // add it to the final list
                    if (distance == k) {
                        res.add(curr.val);
                    } else {
                        // otherwise, add the adjacent neighbors for further search
                        // avoid adding null into the queue
                        // add neighbors from the downward
                        if (curr.left != null) {
                            queue.add(curr.left);
                        }
                        if (curr.right != null) {
                            queue.add(curr.right);
                        }
                        // search upward to add the parent as well
                        if (parentMap.get(curr) != null) {
                            queue.add(parentMap.get(curr));
                        }
                    }
                }
            }
            // increment the distance after the current level is done
            distance++;
        }
        return res;
    }

    private void buildParentMap(TreeNode root, TreeNode parent, Map<TreeNode, TreeNode> parentMap) {
        if (root == null) return;
        parentMap.put(root, parent);
        buildParentMap(root.left, root, parentMap);
        buildParentMap(root.right, root, parentMap);
    }

    @Test
    public void distanceKTest() {
        /**
         * Input: root = [3,5,1,6,2,0,8,null,null,7,4], target = 5, k = 2
         * Output: [7,4,1]
         * Explanation: The nodes that are a distance 2 from the target node (with value 5) have values 7, 4, and 1.
         *                     3
         *                   /   \
         *                  5     1
         *                /  \   /  \
         *               6   2  0    8
         *                  / \
         *                 7   4
         */
        TreeNode tree1 = new TreeNode(3);
        tree1.left = new TreeNode(5);
        tree1.right = new TreeNode(1);
        tree1.left.left = new TreeNode(6);
        tree1.left.right = new TreeNode(2);
        tree1.right.left = new TreeNode(0);
        tree1.right.right = new TreeNode(8);
        tree1.left.right.left = new TreeNode(7);
        tree1.left.right.right = new TreeNode(4);
        Set<Integer> expected1 = Set.of(7, 4, 1);
        List<Integer> actual1 = distanceK(tree1, tree1.left, 2);
        assertEquals(expected1.size(), actual1.size());
        for (int value : actual1) {
            assertTrue(expected1.contains(value));
        }
        /**
         * Example 2:
         * Input: root = [1], target = 1, k = 3
         * Output: []
         */
        TreeNode tree2 = new TreeNode(1);
        List<Integer> actual2 = distanceK(tree2, tree2, 3);
        assertTrue(actual2.isEmpty());
    }

    private static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        public TreeNode(int x) {
            this.val = x;
        }
    }
}
