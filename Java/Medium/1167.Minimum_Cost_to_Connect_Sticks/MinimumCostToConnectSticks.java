import org.junit.Test;

import java.util.PriorityQueue;

import static org.junit.Assert.assertEquals;

public class MinimumCostToConnectSticks {

    /**
     * You have some number of sticks with positive integer lengths. These lengths are given as an array sticks, where
     * sticks[i] is the length of the ith stick.
     * <p>
     * You can connect any two sticks of lengths x and y into one stick by paying a cost of x + y. You must connect all the
     * sticks until there is only one stick remaining.
     * <p>
     * Return the minimum cost of connecting all the given sticks into one stick in this way.
     * <p>
     * Constraints:
     * <p>
     * 1 <= sticks.length <= 10^4
     * 1 <= sticks[i] <= 10^4
     * <p>
     * Approach: Greedy
     * We should always merge two shortest sticks together since the cost of merge will be the sum of two lengths. We should
     * avoid use the longest stick until we have to. Also note that after merging two shortest sticks, the combined stick
     * may not be one of the two shortest anymore. In order to always return the smallest at first. We should use a min heap.
     * <p>
     * Time: O(nlogn) we need n - 1 operations and for each operation we will pop the smallest two elements from the heap
     * and add one back. remove and add both take O(logn) time
     * Space: O(n)
     */
    public int connectSticks(int[] sticks) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        for (int stick : sticks) {
            minHeap.add(stick);
        }

        int totalCost = 0;

        while (minHeap.size() > 1) {
            // get two shortest sticks from the heap
            int stick1 = minHeap.poll();
            int stick2 = minHeap.poll();
            // merge them and get the cost
            int cost = stick1 + stick2;
            // add the cost to total cost
            totalCost += cost;
            // add the merged length back to the heap
            minHeap.add(cost);
        }
        return totalCost;
    }

    @Test
    public void connectSticksTest() {
        /**
         * Example 1:
         * Input: sticks = [2,4,3]
         * Output: 14
         * Explanation: You start with sticks = [2,4,3].
         * 1. Combine sticks 2 and 3 for a cost of 2 + 3 = 5. Now you have sticks = [5,4].
         * 2. Combine sticks 5 and 4 for a cost of 5 + 4 = 9. Now you have sticks = [9].
         * There is only one stick left, so you are done. The total cost is 5 + 9 = 14.
         */
        assertEquals(14, connectSticks(new int[]{2, 4, 3}));
        /**
         * Example 2:
         * Input: sticks = [1,8,3,5]
         * Output: 30
         * Explanation: You start with sticks = [1,8,3,5].
         * 1. Combine sticks 1 and 3 for a cost of 1 + 3 = 4. Now you have sticks = [4,8,5].
         * 2. Combine sticks 4 and 5 for a cost of 4 + 5 = 9. Now you have sticks = [9,8].
         * 3. Combine sticks 9 and 8 for a cost of 9 + 8 = 17. Now you have sticks = [17].
         * There is only one stick left, so you are done. The total cost is 4 + 9 + 17 = 30.
         */
        assertEquals(30, connectSticks(new int[]{1, 8, 3, 5}));
        /**
         * Example 3:
         * Input: sticks = [5]
         * Output: 0
         * Explanation: There is only one stick, so you don't need to do anything. The total cost is 0.
         */
        assertEquals(0, connectSticks(new int[]{5}));
        /**
         * Example 4:
         * Input: sticks = [3354,4316,3259,4904,4598,474,3166,6322,8080,9009]
         * Output: 151646
         */
        assertEquals(151646, connectSticks(new int[]{3354, 4316, 3259, 4904, 4598, 474, 3166, 6322, 8080, 9009}));
    }
}
