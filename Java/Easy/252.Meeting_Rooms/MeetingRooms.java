import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MeetingRooms {

    /**
     * Given an array of meeting time intervals consisting of
     * start and end times [[s1,e1],[s2,e2],...] (si < ei)
     * determine if a person could attend all meetings.
     * <p>
     * Constraints:
     * <p>
     * 0 <= intervals.length <= 10^4
     * intervals[i].length == 2
     * 0 <= start[i] < end[i] <= 10^6
     * <p>
     * Approach: Sorting
     * Sort meeting times by start time then end time. If start[i + 1] < end[i], then the person cannot attend both meetings,
     * return false.
     * <p>
     * Time: O(nlogn) sorting time complexity will dominate the performance
     * Space: O(1) or O(n) depends upon which sorting algorithm is used
     */
    public boolean canAttendMeetings(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> {
            if (a[0] == b[0]) return a[1] - b[1];
            else return a[0] - b[0];
        });
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] < intervals[i - 1][1]) return false;
        }
        return true;
    }

    @Test
    public void canAttendMeetingsTest() {
        /**
         * Example 1：
         * Input: intervals = [[0,30],[5,10],[15,20]]
         * Output: false
         */
        assertFalse(canAttendMeetings(new int[][]{{0, 30}, {5, 10}, {15, 20}}));
        /**
         * Example 2:
         * Input: intervals = [[7,10],[2,4]]
         * Output: true
         */
        assertTrue(canAttendMeetings(new int[][]{{7, 10}, {2, 4}}));
    }
}
