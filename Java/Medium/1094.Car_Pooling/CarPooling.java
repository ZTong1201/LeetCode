import org.junit.Test;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.TreeMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CarPooling {

    /**
     * There is a car with capacity empty seats. The vehicle only drives east (i.e., it cannot turn around and drive west).
     * <p>
     * You are given the integer capacity and an array trips where trip[i] = [numPassengersi, fromi, toi] indicates that the
     * ith trip has numPassengersi passengers and the locations to pick them up and drop them off are fromi and toi
     * respectively. The locations are given as the number of kilometers due east from the car's initial location.
     * <p>
     * Return true if it is possible to pick up and drop off all passengers for all the given trips, or false otherwise.
     * <p>
     * Constraints:
     * <p>
     * 1 <= trips.length <= 1000
     * trips[i].length == 3
     * 1 <= numPassengersi <= 100
     * 0 <= fromi < toi <= 1000
     * 1 <= capacity <= 10^5
     * <p>
     * Approach 1: Sort + Heap
     * We'd like to iterate over the trip from the smallest distance to the largest. If there is an overlap between the new
     * trip and the existing trips, we need to keep adding passengers. If at any time it exceeds the capacity, we can directly
     * return false. If the new trip has no overlap with previous trips, we can unload some passengers and remove those
     * trips. In order to loop through trips from the smallest to the largest, we need to sort the array first. Then for
     * all existing trips, we add it into a max heap, where the top value has the furthest distance of all existing trips.
     * <p>
     * Time: O(nlogn) sorting takes O(nlogn) time, and we also need a heap to keep track of existing trips, which may have
     * up to O(n) trips, removing and adding operations will take O(logn) time, each trip will be added and removed at most
     * once each. Hence, in total it's O(nlogn) runtime
     * Space: O(n)
     */
    public boolean carPoolingHeap(int[][] trips, int capacity) {
        // sort the trips by the starting distance, then by the ending distance
        Arrays.sort(trips, (a, b) -> {
            if (a[1] == b[1]) return Integer.compare(a[2], b[2]);
            return Integer.compare(a[1], b[1]);
        });
        // also need a max heap to keep track of existing trips
        // we only need [to, passenger] in the heap
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>((a, b) -> (Integer.compare(a[0], b[0])));

        int numOfPassengersInCar = 0;
        // loop through all trips
        for (int[] trip : trips) {
            int passengers = trip[0], from = trip[1], to = trip[2];

            // if the previous trips have no overlap with the current one
            // we can complete those trips and release some capacity
            while (!maxHeap.isEmpty() && from >= maxHeap.peek()[0]) {
                numOfPassengersInCar -= maxHeap.poll()[1];
            }

            // add new passengers
            numOfPassengersInCar += passengers;
            // return false if we are already over-capacity
            if (numOfPassengersInCar > capacity) return false;
            // add current trip into the heap
            maxHeap.add(new int[]{to, passengers});
        }
        return true;
    }

    /**
     * Approach 2: Sweep Line
     * The algorithm will be simply adding passengers when entering an event and removing passengers when leaving an event.
     * Therefore, this could be done by using a sweep line approach. Essentially, we keep a tree map (why tree map? because
     * sweep line needs to be done in a sorted order, we can get the order for free from tree map) and put a positive
     * passenger number to the from distance (meaning we add passengers at the beginning of the trip) and also put a negative
     * number to the to distance (meaning we remove passengers at the end of the trip). Then sweep each distance position,
     * add/remove passengers on the fly, if at any time it exceeds the capacity, then we must have been overloaded in between.
     * <p>
     * Time: O(nlogn) we need to add 2 * n events into a tree map in the worst case, we essentially sort these events using
     * a tree, hence it takes O(nlogn)
     * Space: O(n)
     */
    public boolean carPoolingSweepLine(int[][] trips, int capacity) {
        TreeMap<Integer, Integer> lines = new TreeMap<>();
        for (int[] trip : trips) {
            // add a positive number for entering events
            lines.put(trip[1], lines.getOrDefault(trip[1], 0) + trip[0]);
            // add a negative number for leaving events
            lines.put(trip[2], lines.getOrDefault(trip[2], 0) - trip[0]);
        }

        int numOfPassengersInCar = 0;
        // iterate over all values - we will know the number of passengers in each range
        for (int passengers : lines.values()) {
            numOfPassengersInCar += passengers;
            if (numOfPassengersInCar > capacity) return false;
        }
        return true;
    }

    @Test
    public void carPoolingTest() {
        /**
         * Example 1：
         * Input: trips = [[2,1,5],[3,3,7]], capacity = 4
         * Output: false
         */
        assertFalse(carPoolingHeap(new int[][]{{2, 1, 5}, {3, 3, 7}}, 4));
        assertFalse(carPoolingSweepLine(new int[][]{{2, 1, 5}, {3, 3, 7}}, 4));
        /**
         * Example 2:
         * Input: trips = [[2,1,5],[3,3,7]], capacity = 5
         * Output: true
         */
        assertTrue(carPoolingHeap(new int[][]{{2, 1, 5}, {3, 3, 7}}, 5));
        assertTrue(carPoolingSweepLine(new int[][]{{2, 1, 5}, {3, 3, 7}}, 5));
        /**
         * Example 3:
         * Input: trips = [[2,1,5],[3,5,7]], capacity = 3
         * Output: true
         */
        assertTrue(carPoolingHeap(new int[][]{{2, 1, 5}, {3, 5, 7}}, 3));
        assertTrue(carPoolingSweepLine(new int[][]{{2, 1, 5}, {3, 5, 7}}, 3));
        /**
         * Example 4:
         * Input: trips = [[3,2,7],[3,7,9],[8,3,9]], capacity = 11
         * Output: true
         */
        assertTrue(carPoolingHeap(new int[][]{{3, 2, 7}, {3, 7, 9}, {8, 3, 9}}, 11));
        assertTrue(carPoolingSweepLine(new int[][]{{3, 2, 7}, {3, 7, 9}, {8, 3, 9}}, 11));
    }
}
