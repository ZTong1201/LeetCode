import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class reverseList {

    /**
     * Reverse a linked list from position m to n. Do it in one-pass.
     * <p>
     * Note: 1 ≤ m ≤ n ≤ length of list.
     * <p>
     * Approach 1: Iteration
     * Just like reverse the whole linked list, when we find the correct position, we could easily reverse the list in between use
     * the three pointers trick. However, since we have to do one-pass, we need to record the node before we start reverse (front),
     * and the tail node of the reversed list (tail). We'll assign the reversed list to front.next, and assign the rest of list to
     * tail.next. In this case, front is the previous node of start node, and tail is just the initial start node. After reversing, the
     * start node becomes the tail.
     * <p>
     * Meanwhile, for the sake of simplicity to handle edge cases and m = 1, we build a dummy node at the very beginning to guarantee
     * that we always at least have two nodes in the list.
     * <p>
     * Time: O(n), we do it in one-pass;
     * Space: O(1), we just assign pointers, no extra space required
     */
    public ListNode reverseBetweenIterative(ListNode head, int left, int right) {
        ListNode dummy = new ListNode();
        dummy.next = head;
        //assign two pointers
        ListNode prev = dummy;
        ListNode curr = dummy.next;
        //move two pointers to the correct starting node
        while (left > 1) {
            prev = prev.next;
            curr = curr.next;
            left--;
            right--;
        }

        //We are at the starting node, assign the front and the tail pointers
        ListNode front = prev, tail = curr;
        while (right > 0) {
            ListNode temp = curr.next;
            curr.next = prev;
            prev = curr;
            curr = temp;
            right--;
        }
        front.next = prev;
        tail.next = curr;
        return dummy.next;
    }

    /**
     * Approach 2: Recursion
     * Actually we can break the problem into two problems: 1. reverse N elements from the beginning of the list 2. skip
     * the first left - 1 elements and reverse (right - left) elements subsequently.
     * <p>
     * The first problem is pretty much the same as reversing the entire list. However, in order to reverse the whole list,
     * we always assign head.next as null since it will always be the tail of the list. As for reversing first N elements,
     * when the element is reached, we need keep track of its successor (head.next) and always assign the tail of the list
     * to that successor in each recursion call.
     * <p>
     * Then, we tackle the second problem by recursively skipping first left - 1 elements and start calling reversing N
     * elements from there
     * Time: O(n), we typically do it by one-pass. However, in the worst, we may do it in two-pass
     * Space: O(1), only assigned pointers, no extra space required
     */
    private ListNode successor;

    public ListNode reverseBetweenRecursive(ListNode head, int left, int right) {
        // when m is 1, we are just basically reversing right elements from the beginning
        // (first left elements have been skipped)
        if (left == 1) {
            return reverseN(head, right);
        }
        // recursively skipping first left elements
        head.next = reverseBetweenRecursive(head.next, left - 1, right - 1);
        return head;
    }

    private ListNode reverseN(ListNode head, int n) {
        // base case, if we reach the end of the list we need to reverse
        if (n == 1) {
            // keep track of the tail of the rest list
            // which will always be assigned to the end of reversed list
            successor = head.next;
            return head;
        }
        // recursively reversing first N elements in head
        ListNode p = reverseN(head.next, n - 1);
        // after hitting the end
        // reverse the list structure
        head.next.next = head;
        // reassign the tail of the reversed list to avoid cycle
        head.next = successor;
        return p;
    }


    @Test
    public void reverseBetweenRecursiveTest() {
        /**
         * Example 1:
         * Input: 1->2->3->4->5->NULL, m = 2, n = 4
         * Output: 1->4->3->2->5->NULL
         */
        ListNode list1 = new ListNode(1);
        list1.next = new ListNode(2);
        list1.next.next = new ListNode(3);
        list1.next.next.next = new ListNode(4);
        list1.next.next.next.next = new ListNode(5);
        int[] expected1 = new int[]{1, 4, 3, 2, 5};
        int[] actual1 = linkedListToArray(reverseBetweenRecursive(list1, 2, 4), 5);
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: 3->5->NULL, m = 1, n = 2
         * Output: 5->3->NULL
         */
        ListNode list2 = new ListNode(3);
        list2.next = new ListNode(5);
        int[] expected2 = new int[]{5, 3};
        int[] actual2 = linkedListToArray(reverseBetweenRecursive(list2, 1, 2), 2);
        assertArrayEquals(expected2, actual2);
    }

    @Test
    public void reverseBetweenIterativeTest() {
        /**
         * Example 1:
         * Input: 1->2->3->4->5->NULL, m = 2, n = 4
         * Output: 1->4->3->2->5->NULL
         */
        ListNode list1 = new ListNode(1);
        list1.next = new ListNode(2);
        list1.next.next = new ListNode(3);
        list1.next.next.next = new ListNode(4);
        list1.next.next.next.next = new ListNode(5);
        int[] expected1 = new int[]{1, 4, 3, 2, 5};
        int[] actual1 = linkedListToArray(reverseBetweenIterative(list1, 2, 4), 5);
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: 3->5->NULL, m = 1, n = 2
         * Output: 5->3->NULL
         */
        ListNode list2 = new ListNode(3);
        list2.next = new ListNode(5);
        int[] expected2 = new int[]{5, 3};
        int[] actual2 = linkedListToArray(reverseBetweenIterative(list2, 1, 2), 2);
        assertArrayEquals(expected2, actual2);
    }


    private int[] linkedListToArray(ListNode head, int length) {
        int[] res = new int[length];
        for (int i = 0; i < length; i++) {
            res[i] = head.val;
            head = head.next;
        }
        return res;
    }

    private static class ListNode {
        int val;
        ListNode next;

        ListNode() {
            
        }

        ListNode(int x) {
            this.val = x;
        }
    }
}
