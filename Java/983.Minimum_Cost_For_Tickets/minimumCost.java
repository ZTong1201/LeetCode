import org.junit.Test;
import static org.junit.Assert.*;

public class minimumCost {

    /**
     * In a country popular for train travel, you have planned some train travelling one year in advance.
     * The days of the year that you will travel is given as an array days.  Each day is an integer from 1 to 365.
     *
     * Train tickets are sold in 3 different ways:
     *
     * 1. a 1-day pass is sold for costs[0] dollars;
     * 2. a 7-day pass is sold for costs[1] dollars;
     * 3. a 30-day pass is sold for costs[2] dollars.
     * The passes allow that many days of consecutive travel.
     * For example, if we get a 7-day pass on day 2, then we can travel for 7 days: day 2, 3, 4, 5, 6, 7, and 8.
     *
     * Return the minimum number of dollars you need to travel every day in the given list of days.
     *
     * This problem is a typical dynamic programming problem. We can build an array which has the length of largest day + 1,
     * hence the index from 1 to length - 1 corresponds to each day. If we don't have to travel on each day, the best strategy is
     * to wait to buy an appropriate pass. If we have to travel today, we have to decide which pass should buy.
     * Specifically, dp[i] = min(dp[i - 1] + costs[0], dp[i - 7] + costs[1], dp[i - 30] + costs[2])
     *                              buy a day pass       buy a week pass        buy a month pass
     * Note that if i - 7 or i - 30 < 0, which means we haven't come into a week or a month frame, simply assign its value to 0.
     * If i - 7 or i - 30 >= 0, we will buy a week or a month pass if and only if the min cost before this week or this month +
     * the cost of week pass or month pass gives the minimum cost.
     *
     * Time: O(N) = O(1), where N is the largest day in the days array, however, the largest possible day is 365, the overall runtime is O(1)
     * Space: O(N) = O(1), we need an array corresponds to each day
     */
    public int mincostTickets(int[] days, int[] costs) {
        int[] minCost = new int[days[days.length - 1] + 1];
        int index = 0;
        for(int i = 1; i < minCost.length; i++) {
            if(days[index] != i) {
                minCost[i] = minCost[i - 1];
                continue;
            }
            int prevWeek = minCost[Math.max(0, i - 7)];
            int prevMonth = minCost[Math.max(0, i - 30)];
            minCost[i] = Math.min(minCost[i - 1] + costs[0], Math.min(prevWeek + costs[1], prevMonth + costs[2]));
            index += 1;
        }
        return minCost[minCost.length - 1];
    }

    @Test
    public void mincostTicketsTest() {
        /**
         * Example 1:
         * Input: days = [1,4,6,7,8,20], costs = [2,7,15]
         * Output: 11
         * Explanation:
         * For example, here is one way to buy passes that lets you travel your travel plan:
         * On day 1, you bought a 1-day pass for costs[0] = $2, which covered day 1.
         * On day 3, you bought a 7-day pass for costs[1] = $7, which covered days 3, 4, ..., 9.
         * On day 20, you bought a 1-day pass for costs[0] = $2, which covered day 20.
         * In total you spent $11 and covered all the days of your travel.
         */
        int[] days1 = new int[]{1, 4, 6, 7, 8, 20};
        int[] costs1 = new int[]{2, 7, 15};
        int actual1 = mincostTickets(days1, costs1);
        assertEquals(11, actual1);
        /**
         * Example 2:
         * Input: days = [1,2,3,4,5,6,7,8,9,10,30,31], costs = [2,7,15]
         * Output: 17
         * Explanation:
         * For example, here is one way to buy passes that lets you travel your travel plan:
         * On day 1, you bought a 30-day pass for costs[2] = $15 which covered days 1, 2, ..., 30.
         * On day 31, you bought a 1-day pass for costs[0] = $2 which covered day 31.
         * In total you spent $17 and covered all the days of your travel.
         */
        int[] days2 = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 30, 31};
        int[] costs2 = new int[]{2, 7, 15};
        int actual2 = mincostTickets(days2, costs2);
        assertEquals(17, actual2);
    }
}
