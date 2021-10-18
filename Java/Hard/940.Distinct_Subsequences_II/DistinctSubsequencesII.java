import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class DistinctSubsequencesII {

    /**
     * Given a string s, return the number of distinct non-empty subsequences of s. Since the answer may be very large, return
     * it modulo 10^9 + 7.
     * <p>
     * A subsequence of a string is a new string that is formed from the original string by deleting some (can be none) of the
     * characters without disturbing the relative positions of the remaining characters. (i.e., "ace" is a subsequence of
     * "abcde" while "aec" is not.
     * <p>
     * Constraints:
     * <p>
     * 1 <= s.length <= 2000
     * s consists of lowercase English letters.
     * <p>
     * Approach 1: DP
     * Given a character in the string, we can find all characters whose index is smaller than the current char, the number
     * of unique subsequences will be the sum of all the previous subsequences. For example, "abcd", if we want to know
     * how many subsequences ends with "d", then the result is subseq("a") + subseq("b") + subseq("c"). Why? Because we can
     * simply append "d" after them to form a longer subsequence. However, there is one special case that when there are
     * the same characters. For instance, given "aaa", for the third "a" at index 2, we can only append "a" after "aa" to form
     * a distinct subsequence, appending "a" after the first "a" to form "aa" will be a duplicate. That being said, we there
     * is a duplicate character, we can only form one new subsequence - which is appending the character to the last appearance
     * of the same character. Therefore, here's the complete algorithm.
     * 1. Initialize a dp array, where dp[i] is the total number of unique subsequences ends with s.charAt(i), and we can fill
     * the entire array with 1. Because, in the special case when there are same characters, the number of subsequences is 1
     * and any unique character itself is a subsequence of length 1.
     * 2. For a given index i, we search all the characters whose index j is in the range [0, i) and sum up the previous number
     * of subsequences if the character is different. We can take the modulo on the fly.
     * 3. The final result will be the total subsequences ends with each index.
     * <p>
     * Time: O(n^2) since for each index, we need to search all previous indexes, which gives O(n^2) pairs to be searched
     * Space: O(n)
     */
    public int distinctSubseqIIDP1(String s) {
        final int MOD = 1_000_000_007;
        int length = s.length();
        int[] numOfDistinctSubseq = new int[length];
        // fill the array with 1, since each character itself will be a subsequence
        Arrays.fill(numOfDistinctSubseq, 1);
        int total = 0;

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < i; j++) {
                // for each character, we need to search all characters before it and sum up the result
                // when the character is different
                if (s.charAt(i) != s.charAt(j)) {
                    // we need to take the modulo on the fly
                    numOfDistinctSubseq[i] = (numOfDistinctSubseq[i] + numOfDistinctSubseq[j]) % MOD;
                }
            }
            // then we add the current number of subsequences to the final result
            total = (total + numOfDistinctSubseq[i]) % MOD;
        }
        return total;
    }

    /**
     * Approach 2: DP - One pass
     * Actually, we don't need to look back through all the previous characters before index i. What are we actually
     * needing? We need the total number of unique subsequences which is not ending with the same character. Therefore,
     * we only need two pieces of information:
     * 1. The total number of unique subsequences before index i - which we will be computing it on the fly and return it
     * in the end
     * 2. The total number of unique subsequences end with s.charAt(i) before index i - since the string can only contain
     * lowercase English letters, we can use an array of size 26 to store it.
     * Therefore, the number of subsequences will be num(1) - num(2) + 1 (plus one because either we can append the
     * character to the last subsequence ends with the same character, or this character never appeared before and the
     * single character itself is a subsequence).
     * <p>
     * Time: O(n) we use an array to keep track of the information needed, hence no need to traverse all characters before i
     * Space: O(1) only need an array of size 26
     */
    public int distinctSubseqIIDP2(String s) {
        final int MOD = 1_000_000_007;
        int[] numOfDistinctSubseqEndsWithChar = new int[26];
        int total = 0;

        for (int i = 0; i < s.length(); i++) {
            int index = s.charAt(i) - 'a';
            // compute the current number of subsequences ends with s.charAt(i)
            // since we need take the modulo on the fly
            // we need to add MOD when we subtract something to avoid negative numbers
            int curr = (1 + total - numOfDistinctSubseqEndsWithChar[index] + MOD) % MOD;
            // update the number of subsequences ends with the current character
            numOfDistinctSubseqEndsWithChar[index] = (numOfDistinctSubseqEndsWithChar[index] + curr) % MOD;
            // update the total subsequences too
            total = (total + curr) % MOD;
        }
        return total;
    }

    @Test
    public void distinctSubseqIITest() {
        /**
         * Example 1:
         * Input: s = "abc"
         * Output: 7
         * Explanation: The 7 distinct subsequences are "a", "b", "c", "ab", "ac", "bc", and "abc".
         */
        assertEquals(7, distinctSubseqIIDP1("abc"));
        assertEquals(7, distinctSubseqIIDP2("abc"));
        /**
         * Example 2:
         * Input: s = "aba"
         * Output: 6
         * Explanation: The 6 distinct subsequences are "a", "b", "ab", "aa", "ba", and "aba".
         */
        assertEquals(6, distinctSubseqIIDP1("aba"));
        assertEquals(6, distinctSubseqIIDP2("aba"));
        /**
         * Example 3:
         * Input: s = "aaa"
         * Output: 3
         * Explanation: The 3 distinct subsequences are "a", "aa" and "aaa".
         */
        assertEquals(3, distinctSubseqIIDP1("aaa"));
        assertEquals(3, distinctSubseqIIDP2("aaa"));
    }
}
