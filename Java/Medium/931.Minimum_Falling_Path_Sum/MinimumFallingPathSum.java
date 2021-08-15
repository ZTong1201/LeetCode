import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MinimumFallingPathSum {

    /**
     * Given an n x n array of integers matrix, return the minimum sum of any falling path through matrix.
     * <p>
     * A falling path starts at any element in the first row and chooses the element in the next row that is either directly
     * below or diagonally left/right. Specifically, the next element from position (row, col) will be (row + 1, col - 1),
     * (row + 1, col), or (row + 1, col + 1).
     * <p>
     * Constraints:
     * <p>
     * n == matrix.length
     * n == matrix[i].length
     * 1 <= n <= 100
     * -100 <= matrix[i][j] <= 100
     * <p>
     * Approach: Dynamic Programming
     * <p>
     * Time: O(n^2)
     * Space: O(n^2)
     */
    public int minFallingPathSum(int[][] matrix) {
        int n = matrix.length;
        int[][] dp = new int[n][n];
        // fill the first row with the matrix first row
        dp[0] = matrix[0];
        // start DP from the second row
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // if it's the first column, we can't reach the left column from the row above
                if (j == 0) {
                    dp[i][j] = Math.min(dp[i - 1][j], dp[i - 1][j + 1]);
                } else if (j == n - 1) {
                    // likewise, if it's the last column, we can't reach the right column from the row above
                    dp[i][j] = Math.min(dp[i - 1][j], dp[i - 1][j - 1]);
                } else {
                    dp[i][j] = Math.min(dp[i - 1][j], Math.min(dp[i - 1][j - 1], dp[i - 1][j + 1]));
                }
                // found the minimum value from the previous row
                // add the current value to construct minimum falling path sum at index (i, j)
                dp[i][j] += matrix[i][j];
            }
        }
        // find the global minimum in the last row
        int res = Integer.MAX_VALUE;
        for (int num : dp[n - 1]) {
            res = Math.min(res, num);
        }
        return res;
    }

    @Test
    public void minFallingPathSumTest() {
        /**
         * Example 1:
         * Input: matrix = [[2,1,3],[6,5,4],[7,8,9]]
         * Output: 13
         * Explanation: There are two falling paths with a minimum sum underlined below:
         * [[2,1,3],      [[2,1,3],
         *  [6,5,4],       [6,5,4],
         *  [7,8,9]]       [7,8,9]]
         */
        assertEquals(13, minFallingPathSum(new int[][]{{2, 1, 3}, {6, 5, 4}, {7, 8, 9}}));
        /**
         * Example 2:
         * Input: matrix = [[-19,57],[-40,-5]]
         * Output: -59
         * Explanation: The falling path with a minimum sum is underlined below:
         * [[-19,57],
         *  [-40,-5]]
         */
        assertEquals(-59, minFallingPathSum(new int[][]{{-19, 57}, {-40, -5}}));
        /**
         * Example 3:
         * Input: matrix = [[-48]]
         * Output: -48
         */
        assertEquals(-48, minFallingPathSum(new int[][]{{-48}}));
    }
}
