import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class FindPeakElementII {

    /**
     * A peak element in a 2D grid is an element that is strictly greater than all of its adjacent neighbors to the left,
     * right, top, and bottom.
     * <p>
     * Given a 0-indexed m x n matrix mat where no two adjacent cells are equal, find any peak element mat[i][j] and
     * return the length 2 array [i,j].
     * <p>
     * You may assume that the entire matrix is surrounded by an outer perimeter with the value -1 in each cell.
     * <p>
     * You must write an algorithm that runs in O(m log(n)) or O(n log(m)) time.
     * <p>
     * Constraints:
     * m == mat.length
     * n == mat[i].length
     * 1 <= m, n <= 500
     * 1 <= mat[i][j] <= 10^5
     * No two adjacent cells are equal.
     * <p>
     * Approach: Global max + binary search
     * Similar to the 1-D version, the algorithm looks like this:
     * 1. find midpoint of row (m / 2)
     * 2. feed the entire row gird[m/2] into a helper method to find the global maximum ~ O(n)
     * 3. since the global maximum guarantees it's strictly larger than its left and right neighbors
     * 4. check whether the global maximum is also larger than its bottom neighbor
     * 5. if larger, search the row m / 2 rows
     * 6. otherwise, discard the top m / 2 rows and search the bottom m / 2 rows until find the 2-D peak
     * <p>
     * Time: O(n * logm)
     * Space: O(1)
     */
    public int[] findPeakGrid(int[][] mat) {
        int m = mat.length, n = mat[0].length;
        // execute binary search on rows
        int left = 0, right = m - 1;
        // the column index of peak cell will be updated later
        int col = -1;
        while (left < right) {
            int mid = (right - left) / 2 + left;
            // feed the middle row and find the global max of that row
            int colPeak = findMax(mat[mid]);
            // determine which half needs to be searched next
            if (mat[mid][colPeak] < mat[mid + 1][colPeak]) {
                left = mid + 1;
            } else {
                right = mid;
            }
            // update the potential column index for the peak cell
            col = colPeak;
        }
        // in the end, left points to the correct row index
        return new int[]{left, col};
    }

    private int findMax(int[] row) {
        int index = 0, colMax = row[0];
        for (int i = 1; i < row.length; i++) {
            if (row[i] > colMax) {
                index = i;
                colMax = row[i];
            }
        }
        return index;
    }

    @Test
    public void findPeakGridTest() {
        /**
         * Example 1:
         * Input: mat = [[1,4],[3,2]]
         * Output: [0,1]
         * Explanation: Both 3 and 4 are peak elements so [1,0] and [0,1] are both acceptable answers.
         */
        int[] expected1 = new int[]{0, 1};
        int[] actual1 = findPeakGrid(new int[][]{{1, 4}, {3, 2}});
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: mat = [[10,20,15],[21,30,14],[7,16,32]]
         * Output: [1,1]
         * Explanation: Both 30 and 32 are peak elements so [1,1] and [2,2] are both acceptable answers.
         */
        int[] expected2 = new int[]{1, 1};
        int[] actual2 = findPeakGrid(new int[][]{{10, 20, 15}, {21, 30, 14}, {7, 16, 32}});
        assertArrayEquals(expected1, actual1);
    }
}
