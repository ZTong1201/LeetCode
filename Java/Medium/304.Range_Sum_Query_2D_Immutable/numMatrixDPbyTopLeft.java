public class numMatrixDPbyTopLeft {

    /**
     * Approach 2: Top Left Sum
     * 可以类比1D情况下就算prefix sum，在2D中计算到当前节点为止，所有左上角元素的和。
     * ------------------
     * |     |          |
     * |  A  |     B    |
     * ------------------
     * |     |          |
     * |  C  |     D    |
     * |     |          |
     * ------------------（row2，col2）
     * 假设有上图四个区域，D区域的和为sum(ABCD) - sum(AC) - sum(AB) + sum(A)，因为在减去AB和AC时，A区域被算了两遍。需要再加回来。
     * 注意在任意index时，存储的是左上角的和。因此只要给定任意的(row, col1)和(row2, col2)都可以计算出中间区域的和，即
     * sum(row2 + 1, col2 + 1) - sum(row2 + 1, col1) - sum(row1, col2 + 1) + sum(row1, col1)
     *
     * Time: 初始化需要O(mn)时间，之后每次query只需要O(1)
     * Space: O(mn)，需要额外的(m + 1) * (n + 1)的二维数组来记录左上角的和。
     */
    private int[][] dp;

    public numMatrixDPbyTopLeft(int[][] matrix) {
        if(matrix.length != 0 && matrix[0].length != 0) {
            //注意要将dp的size初始化为(m + 1) * (n + 1)
            dp = new int[matrix.length + 1][matrix[0].length + 1];
            for(int i = 0; i < matrix.length; i++) {
                for(int j = 0; j < matrix[0].length; j++) {
                    dp[i + 1][j + 1] = dp[i + 1][j] + dp[i][j + 1] - dp[i][j] + matrix[i][j];
                }
            }
        }
    }

    public int sumRegion(int row1, int col1, int row2, int col2) {
        return dp[row2 + 1][col2 + 1] - dp[row2 + 1][col1] - dp[row1][col2 + 1] + dp[row1][col1];
    }
}
