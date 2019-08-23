import org.junit.Test;
import static org.junit.Assert.*;

public class lengthOfLongestSubstringKDistinct {

    /**
     * Given a string, find the length of the longest substring T that contains at most k distinct characters
     *
     * Approach: Sliding Window + Hash Map
     * 只需要用sliding window的方法配合hash map记录当前window中distinct字符的个数即可。将两个pointer都初始化为0，即从index 0，然后不断将右指针所指的字符
     * 加入当前子串，若当前子串distinct的字符数目小于k，则不断地向右移动右指针，更新每个字符出现的频率，同时更新最大子串长度。若加入新字符使得当前子串的distinct
     * 字符数大于k，则移动左指针，减小window size直到再次满足条件。之后再移动右指针，重复上述过程，直到右指针遍历完整个字符串
     *
     * Time: O(n) 每个字符最多被两个指针各遍历一遍
     * Space: O(1)，若假设输入字符为ASCII码，可以用一个长度为256的数组来代替hash map
     */
    public int lengthOfLongestSubstringKDistinct(String s, int k) {
        int maxLen = 0;
        //edge case，若字符串为空，或者k为0，则最大长度就是0
        if(s == null || s.length() == 0 || k == 0) return maxLen;
        int[] map = new int[256];
        int left = 0, right = 0;
        //记录当前子串中distinct的字符个数
        int count = 0;
        while(right < s.length()) {
            //将右指针所指字符不断地加入子串中
            char rightChar = s.charAt(right);
            //更新该字符出现的频率，若该字符是第一次出现，则需要更新count
            if(map[rightChar] == 0) {
                count++;
            }
            map[rightChar]++;
            //判断加入新字符后是否不再满足条件，若不满足，则需要移动左指针，缩小window size
            while(left < s.length() && count > k) {
                //不断地将左指针所指字符移除子串
                char leftChar = s.charAt(left);
                //更新字符出现频率，若该字符是最后一次出现，则需要对count - 1
                if(map[leftChar] == 1) {
                    count--;
                }
                map[leftChar]--;
                //移动左指针到下一个字符，判断是否满足条件
                left++;
            }
            //更新最大长度
            maxLen = Math.max(maxLen, right - left + 1);
            //然后移动右指针，扩大window size，继续上述过程
            right++;
        }
        return maxLen;
    }


    @Test
    public void lengthOfLongestSubstringKDistinctTest() {
        /**
         * Example 1:
         * Input: s = "eceba", k = 2
         * Output: 3
         * Explanation: T is "ece" which its length is 3.
         */
        String s1 = "eceba";
        int k1 = 2;
        assertEquals(3, lengthOfLongestSubstringKDistinct(s1, k1));
        /**
         * Example 2:
         * Input: s = "aa", k = 1
         * Output: 2
         * Explanation: T is "aa" which its length is 2.
         */
        String s2 = "aa";
        int k2 = 1;
        assertEquals(2, lengthOfLongestSubstringKDistinct(s2, k2));
    }
}
