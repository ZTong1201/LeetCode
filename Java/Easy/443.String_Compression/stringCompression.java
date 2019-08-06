import org.junit.Test;
import static org.junit.Assert.*;

public class stringCompression {

    /**
     * Given an array of characters, compress it in-place.
     *
     * The length after compression must always be smaller than or equal to the original array.
     *
     * Every element of the array should be a character (not int) of length 1.
     *
     * After you are done modifying the input array in-place, return the new length of the array.
     * Note:
     *
     * All characters have an ASCII value in [35, 126].
     * 1 <= len(chars) <= 1000.
     *
     *
     * Follow up:
     * Could you solve it using only O(1) extra space?
     *
     * Approach: Two pointers
     * 只需要两个快慢指针来记录当前是否为相同字符，同时记录下一个字符的插入位置即可。只要两指针所指元素相同，则持续移动快指针直到两者不同，快慢指针的间隔就是
     * 当前元素的出现次数。将当前元素插入正确位置，判断当前元素出现的次数（出现1次无需compress），当出现多于一次时，需将出现次数变为字符。因为输入array的
     * 长度最长为1000，最坏情况下，记录出现次数的字符串需要4位，因此仍未constant space
     *
     * Time: O(N) one-pass
     * Space: O(1)
     */
    public int compress(char[] chars) {
        int length = chars.length;
        //若输入数组长度小于1，则无需压缩
        if(length <= 1) return length;
        int curr = 0, slow = 0, fast = 0;
        while(fast < length) {
            //只要当前两指针所指元素相等，就移动快指针直到不等
            while(chars[slow] == chars[fast]) {
                fast++;
                //注意快指针有可能越过边界
                if(fast == length) {
                    break;
                }
            }
            //记录当前字符出现次数
            int count = fast - slow;
            //将当前字符插入正确位置，然后移动curr
            chars[curr++] = chars[slow];
            //出现次数为1无需压缩
            if(count != 1) {
                String s = String.valueOf(count);
                //因输入数组长度最大为1000，因此s的长度最大为4
                for(int i = 0; i < s.length(); i++) {
                    chars[curr++] = s.charAt(i);
                }
            }
            //将快慢指针汇合，重新开始下一次循环
            slow = fast;
        }
        return curr;
    }

    @Test
    public void compressTest() {
        /**
         * Input:
         * ["a","a","b","b","c","c","c"]
         *
         * Output:
         * Return 6, and the first 6 characters of the input array should be: ["a","2","b","2","c","3"]
         *
         * Explanation:
         * "aa" is replaced by "a2". "bb" is replaced by "b2". "ccc" is replaced by "c3".
         */
        char[] chars1 = new char[]{'a', 'a', 'b', 'b', 'c', 'c', 'c'};
        assertEquals(6, compress(chars1));
        char[] expected1 = new char[]{'a', '2', 'b', '2', 'c', '3'};
        for(int i = 0; i < expected1.length; i++) {
            assertEquals(expected1[i], chars1[i]);
        }
        /**
         * Input:
         * ["a"]
         *
         * Output:
         * Return 1, and the first 1 characters of the input array should be: ["a"]
         *
         * Explanation:
         * Nothing is replaced.
         */
        char[] chars2 = new char[]{'a'};
        assertEquals(1, compress(chars2));
        char[] expected2 = new char[]{'a'};
        for(int i = 0; i < expected2.length; i++) {
            assertEquals(expected2[i], chars2[i]);
        }
        /**
         * Input:
         * ["a","b","b","b","b","b","b","b","b","b","b","b","b"]
         *
         * Output:
         * Return 4, and the first 4 characters of the input array should be: ["a","b","1","2"].
         *
         * Explanation:
         * Since the character "a" does not repeat, it is not compressed. "bbbbbbbbbbbb" is replaced by "b12".
         * Notice each digit has it's own entry in the array.
         */
        char[] chars3 = new char[]{'a', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b'};
        assertEquals(4, compress(chars3));
        char[] expected3 = new char[]{'a', 'b', '1', '2'};
        for(int i = 0; i < expected3.length; i++) {
            assertEquals(expected3[i], chars3[i]);
        }

    }
}
