import org.junit.Test;
import static org.junit.Assert.*;

public class TrieTest {

    @Test
    public void trieTest() {
        /**
         * Example:
         * Trie trie = new Trie();
         *
         * trie.insert("apple");
         * trie.search("apple");   // returns true
         * trie.search("app");     // returns false
         * trie.startsWith("app"); // returns true
         * trie.insert("app");
         * trie.search("app");     // returns true
         */
        Trie trie = new Trie();
        trie.insert("apple");
        assertTrue(trie.search("apple"));
        assertFalse(trie.search("app"));
        assertTrue(trie.startsWith("app"));
        trie.insert("app");
        assertTrue(trie.search("app"));
    }
}
