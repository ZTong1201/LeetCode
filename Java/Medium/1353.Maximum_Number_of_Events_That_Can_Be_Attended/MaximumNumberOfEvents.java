import org.junit.Test;

import java.util.Arrays;
import java.util.PriorityQueue;

import static org.junit.Assert.assertEquals;

public class MaximumNumberOfEvents {

    /**
     * Given an array of events where events[i] = [startDayi, endDayi]. Every event i starts at startDayi and ends at endDayi.
     * <p>
     * You can attend an event i at any day d where startTimei <= d <= endTimei. Notice that you can only attend one event at any time d.
     * <p>
     * Return the maximum number of events you can attend.
     * <p>
     * Constraints:
     * <p>
     * 1 <= events.length <= 10^5
     * events[i].length == 2
     * 1 <= startDayi <= endDayi <= 10^5
     * <p>
     * Approach: Greedy
     * Essentially, we'd like to attend events whose start day happens at first. If there is a tie with the start date, then
     * we attend the meeting which ends sooner. Why? Cuz if the event ends later, then we are probably able to attend the
     * event in the last couple of days. Therefore, we can greedily always attend meeting which ends sooner, and keep track
     * of the earliest day we can attend the next event. Also, we need a priority queue to store the ending time in the
     * ascending order. If the earliest day we can attend the next event exceeds some ending times, then we're not able to
     * catch up with those meetings - remove them from the heap.
     * <p>
     * Time: O(nlogn) each event will be added and removed from the heap at most once during each iteration.
     * Space: O(n)
     */
    public int maxEvents(int[][] events) {
        // sort the events by the starting time, then by ending time in ascending order if there is a tie
        Arrays.sort(events, (a, b) -> {
            if (a[0] == b[0]) return a[1] - b[1];
            else return a[0] - b[0];
        });

        // Use a min heap to keep track of the event ending day
        PriorityQueue<Integer> endingDay = new PriorityQueue<>();
        int numOfEventsCanBeAttended = 0, earliestDayToAttendEvent = 0, index = 0, n = events.length;

        while (index < n || !endingDay.isEmpty()) {
            // if we don't have any ending day limitation
            // the earliest day we can attend next event will be the new event starting day
            if (endingDay.isEmpty()) {
                earliestDayToAttendEvent = events[index][0];
            }
            // while we can attend more events, adding their ending days in the heap
            while (index < n && events[index][0] <= earliestDayToAttendEvent) {
                endingDay.add(events[index][1]);
                index++;
            }
            // attend the event which ends the soonest
            endingDay.poll();
            // increment the result
            numOfEventsCanBeAttended++;
            // also, the earliest day we can attend next event will be the next day after attending the current event
            earliestDayToAttendEvent++;
            // once the earliest day has been updated, we might never be able to attend some events
            // the events whose ending day is earlier than the earliest day we can attend new events
            while (!endingDay.isEmpty() && endingDay.peek() < earliestDayToAttendEvent) {
                endingDay.poll();
            }
        }
        return numOfEventsCanBeAttended;
    }

    @Test
    public void maxEventsTest() {
        /**
         * Example 1:
         * Input: events = [[1,2],[2,3],[3,4]]
         * Output: 3
         * Explanation: You can attend all the three events.
         * Attend the first event on day 1.
         * Attend the second event on day 2.
         * Attend the third event on day 3.
         */
        assertEquals(3, maxEvents(new int[][]{{1, 2}, {2, 3}, {3, 4}}));
        /**
         * Example 2:
         * Input: events= [[1,2],[2,3],[3,4],[1,2]]
         * Output: 4
         */
        assertEquals(4, maxEvents(new int[][]{{1, 2}, {2, 3}, {3, 4}, {1, 2}}));
        /**
         * Example 3:
         * Input: events = [[1,4],[4,4],[2,2],[3,4],[1,1]]
         * Output: 4
         */
        assertEquals(4, maxEvents(new int[][]{{1, 4}, {4, 4}, {2, 2}, {3, 4}}));
        /**
         * Example 4:
         * Input: events = [[1,100000]]
         * Output: 1
         */
        assertEquals(1, maxEvents(new int[][]{{1, 100000}}));
        /**
         * Example 5:
         * Input: events = [[1,1],[1,2],[1,3],[1,4],[1,5],[1,6],[1,7]]
         * Output: 7
         */
        assertEquals(7, maxEvents(new int[][]{{1, 1}, {1, 2}, {1, 3}, {1, 4}, {1, 5}, {1, 6}, {1, 7}}));
    }
}
