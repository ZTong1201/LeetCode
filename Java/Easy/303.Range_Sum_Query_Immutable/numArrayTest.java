import org.junit.Test;
import static org.junit.Assert.*;

public class numArrayTest {

    @Test
    public void numArrayTest() {
        /**
         * Given nums = [-2, 0, 3, -5, 2, -1]
         *
         * sumRange(0, 2) -> 1
         * sumRange(2, 5) -> -1
         * sumRange(0, 5) -> -3
         */
        int[] nums = new int[]{-2, 0, 3, -5, 2, -1};
        numArray numArray = new numArray(nums);
        assertEquals(1, numArray.sumRange(0, 2));
        assertEquals(-1, numArray.sumRange(2, 5));
        assertEquals(-3, numArray.sumRange(0, 5));
    }
}
