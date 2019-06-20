import org.junit.Test;
import static org.junit.Assert.*;

public class LRUCacheTest{

    @Test
    public void LRUCache1Test() {
        /**
         * LRUCache cache = new LRUCache(2) capacity
         *
         * cache.put(1,1);
         * cache.put(2,2);
         * cache.get(1);       // returns 1
         * cache.put(3,3);    // evicts key 2
         * cache.get(2);       // returns -1 (not found)
         * cache.put(4,4);    // evicts key 1
         * cache.get(1);       // returns -1 (not found)
         * cache.get(3);       // returns 3
         * cache.get(4);       // returns 4
         * cache.put(4,5);
         * cache.get(4);       // return 5
         * cache.put(7, 100);  // evicts key 3
         * cache.get(3);       // returns -1 (not found)
         * cache.get(7);       // return 100
         */
        LRUCache1 cache = new LRUCache1(2);
        cache.put(1, 1);
        cache.put(2, 2);
        assertEquals(1, cache.get(1));
        cache.put(3, 3);
        assertEquals(-1, cache.get(2));
        cache.put(4, 4);
        assertEquals(-1, cache.get(1));
        assertEquals(3, cache.get(3));
        assertEquals(4, cache.get(4));
        cache.put(4, 5);
        assertEquals(5, cache.get(4));
        cache.put(7, 100);
        assertEquals(-1, cache.get(3));
        assertEquals(100, cache.get(7));
    }

    @Test
    public void LRUCache2Test() {
        /**
         * LRUCache cache = new LRUCache(2) capacity
         *
         * cache.put(1,1);
         * cache.put(2,2);
         * cache.get(1);       // returns 1
         * cache.put(3,3);    // evicts key 2
         * cache.get(2);       // returns -1 (not found)
         * cache.put(4,4);    // evicts key 1
         * cache.get(1);       // returns -1 (not found)
         * cache.get(3);       // returns 3
         * cache.get(4);       // returns 4
         * cache.put(4,5);
         * cache.get(4);       // return 5
         * cache.put(7, 100);  // evicts key 3
         * cache.get(3);       // returns -1 (not found)
         * cache.get(7);       // return 100
         */
        LRUCache2 cache = new LRUCache2(2);
        cache.put(1, 1);
        cache.put(2, 2);
        assertEquals(1, cache.get(1));
        cache.put(3, 3);
        assertEquals(-1, cache.get(2));
        cache.put(4, 4);
        assertEquals(-1, cache.get(1));
        assertEquals(3, cache.get(3));
        assertEquals(4, cache.get(4));
        cache.put(4, 5);
        assertEquals(5, cache.get(4));
        cache.put(7, 100);
        assertEquals(-1, cache.get(3));
        assertEquals(100, cache.get(7));
    }

}
