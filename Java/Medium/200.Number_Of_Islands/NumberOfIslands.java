import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;

import static org.junit.Assert.assertEquals;

public class NumberOfIslands {

    /**
     * Given an m x n 2D binary grid grid which represents a map of '1's (land) and '0's (water), return
     * the number of islands.
     * <p>
     * An island is surrounded by water and is formed by connecting adjacent lands horizontally or vertically.
     * You may assume all four edges of the grid are all surrounded by water.
     * <p>
     * Constraints:
     * <p>
     * m == grid.length
     * n == grid[i].length
     * 1 <= m, n <= 300
     * grid[i][j] is '0' or '1'.
     * <p>
     * Approach 1: Depth-First Search (DFS)
     * <p>
     * Time: O(M * N) it's required to traverse the entire map to find the count
     * Space: O(M * N) the call stack will be of size M * N in the worst case
     */
    public int numIslandsDFS(char[][] grid) {
        int count = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == '1') {
                    count += 1;
                    dfs(grid, i, j);
                }
            }
        }
        return count;
    }

    private void dfs(char[][] grid, int i, int j) {
        if (i < 0 || j < 0 || i >= grid.length || j >= grid[0].length || grid[i][j] == '0') {
            return;
        }
        if (grid[i][j] == '1') grid[i][j] = '0';
        dfs(grid, i - 1, j);
        dfs(grid, i + 1, j);
        dfs(grid, i, j - 1);
        dfs(grid, i, j + 1);
    }

    /**
     * Approach 2: Breadth-First Search (BFS)
     * <p>
     * Time: O(M * N)
     * Space: O(sqrt(M * N)) -> O(min(M, N)) when M and N are positive integers
     */
    public int numIslandsBFS(char[][] grid) {
        int nr = grid.length, nc = grid[0].length;
        int count = 0;
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                if (grid[i][j] == '1') {
                    count++;
                    grid[i][j] = '0';
                    Queue<Integer> queue = new LinkedList<>();
                    // convert 2-D loc into 1-D id
                    queue.add(i * nc + j);
                    while (!queue.isEmpty()) {
                        int id = queue.poll();
                        // convert 1-D id back to 2-D pos index
                        int row = id / nc;
                        int col = id % nc;
                        // check 4 directions
                        if (row - 1 >= 0 && grid[row - 1][col] == '1') {
                            grid[row - 1][col] = '0';
                            queue.add((row - 1) * nc + col);
                        }
                        if (row + 1 < nr && grid[row + 1][col] == '1') {
                            grid[row + 1][col] = '0';
                            queue.add((row + 1) * nc + col);
                        }
                        if (col - 1 >= 0 && grid[row][col - 1] == '1') {
                            grid[row][col - 1] = '0';
                            queue.add(row * nc + col - 1);
                        }
                        if (col + 1 < nc && grid[row][col + 1] == '1') {
                            grid[row][col + 1] = '0';
                            queue.add(row * nc + col + 1);
                        }
                    }
                }
            }
        }
        return count;
    }

    @Test
    public void numberOfIslandsDfsTest() {
        /**
         * Example 1:
         * Input: grid = [
         *   ["1","1","1","1","0"],
         *   ["1","1","0","1","0"],
         *   ["1","1","0","0","0"],
         *   ["0","0","0","0","0"]
         * ]
         * Output: 1
         */
        char[][] test1 = {{'1', '1', '1', '1', '0'},
                {'1', '1', '0', '1', '0'},
                {'1', '1', '0', '0', '0'},
                {'0', '0', '0', '0', '0'}};
        assertEquals(1, numIslandsDFS(test1));
        /**
         * Example 2:
         * Input: grid = [
         *   ["1","1","0","0","0"],
         *   ["1","1","0","0","0"],
         *   ["0","0","1","0","0"],
         *   ["0","0","0","1","1"]
         * ]
         * Output: 3
         */
        char[][] test2 = {{'1', '1', '0', '0', '0'},
                {'1', '1', '0', '0', '0'},
                {'0', '0', '1', '0', '0'},
                {'0', '0', '0', '1', '1'}};
        assertEquals(3, numIslandsDFS(test2));
    }

    @Test
    public void numberOfIslandsBfsTest() {
        /**
         * Example 1:
         * Input: grid = [
         *   ["1","1","1","1","0"],
         *   ["1","1","0","1","0"],
         *   ["1","1","0","0","0"],
         *   ["0","0","0","0","0"]
         * ]
         * Output: 1
         */
        char[][] test1 = {{'1', '1', '1', '1', '0'},
                {'1', '1', '0', '1', '0'},
                {'1', '1', '0', '0', '0'},
                {'0', '0', '0', '0', '0'}};
        assertEquals(1, numIslandsBFS(test1));
        /**
         * Example 2:
         * Input: grid = [
         *   ["1","1","0","0","0"],
         *   ["1","1","0","0","0"],
         *   ["0","0","1","0","0"],
         *   ["0","0","0","1","1"]
         * ]
         * Output: 3
         */
        char[][] test2 = {{'1', '1', '0', '0', '0'},
                {'1', '1', '0', '0', '0'},
                {'0', '0', '1', '0', '0'},
                {'0', '0', '0', '1', '1'}};
        assertEquals(3, numIslandsBFS(test2));
    }
}
