import org.junit.Test;
import static org.junit.Assert.*;

public class removeDuplicates {


    /**
     * Given a sorted linked list, delete all duplicates such that each element appear only once.
     *
     * Traverse the list once, if the next value equals to the current value, we simply assign the next node to the next after
     * the next. If not, move one step ahead.
     *
     * Time: O(n) traverse the list once
     * Space: O(1) only assign one pointer, no extra space required;
     */
    public ListNode deleteDuplicates(ListNode head) {
        ListNode curr = head;
        while(curr != null && curr.next != null) {
            if(curr.next.val == curr.val) {
                curr.next = curr.next.next;
            } else {
                curr = curr.next;
            }
        }
        return head;
    }

    @Test
    public void deleteDuplicatesTest() {
        /**
         * Example 1:
         * Input: 1->1->2
         * Output: 1->2
         */
        ListNode list1 = new ListNode(1);
        list1.next = new ListNode(1);
        list1.next.next = new ListNode(2);
        int[] expected1 = new int[]{1, 2};
        int[] actual1 = linkedListToArray(deleteDuplicates(list1), 2);
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: 1->1->2->3->3
         * Output: 1->2->3
         */
        ListNode list2 = new ListNode(1);
        list2.next = new ListNode(1);
        list2.next.next = new ListNode(2);
        list2.next.next.next = new ListNode(3);
        list2.next.next.next.next = new ListNode(3);
        int[] expected2 = new int[]{1, 2, 3};
        int[] actual2 = linkedListToArray(deleteDuplicates(list2), 3);
        assertArrayEquals(expected2, actual2);
    }

    private int[] linkedListToArray(ListNode head, int length) {
        int[] res = new int[length];
        for(int i = 0; i < length; i++) {
            res[i] = head.val;
            head = head.next;
        }
        return res;
    }

    private class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            this.val = x;
        }
    }
}
