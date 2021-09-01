import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;

import static org.junit.Assert.assertEquals;

public class LongestIncreasingPathInMatrix {

    /**
     * Given an m x n integers matrix, return the length of the longest increasing path in matrix.
     * <p>
     * From each cell, you can either move in four directions: left, right, up, or down. You may not move diagonally or move
     * outside the boundary (i.e., wrap-around is not allowed).
     * <p>
     * Constraints:
     * <p>
     * m == matrix.length
     * n == matrix[i].length
     * 1 <= m, n <= 200
     * 0 <= matrix[i][j] <= 2^31 - 1
     * <p>
     * Approach 1: DFS + memorization
     * The naive approach would be for each cell (i, j), we implement a DFS to find the longest path, each DFS may take
     * O(mn) time since we should visit the entire grid in the worst case. This gives O((mn)^2) overall runtime. Note that
     * there might will be lots of duplicate visits if we restart DFS at every cell. Essentially, if we reach a visited cell,
     * then we don't need to keep searching, just return the longest path it can achieve. By doing so, we only need to calculate
     * the maximum for each cell once. The subsequent visit will only take O(1) time to get the result.
     * <p>
     * Time: O(mn) each cell will be visited once for calculation
     * Space: O(mn) need a 2D array for memorization
     */
    private int[][] memo;

    public int longestIncreasingPathDFS(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        memo = new int[m][n];
        int res = 0;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // compute the longest path at each cell via dfs
                // since the cell value is guaranteed to be >= 0
                // we use -1 as the prev value such that dfs can proceed
                res = Math.max(res, dfs(matrix, i, j, -1));
            }
        }
        return res;
    }

    private int dfs(int[][] matrix, int i, int j, int prev) {
        // base case - if we jump outside the grid or hitting a non-increasing cell, stop
        if (i < 0 || j < 0 || i >= matrix.length || j >= matrix[0].length || matrix[i][j] <= prev) {
            return 0;
        }
        // or if we revisit a cell - return the memorized value
        if (memo[i][j] != 0) return memo[i][j];
        // otherwise, it's a brand-new cell, calculate the longest path via dfs
        // need to search 4 directions and find the maximum
        int curr = matrix[i][j];
        // need to increment by 1 since the current value will always be part of that path
        memo[i][j] = Math.max(dfs(matrix, i + 1, j, curr), Math.max(
                Math.max(dfs(matrix, i - 1, j, curr), dfs(matrix, i, j + 1, curr)),
                dfs(matrix, i, j - 1, curr))) + 1;
        return memo[i][j];
    }

    /**
     * Approach 2: Topological Sorting
     * We can treat the entire matrix as a graph. Essentially, each cell is a graph node, and there is a directed edge
     * from a smaller value to a larger value. In order to find the longest path, we can use topological sort to
     * pre-process the graph. Then we start with nodes which have 0 out degrees and traverse the entire graph layer by layer,
     * the longest path depends upon how many layers we have in the graph.
     * <p>
     * Time: O(mn) we traverse the entire matrix 3 times for sorting, adding 0 out degree nodes, and final BFS
     * Space: O(mn)
     */
    public int longestIncreasingPathTopologicalSorting(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        // treat the entire matrix as a graph, then each cell will be a node in the graph
        // consider there is a directed edge from a smaller value to a larger value
        // if a node has out degree 0, which means it must be larger than or equal to all of its neighbors
        int[][] outDegrees = new int[m][n];
        int[][] step = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        // compute out degrees for all nodes
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < 4; k++) {
                    int nextRow = i + step[k][0];
                    int nextCol = j + step[k][1];
                    // make sure the search is still within the grid and compute out degrees for current node
                    if (nextRow >= 0 && nextCol >= 0 && nextRow < m && nextCol < n &&
                            matrix[nextRow][nextCol] > matrix[i][j]) {
                        outDegrees[i][j]++;
                    }
                }
            }
        }

        // since topological sorting is used - start with nodes which have 0 out degree
        Queue<int[]> queue = new LinkedList<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (outDegrees[i][j] == 0) queue.add(new int[]{i, j});
            }
        }

        // use BFS to do the topological sorting
        // the longest path will be the number of levels the graph has
        int longest = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int[] curr = queue.poll();
                int row = curr[0], col = curr[1];
                // the node in queue is guaranteed to have 0 out degree
                // now, check out its 4 neighbors and pretend to remove the current node from the graph
                // since the edge is from smaller to larger, then if its neighbor is smaller than current node
                // removing current node will decrement its neighbor's out degree by 1
                // if its neighbor's out degree becomes 0, then add it to the queue
                for (int k = 0; k < 4; k++) {
                    int nextRow = row + step[k][0];
                    int nextCol = col + step[k][1];
                    // check whether the search is still inside the grid and
                    // whether we can decrement out degrees of its neighbors
                    if (nextRow >= 0 && nextCol >= 0 && nextRow < m && nextCol < n &&
                            matrix[row][col] > matrix[nextRow][nextCol]) {
                        outDegrees[nextRow][nextCol]--;
                        if (outDegrees[nextRow][nextCol] == 0) queue.add(new int[]{nextRow, nextCol});
                    }
                }
            }
            // the traversal of current level is done, increment the path and move to next level
            longest++;
        }
        return longest;
    }

    @Test
    public void longestIncreasingPathTest() {
        /**
         * Example 1:
         * Input: matrix = [
         * [9,9,4],
         * [6,6,8],
         * [2,1,1]]
         * Output: 4
         * Explanation: The longest increasing path is [1, 2, 6, 9].
         */
        assertEquals(4, longestIncreasingPathDFS(new int[][]{{9, 9, 4}, {6, 6, 8}, {2, 1, 1}}));
        assertEquals(4, longestIncreasingPathTopologicalSorting(new int[][]{{9, 9, 4}, {6, 6, 8}, {2, 1, 1}}));
        /**
         * Example 2:
         * Input: matrix = [
         * [3,4,5]
         * [3,2,6]
         * [2,2,1]]
         * Output: 4
         * Explanation: The longest increasing path is [3, 4, 5, 6]. Moving diagonally is not allowed.
         */
        assertEquals(4, longestIncreasingPathDFS(new int[][]{{3, 4, 5}, {3, 2, 6}, {2, 2, 1}}));
        assertEquals(4, longestIncreasingPathTopologicalSorting(new int[][]{{3, 4, 5}, {3, 2, 6}, {2, 2, 1}}));
        /**
         * Example 3:
         * Input: matrix = [[1]]
         * Output: 1
         */
        assertEquals(1, longestIncreasingPathDFS(new int[][]{{1}}));
        assertEquals(1, longestIncreasingPathTopologicalSorting(new int[][]{{1}}));
    }
}
