import org.junit.Test;
import static org.junit.Assert.*;

public class removeNthNode {


    /**
     * Given a linked list, remove the n-th node from the end of list and return its head.
     *
     * Note:
     * Given n will always be valid.
     *
     * Two pass algorithm: This problem can be easily handled if we know the actual length of the list, we can iterate over nodes
     * until we reach the (length - n)-th node in the list, and assign the node after its next node as the next node (ptr.next = ptr.next.next)
     * The tricky part is how do we remove nodes from the head of the list, the approach is simply add a dummy node at the front, which points
     * to the list head.
     *
     * Time: O(2N) = O(N), we iterate over the list twice
     * Space: O(1), no extra space required
     */
    public ListNode removeNthFromEndTwoPass(ListNode head, int n) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        int length = 0;
        ListNode ptr = head;
        while(ptr != null) {
            length += 1;
            ptr = ptr.next;
        }
        ptr = dummy;
        for(int i = 0; i < length - n; i++) {
            ptr = ptr.next;
        }
        ptr.next = ptr.next.next;
        return dummy.next;
    }

    /**
     * We can also do one pass by using two pointers. Both pointers start at the dummy node, the first pointer locates n + 1 far
     * away from the second pointer. These two pointers move one step at a time, when first pointer reach the null node, the second
     * pointer locates at one node ahead of the node we want to remove. We simply assign second.next = second.next.next in the end
     * and get the desired linked list
     *
     * Time: O(N) since we only traverse the list once
     * Space: O(1), no extra space required
     */
    public ListNode removeNthFromEndOnePass(ListNode head, int n) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode first = dummy, second = dummy;
        for(int i = 1; i <= n + 1; i++) {
            first = first.next;
        }
        while(first != null) {
            first = first.next;
            second = second.next;
        }
        second.next = second.next.next;
        return dummy.next;
    }

    @Test
    public void removeNthFromEndOnePassTest() {
        /**
         * Example 1:
         * Given linked list: 1->2->3->4->5, and n = 2.
         *
         * After removing the second node from the end, the linked list becomes 1->2->3->5.
         */
        ListNode list1 = new ListNode(1);
        list1.next = new ListNode(2);
        list1.next.next = new ListNode(3);
        list1.next.next.next = new ListNode(4);
        list1.next.next.next.next = new ListNode(5);
        int[] expected1 = new int[]{1, 2, 3, 5};
        int[] actual1 = linkedListToArray(removeNthFromEndOnePass(list1, 2), 4);
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Given linked list: 1->2, and n = 2.
         *
         * After removing the second node from the end, the linked list becomes 2.
         */
        ListNode list2 = new ListNode(1);
        list2.next = new ListNode(2);
        int[] expected2 = new int[]{2};
        int[] actual2 = linkedListToArray(removeNthFromEndOnePass(list2, 2), 1);
        assertArrayEquals(expected2, actual2);
        /**
         * Example 3:
         * Given linked list: 1, and n = 1.
         *
         * After removing the second node from the end, the linked list becomes null.
         */
        ListNode list3 = new ListNode(1);
        int[] expected3 = new int[]{};
        int[] actual3 = linkedListToArray(removeNthFromEndOnePass(list3, 1), 0);
        assertArrayEquals(expected3, actual3);
    }

    @Test
    public void removeNthFromEndTwoPassTest() {
        /**
         * Example 1:
         * Given linked list: 1->2->3->4->5, and n = 2.
         *
         * After removing the second node from the end, the linked list becomes 1->2->3->5.
         */
        ListNode list1 = new ListNode(1);
        list1.next = new ListNode(2);
        list1.next.next = new ListNode(3);
        list1.next.next.next = new ListNode(4);
        list1.next.next.next.next = new ListNode(5);
        int[] expected1 = new int[]{1, 2, 3, 5};
        int[] actual1 = linkedListToArray(removeNthFromEndTwoPass(list1, 2), 4);
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Given linked list: 1->2, and n = 2.
         *
         * After removing the second node from the end, the linked list becomes 2.
         */
        ListNode list2 = new ListNode(1);
        list2.next = new ListNode(2);
        int[] expected2 = new int[]{2};
        int[] actual2 = linkedListToArray(removeNthFromEndTwoPass(list2, 2), 1);
        assertArrayEquals(expected2, actual2);
        /**
         * Example 3:
         * Given linked list: 1, and n = 1.
         *
         * After removing the second node from the end, the linked list becomes null.
         */
        ListNode list3 = new ListNode(1);
        int[] expected3 = new int[]{};
        int[] actual3 = linkedListToArray(removeNthFromEndTwoPass(list3, 1), 0);
        assertArrayEquals(expected3, actual3);
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
