import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BestTimeToBuyAndSell {

    /**
     * Say you have an array for which the ith element is the price of a given stock on day i.
     * <p>
     * If you were only permitted to complete at most one transaction
     * (i.e., buy one and sell one share of the stock), design an algorithm to find the maximum profit.
     * <p>
     * Note that you cannot sell a stock before you buy one.
     * <p>
     * Constraints:
     * <p>
     * 1 <= prices.length <= 105
     * 0 <= prices[i] <= 104
     * <p>
     * Approach 1: Finding minimum and maximum in one pass
     * <p>
     * Actually we're finding the maximum and minimum point in the array. We can solve it by one pass.
     * <p>
     * Time: O(N)
     * Space: O(1)
     */
    public int maxProfit(int[] prices) {
        int res = 0;
        int currMin = prices[0];
        for (int price : prices) {
            if (price < currMin) currMin = price;
            else if (price - currMin > res) res = price - currMin;
        }
        return res;
    }

    /**
     * Approach 2: Dynamic Programming
     * We can also use dynamic programming method, update max profit at each price, and then return the final value
     * in the array
     * <p>
     * Time: O(N) still one pass
     * Space: O(1) no extra space needed
     */
    public int maxProfitDP(int[] prices) {
        int res = 0, currMin = prices[0];
        for (int price : prices) {
            res = Math.max(res, price - currMin);
            currMin = Math.min(price, currMin);
        }
        return res;
    }

    @Test
    public void maxProfitDPTest() {
        /**
         * Example 1:
         * Input: [7,1,5,3,6,4]
         * Output: 5
         */
        int[] prices1 = new int[]{7, 1, 5, 3, 6, 4};
        assertEquals(5, maxProfitDP(prices1));
        /**
         * Example 2:
         * Input: [7,6,4,3,1]
         * Output: 0
         */
        int[] prices2 = new int[]{7, 6, 4, 3, 1};
        assertEquals(0, maxProfitDP(prices2));
        /**
         * Example 3:
         * Input: [2, 4, 1]
         * Output: 2
         */
        int[] prices3 = new int[]{2, 4, 1};
        assertEquals(2, maxProfitDP(prices3));
    }


    @Test
    public void maxProfitTest() {
        /**
         * Example 1:
         * Input: [7,1,5,3,6,4]
         * Output: 5
         */
        int[] prices1 = new int[]{7, 1, 5, 3, 6, 4};
        assertEquals(5, maxProfit(prices1));
        /**
         * Example 2:
         * Input: [7,6,4,3,1]
         * Output: 0
         */
        int[] prices2 = new int[]{7, 6, 4, 3, 1};
        assertEquals(0, maxProfit(prices2));
        /**
         * Example 3:
         * Input: [2, 4, 1]
         * Output: 2
         */
        int[] prices3 = new int[]{2, 4, 1};
        assertEquals(2, maxProfit(prices3));
    }
}
