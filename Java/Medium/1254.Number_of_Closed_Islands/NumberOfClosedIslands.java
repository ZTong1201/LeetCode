import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumberOfClosedIslands {

    /**
     * Given a 2D grid consists of 0s (land) and 1s (water).  An island is a maximal 4-directionally connected group of 0s
     * and a closed island is an island totally (all left, top, right, bottom) surrounded by 1s.
     * <p>
     * Return the number of closed islands.
     * Constraints:
     * <p>
     * 1 <= grid.length, grid[0].length <= 100
     * 0 <= grid[i][j] <=1
     * <p>
     * Approach: DFS
     * Similar idea as in LeetCode 200: https://leetcode.com/problems/number-of-islands/
     * We can first traverse all the cells around the corner and fill land with water (DFS) if there is a land cell at the
     * corner position. After the first run is done, we need to traverse the grid again and if there is any land cell,
     * increment the number of closed islands by 1 and fill that island with water again.
     * <p>
     * Time: O(mn) each cell will be visited and filled with water once
     * Space: O(mn) call stack require O(mn) space
     */
    public int closedIsland(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // first round traversal
                // fill island with water if there is a land cell at the corner
                if (i == 0 || j == 0 || i == m - 1 || j == n - 1 && grid[i][j] == 0) {
                    fillWithWater(grid, i, j);
                }
            }
        }

        int numOfClosedIslands = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // second round traversal
                // increment number of islands if there are any land cells left
                // and fill the entire island with water
                if (grid[i][j] == 0) {
                    numOfClosedIslands++;
                    fillWithWater(grid, i, j);
                }
            }
        }
        return numOfClosedIslands;
    }

    private void fillWithWater(int[][] grid, int i, int j) {
        if (i < 0 || j < 0 || i >= grid.length || j >= grid[0].length || grid[i][j] == 1) return;
        // fill land with water
        grid[i][j] = 1;
        // fill its four neighbors with water (if any)
        fillWithWater(grid, i + 1, j);
        fillWithWater(grid, i - 1, j);
        fillWithWater(grid, i, j + 1);
        fillWithWater(grid, i, j - 1);
    }

    @Test
    public void closedIslandTest() {
        /**
         * Example 1:
         * Input: grid = [[1,1,1,1,1,1,1,0],[1,0,0,0,0,1,1,0],[1,0,1,0,1,1,1,0],[1,0,0,0,0,1,0,1],[1,1,1,1,1,1,1,0]]
         * Output: 2
         */
        assertEquals(2, closedIsland(new int[][]{
                {1, 1, 1, 1, 1, 1, 1, 0},
                {1, 0, 0, 0, 0, 1, 1, 0},
                {1, 0, 1, 0, 1, 1, 1, 0},
                {1, 0, 0, 0, 0, 1, 0, 1},
                {1, 1, 1, 1, 1, 1, 1, 0}}));
        /**
         * Example 2:
         * Input: grid = [[0,0,1,0,0],[0,1,0,1,0],[0,1,1,1,0]]
         * Output: 1
         */
        assertEquals(1, closedIsland(new int[][]{
                {0, 0, 1, 0, 0},
                {0, 1, 0, 1, 0},
                {0, 1, 1, 1, 0}}));
        /**
         * Example 3:
         * Input: grid = [[1,1,1,1,1,1,1],
         *                [1,0,0,0,0,0,1],
         *                [1,0,1,1,1,0,1],
         *                [1,0,1,0,1,0,1],
         *                [1,0,1,1,1,0,1],
         *                [1,0,0,0,0,0,1],
         *                [1,1,1,1,1,1,1]]
         * Output: 2
         */
        assertEquals(2, closedIsland(new int[][]{
                {1, 1, 1, 1, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 1},
                {1, 0, 1, 1, 1, 0, 1},
                {1, 0, 1, 0, 1, 0, 1},
                {1, 0, 1, 1, 1, 0, 1},
                {1, 0, 0, 0, 0, 0, 1},
                {1, 1, 1, 1, 1, 1, 1}}));
    }
}
