import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class wordBreak {

    /**
     * Given a non-empty string s and a dictionary wordDict containing a list of non-empty words, determine if s can be segmented
     * into a space-separated sequence of one or more dictionary words.
     *
     * Note:
     *
     * The same word in the dictionary may be reused multiple times in the segmentation.
     * You may assume the dictionary does not contain duplicate words.
     *
     * Approach 1: BFS
     * 此题可以用bfs解，即从index 0出发，找到所有以当前index开始，且在wordDict的单词，以该单词的末尾的下一个index作为新的起始点，继续搜索新单词。
     * 若在任意时刻，找到某一单词以最后一个字母结尾，则返回true。
     *
     * Time: O(n^2) 最坏情况下，例如输入"aaaaab"，wordDict = ["a","aa","aaa","aaaa","aaaaa"]，每一个index都可以做为开始，同时每次搜索都有可能搜索
     *       整个字符串，所以runtime为O(n^2)
     * Space: O(n) 最坏情况下，queue中会含有n个待搜索index，同时需要一个boolean数组来记录已遍历的节点，避免重复遍历
     */
    public boolean wordBreakBFS(String s, List<String> wordDict) {
        Set<String> words = new HashSet<>(wordDict);
        Queue<Integer> queue = new LinkedList<>();
        queue.add(0);
        boolean[] visited = new boolean[s.length()];

        while(!queue.isEmpty()) {
            //找到起始index
            int start = queue.poll();
            //若当前index尚未遍历，则以此为起点搜寻单词
            if(!visited[start]) {
                visited[start] = true;
                //遍历起点后的所有index，若有某些substring在wordDict中出现，则以该word的终点最为新起点搜寻
                for(int end = start + 1; end <= s.length(); end++) {
                    if(words.contains(s.substring(start, end))) {
                        queue.add(end);
                        //若某一时刻，单词以最后一个字母结尾，则返回true
                        if(end == s.length()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }


    /**
     * Approach 2: 2-D DP
     * 可以将上述过程转化为2维dp。首先需要一个二维的boolean数组, dp[i][j]表示的从index i开始到index j结束，中间的substring是否在wordDict中。为了保证
     * 整个字符串可以分割成不重叠的单词，当i不为0时，dp[i][j]若为true，说明从i开始到j结束的substring在wordDict中，则此时需要判断从0开始到i - 1结束，
     * 这段substring是否在wordDict中，即判断dp[0][i - 1]，若仍为true，说明dp[0][j]也应为true，表示从0开始到j结束，可以分割成不重叠的子串。最后
     * 只需返回dp[0][length - 1]即可
     *
     * Time: O(n^2) 需要遍历O(n^2)子串，判断是否在wordDict中
     * Space: O(n^2) 需要一个二维boolean数组
     */
    public boolean wordBreak2D_DP(String s, List<String> wordDict) {
        Set<String> words = new HashSet<>(wordDict);
        boolean[][] dp = new boolean[s.length()][s.length()];
        for(int start = 0; start < s.length(); start++) {
            for(int end = start + 1; end <= s.length(); end++) {
                String substr = s.substring(start, end);
                if(wordDict.contains(substr)) {
                    dp[start][end - 1] = true;
                    if(dp[start][end - 1]) {
                        if(start != 0 && dp[0][start - 1]) {
                            dp[0][end - 1] = true;
                        }
                    }
                }
            }
        }
        return dp[0][s.length() - 1];
    }

    /**
     * Approach 3: 1-D DP
     * 可以将方法二转化为1维dp，本质上，需要判断的是从0开始到i结束，是否可以分割成不重叠的子串。所以只关心dp[i]，即从0到i是否满足条件即可。对于index i,
     * 还需要另一个变量j从0到i，将前i个子串分成两个subproblem，若dp[j]为true且从j到i的substring也在wordDict中，则dp[j]即可为true。
     * 一维数组需要length + 1的空间，同时要把dp[0]初始化为true，表示任何空字符都存在于wordDict
     *
     * Time: O(n^2) two nested loop to fill in dp array
     * Space: O(n) 只需要一个一维数组
     */
    public boolean wordBreak1D_DP(String s, List<String> wordDict) {
        Set<String> words = new HashSet<>(wordDict);
        boolean[] dp = new boolean[s.length() + 1];
        dp[0] = true;
        for(int i = 1; i <= s.length(); i++) {
            for(int j = 0; j < i; j++) {
                if(dp[j] && words.contains(s.substring(j, i))) {
                    dp[i] = true;
                    break;
                }
            }
        }
        return dp[s.length()];
    }

    @Test
    public void wordBreakTest() {
        /**
         * Example 1:
         * Input: s = "leetcode", wordDict = ["leet", "code"]
         * Output: true
         * Explanation: Return true because "leetcode" can be segmented as "leet code".
         */
        String s1 = "leetcode";
        List<String> wordDict1 = new ArrayList<>(Arrays.asList("leet", "code"));
        assertTrue(wordBreakBFS(s1, wordDict1));
        assertTrue(wordBreak2D_DP(s1, wordDict1));
        assertTrue(wordBreak1D_DP(s1, wordDict1));
        /**
         * Example 2:
         * Input: s = "applepenapple", wordDict = ["apple", "pen"]
         * Output: true
         * Explanation: Return true because "applepenapple" can be segmented as "apple pen apple".
         *              Note that you are allowed to reuse a dictionary word.
         */
        String s2 = "applepenapple";
        List<String> wordDict2 = new ArrayList<>(Arrays.asList("apple", "pen"));
        assertTrue(wordBreakBFS(s2, wordDict2));
        assertTrue(wordBreak2D_DP(s2, wordDict2));
        assertTrue(wordBreak1D_DP(s2, wordDict2));
        /**
         * Example 3:
         * Input: s = "catsandog", wordDict = ["cats", "dog", "sand", "and", "cat"]
         * Output: false
         */
        String s3 = "catsandog";
        List<String> wordDict3 = new ArrayList<>(Arrays.asList("cats", "dog", "sand", "and", "cat"));
        assertFalse(wordBreakBFS(s3, wordDict3));
        assertFalse(wordBreak2D_DP(s3, wordDict3));
        assertFalse(wordBreak1D_DP(s3, wordDict3));
    }
}
