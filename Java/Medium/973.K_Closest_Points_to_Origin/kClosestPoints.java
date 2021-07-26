import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class kClosestPoints {

    /**
     * We have a list of points on the plane.  Find the k closest points to the origin (0, 0).
     * (Here, the distance between two points on a plane is the Euclidean distance.)
     * <p>
     * You may return the answer in any order.  The answer is guaranteed to be unique (except for the order that it is in.)
     * <p>
     * Approach 1: Sorting
     * For each point, we can compute their distance to origin and store them in an array. We then sort them in ascending order,
     * the k-th smallest distance will locate at index k - 1. We now iterate over the original 2-D array, compute each distance and
     * if the distance is <= k-th smallest distance, which means it is one of the k closest points.
     * <p>
     * Time: O(NlogN), the runtime is dominated by a typical sorting algorithm, which is O(NlogN)
     * Space: O(N), we need to create another array with the same length of points array to store distance of each point.
     */
    public int[][] kClosestSorting(int[][] points, int k) {
        int length = points.length;
        int[] dists = new int[length];
        int[][] res = new int[k][2];
        for (int i = 0; i < length; i++) {
            dists[i] = getDist(points[i]);
        }
        Arrays.sort(dists);
        int distK = dists[k - 1];
        int index = 0;
        for (int[] point : points) {
            if (getDist(point) <= distK) {
                res[index++] = point;
            }
        }
        return res;
    }

    private int getDist(int[] point) {
        return point[0] * point[0] + point[1] * point[1];
    }

    /**
     * Approach 2: Max Heap
     * We can actually use a max heap to keep track of correct points. We build a max heap and keep the size == k. We add a point
     * at each time, if at any time, the size of max heap is larger than k, we simply remove the point with largest distance so far.
     * By doing so, we will keep k points with k-th smallest distance in the max heap.
     * <p>
     * Time: O(NlogK), remove the largest element takes O(logK) time in a max heap. We need to do N - k times removal
     * Space: O(k) since we build a max heap of size k
     */
    public int[][] kClosestMaxHeap(int[][] points, int k) {
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>((int[] a, int[] b) -> b[0] * b[0] + b[1] * b[1] -
                a[0] * a[0] - a[1] * a[1]);
        int[][] res = new int[k][2];
        for (int[] point : points) {
            maxHeap.add(point);
            if (maxHeap.size() > k) maxHeap.poll();
        }
        int index = 0;
        while (!maxHeap.isEmpty()) res[index++] = maxHeap.poll();
        return res;
    }

    /**
     * Approach 3: Quick Select
     * Since the order doesn't matter in the final result, we can actually use a divide and conquer approach to solve this problem.
     * Just like select k-th largest element in an array (or (n - k)-th smallest), we can first pick a pivot value to partition the
     * whole array into two sub-arrays. where left subarray contains all points with smaller distance than the pivot point, and the
     * right subarray contains all points with larger distance. If the pivot distance locates at index k - 1 after partitioning, then
     * we are done, simply return points from index 0 to index k - 1. Otherwise, we can abandon one side and repeat partitioning in
     * the proper side. (e.g. If the pivot value locates at index larger than k - 1, we need to search the left side until we end at
     * index k - 1)
     * <p>
     * Time: O(N), on average, we abandon half of the input array when we do a partitioning. Hence we need to care about N + N/2 + N/4
     * + ... items = 2N. However, in the worst case, the algorithm will degenerate to O(N^2) runtime. We can solve this issue by
     * randomly pick a pivot value at each partitioning step
     * Space: O(N) we need space to keep track of call stack before we finish the partitioning
     */
    public int[][] kCloestQuickSelect(int[][] points, int k) {
        int[][] res = new int[k][2];
        quickSelect(points, 0, points.length - 1, k);
        for (int i = 0; i < k; i++) {
            res[i] = points[i];
        }
        return res;
    }

    private void quickSelect(int[][] points, int start, int end, int k) {
        if (start >= end) return;
        int pivot = partition(points, start, end);
        // if the pivot correct equals to k, which means the first k-th smallest points
        // are already in the right section (the order might not as well be correct)
        // since order doesn't matter in this question - return it as is
        if (pivot == k) return;
            // first k-th points are not found, keep searching the right half
        else if (pivot < k) quickSelect(points, pivot + 1, end, k);
            // otherwise search the left half
        else quickSelect(points, start, pivot - 1, k);
    }

    private int partition(int[][] points, int start, int end) {
        // always pick the midpoint to bring randomization
        int mid = (end - start) / 2 + start;
        // find the distance for the midpoint value as the pivot
        int pivot = getDist(points[mid]);
        // this is the correct index of where the pivot value would've been after partiton
        int correct_index = start;
        // swap the pivot point to the end
        swap(points, mid, end);

        for (int i = start; i < end; i++) {
            if (getDist(points[i]) < pivot) {
                // keep swapping smaller distance to the left of correct pivot value index
                swap(points, i, correct_index);
                // increment the correct index by since a smaller distance has been found
                correct_index++;
            }
        }
        // swap the pivot value to its correct index and done with the partition
        swap(points, correct_index, end);
        return correct_index;
    }

    private void swap(int[][] points, int i, int j) {
        int[] temp = points[i];
        points[i] = points[j];
        points[j] = temp;
    }


    @Test
    public void kClosestQuickSelectTest() {
        /**
         * Example 1:
         * Input: points = [[1,3],[-2,2]], K = 1
         * Output: [[-2,2]]
         * Explanation:
         * The distance between (1, 3) and the origin is sqrt(10).
         * The distance between (-2, 2) and the origin is sqrt(8).
         * Since sqrt(8) < sqrt(10), (-2, 2) is closer to the origin.
         * We only want the closest K = 1 points from the origin, so the answer is just [[-2,2]].
         */
        int[][] points1 = new int[][]{{1, 3}, {-2, 2}};
        Set<int[]> expected1 = new HashSet<>();
        expected1.add(points1[1]);
        int[][] actual1 = kCloestQuickSelect(points1, 1);
        assertEquals(actual1.length, expected1.size());
        for (int i = 0; i < actual1.length; i++) {
            assertTrue(expected1.contains(actual1[i]));
        }
        /**
         * Input: points = [[3,3],[5,-1],[-2,4]], K = 2
         * Output: [[3,3],[-2,4]]
         * (The answer [[-2,4],[3,3]] would also be accepted.)
         */
        int[][] points2 = new int[][]{{3, 3}, {5, -1}, {-2, 4}};
        Set<int[]> expected2 = new HashSet<>();
        expected2.add(points2[2]);
        expected2.add(points2[0]);
        int[][] actual2 = kCloestQuickSelect(points2, 2);
        assertEquals(actual2.length, expected2.size());
        for (int i = 0; i < actual2.length; i++) {
            assertTrue(expected2.contains(actual2[i]));
        }
        /**
         * Input: points = [[1,0],[0,1]], K = 2
         * Output: [[1,0],[0,1]]
         * (The answer [[0,1],[1,0]] would also be accepted.)
         */
        int[][] points3 = new int[][]{{1, 0}, {0, 1}};
        Set<int[]> expected3 = new HashSet<>();
        expected3.add(points3[0]);
        expected3.add(points3[1]);
        int[][] actual3 = kCloestQuickSelect(points3, 2);
        assertEquals(actual3.length, expected3.size());
        for (int i = 0; i < actual3.length; i++) {
            assertTrue(expected3.contains(actual3[i]));
        }
    }

    @Test
    public void kClosestMaxHeapTest() {
        /**
         * Example 1:
         * Input: points = [[1,3],[-2,2]], K = 1
         * Output: [[-2,2]]
         * Explanation:
         * The distance between (1, 3) and the origin is sqrt(10).
         * The distance between (-2, 2) and the origin is sqrt(8).
         * Since sqrt(8) < sqrt(10), (-2, 2) is closer to the origin.
         * We only want the closest K = 1 points from the origin, so the answer is just [[-2,2]].
         */
        int[][] points1 = new int[][]{{1, 3}, {-2, 2}};
        Set<int[]> expected1 = new HashSet<>();
        expected1.add(points1[1]);
        int[][] actual1 = kClosestMaxHeap(points1, 1);
        assertEquals(actual1.length, expected1.size());
        for (int i = 0; i < actual1.length; i++) {
            assertTrue(expected1.contains(actual1[i]));
        }
        /**
         * Input: points = [[3,3],[5,-1],[-2,4]], K = 2
         * Output: [[3,3],[-2,4]]
         * (The answer [[-2,4],[3,3]] would also be accepted.)
         */
        int[][] points2 = new int[][]{{3, 3}, {5, -1}, {-2, 4}};
        Set<int[]> expected2 = new HashSet<>();
        expected2.add(points2[2]);
        expected2.add(points2[0]);
        int[][] actual2 = kClosestMaxHeap(points2, 2);
        assertEquals(actual2.length, expected2.size());
        for (int i = 0; i < actual2.length; i++) {
            assertTrue(expected2.contains(actual2[i]));
        }
        /**
         * Input: points = [[1,0],[0,1]], K = 2
         * Output: [[1,0],[0,1]]
         * (The answer [[0,1],[1,0]] would also be accepted.)
         */
        int[][] points3 = new int[][]{{1, 0}, {0, 1}};
        Set<int[]> expected3 = new HashSet<>();
        expected3.add(points3[0]);
        expected3.add(points3[1]);
        int[][] actual3 = kClosestMaxHeap(points3, 2);
        assertEquals(actual3.length, expected3.size());
        for (int i = 0; i < actual3.length; i++) {
            assertTrue(expected3.contains(actual3[i]));
        }
    }


    @Test
    public void kClosestSortingTest() {
        /**
         * Example 1:
         * Input: points = [[1,3],[-2,2]], K = 1
         * Output: [[-2,2]]
         * Explanation:
         * The distance between (1, 3) and the origin is sqrt(10).
         * The distance between (-2, 2) and the origin is sqrt(8).
         * Since sqrt(8) < sqrt(10), (-2, 2) is closer to the origin.
         * We only want the closest K = 1 points from the origin, so the answer is just [[-2,2]].
         */
        int[][] points1 = new int[][]{{1, 3}, {-2, 2}};
        Set<int[]> expected1 = new HashSet<>();
        expected1.add(points1[1]);
        int[][] actual1 = kClosestSorting(points1, 1);
        assertEquals(actual1.length, expected1.size());
        for (int i = 0; i < actual1.length; i++) {
            assertTrue(expected1.contains(actual1[i]));
        }
        /**
         * Input: points = [[3,3],[5,-1],[-2,4]], K = 2
         * Output: [[3,3],[-2,4]]
         * (The answer [[-2,4],[3,3]] would also be accepted.)
         */
        int[][] points2 = new int[][]{{3, 3}, {5, -1}, {-2, 4}};
        Set<int[]> expected2 = new HashSet<>();
        expected2.add(points2[2]);
        expected2.add(points2[0]);
        int[][] actual2 = kClosestSorting(points2, 2);
        assertEquals(actual2.length, expected2.size());
        for (int i = 0; i < actual2.length; i++) {
            assertTrue(expected2.contains(actual2[i]));
        }
        /**
         * Input: points = [[1,0],[0,1]], K = 2
         * Output: [[1,0],[0,1]]
         * (The answer [[0,1],[1,0]] would also be accepted.)
         */
        int[][] points3 = new int[][]{{1, 0}, {0, 1}};
        Set<int[]> expected3 = new HashSet<>();
        expected3.add(points3[0]);
        expected3.add(points3[1]);
        int[][] actual3 = kClosestSorting(points3, 2);
        assertEquals(actual3.length, expected3.size());
        for (int i = 0; i < actual3.length; i++) {
            assertTrue(expected3.contains(actual3[i]));
        }
    }

}
