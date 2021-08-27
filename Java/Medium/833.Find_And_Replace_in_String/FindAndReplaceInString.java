import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class FindAndReplaceInString {

    /**
     * You are given a 0-indexed string s that you must perform k replacement operations on. The replacement operations
     * are given as three 0-indexed parallel arrays, indices, sources, and targets, all of length k.
     * <p>
     * To complete the ith replacement operation:
     * <p>
     * Check if the substring sources[i] occurs at index indices[i] in the original string s.
     * If it does not occur, do nothing.
     * Otherwise if it does occur, replace that substring with targets[i].
     * For example, if s = "abcd", indices[i] = 0, sources[i] = "ab", and targets[i] = "eee", then the result of this
     * replacement will be "eeecd".
     * <p>
     * All replacement operations must occur simultaneously, meaning the replacement operations should not affect the
     * indexing of each other. The testcases will be generated such that the replacements will not overlap.
     * <p>
     * For example, a testcase with s = "abc", indices = [0, 1], and sources = ["ab","bc"] will not be generated because
     * the "ab" and "bc" replacements overlap.
     * Return the resulting string after performing all replacement operations on s.
     * <p>
     * A substring is a contiguous sequence of characters in a string.
     * <p>
     * Constraints:
     * <p>
     * 1 <= s.length <= 1000
     * k == indices.length == sources.length == targets.length
     * 1 <= k <= 100
     * 0 <= indexes[i] < s.length
     * 1 <= sources[i].length, targets[i].length <= 50
     * s consists of only lowercase English letters.
     * sources[i] and targets[i] consist of only lowercase English letters.
     * <p>
     * Approach: Hash Table
     * We can loop through all the substrings in sources array and check whether the appearance is actually at desired index.
     * If yes, we add it to a hash table for finalization. Once all the valid replacement were found, we start from index 0
     * to the end, if the substring needs to be fixed at that index - execute the replacement and skip the original length
     * of the substring (since it's being replaced). Otherwise, the current character remains unchanged - append it to the
     * final result "AS IS".
     * <p>
     * Note that the startsWith(String prefix, int toffset) is extremely useful for this problem. It returns true if the substring
     * starting at toffset index actual starts with a specific prefix.
     * Ref java doc: https://docs.oracle.com/javase/7/docs/api/java/lang/String.html#startsWith(java.lang.String,%20int)
     * <p>
     * Time: O(N + M * L) where N is the length of original string, M is the length of indices, and L is the average length
     * of substrings in sources. For each substring to be replaced, we need to call startsWith, which make takes O(L) runtime.
     * Then we need to iterate over the entire string to make the replacement.
     * Space: O(N + M) we need M replacement pairs in the hash table and the final result requires O(n) space for string builder
     */
    public String findReplaceString(String s, int[] indices, String[] sources, String[] targets) {
        // create a hash map - find the replacement string index j in targets
        // if string s needs to be placed at index i
        Map<Integer, Integer> stringIndexToTargetIndex = new HashMap<>();
        for (int i = 0; i < indices.length; i++) {
            // if string s has substring sources[i] at index indices[i]
            // add the mapping such that it can be replaced later
            if (s.startsWith(sources[i], indices[i])) {
                stringIndexToTargetIndex.put(indices[i], i);
            }
        }

        StringBuilder res = new StringBuilder();
        // iterate over the string to replace certain substrings
        // this works since there is no overlapping replacement
        int index = 0;
        while (index < s.length()) {
            // if the current index can be found in the map
            // which means it needs to be replaced
            if (stringIndexToTargetIndex.containsKey(index)) {
                // add the replacement substring into the string builder
                res.append(targets[stringIndexToTargetIndex.get(index)]);
                // skip the original substrings in s
                index += sources[stringIndexToTargetIndex.get(index)].length();
            } else {
                // otherwise, the character needs to be appended as is
                res.append(s.charAt(index));
                index++;
            }
        }
        return res.toString();
    }

    @Test
    public void findReplaceStringTest() {
        /**
         * Example 1:
         * Input: s = "abcd", indices = [0, 2], sources = ["a", "cd"], targets = ["eee", "ffff"]
         * Output: "eeebffff"
         * Explanation:
         * "a" occurs at index 0 in s, so we replace it with "eee".
         * "cd" occurs at index 2 in s, so we replace it with "ffff".
         */
        assertEquals("eeebffff", findReplaceString("abcd", new int[]{0, 2},
                new String[]{"a", "cd"}, new String[]{"eee", "ffff"}));
        /**
         * Example 2:
         * Input: s = "abcd", indices = [0, 2], sources = ["ab","ec"], targets = ["eee","ffff"]
         * Output: "eeecd"
         * Explanation:
         * "ab" occurs at index 0 in s, so we replace it with "eee".
         * "ec" does not occur at index 2 in s, so we do nothing.
         */
        assertEquals("eeecd", findReplaceString("abcd", new int[]{0, 2},
                new String[]{"ab", "ec"}, new String[]{"eee", "ffff"}));
        /**
         * Example 3:
         * Input: s = "wreorttvosuidhrxvmvo", indices = [14,12,10,5,0,18]
         * sources = ["rxv","dh","ui","ttv","wreor","vo"]
         * targets = ["frs","c","ql","qpir","gwbeve","n"]
         * Output: "gwbeveqpirosqlcfrsmn"
         * Explanation:
         * "vo" appears twice, but only the one starts at 18 will be replaced
         */
        assertEquals("gwbeveqpirosqlcfrsmn", findReplaceString("wreorttvosuidhrxvmvo", new int[]{14, 12, 10, 5, 0, 18},
                new String[]{"rxv", "dh", "ui", "ttv", "wreor", "vo"}, new String[]{"frs", "c", "ql", "qpir", "gwbeve", "n"}));
    }
}
