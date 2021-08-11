import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class courseSchedule {

    /**
     * There are a total of n courses you have to take, labeled from 0 to n-1.
     * <p>
     * Some courses may have prerequisites, for example to take course 0 you have to first take course 1, which is expressed as a pair: [0,1]
     * <p>
     * Given the total number of courses and a list of prerequisite pairs, is it possible for you to finish all courses?
     * <p>
     * Note:
     * <p>
     * The input prerequisites is a graph represented by a list of edges, not adjacency matrices.
     * You may assume that there are no duplicate edges in the input prerequisites.
     * <p>
     * Approach 1: Topological Sorting
     * 此题可转化成判断有向图中是否有环，若存在环，则不可能finish all courses，该有向图也就不可能进行拓扑排序。所以可以用DFS对该有向图进行拓扑排序，若任意
     * 时刻在图中发现环则直接return false
     * <p>
     * 每个节点有三种状态，0 - 未遍历， 1 - 正在遍历（其相邻节点）， 2 - 已遍历完成。
     * 可以先将图的edge存放在一个array of arraylist里，array的每一个index代表一个节点，每一个位置的list存放其相邻节点以便进行dfs。遍历每一个节点，若该节点
     * 为遍历过，则先将其标记为visiting节点，然后遍历其相邻节点，然后recursively遍历其更深的邻居节点，若任意时刻又回到该visiting节点，说明图中有环存在，return
     * false。若未发现环，遍历完成后，将其标位visited，继续遍历图中剩余节点。
     * <p>
     * Time: O(V + E) 需要遍历每个节点和每条边各一次
     * Space: O(V + E) 需要将所有的edge存放在一个array of arraylist里(O(E))，同时需要一个integer array来记录各节点状态(O(V))
     */
    public boolean canFinishTopologicalSorting(int numCourses, int[][] prerequisites) {
        //需要一个array of arraylist来记录所有edge
        ArrayList<Integer>[] edges = new ArrayList[numCourses];
        for (int i = 0; i < numCourses; i++) {
            edges[i] = new ArrayList<>();
        }
        for (int[] edge : prerequisites) {
            //边的指向顺序其实并不重要
            edges[edge[1]].add(edge[0]);
        }

        //需要一个integer array来记录节点遍历状态
        //0 - unvisited, 1 - visiting, 2 - visited
        int[] visit = new int[numCourses];
        for (int i = 0; i < numCourses; i++) {
            //dfs遍历所有节点和边，若图中有环则直接返回false
            if (hasCycle(i, edges, visit)) return false;

        }
        return true;
    }

    //判断有向图中是否有环（同时可以进行拓扑排序）
    private boolean hasCycle(int curr, ArrayList<Integer>[] edges, int[] visit) {
        //如果curr节点正在遍历中，说明发现环
        if (visit[curr] == 1) return true;
        //若curr节点已遍历结束，则未发现环，需要继续遍历后续节点
        if (visit[curr] == 2) return false;

        //若上述条件不符，说明该节点未遍历，将其标为正在遍历节点（1），继续遍历其相邻节点
        visit[curr] = 1;
        for (int neighbor : edges[curr]) {
            //递归调用该函数，判断其相邻节点是否能发现环
            //若发现环，return true，若未发现，则继续遍历
            if (hasCycle(neighbor, edges, visit)) return true;
        }
        //遍历结束后，将curr节点标为已遍历节点（2）
        visit[curr] = 2;
        //若需要拓扑排序，可将curr节点插在result list的首端
        return false;
    }

    /**
     * Approach 2: Pure DFS
     * 此题可以直接用DFS求解，对于每一个节点，都用一个新的boolean array来记录以此节点为起点的遍历情况，若能发现环，则return false
     * 此题的base case有如下几种情况
     * 1.若dfs第二次访问至起始节点，则说明有环（因此我们需要在后续递归调用判断函数之前将要遍历节点记录为true）
     * 2.若dfs遍历至终止节点（无出度）或该节点为孤立节点，则说明无环
     * <p>
     * 对于一个节点的所有邻居，为避免重复遍历，只访问未遍历节点。
     * <p>
     * Time: O(V^2) = O(n^2) 最坏情况，对于任意节点，我们都需要访问至剩余所有节点才能判断是否有环
     * Time: O(V + E), 需要记录所有的边，同时对于任意节点，遍历开始前要建立一个boolean array记录遍历情况
     */
    public boolean canFinishDFS(int numCourses, int[][] prerequisites) {
        ArrayList<Integer>[] edges = new ArrayList[numCourses];
        for (int[] edge : prerequisites) {
            if (edges[edge[1]] == null) edges[edge[1]] = new ArrayList<>();
            edges[edge[1]].add(edge[0]);
        }

        for (int i = 0; i < numCourses; i++) {
            //每次遍历开始前需要一个新的boolean array记录此次遍历情况
            boolean[] visit = new boolean[numCourses];
            //若有环，return false
            if (hasCycle(i, i, edges, visit)) return false;
        }
        return true;
    }

    private boolean hasCycle(int start, int curr, ArrayList<Integer>[] edges, boolean[] visit) {
        //若dfs第二次回到起始点，说明有环
        if (curr == start && visit[start]) return true;
        //若访问至末尾节点，或访问一个孤立节点，说明无环
        if (edges[curr] == null) return false;

        for (int neighbor : edges[curr]) {
            //为避免重复遍历，访问起始节点的尚未访问邻居节点
            if (visit[neighbor]) continue;
            //进行递归调用前，将当前节点标记为已遍历
            visit[neighbor] = true;
            //递归搜索邻居节点，若找到环，return true
            if (hasCycle(start, neighbor, edges, visit)) return true;
        }
        return false;
    }

    @Test
    public void canFinishTest() {
        /**
         * Example 1:
         * Input: 2, [[1,0]]
         * Output: true
         * Explanation: There are a total of 2 courses to take.
         *              To take course 1 you should have finished course 0. So it is possible.
         */
        int[][] prerequisite1 = new int[][]{{1, 0}};
        assertTrue(canFinishTopologicalSorting(2, prerequisite1));
        assertTrue(canFinishDFS(2, prerequisite1));
        /**
         * Example 2:
         * Input: 2, [[1,0],[0,1]]
         * Output: false
         * Explanation: There are a total of 2 courses to take.
         *              To take course 1 you should have finished course 0, and to take course 0 you should
         *              also have finished course 1. So it is impossible.
         */
        int[][] prerequisite2 = new int[][]{{1, 0}, {0, 1}};
        assertFalse(canFinishTopologicalSorting(2, prerequisite2));
        assertFalse(canFinishDFS(2, prerequisite2));
    }
}
