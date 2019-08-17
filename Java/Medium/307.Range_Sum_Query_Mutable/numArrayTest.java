import org.junit.Test;
import static org.junit.Assert.*;

public class numArrayTest {

    @Test
    public void numArraySegmentTreeTest() {
        /**
         * Given nums = [1, 3, 5]
         *
         * sumRange(0, 2) -> 9
         * update(1, 2)
         * sumRange(0, 2) -> 8
         */
        int[] nums = new int[]{1, 3, 5};
        numArraySegmentTree numArray = new numArraySegmentTree(nums);
        assertEquals(9, numArray.sumRange(0, 2));
        numArray.update(1, 2);
        assertEquals(8, numArray.sumRange(0, 2));
    }

    @Test
    public void numArrayBinaryIndexTreeTest() {
        /**
         * Given nums = [1, 3, 5]
         *
         * sumRange(0, 2) -> 9
         * update(1, 2)
         * sumRange(0, 2) -> 8
         */
        int[] nums = new int[]{1, 3, 5};
        numArrayBinaryIndexedTree numArray = new numArrayBinaryIndexedTree(nums);
        assertEquals(9, numArray.sumRange(0, 2));
        numArray.update(1, 2);
        assertEquals(8, numArray.sumRange(0, 2));
    }
}
