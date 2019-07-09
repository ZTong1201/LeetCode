import org.junit.Test;
import static org.junit.Assert.*;


public class removeElements {
    /**
     * Remove all elements from a linked list of integers that have value val.
     *
     * Approach 1: dummy node
     * In order to easily handle corner cases, we add a dummy node at the front of the linked list and assign a pointer at the head.
     * If ptr.next.val == val, we should skip the next node, simply ptr.next = ptr.next.next. Otherwise, we keep traversing the node
     * forwardly.
     *
     * Time: O(n) we traverse the list by one-pass
     * Space: O(1) no extra space required
     */
    public ListNode removeElmentsDummyNode(ListNode head, int val) {
        ListNode dummy = new ListNode(val - 1);  //build a dummy node
        dummy.next = head;
        ListNode ptr = dummy;
        while(ptr.next != null) {
            if(ptr.next.val == val) ptr.next = ptr.next.next; //if next value equals to val, skip that node
            else ptr = ptr.next;                              //otherwise, keep traversing
        }
        return dummy.next;
    }

    /**
     * Approach 2: Without dummy node
     * We can avoid using the dummy node. However, we need to handle corner cases, such as the input list is null, or the input
     * list is of length 1 and the only value equals to the val to be removed.
     *
     * Time: O(n) single one-pass
     * Space: O(1)
     */
    public ListNode removeElementsWithoutDummyNode(ListNode head, int val) {
        if(head == null) return head;   //first corner case, if the input list is null
        ListNode ptr = head;
        while(ptr.next != null) {   //exactly the same as with dummy node
            if(ptr.next.val == val) ptr.next = ptr.next.next;
            else ptr = ptr.next;
        }
        //second corner case, if the list is of length 1 and the only value equals to val
        if(head.val == val) head = head.next;
        return head;
    }


    @Test
    public void removeElementsDummyNodeTest() {
        /**
         * Example 1:
         * Input:  1->2->6->3->4->5->6, val = 6
         * Output: 1->2->3->4->5
         */
        ListNode list1 = new ListNode(1);
        list1.next = new ListNode(2);
        list1.next.next = new ListNode(6);
        list1.next.next.next = new ListNode(3);
        list1.next.next.next.next = new ListNode(4);
        list1.next.next.next.next.next = new ListNode(5);
        list1.next.next.next.next.next.next = new ListNode(6);
        int[] expected1 = new int[]{1, 2, 3, 4, 5};
        ListNode actualList1 = removeElmentsDummyNode(list1, 6);
        int[] actual1 = linkedListToArray(actualList1, lengthOfList(actualList1));
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: 1->1, val = 1
         * Output: NULL
         */
        ListNode list2 = new ListNode(1);
        list2.next = new ListNode(1);
        int[] expected2 = new int[0];
        ListNode actualList2 = removeElmentsDummyNode(list2, 1);
        int[] actual2 = linkedListToArray(actualList2, lengthOfList(actualList2));
        assertArrayEquals(expected2, actual2);
        /**
         * Example 3:
         * Input: 1, val = 1
         * Output; NULL
         */
        ListNode list3 = new ListNode(1);
        int[] expected3 = new int[0];
        ListNode actualList3 = removeElmentsDummyNode(list3, 1);
        int[] actual3 = linkedListToArray(actualList3, lengthOfList(actualList3));
        assertArrayEquals(expected3, actual3);
    }

    @Test
    public void removeElementsWithoutDummyNodeTest() {
        /**
         * Example 1:
         * Input:  1->2->6->3->4->5->6, val = 6
         * Output: 1->2->3->4->5
         */
        ListNode list1 = new ListNode(1);
        list1.next = new ListNode(2);
        list1.next.next = new ListNode(6);
        list1.next.next.next = new ListNode(3);
        list1.next.next.next.next = new ListNode(4);
        list1.next.next.next.next.next = new ListNode(5);
        list1.next.next.next.next.next.next = new ListNode(6);
        int[] expected1 = new int[]{1, 2, 3, 4, 5};
        ListNode actualList1 = removeElementsWithoutDummyNode(list1, 6);
        int[] actual1 = linkedListToArray(actualList1, lengthOfList(actualList1));
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: 1->1, val = 1
         * Output: NULL
         */
        ListNode list2 = new ListNode(1);
        list2.next = new ListNode(1);
        int[] expected2 = new int[0];
        ListNode actualList2 = removeElementsWithoutDummyNode(list2, 1);
        int[] actual2 = linkedListToArray(actualList2, lengthOfList(actualList2));
        assertArrayEquals(expected2, actual2);
        /**
         * Example 3:
         * Input: 1, val = 1
         * Output; NULL
         */
        ListNode list3 = new ListNode(1);
        int[] expected3 = new int[0];
        ListNode actualList3 = removeElementsWithoutDummyNode(list3, 1);
        int[] actual3 = linkedListToArray(actualList3, lengthOfList(actualList3));
        assertArrayEquals(expected3, actual3);
    }




    private int[] linkedListToArray(ListNode head, int length) {
        int[] res = new int[length];
        for(int i = 0; i < length; i++) {
            res[i] = head.val;
            head = head.next;
        }
        return res;
    }

    private int lengthOfList(ListNode head) {
        int count = 0;
        while(head != null) {
            count += 1;
            head = head.next;
        }
        return count;
    }


    private class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            this.val = x;
        }
    }
}
