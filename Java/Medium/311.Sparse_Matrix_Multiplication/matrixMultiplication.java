import org.junit.Test;
import static org.junit.Assert.*;

public class matrixMultiplication {

    /**
     * Given two sparse matrices A and B, return the result of AB.
     * You may assume that A's column number is equal to B's row number.
     *
     * Approach 1: Brute Force
     * 直接遵循矩阵相乘规律，若A为size M * P, B为size P * N，那么相乘得到的矩阵的size为M * N，同时res[i][j] = sum(A[i][0 -> p] * B[0 -> p][i])
     *
     * Time: O(MNP) 对于新矩阵的每个位置（总共MN个位置），都需要进行P次相乘运算以及P次相加运算，因此总的runtime为O(MNP)
     * Space: O(MN) 相乘的结果矩阵为O(MN)空间
     */
    public int[][] multiply(int[][] A, int[][] B) {
        int M = A.length, P = A[0].length, N = B[0].length;
        int[][] res = new int[M][N];
        for(int i = 0; i < M; i++) {
            for(int j = 0; j < N; j++) {
                int sum = 0;
                for(int k = 0; k < P; k++) {
                    sum += A[i][k] * B[k][j];
                }
                res[i][j] = sum;
            }
        }
        return res;
    }

    /**
     * Approach 2: Using Sparsity
     * 因为输入的两个矩阵都是sparse的，所以可以利用这个性质来减少运算的次数。首先明确，如果A中的某个位置A[i][j]不为0，那么对应B中第j行的每个元素都会受到
     * 该值的影响，即在结果矩阵中，C[i][k]都需要加上A[i][j] * B[j][k]的值。
     * 因此可以简化矩阵乘法为
     * 1.遍历整个矩阵A，若找到A[i][j]不为0
     * 2.则在矩阵B中找到第j行的元素，将两者相乘放在结果矩阵C[i][k]中
     *
     * 之所以这种矩阵相乘可以简化运算步骤，是因为若矩阵A中的某一列都为0，那么矩阵B中对应的那一行就不会被访问。当矩阵很大而且很sparse的时候，A矩阵中全为0的
     * 列数可能很多，这样就大大简化了对矩阵B的访问和不必要的运算
     *
     * Time: O(MNP)，运算的upper bound依旧为O(MNP)，但实际中会远远小于这个值
     * Space: O(MN)
     */
    public int[][] multiplySparsity(int[][] A, int[][] B) {
        int M = A.length, P = A[0].length, N = B[0].length;
        int[][] res = new int[M][N];
        for(int i = 0; i < M; i++ ) {
            for(int j = 0; j < P; j++) {
                if(A[i][j] != 0) {
                    for(int k = 0; k < N; k++) {
                        res[i][k] += A[i][j] * B[j][k];
                    }
                }
            }
        }
        return res;
    }


    @Test
    public void multiplyTest() {
        /**
         * Example:
         * Input:
         *
         * A = [
         *   [ 1, 0, 0],
         *   [-1, 0, 3]
         * ]
         *
         * B = [
         *   [ 7, 0, 0 ],
         *   [ 0, 0, 0 ],
         *   [ 0, 0, 1 ]
         * ]
         *
         * Output:
         *
         *      |  1 0 0 |   | 7 0 0 |   |  7 0 0 |
         * AB = | -1 0 3 | x | 0 0 0 | = | -7 0 3 |
         *                   | 0 0 1 |
         */
        int[][] A = new int[][]{{1, 0, 0}, {-1, 0, 3}};
        int[][] B = new int[][]{{7, 0, 0}, {0, 0, 0}, {0, 0, 1}};
        int[][] expected = new int[][]{{7, 0, 0}, {-7, 0, 3}};
        int[][] actual1 = multiply(A, B);
        assertArrayEquals(expected, actual1);
        int[][] actual2 = multiplySparsity(A, B);
        assertArrayEquals(expected, actual2);
    }
}
