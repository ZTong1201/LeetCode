import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumberOfUniqueGoodSubsequences {

    /**
     * You are given a binary string binary. A subsequence of binary is considered good if it is not empty and has no leading
     * zeros (with the exception of "0").
     * <p>
     * Find the number of unique good subsequences of binary.
     * <p>
     * For example, if binary = "001", then all the good subsequences are ["0", "0", "1"], so the unique good subsequences
     * are "0" and "1". Note that subsequences "00", "01", and "001" are not good because they have leading zeros.
     * Return the number of unique good subsequences of binary. Since the answer may be very large, return it modulo 10^9 + 7.
     * <p>
     * A subsequence is a sequence that can be derived from another sequence by deleting some or no elements without changing
     * the order of the remaining elements.
     * <p>
     * Constraints:
     * <p>
     * 1 <= binary.length <= 10^5
     * binary consists of only '0's and '1's.
     * <p>
     * Approach: DP
     * Notice the recurrence relations that, if we already know the number of unique subsequences ending with 0 and 1,
     * denote them as end0 and end1.
     * If the current character is '1', then we can always append a '1' after those subsequences i.e. we will have
     * end0 + end1 new unique subsequences endings with 1 now, and keep in mind that '1' itself is also a valid subsequence,
     * hence we have (end0 + end1 + 1).
     * If the character is '0', this is tricky. Similarly, we still can always append a '0' behind end0 and end1 to form
     * new subsequences. However, even though '0' itself is a valid subsequence, we don't want to carry it over again and again
     * in case we have consecutive zeros. For instance, if previously we have "10" which ends with 0, and if we can count "0"
     * itself as a unique subsequence, then we have "10" and "0", then if the next character is still "0", we will end up
     * having "00" which is not a valid subsequence anymore. Therefore, we will not count the single '0' subsequence for now.
     * We could record whether 0 ever exists in the input string, and add one to the final result if it exists. To avoid
     * overflow, we will take modulo on the fly as well.
     * <p>
     * Time: O(n) only need to go through the entire string once
     * Space: O(1)
     */
    public int numberOfUniqueGoodSubsequences(String binary) {
        final int MOD = 1_000_000_007;
        int endsWithZero = 0, endsWithOne = 0, hasZero = 0;
        for (int i = 0; i < binary.length(); i++) {
            char curr = binary.charAt(i);

            if (curr == '1') {
                // need add an extra one since a single '1' character is considered as a good subsequence
                // and can be carried over
                endsWithOne = (endsWithZero + endsWithOne + 1) % MOD;
            } else {
                // not carrying single '0' character to avoid consecutive zeros, e.g. "000"
                endsWithZero = (endsWithZero + endsWithOne) % MOD;
                // but need to know whether 0 ever exists
                hasZero = 1;
            }
        }
        return (endsWithOne + endsWithZero + hasZero) % MOD;
    }

    @Test
    public void numberOfUniqueGoodSubsequencesTest() {
        /**
         * Example 1:
         * Input: binary = "001"
         * Output: 2
         * Explanation: The good subsequences of binary are ["0", "0", "1"].
         * The unique good subsequences are "0" and "1".
         */
        assertEquals(2, numberOfUniqueGoodSubsequences("001"));
        /**
         * Example 2:
         * Input: binary = "11"
         * Output: 2
         * Explanation: The good subsequences of binary are ["1", "1", "11"].
         * The unique good subsequences are "1" and "11".
         */
        assertEquals(2, numberOfUniqueGoodSubsequences("11"));
        /**
         * Example 3:
         * Input: binary = "101"
         * Output: 5
         * Explanation: The good subsequences of binary are ["1", "0", "1", "10", "11", "101"].
         * The unique good subsequences are "0", "1", "10", "11", and "101".
         */
        assertEquals(5, numberOfUniqueGoodSubsequences("101"));
    }
}
