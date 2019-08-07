import org.junit.Test;
import static org.junit.Assert.*;

public class paintHouse {

    /**
     * There are a row of n houses, each house can be painted with one of the three colors: red, blue or green. The cost of painting
     * each house with a certain color is different. You have to paint all the houses such that no two adjacent houses have the same color.
     *
     * The cost of painting each house with a certain color is represented by a n x 3 cost matrix. For example, costs[0][0] is the cost
     * of painting house 0 with color red; costs[1][2] is the cost of painting house 1 with color green, and so on...
     * Find the minimum cost to paint all houses.
     *
     * Note:
     * All costs are positive integers.
     *
     * Approach: Dynamic Programming
     * 可以额外构建一个n x 3的matrix来记录转移状态。
     * 初始化：第一行为costs数组的第一行，因为paint第0个房子需要的cost就是数组中的元素
     * 转移状态：从第二行开始，将该房子paint成对应颜色的最小cost = 上一个房子paint其他两种颜色的cost的最小值 + 当前cost
     * e.g. 对于红色：dp[i][0] = min(dp[i - 1][1], dp[i - 1][2]) + costs[i][0]
     * 对于其他颜色同理，最后只需返回最后一行的最小值
     *
     * Time: O(3n) = O(n) 遍历所有格子
     * Space: O(3n) = O(n) 需要一个额外的二维数组
     */
    public int minCost(int[][] costs) {
        if(costs == null) return 0;
        int row = costs.length;
        int[][] dp = new int[row][3];
        dp[0] = costs[0];
        for(int i = 1; i < row; i++) {
            dp[i][0] = Math.min(dp[i - 1][1], dp[i - 1][2]) + costs[i][0];
            dp[i][1] = Math.min(dp[i - 1][0], dp[i - 1][2]) + costs[i][1];
            dp[i][2] = Math.min(dp[i - 1][0], dp[i - 1][1]) + costs[i][2];
        }
        return Math.min(dp[row - 1][0], Math.min(dp[row - 1][1], dp[row - 1][2]));
    }


    @Test
    public void minCostTest() {
        /**
         * Example:
         * Input: [[17,2,17],[16,16,5],[14,3,19]]
         * Output: 10
         * Explanation: Paint house 0 into blue, paint house 1 into green, paint house 2 into blue.
         *              Minimum cost: 2 + 5 + 3 = 10.
         */
        int[][] costs = new int[][]{{17, 2, 17}, {16, 16, 5}, {14, 3, 19}};
        assertEquals(10, minCost(costs));
    }
}
