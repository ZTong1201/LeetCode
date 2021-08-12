import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class SudokuSolver {

    /**
     * Write a program to solve a Sudoku puzzle by filling the empty cells.
     * <p>
     * A sudoku solution must satisfy all the following rules:
     * <p>
     * Each of the digits 1-9 must occur exactly once in each row.
     * Each of the digits 1-9 must occur exactly once in each column.
     * Each of the digits 1-9 must occur exactly once in each of the 9 3x3 sub-boxes of the grid.
     * The '.' character indicates empty cells.
     * <p>
     * Constraints:
     * <p>
     * board.length == 9
     * board[i].length == 9
     * board[i][j] is a digit or '.'.
     * It is guaranteed that the input board has only one solution.
     * <p>
     * Approach: DFS + Backtrack
     * Using DFS to search all possible combination row by row.
     * At a given cell, we enumerate 1-9 and if it doesn't violate the sudoku rule, we move to the next column and see
     * whether it can finally solve the sudoku.
     * Something very important for this algorithm:
     * 1. the helper function should return a boolean type to indicate whether the sudoku is solved or not
     * 2. the sudoku is solved row by row, hence when the row index is incremented, the column index needs to be reset
     * as 0 to start a new row
     * 3. need to keep track of existing numbers for each row, column, and cell to check whether placing a random value
     * from 1-9 is valid or not in O(1) time
     * <p>
     * Time: (9^(9*9)) For each empty cell, we have 9 options to search, in the worst case, all 9*9 cells need to be searched
     * Space: (3*9*9) need 3 2-D (9x9) arrays to keep track of existing numbers for each row, column, and cell
     */
    private boolean[][] _row, _col, _cell;

    public void solveSudoku(char[][] board) {
        _row = new boolean[9][9];
        _col = new boolean[9][9];
        _cell = new boolean[9][9];
        // traverse the board and record existing numbers
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] != '.') {
                    int cellRow = i / 3, cellCol = j / 3;
                    // convert char 1-9 to index 0-8
                    int num = board[i][j] - '1';
                    _row[i][num] = true;
                    _col[j][num] = true;
                    _cell[cellRow * 3 + cellCol][num] = true;
                }
            }
        }
        solve(board, 0, 0);
    }

    private boolean solve(char[][] board, int row, int col) {
        // since the sudoku is solved row by row
        // once we move to the next row, the column index must be reset
        for (int i = row; i < 9; i++, col = 0) {
            for (int j = col; j < 9; j++) {
                if (board[i][j] != '.') continue;
                // enumerate all values 1-9 for an empty cell
                for (char k = '1'; k <= '9'; k++) {
                    int cellRow = i / 3, cellCol = j / 3;
                    // only proceed when current value doesn't violate sudoku rule
                    if (!_row[i][k - '1'] && !_col[j][k - '1'] && !_cell[cellRow * 3 + cellCol][k - '1']) {
                        // mark the number as used in row, col, and cell
                        _row[i][k - '1'] = true;
                        _col[j][k - '1'] = true;
                        _cell[cellRow * 3 + cellCol][k - '1'] = true;
                        board[i][j] = k;
                        // return true if it actually solves the sudoku already
                        if (solve(board, i, j + 1)) return true;
                        // if not - backtrack to the previous state
                        _row[i][k - '1'] = false;
                        _col[j][k - '1'] = false;
                        _cell[cellRow * 3 + cellCol][k - '1'] = false;
                        board[i][j] = '.';
                    }
                }
                // if all 9 options have been tried but still cannot solve the sudoku
                // return false and further backtrack to previous cell
                return false;
            }
        }
        // all 9x9 cells have been correctly placed with number - return true;
        return true;
    }

    @Test
    public void solveSudokuTest() {
        /**
         * Example:
         * Input: board = [
         * ['5','3','.','.','7','.','.','.','.'],
         * ['6','.','.','1','9','5','.','.','.'],
         * ['.','9','8','.','.','.','.','6','.'],
         * ['8','.','.','.','6','.','.','.','3'],
         * ['4','.','.','8','.','3','.','.','1'],
         * ['7','.','.','.','2','.','.','.','6'],
         * ['.','6','.','.','.','.','2','8','.'],
         * ['.','.','.','4','1','9','.','.','5'],
         * ['.','.','.','.','8','.','.','7','9']]
         * Output: [
         * ['5','3','4','6','7','8','9','1','2'],
         * ['6','7','2','1','9','5','3','4','8'],
         * ['1','9','8','3','4','2','5','6','7'],
         * ['8','5','9','7','6','1','4','2','3'],
         * ['4','2','6','8','5','3','7','9','1'],
         * ['7','1','3','9','2','4','8','5','6'],
         * ['9','6','1','5','3','7','2','8','4'],
         * ['2','8','7','4','1','9','6','3','5'],
         * ['3','4','5','2','8','6','1','7','9']]
         */
        char[][] board = new char[][]{
                {'5', '3', '.', '.', '7', '.', '.', '.', '.'},
                {'6', '.', '.', '1', '9', '5', '.', '.', '.'},
                {'.', '9', '8', '.', '.', '.', '.', '6', '.'},
                {'8', '.', '.', '.', '6', '.', '.', '.', '3'},
                {'4', '.', '.', '8', '.', '3', '.', '.', '1'},
                {'7', '.', '.', '.', '2', '.', '.', '.', '6'},
                {'.', '6', '.', '.', '.', '.', '2', '8', '.'},
                {'.', '.', '.', '4', '1', '9', '.', '.', '5'},
                {'.', '.', '.', '.', '8', '.', '.', '7', '9'}};
        char[][] expected = new char[][]{
                {'5', '3', '4', '6', '7', '8', '9', '1', '2'},
                {'6', '7', '2', '1', '9', '5', '3', '4', '8'},
                {'1', '9', '8', '3', '4', '2', '5', '6', '7'},
                {'8', '5', '9', '7', '6', '1', '4', '2', '3'},
                {'4', '2', '6', '8', '5', '3', '7', '9', '1'},
                {'7', '1', '3', '9', '2', '4', '8', '5', '6'},
                {'9', '6', '1', '5', '3', '7', '2', '8', '4'},
                {'2', '8', '7', '4', '1', '9', '6', '3', '5'},
                {'3', '4', '5', '2', '8', '6', '1', '7', '9'}};
        solveSudoku(board);
        assertArrayEquals(expected, board);
    }
}
