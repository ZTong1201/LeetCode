import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class longestSubstring {

    /**
     * Given a string, find the length of the longest substring without repeating characters.
     * <p>
     * Brute force: we can iterate through all substrings to get the maximum length of substring without repeating characters.
     * <p>
     * Time: O(N^3) for a given range [i,j) to check whether there is no duplicates cost linear time O(j - i),
     * for a given index i, j is in range [i + 1, n], the sum of time is sum from i + 1 to n of O(j - i), which is
     * O((1 + n - i) * (n - i) / 2)
     * for all of the indexes i is in range [0, n - 1], hence the total sum is sum from 0 to n - 1 of O((1 + n - i) * (n - i) / 2)
     * which is around O(N^3)
     * Space: O(min(n, m)), we need a set to check whether a substring has duplicates, which is upper bounded by the size of string s
     * or the number of unique characters in the string.
     */
    public int lengthOfLongestSubstringBruteForce(String s) {
        int res = 0;
        for (int i = 0; i < s.length(); i++) {
            for (int j = i + 1; j <= s.length(); j++) {
                if (noDuplicates(s, i, j)) res = Math.max(res, j - i);
            }
        }
        return res;
    }

    private boolean noDuplicates(String s, int start, int end) {
        Set<Character> charSet = new HashSet<>();
        for (int i = start; i < end; i++) {
            if (charSet.contains(s.charAt(i))) return false;
            charSet.add(s.charAt(i));
        }
        return true;
    }

    /**
     * Sliding Window: A sliding window is an abstract concept commonly used in array/string problems.
     * A window is a range of elements in the array/string which usually defined by the start and end indices, i.e. [i, j)
     * (left-closed, right-open). A sliding window is a window "slides" its two boundaries to the certain direction.
     * For example, if we slide [i, j)[i,j) to the right by 11 element, then it becomes [i+1, j+1)[i+1,j+1) (left-closed, right-open).
     * <p>
     * We use hashSet to store characters in current window [i, j) (i == j initially), if s[j] not in hashSet, we slide j further.
     * Doing so until we found a duplicate. We now find the maximum length of substring starting from index i. We do this for all i,
     * then we get the answer.
     * <p>
     * Time: O(2n) = O(n), in the worst case, we visited each character twice (i and j)
     * Space: O(min(n, m)), we need a set to check whether a substring has duplicates, which is upper bounded by the size of string s
     * or the number of unique characters in the string.
     */
    public int lengthOfLongestSubstringSlidingWindow(String s) {
        boolean[] seen = new boolean[256];
        char[] chars = s.toCharArray();
        int res = 0, slow = 0, fast = 0;

        // if the window is sliding out of the array - then done
        while (fast < chars.length) {
            // if the current character doesn't exist in the substring
            if (!seen[chars[fast]]) {
                // update the longest substring
                res = Math.max(res, fast - slow + 1);
                // put that character into the hash table
                seen[chars[fast]] = true;
                // expand the window
                fast++;
            } else {
                // otherwise, we have a repeating character
                // pushing that character out of the hash table
                seen[chars[slow]] = false;
                // shrink the window by sliding the left bound
                slow++;
            }
        }
        return res;
    }

    /**
     * Sliding window with memorization:
     * As discussed before, in the worst case, we will visit each character twice.
     * We don't really need to slide i one step at a time. If s[j] is a duplicate in slide window [i, j) with index j',
     * We could simply slide the start point to j' + 1
     * <p>
     * Time: O(n), pointer j will iterate over each character
     * Space: O(min(m, n)) we need a set to check whether a substring has duplicates, which is upper bounded by the size of string s
     * or the number of unique characters in the string.
     * <p>
     * For memorization, if we actually know the size of charSet, we can use an array to store the index information
     * Commonly used tables are:
     * <p>
     * int[26] for Letters 'a' - 'z' or 'A' - 'Z'
     * int[128] for ASCII
     * int[256] for Extended ASCII
     * <p>
     * By doing this, we are actually using a constant space. O(1)
     */
    public int lengthOfLongestSubstringSlidingWindowWithMemorzation(String s) {
        int[] index = new int[128];
        int res = 0;
        for (int i = 0, j = 0; j < s.length(); j++) {
            i = Math.max(i, index[s.charAt(j)]);
            res = Math.max(res, j - i + 1);
            index[s.charAt(j)] = j + 1;
        }
        return res;
    }

    @Test
    public void lengthOfLongestSubstringSlidingWindowWithMemorizationTest() {
        /**
         * Example 1:
         * Input: "abcabcbb"
         * Output: 3
         */
        String test1 = "abcabcbb";
        assertEquals(3, lengthOfLongestSubstringSlidingWindowWithMemorzation(test1));
        /**
         * Example 2:
         * Input: "bbbbb"
         * Output: 1
         */
        String test2 = "bbbbb";
        assertEquals(1, lengthOfLongestSubstringSlidingWindowWithMemorzation(test2));
        /**
         * Example 3:
         * Input: "pwwkew"
         * Output: 3
         */
        String test3 = "pwwkew";
        assertEquals(3, lengthOfLongestSubstringSlidingWindowWithMemorzation(test3));
        /**
         * Example 4:
         * Input: "dvdf"
         * Output: 3
         */
        String test4 = "dvdf";
        assertEquals(3, lengthOfLongestSubstringSlidingWindowWithMemorzation(test4));

    }

    @Test
    public void lengthOfLongestSubstringSlidingWindowTest() {
        /**
         * Example 1:
         * Input: "abcabcbb"
         * Output: 3
         */
        String test1 = "abcabcbb";
        assertEquals(3, lengthOfLongestSubstringSlidingWindow(test1));
        /**
         * Example 2:
         * Input: "bbbbb"
         * Output: 1
         */
        String test2 = "bbbbb";
        assertEquals(1, lengthOfLongestSubstringSlidingWindow(test2));
        /**
         * Example 3:
         * Input: "pwwkew"
         * Output: 3
         */
        String test3 = "pwwkew";
        assertEquals(3, lengthOfLongestSubstringSlidingWindow(test3));
        /**
         * Example 4:
         * Input: "dvdf"
         * Output: 3
         */
        String test4 = "dvdf";
        assertEquals(3, lengthOfLongestSubstringSlidingWindow(test4));

    }

    @Test
    public void lengthOfLongestSubstringBruteForceTest() {
        /**
         * Example 1:
         * Input: "abcabcbb"
         * Output: 3
         */
        String test1 = "abcabcbb";
        assertEquals(3, lengthOfLongestSubstringBruteForce(test1));
        /**
         * Example 2:
         * Input: "bbbbb"
         * Output: 1
         */
        String test2 = "bbbbb";
        assertEquals(1, lengthOfLongestSubstringBruteForce(test2));
        /**
         * Example 3:
         * Input: "pwwkew"
         * Output: 3
         */
        String test3 = "pwwkew";
        assertEquals(3, lengthOfLongestSubstringBruteForce(test3));
        /**
         * Example 4:
         * Input: "dvdf"
         * Output: 3
         */
        String test4 = "dvdf";
        assertEquals(3, lengthOfLongestSubstringBruteForce(test4));

    }
}
