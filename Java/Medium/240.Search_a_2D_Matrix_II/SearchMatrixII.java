import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SearchMatrixII {

    /**
     * Write an efficient algorithm that searches for a target value in an m x n integer matrix. The matrix has the
     * following properties:
     * <p>
     * Integers in each row are sorted in ascending from left to right.
     * Integers in each column are sorted in ascending from top to bottom.
     * <p>
     * Constraints:
     * <p>
     * m == matrix.length
     * n == matrix[i].length
     * 1 <= n, m <= 300
     * -10^9 <= matix[i][j] <= 10^9
     * All the integers in each row are sorted in ascending order.
     * All the integers in each column are sorted in ascending order.
     * -10^9 <= target <= 10^9
     * <p>
     * Approach 1: Binary search
     * Since either the column or the row is sorted, we can use binary search to speed up the search performance. Basically,
     * for each row, we can search both vertically and horizontally to see whether the target is in that direction.
     * <p>
     * Time: The time complexity is not very obvious. Assume m is the number of rows and n is the number of columns. The
     * iteration would stop when hitting min(m, n). In each iteration call, we will do two binary searches in arrays of size
     * (m - i) and (n - i). Hence the time complexity will be O(log(m - i) + log(n - i)), in the worst case n ~ m (otherwise
     * either m or n will dominate the runtime), O(2 * log(n - i)) = O(log(n - i)).
     * The total runtime would be O(logn) + O(log(n - 1)) + O(log(n - 2)) + ... O(log1) = O(logn!). When n is large, n! << n^n
     * hence the runtime will be better than O(logn^n) = O(n * logn)
     * Space: O(1)
     */
    public boolean searchMatrixBinarySearch(int[][] matrix, int target) {
        int shortDim = Math.min(matrix.length, matrix[0].length);
        // the iteration stops when hitting either boundary
        for (int i = 0; i < shortDim; i++) {
            // starting from the top left corner
            boolean horizontalFound = binarySearch(matrix, i, target, false);
            boolean verticalFound = binarySearch(matrix, i, target, true);
            // if the target is found in either direction - return true;
            if (horizontalFound || verticalFound) return true;
        }
        return false;
    }

    private boolean binarySearch(int[][] matrix, int start, int target, boolean isVertical) {
        // updating the row index while searching vertically
        // or updating the column index for horizontal search
        // 左闭右开区间
        int left = 0, right = isVertical ? matrix.length : matrix[0].length;
        while (left < right) {
            int mid = (right - left) / 2 + left;
            if (isVertical) {
                // row index in changed
                if (matrix[mid][start] == target) return true;
                if (matrix[mid][start] < target) left = mid + 1;
                else right = mid;
            } else {
                // column index is changed
                if (matrix[start][mid] == target) return true;
                if (matrix[start][mid] < target) left = mid + 1;
                else right = mid;
            }
        }
        return false;
    }

    /**
     * Approach 2: Search Space Reduction
     * Since the matrix is sorted left to right, top to bottom, we can prune O(m) or O(n) values at each particular value.
     * Basically, we initialize the starting point in the bottom-left corner (or top-right corner, the result will be the same)
     * Because vertically it's the largest value, and it's the smallest horizontally. Given any particular cell, we simply
     * 1. return true if it equals to the target value
     * 2. decrement row if it's larger than target
     * 3. increment col if it's smaller than target
     * <p>
     * Time: O(m + n), at each iteration either the row is decremented or the col is incremented one time, also note that the
     * number of increment/decrement won't exceed O(m) or O(n)
     * Space: O(1)
     */
    public boolean searchMatrixSpaceReduction(int[][] matrix, int target) {
        // start from bottom left corner
        int row = matrix.length - 1, col = 0;

        // keep searching while within the grid
        while (row >= 0 && col < matrix[0].length) {
            if (matrix[row][col] == target) return true;
            if (matrix[row][col] > target) row--;
            else col++;
        }
        return false;
    }

    /**
     * Approach 3: Divide and conquer
     * Given a cell, it can partition the entire matrix into 4 sections, and it's guaranteed two of them won't contain target
     * and the rest of them might contain.
     * <p>
     * Base case:
     * 1. If the sub-matrix is empty, no way to contain target -> return false
     * 2. If target is smaller than the top-left value (the smallest one in the sub-matrix) or it's larger than the bottom right
     * value (the largest one) -> return false
     * <p>
     * Recursion:
     * Given base case is correctly handled, then the top-left and bottom-right sub-matrices will definitely not contain target,
     * hence in each recursion call, we will only search the rest of two sections.
     * <p>
     * Time: O(n * logn)
     * Space: O(logn) for call stack
     */
    public boolean searchMatrixDivideAndConquer(int[][] matrix, int target) {
        return searchRectangle(matrix, target, 0, 0, matrix[0].length - 1, matrix.length - 1);
    }

    private boolean searchRectangle(int[][] matrix, int target, int left, int top, int right, int bottom) {
        // base case 1: if matrix is empty
        if (left > right || top > bottom) return false;
            // base case 2: if target is smaller than top left value or larger than bottom right
        else if (target < matrix[top][left] || target > matrix[bottom][right]) return false;
        // find midpoint horizontally (columns) to achieve divide and conquer
        int mid = (right - left) / 2 + left;
        // we can completely discard the top left and the bottom right section
        // since they have been handled by the base case
        // find a pivot row between [top, bottom] in the middle column
        // such that matrix[row-1][mid] < target < matrix[row][mid]
        int row = top;
        while (row <= bottom && matrix[row][mid] <= target) {
            // found the value
            if (matrix[row][mid] == target) return true;
            row++;
        }
        // search the top right and the bottom left sub-matrices based on new boundaries
        // only one side might contain target - use or (||)
        return searchRectangle(matrix, target, left, row, mid - 1, bottom) ||
                searchRectangle(matrix, target, mid + 1, top, right, row - 1);
    }


    @Test
    public void searchMatrixTest() {
        /**
         * Example 1:
         * Input: matrix = [[1,4,7,11,15],[2,5,8,12,19],[3,6,9,16,22],[10,13,14,17,24],[18,21,23,26,30]], target = 5
         * Output: true
         */
        int[][] matrix = new int[][]{{1, 4, 7, 11, 15}, {2, 5, 8, 12, 19}, {3, 6, 9, 16, 12},
                {10, 13, 14, 17, 24}, {18, 21, 23, 26, 39}};
        assertTrue(searchMatrixBinarySearch(matrix, 5));
        assertTrue(searchMatrixSpaceReduction(matrix, 5));
        assertTrue(searchMatrixDivideAndConquer(matrix, 5));
        /**
         * Example 2:
         * Input: matrix = [[1,4,7,11,15],[2,5,8,12,19],[3,6,9,16,22],[10,13,14,17,24],[18,21,23,26,30]], target = 20
         * Output: false
         */
        assertFalse(searchMatrixBinarySearch(matrix, 20));
        assertFalse(searchMatrixSpaceReduction(matrix, 20));
        assertFalse(searchMatrixDivideAndConquer(matrix, 20));
    }
}
