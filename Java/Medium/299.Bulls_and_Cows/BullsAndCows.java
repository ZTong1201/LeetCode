import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BullsAndCows {

    /**
     * You are playing the Bulls and Cows game with your friend.
     * <p>
     * You write down a secret number and ask your friend to guess what the number is. When your friend makes a guess,
     * you provide a hint with the following info:
     * <p>
     * The number of "bulls", which are digits in the guess that are in the correct position.
     * The number of "cows", which are digits in the guess that are in your secret number but are located in the wrong
     * position. Specifically, the non-bull digits in the guess that could be rearranged such that they become bulls.
     * Given the secret number secret and your friend's guess guess, return the hint for your friend's guess.
     * <p>
     * The hint should be formatted as "xAyB", where x is the number of bulls and y is the number of cows. Note that both
     * secret and guess may contain duplicate digits.
     * <p>
     * Constraints:
     * <p>
     * 1 <= secret.length, guess.length <= 1000
     * secret.length == guess.length
     * secret and guess consist of digits only.
     * <p>
     * Approach 1: Two pass
     * First pass: find all bulls + record character frequencies in both strings when there is a mismatch
     * Second pass: loop over all 10 possible (0-9) digits, the number of cows will be the minimum frequency
     * <p>
     * Time: O(n)
     * Space: O(1) constant space is used
     */
    public String getHintTwoPass(String secret, String guess) {
        int bulls = 0, cows = 0;
        int[] charFrequencyInSecret = new int[10];
        int[] charFrequencyInGuess = new int[10];

        // first pass - find bulls and record frequency
        for (int i = 0; i < secret.length(); i++) {
            if (guess.charAt(i) == secret.charAt(i)) {
                bulls++;
            } else {
                // record the frequency of mismatched character in both hash tables
                charFrequencyInGuess[guess.charAt(i) - '0']++;
                charFrequencyInSecret[secret.charAt(i) - '0']++;
            }
        }

        // second pass - find cows
        for (int i = 0; i < 10; i++) {
            cows += Math.min(charFrequencyInGuess[i], charFrequencyInSecret[i]);
        }
        StringBuilder res = new StringBuilder();
        return res.append(bulls).append('A').append(cows).append('B').toString();
    }

    /**
     * Approach 2: One pass
     * We can build the map on the fly and compute the number of bulls and cows at the same time. For bulls, the logic
     * will remain the same - when there is an exact match at index i, increment bulls and not updating any frequencies.
     * For cows, we need to check the number of frequencies of mismatched characters in both strings to see whether a cow
     * needs to be incremented.
     * Specifically, the characters (s) in the secret string will have a positive affect while characters (g) in the guess
     * string has a negative effect, i.e. if s != g, freq(s) += 1 and freq(g) -= 1. Note the freq(c) means the number of
     * frequency difference in both strings. If freq(s) < 0, means there are more characters s in guess string than in secret
     * string so far, then there must have been a cow. Similarly, if freq(g) > 0, means there are more characters g in
     * the secret string, since it's not a bull, then there must have been a cow too.
     * <p>
     * Time: O(n)
     * Space: O(1)
     */
    public String getHintOnePass(String secret, String guess) {
        int[] mismatchedCharFrequency = new int[10];
        int bulls = 0, cows = 0;

        for (int i = 0; i < secret.length(); i++) {
            char charInSecret = secret.charAt(i);
            char charInGuess = guess.charAt(i);

            if (charInSecret == charInGuess) {
                bulls++;
            } else {
                // if the number of charInSecret is larger in the guess string
                // then there is a cow
                if (mismatchedCharFrequency[charInSecret - '0'] < 0) {
                    cows++;
                }
                // or if the number of charInGuess is larger in the secret string
                // there is a cow as well
                if (mismatchedCharFrequency[charInGuess - '0'] > 0) {
                    cows++;
                }
                // character in secret will have a positive impact
                mismatchedCharFrequency[charInSecret - '0']++;
                // meanwhile, character in guess will have a negative impact
                mismatchedCharFrequency[charInGuess - '0']--;
            }
        }
        StringBuilder res = new StringBuilder();
        return res.append(bulls).append('A').append(cows).append('B').toString();
    }

    @Test
    public void getHintTest() {
        /**
         * Example 1:
         * Input: secret = "1807", guess = "7810"
         * Output: "1A3B"
         * Explanation: Bulls are connected with a '|' and cows are underlined:
         * "1807"
         *   |
         * "7810"
         */
        assertEquals("1A3B", getHintTwoPass("1807", "7810"));
        assertEquals("1A3B", getHintOnePass("1807", "7810"));
        /**
         * Example 2:
         * Input: secret = "1123", guess = "0111"
         * Output: "1A1B"
         * Explanation: Bulls are connected with a '|' and cows are underlined:
         * "1123"        "1123"
         *   |      or     |
         * "0111"        "0111"
         * Note that only one of the two unmatched 1s is counted as a cow since the non-bull digits can only be rearranged
         * to allow one 1 to be a bull.
         */
        assertEquals("1A1B", getHintTwoPass("1123", "0111"));
        assertEquals("1A1B", getHintOnePass("1123", "0111"));
        /**
         * Example 3:
         * Input: secret = "1", guess = "0"
         * Output: "0A0B"
         */
        assertEquals("0A0B", getHintTwoPass("1", "0"));
        assertEquals("0A0B", getHintOnePass("1", "0"));
        /**
         * Example 4:
         * Input: secret = "1", guess = "1"
         * Output: "1A0B"
         */
        assertEquals("1A0B", getHintTwoPass("1", "1"));
        assertEquals("1A0B", getHintOnePass("1", "1"));
    }
}
