import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.assertArrayEquals;

public class InsertInterval {

    /**
     * You are given an array of non-overlapping intervals intervals where intervals[i] = [starti, endi] represent the
     * start and the end of the ith interval and intervals is sorted in ascending order by starti. You are also given an
     * interval newInterval = [start, end] that represents the start and end of another interval.
     * <p>
     * Insert newInterval into intervals such that intervals is still sorted in ascending order by starti and intervals
     * still does not have any overlapping intervals (merge overlapping intervals if necessary).
     * <p>
     * Return intervals after the insertion.
     * <p>
     * Constraints:
     * <p>
     * 0 <= intervals.length <= 10^4
     * intervals[i].length == 2
     * 0 <= starti <= endi <= 10^5
     * intervals is sorted by starti in ascending order.
     * newInterval.length == 2
     * 0 <= start <= end <= 10^5
     * <p>
     * Approach: Greedy
     * Basically, we need to traverse all the intervals and insert intervals whose start time <= the start time of new interval.
     * And after that, insert new interval into the result list. Note that inserting the new interval will only potentially
     * impact the last interval in the result list. Since it's guaranteed there is no overlap between intervals in the
     * original array. If the new interval can be merged with the last interval in the result list so far, then merge it.
     * Otherwise, append new interval into the list directly as well. Then keep finishing traversal of the entire array, this
     * time we might be able to merge intervals with the last interval as well. Keep doing it until the traversal is done.
     * <p>
     * Time: O(n) one pass traversal
     * Space: O(n) we need a result list
     */
    public int[][] insert(int[][] intervals, int[] newInterval) {
        int newStart = newInterval[0], newEnd = newInterval[1];
        // use linked list to get last element in O(1) time
        LinkedList<int[]> res = new LinkedList<>();
        int index = 0, length = intervals.length;

        // first insert intervals whose start time is smaller
        while (index < length && intervals[index][0] <= newStart) {
            res.addLast(intervals[index]);
            index++;
        }

        // then add new interval into the result list
        // also check whether we can merge with the last element
        if (res.isEmpty() || res.getLast()[1] < newStart) {
            // if there is nothing in the list or there is no overlap
            // append the new interval directly
            res.addLast(newInterval);
        } else {
            // otherwise, merge with the last interval
            int[] interval = res.removeLast();
            // the end time will be the only thing impacted
            // since the start time is sorted in ascending order
            interval[1] = Math.max(interval[1], newEnd);
            res.addLast(interval);
        }

        // keep traversing the rest of the interval array
        while (index < length) {
            int[] interval = intervals[index];
            int start = interval[0], end = interval[1];
            // check if there is an overlap, merge with the last interval if any
            if (res.getLast()[1] >= start) {
                interval = res.removeLast();
                interval[1] = Math.max(interval[1], end);
            }
            // add interval as is or merged interval to the final list
            res.addLast(interval);
            index++;
        }
        return res.toArray(new int[res.size()][2]);
    }

    @Test
    public void insertTest() {
        /**
         * Example 1:
         * Input: intervals = [[1,3],[6,9]], newInterval = [2,5]
         * Output: [[1,5],[6,9]]
         */
        int[][] expected1 = new int[][]{{1, 5}, {6, 9}};
        int[][] actual1 = insert(new int[][]{{1, 3}, {6, 9}}, new int[]{2, 5});
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: intervals = [[1,2],[3,5],[6,7],[8,10],[12,16]], newInterval = [4,8]
         * Output: [[1,2],[3,10],[12,16]]
         * Explanation: Because the new interval [4,8] overlaps with [3,5],[6,7],[8,10].
         */
        int[][] expected2 = new int[][]{{1, 2}, {3, 10}, {12, 16}};
        int[][] actual2 = insert(new int[][]{{1, 2}, {3, 5}, {6, 7}, {8, 10}, {12, 16}}, new int[]{4, 8});
        assertArrayEquals(expected2, actual2);
        /**
         * Example 3:
         * Input: intervals = [], newInterval = [5,7]
         * Output: [[5,7]]
         */
        int[][] expected3 = new int[][]{{5, 7}};
        int[][] actual3 = insert(new int[0][0], new int[]{5, 7});
        assertArrayEquals(expected3, actual3);
        /**
         * Example 4:
         * Input: intervals = [[1,5]], newInterval = [2,3]
         * Output: [[1,5]]
         */
        int[][] expected4 = new int[][]{{1, 5}};
        int[][] actual4 = insert(new int[][]{{1, 5}}, new int[]{2, 3});
        assertArrayEquals(expected4, actual4);
        /**
         * Example 5:
         * Input: intervals = [[1,5]], newInterval = [2,7]
         * Output: [[1,7]]
         */
        int[][] expected5 = new int[][]{{1, 7}};
        int[][] actual5 = insert(new int[][]{{1, 5}}, new int[]{2, 7});
        assertArrayEquals(expected5, actual5);
    }
}
