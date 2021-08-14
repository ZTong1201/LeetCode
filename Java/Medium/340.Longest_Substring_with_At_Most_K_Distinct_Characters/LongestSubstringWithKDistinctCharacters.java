import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LongestSubstringWithKDistinctCharacters {

    /**
     * Given a string s and an integer k, return the length of the longest substring of s that contains at most k
     * distinct characters.
     * <p>
     * Constraints:
     * <p>
     * 1 <= s.length <= 5 * 10^4
     * 0 <= k <= 50
     * <p>
     * Approach: Sliding Window
     * Maintain a sliding window - keep expanding the window if the current substring has less than K distinct characters,
     * and update the longest length if applicable. Otherwise, popping existing characters from the left bound of the window
     * and shrink the size until we fulfill the requirement again.
     * <p>
     * Time: O(n)
     * Space: O(1) always need an integer array of size 256
     */
    public int lengthOfLongestSubstringKDistinct(String s, int k) {
        char[] chars = s.toCharArray();
        // use an integer array to keep the count of each character
        int[] count = new int[256];
        int res = 0, slow = 0;

        for (int fast = 0; fast < chars.length; fast++) {
            // decrement k if meeting a new character
            if (count[chars[fast]] == 0) k--;
            // always increment the count for that character
            count[chars[fast]]++;

            // pushing out existing character and shrink the window size from the left bound (slow pointer)
            // if the k is less than 0
            while (k < 0) {
                // pushing out one character
                count[chars[slow]]--;
                // see whether all of that characters have been pushed out
                if (count[chars[slow]] == 0) k++;
                // shrink the window size
                slow++;
            }

            // update the longest length
            res = Math.max(res, fast - slow + 1);
        }
        return res;
    }

    @Test
    public void lengthOfLongestSubstringKDistinctTest() {
        /**
         * Example 1:
         * Input: s = "eceba", k = 2
         * Output: 3
         * Explanation: The substring is "ece" with length 3.
         */
        assertEquals(3, lengthOfLongestSubstringKDistinct("eceba", 2));
        /**
         * Example 2:
         * Input: s = "aa", k = 1
         * Output: 2
         * Explanation: The substring is "aa" with length 2.
         */
        assertEquals(2, lengthOfLongestSubstringKDistinct("aa", 1));
    }
}
