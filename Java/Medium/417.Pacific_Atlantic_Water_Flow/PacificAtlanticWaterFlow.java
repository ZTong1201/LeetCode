import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static org.junit.Assert.assertEquals;

public class PacificAtlanticWaterFlow {

    /**
     * There is an m x n rectangular island that borders both the Pacific Ocean and Atlantic Ocean. The Pacific Ocean touches
     * the island's left and top edges, and the Atlantic Ocean touches the island's right and bottom edges.
     * <p>
     * The island is partitioned into a grid of square cells. You are given an m x n integer matrix heights where
     * heights[r][c] represents the height above sea level of the cell at coordinate (r, c).
     * <p>
     * The island receives a lot of rain, and the rain water can flow to neighboring cells directly north, south, east, and
     * west if the neighboring cell's height is less than or equal to the current cell's height. Water can flow from any
     * cell adjacent to an ocean into the ocean.
     * <p>
     * Return a 2D list of grid coordinates result where result[i] = [ri, ci] denotes that rain water can flow from cell
     * (ri, ci) to both the Pacific and Atlantic oceans.
     * <p>
     * Constraints:
     * <p>
     * m == heights.length
     * n == heights[r].length
     * 1 <= m, n <= 200
     * 0 <= heights[r][c] <= 10^5
     * <p>
     * Approach 1: DFS
     * Basically, we want to start from the edge cells which are connected to the Pacific (or the Atlantic) and flow back
     * until we can't. (In order to flow back, we need the value of adjacent cell >= current cell such that the water
     * is flowed from a higher height to a lower one). We need two 2-D arrays to keep track of whether the cell is connected
     * to the Pacific and the Atlantic. Finally, we go through all the cells, and put the intersection into the list.
     * <p>
     * Time: O(mn) we at most traverse the entire grid when flow back. Then we go through the grid to find the intersection.
     * Space: O(mn) need two 2-D arrays which have the same size of the grid
     */
    private int[][] next;

    public List<List<Integer>> pacificAtlanticDFS(int[][] heights) {
        next = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        int rows = heights.length, cols = heights[0].length;
        // need two 2-D arrays
        boolean[][] isConnectedToPacific = new boolean[rows][cols], isConnectedToAtlantic = new boolean[rows][cols];

        // first, flow water from both ends of the columns into the grid
        for (int i = 0; i < rows; i++) {
            // flow from the leftmost column - link cells which connect to Pacific
            flowBack(heights, i, 0, isConnectedToPacific);
            // flow from the rightmost column - link cells which connect to Atlantic
            flowBack(heights, i, cols - 1, isConnectedToAtlantic);
        }

        // similarly, flow water from both ends of the rows
        for (int i = 0; i < cols; i++) {
            // flow from the topmost row - link cells which connect to Pacific
            flowBack(heights, 0, i, isConnectedToPacific);
            // flow from the bottom-most column - link cells which connect to Atlantic
            flowBack(heights, rows - 1, i, isConnectedToAtlantic);
        }

        List<List<Integer>> res = new ArrayList<>();
        // loop through the grid and find intersections
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (isConnectedToPacific[i][j] && isConnectedToAtlantic[i][j]) {
                    res.add(List.of(i, j));
                }
            }
        }
        return res;
    }

    private void flowBack(int[][] heights, int row, int col, boolean[][] isConnected) {
        // mark the current cell as connected
        isConnected[row][col] = true;
        // search 4 neighbors
        for (int[] step : next) {
            int nextRow = row + step[0], nextCol = col + step[1];
            // handle edge cases
            // don't proceed if out of the grid
            if (nextRow < 0 || nextCol < 0 || nextRow >= heights.length || nextCol >= heights[0].length) continue;
            // also don't need to revisit connected cells
            if (isConnected[nextRow][nextCol]) continue;
            // when flow back, we need to find a higher (or equal) height, stop searching if the adjacent height is smaller
            if (heights[nextRow][nextCol] < heights[row][col]) continue;
            // otherwise, keep searching and connecting neighbor cells
            flowBack(heights, nextRow, nextCol, isConnected);
        }
    }

    /**
     * Approach 2: BFS
     * We can also use BFS to flow water back. Instead of counting on the call stack, we can explicitly add boundary cells
     * into two queues as starting points. Then move step by step to connect all cells.
     * <p>
     * Time: O(mn) the time complexity remain unchanged, we need traverse the entire grid 3 times (at most): one for flowing
     * cells which are connected to the Pacific, one for flowing cells which are connected to the Atlantic, and final
     * traversal to find valid cells
     * Space: O(mn)
     */
    public List<List<Integer>> pacificAtlanticBFS(int[][] heights) {
        int rows = heights.length, cols = heights[0].length;
        boolean[][] isConnectedToPacific = new boolean[rows][cols], isConnectedToAtlantic = new boolean[rows][cols];
        // use queue to keep track of valid cells
        Queue<int[]> pacificQueue = new LinkedList<>();
        Queue<int[]> atlanticQueue = new LinkedList<>();

        // add boundary cells into the queue
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if ((i == 0) || (j == 0)) pacificQueue.add(new int[]{i, j});
                if ((i == rows - 1) || (j == cols - 1)) atlanticQueue.add(new int[]{i, j});
            }
        }

        // execute BFS to find connected cells
        connectCells(heights, pacificQueue, isConnectedToPacific);
        connectCells(heights, atlanticQueue, isConnectedToAtlantic);

        // final traversal to add all valid cells
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (isConnectedToPacific[i][j] && isConnectedToAtlantic[i][j]) {
                    res.add(List.of(i, j));
                }
            }
        }
        return res;
    }

    private void connectCells(int[][] heights, Queue<int[]> queue, boolean[][] isConnected) {
        int[][] next = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int currRow = curr[0], currCol = curr[1];
            // every cell pushed into the queue is guaranteed to be connected with the ocean
            // mark it as true
            isConnected[currRow][currCol] = true;

            // search 4 neighbors and add valid neighbors into the queue
            for (int[] step : next) {
                int nextRow = currRow + step[0], nextCol = currCol + step[1];
                // handle edge cases
                if (nextRow < 0 || nextCol < 0 || nextRow >= heights.length || nextCol >= heights[0].length) continue;
                if (isConnected[nextRow][nextCol]) continue;
                if (heights[nextRow][nextCol] < heights[currRow][currCol]) continue;
                // otherwise, add the adjacent cells
                queue.add(new int[]{nextRow, nextCol});
            }
        }
    }

    @Test
    public void pacificAtlanticTest() {
        /**
         * Example 1:
         * Input: heights = [[1,2,2,3,5],[3,2,3,4,4],[2,4,5,3,1],[6,7,1,4,5],[5,1,1,2,4]]
         * Output: [[0,4],[1,3],[1,4],[2,2],[3,0],[3,1],[4,0]]
         */
        List<List<Integer>> expected1 = List.of(List.of(0, 4), List.of(1, 3), List.of(1, 4), List.of(2, 2),
                List.of(3, 0), List.of(3, 1), List.of(4, 0));
        List<List<Integer>> actualDFS1 = pacificAtlanticDFS(new int[][]{{1, 2, 2, 3, 5}, {3, 2, 3, 4, 4}, {2, 4, 5, 3, 1},
                {6, 7, 1, 4, 5}, {5, 1, 1, 2, 4}});
        List<List<Integer>> actualBFS1 = pacificAtlanticBFS(new int[][]{{1, 2, 2, 3, 5}, {3, 2, 3, 4, 4}, {2, 4, 5, 3, 1},
                {6, 7, 1, 4, 5}, {5, 1, 1, 2, 4}});
        assertEquals(expected1.size(), actualDFS1.size());
        assertEquals(expected1.size(), actualBFS1.size());
        for (int i = 0; i < expected1.size(); i++) {
            assertEquals(expected1.get(i), actualDFS1.get(i));
        }
        for (int i = 0; i < expected1.size(); i++) {
            assertEquals(expected1.get(i), actualBFS1.get(i));
        }
        /**
         * Example 2:
         * Input: heights = [[2,1],[1,2]]
         * Output: [[0,0],[0,1],[1,0],[1,1]]
         */
        List<List<Integer>> expected2 = List.of(List.of(0, 0), List.of(0, 1), List.of(1, 0), List.of(1, 1));
        List<List<Integer>> actualDFS2 = pacificAtlanticDFS(new int[][]{{2, 1}, {1, 2}});
        List<List<Integer>> actualBFS2 = pacificAtlanticBFS(new int[][]{{2, 1}, {1, 2}});
        assertEquals(expected2.size(), actualDFS2.size());
        assertEquals(expected2.size(), actualBFS2.size());
        for (int i = 0; i < expected2.size(); i++) {
            assertEquals(expected2.get(i), actualDFS2.get(i));
        }
        for (int i = 0; i < expected2.size(); i++) {
            assertEquals(expected2.get(i), actualBFS2.get(i));
        }
    }
}
