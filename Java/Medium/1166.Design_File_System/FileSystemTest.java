import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FileSystemTest {

    @Test
    public void fileSystemMapTest() {
        /**
         * Example 1:
         * Input:
         * ["FileSystem","createPath","get"]
         * [[],["/a",1],["/a"]]
         * Output:
         * [null,true,1]
         * Explanation:
         * FileSystem fileSystem = new FileSystem();
         *
         * fileSystem.createPath("/a", 1); // return true
         * fileSystem.get("/a"); // return 1
         */
        FileSystemMap fileSystem = new FileSystemMap();
        assertTrue(fileSystem.createPath("/a", 1));
        assertEquals(1, fileSystem.get("/a"));
        /**
         * Example 2:
         * Input:
         * ["FileSystem","createPath","createPath","get","createPath","get"]
         * [[],["/leet",1],["/leet/code",2],["/leet/code"],["/leet/code",3],["/leet/code"]]
         * Output:
         * [null,true,true,2,false,2]
         */
        fileSystem = new FileSystemMap();
        assertTrue(fileSystem.createPath("/leet", 1));
        assertTrue(fileSystem.createPath("/leet/code", 2));
        assertEquals(2, fileSystem.get("/leet/code"));
        assertFalse(fileSystem.createPath("/leet/code", 3));
        assertEquals(2, fileSystem.get("/leet/code"));
    }

    @Test
    public void fileSystemTrieTest() {
        /**
         * Example 1:
         * Input:
         * ["FileSystem","createPath","get"]
         * [[],["/a",1],["/a"]]
         * Output:
         * [null,true,1]
         * Explanation:
         * FileSystem fileSystem = new FileSystem();
         *
         * fileSystem.createPath("/a", 1); // return true
         * fileSystem.get("/a"); // return 1
         */
        FileSystemTrie fileSystem = new FileSystemTrie();
        assertTrue(fileSystem.createPath("/a", 1));
        assertEquals(1, fileSystem.get("/a"));
        /**
         * Example 2:
         * Input:
         * ["FileSystem","createPath","createPath","get","createPath","get"]
         * [[],["/leet",1],["/leet/code",2],["/leet/code"],["/leet/code",3],["/leet/code"]]
         * Output:
         * [null,true,true,2,false,2]
         */
        fileSystem = new FileSystemTrie();
        assertTrue(fileSystem.createPath("/leet", 1));
        assertTrue(fileSystem.createPath("/leet/code", 2));
        assertEquals(2, fileSystem.get("/leet/code"));
        assertFalse(fileSystem.createPath("/leet/code", 3));
        assertEquals(2, fileSystem.get("/leet/code"));
    }
}
