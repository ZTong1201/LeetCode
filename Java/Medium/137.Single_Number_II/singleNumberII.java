import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class singleNumberII {

    /**
     * Given a non-empty array of integers, every element appears three times except for one, which appears exactly once. Find that
     * single one
     *
     * Note:
     * Your algorithm should have a linear runtime complexity. Could you implement it without using extra memory?
     *
     * Approach 1: Hash Map
     * 因为其余元素都出现3次，因此当某数字第三次出现时，再将该数字移除，因此需要一个hash map来记录每个数字出现的次数。最终剩下的唯一一个数字即为答案
     *
     * Time: O(n)
     * Space: O(n)
     */
    public int singleNumberHash(int[] nums) {
        Map<Integer, Integer> map = new HashMap<>();
        for(int num : nums) {
            if(map.getOrDefault(num, 0) < 2) {
                map.put(num, map.getOrDefault(num, 0) + 1);
            } else {
                map.remove(num);
            }
        }
        return map.keySet().iterator().next();
    }

    /**
     * Approach 2: Bit Manipulation
     * 与136一样，当某数字出现奇数次时，从0开始对该数字不断做XOR操作，会得到它本身，偶数次时，结果为0。所以此题的本质是，如何区分出现3次与出现1次，此时需要
     * 两个bitmask，分别即为seen_once和seen_twice，即记录某数字是出现了一次还是两次，更新条件如下：
     * 1.若seen_twice为0，则改变seen_once -> seen_once ^ num = num，表示该数字第一次出现
     * 2.若seen_once不为0，seen_twice为0，说明该数字出现第二次，seen_once -> 0, seen_twice -> num
     * 3.若seen_twice不为0，说明该值已是第三次出现，此时需要改变seen_twice -> 0，那么最后记录下来只出现一次的数字即是最终结果
     *
     * Time: O(n)
     * Space: O(1)
     */
    public int singleNumberBit(int[] nums) {
        int seen_once = 0, seen_twice = 0;
        for(int num : nums) {
            //第一次出现，将seen_once变为num，seen_twice不变（仍为0）

            //第二次出现，将num从seen_once移除（变为0），seen_twice变为num

            //第三次出现，因为num在seen_twice出现，因此不会将seen_once加上num，同时将num从seen_twice移除
            seen_once = ~seen_twice & (seen_once ^ num);
            seen_twice = ~seen_once & (seen_twice ^ num);
        }
        return seen_once;
    }

    @Test
    public void singleNumberTest() {
        /**
         * Example 1:
         * Input: [2,2,3,2]
         * Output: 3
         */
        int[] nums1 = new int[]{2, 2, 3, 2};
        assertEquals(3, singleNumberHash(nums1));
        assertEquals(3, singleNumberBit(nums1));
        /**
         * Example 2:
         * Input: [0,1,0,1,0,1,99]
         * Output: 99
         */
        int[] nums2 = new int[]{0, 1, 0, 1, 0, 1, 99};
        assertEquals(99, singleNumberHash(nums2));
        assertEquals(99, singleNumberBit(nums2));
    }
}
