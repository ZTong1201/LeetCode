public class TicTacToe {

    /**
     * Design a Tic-tac-toe game that is played between two players on a n x n grid.
     *
     * You may assume the following rules:
     *
     * A move is guaranteed to be valid and is placed on an empty block.
     * Once a winning condition is reached, no more moves is allowed.
     * A player who succeeds in placing n of their marks in a horizontal, vertical, or diagonal row wins the game.
     *
     * Follow up:
     * Could you do better than O(n2) per move() operation?
     *
     * Approach: Accumulate moves
     * 可以将grid中每一行，每一列，两个对角线的move累加起来。因为输入保证了不会有重复的move，那么若为player 1的turn，将对应行，列，（如果落在对角线上，也包括
     * 对角线位置）的位置加上1，若为player 2，则在上述位置中都减去1。若在任意时刻，某个player的move结束以后，其所在行，列（或对角线）上累积的move为n，说明
     * player 1胜，若为-n，说明player 2胜。若其他情况，则没人胜出，返回0。当有人胜出后，再做任何move都只返回当前结果，不会再做累加。
     *
     * Time: move - O(1) 每次只判断所在行，列和对角线的累加值即可
     * Space: O(n)，需要两个数组记录行和列的累加情况
     */
    private int[] rows;
    private int[] cols;
    private int size;
    private int win;
    private int diagonal;
    private int anti_diagonal;

    /** Initialize your data structure here. */
    public TicTacToe(int n) {
        this.size = n;
        this.win = 0;
        this.rows = new int[this.size];
        this.cols = new int[this.size];
        this.diagonal = 0;
        this.anti_diagonal = 0;
    }

    /** Player {player} makes a move at ({row}, {col}).
     @param row The row of the board.
     @param col The column of the board.
     @param player The player, can be either 1 or 2.
     @return The current winning condition, can be either:
     0: No one wins.
     1: Player 1 wins.
     2: Player 2 wins. */
    public int move(int row, int col, int player) {
        //若有人胜出，则直接返回结果，不再move
        if(win != 0) return win;
        //若当前为player 1的turn，则需要累加
        //若为player 2的turn，则需要在对应位置累减
        int move = player == 1 ? 1 : -1;
        rows[row] += move;
        cols[col] += move;
        //若其落在某个对角线上，需要将对角线上的值累加
        if(row == col) {
            diagonal += move;
        }
        if(row + col == size - 1) {
            anti_diagonal += move;
        }
        //判断是否有人胜出
        if(rows[row] == size || cols[col] == size || diagonal == size || anti_diagonal == size) {
            win = 1;
        } else if(rows[row] == -size || cols[col] == -size || diagonal == -size || anti_diagonal == -size){
            win = 2;
        }
        return win;
    }
}
