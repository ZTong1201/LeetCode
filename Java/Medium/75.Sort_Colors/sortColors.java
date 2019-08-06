import org.junit.Test;
import static org.junit.Assert.*;

public class sortColors {

    /**
     * Given an array with n objects colored red, white or blue, sort them in-place so that objects of the same color are adjacent,
     * with the colors in the order red, white and blue.
     *
     * Here, we will use the integers 0, 1, and 2 to represent the color red, white, and blue respectively.
     *
     * Note: You are not suppose to use the library's sort function for this problem.
     *
     * Follow up:
     *
     * A rather straight forward solution is a two-pass algorithm using counting sort.
     * First, iterate the array counting number of 0's, 1's, and 2's, then overwrite array with total number of 0's, then 1's and
     * followed by 2's.
     * Could you come up with a one-pass algorithm using only constant space?
     *
     * Approach 1: Counting sort
     * 扫描整个array，记录0和1出现的次数，然后overwrite原array，array的最后都填上2
     *
     * Time: O(N)
     * Space: O(1)
     */
    public void sortColorsCounting(int[] nums) {
        int[] counts = new int[2];
        for(int num : nums) {
            if(num != 2) {
                counts[num] += 1;
            }
        }
        for(int i = 0; i < nums.length; i++) {
            if(i < counts[0]) {
                nums[i] = 0;
            } else if(i < counts[0] + counts[1]) {
                nums[i] = 1;
            } else {
                nums[i] = 2;
            }
        }
    }

    @Test
    public void sortColorsCountingTest() {
        /**
         * Example:
         * Input: [2,0,2,1,1,0]
         * Output: [0,0,1,1,2,2]
         */
        int[] nums = new int[]{2, 0, 2, 1, 1, 0};
        sortColorsCounting(nums);
        int[] expected = new int[]{0, 0, 1, 1, 2, 2};
        assertArrayEquals(expected, nums);
    }

    /**
     * Approach 2: Three pointers
     * 对于三种颜色排序，只需要将0和2排在合适的位置，中间（若有空位）则会全为1
     * 需要3个指针来分别记录0的右边界p0和2的左边界p2，以及当前访问元素curr
     * index < p0 元素皆为0， index > p2 元素皆为2
     * 算法如下：
     *
     * 1.将p0，curr初始化为index 0，p2初始化为index length - 1
     * 2.若nums[curr] = 0, 则将nums[curr]和nums[p0]互换，然后右移curr和p0
     * 3.若nums[curr] = 2，则将nums[curr]和nums[p2]互换，然后左移p2
     * 4.若nums[curr] = 1，则只移动curr即可，直到curr和p2相交
     *
     * Time: O(N)
     * Space: O(1)
     */
    public void sortColorsThreePointers(int[] nums) {
        int p0 = 0, p2 = nums.length - 1;
        int curr = 0;

        while(curr <= p2) {
            if(nums[curr] == 0) {
                int temp = nums[curr];
                nums[curr++] = nums[p0];
                nums[p0++] = temp;
            } else if(nums[curr] == 2) {
                int temp = nums[curr];
                nums[curr] = nums[p2];
                nums[p2--] = temp;
            } else {
                curr++;
            }
        }
    }

    @Test
    public void sortColorsThreePointersTest() {
        /**
         * Example:
         * Input: [2,0,2,1,1,0]
         * Output: [0,0,1,1,2,2]
         */
        int[] nums = new int[]{2, 0, 2, 1, 1, 0};
        sortColorsThreePointers(nums);
        int[] expected = new int[]{0, 0, 1, 1, 2, 2};
        assertArrayEquals(expected, nums);
    }

    /**
     * Approach 3: Quick Sort
     * in-place的排序永远可以考虑使用quick sort
     *
     * Time: O(nlogn)
     * Space: O(1)
     */
    public void sortColorsQuickSort(int[] nums) {
        quickSort(nums, 0, nums.length - 1);
    }

    private void quickSort(int[] nums, int low, int high) {
        if(nums == null || nums.length == 0) return;
        if(low >= high) return;

        int i = low, j = high;
        //选择中间值作为pivot
        int mid = low + (high - low) / 2;
        int pivot = nums[mid];

        while(i <= j) {
            while(nums[i] < pivot) {
                i++;
            }
            while(nums[j] > pivot) {
                j--;
            }
            //找到第一对处于错位位置的数字，若此时两个指针尚未相交，则交换当前两元素
            //保证pivot左边的元素都小于它，右边的元素都大于它
            if(i <= j) {
                int temp = nums[i];
                nums[i] = nums[j];
                nums[j] = temp;
                i++;
                j--;
            }
        }
        //继续sort剩下的两个subarray
        quickSort(nums, low, j);
        quickSort(nums, i, high);
    }

    @Test
    public void sortColorsQuickSortTest() {
        /**
         * Example:
         * Input: [2,0,2,1,1,0]
         * Output: [0,0,1,1,2,2]
         */
        int[] nums = new int[]{2, 0, 2, 1, 1, 0};
        sortColorsQuickSort(nums);
        int[] expected = new int[]{0, 0, 1, 1, 2, 2};
        assertArrayEquals(expected, nums);
    }
}
