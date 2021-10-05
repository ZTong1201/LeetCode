import org.junit.Test;

import java.util.PriorityQueue;

import static org.junit.Assert.assertEquals;

public class MinimumNumberOfRefuelingStops {

    /**
     * A car travels from a starting position to a destination which is target miles east of the starting position.
     * <p>
     * There are gas stations along the way. The gas stations are represented as an array stations where stations[i] =
     * [positioni, fueli] indicates that the ith gas station is positioni miles east of the starting position and has fueli
     * liters of gas.
     * <p>
     * The car starts with an infinite tank of gas, which initially has startFuel liters of fuel in it. It uses one liter
     * of gas per one mile that it drives. When the car reaches a gas station, it may stop and refuel, transferring all the
     * gas from the station into the car.
     * <p>
     * Return the minimum number of refueling stops the car must make in order to reach its destination. If it cannot reach
     * the destination, return -1.
     * <p>
     * Note that if the car reaches a gas station with 0 fuel left, the car can still refuel there. If the car reaches the
     * destination with 0 fuel left, it is still considered to have arrived.
     * <p>
     * Constraints:
     * <p>
     * 1 <= target, startFuel <= 10^9
     * 0 <= stations.length <= 500
     * 0 <= positioni <= positioni+1 < target
     * 1 <= fueli < 10^9
     * <p>
     * Approach 1: DP
     * Denote dp[i][j] as at station i the maximum distance could reach with j stops. Apparently, dp[i][0] will always
     * be equal to startFuel since it is the further distance one can make without refueling. If we can refuel,
     * there are two options
     * 1. We don't refuel at current station, the maximum distance will be equal to the maximum distance we could make
     * from the previous station, i.e. dp[i][j] = dp[i - 1][j]
     * 2. Otherwise, we can refuel at current station, then the distance will be the maximum distance we could make from
     * the previous station with 1 less refuel + the fuel we get at current station, i.e. dp[i][j] = dp[i - 1][j - 1] + station[j][1]
     * We want to take the maximum of both options.
     * In the end, for all dp[n][j], where 0 <= j <= n, if dp[n][j] >= target means we can reach the target distance with j
     * refuels, return it directly since it's the smallest j. Otherwise, return -1.
     * <p>
     * Time: O(n^2) for each station i, the maximum refuels we could make is i, we need to look back for all j, where 0 <= j < i
     * to compute the maximum distance we could reach with fewer refuels. It takes O(n^2) in total.
     * Space: O(n^2) need a 2-D array
     */
    public int minRefuelStopsDP(int target, int startFuel, int[][] stations) {
        int n = stations.length;
        // need one more space since we may have [0, n] refuels
        int[][] maxDistanceAtStationWithRefuels = new int[n + 1][n + 1];

        // initialization - at any station, the max distance we can reach without refuel is the startFuel
        for (int i = 0; i <= n; i++) {
            maxDistanceAtStationWithRefuels[i][0] = startFuel;
        }

        // then check the maximum distance we could reach at each station
        for (int i = 1; i <= n; i++) {
            // we may have up to i refuels for i number of stations
            for (int j = 1; j <= i; j++) {
                // option 1 - we don't refuel at current station
                // the max distance will be the same distance from the previous station
                maxDistanceAtStationWithRefuels[i][j] = maxDistanceAtStationWithRefuels[i - 1][j];

                // option two - we want to refuel at current station
                // before that, we need to check whether it's possible to arrive
                // i.e. the maximum distance from previous stations with 1 fewer refuel is greater than the current station pos
                if (maxDistanceAtStationWithRefuels[i - 1][j - 1] >= stations[i - 1][0]) {
                    // take the maximum distance
                    maxDistanceAtStationWithRefuels[i][j] = Math.max(maxDistanceAtStationWithRefuels[i][j],
                            maxDistanceAtStationWithRefuels[i - 1][j - 1] + stations[i - 1][1]);
                }
            }
        }

        // check each number of refuels and see whether we can reach the target
        for (int i = 0; i <= n; i++) {
            if (maxDistanceAtStationWithRefuels[n][i] >= target) return i;
        }

        return -1;
    }

    /**
     * Approach 2: Greedy
     * Note that if we don't refuel, the maximum distance we can reach is startFuel. That's being said, in order to reach
     * a further distance, we have to refuel at one of the station before reaching startFuel.
     * For example, we have stations at 10, 20, 30, 40, 50, the startFuel is 35. We must refuel at one of these stations:
     * 10, 20, 30 in order to go beyond 35. What will be optimal solution at current state? We choose to refuel at the station
     * which provides the most gas. In other words, the maximum distance is not determined by where the gas stations is located,
     * but it's determined by the amount of gas. By refueling one time, we can obtain a further distance, and similarly, in order
     * to reach an even further distance, we must refuel at one of the gas stations (but not the one we just refueled). Again,
     * the best strategy here is to choose the station with the largest amount of gas. We greedily pick one gas station until
     * the maximum distance we could reach is >= target. There is one more edge case is that if the startFuel is >= target,
     * we don't need to refuel.
     * <p>
     * Time: O(nlogn) worst case scenario, we need to put all stations in the priority queue, then it will be of size O(n)
     * removing the largest amount of gas from the heap will take O(logn). In the worst case, we need to refuel n times,
     * and the total runtime will be O(nlogn)
     * Space: O(n)
     */
    public int minRefuelStopsGreedy(int target, int startFuel, int[][] stations) {
        // edge case - if startFuel >= target, we can reach target without any refuel
        if (startFuel >= target) return 0;
        // we only need to keep track of the amount of gas for each station
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> (Integer.compare(b, a)));
        int index = 0, numOfStations = stations.length, minStops = 0, maxDistance = startFuel;
        // we need to keep refueling until we can reach the target
        while (maxDistance < target) {
            // add all stations (specifically amount of gas) we met before reaching the current max distance
            while (index < numOfStations && stations[index][0] <= maxDistance) {
                maxHeap.add(stations[index][1]);
                index++;
            }
            // if we haven't reached any stations but the gas is used up before the target distance - return -1
            if (maxHeap.isEmpty()) return -1;
            // otherwise, we get refueled, the maximum distance is updated by the gas added
            maxDistance += maxHeap.poll();
            // increment the number of refuel
            minStops++;
        }
        return minStops;
    }

    @Test
    public void minRefuelStopsTest() {
        /**
         * Example 1:
         * Input: target = 1, startFuel = 1, stations = []
         * Output: 0
         * Explanation: We can reach the target without refueling.
         */
        assertEquals(0, minRefuelStopsDP(1, 1, new int[0][]));
        assertEquals(0, minRefuelStopsGreedy(1, 1, new int[0][]));
        /**
         * Example 2:
         * Input: target = 100, startFuel = 1, stations = [[10,100]]
         * Output: -1
         * Explanation: We can not reach the target (or even the first gas station).
         */
        assertEquals(-1, minRefuelStopsDP(100, 1, new int[][]{{10, 100}}));
        assertEquals(-1, minRefuelStopsGreedy(100, 1, new int[][]{{10, 100}}));
        /**
         * Example 3:
         * Input: target = 100, startFuel = 10, stations = [[10,60],[20,30],[30,30],[60,40]]
         * Output: 2
         * Explanation: We start with 10 liters of fuel.
         * We drive to position 10, expending 10 liters of fuel.  We refuel from 0 liters to 60 liters of gas.
         * Then, we drive from position 10 to position 60 (expending 50 liters of fuel),
         * and refuel from 10 liters to 50 liters of gas.  We then drive to and reach the target.
         * We made 2 refueling stops along the way, so we return 2.
         */
        assertEquals(2, minRefuelStopsDP(100, 10, new int[][]{{10, 60}, {20, 30}, {30, 30}, {60, 40}}));
        assertEquals(2, minRefuelStopsGreedy(100, 10, new int[][]{{10, 60}, {20, 30}, {30, 30}, {60, 40}}));
    }
}
