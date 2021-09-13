import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import static org.junit.Assert.assertEquals;

public class ShortestPathVisitingAllNodes {

    /**
     * You have an undirected, connected graph of n nodes labeled from 0 to n - 1. You are given an array graph where
     * graph[i] is a list of all the nodes connected with node i by an edge.
     * <p>
     * Return the length of the shortest path that visits every node. You may start and stop at any node, you may revisit
     * nodes multiple times, and you may reuse edges.
     * <p>
     * Constraints:
     * <p>
     * n == graph.length
     * 1 <= n <= 12
     * 0 <= graph[i].length < n
     * graph[i] does not contain i.
     * If graph[a] contains b, then graph[b] contains a.
     * The input graph is always connected.
     * <p>
     * Approach: Bitmask DP + BFS
     * 因为每个节点，每条边都可以被重复使用，因此原始BFS并不适用。所以需要对在当前节点已遍历的path进行编码，e.g.对于节点0，如果path (0, 1)已经
     * 遍历（即经过path 0 -> 1 -> 0回到节点0）那么下次遍历需要避免继续访问节点1，即(0, 1)path已被遍历。可以使用bitmask解决此类问题，即对于
     * 一个元素个数为n的集合（通常n很小），我们需要选取一个该集合的subset，那么可以使用一个2^n的二进制数来代表某元素是否被选取，e.g.
     * 0 1 1 0
     * 3 2 1 0
     * 对于0110 = 6则代表元素1和2被选取。如何将某元素加入到当前subset当中？ mask | (1 << i)
     * e.g. i = 0
     * 0110 | 0001
     * => 0111代表{0, 1, 2}的subset
     * 对于此问题，定义一个二维数组row = 2^n (n为array的长度 - 节点的个数，最终每个节点都要被选取），col = n（从哪个节点实现当前的subset组合)
     * dp[i][j] 表示从节点j实现一个subset组合所需要的最小距离
     * e.g. dp[3][1] = dp[0011][1] 即上一个节点为节点1实现{0, 1}subset的最短距离。
     * 此时可以引入BFS，即对于当前节点，尝试将其neighbor节点加入mask，然后更新最短距离，若该mask的最短距离已被更新，说明当前路径已被访问，跳过
     * 该路径避免死循环。
     * 同时为了加快BFS进度，可以最开始将所有初始节点加入queue中（即每个节点都可作为起点）再寻找最短路径。
     * <p>
     * Time: O(2^n * n^2) 总共有n * 2^n个state，对于每个节点则有至多n个neighbor，每个neighbor都需要search所有可能的state
     * Space: O(n * 2^n)
     */
    public int shortestPathLength(int[][] graph) {
        int n = graph.length;
        // get the number of bit masks
        int row = (int) Math.pow(2, n);
        int[][] minDistance = new int[row][n];
        // initialize everything as -1 (unvisited)
        for (int i = 0; i < row; i++) {
            Arrays.fill(minDistance[i], -1);
        }
        // add all nodes into the queue as a starting point
        Queue<int[]> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            // add current node into the bit mask (subset of nodes)
            int mask = setBit(0, i);
            int prev = i;
            // initialize the min distance as 0 since it's the beginning of the path
            minDistance[mask][prev] = 0;
            queue.add(new int[]{mask, prev});
        }

        // BFS
        while (!queue.isEmpty()) {
            int size = queue.size();

            for (int i = 0; i < size; i++) {
                int[] curr = queue.poll();
                int mask = curr[0];
                int prev = curr[1];

                // if all nodes have been added to the mask, return the minimum distance so far
                if (mask == row - 1) return minDistance[mask][prev];

                // try to add neighbors into the subset and update the min distance
                for (int neighbor : graph[prev]) {
                    // add neighbor into the mask
                    int newMask = setBit(mask, neighbor);
                    // if that mask has been visited - skip it avoid cycle
                    if (minDistance[newMask][neighbor] != -1) continue;

                    // otherwise, update the minimum distance
                    minDistance[newMask][neighbor] = minDistance[mask][prev] + 1;
                    // add new mask (subsets of visited nodes) into the queue
                    queue.add(new int[]{newMask, neighbor});
                }
            }
        }
        // this statement shall never be reached if the graph is fully connected
        return -1;
    }

    private int setBit(int mask, int i) {
        return mask | (1 << i);
    }

    @Test
    public void shortestPathLengthTest() {
        /**
         * Example 1:
         * Input: graph = [[1,2,3],[0],[0],[0]]
         * Output: 4
         * Explanation: One possible path is [1,0,2,0,3]
         */
        assertEquals(4, shortestPathLength(new int[][]{{1, 2, 3}, {0}, {0}, {0}}));
        /**
         * Example 2:
         * Input: graph = [[1],[0,2,4],[1,3,4],[2],[1,2]]
         * Output: 4
         * Explanation: One possible path is [0,1,4,2,3]
         */
        assertEquals(4, shortestPathLength(new int[][]{{1}, {0, 2, 4}, {1, 3, 4}, {2}, {1, 2}}));
    }
}
