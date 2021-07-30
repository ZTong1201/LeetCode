import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SearchMatrix {

    /**
     * Write an efficient algorithm that searches for a value in an m x n matrix. This matrix has the following properties:
     * <p>
     * Integers in each row are sorted from left to right.
     * The first integer of each row is greater than the last integer of the previous row.
     * <p>
     * Constraints:
     * <p>
     * m == matrix.length
     * n == matrix[i].length
     * 1 <= m, n <= 100
     * -10^4 <= matrix[i][j], target <= 10^4
     * <p>
     * Approach: Binary Search
     * Based on the matrix properties, the entire m * n matrix can be actually treated a 1-D array of size m * n. The problem
     * downgrades to search the position in the array. We can give each cell an ID, and based on the id, the row and column
     * indexes can be computed as (id / column.length) and (id % column.length)
     * <p>
     * Time: O(log(mn))
     * Space: O(1)
     */
    public boolean searchMatrix(int[][] matrix, int target) {
        int m = matrix.length, n = matrix[0].length;
        // 使用左闭右开区间
        int left = 0, right = m * n;
        while (left < right) {
            int mid = (right - left) / 2 + left;
            // get midpoint value
            int pivot = matrix[mid / n][mid % n];
            if (pivot == target) return true;
            if (pivot < target) left = mid + 1;
            else right = mid;
        }
        return false;
    }

    @Test
    public void searchMatrixTest() {
        /**
         * Example 1:
         * Input: matrix = [[1,3,5,7],[10,11,16,20],[23,30,34,60]], target = 3
         * Output: true
         */
        int[][] matrix = new int[][]{{1, 3, 5, 7}, {10, 11, 16, 20}, {23, 30, 34, 60}};
        assertTrue(searchMatrix(matrix, 3));
        /**
         * Example 2:
         * Input: matrix = [[1,3,5,7],[10,11,16,20],[23,30,34,60]], target = 13
         * Output: false
         */
        assertFalse(searchMatrix(matrix, 13));
    }
}
