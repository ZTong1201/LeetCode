import org.junit.Test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WaterAndJug {

    /**
     * You are given two jugs with capacities jug1Capacity and jug2Capacity liters. There is an infinite amount of water
     * supply available. Determine whether it is possible to measure exactly targetCapacity liters using these two jugs.
     * <p>
     * If targetCapacity liters of water are measurable, you must have targetCapacity liters of water contained within one
     * or both buckets by the end.
     * <p>
     * Operations allowed:
     * Fill any of the jugs with water.
     * Empty any of the jugs.
     * Pour water from one jug into another till the other jug is completely full, or the first jug itself is empty.
     * <p>
     * Constraints:
     * <p>
     * 1 <= jug1Capacity, jug2Capacity, targetCapacity <= 10^6
     * <p>
     * Approach 1: BFS
     * Essentially, we're searching the total amount of water that two jugs can measure. At each step, we basically have 4
     * options.
     * 1. + jug1Capacity water (if jug1 is empty)
     * 2. + jug2Capacity water (if jug2 is empty)
     * 3. - jug1Capacity water (if we empty jug1)
     * 4. - jug2Capacity water (if we empty jug2)
     * Pour from one to another doesn't make a difference on the total water. Hence, we can search from 0 total water and
     * at each step we have 4 choices. If we reach the target capacity, then we can return true. We can even prune the tree
     * if the total amount of water actually exceeds jug1Capacity + jug2Capacity, which is impossible.
     * <p>
     * Time: O(m / min(x, y)), where m is the target capacity and x, y is the capacity of jug1, jug2, respectively. Essentially,
     * at each time, we at least increment the amount of water by min(x, y), once it exceeds m, the search stops. Hence we can
     * have m / min(x, y) to reach the target.
     */
    public boolean canMeasureWaterBFS(int jug1Capacity, int jug2Capacity, int targetCapacity) {
        Queue<Integer> queue = new LinkedList<>();
        queue.add(0);
        Set<Integer> visited = new HashSet<>();
        int[] next = new int[]{jug1Capacity, -jug1Capacity, jug2Capacity, -jug2Capacity};

        while (!queue.isEmpty()) {
            int curr = queue.poll();
            if (!visited.contains(curr)) {
                visited.add(curr);

                for (int i = 0; i < 4; i++) {
                    int total = curr + next[i];
                    if (total == targetCapacity) return true;
                    // prune the tree, stop searching if the total amount of water is not accessible given two jugs
                    if (total < 0 || total > jug1Capacity + jug2Capacity) continue;
                    queue.add(total);
                }
            }
        }
        return false;
    }

    /**
     * Approach 2: Math
     * Basically, we need to check whether the target capacity is a multiplier of the greatest common divisor of x and y.
     * Why is that? The gcd of x, y is the smallest divisible unit given two jugs. If the target capacity is a multiplier
     * of it, then we can definitely achieve the target by keep adding the smallest unit it reaches the target.
     * <p>
     * Time: O(log(m, n)) need to compute the gcd of input capacity m and n
     */
    public boolean canMeasureWaterMath(int jug1Capacity, int jug2Capacity, int targetCapacity) {
        if (jug1Capacity + jug2Capacity < targetCapacity) return false;
        return (targetCapacity % gcd(jug1Capacity, jug2Capacity)) == 0;
    }

    private int gcd(int x, int y) {
        if (y == 0) return x;
        return gcd(y, x % y);
    }

    @Test
    public void canMeasureWaterTest() {
        /**
         * Example 1:
         * Input: jug1Capacity = 3, jug2Capacity = 5, targetCapacity = 4
         * Output: true
         * Explanation: The famous Die Hard example
         */
        assertTrue(canMeasureWaterBFS(3, 5, 4));
        assertTrue(canMeasureWaterMath(3, 5, 4));
        /**
         * Example 2:
         * Input: jug1Capacity = 2, jug2Capacity = 6, targetCapacity = 5
         * Output: false
         */
        assertFalse(canMeasureWaterBFS(2, 6, 5));
        assertFalse(canMeasureWaterMath(2, 6, 5));
        /**
         * Example 3:
         * Input: jug1Capacity = 1, jug2Capacity = 2, targetCapacity = 3
         * Output: true
         */
        assertTrue(canMeasureWaterBFS(1, 2, 3));
        assertTrue(canMeasureWaterMath(1, 2, 3));
    }
}
