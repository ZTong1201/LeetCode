import org.junit.Test;

import java.util.Arrays;
import java.util.PriorityQueue;

import static org.junit.Assert.assertEquals;

public class MeetingRoomsII {

    /**
     * Given an array of meeting time intervals consisting of start and end times [[s1,e1],[s2,e2],...] (si < ei)
     * find the minimum number of conference rooms required.
     * <p>
     * Constraints:
     * <p>
     * 1 <= intervals.length <= 10^4
     * 0 <= start[i] < end[i] <= 10^6
     * <p>
     * Approach 1: Sorting + priority queue
     * We first sort the meetings by its starting time. Then, when a new meeting kicks in, we know for sure that it will
     * always start after the previous meeting started. Now we have two options:
     * 1. Assign an extra room for the meeting, if it starts before the ending time of previous meeting. - i.e. add it
     * to the priority queue of ending time (size increases)
     * 2. The earliest meeting has been over, we can free the room up and the new meeting can reuse the room - i.e.
     * remove the smallest ending time from the queue, and add its ending time to the queue. (size remains the same)
     * Finally, we return the size of the queue, which means the last couple of ongoing meetings.
     * <p>
     * Time: O(nlogn)
     * Space: O(n)
     */
    public int minMeetingRoomsPQ(int[][] intervals) {
        // sort meetings by starting time
        Arrays.sort(intervals, (a, b) -> (a[0] - b[0]));
        // create a priority queue in which the smallest element
        // corresponds to the earliest meeting ending time
        PriorityQueue<Integer> meetingEndingTime = new PriorityQueue<>();
        for (int[] interval : intervals) {
            // if the new meeting starts after the earliest meeting ending time
            // we can free up a room - remove that meeting from the meeting room
            if (!meetingEndingTime.isEmpty() && interval[0] >= meetingEndingTime.peek()) {
                meetingEndingTime.poll();
            }
            // the current meeting is ongoing now
            meetingEndingTime.add(interval[1]);
        }
        // the number of needed rooms will be the size of ultimate ongoing meetings
        return meetingEndingTime.size();
    }

    /**
     * Approach 2: Two pointers
     * We can avoid using priority queue to solve this problem. The key part of this problem is to first sort all meetings
     * by their starting time to make sure we visit meetings in a chronically increasing order. In addition, we also need
     * to sort the ending time in the ascending order as well to free up a room. Therefore, we can split up the starting
     * and ending times into two separate arrays and sort them both in ascending order. As long as there is a new meeting,
     * we assign a new room for it. However, if the starting time >= the smallest ending time, which means there is a room
     * can be freed up - we decrement the number of rooms used.
     * <p>
     * Time: O(nlogn)
     * Space: O(n)
     */
    public int minMeetingRoomsTwoPointers(int[][] intervals) {
        int length = intervals.length;
        int[] startTime = new int[length];
        int[] endTime = new int[length];
        for (int i = 0; i < length; i++) {
            startTime[i] = intervals[i][0];
            endTime[i] = intervals[i][1];
        }
        // sort both arrays - hence we can traverse the arrays from the smallest to the largest
        Arrays.sort(startTime);
        Arrays.sort(endTime);

        int startPointer = 0, endPointer = 0;
        int usedRooms = 0;
        while (startPointer < length) {
            // if there is a meeting is over - decrement the number of used rooms
            if (startTime[startPointer] >= endTime[endPointer]) {
                usedRooms--;
                endPointer++;
            }
            // always assign a new room for the meeting
            usedRooms++;
            startPointer++;
        }
        return usedRooms;
    }

    @Test
    public void minMeetingRoomsTest() {
        /**
         * Example 1:
         * Input: intervals = [[0,30],[5,10],[15,20]]
         * Output: 2
         */
        assertEquals(2, minMeetingRoomsPQ(new int[][]{{0, 30}, {5, 10}, {15, 20}}));
        assertEquals(2, minMeetingRoomsTwoPointers(new int[][]{{0, 30}, {5, 10}, {15, 20}}));
        /**
         * Example 2:
         * Input: intervals = [[13,15],[1,13]]
         * Output: 1
         */
        assertEquals(1, minMeetingRoomsPQ(new int[][]{{13, 15}, {1, 13}}));
        assertEquals(1, minMeetingRoomsTwoPointers(new int[][]{{13, 15}, {1, 13}}));
        /**
         * Example 3:
         * Input: intervals = [[13,15],[1,13],[6,9]]
         * Output: 2
         */
        assertEquals(2, minMeetingRoomsPQ(new int[][]{{13, 15}, {1, 13}, {6, 9}}));
        assertEquals(2, minMeetingRoomsTwoPointers(new int[][]{{13, 15}, {1, 13}, {6, 9}}));
    }
}
