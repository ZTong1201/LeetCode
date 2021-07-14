import org.junit.Test;
import static org.junit.Assert.*;

public class LFUCacheTest {

    @Test
    public void LFUCacheTest() {
        /**
         * Example:
         * LFUCache cache = new LFUCache(2);  //capacity
         *
         * cache.put(1,1);
         * cache.put(2,2);
         * cache.get(1);       // returns 1
         * cache.put(3,3);    // evicts key 2
         * cache.get(2);       // returns -1 (not found)
         * cache.get(3);       // returns 3.
         * cache.put(4,4);    // evicts key 1.
         * cache.get(1);       // returns -1 (not found)
         * cache.get(3);       // returns 3
         * cache.get(4);       // returns 4
         */
        LFUCache cache = new LFUCache(2);
        cache.put(1, 1);
        cache.put(2, 2);
        assertEquals(1, cache.get(1));
        cache.put(3, 3);
        assertEquals(-1, cache.get(2));
        assertEquals(3, cache.get(3));
        cache.put(4, 4);
        assertEquals(-1, cache.get(1));
        assertEquals(3, cache.get(3));
        assertEquals(4, cache.get(4));
    }
}
