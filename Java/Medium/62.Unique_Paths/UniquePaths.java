import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class UniquePaths {

    /**
     * A robot is located at the top-left corner of a m x n grid
     * <p>
     * The robot can only move either down or right at any point in time.
     * The robot is trying to reach the bottom-right corner of the grid.
     * <p>
     * How many possible unique paths are there?
     * <p>
     * Constraints:
     * <p>
     * 1 <= m, n <= 100
     * It's guaranteed that the answer will be less than or equal to 2 * 10^9.
     * <p>
     * 2-D dynamic programming
     * Time: O(m*n)
     * Space: O(m*n)
     */
    public int uniquePaths(int m, int n) {
        int[][] grid = new int[m][n];
        // fill the first row with 1's, since it can only be reached from the very left
        Arrays.fill(grid[0], 1);
        // fill the first column with 1's as well
        for (int i = 0; i < m; i++) {
            grid[i][0] = 1;
        }
        // loop through the rest of grid and update the number of paths
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                grid[i][j] = grid[i - 1][j] + grid[i][j - 1];
            }
        }
        return grid[m - 1][n - 1];
    }

    @Test
    public void uniquePathsTest() {
        /**
         * Example 1:
         * Input: m = 3, n = 7
         * Output: 28
         */
        assertEquals(3, uniquePaths(3, 2));
        /**
         * Example 2:
         * Input: m = 3, n = 2
         * Output: 3
         * Explanation:
         * From the top-left corner, there are a total of 3 ways to reach the bottom-right corner:
         * 1. Right -> Down -> Down
         * 2. Down -> Down -> Right
         * 3. Down -> Right -> Down
         */
        assertEquals(28, uniquePaths(7, 3));
    }
}
