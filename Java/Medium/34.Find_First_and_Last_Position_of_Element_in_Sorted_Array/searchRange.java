import org.junit.Test;
import static org.junit.Assert.*;

public class searchRange {

    /**
     * Given an array of integers nums sorted in ascending order, find the starting and ending position of a given target value.
     *
     * Your algorithm's runtime complexity must be in the order of O(log n).
     *
     * If the target is not found in the array, return [-1, -1].
     *
     * Approach 1: Linear Scan
     * First pass starts from the front of the array find the first position, second pass starts from the end of the array, find the last.
     *
     * Time: O(n)
     * Space: O(1)
     */
    public int[] searchRangeLinearScan(int[] nums, int target) {
        int[] res = new int[]{-1, -1};
        for(int i = 0; i < nums.length; i++) {
            if(nums[i] == target) {
                res[0] = i;
                break;
            }
        }

        //if res[0] == -1, we didn't find the target value
        if(res[0] == -1) return res;

        //otherwise, we search for the last position
        for(int j = nums.length - 1; j >= 0; j--) {
            if(nums[j] == target) {
                res[1] = j;
                break;
            }
        }
        return res;
    }

    @Test
    public void searchRangeLinearScanTest() {
        /**
         * Example 1:
         * Input: nums = [5,7,7,8,8,10], target = 8
         * Output: [3,4]
         */
        int[] nums1 = new int[]{5, 7, 7, 8, 8, 10};
        int[] expected1 = searchRangeLinearScan(nums1, 8);
        int[] actual1 = new int[]{3, 4};
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: nums = [5,7,7,8,8,10], target = 6
         * Output: [-1,-1]
         */
        int[] nums2 = new int[]{5, 7, 7, 8, 8, 10};
        int[] expected2 = searchRangeLinearScan(nums2, 6);
        int[] actual2 = new int[]{-1, -1};
        assertArrayEquals(expected2, actual2);
    }


    /**
     * Approach 2: Binary Search
     * We can do two binary search to find the first and last position.
     * When finding the first position, if the midpoint equals to the target, we still need to search the left subarray. Similarly, when
     * finding the last position, if the midpoint equals to the target, we need to check the right subarray.
     * The terminating condition is when left and right pointers meet each other.
     */
    public int[] searchRangeBinarySearch(int[] nums, int target) {
        int left = 0, right = nums.length - 1; //assign two pointers to search target
        boolean found = false; //a boolean variable to check whether we actually found one target number

        //first binary search to find first position
        while(left <= right) {
            int mid = left + (right - left) / 2;

            //if the value at the midpoint larger than or equal to the target, search the left subarray
            if(nums[mid] >= target) {
                if(nums[mid] == target) found = true; //check whether we found the target
                right = mid - 1;
            } else {
                //otherwise, check the right subarray
                left = mid + 1;
            }
        }

        //assign two new pointers, whereas the starting point is now the first position of target (or just 0)
        int newLeft = left;
        right = nums.length - 1;
        while(newLeft <= right) {
            int mid = newLeft + (right - newLeft) / 2;
            //this time, if the value of midpoint is smaller than or equal to target, search the right subarray
            if(nums[mid] <= target) {
                newLeft = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return found ? new int[]{left, right} : new int[]{-1, -1};
    }

    @Test
    public void searchRangeBinarySearchTest() {
        /**
         * Example 1:
         * Input: nums = [5,7,7,8,8,10], target = 8
         * Output: [3,4]
         */
        int[] nums1 = new int[]{5, 7, 7, 8, 8, 10};
        int[] expected1 = searchRangeBinarySearch(nums1, 8);
        int[] actual1 = new int[]{3, 4};
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: nums = [5,7,7,8,8,10], target = 6
         * Output: [-1,-1]
         */
        int[] nums2 = new int[]{5, 7, 7, 8, 8, 10};
        int[] expected2 = searchRangeBinarySearch(nums2, 6);
        int[] actual2 = new int[]{-1, -1};
        assertArrayEquals(expected2, actual2);
    }
}
