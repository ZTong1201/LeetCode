import org.junit.Test;
import static org.junit.Assert.*;


public class rotateImage {

    /**
     * You are given an n x n 2D matrix representing an image.
     *
     * Rotate the image by 90 degrees (clockwise).
     *
     * Note:
     * You have to rotate the image in-place, which means you have to modify the input 2D matrix directly.
     * DO NOT allocate another 2D matrix and do the rotation.
     *
     * Approach 1: Transpose and reverse
     * The simplest algorithm will be transpose the matrix first. By doing so, every element is in the correct row. Then we reverse
     * each row, the elements are in the correct column.
     *
     *
     * Time:O(N^2), we need to visited all the elements to rotate an image
     * Space: O(1), in-place algorithm
     */
    public void rotateTransposeAndReverse(int[][] matrix) {
        int n = matrix.length; //since it is a nxn matrix

        //First, transpose the image
        for(int i = 0; i < n; i++) {
            for(int j = i; j < n; j++) {
                int tmp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = tmp;
            }
        }

        //Second, reverse each row
        for(int i = 0; i < n; i++) {
            int left = 0, right = n - 1;
            while(left < right) {
                int tmp = matrix[i][left];
                matrix[i][left] = matrix[i][right];
                matrix[i][right] = tmp;
                left += 1;
                right -= 1;
            }
        }
    }

    /**
     * Approach 2: Rotate four rectangles
     * Actually, we don't need to transpose and reverse the whole matrix. We should only care about the top left corner. Based on this
     * small rectangle block, we can find its corresponding top-right, bottom-right, and bottom-left rectangles. The only tricky part
     * for this algorithm is that, when the size of matrix is an odd number, we need to consider one more row and column. Since when we
     * find one element need to be changed, we actually target all of them in four rectangles, we should only consider row or column.
     * DO NOT consider it twice!
     *
     * Important note:
     * In order to compute next clockwise position, we have
     * x = prev row;
     * new row = prev col;
     * new col = n - 1 - prev row (x);
     *
     * Time: O(N^2) we still need to loop through about N/2*N/2 = (N^2)/4 elements
     * Space: O(1)
     */
    public void rotateRectangle(int[][] matrix) {
        int n = matrix.length;
        //for odd size matrix, we need to consider one more row
        for(int i = 0; i < (n + 1) / 2; i++) {
            for(int j = 0; j < n / 2; j++) {
                int[] tmp = new int[4];  //we need to store 4 values in 4 rectangles
                int row = i;
                int col = j;
                for(int k = 0; k < 4; k++) {
                    tmp[k] = matrix[row][col];
                    int x = row;   //a temporary value to record previous row
                    row = col;
                    col = n - 1 - x;   //computing these two values, we actually move the next clockwise position
                }
                //after 4 loops, the row and col move back to the original position
                //we now loop through 4 times (clockwise) to assign correct value to each position
                for(int k = 0; k < 4; k++) {
                    matrix[row][col] = tmp[(k + 3) % 4];
                    int x = row;
                    row = col;
                    col = n - 1 - x;
                }
            }
        }
    }

    /**
     * Approach 3: Interchange values simultaneously
     * The tmp array to store all the clockwise values and reassign them is redundant. We can actually interchange their values
     * in one pass. However, since we want to rotate the image by 90 degrees clockwise, if we assign value clockwisely, there is no way
     * to retrieve back the previous value when we move to next position. Therefore, we need to interchange them in counter-clockwise
     * direction.
     *
     * Hence, the computation is slightly difference now:
     * int x = prev col;
     * new col = prev row;
     * new row = n - 1 - prev col (x)
     */
    public void rotateRectangleOnePass(int[][] matrix) {
        int n = matrix.length;

        for(int i = 0; i < (n + 1) / 2; i++) {
            for(int j = 0; j < n / 2; j++) {
                int tmp = matrix[n - 1 - j][i];
                matrix[n - 1 - j][i] = matrix[n - 1 - i][n - 1 - j];
                matrix[n - 1 - i][n - 1 - j] = matrix[j][n - 1 - i];
                matrix[j][n - 1 - i] = matrix[i][j];
                matrix[i][j] = tmp;
            }
        }
    }

    @Test
    public void rotateRectangleOnePassTest() {
        /**
         * Example 1:
         * Given input matrix =
         * [
         *   [1,2,3],
         *   [4,5,6],
         *   [7,8,9]
         * ],
         *
         * rotate the input matrix in-place such that it becomes:
         * [
         *   [7,4,1],
         *   [8,5,2],
         *   [9,6,3]
         * ]
         */
        int[][] matrix1 = new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        rotateRectangleOnePass(matrix1);
        int[][] expected1 = new int[][]{{7, 4, 1}, {8, 5, 2}, {9, 6, 3}};
        assertArrayEquals(expected1, matrix1);
        /**
         * Example 2:
         * Given input matrix =
         * [
         *   [ 5, 1, 9,11],
         *   [ 2, 4, 8,10],
         *   [13, 3, 6, 7],
         *   [15,14,12,16]
         * ],
         *
         * rotate the input matrix in-place such that it becomes:
         * [
         *   [15,13, 2, 5],
         *   [14, 3, 4, 1],
         *   [12, 6, 8, 9],
         *   [16, 7,10,11]
         * ]
         */
        int[][] matrix2 = new int[][]{{5, 1, 9, 11}, {2, 4, 8, 10}, {13, 3, 6, 7}, {15, 14, 12, 16}};
        rotateRectangleOnePass(matrix2);
        int[][] expected2 = new int[][]{{15, 13, 2, 5}, {14, 3, 4, 1}, {12, 6, 8, 9}, {16, 7, 10, 11}};
        assertArrayEquals(expected2, matrix2);
    }

    @Test
    public void rotateRectangleTest() {
        /**
         * Example 1:
         * Given input matrix =
         * [
         *   [1,2,3],
         *   [4,5,6],
         *   [7,8,9]
         * ],
         *
         * rotate the input matrix in-place such that it becomes:
         * [
         *   [7,4,1],
         *   [8,5,2],
         *   [9,6,3]
         * ]
         */
        int[][] matrix1 = new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        rotateRectangle(matrix1);
        int[][] expected1 = new int[][]{{7, 4, 1}, {8, 5, 2}, {9, 6, 3}};
        assertArrayEquals(expected1, matrix1);
        /**
         * Example 2:
         * Given input matrix =
         * [
         *   [ 5, 1, 9,11],
         *   [ 2, 4, 8,10],
         *   [13, 3, 6, 7],
         *   [15,14,12,16]
         * ],
         *
         * rotate the input matrix in-place such that it becomes:
         * [
         *   [15,13, 2, 5],
         *   [14, 3, 4, 1],
         *   [12, 6, 8, 9],
         *   [16, 7,10,11]
         * ]
         */
        int[][] matrix2 = new int[][]{{5, 1, 9, 11}, {2, 4, 8, 10}, {13, 3, 6, 7}, {15, 14, 12, 16}};
        rotateRectangle(matrix2);
        int[][] expected2 = new int[][]{{15, 13, 2, 5}, {14, 3, 4, 1}, {12, 6, 8, 9}, {16, 7, 10, 11}};
        assertArrayEquals(expected2, matrix2);
    }

    @Test
    public void rotateTransposeAndReverseTest() {
        /**
         * Example 1:
         * Given input matrix =
         * [
         *   [1,2,3],
         *   [4,5,6],
         *   [7,8,9]
         * ],
         *
         * rotate the input matrix in-place such that it becomes:
         * [
         *   [7,4,1],
         *   [8,5,2],
         *   [9,6,3]
         * ]
         */
        int[][] matrix1 = new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        rotateTransposeAndReverse(matrix1);
        int[][] expected1 = new int[][]{{7, 4, 1}, {8, 5, 2}, {9, 6, 3}};
        assertArrayEquals(expected1, matrix1);
        /**
         * Example 2:
         * Given input matrix =
         * [
         *   [ 5, 1, 9,11],
         *   [ 2, 4, 8,10],
         *   [13, 3, 6, 7],
         *   [15,14,12,16]
         * ],
         *
         * rotate the input matrix in-place such that it becomes:
         * [
         *   [15,13, 2, 5],
         *   [14, 3, 4, 1],
         *   [12, 6, 8, 9],
         *   [16, 7,10,11]
         * ]
         */
        int[][] matrix2 = new int[][]{{5, 1, 9, 11}, {2, 4, 8, 10}, {13, 3, 6, 7}, {15, 14, 12, 16}};
        rotateTransposeAndReverse(matrix2);
        int[][] expected2 = new int[][]{{15, 13, 2, 5}, {14, 3, 4, 1}, {12, 6, 8, 9}, {16, 7, 10, 11}};
        assertArrayEquals(expected2, matrix2);
    }
}
