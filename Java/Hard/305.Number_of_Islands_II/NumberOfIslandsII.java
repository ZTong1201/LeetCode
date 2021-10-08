import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class NumberOfIslandsII {

    /**
     * You are given an empty 2D binary grid grid of size m x n. The grid represents a map where 0's represent water and 1's
     * represent land. Initially, all the cells of grid are water cells (i.e., all the cells are 0's).
     * <p>
     * We may perform an add land operation which turns the water at position into a land. You are given an array positions
     * where positions[i] = [ri, ci] is the position (ri, ci) at which we should operate the ith operation.
     * <p>
     * Return an array of integers answer where answer[i] is the number of islands after turning the cell (ri, ci) into a land.
     * <p>
     * An island is surrounded by water and is formed by connecting adjacent lands horizontally or vertically. You may assume
     * all four edges of the grid are all surrounded by water.
     * <p>
     * Constraints:
     * <p>
     * 1 <= m, n, positions.length <= 10^4
     * 1 <= m * n <= 10^4
     * positions[i].length == 2
     * 0 <= ri < m
     * 0 <= ci < n
     * <p>
     * Approach: Union Find
     * Basically, we want to compute the number of connected components after adding a new land cell. The best data structure
     * to handle connected components will be union find.
     * 1. Initially, we will have 0 connected components in the graph - we can initialize the parent index as -1 for all nodes
     * at beginning, indicating the node has been added
     * 2. It's possible that the input has duplication positions, then the second time the position is met, the parent node
     * must not be -1, in this case, we won't do any calculation but return the count of connected components.
     * 3. How to compute the number of connected components when a new land cell is added? We need to first change the parent
     * node from -1 to itself, and increment the count (pretend this is a standalone node first). Then we check the four
     * neighbors (at most), if the neighbor is a land cell (the parent index is not -1), we union the new land with its neighbor.
     * When two cells are unioned, we will decrement the size of connected components.
     */
    public List<Integer> numIslands2(int m, int n, int[][] positions) {
        UnionFind uf = new UnionFind(m * n);
        List<Integer> res = new ArrayList<>();
        int[][] next = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        for (int[] position : positions) {
            int row = position[0], col = position[1];
            int id = row * n + col;
            // if it's a duplicate node - no need for extra computation
            if (uf.isValidNode(id)) {
                res.add(uf.getNumOfConnectedComponents());
                continue;
            }

            // otherwise, add the node into the union find (set parent as itself)
            uf.setParent(id);

            // check 4 neighbors and see whether we can connect with any nodes
            for (int[] step : next) {
                int nextRow = row + step[0], nextCol = col + step[1];
                int nextId = nextRow * n + nextCol;
                // need to make sure the search is still in the grid + the neighbor is actually a land cell
                if (nextRow >= 0 && nextCol >= 0 && nextRow < m && nextCol < n && uf.isValidNode(nextId)) {
                    uf.union(id, nextId);
                }
            }
            // after union with the neighbors - get the number of updated connected components
            res.add(uf.getNumOfConnectedComponents());
        }
        return res;
    }

    private static class UnionFind {
        private final int[] size;
        private final int[] parent;
        private int numOfConnectedComponents;

        public UnionFind(int n) {
            this.size = new int[n];
            this.parent = new int[n];
            Arrays.fill(size, 1);
            // assign all parent nodes as -1 - no land cell initially
            Arrays.fill(parent, -1);
            numOfConnectedComponents = 0;
        }

        private int find(int i) {
            while (i != parent[i]) {
                parent[i] = parent[parent[i]];
                i = parent[i];
            }
            return i;
        }

        public boolean isValidNode(int i) {
            return this.parent[i] != -1;
        }

        public void setParent(int i) {
            // assign the parent node as itself first
            parent[i] = i;
            // increment the number of connected component (pretend the new node is a standalone node)
            numOfConnectedComponents++;
        }

        public int getNumOfConnectedComponents() {
            return this.numOfConnectedComponents;
        }

        public void union(int i, int j) {
            int p = find(i), q = find(j);
            if (p == q) return;
            if (size[p] >= size[q]) {
                parent[q] = p;
                size[p] += size[q];
            } else {
                parent[q] = p;
                size[q] += size[p];
            }
            // as long as we union a node with its neighbor, we need to decrement the number of connected components
            numOfConnectedComponents--;
        }
    }

    @Test
    public void numIslands2Test() {
        /**
         * Example 1:
         * Input: m = 3, n = 3, positions = [[0,0],[0,1],[1,2],[2,1]]
         * Output: [1,1,2,3]
         * Explanation:
         * Initially, the 2d grid is filled with water.
         * - Operation #1: addLand(0, 0) turns the water at grid[0][0] into a land. We have 1 island.
         * - Operation #2: addLand(0, 1) turns the water at grid[0][1] into a land. We still have 1 island.
         * - Operation #3: addLand(1, 2) turns the water at grid[1][2] into a land. We have 2 islands.
         * - Operation #4: addLand(2, 1) turns the water at grid[2][1] into a land. We have 3 islands.
         */
        List<Integer> expected1 = List.of(1, 1, 2, 3);
        List<Integer> actual1 = numIslands2(3, 3, new int[][]{{0, 0}, {0, 1}, {1, 2}, {2, 1}});
        assertEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: m = 1, n = 1, positions = [[0,0]]
         * Output: [1]
         */
        List<Integer> expected2 = List.of(1);
        List<Integer> actual2 = numIslands2(1, 1, new int[][]{{0, 0}});
        assertEquals(expected2, actual2);
        /**
         * Example 3:
         * Input: m = 3, n = 3, positions = [[0,0],[0,1],[1,2],[1,2]]
         * Output: [1,1,2,2]
         */
        List<Integer> expected3 = List.of(1, 1, 2, 2);
        List<Integer> actual3 = numIslands2(3, 3, new int[][]{{0, 0}, {0, 1}, {1, 2}, {1, 2}});
        assertEquals(expected3, actual3);
    }
}
