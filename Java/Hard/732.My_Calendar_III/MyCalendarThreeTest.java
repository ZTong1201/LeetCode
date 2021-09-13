import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MyCalendarThreeTest {

    @Test
    public void myCalendarThreeTest() {
        /**
         * Example:
         * Input
         * ["MyCalendarThree", "book", "book", "book", "book", "book", "book"]
         * [[], [10, 20], [50, 60], [10, 40], [5, 15], [5, 10], [25, 55]]
         * Output
         * [null, 1, 1, 2, 3, 3, 3]
         *
         * Explanation
         * MyCalendarThree myCalendarThree = new MyCalendarThree();
         * myCalendarThree.book(10, 20); // return 1, The first event can be booked and is disjoint, so the maximum k-booking is a 1-booking.
         * myCalendarThree.book(50, 60); // return 1, The second event can be booked and is disjoint, so the maximum k-booking is a 1-booking.
         * myCalendarThree.book(10, 40); // return 2, The third event [10, 40) intersects the first event, and the maximum k-booking is a 2-booking.
         * myCalendarThree.book(5, 15); // return 3, The remaining events cause the maximum K-booking to be only a 3-booking.
         * myCalendarThree.book(5, 10); // return 3
         * myCalendarThree.book(25, 55); // return 3
         */
        MyCalendarThree myCalendarThree = new MyCalendarThree();
        assertEquals(1, myCalendarThree.book(10, 20));
        assertEquals(1, myCalendarThree.book(50, 60));
        assertEquals(2, myCalendarThree.book(10, 40));
        assertEquals(3, myCalendarThree.book(5, 15));
        assertEquals(3, myCalendarThree.book(5, 10));
        assertEquals(3, myCalendarThree.book(25, 55));
    }
}
