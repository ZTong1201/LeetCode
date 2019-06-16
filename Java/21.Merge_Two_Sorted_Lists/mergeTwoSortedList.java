import org.junit.Test;
import static org.junit.Assert.*;

public class mergeTwoSortedList {

    /**
     * Merge two sorted linked lists and return it as a new list.
     * The new list should be made by splicing together the nodes of the first two lists.
     *
     * Use iteration to compare values at each node, add the smaller node to a new linked List.
     *
     * Time: O(m + n), where m is the length of l1, n is the length of l2
     * Space: O(m + n), we create a new linked list to store the brand new list
     */
    public ListNode mergeTwoListsIterative(ListNode l1, ListNode l2) {
        ListNode res = new ListNode(0);
        ListNode ptr = res;
        while(l1 != null && l2 != null) {
            if(l1.val <= l2.val) {
                ptr.next = l1;
                l1 = l1.next;
            } else {
                ptr.next = l2;
                l2 = l2.next;
            }
            ptr = ptr.next;
        }
        ptr.next = l1 == null ? l2 : l1;
        return res.next;
    }

    /**
     * We can do it using recursion as well. If the list can be modified, we first accounting for edge cases.
     * Specifically, if either of l1 or l2 is initially null, there is no merge to perform, so we simply return the non-null list.
     * Otherwise, we determine which of l1 and l2 has a smaller head, and recursively set the next value for that head to the
     * next merge result.
     *
     * Time: O(m + n);
     * Space: O(m + n), in the worst case, the call stack will return value when we reach the last value in either list.
     */
    public ListNode mergeTwoListsRecursive(ListNode l1, ListNode l2) {
        if(l1 == null) return l2;
        if(l2 == null) return l1;
        if(l1.val <= l2.val) {
            l1.next = mergeTwoListsRecursive(l1.next, l2);
            return l1;
        } else {
            l2.next = mergeTwoListsRecursive(l1, l2.next);
            return l2;
        }
    }

    @Test
    public void mergeTwoListsRecursiveTest() {
        /**
         * Example 1:
         * Input: 1->2->4, 1->3->4
         * Output: 1->1->2->3->4->4
         */
        ListNode list1 = new ListNode(1);
        list1.next = new ListNode(2);
        list1.next.next = new ListNode(4);
        ListNode list2 = new ListNode(1);
        list2.next = new ListNode(3);
        list2.next.next = new ListNode(4);
        ListNode actual1 = mergeTwoListsRecursive(list1, list2);
        int[] expected1 = new int[]{1, 1, 2, 3, 4, 4};
        assertArrayEquals(expected1, linkedListToArray(actual1, 6));
        /**
         * Example 2:
         * Input: 1->2->3, 4->5->6
         * Output: 1->2->3->4->5->6
         */
        ListNode list3 = new ListNode(1);
        list3.next = new ListNode(2);
        list3.next.next = new ListNode(3);
        ListNode list4 = new ListNode(4);
        list4.next = new ListNode(5);
        list4.next.next = new ListNode(6);
        ListNode actual2 = mergeTwoListsRecursive(list3, list4);
        int[] expected2 = new int[]{1, 2, 3, 4, 5, 6};
        assertArrayEquals(expected2, linkedListToArray(actual2, 6));
    }


    @Test
    public void mergeTwoListsIterative() {
        /**
         * Example 1:
         * Input: 1->2->4, 1->3->4
         * Output: 1->1->2->3->4->4
         */
        ListNode list1 = new ListNode(1);
        list1.next = new ListNode(2);
        list1.next.next = new ListNode(4);
        ListNode list2 = new ListNode(1);
        list2.next = new ListNode(3);
        list2.next.next = new ListNode(4);
        ListNode actual1 = mergeTwoListsIterative(list1, list2);
        int[] expected1 = new int[]{1, 1, 2, 3, 4, 4};
        assertArrayEquals(expected1, linkedListToArray(actual1, 6));
        /**
         * Example 2:
         * Input: 1->2->3, 4->5->6
         * Output: 1->2->3->4->5->6
         */
        ListNode list3 = new ListNode(1);
        list3.next = new ListNode(2);
        list3.next.next = new ListNode(3);
        ListNode list4 = new ListNode(4);
        list4.next = new ListNode(5);
        list4.next.next = new ListNode(6);
        ListNode actual2 = mergeTwoListsIterative(list3, list4);
        int[] expected2 = new int[]{1, 2, 3, 4, 5, 6};
        assertArrayEquals(expected2, linkedListToArray(actual2, 6));
    }

    private int[] linkedListToArray(ListNode head, int n) {
        int[] res = new int[n];
        for(int i = 0; i < n; i++) {
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
