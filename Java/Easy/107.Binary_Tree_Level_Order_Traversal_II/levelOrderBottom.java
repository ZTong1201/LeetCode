import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class levelOrderBottom {

    /**
     * Given a binary tree, return the bottom-up level order traversal of its nodes' values.
     * (ie, from left to right, level by level from leaf to root).
     *
     * Approach: Breadth-First Search (BFS)
     * using queue to implement BFS, and use Linked list as a final result list to add element from the front
     */
    public List<List<Integer>> levelOrderBottom(TreeNode root) {
        LinkedList<List<Integer>> res = new LinkedList<>();
        if(root == null) return res;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while(!queue.isEmpty()) {
            int size = queue.size();
            List<Integer> level = new ArrayList<>();
            for(int i = 0; i < size; i++) {
                TreeNode curr = queue.poll();
                level.add(curr.val);
                if(curr.left != null) queue.add(curr.left);
                if(curr.right != null) queue.add(curr.right);
            }
            res.addFirst(level);
        }
        return res;
    }

    @Test
    public void levelOrderBottomTest() {
        /**
         * Given binary tree [3,9,20,null,null,15,7],
         *     3
         *    / \
         *   9  20
         *     /  \
         *    15   7
         * return its bottom-up level order traversal as:
         * [
         *   [15,7],
         *   [9,20],
         *   [3]
         * ]
         */
        TreeNode tree = new TreeNode(3);
        tree.left = new TreeNode(9);
        tree.right = new TreeNode(20);
        tree.right.left = new TreeNode(15);
        tree.right.right = new TreeNode(7);
        List<List<Integer>> actual = levelOrderBottom(tree);
        List<List<Integer>> expected = new ArrayList<>();
        expected.add(new ArrayList<>(Arrays.asList(15, 7)));
        expected.add(new ArrayList<>(Arrays.asList(9, 20)));
        expected.add(new ArrayList<>(Arrays.asList(3)));
        assertEquals(expected.size(), actual.size());
        for(int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).size(), actual.get(i).size());
            for(int j = 0; j < expected.get(i).size(); j++) {
                assertEquals(expected.get(i).get(j), actual.get(i).get(j));
            }
        }
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
