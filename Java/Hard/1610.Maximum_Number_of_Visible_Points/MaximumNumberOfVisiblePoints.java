import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MaximumNumberOfVisiblePoints {

    /**
     * You are given an array points, an integer angle, and your location, where location = [posx, posy] and
     * points[i] = [xi, yi] both denote integral coordinates on the X-Y plane.
     * <p>
     * Initially, you are facing directly east from your position. You cannot move from your position, but you can rotate.
     * In other words, posx and posy cannot be changed. Your field of view in degrees is represented by angle, determining
     * how wide you can see from any given view direction. Let d be the amount in degrees that you rotate counterclockwise.
     * Then, your field of view is the inclusive range of angles [d - angle/2, d + angle/2].
     * <p>
     * You can see some set of points if, for each point, the angle formed by the point, your position, and the immediate
     * east direction from your position is in your field of view.
     * <p>
     * There can be multiple points at one coordinate. There may be points at your location, and you can always see these
     * points regardless of your rotation. Points do not obstruct your vision to other points.
     * <p>
     * Return the maximum number of points you can see.
     * <p>
     * Constraints:
     * <p>
     * 1 <= points.length <= 10^5
     * points[i].length == 2
     * location.length == 2
     * 0 <= angle < 360
     * 0 <= posx, posy, xi, yi <= 100
     * <p>
     * Approach: Sliding Window
     * We can compute the angle (in degrees) between each point and the original position, now the angle between two points
     * can be easily obtained by computing the difference between two angles. In order to achieve better performance, we could
     * sort the angles and use sliding window to get the maximum window size. If the difference between maximum angle & minimum
     * angle in the window, we could keep expanding the window size. If at any time the difference is greater than the desired
     * angle - we shrink the window size. One pitfall here is that, we could have negative angles, but the difference between
     * a positive angle and a negative angle might not be the actual angle in between. For instance, the angle between 150 and
     * -150 should be 60 instead of 300. Something similar happens when we have 1 degree and 359 degree, the angle in between
     * should be 2 instead of 358. Hence, we could add 360 degree to all the degrees computed as a complement and append them
     * all to the end of the sorted degree list. Hence, we can visit all the points in a cyclic way without losing any points
     * in a given angle.
     * <p>
     * Time: O(nlogn) we need to go through a list of size 2 * n to find the maximum value which takes O(n) time, however,
     * sorting would take O(nlogn) time which will eventually dominate the time complexity
     * Space: O(n)
     */
    public int visiblePoints(List<List<Integer>> points, int angle, List<Integer> location) {
        List<Double> angles = new ArrayList<>();
        int same = 0;
        // convert each point into angles between the current point and the origin point
        for (List<Integer> point : points) {
            int x = point.get(0) - location.get(0);
            int y = point.get(1) - location.get(1);

            // edge case - count all points which already at the origin point
            if (x == 0 && y == 0) {
                same++;
                continue;
            }
            // compute arc tangent value and convert it to degree
            // use atan2 hence we can access to all angles
            angles.add(Math.toDegrees(Math.atan2(x, y)));
        }

        // sort the angles to execute sliding window
        Collections.sort(angles);
        // add 360 to all angles and append them all to the angle list
        // - to achieve cyclic traverse
        List<Double> temp = new ArrayList<>(angles);
        for (double d : angles) temp.add(d + 360);

        // sliding window
        // i is the left bound and j is the right bound
        int res = 0, i = 0;
        for (int j = 0; j < temp.size(); j++) {
            // shrink the window by moving the left bound
            // if the difference is larger than desired
            while (temp.get(j) - temp.get(i) > angle) {
                i++;
            }
            // otherwise, keep expanding the window size and update the result
            res = Math.max(res, j - i + 1);
        }
        // remember to always add the points at the origin location
        return res + same;
    }

    @Test
    public void visiblePointsTest() {
        /**
         * Example 1:
         * Input: points = [[2,1],[2,2],[3,3]], angle = 90, location = [1,1]
         * Output: 3
         * Explanation: The shaded region represents your field of view. All points can be made visible in your field of
         * view, including [3,3] even though [2,2] is in front and in the same line of sight.
         */
        assertEquals(3, visiblePoints(List.of(List.of(2, 1), List.of(2, 2), List.of(3, 3)), 90, List.of(1, 1)));
        /**
         * Example 2:
         * Input: points = [[2,1],[2,2],[3,4],[1,1]], angle = 90, location = [1,1]
         * Output: 4
         * Explanation: All points can be made visible in your field of view, including the one at your location.
         */
        assertEquals(4, visiblePoints(List.of(List.of(2, 1), List.of(2, 2), List.of(3, 4), List.of(1, 1)), 90, List.of(1, 1)));
        /**
         * Example 3:
         * Input: points = [[1,0],[2,1]], angle = 13, location = [1,1]
         * Output: 1
         * Explanation: You can only see one of the two points, as shown above.
         */
        assertEquals(1, visiblePoints(List.of(List.of(1, 0), List.of(2, 1)), 13, List.of(1, 1)));
    }
}
