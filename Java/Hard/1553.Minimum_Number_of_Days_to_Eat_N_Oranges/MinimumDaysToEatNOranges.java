import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class MinimumDaysToEatNOranges {

    /**
     * There are n oranges in the kitchen and you decided to eat some of these oranges every day as follows:
     * <p>
     * Eat one orange.
     * If the number of remaining oranges (n) is divisible by 2 then you can eat  n/2 oranges.
     * If the number of remaining oranges (n) is divisible by 3 then you can eat  2*(n/3) oranges.
     * You can only choose one of the actions per day.
     * <p>
     * Return the minimum number of days to eat n oranges.
     * <p>
     * Constraints:
     * <p>
     * 1 <= n <= 2*10^9
     * <p>
     * Approach 1: BFS
     * Since the minimum number of days is to be searched, considering BFS to find the shortest path. Essentially, for a given
     * number of oranges (n), there are at most three options to be searched
     * 1. the number of oranges becomes n / 3 if n % 3 == 0
     * 2. the number of oranges becomes n / 2 if n % 2 == 0
     * 3. the number of oranges becomes n - 1 (always possible)
     * Hence, we can add these options to the queue as next search space. If at any time the oranges are eaten up, it is the
     * shortest path we're looking for, i.e. the minimum number of days
     * <p>
     * O(3^log2(N)) For a given number n, we'll have at most log2(n) options to be searched for. And we have 3 options for each
     * number we need to search
     * Space: O(logN)
     */
    public int minDaysBFS(int n) {
        Queue<Integer> queue = new LinkedList<>();
        queue.add(n);
        int res = 0;
        // use a set to avoid duplicate visit
        Set<Integer> seen = new HashSet<>();

        while (!queue.isEmpty()) {
            int size = queue.size();

            for (int i = 0; i < size; i++) {
                int curr = queue.poll();

                // if all oranges have been eaten up - return the result
                if (curr == 0) return res;
                // avoid duplicates
                if (!seen.contains(curr)) {
                    seen.add(curr);
                    // check whether all three options are applicable
                    // and add new number of oranges into the queue
                    if (curr % 3 == 0) {
                        // 2/3 oranges have been eaten, the rest is 1/3
                        queue.add(curr / 3);
                    }
                    if (curr % 2 == 0) {
                        // 1/2 oranges have been eaten, the rest is 1/2
                        queue.add(curr / 2);
                    }
                    // always add the option that one orange has been eaten
                    queue.add(curr - 1);
                }
            }
            // increment one step
            res++;
        }
        // this return statement shall never be reached
        return -1;
    }

    /**
     * Approach 2: Greedy + DP
     * We always want to eat more oranges in a day, the most efficient way is apparently either eat a half or 1/3 of all
     * oranges if possible. The rest of oranges can be caught by eating one orange a day. Therefore, the number of days
     * to eat all oranges if always trying to eat a half would be n % 2 + days(n / 2). Likewise, if trying to eat 1/3
     * of oranges, then the number of days should be n % 3 + days(n / 3). We take the minimum of them, and add one more
     * operation (why? cuz we have to either eat 1/2 oranges or eat 2/3 oranges) which will give the final result.
     * The search space will be shrunk in log time, and we can use hash map to store visited options.
     * <p>
     * Time: O(log^2(N)) we will have log2(N) options if choosing to eat 1/2 oranges, and log3(N) options if choosing to eat
     * 1/3 oranges. The total search space will be roughly the product of log2(N) * log3(N), hence no matter what's the base
     * of the logarithm, the final runtime will be bounded by O(log^2(N))
     * Space: O(logN)
     */
    private final Map<Integer, Integer> numberOfDaysToEatOranges = new HashMap<>();

    public int minDaysDP(int n) {
        // base case - at least need one day to eat a positive number of oranges
        if (n <= 1) return 1;
        // only do calculation for unvisited node
        if (!numberOfDaysToEatOranges.containsKey(n)) {
            numberOfDaysToEatOranges.put(n, 1 + Math.min(n % 2 + minDaysDP(n / 2), n % 3 + minDaysDP(n / 3)));
        }
        // otherwise, return memorized result
        return numberOfDaysToEatOranges.get(n);
    }

    @Test
    public void minDaysTest() {
        /**
         * Example 1:
         * Input: n = 10
         * Output: 4
         * Explanation: You have 10 oranges.
         * Day 1: Eat 1 orange,  10 - 1 = 9.
         * Day 2: Eat 6 oranges, 9 - 2*(9/3) = 9 - 6 = 3. (Since 9 is divisible by 3)
         * Day 3: Eat 2 oranges, 3 - 2*(3/3) = 3 - 2 = 1.
         * Day 4: Eat the last orange  1 - 1  = 0.
         * You need at least 4 days to eat the 10 oranges.
         */
        assertEquals(4, minDaysBFS(10));
        assertEquals(4, minDaysDP(10));
        /**
         * Example 2:
         * Input: n = 6
         * Output: 3
         * Explanation: You have 6 oranges.
         * Day 1: Eat 3 oranges, 6 - 6/2 = 6 - 3 = 3. (Since 6 is divisible by 2).
         * Day 2: Eat 2 oranges, 3 - 2*(3/3) = 3 - 2 = 1. (Since 3 is divisible by 3)
         * Day 3: Eat the last orange  1 - 1  = 0.
         * You need at least 3 days to eat the 6 oranges.
         */
        assertEquals(3, minDaysBFS(6));
        assertEquals(3, minDaysDP(6));
        /**
         * Example 3:
         * Input: n = 1
         * Output: 1
         */
        assertEquals(1, minDaysBFS(1));
        assertEquals(1, minDaysDP(1));
        /**
         * Example 4:
         * Input: n = 56
         * Output: 6
         */
        assertEquals(6, minDaysBFS(56));
        assertEquals(6, minDaysDP(56));
    }
}
