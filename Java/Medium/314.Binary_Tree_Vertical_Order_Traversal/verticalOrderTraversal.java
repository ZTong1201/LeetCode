import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class verticalOrderTraversal {

    /**
     * Given a binary tree, return the vertical order traversal of its nodes' values. (ie, from top to bottom, column by column).
     *
     * If two nodes are in the same row and column, the order should be from left to right.
     *
     * Approach 1: Tree Map
     *
     * Notice that when we move to the left of tree, the corresponding column decrement, if move to the right subtree, the column increment.
     * Hence we can build a new class which contains the node information and its corresponding column. Then we use a map to map from the
     * column number to a list of values to that column. Using a tree map, the key will automatically be sorted, hence we can take advantage
     * when we add each vertical column to final result.
     *
     * One thing we need to care about is that since we need to traverse from the top to the bottom, from left to the right, we need do
     * breadth-first traversal.
     *
     * Time: O(n^2) in the worst case, we need to insert a new column in the tree map, insertion will take O(n) time. On average, it will
     *       just be O(nlogn)
     * Space: O(n) in the worst case. On average it will be O(logn) for the queue and the tree map
     */
    private class myNode {
        TreeNode node;
        int column;

        myNode(TreeNode root, int col) {
            this.node = root;
            this.column = col;
        }
    }


    public List<List<Integer>> verticalOrderTreeMap(TreeNode root) {
        if(root == null) return new ArrayList<>();
        //need a map to record all the values for that column
        Map<Integer, List<Integer>> map = new TreeMap<>();
        //since we will do BFS, a queue is required
        Queue<myNode> queue = new LinkedList<>();
        //add root node with column number 0 in the queue
        queue.add(new myNode(root, 0));

        //BFS
        while(!queue.isEmpty()) {
            myNode curr = queue.poll();
            int col = curr.column;
            //if no key-value pair for current column level, add a new list
            map.putIfAbsent(col, new ArrayList<>());
            //otherwise, add value to the corresponding column
            map.get(col).add(curr.node.val);

            //search the left subtree and decrement the column level
            if(curr.node.left != null) {
                queue.add(new myNode(curr.node.left, col - 1));
            }
            //search the right subtree and increment the column level
            if(curr.node.right != null) {
                queue.add(new myNode(curr.node.right, col + 1));
            }
        }
        //since it is a tree map, the key-value pairs are sorted, we can directly add all the values in the final list
        return new ArrayList<>(map.values());
    }


    @Test
    public void verticalOrderTreeMapTest() {
        /**
         * Example 1:
         * Input: [3,9,20,null,null,15,7]
         *
         *    3
         *   /\
         *  /  \
         *  9  20
         *     /\
         *    /  \
         *   15   7
         *
         * Output:
         *
         * [
         *   [9],
         *   [3,15],
         *   [20],
         *   [7]
         * ]
         */
        TreeNode tree1 = new TreeNode(3);
        tree1.left = new TreeNode(9);
        tree1.right = new TreeNode(20);
        tree1.right.left = new TreeNode(15);
        tree1.right.right = new TreeNode(7);
        List<List<Integer>> expected1 = new ArrayList<>();
        expected1.add(Arrays.asList(9));
        expected1.add(Arrays.asList(3, 15));
        expected1.add(Arrays.asList(20));
        expected1.add(Arrays.asList(7));
        List<List<Integer>> actual1 = verticalOrderTreeMap(tree1);
        assertEquals(expected1.size(), actual1.size());
        for(int i = 0; i < expected1.size(); i++) {
            assertEquals(expected1.get(i).size(), actual1.get(i).size());
            for(int j = 0; j < expected1.get(i).size(); j++) {
                assertEquals(expected1.get(i).get(j), actual1.get(i).get(j));
            }
        }
        /**
         * Example 2:
         * Input: [3,9,8,4,0,1,7]
         *
         *      3
         *     /\
         *    /  \
         *    9   8
         *   /\  /\
         *  /  \/  \
         *  4  01   7
         *
         * Output:
         *
         * [
         *   [4],
         *   [9],
         *   [3,0,1],
         *   [8],
         *   [7]
         * ]
         */
        TreeNode tree2 = new TreeNode(3);
        tree2.left = new TreeNode(9);
        tree2.right = new TreeNode(8);
        tree2.left.left = new TreeNode(4);
        tree2.left.right = new TreeNode(0);
        tree2.right.left = new TreeNode(1);
        tree2.right.right = new TreeNode(7);
        List<List<Integer>> expected2 = new ArrayList<>();
        expected2.add(Arrays.asList(4));
        expected2.add(Arrays.asList(9));
        expected2.add(Arrays.asList(3, 0, 1));
        expected2.add(Arrays.asList(8));
        expected2.add(Arrays.asList(7));
        List<List<Integer>> actual2 = verticalOrderTreeMap(tree2);
        assertEquals(expected2.size(), actual2.size());
        for(int i = 0; i < expected2.size(); i++) {
            assertEquals(expected2.get(i).size(), actual2.get(i).size());
            for(int j = 0; j < expected2.get(i).size(); j++) {
                assertEquals(expected2.get(i).get(j), actual2.get(i).get(j));
            }
        }
        /**
         * Example 3:
         * Input: [3,9,8,4,0,1,7,null,null,null,2,5] (0's right child is 2 and 1's left child is 5)
         *
         *      3
         *     /\
         *    /  \
         *    9   8
         *   /\  /\
         *  /  \/  \
         *  4  01   7
         *     /\
         *    /  \
         *    5   2
         *
         * Output:
         *
         * [
         *   [4],
         *   [9,5],
         *   [3,0,1],
         *   [8,2],
         *   [7]
         * ]
         */
        TreeNode tree3 = new TreeNode(3);
        tree3.left = new TreeNode(9);
        tree3.right = new TreeNode(8);
        tree3.left.left = new TreeNode(4);
        tree3.left.right = new TreeNode(0);
        tree3.right.left = new TreeNode(1);
        tree3.right.right = new TreeNode(7);
        tree3.left.right.right = new TreeNode(2);
        tree3.right.left.left = new TreeNode(5);
        List<List<Integer>> expected3 = new ArrayList<>();
        expected3.add(Arrays.asList(4));
        expected3.add(Arrays.asList(9, 5));
        expected3.add(Arrays.asList(3, 0, 1));
        expected3.add(Arrays.asList(8, 2));
        expected3.add(Arrays.asList(7));
        List<List<Integer>> actual3 = verticalOrderTreeMap(tree3);
        assertEquals(expected3.size(), actual3.size());
        for(int i = 0; i < expected3.size(); i++) {
            assertEquals(expected3.get(i).size(), actual3.get(i).size());
            for(int j = 0; j < expected3.get(i).size(); j++) {
                assertEquals(expected3.get(i).get(j), actual3.get(i).get(j));
            }
        }
    }

    /**
     * Approach 2: Hash Map
     *
     * The basic idea is exactly the same. The only difference is the key-value pairs are not sorted. Hence we need to record the minimum
     * and maximum values in the map and iterate between min and max to add values to the list.
     *
     * Time: O(n) we need to traverse all the nodes in the hash map, insertion in hash map cost O(1) runtime
     * Space: O(n), the map and the queue will cost O(n) in the worst case, on average, it will be O(logn)
     *
     */
    public List<List<Integer>> verticalOrderHashMap(TreeNode root) {
        if(root == null) return new ArrayList<>();
        Map<Integer, List<Integer>> map = new HashMap<>();
        Queue<myNode> queue = new LinkedList<>();
        queue.add(new myNode(root, 0));
        int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;

        while(!queue.isEmpty()) {
            myNode curr = queue.poll();
            int col = curr.column;
            min = Math.min(min, col);
            max = Math.max(max, col);

            map.putIfAbsent(col, new ArrayList<>());
            map.get(col).add(curr.node.val);

            if(curr.node.left != null) {
                queue.add(new myNode(curr.node.left, col - 1));
            }
            if(curr.node.right != null) {
                queue.add(new myNode(curr.node.right, col + 1));
            }
        }
        List<List<Integer>> res = new ArrayList<>();
        for(int i = min; i <= max; i++) {
            res.add(map.get(i));
        }
        return res;
    }


    @Test
    public void verticalOrderHashMapTest() {
        /**
         * Example 1:
         * Input: [3,9,20,null,null,15,7]
         *
         *    3
         *   /\
         *  /  \
         *  9  20
         *     /\
         *    /  \
         *   15   7
         *
         * Output:
         *
         * [
         *   [9],
         *   [3,15],
         *   [20],
         *   [7]
         * ]
         */
        TreeNode tree1 = new TreeNode(3);
        tree1.left = new TreeNode(9);
        tree1.right = new TreeNode(20);
        tree1.right.left = new TreeNode(15);
        tree1.right.right = new TreeNode(7);
        List<List<Integer>> expected1 = new ArrayList<>();
        expected1.add(Arrays.asList(9));
        expected1.add(Arrays.asList(3, 15));
        expected1.add(Arrays.asList(20));
        expected1.add(Arrays.asList(7));
        List<List<Integer>> actual1 = verticalOrderHashMap(tree1);
        assertEquals(expected1.size(), actual1.size());
        for(int i = 0; i < expected1.size(); i++) {
            assertEquals(expected1.get(i).size(), actual1.get(i).size());
            for(int j = 0; j < expected1.get(i).size(); j++) {
                assertEquals(expected1.get(i).get(j), actual1.get(i).get(j));
            }
        }
        /**
         * Example 2:
         * Input: [3,9,8,4,0,1,7]
         *
         *      3
         *     /\
         *    /  \
         *    9   8
         *   /\  /\
         *  /  \/  \
         *  4  01   7
         *
         * Output:
         *
         * [
         *   [4],
         *   [9],
         *   [3,0,1],
         *   [8],
         *   [7]
         * ]
         */
        TreeNode tree2 = new TreeNode(3);
        tree2.left = new TreeNode(9);
        tree2.right = new TreeNode(8);
        tree2.left.left = new TreeNode(4);
        tree2.left.right = new TreeNode(0);
        tree2.right.left = new TreeNode(1);
        tree2.right.right = new TreeNode(7);
        List<List<Integer>> expected2 = new ArrayList<>();
        expected2.add(Arrays.asList(4));
        expected2.add(Arrays.asList(9));
        expected2.add(Arrays.asList(3, 0, 1));
        expected2.add(Arrays.asList(8));
        expected2.add(Arrays.asList(7));
        List<List<Integer>> actual2 = verticalOrderHashMap(tree2);
        assertEquals(expected2.size(), actual2.size());
        for(int i = 0; i < expected2.size(); i++) {
            assertEquals(expected2.get(i).size(), actual2.get(i).size());
            for(int j = 0; j < expected2.get(i).size(); j++) {
                assertEquals(expected2.get(i).get(j), actual2.get(i).get(j));
            }
        }
        /**
         * Example 3:
         * Input: [3,9,8,4,0,1,7,null,null,null,2,5] (0's right child is 2 and 1's left child is 5)
         *
         *      3
         *     /\
         *    /  \
         *    9   8
         *   /\  /\
         *  /  \/  \
         *  4  01   7
         *     /\
         *    /  \
         *    5   2
         *
         * Output:
         *
         * [
         *   [4],
         *   [9,5],
         *   [3,0,1],
         *   [8,2],
         *   [7]
         * ]
         */
        TreeNode tree3 = new TreeNode(3);
        tree3.left = new TreeNode(9);
        tree3.right = new TreeNode(8);
        tree3.left.left = new TreeNode(4);
        tree3.left.right = new TreeNode(0);
        tree3.right.left = new TreeNode(1);
        tree3.right.right = new TreeNode(7);
        tree3.left.right.right = new TreeNode(2);
        tree3.right.left.left = new TreeNode(5);
        List<List<Integer>> expected3 = new ArrayList<>();
        expected3.add(Arrays.asList(4));
        expected3.add(Arrays.asList(9, 5));
        expected3.add(Arrays.asList(3, 0, 1));
        expected3.add(Arrays.asList(8, 2));
        expected3.add(Arrays.asList(7));
        List<List<Integer>> actual3 = verticalOrderHashMap(tree3);
        assertEquals(expected3.size(), actual3.size());
        for(int i = 0; i < expected3.size(); i++) {
            assertEquals(expected3.get(i).size(), actual3.get(i).size());
            for(int j = 0; j < expected3.get(i).size(); j++) {
                assertEquals(expected3.get(i).get(j), actual3.get(i).get(j));
            }
        }
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
