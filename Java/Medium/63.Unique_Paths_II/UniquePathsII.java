import org.junit.Test;
import static org.junit.Assert.*;

public class UniquePathsII {

    /**
     * A robot is located at the top-left corner of a m x n grid.
     *
     * The robot can only move either down or right at any point in time.
     * The robot is trying to reach the bottom-right corner of the grid.
     *
     * Now consider if some obstacles are added to the grids. How many unique paths would there be?
     *
     * An obstacle and empty space is marked as 1 and 0 respectively in the grid.
     *
     * Note: m and n will be at most 100.
     *
     * 2-D dynamic programming
     *
     * Time: O(m*n)
     * Space: O(1) since we change the grid in-place, no need for extra space
     */
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        int m = obstacleGrid.length;
        int n = obstacleGrid[0].length;
        for(int i = 0; i < m; i++) {
            for(int j = 0; j < n; j++) {
                if(i == 0 && j == 0) obstacleGrid[i][j] = obstacleGrid[i][j] == 1 ? 0 : 1;
                else if(i < 1) obstacleGrid[i][j] = obstacleGrid[i][j] == 1 ? 0 : obstacleGrid[i][j - 1];
                else if(j < 1) obstacleGrid[i][j] = obstacleGrid[i][j] == 1 ? 0 : obstacleGrid[i - 1][j];
                else obstacleGrid[i][j] = obstacleGrid[i][j] == 1 ? 0 : obstacleGrid[i - 1][j] + obstacleGrid[i][j - 1];
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
