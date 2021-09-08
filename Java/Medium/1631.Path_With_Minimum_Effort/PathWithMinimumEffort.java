import org.junit.Test;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import static org.junit.Assert.assertEquals;

public class PathWithMinimumEffort {

    /**
     * You are a hiker preparing for an upcoming hike. You are given heights, a 2D array of size rows x columns, where
     * heights[row][col] represents the height of cell (row, col). You are situated in the top-left cell, (0, 0), and you
     * hope to travel to the bottom-right cell, (rows-1, columns-1) (i.e., 0-indexed). You can move up, down, left, or right,
     * and you wish to find a route that requires the minimum effort.
     * <p>
     * A route's effort is the maximum absolute difference in heights between two consecutive cells of the route.
     * <p>
     * Return the minimum effort required to travel from the top-left cell to the bottom-right cell.
     * <p>
     * Constraints:
     * <p>
     * rows == heights.length
     * columns == heights[i].length
     * 1 <= rows, columns <= 100
     * 1 <= heights[i][j] <= 10^6
     * <p>
     * Approach 1: Modified Dijkstra's Algorithm (the shortest path in weighted graph)
     * We can treat the entire grid as a graph, then each cell is a node in the graph. There is an edge between two adjacent
     * cells in which the cost is the absolute difference between two heights. Using priority queue, we can always pick
     * the node which has the minimum absolute difference with its neighbor. This is analogous to the Dijkstra's algorithm.
     * When the destination is reached the first time, the current maximum absolute difference is minimized.
     * <p>
     * Time: O(mn * log(mn)) in the worst case, we need to traverse the entire grid, and the priority queue is of size m * n.
     * Removing the smallest element from the heap requires O(log(mn)) run time.
     * Space: O(mn)
     */
    public int minimumEffortPathMinHeap(int[][] heights) {
        int rows = heights.length, cols = heights[0].length;
        boolean[][] visited = new boolean[rows][cols];
        int[][] next = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        // the priority queue will be sorted based on the min absolute difference
        PriorityQueue<Cell> minHeap = new PriorityQueue<>((a, b) -> (a.difference - b.difference));
        minHeap.add(new Cell(0, 0, 0));

        while (!minHeap.isEmpty()) {
            Cell curr = minHeap.poll();
            // if reaching the destination cell - return the minimum absolute difference
            if (curr.row == rows - 1 && curr.col == cols - 1) return curr.difference;

            visited[curr.row][curr.col] = true;

            // check out its 4 neighbors and go to the neighbor with the least effort
            for (int i = 0; i < 4; i++) {
                int nextRow = curr.row + next[i][0];
                int nextCol = curr.col + next[i][1];

                if (nextRow >= 0 && nextCol >= 0 && nextRow < rows && nextCol < cols && !visited[nextRow][nextCol]) {
                    // compute the current effort to move to its neighbor
                    int currDifference = Math.abs(heights[nextRow][nextCol] - heights[curr.row][curr.col]);
                    // get the actual maximum effort so far
                    int maxDifference = Math.max(currDifference, curr.difference);
                    // use the maximum effort on that path and move to next neighbor
                    minHeap.add(new Cell(nextRow, nextCol, maxDifference));
                }
            }
        }
        // this return statement shall never be reached
        return -1;
    }

    private static class Cell {
        int row;
        int col;
        int difference;

        public Cell(int row, int col, int difference) {
            this.row = row;
            this.col = col;
            this.difference = difference;
        }
    }

    /**
     * Approach 2: Binary Search + Graph Traversal (BFS or DFS)
     * Since the values in heights is bounded in range [1, 10^6]. Hence, the lower bound of effort will be 0, and the upper
     * bound is 10^6. We can use binary search to find the value. Essentially, the value we're searching is the minimum effort
     * such that there is a path between (0, 0) and (m - 1, n - 1). We can use either DFS or BFS to traverse the graph with
     * one new restriction: the neighbor cell is only reachable if and only if the absolute difference is less than or equal
     * to the value we're searching. The final result will be the minimum viable effort.
     * <p>
     * Time: O(mn) the binary search will take O(logn) time which is O(log10^6) in this case. For each searched value, we might
     * have to traverse the entire grid which gives O(mn) time. Therefore, in total we have O(mn * log10^6) = O(mn)
     * Space: O(min(m, n)) the maximum number of nodes in the queue will be min(m, n) if BFS is used
     */
    public int minimumEffortPathBinarySearch(int[][] heights) {
        int left = 0, right = 1000000;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (canReachDestination(heights, mid)) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }

    private boolean canReachDestination(int[][] heights, int minCost) {
        // normal BFS to check whether there is a path between (0, 0) and (m - 1, n - 1) with a cost upper bound
        int rows = heights.length, cols = heights[0].length;
        boolean[][] visited = new boolean[rows][cols];
        int[][] next = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        Queue<Integer> queue = new LinkedList<>();
        queue.add(0);

        while (!queue.isEmpty()) {
            int size = queue.size();

            for (int i = 0; i < size; i++) {
                int id = queue.poll();
                int currRow = id / cols, currCol = id % cols;
                // return true if reaching the destination
                if (currRow == rows - 1 && currCol == cols - 1) return true;

                if (!visited[currRow][currCol]) {
                    visited[currRow][currCol] = true;

                    for (int j = 0; j < 4; j++) {
                        int nextRow = currRow + next[j][0];
                        int nextCol = currCol + next[j][1];

                        if (nextRow >= 0 && nextCol >= 0 && nextRow < rows && nextCol < cols && !visited[nextRow][nextCol]) {
                            // compute the cost to move to its neighbor
                            int currCost = Math.abs(heights[nextRow][nextCol] - heights[currRow][currCol]);
                            // only add the neighbor into the queue when the cost doesn't exceed the restriction
                            if (currCost <= minCost) queue.add(nextRow * cols + nextCol);
                        }
                    }
                }
            }
        }
        return false;
    }

    @Test
    public void minimumEffortPathTest() {
        /**
         * Example 1:
         * Input: heights = [
         * [1,2,2],
         * [3,8,2],
         * [5,3,5]]
         * Output: 2
         * Explanation: The route of [1,3,5,3,5] has a maximum absolute difference of 2 in consecutive cells.
         * This is better than the route of [1,2,2,2,5], where the maximum absolute difference is 3.
         */
        assertEquals(2, minimumEffortPathMinHeap(new int[][]{{1, 2, 2}, {3, 8, 2}, {5, 3, 5}}));
        assertEquals(2, minimumEffortPathBinarySearch(new int[][]{{1, 2, 2}, {3, 8, 2}, {5, 3, 5}}));
        /**
         * Example 2:
         * Input: heights = [
         * [1,2,3],
         * [3,8,4],
         * [5,3,5]]
         * Output: 1
         * Explanation: The route of [1,2,3,4,5] has a maximum absolute difference of 1 in consecutive cells,
         * which is better than route [1,3,5,3,5].
         */
        assertEquals(1, minimumEffortPathMinHeap(new int[][]{{1, 2, 3}, {3, 8, 4}, {5, 3, 5}}));
        assertEquals(1, minimumEffortPathBinarySearch(new int[][]{{1, 2, 3}, {3, 8, 4}, {5, 3, 5}}));
        /**
         * Example 3:
         * Input: heights = [[1,2,1,1,1],[1,2,1,2,1],[1,2,1,2,1],[1,2,1,2,1],[1,1,1,2,1]]
         * Output: 0
         * Explanation: This route does not require any effort.
         */
        assertEquals(0, minimumEffortPathMinHeap(new int[][]{{1, 2, 1, 1, 1},
                {1, 2, 1, 2, 1}, {1, 2, 1, 2, 1}, {1, 2, 1, 2, 1}, {1, 1, 1, 2, 1}}));
        assertEquals(0, minimumEffortPathBinarySearch(new int[][]{{1, 2, 1, 1, 1},
                {1, 2, 1, 2, 1}, {1, 2, 1, 2, 1}, {1, 2, 1, 2, 1}, {1, 1, 1, 2, 1}}));
    }
}
