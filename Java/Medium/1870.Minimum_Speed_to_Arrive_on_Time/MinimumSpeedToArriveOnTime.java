import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MinimumSpeedToArriveOnTime {

    /**
     * You are given a floating-point number hour, representing the amount of time you have to reach the office. To commute
     * to the office, you must take n trains in sequential order. You are also given an integer array dist of length n,
     * where dist[i] describes the distance (in kilometers) of the ith train ride.
     * <p>
     * Each train can only depart at an integer hour, so you may need to wait in between each train ride.
     * <p>
     * For example, if the 1st train ride takes 1.5 hours, you must wait for an additional 0.5 hours before you can depart
     * on the 2nd train ride at the 2 hour mark.
     * Return the minimum positive integer speed (in kilometers per hour) that all the trains must travel at for you to
     * reach the office on time, or -1 if it is impossible to be on time.
     * <p>
     * Tests are generated such that the answer will not exceed 10^7 and hour will have at most two digits after the decimal
     * point.
     * <p>
     * Constraints:
     * <p>
     * n == dist.length
     * 1 <= n <= 10^5
     * 1 <= dist[i] <= 10^5
     * 1 <= hour <= 10^9
     * There will be at most two digits after the decimal point in hour.
     * <p>
     * Approach: Binary Search
     * It's a typical binary search problem, the key part is
     * - determine the lower and upper bound, since the integer speed is required, the lower bound will always be 1, and the
     * upper bound will be 10^7 + 1 based on the problem statement (the answer will not exceed 10^7).
     * - how to move boundary? if we can reach on time, then we always discard the right half since we need the minimum integer,
     * otherwise, discard the right half.
     * - initialize the result as -1 first, if there is no such speed that we can reach on time, return -1 directly
     * <p>
     * Time: O(nlog10^7) = O(n) for binary search, it requires O(logn) time to find the desired value, the upper bound is 10^7,
     * hence we need O(log10^7) constant time to find the boundary. For each candidate value, we need to traverse the entire
     * array (in the worst case) to check whether we're able to reach on time
     * Space: O(1)
     */
    public int minSpeedOnTime(int[] dist, double hour) {
        int left = 1, right = (int) 1e7 + 1;
        // initialize the result as -1, if no such speed can be found, -1 will be returned
        int result = -1;
        // left closed, right open - [left, right)
        while (left < right) {
            int mid = left + (right - left) / 2;
            // if we can reach on time
            // assign it to the result
            // and discard the right half to find a smaller speed
            if (canReachOnTime(dist, mid, hour)) {
                right = mid;
                result = mid;
            } else {
                // otherwise, discard the left half to find a larger speed to reach on time
                left = mid + 1;
            }
        }
        return result;
    }

    private boolean canReachOnTime(int[] dist, int speed, double hour) {
        double hoursNeeded = 0;
        // for first n - 1 distances, we need to the integer hour for each train
        for (int i = 0; i < dist.length - 1; i++) {
            hoursNeeded += Math.ceil((double) dist[i] / speed);
            // early stop if exceeds the hour limit
            if (hoursNeeded > hour) return false;
        }
        // the time taken for the last train can have decimal places
        hoursNeeded += (double) dist[dist.length - 1] / speed;
        return hoursNeeded <= hour;
    }

    @Test
    public void minSpeedOnTimeTest() {
        /**
         * Example 1:
         * Input: dist = [1,3,2], hour = 6
         * Output: 1
         * Explanation: At speed 1:
         * - The first train ride takes 1/1 = 1 hour.
         * - Since we are already at an integer hour, we depart immediately at the 1 hour mark.
         * The second train takes 3/1 = 3 hours.
         * - Since we are already at an integer hour, we depart immediately at the 4 hour mark.
         * The third train takes 2/1 = 2 hours.
         * - You will arrive at exactly the 6 hour mark.
         */
        assertEquals(1, minSpeedOnTime(new int[]{1, 3, 2}, 6));
        /**
         * Example 2:
         * Input: dist = [1,3,2], hour = 2.7
         * Output: 3
         * Explanation: At speed 3:
         * - The first train ride takes 1/3 = 0.33333 hours.
         * - Since we are not at an integer hour, we wait until the 1 hour mark to depart.
         * The second train ride takes 3/3 = 1 hour.
         * - Since we are already at an integer hour, we depart immediately at the 2 hour mark.
         * The third train takes 2/3 = 0.66667 hours.
         * - You will arrive at the 2.66667 hour mark.
         */
        assertEquals(3, minSpeedOnTime(new int[]{1, 3, 2}, 2.7));
        /**
         * Example 3:
         * Input: dist = [1,3,2], hour = 1.9
         * Output: -1
         * Explanation: It is impossible because the earliest the third train can depart is at the 2 hour mark.
         */
        assertEquals(-1, minSpeedOnTime(new int[]{1, 3, 2}, 1.9));
        /**
         * Example 4:
         * Input: dist[1, 1, 100000], hour = 2.01
         * Output: 10000000
         */
        assertEquals(10000000, minSpeedOnTime(new int[]{1, 1, 100000}, 2.01));
    }
}
