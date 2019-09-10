import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class removeStones {

    /**
     * On a 2D plane, we place stones at some integer coordinate points.  Each coordinate point may have at most one stone.
     *
     * Now, a move consists of removing a stone that shares a column or row with another stone on the grid.
     *
     * What is the largest possible number of moves we can make?
     *
     * Note:
     *
     * 1 <= stones.length <= 1000
     * 0 <= stones[i][j] < 10000
     *
     * Approach 1: DFS
     * 本质上，就是将所有行或者列相同的stones看成一个connected component，对于一个给定的component，总可以不断的remove stone，直到该component只剩1个stone
     * 因此，最终总的可以remove的stone数量就是每个component的size - 1的和。即每个component的size和（即为stone总数）- component的数量。因此问题转化为
     * 给定的数组中有多少connected component。对于一个给定的位置，可以直接用DFS遍历所有行或列相连的其他stones，将其加入visited的set中，直到所有节点均被
     * 遍历一遍后，可以得到connected component的总数
     *
     * Time: O(n^2) 对于一个给定节点，需要判断输入的其他节点与之是否行或列相连
     * Space: O(n) 需要将所有的节点放入一个hash set
     */
    public int removeStonesDFS(int[][] stones) {
        Set<int[]> visited = new HashSet<>();
        int numOfComponents = 0;
        for (int[] stone : stones) {
            //若当前节点尚未遍历，则说明找到一个新的component，然后将其所有相连节点遍历一遍，放入visited中
            if (!visited.contains(stone)) {
                dfs(stone, stones, visited);
                numOfComponents++;
            }
        }
        return stones.length - numOfComponents;
    }

    private void dfs(int[] curr, int[][] stones, Set<int[]> visited) {
        visited.add(curr);
        for (int[] nei : stones) {
            //首先保证不会重复遍历
            if (!visited.contains(nei)) {
                //同时，只将行或列相同的节点放入visited中
                if (curr[0] == nei[0] || curr[1] == nei[1]) {
                    dfs(nei, stones, visited);
                }
            }
        }
    }

    /**
     * Approach 2: Union Find
     * 若想在接近线性时间内找到connected component，可以考虑使用union find。因为输入的位置坐标一定在[0, 10000)之间，因此可以创建一个size为20000的union
     * find，前10000个位置代表每一行，后10000个位置代表每一列。因此对于输入的任何节点，只需将其行和列进行union即可。最后，在所有输入的节点中找到根节点的个数
     * 即为connected component的数量
     *
     * Time: O(n*a(n)) 因为需要对所有的输入节点进行union操作，同时最后又要进行一遍find操作，查找该节点的parent节点。
     * Space: O(1) 因为无论输入数组size为多少，都只需要size为20000的union find
     */
    public int removeStonesUnionFind(int[][] stones) {
        unionFind uf = new unionFind();
        //对于输入的位置坐标，需要将行和列在union find中的节点相连
        for(int[] stone : stones) {
            //注意前10000个是行坐标的节点位置，后10000个是列坐标位置
            uf.union(stone[0], stone[1] + 10000);
        }
        //利用hash set记录根节点数量即为connected component的数量
        Set<Integer> parents = new HashSet<>();
        for(int[] stone : stones) {
            parents.add(uf.find(stone[0]));
        }
        return stones.length - parents.size();
    }

    private class unionFind {
        private int[] size;
        private int[] parent;

        public unionFind() {
            this.size = new int[20000];
            this.parent = new int[20000];
            for (int i = 0; i < 20000; i++) {
                this.parent[i] = i;
            }
            Arrays.fill(this.size, 1);
        }

        public int find(int i) {
            while (i != parent[i]) {
                parent[i] = parent[parent[i]];
                i = parent[i];
            }
            return i;
        }

        public void union(int i, int j) {
            int p = find(i);
            int q = find(j);
            if (p == q) return;
            if (size[p] >= size[q]) {
                size[p] += size[q];
                parent[q] = p;
            } else {
                size[q] += size[p];
                parent[p] = q;
            }
        }
    }

    @Test
    public void removeStonesTest() {
        /**
         * Example 1:
         * Input: stones = [[0,0],[0,1],[1,0],[1,2],[2,1],[2,2]]
         * Output: 5
         */
        int[][] stones1 = new int[][]{{0, 0}, {0, 1}, {1, 0}, {1, 2}, {2, 1}, {2, 2}};
        assertEquals(5, removeStonesDFS(stones1));
        assertEquals(5, removeStonesUnionFind(stones1));
        /**
         * Example 2:
         * Input: stones = [[0,0],[0,2],[1,1],[2,0],[2,2]]
         * Output: 3
         */
        int[][] stones2 = new int[][]{{0, 0}, {0, 2}, {1, 1,}, {2, 0}, {2, 2}};
        assertEquals(3, removeStonesDFS(stones2));
        assertEquals(3, removeStonesUnionFind(stones2));
        /**
         * Example 3:
         * Input: stones = [[0,0]]
         * Output: 0
         */
        int[][] stones3 = new int[][]{{0, 0}};
        assertEquals(0, removeStonesDFS(stones3));
        assertEquals(0, removeStonesUnionFind(stones3));
    }
}
