import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SplitArrayIntoConsecutiveSubsequences {

    /**
     * You are given an integer array nums that is sorted in non-decreasing order.
     * <p>
     * Determine if it is possible to split nums into one or more subsequences such that both of the following conditions are true:
     * <p>
     * Each subsequence is a consecutive increasing sequence (i.e. each integer is exactly one more than the previous integer).
     * All subsequences have a length of 3 or more.
     * Return true if you can split nums according to the above conditions, or false otherwise.
     * <p>
     * A subsequence of an array is a new array that is formed from the original array by deleting some (can be none) of the
     * elements without disturbing the relative positions of the remaining elements. (i.e., [1,3,5] is a subsequence of
     * [1,2,3,4,5] while [1,3,2] is not).
     * <p>
     * Constraints:
     * <p>
     * 1 <= nums.length <= 10^4
     * -1000 <= nums[i] <= 1000
     * nums is sorted in non-decreasing order.
     * <p>
     * Approach 1: Greedy (Priority Queue)
     * Since the input array is sorted, we know that the new number can either form a longer subsequence or start a new
     * one as the smallest number in that sequence. In order to make sure to fulfill the requirement that each subsequence
     * should have a length of 3 or more. When there is a tie in the largest number in multiple subsequences, we should consider
     * add the new number to the shortest one. For example, given [1,2,3] and [3], when a 4 is given, we should append it to
     * [3] and form [1,2,3] and [3,4] instead of [1,2,3,4], [3]. Hence, we can use a priority queue to keep track of the largest
     * number and the length of each subsequence, and the object will be sorted by the largest number in the ascending order
     * and then by length.
     * <p>
     * Given a new number num, there will be 3 cases:
     * 1. num == the smallest largest number, e.g. [1,2,3] in the queue, and num = 3, we need to create a new subsequence starting
     * from 3.
     * 2. num = the smallest largest number + 1, we can form a longer subsequence, e.g. [1,2,3] in the queue, and num = 4, we can
     * have [1,2,3,4] now.
     * 3. num > the smallest largest number + 1, e.g. [1,2,3], and num = 5, we cannot form a longer subsequence given the smallest
     * subsequence, and given the array is sorted, we cannot form a longer one moving forward, this subsequence needs to be
     * removed from the queue completely. While removing it, we need to check whether the length is at least 3. Then we keep
     * checking condition 1 & 2 after removing this subsequence
     * <p>
     * Time: O(nlogn) on average, but O(n^2logn) in the worst. Most of the time, we just need to compare with the peek value of the
     * queue and update the queue, however, we need to remove some subsequences from the queue in certain cases, in the worst
     * case we need to remove O(n) subsequences, and it will take O(nlogn) for one element.
     * Space: O(n)
     */
    public boolean isPossiblePriorityQueue(int[] nums) {
        // sort each subsequence by the largest value and then by length
        PriorityQueue<SequenceCondition> sequences = new PriorityQueue<>((a, b) -> {
            if (a.largestNum == b.largestNum) return Integer.compare(a.length, b.length);
            return Integer.compare(a.largestNum, b.largestNum);
        });

        for (int num : nums) {
            // check whether we should retire some subsequences
            while (!sequences.isEmpty() && sequences.peek().largestNum + 1 < num) {
                // when we cannot form a longer subsequence - it should be removed completely
                // also need to check whether the length is greater than or equal to 3
                if (sequences.poll().length < 3) return false;
            }

            // case 1: need to form a new subsequence with the current num
            if (sequences.isEmpty() || sequences.peek().largestNum == num) {
                sequences.add(new SequenceCondition(num));
            } else if (sequences.peek().largestNum + 1 == num) {
                // otherwise, we can form a longer subsequence
                SequenceCondition curr = sequences.poll();
                // update the largest number
                curr.largestNum = num;
                // increment the length
                curr.length++;
                // add the subsequence back
                sequences.add(curr);
            }
        }

        // finally, all subsequences are settled - check length
        while (!sequences.isEmpty()) {
            if (sequences.poll().length < 3) return false;
        }
        return true;
    }

    private static class SequenceCondition {
        int largestNum;
        int length;

        public SequenceCondition(int num) {
            this.largestNum = num;
            this.length = 1;
        }
    }

    /**
     * Approach 2: Greedy (hash map)
     * Similarly, we can build the sequence on the fly when a new number kicks in. Basically, we need to two hash maps, the
     * first one will keep track of the frequency of each number, and we can use that to form new subsequences. The other
     * hash map will be used to keep track of the frequency of next needed number in order to form a longer subsequence.
     * For a given number, there are two options
     * 1. We either append it to the previous existing subsequence
     * 2. or we form a new subsequence with at least length 3
     * Since we're running a greedy algorithm, the first option is preferable than the second one. If we cannot satisfy any
     * of them, then we can directly return false.
     * <p>
     * For instance, given a new number 4, we first check whether we can append 4 to an existing of subsequence which ends
     * with 3 <=> check whether the frequency of 4 is greater than 0 in the next needed number map.
     * If it doesn't exist, which means we cannot append it to an existing one. Then we should consider start a new subsequence
     * with at least length 3, i.e. we check whether 5 and 6 still exist in the original array, if yes, then we now have a
     * subsequence [4,5,6] which ends with 6, we need to add 7 in the next needed number map in order to form a longer one.
     * <p>
     * Time: O(n) we traverse the entire array twice
     * Space: O(n)
     */
    public boolean isPossibleHashMap(int[] nums) {
        Map<Integer, Integer> numFrequency = new HashMap<>(), nextNeededFrequency = new HashMap<>();
        // first pass - keep track of the frequency of each number
        for (int num : nums) numFrequency.put(num, numFrequency.getOrDefault(num, 0) + 1);
        // second pass - greedily form subsequence with a longer length
        for (int num : nums) {
            // skip used numbers
            if (numFrequency.get(num) == 0) continue;
            if (nextNeededFrequency.getOrDefault(num, 0) > 0) {
                // try to append the current number into an existing subsequence
                // decrement the frequency of needed number
                nextNeededFrequency.put(num, nextNeededFrequency.get(num) - 1);
                // now, in order to form a longer subsequence, we need num + 1 in the future
                nextNeededFrequency.put(num + 1, nextNeededFrequency.getOrDefault(num + 1, 0) + 1);
            } else if (numFrequency.getOrDefault(num + 1, 0) > 0 && numFrequency.getOrDefault(num + 2, 0) > 0) {
                // or, we cannot append it to an existing one
                // try to form a new subsequence with [num, num + 1, num + 2]
                numFrequency.put(num + 1, numFrequency.get(num + 1) - 1);
                numFrequency.put(num + 2, numFrequency.get(num + 2) - 1);
                // then, the next needed number will be num + 3
                nextNeededFrequency.put(num + 3, nextNeededFrequency.getOrDefault(num + 3, 0) + 1);
            } else
                return false; // otherwise, we cannot form a subsequence with at least length 3 including num, return false
            // decrement the frequency of current number
            numFrequency.put(num, numFrequency.get(num) - 1);
        }
        return true;
    }

    @Test
    public void isPossibleTest() {
        /**
         * Example 1:
         * Input: nums = [1,2,3,3,4,5]
         * Output: true
         * Explanation: nums can be split into the following subsequences:
         * [1,2,3,3,4,5] --> 1, 2, 3
         * [1,2,3,3,4,5] --> 3, 4, 5
         */
        assertTrue(isPossiblePriorityQueue(new int[]{1, 2, 3, 3, 4, 5}));
        assertTrue(isPossibleHashMap(new int[]{1, 2, 3, 3, 4, 5}));
        /**
         * Example 2:
         * Input: nums = [1,2,3,3,4,4,5,5]
         * Output: true
         * Explanation: nums can be split into the following subsequences:
         * [1,2,3,3,4,4,5,5] --> 1, 2, 3, 4, 5
         * [1,2,3,3,4,4,5,5] --> 3, 4, 5
         */
        assertTrue(isPossiblePriorityQueue(new int[]{1, 2, 3, 3, 4, 4, 5, 5}));
        assertTrue(isPossibleHashMap(new int[]{1, 2, 3, 3, 4, 4, 5, 5}));
        /**
         * Example 3:
         * Input: nums = [1,2,3,4,4,5]
         * Output: false
         * Explanation: It is impossible to split nums into consecutive increasing subsequences of length 3 or more.
         */
        assertFalse(isPossiblePriorityQueue(new int[]{1, 2, 3, 4, 4, 5}));
        assertFalse(isPossibleHashMap(new int[]{1, 2, 3, 4, 4, 5}));
    }
}
