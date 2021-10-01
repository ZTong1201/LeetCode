import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import static org.junit.Assert.assertEquals;

public class CheapestFlightsWithinKStops {

    /**
     * There are n cities connected by some number of flights. You are given an array flights where flights[i] =
     * [fromi, toi, pricei] indicates that there is a flight from city fromi to city toi with cost pricei.
     * <p>
     * You are also given three integers src, dst, and k, return the cheapest price from src to dst with at most k stops.
     * If there is no such route, return -1.
     * <p>
     * Constraints:
     * <p>
     * 1 <= n <= 100
     * 0 <= flights.length <= (n * (n - 1) / 2)
     * flights[i].length == 3
     * 0 <= fromi, toi < n
     * fromi != toi
     * 1 <= pricei <= 10^4
     * There will not be any multiple flights between two cities.
     * 0 <= src, dst, k < n
     * src != dst
     * <p>
     * Approach: Dijkstra Algorithm
     * Basically, at each step, we want to greedily pick the smallest price to the next neighbor and keep doing this will
     * guarantee the cheapest price when the destination city is reached. If it's a typical dijkstra problem, then we could
     * just keep an array to record the minimum distance to each city. However, there is one more condition that we can only
     * have no more than k stops in between. In this scenario, the minimum distance to one node may not be the route
     * we're able to take (It might exceed the number of stops restriction if we always take the shortest distance to its
     * neighbor). Therefore, we can also keep track of the minimum stops to each node. By doing so, we will add a new neighbor
     * into the queue
     * 1. If we can obtain a smaller price to the neighbor
     * 2. Or if we can take a smaller number of stops to the neighbor
     * <p>
     * Time: O(n^2 * log(n)) essentially, for each city, we may have to visit all the neighbors every time, and it's bounded
     * by O(n). The size of the queue is approximately O(n) (it can exceed n since a node can be inserted multiple times).
     * Hence, in total we have O(n^2 * log(n))
     * Space: O(n + E)
     */
    public int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {
        // use an array of map to build the graph
        Map<Integer, Integer>[] graph = new HashMap[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new HashMap<>();
        }
        // add edges into the graph
        for (int[] flight : flights) {
            graph[flight[0]].put(flight[1], flight[2]);
        }

        // need two arrays to keep track of:
        // 1. the minimum price to reach each node
        // 2. the number of stops to reach each node
        int[] prices = new int[n], stops = new int[n];
        Arrays.fill(prices, Integer.MAX_VALUE);
        Arrays.fill(stops, Integer.MAX_VALUE);
        // initialize the source city value to 0
        prices[src] = 0;
        stops[src] = 0;

        // use priority queue to store pairs and sort them by prices
        // pair[0] = city id, pair[1] = current price to reach the city, pair[2] = number of stops to reach the city
        PriorityQueue<int[]> minPQ = new PriorityQueue<>((a, b) -> (Integer.compare(a[1], b[1])));
        minPQ.add(new int[]{src, 0, 0});

        while (!minPQ.isEmpty()) {
            int[] curr = minPQ.poll();
            int city = curr[0], currPrice = curr[1], numOfStops = curr[2];

            // with the greediness, we guarantee the price will be the cheapest when the destination is first reached
            if (city == dst) return currPrice;

            // if we have more than k stops so far, stop traversing
            if (numOfStops > k) continue;

            // search all its neighbors
            for (int neighbor : graph[city].keySet()) {
                int nextPrice = graph[city].get(neighbor);

                // update the cheapest price so far and add the neighbor into the queue
                // if we can obtain a smaller price
                if (currPrice + nextPrice < prices[neighbor]) {
                    prices[neighbor] = currPrice + nextPrice;
                    minPQ.add(new int[]{neighbor, currPrice + nextPrice, numOfStops + 1});
                } else if (numOfStops < stops[neighbor]) {
                    // or we can use a smaller number of stops to reach the neighbor
                    // also add it to the queue
                    minPQ.add(new int[]{neighbor, currPrice + nextPrice, numOfStops + 1});
                }
                // always update the number of stops needed
                stops[neighbor] = numOfStops;
            }
        }
        return -1;
    }

    @Test
    public void findCheapestPriceTest() {
        /**
         * Example 1:
         * Input: n = 3, flights = [[0,1,100],[1,2,100],[0,2,500]], src = 0, dst = 2, k = 1
         * Output: 200
         * Explanation: The graph is shown.
         * The cheapest price from city 0 to city 2 with at most 1 stop costs 200
         */
        assertEquals(200, findCheapestPrice(3, new int[][]{{0, 1, 100}, {1, 2, 100}, {0, 2, 500}}, 0, 2, 1));
        /**
         * Example 2:
         * Input: n = 3, flights = [[0,1,100],[1,2,100],[0,2,500]], src = 0, dst = 2, k = 0
         * Output: 500
         * Explanation: The graph is shown.
         * The cheapest price from city 0 to city 2 with at most 0 stop costs 500
         */
        assertEquals(500, findCheapestPrice(3, new int[][]{{0, 1, 100}, {1, 2, 100}, {0, 2, 500}}, 0, 2, 0));
    }
}
