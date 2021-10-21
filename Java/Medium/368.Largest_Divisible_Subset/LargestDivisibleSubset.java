import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class LargestDivisibleSubset {

    /**
     * Given a set of distinct positive integers nums, return the largest subset answer such that every pair
     * (answer[i], answer[j]) of elements in this subset satisfies:
     * <p>
     * answer[i] % answer[j] == 0, or
     * answer[j] % answer[i] == 0
     * If there are multiple solutions, return any of them.
     * <p>
     * Constraints:
     * <p>
     * 1 <= nums.length <= 1000
     * 1 <= nums[i] <= 2 * 10^9
     * All the integers in nums are unique.
     * <p>
     * Approach: Sorting + DP
     * An important property of modulo for this problem is that, if the given subset {E, F, G} is a divisible subset, and the
     * set is already sorted in which E < F < G. Then if given a new number H > G and H % G == 0, then we can form a larger
     * subset {E, F, G, H} without taking modulo of every pair in the set.
     * <p>
     * Therefore, we need to sort the entire array to make sure the array is arranged from the smallest to the largest. Then,
     * for a given number at index i, we just need to go through all smaller numbers at index j in range [0, i) and see
     * whether nums[i] % nums[j] == 0, if yes, then we obtain a larger size. We can use DP to record the largest divisible
     * subset size at index i. However, the problem is not just asking the size, we need a way to construct the entire
     * subset. Hence, we also need another DP array to also keep track of at which index j we actually can form a larger
     * set for index i. We also need global variables record the current max size and the last index of the elements in the
     * subset, when we actually get a larger set, we can update the global result accordingly.
     * <p>
     * Time: O(n^2) the sorting algorithm takes O(nlogn) time. However, in order to find the largest size, we need a nested
     * loop to enumerate all pairs at each index, which takes O(n^2) in total.
     * Space: O(n)
     */
    public List<Integer> largestDivisibleSubset(int[] nums) {
        // the key part of this algorithm is to sort the array first
        Arrays.sort(nums);
        int n = nums.length;
        // need to two 1-D arrays to keep track of the largest size so far,
        // and the previous element index in order to make this larger set
        int[] sequencePrevIndex = new int[n], largestSizeAtIndex = new int[n];
        // also need two global variables to record the largest size & where it ends
        int maxSize = 0, sequenceEndIndex = 0;
        // each element itself is a single subset, so we initialize every index with 1
        Arrays.fill(largestSizeAtIndex, 1);
        // as a result, each subset will end with itself initially
        for (int i = 0; i < n; i++) {
            sequencePrevIndex[i] = i;
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                // enumerate all pairs
                if (nums[i] % nums[j] == 0) {
                    // if we find a larger subset - update the result
                    if (largestSizeAtIndex[j] + 1 > largestSizeAtIndex[i]) {
                        largestSizeAtIndex[i] = largestSizeAtIndex[j] + 1;
                        sequencePrevIndex[i] = j;
                    }
                }
            }
            // after getting the largest subset we can make ending with nums[i]
            // we now check whether it gives a global maximum as well
            if (largestSizeAtIndex[i] > maxSize) {
                maxSize = largestSizeAtIndex[i];
                sequenceEndIndex = i;
            }
        }

        List<Integer> res = new ArrayList<>();
        // now we need to construct the subset backwards
        int index = sequenceEndIndex;
        while (sequencePrevIndex[index] != index) {
            res.add(nums[index]);
            index = sequencePrevIndex[index];
        }
        // also need to add the very beginning element where nums[i] = i
        res.add(nums[index]);
        return res;
    }

    @Test
    public void largestDivisibleSubsetTest() {
        /**
         * Example 1:
         * Input: nums = [1,2,3]
         * Output: [1,2]
         * Explanation: [2,1], [1,3], [3,1] are also accepted.
         */
        List<Integer> expected1 = List.of(2, 1);
        List<Integer> actual1 = largestDivisibleSubset(new int[]{1, 2, 3});
        assertEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: nums = [1,2,4,8]
         * Output: [1,2,4,8]
         */
        List<Integer> expected2 = List.of(8, 4, 2, 1);
        List<Integer> actual2 = largestDivisibleSubset(new int[]{1, 2, 4, 8});
        assertEquals(expected2, actual2);
        /**
         * Example 3:
         * Input: nums = [3,4,16,8]
         * Output: [4,8,16]
         */
        List<Integer> expected3 = List.of(16, 8, 4);
        List<Integer> actual3 = largestDivisibleSubset(new int[]{3, 4, 16, 8});
        assertEquals(expected3, actual3);
    }
}
