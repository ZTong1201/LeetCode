import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class splitArray {

    /**
     * Given an array which consists of non-negative integers and an integer m, you can split the array into m non-empty continuous
     * subarrays. Write an algorithm to minimize the largest sum among these m subarrays.
     * <p>
     * Note:
     * If n is the length of array, assume the following constraints are satisfied:
     * <p>
     * Constraints:
     * <p>
     * 1 <= nums.length <= 1000
     * 0 <= nums[i] <= 10^6
     * 1 <= m <= min(50, nums.length)
     * <p>
     * Approach 1: Dynamic Programming
     * 因为若把一个array nums[0...i]分成j部分，不会影响如何分配array中剩余的部分，所以可以用动态规划求解。首先确定的是，需要一个二维数组，行数为m + 1，
     * 即每一行表示可以把array中的数组分成i部分，列数为n，即每一列表示的是把前[0, j]的元素分成i部分时，最小的largest sum。
     * 接下来明确初始化条件，当i为1时，将数组分成1组，那么dp[1][j]就应该是数组中0到j的和。剩下的位置，因为要求最小值，所以要先初始化成MAX_VALUE
     * 当2 <= i <= m，要把数组分成i部分，状态转移的方法为，在数组nums[0, j]中找到一个分割点k，使得将数组分为nums[0, k]分成i - 1部分和nums[k + 1, j]，
     * 使得largest sum最小，因为dp[i][j]记录的就是将前j个元素分成i部分的最小largest sum，因此第一部分的值即为dp[i - 1][k]，而第二部分其实就是将数组
     * 后半段看成一部分，这部分的最小largest sum就是这段数组的和。因此对于给定的i，和数组中某一位置j，需要枚举所有从0到j - 1的k，使得max（dp[i - 1][k],
     * sum(nums[k + 1, j]))最小即可
     * 最后返回dp[m][n - 1]，即将数组中所有元素分成m分的最小largest sum
     * <p>
     * 因为整个dp过程需要频繁用到某一段的和，因此可以先计算prefix sum，使得求任意一段的和为O(1)时间
     * <p>
     * Time: O(mn^2)，需要遍历整个二维数组，同时在每一行，需要枚举所有的k，k的数量是bounded by O(n)，因此总的时间为O(mn^2)
     * Space: O(mn)，需要一个二维数组记录状态
     */
    public int splitArrayDP(int[] nums, int m) {
        int n = nums.length;
        int[] prefixSum = new int[n];
        prefixSum[0] = nums[0];
        //先计算数组的prefix sum，使得计算任意一段的sum为O(1)时间
        for (int i = 1; i < n; i++) {
            prefixSum[i] = prefixSum[i - 1] + nums[i];
        }
        int[][] dp = new int[m + 1][n];
        //先初始化，若将前i个数组元素分为1部分，最小的largest sum就是这段的和
        dp[1] = prefixSum;

        //为了求最小值，将其他未遍历部分先设成MAX_VALUE
        for (int i = 2; i <= m; i++) {
            Arrays.fill(dp[i], Integer.MAX_VALUE);
        }

        //然后遍历整个数组，先固定分割成几部分
        for (int i = 2; i <= m; i++) {
            //再遍历整个数组，值得注意的是，若将前j个元素分为i部分，至少前面需要i - 1个元素
            for (int j = i - 1; j < n; j++) {
                //然后枚举所有0到j - 1的k
                for (int k = 0; k < j; k++) {
                    //更新将前j个元素分成i部分的最小largest sum
                    //largest sum为将前k个元素分成i - 1部分的最小largest sum和剩余k + 1到j各元素的和的最大值
                    dp[i][j] = Math.min(dp[i][j], Math.max(dp[i - 1][k], prefixSum[j] - prefixSum[k]));
                }
            }
        }
        //最后返回将所有数组元素分成m部分的最小largest sum
        return dp[m][n - 1];
    }

    /**
     * Approach 2: Binary Search + Greedy
     * 若将整个array分成1组，那么最小的largest sum就是整个数组的和。若将整个array分成和数组元素一样多的组，那么每组中只有一个元素，所以largest sum就是数组
     * 元素中的最大值。其余的情况下，最终结果一定是介于两者之间的某个值，因此可以考虑用binary search来寻找这个值。
     * <p>
     * 如何确定该怎样移动边界？
     * 可以考虑用greedy的方式split整个数组，即，对于当前candidate值（假设为C），那么分好的数组中的元素和一定 <= C，因此可以遍历整个数组，若当前的累加和大于C，
     * 就重新增加一组，确保每组的和都小于等于C，若最终返回的组数大于m，说明当前的和C太小，需要扩大C使得每组能包括更多的元素，总组数下降。反之则和C太大，需要
     * 减小C。可以通过这种方式移动左右边界，直到左右边界交叉，该元素即为最小的largest sum
     * <p>
     * Time: O(n * log(sum(nums)) 因为总的考虑范围是以数组和为边界的，通过binary search，总共需要查找的值的个数为O(log(sum(nums))，同时对于每一个
     * candidate值，都需要再遍历整个数组，计算以当前值为上界，可将整个数组分成几组。
     * Space: O(1) 只需要常数空间
     */
    public int splitArrayBinarySearch(int[] nums, int m) {
        int low = 0, high = 0;
        //先遍历一遍数组，计算最大值和数组元素和，并记为左右边界
        for (int num : nums) {
            low = Math.max(low, num);
            high += num;
        }
        //然后进行binary search
        while (low < high) {
            int mid = low + (high - low) / 2;
            //以当前中点值作为candidate，判断可将数组分成几组
            //若总组数大于m，则需要查找更大的值
            if (minGroups(mid, nums) > m) {
                low = mid + 1;
            } else {
                //否则就查找更小的值
                high = mid;
            }
        }
        //最后当前左右边界交叉，该值一定为最小的largest sum
        return low;
    }

    private int minGroups(long limit, int[] nums) {
        long sum = 0;
        //整个数组最小的组数为1
        int groups = 1;
        for (int num : nums) {
            //若加入当前值超过了candidate值，则需要将之前的元素看成一组，以当前元素为起始点，开始一个新组
            if (sum + num > limit) {
                sum = num;
                groups++;
            } else {
                //否则就继续将当前元素加进现在的组
                sum += num;
            }
        }
        //返回总的组数
        return groups;
    }


    @Test
    public void splitArrayTest() {
        /**
         * Example 1:
         * Input:
         * nums = [7,2,5,10,8]
         * m = 2
         *
         * Output:
         * 18
         *
         * Explanation:
         * There are four ways to split nums into two subarrays.
         * The best way is to split it into [7,2,5] and [10,8],
         * where the largest sum among the two subarrays is only 18.
         */
        assertEquals(18, splitArrayDP(new int[]{7, 2, 5, 10, 8}, 2));
        assertEquals(18, splitArrayBinarySearch(new int[]{7, 2, 5, 10, 8}, 2));
        /**
         * Example 2:
         * Input: nums = [1,2,3,4,5], m = 2
         * Output: 9
         */
        assertEquals(9, splitArrayDP(new int[]{1, 2, 3, 4, 5}, 2));
        assertEquals(9, splitArrayBinarySearch(new int[]{1, 2, 3, 4, 5}, 2));
        /**
         * Example 3:
         * Input: nums = [1,4,4], m = 3
         * Output: 4
         */
        assertEquals(4, splitArrayDP(new int[]{1, 4, 4}, 3));
        assertEquals(4, splitArrayBinarySearch(new int[]{1, 4, 4}, 3));
    }
}
