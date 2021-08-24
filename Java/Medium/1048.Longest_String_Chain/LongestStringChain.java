import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class LongestStringChain {

    /**
     * You are given an array of words where each word consists of lowercase English letters.
     * <p>
     * wordA is a predecessor of wordB if and only if we can insert exactly one letter anywhere in wordA without changing
     * the order of the other characters to make it equal to wordB.
     * <p>
     * For example, "abc" is a predecessor of "abac", while "cba" is not a predecessor of "bcad".
     * A word chain is a sequence of words [word1, word2, ..., wordk] with k >= 1, where word1 is a predecessor of word2,
     * word2 is a predecessor of word3, and so on. A single word is trivially a word chain with k == 1.
     * <p>
     * Return the length of the longest possible word chain with words chosen from the given list of words.
     * <p>
     * Constraints:
     * <p>
     * 1 <= words.length <= 1000
     * 1 <= words[i].length <= 16
     * words[i] only consists of lowercase English letters.
     * <p>
     * Approach: Bottom up DP
     * We can compute the longest sequence in the reverse way by deleting one character from a longer word string, then the
     * longest sequence word2 (deleting one character from word) can make would be max(length(word2), length(word) + 1).
     * Since each word is trivially a word chain with k == 1, we can initialize every string with length 1. In addition,
     * this algorithm only works if we traverse the word array in reverse length order. However, sorting string is based
     * on lexicographical order by default, we need a new comparator to sort by string length.
     * <p>
     * Time: O(NlogN + N * L^2) We need to sort the string first, which takes O(NlogN) time, and for each string, we need
     * to enumerate all possible options by deleting one character from the string, we have L characters to be deleted for
     * a given string. After the character is deleted, we need O(L - 1) time to build the new string. Hence, the runtime for
     * DP is O(N * L^2)
     * Space: O(N)
     */
    public int longestStrChain(String[] words) {
        Comparator<String> comparator = (String a, String b) -> {
            return a.length() - b.length();
        };
        // sort strings by length
        Arrays.sort(words, comparator);
        // string -> length of the longest string chain constructed in the reverse way
        Map<String, Integer> map = new HashMap<>();
        for (String word : words) {
            // since each word is trivially a string chain with length 1
            map.put(word, 1);
        }

        int res = 1;
        // find the longest length in the reverse order
        for (int i = words.length - 1; i >= 0; i--) {
            String word = words[i];
            // construct new strings by deleting one character
            for (int j = 0; j < word.length(); j++) {
                // always delete character at index j
                String newString = word.substring(0, j) + word.substring(j + 1);
                // if the new string exists in the array
                // which means we might be able to form a longer string chain
                if (map.containsKey(newString)) {
                    // update the longest string chain we can build for this specific word
                    map.put(newString, Math.max(map.get(newString), map.get(word) + 1));
                    // update the global maximum value
                    res = Math.max(res, map.get(newString));
                }
            }
        }
        return res;
    }

    @Test
    public void longestStrChainTest() {
        /**
         * Example 1:
         * Input: words = ["a","b","ba","bca","bda","bdca"]
         * Output: 4
         * Explanation: One of the longest word chains is ["a","ba","bda","bdca"].
         */
        assertEquals(4, longestStrChain(new String[]{"a", "b", "ba", "bca", "bda", "bdca"}));
        /**
         * Example 2:
         * Input: words = ["xbc","pcxbcf","xb","cxbc","pcxbc"]
         * Output: 5
         * Explanation: All the words can be put in a word chain ["xb", "xbc", "cxbc", "pcxbc", "pcxbcf"].
         */
        assertEquals(5, longestStrChain(new String[]{"xb", "xbc", "cxbc", "pcxbc", "pcxbcf"}));
        /**
         * Example 3:
         * Input: words = ["abcd","dbqca"]
         * Output: 1
         * Explanation: The trivial word chain ["abcd"] is one of the longest word chains.
         * ["abcd","dbqca"] is not a valid word chain because the ordering of the letters is changed.
         */
        assertEquals(1, longestStrChain(new String[]{"abcd", "dbqca"}));
    }
}
