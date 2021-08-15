import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WordFilterSuffixWrappedWordsTest {

    @Test
    public void wordFilterSuffixWrappedWordsTest1() {
        /**
         * Example 1:
         * Input:
         * ["WordFilter", "f"]
         * [[["apple"]], ["a", "e"]]
         * Output:
         * [null, 0]
         *
         * Explanation
         * WordFilter wordFilter = new WordFilter(["apple"]);
         * wordFilter.f("a", "e"); // return 0, because the word at index 0 has prefix = "a" and suffix = 'e".
         */
        WordFilterSuffixWrappedWords wordFilterSuffixWrappedWords = new WordFilterSuffixWrappedWords(new String[]{"apple"});
        assertEquals(0, wordFilterSuffixWrappedWords.f("a", "e"));
    }

    @Test
    public void wordFilterSuffixWrappedWordsTest2() {
        /**
         * Example 2:
         * Input:
         * ["WordFilter","f","f","f","f","f","f","f","f","f","f"]
         * [[["cabaabaaaa","ccbcababac","bacaabccba","bcbbcbacaa","abcaccbcaa","accabaccaa","cabcbbbcca","ababccabcb",
         * "caccbbcbab","bccbacbcba"]],["bccbacbcba","a"],["ab","abcaccbcaa"],["a","aa"],["cabaaba","abaaaa"],
         * ["cacc","accbbcbab"],["ccbcab","bac"],["bac","cba"],["ac","accabaccaa"],["bcbb","aa"],["ccbca","cbcababac"]]
         * Output:
         * [null,9,4,5,0,8,1,2,5,3,1]
         */
        WordFilterSuffixWrappedWords wordFilterSuffixWrappedWords = new WordFilterSuffixWrappedWords(new String[]{"cabaabaaaa", "ccbcababac", "bacaabccba", "bcbbcbacaa",
                "abcaccbcaa", "accabaccaa", "cabcbbbcca", "ababccabcb", "caccbbcbab", "bccbacbcba"});
        assertEquals(9, wordFilterSuffixWrappedWords.f("bccbacbcba", "a"));
        assertEquals(4, wordFilterSuffixWrappedWords.f("ab", "abcaccbcaa"));
        assertEquals(5, wordFilterSuffixWrappedWords.f("a", "aa"));
        assertEquals(0, wordFilterSuffixWrappedWords.f("cabaaba", "abaaaa"));
        assertEquals(8, wordFilterSuffixWrappedWords.f("cacc", "accbbcbab"));
        assertEquals(1, wordFilterSuffixWrappedWords.f("ccbcab", "bac"));
        assertEquals(2, wordFilterSuffixWrappedWords.f("bac", "cba"));
        assertEquals(5, wordFilterSuffixWrappedWords.f("ac", "accabaccaa"));
        assertEquals(3, wordFilterSuffixWrappedWords.f("bcbb", "aa"));
        assertEquals(1, wordFilterSuffixWrappedWords.f("ccbca", "cbcababac"));
    }

    @Test
    public void wordFilterSuffixWrappedWordsTest3() {
        /**
         * Example 3:
         * Input:
         * ["WordFilter", "f"]
         * [[["apple"]], ["b", "e"]]
         * Output:
         * [null, -1]
         */
        WordFilterSuffixWrappedWords wordFilterSuffixWrappedWords = new WordFilterSuffixWrappedWords(new String[]{"apple"});
        assertEquals(-1, wordFilterSuffixWrappedWords.f("b", "e"));
    }
}
