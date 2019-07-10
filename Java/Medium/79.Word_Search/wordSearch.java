import org.junit.Test;
import static org.junit.Assert.*;

public class wordSearch {
    
    /**
     * Given a 2D board and a word, find if the word exists in the grid.
     *
     * The word can be constructed from letters of sequentially adjacent cell,
     * where "adjacent" cells are those horizontally or vertically neighboring.
     * The same letter cell may not be used more than once.
     *
     * Approach 1: Depth-First Search (DFS)
     * This is a typical DFS problem. For each entry in the grid, we search as deep as possible to search for the result.
     * We return false for the given starting cell, if at any time:
     * 1. The search point is out of the grid
     * 2. We reached a cell that has been visited before (since the same cell may not be used more than once)
     * 3. The letter in the cell is different with the word at the desired index
     * If we meet all the requirements when all letters have been found, we return true.
     *
     * The key part for this algorithm is, when we done search for a given cell, we need reset the cell to be unvisited (backtrack).
     *
     * Time: O(m*n*4^k) where m is the number of row, n is the number of length, k is the length of word. In the worst case, we need to do
     *       dfs for every cell in the grid and the search requires at most 4^k iterations. When k is small, the runtime is just O(m*n)
     * Space: O(m*n), we need a 2-d boolean array to record whether we visit a cell or not. The call stack will be O(4^k) space, if k is
     *        small, the O(m*n) will dominate the complexity
     */
    public boolean exist1(char[][] board, String word) {
        if(board.length == 0) return false;
        int R = board.length, C = board[0].length;
        boolean[][] visited = new boolean[R][C];
        for(int i = 0; i < R; i++) {
            for(int j = 0; j < C; j++) {
                if(dfs(board, i, j, word, 0, visited)) return true;
            }
        }
        return false;
    }

    private boolean dfs(char[][] board, int i, int j, String word, int index, boolean[][] visited) {
        if(index == word.length()) return true;
        if(i < 0 || j < 0 || i >= board.length || j >= board[0].length || visited[i][j] || word.charAt(index) != board[i][j]) return false;
        visited[i][j] = true; //record as visited
        boolean res = dfs(board, i + 1, j, word, index + 1, visited) || dfs(board, i - 1, j, word, index + 1, visited)
                || dfs(board, i, j + 1, word, index + 1, visited) || dfs(board, i, j - 1, word, index + 1, visited);
        //MOST IMPORTANT! We need backtrack after searching for the given cell
        visited[i][j] = false;
        return res;
    }

    /**
     * Approach 2: DFS without visited array
     */
    public boolean exist2(char[][] board, String word) {
        if(board.length == 0) return false;
        int R = board.length;
        int C = board[0].length;
        for(int i = 0; i < R; i++) {
            for(int j = 0; j < C; j++) {
                if(dfs2(board, i, j, word, 0)) return true;
            }
        }
        return false;
    }

    private boolean dfs2(char[][] board, int i, int j, String word, int index) {
        if(index == word.length()) return true;
        if(i < 0 || j < 0 || i >= board.length || j >= board[0].length || board[i][j] != word.charAt(index)) return false;
        char tmp = board[i][j];
        board[i][j] = '*';
        boolean res = dfs2(board, i + 1, j, word, index + 1) || dfs2(board, i - 1, j, word, index + 1) ||
                dfs2(board, i, j + 1, word, index + 1) || dfs2(board, i, j - 1, word, index + 1);
        board[i][j] = tmp;
        return res;
    }

    @Test
    public void existTest1() {
        /**
         * Example 1:
         * board =
         * [
         *   ['A','B','C','E'],
         *   ['S','F','C','S'],
         *   ['A','D','E','E']
         * ]
         *
         * Given word = "ABCCED", return true.
         * Given word = "SEE", return true.
         * Given word = "ABCB", return false.
         */
        char[][] board1 = new char[][]{{'A', 'B', 'C', 'E'}, {'S', 'F', 'C', 'S'}, {'A', 'D', 'E', 'E'}};
        assertTrue(exist1(board1, "ABCCED"));
        assertTrue(exist1(board1, "SEE"));
        assertFalse(exist1(board1, "ABCB"));
        /**
         * Example 2:
         * board =
         * [
         *   ['a', 'a']
         * ]
         *
         * Given word = "aaa", return false.
         */
        char[][] board2 = new char[][]{{'a', 'a'}};
        assertFalse(exist1(board2, "aaa"));
    }

    @Test
    public void existTest2() {
        /**
         * Example 1:
         * board =
         * [
         *   ['A','B','C','E'],
         *   ['S','F','C','S'],
         *   ['A','D','E','E']
         * ]
         *
         * Given word = "ABCCED", return true.
         * Given word = "SEE", return true.
         * Given word = "ABCB", return false.
         */
        char[][] board1 = new char[][]{{'A', 'B', 'C', 'E'}, {'S', 'F', 'C', 'S'}, {'A', 'D', 'E', 'E'}};
        assertTrue(exist2(board1, "ABCCED"));
        assertTrue(exist2(board1, "SEE"));
        assertFalse(exist2(board1, "ABCB"));
        /**
         * Example 2:
         * board =
         * [
         *   ['a', 'a']
         * ]
         *
         * Given word = "aaa", return false.
         */
        char[][] board2 = new char[][]{{'a', 'a'}};
        assertFalse(exist2(board2, "aaa"));
    }
}
