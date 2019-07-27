import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;

public class coinChange {

    /**
     * You are given coins of different denominations and a total amount of money amount. Write a function to compute the fewest number
     * of coins that you need to make up that amount. If that amount of money cannot be made up by any combination of the coins, return -1
     *
     * Note:
     * You may assume that you have an infinite number of each kind of coin.
     *
     * Approach 1: Dynamic programming
     *
     * We need to construct an array to store the minimum coin usage from 1 to the target amount. Since we know that the minimum value of
     * an integer coin must be 1. Hence, the maximum coin usage would be equal to amount. Hence, we can initialize the index from 1 to amount
     * with value amount + 1. Thus, when the final result is large than amount, it means we can not change coin with the coins given, return -1.
     *
     * At a given amount, if the amount is large than the coin given, we can actually search the number of ways to change (amount - coin value).
     * Because, we can easily change coins by adding one more coin with that coin value. As long as amount - coin value is >= 0, we keep searching
     * the smallest number for coin change, then add one more coin is the desired coin usage for that amount.
     *
     * Time: O(n*amount) where n is the number of coins. In the worst case, for 1 - amount values we care about, we have to search all the coin
     *      values to get the desired result.
     * Space: O(n) we need an array to store coin usage
     */
    public int coinChangeDP(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        //fill all the entries with amount + 1
        Arrays.fill(dp, amount + 1);
        //however, the first value will be 0, indicating we need no coins to make up amount 0
        dp[0] = 0;
        //loop through all the amounts
        for(int i = 1; i <= amount; i++) {
            //then check for every coin value
            for(int coin : coins) {
                //if we can use that coin value
                if(i - coin >= 0) {
                    //find the minimum coin usage
                    dp[i] = Math.min(dp[i], dp[i - coin] + 1);
                }
            }
        }
        //if dp[amount] is larger than the amount, which means we cannot make up the amount using coins given, return -1
        return dp[amount] > amount ? -1 : dp[amount];
    }

    /**
     * Approach 2: Greedy
     *
     * Actually, in order to use the least number of coins, we want to use the coin with the largest value as many as possible. Hence, by
     * greedily always change the left amount with the largest coin we can use, we will end with the smallest number of coin usage.
     * Therefore, we need to initially sort the coin array to get the largest coin we can change easily. Then as long as the amount is not
     * divisible by the current coin, we keep searching for a smaller coin value. Note that we need to make sure to use as many coins as
     * possible for the current coin value before we move to the next coin. If at any time, the amount reaches 0 (or can be divisible by
     * current coin value), we return the result. We will initialize the result with Integer.MAX_VALUE, hence, if we cannot change amount
     * with coins given, the result will remain unchanged. If that is the case, we will return -1 in the end.
     *
     * Time: (nlogn + n*amount), the typical sorting algorithm will cost O(nlogn) runtime. In the worst case, we need to search amount times
     *      and each searching process will check every coin in the coins array.
     * Space: O(amount) the call stack will require O(amount) space in the worst case
     */
    private int res;

    public int coinChangeGreedy(int[] coins, int amount) {
        this.res = Integer.MAX_VALUE;
        //sort the coins array for greedy algorithm
        Arrays.sort(coins);
        //since it is a greedy algorithm, we need to search starting from the largest coin value
        helper(coins, 0, coins.length - 1, amount);
        return this.res == Integer.MAX_VALUE ? -1 : this.res;
    }

    private void helper(int[] coins, int used, int curr, int amount) {
        //base case, since we search coins from the largest to the smallest, if index < 0, we simply return
        if(curr < 0) {
            return;
        }
        //if the amount left is divisible by the current coin value
        //we add the coin usage to the final result and return it
        if(amount % coins[curr] == 0) {
            this.res = Math.min(this.res, used + amount / coins[curr]);
            return;
        }
        //as long as the amount left is not divisible by current coin value
        //we keep searching by using a smaller coin value
        for(int i = amount / coins[curr]; i >= 0; i--) {
            //if the coins usage is larger than the smallest result we have, simply return
            if(used + i > this.res) return;
            //otherwise, we update the current coin usage, the amount left over, and decrement index to use a smaller value of coin
            helper(coins, used + i, curr - 1, amount - i * coins[curr]);
        }
    }

    @Test
    public void coinChangeTest() {
        /**
         * Example 1:
         * Input: coins = [1, 2, 5], amount = 11
         * Output: 3
         * Explanation: 11 = 5 + 5 + 1
         */
        int[] coins1 = new int[]{1, 2, 5};
        assertEquals(3, coinChangeDP(coins1, 11));
        assertEquals(3, coinChangeGreedy(coins1, 11));
        /**
         * Example 2:
         * Input: coins = [2], amount = 3
         * Output: -1
         */
        int[] coins2 = new int[]{2};
        assertEquals(-1, coinChangeDP(coins2, 3));
        assertEquals(-1, coinChangeGreedy(coins2, 3));
    }
}
