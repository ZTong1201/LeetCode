import org.junit.Test;
import static org.junit.Assert.*;

public class numMatrixTest {


    @Test
    public void numMatrixDPbyRowTest() {
        /**
         * Example:
         * Given matrix = [
         *   [3, 0, 1, 4, 2],
         *   [5, 6, 3, 2, 1],
         *   [1, 2, 0, 1, 5],
         *   [4, 1, 0, 1, 7],
         *   [1, 0, 3, 0, 5]
         * ]
         *
         * sumRegion(2, 1, 4, 3) -> 8
         * sumRegion(1, 1, 2, 2) -> 11
         * sumRegion(1, 2, 2, 4) -> 12
         */
        int[][] matrix = new int[][]{{3, 0, 1, 4, 2},
                {5, 6, 3, 2, 1},
                {1, 2, 0, 1, 5},
                {4, 1, 0, 1, 7},
                {1, 0, 3, 0, 5}};
        numMatrixDPbyRow numMatrix = new numMatrixDPbyRow(matrix);
        assertEquals(8, numMatrix.sumRegion(2, 1, 4, 3));
        assertEquals(11, numMatrix.sumRegion(1, 1, 2, 2));
        assertEquals(12, numMatrix.sumRegion(1, 2, 2, 4));
    }

    @Test
    public void numMatrixDPbyTopLeftTest() {
        /**
         * Example:
         * Given matrix = [
         *   [3, 0, 1, 4, 2],
         *   [5, 6, 3, 2, 1],
         *   [1, 2, 0, 1, 5],
         *   [4, 1, 0, 1, 7],
         *   [1, 0, 3, 0, 5]
         * ]
         *
         * sumRegion(2, 1, 4, 3) -> 8
         * sumRegion(1, 1, 2, 2) -> 11
         * sumRegion(1, 2, 2, 4) -> 12
         */
        int[][] matrix = new int[][]{{3, 0, 1, 4, 2},
                {5, 6, 3, 2, 1},
                {1, 2, 0, 1, 5},
                {4, 1, 0, 1, 7},
                {1, 0, 3, 0, 5}};
        numMatrixDPbyTopLeft numMatrix = new numMatrixDPbyTopLeft(matrix);
        assertEquals(8, numMatrix.sumRegion(2, 1, 4, 3));
        assertEquals(11, numMatrix.sumRegion(1, 1, 2, 2));
        assertEquals(12, numMatrix.sumRegion(1, 2, 2, 4));
    }
}
