import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class OpenTheLock {

    /**
     * You have a lock in front of you with 4 circular wheels. Each wheel has 10 slots: '0', '1', '2', '3', '4', '5', '6',
     * '7', '8', '9'. The wheels can rotate freely and wrap around: for example we can turn '9' to be '0', or '0' to be '9'.
     * Each move consists of turning one wheel one slot.
     * <p>
     * The lock initially starts at '0000', a string representing the state of the 4 wheels.
     * <p>
     * You are given a list of deadends dead ends, meaning if the lock displays any of these codes, the wheels of the lock
     * will stop turning and you will be unable to open it.
     * <p>
     * Given a target representing the value of the wheels that will unlock the lock, return the minimum total number of
     * turns required to open the lock, or -1 if it is impossible.
     * <p>
     * Constraints:
     * <p>
     * 1 <= deadends.length <= 500
     * deadends[i].length == 4
     * target.length == 4
     * target will not be in the list deadends.
     * target and deadends[i] consist of digits only.
     * <p>
     * Approach: BFS
     * The problem can be translated to find the shortest path between two nodes. The dead ends are obstacles we cannot reach
     * and there is an edge between the digit with its adjacent neighbor, for instance, '1' is connected with '0' and '2'. One
     * thing needs to be careful here is that '0' and '9' are connected with each other too. We can actually start from
     * "0000" and for each slot we can move 1 step to the next stage and skip any dead ends. If we can reach the target
     * in the end, return the steps we have travelled. Otherwise, return -1 in the end.
     * <p>
     * Time: (n^2 * 10^n + L) where n is the number of slots (4 in this case) and L is the number of unique dead ends.
     * We need to add all dead ends into a hash set, which takes O(L) time. For a wheel with n slots, and there are 10 options
     * for a given slot, there will be n * 10^n total combinations to be searched. For each combination, we need to build a
     * new String which takes O(n) time again.
     * Space: O(n * 10^n + L) for the queue to store combinations and the set of dead ends
     */
    public int openLock(String[] deadEnds, String target) {
        Set<String> deadEndSet = new HashSet<>(Arrays.asList(deadEnds));
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        // always start with "0000"
        queue.add("0000");
        int minSteps = 0;
        int[] next = new int[]{1, -1};

        // run BFS to find the shortest path
        while (!queue.isEmpty()) {
            int size = queue.size();

            for (int i = 0; i < size; i++) {
                String curr = queue.poll();

                // we cannot proceed from a dead end - skip it
                if (deadEndSet.contains(curr)) continue;

                // BFS guarantees the shortest sequence when the target is first met
                if (target.equals(curr)) return minSteps;

                // avoid duplicate visit
                if (!visited.contains(curr)) {
                    visited.add(curr);

                    // for each slot on the wheel, we can scroll it to its neighbor
                    for (int j = 0; j < curr.length(); j++) {
                        for (int step : next) {
                            // convert the string to char array for better manipulation
                            char[] strArray = curr.toCharArray();
                            // need to handle edge case for '0' and '9'
                            if (strArray[j] == '0' && step == -1) {
                                strArray[j] = '9';
                            } else if (strArray[j] == '9' && step == 1) {
                                strArray[j] = '0';
                            } else {
                                // otherwise, we can scroll to both neighbors
                                strArray[j] += step;
                            }

                            // get the string for next stage
                            String nextWheel = new String(strArray);
                            // prune search tree by not duplicate visiting nodes
                            if (!visited.contains(nextWheel)) {
                                queue.add(nextWheel);
                            }
                        }
                    }
                }
            }
            // after each layer is done - increment the number of steps;
            minSteps++;
        }
        // return -1 in the end since we cannot reach the target
        return -1;
    }

    @Test
    public void openLockTest() {
        /**
         * Example 1:
         * Input: deadends = ["0201","0101","0102","1212","2002"], target = "0202"
         * Output: 6
         * Explanation:
         * A sequence of valid moves would be "0000" -> "1000" -> "1100" -> "1200" -> "1201" -> "1202" -> "0202".
         * Note that a sequence like "0000" -> "0001" -> "0002" -> "0102" -> "0202" would be invalid,
         * because the wheels of the lock become stuck after the display becomes the dead end "0102".
         */
        assertEquals(6, openLock(new String[]{"0201", "0101", "0102", "1212", "2002"}, "0202"));
        /**
         * Example 2:
         * Input: deadends = ["8888"], target = "0009"
         * Output: 1
         * Explanation:
         * We can turn the last wheel in reverse to move from "0000" -> "0009".
         */
        assertEquals(1, openLock(new String[]{"8888"}, "0009"));
        /**
         * Example 3:
         * Input: deadends = ["8887","8889","8878","8898","8788","8988","7888","9888"], target = "8888"
         * Output: -1
         * Explanation:
         * We can't reach the target without getting stuck.
         */
        assertEquals(-1, openLock(new String[]{"8887", "8889", "8878", "8898", "8788", "8988", "7888", "9888"},
                "8888"));
        /**
         * Example 4:
         * Input: deadends = ["0000"], target = "8888"
         * Output: -1
         */
        assertEquals(-1, openLock(new String[]{"0000"}, "8888"));
    }
}
