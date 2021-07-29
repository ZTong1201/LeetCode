import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;

import static org.junit.Assert.assertEquals;

public class MaxDistance {

    /**
     * Given an n x n grid containing only values 0 and 1, where 0 represents water and 1 represents land, find a water
     * cell such that its distance to the nearest land cell is maximized, and return the distance. If no land or water
     * exists in the grid, return -1.
     * <p>
     * The distance used in this problem is the Manhattan distance: the distance between two cells (x0, y0) and (x1, y1)
     * is |x0 - x1| + |y0 - y1|.
     * <p>
     * Constraints:
     * <p>
     * n == grid.length
     * n == grid[i].length
     * 1 <= n <= 100
     * grid[i][j] is 0 or 1
     * <p>
     * Approach 1: BFS from all lands
     * Any minimum distance problems could be solved BFS. The naive BFS approach would be for each water cell '0', executing
     * BFS to find it's nearest land cell '1'. Keep doing BFS for all water cells and update the maximum value. This approach
     * can solve this question without a problem, however, the BFS at each water cell can take up to O(N^2) (search the entire
     * grid) to find the minimum value and in the worst case (the grid is full of water cells, N^2), the total runtime wil take
     * up to O(N^4).
     * An alternative would be start BFS at all land cells at the same time (putting them into a queue), and find the nearest
     * water cell for each land cell. When the land cell find the nearest water cell, it will be removed from the queue
     * permanently. Hence the last land cell removed from the queue will give the max distance. To avoid revisiting any cells,
     * the easiest approach is to create a 2-D boolean array to record whether the cell has been visited. However, since the
     * cell value can only be 0 or 1. We can assign grid[i][j] to 2 to mark it as visited.
     * <p>
     * Time: O(N^2) each cell will at most be visited once during BFS
     * Space: O(N^2) we need a queue to keep track of all land cells, in the worst case the grid contains all lands
     */
    public int maxDistanceBFS(int[][] grid) {
        int n = grid.length;
        Queue<Integer> queue = new LinkedList<>();
        // add all land cells into the queue
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    queue.add(i * n + j);
                }
            }
        }

        // define a 2-D array to achieve visiting adjacent cells
        int[][] next = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        int res = -1;
        // keep track of the distance
        int dist = 0;
        while (!queue.isEmpty()) {
            // start BFS at all the land cells
            // that's why we need the size to keep track of current breadth level
            int size = queue.size();
            while (size > 0) {
                int id = queue.poll();
                int row = id / n;
                int col = id % n;
                // if the cell has been visited (value is 2 instead of 0 or 1)
                // we found the nearest water cell for that land already
                if (grid[row][col] == 2) {
                    res = Math.max(res, dist);
                }
                // visit adjacent cells to proceed BFS
                for (int[] step : next) {
                    int nr = row + step[0];
                    int nc = col + step[1];
                    // only visit valid neighbor (inside the grid and never be visited before != 2)
                    // or it is actually a water cell
                    if (nr >= 0 && nc >= 0 && nr < n && nc < n && grid[nr][nc] == 0) {
                        // mark it as visited
                        grid[nr][nc] = 2;
                        // add it to the queue for deeper BFS
                        queue.add(nr * n + nc);
                    }
                }
                size--;
            }
            // once one level of BFS is done, every land cell has moved one step
            // update the distance accordingly
            dist++;
        }
        return res;
    }

    /**
     * Approach 2: Dynamic Programming
     * For a given water cell, it's only allowed to visit its 4-directional neighbors. Therefore, we can use a 2-pass
     * DP to update the minimum distance. The first run is from top left -> bottom right, which indicates the water cell
     * can go leftward or upward to find the nearest land. The second run is from bottom right -> top left, this time
     * the water cell can go rightward or downward. Now dp[i][j] represents the minimum distance to the nearest land cell
     * (in the correct section) + 1. (+ 1 because we keep track of the number of nodes on the minimum path not the actual
     * distance). Since the problem statement says the length of grid can never exceeds 100, which means the maximum distance
     * is strictly smaller than 201. We can use it as a baseline value and keep taking minimum (if any)
     * Note that there can be no water or land cells in the grid, and 201 will be finally returned in that scenario. We
     * need to manually handle this edge case and return -1 instead;
     * <p>
     * Time: O(N^2)
     * Space: O(1)
     */
    public int maxDistanceDP(int[][] grid) {
        int n = grid.length, res = 0;
        // we can do DP in-place, no extra space is needed
        // first DP pass - compute minimum distance to the nearest land cell from the left and the top
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // skip the land cell
                if (grid[i][j] == 1) continue;
                // assign a default value in case there is no land cell
                grid[i][j] = 201;
                // nearest land comes from the left
                if (i > 0) grid[i][j] = Math.min(grid[i][j], grid[i - 1][j] + 1);
                // or nearest land comes from the top
                if (j > 0) grid[i][j] = Math.min(grid[i][j], grid[i][j - 1] + 1);
            }
        }

        // second DP pass - take care of the right and the bottom
        for (int i = n - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
                // still skip the land cell
                if (grid[i][j] == 1) continue;
                // nearest land comes from the right
                if (j < n - 1) grid[i][j] = Math.min(grid[i][j], grid[i][j + 1] + 1);
                // or nearest land comes from the bottom
                if (i < n - 1) grid[i][j] = Math.min(grid[i][j], grid[i + 1][j] + 1);
                // update the maximum distance
                res = Math.max(res, grid[i][j]);
            }
        }
        // if res is 201, which means there is no land cell return -1
        // otherwise return the max distance
        return res == 201 ? -1 : res - 1;
    }

    @Test
    public void maxDistanceBFSTest() {
        /**
         * Example 1:
         * Input: grid = [[1,0,1],[0,0,0],[1,0,1]]
         * Output: 2
         * Explanation: The cell (1, 1) is as far as possible from all the land with distance 2.
         */
        int[][] grid1 = new int[][]{{1, 0, 1}, {0, 0, 0}, {1, 0, 1}};
        assertEquals(2, maxDistanceBFS(grid1));
        /**
         * Example 2:
         * Input: grid = [[1,0,0],[0,0,0],[0,0,0]]
         * Output: 4
         * Explanation: The cell (2, 2) is as far as possible from all the land with distance 4.
         */
        int[][] grid2 = new int[][]{{1, 0, 0}, {0, 0, 0}, {0, 0, 0}};
        assertEquals(4, maxDistanceBFS(grid2));
    }

    @Test
    public void maxDistanceDPTest() {
        /**
         * Example 1:
         * Input: grid = [[1,0,1],[0,0,0],[1,0,1]]
         * Output: 2
         * Explanation: The cell (1, 1) is as far as possible from all the land with distance 2.
         */
        int[][] grid1 = new int[][]{{1, 0, 1}, {0, 0, 0}, {1, 0, 1}};
        assertEquals(2, maxDistanceDP(grid1));
        /**
         * Example 2:
         * Input: grid = [[1,0,0],[0,0,0],[0,0,0]]
         * Output: 4
         * Explanation: The cell (2, 2) is as far as possible from all the land with distance 4.
         */
        int[][] grid2 = new int[][]{{1, 0, 0}, {0, 0, 0}, {0, 0, 0}};
        assertEquals(4, maxDistanceDP(grid2));
    }
}
