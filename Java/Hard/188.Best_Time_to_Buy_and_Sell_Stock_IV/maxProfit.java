import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;

public class maxProfit {

    /**
     * Say you have an array for which the i-th element is the price of a given stock on day i.
     *
     * Design an algorithm to find the maximum profit. You may complete at most k transactions.
     *
     * Note:
     * You may not engage in multiple transactions at the same time (ie, you must sell the stock before you buy again).
     *
     * Approach: Dynamic Programming
     * 与123题讨论一致，首先要判断k的大小，因为一买一卖至少需要2天，对于一个size为n的数组来说，最多能进行n/2次交易操作，当输入的k大于等于n/2时，再大的k也不会
     * 对最终结果产生影响，这就等同于可以进行infinite次交易操作。对于此种情况，可以用解决infinite次交易的方法先在常数空间，O(n)时间内解决此类问题。
     * 对于其他情况，和123题类似，对于k次交易次数限制，在第i天，总共应该有k * 2个状态，表示可以进行1 - k次操作，持有0支或1支股票。即dp[i][1][0],
     * dp[i][1][1], dp[i][2][0], dp[i][2][1], ..., dp[i][k][0], dp[i][k][1]。根据之前讨论，因为第i天的状态只与第i - 1天的状态相关，可以将交易次数
     * 为k的状态降为O(1)空间，因此对于k次交易来说，可以用一个size为k + 1的数组（因为需要1 - k的值）滚动地更新每个状态的值。值得注意的是，对于重新买进股票
     * 的操作，需要从当前交易次数减1的状态转移过来。因为当股票卖出后，可交易次数会减1。
     *
     * 初始化条件仍为dp[i][0][0] = 0, dp[i][0][1] = -Infinity
     * 同时，因为希望在更新交易次数更小的状态之后，不影响后续交易次数更大的状态更新，对于第i天，选择从交易次数位k倒序更新至交易次数为1
     *
     * Time: O(nk) 对于每一天，需要更新k个状态
     * Space: O(k) 需要两个size为k + 1的数组分别记录每一天每个交易次数条件下的最大profit
     */
    public int maxProfit(int k, int[] prices) {
        //若可交易次数大于n/2，对于任何的输入k，其结果等同于不限制交易次数得到的结果
        if(k >= prices.length / 2) {
            return quickSolver(prices);
        }
        //与general case一直，每一个交易次数k，需要两个状态：持有或不持有股票
        int[] T_ik0 = new int[k + 1];
        int[] T_ik1 = new int[k + 1];
        Arrays.fill(T_ik1, Integer.MIN_VALUE);
        //对于每一天的股价，都需要单独更新交易次数从1 - k的两个状态
        for(int price : prices) {
            //同时为了先更新的状态不影响之后的状态更新，选择倒序更新
            for(int i = k; i > 0; i--) {
                T_ik0[i] = Math.max(T_ik0[i], T_ik1[i] + price);
                //对于重新买进股票的状态，因为之前刚刚卖出一次股票，意味着完成了一次交易，因此要从当前交易次数 - 1的状态转移过来
                T_ik1[i] = Math.max(T_ik1[1], T_ik0[i - 1] - price);
            }
        }
        return T_ik0[k];
    }

    private int quickSolver(int[] prices) {
        int T_ik0 = 0, T_ik1 = Integer.MIN_VALUE;
        for(int price : prices) {
            int T_ik0_old = T_ik0;
            T_ik0 = Math.max(T_ik0, T_ik1 + price);
            T_ik1 = Math.max(T_ik1, T_ik0_old - price);
        }
        return T_ik0;
    }

    @Test
    public void maxProfitTest() {
        /**
         * Example 1:
         * Input: [2,4,1], k = 2
         * Output: 2
         * Explanation: Buy on day 1 (price = 2) and sell on day 2 (price = 4), profit = 4-2 = 2.
         */
        int[] prices1 = new int[]{2, 4, 1};
        assertEquals(2, maxProfit(2, prices1));
        /**
         * Example 2:
         * Input: [3,2,6,5,0,3], k = 2
         * Output: 7
         * Explanation: Buy on day 2 (price = 2) and sell on day 3 (price = 6), profit = 6-2 = 4.
         *              Then buy on day 5 (price = 0) and sell on day 6 (price = 3), profit = 3-0 = 3.
         */
        int[] prices2 = new int[]{3, 2, 6, 5, 0, 3};
        assertEquals(7, maxProfit(2, prices2));
    }
}
