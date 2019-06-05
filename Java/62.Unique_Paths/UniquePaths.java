import org.junit.Test;
import static org.junit.Assert.*;

public class UniquePaths {

    /**
     * A robot is located at the top-left corner of a m x n grid
     *
     * The robot can only move either down or right at any point in time.
     * The robot is trying to reach the bottom-right corner of the grid.
     *
     * How many possible unique paths are there?
     *
     * 2-D dynamic programming
     * Time: O(m*n)
     * Space: O(m*n)
     */
    public int uniquePaths(int m, int n) {
        int[][] grid = new int[m][n];
        for(int i = 0; i < m; i++) {
            for(int j = 0; j < n; j++) {
                if(i - 1 < 0 || j - 1 < 0) grid[i][j] = 1;
                else grid[i][j] = grid[i - 1][j] + grid[i][j - 1];
            }
        }
        return grid[m - 1][n - 1];
    }

    @Test
    public void uniquePathsTest() {
        assertEquals(3, uniquePaths(3, 2));
        assertEquals(28, uniquePaths(7, 3));
    }
}
