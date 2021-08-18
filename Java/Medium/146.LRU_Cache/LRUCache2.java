import java.util.HashMap;
import java.util.Map;

public class LRUCache2 {

    /**
     * In order to implement get and put in O(1) runtime, we are actually implementing a Linked hash map a.k.a. Ordered Dictionary
     * Hash map will give us O(1) runtime for remove(), put(), and get()
     * However, if we want to remove element at any location, we need a linked list, specifically, a doubly linked list.
     * <p>
     * Time: O(1), by implementing another doubly linked list, we reduce both runtime to O(1)
     * Space: O(N), we still need a hash map to store key-node pairs, where N is at most the capacity.
     */
    private static class DLL {
        int val;
        int key;
        DLL prev;
        DLL next;

        DLL() {

        }

        DLL(int key, int val) {
            this.key = key;
            this.val = val;
        }
    }

    private final DLL head;
    private final DLL tail;
    private final int capacity;
    private final Map<Integer, DLL> map;

    public LRUCache2(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>();
        this.head = new DLL();
        this.tail = new DLL();
        this.head.next = tail;
        this.tail.prev = head;
    }

    public int get(int key) {
        //If key isn't in the map, return -1
        if (!this.map.containsKey(key)) return -1;
        //If key is in the map, return its value;
        DLL node = this.map.get(key);
        //Remove value from current node
        removeNode(node);
        //Add node at the front of the node so that it won't be removed first
        addFront(node);
        return node.val;
    }

    public void put(int key, int value) {
        //If key is in the map, update the value to that key
        if (this.map.containsKey(key)) {
            DLL node = this.map.get(key);
            //Assign new value to that key
            node.val = value;
            //Since we use the current node, we remove it from the current position
            removeNode(node);
            //Add it back to the front of the list
            addFront(node);
        } else {
            //If we reach the capacity, we need to remove the LRU item, which is at the tail of the list
            if (this.capacity == this.map.size()) {
                //remove key-node pair in our map
                this.map.remove(this.tail.prev.key);
                //remove node at the end of the list
                removeNode(this.tail.prev);
            }
            //Otherwise, we put the key-node pair in the hash map, and add new node at the front of the list
            DLL node = new DLL(key, value);
            this.map.put(key, node);
            addFront(node);
        }

    }


    private void removeNode(DLL node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private void addFront(DLL node) {
        head.next.prev = node;
        node.next = head.next;
        head.next = node;
        node.prev = head;
    }
}
