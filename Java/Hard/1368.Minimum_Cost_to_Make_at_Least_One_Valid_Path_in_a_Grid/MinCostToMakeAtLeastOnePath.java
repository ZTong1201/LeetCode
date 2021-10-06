import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import static org.junit.Assert.assertEquals;

public class MinCostToMakeAtLeastOnePath {

    /**
     * Given a m x n grid. Each cell of the grid has a sign pointing to the next cell you should visit if you are currently
     * in this cell. The sign of grid[i][j] can be:
     * 1 which means go to the cell to the right. (i.e go from grid[i][j] to grid[i][j + 1])
     * 2 which means go to the cell to the left. (i.e go from grid[i][j] to grid[i][j - 1])
     * 3 which means go to the lower cell. (i.e go from grid[i][j] to grid[i + 1][j])
     * 4 which means go to the upper cell. (i.e go from grid[i][j] to grid[i - 1][j])
     * Notice that there could be some invalid signs on the cells of the grid which points outside the grid.
     * <p>
     * You will initially start at the upper left cell (0,0). A valid path in the grid is a path which starts from the upper
     * left cell (0,0) and ends at the bottom-right cell (m - 1, n - 1) following the signs on the grid. The valid path
     * doesn't have to be the shortest.
     * <p>
     * You can modify the sign on a cell with cost = 1. You can modify the sign on a cell one time only.
     * <p>
     * Return the minimum cost to make the grid have at least one valid path.
     * <p>
     * Constraints:
     * <p>
     * m == grid.length
     * n == grid[i].length
     * 1 <= m, n <= 100
     * <p>
     * Approach 1: Modified Dijkstra Algorithm
     * The key to this problem is translating the question into finding the shortest path in the graph. We can denote the
     * entire grid as a weighted graph, where each cell has at most weighted edges to its adjacent cells, the weight will be
     * 0 if the direction is correct (one can move from current cell to its neighbor), otherwise 1. Then we can start from
     * the cell (0, 0) and each time we will take the edge with the least weight, i.e. we will pick all 0 edges first. Then
     * we will move to other reachable cells by taking some cost. Since the smallest cost will always be taken at each step,
     * it's guaranteed that the cost will be the minimum when (m - 1, n - 1) is first reached.
     * <p>
     * Time: O(mn * log(mn)) For each cell, we have at most 4 edges, hence in total we can have 4*mn edges. These edges will
     * need to be added into the heap, so the size of heap is O(mn) and removing the smallest from the heap at each step
     * takes O(log(mn) time, and we need to go through the entire grid in the worst case
     * Space: O(mn)
     */
    public int minCostDijkstra(int[][] grid) {
        int rows = grid.length, cols = grid[0].length;
        // 0 - right, 1 - left, 2 - down, 3 - up
        int[][] next = new int[][]{{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

        // sort the neighbors by its cost
        PriorityQueue<Neighbor> minHeap = new PriorityQueue<>((a, b) -> (Integer.compare(a.cost, b.cost)));
        // start from (0, 0) with 0 cost
        minHeap.add(new Neighbor(0, 0, 0));
        boolean[][] visited = new boolean[rows][cols];

        while (!minHeap.isEmpty()) {
            Neighbor curr = minHeap.poll();
            // only travers unvisited nodes
            if (!visited[curr.row][curr.col]) {
                visited[curr.row][curr.col] = true;

                // return the minimum cost if reaching the destination point
                if ((curr.row == rows - 1) && (curr.col == cols - 1)) {
                    return curr.cost;
                }

                // otherwise, check 4 directions for valid neighbors and add new edges into the heap
                for (int dir = 0; dir < 4; dir++) {
                    int nextRow = curr.row + next[dir][0], nextCol = curr.col + next[dir][1];
                    // make sure neighbors are reachable and non-visited
                    if (nextRow >= 0 && nextCol >= 0 && nextRow < rows && nextCol < cols && !visited[nextRow][nextCol]) {
                        // compute the cost to move to the neighbor
                        // get the direction of current cell
                        int currDirection = grid[curr.row][curr.col] - 1;
                        // the cost to the neighbor will be 0 if the direction is the same with the original one, otherwise 1
                        int nextCost = currDirection == dir ? 0 : 1;
                        // add neighbors into the heap
                        minHeap.add(new Neighbor(nextRow, nextCol, curr.cost + nextCost));
                    }
                }
            }
        }
        // this return statement shall never be reached
        return -1;
    }

    private static class Neighbor {
        int row;
        int col;
        int cost;

        public Neighbor(int row, int col, int cost) {
            this.row = row;
            this.col = col;
            this.cost = cost;
        }
    }

    /**
     * Approach 2: Greedy (BFS + DFS)
     * Since we want to get the minimum cost, we'd like to first run DFS without changing any directions. If we can reach
     * the destination, then the cost will definitely be the minimum. However, it's also possible that we cannot reach
     * the target. In that scenario, we will use those nodes we already visited as starting point, change the direction, and
     * see whether we're able to reach the target. It's essentially a BFS step, starting from all visited nodes, and change
     * directions (4 at most) and redo the DFS. After each layer of nodes has been completed, we increment the cost since
     * we need to change one more direction before getting the destination. We could use a 2-D array to server 2 purposes
     * 1. mark visited nodes, 2. record the minimum cost to reach the cell. We can initialize the array with +inf, and if
     * the cost is updated, then we know the cell has been visited, and we wouldn't visit it again.
     * <p>
     * Time: O(mn) essentially, each node will be visited once, and we will change 3 directions to see whether we can visit
     * more nodes.
     * Space: O(mn)
     */
    private int[][] next;

    public int minCostGreedy(int[][] grid) {
        next = new int[][]{{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        int rows = grid.length, cols = grid[0].length;
        // use a 2-D array to keep track of the minimum cost to reach each node
        // also if the cost is not +inf, we know it's been visited
        int[][] minimumCost = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            Arrays.fill(minimumCost[i], Integer.MAX_VALUE);
        }

        // initialize a queue to add visited nodes - these will be starting points for the BFS step
        Queue<int[]> queue = new LinkedList<>();
        // we don't change any directions first, the cost will be 0
        int cost = 0;
        // starting from (0, 0), we'll visit all the nodes without changing the direction
        dfs(grid, queue, 0, 0, cost, minimumCost);
        // after the initial DFS is done
        // we use all visited nodes as entrypoint and try to direction at each position
        while (!queue.isEmpty()) {
            int size = queue.size();
            // increment the cost - since we need to change directions at current layer
            cost++;

            for (int i = 0; i < size; i++) {
                int[] curr = queue.poll();
                // change direction and search the grid again
                for (int dir = 0; dir < 4; dir++) {
                    dfs(grid, queue, curr[0] + next[dir][0], curr[1] + next[dir][1], cost, minimumCost);
                }
            }
        }
        return minimumCost[rows - 1][cols - 1];
    }

    private void dfs(int[][] grid, Queue<int[]> queue, int row, int col, int cost, int[][] minimumCost) {
        // base case, if out of grid or reach a visited node, do nothing
        if (row < 0 || col < 0 || row >= grid.length || col >= grid[0].length || minimumCost[row][col] != Integer.MAX_VALUE)
            return;
        // the current cost is the minimum cost to reach current node
        minimumCost[row][col] = cost;
        // add it to the queue for further search
        queue.add(new int[]{row, col});
        int currDirection = grid[row][col] - 1;
        // move to the neighbor and keep searching, the cost will remain unchanged
        dfs(grid, queue, row + next[currDirection][0], col + next[currDirection][1], cost, minimumCost);
    }

    @Test
    public void minCostTest() {
        /**
         * Example 1:
         * Input: grid = [[1,1,1,1],[2,2,2,2],[1,1,1,1],[2,2,2,2]]
         * Output: 3
         * Explanation: You will start at point (0, 0).
         * The path to (3, 3) is as follows. (0, 0) --> (0, 1) --> (0, 2) --> (0, 3) change the arrow to down with
         * cost = 1 --> (1, 3) --> (1, 2) --> (1, 1) --> (1, 0) change the arrow to down with cost = 1 -->
         * (2, 0) --> (2, 1) --> (2, 2) --> (2, 3) change the arrow to down with cost = 1 --> (3, 3)
         * The total cost = 3.
         */
        assertEquals(3, minCostDijkstra(new int[][]{{1, 1, 1, 1}, {2, 2, 2, 2}, {1, 1, 1, 1}, {2, 2, 2, 2}}));
        assertEquals(3, minCostGreedy(new int[][]{{1, 1, 1, 1}, {2, 2, 2, 2}, {1, 1, 1, 1}, {2, 2, 2, 2}}));
        /**
         * Example 2:
         * Input: grid = [[1,1,3],[3,2,2],[1,1,4]]
         * Output: 0
         * Explanation: You can follow the path from (0, 0) to (2, 2).
         */
        assertEquals(0, minCostDijkstra(new int[][]{{1, 1, 3}, {3, 2, 2}, {1, 1, 4}}));
        assertEquals(0, minCostGreedy(new int[][]{{1, 1, 3}, {3, 2, 2}, {1, 1, 4}}));
        /**
         * Example 3：
         * Input: grid = [[1,2],[4,3]]
         * Output: 1
         */
        assertEquals(1, minCostDijkstra(new int[][]{{1, 2}, {4, 3}}));
        assertEquals(1, minCostGreedy(new int[][]{{1, 2}, {4, 3}}));
        /**
         * Example 4:
         * Input: grid = [[2,2,2],[2,2,2]]
         * Output: 3
         */
        assertEquals(3, minCostDijkstra(new int[][]{{2, 2, 2}, {2, 2, 2}}));
        assertEquals(3, minCostGreedy(new int[][]{{2, 2, 2}, {2, 2, 2}}));
        /**
         * Example 5:
         * Input: grid = [[4]]
         * Output: 0
         */
        assertEquals(0, minCostDijkstra(new int[][]{{4}}));
        assertEquals(0, minCostGreedy(new int[][]{{4}}));
    }
}
