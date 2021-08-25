import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;

import static org.junit.Assert.assertEquals;

public class ShortestPathWithObstaclesElimination {

    /**
     * You are given an m x n integer matrix grid where each cell is either 0 (empty) or 1 (obstacle). You can move up,
     * down, left, or right from and to an empty cell in one step.
     * <p>
     * Return the minimum number of steps to walk from the upper left corner (0, 0) to the lower right corner (m - 1, n - 1)
     * given that you can eliminate at most k obstacles. If it is not possible to find such walk return -1.
     * <p>
     * Constraints:
     * <p>
     * m == grid.length
     * n == grid[i].length
     * 1 <= m, n <= 40
     * 1 <= k <= m * n
     * grid[i][j] == 0 or 1
     * grid[0][0] == grid[m - 1][n - 1] == 0
     * <p>
     * Approach: BFS
     * In order to find the shortest path, BFS should always be considered. The key part of this problem is to BFS on a triplet
     * of (x, y, k) where x and y are coordinates in the grid and k is the number of removals left.
     * 1. If grid[x][y] is 0, we simply add it to the queue for further search since we are not eliminating any obstacles, k
     * should remain unchanged.
     * 2. If grid[x][y] is 1, which is an obstacle, we need to check whether we still have any quotes left for obstacle removal,
     * if yes, we can proceed searching on that position and decrement k. If we have removed k obstacles, then we won't be able
     * to reach that position.
     * To avoid revisiting, we need to make sure to mark each cell as visited after visiting. However, each cell has more than
     * one state - whether we have removed i (0 <= i <= k) obstacles before reaching the current cell. Therefore, we could use
     * a 3-D array where visited[x][y][k] means whether cell grid[x][y] has been visited with k elements to be removed.
     * <p>
     * Time: O(M * N * K) we need to visit all the cells at most K times.
     * Space: O(M * N * K)
     */
    public int shortestPath(int[][] grid, int k) {
        int m = grid.length, n = grid[0].length;
        Queue<Triplet> queue = new LinkedList<>();
        // add starting point to the queue
        queue.add(new Triplet(0, 0, k));
        // a 3-D array to mark visited at cell (i, j) for k states
        boolean[][][] visited = new boolean[m][n][k + 1];

        int level = 0;
        int[][] next = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        while (!queue.isEmpty()) {
            int size = queue.size();
            // need to traverse the current level for BFS
            for (int i = 0; i < size; i++) {
                Triplet curr = queue.poll();
                // avoid revisiting
                if (!visited[curr.x][curr.y][curr.k]) {
                    visited[curr.x][curr.y][curr.k] = true;
                    // since BFS is used, the shortest path is guaranteed if we reach the bottom right cell
                    if (curr.x == m - 1 && curr.y == n - 1) return level;
                    // BFS its four neighbor
                    for (int j = 0; j < 4; j++) {
                        int row = curr.x + next[j][0];
                        int col = curr.y + next[j][1];

                        // make sure the search is still in the grid
                        if (row >= 0 && col >= 0 && row < m && col < n) {
                            // if the neighbor is 0, it's always reachable
                            if (grid[row][col] == 0) {
                                queue.add(new Triplet(row, col, k));
                            } else if (grid[row][col] == 1 && curr.k > 0) {
                                // otherwise, we need to eliminate one obstacle to reach the neighbor cell
                                queue.add(new Triplet(row, col, k - 1));
                            }
                        }
                    }
                }
            }
            // the current level traversal is done, increment the step
            level++;
        }
        // the bottom-right cell is not reachable, return -1
        return -1;
    }

    @Test
    public void shortestPathTest() {
        /**
         * Example 1:
         * Input:
         * grid =
         * [[0,0,0],
         *  [1,1,0],
         *  [0,0,0],
         *  [0,1,1],
         *  [0,0,0]],
         * k = 1
         * Output: 6
         * Explanation:
         * The shortest path without eliminating any obstacle is 10.
         * The shortest path with one obstacle elimination at position (3,2) is 6. Such path is
         * (0,0) -> (0,1) -> (0,2) -> (1,2) -> (2,2) -> (3,2) -> (4,2).
         */
        assertEquals(6, shortestPath(new int[][]{
                {0, 0, 0}, {1, 1, 0}, {0, 0, 0},
                {0, 1, 1}, {0, 0, 0}}, 1));
        /**
         * Example 2:
         * Input:
         * grid =
         * [[0,1,1],
         *  [1,1,1],
         *  [1,0,0]],
         * k = 1
         * Output: -1
         * Explanation:
         * We need to eliminate at least two obstacles to find such a walk.
         */
        assertEquals(-1, shortestPath(new int[][]{{0, 1, 1}, {1, 1, 1}, {1, 0, 0}}, 1));
        /**
         * Example 3:
         * Input:
         * grid = [[0]],
         * k = 1,
         * Output: 1
         */
        assertEquals(0, shortestPath(new int[][]{{0}}, 1));
    }

    private static class Triplet {
        int x;
        int y;
        int k;

        public Triplet(int x, int y, int k) {
            this.x = x;
            this.y = y;
            this.k = k;
        }
    }
}
