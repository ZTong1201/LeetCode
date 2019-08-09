import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class missingNumber {

    /**
     * Given an array containing n distinct numbers taken from 0, 1, 2, ..., n, find the one that is missing from the array.
     *
     * Note:
     * Your algorithm should run in linear runtime complexity. Could you implement it using only constant extra space complexity?
     *
     * Approach 1: Hash Set
     * 遍历整个数组，把所有的数字存在set里，再从0循环到nums.length + 1，不在set里的元素即为missing
     *
     * Time: O(N) 相当于从0到N循环了两次
     * Space: O(N) 需要一个hash set来记录数组中的元素
     */
    public int missingNumberHashSet(int[] nums) {
        Set<Integer> seen = new HashSet<>();
        for(int num : nums) {
            seen.add(num);
        }
        for(int i = 0; i <= nums.length + 1; i++) {
            if(!seen.contains(i)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Approach 2: Bit Manipulation
     * 根据题意，missing的数字一定在0 ~ n - 1之间，所以我们可以将数字初始化为n然后和index和value做个XOR运算
     * XOR运算的结果是，当两位置相同（0，0或1，1）返回0，不同时返回1.
     * e.g.
     * index: 0   1    2     3
     * value: 0   1    3     4
     * missing = 4 ^ (0 ^ 0) ^ (1 ^ 1) ^ (2 ^ 3) ^ (3 ^ 4) = (4 ^ 4) ^ (0 ^ 0) ^ (1 ^ 3) ^ (3 ^ 3) ^ 2
     *         = 0 ^ 0 ^ 0 ^ 0 ^ 2
     *         = 2
     * 输入数组不一定是sorted的，但我们可以移动XOR运算，总能使某元素和它该出现的index做XOR运算并得到0，无法得到0的数字即为缺失数字
     *
     * Time: O(N)  假定XOR运算需要constant time，那么整体运算时间即为O(N)
     * Space: O(1)
     */
    public int missingNumberXOR(int[] nums) {
        int missing = nums.length;
        for(int i = 0; i < nums.length; i++) {
            missing ^= i ^ nums[i];
        }
        return missing;
    }


    /**
     * Approach 3: Guass's Formula
     * 根据高斯公式，可以轻易的算出0到n的和 = (n + 1)n/2，同时可以遍历整个数组，计算数组实际的和。missing number即为两个和之间的差值
     *
     * Time: O(N)
     * Space: O(1)
     */
    public int missingNumberGauss(int[] nums) {
        int sum = 0;
        for(int num : nums) {
            sum += num;
        }
        return nums.length * (nums.length + 1) / 2 - sum;
    }

    @Test
    public void missingNumberTest() {
        /**
         * Example 1:
         * Input: [3,0,1]
         * Output: 2
         */
        int[] nums1 = new int[]{3, 0, 1};
        assertEquals(2, missingNumberHashSet(nums1));
        assertEquals(2, missingNumberXOR(nums1));
        assertEquals(2, missingNumberGauss(nums1));
        /**
         * Example 2:
         * Input: [9,6,4,2,3,5,7,0,1]
         * Output: 8
         */
        int[] nums2 = new int[]{9, 6, 4, 2, 3, 5, 7, 0, 1};
        assertEquals(8, missingNumberHashSet(nums2));
        assertEquals(8, missingNumberXOR(nums2));
        assertEquals(8, missingNumberGauss(nums2));
    }
}
