import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class OptimalAccountBalancing {

    /**
     * You are given an array of transactions where transactions[i] = [fromi, toi, amounti] indicates that the
     * person with ID = fromi gave amounti $ to the person with ID = toi.
     * <p>
     * Return the minimum number of transactions required to settle the debt.
     * <p>
     * Constraints:
     * <p>
     * 1 <= transactions.length <= 8
     * transactions[i].length == 3
     * 0 <= fromi, toi <= 20
     * fromi != toi
     * 1 <= amounti <= 100
     * <p>
     * Approach 1: Greedy + Backtrack
     * Basically, after making the transactions, there are some persons will have positive balances while some have negative
     * ones. The key part is trying to make money transfers between these groups of people and if he/she already has a zero
     * balance, then it should remain unchanged. Therefore, for each person who has an open balance, we will greedily make a
     * transaction between him/her and another person who has a balance with a different sign. Essentially, we need to
     * enumerate all possible solutions to find the minimum transactions for each person who has an open balance, and backtrack
     * to the previous state, and move to the next person and recompute the answer until the global minimum has been found.
     * <p>
     * Time: O(n!) basically we are trying to all permutations of the person id in which permutation[i] will make a transaction
     * to permutation[i + 1].
     * Space: O(n) for the hashmap. Based on the problem statement, we could use an array of size 21 to fill in all the users,
     * however, in order to be more generalizable, we should initialize a hash map.
     */
    public int minTransfersBacktrack(int[][] transactions) {
        Map<Integer, Integer> moneyBalance = new HashMap<>();
        for (int[] transaction : transactions) {
            // transfer money out of fromi
            moneyBalance.put(transaction[0], moneyBalance.getOrDefault(transaction[0], 0) - transaction[2]);
            // deposit money in toi
            moneyBalance.put(transaction[1], moneyBalance.getOrDefault(transaction[1], 0) + transaction[2]);
        }

        // convert the map into an array for better performance
        int index = 0;
        int[] openBalance = new int[moneyBalance.size()];

        for (int key : moneyBalance.keySet()) {
            openBalance[index++] = moneyBalance.get(key);
        }
        // starting from the first person to find the minimum number of transactions
        return findMinTransactions(0, openBalance);
    }

    private int findMinTransactions(int index, int[] openBalance) {
        // base case 1: if we reach the last person, no need for more transactions
        if (index == openBalance.length) return 0;
        // base case 2: if current person has 0 balance, skip it and return the answer from the next person
        if (openBalance[index] == 0) return findMinTransactions(index + 1, openBalance);

        int minTransactions = Integer.MAX_VALUE;
        // will try to transfer money to all the persons behind him/her
        int currAmount = openBalance[index];

        for (int i = index + 1; i < openBalance.length; i++) {
            // key part -
            // 1. if two persons have both positive or negative amounts, it's meaningless to transfer money between them
            // 2. or if anyone has a zero balance, we won't transfer money to them
            if (openBalance[i] * currAmount >= 0) continue;

            // transfer all money to the i-th person after index
            openBalance[i] += currAmount;
            // we transfer all the money from the person at index, hence we made 1 transaction already
            // move to the next person (index + 1) to find the minimum transactions
            minTransactions = Math.min(minTransactions, 1 + findMinTransactions(index + 1, openBalance));
            // backtrack to the previous state
            openBalance[i] -= currAmount;
        }
        return minTransactions;
    }

    /**
     * Approach 2: DP + Bitmask
     * As stated in approach 1, we don't really care about those who already had a zero balance. In fact, we are trying
     * to group some people into a subset in which the sum of their open balance equals to 0. For example, {4, -2, -2, 6, -6}
     * is a subset whose sum is 0, and the number of transactions needed will always be len(subset) - 1 = 4. Assume we have
     * n distinct values, and we'd like to find a number m to split n into m mutually exclusive subsets in which the sum is 0.
     * Therefore, the total number of transactions needed will be len(subset1) - 1 + len(subset2) - 1 + ... + len(subsetm) - 1
     * = len(n) - m because any two of subsets is mutually exclusive. Since n is fixed, we're actually trying to find the maximum
     * number of subsets we can form to make sure each subset has a sum of 0.
     * Denote dp[i] = the maximum number of subsets given some elements have been selected, where i is a bitmask, the position
     * j == 1 means that value has been taken.
     * Basically, for each bitmask (each unique subset of values), we want to take out one element at a time and get the maximum
     * number of subsets from the previous state. If adding the element can make the sum each to 0, which means we can form
     * one more subset, otherwise, the number of subsets should remain the same.
     * The algorithm will look like this:
     * For each bitmask from 0 (nothing is selected) -> 2^n - 1 (everything is selected)
     * 1. For position 0 -> n - 1, if that element hasn't been selected, get the mask for adding that element into the subset,
     * 2. If the sum equals to 0 after adding the new element, dp[newMask] = max(dp[newMask], dp[mask] + 1)
     * 3. Otherwise, the maximum remains the same, dp[newMask] = max(dp[newMask], dp[mask])
     * finally, return n - dp[2^n - 1]
     * <p>
     * Time: O(n * 2^n) we have 2^n bitmasks to search, for each bitmask, we need to try to add each element into the mask
     * which takes O(n) time. Hence, we have O(n * 2^n) in total
     * Space: O(2^n)
     */
    public int minTransfersDP(int[][] transactions) {
        // need to find all non-zero balances
        Map<Integer, Integer> moneyBalance = new HashMap<>();
        for (int[] transaction : transactions) {
            moneyBalance.put(transaction[0], moneyBalance.getOrDefault(transaction[0], 0) - transaction[2]);
            moneyBalance.put(transaction[1], moneyBalance.getOrDefault(transaction[1], 0) + transaction[2]);
            // remove zero balance from the map
            if (moneyBalance.get(transaction[0]) == 0) moneyBalance.remove(transaction[0]);
            if (moneyBalance.get(transaction[1]) == 0) moneyBalance.remove(transaction[1]);
        }
        // now we will have n non-zero balances
        int n = moneyBalance.size(), index = 0;
        int[] amounts = new int[n];
        for (int key : moneyBalance.keySet()) {
            amounts[index++] = moneyBalance.get(key);
        }

        // need 2^n placeholders for DP
        int[] dp = new int[1 << n];
        // also need to compute the summation of each bitmask
        int[] sum = new int[1 << n];
        // iterate over each bitmask
        for (int curr = 0; curr < (1 << n); curr++) {
            // start from position 0 to position n - 1 (1 << i)
            for (int i = 0; i < n; i++) {
                // if the element hasn't been selected
                if ((curr & (1 << i)) == 0) {
                    // add it to the bit mask
                    int newMask = curr | (1 << i);
                    // add the element into the sum
                    sum[newMask] = sum[curr] + amounts[i];
                    // if the sum is 0, we can obtain one more subset
                    if (sum[newMask] == 0) {
                        dp[newMask] = Math.max(dp[newMask], dp[curr] + 1);
                    } else {
                        // otherwise, the maximum subset remains the same
                        dp[newMask] = Math.max(dp[newMask], dp[curr]);
                    }
                }
            }
        }
        return n - dp[dp.length - 1];
    }

    @Test
    public void minTransfersTest() {
        /**
         * Example 1:
         * Input: transactions = [[0,1,10],[2,0,5]]
         * Output: 2
         * Explanation:
         * Person #0 gave person #1 $10.
         * Person #2 gave person #0 $5.
         * Two transactions are needed. One way to settle the debt is person #1 pays person #0 and #2 $5 each.
         */
        assertEquals(2, minTransfersBacktrack(new int[][]{{0, 1, 10}, {2, 0, 5}}));
        assertEquals(2, minTransfersDP(new int[][]{{0, 1, 10}, {2, 0, 5}}));
        /**
         * Example 2:
         * Input: transactions = [[0,1,10],[1,0,1],[1,2,5],[2,0,5]]
         * Output: 1
         * Explanation:
         * Person #0 gave person #1 $10.
         * Person #1 gave person #0 $1.
         * Person #1 gave person #2 $5.
         * Person #2 gave person #0 $5.
         * Therefore, person #1 only need to give person #0 $4, and all debt is settled.
         */
        assertEquals(1, minTransfersBacktrack(new int[][]{{0, 1, 10}, {1, 0, 1}, {1, 2, 5}, {2, 0, 5}}));
        assertEquals(1, minTransfersDP(new int[][]{{0, 1, 10}, {1, 0, 1}, {1, 2, 5}, {2, 0, 5}}));
    }
}
