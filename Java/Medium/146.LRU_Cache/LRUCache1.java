import java.util.*;

public class LRUCache1 {
    /**
     * Design and implement a data structure for Least Recently Used (LRU) cache. It should support the following operations: get and put.
     *
     * get(key) - Get the value (will always be positive) of the key if the key exists in the cache, otherwise return -1.
     * put(key, value) - Set or insert the value if the key is not already present. When the cache reached its capacity, it should invalidate the least recently used item before inserting a new item.
     *
     * The cache is initialized with a positive capacity.
     *
     * The tricky part for this problem is that we need to track the Least Recently Used (LRU) item. If we never use these items,
     * we simply remove the first added item when exceeds capacity. If we at any time use the item (e.g. get() method), we should
     * rearrange the priority for removal.
     *
     * In this implementation, a hash map and a deque are used.
     *
     * Time: O(n), for both put() and get(), we need to remove key from deque, it will take O(n) time to complete the process
     * Space: O(2*n), where n is the capacity, we need to store items in a deque and a map
     */
    private Map<Integer, Integer> map;
    private Deque<Integer> deque;
    private int capacity;

    public LRUCache1() {
        this.map = new HashMap<>();
        this.deque = new ArrayDeque<>();
        //this.capacity = capacity;
    }

    public LRUCache1(int capacity) {
        this.map = new HashMap<>();
        this.deque = new ArrayDeque<>();
        this.capacity = capacity;
    }

    public int get(int key) {
        if(!deque.contains(key)) return -1;
        deque.remove(key);
        deque.addFirst(key);
        return this.map.get(key);
    }

    public void put(int key, int value) {
        if(this.deque.contains(key)) this.deque.remove(key);
        else if(this.map.size() == this.capacity) this.map.remove(this.deque.removeLast());
        this.map.put(key, value);
        this.deque.addFirst(key);
    }
}
