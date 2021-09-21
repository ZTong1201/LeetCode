import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;

import static org.junit.Assert.assertEquals;

public class ShortestPathInBinaryMatrix {

    /**
     * Given an n x n binary matrix grid, return the length of the shortest clear path in the matrix. If there is no clear
     * path, return -1.
     * <p>
     * A clear path in a binary matrix is a path from the top-left cell (i.e., (0, 0)) to the bottom-right cell (i.e.,
     * (n - 1, n - 1)) such that:
     * <p>
     * All the visited cells of the path are 0.
     * All the adjacent cells of the path are 8-directionally connected (i.e., they are different and they share an edge or a corner).
     * The length of a clear path is the number of visited cells of this path.
     * <p>
     * Constraints:
     * <p>
     * n == grid.length
     * n == grid[i].length
     * 1 <= n <= 100
     * grid[i][j] is 0 or 1
     * <p>
     * Approach: BFS
     * It's a basic BFS problem except each cell has 8 neighbors instead of 4. We use a queue to traverse the grid layer by
     * layer and only add unvisited accessible neighbors into the queue (i.e. grid[i][j] = 0). If any time the bottom-right
     * cell is reached - return the minimum distance so far. Since the definition of length of the path is the number of
     * visited nodes of the path. We could initiate the distance as 1, which is different with common BFS template.
     * <p>
     * Time: O(n^2) in the worst case we need to traverse the entire grid
     * Space: O(n^2) need a 2-D array to keep track of visited cell
     */
    public int shortestPathBinaryMatrix(int[][] grid) {
        int n = grid.length;
        Queue<Integer> queue = new LinkedList<>();
        queue.add(0);
        boolean[][] visited = new boolean[n][n];
        int minDistance = 1;
        // each cell has 8 neighbors now
        int[][] next = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

        while (!queue.isEmpty()) {
            int size = queue.size();

            for (int i = 0; i < size; i++) {
                int id = queue.poll();
                int currRow = id / n, currCol = id % n;

                // avoid duplicate visit and avoid visiting non-accessible cells
                if (!visited[currRow][currCol] && grid[currRow][currCol] == 0) {
                    visited[currRow][currCol] = true;

                    // if the bottom-right cell is reached, return the min length
                    if (currRow == n - 1 && currCol == n - 1) return minDistance;

                    // search its 8 neighbors
                    for (int j = 0; j < 8; j++) {
                        int nextRow = currRow + next[j][0];
                        int nextCol = currCol + next[j][1];

                        if (nextRow >= 0 && nextCol >= 0 && nextRow < n && nextCol < n) {
                            queue.add(nextRow * n + nextCol);
                        }
                    }
                }
            }
            // increment the length of path after completing a layer
            minDistance++;
        }
        return -1;
    }

    @Test
    public void shortestPathBinaryMatrixTest() {
        /**
         * Example 1:
         * Input: grid = [[0,1],[1,0]]
         * Output: 2
         */
        assertEquals(2, shortestPathBinaryMatrix(new int[][]{{0, 1}, {1, 0}}));
        /**
         * Example 2:
         * Input: grid = [[0,0,0],[1,1,0],[1,1,0]]
         * Output: 4
         */
        assertEquals(4, shortestPathBinaryMatrix(new int[][]{{0, 0, 0}, {1, 1, 0}, {1, 1, 0}}));
        /**
         * Example 3:
         * Input: grid = [[1,0,0],[1,1,0],[1,1,0]]
         * Output: -1
         */
        assertEquals(-1, shortestPathBinaryMatrix(new int[][]{{1, 0, 0}, {1, 1, 0}, {1, 1, 0}}));
    }
}
