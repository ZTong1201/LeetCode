import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class NumberOfMatchingSubsequences {

    /**
     * Given a string s and an array of strings words, return the number of words[i] that is a subsequence of s.
     * <p>
     * A subsequence of a string is a new string generated from the original string with some characters (can be none)
     * deleted without changing the relative order of the remaining characters.
     * <p>
     * For example, "ace" is a subsequence of "abcde".
     * <p>
     * Constraints:
     * <p>
     * 1 <= s.length <= 5 * 10^4
     * 1 <= words.length <= 5000
     * 1 <= words[i].length <= 50
     * s and words[i] consist of only lowercase English letters.
     * <p>
     * Approach 1: Brute Force.
     * For a given word, directly check whether it is a subsequence of string s. Also add these subsequences to a hash set
     * to avoid reiterate over the original string.
     * <p>
     * Time: since words[i].length <= 50, the subsequence check will be eventually dominated by the length of input string,
     * which is O(S), the total runtime is O(S * L)
     * Space: O(L) we need a hash set to record true subsequence
     */
    public int numMatchingSubseqBruteForce(String s, String[] words) {
        int count = 0;
        Set<String> subsequences = new HashSet<>();
        for (String word : words) {
            if (subsequences.contains(word)) count++;
            else if (isSubsequence(s, word)) {
                // add true subsequence into a hash set to avoid reiteration
                subsequences.add(word);
                count++;
            }
        }
        return count;
    }

    private boolean isSubsequence(String s, String word) {
        int startIndex = 0;
        for (int i = 0; i < word.length(); i++) {
            // check character i appears in the string after start index
            int occurrenceIndex = s.indexOf(word.charAt(i), startIndex);
            if (occurrenceIndex == -1) return false;
            startIndex = occurrenceIndex + 1;
        }
        return true;
    }

    /**
     * Approach 2: Next Letter Pointers
     * To better improve performance, we would like to iterate the entire string only once instead of looping through the entire
     * string again and again. Essentially, if we're at a specific character 'c' at the given string, there are certain words
     * in the words array might be the candidate subsequence. For example if we have "cat", "cog", but "dog" cannot form
     * a subsequence (at least as of now). Hence, for "cat" and "cog", the next target would be "at" and "og", i.e.
     * we can have a map "c" : ["at", "og"]. If we reach an 'a' moving forward, then the bucket will be updated accordingly.
     * Therefore, we can iterate over the words array first to get an initial state and proactively regroup each bucket while
     * iterating over the string s. If by any time all characters are found in s, then that word is a valid subsequence.
     * Note that taking substrings is relatively time-consuming, considering to create a new class to store which index the
     * word is currently at.
     * <p>
     * Time: O(S + L) we need to traverse the words array and string s once
     * Space: O(L)
     */
    public int numMatchingSubseqNextLetterPointer(String s, String[] words) {
        int count = 0;
        // since word and s only contain lower case English letter
        // use an array of size 26 as a hash map
        ArrayList<IndexAtWord>[] nextLetterMap = new ArrayList[26];
        for (int i = 0; i < 26; i++) {
            nextLetterMap[i] = new ArrayList<>();
        }
        // initialize the map
        for (String word : words) {
            nextLetterMap[word.charAt(0) - 'a'].add(new IndexAtWord(word, 0));
        }

        // iterate over the string to regroup each bucket and find real subsequence
        for (char c : s.toCharArray()) {
            // get candidates from the old bucket
            ArrayList<IndexAtWord> oldBucket = nextLetterMap[c - 'a'];
            // the bucket will be regrouped later
            nextLetterMap[c - 'a'] = new ArrayList<>();

            // for each word in the bucket - find its next letter and assign it to the correct bucket
            for (IndexAtWord currWord : oldBucket) {
                // jump to next index in the word
                currWord.index++;
                // if all characters have been found - this is a real subsequence
                if (currWord.index == currWord.word.length()) count++;
                    // otherwise, add it to a new bucket
                else nextLetterMap[currWord.word.charAt(currWord.index) - 'a'].add(currWord);
            }
        }
        return count;
    }

    private static class IndexAtWord {
        String word;
        int index;

        public IndexAtWord(String word, int currIndex) {
            this.word = word;
            this.index = currIndex;
        }
    }

    @Test
    public void numMatchingSubseqTest() {
        /**
         * Example 1:
         * Input: s = "abcde", words = ["a","bb","acd","ace"]
         * Output: 3
         * Explanation: There are three strings in words that are a subsequence of s: "a", "acd", "ace".
         */
        assertEquals(3, numMatchingSubseqBruteForce("abcde", new String[]{"a", "bb", "acd", "ace"}));
        assertEquals(3, numMatchingSubseqNextLetterPointer("abcde", new String[]{"a", "bb", "acd", "ace"}));
        /**
         * Example 2:
         * Input: s = "dsahjpjauf", words = ["ahjpjau","ja","ahbwzgqnuk","tnmlanowax"]
         * Output: 2
         */
        assertEquals(2, numMatchingSubseqBruteForce("dsahjpjauf", new String[]{"ahjpjau", "ja", "ahbwzgqnuk", "tnmlanowax"}));
        assertEquals(2, numMatchingSubseqNextLetterPointer("dsahjpjauf", new String[]{"ahjpjau", "ja", "ahbwzgqnuk", "tnmlanowax"}));
    }
}
