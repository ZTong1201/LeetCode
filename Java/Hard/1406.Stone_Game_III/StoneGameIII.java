import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StoneGameIII {

    /**
     * Alice and Bob continue their games with piles of stones. There are several stones arranged in a row, and each stone
     * has an associated value which is an integer given in the array stoneValue.
     * <p>
     * Alice and Bob take turns, with Alice starting first. On each player's turn, that player can take 1, 2 or 3 stones
     * from the first remaining stones in the row.
     * <p>
     * The score of each player is the sum of values of the stones taken. The score of each player is 0 initially.
     * <p>
     * The objective of the game is to end with the highest score, and the winner is the player with the highest score and
     * there could be a tie. The game continues until all the stones have been taken.
     * <p>
     * Assume Alice and Bob play optimally.
     * <p>
     * Return "Alice" if Alice will win, "Bob" if Bob will win or "Tie" if they end the game with the same score.
     * <p>
     * Constraints:
     * <p>
     * 1 <= values.length <= 50000
     * -1000 <= values[i] <= 1000
     * <p>
     * Approach: DP
     * This game can be translated into a MinMax game in which Alice is trying to maximize the total score (since Alice moves
     * first) whereas Bob is trying to minimize the total score. In a nutshell, we can denote the stone value Alice takes as
     * a positive number, while the stone value Bob takes as a negative number. That is being said, if the sum of total
     * stone values (total score) is positive, then Alice wins. Bob wins if the total score is negative, and there is a tie
     * if the total score is 0.
     * How to play optimally?
     * We can play the backward. At a given point (i), the player (either Alice or Bob) can take at most 3 stones moving forward.
     * i.e. the stone value player can get from index i is either stone[i], stone[i] + stone[i + 1],
     * or stone[i] + stone[i + 1] + stone[i + 2]. Then the player will win the maximum of the following 3 options
     * 1. stone[i] - dp[i + 1]
     * 2. stone[i] + stone[i + 1] - dp[i + 2]
     * 3. stone[i] + stone[i + 1] + stone[i + 2] - dp[i + 3]
     * As mentioned above, the positive values indicate Alice wins against Bob, and vice versa.
     * <p>
     * Time: O(n)
     * Space: O(n)
     */
    public String stoneGameIII(int[] stoneValue) {
        int n = stoneValue.length;
        // need one space indicates that if a player takes the last stone
        // the score another player can get after it is 0
        int[] dp = new int[n + 1];

        for (int i = n - 1; i >= 0; i--) {
            // compute the current score to take at most 3 stones starting from i
            int currScore = 0;
            // since max value is desired, set the default value as -inf
            dp[i] = Integer.MIN_VALUE;

            // take stones from i until 3 stones have been taken or the last piece is taken - whichever comes first
            for (int j = i; j < Math.min(n, i + 3); j++) {
                currScore += stoneValue[j];
                // update the max value player can win at index i
                dp[i] = Math.max(dp[i], currScore - dp[j + 1]);
            }
        }

        if (dp[0] > 0) return "Alice";
        else if (dp[0] < 0) return "Bob";
        return "Tie";
    }

    @Test
    public void stoneGameIIITest() {
        /**
         * Example 1:
         * Input: values = [1,2,3,7]
         * Output: "Bob"
         * Explanation: Alice will always lose. Her best move will be to take three piles and the score become 6.
         * Now the score of Bob is 7 and Bob wins.
         */
        assertEquals("Bob", stoneGameIII(new int[]{1, 2, 3, 7}));
        /**
         * Example 2:
         * Input: values = [1,2,3,-9]
         * Output: "Alice"
         * Explanation: Alice must choose all the three piles at the first move to win and leave Bob with negative score.
         * If Alice chooses one pile her score will be 1 and the next move Bob's score becomes 5.
         * The next move Alice will take the pile with value = -9 and lose.
         * If Alice chooses two piles her score will be 3 and the next move Bob's score becomes 3.
         * The next move Alice will take the pile with value = -9 and also lose.
         * Remember that both play optimally so here Alice will choose the scenario that makes her win.
         */
        assertEquals("Alice", stoneGameIII(new int[]{1, 2, 3, -9}));
        /**
         * Example 3:
         * Input: values = [1,2,3,6]
         * Output: "Tie"
         * Explanation: Alice cannot win this game. She can end the game in a draw if she decided to choose all the first
         * three piles, otherwise she will lose.
         */
        assertEquals("Tie", stoneGameIII(new int[]{1, 2, 3, 6}));
        /**
         * Example 4:
         * Input: values = [1,2,3,-1,-2,-3,7]
         * Output: "Alice"
         */
        assertEquals("Alice", stoneGameIII(new int[]{1, 2, 3, -1, -2, -3, 7}));
        /**
         * Example 5:
         * Input: values = [-1,-2,-3]
         * Output: "Tie"
         */
        assertEquals("Tie", stoneGameIII(new int[]{-1, -2, -3}));
    }
}
