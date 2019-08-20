import org.junit.Test;
import static org.junit.Assert.*;

public class maxProfitWithCooldown {

    /**
     * Say you have an array for which the ith element is the price of a given stock on day i.
     *
     * Design an algorithm to find the maximum profit. You may complete as many transactions as you like (ie, buy one and sell one share
     * of the stock multiple times) with the following restrictions:
     *
     * You may not engage in multiple transactions at the same time (ie, you must sell the stock before you buy again).
     * After you sell your stock, you cannot buy stock on next day. (ie, cooldown 1 day)
     *
     * Approach 1: Dynamic Programming (With Extra Space)
     * 对于此问题，每天其实可以有三种状态，hold， sold以及rest，分别代表：
     * hold[i]：在第i天，前i - 1天中买卖股票的收益，或者经过一天cooldown之后，重新持有股票，所能收获的profit最大值
     * sold[i]：表示第i天卖掉当前持有股票所能收获的最大收益
     * rest[i]：在第i天，前一天刚刚卖出股票所能获得的最大收益，或者继续保持cooldown状态所能得到的收益，两者中的最大值。
     *
     * 转移状态为：
     *                       rest
     *                    |----------|         rest
     *                    --> rest <--   <-----------------
     *                         |                          |
     *                         | buy                      |
     *                         |              sell        |
     *                         --> hold  ------------->  sold
     *                         |    ^
     *                         |    |
     *                         -----
     *                          hold
     *
     * rest[i] = max(rest[i - 1], sold[i - 1])
     * hold[i] = max(hold[i - 1], rest[i - 1] - prices[i])   减掉prices意味着买进股票需要支出
     * sold[i] = hold[i - 1] + prices[i]                     加上prices意味着卖掉股票能得到的收益
     *
     * 初始化: sold[0] = 0, rest[0] = 0，表示没有买卖过股票，收益为0, hold[0] = Integer.MIN_VALUE，表示从未持有股票，没有收益
     * 最后一天的最大收益要么是刚好最后一天卖出股票，要么是之前卖出股票还在cooldown状态，两者去最大值，即返回max(rest[n], sold[n])
     *
     * Time: O(n)
     * Space: O(n) 需要三个数组来记录三种状态
     */
    public int maxProfitDPWithSpace(int[] prices) {
        int length = prices.length;
        int[] hold = new int[length + 1];
        int[] sold = new int[length + 1];
        int[] rest = new int[length + 1];
        hold[0] = Integer.MIN_VALUE;
        sold[0] = 0;
        rest[0] = 0;
        for(int i = 1; i <= length; i++) {
            hold[i] = Math.max(rest[i - 1] - prices[i - 1], hold[i - 1]);
            sold[i] = hold[i - 1] + prices[i - 1];
            rest[i] = Math.max(rest[i - 1], sold[i - 1]);
        }
        return Math.max(rest[length], sold[length]);
    }

    /**
     * Approach 2: DP Without Extra Space
     * 因为该DP过程每次只看前一次的状态，因此可以滚动式的更新当前值即可。无需额外数组。
     *
     * Time: O(n)
     * Space: O(1)
     */
    public int maxProfitDPWithoutExtraSpace(int[] prices) {
        int sold = 0, rest = 0, hold = Integer.MIN_VALUE;
        for(int price : prices) {
            //为了更新rest的状态，需要记下前一天的sold所能得到的最大收益
            int prevSold = sold;
            sold = hold + price;
            hold = Math.max(rest - price, hold);
            rest = Math.max(rest, prevSold);
        }
        return Math.max(rest, sold);
    }

    @Test
    public void maxProfitTest() {
        /**
         * Example 1:
         * Input: [1,2,3,0,2]
         * Output: 3
         * Explanation: transactions = [buy, sell, cooldown, buy, sell]
         */
        int[] prices1 = new int[]{1, 2, 3, 0, 2};
        assertEquals(3, maxProfitDPWithSpace(prices1));
        /**
         * Example 2:
         * Input: [6,1,3,2,4,7]
         * Output: 6
         * Explanation: transactions = [x, buy, x, x, x, sell]
         */
        int[] prices2 = new int[]{6, 1, 3, 2, 4, 7};
        assertEquals(6, maxProfitDPWithSpace(prices2));
    }
}
