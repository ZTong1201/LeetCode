import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class networkDelayTime {

    /**
     * There are N network nodes, labelled 1 to N.
     *
     * Given times, a list of travel times as directed edges times[i] = (u, v, w), where u is the source node, v is the target node,
     * and w is the time it takes for a signal to travel from source to target.
     *
     * Now, we send a signal from a certain node K. How long will it take for all nodes to receive the signal? If it is impossible, return -1.
     *
     * Approach 1: DFS
     * 可以用一个hashmap来记录目前为止，到各个节点的最短距离（initialize时候，将距离都赋成MAX_VALUE）。若遍历到某节点时，当前距离小于原来记录的最短距离，就更新
     * 最短距离，同时继续遍历其相邻节点，为保证到达某节点的距离仍未最小，可以以距离从小到大的顺序遍历后续节点。
     *
     * 重点是图的建立，可以构建一个hashmap，以source节点为key，后续信息的list为value，将target节点和time作为一个array存在一个list里，用list的好处是可以对
     * list元素进行排序。
     *
     * Time: O(N^N + ElogE) 每一个节点都有被重复遍历的可能，最坏情况下，每个节点都会被遍历(N - 1)次，对于节点遍历的时间为O(N^N)，对于边，我们对其分别遍历的
     *      runtime的上限就是sort整个数组的时间，因为xlogx + ylogy = (x + y)log(x + y)
     * Space: O(N + E) 需要一个map来存储边的信息，同时需要另一个map记录dist信息，dfs的call stack在最坏情况下也需要O(N)
     */
    //需要一个全局变量来存储到每个节点的最短距离
    private Map<Integer, Integer> dist;

    public int networkDelayTimeDFS(int[][] times, int N, int K) {
        //用一个map来记录图的信息，key为source node，value为[target node, time]的数组
        Map<Integer, List<int[]>> edges = new HashMap<>();
        for(int[] time : times) {
            edges.putIfAbsent(time[0], new ArrayList<>());
            edges.get(time[0]).add(new int[]{time[1], time[2]});
        }
        //需要将每个source node的outgoing边进行排序，便于后续遍历
        for(int node : edges.keySet()) {
            //需要对时间进行排序
            Collections.sort(edges.get(node), (a, b) -> (a[1] - b[1]));
        }
        dist = new HashMap<>();
        for(int i = 1; i <= N; i++) {
            //为记录最短距离，将dist map初始化为max value
            dist.put(i, Integer.MAX_VALUE);
        }
        //对图进行dfs，以k为起始点，初始时间为0
        dfs(edges, K, 0);
        int res = 0;
        for(int curr : dist.values()) {
            //若dist map中的value仍有max value，说明该节点未被遍历，return -1
            if(curr == Integer.MAX_VALUE) return -1;
            res = Math.max(res, curr);
        }
        return res;
    }

    private void dfs(Map<Integer, List<int[]>> edges, int start, int elapsed) {
        //若到某一节点的current距离比原记录距离大，则不需要更新
        if(elapsed >= dist.get(start)) return;
        //否则需要更新到当前节点的最短距离
        dist.put(start, elapsed);
        //如果当前节点有outgoing节点，说明可以继续搜索
        if(edges.containsKey(start)) {
            for(int[] edge : edges.get(start)) {
                //对于其相邻节点，继续进行遍历，同时更新目前经过的时间
                dfs(edges, edge[0], elapsed + edge[1]);
            }
        }
    }


    @Test
    public void networkDelayTime() {
        /**
         * Example:
         * Input: times = [[2,1,1],[2,3,1],[3,4,1]], N = 4, K = 2
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
        int[][] times = new int[][]{{2, 1, 1}, {2, 3, 1}, {3, 4, 1}};
        assertEquals(2, networkDelayTimeDFS(times, 4, 2));
        assertEquals(2, networkDelayTimeDijkstra(times, 4, 2));

    }

    /**
     * Approach 2: Dijkstra's Algorithm
     * Dijkstra算法可以计算从source node到所有target node的最短距离，无需进行dfs重复遍历。可以利用minheap每次从heap中取出下次需要访问的最小距离，dijkstra
     * 算法结束后，会更新source节点到所有节点的最短距离。若有节点无法访问，则返回-1，若图是完全连通的，则返回所有最短距离的最大值。
     *
     *
     */
    public int networkDelayTimeDijkstra(int[][] times, int N, int K) {
        Map<Integer, List<int[]>> graph = new HashMap<>();
        for(int[] edge : times) {
            graph.putIfAbsent(edge[0], new ArrayList<>());
            graph.get(edge[0]).add(new int[]{edge[1], edge[2]});
        }
        //为了保证下次遍历与当前节点距离最小的节点，需要用minheap来存储待遍历节点
        PriorityQueue<int[]> minPQ = new PriorityQueue<>((a, b) -> (a[1] - b[1]));
        //先将起始节点K加入minheap，最短距离为0
        minPQ.add(new int[]{K, 0});
        //建立一个dist map来存储到每个节点的最短距离
        Map<Integer, Integer> dist = new HashMap<>();
        while(!minPQ.isEmpty()) {
            int[] curr = minPQ.poll();
            int node = curr[0], d = curr[1];
            //Dijkstra算法只遍历每个节点一次更新最短距离，因此若当前节点已被遍历过，则无需继续访问
            if(dist.containsKey(node)) continue;
            //若为遍历，则更新最短距离
            dist.put(node, d);
            //若当前节点存在相邻节点可以继续访问
            if(graph.containsKey(node)) {
                //则将其所有未遍历相邻节点加入minheap
                for(int[] neighbor : graph.get(node)) {
                    //同样只将未遍历节点加入minheap
                    if(!dist.containsKey(neighbor[0])) {
                        //注意更新最短距离！
                        minPQ.add(new int[]{neighbor[0], d + neighbor[1]});
                    }
                }
            }
        }
        //若所有节点均被遍历，则dist map应该含有所有的节点最短距离，则找到最短距离中的最大值即为最终结果
        //反之，若有节点未访问，则返回-1
        if(dist.size() != N) return -1;
        int res = 0;
        for(int maxValue : dist.values()) {
            res = Math.max(maxValue, res);
        }
        return res;
    }
}
