import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Given an array of meeting time intervals consisting of start and end times [[s1,e1],[s2,e2],...] (si < ei)
 * find the minimum number of conference rooms required.
 * Use two Priority Queues.
 */
public class Meeting_Rooms_II_1 {

    public static int minMeetingRooms(int[][] intervals) {
        int roomCount = 0;
        Comparator<int[]> startCompare = (int[] meet1, int[] meet2) -> {
            /*if(meet1[0] - meet2[0] == 0) {
                return (-1)*(meet1[1] - meet2[1]);
            }*/
            return meet1[0] - meet2[0];
        };
        Comparator<int[]> endCompare = (int[] meet1, int[] meet2) -> {
            /*if(meet1[1] - meet2[1] == 0) {
                return (-1)*(meet1[0] - meet2[0]);
            }*/
            return meet1[1] - meet2[1];
        };
        PriorityQueue<int[]> startMinPQ = new PriorityQueue<>(startCompare);
        PriorityQueue<int[]> endMinPQ = new PriorityQueue<>(endCompare);
        for(int i = 0; i < intervals.length; i++) {
            startMinPQ.add(intervals[i]);
            endMinPQ.add(intervals[i]);
        }
        while(!startMinPQ.isEmpty()) {
            int[] meet1 = startMinPQ.peek();
            int[] meet2 = endMinPQ.peek();
            if(meet1[0] - meet2[1] >= 0) {
                endMinPQ.poll();
                roomCount -= 1;
            }
            startMinPQ.poll();
            roomCount += 1;
        }
        return roomCount;
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
