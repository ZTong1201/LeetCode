import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NewTwentyOneGame {

    /**
     * Alice plays the following game, loosely based on the card game "21".
     * <p>
     * Alice starts with 0 points and draws numbers while she has less than k points. During each draw, she gains an integer
     * number of points randomly from the range [1, maxPts], where maxPts is an integer. Each draw is independent and the
     * outcomes have equal probabilities.
     * <p>
     * Alice stops drawing numbers when she gets k or more points.
     * <p>
     * Return the probability that Alice has n or fewer points.
     * <p>
     * Answers within 10-5 of the actual answer are considered accepted.
     * <p>
     * Constraints:
     * <p>
     * 0 <= k <= n <= 10^4
     * 1 <= maxPts <= 10^4
     * <p>
     * Approach: DP
     * Notice that the probability of getting points i is the following
     * p(i) = p(i - 1) / maxPts + p(i - 2) / maxPts + ... + p(i - maxPts) / maxPts
     * Why?
     * For example, if we can draw 1, 2, 3, 4 from the cards, and how to achieve 5?
     * 1. draw 1 starting from point 4
     * 2. draw 2 starting from point 3
     * 3. draw 3 starting from point 2
     * 4. draw 4 starting from point 1
     * <p>
     * Therefore,
     * p(i) = sum(i - k) where k in [1, maxPts] / maxPts
     * denote sum(i - k) where k in [1, maxPts] as probSum. We need to actually keep a window to always compute the right
     * probabilities. Then p(i) = probSum / maxPts.
     * The final result desired will be p(k) + p(k + 1) + ... + p(n) denotes the probability that the total score is larger
     * than or equal to k but still less or equal to n.
     * <p>
     * How to maintain the window?
     * keep adding new probabilities into the probSum, however, start shrinking the window when the card points exceed
     * maxPoint.
     * Why? For example, if the card points in [1, 10], in order to achieve point 12, we don't need p(1) cuz we cannot
     * obtain 12 from point 1.
     * <p>
     * Time: O(n) we only need to compute probability up to n
     * Space: O(n)
     */
    public double new21Game(int n, int k, int maxPts) {
        // edge case, the probability is 1 if k is 0 or n is larger than k + maxPts
        if (k == 0 || n >= k + maxPts) return 1.0;
        double[] prob = new double[n + 1];
        // initialize p(0) = 1.0, meaning it's guaranteed the card points will be <= n (since 0 <= k <= n)
        prob[0] = 1.0;
        double probSum = 1.0, res = 0.0;

        for (int i = 1; i <= n; i++) {
            // compute prob(i)
            prob[i] = probSum / maxPts;
            if (i < k) {
                // keep adding probabilities since the game hasn't stopped (total points still strictly less than k)
                probSum += prob[i];
            } else {
                // compute probabilities from k to n to get the final result
                res += prob[i];
            }
            // shrink the window size to remove unnecessary pairs
            // e.g. remove P(1) since we cannot get 12 from 1 if the card points are in [1, 10]
            if (i >= maxPts) probSum -= prob[i - maxPts];
        }
        return res;
    }

    @Test
    public void new21GameTest() {
        /**
         * Example 1:
         * Input: n = 10, k = 1, maxPts = 10
         * Output: 1.00000
         * Explanation: Alice gets a single card, then stops.
         */
        assertEquals(1.0, new21Game(10, 1, 10), 1e-5);
        /**
         * Example 2:
         * Input: n = 6, k = 1, maxPts = 10
         * Output: 0.60000
         * Explanation: Alice gets a single card, then stops.
         * In 6 out of 10 possibilities, she is at or below 6 points.
         */
        assertEquals(0.6, new21Game(6, 1, 10), 1e-5);
        /**
         * Example 3:
         * Input: n = 21, k = 17, maxPts = 10
         * Output: 0.73278
         */
        assertEquals(0.73278, new21Game(21, 17, 10), 1e-5);
    }
}
