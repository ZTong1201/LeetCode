import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MinimumTimeToCollectAllApples {

    /**
     * Given an undirected tree consisting of n vertices numbered from 0 to n-1, which has some apples in their vertices.
     * You spend 1 second to walk over one edge of the tree. Return the minimum time in seconds you have to spend to collect
     * all apples in the tree, starting at vertex 0 and coming back to this vertex.
     * <p>
     * The edges of the undirected tree are given in the array edges, where edges[i] = [ai, bi] means that exists an edge
     * connecting the vertices ai and bi. Additionally, there is a boolean array hasApple, where hasApple[i] = true means
     * that vertex i has an apple; otherwise, it does not have any apple.
     * <p>
     * Constraints:
     * <p>
     * 1 <= n <= 10^5
     * edges.length == n - 1
     * edges[i].length == 2
     * 0 <= ai < bi <= n - 1
     * fromi < toi
     * hasApple.length == n
     * <p>
     * Approach: DFS
     * Basically, if the child of current node has apples, then the minimum time between two nodes will always be 2 since
     * we need to move forward to the child then move backward. Therefore, we can use DFS to recursively return the min time
     * to collect all apples from all the children and append the deeper level result to an upper level node. There are some
     * edge cases we need to be careful:
     * 1. If there are apples at the root node, we don't need to add any time, the effort will always be 0
     * 2. If the current node has apples, we always need to add 2 and return to its parent node
     * 3. If the current node doesn't have apples, but its children might have, in that case, the min time returned from
     * the children will be greater than 0. We also need to add 2 and return to its parent node.
     * <p>
     * Time: O(n) each node will be visited once
     * Space: O(n) need an array of list to keep track of all edges, since the edges are bi-directional, we need O(2n) edges.
     * Also need a boolean array of size n to mark nodes as visited.
     */
    private boolean[] visited;
    private ArrayList<Integer>[] graph;

    public int minTime(int n, int[][] edges, List<Boolean> hasApple) {
        buildGraph(n, edges);
        visited = new boolean[n];

        // recursively compute the min time to collect all apples from the children
        return computeTime(hasApple, 0);
    }

    private int computeTime(List<Boolean> hasApple, int node) {
        // edge case 1: if hit a visited node, don't traverse again, return 0
        if (visited[node]) return 0;
        // mark as visited
        visited[node] = true;

        int costAtCurrentNode = 0;
        // DFS all children
        for (int neighbor : graph[node]) {
            // in order to compute the time needed for current node
            // we need to recursively calculate the time for all branches
            costAtCurrentNode += computeTime(hasApple, neighbor);
        }

        // we only need to add 2 (forward and backward) when
        // 1. the current node has apples or
        // 2. any of its children has apples (the time must be greater than 0)
        // and the current node is not the root node
        if ((costAtCurrentNode > 0 || hasApple.get(node)) && node != 0) costAtCurrentNode += 2;
        return costAtCurrentNode;
    }

    private void buildGraph(int n, int[][] edges) {
        graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        // need bi-directional edges in case we cannot traverse the entire tree
        // e.g. [[0,2], [0,3], [1,2]]
        for (int[] edge : edges) {
            graph[edge[0]].add(edge[1]);
            graph[edge[1]].add(edge[0]);
        }
    }

    @Test
    public void minTimeTest() {
        /**
         * Example 1:
         * Input: n = 7, edges = [[0,1],[0,2],[1,4],[1,5],[2,3],[2,6]], hasApple = [false,false,true,false,true,true,false]
         * Output: 8  
         */
        assertEquals(8, minTime(7, new int[][]{{0, 1}, {0, 2}, {1, 4}, {1, 5}, {2, 3}, {2, 6}},
                List.of(false, false, true, false, true, true, false)));
        /**
         * Example 2:
         * Input: n = 7, edges = [[0,1],[0,2],[1,4],[1,5],[2,3],[2,6]], hasApple = [false,false,true,false,false,true,false]
         * Output: 6
         */
        assertEquals(6, minTime(7, new int[][]{{0, 1}, {0, 2}, {1, 4}, {1, 5}, {2, 3}, {2, 6}},
                List.of(false, false, true, false, false, true, false)));
        /**
         * Example 3:
         * Input: n = 7, edges = [[0,1],[0,2],[1,4],[1,5],[2,3],[2,6]], hasApple = [false,false,false,false,false,false,false]
         * Output: 0
         */
        assertEquals(0, minTime(7, new int[][]{{0, 1}, {0, 2}, {1, 4}, {1, 5}, {2, 3}, {2, 6}},
                List.of(false, false, false, false, false, false, false)));
    }
}
