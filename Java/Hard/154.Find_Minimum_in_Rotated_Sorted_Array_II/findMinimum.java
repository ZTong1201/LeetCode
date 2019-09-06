import org.junit.Test;
import static org.junit.Assert.*;

public class findMinimum {

    /**
     * Suppose an array sorted in ascending order is rotated at some pivot unknown to you beforehand.
     *
     * (i.e.,  [0,1,2,4,5,6,7] might become  [4,5,6,7,0,1,2]).
     *
     * Find the minimum element.
     *
     * The array may contain duplicates.
     *
     * Note:
     *
     * This is a follow up problem to 153.Find Minimum in Rotated Sorted Array.
     * Would allow duplicates affect the run-time complexity? How and why?
     *
     * Approach 1: Linear Scan
     * 即时允许重复元素存在，也可以遍历整个数组得到最小值
     *
     * Time: O(n)
     * Space: O(1)
     */
    public int findMinLinear(int[] nums) {
        int res = nums[0];
        for(int num : nums) {
            if(num < res) {
                res = num;
                break;
            }
        }
        return res;
    }

    /**
     * Approach 2: Convert back to Problem 153 (Binary Search)
     * 因为数组中现在存在重复元素，因此无法直接进行binary search。因为当中间值与最右端值相等时，最小元素可能在左边也可能在右边。e.g. [3, 3, 1, 3]最小值在
     * 数组右半部分，或者[1, 3, 3]，最小值在数组左半部分。因此可以将题目转化回153，即先让数组跳过所有的重复元素，然后在非重复元素中，重复上述步骤即可。
     *
     * Time: O(n) in worst case，最坏情况下，数组可以全为重复元素，需要遍历所有数组元素才能得到答案
     * Space: O(1)
     */
    public int findMinBinarySearch1(int[] nums) {
        int low = 0, high = nums.length - 1;
        while (low < high) {
            //首先，从前往后和从后往前跳过所有重复元素
            while (low < high && nums[low] == nums[low + 1]) {
                low++;
            }
            while (low < high && nums[high] == nums[high - 1]) {
                high--;
            }
            //剩下的部分和153题一样，可以进行binary search
            int mid = low + (high - low) / 2;
            if(nums[mid] > nums[high]) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return nums[low];
    }

    /**
     * Approach 3: Binary Search
     * 或者可以直接进行binary search，但正如方法二讨论，当中间值与最右端的值相等时，左右两端都有可能存在最小值，因此在此情况下，需要对左右两边都进行binary
     * search
     *
     * Time: O(n) in worst case，最坏情况下，仍然需要遍历整个数组
     * Space: O(n) 因为是递归地进行binary search，call stack需要记录每次递归的状态，最坏情况下也需要O(n)空间
     */
    public int findMinBinarySearch2(int[] nums) {
        int[] res = new int[]{nums[0]};
        binarySearch(nums, 0, nums.length - 1, res);
        return res[0];
    }

    private void binarySearch(int[] nums, int low, int high, int[] res) {
        //base case，若遍历空数组，则无需继续查找
        if (low > high) {
            return;
        }
        int mid = low + (high - low) / 2;
        //更新当前记录的最小值
        res[0] = Math.min(res[0], nums[mid]);
        //与之前一样，若中间值小于最右端值，说明右半部分已经排好序，可以只查看左边
        if (nums[mid] < nums[high]) {
            binarySearch(nums, low, mid - 1, res);
        } else if (nums[mid] > nums[high]) {
            //若中间值大于最右端值，说明左半部分已经排好序，可以只看右边
            binarySearch(nums, mid + 1, high, res);
        } else {
            //否则说明两值相等，此时最小值可能在任意一侧，需要对两边都继续做binary search
            binarySearch(nums, low, mid - 1, res);
            binarySearch(nums, mid + 1, high, res);
        }
    }

    @Test
    public void findMinTest() {
        /**
         * Example 1:
         * Input: [1,3,5]
         * Output: 1
         */
        int[] nums1 = new int[]{1, 3, 5};
        assertEquals(1, findMinLinear(nums1));
        assertEquals(1, findMinBinarySearch1(nums1));
        assertEquals(1, findMinBinarySearch2(nums1));
        /**
         * Example 2:
         * Input: [2,2,2,0,1]
         * Output: 0
         */
        int[] nums2 = new int[]{2, 2, 2, 0, 1};
        assertEquals(0, findMinLinear(nums2));
        assertEquals(0, findMinBinarySearch1(nums2));
        assertEquals(0, findMinBinarySearch2(nums2));
    }
}
