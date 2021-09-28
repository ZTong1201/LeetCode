import org.junit.Test;

import java.util.Arrays;
import java.util.Stack;

import static org.junit.Assert.assertEquals;

public class maximalRectangle {

    /**
     * Given a 2D binary matrix filled with 0's and 1's, find the largest rectangle containing only 1's and return its area.
     * <p>
     * Approach 1: Better Brute Force.
     * Brute Force解法为找到所有的rectangle组合，然后判断该rectangle是否符合条件（全为'1'），最后返回面积最大值。所有rectangle组合为O((mn)^2)个，判断
     * 一个rectangle是否符合条件需要O(mn)，因此总的运行时间为O((mn)^3)
     * <p>
     * 可以使用动态规划，记录当前这一行，每个位置所能形成的最大宽度，即若matrix[i][j] = '1'，则dp[i][j] = dp[i][j - 1] + 1。然后对应这一行的每个位置，
     * 沿着列向上看，所构成的rectangle的高度为orginalRow - currRow + 1，而所能形成的最大rectangle的宽度则由两行的最小宽度决定。即对应每个位置，可以寻找
     * 以该位置为rectangle右下角的所有rectangle，更新最大值即可
     * <p>
     * Time: O(MN^2) 需要遍历整个矩阵更新每一行每个位置所能形成的最大宽度，同时对应每个为1的位置，需要查看在它上面的所有行，来找到以它为右下角的矩形的最大面积。
     * 最坏情况下需要遍历N行
     * Space: O(MN) 需要一个额外的二维数组记录每行每个位置所能形成的最大宽度。
     */
    public int maximalRectangleBetterBruteForce(char[][] matrix) {
        int maxArea = 0;
        if (matrix.length == 0) return maxArea;
        int row = matrix.length, col = matrix[0].length;
        int[][] dp = new int[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (matrix[i][j] == '1') {
                    //注意edge case，若当前为某一行的第一个位置，若其值为'1'，则直接将dp赋成1即可
                    dp[i][j] = j == 0 ? 1 : dp[i][j - 1] + 1;
                    int width = dp[i][j];

                    for (int k = i; k >= 0; k--) {
                        //从当前行往上找，找到两个宽度的最小值为所能形成的矩形宽度最大值
                        width = Math.min(width, dp[k][j]);
                        maxArea = Math.max(maxArea, width * (i - k + 1));
                        //可以early stop，若某一行的最大宽度已经为0，再往上无法再形成矩形
                        //因此可以直接停止向上寻找
                        if (width == 0) {
                            break;
                        }
                    }
                }
            }
        }
        return maxArea;
    }

    /**
     * Approach 2: Using Histogram (With Stack)
     * 可以将问题转化成84题Largest Rectangle in Histogram，即先通过动态规划算出每一行每个位置所能达到的最大高度（histogram），若当前位置为'1'，则
     * 该位置所能形成的最大高度为上一行高度 + 1，反之则为0。因为状态转移只和上一行有关，因此可以只用一个一维数组，记录当前行的高度。在得到了当前行的最大高度后，
     * 可以利用单调栈，即保持栈内元素为单调递增的，当遇到某元素小于栈顶元素时，说明前面所能形成的histogram更大，将栈顶元素顶出，更新最大面积。然后再继续判断。
     * <p>
     * Time: O(NM)，对于每一行，首先要遍历一遍更新最大高度O(M)，只会把这一行的值传递进求最大面积的函数，仍然要遍历一遍，依旧为O(M)，总共行数为N，运行时间为O(MN)
     * Space: O(M)，只需要一个一维数组，记录这一行最大高度，同时在求最大面积时，需要一个stack，若高度一直单调递增，则也需要O(M)空间
     */
    public int maximalRectangleStack(char[][] matrix) {
        int maxArea = 0;
        if (matrix.length == 0) return maxArea;
        int[] dp = new int[matrix[0].length];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {

                dp[j] = matrix[i][j] == '1' ? dp[j] + 1 : 0;
            }
            maxArea = Math.max(maxArea, findLargest(dp));
        }
        return maxArea;
    }

    private int findLargest(int[] heights) {
        Stack<Integer> stack = new Stack<>();
        int max = 0;
        stack.push(-1);
        for (int i = 0; i < heights.length; i++) {
            while (stack.peek() != -1 && heights[stack.peek()] >= heights[i]) {
                max = Math.max(max, heights[stack.pop()] * (i - stack.peek() - 1));
            }
            stack.push(i);
        }
        while (stack.peek() != -1) {
            max = Math.max(max, heights[stack.pop()] * (heights.length - stack.peek() - 1));
        }
        return max;
    }

    /**
     * Approach 3: DP: Maximal Area at each point
     * 根据上述方法分析，在某位置所能形成的最大矩形面积其实由三部分决定，从当前位置向上所能达到的最大高度height，以及向左向右所能达到的最大边界left和right
     * 因此，对于每一行的每一个元素，可以分别利用DP的方式计算其height，left，right。
     * 可以用三个一维数组，记录某一行的每个位置的最优值。在计算第i行时，此时height，left和right中分别记录的是第i - 1行每个位置的最优值。
     * 更新规则如下：
     * height：与之前相同，若当前位置为'1'，则其所能达到的最大高度为上一行的最大高度 + 1，反之则为0
     * left：left表示的在当前位置左边的rightmost的0的下一个index，若rightmost的0的index为j，则left记为j + 1，表示当前矩形所能到达的最左端
     * right：与left类似，表示的在当前位置右边的leftmost的0的index，之所以不用前一个index，是因为在此问题中若考虑左闭右开的区间[l, r)，在计算面积时
     * 可以直接用height * (right - left)，而不用height * (right - left + 1）。同时为保证找到leftmost的0，需要从后往前遍历。
     * <p>
     * 在每一行，更新完上述三个值之后，在从前往后遍历一遍，计算每个位置所能达到的最大值。
     * <p>
     * <p>
     * Time: O(NM)，对于每一行，需要4次不同的单向遍历，每次的runtime都为O(M)，为了计算最终结果，要遍历N行，因此overall的时间为O(NM)
     * Space: O(M)，需要3个和col数相等的数组分别记录height，left和right
     */
    public int maximalRectangleDP(char[][] matrix) {
        int maxArea = 0;
        if (matrix.length == 0) return maxArea;
        int rows = matrix.length;
        int cols = matrix[0].length;
        int[] height = new int[cols], left = new int[cols], right = new int[cols];
        //先将右边界都设成matrix的最右端
        Arrays.fill(right, cols);

        for (int i = 0; i < rows; i++) {
            //先计算每个位置所能向上达到的最大高度
            for (int j = 0; j < cols; j++) {
                height[j] = matrix[i][j] == '1' ? height[j] + 1 : 0;
            }

            //在计算每个位置所能达到的左边界，其值为当前的左边界和上一行的左边界的最大值。
            int currLeft = 0;
            for (int j = 0; j < cols; j++) {
                //若当前位置为1，则找到最大的left边界
                if (matrix[i][j] == '1') {
                    left[j] = Math.max(left[j], currLeft);
                } else {
                    //反之，left边界为0，并将currLeft设为current index + 1
                    left[j] = 0;
                    currLeft = j + 1;
                }
            }

            //倒序计算每个位置的右边界，其值为当前的右边界和上一行的右边界的最小值
            int currRight = cols;
            for (int j = cols - 1; j >= 0; j--) {
                //若当前位置为1，则找到最小的右边界
                if (matrix[i][j] == '1') {
                    right[j] = Math.min(right[j], currRight);
                } else {
                    //反之，right边界为矩阵最右端，并将currRight设为当前的index
                    right[j] = cols;
                    currRight = j;
                }
            }

            //计算完上述值之后，更新最大面积
            for (int j = 0; j < cols; j++) {
                maxArea = Math.max(maxArea, height[j] * (right[j] - left[j]));
            }
        }
        return maxArea;
    }


    @Test
    public void maximalRectangleTest() {
        /**
         * Example:
         * Input:
         * [
         *   ['1','0','1','0','0'],
         *   ['1','0','1','1','1'],
         *   ['1','1','1','1','1'],
         *   ['1','0','0','1','0']
         * ]
         * Output: 6
         */
        char[][] matrix = new char[][]{
                {'1', '0', '1', '0', '0'},
                {'1', '0', '1', '1', '1'},
                {'1', '1', '1', '1', '1'},
                {'1', '0', '0', '1', '0'}};
        assertEquals(6, maximalRectangleBetterBruteForce(matrix));
        assertEquals(6, maximalRectangleStack(matrix));
        assertEquals(6, maximalRectangleDP(matrix));
    }
}
