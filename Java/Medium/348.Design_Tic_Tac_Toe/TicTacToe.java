public class TicTacToe {

    /**
     * Design a Tic-tac-toe game that is played between two players on a n x n grid.
     * <p>
     * You may assume the following rules:
     * <p>
     * A move is guaranteed to be valid and is placed on an empty block.
     * Once a winning condition is reached, no more moves is allowed.
     * A player who succeeds in placing n of their marks in a horizontal, vertical, or diagonal row wins the game.
     * <p>
     * Constraints:
     * <p>
     * 2 <= n <= 100
     * player is 1 or 2.
     * 0 <= row, col < n
     * (row, col) are unique for each different call to move.
     * At most n^2 calls will be made to move.
     * <p>
     * Follow up:
     * Could you do better than O(n^2) per move() operation?
     * <p>
     * Approach: Accumulate moves
     * 可以将grid中每一行，每一列，两个对角线的move累加起来。因为输入保证了不会有重复的move，那么若为player 1的turn，将对应行，列，（如果落在对角线上，也包括
     * 对角线位置）的位置加上1，若为player 2，则在上述位置中都减去1。若在任意时刻，某个player的move结束以后，其所在行，列（或对角线）上累积的move为n，说明
     * player 1胜，若为-n，说明player 2胜。若其他情况，则没人胜出，返回0。当有人胜出后，再做任何move都只返回当前结果，不会再做累加。
     * <p>
     * Time: move - O(1) 每次只判断所在行，列和对角线的累加值即可
     * Space: O(n)，需要两个数组记录行和列的累加情况
     */
    private final int[] rows, cols;
    private final int size;
    private int winner;
    private int diagonal, antiDiagonal;

    /**
     * Initialize your data structure here.
     */
    public TicTacToe(int n) {
        this.size = n;
        this.winner = 0;
        this.rows = new int[n];
        this.cols = new int[n];
        this.diagonal = 0;
        this.antiDiagonal = 0;
    }

    /**
     * Player {player} makes a move at ({row}, {col}).
     *
     * @param row    The row of the board.
     * @param col    The column of the board.
     * @param player The player, can be either 1 or 2.
     * @return The current winning condition, can be either:
     * 0: No one wins.
     * 1: Player 1 wins.
     * 2: Player 2 wins.
     */
    public int move(int row, int col, int player) {
        //若有人胜出，则直接返回结果，不再move
        if (winner != 0) return winner;
        //若当前为player 1的turn，则需要累加
        //若为player 2的turn，则需要在对应位置累减
        int currPlayer = player == 1 ? 1 : -1;
        rows[row] += currPlayer;
        cols[col] += currPlayer;
        //若其落在某个对角线上，需要将对角线上的值累加
        //对角线上的左边满足 row - col == 0, e.g. (0, 0), (1, 1), (2, 2)
        if (row - col == 0) {
            diagonal += currPlayer;
        }
        //反对角线上的左边满足 row + col == n - 1, e.g. (0, 2), (1, 1), (2, 0)
        if (row + col == size - 1) {
            antiDiagonal += currPlayer;
        }
        //判断是否有人胜出
        if (Math.abs(rows[row]) == size || Math.abs(cols[col]) == size ||
                Math.abs(diagonal) == size || Math.abs(antiDiagonal) == size) {
            winner = player;
            return winner;
        }
        return 0;
    }
}
