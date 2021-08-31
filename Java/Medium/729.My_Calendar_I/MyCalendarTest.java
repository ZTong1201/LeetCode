import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MyCalendarTest {

    @Test
    public void myCalendarBruteForceTest() {
        /**
         * Input
         * ["MyCalendar", "book", "book", "book"]
         * [[], [10, 20], [15, 25], [20, 30]]
         * Output
         * [null, true, false, true]
         *
         * Explanation
         * MyCalendar myCalendar = new MyCalendar();
         * myCalendar.book(10, 20); // return True
         * myCalendar.book(15, 25); // return False, It can not be booked because time 15 is already booked by another event.
         * myCalendar.book(20, 30); // return True, The event can be booked, as the first event takes every time less than
         * 20, but not including 20.
         */
        MyCalendarBruteForce myCalendar = new MyCalendarBruteForce();
        assertTrue(myCalendar.book(10, 20));
        assertFalse(myCalendar.book(15, 25));
        assertTrue(myCalendar.book(20, 30));
    }

    @Test
    public void myCalendarTreeMapTest() {
        /**
         * Input
         * ["MyCalendar", "book", "book", "book"]
         * [[], [10, 20], [15, 25], [20, 30]]
         * Output
         * [null, true, false, true]
         *
         * Explanation
         * MyCalendar myCalendar = new MyCalendar();
         * myCalendar.book(10, 20); // return True
         * myCalendar.book(15, 25); // return False, It can not be booked because time 15 is already booked by another event.
         * myCalendar.book(20, 30); // return True, The event can be booked, as the first event takes every time less than
         * 20, but not including 20.
         */
        MyCalendarTreeMap myCalendar = new MyCalendarTreeMap();
        assertTrue(myCalendar.book(10, 20));
        assertFalse(myCalendar.book(15, 25));
        assertTrue(myCalendar.book(20, 30));
    }
}
