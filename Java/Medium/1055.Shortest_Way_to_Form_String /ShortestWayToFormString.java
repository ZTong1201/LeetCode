import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ShortestWayToFormString {

    /**
     * A subsequence of a string is a new string that is formed from the original string by deleting some (can be none) of
     * the characters without disturbing the relative positions of the remaining characters. (i.e., "ace" is a subsequence
     * of "abcde" while "aec" is not).
     * <p>
     * Given two strings source and target, return the minimum number of subsequences of source such that their concatenation
     * equals target. If the task is impossible, return -1.
     * <p>
     * Constraints:
     * <p>
     * 1 <= source.length, target.length <= 1000
     * source and target consist of lowercase English letters.
     * <p>
     * Approach: Greedy
     * If there is a character in target that doesn't exist in source, return -1. Otherwise, we would always be able to construct
     * target from the source string. In order to achieve the shortest length, we need to use as many characters in the source
     * string as possible. Hence, we try to start from the leftmost character in source to find a matching character in target,
     * if the two characters are not matched, we move to the next character in source. The key part is to restart from the
     * beginning when we reach the end of the source string, whenever there is a restart, we need to increment the count
     * because we need one more subsequence.
     * <p>
     * Time: O(m * n) where m is the length of source, and n is the length of target. In the worst case, we need to traverse
     * the entire source string to find a matching character. For example, if source = "aaaaaaaz", where target = "zzzzzzzz",
     * each time, we need to traverse the source to get the character 'z' and we need to repeat it n times.
     * Space: O(1)
     */
    public int shortestWay(String source, String target) {
        // if we can form target using source, we need at least 1 subsequence of source
        int sourceIndex = 0, count = 1;
        // iterate over every character in target
        for (int i = 0; i < target.length(); i++) {
            char curr = target.charAt(i);
            // if target has a character that doesn't exist in source, return -1 directly
            if (source.indexOf(curr) == -1) return -1;

            // otherwise, find the correct index after the previous source index
            sourceIndex = source.indexOf(curr, sourceIndex);
            // if we cannot find desired character after previous source index
            if (sourceIndex == -1) {
                // we need restart the beginning and increment the count
                sourceIndex = source.indexOf(curr);
                count++;
            }
            // the current index matches with the desired character - move to the next position
            sourceIndex++;
        }
        return count;
    }

    @Test
    public void shortestWayTest() {
        /**
         * Example 1:
         * Input: source = "abc", target = "abcbc"
         * Output: 2
         * Explanation: The target "abcbc" can be formed by "abc" and "bc", which are subsequences of source "abc".
         */
        assertEquals(2, shortestWay("abc", "abcbc"));
        /**
         * Example 2:
         * Input: source = "abc", target = "acdbc"
         * Output: -1
         * Explanation: The target string cannot be constructed from the subsequences of source string due to the
         * character "d" in target string.
         */
        assertEquals(-1, shortestWay("abc", "acdbc"));
        /**
         * Example 3:
         * Input: source = "xyz", target = "xzyxz"
         * Output: 3
         * Explanation: The target string can be constructed as follows "xz" + "y" + "xz".
         */
        assertEquals(3, shortestWay("xyz", "xzyxz"));
    }
}
