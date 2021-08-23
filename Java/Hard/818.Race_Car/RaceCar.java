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
    public int raceCar(int target) {
        Set<String> visited = new HashSet<>();
        // add pos/speed in the set to avoid duplicate visit
        visited.add("0/1");
        // never go to the negative side
        visited.add("0/-1");
        Queue<CarCondition> queue = new LinkedList<>();
        queue.add(new CarCondition(0, 1));
        int res = 0;

        while (!queue.isEmpty()) {
            int size = queue.size();
            // execute BFS to sequentially pop every node from the current level
            for (int i = 0; i < size; i++) {
                CarCondition curr = queue.poll();
                int pos = curr.pos, speed = curr.speed;

                // if accelerate
                int pos1 = pos + speed, speed1 = speed * 2;
                // return the shortest length if reaching the target position
                if (pos1 == target) return res + 1;
                // never visit nodes which are in the negative side or beyond 2 * target range
                if (pos1 > 0 && pos1 < 2 * target) {
                    queue.add(new CarCondition(pos1, speed1));
                }

                // if reverse
                int speed2 = speed > 0 ? -1 : 1;
                String R = pos + "/" + speed2;
                // we can only reach a duplicated position when reversing back
                if (!visited.contains(R)) {
                    visited.add(R);
                    queue.add(new CarCondition(pos, speed2));
                }
            }
            // update the shortest length
            res++;
        }
        // this statement shall never be reached
        return -1;
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
        assertEquals(2, raceCar(3));
        /**
         * Example 2:
         * Input: target = 6
         * Output: 5
         * Explanation:
         * The shortest instruction sequence is "AAARA".
         * Your position goes from 0 --> 1 --> 3 --> 7 --> 7 --> 6.
         */
        assertEquals(5, raceCar(6));
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
