import org.junit.Test;

import java.util.Deque;
import java.util.LinkedList;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;

public class LongestContinuousSubarray {

    /**
     * Given an array of integers nums and an integer limit, return the size of the longest non-empty subarray such that the
     * absolute difference between any two elements of this subarray is less than or equal to limit.
     * <p>
     * Constraints:
     * <p>
     * 1 <= nums.length <= 105
     * 1 <= nums[i] <= 10^9
     * 0 <= limit <= 10^9
     * <p>
     * Approach 1: Sliding Window
     * Since the longest continuous subarray is the target, we can consider the sliding window algorithm. Basically, we keep
     * two pointers specifying a certain window and the subarray within that window will always be a valid answer. We expand
     * the window size by moving the right pointer if we can add more elements to keep the property, or we shrink the
     * window size by shifting the left one if current window is invalid. How to determine whether a window is valid or not?
     * We need to compute the difference between the maximum and the minimum value in that window. Since the value can appear
     * more than once, the best data structure to be used here is a tree map. Because we can get both the maximum and the
     * minimum in O(logn) time and also keep the count of each element.
     * <p>
     * Time: O(nlogn) in the worst case, we need to remove everything we keep in the tree map for any newly added elements,
     * then the total runtime is bounded by O(nlogn)
     * Space: O(n)
     */
    public int longestSubarraySlidingWindow(int[] nums, int limit) {
        TreeMap<Integer, Integer> numFrequency = new TreeMap<>();
        // use two pointers to maintain a window
        int left = 0, right = 0;
        int maxLength = 0;

        while (right < nums.length) {
            // add the new element into the window
            numFrequency.put(nums[right], numFrequency.getOrDefault(nums[right], 0) + 1);

            // check whether the current window is still valid
            // if not, shrink the window by taking out the left element
            // if the difference between the max and the min in the current window is strictly greater than the limit
            // it is an invalid window
            while (left < right && ((numFrequency.lastKey() - numFrequency.firstKey()) > limit)) {
                numFrequency.put(nums[left], numFrequency.get(nums[left]) - 1);
                // remove the entry if the frequency is 0
                if (numFrequency.get(nums[left]) == 0) {
                    numFrequency.remove(nums[left]);
                }
                // shrink the window
                left++;
            }
            maxLength = Math.max(maxLength, right - left + 1);
            // expand the window
            right++;
        }
        return maxLength;
    }

    /**
     * Approach 2: Deque (Monotonic Queue)
     * Similar to LeetCode 239: https://leetcode.com/problems/sliding-window-maximum/, we just want to know the maximum
     * and the minimum in a given sliding window. Therefore, we can keep a monotonic queue to keep track of the those values.
     * The max queue will store elements in a non-increasing order, whereas the min queue will store elements in a non-decreasing
     * order. By doing so, we can get the desired values from the head of both queues. If the difference is strictly greater than
     * the limit, we need to shrink the window by moving the left pointer. If now the top value in the queue equals to the
     * value the left pointer is referencing - we need to remove that element from the queue.
     * <p>
     * Time: O(n) now each element will be enqueued and dequeued at most once, and using a deque structure, the runtime for both
     * operations are O(1)
     * Space: O(n)
     */
    public int longestSubarrayDeque(int[] nums, int limit) {
        int maxLength = 0;
        int left = 0;
        Deque<Integer> maxDeque = new LinkedList<>(), minDeque = new LinkedList<>();

        for (int right = 0; right < nums.length; right++) {
            // for new element, update each queue to have the desired order
            while (!minDeque.isEmpty() && minDeque.peekLast() > nums[right]) {
                minDeque.pollLast();
            }
            minDeque.addLast(nums[right]);

            while (!maxDeque.isEmpty() && maxDeque.peekLast() < nums[right]) {
                maxDeque.pollLast();
            }
            maxDeque.addLast(nums[right]);

            // now the peek values of both queues are the desired ones
            // check whether current window is still valid
            if ((maxDeque.peekFirst() - minDeque.peekFirst()) > limit) {
                // check whether we need to take the left value out of the queue
                if (minDeque.peekFirst() == nums[left]) minDeque.pollFirst();
                if (maxDeque.peekFirst() == nums[left]) maxDeque.pollFirst();
                left++;
            }
            // update the maximum window size
            maxLength = Math.max(maxLength, right - left + 1);
        }
        return maxLength;
    }

    @Test
    public void longestSubarrayTest() {
        /**
         * Example 1:
         * Input: nums = [8,2,4,7], limit = 4
         * Output: 2
         * Explanation: All subarrays are:
         * [8] with maximum absolute diff |8-8| = 0 <= 4.
         * [8,2] with maximum absolute diff |8-2| = 6 > 4.
         * [8,2,4] with maximum absolute diff |8-2| = 6 > 4.
         * [8,2,4,7] with maximum absolute diff |8-2| = 6 > 4.
         * [2] with maximum absolute diff |2-2| = 0 <= 4.
         * [2,4] with maximum absolute diff |2-4| = 2 <= 4.
         * [2,4,7] with maximum absolute diff |2-7| = 5 > 4.
         * [4] with maximum absolute diff |4-4| = 0 <= 4.
         * [4,7] with maximum absolute diff |4-7| = 3 <= 4.
         * [7] with maximum absolute diff |7-7| = 0 <= 4.
         * Therefore, the size of the longest subarray is 2.
         */
        assertEquals(2, longestSubarraySlidingWindow(new int[]{8, 2, 4, 7}, 4));
        assertEquals(2, longestSubarrayDeque(new int[]{8, 2, 4, 7}, 4));
        /**
         * Example 2:
         * Input: nums = [10,1,2,4,7,2], limit = 5
         * Output: 4
         * Explanation: The subarray [2,4,7,2] is the longest since the maximum absolute diff is |2-7| = 5 <= 5.
         */
        assertEquals(4, longestSubarraySlidingWindow(new int[]{10, 1, 2, 4, 7, 2}, 5));
        assertEquals(4, longestSubarrayDeque(new int[]{10, 1, 2, 4, 7, 2}, 5));
        /**
         * Example 3:
         * Input: nums = [4,2,2,2,4,4,2,2], limit = 0
         * Output: 3
         */
        assertEquals(3, longestSubarraySlidingWindow(new int[]{4, 2, 2, 2, 4, 4, 2, 2}, 0));
        assertEquals(3, longestSubarrayDeque(new int[]{4, 2, 2, 2, 4, 4, 2, 2}, 0));
    }
}
