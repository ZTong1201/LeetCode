import org.junit.Test;
import static org.junit.Assert.*;

public class isomorphicStrings {

    /**
     * Given two strings s and t, determine if they are isomorphic.
     *
     * Two strings are isomorphic if the characters in s can be replaced to get t.
     *
     * All occurrences of a character must be replaced with another character while preserving the order of characters. No two characters
     * may map to the same character but a character may map to itself.
     *
     * Note:
     * You may assume both s and t have the same length.
     *
     * Approach: Hash Map
     * 因为输入保证了s和t的长度相同，因此可以同时扫描两个字符串，将每个位置的字符互相映射起来，
     * e.g. "foo"和"bar"，先将'f'和'b'映射起来，当再次遇到'f'时，要判断t中同位置是否为'b'，若不相同，则说明两字符串不是isomorphic的。
     *
     * Time: O(n) 只需要遍历一遍字符串
     * Space: O(1) 假设输入字符都在ASCII范围内，因此只需要一个size为256的数组就可以实现map的功能
     */
    public boolean isIsomorphic(String s, String t) {
        if(s == null || s.length() == 0) {
            return false;
        }
        int[] sMap = new int[256];
        int[] tMap = new int[256];
        for(int i = 0; i < s.length(); i++) {
            char sc = s.charAt(i);
            char tc = t.charAt(i);
            if(sMap[sc] != 0 && sMap[sc] != tc) {
                return false;
            }
            if(tMap[tc] != 0 && tMap[tc] != sc) {
                return false;
            }
            sMap[sc] = tc;
            tMap[tc] = sc;
        }
        return true;
    }


    @Test
    public void isIsomorphicTest() {
        /**
         * Example 1:
         * Input: s = "egg", t = "add"
         * Output: true
         */
        assertTrue(isIsomorphic("egg", "add"));
        /**
         * Example 2:
         * Input: s = "foo", t = "bar"
         * Output: false
         */
        assertFalse(isIsomorphic("foo", "bar"));
        /**
         * Example 3:
         * Input: s = "paper", t = "title"
         * Output: true
         */
        assertTrue(isIsomorphic("paper", "title"));
    }
}
