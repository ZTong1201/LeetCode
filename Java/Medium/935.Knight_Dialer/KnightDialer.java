import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class KnightDialer {

    /**
     * The chess knight has a unique movement, it may move two squares vertically and one square horizontally, or two squares
     * horizontally and one square vertically (with both forming the shape of an L).
     * <p>
     * We have a chess knight and a phone pad as shown below, the knight can only stand on a numeric cell.
     * 1 2 3
     * 4 5 6
     * 7 8 9
     * * 0 #
     * <p>
     * Given an integer n, return how many distinct phone numbers of length n we can dial.
     * <p>
     * You are allowed to place the knight on any numeric cell initially and then you should perform n - 1 jumps to dial a
     * number of length n. All jumps should be valid knight jumps.
     * <p>
     * As the answer may be very large, return the answer modulo 10^9 + 7.
     * <p>
     * Constraints:
     * <p>
     * 1 <= n <= 5000
     * <p>
     * Approach 1: Top down DP (Recursion + Memo)
     * Basically, the number of distinct phone numbers for length of n will be the total number of ways ending with 0 - 9,
     * i.e. we need to compute the sum in the final state. However, how do we know the number of ways to form a distinct
     * phone number with length n e.g. ends with 0? Note that according to the knight movement, we can only jump from 4 and 6
     * to 0. Therefore, if we know the number of ways to form a length of (n - 1) phone number ending with 4 and 6, then the
     * sum will be the answer. We can translate this process into a recursion and to better speed up the performance, we can
     * use a 2-D array to keep track of the result ending with 0 - 9 and with length 1 - n. The next time we reach a visited
     * node, we can get the result in O(1) time.
     * <p>
     * Time: O(n) for each length, we need to go through 0 - 9 (10 options) to get the count
     * Space: O(n)
     */
    private final int MOD = 1_000_000_007;

    public int knightDialerTopDown(int n) {
        // keep track of which two nodes are connected based on knight movement
        int[][] graph = new int[][]{{4, 6}, {6, 8}, {7, 9}, {4, 8}, {0, 3, 9}, {}, {0, 1, 7}, {2, 6}, {1, 3}, {2, 4}};
        // plus one since length starts with 1
        int[][] numOfWays = new int[n + 1][10];

        int res = 0;
        for (int number = 0; number <= 9; number++) {
            // sum up the total number of ways ending with 0 - 9 of length n
            res = (res + countNumberOfWays(n, number, graph, numOfWays)) % MOD;
        }
        return res;
    }

    private int countNumberOfWays(int n, int number, int[][] graph, int[][] numOfWays) {
        // base case - if the length is n, there is only one way
        if (n == 1) return 1;
        // if it is a visited node - return the recorded result
        if (numOfWays[n][number] != 0) return numOfWays[n][number];

        // for current length n, we need to get the sum from all neighbors with length (n - 1)
        int count = 0;
        for (int neighbor : graph[number]) {
            count = (count + countNumberOfWays(n - 1, neighbor, graph, numOfWays)) % MOD;
        }
        // store the result
        numOfWays[n][number] = count;
        return count;
    }

    /**
     * Approach 2: Bottom up DP
     * The above approach is essentially a top-down DP. Why? Cuz we directly compute the result for the largest problem,
     * however, we cannot know the answer until we hit the smallest sub-problem (i.e. n == 1). Hence, we traverse the
     * entire recursion tree to find the answer. We can also do it in a bottom-up way. Basically, we know that for each
     * number 0 - 9, the total number of ways to form a length 1 is always 1. Then, we can compute the number of ways
     * to build a length of 2 phone number ending with 0 again. How? Since we can jump from 4 and 6 to 0, then add up
     * T(1, 4) + T(1, 6) = T(2, 0) = 2. We can keep doing this until the length is n, then we sum up the result from 0
     * to 9. Note that the current state is only dependent upon the previous one. Therefore, we don't actually need
     * n dimension, the space can be reduced to a size of 10 array, which is O(1).
     * <p>
     * Time: O(n)
     * Space: O(1)
     */
    public int knightDialerBottomUp(int n) {
        // keep track of which two nodes are connected based on knight movement
        int[][] graph = new int[][]{{4, 6}, {6, 8}, {7, 9}, {4, 8}, {0, 3, 9}, {}, {0, 1, 7}, {2, 6}, {1, 3}, {2, 4}};
        // only need a size of 10 array to keep track of the result ending with number
        int[] numOfWays = new int[10];
        // fill every cell with 1 - that's the base case for length 1
        Arrays.fill(numOfWays, 1);

        for (int i = 2; i <= n; i++) {
            // since the result for each number will be updated later
            // we first need to clone the previous state
            int[] clonedArray = numOfWays.clone();
            for (int number = 0; number <= 9; number++) {
                // count the result for each number
                int count = 0;
                for (int neighbor : graph[number]) {
                    // remember to use the cloned array
                    count = (count + clonedArray[neighbor]) % MOD;
                }
                // update in the result array instead of the cloned one
                numOfWays[number] = count;
            }
        }

        // finally, we get the number of ways to form length n phone numbers ending with each number
        // now, we need to add them up
        int res = 0;
        for (int number = 0; number <= 9; number++) {
            res = (res + numOfWays[number]) % MOD;
        }
        return res;
    }

    @Test
    public void knightDialerTest() {
        /**
         * Example 1:
         * Input: n = 1
         * Output: 10
         * Explanation: We need to dial a number of length 1, so placing the knight over any numeric cell of the 10 cells is
         * sufficient.
         */
        assertEquals(10, knightDialerTopDown(1));
        assertEquals(10, knightDialerBottomUp(1));
        /**
         * Example 2:
         * Input: n = 2
         * Output: 20
         * Explanation: All the valid number we can dial are [04, 06, 16, 18, 27, 29, 34, 38, 40, 43, 49, 60, 61, 67, 72, 76,
         * 81, 83, 92, 94]
         */
        assertEquals(20, knightDialerTopDown(2));
        assertEquals(20, knightDialerBottomUp(2));
        /**
         * Example 3:
         * Input: n = 3
         * Output: 46
         */
        assertEquals(46, knightDialerTopDown(3));
        assertEquals(46, knightDialerBottomUp(3));
        /**
         * Example 4:
         * Input: n = 4
         * Output: 104
         */
        assertEquals(104, knightDialerTopDown(4));
        assertEquals(104, knightDialerBottomUp(4));
        /**
         * Example 5:
         * Input: n = 3131
         * Output: 136006598
         * Explanation: Please take care of the mod.
         */
        assertEquals(136006598, knightDialerTopDown(3131));
        assertEquals(136006598, knightDialerBottomUp(3131));
    }
}
