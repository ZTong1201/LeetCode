import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class NumberOfGoodWaysToSplitString {

    /**
     * You are given a string s, a split is called good if you can split s into 2 non-empty strings p and q where its
     * concatenation is equal to s and the number of distinct letters in p and q are the same.
     * <p>
     * Return the number of good splits you can make in s.
     * <p>
     * Constraints:
     * <p>
     * s contains only lowercase English letters.
     * 1 <= s.length <= 10^5
     * <p>
     * Approach: DP
     * For a given string of length n, there will be n - 1 ways to split a string. We basically need to go through all
     * the possible splits and see whether it is a good split. In order to easily get the number of unique characters in the
     * both sides. We could use extra arrays to keep track of the number of unique characters until the correct index from
     * both forward and backward direction. Then, when a string is split at index i, we could simply check whether
     * forward[i] == backward[i + 1].
     * <p>
     * Time: O(n)
     * Space: O(n)
     */
    public int numSplits(String s) {
        Set<Character> forwardSeen = new HashSet<>(), backwardSeen = new HashSet<>();
        int length = s.length();
        int[] numOfUniqueForward = new int[length], numOfUniqueBackward = new int[length];
        // compute the number of unique characters till index i from both forward and backward directions
        for (int i = 0; i < length; i++) {
            forwardSeen.add(s.charAt(i));
            backwardSeen.add(s.charAt(length - 1 - i));
            numOfUniqueForward[i] = forwardSeen.size();
            numOfUniqueBackward[length - 1 - i] = backwardSeen.size();
        }

        int res = 0;
        // enumerate all splits and check whether it is a good split
        for (int i = 0; i < length - 1; i++) {
            if (numOfUniqueForward[i] == numOfUniqueBackward[i + 1]) res++;
        }
        return res;
    }

    @Test
    public void numSplitsTest() {
        /**
         * Example 1:
         * Input: s = "aacaba"
         * Output: 2
         * Explanation: There are 5 ways to split "aacaba" and 2 of them are good.
         * ("a", "acaba") Left string and right string contains 1 and 3 different letters respectively.
         * ("aa", "caba") Left string and right string contains 1 and 3 different letters respectively.
         * ("aac", "aba") Left string and right string contains 2 and 2 different letters respectively (good split).
         * ("aaca", "ba") Left string and right string contains 2 and 2 different letters respectively (good split).
         * ("aacab", "a") Left string and right string contains 3 and 1 different letters respectively.
         */
        assertEquals(2, numSplits("aacaba"));
        /**
         * Example 2:
         * Input: s = "abcd"
         * Output: 1
         * Explanation: Split the string as follows ("ab", "cd").
         */
        assertEquals(1, numSplits("abcd"));
        /**
         * Example 3:
         * Input: s = "aaaaa"
         * Output: 4
         * Explanation: All possible splits are good.
         */
        assertEquals(4, numSplits("aaaaa"));
        /**
         * Example 4:
         * Input: s = "acbadbaada"
         * Output: 2
         */
        assertEquals(2, numSplits("acbadbaada"));
    }
}
