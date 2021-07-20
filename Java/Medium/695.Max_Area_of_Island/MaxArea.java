import org.junit.Test;

import java.util.Stack;

import static org.junit.Assert.assertEquals;

public class MaxArea {

    /**
     * You are given an m x n binary matrix grid. An island is a group of 1's (representing land) connected
     * 4-directionally (horizontal or vertical.) You may assume all four edges of the grid are surrounded by water.
     * <p>
     * The area of an island is the number of cells with a value 1 in the island.
     * <p>
     * Return the maximum area of an island in grid. If there is no island, return 0.
     * <p>
     * Constraints:
     * <p>
     * m == grid.length
     * n == grid[i].length
     * 1 <= m, n <= 50
     * grid[i][j] is either 0 or 1.
     * <p>
     * Approach 1: Depth-First Search (Iterative)
     * <p>
     * Time: O(m * n) we always need to visit all the squares to get the answer
     * Space: O(m * n) in the worst case, the stack will of size m * n
     */
    public int maxAreaOfIslandDfsIterative(int[][] grid) {
        int res = 0;
        int m = grid.length, n = grid[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    res = Math.max(res, dfsIterative(grid, i, j));
                }
            }
        }
        return res;
    }

    private int dfsIterative(int[][] grid, int i, int j) {
        Stack<Integer> stack = new Stack<>();
        int area = 0;
        int m = grid.length, n = grid[0].length;
        stack.push(i * n + j);
        while (!stack.isEmpty()) {
            int id = stack.pop();
            int row = id / n;
            int col = id % n;
            if (grid[row][col] == 1) {
                grid[row][col] = 0;
                area++;
                if (row - 1 >= 0 && grid[row - 1][col] == 1) {
                    stack.push((row - 1) * n + col);
                }
                if (row + 1 < m && grid[row + 1][col] == 1) {
                    stack.push((row + 1) * n + col);
                }
                if (col - 1 >= 0 && grid[row][col - 1] == 1) {
                    stack.push(row * n + col - 1);
                }
                if (col + 1 < n && grid[row][col + 1] == 1) {
                    stack.push(row * n + col + 1);
                }
            }
        }
        return area;
    }

    /**
     * Approach 2: Depth-First Search (Recursive)
     * <p>
     * Time: O(m * n) still need to visit all the squares
     * Space: O(m * n)
     */
    public int maxAreaOfIslandDfsRecursive(int[][] grid) {
        int res = 0;
        int m = grid.length, n = grid[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    res = Math.max(res, dfsRecursive(grid, i, j));
                }
            }
        }
        return res;
    }

    /**
     * The idea of recursive dfs is that - in order to know the size of connected component at (i, j), we need to first
     * know the size of each connected component from 4 adjacent directions, then the final result will be
     * size(up) + size(down) + size(left) + size(right) + 1
     * <p>
     * The key part is to define the base case, once it travels outside of the grid or the square is not 1 (not part of
     * current connected component), the area is 0.
     */
    public int dfsRecursive(int[][] grid, int i, int j) {
        // base case
        if (i < 0 || j < 0 || i >= grid.length || j >= grid[0].length || grid[i][j] == 0) return 0;
        // update the grid value to 0 - avoid revisiting
        grid[i][j] = 0;
        return 1 + dfsRecursive(grid, i + 1, j) + dfsRecursive(grid, i - 1, j) + dfsRecursive(grid, i, j + 1) +
                dfsRecursive(grid, i, j - 1);
    }

    @Test
    public void maxAreaOfIslandDfsIterativeTest() {
        /**
         * Example 1
         * Input: grid =
         * [[0,0,1,0,0,0,0,1,0,0,0,0,0],
         * [0,0,0,0,0,0,0,1,1,1,0,0,0],
         * [0,1,1,0,1,0,0,0,0,0,0,0,0],
         * [0,1,0,0,1,1,0,0,1,0,1,0,0],
         * [0,1,0,0,1,1,0,0,1,1,1,0,0],
         * [0,0,0,0,0,0,0,0,0,0,1,0,0],
         * [0,0,0,0,0,0,0,1,1,1,0,0,0],
         * [0,0,0,0,0,0,0,1,1,0,0,0,0]]
         * Output: 6
         * Explanation: The answer is not 11, because the island must be connected 4-directionally.
         */
        int[][] grid = new int[][]{
                {0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0},
                {0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 0, 1, 1, 0, 0, 1, 0, 1, 0, 0},
                {0, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0}};
        assertEquals(6, maxAreaOfIslandDfsIterative(grid));
    }

    @Test
    public void maxAreaOfIslandDfsRecursiveTest() {
        /**
         * Example 1
         * Input: grid =
         * [[0,0,1,0,0,0,0,1,0,0,0,0,0],
         * [0,0,0,0,0,0,0,1,1,1,0,0,0],
         * [0,1,1,0,1,0,0,0,0,0,0,0,0],
         * [0,1,0,0,1,1,0,0,1,0,1,0,0],
         * [0,1,0,0,1,1,0,0,1,1,1,0,0],
         * [0,0,0,0,0,0,0,0,0,0,1,0,0],
         * [0,0,0,0,0,0,0,1,1,1,0,0,0],
         * [0,0,0,0,0,0,0,1,1,0,0,0,0]]
         * Output: 6
         * Explanation: The answer is not 11, because the island must be connected 4-directionally.
         */
        int[][] grid = new int[][]{
                {0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0},
                {0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 0, 1, 1, 0, 0, 1, 0, 1, 0, 0},
                {0, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0}};
        assertEquals(6, maxAreaOfIslandDfsRecursive(grid));
    }
}
