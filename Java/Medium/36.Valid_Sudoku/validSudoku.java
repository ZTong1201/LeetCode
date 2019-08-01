import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class validSudoku {

    /**
     * Determine if a 9x9 Sudoku board is valid. Only the filled cells need to be validated according to the following rules:
     *
     * Each row must contain the digits 1-9 without repetition.
     * Each column must contain the digits 1-9 without repetition.
     * Each of the 9 3x3 sub-boxes of the grid must contain the digits 1-9 without repetition.
     *
     * The Sudoku board could be partially filled, where empty cells are filled with the character '.'.
     *
     * Note:
     *
     * A Sudoku board (partially filled) could be valid but is not necessarily solvable.
     * Only the filled cells need to be validated according to the mentioned rules.
     * The given board contain only digits 1-9 and the character '.'.
     * The given board size is always 9x9.
     *
     * Approach 1: Check for each position
     * 对于每一个为数字的cell，分别判断它所属行，所属列，所属格子是否符合数独规则，如不符合，直接返回false。若符合，则继续查找
     * 值得注意的是要的到该cell所属格子所在的行为i / 3, 所在列为 j / 3
     *
     * Time: O(1) 对于所有输入，只需至多访问81个格子，每个格子则会额外访问27个格子，所以为常数运行时间
     * Space: O(1) 对于行，列，格子的查询，只需每次构建一个长度为9的array即可
     */
    public boolean isValidSudokuDFS(char[][] board) {
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                //若当前位置为数字，则需要判读是否符合数独规则
                if(board[i][j] != '.') {
                    if(isRowValid(board, i) && isColValid(board, j) && isCellValid(board, i, j)) {
                        continue;
                    } else {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    //判断该位置所在行是否符合数独规则
    private boolean isRowValid(char[][] board, int row) {
        int[] map = new int[9];
        for(int i = 0; i < 9; i++) {
            char curr = board[row][i];
            if(curr != '.') {
                map[curr - '1'] += 1;
                if(map[curr - '1'] > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    //判断该位置所在列是否符合数独规则
    private boolean isColValid(char[][] board, int col) {
        int[] map = new int[9];
        for(int i = 0; i < 9; i++) {
            char curr = board[i][col];
            if(curr != '.') {
                map[curr - '1'] += 1;
                if(map[curr - '1'] > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    //判断该位置所在小格是否符合数独规则
    private boolean isCellValid(char[][] board, int i, int j) {
        int cellRow = i / 3;
        int cellCol = i / 3;
        int[] map = new int[9];
        for(int row = cellRow * 3; row < cellCol * 3 + 3; row++) {
            for(int col = cellCol * 3; col < cellCol * 3 + 3; col++) {
                char curr = board[row][col];
                if(curr != '.') {
                    map[curr - '1'] += 1;
                    if(map[curr - '1'] > 1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Approach 2: One Iteration
     *
     * 如果对每一个格子都做相同的判断，会产生很多重复判断，比如处于同一行，同一列或同一格子的位置都会被重复判断。如果只想通过一次循环得到最终结果，
     * 那就需要将该位置的数字分别放在三个不同的hash map（分别记录每一行，每一列，每一格子出现的数字）里，只要任意位置，其数字在某个hash map出现多于1次，则
     * 直接return false。
     *
     * 如果要对每一个格子进行从上至下，从左至右依次标号(0 -> 8)，则可以计算 (i / 3) * 3 + j / 3
     *
     * Time: O(1)， 只需要一次循环，最多遍历81个格子
     * Space: O(1), 为了记录所有数字，需要三个长度为9的array of hash map，每一个hash map至多会存放9个数字，因此最多需要9*9*3的空间来存放。仍为常数空间
     */
    public boolean isValidSudokuOneIteration(char[][] board) {
        Map<Integer, Integer>[] row = new HashMap[9];
        Map<Integer, Integer>[] col = new HashMap[9];
        Map<Integer, Integer>[] cell = new HashMap[9];
        //initialize hash maps
        for(int i = 0; i < 9; i++) {
            row[i] = new HashMap<>();
            col[i] = new HashMap<>();
            cell[i] = new HashMap<>();
        }
        //循环整个board一次，若某一位置位数字，则将该数字分别加到其所属行，所属列，所属格子的hash map中，若该数字已经出现过一次，直接返回false
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                if(board[i][j] != '.') {
                    int val = (int) board[i][j];
                    //计算该位置所处的格子index
                    int boxIndex = (i / 3) * 3 + (j / 3);
                    row[i].put(val, row[i].getOrDefault(val, 0) + 1);
                    col[j].put(val, col[j].getOrDefault(val, 0) + 1);
                    cell[boxIndex].put(val, cell[boxIndex].getOrDefault(val, 0) + 1);

                    if(row[i].get(val) > 1 || col[j].get(val) > 1 || cell[boxIndex].get(val) > 1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Test
    public void isValidSudokuTest() {
        /**
         * Example 1:
         * Input:
         * [
         *   ['5','3','.','.','7','.','.','.','.'],
         *   ['6','.','.','1','9','5','.','.','.'],
         *   ['.','9','8','.','.','.','.','6','.'],
         *   ['8','.','.','.','6','.','.','.','3'],
         *   ['4','.','.','8','.','3','.','.','1'],
         *   ['7','.','.','.','2','.','.','.','6'],
         *   ['.','6','.','.','.','.','2','8','.'],
         *   ['.','.','.','4','1','9','.','.','5'],
         *   ['.','.','.','.','8','.','.','7','9']
         * ]
         * Output: true
         */
        char[][] board1 = new char[][]{{'5', '3', '.', '.', '7', '.', '.', '.', '.'},
                {'6', '.', '.', '1', '9', '5', '.', '.', '.'},
                {'.', '9', '8', '.', '.', '.', '.', '6', '.'},
                {'8', '.', '.', '.', '6', '.', '.', '.', '3'},
                {'4', '.', '.', '8', '.', '3', '.', '.', '1'},
                {'7', '.', '.', '.', '2', '.', '.', '.', '6'},
                {'.', '6', '.', '.', '.', '.', '2', '8', '.'},
                {'.', '.', '.', '4', '1', '9', '.', '.', '5'},
                {'.', '.', '.', '.', '8', '.', '.', '7', '9'}};
        assertTrue(isValidSudokuDFS(board1));
        assertTrue(isValidSudokuOneIteration(board1));
        /**
         * Example 2:
         * Input:
         * [
         *   ['8','3','.','.','7','.','.','.','.'],
         *   ['6','.','.','1','9','5','.','.','.'],
         *   ['.','9','8','.','.','.','.','6','.'],
         *   ['8','.','.','.','6','.','.','.','3'],
         *   ['4','.','.','8','.','3','.','.','1'],
         *   ['7','.','.','.','2','.','.','.','6'],
         *   ['.','6','.','.','.','.','2','8','.'],
         *   ['.','.','.','4','1','9','.','.','5'],
         *   ['.','.','.','.','8','.','.','7','9']
         * ]
         * Output: false
         * Explanation: Same as Example 1, except with the 5 in the top left corner being 
         *     modified to 8. Since there are two 8's in the top left 3x3 sub-box, it is invalid.
         */
        char[][] board2 = new char[][]{{'8', '3', '.', '.', '7', '.', '.', '.', '.'},
                {'6', '.', '.', '1', '9', '5', '.', '.', '.'},
                {'.', '9', '8', '.', '.', '.', '.', '6', '.'},
                {'8', '.', '.', '.', '6', '.', '.', '.', '3'},
                {'4', '.', '.', '8', '.', '3', '.', '.', '1'},
                {'7', '.', '.', '.', '2', '.', '.', '.', '6'},
                {'.', '6', '.', '.', '.', '.', '2', '8', '.'},
                {'.', '.', '.', '4', '1', '9', '.', '.', '5'},
                {'.', '.', '.', '.', '8', '.', '.', '7', '9'}};
        assertFalse(isValidSudokuDFS(board2));
        assertFalse(isValidSudokuOneIteration(board2));
    }
}
