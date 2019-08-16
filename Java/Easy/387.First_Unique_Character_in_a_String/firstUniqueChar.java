import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class firstUniqueChar {

    /**
     * Given a string, find the first non-repeating character in it and return it's index. If it doesn't exist, return -1.
     * Note: You may assume the string contain only lowercase letters.
     *
     * Approach: Hash Table
     * 因为输入只有小写字母，可以用一个size为26的数组来当做map，记录每个字母出现的次数。然后再从头扫一遍字符串，若任意时刻某字符只出现一次，返回其index。
     * 若扫到字符串末尾仍未找到，返回-1
     *
     * Time: O(N) 两遍单向扫描
     * Space: O(1) 无论输入如何，都只需要size为26的数组
     */
    public int firstUniqChar(String s) {
        int[] map = new int[26];
        for(int i = 0; i < s.length(); i++) {
            map[s.charAt(i) - 'a']++;
        }
        for(int i = 0; i < s.length(); i++) {
            if(map[s.charAt(i) - 'a'] == 1) {
                return i;
            }
        }
        return -1;
    }


    @Test
    public void firstUniqCharTest() {
        /**
         * Example 1:
         * s = "leetcode"
         * return 0
         */
        assertEquals(0, firstUniqChar("leetcode"));
        /**
         * Example 2:
         * s = "loveleetcode",
         * return 2
         */
        assertEquals(2, firstUniqChar("loveleetcode"));
    }
}
