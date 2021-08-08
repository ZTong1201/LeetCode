import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MiddleOfLinkedList {

    /**
     * Given the head of a singly linked list, return the middle node of the linked list.
     * <p>
     * If there are two middle nodes, return the second middle node.
     * <p>
     * Constraints:
     * <p>
     * The number of nodes in the list is in the range [1, 100].
     * 1 <= Node.val <= 100
     * <p>
     * Approach: Fast & Slow pointers
     * <p>
     * Time: O(n)
     * Space: O(1)
     */
    public ListNode middleNode(ListNode head) {
        ListNode fast = head, slow = head;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        return slow;
    }

    @Test
    public void middleNodeTest() {
        /**
         * Example 1:
         * Input: head = [1,2,3,4,5]
         * Output: [3,4,5]
         * Explanation: The middle node of the list is node 3.
         * 1 -> 2 -> 3 -> 4 -> 5 -> null
         */
        ListNode list1 = new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(4, new ListNode(5)))));
        ListNode expected1 = list1.next.next;
        ListNode actual1 = middleNode(list1);
        assertEquals(expected1.val, actual1.val);
        /**
         * Example 2:
         * Input: head = [1,2,3,4,5,6]
         * Output: [4,5,6]
         * Explanation: Since the list has two middle nodes with values 3 and 4, we return the second one.
         * 1 -> 2 -> 3 -> 4 -> 5 -> 6 -> null
         */
        ListNode list2 = new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(4,
                new ListNode(5, new ListNode(6))))));
        ListNode expected2 = list1.next.next.next;
        ListNode actual2 = middleNode(list2);
        assertEquals(expected2.val, actual2.val);
    }

    private static class ListNode {
        int val;
        ListNode next;

        public ListNode(int x) {
            this.val = x;
        }

        public ListNode(int x, ListNode next) {
            this.val = x;
            this.next = next;
        }
    }
}
