import java.util.HashMap;
import java.util.Map;

public class LFUCache {

    /**
     * Design and implement a data structure for Least Frequently Used (LFU) cache. It should support the following operations: get and put.
     * <p>
     * get(key) - Get the value (will always be positive) of the key if the key exists in the cache, otherwise return -1.
     * put(key, value) - Set or insert the value if the key is not already present. When the cache reaches its capacity, it should
     * invalidate the least frequently used item before inserting a new item. For the purpose of this problem, when there is a tie
     * (i.e., two or more keys that have the same frequency), the least recently used key would be evicted.
     * <p>
     * Follow up:
     * Could you do both operations in O(1) time complexity?
     * <p>
     * Approach: HashMap + Doubly Linked List
     * This problem is an extension of LeetCode 146: https://leetcode.com/problems/lru-cache/
     * Similar to LRU cache, we need a doubly linked list structure to be capable of inserting/deleting any nodes in O(1) time.
     * However, it's also different that now we need to first remove the least frequently used item first, if there is a tie,
     * then delete an LRU element.
     * <p>
     * In order to achieve that, we can also keep a mapping between each frequency and a doubly linked list, the DLL will also
     * remain the LRU order. Therefore, when an element is updated, we first remove that node from the DLL which attaches to
     * the previous frequency (say 1), then modify the frequency (say it's 2 now after get()), and insert the node to the
     * FRONT (we still need to keep the LRU order) of the DLL attached to frequency 2.
     * <p>
     * Time:
     * put: O(1)
     * get: O(1)
     * Space: O(N)
     */
    private int capacity, minFreq;
    private final Map<Integer, DLLNode> nodeMap;
    private final Map<Integer, DLList> countMap;

    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.minFreq = 1;
        this.nodeMap = new HashMap<>();
        this.countMap = new HashMap<>();
    }

    public int get(int key) {
        if (!nodeMap.containsKey(key)) return -1;
        DLLNode node = nodeMap.get(key);
        // since a get method is called, the frequency is updated
        // we need to remove the node from the list attached to the previous frequency
        // and insert the node (with updated frequency) to a new list
        update(node);
        return node.value;
    }

    public void put(int key, int value) {
        // edge case, cannot execute put if the capacity is 0
        if (capacity == 0) return;
        // update the key value pair if the key already exists
        if (nodeMap.containsKey(key)) {
            DLLNode node = nodeMap.get(key);
            // update the value to that key
            node.value = value;
            // since a put method is called - update the frequency for that key
            update(node);
        } else {
            // check whether the cache is at capacity before proceed
            if (nodeMap.size() == capacity) {
                // we need to remove the LFU element
                // first - fetch the DLL with the minimum frequency
                DLList minFreqList = countMap.get(minFreq);
                // remove the key of last node (LFU node) from the node map
                nodeMap.remove(minFreqList.tail.prev.key);
                // also remove the last node in the DLL
                minFreqList.removeNode(minFreqList.tail.prev);
            }
            // since a new element needs to be added - create a new DLL node
            DLLNode node = new DLLNode(key, value);
            // put the new node into the node map
            nodeMap.put(key, node);
            
            // the minimum frequency will be always reset to 1
            minFreq = 1;
            // insert the new node to the DLList
            DLList newList = countMap.getOrDefault(node.count, new DLList());
            newList.addFront(node);
            // update the DLList for that frequency
            countMap.put(node.count, newList);
        }
    }

    private void update(DLLNode node) {
        // get the old list with current frequency
        DLList oldList = countMap.get(node.count);
        // remove the node from old list since the frequency is not valid anymore
        oldList.removeNode(node);
        // then check if the node is the only one which has the minimum frequency
        // if yes, the minimum frequency is not valid since it's been removed
        if (node.count == minFreq && oldList.size == 0) minFreq++;
        // increment the frequency
        node.count++;
        // insert the node to new DDL (which might have existed)
        DLList newList = countMap.getOrDefault(node.count, new DLList());
        // insert the node to the front of DLL (keep the LRU order)
        newList.addFront(node);
        // update the list to the new frequency
        countMap.put(node.count, newList);
    }

    // a doubly linked list to delete/insert node in O(1)
    private static class DLList {
        DLLNode head;
        DLLNode tail;
        int size;

        public DLList() {
            this.head = new DLLNode();
            this.tail = new DLLNode();
            this.size = 0;
            this.head.next = this.tail;
            this.tail.prev = this.head;
        }

        public void removeNode(DLLNode node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
            size--;
        }

        public void addFront(DLLNode node) {
            head.next.prev = node;
            node.next = head.next;
            node.prev = head;
            head.next = node;
            size++;
        }
    }


    // a node class which is used to construct DLL
    private static class DLLNode {
        int key;
        int value;
        int count;
        DLLNode prev;
        DLLNode next;

        DLLNode() {

        }

        // initialize count to 1 as default
        DLLNode(int key, int value) {
            this.key = key;
            this.value = value;
            this.count = 1;
        }
    }
}
