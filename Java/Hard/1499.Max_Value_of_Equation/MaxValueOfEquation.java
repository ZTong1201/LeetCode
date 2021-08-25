import org.junit.Test;

import java.util.Deque;
import java.util.LinkedList;
import java.util.PriorityQueue;

import static org.junit.Assert.assertEquals;

public class MaxValueOfEquation {

    /**
     * You are given an array points containing the coordinates of points on a 2D plane, sorted by the x-values, where
     * points[i] = [xi, yi] such that xi < xj for all 1 <= i < j <= points.length. You are also given an integer k.
     * <p>
     * Return the maximum value of the equation yi + yj + |xi - xj| where |xi - xj| <= k and 1 <= i < j <= points.length.
     * <p>
     * It is guaranteed that there exists at least one pair of points that satisfy the constraint |xi - xj| <= k.
     * <p>
     * Constraints:
     * <p>
     * 2 <= points.length <= 10^5
     * points[i].length == 2
     * -10^8 <= xi, yi <= 10^8
     * 0 <= k <= 2 * 10^8
     * xi < xj for all 1 <= i < j <= points.length
     * xi form a strictly increasing sequence.
     * <p>
     * Approach 1: Priority Queue
     * For the given equation yi + yj + |xi - xj|, assume xj > xi (since the array is in a strictly increasing order),
     * the equation becomes to yi + yj + xj - xi = (yj + xj) + (yi - xi) which means for any pairs that satisfy xj - xi <= k,
     * we want to maximum (yj + xj) + (yi - xi). Since (xj + yj) is a fixed value for index j, which means we just want to
     * maximum (yi - xi) for all xj - xi <= k. Notice that the final result is irrelevant with K, we only need it to check
     * whether a given pair needs to be considered. To get a sorted order for (yi - xi), we could take advantage of the
     * priority queue which the top value always gives the maximum. In the meantime, we want to avoid checking pairs
     * where xj - xi > k. In summary, we want to keep a priority queue of a pair of (yi - xi, xi) in which the maximum
     * value of yi - xi comes first and if there is a tie - we return a smaller xi. Because smaller xi will result in a larger
     * value of xj - xi. This would help us to avoid visiting unnecessary pairs.
     * <p>
     * Time: O(nlogn) remove element from PQ takes O(logn) time, in the worst case, we need to remove the entire array
     * (invalid pairs) for a given point.
     * Space: O(n)
     */
    public int findMaxValueOfEquationPQ(int[][] points, int k) {
        PriorityQueue<int[]> heap = new PriorityQueue<>((int[] a, int[] b) -> {
            // the largest yi - xi comes first, then sorted by xi in ascending order
            if (a[0] == b[0]) return a[1] - b[1];
            else return b[0] - a[0];
        });
        int res = Integer.MIN_VALUE;
        for (int[] point : points) {
            int currX = point[0], currY = point[1];
            // since points are sorted by x, we guarantee xj > xi for each iteration
            // remove invalid pairs from the top of the heap, i.e. xj - xi > k
            while (!heap.isEmpty() && currX - heap.peek()[1] > k) {
                heap.poll();
            }
            // if we have valid pairs to be visited
            // the top value will give the greatest (yi - xi)
            // update result - (yi - xi) + (xj + yj)
            if (!heap.isEmpty()) {
                res = Math.max(res, heap.peek()[0] + currX + currY);
            }
            // add new pair (yj - xj, xj) into the PQ for further search
            heap.add(new int[]{currY - currX, currX});
        }
        return res;
    }

    /**
     * Approach 2: Monotonic Queue
     * We can replace the priority queue by using a monotonic queue to achieve better time performance. Essentially, we'd like
     * to have a queue in which xi is in strictly increasing order while (yi - xi) is in strictly decreasing order. As such,
     * the top value of the queue will always give the largest value for (yi - xi) without violating (xj - xi) > k.
     * We could use a deque to have O(1) time complexity for addition & removal from both front and rear.
     * <p>
     * The algorithm would be:
     * 1. For a given point, if mono queue is not empty, keep removing smaller xi from the front such that xj - xi <= k
     * 2. Now the top value of mono queue has the largest value of (yi - xi) which also satisfies xj - xi <= k, update
     * the maximum
     * 3. Now the elements in the mono queue are all satisfying the condition xj - xi <= k, we keep removing smaller
     * (yi - xi) values (which is smaller than current yj - xj) from the rear such that the greatest (yi - xi) will always
     * come first
     * <p>
     * Time: O(n) each element will be added and polled from the queue once, add and poll can be achieved in O(1) time if a
     * linked list structure is used
     * Space: O(n)
     */
    public int findMaxValueOfEquationMonotonicQueue(int[][] points, int k) {
        Deque<int[]> monoQueue = new LinkedList<>();
        int res = Integer.MIN_VALUE;
        for (int[] point : points) {
            int currX = point[0], currY = point[1];
            while (!monoQueue.isEmpty() && currX - monoQueue.peekFirst()[1] > k) {
                monoQueue.pollFirst();
            }
            if (!monoQueue.isEmpty()) {
                res = Math.max(res, monoQueue.peekFirst()[0] + currX + currY);
            }
            while (!monoQueue.isEmpty() && currY - currX > monoQueue.peekLast()[0]) {
                monoQueue.pollLast();
            }
            monoQueue.addLast(new int[]{currY - currX, currX});
        }
        return res;
    }

    @Test
    public void findMaxValueOfEquationTest() {
        /**
         * Example 1:
         * Input: points = [[1,3],[2,0],[5,10],[6,-10]], k = 1
         * Output: 4
         * Explanation: The first two points satisfy the condition |xi - xj| <= 1 and if we calculate the equation we
         * get 3 + 0 + |1 - 2| = 4. Third and fourth points also satisfy the condition and give a value of
         * 10 + -10 + |5 - 6| = 1.
         * No other pairs satisfy the condition, so we return the max of 4 and 1.
         */
        int[][] points1 = new int[][]{{1, 3}, {2, 0}, {5, 10}, {6, -10}};
        assertEquals(4, findMaxValueOfEquationPQ(points1, 1));
        assertEquals(4, findMaxValueOfEquationMonotonicQueue(points1, 1));
        /**
         * Example 3:
         * Input: points = [[0,0],[3,0],[9,2]], k = 3
         * Output: 3
         * Explanation: Only the first two points have an absolute difference of 3 or less in the x-values,
         * and give the value of 0 + 0 + |0 - 3| = 3.
         */
        int[][] points2 = new int[][]{{0, 0}, {3, 0}, {9, 2}};
        assertEquals(3, findMaxValueOfEquationPQ(points2, 3));
        assertEquals(3, findMaxValueOfEquationMonotonicQueue(points2, 3));
    }
}
