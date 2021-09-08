import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SnapshotArrayTest {

    @Test
    public void snapshotArrayTest() {
        /**
         * Example:
         * Input: ["SnapshotArray","set","snap","set","get"]
         * [[3],[0,5],[],[0,6],[0,0]]
         * Output: [null,null,0,null,5]
         * Explanation:
         * SnapshotArray snapshotArr = new SnapshotArray(3); // set the length to be 3
         * snapshotArr.set(0,5);  // Set array[0] = 5
         * snapshotArr.snap();  // Take a snapshot, return snap_id = 0
         * snapshotArr.set(0,6);
         * snapshotArr.get(0,0);  // Get the value of array[0] with snap_id = 0, return 5
         */
        SnapshotArray snapshotArray = new SnapshotArray(3);
        snapshotArray.set(0, 5);
        assertEquals(0, snapshotArray.snap());
        snapshotArray.set(0, 6);
        assertEquals(5, snapshotArray.get(0, 0));
    }
}
