import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class VerticalOrderTraversal {

    /**
     * Given the root of a binary tree, calculate the vertical order traversal of the binary tree.
     * <p>
     * For each node at position (row, col), its left and right children will be at positions (row + 1, col - 1) and
     * (row + 1, col + 1) respectively. The root of the tree is at (0, 0).
     * <p>
     * The vertical order traversal of a binary tree is a list of top-to-bottom orderings for each column index starting
     * from the leftmost column and ending on the rightmost column. There may be multiple nodes in the same row and same
     * column. In such a case, sort these nodes by their values.
     * <p>
     * Return the vertical order traversal of the binary tree.
     * <p>
     * Constraints:
     * <p>
     * The number of nodes in the tree is in the range [1, 1000].
     * 0 <= Node.val <= 1000
     * <p>
     * Approach 1: DFS + global sorting
     * Basically, the idea behind the scene is to give each node a triplet of values (column, row, val), after traversing
     * the entire tree and get list of nodes, we would like sort them by the following order:
     * 1. smallest column comes first
     * 2. if there is a tie on the column, then smallest row comes first
     * 3. the final tiebreaker will be the value itself
     * <p>
     * Therefore, the algorithm looks like this:
     * 1. Traverse the entire tree (either BFS or DFS) and get a list of triplets
     * 2. sort the triplet based on the ordering rules above
     * 3. traverse the triplet list again and group nodes with the same column number together and add to the list
     * <p>
     * Time: O(nlogn)
     * 1. Traversal the entire tree requires O(n)
     * 2. sort all nodes - O(nlogn)
     * 3. traverse the node list again and add them into result list - O(n)
     * hence the sorting algorithm will dominate the time complexity
     * Space: O(n)
     */
    private List<Triplet> triplets;

    public List<List<Integer>> verticalTraversalGlobalSorting(TreeNode root) {
        triplets = new ArrayList<>();
        // use DFS to traverse the tree starting from (0, 0)
        preorder(root, 0, 0);
        // sort the triplet list
        Comparator<Triplet> comparator = (Triplet p, Triplet q) -> {
            if (p.column == q.column) {
                if (p.row == q.row) return p.val - q.val;
                else return p.row - q.row;
            } else return p.column - q.column;
        };
        Collections.sort(triplets, comparator);
        // group the same column together and add them to the result list
        List<List<Integer>> res = new ArrayList<>();
        List<Integer> currColumn = new ArrayList<>();
        // since the list has been sorted, starting from the smallest column index
        int columnIndex = triplets.get(0).column;

        for (Triplet triplet : triplets) {
            // add values to the same column list
            if (triplet.column == columnIndex) {
                currColumn.add(triplet.val);
            } else {
                // otherwise, the current column is complete
                // append the column list to the final result and reinitialize everything
                res.add(currColumn);
                currColumn = new ArrayList<>();
                // remember to add current value into the newest list
                currColumn.add(triplet.val);
                columnIndex = triplet.column;
            }
        }
        // the last column hasn't been appended to the result list yet
        res.add(currColumn);
        return res;
    }

    private void preorder(TreeNode root, int row, int column) {
        if (root == null) return;
        triplets.add(new Triplet(column, row, root.val));
        preorder(root.left, row + 1, column - 1);
        preorder(root.right, row + 1, column + 1);
    }

    private static class Triplet {
        int column;
        int row;
        int val;

        Triplet(int column, int row, int val) {
            this.column = column;
            this.row = row;
            this.val = val;
        }
    }

    /**
     * Approach 2: Partition Sorting
     * Based on the problem definition, the sorting is somewhat inevitable. Hence, we could only optimize the sorting part
     * to get performance boost. Instead of sorting the entire triplets, we can actually only sort by row number and value
     * under the same column group. Besides, this is exactly what is required by the output. We can take advantage of the
     * map structure to easily get all nodes with the same column index. The updated algorithm:
     * 1. Traversal the tree as usual and keep a map to record column index -> a list of nodes (with row index & value),
     * also update the minimum and maximum column indexes for step 3
     * 2. enumerate all the key-value pairs in the map and sort each sub-list
     * 3. loop from minimum to the maximum column index and add the sorted sub-list to the result list.
     * <p>
     * Time: O(nlog(n/k)) assume we have k columns, it takes O((n/k)*log(n/k)) to sort individual list. The overall time
     * complexity will be O(k * (n/k)log(n/k)) = O(nlog(n/k))
     * Space: O(n)
     */
    private Map<Integer, List<Pair>> map;
    private int minColumn, maxColumn;

    public List<List<Integer>> verticalTraversalPartitionSorting(TreeNode root) {
        map = new HashMap<>();
        minColumn = 0;
        maxColumn = 0;
        // traverse the entire tree as usual
        preorderMap(root, 0, 0);
        // define a comparator to sort based on row index and value
        Comparator<Pair> comparator = (Pair a, Pair b) -> {
            if (a.row == b.row) return a.val - b.val;
            else return a.row - b.row;
        };
        // traverse the map from minimum to maximum column index
        List<List<Integer>> res = new ArrayList<>();
        for (int i = minColumn; i <= maxColumn; i++) {
            // sort the sub-list first
            map.get(i).sort(comparator);
            // extract values from the pair object
            List<Integer> sortedColumn = new ArrayList<>();
            for (Pair pair : map.get(i)) {
                sortedColumn.add(pair.val);
            }
            // append the column list to final list
            res.add(sortedColumn);
        }
        return res;
    }

    private void preorderMap(TreeNode root, int row, int column) {
        if (root == null) return;
        // add column index and list of pairs key-value pair into the map
        map.putIfAbsent(column, new ArrayList<>());
        map.get(column).add(new Pair(row, root.val));
        // also update minimum and maximum row indexes
        minColumn = Math.min(minColumn, column);
        maxColumn = Math.max(maxColumn, column);
        preorderMap(root.left, row + 1, column - 1);
        preorderMap(root.right, row + 1, column + 1);
    }

    private static class Pair {
        int row;
        int val;

        Pair(int row, int val) {
            this.row = row;
            this.val = val;
        }
    }

    @Test
    public void verticalTraversalGlobalSortingTest() {
        /**
         * Example 1:
         * Input: root = [3,9,20,null,null,15,7]
         * Output: [[9],[3,15],[20],[7]]
         * Explanation:
         * Column -1: Only node 9 is in this column.
         * Column 0: Nodes 3 and 15 are in this column in that order from top to bottom.
         * Column 1: Only node 20 is in this column.
         * Column 2: Only node 7 is in this column.
         *                          3
         *                         / \
         *                        9  20
         *                           / \
         *                          15  7
         */
        TreeNode root1 = new TreeNode(3);
        root1.left = new TreeNode(9);
        root1.right = new TreeNode(20);
        root1.right.left = new TreeNode(15);
        root1.right.right = new TreeNode(7);
        List<List<Integer>> actualGlobalSorting1 = verticalTraversalGlobalSorting(root1);
        List<List<Integer>> expected1 = List.of(List.of(9), List.of(3, 15), List.of(20), List.of(7));
        assertEquals(expected1.size(), actualGlobalSorting1.size());
        for (int i = 0; i < expected1.size(); i++) {
            assertEquals(expected1.get(i).size(), actualGlobalSorting1.get(i).size());
            for (int j = 0; j < expected1.get(i).size(); j++) {
                assertEquals(expected1.get(i).get(j), actualGlobalSorting1.get(i).get(j));
            }
        }
        /**
         * Example 2:
         * Input: root = [1,2,3,4,5,6,7]
         * Output: [[4],[2],[1,5,6],[3],[7]]
         * Explanation:
         * Column -2: Only node 4 is in this column.
         * Column -1: Only node 2 is in this column.
         * Column 0: Nodes 1, 5, and 6 are in this column.
         *           1 is at the top, so it comes first.
         *           5 and 6 are at the same position (2, 0), so we order them by their value, 5 before 6.
         * Column 1: Only node 3 is in this column.
         * Column 2: Only node 7 is in this column.
         *                                     1
         *                                   /  \
         *                                  2    3
         *                                 / \  / \
         *                                4  5 6   7
         */
        TreeNode root2 = new TreeNode(1);
        root2.left = new TreeNode(2);
        root2.right = new TreeNode(3);
        root2.left.left = new TreeNode(4);
        root2.left.right = new TreeNode(5);
        root2.right.left = new TreeNode(6);
        root2.right.right = new TreeNode(7);
        List<List<Integer>> actualGlobalSorting2 = verticalTraversalGlobalSorting(root2);
        List<List<Integer>> expected2 = List.of(List.of(4), List.of(2), List.of(1, 5, 6), List.of(3), List.of(7));
        assertEquals(expected2.size(), actualGlobalSorting2.size());
        for (int i = 0; i < expected2.size(); i++) {
            assertEquals(expected2.get(i).size(), actualGlobalSorting2.get(i).size());
            for (int j = 0; j < expected2.get(i).size(); j++) {
                assertEquals(expected2.get(i).get(j), actualGlobalSorting2.get(i).get(j));
            }
        }
        /**
         * Example 3:
         * Input: root = [1,2,3,4,6,5,7]
         * Output: [[4],[2],[1,5,6],[3],[7]]
         * Explanation:
         * This case is the exact same as example 2, but with nodes 5 and 6 swapped.
         * Note that the solution remains the same since 5 and 6 are in the same location and should be ordered by
         * their values.
         *                                    1
         *                                  /  \
         *                                 2    3
         *                                / \  / \
         *                               4  6 5   7
         */
        TreeNode root3 = new TreeNode(1);
        root3.left = new TreeNode(2);
        root3.right = new TreeNode(3);
        root3.left.left = new TreeNode(4);
        root3.left.right = new TreeNode(6);
        root3.right.left = new TreeNode(5);
        root3.right.right = new TreeNode(7);
        List<List<Integer>> actualGlobalSorting3 = verticalTraversalGlobalSorting(root3);
        List<List<Integer>> expected3 = List.of(List.of(4), List.of(2), List.of(1, 5, 6), List.of(3), List.of(7));
        assertEquals(expected3.size(), actualGlobalSorting3.size());
        for (int i = 0; i < expected3.size(); i++) {
            assertEquals(expected3.get(i).size(), actualGlobalSorting3.get(i).size());
            for (int j = 0; j < expected3.get(i).size(); j++) {
                assertEquals(expected3.get(i).get(j), actualGlobalSorting3.get(i).get(j));
            }
        }
    }

    @Test
    public void verticalTraversalPartitionSortingTest() {
        /**
         * Example 1
         */
        TreeNode root1 = new TreeNode(3);
        root1.left = new TreeNode(9);
        root1.right = new TreeNode(20);
        root1.right.left = new TreeNode(15);
        root1.right.right = new TreeNode(7);
        List<List<Integer>> actualPartitionSorting1 = verticalTraversalPartitionSorting(root1);
        List<List<Integer>> expected1 = List.of(List.of(9), List.of(3, 15), List.of(20), List.of(7));
        assertEquals(expected1.size(), actualPartitionSorting1.size());
        for (int i = 0; i < expected1.size(); i++) {
            assertEquals(expected1.get(i).size(), actualPartitionSorting1.get(i).size());
            for (int j = 0; j < expected1.get(i).size(); j++) {
                assertEquals(expected1.get(i).get(j), actualPartitionSorting1.get(i).get(j));
            }
        }
        /**
         * Example 2
         */
        TreeNode root2 = new TreeNode(1);
        root2.left = new TreeNode(2);
        root2.right = new TreeNode(3);
        root2.left.left = new TreeNode(4);
        root2.left.right = new TreeNode(5);
        root2.right.left = new TreeNode(6);
        root2.right.right = new TreeNode(7);
        List<List<Integer>> actualPartitionSorting2 = verticalTraversalPartitionSorting(root2);
        List<List<Integer>> expected2 = List.of(List.of(4), List.of(2), List.of(1, 5, 6), List.of(3), List.of(7));
        assertEquals(expected2.size(), actualPartitionSorting2.size());
        for (int i = 0; i < expected2.size(); i++) {
            assertEquals(expected2.get(i).size(), actualPartitionSorting2.get(i).size());
            for (int j = 0; j < expected2.get(i).size(); j++) {
                assertEquals(expected2.get(i).get(j), actualPartitionSorting2.get(i).get(j));
            }
        }
        /**
         * Example 3
         */
        TreeNode root3 = new TreeNode(1);
        root3.left = new TreeNode(2);
        root3.right = new TreeNode(3);
        root3.left.left = new TreeNode(4);
        root3.left.right = new TreeNode(6);
        root3.right.left = new TreeNode(5);
        root3.right.right = new TreeNode(7);
        List<List<Integer>> actualPartitionSorting3 = verticalTraversalPartitionSorting(root3);
        List<List<Integer>> expected3 = List.of(List.of(4), List.of(2), List.of(1, 5, 6), List.of(3), List.of(7));
        assertEquals(expected3.size(), actualPartitionSorting3.size());
        for (int i = 0; i < expected3.size(); i++) {
            assertEquals(expected3.get(i).size(), actualPartitionSorting3.get(i).size());
            for (int j = 0; j < expected3.get(i).size(); j++) {
                assertEquals(expected3.get(i).get(j), actualPartitionSorting3.get(i).get(j));
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
