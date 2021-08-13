import org.junit.Test;

import java.util.Comparator;
import java.util.PriorityQueue;

import static org.junit.Assert.assertEquals;

public class KthSmallestElementInSortedMatrix {

    /**
     * Given an n x n matrix where each of the rows and columns are sorted in ascending order, return the kth smallest
     * element in the matrix.
     * <p>
     * Note that it is the kth smallest element in the sorted order, not the kth distinct element.
     * <p>
     * Constraints:
     * <p>
     * n == matrix.length
     * n == matrix[i].length
     * 1 <= n <= 300
     * -10^9 <= matrix[i][j] <= 10^9
     * All the rows and columns of matrix are guaranteed to be sorted in non-decreasing order.
     * 1 <= k <= n^2
     * <p>
     * Approach 1: Max heap (priority queue)
     * Keep a max heap of size K and adding new elements into the heap while traversing the matrix. Remove the largest item
     * from the max heap (O(1)) when the size of heap is larger than K. After traversal is done, the kth smallest elements
     * will be left in the heap.
     * <p>
     * Time: O(n^2 * logK)
     * Space: O(K)
     */
    public int kthSmallestMaxHeap(int[][] matrix, int k) {
        Comparator<Integer> comparator = (Integer a, Integer b) -> {
            return b - a;
        };
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(comparator);
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                maxHeap.add(matrix[i][j]);
                if (maxHeap.size() > k) maxHeap.poll();
            }
        }
        return maxHeap.poll();
    }

    /**
     * Approach 2: Binary Search
     * Since the matrix is sorted, we should always think about using binary search to shrink the search size. Note that if
     * the row and column are guaranteed to be in a non-decreasing order, a matrix will be limited by the top-left & the
     * bottom right corner. For instance, matrix X =
     * x1 x2 x3
     * x4 x5 x6
     * x7 x8 x9
     * we would've known that all elements in X are in range [x1, x9]. Therefore, we can use binary search to determine
     * which half to be explored. We can calculate an imaginary midpoint mid = (x1 + x9) / 2, not that mid doesn't necessarily
     * exist in the matrix yet. We're expecting the mid value will split matrix X equally,
     * i.e. x1, x2, x3, x4, x5, mid, x6, x7, x8, x9
     * However, we don't know the actual condition.
     * Fortunately, we can take advantage of the sorted property to search a
     * value in a 2-D matrix in O(m + n) time (see LeetCode 240: https://leetcode.com/problems/search-a-2d-matrix-ii/).
     * Therefore, once we compute the midpoint, we can count the number of elements which are less than or equal to mid in the
     * matrix. If that count equals to K, then we're done, the return value will be the one next to the midpoint. For example,
     * return x5 if K = 5 in the scenario above. In order to return the value, while we take the count in the matrix, we
     * can also keep track of the maximum value from the left half (a.k.a. x5) and the minimum value from the right half
     * (a.k.a. x6).
     * If the count is less than K, it means we're too far from the right, need to dump the left half and search the right
     * side, now the new upper bound should be the minimum value on the right (we just recorded, x6).
     * If the count is less than K, then we search the left half with the new upper bound (x5)
     * <p>
     * Time: O(n^2 * log(max - min)) the search range is defined by the max and min values in the matrix, in the worst case,
     * we need to traverse the entire matrix to count the number of elements which are less than or equal to the midpoint.
     * Space: O(1)
     */
    public int kthSmallestBinarySearch(int[][] matrix, int k) {
        int n = matrix.length;
        // the search range will be determined by the minimum and maximum values in the matrix
        // which is at the top-left, and the bottom-right corner, respectively
        int left = matrix[0][0], right = matrix[n - 1][n - 1];

        while (left < right) {
            // find the (imaginary) midpoint
            int mid = (right - left) / 2 + left;
            // while counting the number, we also need to keep track of
            // the largest value from the left half and
            // the smallest value from the right half
            // smallLargePair[0] is the smallest, smallLargePair[1] is the largest
            int[] smallLargePair = new int[]{matrix[0][0], right = matrix[n - 1][n - 1]};
            // count the number of elements which are smaller than or equal to the midpoint
            int count = countLessThanOrEqual(matrix, mid, smallLargePair);

            if (count == k) {
                // if we find the split point, return the largest value from the left half
                return smallLargePair[0];
            } else if (count < k) {
                // if the count is smaller than k, we need to ditch the left half and search the right
                // the new lower bound will be the minimum value from the right half
                left = smallLargePair[1];
            } else {
                // otherwise, search the left half
                // now the new upper bound will be the maximum value from the left half
                right = smallLargePair[0];
            }
        }
        return left;
    }

    private int countLessThanOrEqual(int[][] matrix, int target, int[] smallLargePair) {
        int n = matrix.length, count = 0;
        // we can start from the bottom-left corner (or the top-right corner)
        int row = n - 1, col = 0;

        // the search range should be within the matrix
        while (row >= 0 && col < n) {
            if (matrix[row][col] > target) {
                // since we have a larger than value (which is supposed to be placed in the right half)
                // we might be able to update the minimum value from the right half now
                smallLargePair[1] = Math.min(smallLargePair[1], matrix[row][col]);
                // if the current value is already greater than the target
                // we can completely ditch the entire row and search the row above
                row--;
            } else {
                // otherwise, the value is supposed to be placed in the left
                // we need to update the maximum value from the left half now
                smallLargePair[0] = Math.max(smallLargePair[0], matrix[row][col]);
                // since the column is already sorted, any values above the current row index should be smaller than target
                // we can update count by adding the number of rows above
                count += row + 1;
                // then no need to search the rows above, move the right column (larger values)
                col++;
            }
        }
        return count;
    }

    @Test
    public void kthSmallestTest() {
        /**
         * Example 1:
         * Input: matrix = [[1,5,9],[10,11,13],[12,13,15]], k = 8
         * Output: 13
         * Explanation: The elements in the matrix are [1,5,9,10,11,12,13,13,15], and the 8th smallest number is 13
         */
        assertEquals(13, kthSmallestMaxHeap(new int[][]{{1, 5, 9}, {10, 11, 13}, {12, 13, 15}}, 8));
        assertEquals(13, kthSmallestBinarySearch(new int[][]{{1, 5, 9}, {10, 11, 13}, {12, 13, 15}}, 8));
        /**
         * Example 2:
         * Input: matrix = [[-5]], k = 1
         * Output: -5
         */
        assertEquals(-5, kthSmallestMaxHeap(new int[][]{{-5}}, 1));
        assertEquals(-5, kthSmallestBinarySearch(new int[][]{{-5}}, 1));
    }
}
