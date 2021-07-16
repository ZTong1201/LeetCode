import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SwapNodes {

    /**
     * Given a linked list, swap every two adjacent nodes and return its head. You must solve the problem without
     * modifying the values in the list's nodes (i.e., only nodes themselves may be changed.)
     * <p>
     * <p>
     * Approach 1: Iterative
     * Time: O(n)
     * Space: O(1)
     */
    public ListNode swapPairsIterative(ListNode head) {
        ListNode dummy = new ListNode(-1);
        dummy.next = head;

        // assign a prev node to easily get current head
        ListNode prevNode = dummy;
        while ((head != null) && (head.next != null)) {
            // get nodes in pairs
            ListNode firstNode = head;
            ListNode secondNode = head.next;

            // assign the new head after prev node
            prevNode.next = secondNode;
            // swap nodes
            firstNode.next = secondNode.next;
            secondNode.next = firstNode;

            // move the prev node to the current place to easily get next head node
            prevNode = firstNode;
            // jump the head to next pairs
            head = firstNode.next;
        }
        return dummy.next;
    }

    /**
     * Approach 2: Recursive
     * In each recursive call, it's only necessary to swap the current two nodes since the subsequent sub-list has been
     * swapped successfully by the previous recursion calls. i.e.
     * ....-> 1 -> 2 -> [list has been swapped]
     * after this method call, the list becomes
     * ....-> 2 -> 1 -> [list has been swapped]
     * The base case is hit when reaching the tail of the list.
     * <p>
     * Time: O(n)
     * Space: O(n) stack trace
     */
    public ListNode swapPairsRecursive(ListNode head) {
        // base case
        if ((head == null) || (head.next == null)) {
            return head;
        }

        // get candidate nodes to be swapped in this recursive call
        ListNode firstNode = head;
        ListNode secondNode = head.next;

        // swap the two nodes assuming the subsequent sub-list has been swapped successfully
        firstNode.next = swapPairsRecursive(secondNode.next);
        secondNode.next = firstNode;

        // after swapping, the new head will be the second node (since it's been swapped to the front)
        return secondNode;
    }

    @Test
    public void swapPairsIterativeTest() {
        /**
         * Example 1:
         * Input: head = [1,2,3,4]
         * Output: [2,1,4,3]
         */
        ListNode list1 = new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(4))));
        ListNode expected1 = new ListNode(2, new ListNode(1, new ListNode(4, new ListNode(3))));
        ListNode actual1 = swapPairsIterative(list1);
        while (expected1 != null) {
            assertEquals(expected1.val, actual1.val);
            expected1 = expected1.next;
            actual1 = actual1.next;
        }
        /**
         * Example 2:
         * Input: head = []
         * Output: []
         */
        assertNull(swapPairsIterative(null));
        /**
         * Example 3:
         * Input: head = [1]
         * Output: [1]
         */
        ListNode list2 = new ListNode(1);
        ListNode actual2 = swapPairsIterative(list2);
        while (list2 != null) {
            assertEquals(list2.val, actual2.val);
            list2 = list2.next;
            actual2 = actual2.next;
        }
    }

    @Test
    public void swapPairsRecursiveTest() {
        /**
         * Example 1:
         * Input: head = [1,2,3,4]
         * Output: [2,1,4,3]
         */
        ListNode list1 = new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(4))));
        ListNode expected1 = new ListNode(2, new ListNode(1, new ListNode(4, new ListNode(3))));
        ListNode actual1 = swapPairsRecursive(list1);
        while (expected1 != null) {
            assertEquals(expected1.val, actual1.val);
            expected1 = expected1.next;
            actual1 = actual1.next;
        }
        /**
         * Example 2:
         * Input: head = []
         * Output: []
         */
        assertNull(swapPairsRecursive(null));
        /**
         * Example 3:
         * Input: head = [1]
         * Output: [1]
         */
        ListNode list2 = new ListNode(1);
        ListNode actual2 = swapPairsRecursive(list2);
        while (list2 != null) {
            assertEquals(list2.val, actual2.val);
            list2 = list2.next;
            actual2 = actual2.next;
        }
    }

    private static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            this.val = x;
        }

        ListNode(int x, ListNode next) {
            this.val = x;
            this.next = next;
        }
    }
}
