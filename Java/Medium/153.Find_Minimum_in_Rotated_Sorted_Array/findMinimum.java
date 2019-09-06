import org.junit.Test;
import static org.junit.Assert.*;

public class findMinimum {

    /**
     * Suppose an array sorted in ascending order is rotated at some pivot unknown to you beforehand.
     * (i.e.,  [0,1,2,4,5,6,7] might become  [4,5,6,7,0,1,2]).
     *
     * Find the minimum element.
     * You may assume no duplicate exists in the array.
     *
     * Approach 1: Linear Scan
     * 直接先将最小值初始化为数组第一个元素，因为数组时排好序后rotate的，因此只要找到第一个小于初始数字的num即为最终答案
     *
     * Time: O(n) 最坏情况下，数组没有被rotate，需要遍历到数组结尾，然后返回初始化的数值
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
     * Approach 2: Binary Search
     * 如果给定的数组是sorted的，那么该数组的起始元素一定小于末尾元素。可以利用这个性质在每次search时舍弃掉一半的数组元素。每次search时，寻找当前元素的中点值，
     * 若该值小于其最右端的值，说明右半部分已经是sorted的，那么就继续搜索左半部分。若中点值大于最右端元素，说明左半部分已经是sorted，应该在右半部分里继续寻找。
     * 当数组仅剩下一个元素时，该元素即为最小值
     *
     * Time: O(logn)
     * Space: O(1)
     */
    public int findMinBinarySearch(int[] nums) {
        int left = 0, right = nums.length - 1;
        while (left < right) {
            int mid = left + (right - left) / 2;
            //若中点值大于最右端的值，说明左半部分已经是sorted的，在右半部分里搜索
            if (nums[mid] > nums[right]) {
                left = mid + 1;
            } else {
                //否则说明右半部分已经是sorted，在左半部分里搜索
                right = mid;
            }
        }
        return nums[left];
    }

    @Test
    public void findMinTest() {
        /**
         * Example 1:
         * Input: [3,4,5,1,2]
         * Output: 1
         */
        int[] nums1 = new int[]{3, 4, 5, 1, 2};
        assertEquals(1, findMinLinear(nums1));
        assertEquals(1, findMinBinarySearch(nums1));
        /**
         * Example 2:
         * Input: [4,5,6,7,0,1,2]
         * Output: 0
         */
        int[] nums2 = new int[]{4, 5, 6, 7, 0, 1, 2};
        assertEquals(0, findMinLinear(nums2));
        assertEquals(0, findMinBinarySearch(nums2));
    }
}
