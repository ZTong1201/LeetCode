import org.junit.Test;
import static org.junit.Assert.*;

public class oneEditDistance {

    /**
     * Given two strings s and t, determine if they are both one edit distance apart.
     *
     * Note:
     *
     * There are 3 possiblities to satisify one edit distance apart:
     *
     * Insert a character into s to get t
     * Delete a character from s to get t
     * Replace a character of s to get t
     *
     * Approach: One-pass Algorithm
     * 首先排除长度差大于1的两个字符串，直接返回false
     * 对于长度差为1或相等长度的两字符串，不妨假设s的长度 <= t的长度，若s的长度 > t的长度，可以直接call isOneEditDistance(t, s)即可
     *
     * 对于不同字符串的同一index i，有两种情况，该位置两字符相等或不等
     * 1.若直到s的最后一位，均为发现不同字符，则有两种情况：
     *   a.两字符串长度相等，为相同字符串，return false. e.g. s="abcd", t="abcd"
     *   b.两字符串长度不等，且长度差1，return true. e.g. s="abcd", t="abcde"
     * 2.若某一位字符不等，则该不同字符只能是两字符串的唯一差别。此时还有两种可能
     *   a.若两字符串相等，要判断两剩余子串是否相等, return s.substring(i + 1) == t.substring(i + 1) e.g. s="abcde", t="abfde"
     *   b.若两字符串不相等，则长字符串该字符为唯一不同字符，需判断s和t的后续相同长度子串是否相同，return s.substring(i) == t.substring(i + 1)
     *     e.g. s="abcd", t="abxcd"
     *
     * Time: O(n), 最坏情况需要遍历整个长度最小的字符串
     * Space: O(n), substring函数会构建一个新的字符串，若第一位两字符串不等，则需要O(n)的空间来存储剩余子串
     */
    public boolean isOneEditDistance(String s, String t) {
        int sLen = s.length();
        int tLen = t.length();
        //默认s的长度小于等于t的长度，若大于则只需要调换s和t
        if(sLen > tLen) {
            return isOneEditDistance(t, s);
        }

        //如果两字符串长度差大于1，直接return false
        if(tLen - sLen > 1) return false;

        for(int i = 0; i < sLen; i++) {
            if(s.charAt(i) != t.charAt(i)) {
                if(sLen == tLen) {
                    return s.substring(i + 1).equals(t.substring(i + 1));
                } else {
                    return s.substring(i).equals(t.substring(i + 1));
                }
            }
        }

        //若短字符串遍历结束后，尚未发现不同字符，则只有两字符串长度差为1的情况return true
        //不然说明两字符串完全相同，return false
        return (sLen + 1 == tLen);
    }


    @Test
    public void isOneEditDistanceTest() {
        /**
         * Example 1:
         * Input: s = "ab", t = "acb"
         * Output: true
         * Explanation: We can insert 'c' into s to get t.
         */
        assertTrue(isOneEditDistance("ab", "acb"));
        /**
         * Example 2:
         * Input: s = "cab", t = "ad"
         * Output: false
         * Explanation: We cannot get t from s by only one step.
         */
        assertFalse(isOneEditDistance("cab", "ad"));
        /**
         * Example 3:
         * Input: s = "1203", t = "1213"
         * Output: true
         * Explanation: We can replace '0' with '1' to get t.
         */
        assertTrue(isOneEditDistance("1203", "1213"));
        /**
         * Example 4:
         * Input: s = "", t = ""
         * Output: false
         * Explanation: Any two same string should return false;
         */
        assertFalse(isOneEditDistance("", ""));
    }
}
