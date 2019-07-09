import org.junit.Test;
import static org.junit.Assert.*;

public class BestTimeToBuyAndSell {

    /**
     * Say you have an array for which the ith element is the price of a given stock on day i.
     *
     * If you were only permitted to complete at most one transaction
     * (i.e., buy one and sell one share of the stock), design an algorithm to find the maximum profit.
     *
     * Note that you cannot sell a stock before you buy one.
     *
     * Actually we're finding the maximum and minimum point in the array. We can solve it by one pass.
     *
     * Time: O(N)
     * Space: O(1)
     */
    public int maxProfit(int[] prices) {
        int res = 0;
        int currMin = Integer.MAX_VALUE;
        for(int i = 0; i < prices.length; i++) {
            if(prices[i] < currMin) currMin = prices[i];
            else if(prices[i] - currMin > res) res = prices[i] - currMin;
        }
        return res;
    }

    /**
     * We can also use dynamic programming method, update max profit at each price, and then return the final value in the array
     *
     * Time: O(N) still one pass
     * Space: O(1) no extra space needed
     */
    public int maxProfitDP(int[] prices) {
        int currMin = Integer.MAX_VALUE;
        for(int i = 0; i < prices.length; i++) {
            if(prices[i] < currMin) {
                currMin = prices[i];
                prices[i] = i == 0 ? 0 : prices[i - 1];
            } else {
                prices[i] = Math.max(prices[i - 1], prices[i] - currMin);
            }
        }
        return prices.length == 0 ? 0 : prices[prices.length - 1];
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
         * Input: []
         * Output: 0
         */
        int[] prices3 = new int[0];
        assertEquals(0, maxProfitDP(prices3));
        /**
         * Example 4:
         * Input: [2, 4, 1]
         * Output: 2
         */
        int[] prices4 = new int[]{2, 4, 1};
        assertEquals(2, maxProfitDP(prices4));
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
         * Input: []
         * Output: 0
         */
        int[] prices3 = new int[0];
        assertEquals(0, maxProfit(prices3));
        /**
         * Example 4:
         * Input: [2, 4, 1]
         * Output: 2
         */
        int[] prices4 = new int[]{2, 4, 1};
        assertEquals(2, maxProfit(prices4));
    }
}
