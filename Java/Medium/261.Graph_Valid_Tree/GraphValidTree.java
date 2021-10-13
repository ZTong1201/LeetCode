import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GraphValidTree {

    /**
     * You have a graph of n nodes labeled from 0 to n - 1. You are given an integer n and a list of edges where edges[i] =
     * [ai, bi] indicates that there is an undirected edge between nodes ai and bi in the graph.
     * <p>
     * Return true if the edges of the given graph make up a valid tree, and false otherwise.
     * <p>
     * Constraints:
     * <p>
     * 1 <= 2000 <= n
     * 0 <= edges.length <= 5000
     * edges[i].length == 2
     * 0 <= ai, bi < n
     * ai != bi
     * There are no self-loops or repeated edges.
     * <p>
     * Approach: Union Find
     * The key part of this problem is the definition of a tree. For a given undirected graph, the graph is a tree if and only
     * if the graph is fully connected and there is no cycle in the graph. There are multiple algorithms to detect cycle and
     * get the number of connected components. The union find data structure is designed to resolve a problem like this.
     * We can union two nodes given an edge and if these two nodes have been connected together, then there must have been
     * a cycle - return false directly. If a cycle is not detected, in the end, we must check whether the graph is fully connected,
     * i.e. there is only one connected component in the graph.
     * <p>
     * Performance boosting:
     * Actually, if the graph (for n nodes) is a tree, there must be n - 1 edges in the graph. With less, the graph cannot
     * be connected. With more, there must be a redundant edge which lead to an edge. We can first check whether the length
     * of edges is n - 1 and then start detecting the cycle.
     * <p>
     * Time: O(E * a(n)) for n nodes, the union operation will take O(a(n)) which is approximately constant time. For E edges,
     * we need to do O(E) union operations in total.
     * Space: O(n)
     */
    public boolean validTree(int n, int[][] edges) {
        // denote the graph as union find
        UnionFind graph = new UnionFind(n);
        for (int[] edge : edges) {
            // try to connect two nodes, if two nodes have been connected, return false
            if (!graph.union(edge[0], edge[1])) return false;
        }
        // finally, check whether the graph is fully connected
        return graph.getNumOfConnectedComponent() == 1;
    }

    private static class UnionFind {
        private final int[] parent;
        private final int[] size;

        public UnionFind(int n) {
            this.parent = new int[n];
            this.size = new int[n];
            Arrays.fill(this.size, 1);
            for (int i = 0; i < n; i++) {
                this.parent[i] = i;
            }
        }

        private int find(int i) {
            while (i != parent[i]) {
                parent[i] = parent[parent[i]];
                i = parent[i];
            }
            return i;
        }

        public boolean union(int i, int j) {
            int p = find(i), q = find(j);
            // if two nodes are already connected - then there is a cycle
            if (p == q) return false;
            if (size[p] >= size[q]) {
                parent[q] = p;
                size[p] += size[q];
            } else {
                parent[p] = q;
                size[q] += size[p];
            }
            return true;
        }

        private int getNumOfConnectedComponent() {
            int count = 0;
            for (int i = 0; i < parent.length; i++) {
                if (parent[i] == i) count++;
            }
            return count;
        }
    }

    @Test
    public void validTreeTest() {
        /**
         * Example 1:
         * Input: n = 5, edges = [[0,1],[0,2],[0,3],[1,4]]
         * Output: true
         */
        assertTrue(validTree(5, new int[][]{{0, 1}, {0, 2}, {0, 3}, {1, 4}}));
        /**
         * Example 2:
         * Input: n = 5, edges = [[0,1],[1,2],[2,3],[1,3],[1,4]]
         * Output: false
         */
        assertFalse(validTree(5, new int[][]{{0, 1}, {1, 2}, {2, 3}, {1, 3}, {1, 4}}));
    }
}
