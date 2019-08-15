import org.junit.Test;
import static org.junit.Assert.*;

public class gameOfLife {

    /**
     * According to the Wikipedia's article: "The Game of Life, also known simply as Life, is a cellular automaton devised by
     * the British mathematician John Horton Conway in 1970."
     *
     * Given a board with m by n cells, each cell has an initial state live (1) or dead (0). Each cell interacts with its eight neighbors
     * (horizontal, vertical, diagonal) using the following four rules (taken from the above Wikipedia article):
     *
     * Any live cell with fewer than two live neighbors dies, as if caused by under-population.
     * Any live cell with two or three live neighbors lives on to the next generation.
     * Any live cell with more than three live neighbors dies, as if by over-population..
     * Any dead cell with exactly three live neighbors becomes a live cell, as if by reproduction.
     * Write a function to compute the next state (after one update) of the board given its current state. The next state is created by
     * applying the above rules simultaneously to every cell in the current state, where births and deaths occur simultaneously.
     *
     * Approach 1: Using extra array
     * 如果可以使用额外数组，就可以很容易地得到每个位置的值，然后将每一行copy回原数组即可。
     *
     * Time: O(mn)，每个cell都需要考虑一遍，都是每个cell只会遍历其至多8个邻居。
     * Space: O(mn) 需要一个额外的二维数组
     */
    public void gameOfLifeWithExtraSpace(int[][] board) {
        int[][] copy = new int[board.length][board[0].length];
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[0].length; j++) {
                if(board[i][j] == 0) {
                    if(checkNeighbors(board, i, j) == 3) {
                        copy[i][j] = 1;
                    }
                } else {
                    int count = checkNeighbors(board, i, j);
                    if(count == 2 || count == 3) {
                        copy[i][j] = 1;
                    }
                }
            }
        }
        for(int i = 0; i < board.length; i++) {
            board[i] = copy[i].clone();
        }
    }

    private int checkNeighbors(int[][] board, int i, int j) {
        int[] dr = new int[]{0, 1, 0, -1, 1, -1, 1, -1};
        int[] dc = new int[]{1, 0, -1, 0, 1, -1, -1, 1};
        int count = 0;
        for(int k = 0; k < 8; k++) {
            int newR = i + dr[k];
            int newC = j + dc[k];
            if(newR >= 0 && newC >= 0 && newR < board.length && newC < board[0].length && board[newR][newC] == 1) {
                count += 1;
            }
        }
        return count;
    }

    @Test
    public void gameOfLifeWithExtraSpaceTest() {
        /**
         * Example:
         * Input:
         * [
         *   [0,1,0],
         *   [0,0,1],
         *   [1,1,1],
         *   [0,0,0]
         * ]
         * Output:
         * [
         *   [0,0,0],
         *   [1,0,1],
         *   [0,1,1],
         *   [0,1,0]
         * ]
         */
        int[][] board = new int[][]{{0, 1, 0}, {0, 0, 1}, {1, 1, 1}, {0, 0, 0}};
        gameOfLifeWithExtraSpace(board);
        int[][] expected = new int[][]{{0, 0, 0}, {1, 0, 1}, {0, 1, 1}, {0, 1, 0}};
        assertArrayEquals(expected, board);
    }

    /**
     * Follow up:
     * Could you solve it in-place? Remember that the board needs to be updated at the same time: You cannot update some cells first and
     * then use their updated values to update other cells.
     * In this question, we represent the board using a 2D array. In principle, the board is infinite, which would cause problems when the
     * active area encroaches the border of the array. How would you address these problems?
     *
     * Approach 2: In-place
     * 为了in-place的替代每个cell的值，可以记录当前cell的转移状态。等二次扫描时，再更新值。本质上，转移状态只有两种
     * 1.该cell原来是dead cell，但其neighbor有3个live cell，所以，它在转移后会变成1.那么可以将它暂时即为2，因为它不会影响其余cell的计数值，因为它
     *   原本是dead cell（0）
     * 2.该cell原来是live cell，但其neighbor有小于2个或大于3个live cell，所以它在转移后会变为0.因为该位置当前的状态（1）会对其他位置计数产生影响，因此
     *   可以将其记为-1。
     *
     * 根据以上状态，在每个格子计数时，需要记录周围8个格子绝对值为1的个数（1表示其无变化，一直为live cell，-1表示它原来是live cell，只是之后需要改变）。
     * 然后根据格子当前状态，和周围live cell个数，更新格子状态。
     *
     * 最后再扫一遍格子，将大于0的格子赋为1，小于0的格子赋为0即可
     *
     * Time: O(mn), 第一次扫描格子，每个格子会访问其8个邻居，需要O(8mn)，第二遍扫描格子，根据状态改变值，需要O(mn)，所以overall也是O(mn)
     * Space: O(1) in-place算法，无需额外空间.
     */

    public void gameOfLifeInPlace(int[][] board) {
        int[] dr = new int[]{0, 1, 0, -1, 1, -1, 1, -1};
        int[] dc = new int[]{1, 0, -1, 0, 1, -1, -1, 1};
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[0].length; j++) {
                int count = 0;

                for(int k = 0; k < 8; k++) {
                    int newR = i + dr[k];
                    int newC = j + dc[k];
                    if(newR >=0 && newC >=0 && newR < board.length && newC < board[0].length && Math.abs(board[newR][newC]) == 1) {
                        count += 1;
                    }
                }

                if(board[i][j] == 0 && count == 3) {
                    board[i][j] = 2;
                }
                if(board[i][j] == 1 && (count < 2 || count > 3)) {
                    board[i][j] = -1;
                }
            }
        }
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[0].length; j++) {
                if(board[i][j] > 0) {
                    board[i][j] = 1;
                } else {
                    board[i][j] = 0;
                }
            }
        }
    }

    @Test
    public void gameOfLifeInPlaceTest() {
        /**
         * Example:
         * Input:
         * [
         *   [0,1,0],
         *   [0,0,1],
         *   [1,1,1],
         *   [0,0,0]
         * ]
         * Output:
         * [
         *   [0,0,0],
         *   [1,0,1],
         *   [0,1,1],
         *   [0,1,0]
         * ]
         */
        int[][] board = new int[][]{{0, 1, 0}, {0, 0, 1}, {1, 1, 1}, {0, 0, 0}};
        gameOfLifeInPlace(board);
        int[][] expected = new int[][]{{0, 0, 0}, {1, 0, 1}, {0, 1, 1}, {0, 1, 0}};
        assertArrayEquals(expected, board);
    }
}
