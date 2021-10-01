import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class networkDelayTime {

    /**
     * You are given a network of n nodes, labeled from 1 to n. You are also given times, a list of travel times as
     * directed edges times[i] = (ui, vi, wi), where ui is the source node, vi is the target node, and wi is the time
     * it takes for a signal to travel from source to target.
     * <p>
     * We will send a signal from a given node k. Return the time it takes for all the n nodes to receive the signal.
     * If it is impossible for all the n nodes to receive the signal, return -1.
     * <p>
     * Constraints:
     * <p>
     * 1 <= k <= n <= 100
     * 1 <= times.length <= 6000
     * times[i].length == 3
     * 1 <= ui, vi <= n
     * ui != vi
     * 0 <= wi <= 100
     * All the pairs (ui, vi) are unique. (i.e., no multiple edges.)
     * <p>
     * Approach 1: DFS
     * 可以用一个hashmap来记录目前为止，到各个节点的最短距离（initialize时候，将距离都赋成MAX_VALUE）。若遍历到某节点时，当前距离小于原来记录的最短距离，就更新
     * 最短距离，同时继续遍历其相邻节点，为保证到达某节点的距离仍未最小，可以以距离从小到大的顺序遍历后续节点。
     * <p>
     * 重点是图的建立，可以构建一个hashmap，以source节点为key，后续信息的list为value，将target节点和time作为一个array存在一个list里，用list的好处是可以对
     * list元素进行排序。
     * <p>
     * Time: O(N^N + ElogE) 每一个节点都有被重复遍历的可能，最坏情况下，每个节点都会被遍历(N - 1)次，对于节点遍历的时间为O(N^N)，对于边，我们对其分别遍历的
     * runtime的上限就是sort整个数组的时间，因为xlogx + ylogy = (x + y)log(x + y)
     * Space: O(N + E) 需要一个map来存储边的信息，同时需要另一个map记录dist信息，dfs的call stack在最坏情况下也需要O(N)
     */
    //需要一个全局变量来存储到每个节点的最短距离
    private Map<Integer, Integer> dist;

    public int networkDelayTimeDFS(int[][] times, int n, int k) {
        //用一个map来记录图的信息，key为source node，value为[target node, time]的数组
        Map<Integer, List<int[]>> edges = new HashMap<>();
        for (int[] time : times) {
            edges.putIfAbsent(time[0], new ArrayList<>());
            edges.get(time[0]).add(new int[]{time[1], time[2]});
        }
        //需要将每个source node的outgoing边进行排序，便于后续遍历
        for (int node : edges.keySet()) {
            //需要对时间进行排序
            Collections.sort(edges.get(node), (a, b) -> (a[1] - b[1]));
        }
        dist = new HashMap<>();
        for (int i = 1; i <= n; i++) {
            //为记录最短距离，将dist map初始化为max value
            dist.put(i, Integer.MAX_VALUE);
        }
        //对图进行dfs，以k为起始点，初始时间为0
        dfs(edges, k, 0);
        int res = 0;
        for (int curr : dist.values()) {
            //若dist map中的value仍有max value，说明该节点未被遍历，return -1
            if (curr == Integer.MAX_VALUE) return -1;
            res = Math.max(res, curr);
        }
        return res;
    }

    private void dfs(Map<Integer, List<int[]>> edges, int start, int elapsed) {
        //若到某一节点的current距离比原记录距离大，则不需要更新
        if (elapsed >= dist.get(start)) return;
        //否则需要更新到当前节点的最短距离
        dist.put(start, elapsed);
        //如果当前节点有outgoing节点，说明可以继续搜索
        if (edges.containsKey(start)) {
            for (int[] edge : edges.get(start)) {
                //对于其相邻节点，继续进行遍历，同时更新目前经过的时间
                dfs(edges, edge[0], elapsed + edge[1]);
            }
        }
    }

    /**
     * Approach 2: Dijkstra's Algorithm
     * Dijkstra算法可以计算从source node到所有target node的最短距离，无需进行dfs重复遍历。可以利用minheap每次从heap中取出下次需要访问的最小距离，dijkstra
     * 算法结束后，会更新source节点到所有节点的最短距离。若有节点无法访问，则返回-1，若图是完全连通的，则返回所有最短距离的最大值。
     * <p>
     * Time: O(N + ElogE) 每个节点需被遍历一遍来更新到该节点的最短距离，同时将元素从heap中取出需要O(logn) time, heap最怀情况下会将所有边都放入其中，upper bound
     * 为O(ElogE)
     * Space: O(N + E) 需要将所有的边存在图中，同时需要一个dist map来记录到每个节点的最短距离。minheap最坏情况需要O(E)的空间存放所有的边
     */
    public int networkDelayTimeDijkstra(int[][] times, int n, int k) {
        // need n + 1 spaces since nodes are 1-indexed
        Map<Integer, Integer>[] graph = new HashMap[n + 1];
        for (int i = 1; i <= n; i++) {
            graph[i] = new HashMap<>();
        }
        for (int[] time : times) {
            graph[time[0]].put(time[1], time[2]);
        }

        //为了保证下次遍历与当前节点距离最小的节点，需要用minheap来存储待遍历节点
        PriorityQueue<int[]> minPQ = new PriorityQueue<>((a, b) -> (Integer.compare(a[1], b[1])));
        //先将起始节点K加入minheap，最短距离为0
        minPQ.add(new int[]{k, 0});
        //建立一个dist map来存储到每个节点的最短距离
        int[] minDistance = new int[n + 1];
        //记录已访问节点
        Set<Integer> visited = new HashSet<>();

        while (!minPQ.isEmpty()) {
            int[] curr = minPQ.poll();
            int node = curr[0], dist = curr[1];
            //Dijkstra算法只遍历每个节点一次更新最短距离，因此若当前节点已被遍历过，则无需继续访问
            if (!visited.contains(node)) {
                visited.add(node);
                //若未遍历，则更新最短距离
                minDistance[node] = dist;

                //搜寻相邻节点并把未访问节点加入minheap
                for (int neighbor : graph[node].keySet()) {
                    if (!visited.contains(neighbor)) {
                        //注意更新最短距离!
                        minPQ.add(new int[]{neighbor, dist + graph[node].get(neighbor)});
                    }
                }
            }
        }

        //若所有节点均被遍历，则dist map应该含有所有的节点最短距离，则找到最短距离中的最大值即为最终结果
        //反之，若有节点未访问，则返回-1
        if (visited.size() != n) return -1;
        int res = 0;
        for (int maxValue : minDistance) {
            res = Math.max(maxValue, res);
        }
        return res;
    }

    @Test
    public void networkDelayTimeTest() {
        /**
         * Example 1:
         * Input: times = [[2,1,1],[2,3,1],[3,4,1]], n = 4, k = 2
         * Output: 2
         *
         *        1
         *  1 <-------- 2
         *              |
         *              | 1
         *              |
         *  4 <-------- 3
         *        1
         */
        int[][] times1 = new int[][]{{2, 1, 1}, {2, 3, 1}, {3, 4, 1}};
        assertEquals(2, networkDelayTimeDFS(times1, 4, 2));
        assertEquals(2, networkDelayTimeDijkstra(times1, 4, 2));
        /**
         * Example 2:
         * Input: times = [[1,2,1]], n = 2, k = 1
         * Output: 1
         */
        int[][] times2 = new int[][]{{1, 2, 1}};
        assertEquals(1, networkDelayTimeDFS(times2, 2, 1));
        assertEquals(1, networkDelayTimeDijkstra(times2, 2, 1));
        /**
         * Example 3:
         * Input: times = [[1,2,1]], n = 2, k = 2
         * Output: -1
         */
        int[][] times3 = new int[][]{{1, 2, 1}};
        assertEquals(-1, networkDelayTimeDFS(times3, 2, 2));
        assertEquals(-1, networkDelayTimeDijkstra(times3, 2, 2));
    }
}
