import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class ConnectingCitiesWithMinimumCost {

    /**
     * There are n cities labeled from 1 to n. You are given the integer n and an array connections where connections[i] =
     * [xi, yi, costi] indicates that the cost of connecting city xi and city yi (bidirectional connection) is costi.
     * <p>
     * Return the minimum cost to connect all the n cities such that there is at least one path between each pair of cities.
     * If it is impossible to connect all the n cities, return -1,
     * <p>
     * The cost is the sum of the connections' costs used.
     * <p>
     * Constraints:
     * <p>
     * 1 <= n <= 10^4
     * 1 <= connections.length <= 10^4
     * connections[i].length == 3
     * 1 <= xi, yi <= n
     * xi != yi
     * 0 <= costi <= 10^5
     * <p>
     * Approach: MST (Kruskal's algorithm)
     * Actually the problem is to find the minimum spanning tree (MST) which connects all nodes with the minimum cost.
     * We could use Union Find data structure to better improve the performance. Basically, we need to sort the connections
     * (edges) by its cost and start from the smallest to the largest. By doing so, we make sure we always connect two nodes
     * with the smallest cost. If there is an edge between two edges that have been connected before, then we just skip that
     * edge cuz it's not the smallest cost. Whenever we have n - 1 unique edges connecting n nodes, we successfully built the
     * MST - return the minimum cost, otherwise, return -1 in the end.
     * <p>
     * Time: O(nlogn) where n is the size of connection arrays, sorting the array takes O(nlogn) time, and for each edge, in
     * the worst case we will union two nodes for every edge. Union will take O(a) which is constant time in practice. Hence,
     * the sorting will dominate the runtime complexity.
     * Space: O(n)
     */
    public int minimumCost(int n, int[][] connections) {
        UnionFind uf = new UnionFind(n);
        // sort the edges by the cost
        Arrays.sort(connections, (a, b) -> (a[2] - b[2]));
        int totalCost = 0, totalEdges = 0;

        for (int[] connection : connections) {
            int node1 = connection[0], node2 = connection[1];
            // not connect two nodes which have been connected
            if (uf.isSameGroup(node1, node2)) continue;
            // otherwise, union these two nodes
            uf.union(node1, node2);
            // sum up the cost
            totalCost += connection[2];
            // increment the number of unique edges
            totalEdges++;
            if (totalEdges == n - 1) return totalCost;
        }
        return -1;
    }

    private static class UnionFind {
        private final int[] size;
        private final int[] parent;

        public UnionFind(int n) {
            // need one more space since node is 1-indexed
            this.size = new int[n + 1];
            this.parent = new int[n + 1];
            Arrays.fill(size, 1);
            for (int i = 1; i <= n; i++) {
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

        public boolean isSameGroup(int i, int j) {
            return find(i) == find(j);
        }
    }

    @Test
    public void minimumCostTest() {
        /**
         * Example 1:
         * Input: n = 3, connections = [[1,2,5],[1,3,6],[2,3,1]]
         * Output: 6
         * Explanation: Choosing any 2 edges will connect all cities so we choose the minimum 2.
         */
        assertEquals(6, minimumCost(3, new int[][]{{1, 2, 5}, {1, 3, 6}, {2, 3, 1}}));
        /**
         * Example 2:
         * Input: n = 4, connections = [[1,2,3],[3,4,4]]
         * Output: -1
         * Explanation: There is no way to connect all cities even if all edges are used.
         */
        assertEquals(-1, minimumCost(4, new int[][]{{1, 2, 3}, {3, 4, 4}}));
    }
}
