import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MyCalendarTwoTest {

    @Test
    public void myCalendarTwoTest() {
        /**
         * Example:
         * Input
         * ["MyCalendarTwo", "book", "book", "book", "book", "book", "book"]
         * [[], [10, 20], [50, 60], [10, 40], [5, 15], [5, 10], [25, 55]]
         * Output
         * [null, true, true, true, false, true, true]
         *
         * Explanation
         * MyCalendarTwo myCalendarTwo = new MyCalendarTwo();
         * myCalendarTwo.book(10, 20); // return True, The event can be booked.
         * myCalendarTwo.book(50, 60); // return True, The event can be booked.
         * myCalendarTwo.book(10, 40); // return True, The event can be double booked.
         * myCalendarTwo.book(5, 15);  // return False, The event ca not be booked, because it would result in a triple booking.
         * myCalendarTwo.book(5, 10); // return True, The event can be booked, as it does not use time 10 which is already double booked.
         * myCalendarTwo.book(25, 55); // return True, The event can be booked, as the time in [25, 40) will be double booked
         * with the third event, the time [40, 50) will be single booked, and the time [50, 55) will be double booked with
         * the second event.
         */
        MyCalendarTwo myCalendarTwo = new MyCalendarTwo();
        assertTrue(myCalendarTwo.book(10, 20));
        assertTrue(myCalendarTwo.book(50, 60));
        assertTrue(myCalendarTwo.book(10, 40));
        assertFalse(myCalendarTwo.book(5, 15));
        assertTrue(myCalendarTwo.book(5, 10));
        assertTrue(myCalendarTwo.book(25, 55));
    }
}
