import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MaximumNumberOfBalloons {

    /**
     * Given a string text, you want to use the characters of text to form as many instances of the word "balloon" as possible.
     * <p>
     * You can use each character in text at most once. Return the maximum number of instances that can be formed.
     * <p>
     * Constraints:
     * <p>
     * 1 <= text.length <= 10^4
     * text consists of lower case English letters only.
     * <p>
     * Approach: Generalized Solution
     * We can use an array of size 26 as a frequency map, count the frequency of desired pattern, and compute the frequency for
     * each character on the fly. Finally, take the minimum number of pattern made of each character.
     * <p>
     * Time: O(M + N)
     * Space: O(1)
     */
    public int maxNumberOfBalloons(String text) {
        return findMaxOfPattern("balloon", text);
    }

    private int findMaxOfPattern(String pattern, String text) {
        int[] frequencyInPattern = new int[26], frequencyInText = new int[26];
        for (int i = 0; i < pattern.length(); i++) {
            frequencyInPattern[pattern.charAt(i) - 'a']++;
        }
        for (int i = 0; i < text.length(); i++) {
            frequencyInText[text.charAt(i) - 'a']++;
        }

        int res = Integer.MAX_VALUE;
        for (int i = 0; i < 26; i++) {
            if (frequencyInPattern[i] > 0) {
                res = Math.min(res, frequencyInText[i] / frequencyInPattern[i]);
            }
        }
        return res;
    }

    @Test
    public void maxNumberOfBalloonsTest() {
        /**
         * Example 1:
         * Input: text = "nlaebolko"
         * Output: 1
         */
        assertEquals(1, maxNumberOfBalloons("nlaebolko"));
        /**
         * Example 2:
         * Input: text = "loonbalxballpoon"
         * Output: 2
         */
        assertEquals(2, maxNumberOfBalloons("loonbalxballpoon"));
        /**
         * Example 3:
         * Input: text = "leetcode"
         * Output: 0
         */
        assertEquals(0, maxNumberOfBalloons("leetcode"));
        /**
         * Example 4:
         * Input: text = "ballon"
         * Output: 0
         */
        assertEquals(0, maxNumberOfBalloons("ballon"));
    }
}
