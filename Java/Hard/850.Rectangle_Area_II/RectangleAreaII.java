import org.junit.Test;

import java.util.Arrays;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;

public class RectangleAreaII {

    /**
     * You are given a 2D array of axis-aligned rectangles. Each rectangle[i] = [xi1, yi1, xi2, yi2] denotes the ith
     * rectangle where (xi1, yi1) are the coordinates of the bottom-left corner, and (xi2, yi2) are the coordinates of the
     * top-right corner.
     * <p>
     * Calculate the total area covered by all rectangles in the plane. Any area covered by two or more rectangles should
     * only be counted once.
     * <p>
     * Return the total area. Since the answer may be too large, return it modulo 10^9 + 7.
     * <p>
     * Constraints:
     * <p>
     * 1 <= rectangles.length <= 200
     * rectanges[i].length == 4
     * 0 <= xi1, yi1, xi2, yi2 <= 109
     * <p>
     * Approach: Sweep Line
     * Similar to LeetCode 218: https://leetcode.com/problems/the-skyline-problem/, we need solve this geometry problem using
     * the sweep line. There are two key parts to be determined before executing the sweep line algorithm,
     * 1. From which direction we need to sweep the line, typically, we can sweep from left to right, or bottom to top. In this
     * certain problem, we choose to sweep from the very bottom and move up. Basically, for a given height difference, if we
     * know the number of segments in between, the total area will be (height diff * num of segments)
     * 2. What should we do to deal with different types of events. As discussed in point 1, we can obtain some area as long
     * as the height difference is larger than 0. However, we also need to know how many rectangles are still active in that
     * height difference chunk. Therefore, we need a tree map to keep track of the frequency of each interval, for an
     * ENTERING event, we increment the frequency. On the contrary, we decrement the frequency if it's a leaving event, and will
     * remove it from the map completely if the frequency is 0 (which means that specific rectangle will never be visited)
     * <p>
     * We need to sort the events by their corresponding height in order to run sweep line, hence, we could define an event
     * as [height, type, left, right], where type == 0 means entering and type == 1 means leaving, for a given rectangle,
     * there will always be 2 events (bottom line for entering and upper line for leaving) hence we have 2 * n events in
     * total. For example, given rectangle [0, 0, 2, 2], we have two events [0, 0, 0, 2] and [2, 1, 0, 2].
     * <p>
     * Time: O(n^2) in the worst case. For a given height difference, we need to go through the entire
     * tree map to find the number of active rectangles in between. The size of the map is bounded by O(n). and we have 2 * n
     * events in total.
     * Space: O(n)
     */
    public int rectangleArea(int[][] rectangles) {
        int n = rectangles.length;
        // need 2 * n spaces to store events
        int[][] events = new int[n * 2][4];
        int i = 0;
        for (int[] rectangle : rectangles) {
            // mark as an entering event for the bottom line of the rectangle
            events[i++] = new int[]{rectangle[1], 0, rectangle[0], rectangle[2]};
            // mark as a leaving event for the top line of the rectangle
            events[i++] = new int[]{rectangle[3], 1, rectangle[0], rectangle[2]};
        }
        // sort events by the height
        Arrays.sort(events, (a, b) -> (Integer.compare(a[0], b[0])));

        // define a tree map to store the frequency of the interval of active rectangles
        // sort by the left boundary then by the right boundary
        TreeMap<int[], Integer> active = new TreeMap<>((a, b) -> {
            if (a[0] == b[0]) return Integer.compare(a[1], b[1]);
            return a[0] - b[0];
        });

        long totalArea = 0;
        // initialize the previous height as the smallest height in the coordinate
        int prevHeight = events[0][0];

        // sweep lines from bottom to the top
        for (int[] event : events) {
            int currHeight = event[0], type = event[1], left = event[2], right = event[3];

            // if there is a height difference, compute the total area in between
            if (currHeight > prevHeight) {
                totalArea += getTotalWidth(active) * (currHeight - prevHeight);
                // the current height difference is done, update the previous height
                prevHeight = currHeight;
            }

            // update the frequency of each interval based on event type
            int[] interval = new int[]{left, right};
            if (type == 0) {
                // if it's an entering event, add the rectangle into the map
                active.put(interval, active.getOrDefault(interval, 0) + 1);
            } else {
                // otherwise, remove that rectangle
                active.put(interval, active.getOrDefault(interval, 0) - 1);
                // remove the interval if there is no rectangle in between
                if (active.get(interval) == 0) {
                    active.remove(interval);
                }
            }
        }
        return (int) (totalArea % (1_000_000_007));
    }

    // return a long type since the total width within a height difference can be large
    // we need to compute the total non-overlapping width
    private long getTotalWidth(TreeMap<int[], Integer> active) {
        // the logic will be starting from the leftmost boundary and loop through all intervals (already sorted)
        // for a given interval, first need to check which left boundary is larger, for example, if the previous boundary
        // was 1, but the current interval is [2, 3], we can only get 3 - 2 = 1 width instead of 3 - 1 = 2
        // Then append the width we could gain for each interval, note that it's also possible the ending point of current
        // interval is smaller than the previous left boundary, in the case, we will add 0
        // finally, update the left boundary by the rightmost boundary
        long res = 0;
        int leftBound = 0;
        for (int[] interval : active.keySet()) {
            leftBound = Math.max(leftBound, interval[0]);
            res += Math.max(interval[1] - leftBound, 0);
            leftBound = Math.max(leftBound, interval[1]);
        }
        return res;
    }

    @Test
    public void rectangleAreaTest() {
        /**
         * Example 1:
         * Input: rectangles = [[0,0,2,2],[1,0,2,3],[1,0,3,1]]
         * Output: 6
         * Explanation: A total area of 6 is covered by all three rectangales, as illustrated in the picture.
         * From (1,1) to (2,2), the green and red rectangles overlap.
         * From (1,0) to (2,3), all three rectangles overlap.
         */
        assertEquals(6, rectangleArea(new int[][]{{0, 0, 2, 2}, {1, 0, 2, 3}, {1, 0, 3, 1}}));
        /**
         * Example 2:
         * Input: rectangles = [[0,0,1000000000,1000000000]]
         * Output: 49
         * Explanation: The answer is 1018 modulo (109 + 7), which is 49.
         */
        assertEquals(49, rectangleArea(new int[][]{{0, 0, 1000000000, 1000000000}}));
    }
}
