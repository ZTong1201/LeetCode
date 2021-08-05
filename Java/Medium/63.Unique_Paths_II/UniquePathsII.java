import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UniquePathsII {

    /**
     * A robot is located at the top-left corner of a m x n grid.
     * <p>
     * The robot can only move either down or right at any point in time.
     * The robot is trying to reach the bottom-right corner of the grid.
     * <p>
     * Now consider if some obstacles are added to the grids. How many unique paths would there be?
     * <p>
     * An obstacle and empty space is marked as 1 and 0 respectively in the grid.
     * <p>
     * Note: m and n will be at most 100.
     * <p>
     * Constraints:
     * <p>
     * m == obstacleGrid.length
     * n == obstacleGrid[i].length
     * 1 <= m, n <= 100
     * obstacleGrid[i][j] is 0 or 1.
     * <p>
     * 2-D dynamic programming
     * <p>
     * Time: O(m*n)
     * Space: O(1) since we change the grid in-place, no need for extra space
     */
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        int m = obstacleGrid.length;
        int n = obstacleGrid[0].length;
        // check whether the starting point is accessible
        obstacleGrid[0][0] = obstacleGrid[0][0] == 1 ? 0 : 1;
        // fill in the first row, if the cell is an obstacle, fill with 0
        for (int i = 1; i < n; i++) {
            if (obstacleGrid[0][i] == 1) obstacleGrid[0][i] = 0;
            else obstacleGrid[0][i] = obstacleGrid[0][i - 1];
        }
        // fill the first column
        for (int i = 1; i < m; i++) {
            if (obstacleGrid[i][0] == 1) obstacleGrid[i][0] = 0;
            else obstacleGrid[i][0] = obstacleGrid[i - 1][0];
        }
        // fill other cells
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (obstacleGrid[i][j] == 1) obstacleGrid[i][j] = 0;
                else obstacleGrid[i][j] = obstacleGrid[i - 1][j] + obstacleGrid[i][j - 1];
            }
        }
        return obstacleGrid[m - 1][n - 1];
    }

    @Test
    public void uniquePathsWithObstaclesTest() {
        /**
         * [
         *   [0,0,0],
         *   [0,1,0],
         *   [0,0,0]
         * ]
         * paths = 2
         */
        int[][] grid1 = new int[3][3];
        grid1[1][1] = 1;
        assertEquals(2, uniquePathsWithObstacles(grid1));
        int[][] grid2 = new int[3][3];
        /**
         * [
         *   [0,1,0],
         *   [0,1,0],
         *   [0,0,0]
         * ]
         * paths = 1
         */
        grid2[0][1] = 1;
        grid2[1][1] = 1;
        assertEquals(1, uniquePathsWithObstacles(grid2));
        /**
         * [
         * [1]
         * ]
         * paths = 0
         */
        int[][] grid3 = new int[1][1];
        grid3[0][0] = 1;
        assertEquals(0, uniquePathsWithObstacles(grid3));
    }

}
