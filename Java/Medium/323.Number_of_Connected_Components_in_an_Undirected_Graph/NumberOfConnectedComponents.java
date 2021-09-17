import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class NumberOfConnectedComponents {

    /**
     * You have a graph of n nodes. You are given an integer n and an array edges where edges[i] = [ai, bi] indicates that
     * there is an edge between ai and bi in the graph.
     * <p>
     * Return the number of connected components in the graph.
     * <p>
     * Constraints:
     * <p>
     * 1 <= n <= 2000
     * 1 <= edges.length <= 5000
     * edges[i].length == 2
     * 0 <= ai <= bi < n
     * ai != bi
     * There are no repeated edges.
     * <p>
     * Approach 1: DFS
     * We can build a graph using the edges array and use each node as a starting point and mark all the connected nodes
     * as visited. If we visit an unvisited node - which means we have a new connected component to be searched. We'll get
     * the number of total connected components after the entire graph traversal is done.
     * <p>
     * Time: O(n) each node will be visited once
     * Space: O(n) the call stack will take up to O(n) space + we need an array of list to build the graph
     */
    public int countComponentsDFS(int n, int[][] edges) {
        int count = 0;
        ArrayList<Integer>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int[] edge : edges) {
            // add edge in both nodes - since the graph is undirected
            graph[edge[0]].add(edge[1]);
            graph[edge[1]].add(edge[0]);
        }

        boolean[] visited = new boolean[n];
        // use any nodes as a starting point to connect with its neighbors
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                count++;
                dfs(i, visited, graph);
            }
        }
        return count;
    }

    private void dfs(int index, boolean[] visited, ArrayList<Integer>[] graph) {
        if (visited[index]) return;
        visited[index] = true;
        for (int neighbor : graph[index]) {
            dfs(neighbor, visited, graph);
        }
    }

    /**
     * Approach 2: Union Find
     * We can union two nodes on a given edge and finally returned the number of connected components.
     * <p>
     * Time: O(L) where L is the length of the edges array. For each union call, it takes O(a) (which is approximately constant
     * time in practice) with path compression and union by size
     * Space: O(n)
     */
    public int countComponentsUnionFind(int n, int[][] edges) {
        UnionFind uf = new UnionFind(n);
        for (int[] edge : edges) {
            uf.union(edge[0], edge[1]);
        }
        return uf.numOfConnectedComponents();
    }

    private static class UnionFind {
        private final int[] size;
        private final int[] parent;

        public UnionFind(int n) {
            this.size = new int[n];
            this.parent = new int[n];
            Arrays.fill(size, 1);
            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
        }

        private int find(int i) {
            while (i != parent[i]) {
                parent[i] = parent[parent[i]];
                i = parent[i];
            }
            return i;
        }

        public void union(int i, int j) {
            int p = find(i), q = find(j);
            if (p == q) return;
            if (size[p] >= size[q]) {
                parent[q] = p;
                size[p] += size[q];
            } else {
                parent[p] = q;
                size[q] += size[p];
            }
        }

        public int numOfConnectedComponents() {
            int count = 0;
            for (int i = 0; i < parent.length; i++) {
                if (parent[i] == i) count++;
            }
            return count;
        }
    }

    @Test
    public void countComponentsTest() {
        /**
         * Example 1:
         * Input: n = 5, edges = [[0,1],[1,2],[3,4]]
         * Output: 2
         */
        assertEquals(2, countComponentsDFS(5, new int[][]{{0, 1}, {1, 2}, {3, 4}}));
        assertEquals(2, countComponentsUnionFind(5, new int[][]{{0, 1}, {1, 2}, {3, 4}}));
        /**
         * Example 2:
         * Input: n = 5, edges = [[0,1],[1,2],[2,3],[3,4]]
         * Output: 1
         */
        assertEquals(1, countComponentsDFS(5, new int[][]{{0, 1}, {1, 2}, {2, 3}, {3, 4}}));
        assertEquals(1, countComponentsUnionFind(5, new int[][]{{0, 1}, {1, 2}, {2, 3}, {3, 4}}));
    }
}
