import java.util.*;

public class LRUCache2 {

    /**
     * In order to implement get and put in O(1) runtime, we are actually implementing a Linked hash map a.k.a. Ordered Dictionary
     * Hash map will give us O(1) runtime for remove(), put(), and get()
     * However, if we want to remove element at any location, we need a linked list, specifically, a doubly linked list.
     *
     * Time: O(1), by implementing another doubly linked list, we reduce both runtime to O(1)
     * Space: O(N), we still need a hash map to store key-node pairs, where N is at most the capacity.
     */
    private class DLL {
        int val;
        int key;
        DLL prev;
        DLL next;

        DLL(int key, int val) {
            this.key = key;
            this.val = val;
        }
    }

    private DLL head;
    private DLL tail;
    private int capacity;
    private Map<Integer, DLL> map;

    public LRUCache2(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>();
        this.head = new DLL(0, 0);
        this.tail = new DLL(0, 0);
        this.head.next = tail;
        this.tail.prev = head;
    }

    public int get(int key) {
        //If key isn't in the map, return -1
        if(!this.map.containsKey(key)) return -1;
        //If key is in the map, return its value;
        DLL node = this.map.get(key);
        int value = node.val;
        //Remove value from current node
        removeNode(node);
        //Add node at the front of the node so that it won't be removed first
        addNode(node);
        return value;
    }

    public void put(int key, int value) {
        //If key is in the map, update the value to that key
        if(this.map.containsKey(key)) {
            DLL node = this.map.get(key);
            //Since we use the current node, we remove it from the current position
            removeNode(node);
            //Assign new value to that key
            node.val = value;
            //Add it back to the front of the list
            addNode(node);
        } else {
            //If we reach the capacity, we need to remove the LRU item, which is at the tail of the list
            if(this.capacity == this.map.size()) {
                //remove key-node pair in our map
                this.map.remove(this.tail.prev.key);
                //remove node at the end of the list
                removeNode(this.tail.prev);
            }
            //Otherwise, we put the key-node pair in the hash map, and add new node at the front of the list
            DLL node = new DLL(key, value);
            this.map.put(key, node);
            addNode(node);
        }

    }



    private void removeNode(DLL dllNode) {
        dllNode.prev.next = dllNode.next;
        dllNode.next.prev = dllNode.prev;
    }

    private void addNode(DLL dllNode) {
        dllNode.next = head.next;
        head.next.prev = dllNode;
        head.next = dllNode;
        dllNode.prev = head;
    }
}
