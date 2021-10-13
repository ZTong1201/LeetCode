import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MeetingScheduler {

    /**
     * Given the availability time slots arrays slots1 and slots2 of two people and a meeting duration, return the earliest
     * time slot that works for both of them and is of duration.
     * <p>
     * If there is no common time slot that satisfies the requirements, return an empty array.
     * <p>
     * The format of a time slot is an array of two elements [start, end] representing an inclusive time range from start to
     * end.
     * <p>
     * It is guaranteed that no two availability slots of the same person intersect with each other. That is, for any two time
     * slots [start1, end1] and [start2, end2] of the same person, either start1 > end2 or start2 > end1.
     * <p>
     * Constraints:
     * <p>
     * 1 <= slots1.length, slots2.length <= 10^4
     * slots1[i].length, slots2[i].length == 2
     * slots1[i][0] < slots1[i][1]
     * slots2[i][0] < slots2[i][1]
     * 0 <= slots1[i][j], slots2[i][j] <= 10^9
     * 1 <= duration <= 10^6
     * <p>
     * Approach: Sorting
     * The problem is basically to merge intervals. The only difference is that for a given merged interval, we need to check
     * whether the length is larger than a designated duration. If it's greater, then we return that interval since it's
     * guaranteed to be the smallest after sorting. Otherwise, we keep merging all intervals, and if there is no such interval,
     * we return an empty list in the end.
     * <p>
     * Time: O(mlogm + nlogn) we need to sort each array which will dominate the runtime
     * Space: O(1) or O(logn) depending upon which sorting algorithm is used
     */
    public List<Integer> minAvailableDuration(int[][] slots1, int[][] slots2, int duration) {
        // sort both arrays by the start time
        Arrays.sort(slots1, (a, b) -> (Integer.compare(a[0], b[0])));
        Arrays.sort(slots2, (a, b) -> (Integer.compare(a[0], b[0])));
        int index1 = 0, index2 = 0;

        // we can only keep merging if there are intervals in both arrays
        while (index1 < slots1.length && index2 < slots2.length) {
            int[] freeSlots1 = slots1[index1], freeSlots2 = slots2[index2];
            // try to find the overlap between two slots
            int overlapStart = Math.max(freeSlots1[0], freeSlots2[0]), overlapEnd = Math.min(freeSlots1[1], freeSlots2[1]);
            // return the result if the length of overlap is larger than duration
            if (overlapEnd - overlapStart >= duration) {
                return List.of(overlapStart, overlapStart + duration);
            }

            // move the pointer which has an earlier end time
            if (freeSlots1[1] < freeSlots2[1]) index1++;
            else index2++;
        }
        // if we cannot find such an interval, return an empty list
        return List.of();
    }

    @Test
    public void minAvailableDurationTest() {
        /**
         * Example 1:
         * Input: slots1 = [[10,50],[60,120],[140,210]], slots2 = [[0,15],[60,70]], duration = 8
         * Output: [60,68]
         */
        List<Integer> expected1 = List.of(60, 68);
        List<Integer> actual1 = minAvailableDuration(new int[][]{{10, 50}, {60, 120}, {140, 210}},
                new int[][]{{0, 15}, {60, 70}}, 8);
        assertEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: slots1 = [[10,50],[60,120],[140,210]], slots2 = [[0,15],[60,70]], duration = 12
         * Output: []
         */
        List<Integer> actual2 = minAvailableDuration(new int[][]{{10, 50}, {60, 120}, {140, 210}},
                new int[][]{{0, 15}, {60, 70}}, 12);
        assertTrue(actual2.isEmpty());
    }
}
