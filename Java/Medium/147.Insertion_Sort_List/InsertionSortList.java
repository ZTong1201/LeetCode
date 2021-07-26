import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InsertionSortList {

    /**
     * Given the head of a singly linked list, sort the list using insertion sort, and return the sorted list's head.
     * <p>
     * The steps of the insertion sort algorithm:
     * <p>
     * Insertion sort iterates, consuming one input element each repetition and growing a sorted output list.
     * At each iteration, insertion sort removes one element from the input data, finds the location it belongs
     * within the sorted list and inserts it there.
     * It repeats until no input elements remain.
     * <p>
     * Approach: Insertion sort
     * <p>
     * Time: first every element has been traversed once - which takes O(n), for each element, it's necessary to find the
     * right place to insert it - which in the worst case needs to traverse the entire sorted list. The number of
     * operations needed will be 1 + 2 + ... + N = N * (N + 1) / 2 => O(N^2)
     * Space: O(1)
     */
    public ListNode insertionSortList(ListNode head) {
        // need a dummy node to store the sorted list
        ListNode dummy = new ListNode();
        // loop through the entire list and insert them into the correct place
        ListNode curr = head;
        while (curr != null) {
            // use prev pointer to find the insertion place
            ListNode prev = dummy;
            while (prev.next != null && prev.next.val < curr.val) {
                prev = prev.next;
            }
            // now prev.next is pointing to the insertion place for curr
            // curr needs to be unlinked from the head, hence we need a variable
            // to keep track of the next candidate node
            ListNode next = curr.next;

            // unlink curr from the head and link to prev.next
            curr.next = prev.next;
            prev.next = curr;

            // node insertion is complete, move to the next node in the original list;
            curr = next;
        }
        return dummy.next;
    }

    @Test
    public void insertionSortListTest() {
        /**
         * Example 1:
         * Input: head = [4,2,1,3]
         * Output: [1,2,3,4]
         */
        ListNode list1 = new ListNode(4, new ListNode(2, new ListNode(1, new ListNode(3))));
        ListNode expected1 = new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(4))));
        ListNode actual1 = insertionSortList(list1);
        while (expected1 != null) {
            assertEquals(expected1.val, actual1.val);
            expected1 = expected1.next;
            actual1 = actual1.next;
        }
        /**
         * Example 2:
         * Input: head = [-1,5,3,4,0]
         * Output: [-1,0,3,4,5]
         */
        ListNode list2 = new ListNode(-1, new ListNode(5, new ListNode(3, new ListNode(4, new ListNode(0)))));
        ListNode expected2 = new ListNode(-1, new ListNode(0, new ListNode(3, new ListNode(4, new ListNode(5)))));
        ListNode actual2 = insertionSortList(list2);
        while (expected2 != null) {
            assertEquals(expected2.val, actual2.val);
            expected2 = expected2.next;
            actual2 = actual2.next;
        }
    }

    private static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int x) {
            this.val = x;
        }

        ListNode(int x, ListNode next) {
            this.val = x;
            this.next = next;
        }
    }
}
