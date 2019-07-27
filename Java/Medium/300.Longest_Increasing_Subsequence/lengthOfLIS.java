import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class lengthOfLIS {

    /**
     * Given an unsorted array of integers, find the length of longest increasing subsequence.
     *
     * Note:
     *
     * There may be more than one LIS combination, it is only necessary for you to return the length.
     * Your algorithm should run in O(n^2) complexity.
     *
     * Approach 1: Brute Force
     * At each index, we can compute the LIS with/without current element by recursively calling a helper function. The helper function
     * will take the current element into account only if the current element is larger than a previous element we considered. Otherwise,
     * the helper function will skip the current element for further searching. The LIS at the current index will the maximum of LIS with
     * and without including the current element.
     *
     * Time: O(2^n) The recursion tree can have 2^n nodes in the worst case
     * Space: O(n) the call stack will require up to O(n) space
     */
    public int lengthOfLISBruteForce(int[] nums) {
        return helper(nums, Integer.MIN_VALUE, 0);
    }

    private int helper(int[] nums, int prev, int currPos) {
        //base case, if searching is out of the array boundary, return 0
        if(currPos == nums.length) {
            return 0;
        }
        //The length of LIS with current element included
        int included = 0;
        if(nums[currPos] > prev) {
            //only if current element is larger than the previous value
            //we can obtain a longer LIS by taking current element into account
            //if so, the previous value for our next searching will be the current element
            included = 1 + helper(nums, nums[currPos], currPos + 1);
        }
        //The length of LIS without current element
        //The previous value will be unchanged for our next call
        int notIncluded = helper(nums, prev, currPos + 1);
        //The final result will be the maximum
        return Math.max(included, notIncluded);
    }

    /**
     * Approach 2: Recursion + Memorization
     * The brute force method will make a lot of redundant calls to compute values we have pre-computed in the previous calls. Hence, we
     * can actually memorize these values. As long as we reach a previous stage we have already computed, we can directly return that
     * value in O(1) time. By doing so, we are actually searching all of the subsequence in the array, which will be O(n^2) combinations.
     *
     * Time: O(n^2)
     * Space: O(n^2) we need 2-D memo array to store pre-computed values
     */
    public int lengthOfLISMemorization(int[] nums) {
        int[][] memo = new int[nums.length][nums.length];
        //fill every entry with -1 indicating we have visited none of them
        for(int[] l : memo) {
            Arrays.fill(l, -1);
        }
        return helper(nums, -1, 0, memo);
    }

    private int helper(int[] nums, int prevIndex, int currPos, int[][] memo) {
        //base case, if out of array boundary, return 0
        if(currPos == nums.length) {
            return 0;
        }
        //if we have reached a pre-computed value, i.e. the entry in the memo cell is no longer -1, we return that value
        if(memo[prevIndex + 1][currPos] > -1) {
            return memo[prevIndex + 1][currPos];
        }
        //otherwise, the process will be exactly the same with approach 1
        int included = 0;
        //The only difference is, we need to make sure we make the first call, since the previous index is initialized as -1
        if(prevIndex == -1 || nums[currPos] > nums[prevIndex]) {
            included = 1 + helper(nums, currPos, currPos + 1, memo);
        }
        int notIncluded = helper(nums, prevIndex, currPos + 1, memo);
        //After computing these two values, instead of directly returning, we store them in the 2-D memo array
        memo[prevIndex + 1][currPos] = Math.max(included, notIncluded);
        return memo[prevIndex + 1][currPos];
    }

    /**
     * Approach 3: Dynamic Programming
     *
     * The idea behind the scenes is that at current index, the length of LIS will not be influenced by the subsequent values. Hence, if
     * we can record the length of LIS at each index prior to current position, we will know the length of LIS at current position.
     * Hence dp[j] = max(dp[i]) + 1 for 0 <= i < j if nums[j] > nums[i]
     * The final result will be the maximum value of the dp array res = max(dp[i]) 0 <= i < length
     *
     * Time: O(n^2) at each index, we need to look back from the start of the array, hence we have two nested for loops
     * Space: O(n) we need a dp array to store length of LIS at each index
     */
    public int lengthOfLISDP(int[] nums) {
        int length = nums.length;
        int res = 0;
        if(length == 0) return res;
        int[] dp = new int[length];
        //initialize the first index with value 1
        //since one single element is the smallest LIS we can have
        dp[0] = 1;
        for(int i = 0; i < length; i++) {
            //we need to find the maximum value from index 0 to i - 1
            int maxVal = 0;
            for(int j = 0; j < i; j++) {
                //only if the current element is larger can we form a longer LIS
                if(nums[i] > nums[j]) {
                    maxVal = Math.max(maxVal, dp[j]);
                }
            }
            //update current length of LIS
            dp[i] = maxVal + 1;
            //update the final result
            res = Math.max(dp[i], res);
        }
        return res;
    }

    /**
     * Follow up: Could you improve it to O(n log n) time complexity?
     *
     * Approach 4: Dynamic Programming with Binary Search
     *
     * This approach is more like a greedy algorithm, since we would like to find the length of LIS, we want at each index, the value
     * will be as small as possible, hence when we add a new potential element, we have a larger possibility to form a longer LIS. Therefore,
     * the algorithm will look like this:
     *
     * 1. When we encounter a new element, if it is in our dp array, simply replace the value
     * 2. If it is larger than the largest value in the dp array (> dp[dp.size() - 1]), append the element in the end, since we find a longer
     *    LIS, and update the length
     * 3. If it is smaller yet not in the array, we find the correct insertion position (the first element larger than current value)
     *    and replace it with this smaller value.
     *
     * The above process can be done by using binary search. Note that the final sequence might well be an invalid LIS, however, the length
     * of the sequence will be the desired answer
     *
     * Time: O(nlogn) for each element, we implement a binary search
     * Space: O(n)  we need an array of size n to store potential LIS
     */
    public int lengthOfLISBinarySearch(int[] nums) {
        int length = nums.length;
        int len = 0;
        int[] dp = new int[length];
        //iterate over the entire array
        for(int num : nums) {
            //find the correct index of current element among previous stored values
            int index = binarySearch(dp, 0, len, num);
            //replace the correct index with a smaller value
            //or append the current at the end of the previous sequence (binary search will return len, if cannot find correct insertion pos)
            dp[index] = num;
            //if the index to be replaced equals to len, we extend the searching range
            if(index == len) {
                len++;
            }
        }
        //return the length of the sequence, which is the correct answer
        return len;
    }

    private int binarySearch(int[] nums, int left, int right, int val) {
        while(left < right) {
            int mid = left + (right - left) / 2;
            if(nums[mid] == val) {
                return mid;
            } else if(nums[mid] < val) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }




    @Test
    public void lengthOfLISTest() {
        /**
         * Example:
         * Input: [10,9,2,5,3,7,101,18]
         * Output: 4
         * Explanation: The longest increasing subsequence is [2,3,7,101], therefore the length is 4.
         */
        int[] nums1 = new int[]{10, 9, 2, 5, 3, 7, 101, 18};
        assertEquals(4, lengthOfLISBruteForce(nums1));
        assertEquals(4, lengthOfLISMemorization(nums1));
        assertEquals(4, lengthOfLISDP(nums1));
        assertEquals(4, lengthOfLISBinarySearch(nums1));
        /**
         * Example 2:
         * Input: [4,10,4,3,8,9]
         * Output: 3
         * Explanation: The longest increasing subsequence is [4, 8, 9], therefore the length is 3.
         */
        int[] nums2 = new int[]{4, 10, 4, 3, 8, 9};
        assertEquals(3, lengthOfLISBruteForce(nums2));
        assertEquals(3, lengthOfLISMemorization(nums2));
        assertEquals(3, lengthOfLISDP(nums2));
        assertEquals(3, lengthOfLISBinarySearch(nums2));
        /**
         * Example 3:
         * Input: [10,9,2,5,3,4]
         * Output: 3
         * Explanation: The longest increasing subsequence is [2, 3, 4], therefore the length is 3.
         */
        int[] nums3 = new int[]{10, 9, 2, 5, 3, 4};
        assertEquals(3, lengthOfLISBruteForce(nums3));
        assertEquals(3, lengthOfLISMemorization(nums3));
        assertEquals(3, lengthOfLISDP(nums3));
        assertEquals(3, lengthOfLISBinarySearch(nums3));
    }
}
