import org.junit.Test;
import static org.junit.Assert.*;

public class firstMissingPositive {

    /**
     * Given an unsorted integer array, find the smallest missing positive integer.
     *
     * Note:
     *
     * Your algorithm should run in O(n) time and uses constant extra space.
     *
     * Approach: Hash Index
     * 给定一个size为n的数组，理想情况下，数组中元素为1 - n，那么数组中缺失的元素一定为n + 1，在其他情况下，比如数组中含有重复元素，或者存在<= 0或 > n的元素，
     * 那么该缺失元素会在[1, n]之内。所以需要先遍历一遍数组，将<= 0和 > n的数字先变成1，同时在第一遍遍历过程中查看1是否在数组中。若1不在数组中，则需要返回1
     *
     * 为了使用常数空间判断某positive number是否在数组中，需要对该元素进行hash，因为数组中元素理想状态为1 - n，那么对应每个元素都可以将其hash到数组的index
     * 0 - (n - 1)上，即遍历整个数组，若某数字num存在，则将其对应的index所在的元素变为负数，即使得nums[num - 1] = -nums[num - 1]，后续再遍历改变过的数
     * 组时。若index i的元素为负值，说明对应的positive number (i + 1)在数组中。因此只需寻找第一个不为负的index i，返回结果i + 1即可。若所有的index都为
     * 负值，说明1 - n都在原数组中，则需要返回n + 1。为了避免重复（因为可能存在duplicates）将同一index的值不断取相反数，而导致该位置为正。每次都先将该位置
     * 的元素取绝对值再变负。
     *
     * Time: O(n) 总共需要遍历3次数组，第一次将数组小于等于0和大于n的元素变为1，同时判断1是否在数组中，第二次遍历将每个元素对应的index值变负，第三遍寻找第
     *      一个大于0的index，每次遍历的时间都为O(n)
     * Space: O(1) 只需要常数空间即可
     */
    public int firstMissingPositive(int[] nums) {
        boolean found = false;
        int n = nums.length;
        //第一次遍历，将小于等于0和大于n的元素都变为1，同时判断1是否在数组中
        for(int i = 0; i < n; i++) {
            if(nums[i] == 1) {
                found = true;
            }
            if(nums[i] <= 0 || nums[i] > n) {
                nums[i] = 1;
            }
        }
        //如果没找到1，说明1就是结果
        if(!found) return 1;

        //第二次遍历，将每个元素对应的index位置取绝对值变负，表示该元素在数组中出现
        for(int i = 0; i < n; i++) {
            //得到当前位置的数值
            int num = Math.abs(nums[i]);
            //将数值对应的index位置num - 1的值变为负值
            nums[num - 1] = -Math.abs(nums[num - 1]);
        }

        //第三次遍历，寻找第一个大于0的index，i + 1即为结果
        for(int i = 0; i < n; i++) {
            if(nums[i] > 0) {
                return i + 1;
            }
        }
        //否则说明1 - n都在数组中，返回n + 1
        return n + 1;
    }

    @Test
    public void firstMissingPositiveTest() {
        /**
         * Example 1:
         * Input: [1,2,0]
         * Output: 3
         */
        int[] nums1 = new int[]{1, 2, 0};
        assertEquals(3, firstMissingPositive(nums1));
        /**
         * Example 2:
         * Input: [3,4,-1,1]
         * Output: 2
         */
        int[] nums2 = new int[]{3, 4, -1, 1};
        assertEquals(2, firstMissingPositive(nums2));
        /**
         * Example 3:
         * Input: [7,8,9,11,12]
         * Output: 1
         */
        int[] nums3 = new int[]{7, 8, 9, 11, 12};
        assertEquals(1, firstMissingPositive(nums3));
    }
}
