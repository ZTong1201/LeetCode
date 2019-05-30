import java.util.*;
/**
 * We can sort the original array and only use one Priority Queue to store meeting end time.
 */

 public class Meeting_Rooms_II_2 {

    public static int minMeetingRooms(int[][] intervals) {
        int roomCount = 0;
        if(intervals.length == 0) return roomCount;
        Comparator<int[]> startCompare = (int[] meet1, int[] meet2) -> { return meet1[0] - meet2[0];};
        Arrays.sort(intervals, startCompare);
        PriorityQueue<Integer> endTimeMinPQ = new PriorityQueue<>();
        endTimeMinPQ.add(intervals[0][1]);
        for(int i = 1; i < intervals.length; i++) {
            if(intervals[i][0] >= endTimeMinPQ.peek()) {
                endTimeMinPQ.poll();
            }
            endTimeMinPQ.add(intervals[i][1]);
        }
        return endTimeMinPQ.size();
    }

    public static void main(String[] args) {
        int[][] meetings1 = new int[][]{{13, 15}, {1, 13}, {6, 9}};
        System.out.println("Expected: 2, Actual: " + minMeetingRooms(meetings1));
        int[][] meetings2 = new int[][]{{13, 15}, {1, 13}};
        System.out.println("Expected: 1, Actual: " + minMeetingRooms(meetings2));
        int[][] meetings3 = new int[][]{{0, 30}, {5, 10}, {15, 20}};
        System.out.println("Expected: 2, Actual: " + minMeetingRooms(meetings3));
    }
 }