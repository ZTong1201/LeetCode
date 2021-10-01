import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import static org.junit.Assert.assertEquals;

public class PathWithMaximumProbability {

    /**
     * You are given an undirected weighted graph of n nodes (0-indexed), represented by an edge list where edges[i] =
     * [a, b] is an undirected edge connecting the nodes a and b with a probability of success of traversing that edge
     * succProb[i].
     * <p>
     * Given two nodes start and end, find the path with the maximum probability of success to go from start to end and
     * return its success probability.
     * <p>
     * If there is no path from start to end, return 0. Your answer will be accepted if it differs from the correct answer
     * by at most 1e-5.
     * <p>
     * Constraints:
     * <p>
     * 2 <= n <= 10^4
     * 0 <= start, end < n
     * start != end
     * 0 <= a, b < n
     * a != b
     * 0 <= succProb.length == edges.length <= 2*10^4
     * 0 <= succProb[i] <= 1
     * There is at most one edge between every two nodes.
     * <p>
     * Approach: Modified Dijkstra Algorithm
     * Basically, we're trying to an optimal path between start and end, and this can be done by using the priority queue.
     * In the priority queue, the values (nodes) will be sorted the corresponding probability to reach the current node.
     * Starting from the start node, we will compute the probability to reach all the neighbors. If we can get a higher
     * probability, then we assign it to prob[i]. In the next step, we will greedily move to the next node which has the
     * maximum probability, and when the end node is first reached, it's guaranteed that the probability recorded so far
     * is the desired value. If the end node is non-reachable, then return 0.0 in the end.
     * <p>
     * Time: O(Elogn) where n is the number of nodes, E is the number of edges. To build the graph, we need to traverse all
     * the edges, which takes O(E) time. And in the worst case, we have to go through every edge, and if all the nodes are
     * connected to each other, the size of priority queue will be n. Therefore, the total runtime to compute the maximum
     * probability is O(Elogn).
     * Space: O(n + E) we need to build the graph to store E edges, and the size of PQ will be bounded by O(n)
     */
    public double maxProbability(int n, int[][] edges, double[] succProb, int start, int end) {
        // the key part is how to build the graph
        // we can use an array of map, the array index is the from node
        // and the key in the map will be the to node, the corresponding value is the probability
        Map<Integer, Double>[] graph = new HashMap[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new HashMap<>();
        }
        // since the graph is not directed, add edge in both nodes
        for (int i = 0; i < edges.length; i++) {
            int node1 = edges[i][0], node2 = edges[i][1];
            graph[node1].put(node2, succProb[i]);
            graph[node2].put(node1, succProb[i]);
        }

        // create an array to record the maximum probability to reach each node
        double[] maxProb = new double[n];
        // initialize the start point as 1.0 since we need to make multiplication
        maxProb[start] = 1.0;
        // create a priority queue to sort the node by the probability to reach it
        PriorityQueue<Integer> maxPQ = new PriorityQueue<>((a, b) -> (Double.compare(maxProb[b], maxProb[a])));
        // add starting point into the queue
        maxPQ.add(start);

        while (!maxPQ.isEmpty()) {
            int curr = maxPQ.poll();

            // if reach the end node, return the maximum probability recorded so far
            if (curr == end) return maxProb[end];

            // otherwise, update the maximum probability to its neighbors
            // and add those neighbors into the queue
            // next time we'll move to the neighbor which has the highest probability
            for (int neighbor : graph[curr].keySet()) {
                // get the probability from curr to move to the neighbor
                double probToNeighbor = graph[curr].get(neighbor);
                // if the new probability is greater than the previous one
                // update the prob and add that node into the queue
                if (maxProb[curr] * probToNeighbor > maxProb[neighbor]) {
                    maxProb[neighbor] = maxProb[curr] * probToNeighbor;
                    maxPQ.add(neighbor);
                }
            }
        }
        // if the end node is non-reachable, return 0.0 in the end
        return 0.0;
    }

    @Test
    public void maxProbabilityTest() {
        /**
         * Example 1:
         * Input: n = 3, edges = [[0,1],[1,2],[0,2]], succProb = [0.5,0.5,0.2], start = 0, end = 2
         * Output: 0.25000
         * Explanation: There are two paths from start to end, one having a probability of success = 0.2 and the other
         * has 0.5 * 0.5 = 0.25.
         */
        assertEquals(0.25, maxProbability(3, new int[][]{{0, 1}, {1, 2}, {0, 2}},
                new double[]{0.5, 0.5, 0.2}, 0, 2), 1e-5);
        /**
         * Example 2:
         * Input: n = 3, edges = [[0,1],[1,2],[0,2]], succProb = [0.5,0.5,0.3], start = 0, end = 2
         * Output: 0.30000
         */
        assertEquals(0.3, maxProbability(3, new int[][]{{0, 1}, {1, 2}, {0, 2}},
                new double[]{0.5, 0.5, 0.3}, 0, 2), 1e-5);
        /**
         * Example 3:
         * Input: n = 3, edges = [[0,1]], succProb = [0.5], start = 0, end = 2
         * Output: 0.00000
         * Explanation: There is no path between 0 and 2.
         */
        assertEquals(0.0, maxProbability(3, new int[][]{{0, 1}},
                new double[]{0.5}, 0, 2), 1e-5);
    }
}
