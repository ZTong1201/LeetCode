import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class wordSearch {

    /**
     * Given a 2D board and a word, find if the word exists in the grid.
     * <p>
     * The word can be constructed from letters of sequentially adjacent cell,
     * where "adjacent" cells are those horizontally or vertically neighboring.
     * The same letter cell may not be used more than once.
     * <p>
     * Constraints:
     * <p>
     * m == board.length
     * n = board[i].length
     * 1 <= m, n <= 6
     * 1 <= word.length <= 15
     * board and word consists of only lowercase and uppercase English letters.
     * <p>
     * Approach 1: Depth-First Search (DFS)
     * This is a typical DFS problem. For each entry in the grid, we search as deep as possible to search for the result.
     * We return false for the given starting cell, if at any time:
     * 1. The search point is out of the grid
     * 2. We reached a cell that has been visited before (since the same cell may not be used more than once)
     * 3. The letter in the cell is different with the word at the desired index
     * If we meet all the requirements when all letters have been found, we return true.
     * <p>
     * The key part for this algorithm is, when we done search for a given cell, we need reset the cell to be unvisited (backtrack).
     * <p>
     * Time: O(m*n*4^k) where m is the number of row, n is the number of length, k is the length of word. In the worst case, we need to do
     * dfs for every cell in the grid and the search requires at most 4^k iterations. When k is small, the runtime is just O(m*n)
     * Space: O(m*n), we need a 2-d boolean array to record whether we visit a cell or not. The call stack will be O(4^k) space, if k is
     * small, the O(m*n) will dominate the complexity
     */
    public boolean exist1(char[][] board, String word) {
        int m = board.length, n = board[0].length;
        boolean[][] visited = new boolean[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (dfs(board, i, j, word, 0, visited)) return true;
            }
        }
        return false;
    }

    private boolean dfs(char[][] board, int i, int j, String word, int index, boolean[][] visited) {
        // base case - when all characters were found in the board, return true
        if (index == word.length()) return true;
        // other base cases - return false if
        // 1. search is outside the board
        // 2. reach a visited cell
        // 3. the current character is not what to be searched
        if (i < 0 || j < 0 || i >= board.length || j >= board[0].length || visited[i][j] || word.charAt(index) != board[i][j])
            return false;
        // record as visited
        visited[i][j] = true;
        // search 4 neighbors
        boolean top = dfs(board, i + 1, j, word, index + 1, visited);
        boolean bottom = dfs(board, i - 1, j, word, index + 1, visited);
        boolean left = dfs(board, i, j - 1, word, index + 1, visited);
        boolean right = dfs(board, i, j + 1, word, index + 1, visited);
        // MOST IMPORTANT! We need backtrack after searching for the given cell
        visited[i][j] = false;
        // collect the result from all 4 neighbors
        return top || bottom || left || right;
    }

    /**
     * Approach 2: DFS without visited array
     */
    public boolean exist2(char[][] board, String word) {
        int m = board.length, n = board[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (dfs2(board, i, j, word, 0)) return true;
            }
        }
        return false;
    }

    private boolean dfs2(char[][] board, int i, int j, String word, int index) {
        if (index == word.length()) return true;
        if (i < 0 || j < 0 || i >= board.length || j >= board[0].length || board[i][j] != word.charAt(index))
            return false;
        char temp = board[i][j];
        // inplace mark the cell as visited
        board[i][j] = '*';

        boolean top = dfs2(board, i + 1, j, word, index + 1);
        boolean bottom = dfs2(board, i - 1, j, word, index + 1);
        boolean left = dfs2(board, i, j - 1, word, index + 1);
        boolean right = dfs2(board, i, j + 1, word, index + 1);

        // revert the cell back - backtrack
        board[i][j] = temp;
        return top || bottom || left || right;
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
