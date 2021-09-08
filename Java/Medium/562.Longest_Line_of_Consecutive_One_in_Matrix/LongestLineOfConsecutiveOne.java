import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LongestLineOfConsecutiveOne {

    /**
     * Given an m x n binary matrix mat, return the length of the longest line of consecutive one in the matrix.
     * <p>
     * The line could be horizontal, vertical, diagonal, or anti-diagonal.
     * <p>
     * Constraints:
     * <p>
     * m == mat.length
     * n == mat[i].length
     * 1 <= m, n <= 10^4
     * 1 <= m * n <= 10^4
     * mat[i][j] is either 0 or 1.
     * <p>
     * Approach: DP
     * Use a 3-D array to record the maximum length for each direction (horizontal, vertical, diagonal, anti-diagonal).
     * For example, dp[i][j][0] indicates the maximum length of consecutive ones until cell (i, j) horizontally.
     * <p>
     * Time: O(mn) since for each cell we need to check 4 states O(4mn) = O(mn)
     * Space: O(mn)
     */
    public int longestLine(int[][] mat) {
        int m = mat.length, n = mat[0].length;
        int[][][] dp = new int[m][n][4];
        int res = 0;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // update length if we have a one
                // always need to make sure the search is still in the grid
                if (mat[i][j] == 1) {
                    // increment length horizontally
                    dp[i][j][0] = j > 0 ? dp[i][j - 1][0] + 1 : 1;
                    // increment length vertically
                    dp[i][j][1] = i > 0 ? dp[i - 1][j][1] + 1 : 1;
                    // increment length diagonally
                    dp[i][j][2] = (i > 0 && j > 0) ? dp[i - 1][j - 1][2] + 1 : 1;
                    // increment length anti-diagonally
                    dp[i][j][3] = (i > 0 && j < n - 1) ? dp[i - 1][j + 1][3] + 1 : 1;
                }
                // update the maximum length so far
                res = Math.max(res, Math.max(dp[i][j][0], Math.max(dp[i][j][1], Math.max(dp[i][j][2], dp[i][j][3]))));
            }
        }
        return res;
    }

    @Test
    public void longestLineTest() {
        /**
         * Example 1:
         * Input: mat = [
         * [0,1,1,0],
         * [0,1,1,0],
         * [0,0,0,1]]
         * Output: 3
         */
        assertEquals(3, longestLine(new int[][]{{0, 1, 1, 0}, {0, 1, 1, 0}, {0, 0, 0, 1}}));
        /**
         * Example 2:
         * Input: mat = [[1,1,1,1],[0,1,1,0],[0,0,0,1]]
         * Output: 4
         */
        assertEquals(4, longestLine(new int[][]{{1, 1, 1, 1}, {0, 1, 1, 0}, {0, 0, 0, 1}}));
    }
}
