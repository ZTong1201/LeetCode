import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class kClosestPoints {

    /**
     * We have a list of points on the plane.  Find the K closest points to the origin (0, 0).
     * (Here, the distance between two points on a plane is the Euclidean distance.)
     *
     * You may return the answer in any order.  The answer is guaranteed to be unique (except for the order that it is in.)
     *
     * Approach 1: Sorting
     * For each point, we can compute their distance to origin and store them in an array. We then sort them in ascending order,
     * the k-th smallest distance will locate at index K - 1. We now iterate over the original 2-D array, compute each distance and
     * if the distance is <= k-th smallest distance, which means it is one of the k closest points.
     *
     * Time: O(NlogN), the runtime is dominated by a typical sorting algorithm, which is O(NlogN)
     * Space: O(N), we need to create another array with the same length of points array to store distance of each point.
     */
    public int[][] kClosestSorting(int[][] points, int K) {
        int length = points.length;
        int[] dists = new int[length];
        int[][] res = new int[K][2];
        for(int i = 0; i < length; i++) {
            dists[i] = getDist(points[i]);
        }
        Arrays.sort(dists);
        int distK = dists[K - 1];
        int index = 0;
        for(int i = 0; i < length; i++) {
            if(getDist(points[i]) <= distK) {
                res[index++] = points[i];
            }
        }
        return res;
    }

    private int getDist(int[] point) {
        return point[0] * point[0] + point[1] * point[1];
    }

    /**
     * Approach 2: Max Heap
     * We can actually use a max heap to keep track of correct points. We build a max heap and keep the size == K. We add a point
     * at each time, if at any time, the size of max heap is larger than K, we simply remove the point with largest distance so far.
     * By doing so, we will keep k points with k-th smallest distance in the max heap.
     *
     * Time: O(NlogK), remove the largest element takes O(logK) time in a max heap. We need to do N - k times removal
     * Space: O(K) since we build a max heap of size K
     */
    public int[][] kClosestMaxHeap(int[][] points, int K) {
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>((point1, point2) -> point2[0]*point2[0] + point2[1]*point2[1] -
                point1[0]*point1[0] - point1[1]*point1[1]);
        int[][] res = new int[K][2];
        int length = points.length;
        for(int i = 0; i < length; i++) {
            maxHeap.add(points[i]);
            if(maxHeap.size() > K) maxHeap.poll();
        }
        int index = 0;
        while(!maxHeap.isEmpty()) res[index++] = maxHeap.poll();
        return res;
    }

    /**
     * Approach 3: Quick Select
     * Since the order doesn't matter in the final result, we can actually use a divide and conquer approach to solve this problem.
     * Just like select k-th largest element in an array (or (n - k)-th smallest), we can first pick a pivot value to partition the
     * whole array into two sub-arrays. where left subarray contains all points with smaller distance than the pivot point, and the
     * right subarray contains all points with larger distance. If the pivot distance locates at index K - 1 after partitioning, then
     * we are done, simply return points from index 0 to index K - 1. Otherwise, we can abandon one side and repeat partitioning in
     * the proper side. (e.g. If the pivot value locates at index larger than K - 1, we need to search the left side until we end at
     * index K - 1)
     *
     * Time: O(N), on average, we abandon half of the input array when we do a partitioning. Hence we need to care about N + N/2 + N/4
     *      + ... items = 2N. However, in the worst case, the algorithm will degenerate to O(N^2) runtime. We can solve this issue by
     *      randomly pick a pivot value at each partitioning step
     * Space: O(N) we need space to keep track of call stack before we finish the partitioning
     */
    public int[][] kCloestQuickSelect(int[][] points, int K) {
        int[][] res = new int[K][2];
        Random random = new Random();
        helper(points, 0, points.length - 1, K, random);
        for(int i = 0; i < K; i++) {
            res[i] = points[i];
        }
        return res;
    }

    private void helper(int[][] points, int low, int high, int K, Random random) {
        // If any time low and high cross each other, we finish the partitioning
        if(low >= high) return;
        // select a random index in the subarray
        int index = low + random.nextInt(high - low);
        // compute the distance of that point as our pivot value
        int pivot = getDist(points[index]);
        swap(points, high, index); // move the pivot point to the end of the subarray
        // assign two pointers at the front and the end, the pivot point is not included
        int i = low;
        int j = high - 1;
        while(i <= j) {
            // if any point has a smaller distance, it is at the correct location, we increment the front pointer
            while(i <= high - 1 && getDist(points[i]) <= pivot) i++;
            // if any point has a larger distance, it is at the correct location, we decrement the end pointer
            while(j >= low && getDist(points[j]) > pivot) j--;
            // if front pointer finds a larger distance and the end pointer finds a smaller distance, yet they haven't
            // reach each other, then we haven't done the partitioning, we simply swap these two points and keep checking further
            if(i < j) swap(points, i, j);
        }
        // when partitioning is done, move the pivot value to its correct index, which is where pointer i points to
        swap(points, high, i);

        // if the pivot value locates at index K - 1, then we are done
        if(i == K - 1) return;
        // else if the pivot value locates at the right of K - 1, which means we have more than K points,
        // redo partitioning in the left subarray
        else if(i > K - 1) helper(points, low, i - 1, K ,random);
        // else, redo partitioning in the right subarray
        else helper(points, i + 1, high, K, random);
    }

    private void swap(int[][] points, int a, int b) {
        int[] temp = points[a];
        points[a] = points[b];
        points[b] = temp;
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
        for(int i = 0; i < actual1.length; i++) {
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
        for(int i = 0; i < actual2.length; i++) {
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
        for(int i = 0; i < actual3.length; i++) {
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
        for(int i = 0; i < actual1.length; i++) {
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
        for(int i = 0; i < actual2.length; i++) {
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
        for(int i = 0; i < actual3.length; i++) {
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
        for(int i = 0; i < actual1.length; i++) {
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
        for(int i = 0; i < actual2.length; i++) {
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
        for(int i = 0; i < actual3.length; i++) {
            assertTrue(expected3.contains(actual3[i]));
        }
    }

}
