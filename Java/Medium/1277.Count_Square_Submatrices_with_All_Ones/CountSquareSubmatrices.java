import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CountSquareSubmatrices {

    /**
     * Given a m * n matrix of ones and zeros, return how many square submatrices have all ones.
     * <p>
     * Constraints:
     * <p>
     * 1 <= arr.length <= 300
     * 1 <= arr[0].length <= 300
     * 0 <= arr[i][j] <= 1
     * <p>
     * Approach 1: Enumerate all submatrices - DP
     * We can keep a 2-D array as a prefix sum table where sum[i][j] means the sum of the rectangle from (0,0) to (i,j).
     * Then at each position, we can list all possible submatricies in O(min(i, j)) time to get the sum of that submatrices.
     * If the sum equals to the square value (e.g. 1, 4, 9, etc.), then increment the count because it's a valid square
     * submatrices with all ones.
     * <p>
     * Time: O(m * n * min(m, n)) we need to go through the 2-D matrix and find all possible length of submatrices
     * Space: O(m * n)
     */
    public int countSquaresAllSubmatrices(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        int[][] sum = new int[m][n];
        sum[0][0] = matrix[0][0];
        // initialize first row
        for (int i = 1; i < n; i++) {
            sum[0][i] = matrix[0][i] == 0 ? sum[0][i - 1] : sum[0][i - 1] + 1;
        }
        // initialize first column
        for (int i = 1; i < m; i++) {
            sum[i][0] = matrix[i][0] == 0 ? sum[i - 1][0] : sum[i - 1][0] + 1;
        }
        // compute the sum of each cell
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                int prevSum = sum[i - 1][j] + sum[i][j - 1] - sum[i - 1][j - 1];
                sum[i][j] = matrix[i][j] == 0 ? prevSum : prevSum + 1;
            }
        }

        int count = 0;
        // list all possible submatrices at index (i, j) and see if it equals the desired square number
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                for (int size = 1; size <= Math.min(i, j) + 1; size++) {
                    int top = (j - size) >= 0 ? sum[i][j - size] : 0;
                    int left = (i - size) >= 0 ? sum[i - size][j] : 0;
                    int complement = ((i - size) >= 0 && (j - size) >= 0) ? sum[i - size][j - size] : 0;
                    if (sum[i][j] - top - left + complement == size * size) count++;
                }
            }
        }
        return count;
    }

    /**
     * Approach 2: Optimized DP
     * We can actually compute the total count on the fly. Denote dp[i][j] as
     * 1. the largest size of square with matrix[i][j] as its bottom-right corner
     * 2. the number of squares with matrix[i][j] as its bottom-right corner
     * if matrix[i][j] == 0, there is no way we can construct any squares at (i, j) - just skip
     * and this will also be helpful subsequently if we want to form a larger square at (i, j)
     * <p>
     * Time: O(m * n)
     * Space: O(1)
     */
    public int countSquaresDP(int[][] matrix) {
        int res = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                // skip 0 cell
                if (i > 0 && j > 0 && matrix[i][j] == 1) {
                    matrix[i][j] = Math.min(matrix[i - 1][j - 1], Math.min(matrix[i][j - 1], matrix[i - 1][j])) + 1;
                }
                res += matrix[i][j];
            }
        }
        return res;
    }

    @Test
    public void countSquaresTest() {
        /**
         * Example 1:
         * Input: matrix =
         * [
         *   [0,1,1,1],
         *   [1,1,1,1],
         *   [0,1,1,1]
         * ]
         * Output: 15
         * Explanation: 
         * There are 10 squares of side 1.
         * There are 4 squares of side 2.
         * There is  1 square of side 3.
         * Total number of squares = 10 + 4 + 1 = 15.
         */
        int[][] matrix1 = new int[][]{{0, 1, 1, 1}, {1, 1, 1, 1}, {0, 1, 1, 1}};
        assertEquals(15, countSquaresAllSubmatrices(matrix1));
        assertEquals(15, countSquaresDP(matrix1));
        /**
         * Example 2:
         * Input: matrix =
         * [
         *   [1,0,1],
         *   [1,1,0],
         *   [1,1,0]
         * ]
         * Output: 7
         * Explanation:
         * There are 6 squares of side 1.
         * There is 1 square of side 2.
         * Total number of squares = 6 + 1 = 7.
         */
        int[][] matrix2 = new int[][]{{1, 0, 1}, {1, 1, 0}, {1, 1, 0}};
        assertEquals(7, countSquaresAllSubmatrices(matrix2));
        assertEquals(7, countSquaresDP(matrix2));
    }
}
