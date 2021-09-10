import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ValidSquare {

    /**
     * Given the coordinates of four points in 2D space p1, p2, p3 and p4, return true if the four points construct a square.
     * <p>
     * The coordinate of a point pi is represented as [xi, yi]. The input is not given in any order.
     * <p>
     * A valid square has four equal sides with positive length and four equal angles (90-degree angles).
     * <p>
     * Constraints:
     * <p>
     * p1.length == p2.length == p3.length == p4.length == 2
     * -10^4 <= xi, yi <= 10^4
     * <p>
     * Approach: All combinations
     * Basically, we just need to compute the length of 4 edges and the length of diagonals. If these 4 points can actually
     * make a square, then there could be only 2 unique values of lengths which connect two arbitrary points. The key part
     * is to determine which two points construct a diagonal. If we fix the point 1, then no matter what the layout look like,
     * there are only 3 options of forming a square.
     * <p>
     * 1. p1 and p3 makes the diagonal (p2 & p4 is another)
     * 2. p1 and p2 makes the diagonal (p3 & p4 is another)
     * 3. p1 and p4 makes the diagonal (p2 & p3 is another)
     * Therefore, for a given layout, we only need check four edges, e.g. (p1, p2), (p2, p3), (p3, p4) and (p4, p1) are of
     * the same length, and two diagonals, e.g. (p1, p3) and (p2, p4) are of the same length.
     * Note that there could be edge cases when two points are the same which gives 0 length. Need to make sure all lengths
     * are > 0.
     * <p>
     * Time: O(1) for given 4 points, there are only 3 possible layouts and for each layout we check the equality of 4 edges
     * and 2 diagonals
     * Space: O(1)
     */
    public boolean validSquare(int[] p1, int[] p2, int[] p3, int[] p4) {
        // for given 4 points, there are only 3 different combinations of diagonal formation
        return isValidSquare(p1, p2, p3, p4) || isValidSquare(p1, p3, p2, p4) || isValidSquare(p1, p2, p4, p3);
    }

    private boolean isValidSquare(int[] p1, int[] p2, int[] p3, int[] p4) {
        // check the following conditions
        // 1. there are at least two adjacent edges > 0
        // 2. 4 edges are of the same length
        // 3. 2 diagonals are of the same length
        return distance(p1, p2) > 0 && distance(p1, p3) > 0 && distance(p1, p2) == distance(p2, p3)
                && distance(p2, p3) == distance(p3, p4) && distance(p3, p4) == distance(p4, p1)
                && distance(p1, p3) == distance(p2, p4);
    }

    private int distance(int[] point1, int[] point2) {
        return (point1[0] - point2[0]) * (point1[0] - point2[0]) + (point1[1] - point2[1]) * (point1[1] - point2[1]);
    }

    @Test
    public void validSquareTest() {
        /**
         * Example 1:
         * Input: p1 = [0,0], p2 = [1,1], p3 = [1,0], p4 = [0,1]
         * Output: true
         */
        assertTrue(validSquare(new int[]{0, 0}, new int[]{1, 1}, new int[]{1, 0}, new int[]{0, 1}));
        /**
         * Example 2:
         * Input: p1 = [0,0], p2 = [1,1], p3 = [1,0], p4 = [0,12]
         * Output: false
         */
        assertFalse(validSquare(new int[]{0, 0}, new int[]{1, 1}, new int[]{1, 0}, new int[]{0, 12}));
        /**
         * Example 3:
         * Input: p1 = [1,0], p2 = [-1,0], p3 = [0,1], p4 = [0,-1]
         * Output: true
         */
        assertTrue(validSquare(new int[]{1, 0}, new int[]{-1, 0}, new int[]{0, 1}, new int[]{0, -1}));
        /**
         * Example 4:
         * Input: p1 = [0,0], p2 = [2,2], p3 = [0,0], p4 = [2,2]
         * Output: false
         */
        assertFalse(validSquare(new int[]{0, 0}, new int[]{2, 2}, new int[]{0, 0}, new int[]{2, 2}));
    }
}
