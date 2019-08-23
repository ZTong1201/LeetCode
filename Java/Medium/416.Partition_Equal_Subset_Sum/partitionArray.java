import org.junit.Test;
import static org.junit.Assert.*;

public class partitionArray {

    /**
     * Given a non-empty array containing only positive integers, find if the array can be partitioned into two subsets such that
     * the sum of elements in both subsets is equal.
     *
     * Note:
     *
     * Each of the array element will not exceed 100.
     * The array size will not exceed 200.
     *
     * Approach 1: Dynamic Programming (2D)
     * 本质上此题就是找到一个原数组的subset使得该subset的和为数组总和的1/2。因此可以用subset sum的方法来解。首先求出数组的总和，若该总和为奇数，则不可能存在
     * 两个subset使得subset的和为总和的一半，直接return false。若为偶数，则可以设定target为总和的一半。
     * 之后可以用dp求解。二维boolean数组dp[i][j]表示，能否用前i个元素使得和为j。因此二维数组的行数为nums.length + 1，列数为target + 1。首先将数组
     * 的第一列(即dp[0->n+1][0])都初始化为true，表示不把数组中任何元素放入subset中，可以使得该subset的和为0。然后针对于每一行的每个位置，该位置为true只有两
     * 种可能：
     * 1.若上一行该位置为true（dp[i - 1][j] = true），那么意味着不放入当前元素可以得到和为j
     * 2.若上一行和为j - nums[i]的位置为true，那么意味着放入当前元素可以使得和为j
     * 对于每一行，需要遍历所有的和的可能（0 -> target)，更新每个位置的值，若任意时刻dp[i][target] = true，则说明可以找到这样一个subset，return true，
     * 反之return false
     *
     * Time: O(n * target) 需要遍历每一行更新0 -> target位置的boolean值
     * Space: O(n * target) 需要一个二维数组记录到目前为止所能得到的和
     */
    public boolean canPartitionDP_2D(int[] nums) {
        int sum = 0;
        for(int num : nums) {
            sum += num;
        }
        if(sum % 2 != 0) return false;
        int target = sum / 2;
        boolean[][] dp = new boolean[nums.length + 1][target + 1];
        //先将第一列都初始化为true
        for(int i = 0; i <= nums.length; i++) {
            dp[i][0] = true;
        }
        //然后遍历之后的每一行，即数组中的每个元素
        for(int i = 0; i < nums.length; i++) {
            //再遍历所有可能得到的和
            for(int j = 1; j <= target; j++) {
                //更新当前的和的值，要看上一行当前和的值（表示不把当前元素放入subset中能得到和j）
                //以及上一行j - 当前元素的值（表示把当前元素放入subset中能得到和j）
                //注意j - nums[i]可能小于0，当小于0时，只考虑dp[i][j]的值
                dp[i + 1][j] = dp[i][j] || (j >= nums[i] ? dp[i][j - nums[i]] : false);
                //如果任意时刻能得到target，则可以直接返回true
                if(dp[i + 1][target]) return true;
            }
        }
        //反之，返回false
        return false;
    }

    /**
     * Approach 2: Dynamic Programming (1D)
     * 由上述讨论可以看出，某一行的值只依赖于上一行的状态，同时和较大的值不会再对和较小的值产生影响。因此可以对数组进行降维。只有一维数组，滚动的更新每一行的值，
     * 同时因为较大的和不会对较小的和产生影响，因此在更新每一行时，需要从大往小更新
     *
     * Time: O(n * target) 同样还是要遍历数组中的所有元素，然后更新每个可能取到的和的值
     * Space: O(target) 因为是滚动的更新数组，只需要一维数组即可
     */
    public boolean canPartitionDP_1D(int[] nums) {
        int sum = 0;
        for(int num : nums) {
            sum += num;
        }
        if(sum % 2 != 0) return false;
        int target = sum / 2;
        boolean[] dp = new boolean[target + 1];
        dp[0] = true;
        for(int i = 0; i < nums.length; i++) {
            //因为较大的和的更新不会对较小的和的更新产生影响，所以要倒序更新
            //注意index最小为0，不能溢出左边界
            for(int j = target; j >= nums[i]; j--) {
                dp[j] = dp[j] || dp[j - nums[i]];
                if(dp[target]) return true;
            }
        }
        return false;
    }

    @Test
    public void canPartitionTest() {
        /**
         * Example 1:
         * Input: [1, 5, 11, 5]
         *
         * Output: true
         *
         * Explanation: The array can be partitioned as [1, 5, 5] and [11].
         */
        int[] nums1 = new int[]{1, 5, 11, 5};
        assertTrue(canPartitionDP_2D(nums1));
        assertTrue(canPartitionDP_1D(nums1));
        /**
         * Example 2:
         * Input: [1, 2, 3, 5]
         *
         * Output: false
         *
         * Explanation: The array cannot be partitioned into equal sum subsets.
         */
        int[] nums2 = new int[]{1, 2, 3, 5};
        assertFalse(canPartitionDP_2D(nums2));
        assertFalse(canPartitionDP_1D(nums2));
    }
}
