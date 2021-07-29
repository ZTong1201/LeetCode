import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class LargestIsland {

    /**
     * You are given an n x n binary matrix grid. You are allowed to change at most one 0 to be 1.
     * <p>
     * Return the size of the largest island in grid after applying this operation.
     * <p>
     * An island is a 4-directionally connected group of 1s.
     * <p>
     * Constraints:
     * <p>
     * n == grid.length
     * n == grid[i].length
     * 1 <= n <= 500
     * grid[i][j] is either 0 or 1.
     * <p>
     * Approach: Color coding + DFS
     * The main methodology is pretty similar to LeetCode 695: https://leetcode.com/problems/max-area-of-island/
     * Essentially, we can computing the area of each island and give each island an index (or code it into different colors)
     * By doing so, one can easily get the area if we know in which color the island is coded. When we're trying to turn a '0'
     * into a '1', we can visit it's four neighbors and get the area of the island to which the neighbors belong to get a larger
     * island, basically new area = 1 + area[left] + area[right] + area[top] + area[bottom]. Note that some of the neighbors
     * might belong to the same island already, hence it's not necessarily always the case to plus all 4 directional areas.
     * <p>
     * The algorithm looks like this:
     * 1. Initialize color = 2 as a starting point
     * 2. Traverse the entire grid, if a land cell is encountered, compute the area of that island and code the entire island
     * into a specific color. Update current maximum area. Record color -> area mapping for next traversal.
     * Color value will be incremented afterwards.
     * 3. Traverse the entire grid again, this time focus on the water cell. For a given '0' cell, find the set of its neighbors'
     * color, compute the area of newly formed island. Update the maximum area.
     * <p>
     * <p>
     * Time: O(N^2) each DFS pass would visit the entire grid
     * Space: O(N^2) need an array to record color -> area mapping
     */
    public int largestIsland(int[][] grid) {
        int res = 0, n = grid.length;
        // since 0 and 1 are already in use
        // the first color should start at 2
        int color = 2;
        // in theory, each cell would've needed a color
        // and since 0 and 1 are already taken, the n * n + 2 would be enough
        int[] area = new int[n * n + 2];
        // first DFS pass
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // get the area of each island and color coding it
                if (grid[i][j] == 1) {
                    // keep track of the color -> area mapping
                    area[color] = getArea(grid, i, j, color);
                    // update current maximum area
                    res = Math.max(res, area[color]);
                    // increment color for next island
                    color++;
                }
            }
        }

        int[][] next = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        // second DFS pass - to (virtually) turn one water cell into a land cell
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // reach a water cell
                if (grid[i][j] == 0) {
                    int newArea = 1;
                    // need a set to avoid visiting the same island multiple times
                    Set<Integer> colorSet = new HashSet<>();
                    for (int[] step : next) {
                        colorSet.add(getColor(grid, i + step[0], j + step[1]));
                    }
                    // compute the area of newest island
                    for (int neighborColor : colorSet) {
                        newArea += area[neighborColor];
                    }
                    // update maximum area after connection
                    res = Math.max(res, newArea);
                }
            }
        }
        return res;
    }

    /**
     * Given a position in the grid, return the color of that island. 0 will be returned if the position is not valid
     *
     * @param grid the entire grid
     * @param i    row index
     * @param j    column index
     * @return the color of that position, return 0 if it's not a valid position
     */
    private int getColor(int[][] grid, int i, int j) {
        if (i < 0 || j < 0 || i >= grid.length || j >= grid[0].length) return 0;
        // 0 will be returned if it's not colored
        return grid[i][j];
    }

    /**
     * Helper method to compute the area of island starting at point (i, j) and code the entire island into desired color
     *
     * @param grid  the whole grid
     * @param i     row index
     * @param j     column index
     * @param color the island will be coded into
     * @return the area of current island
     */
    private int getArea(int[][] grid, int i, int j, int color) {
        // base case
        // if the index is not valid or reaching a water cell '0' or any other island >= 2
        if (i < 0 || j < 0 || i >= grid.length || j >= grid[0].length || grid[i][j] != 1) return 0;
        // color coding
        grid[i][j] = color;
        // DFS
        return 1 + getArea(grid, i + 1, j, color) + getArea(grid, i - 1, j, color) +
                getArea(grid, i, j + 1, color) + getArea(grid, i, j - 1, color);
    }

    @Test
    public void largestIslandTest() {
        /**
         * Example 1:
         * Input: grid = [[1,0],[0,1]]
         * Output: 3
         * Explanation: Change one 0 to 1 and connect two 1s, then we get an island with area = 3.
         */
        assertEquals(3, largestIsland(new int[][]{{1, 0}, {0, 1}}));
        /**
         * Example 2:
         * Input: grid = [[1,1],[1,0]]
         * Output: 4
         * Explanation: Change the 0 to 1 and make the island bigger, only one island with area = 4.
         */
        assertEquals(4, largestIsland(new int[][]{{1, 1}, {0, 1}}));
        /**
         * Example 3:
         * Input: grid = [[1,1],[1,1]]
         * Output: 4
         * Explanation: Can't change any 0 to 1, only one island with area = 4.
         */
        assertEquals(4, largestIsland(new int[][]{{1, 1}, {1, 1}}));
    }
}
