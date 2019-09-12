import org.junit.Test;
import static org.junit.Assert.*;

public class medianFinderTest {

    @Test
    public void medianFinderInsertionSortTest() {
        /**
         * addNum(1)
         * addNum(2)
         * findMedian() -> 1.5
         * addNum(3)
         * findMedian() -> 2
         */
        medianFinderInsertionSort stream = new medianFinderInsertionSort();
        stream.addNum(1);
        stream.addNum(2);
        assertEquals(1.5, stream.findMedian(), 0);
        stream.addNum(3);
        assertEquals(2.0, stream.findMedian(), 0);
    }

    @Test
    public void medianFinderTwoHeapsTest() {
        /**
         * addNum(1)
         * addNum(2)
         * findMedian() -> 1.5
         * addNum(3)
         * findMedian() -> 2
         */
        medianFinderTwoHeaps stream = new medianFinderTwoHeaps();
        stream.addNum(1);
        stream.addNum(2);
        assertEquals(1.5, stream.findMedian(), 0);
        stream.addNum(3);
        assertEquals(2.0, stream.findMedian(), 0);
    }
}
