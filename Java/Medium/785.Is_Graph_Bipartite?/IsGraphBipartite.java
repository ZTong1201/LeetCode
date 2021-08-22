import org.junit.Test;

import java.util.Stack;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class IsGraphBipartite {

    /**
     * Given an undirected graph, return true if and only if it is bipartite.
     * <p>
     * Recall that a graph is bipartite if we can split it's set of nodes into two independent subsets A and B such that
     * every edge in the graph has one node in A and another node in B.
     * <p>
     * The graph is given in the following form: graph[i] is a list of indexes j for which the edge between nodes i and j exists.
     * Each node is an integer between 0 and graph.length - 1.  There are no self edges or parallel edges: graph[i] does not contain i,
     * and it doesn't contain any element twice.
     * <p>
     * Note:
     * <p>
     * graph will have length in range [1, 100].
     * graph[i] will contain integers in range [0, graph.length - 1].
     * graph[i] will not contain i or duplicate values.
     * The graph is undirected: if any element j is in graph[i], then i will be in graph[j].
     * <p>
     * Approach: Coloring nodes (DFS or BFS)
     * 确定一个图是否是bipartite，可以利用染色法（如红色和蓝色）。对于任意一个节点，可能有三种状态(0：为染色，1：红色，-1：蓝色），用1和-1可以更容易的在
     * 两个颜色之间切换。对于当前节点，若其位蓝色（-1），则它的所有相邻未染色节点（0）都必须染成红色（1）。对于红色节点染色过程正好相反。如果对于任意一个节点，
     * 其颜色与其相邻节点颜色相同，说明该图不是bipartite。
     * <p>
     * 染色过程可以用DFS（用stack）或者BFS（用queue）来记录接下来要遍历的节点。需要主要的是图中可能存在disconnected节点，因此需要for loop来遍历所有节点
     * <p>
     * Time: O(V + E) 需要遍历所有的节点和edge一次来进行染色
     * Space: O(V) 需要一个color array来记录染色结果，和一个stack来记录即将遍历的节点(O(V) space in the worst case)
     */
    public boolean isBipartite(int[][] graph) {
        Stack<Integer> stack = new Stack<>();
        int[] colors = new int[graph.length];

        for (int i = 0; i < graph.length; i++) {
            //只需继续遍历未染色节点，保证每个节点遍历一次，不会重复染色
            if (colors[i] == 0) {
                //染成红色
                colors[i] = 1;
                stack.push(i);

                while (!stack.isEmpty()) {
                    int curr = stack.pop();
                    //对于现在的节点，访问其为染色(colors[i] == 0)相邻节点并进行染色和压栈
                    for (int neighbor : graph[curr]) {
                        if (colors[neighbor] == 0) {
                            //染成相反颜色
                            colors[neighbor] = -colors[curr];
                            //将相邻节点压栈，做DFS访问
                            stack.push(neighbor);
                        } else if (colors[neighbor] == colors[curr]) {
                            //若相邻已染色节点和现节点颜色相同，说明该图不是bipartite
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    @Test
    public void isBipartiteTest() {
        /**
         * Example 1:
         * Input: [[1,3], [0,2], [1,3], [0,2]]
         * Output: true
         * Explanation:
         * The graph looks like this:
         * 0----1
         * |    |
         * |    |
         * 3----2
         * We can divide the vertices into two groups: {0, 2} and {1, 3}.
         */
        int[][] graph1 = new int[][]{{1, 3}, {0, 2}, {1, 3}, {0, 2}};
        assertTrue(isBipartite(graph1));
        /**
         * Example 2:
         * Input: [[1,2,3], [0,2], [0,1,3], [0,2]]
         * Output: false
         * Explanation:
         * The graph looks like this:
         * 0----1
         * | \  |
         * |  \ |
         * 3----2
         * We cannot find a way to divide the set of nodes into two independent subsets.
         */
        int[][] graph2 = new int[][]{{1, 2, 3}, {0, 2}, {0, 1, 3}, {0, 2}};
        assertFalse(isBipartite(graph2));
        /**
         * Example 3:
         * Input: [[1], [0,3], [3], [1,2]]
         * Output: true;
         * The graph looks like this:
         * 0----1
         *    /
         *   /
         * 3----2
         * We can divide the vertices into two groups: {0, 3}, {1, 2}
         */
        int[][] graph3 = new int[][]{{1}, {0, 3}, {3}, {1, 2}};
        assertTrue(isBipartite(graph3));
    }
}
