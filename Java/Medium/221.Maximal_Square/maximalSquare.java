import org.junit.Test;
import static org.junit.Assert.*;

public class maximalSquare {

    /**
     * Given a 2D binary matrix filled with 0's and 1's, find the largest square containing only 1's and return its area.
     *
     * Approach 1: Brute Force
     * 暴力解法就是对于每一个元素为1的起始index（注意只有当其为1的时候可能形成square），找到其最大可能的square，然后不断更新最大值就是最终结果
     *
     * Time: O((mn)^2) 最坏情况下，每个index都会遍历以它为起点的剩余matrix
     * Space: O(1)
     */
    public int maximalSquareBruteForce(char[][] matrix) {
        int res = 0;
        if(matrix == null || matrix.length == 0) return res;
        int row = matrix.length, col = matrix[0].length;
        for(int i = 0; i < row; i++) {
            for(int j = 0; j < row; j++) {
                //只考虑值为1的cell
                if(matrix[i][j] == '1') {
                    //用来记录以当前节点为起点的最大square长度
                    int currLen = 1;
                    //flag记录是否能构成当前长度的square
                    boolean flag = true;
                    //要保证以当前节点开始，加上当前长度不会溢出边界，同时当前长度能形成square
                    while (i + currLen < row && j + currLen < col && flag) {
                        //看列方向是否都为1
                        for (int k = i; k <= i + currLen; k++) {
                            if (matrix[k][j + currLen] == '0') {
                                flag = false;
                                break;
                            }
                        }

                        //看行方向是否都为1
                        for (int k = j; k <= j + currLen; k++) {
                            if (matrix[i + currLen][k] == '0') {
                                flag = false;
                                break;
                            }
                        }

                        //若当前长度能形成square，则扩大长度及时判断
                        if (flag) {
                            currLen++;
                        }
                    }
                    res = Math.max(res, currLen);
                }
            }
        }
        return res * res;
    }


    /**
     * Approach 2: 2-D DP
     *
     * 若考虑一个square的右下角元素，matrix[i][j]，其所能形成的最大square只由matrix[i - 1][j], matrix[i][j - 1], matrix[i - 1][j - 1]的最小值决定
     * e.g.
     * 1 0
     * 1 1
     * matrix[1][1]所能形成的最大左上角square为min(matrix[0][1], matrix[0][0], matrix[1][0]) + 1 = 1，即只有它自己能形成最大的square
     * 因为使用DP计算，所以每个cell的位置记录的就是以该位置为bottom right位置所能形成的square的最大长度。在更新每个位置的值时，同时再更新最大值即可。
     * 注意当位置在第一行或第一列时，会存在边界溢出的情况。所以可以初始化一个维度为m + 1 * n + 1的二维int数组，这样原始matrix的边界外都是0，可以有效处理
     * 边界溢出的情况。
     *
     * Time: O(mn) 只需要遍历一遍数组即可
     * Space: O(mn) 需要一个(m + 1) * (n + 1)的二维数组
     *
     */
    public int maximalSquare2D_DP(char[][] matrix) {
        int res = 0;
        if(matrix == null || matrix.length == 0) return res;
        int row = matrix.length, col = matrix[0].length;
        int[][] dp = new int[row + 1][col + 1];
        for(int i = 1; i <= row; i++) {
            for(int j = 1; j <= col; j++) {
                if(matrix[i - 1][j - 1] == '1') {
                    dp[i][j] = Math.min(dp[i - 1][j], Math.min(dp[i][j - 1], dp[i - 1][j - 1])) + 1;
                    res = Math.max(res, dp[i][j]);
                }
            }
        }
        return res * res;
    }

    /**
     * Approach 1: 1-D DP
     * 本质上，在计算dp[i][j]时，需要如下三个值dp[i - 1][j], dp[i - 1][j - 1], d[[i][j - 1]。而每次在更新值时，都是固定一行，对列进行更新。
     * 因此可以用一个一维数组来减少空间
     * 在计算第i行的值时，此时dp数组中存的值是第i - 1行的值，即dp[i - 1][0 ~ n]，因此在index j时，dp[i - 1][j]即保留在当前的dp[j]里，而在更新dp[j]之前，
     * dp[j - 1]已经被更新。注意更新前dp[j - 1]保存的值为dp[i - 1][j - 1]，更新保存的值即为dp[i][j - 1]，因此需要在更新dp[j - 1]时，将之前的dp[j - 1]
     * 记录下来，记为prev。至此，所需要的三个值都已存在，可以更新dp[j]即二维时的dp[i][j]。
     * 需要注意的时，更新只在matrix中元素为1时，若不为1，直接将dp[j]赋值为0。
     *
     * Time: O(mn) one-pass
     * Space: O(n) 只需要一个size为col + 1的一维数组即可
     */
    public int maximalSquare1D_DP(char[][] matrix) {
        int length = 0;
        if(matrix == null || matrix.length == 0) return length;
        int row = matrix.length, col = matrix[0].length;
        //将方法二的二维数组变为一维，按行更新即可
        int[] dp = new int[col + 1];
        //将最开始prev设为0，因为二维数组中在原边界的左侧和外侧初始化了一圈0
        int prev = 0;
        for(int i = 1; i <= row; i++) {
            for(int j = 1; j <= col; j++) {
                //将当前j的记录下来，用来更新prev，当j + 1后prev记得值即为dp[i - 1][j]
                int temp = dp[j];
                //当matrix中值为1才更新
                if(matrix[i - 1][j - 1] == '1') {
                    //对于新的j, prev = dp[i - 1][j - 1], dp[j - 1] = dp[i][j - 1], dp[j]尚未更新 = dp[i - 1][j]
                    dp[j] = Math.min(Math.min(prev, dp[j - 1]), dp[j]) + 1;
                    length = Math.max(length, dp[j]);
                } else {
                    //否则直接设为0
                    dp[j] = 0;
                }
                //每次都要更新prev的值
                prev = temp;
            }
        }
        return length * length;
    }

    @Test
    public void maximalSquareTest() {
        /**
         * Example 1:
         * Input:
         *
         * 1 0 1 0 0
         * 1 0 1 1 1
         * 1 1 1 1 1
         * 1 0 0 1 0
         *
         * Output: 4
         */
        char[][] matrix1 = new char[][]{{'1', '0', '1', '0', '0'},
                {'1', '0', '1', '1', '1'},
                {'1', '1', '1', '1', '1'},
                {'1', '0', '0', '1', '0'}};
        assertEquals(4, maximalSquareBruteForce(matrix1));
        assertEquals(4, maximalSquare2D_DP(matrix1));
        assertEquals(4, maximalSquare1D_DP(matrix1));
        /**
         * Example 2:
         * Input:
         *
         * 1 1 1 1
         * 1 1 1 1
         * 1 1 1 1
         *
         * Output: 9
         */
        char[][] matrix2 = new char[][]{{'1', '1', '1', '1'},
                {'1', '1', '1', '1'},
                {'1', '1', '1', '1'}};
        assertEquals(9, maximalSquareBruteForce(matrix2));
        assertEquals(9, maximalSquare2D_DP(matrix2));
        assertEquals(9, maximalSquare1D_DP(matrix2));
    }
}
