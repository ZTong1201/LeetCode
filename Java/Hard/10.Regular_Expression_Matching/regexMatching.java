import org.junit.Test;
import static org.junit.Assert.*;

public class regexMatching {

    /**
     * Given an input string (s) and a pattern (p), implement regular expression matching with support for '.' and '*'.
     *
     * '.' Matches any single character.
     * '*' Matches zero or more of the preceding element.
     * The matching should cover the entire input string (not partial).
     *
     * Note:
     * s could be empty and contains only lowercase letters a-z.
     * p could be empty and contains only lowercase letters a-z, and characters like . or *.
     *
     * Approach 1: Recursion
     * 首先考虑base case，若p为空字符串，只有当s为空时返回true
     * 当p不为空字符串的时候，首先考虑两字符串第一位是否match，只有当s不为空，同时s和p的一位相同，或p的一位是'.'时，第一位才match
     * 当有了第一位match的结果，后面可以recursively判断剩余部分是否match。有如下情况
     * （假定p不会存在'*'这种无效字符串）
     * 1.p的长度至少为2，且第2位为'*'，则要么舍弃前两位pattern继续判断是否和s匹配，或者在第一位match的情况，判断s的后续字符和当前pattern是否匹配
     * 2.其他情况，则需要保证在第一位match的情况下，s和p的后续字符也能匹配
     */
    public boolean isMatchRecursive(String s, String p) {
        if(p.isEmpty()) return s.isEmpty();
        boolean firstMatch = !s.isEmpty() && (s.charAt(0) == p.charAt(0) || p.charAt(0) == '.');

        if(p.length() >= 2 && p.charAt(1) == '*') {
            return isMatchRecursive(s, p.substring(2)) || (firstMatch && isMatchRecursive(s.substring(1), p));
        } else {
            return firstMatch && isMatchRecursive(s.substring(1), p.substring(1));
        }
    }

    /**
     * Approach 2: Dynamic Programming
     * String Matching相关系列都可以用DP来解，主要弄清楚如下几个问题：
     * 1.state：需要一个二维boolean数组dp[sLen + 1][pLen + 1]，注意size一定是字符串长度加1，dp[i][j]代表的含义是，从s中取出i个字符，和从p中取出j个字符，
     *   两字符是否匹配
     * 2.初始化：首先，dp[0][0]代表两个空字符，因此dp[0][0]为true。同时因为'*'可以代表0-n个在它之前的字符，所以p中字符若为'*'则要看两位之前为true还是false
     *   例如'a*', 'a*b*'都可以表示一个空字符串，所以index为2处要看index为0的状态，index为4要看index为2的状态。以此类推
     * 3.转移状态：主要分为三种情况
     *   1.第一种情况，两字符串同位置字符相同或p中同位置为'.'，只要在前一位置时，两字符串匹配，那么在当前位置也是匹配的,所以dp[i][j] = dp[i - 1][j - 1]
     *   2.第二种情况，p字符串当前位置为'*'，且p字符串前一位与s字符串当前位置字符不同，说明需要省略掉p字符串中'*'之前的字符以和s匹配。因此转移状态与初始化类似
     *     若两位之前两字符串匹配，则当前位置也匹配，例如"abc"和"ac*" （所以dp[i][j] = dp[i][j - 2]）
     *   3.第三种情况，p字符串当前位置为'*'， 且p字符串前一位与s字符串当前位置字符相同，说明可以'*'之前的位置也可以被考虑在内，那么只需要看s字符串往前移动一位
     *     时，两字符串是否匹配（例如"abcd"和"abcd*"），或者和第二种情况一样，若p字符串向前移动两位，两字符串仍匹配（例如"abcd"和"abcde*"，
     *     两种case都可以使字符串匹配
     * 4.返回结果，只需返回dp[sLen][pLen]的结果
     *
     * 以上算法假定p不会是'*'这种无效pattern，因为若p中第一位为'*'，往前看两位会溢出边界
     *
     * Time: O(mn) 创建了一个二维数组，每个位置只会被遍历一次判断是否匹配
     * Space: O(mn)
     */
    public boolean isMatchDP(String s, String p) {
        int sLen = s.length(), pLen = p.length();
        boolean[][] match = new boolean[sLen + 1][pLen + 1];
        match[0][0] = true;

        //初始化
        for(int i = 1; i <= pLen; i++) {
            //因为数组index表示用了字符串中几个数字，因此未得到某个index的字符，需要将index减1
            if(p.charAt(i - 1) == '*') {
                match[0][i] = match[0][i - 2];
            }
        }

        //转移状态
        for(int i = 1; i <= sLen; i++) {
            for (int j = 1; j <= pLen; j++) {
                if (s.charAt(i - 1) == p.charAt(j - 1) || p.charAt(j - 1) == '.') {
                    match[i][j] = match[i - 1][j - 1];
                } else if (p.charAt(j - 1) == '*') {
                    if (s.charAt(i - 1) == p.charAt(j - 2) || p.charAt(j - 2) == '.') {
                        match[i][j] = match[i - 1][j] || match[i][j - 2];
                    } else {
                        match[i][j] = match[i][j - 2];
                    }
                }
            }
        }
        return match[sLen][pLen];
    }


    @Test
    public void isMatchTest() {
        /**
         * Example 1:
         * Input:
         * s = "aa"
         * p = "a"
         * Output: false
         * Explanation: "a" does not match the entire string "aa".
         */
        assertFalse(isMatchRecursive("aa", "a"));
        assertFalse(isMatchDP("aa", "a"));
        /**
         * Example 2:
         * Input:
         * s = "aa"
         * p = "a*"
         * Output: true
         * Explanation: '*' means zero or more of the preceding element, 'a'. Therefore, by repeating 'a' once, it becomes "aa".
         */
        assertTrue(isMatchRecursive("aa", "a*"));
        assertTrue(isMatchDP("aa", "a*"));
        /**
         * Example 3:
         * Input:
         * s = "ab"
         * p = ".*"
         * Output: true
         * Explanation: ".*" means "zero or more (*) of any character (.)".
         */
        assertTrue(isMatchRecursive("ab", ".*"));
        assertTrue(isMatchDP("ab", ".*"));
        /**
         * Example 4:
         * Input:
         * s = "aab"
         * p = "c*a*b"
         * Output: true
         * Explanation: c can be repeated 0 times, a can be repeated 1 time. Therefore, it matches "aab".
         */
        assertTrue(isMatchRecursive("aab", "c*a*b"));
        assertTrue(isMatchDP("aab", "c*a*b"));
        /**
         * Example 5:
         * Input:
         * s = "mississippi"
         * p = "mis*is*p*."
         * Output: false
         */
        assertFalse(isMatchRecursive("mississippi", "mis*is*p*"));
        assertFalse(isMatchDP("mississippi", "mis*is*p*"));
    }
}
