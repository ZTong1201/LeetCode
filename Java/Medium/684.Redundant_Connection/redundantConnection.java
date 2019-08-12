import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class redundantConnection {

    /**
     * In this problem, a tree is an undirected graph that is connected and has no cycles.
     *
     * The given input is a graph that started as a tree with N nodes (with distinct values 1, 2, ..., N), with one additional edge added.
     * The added edge has two different vertices chosen from 1 to N, and was not an edge that already existed.
     *
     * The resulting graph is given as a 2D-array of edges. Each element of edges is a pair [u, v] with u < v, that represents an
     * undirected edge connecting nodes u and v.
     *
     * Return an edge that can be removed so that the resulting graph is a tree of N nodes. If there are multiple answers, return the
     * answer that occurs last in the given 2D-array. The answer edge [u, v] should be in the same format, with u < v.
     *
     * Note:
     * The size of the input 2D-array will be between 3 and 1000.
     * Every integer represented in the 2D-array will be between 1 and N, where N is the size of the input array.
     *
     * Approach 1: DFS
     * 本质上，此题的目的是找到多余那条会令图形成环的边。所以可以对于每一条边(u, v)，从source节点u出发，若用DFS遍历其相邻节点，可以达到节点v，说明
     * 必存在另外一条路径连通(u, v)，说明边(u, v)是多余的。
     * 需要注意的是，为了快速查找相邻节点，可以用一个arraylist的数组存储每个节点的相邻节点。同时，因为此问题是无向图，所以对于每条边，要双向地存在数组里。
     * 同时，为了节约遍历时间，只有当起始节点u和终点v都存在相邻节点（即可能存在另一条通路连接u和v时），再运行dfs
     *
     * Time: O(n^2) 最坏情况下，对与每条边，都需要遍历剩余所有的边，才能找到多余的边
     * Space: O(N) 需要一个arraylist数组来存储节点，同时call stack在最坏情况下也需要O(N)空间，同时每次dfs遍历也需要一个hash set来记录已访问节点
     */
    public int[] findRedundantConnectionDFS(int[][] edges) {
        ArrayList<Integer>[] graph = new ArrayList[edges.length + 1];
        for(int i = 1; i <= edges.length; i++) {
            graph[i] = new ArrayList<>();
        }

        int[] res = new int[2];
        //对与每一条边做dfs，判断是否有另一条通路连接两个定点
        for(int[] edge : edges) {
            //因为对每一条边都是全新的dfs，所以每次遍历前都需要建一个新的set记录访问过的节点
            Set<Integer> visited = new HashSet<>();
            //因为是无向图，每条边都会双向地记在graph里
            //为了减少不必要的遍历，只有当两个定点存在相邻节点（即可能存在另一条通路连接两定点时，才做dfs遍历）
            if(!graph[edge[0]].isEmpty() && !graph[edge[1]].isEmpty() && dfs(graph, visited, edge[0], edge[1])) {
                res = edge;
                break;
            }
            //否则，将现在的边双向地加入graph中
            graph[edge[0]].add(edge[1]);
            graph[edge[1]].add(edge[0]);
        }
        return res;
    }

    private boolean dfs(ArrayList<Integer>[] graph, Set<Integer> visited, int source, int target) {
        //为避免重复遍历，只寻找为访问节点
        if(!visited.contains(source)) {
            visited.add(source);
            //若从起始source节点，能够找到另一条通路达到target节点，返回true
            if(source == target) return true;
            //若当前节点没有达到target节点，则dfs访问其相邻节点
            for(int neighbor : graph[source]) {
                if(dfs(graph, visited, neighbor, target)) return true;
            }
        }
        //若遍历结束，未找到通路，返回false
        return false;
    }

    @Test
    public void findRedundantConnectionDFSTest() {
        /**
         * Example 1:
         * Input: [[1,2], [1,3], [2,3]]
         * Output: [2,3]
         * Explanation: The given undirected graph will be like this:
         *   1
         *  / \
         * 2 - 3
         */
        int[][] edges1 = new int[][]{{1, 2}, {1, 3}, {2, 3}};
        int[] expected1 = new int[]{2, 3};
        int[] actual1 = findRedundantConnectionDFS(edges1);
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: [[1,2], [2,3], [3,4], [1,4], [1,5]]
         * Output: [1,4]
         * Explanation: The given undirected graph will be like this:
         * 5 - 1 - 2
         *     |   |
         *     4 - 3
         */
        int[][] edges2 = new int[][]{{1, 2}, {2, 3}, {3, 4}, {1, 4}, {1, 5}};
        int[] expected2 = new int[]{1, 4};
        int[] actual2 = findRedundantConnectionDFS(edges2);
        assertArrayEquals(expected2, actual2);

    }

    /**
     * Approach 2: Union Find
     * 判断是否有多余的边（即是否会形成环），用union find可以快速查找。建立一个union find的类，遍历所有给定的边，若某条边的两个节点其实已经隶属于同一个
     * connected component，说明此条边就是多余的边。对于其他的边，只需将两个节点相连放在同一个connected component中即可。最终返回的就是多余的边
     *
     * Time: O(N*a(N)) 初始化union find需要O(N)时间，union以及判断是否connected只需要O(a(N))时间，一般情况下a(N) < 5，可以视作常数。因此overall
     *      runtime可近似为O(N)
     * Space: O(N), 建立union find需要两个size为N + 1的array来记录parent节点，和当前connected component的size
     */
    public int[] findRedundantConnectionUnionFind(int[][] edges) {
        unionFind uf = new unionFind(edges.length);
        int[] res = new int[2];
        for(int[] edge : edges) {
            if(uf.connected(edge[0], edge[1])) {
                res = edge;
                break;
            } else {
                uf.union(edge[0], edge[1]);
            }
        }
        return res;
    }

    private class unionFind {
        private int[] parent;
        private int[] size;

        public unionFind(int N) {
            this.parent = new int[N + 1];
            this.size = new int[N + 1];
            for(int i = 1; i <= N; i++) {
                this.parent[i] = i;
            }
            Arrays.fill(this.size, 1);
        }

        private int root(int i) {
            while(i != parent[i]) {
                parent[i] = parent[parent[i]];
                i = parent[i];
            }
            return i;
        }

        public boolean connected(int p, int q) {
            return root(p) == root(q);
        }

        public void union(int i, int j) {
            int p = root(i);
            int q = root(j);
            if(p == q) return;
            if(size[p] >= size[q]) {
                size[p] += size[q];
                parent[q] = p;
            } else {
                size[q] += size[p];
                parent[p] = q;
            }
        }
    }

    @Test
    public void findRedundantConnectionUnionFindTest() {
        /**
         * Example 1:
         * Input: [[1,2], [1,3], [2,3]]
         * Output: [2,3]
         * Explanation: The given undirected graph will be like this:
         *   1
         *  / \
         * 2 - 3
         */
        int[][] edges1 = new int[][]{{1, 2}, {1, 3}, {2, 3}};
        int[] expected1 = new int[]{2, 3};
        int[] actual1 = findRedundantConnectionUnionFind(edges1);
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: [[1,2], [2,3], [3,4], [1,4], [1,5]]
         * Output: [1,4]
         * Explanation: The given undirected graph will be like this:
         * 5 - 1 - 2
         *     |   |
         *     4 - 3
         */
        int[][] edges2 = new int[][]{{1, 2}, {2, 3}, {3, 4}, {1, 4}, {1, 5}};
        int[] expected2 = new int[]{1, 4};
        int[] actual2 = findRedundantConnectionUnionFind(edges2);
        assertArrayEquals(expected2, actual2);

    }
}
