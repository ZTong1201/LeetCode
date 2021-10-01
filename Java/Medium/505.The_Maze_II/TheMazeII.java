import org.junit.Test;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class TheMazeII {

    /**
     * There is a ball in a maze with empty spaces (represented as 0) and walls (represented as 1). The ball can go through
     * the empty spaces by rolling up, down, left or right, but it won't stop rolling until hitting a wall. When the ball
     * stops, it could choose the next direction.
     * <p>
     * Given the m x n maze, the ball's start position and the destination, where start = [startrow, startcol] and
     * destination = [destinationrow, destinationcol], return the shortest distance for the ball to stop at the
     * destination. If the ball cannot stop at destination, return -1.
     * <p>
     * The distance is the number of empty spaces traveled by the ball from the start position (excluded) to the
     * destination (included).
     * <p>
     * You may assume that the borders of the maze are all walls.
     * <p>
     * Constraints:
     * <p>
     * m == maze.length
     * n == maze[i].length
     * 1 <= m, n <= 100
     * maze[i][j] is 0 or 1.
     * start.length == 2
     * destination.length == 2
     * 0 <= startrow, destinationrow <= m
     * 0 <= startcol, destinationcol <= n
     * Both the ball and the destination exist in an empty space, and they will not be in the same position initially.
     * The maze contains at least 2 empty spaces.
     * <p>
     * Approach: Dijkstra Algorithm
     * We will simulate the process starting from the start position with distance 0, and at each iteration, we search
     * all the reachable neighbors and add the updated distance into a priority queue. Then we greedily move to the neighbor
     * with the least effort and restart looking around until the destination position is first reached. Note that we might
     * not be able to arrive at the destination, hence, we need to keep track of the visited nodes and if all reachable
     * nodes have been iterated whereas we still cannot find the solution, return -1.
     * <p>
     * Time: O(m * n * max(m, n) * log(mn)) in the worst case, we might be able to visit all empty squares and at each iteration,
     * we need either go through the entire row or the entire column to slide the ball to its correct position. Also, in the
     * worst case, we remove 1 position from the queue but add 3 more into it, then we may end up having almost all the position
     * in the queue as well. Then the size of queue is bounded by O(mn) and removing the one with the smallest distance takes
     * O(log(mn)) time.
     * Space: O(mn)
     */
    public int shortestDistance(int[][] maze, int[] start, int[] destination) {
        int m = maze.length, n = maze[0].length;
        // create a priority queue to sort each position by the distance from the start point
        // pair[0] = id of position, pair[1] = distance
        PriorityQueue<int[]> minPQ = new PriorityQueue<>((a, b) -> (Integer.compare(a[1], b[1])));
        // the initial distance is 0
        minPQ.add(new int[]{start[0] * n + start[1], 0});
        Set<Integer> visited = new HashSet<>();
        int[][] next = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        while (!minPQ.isEmpty()) {
            // we will search the position which has the smallest distance to the start point at each iteration
            int[] curr = minPQ.poll();

            // each position will be at most visited once since we need the shortest distance
            if (!visited.contains(curr[0])) {
                visited.add(curr[0]);

                int currRow = curr[0] / n, currCol = curr[0] % n;
                // if we reach the destination point, return the shortest distance recorded so far
                if (currRow == destination[0] && currCol == destination[1]) return curr[1];

                // otherwise, search four directions and add the corresponding (position, distance) pair into the queu
                for (int[] step : next) {
                    int nextRow = currRow + step[0], nextCol = currCol + step[1], distanceTraveled = 0;
                    // need to slide along the direction until next available position
                    while (nextRow >= 0 && nextCol >= 0 && nextRow < m && nextCol < n && maze[nextRow][nextCol] == 0) {
                        nextRow += step[0];
                        nextCol += step[1];
                        distanceTraveled++;
                    }
                    // now nextRow and nextCol corresponds to the first unavailable square along that direction
                    // need to decrement them by step[0] and step[1] before adding into the queue
                    int nextId = (nextRow - step[0]) * n + (nextCol - step[1]);
                    // only add non-visited neighbors
                    if (!visited.contains(nextId)) {
                        minPQ.add(new int[]{nextId, curr[1] + distanceTraveled});
                    }
                }
            }
        }
        return -1;
    }

    @Test
    public void shortestDistanceTest() {
        /**
         * Example 1:
         * Input: maze = [[0,0,1,0,0],[0,0,0,0,0],[0,0,0,1,0],[1,1,0,1,1],[0,0,0,0,0]], start = [0,4], destination = [4,4]
         * Output: 12
         * Explanation: One possible way is : left -> down -> left -> down -> right -> down -> right.
         * The length of the path is 1 + 1 + 3 + 1 + 2 + 2 + 2 = 12.
         */
        assertEquals(12, shortestDistance(new int[][]{{0, 0, 1, 0, 0}, {0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0}, {1, 1, 0, 1, 1}, {0, 0, 0, 0, 0}}, new int[]{0, 4}, new int[]{4, 4}));
        /**
         * Example 2:
         * Input: maze = [[0,0,1,0,0],[0,0,0,0,0],[0,0,0,1,0],[1,1,0,1,1],[0,0,0,0,0]], start = [0,4], destination = [3,2]
         * Output: -1
         * Explanation: There is no way for the ball to stop at the destination. Notice that you can pass through the
         * destination but you cannot stop there.
         */
        assertEquals(-1, shortestDistance(new int[][]{{0, 0, 1, 0, 0}, {0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0}, {1, 1, 0, 1, 1}, {0, 0, 0, 0, 0}}, new int[]{0, 4}, new int[]{3, 2}));
        /**
         * Example 3:
         * Input: maze = [[0,0,0,0,0],[1,1,0,0,1],[0,0,0,0,0],[0,1,0,0,1],[0,1,0,0,0]], start = [4,3], destination = [0,1]
         * Output: -1
         */
        assertEquals(-1, shortestDistance(new int[][]{{0, 0, 0, 0, 0}, {1, 1, 0, 0, 1},
                {0, 0, 0, 0, 0}, {0, 1, 0, 0, 1}, {0, 1, 0, 0, 0}}, new int[]{4, 3}, new int[]{0, 1}));
    }
}
