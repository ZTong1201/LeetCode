import org.junit.Test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class RaceCar {

    /**
     * Your car starts at position 0 and speed +1 on an infinite number line. Your car can go into negative positions.
     * Your car drives automatically according to a sequence of instructions 'A' (accelerate) and 'R' (reverse):
     * <p>
     * When you get an instruction 'A', your car does the following:
     * position += speed
     * speed *= 2
     * When you get an instruction 'R', your car does the following:
     * If your speed is positive then speed = -1
     * otherwise speed = 1
     * Your position stays the same.
     * For example, after commands "AAR", your car goes to positions 0 --> 1 --> 3 --> 3, and your speed goes to
     * 1 --> 2 --> 4 --> -1.
     * <p>
     * Given a target position target, return the length of the shortest sequence of instructions to get there.
     * <p>
     * Constraints:
     * <p>
     * 1 <= target <= 104
     * <p>
     * Approach 1: BFS
     * Basically, we start from position 0 and enumerate all possible options at a given position (either accelerate or reverse)
     * and add next candidate positions in the queue. With such a BFS behavior, we guarantee that the length of sequence is
     * the shortest when first hitting the target position. However, without proper pruning, the search space can be tremendously
     * large. Some pruning rules we can take:
     * 1. Never go negative. Cuz we need one step to reverse and start from a negative point (with speed 1) is obviously taking
     * a longer sequence than starting from 0 (also with speed 1)
     * 2. We can think it in a symmetrical way. If we start at 2 * target and face the left side, it would take the exact same
     * number of steps to the target. Hence, starting from any points which are greater than 2 * target in the reverse way
     * is just similar to starting from any negative points and accelerate to the right.
     * <p>
     * Time: O(2^T) the search space would be [0, 2 * T], in the worst case, at each position, we have 2 options and we might
     * well need to visit all of them to get the shortest length.
     * Space: O(2^T) we have two conditions at each position, hence we might need to store 2 strings for each index
     */
    public int raceCarBFS(int target) {
        Set<String> visited = new HashSet<>();
        Queue<CarCondition> queue = new LinkedList<>();
        queue.add(new CarCondition(0, 1));
        int res = 0;

        while (!queue.isEmpty()) {
            int size = queue.size();
            // execute BFS to sequentially pop every node from the current level
            for (int i = 0; i < size; i++) {
                CarCondition curr = queue.poll();
                int currPos = curr.pos, currSpeed = curr.speed;

                // return the shortest length if reaching the target position
                if (currPos == target) return res;

                // serialize the current car condition to avoid duplicate visit
                String serial = currPos + "/" + currSpeed;

                if (!visited.contains(serial)) {
                    visited.add(serial);

                    // if accelerate
                    int newPos1 = currPos + currSpeed, newSpeed1 = currSpeed * 2;
                    // pruning - never visit nodes which are in the negative side or beyond 2 * target range
                    if (newPos1 > 0 && newPos1 < 2 * target) {
                        queue.add(new CarCondition(newPos1, newSpeed1));
                    }

                    // if reverse
                    int newPos2 = currPos, newSpeed2 = currSpeed > 0 ? -1 : 1;
                    queue.add(new CarCondition(newPos2, newSpeed2));
                }
            }
            // update the shortest length
            res++;
        }
        // this statement shall never be reached
        return -1;
    }

    /**
     * Approach 2: Recursion + Memorization (DP)
     * First notice that if the target position equals to 2^n - 1, e.g. 1, 3, 7, etc. The best strategy will always be keeping
     * accelerating n steps. If the target position is not at 2^n - 1. Then we will have two main strategies, assume the target
     * position is t, where t != 2^n - 1, more specifically, 2^(n - 1) - 1 < t < 2^n - 1
     * 1. We can reach t with the smallest number of steps by first accelerating n steps, then we're on the right side of t.
     * We need to make a reverse (R) then accelerate from 1 again to reach the target. Before re-accelerating, we made n A + 1 R
     * hence, (n + 1) steps already, the rest of distance is 2^n - 1 - t, this is the sub-problem we need to solve, and it
     * should've been memorized already. In this scenario, dp[t] = n + 1 + dp[2^n - 1 - t]
     * 2. We can accelerate (n - 1) steps to reach the left side of t. Then in order to reach t, we need to reverse (R) and
     * accelerate m steps (0 <= m < n - 1, cuz otherwise we will reverse back beyond the 0 point, which is impossible
     * to obtain a shorter sequence), and then reverse (R) again, and accelerate from 1 until t. After these sequence, the
     * current position is actually 2^(n - 1) - 1 - (2^m - 1) = 2^(n - 1) - 2^m, and the rest of distance is t - (2^(n - 1) - 2^m).
     * Therefore, in this scenario, we had (n - 1) A + 1 R + m A + 1 R = n + m + 1 steps already, the total number of steps
     * is dp[t] = n + m + 1 + dp[t - (2^(n - 1) - 2^m)]
     * We will take the minimum from both strategies
     * <p>
     * Time: O(TlogT) where T is the target value. For each target value, in the best case, we can get the result in O(1) time,
     * but on average, we need to take m steps where m < log(T + 1), hence in total we have O(TlogT) runtime
     * Space: O(T + logT) we need O(T) space for the memorization and O(logT) space for the call stack
     */
    public int raceCarDP(int target) {
        // minOperations[i] = the shortest sequence of instructions to reach position i
        int[] minOperations = new int[target + 1];
        return findShortestSequence(target, minOperations);
    }

    private int findShortestSequence(int pos, int[] minOperations) {
        // if the current position has been calculated - return the result in O(1) time
        if (minOperations[pos] > 0) return minOperations[pos];
        // compute the number of accelerates needed to reach or exceed pos
        int steps = (int) Math.ceil(Math.log(pos + 1) / Math.log(2));
        // if pos = 2^n - 1, then we don't need extra steps, just keep accelerating from the beginning
        if ((1 << steps) == pos + 1) {
            minOperations[pos] = steps;
            return minOperations[pos];
        }

        // strategy 1 - go beyond pos and reverse back and accelerate until pos
        // we need n A + 1 R before getting started, the distance left is 2^n - 1 - pos
        minOperations[pos] = steps + 1 + findShortestSequence((1 << steps) - 1 - pos, minOperations);
        // otherwise, we need to stop before exceeding pos, reverse and accelerate for m steps and reverse back
        // we have (n - 1) A + 1 R + m A + 1 R = n + m + 1 steps and the current position is 2^(n - 1) - 1 - (2^m - 1)
        // the distance left is pos - (2^(n - 1) - 2^m)
        for (int reverse = 0; reverse < steps - 1; reverse++) {
            int currPos = (1 << (steps - 1)) - (1 << reverse);
            // always need to the minimum at current pos
            minOperations[pos] = Math.min(minOperations[pos],
                    steps + reverse + 1 + findShortestSequence(pos - currPos, minOperations));
        }
        return minOperations[pos];
    }

    @Test
    public void raceCarTest() {
        /**
         * Example 1:
         * Input: target = 3
         * Output: 2
         * Explanation: 
         * The shortest instruction sequence is "AA".
         * Your position goes from 0 --> 1 --> 3.
         */
        assertEquals(2, raceCarBFS(3));
        assertEquals(2, raceCarDP(3));
        /**
         * Example 2:
         * Input: target = 6
         * Output: 5
         * Explanation:
         * The shortest instruction sequence is "AAARA".
         * Your position goes from 0 --> 1 --> 3 --> 7 --> 7 --> 6.
         */
        assertEquals(5, raceCarBFS(6));
        assertEquals(5, raceCarDP(6));
    }

    private static class CarCondition {
        int pos;
        int speed;

        public CarCondition(int pos, int speed) {
            this.pos = pos;
            this.speed = speed;
        }
    }
}
