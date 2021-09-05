import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;

import static org.junit.Assert.assertEquals;

public class RottingOranges {

    /**
     * You are given an m x n grid where each cell can have one of three values:
     * <p>
     * 0 representing an empty cell,
     * 1 representing a fresh orange, or
     * 2 representing a rotten orange.
     * Every minute, any fresh orange that is 4-directionally adjacent to a rotten orange becomes rotten.
     * <p>
     * Return the minimum number of minutes that must elapse until no cell has a fresh orange. If this is impossible, return -1.
     * <p>
     * Constraints:
     * <p>
     * m == grid.length
     * n == grid[i].length
     * 1 <= m, n <= 10
     * grid[i][j] is 0, 1, or 2.
     * <p>
     * Approach: BFS
     * To find the shortest path in the graph - always consider BFS as an option. We can first iterate the grid to get the
     * total number of oranges and the number of rotten oranges so far + adding all rotten oranges into the queue.
     * The first corner case will be: if there is no orange in the grid at all, return 0 since there is no fresh orange at
     * current state.
     * <p>
     * Then, we start from each of the rotten oranges, and if there is a fresh orange in its neighbor - the orange will get
     * rotten after the current round. And we add these "new" rotten oranges into the queue to further rot other fresh
     * oranges (if any). Since only fresh oranges will be considered, we always mark oranges as rotten on the fly to avoid
     * duplicate visit.
     * <p>
     * Finally, if total # oranges = total # rotten oranges, which means there is no fresh orange in the grid, return the
     * minimum minute. Otherwise, return -1.
     * <p>
     * Time: O(mn) for each iteration, we will at most visit each cell once
     * Space: O(mn) the initial state needs all cells in which there is a rotten orange, worst case scenario, the grid will be
     * full of rotten oranges
     */
    public int orangesRotting(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        Queue<Integer> queue = new LinkedList<>();
        int totalOranges = 0, numOfRottenOranges = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1 || grid[i][j] == 2) {
                    // keep track of all oranges
                    totalOranges++;
                    // put rotten oranges into the queue as an initial state
                    if (grid[i][j] == 2) {
                        numOfRottenOranges++;
                        queue.add(i * n + j);
                    }
                }
            }
        }
        // edge case - no orange at all, keep the initial state as is
        if (totalOranges == 0) return 0;

        // starting from -1 because we start from rotten oranges
        // they don't require any operations to become rotten
        int minMinutes = -1;
        int[][] next = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        while (!queue.isEmpty()) {
            // get the number of oranges at current level to execute BFS
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int currId = queue.poll();
                int row = currId / n, col = currId % n;

                // rot its 4 neighbors if there is a fresh orange
                for (int j = 0; j < 4; j++) {
                    int nextRow = row + next[j][0];
                    int nextCol = col + next[j][1];

                    if (nextRow >= 0 && nextCol >= 0 && nextRow < m && nextCol < n && grid[nextRow][nextCol] == 1) {
                        // make the fresh orange rotten
                        grid[nextRow][nextCol] = 2;
                        // increment the number of rotten oranges
                        numOfRottenOranges++;
                        queue.add(nextRow * n + nextCol);
                    }
                }
            }
            // increment minutes after completing the current layer and move to the next state
            minMinutes++;
        }
        // only return the min minute when all oranges become rotten
        return numOfRottenOranges == totalOranges ? minMinutes : -1;
    }

    @Test
    public void orangesRottingTest() {
        /**
         * Example 1:
         * Input: grid = [[2,1,1],[1,1,0],[0,1,1]]
         * Output: 4
         */
        assertEquals(4, orangesRotting(new int[][]{{2, 1, 1}, {1, 1, 0}, {0, 1, 1}}));
        /**
         * Example 2:
         * Input: grid = [[2,1,1],[0,1,1],[1,0,1]]
         * Output: -1
         * Explanation: The orange in the bottom left corner (row 2, column 0) is never rotten,
         * because rotting only happens 4-directionally.
         */
        assertEquals(-1, orangesRotting(new int[][]{{2, 1, 1}, {1, 1, 0}, {1, 0, 1}}));
        /**
         * Example 3:
         * Input: grid = [[0,2]]
         * Output: 0
         * Explanation: Since there are already no fresh oranges at minute 0, the answer is just 0.
         */
        assertEquals(0, orangesRotting(new int[][]{{0, 2}}));
    }
}
