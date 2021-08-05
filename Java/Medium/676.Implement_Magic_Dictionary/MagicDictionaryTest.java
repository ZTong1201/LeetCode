import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MagicDictionaryTest {

    @Test
    public void magicDictionaryTest() {
        /**
         * Example:
         * Input
         * ["MagicDictionary", "buildDict", "search", "search", "search", "search"]
         * [[], [["hello", "leetcode"]], ["hello"], ["hhllo"], ["hell"], ["leetcoded"]]
         * Output
         * [null, null, false, true, false, false]
         * <p>
         * Explanation
         * MagicDictionary magicDictionary = new MagicDictionary();
         * magicDictionary.buildDict(["hello", "leetcode"]);
         * magicDictionary.search("hello"); // return False
         * magicDictionary.search("hhllo"); // We can change the second 'h' to 'e' to match "hello" so we return True
         * magicDictionary.search("hell"); // return False
         * magicDictionary.search("leetcoded"); // return False
         */
        MagicDictionaryTrie magicDictionaryTrie = new MagicDictionaryTrie();
        magicDictionaryTrie.buildDict(new String[]{"hello", "leetcode"});
        assertFalse(magicDictionaryTrie.search("hello"));
        assertTrue(magicDictionaryTrie.search("hhllo"));
        assertFalse(magicDictionaryTrie.search("hell"));
        assertFalse(magicDictionaryTrie.search("leetcoded"));

        MagicDictionaryHashMap magicDictionaryHashMap = new MagicDictionaryHashMap();
        magicDictionaryHashMap.buildDict(new String[]{"hello", "leetcode"});
        assertFalse(magicDictionaryHashMap.search("hello"));
        assertTrue(magicDictionaryHashMap.search("hhllo"));
        assertFalse(magicDictionaryHashMap.search("hell"));
        assertFalse(magicDictionaryHashMap.search("leetcoded"));
    }
}
