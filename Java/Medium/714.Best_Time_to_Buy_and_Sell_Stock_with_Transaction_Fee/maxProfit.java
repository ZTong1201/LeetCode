import org.junit.Test;
import static org.junit.Assert.*;

public class maxProfit {

    /**
     * Your are given an array of integers prices, for which the i-th element is the price of a given stock on day i; and a non-negative
     * integer fee representing a transaction fee.
     *
     * You may complete as many transactions as you like, but you need to pay the transaction fee for each transaction. You may not buy
     * more than 1 share of a stock at a time (ie. you must sell the stock share before you buy again.)
     *
     * Return the maximum profit you can make.
     *
     * Approach: Dynamic Programming
     * General case讨论和123一致。唯一的区别是，可以做无限次交易，同时每次交易需要transaction fee（一般考虑在卖出时收取），在做无限次交易时，具体能做的交易
     * 总数其实受prices的长度控制，因此可以认为k和k - 1时的值一样，即dp[i - 1][k - 1][0] = dp[i - 1][k][0]，以及dp[i - 1][k - 1][1] = dp[i - 1][k][1]
     * 因此每一天还是只有两种状态，即dp[i][k][0]和dp[i][k][1]
     * 初始化时仍然将dp[i][0][0] = 0, dp[i][0][1] = -Infinity
     *
     * 根据上述讨论，状态转移为：
     * 若在第i天不持有任何股票，可能是从i - 1天不持有任何股票转移过来，也有可能是将第i - 1天持有的股票卖出转移过来（注意收取transaction fee）
     * dp[i][k][0] = max(dp[i - 1][k][0], dp[i - 1][k][1] + price - fee)
     *
     * 若在第i天持有一支股票，可能是从第i - 1天持有股票不做任何事（rest）转移过来，也可能是在第i - 1天没有持有任何股票的前提下，第i天买进一支股票
     * dp[i][k][1] = max(dp[i - 1][k][1], dp[i - 1][k][0] - price)
     *
     * 最后返回dp[i][k][0]即可
     *
     *
     * Time: O(n) 需要遍历整个prices数组
     * Space: O(1) 观察到第i天的值只取决于第i - 1天，因此可能将空间降为常数
     */
    public int maxProfit(int[] prices, int fee) {
        //因此存在减去transaction fee的操作，可能出现整型移除，先转换为long，最后返回结果时再转换回int
        long T_ik0 = 0, T_ik1 = Integer.MIN_VALUE;
        for(int price : prices) {
            long T_ik0_old = T_ik0;
            T_ik0 = Math.max(T_ik0, T_ik1 + price - fee);
            T_ik1 = Math.max(T_ik1, T_ik0_old - price);
        }
        return (int) T_ik0;
    }

    @Test
    public void maxProfitTest() {
        /**
         * Example:
         * Input: prices = [1, 3, 2, 8, 4, 9], fee = 2
         * Output: 8
         * Explanation: The maximum profit can be achieved by:
         * Buying at prices[0] = 1
         * Selling at prices[3] = 8
         * Buying at prices[4] = 4
         * Selling at prices[5] = 9
         * The total profit is ((8 - 1) - 2) + ((9 - 4) - 2) = 8.
         */
        int[] prices = new int[]{1, 3, 2, 8, 4, 9};
        assertEquals(8, maxProfit(prices, 2));
    }
}
