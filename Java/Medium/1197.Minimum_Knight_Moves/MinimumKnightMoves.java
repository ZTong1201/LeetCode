import org.junit.Test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class MinimumKnightMoves {

    /**
     * In an infinite chess board with coordinates from -infinity to +infinity, you have a knight at square [0, 0].
     * <p>
     * A knight has 8 possible moves it can make, as illustrated below. Each move is two squares in a cardinal direction,
     * then one square in an orthogonal direction.
     * <p>
     * Return the minimum number of steps needed to move the knight to the square [x, y]. It is guaranteed the answer exists.
     * <p>
     * Constraints:
     * <p>
     * -300 <= x, y <= 300
     * 0 <= |x| + |y| <= 300
     * <p>
     * Approach: BFS
     * It's a typical BFS problem to find the shortest path. The only caveat here is that the hash set is not a very efficient
     * data structure in Java. If we know the bound of the problem, we can use an array to keep track of visited nodes.
     * <p>
     * Time: O(x * y) the board is bounded by 2x and 2y so there will be 2x * 2y total cells. The time complexity is bounded
     * by O(x * y)
     * Space: O(x * y)
     */
    public int minKnightMoves(int x, int y) {
        int[][] next = new int[][]{{1, 2}, {1, -2}, {-1, 2}, {-1, -2}, {2, 1}, {2, -1}, {-2, 1}, {-2, -1}};
        Queue<int[]> queue = new LinkedList<>();
        // start from (0, 0) to run BFS
        queue.add(new int[]{0, 0});
        // use a hash set to keep track of visited nodes
        Set<String> visited = new HashSet<>();

        int minSteps = 0;

        while (!queue.isEmpty()) {
            int size = queue.size();

            for (int i = 0; i < size; i++) {
                int[] curr = queue.poll();
                String currLoc = curr[0] + "/" + curr[1];

                if (!visited.contains(currLoc)) {
                    visited.add(currLoc);

                    if (curr[0] == x && curr[1] == y) return minSteps;

                    for (int[] step : next) {
                        int nextX = curr[0] + step[0], nextY = curr[1] + step[1];

                        if (!visited.contains(nextX + "/" + nextY)) {
                            queue.add(new int[]{nextX, nextY});
                        }
                    }
                }
            }
            minSteps++;
        }
        // this statement shall never be reached
        return -1;
    }

    @Test
    public void minKnightMovesTest() {
        /**
         * Example 1:
         * Input: x = 2, y = 1
         * Output: 1
         * Explanation: [0, 0] → [2, 1]
         */
        assertEquals(1, minKnightMoves(2, 1));
        /**
         * Example 2:
         * Input: x = 5, y = 5
         * Output: 4
         * Explanation: [0, 0] → [2, 1] → [4, 2] → [3, 4] → [5, 5]
         */
        assertEquals(4, minKnightMoves(5, 5));
    }
}
