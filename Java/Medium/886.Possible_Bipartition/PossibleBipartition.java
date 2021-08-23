import org.junit.Test;

import java.util.ArrayList;
import java.util.Stack;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PossibleBipartition {

    /**
     * We want to split a group of n people (labeled from 1 to n) into two groups of any size. Each person may dislike
     * some other people, and they should not go into the same group.
     * <p>
     * Given the integer n and the array dislikes where dislikes[i] = [ai, bi] indicates that the person labeled ai does not
     * like the person labeled bi, return true if it is possible to split everyone into two groups in this way.
     * <p>
     * Constraints:
     * <p>
     * 1 <= n <= 2000
     * 0 <= dislikes.length <= 10^4
     * dislikes[i].length == 2
     * 1 <= dislikes[i][j] <= n
     * ai < bi
     * All the pairs of dislikes are unique.
     * <p>
     * Approach: Coloring nodes
     * Similar question to LeetCode 785: https://leetcode.com/problems/is-graph-bipartite/
     * This problem can be translated to a graph bipartition problem, the solution is to keep coloring adjacent nodes with
     * different colors, if at any time, this rule has been violated, which means at least two persons who dislike each other
     * would've been placed in a same group.
     * The first step would be constructing the graph, we can use an array of list to keep track of an edge between node i and
     * node j.
     * <p>
     * Time: O(E + V) need to traverse all the edges to construct the graph first. Then in the worst case, we would visit
     * all the nodes with all the edges while coloring the entire map.
     * Space: O(E + V) need to store all the edges to build the graph + a stack to store nodes to be visited
     */
    public boolean possibleBipartition(int n, int[][] dislikes) {
        // first step is to build the graph using adjacent map
        ArrayList<Integer>[] edges = new ArrayList[n + 1];
        for (int i = 1; i <= n; i++) {
            edges[i] = new ArrayList<>();
        }
        for (int[] dislike : dislikes) {
            // add edges in both nodes since the graph is undirected
            edges[dislike[0]].add(dislike[1]);
            edges[dislike[1]].add(dislike[0]);
        }

        // use stack to achieve DFS
        Stack<Integer> stack = new Stack<>();
        int[] color = new int[n + 1];

        for (int i = 1; i <= n; i++) {
            if (color[i] == 0) {
                color[i] = 1;
                stack.push(i);

                while (!stack.isEmpty()) {
                    int curr = stack.pop();
                    for (int neighbor : edges[curr]) {
                        if (color[neighbor] == 0) {
                            color[neighbor] = -color[curr];
                            stack.push(neighbor);
                        } else if (color[neighbor] == color[curr]) return false;
                    }
                }
            }
        }
        return true;
    }

    @Test
    public void possibleBipartitionTest() {
        /**
         * Example 1:
         * Input: n = 4, dislikes = [[1,2],[1,3],[2,4]]
         * Output: true
         * Explanation: group1 [1,4] and group2 [2,3].
         */
        assertTrue(possibleBipartition(4, new int[][]{{1, 2}, {1, 3}, {2, 4}}));
        /**
         * Example 2:
         * Input: n = 3, dislikes = [[1,2],[1,3],[2,3]]
         * Output: false
         */
        assertFalse(possibleBipartition(3, new int[][]{{1, 2}, {1, 3}, {2, 3}}));
        /**
         * Example 3:
         * Input: n = 5, dislikes = [[1,2],[2,3],[3,4],[4,5],[1,5]]
         * Output: false
         */
        assertFalse(possibleBipartition(5, new int[][]{{1, 2}, {2, 3}, {3, 4}, {4, 5}, {1, 5}}));
    }
}
