import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PeekingIteratorTest {

    @Test
    public void peekingIteratorTest() {
        /**
         * Example:
         * Input
         * ["PeekingIterator", "next", "peek", "next", "next", "hasNext"]
         * [[[1, 2, 3]], [], [], [], [], []]
         * Output
         * [null, 1, 2, 2, 3, false]
         *
         * Explanation
         * PeekingIterator peekingIterator = new PeekingIterator([1, 2, 3]); // [1,2,3]
         * peekingIterator.next();    // return 1, the pointer moves to the next element [1,2,3].
         * peekingIterator.peek();    // return 2, the pointer does not move [1,2,3].
         * peekingIterator.next();    // return 2, the pointer moves to the next element [1,2,3]
         * peekingIterator.next();    // return 3, the pointer moves to the next element [1,2,3]
         * peekingIterator.hasNext(); // return False
         */
        Integer[] arr = new Integer[]{1, 2, 3};
        PeekingIterator peekingIterator = new PeekingIterator(Arrays.stream(arr).iterator());
        assertEquals(1, (int) peekingIterator.next());
        assertTrue(peekingIterator.hasNext());
        assertEquals(2, (int) peekingIterator.peek());
        assertEquals(2, (int) peekingIterator.next());
        assertEquals(3, (int) peekingIterator.next());
        assertFalse(peekingIterator.hasNext());
    }
}
