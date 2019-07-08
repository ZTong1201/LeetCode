import org.junit.Test;
import static org.junit.Assert.*;

public class reverseList {

    /**
     * Reverse a linked list from position m to n. Do it in one-pass.
     *
     * Note: 1 ≤ m ≤ n ≤ length of list.
     *
     * Approach 1: Iteration
     * Just like reverse the whole linked list, when we find the correct position, we could easily reverse the list in between use
     * the three pointers trick. However, since we have to do one-pass, we need to record the node before we start reverse (front),
     * and the tail node of the reversed list (tail). We'll assign the reversed list to front.next, and assign the rest of list to
     * tail.next. In this case, front is the previous node of start node, and tail is just the initial start node. After reversing, the
     * start node becomes the tail.
     *
     * Meanwhile, for the sake of simplicity to handle edge cases and m = 1, we build a dummy node at the very beginning to guarantee
     * that we always at least have two nodes in the list.
     *
     * Time: O(n), we do it in one-pass;
     * Space: O(1), we just assign pointers, no extra space required
     */
    public ListNode reverseBetweenIterative(ListNode head, int m, int n) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        //assign two pointers
        ListNode prev = dummy;
        ListNode curr = dummy.next;
        //move two pointers to the correct starting node
        while(m > 1) {
            prev = prev.next;
            curr = curr.next;
            m -= 1;
            n -= 1;
        }

        //We are at the starting node, assign the front and the tail pointers
        ListNode front = prev, tail = curr;
        while(n > 0) {
            ListNode temp = curr.next;
            curr.next = prev;
            prev = curr;
            curr = temp;
            n -= 1;
        }
        front.next = prev;
        tail.next = curr;
        return dummy.next;
    }

    /**
     * Approach 2: Recursion
     * Just like reverse an array recursively, we simply assign two pointers, and swap these values, and move the pointers to each other.
     * When these two pointers crossed, then we are done.
     *
     * In this problem, we take this idea and swap the values of two nodes in the given interval. In order to move two pointers,
     * we make the left pointer as a global variable to record its stage, and use backtracking to retrieve the previous stage of right
     * pointer. When the length of to be reversed list is odd, we stop swapping when left == right. otherwise, we will stop when right
     * pointer moves to the left of the left pointer, a.k.a right.next = left.
     *
     * Time: O(n), we typically do it by one-pass. However, in the worst, we may do it in two-pass
     * Space: O(1), only assigned pointers, no extra space required
     */
    private ListNode left;
    private boolean stop;

    public ListNode reverseBetweenRecursive(ListNode head, int m, int n) {
        this.left = head;
        this.stop = false;
        ListNode right = head;
        helper(right, m, n);
        return head;
    }

    private void helper(ListNode right, int m, int n) {
        //base case, if n == 1 we do nothing
        if(n == 1) return;
        //move the right pointer to the end of the to be reversed node
        right = right.next;
        //if m > 1, which means we haven't reached the starting node, move the left pointer as well
        if(m > 1) this.left = this.left.next;

        helper(right, m - 1, n - 1);
        //when two pointers cross, we stop swapping
        if(this.left == right || right.next == this.left) this.stop = true;
        //if the stopping criterion is not satisfied, swap the elements
        if(!this.stop) {
            int tmp = this.left.val;
            this.left.val = right.val;
            right.val = tmp;
            this.left = this.left.next;
        }
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
