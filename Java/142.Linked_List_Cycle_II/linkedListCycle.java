import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class linkedListCycle {
    /**
     * Given a linked list, return the node where the cycle begins. If there is no cycle, return null.
     *
     * To represent a cycle in the given linked list,
     * we use an integer pos which represents the position (0-indexed) in the linked list where tail connects to.
     * If pos is -1, then there is no cycle in the linked list.
     *
     * Note: Do not modify the linked list.
     *
     * First, still use hashMap to detect where the cycle begins
     *
     * Time: O(N)
     * Space: O(N)
     */
    public ListNode detectCycleHashMap(ListNode head) {
        Set<ListNode> nodeSet = new HashSet<>();
        while(head != null && head.next != null) {
            if(nodeSet.contains(head)) return head;
            nodeSet.add(head);
            head = head.next;
        }
        return null;
    }

    /**
     * We can still use a slow and a fast pointer to solve this problem.
     * If the linked list is cyclic, these two pointers will meet at somewhere in the cycle.
     * Assume the cycle starts at point a, and two pointers meet at point b.
     * Say the length between the head and point a is L, the length of the cycle is C (L + C = N), the
     * length between a and b is h
     *
     * When they met, we know the fast pointer traveled twice far than the slow pointer did, we have 2 * dist(slow) = dist(fast)
     * hence 2 * (L + h) = L + h + n*C  ===> L = nC - h
     *
     * The next step, we will move the slow pointer to the head, and moves both pointers ONE step at each time.
     * Therefore, when the slow pointer reaches the start of the cycle, it moves L
     * Meanwhile, the fast pointer starts from L + h and moves L = nC - h
     * In the end, the fast pointer reaches L + h + (nC - h) mod C = L + (h + nC - h) mod C = L
     * Hence, these two pointers finally meet at the start of the cycle
     *
     *    (L)  a
     * -----------------
     *        /         \
     *       |          |b
     *       |          |
     *       \__________/
     *
     *
     * Time: O(N), since in both steps, the slow pointer will traverse all the nodes in the worst case.
     * Space: O(1), no extra space needed since we only assign two pointers
     */
    public ListNode detectCycleTwoPointers(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;
        while(fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if(slow == fast) {
                slow = head;
                while(slow != fast) {
                    slow = slow.next;
                    fast = fast.next;
                }
                return slow;
            }
        }
        return null;
    }

    @Test
    public void detectCycleTwoPointersTest() {
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
        ListNode expected1 = test1.next;
        assertEquals(expected1, detectCycleTwoPointers(test1));
        /**
         * Input: head = [1,2], pos = 0
         * Output: true
         * 1 -> 2
         * |____|
         */
        ListNode test2 = new ListNode(1);
        test2.next = new ListNode(2);
        test2.next.next = test2;
        ListNode expected2 = test2;
        assertEquals(expected2, detectCycleTwoPointers(test2));
        /**
         * Input: head = [1], pos = -1
         * Output: false
         * 1 -> null
         */
        ListNode test3 = new ListNode(1);
        assertEquals(null, detectCycleTwoPointers(test3));
    }

    @Test
    public void detectCycleHashMapTest() {
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
        ListNode expected1 = test1.next;
        assertEquals(expected1, detectCycleHashMap(test1));
        /**
         * Input: head = [1,2], pos = 0
         * Output: true
         * 1 -> 2
         * |____|
         */
        ListNode test2 = new ListNode(1);
        test2.next = new ListNode(2);
        test2.next.next = test2;
        ListNode expected2 = test2;
        assertEquals(expected2, detectCycleHashMap(test2));
        /**
         * Input: head = [1], pos = -1
         * Output: false
         * 1 -> null
         */
        ListNode test3 = new ListNode(1);
        assertEquals(null, detectCycleHashMap(test3));
    }


    private class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            this.val = x;
        }
    }
}
