import org.junit.Test;
import static org.junit.Assert.*;

public class maxProfit123 {

    /**
     * Say you have an array for which the ith element is the price of a given stock on day i.
     *
     * Design an algorithm to find the maximum profit. You may complete at most two transactions.
     *
     * Note: You may not engage in multiple transactions at the same time (i.e., you must sell the stock before you buy again).
     *
     * Approach: Dynamic Programming
     * 首先明确，因为每次只能做一个transaction，因此手上持有的股票数只能为0或者1，同时在第i-th天，总共用三种选择：buy，rest和sell，注意buy只能在sell之后，
     * 同时sell只能在buy之后。因此，若假设总共有n天，同时最多能做k笔交易。那么每天的状态为dp[i][k][0]和dp[i][k][1]，分别表示在第i天，最多进行k笔交易，手上
     * 没持有股票和正持有着一支股票的profit。
     * 初始化条件为dp[-1][k][0] = 0, dp[i][0][0] = 0表示当一支股票也没有或一笔交易也不让做的条件下，profit为0，同时dp[-1][k][1] = -Infinity,
     * dp[i][0][1] = -Infinity表示不可能在没有一支股票存在或一笔交易也让做的条件下持有一支股票（即profit为负无穷）
     *
     * 对于此题，因为最多只能做两笔交易，k = 2，因此每天总共有四种状态dp[i][2][0], dp[i][2][1], dp[i][1][0], dp[i][1][1]
     * 同时注意，每笔交易是成pair的即buy和sell的一对，只有当之前买进的股票sell之后，才会减少一次交易次数
     *
     * 状态转移方程为：
     * 若在第i天最多进行两笔交易的条件下不持有任何股票，只有可能由两种状态转移而来，即在第i-1天还剩两笔交易的条件下也没持有任何股票，或者将第i-1天持有的股票
     * （可能是之前买进一直持有到i - 1天），然后在第i天将其卖出。
     * dp[i][2][0] = max(dp[i - 1][2][0], dp[i - 1][2][1] + prices[i])
     *
     * 若在第i天最多进行两笔交易的条件下持有1支股票，也有两种可能，即在i - 1天时持有的一支股票在第i天rest，继续持有，或者在第i- 1天刚刚卖出一支股票（即消耗了
     * 一次交易）同时在第i天（今天）买进
     * dp[i][2][1] = max(dp[i - 1][2][1], dp[i - 1][1][0] - prices[i])
     *
     * 与第一种状态类似，但是只能进行一笔交易了。也是从第i-1天没有做任何动作（rest），或者将在第i-1天持有的股票卖掉
     * dp[i][1][0] = max(dp[i - 1][1][0], dp[i - 1][1][1] + prices[i])
     *
     * 与第二种状态类似，要么是第i - 1天持有的股票继续持有下去，或者是已经做了两次交易，再买进一次股票。因为dp[i][0][0] = 0，即不能做任何交易时的profit为0，
     * 因此可以将后者简化为-prices[i]，意义是，当不能再做任何交易时，买进股票（之后无法卖出），只能一直赔本。
     * dp[i][1][1] = max(dp[i - 1][1][1], dp[i - 1][0][0] - prices[i]) = max(dp[i - 1][1][1], -prices[i])
     *
     * 最终返回结果为dp[n][2][0]，即在最后一天做了两笔交易且不再持有任何股票的最大收益（最后一天不再持有任何股票一定是利润最大的，因为如果持有着一股，该股的利润
     * 为（-1）* 当时买进的价格。
     *
     * 同时根据如上转移状态方程，可以轻易的写出O(n)空间的算法，但注意到第i天的值，只和第i-1天有关，因此可以将维度降成O(1)
     *
     * Time: O(n)   需要循环整个数组找到最大profit
     * Space: O(1)
     */
    public int maxProfit(int[] prices) {
        int T_i10 = 0, T_i11 = Integer.MIN_VALUE;
        int T_i20 = 0, T_i21 = Integer.MIN_VALUE;
        for(int price : prices) {
            T_i20 = Math.max(T_i20, T_i21 + price);
            T_i21 = Math.max(T_i21, T_i10 - price);
            T_i10 = Math.max(T_i10, T_i11 + price);
            T_i11 = Math.max(T_i11, -price);
        }
        return T_i20;
    }

    @Test
    public void maxProfitTest() {
        /**
         * Example 1:
         * Input: [3,3,5,0,0,3,1,4]
         * Output: 6
         * Explanation: Buy on day 4 (price = 0) and sell on day 6 (price = 3), profit = 3-0 = 3.
         *              Then buy on day 7 (price = 1) and sell on day 8 (price = 4), profit = 4-1 = 3.
         */
        int[] prices1 = new int[]{3, 3, 5, 0, 0, 3, 1, 4};
        assertEquals(6, maxProfit(prices1));
        /**
         * Example 2:
         * Input: [1,2,3,4,5]
         * Output: 4
         * Explanation: Buy on day 1 (price = 1) and sell on day 5 (price = 5), profit = 5-1 = 4.
         *              Note that you cannot buy on day 1, buy on day 2 and sell them later, as you are
         *              engaging multiple transactions at the same time. You must sell before buying again.
         */
        int[] prices2= new int[]{1, 2, 3, 4, 5};
        assertEquals(4, maxProfit(prices2));
        /**
         * Example 3:
         * Input: [7,6,4,3,1]
         * Output: 0
         * Explanation: In this case, no transaction is done, i.e. max profit = 0.
         */
        int[] prices3= new int[]{7, 6, 4, 3, 1};
        assertEquals(0, maxProfit(prices3));
    }
}
