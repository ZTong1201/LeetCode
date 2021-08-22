import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NQueensII {

    /**
     * The n-queens puzzle is the problem of placing n queens on an n x n chessboard such that no two queens attack each other.
     * <p>
     * Given an integer n, return the number of distinct solutions to the n-queens puzzle.
     * <p>
     * Constraints:
     * <p>
     * 1 <= n <= 9
     * <p>
     * Approach: Backtrack
     * Similar question to LeetCode 51: https://leetcode.com/problems/n-queens/
     * <p>
     * Time: O(N!)
     * Space: O(N^2)
     */
    private int res;
    private boolean[][] placed;

    public int totalNQueens(int n) {
        res = 0;
        placed = new boolean[n][n];
        dfs(0, n);
        return res;
    }

    private void dfs(int row, int n) {
        if (row == n) {
            res++;
            return;
        }
        for (int col = 0; col < n; col++) {
            if (!canPlaceAtIndex(row, col, n)) continue;
            ;

            placed[row][col] = true;
            dfs(row + 1, n);
            placed[row][col] = false;
        }
    }

    private boolean canPlaceAtIndex(int row, int col, int n) {
        int i = row;
        while (i >= 0) {
            if (placed[i][col]) return false;
            i--;
        }

        i = row - 1;
        int j = col - 1;
        while (i >= 0 && j >= 0) {
            if (placed[i][j]) return false;
            i--;
            j--;
        }

        i = row - 1;
        j = col + 1;
        while (i >= 0 && j < n) {
            if (placed[i][j]) return false;
            i--;
            j++;
        }
        return true;
    }

    @Test
    public void totalNQueensTest() {
        /**
         * Example 1:
         * Input: n = 4
         * Output: 2
         */
        assertEquals(2, totalNQueens(4));
        /**
         * Example 2:
         * Input: n = 1
         * Output: 1
         */
        assertEquals(1, totalNQueens(1));
    }
}
