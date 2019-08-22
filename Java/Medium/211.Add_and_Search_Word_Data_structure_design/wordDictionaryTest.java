import org.junit.Test;
import static org.junit.Assert.*;

public class wordDictionaryTest {

    @Test
    public void wordDictionaryTest() {
        /**
         * Example 1:
         * addWord("bad")
         * addWord("dad")
         * addWord("mad")
         * search("pad") -> false
         * search("bad") -> true
         * search(".ad") -> true
         * search("b..") -> true
         */
        wordDictionary wordDict1 = new wordDictionary();
        wordDict1.addWord("bad");
        wordDict1.addWord("dad");
        wordDict1.addWord("mad");
        assertFalse(wordDict1.search("pad"));
        assertTrue(wordDict1.search("bad"));
        assertTrue(wordDict1.search(".ad"));
        assertTrue(wordDict1.search("b.."));
        /**
         * Example 2:
         * addWord("a")
         * addWord("a") -> nothing changed
         * search("a") -> true
         * search(".") -> true
         * search("aa") -> false
         * search(".a") -> false
         * search("..") -> false
         * search("a.") -> false
         */
        wordDictionary wordDict2 = new wordDictionary();
        wordDict2.addWord("a");
        wordDict2.addWord("a");
        assertTrue(wordDict2.search("a"));
        assertTrue(wordDict2.search("."));
        assertFalse(wordDict2.search("aa"));
        assertFalse(wordDict2.search(".a"));
        assertFalse(wordDict2.search(".."));
        assertFalse(wordDict2.search("a."));
    }
}
