import java.util.*;
/**
 * Given an array of meeting time intervals consisting of 
 * start and end times [[s1,e1],[s2,e2],...] (si < ei)
 * determine if a person could attend all meetings.
 */

public class Meeting_Rooms_I {

    public static boolean canAttendMeetings(int[][] intervals) {
        Comparator<int[]> compareStart = (int[] a, int[] b) -> { return a[0] - b[0]; };
        Arrays.sort(intervals, compareStart);
        for(int i = 0; i < intervals.length - 1; i++) {
            if(intervals[i][1] > intervals[i + 1][0]) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        int[][] meetings1 = new int[][]{{0, 30}, {5, 10}, {15, 20}};
        System.out.println("Expected: false Actual: " + canAttendMeetings(meetings1));
        int[][] meetings2 = new int[][]{{7, 10}, {2, 4}};
        System.out.println("Expected: true Actual: " + canAttendMeetings(meetings2));
    }
}