import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LongestSubstringWithAtLeastKRepeatingChars {

    /**
     * Given a string s and an integer k, return the length of the longest substring of s such that the frequency of
     * each character in this substring is greater than or equal to k.
     * <p>
     * Constraints:
     * <p>
     * 1 <= s.length <= 10^4
     * s consists of only lowercase English letters.
     * 1 <= k <= 10^5
     * <p>
     * Approach 1: Divide And Conquer
     * Using divide and conquer we basically split the problem into two sub-problems at each iteration. In this specific problem,
     * longest(s, start, end) = max(longest(s, start, mid), longest(s, mid + 1, end))
     * The tricky part is to find the split point. We can know that the if the number of occurrences of a given character is
     * less than k in the whole string, then it's not possible it will be part of the longest substring. Therefore, for a given
     * string s at one iteration, we could traverse from start to end and discard the characters whose frequency is smaller
     * than k and recursively find the maximum in both half. If we couldn't find a split point, which means all the characters
     * appear at least K times in the whole string, we return (end - start).
     * <p>
     * Time: O(n^2) in the worst case, since at each given character we might need to traverse the entire string to find the split
     * point, and return the length of that string if such split point doesn't exist
     * Space: O(n)
     */
    public int longestSubstringDivideAndConquer(String s, int k) {
        return helper(s, 0, s.length(), k);
    }

    private int helper(String s, int start, int end, int k) {
        // pruning - if the substring from the beginning is already less than k
        // then the substring the problem is looking doesn't exist
        if (end < k) return 0;
        // count the occurrences of each character
        int[] count = new int[26];
        for (int i = start; i < end; i++) {
            count[s.charAt(i) - 'a']++;
        }

        // try to find the split point
        // find the first character whose frequency is less than k
        for (int mid = start; mid < end; mid++) {
            if (count[s.charAt(mid) - 'a'] >= k) continue;
            int midNext = mid + 1;
            // keep skipping more characters if its frequency is less than k as well
            // to find the start point of the second half
            while (midNext < end && count[s.charAt(midNext) - 'a'] < k) midNext++;
            // return the maximum from the left and right half
            return Math.max(helper(s, start, mid, k), helper(s, midNext, end, k));
        }
        // if the split point doesn't exist, return the length of current string
        return (end - start);
    }

    /**
     * Approach 2: Sliding Window
     * The sliding window setup is sort of tricky to implement. Since there are only lowercase letters in the string, which means
     * we have at most 26 unique characters. Hence, we can first traverse the string to get the number of unique characters. Then
     * we enumerate from 1 to the number of unique characters and find the maximum length of substring contains that specific
     * number of characters.
     * <p>
     * Time: O(N), actually it should be O(N * maxUnique) since we need to go through the entire string for each possible
     * unique number. However, since the string only contains lowercase letters, the maximum number of maxUnique is 26. Hence
     * O(26 * N) = O(N)
     * Space: O(1) constant space with size 26 int array to keep track of the occurrence
     */
    public int longestSubstringSlidingWindow(String s, int k) {
        char[] chars = s.toCharArray();
        int maxUnique = getMaxUniqueLetters(chars);
        int res = 0;

        // find maximum length of substring for each unique number of characters
        for (int currUnique = 1; currUnique <= maxUnique; currUnique++) {
            int[] count = new int[26];
            // keep track of number of unique characters
            // and the number of characters whose frequency is at least K in the current substring
            int countAtLeastK = 0, unique = 0;
            // two pointers for the sliding window
            int slow = 0, fast = 0;

            while (fast < chars.length) {
                // expand the window if the number of unique characters is not desired
                if (unique <= currUnique) {
                    char curr = chars[fast];
                    if (count[curr - 'a'] == 0) unique++;
                    count[curr - 'a']++;
                    if (count[curr - 'a'] == k) countAtLeastK++;
                    // expanding
                    fast++;
                } else {
                    // otherwise, shrink the window
                    char curr = chars[slow];
                    if (count[curr - 'a'] == k) countAtLeastK--;
                    count[curr - 'a']--;
                    if (count[curr - 'a'] == 0) unique--;
                    // shrinking
                    slow++;
                }
                if (unique == currUnique && unique == countAtLeastK) {
                    // update the maximum length
                    res = Math.max(fast - slow, res);
                }
            }
        }
        return res;
    }

    private int getMaxUniqueLetters(char[] chars) {
        int[] map = new int[26];
        int count = 0;
        for (char c : chars) {
            if (map[c - 'a'] == 0) count++;
            map[c - 'a']++;
        }
        return count;
    }

    @Test
    public void longestSubstringTest() {
        /**
         * Example 1:
         * Input: s = "aaabb", k = 3
         * Output: 3
         * Explanation: The longest substring is "aaa", as 'a' is repeated 3 times.
         */
        assertEquals(3, longestSubstringDivideAndConquer("aaabb", 3));
        assertEquals(3, longestSubstringSlidingWindow("aaabb", 3));
        /**
         * Example 2:
         * Input: s = "ababbc", k = 2
         * Output: 5
         * Explanation: The longest substring is "ababb", as 'a' is repeated 2 times and 'b' is repeated 3 times.
         */
        assertEquals(5, longestSubstringDivideAndConquer("ababbc", 2));
        assertEquals(5, longestSubstringSlidingWindow("ababbc", 2));
    }
}
