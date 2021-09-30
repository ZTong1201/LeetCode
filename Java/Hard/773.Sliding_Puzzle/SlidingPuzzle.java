import org.junit.Test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class SlidingPuzzle {

    /**
     * On an 2 x 3 board, there are five tiles labeled from 1 to 5, and an empty square represented by 0. A move consists of
     * choosing 0 and a 4-directionally adjacent number and swapping it.
     * <p>
     * The state of the board is solved if and only if the board is [[1,2,3],[4,5,0]].
     * <p>
     * Given the puzzle board, return the least number of moves required so that the state of the board is solved.
     * If it is impossible for the state of the board to be solved, return -1.
     * <p>
     * Constraints:
     * <p>
     * board.length == 2
     * board[i].length == 3
     * 0 <= board[i][j] <= 5
     * Each value board[i][j] is unique.
     * <p>
     * Approach: BFS
     * Basically, we can use BFS to move 0 square with its adjacent square and since BFS is used, we make sure the smallest
     * step will be returned if the board is solved. The key part of this problem is how to record the state of the board.
     * The easiest way will be serializing the entire board into a string by iterating row by row then col by col. For example,
     * "123450" will be the serialization of the solved board. Since the array is stored as reference, to avoid making changes
     * on the previous board, we will clone the entire board and push it to the queue.
     * <p>
     * Time: O(mn * (mn)!) In the worst case, we need to test out every possible permutations. For mn squares, there will be
     * (mn)! permutations to be searched. For each permutation, we need to traverse the entire board to find the serialization,
     * which takes O(mn) time. Hence, in total we have O(mn * (mn)!).
     * Space: O(mn * (mn)!) in the worst case we need to keep all the boards in the queue until we get the final results
     */
    public int slidingPuzzle(int[][] board) {
        int rows = board.length, cols = board[0].length;
        Queue<Integer> emptySquareQueue = new LinkedList<>();
        Queue<int[][]> boardQueue = new LinkedList<>();
        boardQueue.add(cloneBoard(board));
        // add the initial position of the empty square into the queue
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == 0) {
                    emptySquareQueue.add(i * cols + j);
                    break;
                }
            }
        }

        int[][] next = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        Set<String> seen = new HashSet<>();
        String solvedBoard = "123450";
        int minStep = 0;

        while (!emptySquareQueue.isEmpty()) {
            int size = emptySquareQueue.size();

            for (int i = 0; i < size; i++) {
                int id = emptySquareQueue.poll();
                int currRow = id / cols, currCol = id % cols;
                int[][] currBoard = boardQueue.poll();
                // serialize the current board
                String serializedBoard = serialize(currBoard);

                // return the minimum step so far if the board is solved
                if (serializedBoard.equals(solvedBoard)) return minStep;

                if (!seen.contains(serializedBoard)) {
                    seen.add(serializedBoard);

                    for (int[] step : next) {
                        int nextRow = currRow + step[0], nextCol = currCol + step[1];

                        if (nextRow >= 0 && nextCol >= 0 && nextRow < rows && nextCol < cols) {
                            // clone the current board
                            int[][] nextBoard = cloneBoard(currBoard);
                            // make the swap
                            swap(nextBoard, currRow, currCol, nextRow, nextCol);
                            // add the next position & next board into the queue for further search
                            emptySquareQueue.add(nextRow * cols + nextCol);
                            boardQueue.add(nextBoard);
                        }
                    }
                }
            }
            // increment the step after current level is done
            minStep++;
        }
        // if we enumerate all possible permutations but still cannot solve the board, return -1
        return -1;
    }

    private void swap(int[][] board, int row1, int col1, int row2, int col2) {
        int temp = board[row1][col1];
        board[row1][col1] = board[row2][col2];
        board[row2][col2] = temp;
    }

    private String serialize(int[][] board) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                res.append(board[i][j]);
            }
        }
        return res.toString();
    }

    private int[][] cloneBoard(int[][] board) {
        // since array more than 1 dimensions doesn't support direct clone
        // we need clone the entire board row by row
        int[][] clonedBoard = new int[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            clonedBoard[i] = board[i].clone();
        }
        return clonedBoard;
    }

    @Test
    public void slidingPuzzleTest() {
        /**
         * Example 1:
         * Input: board = [[1,2,3],[4,0,5]]
         * Output: 1
         * Explanation: Swap the 0 and the 5 in one move.
         */
        assertEquals(1, slidingPuzzle(new int[][]{{1, 2, 3}, {4, 0, 5}}));
        /**
         * Example 2:
         * Input: board = [[1,2,3],[5,4,0]]
         * Output: -1
         * Explanation: No number of moves will make the board solved.
         */
        assertEquals(-1, slidingPuzzle(new int[][]{{1, 2, 3}, {5, 4, 0}}));
        /**
         * Example 3:
         * Input: board = [[4,1,2],[5,0,3]]
         * Output: 5
         * Explanation: 5 is the smallest number of moves that solves the board.
         * An example path:
         * After move 0: [[4,1,2],[5,0,3]]
         * After move 1: [[4,1,2],[0,5,3]]
         * After move 2: [[0,1,2],[4,5,3]]
         * After move 3: [[1,0,2],[4,5,3]]
         * After move 4: [[1,2,0],[4,5,3]]
         * After move 5: [[1,2,3],[4,5,0]]
         */
        assertEquals(5, slidingPuzzle(new int[][]{{4, 1, 2}, {5, 0, 3}}));
        /**
         * Example 4:
         * Input: board = [[3,2,4],[1,5,0]]
         * Output: 14
         */
        assertEquals(14, slidingPuzzle(new int[][]{{3, 2, 4}, {1, 5, 0}}));
    }
}
