import org.junit.Test;
import static org.junit.Assert.*;

public class maxProfit {

    /**
     * Say you have an array for which the ith element is the price of a given stock on day i.
     *
     * Design an algorithm to find the maximum profit. You may complete as many transactions as you like
     * (i.e., buy one and sell one share of the stock multiple times).
     *
     * Note: You may not engage in multiple transactions at the same time (i.e., you must sell the stock before you buy again).
     *
     * Approach 1: Greedy (Dynamic programming)
     * Since we are not allowed to be involved in multiple transactions. The max profit will be obtained by always buying at the lowest
     * and sell at the highest before we encounter another low price. We can simply convert this process to a DP. Use an integer array
     * to record current max profit. As long as prices[i] > prices[i - 1], we accumulate profit until we find a low price.
     *
     * Time: O(n) one pass of the input array
     * Space: O(n) to store max profit so far
     */
    public int maxProfitDP(int[] prices) {
        int length = prices.length;
        if(length <= 1) return 0;
        int[] profit = new int[length];
        for(int i = 1; i < length; i++) {
            profit[i] = profit[i - 1] + Math.max(0, prices[i] - prices[i - 1]);
        }
        return profit[length - 1];
    }

    /**
     * Approach 2: Greedy
     * We can actually omit the extra space for this problem. The final max profit is just the sum of all the positive numbers of
     * prices[i - 1] - prices[i]. As discussed before, as long as prices[i - 1] - prices[i], we keep accumulating the profit.
     *
     * Time: O(n) simple one pass
     * Space: O(1) no extra space required
     */
    public int maxProfitGreedy(int[] prices) {
        int maxProfit = 0;
        for(int i = 1; i < prices.length; i++) {
            if(prices[i] > prices[i - 1]) maxProfit += prices[i] - prices[i - 1];
        }
        return maxProfit;
    }


    @Test
    public void maxProfitTest() {
        /**
         * Example 1:
         * Input: [7,1,5,3,6,4]
         * Output: 7
         * Explanation: Buy on day 2 (price = 1) and sell on day 3 (price = 5), profit = 5-1 = 4.
         *              Then buy on day 4 (price = 3) and sell on day 5 (price = 6), profit = 6-3 = 3.
         */
        int[] prices1 = new int[]{7, 1, 5, 3, 6, 4};
        assertEquals(7, maxProfitDP(prices1));
        assertEquals(7, maxProfitGreedy(prices1));
        /**
         * Example 2:
         * Input: [1,2,3,4,5]
         * Output: 4
         * Explanation: Buy on day 1 (price = 1) and sell on day 5 (price = 5), profit = 5-1 = 4.
         *              Note that you cannot buy on day 1, buy on day 2 and sell them later, as you are
         *              engaging multiple transactions at the same time. You must sell before buying again.
         */
        int[] prices2 = new int[]{1, 2, 3, 4, 5};
        assertEquals(4, maxProfitDP(prices2));
        assertEquals(4, maxProfitGreedy(prices2));
        /**
         * Example 3:
         * Input: [7,6,4,3,1]
         * Output: 0
         * Explanation: In this case, no transaction is done, i.e. max profit = 0.
         */
        int[] prices3 = new int[]{7, 6, 4, 3, 1};
        assertEquals(0, maxProfitDP(prices3));
        assertEquals(0, maxProfitGreedy(prices3));
    }
}
