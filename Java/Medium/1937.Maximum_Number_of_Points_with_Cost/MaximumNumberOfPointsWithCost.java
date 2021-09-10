import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MaximumNumberOfPointsWithCost {

    /**
     * You are given an m x n integer matrix points (0-indexed). Starting with 0 points, you want to maximize the number of
     * points you can get from the matrix.
     * <p>
     * To gain points, you must pick one cell in each row. Picking the cell at coordinates (r, c) will add points[r][c] to
     * your score.
     * However, you will lose points if you pick a cell too far from the cell that you picked in the previous row.
     * For every two adjacent rows r and r + 1 (where 0 <= r < m - 1), picking cells at coordinates (r, c1) and (r + 1, c2)
     * will subtract abs(c1 - c2) from your score.
     * <p>
     * Return the maximum number of points you can achieve.
     * <p>
     * abs(x) is defined as:
     * x for x >= 0
     * -x for x < 0
     * <p>
     * Constraints:
     * <p>
     * m == points.length
     * n == points[r].length
     * 1 <= m, n <= 10^5
     * 1 <= m * n <= 10^5
     * 0 <= points[r][c] <= 10^5
     * <p>
     * Approach: DP
     * We can easily come up with a DP algorithm with O(m * n * n) first in which we search the entire previous row to find
     * the largest score at that cell. This will take O(n) runtime, and we need to do it on every row except the first one.
     * Hence, this will end up with O(mn^2) which is very time-consuming. We'd like to consider a more efficient to update
     * the value at each cell. However, we actually only have 3 options to maximum the points at given cell (i, j)
     * 1. get the point from the previous row + points[i][j], this will have 0 cost
     * 2. get the point from the left part of the previous row + points[i][j] - 1 since the cost is 1
     * 3. similarly, get the point from the right part of the previous row + points[i][j] - 1
     * The information need for 2 & 3 is actually stored in dp[i - 1][j - 1] and dp[i - 1][j + 1]
     * We actually need to take the maximum from dp[i - 1][j], dp[i - 1][j - 1] - 1, dp[i - 1][j + 1] - 1 and add the point at
     * cell (i, j) to that result. Notice that the current row is only dependent upon the previous row, we can further squeeze
     * it to a 1-D array.
     * <p>
     * Time: O(mn)
     * Space: O(n)
     */
    public long maxPoints(int[][] points) {
        int m = points.length, n = points[0].length;
        // maintain a long array which has the same size of the input rows
        long[] dp = new long[n];

        for (int i = 0; i < m; i++) {
            // first from left to the right - find the maximum value we can get from the previous row
            for (int j = 1; j < n; j++) {
                dp[j] = Math.max(dp[j], dp[j - 1] - 1);
            }

            // second from right to the left - find the maximum
            for (int j = n - 2; j >= 0; j--) {
                dp[j] = Math.max(dp[j], dp[j + 1] - 1);
            }

            // the maximum from previous row has been found for each cell
            // add current points to each index
            for (int j = 0; j < n; j++) {
                dp[j] += points[i][j];
            }
        }

        long res = 0;
        // find final maximum value
        for (int i = 0; i < n; i++) {
            res = Math.max(res, dp[i]);
        }
        return res;
    }

    @Test
    public void maxPointsTest() {
        /**
         * Example 1:
         * Input: points = [
         * [1,2,3],
         * [1,5,1],
         * [3,1,1]]
         * Output: 9
         * Explanation:
         * The blue cells denote the optimal cells to pick, which have coordinates (0, 2), (1, 1), and (2, 0).
         * You add 3 + 5 + 3 = 11 to your score.
         * However, you must subtract abs(2 - 1) + abs(1 - 0) = 2 from your score.
         * Your final score is 11 - 2 = 9.
         */
        assertEquals(9, maxPoints(new int[][]{{1, 2, 3}, {1, 5, 1}, {3, 1, 1}}));
        /**
         * Example 2:
         * Input: points = [
         * [1,5],
         * [2,3],
         * [4,2]]
         * Output: 11
         * Explanation:
         * The blue cells denote the optimal cells to pick, which have coordinates (0, 1), (1, 1), and (2, 0).
         * You add 5 + 3 + 4 = 12 to your score.
         * However, you must subtract abs(1 - 1) + abs(1 - 0) = 1 from your score.
         * Your final score is 12 - 1 = 11.
         */
        assertEquals(11, maxPoints(new int[][]{{1, 5}, {2, 3}, {4, 2}}));
        /**
         * Example 3:
         * Input: points = [
         * [5,2,1,2],
         * [2,1,5,2],
         * [5,5,5,0]]
         * Output: 13
         */
        assertEquals(13, maxPoints(new int[][]{{5, 2, 1, 2}, {2, 1, 5, 2}, {5, 5, 5, 0}}));
    }
}
