import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class surroundedRegions {

    /**
     * Given an m x n matrix board containing 'X' and 'O', capture all regions that are 4-directionally surrounded by 'X'.
     * <p>
     * A region is captured by flipping all 'O's into 'X's in that surrounded region.
     * <p>
     * Constraints:
     * <p>
     * m == board.length
     * n == board[i].length
     * 1 <= m, n <= 200
     * board[i][j] is 'X' or 'O'.
     * <p>
     * Approach: DFS
     * Basically, the key part of this problem is to avoid flipping any 'O's to 'X's if it connects to the border. Then, we can
     * first traverse all the cells on the boundary and mark them as inaccessible (e.g. flip those 'O's into '*'s). We can now
     * safely change any connected 'O's into 'X's afterwards. Eventually, we need to go through the board again to flip those
     * '*'s into 'O's.
     * 1. change our connected 'O's to the borders into '*'s
     * 2. change the rest of 'O's into 'X's
     * 3. change the '*'s back to 'O's
     * <p>
     * Time: O(mn) in the worst case, we traverse the entire grid 3 times
     * Space: O(mn) for the call stack
     */
    public void solve(char[][] board) {
        int rows = board.length, cols = board[0].length;

        // first traversal - change boundary connected 'O's to '*'s
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if ((i == 0 || j == 0 || i == rows - 1 || j == cols - 1) && board[i][j] == 'O') {
                    fillCellWithNewChar(board, i, j, 'O', '*');
                }
            }
        }

        // second traversal - change the rest of 'O's into 'X's
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == 'O') {
                    fillCellWithNewChar(board, i, j, 'O', 'X');
                }
            }
        }

        // third traversal - change the '*'s back to 'O's
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == '*') {
                    fillCellWithNewChar(board, i, j, '*', 'O');
                }
            }
        }
    }

    private void fillCellWithNewChar(char[][] board, int i, int j, char oldChar, char newChar) {
        if (i < 0 || j < 0 || i >= board.length || j >= board[0].length || board[i][j] != oldChar) return;
        board[i][j] = newChar;
        fillCellWithNewChar(board, i + 1, j, oldChar, newChar);
        fillCellWithNewChar(board, i - 1, j, oldChar, newChar);
        fillCellWithNewChar(board, i, j + 1, oldChar, newChar);
        fillCellWithNewChar(board, i, j - 1, oldChar, newChar);
    }


    @Test
    public void solveTest() {
        /**
         * Example:
         * Input:
         * X X X X
         * X O O X
         * X X O X
         * X O X X
         *
         * After running your function, the board should be:
         * X X X X
         * X X X X
         * X X X X
         * X O X X
         *
         * Explanation:
         *
         * Surrounded regions shouldn’t be on the border, which means that any 'O' on the border of the board are not flipped to 'X'.
         * Any 'O' that is not on the border and it is not connected to an 'O' on the border will be flipped to 'X'.
         * Two cells are connected if they are adjacent cells connected horizontally or vertically.
         */
        char[][] board = new char[][]{{'X', 'X', 'X', 'X'}, {'X', 'O', 'O', 'X'}, {'X', 'X', 'O', 'X'}, {'X', 'O', 'X', 'X'}};
        solve(board);
        char[][] expected = new char[][]{{'X', 'X', 'X', 'X'}, {'X', 'X', 'X', 'X'}, {'X', 'X', 'X', 'X'}, {'X', 'O', 'X', 'X'}};
        assertArrayEquals(expected, board);
    }
}
