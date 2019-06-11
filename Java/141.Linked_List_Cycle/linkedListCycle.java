import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class linkedListCycle {


    /**
     * Given a linked list, determine if it has a cycle in it.
     *
     * To represent a cycle in the given linked list,
     * we use an integer pos which represents the position (0-indexed) in the linked list where tail connects to.
     * If pos is -1, then there is no cycle in the linked list.
     *
     *
     * Use hashMap to record previous ListNode, if any ListNode has appeared before, then the Linked List has a cycle
     *
     * Time: O(N), we need to traverse all the nodes
     * Space: O(N), we need to store previous appeared nodes
     */
    public boolean hasCycleHashMap(ListNode head) {
        Set<ListNode> nodeSet = new HashSet<>();
        while(head != null && head.next != null) {
            if(nodeSet.contains(head)) return true;
            nodeSet.add(head);
            head = head.next;
        }
        return false;
    }

    /**
     * We can solve it using two pointers, fast and slow
     * fast pointer moves two steps in a run, whereas slow points move one step
     * If there is a cycle in the Linked List, then they will meet somewhere in the cycle,
     * else the fast pointer will stop at the null
     *
     * Time: O(N)
     * Space: O(1) no extra space required
     */
    public boolean hasCycleTwoPointers(ListNode head) {
        ListNode fast = head;
        ListNode slow = head;
        while(fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
            if(fast == slow) return true;
        }
        return false;
    }

    @Test
    public void hasCycleHashMapTest() {
        /**
         * Example 1:
         * Input: head = [3,2,0,-4], pos = 1
         * Output: true
         * 3 -> 2 -> 0 -> -4
         *      |__________|
         */
        ListNode test1 = new ListNode(3);
        test1.next = new ListNode(2);
        test1.next.next = new ListNode(0);
        test1.next.next.next = test1.next;
        assertTrue(hasCycleHashMap(test1));
        /**
         * Input: head = [1,2], pos = 0
         * Output: true
         * 1 -> 2
         * |____|
         */
        ListNode test2 = new ListNode(1);
        test2.next = new ListNode(2);
        test2.next.next = test2;
        assertTrue(hasCycleHashMap(test2));
        /**
         * Input: head = [1], pos = -1
         * Output: false
         * 1 -> null
         */
        ListNode test3 = new ListNode(1);
        assertFalse(hasCycleHashMap(test3));
    }

    @Test
    public void hasCycleTwoPointersTest() {
        /**
         * Example 1:
         * Input: head = [3,2,0,-4], pos = 1
         * Output: true
         * 3 -> 2 -> 0 -> -4
         *      |__________|
         */
        ListNode test1 = new ListNode(3);
        test1.next = new ListNode(2);
        test1.next.next = new ListNode(0);
        test1.next.next.next = test1.next;
        assertTrue(hasCycleTwoPointers(test1));
        /**
         * Input: head = [1,2], pos = 0
         * Output: true
         * 1 -> 2
         * |____|
         */
        ListNode test2 = new ListNode(1);
        test2.next = new ListNode(2);
        test2.next.next = test2;
        assertTrue(hasCycleTwoPointers(test2));
        /**
         * Input: head = [1], pos = -1
         * Output: false
         * 1 -> null
         */
        ListNode test3 = new ListNode(1);
        assertFalse(hasCycleTwoPointers(test3));
    }

    private class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            this.val = x;
        }
    }
}
