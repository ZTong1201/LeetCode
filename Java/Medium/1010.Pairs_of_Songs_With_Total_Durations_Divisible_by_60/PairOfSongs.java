import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class PairOfSongs {

    /**
     * You are given a list of songs where the ith song has a duration of time[i] seconds.
     * <p>
     * Return the number of pairs of songs for which their total duration in seconds is divisible by 60. Formally,
     * we want the number of indices i, j such that i < j with (time[i] + time[j]) % 60 == 0.
     * <p>
     * Constraints:
     * <p>
     * 1 <= time.length <= 6 * 10^4
     * 1 <= time[i] <= 500
     * <p>
     * Approach: Remainder hash table
     * Basically, if remainder x of song a and remainder y of song b satisfies (x + y) % 60 = 0, then pairs of (a, b) will make
     * a total second which is divisible by 60. Therefore, we can go through the time array and compute the remainder of each
     * time and count the frequency. In order to form a proper pair, we will search the frequency of (60 - remainder) in the hash
     * table and increment the answer. One edge case would be if the time itself is divisible by 60, e.g. 60, the remainder
     * will be 0, however, we will be searching (60 - 0) = 60 when we meet another song whose duration is divisible by 60.
     * This is not desired, since the remainder will always be in the range [0, 59] (inclusive). In that certain scenario, we will
     * look up the remainder itself instead of (60 - remainder).
     * <p>
     * Time: O(n)
     * Space: O(1) since we will have at most 60 unique remainders
     */
    public int numPairsDivisibleBy60(int[] time) {
        Map<Integer, Integer> remainders = new HashMap<>();
        int count = 0;

        for (int songTime : time) {
            int remainder = songTime % 60;
            int needed = (remainder == 0) ? remainder : (60 - remainder);

            count += remainders.getOrDefault(needed, 0);
            // increment the frequency of current remainder
            remainders.put(remainder, remainders.getOrDefault(remainder, 0) + 1);
        }
        return count;
    }

    @Test
    public void numPairsDivisibleBy60Test() {
        /**
         * Example 1:
         * Input: time = [30,20,150,100,40]
         * Output: 3
         * Explanation: Three pairs have a total duration divisible by 60:
         * (time[0] = 30, time[2] = 150): total duration 180
         * (time[1] = 20, time[3] = 100): total duration 120
         * (time[1] = 20, time[4] = 40): total duration 60
         */
        assertEquals(3, numPairsDivisibleBy60(new int[]{30, 20, 150, 100, 40}));
        /**
         * Example 2:
         * Input: time = [60,60,60]
         * Output: 3
         * Explanation: All three pairs have a total duration of 120, which is divisible by 60.
         */
        assertEquals(3, numPairsDivisibleBy60(new int[]{60, 60, 60}));
    }
}
