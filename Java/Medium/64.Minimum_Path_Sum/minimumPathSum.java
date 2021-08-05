import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class minimumPathSum {


    /**
     * Given a m x n grid filled with non-negative numbers, find a path from top left to bottom right which
     * minimizes the sum of all numbers along its path.
     * <p>
     * Note: You can only move either down or right at any point in time.
     * <p>
     * Constraints:
     * <p>
     * m == grid.length
     * n == grid[i].length
     * 1 <= m, n <= 200
     * 0 <= grid[i][j] <= 100
     * <p>
     * Approach 1: 2-D dynamic programming
     * Time: O(m*n);
     * Space: O(1) since we change the grid value in-place, no need for extra space
     */
    public int minPathSum2D(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0 && j == 0) continue;
                if (i == 0) grid[i][j] += grid[i][j - 1];
                else if (j == 0) grid[i][j] += grid[i - 1][j];
                else grid[i][j] += Math.min(grid[i - 1][j], grid[i][j - 1]);
            }
        }
        return grid[m - 1][n - 1];
    }

    /**
     * Approach 2: 1-D DP
     * We can downgrade the 2-D DP into a 1-D version. Essentially, when updating the minimum sum at a cell, it only
     * depends on the left and the top cell. For cell (i, j), the value from the top cell will be stored at dp[j] before
     * the value is updated, the value from the left cell is basically dp[j - 1].
     * <p>
     * Time: O(m * n)
     * Space: O(n)
     */
    public int minPathSum1D(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        int[] dp = new int[n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // assign the starting point to the dp array
                if (i == 0 && j == 0) dp[j] = grid[i][j];
                    // if it's the first row, the minimum sum will be grid[0][j] + grid[0][j - 1] (dp[j - 1])
                else if (i == 0) dp[j] = dp[j - 1] + grid[i][j];
                    // if it's the first column, the minimum sum is grid[i][0] + grid[i - 1][0] (dp[0])
                else if (j == 0) dp[j] = grid[i][j] + dp[j];
                    // otherwise, grid[i][j] = min(grid[i - 1][j] (dp[j]), grid[i][j - 1] (dp[j - 1)) + grid[i][j]
                else dp[j] = Math.min(dp[j], dp[j - 1]) + grid[i][j];
            }
        }
        return dp[n - 1];
    }

    @Test
    public void minPathSum2DTest() {
        /**
         * Example 1:
         * Input: grid = [[1,3,1],[1,5,1],[4,2,1]]
         * Output: 7
         * Explanation: Because the path 1 → 3 → 1 → 1 → 1 minimizes the sum.
         */
        int[][] grid1 = new int[][]{{1, 3, 1}, {1, 5, 1}, {4, 2, 1}};
        assertEquals(7, minPathSum2D(grid1));
        /**
         * Example 2:
         * Input: grid = [[1,2,3],[4,5,6]]
         * Output: 12
         */
        int[][] grid2 = new int[][]{{1, 2, 3}, {4, 5, 6}};
        assertEquals(12, minPathSum2D(grid2));
    }

    @Test
    public void minPathSum1DTest() {
        /**
         * Example 1:
         * Input: grid = [[1,3,1],[1,5,1],[4,2,1]]
         * Output: 7
         * Explanation: Because the path 1 → 3 → 1 → 1 → 1 minimizes the sum.
         */
        int[][] grid1 = new int[][]{{1, 3, 1}, {1, 5, 1}, {4, 2, 1}};
        assertEquals(7, minPathSum1D(grid1));
        /**
         * Example 2:
         * Input: grid = [[1,2,3],[4,5,6]]
         * Output: 12
         */
        int[][] grid2 = new int[][]{{1, 2, 3}, {4, 5, 6}};
        assertEquals(12, minPathSum1D(grid2));
    }
}
