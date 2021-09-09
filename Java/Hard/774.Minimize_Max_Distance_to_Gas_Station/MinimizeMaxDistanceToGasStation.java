import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MinimizeMaxDistanceToGasStation {

    /**
     * You are given an integer array stations that represents the positions of the gas stations on the x-axis.
     * You are also given an integer k.
     * <p>
     * You should add k new gas stations. You can add the stations anywhere on the x-axis, and not necessarily on an integer
     * position.
     * <p>
     * Let penalty() be the maximum distance between adjacent gas stations after adding the k new stations.
     * <p>
     * Return the smallest possible value of penalty(). Answers within 10^-6 of the actual answer will be accepted.
     * <p>
     * Constraints:
     * <p>
     * 10 <= stations.length <= 2000
     * 0 <= stations[i] <= 10^8
     * stations is sorted in a strictly increasing order.
     * 1 <= k <= 10^6
     * <p>
     * Approach: Binary Search
     * The minimum distance we can achieve is 0.0, and the maximum distance so far without building any new stations will be
     * the current maximum difference between two adjacent indexes in stations array. We can use binary search to find the
     * minimum distance (D) at which the distance between two gas stations is at most D with less than or equal to K new
     * stations. Essentially, given a distance D, the number of stations need to be built between two old stations will be
     * Math.floor((stations[i] - stations[i - 1]) / D). If we need <= K new stations, the distance D is viable, and we can
     * keep searching a smaller distance.
     * <p>
     * Time: O(nlogW) where W is the maximum distance before building any new stations. For each possible distance, we need
     * iterate over the array to check whether we need <= k new stations.
     * Space: O(1)
     */
    public double minMaxGasDist(int[] stations, int k) {
        double left = 0.0, right = getMaxDistance(stations);
        // since answers within 10^-6 is accepted, we keep moving the boundaries if the difference is still within that range
        while (right - left > 1e-6) {
            double mid = left + (right - left) / 2;
            // if we need more stations - which means the distance is too small, need to search for a larger one
            if (!possible(stations, k, mid)) {
                left = mid;
            } else {
                right = mid;
            }
        }
        return left;
    }

    private boolean possible(int[] stations, int k, double minDist) {
        int numOfStations = 0;
        for (int i = 1; i < stations.length; i++) {
            numOfStations += (int) ((stations[i] - stations[i - 1]) / minDist);
            if (numOfStations > k) return false;
        }
        return true;
    }

    private double getMaxDistance(int[] stations) {
        double res = 0.0;
        for (int i = 1; i < stations.length; i++) {
            res = Math.max(res, 1.0 * stations[i] - stations[i - 1]);
        }
        return res;
    }

    @Test
    public void minMaxGasDistTest() {
        /**
         * Example 1:
         * Input: stations = [1,2,3,4,5,6,7,8,9,10], k = 9
         * Output: 0.50000
         */
        assertEquals(0.5, minMaxGasDist(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9}, 9), 1e-6);
        /**
         * Example 2:
         * Input: stations = [23,24,36,39,46,56,57,65,84,98], k = 1
         * Output: 14.00000
         */
        assertEquals(14, minMaxGasDist(new int[]{23, 24, 36, 39, 46, 56, 57, 65, 84, 98}, 1), 1e-6);
    }
}
