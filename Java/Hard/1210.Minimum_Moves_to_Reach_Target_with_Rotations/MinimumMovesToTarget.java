import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MinimumMovesToTarget {

    /**
     * In an n*n grid, there is a snake that spans 2 cells and starts moving from the top left corner at (0, 0) and (0, 1).
     * The grid has empty cells represented by zeros and blocked cells represented by ones. The snake wants to reach the
     * lower right corner at (n-1, n-2) and (n-1, n-1).
     * <p>
     * In one move the snake can:
     * <p>
     * Move one cell to the right if there are no blocked cells there. This move keeps the horizontal/vertical position of
     * the snake as it is.
     * Move down one cell if there are no blocked cells there. This move keeps the horizontal/vertical position of the snake'
     * as it is.
     * Rotate clockwise if it's in a horizontal position and the two cells under it are both empty. In that case the snake
     * moves from (r, c) and (r, c+1) to (r, c) and (r+1, c).
     * Rotate counterclockwise if it's in a vertical position and the two cells to its right are both empty. In that case
     * the snake moves from (r, c) and (r+1, c) to (r, c) and (r, c+1).
     * <p>
     * Return the minimum number of moves to reach the target.
     * <p>
     * If there is no way to reach the target, return -1.
     * <p>
     * Constraints:
     * <p>
     * 2 <= n <= 100
     * 0 <= grid[i][j] <= 1
     * It is guaranteed that the snake starts at empty cells.
     * <p>
     * Approach: Dynamic Programming
     * Construct a 2-D array of pairs in which first corresponds to the minimum moves to (i, j) facing right, and second
     * corresponds to the minimum moves to (i, j) facing down.
     * <p>
     * Initialization:
     * a 2-D array of size (n + 1, n + 1), hence dp[i][j] corresponds to dp[i - 1][j - 1] such that we can also start at the
     * beginning point.
     * dp[0][1].first = -1 and dp[1][0].first = -1, since the snake takes two consecutive cells, dp[1][1] (a.k.a. grid[0][0])
     * is non-reachable.
     * <p>
     * Transition:
     * at point (i, j), only update the result if grid[i - 1][j - 1] is 0 (meaning this cell is accessible)
     * for updating moves facing right: update the minimum value if the column index is inside the grid and
     * grid[i - 1][j] is 0.
     * dp[i][j].first = min(dp[i - 1][j].first, dp[i][j - 1].first) + 1 meaning we can move one step from the left cell or the
     * top cell.
     * <p>
     * for updating moves facing down: similar as above, we just need grid[i][j - 1] is 0 now
     * dp[i][j].second = min(dp[i - 1][j].second, dp[i][j - 1].second) + 1
     * <p>
     * if the snaking is facing right, it can also be rotated counterclockwise from facing down.
     * dp[i][j].first = min(dp[i][j].first, dp[i][j].second + 1), the minimum can be achieved if the snake was previously
     * facing down and rotate to facing right now.
     * <p>
     * if the snaking is facing down, similar as above
     * dp[i][j].second = min(dp[i][j].second, dp[i][j].first + 1)
     * <p>
     * Return:
     * return dp[n][n - 1].first since we need the snake facing right at the grid[n - 1][n - 2] cell
     * however, it's also not possible to reach the final cell. We initialize the minimum step as Integer.MAX_VALUE, and while
     * traversing the entire grid, the minimum step will keep incrementing instead of taking the minimum if it's not possible
     * to reach the end.
     * <p>
     * Time: O(n^2)
     * Space: O(n^2);
     */
    public int minimumMoves(int[][] grid) {
        int n = grid.length;
        // need extra row and column to update every cell in the grid
        Pair[][] dp = new Pair[n + 1][n + 1];
        // initialization
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= n; j++) {
                dp[i][j] = new Pair();
            }
        }
        // the following setup makes sure min step to grid[0][0] is 0
        dp[0][1].first = -1;
        dp[1][0].first = -1;

        // transition
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                boolean horizontal = false;
                boolean vertical = false;

                // update min moves when snake is facing right
                if (grid[i - 1][j - 1] == 0 && j < n && grid[i - 1][j] == 0) {
                    dp[i][j].first = Math.min(dp[i - 1][j].first, dp[i][j - 1].first) + 1;
                    // snake is still facing right afterwards
                    horizontal = true;
                }

                // update min moves when snake is facing down
                if (grid[i - 1][j - 1] == 0 && i < n && grid[i][j - 1] == 0) {
                    dp[i][j].second = Math.min(dp[i - 1][j].second, dp[i][j - 1].second) + 1;
                    // snake is still facing down afterwards
                    vertical = true;
                }

                // check whether making rotations can get a smaller min moves
                if (vertical && j < n && grid[i][j] == 0) {
                    dp[i][j].second = Math.min(dp[i][j].second, dp[i][j].first + 1);
                }
                if (horizontal && i < n && grid[i][j] == 0) {
                    dp[i][j].first = Math.min(dp[i][j].first, dp[i][j].second + 1);
                }
            }
        }
        // if the min moves is not reducing, return -1 which means the final cell is not accessible
        return dp[n][n - 1].first >= Integer.MAX_VALUE ? -1 : (int) dp[n][n - 1].first;
    }

    private static class Pair {

        // first corresponds to min moves facing right
        long first;
        // second corresponds to min moves facing down
        long second;

        public Pair() {
            // initialize the min steps as the largest possible value
            // the minimum will be updated if the final cell is accessiable
            this.first = Integer.MAX_VALUE;
            this.second = Integer.MAX_VALUE;
        }
    }

    @Test
    public void minimumMovesTest() {
        /**
         * Example 1:
         * Input: grid = [[0,0,0,0,0,1],
         *                [1,1,0,0,1,0],
         *                [0,0,0,0,1,1],
         *                [0,0,1,0,1,0],
         *                [0,1,1,0,0,0],
         *                [0,1,1,0,0,0]]
         * Output: 11
         * Explanation:
         * One possible solution is [right, right, rotate clockwise, right, down, down, down, down, rotate counterclockwise,
         * right, down].
         */
        assertEquals(11, minimumMoves(new int[][]{
                {0, 0, 0, 0, 0, 1}, {1, 1, 0, 0, 1, 0}, {0, 0, 0, 0, 1, 1},
                {0, 0, 1, 0, 1, 0}, {0, 1, 1, 0, 0, 0}, {0, 1, 1, 0, 0, 0}}));
        /**
         * Example 2:
         * Input: grid = [[0,0,1,1,1,1],
         *                [0,0,0,0,1,1],
         *                [1,1,0,0,0,1],
         *                [1,1,1,0,0,1],
         *                [1,1,1,0,0,1],
         *                [1,1,1,0,0,0]]
         * Output: 9
         */
        assertEquals(9, minimumMoves(new int[][]{
                {0, 0, 1, 1, 1, 1}, {0, 0, 0, 0, 1, 1}, {1, 1, 0, 0, 0, 1},
                {1, 1, 1, 0, 0, 1}, {1, 1, 1, 0, 0, 1}, {1, 1, 1, 0, 0, 0}}));
    }
}
