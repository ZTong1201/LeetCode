import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import static org.junit.Assert.assertArrayEquals;

public class ZeroOneMatrix {

    /**
     * Given an m x n binary matrix mat, return the distance of the nearest 0 for each cell.
     * <p>
     * The distance between two adjacent cells is 1.
     * <p>
     * Constraints:
     * <p>
     * m == mat.length
     * n == mat[i].length
     * 1 <= m, n <= 10^4
     * 1 <= m * n <= 10^4
     * mat[i][j] is either 0 or 1.
     * There is at least one 0 in mat.
     * <p>
     * Approach 1: BFS
     * We can start BFS from all 0's, the problem will be translated into find the nearest 1 of a 0 cell. We can initialize
     * the result array as
     * 1. if mat[i][j] == 0, then res[i][j] = 0
     * 2. if mat[i][j] == 1, then res[i][j] = Integer.MAX_VALUE
     * While searching for nearest 1's at index (i', j') then res[i'][j'] = min(res(all neighbors of i',j')) + 1
     * <p>
     * Time: O(mn) each cell will be visited once
     * Space: O(mn)
     */
    public int[][] updateMatrixBFS(int[][] mat) {
        int m = mat.length, n = mat[0].length;
        int[][] minDistance = new int[m][n];
        // initialize min distance as inf since minimum will be taken
        for (int i = 0; i < m; i++) {
            Arrays.fill(minDistance[i], Integer.MAX_VALUE);
        }
        Queue<Integer> queue = new LinkedList<>();
        // add all 0 positions into the queue
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (mat[i][j] == 0) {
                    queue.add(i * n + j);
                    // the min distance will be 0 for 0 cell
                    minDistance[i][j] = 0;
                }
            }
        }

        int[][] next = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        while (!queue.isEmpty()) {
            // search the nearest 1 cell for all 0's
            int id = queue.poll();
            int currRow = id / n, currCol = id % n;

            for (int i = 0; i < 4; i++) {
                int nextRow = currRow + next[i][0];
                int nextCol = currCol + next[i][1];

                if (nextRow >= 0 && nextCol >= 0 && nextRow < m && nextCol < n) {
                    // need to take the minimum distance from its neighbors
                    if (minDistance[nextRow][nextCol] > minDistance[currRow][currCol] + 1) {
                        minDistance[nextRow][nextCol] = minDistance[currRow][currCol] + 1;
                        queue.add(nextRow * n + nextCol);
                    }
                }
            }
        }
        return minDistance;
    }

    /**
     * Approach 2: DP
     * We can use extra space to store the information we already calculated, then use DP to solve the problem. Since each
     * cell has 4 neighbors. DP from top-left to bottom-right would only handle its left and top neighbors. Similarly, the
     * reverse DP will handle its bottom and right neighbors. Therefore, we can run two separate DPs to compute the minimum
     * at each cell. Since the minimum is searched, we can initialize the result array as Integer.MAX_VALUE.
     * <p>
     * Time: O(mn) need to go through the entire grid twice
     * Space: O(mn)
     */
    public int[][] updateMatrixDP(int[][] mat) {
        int m = mat.length, n = mat[0].length;
        int[][] minDistance = new int[m][n];
        // initialize all values as inf
        for (int i = 0; i < m; i++) {
            Arrays.fill(minDistance[i], Integer.MAX_VALUE);
        }

        // first traversal, from top-left to bottom-right
        // also need to handle cells with 0
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // first round will update all 0 cells to 0
                if (mat[i][j] == 0) {
                    minDistance[i][j] = 0;
                } else {
                    // check if there is a minimum path from its top neighbor
                    if (i > 0) {
                        // Integer.MAX_VALUE + 1 will result in negative numbers
                        // we need to take that into account
                        // only take the minimum from its neighbor when its neighbor has been visited
                        minDistance[i][j] = minDistance[i - 1][j] == Integer.MAX_VALUE ? minDistance[i][j] :
                                Math.min(minDistance[i][j], minDistance[i - 1][j] + 1);
                    }
                    // check if there is a minimum path from its left neighbor
                    if (j > 0) {
                        minDistance[i][j] = minDistance[i][j - 1] == Integer.MAX_VALUE ? minDistance[i][j] :
                                Math.min(minDistance[i][j], minDistance[i][j - 1] + 1);
                    }
                }
            }
        }

        // second traversal - from bottom-right to top-left
        for (int i = m - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
                // check if there is a minimum path from its bottom neighbor
                if (i < m - 1) {
                    minDistance[i][j] = minDistance[i + 1][j] == Integer.MAX_VALUE ? minDistance[i][j] :
                            Math.min(minDistance[i][j], minDistance[i + 1][j] + 1);
                }
                if (j < n - 1) {
                    // check if there is a minimum path from its right neighbor
                    minDistance[i][j] = minDistance[i][j + 1] == Integer.MAX_VALUE ? minDistance[i][j] :
                            Math.min(minDistance[i][j], minDistance[i][j + 1] + 1);
                }
            }
        }
        return minDistance;
    }

    @Test
    public void updateMatrixTest() {
        /**
         * Example 1:
         * Input: mat = [[0,0,0],[0,1,0],[0,0,0]]
         * Output: [[0,0,0],[0,1,0],[0,0,0]]
         */
        int[][] expected1 = new int[][]{{0, 0, 0}, {0, 1, 0}, {0, 0, 0}};
        int[][] actualBFS1 = updateMatrixBFS(new int[][]{{0, 0, 0}, {0, 1, 0}, {0, 0, 0}});
        int[][] actualDP1 = updateMatrixDP(new int[][]{{0, 0, 0}, {0, 1, 0}, {0, 0, 0}});
        assertArrayEquals(expected1, actualBFS1);
        assertArrayEquals(expected1, actualDP1);
        /**
         * Example 2:
         * Input: mat = [[0,0,0],[0,1,0],[1,1,1]]
         * Output: [[0,0,0],[0,1,0],[1,2,1]]
         */
        int[][] expected2 = new int[][]{{0, 0, 0}, {0, 1, 0}, {1, 2, 1}};
        int[][] actualBFS2 = updateMatrixBFS(new int[][]{{0, 0, 0}, {0, 1, 0}, {1, 1, 1}});
        int[][] actualDP2 = updateMatrixDP(new int[][]{{0, 0, 0}, {0, 1, 0}, {1, 1, 1}});
        assertArrayEquals(expected2, actualBFS2);
        assertArrayEquals(expected2, actualDP2);
    }
}
