import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;

import static org.junit.Assert.assertArrayEquals;

public class MapOfHighestPeak {

    /**
     * You are given an integer matrix isWater of size m x n that represents a map of land and water cells.
     * <p>
     * If isWater[i][j] == 0, cell (i, j) is a land cell.
     * If isWater[i][j] == 1, cell (i, j) is a water cell.
     * You must assign each cell a height in a way that follows these rules:
     * <p>
     * The height of each cell must be non-negative.
     * If the cell is a water cell, its height must be 0.
     * Any two adjacent cells must have an absolute height difference of at most 1. A cell is adjacent to another cell if the
     * former is directly north, east, south, or west of the latter (i.e., their sides are touching).
     * Find an assignment of heights such that the maximum height in the matrix is maximized.
     * <p>
     * Return an integer matrix height of size m x n where height[i][j] is cell (i, j)'s height. If there are multiple
     * solutions, return any of them.
     * <p>
     * Constraints:
     * <p>
     * m == isWater.length
     * n == isWater[i].length
     * 1 <= m, n <= 1000
     * isWater[i][j] is 0 or 1.
     * There is at least one water cell.
     * <p>
     * Approach: BFS
     * Even though there might have been lots of different options to assign the heights, what makes the most sense would be
     * starting from all water cells and increment height by 1 in all the neighbors, and keep doing this until we visit all
     * cells in the grid. We basically just need to go over the entire grid and enqueue all water cells and execute BFS until
     * all nodes are visited.
     * <p>
     * Time: O(mn) we will visit the entire grid twice
     * Space: O(mn) need to a queue to store cells for next visit
     */
    public int[][] highestPeak(int[][] isWater) {
        int rows = isWater.length, cols = isWater[0].length;
        boolean[][] visited = new boolean[rows][cols];
        Queue<Integer> queue = new LinkedList<>();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // add all water cells into the queue
                if (isWater[i][j] == 1) {
                    queue.add(i * cols + j);
                }
            }
        }

        int level = 0;
        int[][] next = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        while (!queue.isEmpty()) {
            int size = queue.size();

            for (int i = 0; i < size; i++) {
                int currId = queue.poll();
                int currRow = currId / cols, currCol = currId % cols;

                if (!visited[currRow][currCol]) {
                    visited[currRow][currCol] = true;
                    // update the height on the original array
                    isWater[currRow][currCol] = level;

                    // check all neighbors and see whether we can keep searching
                    for (int[] step : next) {
                        int nextRow = currRow + step[0], nextCol = currCol + step[1];

                        if (nextRow >= 0 && nextCol >= 0 && nextRow < rows && nextCol < cols && !visited[nextRow][nextCol]) {
                            queue.add(nextRow * cols + nextCol);
                        }
                    }
                }
            }
            level++;
        }
        return isWater;
    }

    @Test
    public void highestPeakTest() {
        /**
         * Example 1:
         * Input: isWater = [[0,1],[0,0]]
         * Output: [[1,0],[2,1]]
         * Explanation: The image shows the assigned heights of each cell.
         * The blue cell is the water cell, and the green cells are the land cells.
         */
        int[][] expected1 = new int[][]{{1, 0}, {2, 1}};
        int[][] actual1 = highestPeak(new int[][]{{0, 1}, {0, 0}});
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: isWater = [[0,0,1],[1,0,0],[0,0,0]]
         * Output: [[1,1,0],[0,1,1],[1,2,2]]
         * Explanation: A height of 2 is the maximum possible height of any assignment.
         * Any height assignment that has a maximum height of 2 while still meeting the rules will also be accepted.
         */
        int[][] expected2 = new int[][]{{1, 1, 0}, {0, 1, 1}, {1, 2, 2}};
        int[][] actual2 = highestPeak(new int[][]{{0, 0, 1}, {1, 0, 0}, {0, 0, 0}});
        assertArrayEquals(expected2, actual2);
    }
}
