import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;

import static org.junit.Assert.assertEquals;

public class ShortestPathToGetFood {

    /**
     * You are starving and you want to eat food as quickly as possible. You want to find the shortest path to arrive at
     * any food cell.
     * <p>
     * You are given an m x n character matrix, grid, of these different types of cells:
     * <p>
     * '*' is your location. There is exactly one '*' cell.
     * '#' is a food cell. There may be multiple food cells.
     * 'O' is free space, and you can travel through these cells.
     * 'X' is an obstacle, and you cannot travel through these cells.
     * You can travel to any adjacent cell north, east, south, or west of your current location if there is not an obstacle.
     * <p>
     * Return the length of the shortest path for you to reach any food cell. If there is no path for you to reach food,
     * return -1.
     * <p>
     * Constraints:
     * <p>
     * m == grid.length
     * n == grid[i].length
     * 1 <= m, n <= 200
     * grid[row][col] is '*', 'X', 'O', or '#'.
     * The grid contains exactly one '*'.
     * <p>
     * Approach: BFS
     * Loop through the grid to find the position of '*' as the starting point. Then run a regular BFS to find the shortest
     * path between '*' and the nearest food. If the queue is empty before reaching a food, return -1 since it's not possible
     * to find a path between '*' and a food cell.
     * <p>
     * Time: O(mn)
     * Space: O(mn)
     */
    public int getFood(char[][] grid) {
        int m = grid.length, n = grid[0].length;
        Queue<Integer> queue = new LinkedList<>();
        // add starting point into the queue
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == '*') {
                    queue.add(i * n + j);
                }
            }
        }

        int[][] next = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        boolean[][] visited = new boolean[m][n];
        int minDistance = 0;

        while (!queue.isEmpty()) {
            int size = queue.size();

            for (int i = 0; i < size; i++) {
                int id = queue.poll();
                int currRow = id / n, currCol = id % n;

                // if reaching a food cell, return the minimum distance so far
                if (grid[currRow][currCol] == '#') return minDistance;

                if (!visited[currRow][currCol]) {
                    visited[currRow][currCol] = true;

                    for (int j = 0; j < 4; j++) {
                        int nextRow = currRow + next[j][0];
                        int nextCol = currCol + next[j][1];

                        if (nextRow >= 0 && nextCol >= 0 && nextRow < m && nextCol < n &&
                                !visited[nextRow][nextCol] && grid[nextRow][nextCol] != 'X') {
                            queue.add(nextRow * n + nextCol);
                        }
                    }
                }
            }
            // increment the distance after finishing up the current layer
            minDistance++;
        }
        return -1;
    }

    @Test
    public void getFoodTest() {
        /**
         * Example 1:
         * Input: grid = [
         * ["X","X","X","X","X","X"],
         * ["X","*","O","O","O","X"],
         * ["X","O","O","#","O","X"],
         * ["X","X","X","X","X","X"]]
         * Output: 3
         * Explanation: It takes 3 steps to reach the food.
         */
        assertEquals(3, getFood(new char[][]{
                {'X', 'X', 'X', 'X', 'X', 'X'},
                {'X', '*', 'O', 'O', 'O', 'X'},
                {'X', 'O', 'O', '#', 'O', 'X'},
                {'X', 'X', 'X', 'X', 'X', 'X'}}));
        /**
         * Example 2:
         * Input: grid = [
         * ["X","X","X","X","X"],
         * ["X","*","X","O","X"],
         * ["X","O","X","#","X"],
         * ["X","X","X","X","X"]]
         * Output: -1
         * Explanation: It is not possible to reach the food.
         */
        assertEquals(-1, getFood(new char[][]{
                {'X', 'X', 'X', 'X', 'X'},
                {'X', '*', 'X', 'O', 'X'},
                {'X', 'O', 'X', '#', 'X'},
                {'X', 'X', 'X', 'X', 'X'}}));
        /**
         * Example 3:
         * Input: grid = [
         * ["X","X","X","X","X","X","X","X"],
         * ["X","*","O","X","O","#","O","X"],
         * ["X","O","O","X","O","O","X","X"],
         * ["X","O","O","O","O","#","O","X"],
         * ["X","X","X","X","X","X","X","X"]]
         * Output: 6
         * Explanation: There can be multiple food cells. It only takes 6 steps to reach the bottom food.
         */
        assertEquals(6, getFood(new char[][]{
                {'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'},
                {'X', '*', 'O', 'X', 'O', '#', 'O', 'X'},
                {'X', 'O', 'O', 'X', 'O', 'O', 'X', 'X'},
                {'X', 'O', 'O', 'O', 'O', '#', 'O', 'X'},
                {'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'}
        }));
        /**
         * Example 4:
         * Input: grid = [["O","*"],["#","O"]]
         * Output: 2
         */
        assertEquals(2, getFood(new char[][]{{'O', '*'}, {'#', 'O'}}));
        /**
         * Example 5:
         * Input: grid = [["X","*"],["#","X"]]
         * Output: -1
         */
        assertEquals(-1, getFood(new char[][]{{'X', '*'}, {'#', 'X'}}));
    }
}
