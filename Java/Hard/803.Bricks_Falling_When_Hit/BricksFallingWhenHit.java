import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;

public class BricksFallingWhenHit {

    /**
     * You are given an m x n binary grid, where each 1 represents a brick and 0 represents an empty space. A brick is stable if:
     * <p>
     * It is directly connected to the top of the grid, or
     * At least one other brick in its four adjacent cells is stable.
     * <p>
     * You are also given an array hits, which is a sequence of erasures we want to apply. Each time we want to erase the
     * brick at the location hits[i] = (rowi, coli). The brick on that location (if it exists) will disappear. Some other
     * bricks may no longer be stable because of that erasure and will fall. Once a brick falls, it is immediately erased
     * from the grid (i.e., it does not land on other stable bricks).
     * <p>
     * Return an array result, where each result[i] is the number of bricks that will fall after the ith erasure is applied.
     * <p>
     * Note that an erasure may refer to a location with no brick, and if it does, no bricks drop.
     * <p>
     * Constraints:
     * <p>
     * m == grid.length
     * n == grid[i].length
     * 1 <= m, n <= 200
     * grid[i][j] is 0 or 1.
     * 1 <= hits.length <= 4 * 10^4
     * hits[i].length == 2
     * 0 <= xi <= m - 1
     * 0 <= yi <= n - 1
     * All (xi, yi) are unique.
     * <p>
     * Approach: Union Find + Reverse Time
     * Basically, we want to know the size difference of connected component before and after a brick is hit. The easiest way
     * to handle connected components and their sizes is to use union find. In order to get the correct number, we could consider
     * it in the reverse way, i.e. pretend the brick we want to hit doesn't exist in the grid, if the size of connected component
     * increases after adding it back, then the size difference must be the number of dropped bricks if it was hit. Since previous
     * hit will have impact on the subsequent ones, we need to add brick back in the reverse order. Since all the top bricks
     * should be considered as connected already, we could consider add one more space in the union find for a virtual top node.
     * If a new node is added in the top row, we always connect that node with the virtual top such that everything at the top
     * are connected.
     * <p>
     * Time: O((MN + H) * a(MN)) where grid is of size M * N, and H is the number of hits. We first need to connect the grid
     * by taking out the bricks which will be hit later. In the worst case, there are M * N union operations if the grid is fully
     * connected, and the size of union find is O(MN). Later on, we need to add some bricks back, and in the worst case, there
     * will be O(H) operations.
     * Space: O(MN + H) we need to clone the entire grid + a union find with MN + 1 nodes. And the final array takes O(H) space
     */
    public int[] hitBricks(int[][] grid, int[][] hits) {
        int rows = grid.length, cols = grid[0].length;
        // clone the entire grid
        int[][] clonedGrid = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            clonedGrid[i] = grid[i].clone();
        }
        // take out bricks which will be hit later
        for (int[] hit : hits) {
            clonedGrid[hit[0]][hit[1]] = 0;
        }

        // need one more space for the virtual top node
        UnionFind uf = new UnionFind(rows * cols + 1);
        int virtualTop = rows * cols;
        // iterate over the grid, and connect everything together
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (clonedGrid[i][j] == 1) {
                    int id = i * cols + j;
                    // if there is a brick at the top - connect with the virtual top
                    if (i == 0) uf.union(id, virtualTop);
                    // since we're processing row by row, col by col
                    // it's only possible the top and the left neighbor can be connected
                    if (i > 0 && clonedGrid[i - 1][j] == 1) uf.union(id, (i - 1) * cols + j);
                    if (j > 0 && clonedGrid[i][j - 1] == 1) uf.union(id, i * cols + (j - 1));
                }
            }
        }

        int numOfHits = hits.length;
        int[] res = new int[numOfHits];
        int[][] next = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        // need to add brick back in the reverse order and check the connected component size change
        for (int i = numOfHits - 1; i >= 0; i--) {
            int currRow = hits[i][0], currCol = hits[i][1];
            // if the square in the original grid is not a brick - no need to do anything, the result will be 0
            if (grid[currRow][currCol] == 0) continue;

            int id = currRow * cols + currCol;
            int prevSizeOfTopConnectedComponent = uf.top();
            // try to add the brick back
            // connect with its neighbors if there are any bricks
            for (int[] step : next) {
                int nextRow = currRow + step[0], nextCol = currCol + step[1];
                if (nextRow >= 0 && nextCol >= 0 && nextRow < rows && nextCol < cols && clonedGrid[nextRow][nextCol] == 1) {
                    uf.union(id, nextRow * cols + nextCol);
                }
            }
            // if the added brick is on the top row - connect it with the virtual top
            if (currRow == 0) uf.union(id, virtualTop);
            // update the value in the cloned grid
            clonedGrid[currRow][currCol] = 1;
            // check the size change of connected components
            // note that it is also possible the size doesn't change, in that case,
            // no brick would fall if the brick is hit - the result will be 0
            // if the size indeed changes, we need to compute the difference, minus one because the brick hit doesn't count
            res[i] = Math.max(0, uf.top() - prevSizeOfTopConnectedComponent - 1);
        }
        return res;
    }

    private static class UnionFind {
        private final int[] size;
        private final int[] parent;

        public UnionFind(int n) {
            this.size = new int[n];
            this.parent = new int[n];
            Arrays.fill(size, 1);
            // the last cell is for the virtual top node
            // initially there is nothing connected with it
            // hence the size should be 0
            size[size.length - 1] = 0;
            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
        }

        private int find(int i) {
            while (i != parent[i]) {
                parent[i] = parent[parent[i]];
                i = parent[i];
            }
            return i;
        }

        public void union(int i, int j) {
            int p = find(i), q = find(j);
            if (p == q) return;
            if (size[p] >= size[q]) {
                parent[q] = p;
                size[p] += size[q];
            } else {
                parent[p] = q;
                size[q] += size[p];
            }
        }

        private int size(int i) {
            return size[find(i)];
        }

        public int top() {
            return size(size.length - 1);
        }
    }

    @Test
    public void hitBricksTest() {
        /**
         * Example 1:
         * Input: grid = [[1,0,0,0],[1,1,1,0]], hits = [[1,0]]
         * Output: [2]
         * Explanation: Starting with the grid:
         * [[1,0,0,0],
         *  [1,1,1,0]]
         * We erase the underlined brick at (1,0), resulting in the grid:
         * [[1,0,0,0],
         *  [0,1,1,0]]
         * The two underlined bricks are no longer stable as they are no longer connected to the top nor adjacent to another
         * stable brick, so they will fall. The resulting grid is:
         * [[1,0,0,0],
         *  [0,0,0,0]]
         * Hence the result is [2].
         */
        int[] expected1 = new int[]{2};
        int[] actual1 = hitBricks(new int[][]{{1, 0, 0, 0}, {1, 1, 1, 0}}, new int[][]{{1, 0}});
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: grid = [[1,0,0,0],[1,1,0,0]], hits = [[1,1],[1,0]]
         * Output: [0,0]
         * Explanation: Starting with the grid:
         * [[1,0,0,0],
         *  [1,1,0,0]]
         * We erase the underlined brick at (1,1), resulting in the grid:
         * [[1,0,0,0],
         *  [1,0,0,0]]
         * All remaining bricks are still stable, so no bricks fall. The grid remains the same:
         * [[1,0,0,0],
         *  [1,0,0,0]]
         * Next, we erase the underlined brick at (1,0), resulting in the grid:
         * [[1,0,0,0],
         *  [0,0,0,0]]
         * Once again, all remaining bricks are still stable, so no bricks fall.
         * Hence the result is [0,0].
         */
        int[] expected2 = new int[]{0, 0};
        int[] actual2 = hitBricks(new int[][]{{1, 0, 0, 0}, {1, 1, 0, 0}}, new int[][]{{1, 1}, {1, 0}});
        assertArrayEquals(expected2, actual2);
    }
}
