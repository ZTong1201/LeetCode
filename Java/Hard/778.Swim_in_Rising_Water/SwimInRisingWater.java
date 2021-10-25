import org.junit.Test;

import java.util.Arrays;
import java.util.PriorityQueue;

import static org.junit.Assert.assertEquals;

public class SwimInRisingWater {

    /**
     * You are given an n x n integer matrix grid where each value grid[i][j] represents the elevation at that point (i, j).
     * <p>
     * The rain starts to fall. At time t, the depth of the water everywhere is t. You can swim from a square to another
     * 4-directionally adjacent square if and only if the elevation of both squares individually are at most t. You can swim
     * infinite distances in zero time. Of course, you must stay within the boundaries of the grid during your swim.
     * <p>
     * Return the least time until you can reach the bottom right square (n - 1, n - 1) if you start at the top left square (0, 0).
     * <p>
     * Constraints:
     * <p>
     * n == grid.length
     * n == grid[i].length
     * 1 <= n <= 50
     * 0 <= grid[i][j] < n^2
     * Each value grid[i][j] is unique.
     * <p>
     * Approach: Heap + Union Find
     * Basically, we need greedily rise water one level at a time and see whether there is a path between (0, 0) and
     * (n - 1, n - 1). To achieve this greedy algorithm, we can take advantage of the heap data structure to always provide
     * a cell with the lowest water level. To easily check whether start and end nodes are connected, we can take advantage of
     * the union find structure to check connectivity in O(1) runtime in practice.
     * <p>
     * Time: O(n^2 * logn), we need to put n^2 nodes into the heap, and adding & removing from the heap takes O(logn^2)
     * time which is basically O(2 * logn).
     * Space: O(n^2)
     */
    public int swimInWater(int[][] grid) {
        int n = grid.length;
        // sort each cell by the level of water
        PriorityQueue<int[]> cells = new PriorityQueue<>((a, b) -> (Integer.compare(grid[a[0]][a[1]], grid[b[0]][b[1]])));
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                cells.add(new int[]{i, j});
            }
        }

        int[][] next = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        UnionFind uf = new UnionFind(n * n);
        int start = 0, end = n * n - 1;

        while (!cells.isEmpty()) {
            int[] curr = cells.poll();
            int currRow = curr[0], currCol = curr[1];
            int currLevel = grid[currRow][currCol];

            // for a given cell, connect with its 4 neighbors if the adjacent water level <= current water level
            for (int[] step : next) {
                int nextRow = currRow + step[0], nextCol = currCol + step[1];

                if (nextRow >= 0 && nextCol >= 0 && nextRow < n && nextCol < n) {
                    if (grid[nextRow][nextCol] <= currLevel) {
                        uf.union(currRow * n + currCol, nextRow * n + nextCol);
                    }
                }

                // if the start and end nodes are connected now - we can return the current water level since it's the smallest
                if (uf.isConnected(start, end)) return currLevel;
            }
        }
        // this return statement shall never be reached
        return -1;
    }

    private static class UnionFind {
        private final int[] size;
        private final int[] parent;

        public UnionFind(int n) {
            this.size = new int[n];
            this.parent = new int[n];
            Arrays.fill(this.size, 1);
            for (int i = 0; i < n; i++) {
                this.parent[i] = i;
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
                size[p] += size[q];
                parent[q] = p;
            } else {
                size[q] += size[p];
                parent[p] = q;
            }
        }

        public boolean isConnected(int i, int j) {
            return find(i) == find(j);
        }
    }

    @Test
    public void swimInWaterTest() {
        /**
         * Example 1:
         * Input: grid = [[0,2],[1,3]]
         * Output: 3
         * Explanation:
         * At time 0, you are in grid location (0, 0).
         * You cannot go anywhere else because 4-directionally adjacent neighbors have a higher elevation than t = 0.
         * You cannot reach point (1, 1) until time 3.
         * When the depth of water is 3, we can swim anywhere inside the grid.
         */
        assertEquals(3, swimInWater(new int[][]{{0, 2}, {1, 3}}));
        /**
         * Example 2:
         * Input: grid = [[0,1,2,3,4],[24,23,22,21,5],[12,13,14,15,16],[11,17,18,19,20],[10,9,8,7,6]]
         * Output: 16
         * Explanation:
         * We need to wait until time 16 so that (0, 0) and (4, 4) are connected.
         */
        assertEquals(16, swimInWater(new int[][]{
                {0, 1, 2, 3, 4},
                {24, 23, 22, 21, 5},
                {12, 13, 14, 15, 16},
                {11, 17, 18, 19, 20},
                {10, 9, 8, 7, 6}}));
    }
}
