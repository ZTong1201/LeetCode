import java.util.TreeMap;

public class MyCalendarTreeMap {

    /**
     * Approach 2: Tree Map
     * We want to improve the time performance by searching in O(logn) time. In order to achieve that time complexity, we need
     * a BST structure to always discard half of search space. The java tree map is implemented to guarantee that the tree
     * is balanced, hence O(logn) search time is assured.
     * <p>
     * For a given interval [start, end), we can find its neighbors based on the start time. One way to think of it is
     *       floor        ceiling
     * ...  |------| ... |------| ...
     *         | ---------- |
     *       start         end
     * if start < floor(end) || end > ceiling(start). We can use a map in which the key is the start time and the value
     * is the end time. Note that the neighbors can be null because we might insert at the front or at the rear end.
     * <p>
     * Time: O(nlogn) find two neighbors based on the start time takes O(logn) in java tree map
     * Space: O(n)
     */
    private final TreeMap<Integer, Integer> calendar;

    public MyCalendarTreeMap() {
        calendar = new TreeMap<>();
    }

    public boolean book(int start, int end) {
        // get its lower and upper neighbor based on start time
        Integer prev = calendar.floorKey(start), next = calendar.ceilingKey(start);
        // start >= floor(end) && end <= ceiling(start) - then it can be booked
        if ((prev == null || start >= calendar.get(prev)) && (next == null || end <= next)) {
            // book the meeting
            calendar.put(start, end);
            return true;
        }
        // otherwise, there must have been an overlap
        return false;
    }
}
