import org.junit.Test;

import java.util.Arrays;
import java.util.Stack;

import static org.junit.Assert.assertArrayEquals;

public class CarFleetII {

    /**
     * There are n cars traveling at different speeds in the same direction along a one-lane road. You are given an array
     * cars of length n, where cars[i] = [positioni, speedi] represents:
     * <p>
     * position(i) is the distance between the ith car and the beginning of the road in meters. It is guaranteed that
     * position(i) < position(i+1).
     * speed(i) is the initial speed of the ith car in meters per second.
     * For simplicity, cars can be considered as points moving along the number line. Two cars collide when they occupy the
     * same position. Once a car collides with another car, they unite and form a single car fleet. The cars in the formed
     * fleet will have the same position and the same speed, which is the initial speed of the slowest car in the fleet.
     * <p>
     * Return an array answer, where answer[i] is the time, in seconds, at which the ith car collides with the next car,
     * or -1 if the car does not collide with the next car. Answers within 10^-5 of the actual answers are accepted.
     * <p>
     * Constraints:
     * <p>
     * 1 <= cars.length <= 10^5
     * 1 <= position(i), speed(i) <= 10^6
     * position(i) < position(i+1)
     * <p>
     * Approach: Monotonic Stack
     * 此题的本质是维护一个stack，保证stack里面的每一辆车，赶上前车的时间为严格升序排列。Why?
     * 首先，对于第i辆车，保证其起始位置小于第i + 1辆车，如果第i辆车的速度 <= 第i + 1辆车的速度，那么第i辆车永远不可能赶上前车，即它追上
     * 前车的时间为无穷大，它可能会成为后面车的屏障。
     * 第二，若第i辆车可以追上前车，那么需要计算第i辆车追上第i + 1辆车所需时间是否 <= 第i + 1辆车追上其前车的时间。如果满足条件，则说明在
     * 三辆车汇聚之前，i和i + 1会先汇成一个fleet再继续先前汇聚。若不满足条件，则说明无需考虑第i辆车追上第i + 1辆车，本质上第i辆车是在追逐第i + 2
     * 辆车。
     * <p>
     * 算法逻辑为：
     * 倒序遍历cars array，对于cars[i]
     * 1. 若其追不上前车，所需时间为-1，将前车顶出栈，将其压栈
     * 2. 若能追上前车，计算其追上前车的时间time
     * 2.1 若小于等于前车追上其前车的时间，则第i辆车先与前车汇聚，所需时间即为time，将其压栈
     * 2.2 若大于前车追上其前车的时间，则多车一起汇聚，将前车顶出栈，再次重复步骤2直到找到速度最慢的前车，计算所需时间
     * <p>
     * Time: O(n) each car will be pushed or popped at most once, and push and pop takes O(1) time for a stack
     * Space: O(n)
     */
    public double[] getCollisionTimes(int[][] cars) {
        int n = cars.length;
        double[] res = new double[n];
        Arrays.fill(res, -1.0);
        Stack<Integer> stack = new Stack<>();

        // 倒序遍历所有cars
        for (int i = n - 1; i >= 0; i--) {
            int currPos = cars[i][0];
            int currSpeed = cars[i][1];

            // 若栈里有元素，判断是否可以追赶前车
            while (!stack.isEmpty()) {
                int nextPos = cars[stack.peek()][0];
                int nextSpeed = cars[stack.peek()][1];

                // 若速度小于等于前车，无法追赶
                // 将前车顶栈, 因为后车都无法再赶超前车
                if (currSpeed <= nextSpeed) {
                    stack.pop();
                } else {
                    // 计算追上前车的时间
                    double time = (nextPos - currPos) * 1.0 / (currSpeed - nextSpeed);
                    // 若时间小于等于前车追赶其前车的时间或前车无法追赶上其前车
                    // 第i辆车会先与前车汇聚
                    if (time <= res[stack.peek()] || res[stack.peek()] == -1) {
                        res[i] = time;
                        break;
                    }
                    // 否则，第i辆车其实是在追更前面的车
                    stack.pop();
                }
            }
            // 将当前车压栈，继续判断后车能否追赶
            stack.push(i);
        }
        return res;
    }

    @Test
    public void getCollisionTimesTest() {
        /**
         * Example 1:
         * Input: cars = [[1,2],[2,1],[4,3],[7,2]]
         * Output: [1.00000,-1.00000,3.00000,-1.00000]
         * Explanation: After exactly one second, the first car will collide with the second car, and form a car fleet with
         * speed 1 m/s. After exactly 3 seconds, the third car will collide with the fourth car, and form a car fleet with
         * speed 2 m/s.
         */
        double[] expected1 = new double[]{1.0, -1.0, 3.0, -1.0};
        double[] actual1 = getCollisionTimes(new int[][]{{1, 2}, {2, 1}, {4, 3}, {7, 2}});
        assertArrayEquals(expected1, actual1, Math.pow(10, -5));
        /**
         * Example 2:
         * Input: cars = [[3,4],[5,4],[6,3],[9,1]]
         * Output: [2.00000,1.00000,1.50000,-1.00000]
         */
        double[] expected2 = new double[]{2.0, 1.0, 1.5, -1.0};
        double[] actual2 = getCollisionTimes(new int[][]{{3, 4}, {5, 4}, {6, 3}, {9, 1}});
        assertArrayEquals(expected2, actual2, Math.pow(10, -5));
    }
}
