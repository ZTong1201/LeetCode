import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class NQueens {

    /**
     * The n-queens puzzle is the problem of placing n queens on an n x n chessboard such that no two queens attack each other.
     * <p>
     * Given an integer n, return all distinct solutions to the n-queens puzzle. You may return the answer in any order.
     * <p>
     * Each solution contains a distinct board configuration of the n-queens' placement, where 'Q' and '.' both indicate
     * a queen and an empty space, respectively.
     * <p>
     * Constraints:
     * <p>
     * 1 <= n <= 9
     * <p>
     * Approach: Backtrack
     * Since two queens cannot be placed at the same row, we can enumerate all the possible combinations by recursively
     * picking a place at each row. If at a given place, it violates the rule (two queens cannot attach each other), we
     * immediately dump the solution and keep searching the next possible place.
     * <p>
     * Time: O(N!) when a column is picked at a given, there is no way we can place a queen in that row. Hence, we have N
     * choices in the first row, N - 1 choices in the second row, ... we have N * (N - 1) * (N - 2) = N! options to search.
     * Even though we need to check whether a given option is a possible layout, which might take O(N^2) time in the worst
     * case, however, the number of all possible options will eventually dominate the runtime.
     * Space: O(N^2) we need a 2-D array to keep track of whether a cell has been placed with a queen
     */
    private List<List<String>> res;
    private boolean[][] placed;

    public List<List<String>> solveNQueens(int n) {
        // need a 2-D boolean array to keep track of on which cell a queen has been placed
        placed = new boolean[n][n];
        res = new ArrayList<>();
        dfs(0, n, new ArrayList<>());
        return res;
    }

    private void dfs(int row, int n, List<String> grid) {
        if (row == n) {
            // if a queen can be placed at each row, then add current grid to the final list
            res.add(new ArrayList<>(grid));
            return;
        }
        // enumerate all possible places at a given row
        for (int col = 0; col < n; col++) {
            // check whether placing a queen at current cell is valid
            if (!canPlaceAtIndex(row, col, n)) continue;
            // only proceed when it's valid
            // construct the row by placing the queen at column i
            String currRow = constructRow(col, n);

            // add current row to the grid
            grid.add(currRow);
            // mark current cell as visited
            placed[row][col] = true;

            // keep placing a queen in the next row
            dfs(row + 1, n, grid);

            // current solution is done searching - backtrack
            // remove the previous valid row
            grid.remove(grid.size() - 1);
            // mark the cell as unvisited again
            placed[row][col] = false;
        }
    }

    private boolean canPlaceAtIndex(int row, int col, int n) {
        int i = row;
        // in order to place a queen at current row
        // check whether there is queen at the same column in the rows above
        while (i >= 0) {
            if (placed[i][col]) return false;
            i--;
        }
        // reinitialize row and col
        i = row - 1;
        int j = col - 1;
        // check whether there is a queen placed in the top left diagonal line
        while (i >= 0 && j >= 0) {
            if (placed[i][j]) return false;
            i--;
            j--;
        }
        // reinitialize row and col
        i = row - 1;
        j = col + 1;
        // check whether there is a queen placed in the top right diagonal line
        while (i >= 0 && j < n) {
            if (placed[i][j]) return false;
            i--;
            j++;
        }
        return true;
    }

    private String constructRow(int col, int n) {
        StringBuilder sb = new StringBuilder();
        // place . before current column
        for (int i = 0; i < col; i++) {
            sb.append('.');
        }
        // place a queen
        sb.append('Q');
        // keep placing . after current column
        for (int i = col + 1; i < n; i++) {
            sb.append('.');
        }
        return sb.toString();
    }

    @Test
    public void solveNQueensTest() {
        /**
         * Input: n = 4
         * Output: [[".Q..","...Q","Q...","..Q."],["..Q.","Q...","...Q",".Q.."]]
         */
        List<List<String>> expected1 = List.of(List.of(".Q..", "...Q", "Q...", "..Q."),
                List.of("..Q.", "Q...", "...Q", ".Q.."));
        List<List<String>> actual1 = solveNQueens(4);
        assertEquals(expected1.size(), actual1.size());
        for (int i = 0; i < expected1.size(); i++) {
            assertEquals(expected1.get(i), actual1.get(i));
        }
        /**
         * Input: n = 1
         * Output: [["Q"]]
         */
        List<List<String>> expected2 = List.of(List.of("Q"));
        List<List<String>> actual2 = solveNQueens(1);
        assertEquals(expected2.size(), actual2.size());
        for (int i = 0; i < expected2.size(); i++) {
            assertEquals(expected2.get(i), actual2.get(i));
        }
    }
}
