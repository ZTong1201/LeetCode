import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class ReversePairs {

    /**
     * Given an integer array nums, return the number of reverse pairs in the array.
     * <p>
     * A reverse pair is a pair (i, j) where 0 <= i < j < nums.length and nums[i] > 2 * nums[j].
     * <p>
     * Constraints:
     * <p>
     * 1 <= nums.length <= 5 * 10^4
     * -2^31 <= nums[i] <= 2^31 - 1
     * <p>
     * Approach: Fenwick Tree
     * Similar to LeetCode 315: https://leetcode.com/problems/count-of-smaller-numbers-after-self/
     * 本质是把此题转换为前缀和的计算。此题的pair需要两个条件
     * 1. i < j
     * 2. nums[i] > 2 * nums[j]
     * 对于第一个条件，如果将整个数组排序，那么对于所有的pair都满足i < j。同时如果想要满足num[i]比2倍的nums[j]还大，则需要判断是否在[0, j - 1]
     * 的subarray中存在比nums[i] / 2小的数。如果存在，那么当前为一个有效的pair，同时需要将该值累加在前缀和中。e.g. nums = [1, 3, 2, 3, 1]中，
     * sorted nums = [1, 1, 2, 3, 3]
     * 对于nums[3] = 3, 因为在sorted nums中存在小于等于3/2 = 1.5的数（1），那么nums[3]的有效pair数 =
     * numsOfPairs([x, 1]) + 1 where x < 1/2. +1是因为[1, 3]也是一个新的有效pair。
     * <p>
     * Time: O(nlogn)
     * Space: O(n)
     */
    public int reversePairs(int[] nums) {
        // clone the current array for sorting
        // such that we can find the index of a given value in log(n) time - binary search
        int[] sortedNums = nums.clone();
        Arrays.sort(sortedNums);

        int length = nums.length;
        // plus 1 because Fenwick tree is 1-indexed
        FenwickTree tree = new FenwickTree(length + 1);

        int count = 0;
        // count in the reverse way because the previous valid pairs will be accumulated
        for (int i = length - 1; i >= 0; i--) {
            // search whether the largest value < nums[i] / 2 such that nums[i] > 2 * nums[j] in this pair
            // e.g. for nums[i] = 3, if we have 1 in the array, then the number of reverse pairs for 3 = count(1)
            count += tree.query(searchFirstIndex(sortedNums, 1.0 * nums[i] / 2));
            // then the prefix sum should be updated for current value (nums[i])
            // plus 1 because Fenwick tree is 1-indexed
            // then we can increment one because we find a new pair (1, 3) where index(1) > index(3)
            tree.update(searchFirstIndex(sortedNums, nums[i]) + 1, 1);
        }
        return count;
    }

    private int searchFirstIndex(int[] nums, double target) {
        // 左闭右开区间
        int left = 0, right = nums.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            // since the first occurrence index is searched
            // discard the right half even if the midpoint equals to the target value
            if (nums[mid] >= target) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }

    private static class FenwickTree {

        private final int[] prefixSum;

        public FenwickTree(int size) {
            prefixSum = new int[size + 1];
        }

        public int query(int i) {
            int res = 0;
            while (i > 0) {
                res += prefixSum[i];
                i -= lowbit(i);
            }
            return res;
        }

        public void update(int i, int delta) {
            while (i < prefixSum.length) {
                prefixSum[i] += delta;
                i += lowbit(i);
            }
        }

        private int lowbit(int x) {
            return x & (-x);
        }
    }

    @Test
    public void reversePairsTest() {
        /**
         * Example 1:
         * Input: nums = [1,3,2,3,1]
         * Output: 2
         */
        assertEquals(2, reversePairs(new int[]{1, 3, 2, 3, 1}));
        /**
         * Example 2:
         * Input: nums = [2,4,3,5,1]
         * Output: 3
         */
        assertEquals(3, reversePairs(new int[]{2, 4, 3, 5, 1}));
    }
}
