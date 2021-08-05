import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DungeonGame {

    /**
     * The demons had captured the princess and imprisoned her in the bottom-right corner of a dungeon. The dungeon consists
     * of m x n rooms laid out in a 2D grid. Our valiant knight was initially positioned in the top-left room and must fight
     * his way through dungeon to rescue the princess.
     * <p>
     * The knight has an initial health point represented by a positive integer. If at any point his health point drops to
     * 0 or below, he dies immediately.
     * <p>
     * Some of the rooms are guarded by demons (represented by negative integers), so the knight loses health upon entering
     * these rooms; other rooms are either empty (represented as 0) or contain magic orbs that increase the knight's health
     * (represented by positive integers).
     * <p>
     * To reach the princess as quickly as possible, the knight decides to move only rightward or downward in each step.
     * <p>
     * Return the knight's minimum initial health so that he can rescue the princess.
     * <p>
     * Note that any room can contain threats or power-ups, even the first room the knight enters and the bottom-right room
     * where the princess is imprisoned.
     * <p>
     * Constraints:
     * <p>
     * m == dungeon.length
     * n == dungeon[i].length
     * 1 <= m, n <= 200
     * -1000 <= dungeon[i][j] <= 1000
     * <p>
     * Approach 1: Dynamic Programming (auxiliary space)
     * Since the future value will affect the prediction of initial health, we MUST start DP from the bottom right corner
     * and calculate it backward. The idea is that to compute the minimum health for the knight to reach the current cell,
     * for example, if there is a demon in the room (negative number -5) - in order to reach the room alive, the knight must
     * have at least 6 HP before entering the room. If there is a magic orbs, it will impact the minimum health. Essentially,
     * we can subtract the magic orb point from the HP in order to successfully pass the room to get a lower initial HP
     * (for instance, the knight needs 6 HP to get through the room, however he will gain 3 points from the magic orbs, then
     * he only needs 3 HP before entering the room). If the HP required before entering the room is less than 0, which means
     * the knight just needs to keep the minimum health point to keep him alive - which is 1.
     * <p>
     * Time: O(m * n)
     * Space: O(m * n)
     */
    public int calculateMinimumHPExtraSpace(int[][] dungeon) {
        int m = dungeon.length, n = dungeon[0].length;
        int[][] dp = new int[m][n];
        for (int i = m - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
                // initialize the starting point at bottom right corner
                if (i == m - 1 && j == n - 1) dp[i][j] = Math.max(1, 1 - dungeon[i][j]);
                    // if the knight is on the last row
                else if (i == m - 1) dp[i][j] = Math.max(1, dp[i][j + 1] - dungeon[i][j]);
                    // if the knight is on the rightmost column
                else if (j == n - 1) dp[i][j] = Math.max(1, dp[i + 1][j] - dungeon[i][j]);
                else dp[i][j] = Math.max(1, Math.min(dp[i][j + 1], dp[i + 1][j]) - dungeon[i][j]);
            }
        }
        return dp[0][0];
    }

    /**
     * Approach 2: DP in place
     * We can modify the input array to avoid using extra space
     * <p>
     * Time: O(m * n)
     * Space: O(1)
     */
    public int calculateMinimumHPInplace(int[][] dungeon) {
        int m = dungeon.length, n = dungeon[0].length;
        for (int i = m - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
                if (i == m - 1 && j == n - 1) dungeon[i][j] = Math.max(1, 1 - dungeon[i][j]);
                else if (i == m - 1) dungeon[i][j] = Math.max(1, dungeon[i][j + 1] - dungeon[i][j]);
                else if (j == n - 1) dungeon[i][j] = Math.max(1, dungeon[i + 1][j] - dungeon[i][j]);
                else dungeon[i][j] = Math.max(1, Math.min(dungeon[i + 1][j], dungeon[i][j + 1]) - dungeon[i][j]);
            }
        }
        return dungeon[0][0];
    }

    @Test
    public void calculateMinimumHPTest() {
        /**
         * Example 1:
         * Input: dungeon = [[-2,-3,3],[-5,-10,1],[10,30,-5]]
         * Output: 7
         * Explanation: The initial health of the knight must be at least 7 if he follows the optimal path:
         * RIGHT-> RIGHT -> DOWN -> DOWN.
         */
        assertEquals(7, calculateMinimumHPExtraSpace(new int[][]{{-2, -3, 3}, {-5, -10, 1}, {10, 30, -5}}));
        assertEquals(7, calculateMinimumHPInplace(new int[][]{{-2, -3, 3}, {-5, -10, 1}, {10, 30, -5}}));
        /**
         * Example 2:
         * Input: dungeon = [[0]]
         * Output: 1
         */
        assertEquals(1, calculateMinimumHPExtraSpace(new int[][]{{0}}));
        assertEquals(1, calculateMinimumHPInplace(new int[][]{{0}}));
    }
}
