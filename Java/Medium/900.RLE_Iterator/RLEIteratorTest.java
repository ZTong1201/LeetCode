import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RLEIteratorTest {

    /**
     * Example:
     * Input
     * ["RLEIterator", "next", "next", "next", "next"]
     * [[[3, 8, 0, 9, 2, 5]], [2], [1], [1], [2]]
     * Output
     * [null, 8, 8, 5, -1]
     * <p>
     * Explanation
     * RLEIterator rLEIterator = new RLEIterator([3, 8, 0, 9, 2, 5]); // This maps to the sequence [8,8,8,5,5].
     * rLEIterator.next(2); // exhausts 2 terms of the sequence, returning 8. The remaining sequence is now [8, 5, 5].
     * rLEIterator.next(1); // exhausts 1 term of the sequence, returning 8. The remaining sequence is now [5, 5].
     * rLEIterator.next(1); // exhausts 1 term of the sequence, returning 5. The remaining sequence is now [5].
     * rLEIterator.next(2); // exhausts 2 terms, returning -1. This is because the first term exhausted was 5,
     * but the second term did not exist. Since the last term exhausted does not exist, we return -1.
     */
    @Test
    public void rleIteratorTest() {
        RLEIterator rleIterator = new RLEIterator(new int[]{3, 8, 0, 9, 2, 5});
        assertEquals(8, rleIterator.next(2));
        assertEquals(8, rleIterator.next(1));
        assertEquals(5, rleIterator.next(1));
        assertEquals(-1, rleIterator.next(2));
    }
}
