import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class minWindowSubstring {

    /**
     * Given a string S and a string T, find the minimum window in S which will contain all the characters in T in complexity O(n).
     * <p>
     * Note:
     * <p>
     * If there is no such window in S that covers all characters in T, return the empty string "".
     * If there is such window, you are guaranteed that there will always be only one unique minimum window in S.
     * <p>
     * Approach: Sliding Window (Two pointers)
     * 为了在O(N)时间内解决此问题，只能利用sliding window来解。本质上，是找到一个最小的window size，使得其中间子串包含T中所有字符，注意T中可能有重复
     * 字符。可以证明，最小的window size一定是以T中的某一字符开头，并以某一字符结尾。所以需要一个额外的函数，计算下一个在T中的字符，即跳过其他不相干字符。
     * 同时要保证该子串内包含T中全部字符。可以用一个size为256的数组来记录每个字符出现的频率，只要输入字符在ASCII范围内，256的数组都可以覆盖。同时还需要
     * 一个变量来记录当前match的字符总个数，当match的总字符数等于T的长度时，就可以更新当前的子串。
     * <p>
     * 需要注意的是，因为可能存在重复出现的字符，所以当子串中某字符出现的频率和T中相同后，再出现相同字符就不在累加match的字符个数。若当前window包含了T的全部
     * 字符后，可以尝试减小window size（即移动左指针到下一个T中含有的字符），若当前子串也符合条件，则继续更新结果。若不满足条件，说明缺失某字符，需要继续
     * 扩大当前的window size（即移动右指针到下一个T中含有的字符）。直到最后右指针溢出边界，遍历完毕。
     * <p>
     * Time: O(S + T)，需要遍历T一遍记录字符出现频率，同时在改变window size的过程中，需要遍历整个字符一次。同时，最坏情况下每个T中字符会被遍历两次（
     * 左右指针各一次）
     * Space: O(1)，无论输入字符串长度如何，都只需要两个size为256的数组
     */
    public String minWindow(String s, String t) {
        //记录T中字符出现频率
        int[] tMap = new int[128];
        for (int i = 0; i < t.length(); i++) {
            tMap[t.charAt(i)]++;
        }

        //需要记录S中字符出现频率以及和T中字符match的个数
        int[] sMap = new int[128];
        int matchCount = 0;

        String res = "";
        // initialize the minimum length as infinity
        int minLen = Integer.MAX_VALUE;

        // 首先搜索window的起点, 起始的window从size为1开始
        int slow = findNextValidChar(0, s, tMap), fast = slow;

        while (fast < s.length()) {
            //先将右指针所指字符加入当前子串
            char rightChar = s.charAt(fast);
            //只要当某字符出现频率小于T中频率时，才累加match字符个数
            if (sMap[rightChar] < tMap[rightChar]) {
                matchCount++;
            }
            //但无论如何，都要累加当前字符出现的频率
            sMap[rightChar]++;

            //加入右指针字符后，判断当前子串是否符合条件，若符合条件，则尝试移动左指针，缩小window size
            //注意保证左指针也在边界内
            while (slow < s.length() && matchCount == t.length()) {
                //当前子串符合条件且其长度小于当前的最小长度
                //更新最小长度和子串
                if (minLen > fast - slow + 1) {
                    minLen = fast - slow + 1;
                    res = s.substring(slow, fast + 1);
                }

                //然后，尝试缩小window size，即移动左指针
                char leftChar = s.charAt(slow);
                //若当前子串中某字符频率大于T中频率，则移除该字符不会影响match字符的个数
                //反之，则需要减少一个match字符个数
                if (sMap[leftChar] <= tMap[leftChar]) {
                    matchCount--;
                }
                //但无论如何，当前字符的频率都要减1
                sMap[leftChar]--;
                //然后移动左指针到下一个T中存在的字符
                slow = findNextValidChar(slow + 1, s, tMap);
            }
            //移动之后，当前子串不再符合条件，需要移动右指针扩大当前window size寻找下一个可能子串
            fast = findNextValidChar(fast + 1, s, tMap);
        }
        return res;
    }

    private int findNextValidChar(int start, String s, int[] tMap) {
        while (start < s.length()) {
            //若某字符在T中出现过，则返回当前index
            if (tMap[s.charAt(start)] != 0) return start;
            start++;
        }
        return start;
    }


    @Test
    public void minWindowTest() {
        /**
         * Example:
         * Input: S = "ADOBECODEBANC", T = "ABC"
         * Output: "BANC"
         */
        assertEquals("BANC", minWindow("ADOBECODEBANC", "ABC"));
        /**
         * Example 2:
         * Input: s = "a", t = "a"
         * Output: "a"
         * Explanation: The entire string s is the minimum window.
         */
        assertEquals("a", minWindow("a", "a"));
        /**
         * Example 3:
         * Input: s = "a", t = "aa"
         * Output: ""
         * Explanation: Both 'a's from t must be included in the window.
         * Since the largest window of s only has one 'a', return empty string.
         */
        assertEquals("", minWindow("a", "aa"));
    }
}
