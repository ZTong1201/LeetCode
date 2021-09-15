import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class RotatingTheBox {

    /**
     * You are given an m x n matrix of characters box representing a side-view of a box. Each cell of the box is one of the
     * following:
     * <p>
     * A stone '#'
     * A stationary obstacle '*'
     * Empty '.'
     * The box is rotated 90 degrees clockwise, causing some of the stones to fall due to gravity. Each stone falls down until
     * it lands on an obstacle, another stone, or the bottom of the box. Gravity does not affect the obstacles' positions,
     * and the inertia from the box's rotation does not affect the stones' horizontal positions.
     * <p>
     * It is guaranteed that each stone in box rests on an obstacle, another stone, or the bottom of the box.
     * <p>
     * Return an n x m matrix representing the box after the rotation described above.
     * <p>
     * Constraints:
     * <p>
     * m == box.length
     * n == box[i].length
     * 1 <= m, n <= 500
     * box[i][j] is either '#', '*', or '.'.
     * <p>
     * Approach: Simulation.
     * First, we need to find a relation between coordinate in the original matrix and the coordinate in the rotated matrix.
     * Given a m*n matrix, the rotated matrix will have size n*m.
     * For a coordinate (row, col)
     * box[row][col] = rotated[col][m - 1 - row], i.e. the original column number will become the row number, then the first row
     * will become the last column, etc.
     * Once that is determined, we need to slide stones vertically by gravity. Hence, let's assume the bottom index in the column
     * will be the first empty cell. Then we have 3 possibilities
     * 1. If box[row][col] is '.' (an empty cell), assign it as is
     * 2. If box[row][col] is '*' (an obstacle), assign it as is + update the empty cell index
     * 3. If box[row][col] is '#' (a stone), assign it to the empty cell index + update the empty cell index (decrement by 1)
     * <p>
     * Time: O(mn) we traverse the entire box array once and assign them to the rotated array
     * Space: O(mn)
     */
    public char[][] rotateTheBox(char[][] box) {
        int m = box.length, n = box[0].length;
        char[][] rotated = new char[n][m];

        // start from first row - which will become the last column in the rotated matrix
        for (int row = 0; row < m; row++) {
            // assign the current row backward because it will slide down due to gravity
            // assume the bottom cell is now open
            for (int col = n - 1, emptyIndex = n - 1; col >= 0; col--) {
                // always assign the corresponding cell as empty first
                // since both stone and obstacle will be handled differently
                rotated[col][m - 1 - row] = '.';
                // if the original cell is not an empty cell - either a stone or an obstacle
                if (box[row][col] != '.') {
                    // find the correct row index to place it
                    // if it's an obstacle - nothing slides, the desired index will be col
                    // if it's a stone, it will slide to the previous stored empty index
                    emptyIndex = (box[row][col] == '*') ? col : emptyIndex;
                    // assign the value
                    rotated[emptyIndex][m - 1 - row] = box[row][col];
                    // current empty index has been placed with a stone or an obstacle
                    // the next empty cell will be the one above
                    emptyIndex--;
                }
            }
        }
        return rotated;
    }

    @Test
    public void rotateTheBoxTest() {
        /**
         * Example 1:
         * Input: box = [["#",".","#"]]
         * Output: [["."],
         *          ["#"],
         *          ["#"]]
         */
        char[][] expected1 = new char[][]{{'.'}, {'#'}, {'#'}};
        assertArrayEquals(expected1, rotateTheBox(new char[][]{{'#', '.', '#'}}));
        /**
         * Example 2:
         * Input: box = [["#",".","*","."],
         *               ["#","#","*","."]]
         * Output: [["#","."],
         *          ["#","#"],
         *          ["*","*"],
         *          [".","."]]
         */
        char[][] expected2 = new char[][]{{'#', '.'}, {'#', '#'}, {'*', '*'}, {'.', '.'}};
        assertArrayEquals(expected2, rotateTheBox(new char[][]{{'#', '.', '*', '.'}, {'#', '#', '*', '.'}}));
        /**
         * Example 3:
         * Input: box = [["#","#","*",".","*","."],
         *               ["#","#","#","*",".","."],
         *               ["#","#","#",".","#","."]]
         * Output: [[".","#","#"],
         *          [".","#","#"],
         *          ["#","#","*"],
         *          ["#","*","."],
         *          ["#",".","*"],
         *          ["#",".","."]]
         */
        char[][] expected3 = new char[][]{{'.', '#', '#'}, {'.', '#', '#'}, {'#', '#', '*'},
                {'#', '*', '.'}, {'#', '.', '*'}, {'#', '.', '.'}};
        assertArrayEquals(expected3, rotateTheBox(new char[][]{{'#', '#', '*', '.', '*', '.'},
                {'#', '#', '#', '*', '.', '.'}, {'#', '#', '#', '.', '#', '.'}}));
    }
}
