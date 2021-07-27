public class MyLinkedList {

    /**
     * Design your implementation of the linked list. You can choose to use a singly or doubly linked list.
     * A node in a singly linked list should have two attributes: val and next. val is the value of the current node,
     * and next is a pointer/reference to the next node.
     * If you want to use the doubly linked list, you will need one more attribute prev to indicate the previous node
     * in the linked list. Assume all nodes in the linked list are 0-indexed.
     * <p>
     * Implement the MyLinkedList class:
     * <p>
     * 1. MyLinkedList() Initializes the MyLinkedList object.
     * 2. int get(int index) Get the value of the indexth node in the linked list. If the index is invalid, return -1.
     * 3. void addAtHead(int val) Add a node of value val before the first element of the linked list. After the insertion,
     * the new node will be the first node of the linked list.
     * 4. void addAtTail(int val) Append a node of value val as the last element of the linked list.
     * 5. void addAtIndex(int index, int val) Add a node of value val before the indexth node in the linked list. If
     * index equals the length of the linked list, the node will be appended to the end of the linked list.
     * If index is greater than the length, the node will not be inserted.
     * 6. void deleteAtIndex(int index) Delete the indexth node in the linked list, if the index is valid.
     * <p>
     * Constraints:
     * <p>
     * 0 <= index, val <= 1000
     * Please do not use the built-in LinkedList library.
     * At most 2000 calls will be made to get, addAtHead, addAtTail, addAtIndex and deleteAtIndex.
     * <p>
     * Approach: Doubly linked list with the same head and tail topology
     * <p>
     * Time:
     * MyLinkedList: O(1)
     * get: O(n) on average
     * addAtHead: O(1)
     * addAtTail: O(1)
     * addAtIndex: O(n) on average
     * deleteAtIndex: O(n) on average
     * <p>
     * Space: O(n) we will have n + 1 nodes in the list
     */

    private int size;
    private final ListNode head;

    /**
     * Initialize your data structure here.
     */
    public MyLinkedList() {
        this.size = 0;
        this.head = new ListNode();
        head.next = head;
        head.prev = head;
    }

    /**
     * Get the value of the index-th node in the linked list. If the index is invalid, return -1.
     */
    public int get(int index) {
        if (index < 0 || index > size - 1) return -1;
        ListNode ptr = head;
        while (index != 0) {
            ptr = ptr.next;
            index--;
        }
        return ptr.next.val;
    }

    /**
     * Add a node of value val before the first element of the linked list. After the insertion, the new node
     * will be the first node of the linked list.
     */
    public void addAtHead(int val) {
        ListNode curr = new ListNode(val);
        head.next.prev = curr;
        curr.next = head.next;
        curr.prev = head;
        head.next = curr;
        this.size++;
    }

    /**
     * Append a node of value val to the last element of the linked list.
     */
    public void addAtTail(int val) {
        ListNode curr = new ListNode(val);
        head.prev.next = curr;
        curr.prev = head.prev;
        curr.next = head;
        head.prev = curr;
        this.size++;
    }

    /**
     * Add a node of value val before the index-th node in the linked list. If index equals to the length of linked list,
     * the node will be appended to the end of linked list. If index is greater than the length, the node will not be inserted.
     */
    public void addAtIndex(int index, int val) {
        if (index < 0 || index > size - 1) return;
        ListNode ptr = head;
        while (index != 0) {
            ptr = ptr.next;
            index--;
        }
        ListNode curr = new ListNode(val);
        ptr.next.prev = curr;
        curr.next = ptr.next;
        curr.prev = ptr;
        ptr.next = curr;
        this.size++;
    }

    /**
     * Delete the index-th node in the linked list, if the index is valid.
     */
    public void deleteAtIndex(int index) {
        if (index < 0 || index > size - 1) return;
        ListNode ptr = head;
        while (index != 0) {
            ptr = ptr.next;
            index--;
        }
        ptr.next.prev = null;
        ptr.next = ptr.next.next;
        ptr.next.prev.next = null;
        ptr.next.prev = ptr;
        this.size--;
    }

    public static class ListNode {
        int val;
        ListNode prev;
        ListNode next;

        ListNode() {

        }

        ListNode(int x) {
            this.val = x;
        }
    }
}
