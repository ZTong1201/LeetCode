import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import static org.junit.Assert.assertEquals;

public class LeastNumberOfUniqueIntegers {

    /**
     * Given an array of integers arr and an integer k. Find the least number of unique integers after removing exactly k
     * elements.
     * <p>
     * Constraints:
     * <p>
     * 1 <= arr.length <= 10^5
     * 1 <= arr[i] <= 10^9
     * 0 <= k <= arr.length
     * <p>
     * Approach 1: Greedy + min heap
     * Basically, in order to get the least number of unique integers after removal, we need to consider remove as many integers
     * as possible. How to achieve that? Remove the integer with the least frequency first. Hence, we need to count the occurrence
     * of every integer, and try to remove it from the array. We can use the min heap to guarantee visit the integer with the
     * smallest frequency first.
     * <p>
     * Time: O(nlogn) add and remove from heap will take O(logn) time, in the worst case we might have n different pairs
     * Space: O(n)
     */
    public int findLeastNumOfUniqueIntsMinHeap(int[] arr, int k) {
        Map<Integer, Integer> count = new HashMap<>();
        // count the frequency for each integer
        for (int val : arr) {
            count.put(val, count.getOrDefault(val, 0) + 1);
        }

        // pair[0] - value, pair[1] frequency
        // sort the pairs by the frequency
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> (a[1] - b[1]));
        // add all pairs into the heap
        for (Map.Entry<Integer, Integer> entry : count.entrySet()) {
            minHeap.add(new int[]{entry.getKey(), entry.getValue()});
        }

        // try to remove more integers until we're running of the removals
        while (k > 0) {
            // if the frequency of current integer is strictly larger than k
            // then we used up the number of removals and the current integer will remain in the array
            if (minHeap.peek()[1] > k) {
                break;
            } else {
                // otherwise, the integer should be removed, and the number of removals will be decreased
                k -= minHeap.poll()[1];
            }
        }
        return minHeap.size();
    }

    /**
     * Approach 2: Array
     * The key part is to visit the integer which has the smallest frequency first. We can actually use an array to keep
     * track of the number of unique integers for each frequency. For instance, freq[1] = 2 means we have two unique
     * integers which have frequency 1. Going through the array from left to right will also guarantee the ascending order
     * but the time will be O(n) instead of O(nlogn) in total.
     * <p>
     * Time: O(n)
     * Space: O(n)
     */
    public int findLeastNumOfUniqueIntsArray(int[] arr, int k) {
        Map<Integer, Integer> count = new HashMap<>();
        for (int val : arr) {
            count.put(val, count.getOrDefault(val, 0) + 1);
        }

        // use an array to keep track of how many unique integers for each frequency
        // the max frequency is bounded by length of arr (all duplicates)
        int[] freq = new int[arr.length + 1];
        for (int frequency : count.values()) {
            freq[frequency]++;
        }

        // start from the smallest frequency, which is 1
        int remaining = count.size(), occurrence = 1;
        while (k > 0) {
            // if we can use up all removals at current frequency
            // the k will eventually become 0 - and we need to update the remaining unique number
            if (freq[occurrence] * occurrence >= k) {
                // say k = 3, occurrence 2, we can at most 1 remove 1 unique integer (3 / 2 = 1)
                remaining -= k / occurrence;
                k = 0;
            } else {
                // otherwise, we can remove all the integers in current frequency bucket
                k -= freq[occurrence] * occurrence;
                remaining -= freq[occurrence];
                // move to the next occurrence and keep searching
                occurrence++;
            }
        }
        return remaining;
    }

    @Test
    public void findLeastNumOfUniqueIntsTest() {
        /**
         * Example 1:
         * Input: arr = [5,5,4], k = 1
         * Output: 1
         * Explanation: Remove the single 4, only 5 is left.
         */
        assertEquals(1, findLeastNumOfUniqueIntsMinHeap(new int[]{5, 5, 4}, 1));
        assertEquals(1, findLeastNumOfUniqueIntsArray(new int[]{5, 5, 4}, 1));
        /**
         * Example 2:
         * Input: arr = [4,3,1,1,3,3,2], k = 3
         * Output: 2
         * Explanation: Remove 4, 2 and either one of the two 1s or three 3s. 1 and 3 will be left.
         */
        assertEquals(2, findLeastNumOfUniqueIntsMinHeap(new int[]{4, 3, 1, 1, 3, 3, 2}, 3));
        assertEquals(2, findLeastNumOfUniqueIntsArray(new int[]{4, 3, 1, 1, 3, 3, 2}, 3));
        /**
         * Example 3:
         * Input: arr = [1,1], k = 1
         * Output: 1
         */
        assertEquals(1, findLeastNumOfUniqueIntsMinHeap(new int[]{1, 1}, 1));
        assertEquals(1, findLeastNumOfUniqueIntsArray(new int[]{1, 1}, 1));
        /**
         * Example 4:
         * Input: arr = [1], k = 1
         * Output: 0
         */
        assertEquals(0, findLeastNumOfUniqueIntsMinHeap(new int[]{1}, 1));
        assertEquals(0, findLeastNumOfUniqueIntsArray(new int[]{1}, 1));
    }
}
