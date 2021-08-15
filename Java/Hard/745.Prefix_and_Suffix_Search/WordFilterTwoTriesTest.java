import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WordFilterTwoTriesTest {

    @Test
    public void wordFilterTwoTriesTest1() {
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
        WordFilterTwoTries wordFilterTwoTries = new WordFilterTwoTries(new String[]{"apple"});
        assertEquals(0, wordFilterTwoTries.f("a", "e"));
    }

    @Test
    public void wordFilterTwoTriesTest2() {
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
        WordFilterTwoTries wordFilterTwoTries = new WordFilterTwoTries(new String[]{"cabaabaaaa", "ccbcababac", "bacaabccba", "bcbbcbacaa",
                "abcaccbcaa", "accabaccaa", "cabcbbbcca", "ababccabcb", "caccbbcbab", "bccbacbcba"});
        assertEquals(9, wordFilterTwoTries.f("bccbacbcba", "a"));
        assertEquals(4, wordFilterTwoTries.f("ab", "abcaccbcaa"));
        assertEquals(5, wordFilterTwoTries.f("a", "aa"));
        assertEquals(0, wordFilterTwoTries.f("cabaaba", "abaaaa"));
        assertEquals(8, wordFilterTwoTries.f("cacc", "accbbcbab"));
        assertEquals(1, wordFilterTwoTries.f("ccbcab", "bac"));
        assertEquals(2, wordFilterTwoTries.f("bac", "cba"));
        assertEquals(5, wordFilterTwoTries.f("ac", "accabaccaa"));
        assertEquals(3, wordFilterTwoTries.f("bcbb", "aa"));
        assertEquals(1, wordFilterTwoTries.f("ccbca", "cbcababac"));
    }

    @Test
    public void wordFilterTwoTriesTest3() {
        /**
         * Example 3:
         * Input:
         * ["WordFilter", "f"]
         * [[["apple"]], ["b", "e"]]
         * Output:
         * [null, -1]
         */
        WordFilterTwoTries wordFilterTwoTries = new WordFilterTwoTries(new String[]{"apple"});
        assertEquals(-1, wordFilterTwoTries.f("b", "e"));
    }
}
