import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class singleNumber {

    /**
     * Given a non-empty array of integers, every element appears twice except for one. Find that single one.
     *
     * Note:
     *
     * Your algorithm should have a linear runtime complexity. Could you implement it without using extra memory?
     *
     * Approach 1: Hash Set
     * 利用hash set，将没见过的number放入set，若再次遇到某数字，说明该数字是第二次出现，将其移除。最终剩下的唯一一个元素即是答案
     *
     * Time: O(n)
     * Space: O(n)
     */
    public int singleNumberHash(int[] nums) {
        Set<Integer> res = new HashSet<>();
        for(int num : nums) {
            if(!res.contains(num)) {
                res.add(num);
            } else {
                res.remove(num);
            }
        }
        return res.iterator().next();
    }

    /**
     * Approach 2: Bit Manipulation
     * 如果某数字a和0做XOR操作，会得到该数字本身。若某数字a两两之间进行XOR操作，会得到0。因此可以先初始化一个0，然后遍历整个数组对所有数字做XOR操作。因为
     * 除了最终答案剩余数字都出现两遍，同时XOR操作满足交换律。因此最终得到的结果就是仅出现一次的数字
     *
     * Time: O(n)
     * Space: O(1)
     */
    public int singleNumberBit(int[] nums) {
        int a = 0;
        for(int num : nums) {
            a ^= num;
        }
        return a;
    }

    @Test
    public void singleNumberTest() {
        /**
         * Example 1:
         * Input: [2,2,1]
         * Output: 1
         */
        int[] nums1 = new int[]{2, 2, 1};
        assertEquals(1, singleNumberHash(nums1));
        assertEquals(1, singleNumberBit(nums1));
        /**
         * Example 2:
         * Input: [4,1,2,1,2]
         * Output: 4
         */
        int[] nums2 = new int[]{4, 1, 2, 1, 2};
        assertEquals(4, singleNumberHash(nums2));
        assertEquals(4, singleNumberBit(nums2));
    }
}
