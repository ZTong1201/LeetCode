import org.junit.Test;
import static org.junit.Assert.*;

public class deleteNode {

    /**
     * Write a function to delete a node (except the tail) in a singly linked list, given only access to that node.
     *
     * Note:
     * 1. The linked list will have at least two elements.
     * 2. All of the nodes' values will be unique.
     * 3. The given node will not be the tail and it will always be a valid node of the linked list.
     * 4. Do not return anything from your function.
     *
     * The tricky part is we can only access to the node we want to delete. Since we know the given node will not be the tail,
     * we can simply assign its value to its next node value, and assign its next node to the node after the next.
     *
     * Time: O(1)
     * Space: O(1)
     */
    public void deleteNode(ListNode node) {
        node.val = node.next.val;
        node.next = node.next.next;
    }

    @Test
    public void deleteNodeTest() {
        /**
         * Input: head = [4,5,1,9], node = 5
         * Output: [4,1,9]
         * Explanation: You are given the second node with value 5, the linked list should become 4 -> 1 -> 9 after calling your function.
         */
        ListNode list1 = new ListNode(4);
        list1.next = new ListNode(5);
        list1.next.next = new ListNode(1);
        list1.next.next.next = new ListNode(9);
        ListNode delete1 = list1.next;
        deleteNode(delete1);
        int[] expected1 = new int[]{4, 1, 9};
        int[] actual1 = linkedListToArray(list1, 3);
        assertArrayEquals(expected1, actual1);
        /**
         * Input: head = [4,5,1,9], node = 1
         * Output: [4,5,9]
         * Explanation: You are given the third node with value 1, the linked list should become 4 -> 5 -> 9 after calling your function.
         */
        ListNode list2 = new ListNode(4);
        list2.next = new ListNode(5);
        list2.next.next = new ListNode(1);
        list2.next.next.next = new ListNode(9);
        ListNode delete2 = list2.next.next;
        deleteNode(delete2);
        int[] expected2 = new int[]{4, 5, 9};
        int[] actual2 = linkedListToArray(list2, 3);
        assertArrayEquals(expected2, actual2);
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
