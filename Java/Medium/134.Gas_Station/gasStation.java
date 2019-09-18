import org.junit.Test;
import static org.junit.Assert.*;

public class gasStation {

    /**
     * There are N gas stations along a circular route, where the amount of gas at station i is gas[i].
     *
     * You have a car with an unlimited gas tank and it costs cost[i] of gas to travel from station i to its next station (i+1).
     * You begin the journey with an empty tank at one of the gas stations.
     *
     * Return the starting gas station's index if you can travel around the circuit once in the clockwise direction, otherwise return -1.
     *
     * Note:
     *
     * If there exists a solution, it is guaranteed to be unique.
     * Both input arrays are non-empty and have the same length.
     * Each element in the input arrays is a non-negative integer.
     *
     * Approach 1: Brute Force
     * 最简单的方法是将每个gas station视作起点，判断能否以该位置为起点形成一个circuit。为了去掉不必要的起点判断，若gas[i] < cost[i]，说明无法从station i
     * 走到station i + 1，index i无法作为起点。
     *
     * Time: O(n^2), 最坏情况下，每个位置都可能需要遍历整个数组来判断能否形成circuit
     * Space: O(1)
     */
    public int canCompleteCircuitBruteForce(int[] gas, int[] cost) {
        int len = gas.length;
        for (int i = 0; i < len; i++) {
            if (gas[i] >= cost[i]) {
                if (canComplete(i, gas, cost)) {
                    return i;
                }
            }
        }
        return -1;
    }

    private boolean canComplete(int start, int[] gas, int[] cost) {
        int i = start;
        int len = gas.length;
        int gasLeft = gas[i];
        do {
            gasLeft -= cost[i];
            if (gasLeft < 0) {
                return false;
            } else {
                i = (i + 1) % len;
                gasLeft += gas[i];
            }
        } while (i != start);
        return true;
    }

    /**
     * Approach 2: Greedy
     * 通过观察发现，若sum(gas) < sum(cost)，则一定不可能形成一个circuit，同时如方法一讨论，若gas[i] < cost[i]，位置i无法作为起点。因此先假定index 0为起点，
     * 然后遍历整个数组的过程中，记录total_tank和curr_tank，即每次在两个变量上加上gas[i] - cost[i]，若当前的curr_tank < 0，如上述讨论，说明当前的起点不可能
     * 形成circuit，因此考虑更换起点至i + 1, 然后继续向前遍历。若循环结束后，total_tank < 0则可以返回-1，否则返回在循环中最后记录下的起点位置即可
     *
     * Time: O(n) 只需要遍历整个数组一次
     * Space: O(1)
     */
    public int canCompleteCircuitGreedy(int[] gas, int[] cost) {
        int total_tank = 0, curr_tank = 0;
        int start = 0;
        for (int i = 0; i < gas.length; i++) {
            total_tank += gas[i] - cost[i];
            curr_tank += gas[i] - cost[i];
            if (curr_tank < 0) {
                curr_tank = 0;
                start = i + 1;
            }
        }
        return total_tank < 0 ? -1 : start;
    }

    @Test
    public void canCompleteCircuitTest() {
        /**
         * Example 1:
         * Input:
         * gas  = [1,2,3,4,5]
         * cost = [3,4,5,1,2]
         *
         * Output: 3
         *
         * Explanation:
         * Start at station 3 (index 3) and fill up with 4 unit of gas. Your tank = 0 + 4 = 4
         * Travel to station 4. Your tank = 4 - 1 + 5 = 8
         * Travel to station 0. Your tank = 8 - 2 + 1 = 7
         * Travel to station 1. Your tank = 7 - 3 + 2 = 6
         * Travel to station 2. Your tank = 6 - 4 + 3 = 5
         * Travel to station 3. The cost is 5. Your gas is just enough to travel back to station 3.
         * Therefore, return 3 as the starting index.
         */
        int[] gas1 = new int[]{1, 2, 3, 4, 5};
        int[] cost1 = new int[]{3, 4, 5, 1, 2};
        assertEquals(3, canCompleteCircuitBruteForce(gas1, cost1));
        assertEquals(3, canCompleteCircuitGreedy(gas1, cost1));
        /**
         * Example 2:
         * Input:
         * gas  = [2,3,4]
         * cost = [3,4,3]
         *
         * Output: -1
         *
         * Explanation:
         * You can't start at station 0 or 1, as there is not enough gas to travel to the next station.
         * Let's start at station 2 and fill up with 4 unit of gas. Your tank = 0 + 4 = 4
         * Travel to station 0. Your tank = 4 - 3 + 2 = 3
         * Travel to station 1. Your tank = 3 - 3 + 3 = 3
         * You cannot travel back to station 2, as it requires 4 unit of gas but you only have 3.
         * Therefore, you can't travel around the circuit once no matter where you start.
         */
        int[] gas2 = new int[]{2, 3, 4};
        int[] cost2 = new int[]{3, 4, 3};
        assertEquals(-1, canCompleteCircuitBruteForce(gas2, cost2));
        assertEquals(-1, canCompleteCircuitGreedy(gas2, cost2));
    }
}
