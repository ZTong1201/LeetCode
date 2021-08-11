import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FindEventualSafeStates {

    /**
     * We start at some node in a directed graph, and every turn, we walk along a directed edge of the graph. If we reach
     * a terminal node (that is, it has no outgoing directed edges), we stop.
     * <p>
     * We define a starting node to be safe if we must eventually walk to a terminal node. More specifically, there is a
     * natural number k, so that we must have stopped at a terminal node in less than k steps for any choice of where to walk.
     * <p>
     * Return an array containing all the safe nodes of the graph. The answer should be sorted in ascending order.
     * <p>
     * The directed graph has n nodes with labels from 0 to n - 1, where n is the length of graph. The graph is given in the
     * following form: graph[i] is a list of labels j such that (i, j) is a directed edge of the graph, going from node i
     * to node j.
     * <p>
     * Constraints:
     * <p>
     * n == graph.length
     * 1 <= n <= 104
     * 0 <= graph[i].length <= n
     * graph[i] is sorted in a strictly increasing order.
     * The graph may contain self-loops.
     * The number of edges in the graph will be in the range [1, 4 * 10^4].
     * <p>
     * Approach: Topological sorting + detect cycle
     * We can traverse the entire graph in a topological sorting way, hence it will take O(V + E) to mark every node in the
     * graph. If at a given node, there is no cycle detected, then it's a safe state, and we can add it to the final list.
     * <p>
     * Time: O(V + E) need to visit all edges and nodes to mark them as visited
     * Space: O(V) need a visit array to keep track of the visiting state for each node
     */
    public List<Integer> eventualSafeNodes(int[][] graph) {
        int n = graph.length;
        int[] visit = new int[n];
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (!hasCycle(i, visit, graph)) res.add(i);
        }
        return res;
    }

    private boolean hasCycle(int curr, int[] visit, int[][] graph) {
        if (visit[curr] == 1) return true;
        if (visit[curr] == 2) return false;

        visit[curr] = 1;
        for (int neighbor : graph[curr]) {
            if (hasCycle(neighbor, visit, graph)) return true;
        }
        visit[curr] = 2;
        return false;
    }

    @Test
    public void eventualSafeNodesTest() {
        /**
         * Example 1:
         * Input: graph = [[1,2],[2,3],[5],[0],[5],[],[]]
         * Output: [2,4,5,6]
         */
        List<Integer> expected1 = List.of(2, 4, 5, 6);
        List<Integer> actual1 = eventualSafeNodes(new int[][]{{1, 2}, {2, 3}, {5}, {0}, {5}, {}, {}});
        assertEquals(expected1.size(), actual1.size());
        for (int i = 0; i < expected1.size(); i++) {
            assertEquals(expected1.get(i), actual1.get(i));
        }
        /**
         * Example 2:
         * Input: graph = [[1,2,3,4],[1,2],[3,4],[0,4],[]]
         * Output: [4]
         */
        List<Integer> expected2 = List.of(4);
        List<Integer> actual2 = eventualSafeNodes(new int[][]{{1, 2, 3, 4}, {1, 2}, {3, 4}, {0, 4}, {}});
        assertEquals(expected2.size(), actual2.size());
        for (int i = 0; i < expected2.size(); i++) {
            assertEquals(expected2.get(i), actual2.get(i));
        }
    }
}
