import org.junit.Test;
import static org.junit.Assert.*;

public class minWindowSubstring {

    /**
     * Given a string S and a string T, find the minimum window in S which will contain all the characters in T in complexity O(n).
     *
     * Note:
     *
     * If there is no such window in S that covers all characters in T, return the empty string "".
     * If there is such window, you are guaranteed that there will always be only one unique minimum window in S.
     *
     * Approach: Sliding Window (Two pointers)
     * 为了在O(N)时间内解决此问题，只能利用sliding window来解。本质上，是找到一个最小的window size，使得其中间子串包含T中所有字符，注意T中可能有重复
     * 字符。可以证明，最小的window size一定是以T中的某一字符开头，并以某一字符结尾。所以需要一个额外的函数，计算下一个在T中的字符，即跳过其他不相干字符。
     * 同时要保证该子串内包含T中全部字符。可以用一个size为256的数组来记录每个字符出现的频率，只要输入字符在ASCII范围内，256的数组都可以覆盖。同时还需要
     * 一个变量来记录当前match的字符总个数，当match的总字符数等于T的长度时，就可以更新当前的子串。
     *
     * 需要注意的是，因为可能存在重复出现的字符，所以当子串中某字符出现的频率和T中相同后，再出现相同字符就不在累加match的字符个数。若当前window包含了T的全部
     * 字符后，可以尝试减小window size（即移动左指针到下一个T中含有的字符），若当前子串也符合条件，则继续更新结果。若不满足条件，说明缺失某字符，需要继续
     * 扩大当前的window size（即移动右指针到下一个T中含有的字符）。直到最后右指针溢出边界，遍历完毕。
     *
     * Time: O(S + T)，需要遍历T一遍记录字符出现频率，同时在改变window size的过程中，需要遍历整个字符一次。同时，最坏情况下每个T中字符会被遍历两次（
     *      左右指针各一次）
     * Space: O(1)，无论输入字符串长度如何，都只需要两个size为256的数组
     */
    public String minWindow(String s, String t) {
        //edge case，若S或T中任意一个为空，则返回空字符串
        String res = "";
        if(s == null || t == null || s.length() == 0 || t.length() == 0) {
            return "";
        }
        //记录T中字符出现频率
        int[] tMap = new int[256];
        for(int i = 0; i < t.length(); i++) {
            tMap[t.charAt(i)]++;
        }
        //首先搜索window的起点
        int left = findNextValidChar(0, s, tMap);
        //如果搜索到字符串结束也没有找到T中字符，说明无法匹配，返回空字符串
        if(left == s.length()) return res;
        //需要记录S中字符出现频率以及和T中字符match的个数
        int[] sMap = new int[256];
        int matchCount = 0;
        //其实的window从size为1开始
        int right = left;
        while(right < s.length()) {
            //先将右指针所指字符加入当前子串
            char rightChar = s.charAt(right);
            //只要当某字符出现频率小于T中频率时，才累加match字符个数
            if(sMap[rightChar] < tMap[rightChar]) {
                matchCount++;
            }
            //但无论如何，都要累加当前字符出现的频率
            sMap[rightChar]++;
            //加入右指针字符后，判断当前子串是否符合条件，若符合条件，则尝试移动左指针，缩小window size
            //注意保证左指针也在边界内
            while(left < s.length() && matchCount == t.length()) {
                //当前子串符合条件，若res为空，说明是第一个符合条件的子串
                //或者，现在的window size小于之前记录的子串长度，都需要更新最小子串
                if(res.isEmpty() || res.length() > right - left + 1) {
                    res = s.substring(left, right + 1);
                }
                //然后，尝试缩小window size，即移动左指针
                char leftChar = s.charAt(left);
                //若当前子串中某字符频率大于T中频率，则移除该字符不会影响match字符的个数
                //反之，则需要减少一个match字符个数
                if(sMap[leftChar] <= tMap[leftChar]) {
                    matchCount--;
                }
                //但无论如何，当前字符的频率都要减1
                sMap[leftChar]--;
                //然后移动左指针到下一个T中存在的字符
                left = findNextValidChar(left + 1, s, tMap);
            }
            //移动之后，当前子串不再符合条件，需要移动右指针扩大当前window size寻找下一个可能子串
            right = findNextValidChar(right + 1, s, tMap);
        }
        return res;
    }

    private int findNextValidChar(int start, String s, int[] tMap) {
        while(start < s.length()) {
            char c = s.charAt(start);
            //若某字符在T中出现过，则返回当前index
            if(tMap[c] != 0) return start;
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
        String s = "ADOBECODEBANC";
        String t = "ABC";
        assertEquals("BANC", minWindow(s, t));
    }
}
