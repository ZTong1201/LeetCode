import org.junit.Test;
import static org.junit.Assert.*;

public class minSubArrayLen {

    /**
     * Given an array of n positive integers and a positive integer s, find the minimal length of a contiguous subarray
     * of which the sum ≥ s. If there isn't one, return 0 instead.
     *
     * Approach 1: Two pointers
     * We can solve this problem using Two pointers (or sliding windows). By incrementing the fast pointer, we broaden the window, and
     * accumulate the sum, if the sum is less than s (target), we keep incrementing fast pointer. When the sum >= s, which means we potentially
     * have more elements in the current window, hence we need to increment the slow pointer to narrow down the window. As long as incrementing
     * the slow pointer (i.e. subtract current slow element from the sum) cannot make sum < s, we keep incrementing the slow pointer. If we
     * narrow down the window, which means we potentially have a smaller length, we update our final result. (current window size is fast - slow + 1)
     * When the fast pointer reach the end of the array, we're done.
     *
     * One thing should note that, if the result has never been updated, which means we cannot find a subarray which sums up to s. If that
     * is the case, return 0.
     *
     *
     * Time: O(n) in the worst case, all the elements will be visited twice by slow and fast pointer.
     * Space: O(1) only assign two pointers, require constant space
     */
    public int minSubArrayLenTwoPointers(int s, int[] nums) {
        int length = nums.length;
        int sum = 0, res = Integer.MAX_VALUE;
        int slow = 0, fast = 0;
        while(fast < length) {
            //accumulate sum to the fast pointer
            sum += nums[fast];
            //if the current sum is larger than or equal to s, narrow down the window
            if(sum >= s) {
                //as long as narrow down the window will lead to a sum >= s, keep narrowing down the window
                while (sum - nums[slow] >= s) {
                    sum -= nums[slow];
                    slow++;
                }
                //update the final result to a smaller value
                res = Math.min(res, fast - slow + 1);
            }
            fast++;
        }
        //if res hasn't been updated, we cannot find a subarray sum up to s, return 0
        //otherwise return res
        return res == Integer.MAX_VALUE ? 0 : res;
    }

    @Test
    public void minSubArrayLenTwoPointersTest() {
        /**
         * Example 1:
         * Input: s = 7, nums = [2, 3, 1, 2, 4, 3]
         * Output: 2
         * Explanation: the subarray [4,3] has the minimal length under the problem constraint.
         */
        int[] nums1 = new int[]{2, 3, 1, 2, 4, 3};
        assertEquals(2, minSubArrayLenTwoPointers(7, nums1));
        /**
         * Example 2:
         * Input: s = 11, nums = [1, 2, 3, 4, 5]
         * Output: 3
         */
        int[] nums2 = new int[]{1, 2, 3, 4, 5};
        assertEquals(3, minSubArrayLenTwoPointers(11, nums2));
        /**
         * Example 3:
         * Input: s = 15, nums = [5, 1, 3, 5, 10, 7, 4, 9, 2, 8]
         * Output: 2
         */
        int[] nums3 = new int[]{5, 1, 3, 5, 10, 7, 4, 9, 2, 8};
        assertEquals(2, minSubArrayLenTwoPointers(15, nums3));
        /**
         * Example 4:
         * Input s = 3, nums = [1, 1]
         * Output: 0
         */
        int[] nums4 = new int[]{1, 1};
        assertEquals(0, minSubArrayLenTwoPointers(3, nums4));
    }


    /**
     * Approach 2: Binary Search
     * Since the array contains only positive numbers, hence the cumulative sum will be non-decreasing. We can construct a new array,
     * at each index i, it stores the cumulative sum from 0 to index i - 1. And we have the property that sum[j] - sum[i] = sum[i,j]
     * (from index i to index j - 1). If sum[j] - sum[i] >= s, the length of the subarray will be simply j - i + 1. Therefore, we can
     * iterate over each cumulative sum, and search the target value s + sum[i]. Since the cumulative sum array is non-decreasing, we can
     * use binary search to accelerate the searching process. If we find the exact target, we return that index, otherwise, we actually
     * return the index of a larger value which closest to the target. If we cannot find that value, the return index will be out of the
     * boundary.
     *
     * As a result, if the returning index is inside the boundary, we update the result, otherwise, we move to the next cumulative sum
     *
     * Time: O(nlogn), since for each cumulative sum, we need to search for a target value. Binary search will cost O(logn) time.
     * Space: O(n), we need an extra array to store the cumulative sum from 0 to index i - 1.
     */
    public int minSubArrayLenBinarySearch(int s, int[] nums) {
        int[] sum = new int[nums.length + 1];
        int res = Integer.MAX_VALUE;
        for(int i = 1; i < sum.length; i++) {
            //build the non-decreasing cumulative sum array
            sum[i] = sum[i - 1] + nums[i - 1];
        }

        //for every cumulative sum before the final sum
        for(int i = 1; i < sum.length; i++) {
            //we need to search for a target sum
            int target = s + sum[i - 1];
            //always search in the rest of the array is enough
            //we use a left close and right open range
            int index = binarySearch(sum, target, i, sum.length);
            //only if we found something, i.e. index is inside the boundary, we update the result
            if(index != sum.length) {
                res = Math.min(res, index - i + 1);
            }
        }
        //if result hasn't been updated, return 0. Otherwise, return result.
        return res == Integer.MAX_VALUE ? 0 : res;
    }

    private int binarySearch(int[] sum, int target, int left, int right) {
        while(left < right) {
            int mid = left + (right - left) / 2;
            if(sum[mid] == target) {
                return mid;
            } else if(sum[mid] < target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }


    @Test
    public void minSubArrayLenBinarySearchTest() {
        /**
         * Example 1:
         * Input: s = 7, nums = [2, 3, 1, 2, 4, 3]
         * Output: 2
         * Explanation: the subarray [4,3] has the minimal length under the problem constraint.
         */
        int[] nums1 = new int[]{2, 3, 1, 2, 4, 3};
        assertEquals(2, minSubArrayLenBinarySearch(7, nums1));
        /**
         * Example 2:
         * Input: s = 11, nums = [1, 2, 3, 4, 5]
         * Output: 3
         */
        int[] nums2 = new int[]{1, 2, 3, 4, 5};
        assertEquals(3, minSubArrayLenBinarySearch(11, nums2));
        /**
         * Example 3:
         * Input: s = 15, nums = [5, 1, 3, 5, 10, 7, 4, 9, 2, 8]
         * Output: 2
         */
        int[] nums3 = new int[]{5, 1, 3, 5, 10, 7, 4, 9, 2, 8};
        assertEquals(2, minSubArrayLenBinarySearch(15, nums3));
        /**
         * Example 4:
         * Input s = 3, nums = [1, 1]
         * Output: 0
         */
        int[] nums4 = new int[]{1, 1};
        assertEquals(0, minSubArrayLenBinarySearch(3, nums4));
    }
}
