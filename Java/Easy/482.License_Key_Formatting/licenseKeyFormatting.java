import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class licenseKeyFormatting {

    /**
     * You are given a license key represented as a string S which consists only alphanumeric character and dashes. The string is separated
     * into N+1 groups by N dashes.
     *
     * Given a number K, we would want to reformat the strings such that each group contains exactly K characters, except for the first
     * group which could be shorter than K, but still must contain at least one character. Furthermore, there must be a dash inserted
     * between two groups and all lowercase letters should be converted to uppercase.
     *
     * Given a non-empty string S and a number K, format the string according to the rules described above.
     *
     * Note:
     * The length of string S will not exceed 12,000, and K is a positive integer.
     * String S consists only of alphanumerical characters (a-z and/or A-Z and/or 0-9) and dashes(-).
     * String S is non-empty.
     *
     * Approach 1: Backward
     * 本质上就是将字符串中的K个字符放在一个group中，若字符串的总长度不能被K整除，则只有第一个子串的长度可以小于K，因此可以考虑从后往前group，即从后往前遍历整个
     * 字符串，若遇到'-'就跳过，遇到字母或数字（注意字母都要变成大写）就将其放入stringbuilder中，同时记录当前group中字符的总个数，当总个数等于K时，需要在
     * 结果中插入一个'-'，并将计数结果归零。最后将整个结果反转然后变成字符串输出即可
     *
     * Time: O(n) 从后往前遍历整个字符串
     * Space: O(n) 需要将输入字符串中所有的非'-'字符和一定数量的新'-'放入stringbuilder，最坏情况下，输入字符已经符合规则，输出字符串长度与输入长度相同，否则
     *        输出字符串中会有少于原始个数的'-'
     */
    public String licenseKeyFormattingBackward(String S, int K) {
        int count = 0;
        int i = S.length() - 1;
        StringBuilder res = new StringBuilder();

        while (i >= 0) {
            //记得将当前字符变为大写
            char c = Character.toUpperCase(S.charAt(i));
            //如果当前字符为'-'，直接跳过
            if (c == '-') {
                i--;
            } else if (count == K) {
                //若当前group的字符数等于K个，需要插入一个'-'，然后将计数归零
                count = 0;
                res.append('-');
            } else {
                //否则就继续讲当前字符放入结果中
                res.append(c);
                count++;
                i--;
            }
        }
        //因为是倒序遍历，最后需要将结果反序输出
        return res.reverse().toString();
    }

    /**
     * Approach 2: Forward
     * 若想正序遍历，就需要直到第一个group到底应该有几个字符。该结果可以通过计算得到，先将输入字符串进行处理（去掉所有'-'，并将字母都转换成大写），此时可以得到
     * 输入字符串的的字符总数N，(N / K)即为长度为K的group的总数，(N % K)即为多余字符，这些多余字符就是第一个group中的字符。因此若N % K不为0，就先将前N % K个
     * 字符看做一个group，然后从N % K开始，每K个字符看做一个group，最后在所有group中间插入'-'即可
     *
     * Time: O(N) 此方法需要正序遍历整个字符串
     * Space: O(N)
     */
    public String licenseKeyFormattingForward(String S, int K) {
        S = S.replaceAll("-", "").toUpperCase();
        int len = S.length();
        int start = len % K;
        List<String> res = new ArrayList<>();
        if (start > 0) {
            res.add(S.substring(0, start));
        }
        for (int i = start; i < len; i += K) {
            res.add(S.substring(i, i + K));
        }
        return String.join("-", res);
    }

    @Test
    public void licenseKeyFormattingTest() {
        /**
         * Example 1:
         * Input: S = "5F3Z-2e-9-w", K = 4
         *
         * Output: "5F3Z-2E9W"
         *
         * Explanation: The string S has been split into two parts, each part has 4 characters.
         * Note that the two extra dashes are not needed and can be removed.
         */
        assertEquals("5F3Z-2E9W", licenseKeyFormattingBackward("5F3Z-2e-9-w", 4));
        assertEquals("5F3Z-2E9W", licenseKeyFormattingForward("5F3Z-2e-9-w", 4));
        /**
         * Example 2:
         * Input: S = "2-5g-3-J", K = 2
         *
         * Output: "2-5G-3J"
         *
         * Explanation: The string S has been split into three parts, each part has 2 characters except the first part as it could be
         * shorter as mentioned above.
         */
        assertEquals("2-5G-3J", licenseKeyFormattingBackward("2-5g-3-J", 2));
        assertEquals("2-5G-3J", licenseKeyFormattingForward("2-5g-3-J", 2));
    }
}
