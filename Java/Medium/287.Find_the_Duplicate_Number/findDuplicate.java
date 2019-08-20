import org.junit.Test;
import static org.junit.Assert.*;

public class findDuplicate {

    /**
     * Given an array nums containing n + 1 integers where each integer is between 1 and n (inclusive), prove that at least one duplicate
     * number must exist. Assume that there is only one duplicate number, find the duplicate one.
     *
     * Note:
     *
     * You must not modify the array (assume the array is read only).
     * You must use only constant, O(1) extra space.
     * Your runtime complexity should be less than O(n2).
     * There is only one duplicate number in the array, but it could be repeated more than once.
     *
     * Approach: Detect Cycle
     * 此题和142. Linked List Cycle II相同，根据题意，可以认为每个index的值就是指向的下一个节点，因为数组中有重复元素，因此必然有两个index同时指向
     * 一个别的index。说明该"链表"中有环，对于找到环的方法，可以用快慢指针同时从起点出发，然后当快慢指针指向同一个节点时，该环被找到。若想找到重复元素，
     * 则要找到链表的头部。此时将某指针指向链表的起始端，然后每个指针每次移动一步。当两指针再次相遇，此时两指针均指向链表头部。
     *
     * Time: O(N)
     * Space: O(1)
     */
    public int findDuplicateDetectCycle(int[] nums) {
        int slow = nums[0], fast = nums[0];
        //使用do while是为了保证该循环至少进行一次。
        do {
            //慢指针每次移动两步，快指针每次移动一步
            slow = nums[slow];
            fast = nums[nums[fast]];
        } while(slow != fast);
        //然后将某指针重设为链表头部
        slow = nums[0];
        //双指针每次都移动一步，直到两者再次相等
        while(slow != fast) {
            slow = nums[slow];
            fast = nums[fast];
        }
        return slow;
    }

    /**
     * Approach 2: Binary Search
     * 此题可以改进binary search算法来计算。本质上，因为整个数组包含了1到n的值，且数组中有重复元素。那么1和n取中值，然后遍历整个数组，若数组中元素小于等于
     * 中值mid的个数大于中值，说明在小于中值的元素中有重复，反之，则是大于中值的元素中有重复值。因此每次可以舍弃掉一半的数值来重新查找，最终找到的值即为重复元素。
     *
     *
     * Time: O(nlogn) binary search的运行时间为O(logn)，每次二分查找时，需要遍历整个数组，找到当前小于等于中值的数值个数，时间为O(n)，所以总的时间为O(nlogn)
     * Space: O(1)
     */
    public int findDuplicateBinarySearch(int[] nums) {
        int low = 1, high = nums.length;  //l = 1, r = n + 1（左必右开）
        while(low < high) {
            int mid = low + (high - low) / 2;
            int count = 0;
            for(int num : nums) {
                //记录数组中小于等于中值元素的个数
                if(num <= mid) {
                    count++;
                }
            }
            //若count大于中值，说明小于中值的元素中有duplicate，舍弃右半部分
            if(count > mid) {
                high = mid;
            } else {
                //反之，大于中值的元素中有duplicate，舍弃左半部分
                low = mid + 1;
            }
        }
        return low;
    }

    @Test
    public void findDuplicateTest() {
        /**
         * Example 1:
         * Input: [1,3,4,2,2]
         * Output: 2
         */
        int[] nums1 = new int[]{1, 3, 4, 2, 2};
        assertEquals(2, findDuplicateDetectCycle(nums1));
        assertEquals(2, findDuplicateBinarySearch(nums1));
        /**
         * Example 2:
         * Input: [3,1,3,4,2]
         * Output: 3
         */
        int[] nums2 = new int[]{3, 1, 3, 4, 2};
        assertEquals(3, findDuplicateDetectCycle(nums2));
        assertEquals(3, findDuplicateBinarySearch(nums2));
    }
}
