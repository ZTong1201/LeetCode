import java.util.TreeMap;

public class MyCalendarTwo {

    /**
     * You are implementing a program to use as your calendar. We can add a new event if adding the event will not cause a
     * triple booking.
     * <p>
     * A triple booking happens when three events have some non-empty intersection (i.e., some moment is common to all the
     * three events.).
     * The event can be represented as a pair of integers start and end that represents a booking on the half-open interval
     * [start, end), the range of real numbers x such that start <= x < end.
     * <p>
     * Implement the MyCalendarTwo class:
     * <p>
     * MyCalendarTwo() Initializes the calendar object.
     * boolean book(int start, int end) Returns true if the event can be added to the calendar successfully without causing
     * a triple booking. Otherwise, return false and do not add the event to the calendar.
     * <p>
     * Constraints:
     * <p>
     * 0 <= start < end <= 10^9
     * At most 1000 calls will be made to book.
     * <p>
     * Approach: TreeMap (boundary count)
     * Basically, we treat the start of a meeting as an entering event, and the end of a meeting as a leaving event. If
     * we sweep all the time slots in the ascending order, we can keep track of the number of ongoing events (meetings).
     * To better translate the algorithm into code, we can increment 1 when a meeting starts, and decrement 1 when a meeting
     * ends. If at any time, the ongoing meeting >= 3, which means we have a triple booking if adding the new meeting.
     * Hence, we need to retrospectively revert everything back, i.e. decrement 1 on the start time and increment 1 on
     * the end time. If the number of meetings corresponding to start/end time becomes 0, which means those times didn't exist
     * before adding this new meeting - remove them from the map and return false. To better maintain a sorted array, we
     * can take advantage of the TreeMap structure to avoid sorting the array over and over.
     * <p>
     * Time: O(n^2) for each book() call we need to iterate over the entire calendar to check a triple booking in the worst case
     * Space: O(n)
     */
    private final TreeMap<Integer, Integer> calendar;

    public MyCalendarTwo() {
        calendar = new TreeMap<>();
    }

    public boolean book(int start, int end) {
        // increment 1 for entering events
        calendar.put(start, calendar.getOrDefault(start, 0) + 1);
        // decrement 1 for leaving events
        calendar.put(end, calendar.getOrDefault(end, 0) - 1);

        // compute the number of concurrent events
        int ongoing = 0;
        for (int meeting : calendar.values()) {
            ongoing += meeting;
            // if we have a triple booking because of the new meeting
            if (ongoing >= 3) {
                // revert the calendar back
                calendar.put(start, calendar.get(start) - 1);
                // if the corresponding value is 0, which means this key-value pair is added because of the new meeting
                // remove it from the calendar
                if (calendar.get(start) == 0) {
                    calendar.remove(start);
                }
                // same thing happens for the end time
                calendar.put(end, calendar.get(end) + 1);
                if (calendar.get(end) == 0) {
                    calendar.remove(end);
                }
                return false;
            }
        }
        return true;
    }
}
