import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class CarFleet {

    /**
     * N cars are going to the same destination along a one lane road.  The destination is target miles away.
     * Each car i has a constant speed speed[i] (in miles per hour), and initial position position[i] miles towards the target along the road.
     * A car can never pass another car ahead of it, but it can catch up to it, and drive bumper to bumper at the same speed.
     * The distance between these two cars is ignored - they are assumed to have the same position.
     * A car fleet is some non-empty set of cars driving at the same position and same speed.  Note that a single car is also a car fleet.
     * If a car catches up to a car fleet right at the destination point, it will still be considered as one car fleet.
     * <p>
     * How many car fleets will arrive at the destination?
     * <p>
     * Note:
     * <p>
     * 0 <= N <= 10 ^ 4
     * 0 < target <= 10 ^ 6
     * 0 < speed[i] <= 10 ^ 6
     * 0 <= position[i] < target
     * All initial positions are different.
     * <p>
     * Approach: Sorting
     * Key to solve the problem: sort the position in descending order and start from the largest to the smallest position
     * 可以按照初始位置对所有车进行从大到小排序。若后一辆车S可以超过前一辆车F，那么F和S一定可以在某一位置形成一个fleet。若后续仍有车可以赶上这个fleet，那么一定是
     * 后面的车也可以超过S。
     * <p>
     * 如何判断S可以超过F？
     * 可以先计算出F到达终点所需要的时间t，若后一辆车S可以在t时间内到达大于target的位置，说明S可以在t时间内超过F。因此F和S是同一个fleet。只需遍历整个数组，计算出
     * fleet总数即可
     * <p>
     * Time: O(nlogn) 需要对初始位置进行排序
     * Space: O(n) 因为需要同时存储位置和速度信息，因此需要重新构建一个二维数组存储该信息，然后进行排序
     */
    public int carFleet(int target, int[] position, int[] speed) {
        int n = position.length;
        int[][] pairs = new int[n][2];
        //将位置和速度的信息记录成pair
        for (int i = 0; i < n; i++) {
            pairs[i][0] = position[i];
            pairs[i][1] = speed[i];
        }
        //然后对这些pair按初始位置从大到小排序
        Arrays.sort(pairs, (a, b) -> {
            return b[0] - a[0];
        });
        int i = 0, count = 0;
        while (i < n) {
            count++;
            //计算当前车（F）到达终点所需要的时间
            double time = (target - pairs[i][0]) * 1.0 / pairs[i][1];
            //然后判断下一辆车能否在time时间内也到达终点
            i++;
            while (i < n && (pairs[i][0] + time * pairs[i][1] >= target)) {
                //若S能在time时间内到达终点，说明S和F可以构成一个fleet，然后继续判断下一辆车，能否与S形成fleet
                i++;
            }
        }
        return count;
    }

    @Test
    public void carFleetTest() {
        /**
         * Example 1:
         * Input: target = 12, position = [10,8,0,5,3], speed = [2,4,1,1,3]
         * Output: 3
         * Explanation:
         * The cars starting at 10 and 8 become a fleet, meeting each other at 12.
         * The car starting at 0 doesn't catch up to any other car, so it is a fleet by itself.
         * The cars starting at 5 and 3 become a fleet, meeting each other at 6.
         * Note that no other cars meet these fleets before the destination, so the answer is 3.
         */
        int[] position = new int[]{10, 8, 0, 5, 3};
        int[] speed = new int[]{2, 4, 1, 1, 3};
        assertEquals(3, carFleet(12, position, speed));
        /**
         * Example 2:
         * Input: target = 10, position = [3], speed = [3]
         * Output: 1
         */
        assertEquals(1, carFleet(10, new int[]{3}, new int[]{3}));
    }
}
