import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class GuessNumberHigherOrLowerII {

    /**
     * We are playing the Guessing Game. The game will work as follows:
     * <p>
     * I pick a number between 1 and n.
     * You guess a number.
     * If you guess the right number, you win the game.
     * If you guess the wrong number, then I will tell you whether the number I picked is higher or lower, and you will
     * continue guessing.
     * Every time you guess a wrong number x, you will pay x dollars. If you run out of money, you lose the game.
     * Given a particular n, return the minimum amount of money you need to guarantee a win regardless of what number I pick.
     * <p>
     * Constraints:
     * <p>
     * 1 <= n <= 200
     * <p>
     * Approach: DFS + memorization (DP)
     * There are two strategies to solve this problem, 1. minimize the maximum possible cost to win the game, or
     * 2. minimize the expected cost. The second one is not that straightforward to implement, so we could consider the
     * first approach. Essentially, for a given range [i, j], for an arbitrary number k in this range, we want to minimize
     * the maximum cost, if the actual number is lower than k (i.e. in range [i, k - 1]) or if the actual number is greater
     * than k (i.e. in range [k + 1, j]). Why maximum cost? Cuz we want the guarantee to win the game hence we always
     * compute the worst case scenario. The final answer would be the minimum maximum cost in range [1, n]. This can be solved
     * by a simple DFS solution in which we keep dividing the range [1, n] into a smaller interval (base case is there is
     * only one element in the interval, e.g.[1, 1], return 0) and recursively return the result back until reaching the final
     * interval. However, this will be very time-consuming, and it requires lots of duplicate calculation. We can actually
     * use a 2-D array where dp[i][j] memorize the min max cost for range [i, j], and next we visit the same node it will
     * return the result in O(1) time.
     * <p>
     * Time: O(n^2) there will be n intervals to be searched and for each interval we need to iterate over all the possible
     * numbers in that range to get the min max cost. In total, it requires O(n^2) runtime
     * Space: O(n^2)
     */
    public int getMoneyAmount(int n) {
        // need one more space since we're guessing numbers in range [1, n]
        int[][] minCostInRange = new int[n + 1][n + 1];
        // initialize the cost to be inf
        for (int i = 0; i <= n; i++) {
            Arrays.fill(minCostInRange[i], Integer.MAX_VALUE);
        }
        // return the min max cost in range [1, n]
        return minMaxCost(1, n, minCostInRange);
    }

    private int minMaxCost(int left, int right, int[][] minCostInRange) {
        // base case, if there is less than or equal to 1 element in the interval
        // we'll definitely guess the word without paying any amount
        if (left >= right) return 0;
        // if it's a visited node, return the stored result
        if (minCostInRange[left][right] != Integer.MAX_VALUE) return minCostInRange[left][right];
        // we need to enumerate all possible numbers in current range and get the min max cost for this range
        for (int number = left; number <= right; number++) {
            // for a given number, get the max cost from the left or the right side
            // since we need to guarantee we'll win the game
            minCostInRange[left][right] = Math.min(minCostInRange[left][right],
                    Math.max(minMaxCost(left, number - 1, minCostInRange),
                            minMaxCost(number + 1, right, minCostInRange)) + number);
        }
        return minCostInRange[left][right];
    }

    @Test

    public void getMoneyAmountTest() {
        /**
         * Example 1:
         * Input: n = 10
         * Output: 16
         * Explanation: The winning strategy is as follows:
         * - The range is [1,10]. Guess 7.
         *     - If this is my number, your total is $0. Otherwise, you pay $7.
         *     - If my number is higher, the range is [8,10]. Guess 9.
         *         - If this is my number, your total is $7. Otherwise, you pay $9.
         *         - If my number is higher, it must be 10. Guess 10. Your total is $7 + $9 = $16.
         *         - If my number is lower, it must be 8. Guess 8. Your total is $7 + $9 = $16.
         *     - If my number is lower, the range is [1,6]. Guess 3.
         *         - If this is my number, your total is $7. Otherwise, you pay $3.
         *         - If my number is higher, the range is [4,6]. Guess 5.
         *             - If this is my number, your total is $7 + $3 = $10. Otherwise, you pay $5.
         *             - If my number is higher, it must be 6. Guess 6. Your total is $7 + $3 + $5 = $15.
         *             - If my number is lower, it must be 4. Guess 4. Your total is $7 + $3 + $5 = $15.
         *         - If my number is lower, the range is [1,2]. Guess 1.
         *             - If this is my number, your total is $7 + $3 = $10. Otherwise, you pay $1.
         *             - If my number is higher, it must be 2. Guess 2. Your total is $7 + $3 + $1 = $11.
         * The worst case in all these scenarios is that you pay $16. Hence, you only need $16 to guarantee a win.
         */
        assertEquals(16, getMoneyAmount(10));
        /**
         * Example 2:
         * Input: n = 1
         * Output: 0
         * Explanation: There is only one possible number, so you can guess 1 and not have to pay anything.
         */
        assertEquals(0, getMoneyAmount(1));
        /**
         * Example 3:
         * Input: n = 2
         * Output: 1
         * Explanation: There are two possible numbers, 1 and 2.
         * - Guess 1.
         *     - If this is my number, your total is $0. Otherwise, you pay $1.
         *     - If my number is higher, it must be 2. Guess 2. Your total is $1.
         * The worst case is that you pay $1.
         */
        assertEquals(1, getMoneyAmount(2));
    }
}
