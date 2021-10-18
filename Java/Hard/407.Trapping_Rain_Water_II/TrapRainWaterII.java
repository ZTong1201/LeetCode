import org.junit.Test;

import java.util.PriorityQueue;

import static org.junit.Assert.assertEquals;

public class TrapRainWaterII {

    /**
     * Given an m x n integer matrix heightMap representing the height of each unit cell in a 2D elevation map, return the
     * volume of water it can trap after raining.
     * <p>
     * Constraints:
     * <p>
     * m == heightMap.length
     * n == heightMap[i].length
     * 1 <= m, n <= 200
     * 0 <= heightMap[i][j] <= 2 * 10^4
     * <p>
     * Approach: Priority Queue
     * Similar to the 1-D problem, the key part is that for each cell, we need to find the lowest boundary height in order
     * to determine how much rain water can be trapped. The naive approach will be use BFS to search the entire grid and
     * find the minimum height on the boundary, which will take O(mn) time for each cell. A better idea will be we add all
     * boundary cells into the priority queue and treat them as the boundary walls. We start from the smallest height and try
     * to flood back inside. If we reach a smaller height, which means we must be able to trap some rain water. Or we reach
     * a larger height, and it might become a new boundary wall if all other smaller heights are visited.
     * <p>
     * Time: O(mnlog(mn)) in the worst case we will add all the cells into the heap and add/remove elements from the heap
     * takes O(log(mn)) time, we need to visit every cell once
     * Space: O(mn)
     */
    public int trapRainWater(int[][] heightMap) {
        int rows = heightMap.length, cols = heightMap[0].length;
        // use a heap to keep track of the smallest boundary heights
        PriorityQueue<Cell> minHeap = new PriorityQueue<>((a, b) -> (Integer.compare(a.height, b.height)));
        boolean[][] visited = new boolean[rows][cols];
        // add the boundary cells into the heap first
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (i == 0 || j == 0 || i == rows - 1 || j == cols - 1) {
                    minHeap.add(new Cell(i, j, heightMap[i][j]));
                    // mark as visited to avoid duplicate visit
                    visited[i][j] = true;
                }
            }
        }

        int totalRain = 0;
        int[][] next = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        // traverse the grid from the boundary to the inner cells
        while (!minHeap.isEmpty()) {
            Cell curr = minHeap.poll();
            // check 4 neighbors
            for (int[] step : next) {
                int nextRow = curr.row + step[0], nextCol = curr.col + step[1];
                // still need to be inside the grid and unvisited
                if (nextRow >= 0 && nextCol >= 0 && nextRow < rows && nextCol < cols && !visited[nextRow][nextCol]) {
                    // mark as visited
                    visited[nextRow][nextCol] = true;
                    // check whether we can trap some rain water here
                    // we can when the boundary height is greater than the current height
                    totalRain += Math.max(0, curr.height - heightMap[nextRow][nextCol]);
                    // the height will always keep track of the maximum boundary height
                    minHeap.add(new Cell(nextRow, nextCol, Math.max(curr.height, heightMap[nextRow][nextCol])));
                }
            }
        }
        return totalRain;
    }

    private static class Cell {
        int row;
        int col;
        int height;

        public Cell(int row, int col, int height) {
            this.row = row;
            this.col = col;
            this.height = height;
        }
    }

    @Test
    public void trapRainWaterTest() {
        /**
         * Example 1:
         * Input: heightMap = [[1,4,3,1,3,2],[3,2,1,3,2,4],[2,3,3,2,3,1]]
         * Output: 4
         * Explanation: After the rain, water is trapped between the blocks.
         * We have two small pounds 1 and 3 units trapped.
         * The total volume of water trapped is 4.
         */
        assertEquals(4, trapRainWater(new int[][]{
                {1, 4, 3, 1, 3, 2},
                {3, 2, 1, 3, 2, 4},
                {2, 3, 3, 2, 3, 1}}));
        /**
         * Example 2:
         * Input: heightMap = [[3,3,3,3,3],[3,2,2,2,3],[3,2,1,2,3],[3,2,2,2,3],[3,3,3,3,3]]
         * Output: 10
         */
        assertEquals(10, trapRainWater(new int[][]{
                {3, 3, 3, 3, 3},
                {3, 2, 2, 2, 3},
                {3, 2, 1, 2, 3},
                {3, 2, 2, 2, 3},
                {3, 3, 3, 3, 3}}));
        /**
         * Example 3:
         * Input: heightMap = [[12,13,1,12],[13,4,13,12],[13,8,10,12],[12,13,12,12],[13,13,13,13]]
         * Output: 14
         */
        assertEquals(14, trapRainWater(new int[][]{
                {12, 13, 1, 12},
                {13, 4, 13, 12},
                {13, 8, 10, 12},
                {12, 13, 12, 12},
                {13, 13, 13, 13}}));
    }
}
