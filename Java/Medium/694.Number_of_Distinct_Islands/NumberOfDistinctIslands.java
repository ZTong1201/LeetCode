import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class NumberOfDistinctIslands {

    /**
     * You are given an m x n binary matrix grid. An island is a group of 1's (representing land) connected 4-directionally
     * (horizontal or vertical.) You may assume all four edges of the grid are surrounded by water.
     * <p>
     * An island is considered to be the same as another if and only if one island can be translated (and not rotated or
     * reflected) to equal the other.
     * <p>
     * Return the number of distinct islands.
     * <p>
     * Constraints:
     * <p>
     * m == grid.length
     * n == grid[i].length
     * 1 <= m, n <= 50
     * grid[i][j] is either 0 or 1.
     * <p>
     * Approach: Hash by path signature
     * In order to get the number of unique islands, we need a way to encode the visited island such that it can be recognized
     * if a same layout is met. The easiest approach is to keep track of the path while visiting the island. For example,
     * append 'R' if going to the right, append 'U' if going to the top, etc. We will have edge cases like the following
     * ->               ->                ->
     * |                 |                 |->
     * |                 |->               |
     * |->               |                 |
     * The path signature will all be "RDDDR". Therefore, we need to also record the step where we backtrack, i.e. going back
     * to the previous state. For example, the first path will always be "RDDDR", however, the second path will be "RDDDBR"
     * ("B" means back), and "RDDDBBR" for the third one.
     * <p>
     * Time: O(m * n) we need to traverse the entire grid
     * Space: O(m * n) if the entire grid is an island, then the path will be of length m * n
     */
    public int numDistinctIslands(int[][] grid) {
        Set<String> seenIsland = new HashSet<>();
        int m = grid.length, n = grid[0].length;
        int count = 0;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // only start searching from a land cell
                if (grid[i][j] == 1) {
                    StringBuilder island = new StringBuilder();
                    // connect all land cells and record the path
                    // '*' means the starting point
                    connectAllLandCells(grid, i, j, '*', island);
                    // only count the unique island layout
                    if (!seenIsland.contains(island.toString())) {
                        seenIsland.add(island.toString());
                        count++;
                    }
                }
            }
        }
        return count;
    }

    private void connectAllLandCells(int[][] grid, int i, int j, char direction, StringBuilder island) {
        // base case
        if (i < 0 || j < 0 || i >= grid.length || j >= grid[0].length || grid[i][j] == 0) return;
        // mark land cell as water to avoid revisit
        grid[i][j] = 0;
        // add the current direction into the path
        island.append(direction);
        // search 4 neighbors
        connectAllLandCells(grid, i + 1, j, 'D', island);
        connectAllLandCells(grid, i - 1, j, 'U', island);
        connectAllLandCells(grid, i, j + 1, 'R', island);
        connectAllLandCells(grid, i, j - 1, 'L', island);
        // always need to keep track the step if we come back
        island.append('B');
    }

    @Test
    public void numDistinctIslandsTest() {
        /**
         * Example 1:
         * Input: grid = [
         * [1,1,0,0,0],
         * [1,1,0,0,0],
         * [0,0,0,1,1],
         * [0,0,0,1,1]]
         * Output: 1
         */
        assertEquals(1, numDistinctIslands(new int[][]{{1, 1, 0, 0, 0}, {1, 1, 0, 0, 0},
                {0, 0, 0, 1, 1}, {0, 0, 0, 1, 1}}));
        /**
         * Example 2:
         * Input: grid = [
         * [1,1,0,1,1],
         * [1,0,0,0,0],
         * [0,0,0,0,1],
         * [1,1,0,1,1]]
         * Output: 3
         */
        assertEquals(3, numDistinctIslands(new int[][]{{1, 1, 0, 1, 1}, {1, 0, 0, 0, 0},
                {0, 0, 0, 0, 1}, {1, 1, 0, 1, 1}}));
        /**
         * Example 3:
         * Input: grid = [
         * [1,1,0],
         * [0,1,1],
         * [0,0,0],
         * [1,1,1],
         * [0,1,0]]
         * Output: 2
         */
        assertEquals(2, numDistinctIslands(new int[][]{{1, 1, 0}, {0, 1, 1}, {0, 0, 0},
                {1, 1, 1}, {0, 1, 0}}));
    }
}
