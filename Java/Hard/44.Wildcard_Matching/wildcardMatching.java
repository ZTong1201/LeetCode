import org.junit.Test;
import static org.junit.Assert.*;

public class wildcardMatching {

    /**
     * Given an input string (s) and a pattern (p), implement wildcard pattern matching with support for '?' and '*'.
     *
     * '?' Matches any single character.
     * '*' Matches any sequence of characters (including the empty sequence).
     * The matching should cover the entire input string (not partial).
     *
     * Note:
     *
     * s could be empty and contains only lowercase letters a-z.
     * p could be empty and contains only lowercase letters a-z, and characters like ? or *.
     *
     * Approach 1: Dynamic Programming
     * 此题与10题解法一致，需要维护一个size为(sLen + 1) * (pLen + 1)的二维数组，dp[i][j]表示的含义为s中的前i个字符和p中的前j个字符是否匹配。
     * 最终返回dp[sLen][pLen]即可。
     *
     * 初始化条件比较重要，首先dp[0][0] = true，表示若s和p为两个空字符，它们是match的。同时对于dp[0][i]，即判断p中的前i个字符是否与空字符匹配，唯一能匹配
     * 的条件是，p的前i个字符都是'*'，因此只需遍历字符串p，若p[i] = '*'，则只需要将其上一个位置的值传下来即可，即dp[0][i] = dp[0][i - 1]，因为只有当
     * 之前一位与空字符串匹配，且当前位置为'*'，才可能继续和空字符串匹配。
     *
     * 转移状态分为两种
     * 1.s[i] = p[j]或p[j] = '?'，即s的第i位和p的第j位是可以匹配的，这种情况下，若s的前i - 1个字符和p的前j - 1个字符匹配，那么dp[i][j]为true，因此在
     *   种情况下，只需要让dp[[i][j] = dp[i - 1][j - 1]即可
     * 2.另一种情况为p[j] = '*'，这种情况下可以有两种状态转移，
     *   1)将'*'视为空字符串，即p的前j - 1个字符已经和s的i个字符匹配了，加入'*'不影响结果，即要看dp[i][j - 1]，e.g. s = "ab", p = "ab*"
     *   2)将'*'视为非空字符串，那么就需要s的前i - 1个字符和p的j个字符相匹配，这样'*'可以替代一个当前字符，即dp[i - 1][j], e.g. s = "abc", p = "ab*"
     *   上述二者任意一条满足，则dp[i][j]为true
     *
     * Time: O(SP)
     * Space: O(SP)
     */
    public boolean isMatchDP(String s, String p) {
        int sLen = s.length(), pLen = p.length();
        boolean[][] match = new boolean[sLen + 1][pLen + 1];
        //初始化，两个空串是匹配的
        match[0][0] = true;
        //同时看当前的p和空串是否匹配
        for(int i = 1; i <= pLen; i++) {
            if(p.charAt(i - 1) == '*') {
                match[0][i] = match[0][i - 1];
            }
        }

        for(int i = 1; i <= sLen; i++) {
            for(int j = 1; j <= pLen; j++) {
                if((s.charAt(i - 1) == p.charAt(j - 1)) || (p.charAt(j - 1) == '?')) {
                    match[i][j] = match[i - 1][j - 1];
                } else if(p.charAt(j - 1) == '*') {
                    match[i][j] = match[i - 1][j] || match[i][j - 1];
                }
            }
        }
        return match[sLen][pLen];
    }

    /**
     * Approach 2: Backtrack
     * 对于字符串p来说，当遇到'*'时，dp方法其实是判断了该'*'能匹配的所有可能性，即从空串一直到字符最后。但事实上，很多时候'*'并不能匹配太多字符。因此可以
     * 考虑使用backtrack，即当遇到'*'，先考虑当前'*'匹配空串，然后继续向前匹配，当将该'*'视为空串无法匹配成功时，则需要backtrack回到最近的一个'*'的位置，
     * 考虑其匹配一个字符，以此类推，直到所有字符匹配成功，或不成功。
     *
     * 算法大致过程如下：
     * 维护两个pointer分别从s和p的头部开始，记录当前指向的字符，同时记录下'*'所在的位置和将'*'视为空串时，s的起始位置（之后backtrack要回到这个位置），最开始
     * 先初始化成-1。
     * 1.若当前两字符匹配或p中字符为'?'，则同时移动s和p的指针到下一位置
     * 2.若当前p中字符为'*'，考虑其匹配空串，记录下'*'的index和当前s所在的index
     * 3.若上述两条件都不满足，说明当前遇到了匹配不上的情况，查看是否有记录'*'的index（即判断是否为-1），若没有记录'*'的index，说明没有办法通过回溯创造匹配的
     *   机会，即p中到目前位置的字符都已用完，仍无法匹配，直接返回false
     * 4.若上述三个条件都不满足，说明当前字符无法匹配，但是有回溯的机会，将p的指针调到'*'的index的下一个，s的指针调到当时记录的index的下一个位置，然后再把
     *   当前s的开始位置记录下来（可能需要继续回溯）
     *
     * 当s的字符串已经全部扫描完毕，且与p的当前位置完全匹配。若p还有字符未被遍历，那么只能是后面都是'*'，判断p的后续字符是否全为'*'，若有位置不是'*'，直接返回
     * false，否则返回true
     *
     * Time: O(min(S, P)) in the best case，最好情况下不需要回溯，因此只要遍历完p或者s就能知道是否匹配，在平均情况下，对于'*'可能有不同的回溯程度，运行时间
     *       大概为O(SlogP)
     * Space: O(1)
     */
    public boolean isMatchBacktrack(String s, String p) {
        //两个指针记录当前两字符串中的位置
        int sIdx = 0, pIdx = 0;
        //另外两个指针记录上一个遇到的'*'的位置，以及将'*'视为空串时，s所在的起始位置，用于回溯
        int sTmpIdx = -1, starIdx = -1;
        while (sIdx < s.length()) {
            //若当前字符匹配，则同时移动s和p的指针
            if(pIdx < p.length() && ((s.charAt(sIdx) == p.charAt(pIdx)) || (p.charAt(pIdx) == '?'))) {
                sIdx++;
                pIdx++;
            } else if(pIdx < p.length() && p.charAt(pIdx) == '*') {
                //若当前位置为'*'，先将其视为空串，即p的指针移动一位，s指针不移动
                //在记录下当前'*'的index，和s的index，便于之后无法匹配时回溯
                starIdx = pIdx;
                sTmpIdx = sIdx;
                pIdx++;
            } else if(starIdx == -1) {
                //若前两个条件不满足，说明当前无法匹配，那么需要判断有无通过回溯使得字符串匹配的可能
                //若不可能，直接返回false
                return false;
            } else {
                //若上述三个条件都不满足，说明可以尝试回溯使得字符串匹配
                pIdx = starIdx + 1;
                sIdx = sTmpIdx + 1;
                sTmpIdx = sIdx;
            }
        }

        //若遍历完s，而且p截止到目前为止和s是匹配的，若想整个p和s匹配，只能p中后续字符全是'*'
        for(int i = pIdx; i < p.length(); i++) {
            if(p.charAt(i) != '*') return false;
        }
        return true;
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
        assertFalse(isMatchDP("aa", "a"));
        assertFalse(isMatchBacktrack("aa", "a"));
        /**
         * Example 2:
         * Input:
         * s = "aa"
         * p = "*"
         * Output: true
         * Explanation: '*' matches any sequence.
         */
        assertTrue(isMatchDP("aa", "*"));
        assertTrue(isMatchBacktrack("aa", "*"));
        /**
         * Example 3:
         * Input:
         * s = "cb"
         * p = "?a"
         * Output: false
         * Explanation: '?' matches 'c', but the second letter is 'a', which does not match 'b'.
         */
        assertFalse(isMatchDP("cb", "?a"));
        assertFalse(isMatchBacktrack("cb", "?a"));
        /**
         * Example 4:
         * Input:
         * s = "adceb"
         * p = "*a*b"
         * Output: true
         * Explanation: The first '*' matches the empty sequence, while the second '*' matches the substring "dce".
         */
        assertTrue(isMatchDP("adceb", "*a*b"));
        assertTrue(isMatchBacktrack("adceb", "*a*b"));
        /**
         * Example 5:
         * Input:
         * s = "acdcb"
         * p = "a*c?b"
         * Output: false
         */
        assertFalse(isMatchDP("acdcb", "a*c?b"));
        assertFalse(isMatchBacktrack("acdcb", "a*c?b"));
    }
}
