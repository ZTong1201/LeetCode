import java.util.ArrayList;
import java.util.List;

public class MyCalendarBruteForce {

    /**
     * You are implementing a program to use as your calendar. We can add a new event if adding the event will not cause a
     * double booking.
     * <p>
     * A double booking happens when two events have some non-empty intersection (i.e., some moment is common to both events.).
     * <p>
     * The event can be represented as a pair of integers start and end that represents a booking on the half-open interval
     * [start, end), the range of real numbers x such that start <= x < end.
     * <p>
     * Implement the MyCalendar class:
     * <p>
     * MyCalendar() Initializes the calendar object.
     * boolean book(int start, int end) Returns true if the event can be added to the calendar successfully without causing
     * a double booking. Otherwise, return false and do not add the event to the calendar.
     * <p>
     * Constraints:
     * <p>
     * 0 <= start < end <= 10^9
     * At most 1000 calls will be made to book.
     * <p>
     * Approach 1: Brute Force
     * For a given [start, end) interval, we can visit all the booked time interval see whether there is an overlapping.
     * For example, if we have an interval [e1, e2) and a new meeting [s1, s2) to be added. If s1 < e2 and e1 > s2, then
     * there must have been an overlap.
     * <p>
     * Time: O(n^2) for each book, we need to traverse all the meetings to check whether there is an overlap
     * Space: O(n)
     */
    private final List<int[]> calendar;

    public MyCalendarBruteForce() {
        // sort all meetings by the starting time
        calendar = new ArrayList<>();
    }

    public boolean book(int start, int end) {
        for (int[] meeting : calendar) {
            // cannot book if there was an overlap
            if (start < meeting[1] && end > meeting[0]) return false;
        }
        // otherwise, book the meeting
        calendar.add(new int[]{start, end});
        return true;
    }
}
