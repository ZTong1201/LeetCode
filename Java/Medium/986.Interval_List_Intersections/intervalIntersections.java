import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;

public class intervalIntersections {

    /**
     * Given two lists of closed intervals, each list of intervals is pairwise disjoint and in sorted order.
     * <p>
     * Return the intersection of these two interval lists.
     * <p>
     * (Formally, a closed interval [a, b] (with a <= b) denotes the set of real numbers x with a <= x <= b.
     * The intersection of two closed intervals is a set of real numbers that is either empty, or can be represented as a closed interval.
     * For example, the intersection of [1, 3] and [2, 4] is [2, 3].)
     * <p>
     * Note:
     * <p>
     * 0 <= A.length < 1000
     * 0 <= B.length < 1000
     * 0 <= A[i].start, A[i].end, B[i].start, B[i].end < 10^9
     * NOTE: input types have been changed on April 15, 2019. Please reset to default code definition to get new method signature.
     * <p>
     * Approach: Two pointers
     * If two intervals overlap with each other, the merged interval must be in the range of the max value of the start point and
     * the min value of the endpoint. For example, if [0, 2] merges with [1, 5], the merged interval is [1, 2]. To determine whether
     * two intervals can merge with each other is just verifying whether in the merged interval low <= high.
     * <p>
     * After merging, we will discard the interval with smaller endpoint, since the original arrays are sorted. The interval with smaller
     * endpoint can never be merged with incoming intervals
     * <p>
     * Time: O(m + n) in the worst case, we need traverse both arrays once
     * Space: O(1) if the output list doesn't account for space complexity, otherwise, the maximum output size would be O(m + n)
     */
    public int[][] intervalIntersectionTwoPointers(int[][] firstList, int[][] secondList) {
        List<int[]> res = new ArrayList<>();
        int i = 0, j = 0;
        while (i < firstList.length && j < secondList.length) {
            // when merge two intervals
            // the start point must the maximum of two start points
            int low = Math.max(firstList[i][0], secondList[j][0]);
            // the endpoint must the minimum of two endpoints
            int high = Math.min(firstList[i][1], secondList[j][1]);

            // only when low <= high, the two intervals can be merged
            if (low <= high) {
                res.add(new int[]{low, high});
            }

            // always discard the interval with smaller endpoint, since it can never be merged further
            if (firstList[i][1] < secondList[j][1]) {
                i += 1;
            } else {
                j += 1;
            }
        }
        // to convert a list to array, we just build an array with desired size, and use toArray() method for the list
        return res.toArray(new int[res.size()][2]);
    }


    @Test
    public void intervalIntersectionTwoPointersTest() {
        /**
         * Example:
         * Input: A = [[0,2],[5,10],[13,23],[24,25]], B = [[1,5],[8,12],[15,24],[25,26]]
         * Output: [[1,2],[5,5],[8,10],[15,23],[24,24],[25,25]]
         */
        int[][] A = new int[][]{{0, 2}, {5, 10}, {13, 23}, {24, 25}};
        int[][] B = new int[][]{{1, 5}, {8, 12}, {15, 24}, {25, 26}};
        int[][] actual = intervalIntersectionTwoPointers(A, B);
        int[][] expected = new int[][]{{1, 2}, {5, 5}, {8, 10}, {15, 23}, {24, 24}, {25, 25}};
        assertArrayEquals(expected, actual);
    }
}
