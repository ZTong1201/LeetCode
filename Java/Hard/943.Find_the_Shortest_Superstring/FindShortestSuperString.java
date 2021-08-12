import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FindShortestSuperString {

    /**
     * Given an array of strings words, return the smallest string that contains each string in words as a substring.
     * If there are multiple valid strings of the smallest length, return any of them.
     * <p>
     * You may assume that no string in words is a substring of another string in words.
     * <p>
     * Constraints:
     * <p>
     * 1 <= words.length <= 12
     * 1 <= words[i].length <= 20
     * words[i] consists of lowercase English letters.
     * All the strings of words are unique.
     * <p>
     * Approach 1: DFS (brute force)
     * <p>
     * Time: O(n!) in the worst case, we need to enumerate all the permutations and find the shortest super string
     * Space: O(n^2) the 2-D cost array will dominate the space usage
     */
    private int length, best_len;
    // int array to keep track of the path of the shortest super string
    private int[] path, best_path;
    // cost[i][j] means the number of characters from the end in order to append words[j] to words[i]
    // e.g. words[i] = "gcta", words[j] = "ctaagt", cost[i][j] = 3 since we only need to append "agt" to the end of "gcta"
    private int[][] cost;
    private String[] A;

    public String shortestSuperStringDFS(String[] words) {
        length = words.length;
        best_len = Integer.MAX_VALUE;
        A = words;
        path = new int[length];
        cost = new int[length][length];
        // pre-processing
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                // if there is no overlap between word i and word j
                // the cost would be the length of word j
                cost[i][j] = words[j].length();
                // check whether the suffix of word i is a prefix of word j
                for (int k = 1; k <= Math.min(words[i].length(), words[j].length()); k++) {
                    if (words[i].substring(words[i].length() - k).equals(words[j].substring(0, k))) {
                        // update the newest cost accordingly
                        cost[i][j] = words[j].length() - k;
                    }
                }
            }
        }

        // search the best path to construct the shortest super string
        boolean[] used = new boolean[length];
        dfs(0, used, 0);

        // starting from the first word in the best path
        StringBuilder sb = new StringBuilder(words[best_path[0]]);
        for (int k = 1; k < length; k++) {
            // get two consecutive word index from the best path
            int i = best_path[k - 1];
            int j = best_path[k];
            // need to append the substring of word j
            sb.append(words[j].substring(words[j].length() - cost[i][j]));
        }
        return sb.toString();
    }

    private void dfs(int start, boolean[] used, int curr_len) {
        // early termination if current branch cannot form a shorter string
        if (curr_len >= best_len) return;
        // since any longer super string has been discarded
        // the new path must have been a candidate (would've been updated later)
        if (start == length) {
            best_len = curr_len;
            // need a clone of current path since it may well be updated later
            best_path = path.clone();
            return;
        }

        // DFS
        for (int i = 0; i < length; i++) {
            // skip used words
            if (used[i]) continue;
            // add current word index to the path
            path[start] = i;
            used[i] = true;
            dfs(start + 1, used,
                    start == 0 ? A[i].length() : curr_len + cost[path[start - 1]][i]);
            // backtrack
            used[i] = false;
        }
    }

    @Test
    public void shortestSuperStringTest() {
        /**
         * Example 1:
         * Input: words = ["alex","loves","leetcode"]
         * Output: "alexlovesleetcode"
         * Explanation: All permutations of "alex","loves","leetcode" would also be accepted.
         */
        String[] words1 = new String[]{"alex", "loves", "leetcode" };
        String expected1 = "alexlovesleetcode";
        String actualDFS1 = shortestSuperStringDFS(words1);
        assertEquals(expected1, actualDFS1);
        /**
         * Example 2:
         * Input: words = ["catg","ctaagt","gcta","ttca","atgcatc"]
         * Output: "gctaagttcatgcatc"
         */
        String[] words2 = new String[]{"catg", "ctaagt", "gcta", "ttca", "atgcatc" };
        String expected2 = "gctaagttcatgcatc";
        String actualDFS2 = shortestSuperStringDFS(words2);
        assertEquals(expected2, actualDFS2);
    }
}
