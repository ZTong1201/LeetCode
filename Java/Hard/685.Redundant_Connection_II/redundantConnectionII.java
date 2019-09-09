import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class redundantConnectionII {

    /**
     * In this problem, a rooted tree is a directed graph such that, there is exactly one node (the root) for which all other nodes are
     * descendants of this node, plus every node has exactly one parent, except for the root node which has no parents.
     *
     * The given input is a directed graph that started as a rooted tree with N nodes (with distinct values 1, 2, ..., N), with one
     * additional directed edge added. The added edge has two different vertices chosen from 1 to N, and was not an edge that already
     * existed.
     *
     * The resulting graph is given as a 2D-array of edges. Each element of edges is a pair [u, v] that represents a directed edge
     * connecting nodes u and v, where u is a parent of child v.
     *
     * Return an edge that can be removed so that the resulting graph is a rooted tree of N nodes. If there are multiple answers,
     * return the answer that occurs last in the given 2D-array.
     *
     * Note:
     * The size of the input 2D-array will be between 3 and 1000.
     * Every integer represented in the 2D-array will be between 1 and N, where N is the size of the input array.
     *
     * Approach: Union Find
     * 本质上，对于N个节点的树，若其中存在N条边，其总共可能出现三种错误情况：
     * 1. 整个图中存在环，即可能从某个child再回到parent
     * 2. 某一个节点存在两个parent
     * 3. 或上述两种情况均出现
     * 对于所有节点都只有一个parent的情况，那么图中一定是存在环。对于此种情况可以直接通过union find，将每条边的两个端点连接起来，如果某一条的两个端点已经
     * 在图中连接起来，说明该条边即是多余边，可直接返回。
     *
     * 对于某个节点存在两个parent的情况，图中可能存在环也可能不存在环，所以可以先将同一child不同parent的两条边记录下来，对剩下的边进行union find操作，然后
     * 将edge1加入图中，若加入edge1会产生环，说明edge1为多余边。否则，可以直接返回edge2
     *
     * Time: O(n*a(n)) 对于所有的边都要进行union，和判断是否相连的操作
     * Space: O(n) 需要将所有的节点记录下来，用来执行union find操作
     */
    public int[] findRedundantDirectedConnection(int[][] edges) {
        int N = edges.length;
        //记录每条边的source node（即parent节点）
        ArrayList<Integer>[] parents = new ArrayList[N + 1];
        for(int i = 1; i <= N; i++) {
            parents[i] = new ArrayList<>();
        }
        //同时记录下潜在的multiple parent的child节点的两条边，若图中不存在这样的child，edge1和edge2仍然会是null
        int[] edge1 = null, edge2 = null;
        //先遍历一遍所有的边，记录下multiple parent的两条边
        for(int[] edge : edges) {
            //若某条边的终点已经被记录过parent节点，说明该节点存在multiple parent，将之前记录的边和当前边记录下来即可
            if(parents[edge[1]].size() == 1) {
                edge1 = new int[]{parents[edge[1]].get(0), edge[1]};
                edge2 = edge;
                break;
            }
            parents[edge[1]].add(edge[0]);
        }
        //然后对除去edge1个edge2（若存在）的所有边进行union find操作，寻找图中的环
        unionFind uf = new unionFind(N);
        for(int[] edge : edges) {
            if((edge1 != null) && ((edge[0] == edge1[0] && edge[1] == edge1[1]) || (edge[0] == edge2[0] && edge[1] == edge2[1]))) {
                continue;
            }
            //若某条边的两个节点已经连接在一起，加入该条边会造成环的存在，可以直接返回当前边
            if(uf.isConnected(edge[0], edge[1])) {
                return edge;
            }
            //否则将两节点连接起来
            uf.union(edge[0], edge[1]);
        }
        //若图中尚不存在环，说明可能是multiple parent带来的多余边
        if(edge1 != null) {
            //判断将edge1加入图中是否会造成环，若能造成环，该条边为多余边
            if(uf.isConnected(edge1[0], edge1[1])) {
                return edge1;
            }
        }
        //否则说明edge2一定是多余边
        return edge2;
    }

    private class unionFind {
        private int[] size;
        private int[] parent;

        public unionFind(int N) {
            this.size = new int[N + 1];
            this.parent = new int[N + 1];
            Arrays.fill(this.size, 1);
            for(int i = 1;  i <= N; i++) {
                this.parent[i] = i;
            }
        }

        private int find(int i) {
            while(i != parent[i]) {
                parent[i] = parent[parent[i]];
                i = parent[i];
            }
            return i;
        }

        public void union(int i, int j) {
            int p = find(i);
            int q = find(j);
            if(p == q) return;
            if(size[p] >= size[q]) {
                size[p] += size[q];
                parent[q] = p;
            } else {
                size[q] += size[p];
                parent[p] = q;
            }
        }

        public boolean isConnected(int i, int j) {
            return find(i) == find(j);
        }
    }

    @Test
    public void findRedundantDirectedConnectionTest() {
        /**
         * Example 1:
         * Input: [[1,2], [1,3], [2,3]]
         * Output: [2,3]
         * Explanation: The given directed graph will be like this:
         *   1
         *  / \
         * v   v
         * 2-->3
         */
        int[][] edges1 = new int[][]{{1, 2}, {1, 3}, {2, 3}};
        int[] expected1 = new int[]{2, 3};
        int[] actual1 = findRedundantDirectedConnection(edges1);
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: [[1,2], [2,3], [3,4], [4,1], [1,5]]
         * Output: [4,1]
         * Explanation: The given directed graph will be like this:
         * 5 <- 1 -> 2
         *      ^    |
         *      |    v
         *      4 <- 3
         */
        int[][] edges2 = new int[][]{{1, 2}, {2, 3}, {3, 4}, {4, 1}, {1, 5}};
        int[] expected2 = new int[]{4, 1};
        int[] actual2 = findRedundantDirectedConnection(edges2);
        assertArrayEquals(expected2, actual2);
    }
}
