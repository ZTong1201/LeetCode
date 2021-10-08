import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumberOfWaysToPaintGrid {

    /**
     * Example 1:
     * You have a grid of size n x 3 and you want to paint each cell of the grid with exactly one of the three colors: Red,
     * Yellow, or Green while making sure that no two adjacent cells have the same color (i.e., no two cells that share
     * vertical or horizontal sides have the same color).
     * <p>
     * Given n the number of rows of the grid, return the number of ways you can paint this grid. As the answer may grow
     * large, the answer must be computed modulo 10^9 + 7.
     * <p>
     * Constraints:
     * <p>
     * n == grid.length
     * grid[i].length == 3
     * 1 <= n <= 5000
     * <p>
     * Approach: DP
     * Notice that there is no way we can fill in a row with only one color. We need 2 or more colors to paint the grid.
     * If we use two colors to paint the previous row, for example, Red - Yellow - Red (we can use other colors, but it will
     * be essentially the same). The for the next row,
     * 1. If we will be using two colors, which could be Yellow - Red - Yellow, Green - Red - Green, Yellow - Green - Yellow, 3 options.
     * 2. If we will be using three colors, which could be Yellow - Red - Green or Green - Red - Yellow, 2 options
     * <p>
     * If we use three colors to paint the previous row, e.g. Red - Yellow - Green
     * For the next row,
     * 1. If we will be using two colors, which could be Yellow - Red - Yellow or Yellow - Green - Yellow, 2 options
     * 2. If we will be using three colors, which could be Green - Red - Yellow, Yellow - Green - Red, 2 options
     * <p>
     * So if we denote the number of ways to paint a row in two colors as T(n) and in three colors as S(n), then the recurrence
     * relation is:
     * S(n) = 2 * T(n - 1) + 2 * S(n - 1)
     * T(n) = 3 * T(n - 1) + 2 * S(n - 1)
     * We could use a 1-D array to dynamically compute the result. However, notice that the current state is only dependent
     * upon the previous state. We can use two variables instead.
     * <p>
     * Note that, the initial state for n = 1, should be
     * S(1) = 3 * 2 * 1 = 6
     * T(1) = 3 * 2 = 6
     * <p>
     * Time: O(n)
     * Space: O(1)
     */
    public int numOfWays(int n) {
        final int MOD = 1_000_000_007;
        long twoColors = 6, threeColors = 6;

        for (int i = 2; i <= n; i++) {
            // since the previous number of two colors painting will be updated
            // create a temporary variable for it
            long tempTwoColors = twoColors;
            twoColors = (3 * twoColors + 2 * threeColors) % MOD;
            threeColors = (2 * tempTwoColors + 2 * threeColors) % MOD;
        }
        return (int) ((twoColors + threeColors) % MOD);
    }

    @Test
    public void numOfWaysTest() {
        /**
         * Example 1:
         * Input: n = 1
         * Output: 12
         * Explanation: There are 12 possible way to paint the grid as shown.
         */
        assertEquals(12, numOfWays(1));
        /**
         * Example 2:
         * Input: n = 2
         * Output: 54
         */
        assertEquals(54, numOfWays(2));
        /**
         * Example 3:
         * Input: n = 3
         * Output: 246
         */
        assertEquals(246, numOfWays(3));
        /**
         * Example 4:
         * Input: n = 7
         * Output: 106494
         */
        assertEquals(106494, numOfWays(7));
        /**
         * Example 5:
         * Input: n = 5000
         * Output: 30228214
         */
        assertEquals(30228214, numOfWays(5000));
    }
}
