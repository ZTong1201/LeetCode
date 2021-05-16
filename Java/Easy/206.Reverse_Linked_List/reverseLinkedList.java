import org.junit.Test;
import static org.junit.Assert.*;

public class reverseLinkedList {


    /**
     * Reverse a singly linked list.
     *
     * Use Iteration
     *
     * Time: O(N)
     * Space: O(1) no extra space
     */
    public ListNode reverseListIterative(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;
        while(curr != null) {
            ListNode ptr = curr.next;
            curr.next = prev;
            prev = curr;
            curr = ptr;
        }
        return prev;
    }

    /**
     * Use recursion
     *
     * Time: O(N)
     * Space: O(N) for call stack
     */
    public ListNode reverseListRecursive(ListNode head) {
        if(head == null || head.next == null) return head;
        ListNode res = reverseListRecursive(head.next);
        head.next.next = head;
        head.next = null;
        return res;
    }



    @Test
    public void reverseListTestIterative() {
        /**
         * Example:
         * Input: 1->2->3->4->5->NULL
         * Output: 5->4->3->2->1->NULL
         */
        ListNode list1 = new ListNode(1);
        list1.next = new ListNode(2);
        list1.next.next = new ListNode(3);
        list1.next.next.next = new ListNode(4);
        list1.next.next.next.next = new ListNode(5);
        ListNode expected = new ListNode(5);
        expected.next = new ListNode(4);
        expected.next.next = new ListNode(3);
        expected.next.next.next = new ListNode(2);
        expected.next.next.next.next = new ListNode(1);
        ListNode actual = reverseListIterative(list1);
        while(expected != null) {
            assertEquals(expected.val, actual.val);
            expected = expected.next;
            actual = actual.next;
        }
    }

    @Test
    public void reverseListTestRecursive() {
        /**
         * Example:
         * Input: 1->2->3->4->5->NULL
         * Output: 5->4->3->2->1->NULL
         */
        ListNode list1 = new ListNode(1);
        list1.next = new ListNode(2);
        list1.next.next = new ListNode(3);
        list1.next.next.next = new ListNode(4);
        list1.next.next.next.next = new ListNode(5);
        ListNode expected = new ListNode(5);
        expected.next = new ListNode(4);
        expected.next.next = new ListNode(3);
        expected.next.next.next = new ListNode(2);
        expected.next.next.next.next = new ListNode(1);
        ListNode actual = reverseListRecursive(list1);
        while(expected != null) {
            assertEquals(expected.val, actual.val);
            expected = expected.next;
            actual = actual.next;
        }
    }

    private static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            this.val = x;
        }
    }
}
