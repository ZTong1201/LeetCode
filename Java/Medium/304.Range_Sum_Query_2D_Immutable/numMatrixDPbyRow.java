public class numMatrixDPbyRow {

    /**
     * Given a 2D matrix matrix, find the sum of the elements inside the rectangle defined by its upper left corner (row1, col1)
     * and lower right corner (row2, col2).
     *
     * Note:
     * You may assume that the matrix does not change.
     * There are many calls to sumRegion function.
     * You may assume that row1 ≤ row2 and col1 ≤ col2.
     *
     * Approach 1: 和1D类似，可以对每一行提前计算prefix sum，这样在当前行内计算任意range sum时，时间都为O(n)，对于给定query从(row1, col1)到(row2, col2)
     * 将从row1到row2每一行的sum[col2 + 1] - sum[col1]累加起来即为最终结果
     *
     * Time: 初始化需要O(mn)时间，每次query时，计算每一行的值需要O(1)，总共需要计算(row2 - row1 + 1)行，所以时间为O(row2 - row1 + 1)，最坏为O(m)
     * Space: O(mn)，需要一个size为m * (n + 1)的二维数组，计算每一行的prefix sum
     */
    private int[][] dp;

    public numMatrixDPbyRow(int[][] matrix) {
        //注意edge case，当matrix为null或者空数组时，不做任何处理
        if(matrix.length != 0 && matrix[0].length != 0) {
            dp = new int[matrix.length][matrix[0].length + 1];
            for(int i = 0; i < matrix.length; i++) {
                for(int j = 0; j < matrix[0].length; j++) {
                    dp[i][j + 1] = dp[i][j] + matrix[i][j];
                }
            }
        }
    }

    public int sumRegion(int row1, int col1, int row2, int col2) {
        int res = 0;
        for(int i = row1; i <= row2; i++) {
            res += dp[i][col2 + 1] - dp[i][col1];
        }
        return res;
    }
}
