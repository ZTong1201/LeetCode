import java.util.TreeMap;

public class MyCalendarThree {

    /**
     * A k-booking happens when k events have some non-empty intersection (i.e., there is some time that is common to all k events.)
     * <p>
     * You are given some events [start, end), after each given event, return an integer k representing the maximum k-booking
     * between all the previous events.
     * <p>
     * Implement the MyCalendarThree class:
     * <p>
     * MyCalendarThree() Initializes the object.
     * int book(int start, int end) Returns an integer k representing the largest integer such that there exists a k-booking
     * in the calendar.
     * <p>
     * Constraints:
     * <p>
     * 0 <= start < end <= 10^9
     * At most 400 calls will be made to book.
     * <p>
     * Approach: TreeMap (boundary count)
     * Similar to LeetCode 731: https://leetcode.com/problems/my-calendar-ii/
     * We still compute the ongoing meeting on the fly. This time we can keep adding events into the calendar and hence need
     * to keep track of the maximum ongoing meetings.
     * <p>
     * Time: O(n^2)
     * Space: O(n)
     */
    private final TreeMap<Integer, Integer> calendar;

    public MyCalendarThree() {
        calendar = new TreeMap<>();
    }

    public int book(int start, int end) {
        // increment 1 if there is a meeting starts
        calendar.put(start, calendar.getOrDefault(start, 0) + 1);
        // decrement 1 if there is a meeting terminates
        calendar.put(end, calendar.getOrDefault(end, 0) - 1);

        // compute the number of ongoing meetings on the fly and update the maximum booking
        int ongoing = 0, maxBooking = 0;
        for (int meeting : calendar.values()) {
            ongoing += meeting;
            maxBooking = Math.max(maxBooking, ongoing);
        }
        return maxBooking;
    }
}
