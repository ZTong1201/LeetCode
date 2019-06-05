import org.junit.Test;
import static org.junit.Assert.*;

public class minimumPathSum {


    /**
     * Given a m x n grid filled with non-negative numbers, find a path from top left to bottom right which
     * minimizes the sum of all numbers along its path.
     *
     * Note: You can only move either down or right at any point in time.
     *
     * 2-D dynamic programming
     * Time: O(m*n);
     * Space: O(1) since we change the grid value in-place, no need for extra space
     */
    public int minPathSum(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        for(int i = 0; i < m; i++) {
            for(int j = 0; j < n; j++) {
                if(i == 0 && j == 0) continue;
                else if (i < 1) grid[i][j] += grid[i][j - 1];
                else if (j < 1) grid[i][j] += grid[i - 1][j];
                else grid[i][j] += Math.min(grid[i - 1][j], grid[i][j - 1]);
            }
        }
        return grid[m - 1][n - 1];
    }

    @Test
    public void minPathSumTest() {
        int[][] grid = new int[][]{{1, 3, 1}, {1, 5, 1}, {4, 2, 1}};
        assertEquals(7, minPathSum(grid));
    }
}
