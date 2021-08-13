import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CapacityToShipPackages {

    /**
     * A conveyor belt has packages that must be shipped from one port to another within days.
     * <p>
     * The ith package on the conveyor belt has a weight of weights[i]. Each day, we load the ship with packages on the
     * conveyor belt (in the order given by weights). We may not load more weight than the maximum weight capacity of the ship.
     * <p>
     * Return the least weight capacity of the ship that will result in all the packages on the conveyor belt being shipped
     * within days.
     * <p>
     * Constraints:
     * <p>
     * 1 <= days <= weights.length <= 5 * 10^4
     * 1 <= weights[i] <= 500
     * <p>
     * Approach: Binary Search
     * This question is similar to LeetCode 875: https://leetcode.com/problems/koko-eating-bananas/
     * In order to execute binary search, it's necessary to find the upper and lower bound for the search range. For this problem,
     * the lower bound will be the maximum value (the largest weight) in the array, because we're not able to ship all packages
     * if the minimum capacity is lower than that. The upper bound will be the sum of all weights, since in the worst case we
     * need to ship everything within 1 day.
     * <p>
     * Time: O(log(M * N)), assume the average value in the array is M and array is of size N, the lower bound is at least 1
     * the upper bound is guaranteed to be M * N, hence running binary search in range [1, M * N] will have the time complexity
     * of O(log(M * N))
     * Space: O(1)
     */
    public int shipWithinDays(int[] weights, int days) {
        // find upper and lower for the search range
        int left = 0, right = 0;
        for (int weight : weights) {
            left = Math.max(left, weight);
            right += weight;
        }

        while (left < right) {
            int mid = (right - left) / 2 + left;
            if (!possible(weights, days, mid)) {
                // if current capacity cannot fulfill shipping in D days
                // search a larger capacity
                left = mid + 1;
            } else {
                // otherwise, always search a smaller capacity to find the minimum
                right = mid;
            }
        }
        return left;
    }

    private boolean possible(int[] weights, int days, int capacity) {
        // at least needs 1 day to ship the packages
        int need = 1, sum = 0;
        for (int weight : weights) {
            if (sum + weight > capacity) {
                // if adding current package will exceed the capacity
                // the increment the needed day and reset sum to 0
                need++;
                sum = 0;
            }
            // otherwise, add more weights to the sum
            sum += weight;
        }
        // return true if needed days are less than or equal to the required days
        return need <= days;
    }

    @Test
    public void shipWithinDaysTest() {
        /**
         * Example 1:
         * Input: weights = [1,2,3,4,5,6,7,8,9,10], days = 5
         * Output: 15
         * Explanation: A ship capacity of 15 is the minimum to ship all the packages in 5 days like this:
         * 1st day: 1, 2, 3, 4, 5
         * 2nd day: 6, 7
         * 3rd day: 8
         * 4th day: 9
         * 5th day: 10
         *
         * Note that the cargo must be shipped in the order given, so using a ship of capacity 14 and splitting the packages
         * into parts like (2, 3, 4, 5), (1, 6, 7), (8), (9), (10) is not allowed.
         */
        assertEquals(15, shipWithinDays(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, 5));
        /**
         * Example 2:
         * Input: weights = [3,2,2,4,1,4], days = 3
         * Output: 6
         * Explanation: A ship capacity of 6 is the minimum to ship all the packages in 3 days like this:
         * 1st day: 3, 2
         * 2nd day: 2, 4
         * 3rd day: 1, 4
         */
        assertEquals(6, shipWithinDays(new int[]{3, 2, 2, 4, 1, 4}, 3));
        /**
         * Example 3:
         * Input: weights = [1,2,3,1,1], days = 4
         * Output: 3
         * Explanation:
         * 1st day: 1
         * 2nd day: 2
         * 3rd day: 3
         * 4th day: 1, 1
         */
        assertEquals(3, shipWithinDays(new int[]{1, 2, 3, 1, 1}, 4));
    }
}
