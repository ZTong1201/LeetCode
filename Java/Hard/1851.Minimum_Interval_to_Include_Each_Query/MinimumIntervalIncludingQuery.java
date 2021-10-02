import org.junit.Test;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.TreeMap;

import static org.junit.Assert.assertArrayEquals;

public class MinimumIntervalIncludingQuery {

    /**
     * You are given a 2D integer array intervals, where intervals[i] = [lefti, righti] describes the ith interval starting
     * at lefti and ending at righti (inclusive). The size of an interval is defined as the number of integers it contains,
     * or more formally righti - lefti + 1.
     * <p>
     * You are also given an integer array queries. The answer to the jth query is the size of the smallest interval i
     * such that lefti <= queries[j] <= righti. If no such interval exists, the answer is -1.
     * <p>
     * Return an array containing the answers to the queries.
     * <p>
     * Constraints:
     * <p>
     * 1 <= intervals.length <= 10^5
     * 1 <= queries.length <= 10^5
     * intervals[i].length == 2
     * 1 <= lefti <= righti <= 10^7
     * 1 <= queries[j] <= 10^7
     * <p>
     * Approach 1: Sorting + priority queue
     * We'd like to iterate the queries from the smallest to the largest and  also traverse the intervals from the smallest
     * starting point to the largest one. Why? Since if the queries and intervals are both sorted, once
     * intervals[i][0] > queries[j] for certain (i, j), then all the intervals after i won't contain queries[j] anymore.
     * Then we can loop through all the valid intervals which contains queries[j] until index i in intervals array to find
     * the smallest length. The easiest way is to use a priority queue and sort inserted intervals by their length, so now
     * we can always get the smallest interval length in O(1) time, and inserting and removing from heap will take O(logn).
     * The algorithm looks like this:
     * 1. Before sorting, copy the entire queries array to an 2-D array along with the index for each value
     * 2. Sort intervals by starting point and sort queries in ascending order.
     * 3. Iterate over the sorted queries, for each query value:
     * 4. Starting from the previous stopping index (initialized as 0) and add all possible valid intervals containing
     * query value into the heap (as long as the starting point <= query value, insert it. Some may not be a valid interval,
     * but it will be removed in the next step). When inserting into the heap, add pair [length of intervals, ending point]
     * instead of the original interval
     * 5. If the heap is not empty, also remove any invalid intervals (whose ending points is strictly smaller than the
     * query value) from the heap
     * 6. Now, the top value in the heap is a valid interval with the smallest interval length, assign it to the correct index
     * <p>
     * Time: O((m + n) * logm + nlogn) where m is the length of intervals array, n is the length of queries array. We need to
     * sort both arrays, which takes O(mlogm) and O(nlogn) runtime, respectively. When assigning length to each query value,
     * the size of priority queue is bounded by O(m) (in the worst case, we need to add all intervals into the heap), and
     * removing from the heap takes O(logm), we need to take O(n) operations for all query values.
     * Space: O(m + n) O(m) for the heap and O(n) for the final result
     */
    public int[] minIntervalMinHeap(int[][] intervals, int[] queries) {
        int numOfQueries = queries.length;
        // copy each query value along with its index to a new 2-D array
        int[][] queryWithIndex = new int[numOfQueries][2];
        for (int i = 0; i < numOfQueries; i++) {
            queryWithIndex[i][0] = queries[i];
            queryWithIndex[i][1] = i;
        }
        // sort the queries and the intervals
        Arrays.sort(queryWithIndex, (a, b) -> (Integer.compare(a[0], b[0])));
        Arrays.sort(intervals, (a, b) -> (Integer.compare(a[0], b[0])));

        // create a min heap, we insert pair [length of interval, ending point] into the heap,
        // the heap will be sorted by length
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> (Integer.compare(a[0], b[0])));
        // since the interval array was sorted, we start from the very beginning 0
        int intervalIndex = 0;
        int[] res = new int[numOfQueries];

        // loop through each query value
        for (int i = 0; i < numOfQueries; i++) {
            int query = queryWithIndex[i][0], index = queryWithIndex[i][1];
            // insert all possible valid intervals which might contain the query value into the min heap
            while (intervalIndex < intervals.length && intervals[intervalIndex][0] <= query) {
                minHeap.add(new int[]{intervals[intervalIndex][1] - intervals[intervalIndex][0] + 1, intervals[intervalIndex][1]});
                intervalIndex++;
            }

            // then remove invalid intervals for current query value
            while (!minHeap.isEmpty() && minHeap.peek()[1] < query) {
                minHeap.poll();
            }

            // assign the smallest length to the correct index
            // note that it's possible the heap is empty which means there is no valid interval containing the query value
            // assign -1 in that case
            res[index] = minHeap.isEmpty() ? -1 : minHeap.peek()[0];
        }
        return res;
    }

    /**
     * Approach 2: Sweep Line
     * Basically, we need to define 3 types of events and traverse all events from the smallest to the largest and try
     * to find the answer. The event definition is as follows:
     * 1. 0 - means the entry point of an interval, i.e. intervals[i][0]
     * 2. 1 - means it's a query value
     * 3. 2 - means the ending point of an interval, i.e. intervals[i][1]
     * We can use an array of size 3 to denote all three events, e.g. [value, event type, length of interval/index of query],
     * then we sort the events array by the value first, then by the event type (0, 1, 2).
     * For each event:
     * 1. If the event type is 0, then we insert the corresponding length of interval into a tree map. Why tree map? Because
     * first we want to keep track of the frequency of each length, there would've been different intervals share the same length.
     * On the other hand, tree map will guarantee the O(logn) performance for getting the smallest value.
     * 2. If the event type is 1, then we need to find the smallest length stored in the map, if map is empty, the assign -1
     * 3. If the event type is 2, which means this interval will never be used again, we decrement the frequency of that
     * length by 1. If the frequency is 0, remove that key completely from the map.
     * <p>
     * Time: O((m + n) * log(m + n) + nlogm) For each interval we have two events, and one event for a query value. Hence,
     * we have 2m + n total events, and the sorting all events will take O((m + n) * log(m + n)). Then for getting the smallest
     * length of each query value, we might have O(m) unique length, and getting the smallest will be bounded by O(logm), n operations
     * will take overall O(nlogm)
     * Space: O(m + n)
     */
    public int[] minIntervalSweepLine(int[][] intervals, int[] queries) {
        int m = intervals.length, n = queries.length;
        // need 2 events for an interval and 1 event for a query value - 2m + n in total
        int[][] events = new int[2 * m + n][3];
        int i = 0;
        // event schema for intervals: [value, 0/2, length of interval]
        for (int[] interval : intervals) {
            events[i++] = new int[]{interval[0], 0, interval[1] - interval[0] + 1};
            events[i++] = new int[]{interval[1], 2, interval[1] - interval[0] + 1};
        }
        // event schema for queries: [value, 1, index of query]
        for (int index = 0; index < n; index++) {
            events[i++] = new int[]{queries[index], 1, index};
        }
        // sort all events by the value first, if there is tie, sort them by event type
        Arrays.sort(events, (a, b) -> {
            if (a[0] == b[0]) return Integer.compare(a[1], b[1]);
            return Integer.compare(a[0], b[0]);
        });

        // use a tree map to keep track of all interval lengths and their frequency
        TreeMap<Integer, Integer> intervalLength = new TreeMap<>();

        int[] res = new int[n];

        // sweep each event
        for (int[] event : events) {
            // if it's a starting point of an interval, add the length into the map
            if (event[1] == 0) intervalLength.put(event[2], intervalLength.getOrDefault(event[2], 0) + 1);
                // if it's a query event, assign the smallest interval length so far, if the map is emtpy, assign -1
            else if (event[1] == 1) res[event[2]] = intervalLength.isEmpty() ? -1 : intervalLength.firstKey();
            else {
                // if it's an ending point of an interval, this interval will never be used again
                // decrement the corresponding length and remove it from the map if the frequency becomes 0
                intervalLength.put(event[2], intervalLength.getOrDefault(event[2], 0) - 1);
                if (intervalLength.get(event[2]) == 0) intervalLength.remove(event[2]);
            }
        }
        return res;
    }

    @Test
    public void minIntervalTest() {
        /**
         * Example 1:
         * Input: intervals = [[1,4],[2,4],[3,6],[4,4]], queries = [2,3,4,5]
         * Output: [3,3,1,4]
         * Explanation: The queries are processed as follows:
         * - Query = 2: The interval [2,4] is the smallest interval containing 2. The answer is 4 - 2 + 1 = 3.
         * - Query = 3: The interval [2,4] is the smallest interval containing 3. The answer is 4 - 2 + 1 = 3.
         * - Query = 4: The interval [4,4] is the smallest interval containing 4. The answer is 4 - 4 + 1 = 1.
         * - Query = 5: The interval [3,6] is the smallest interval containing 5. The answer is 6 - 3 + 1 = 4.
         */
        int[] expected1 = new int[]{3, 3, 1, 4};
        int[] actualMinHeap1 = minIntervalMinHeap(new int[][]{{1, 4}, {2, 4}, {3, 6}, {4, 4}}, new int[]{2, 3, 4, 5});
        int[] actualSweepLine1 = minIntervalSweepLine(new int[][]{{1, 4}, {2, 4}, {3, 6}, {4, 4}}, new int[]{2, 3, 4, 5});
        assertArrayEquals(expected1, actualMinHeap1);
        assertArrayEquals(expected1, actualSweepLine1);
        /**
         * Example 2:
         * Input: intervals = [[2,3],[2,5],[1,8],[20,25]], queries = [2,19,5,22]
         * Output: [2,-1,4,6]
         * Explanation: The queries are processed as follows:
         * - Query = 2: The interval [2,3] is the smallest interval containing 2. The answer is 3 - 2 + 1 = 2.
         * - Query = 19: None of the intervals contain 19. The answer is -1.
         * - Query = 5: The interval [2,5] is the smallest interval containing 5. The answer is 5 - 2 + 1 = 4.
         * - Query = 22: The interval [20,25] is the smallest interval containing 22. The answer is 25 - 20 + 1 = 6.
         */
        int[] expected2 = new int[]{2, -1, 4, 6};
        int[] actualMinHeap2 = minIntervalMinHeap(new int[][]{{2, 3}, {2, 5}, {1, 8}, {20, 25}}, new int[]{2, 19, 5, 22});
        int[] actualSweepLine2 = minIntervalSweepLine(new int[][]{{2, 3}, {2, 5}, {1, 8}, {20, 25}}, new int[]{2, 19, 5, 22});
        assertArrayEquals(expected2, actualMinHeap2);
        assertArrayEquals(expected2, actualSweepLine2);
    }
}
