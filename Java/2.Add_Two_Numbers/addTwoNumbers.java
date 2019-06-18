import org.junit.Test;
import static org.junit.Assert.*;

public class addTwoNumbers {

    /**
     * You are given two non-empty linked lists representing two non-negative integers.
     * The digits are stored in reverse order and each of their nodes contain a single digit.
     * Add the two numbers and return it as a linked list.
     *
     * You may assume the two numbers do not contain any leading zero, except the number 0 itself.
     *
     * Just like sum two integer numbers, we begin by summing the least-significant digits. It should be noted that
     * summing two digits in range (0, 9) may lead to "overflow", e.g. 5 + 7 = 12, we will leave 2 at current node and carry 1
     * to the next iteration. The carry number can be either 0 or 1 since the largest possible sum of two digits is 1 + 9 + 9 = 19
     *
     * The algorithm looks like this:
     *
     * 1. Initialize a dummy node for returning list, set a pointer to the head of it
     * 2. Initialize the carry number as 0
     * 3. Get x value from list 1, if l1 == null, set x = 0;
     * 4. Get y value from list 2, if l2 == null, set y = 0;
     * 5. Compute the sum = x + y + carry num
     * 6. Add a new node with value sum % 10 (IMPORTANT!)
     * 7. Assign carry number as sum / 10 (either 0 or 1)
     * 8. Iterate to next node for both lists
     * 9. If both lists reach the end, check whether we should add one more node (check carry num == 1)
     * 10. Return dummy head's next node
     * 
     * Time: O(max(m, n)) we iterate over both nodes to the end, so the overall runtime is determined by the longest linked list
     * Space: O(max(m, n)), we create a new returning list which is at most of length max(m, n) + 1
     */
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode res = new ListNode(0);
        int carry = 0;
        ListNode ptr = res;
        while(l1 != null || l2 != null) {
            int x = l1 == null ? 0 : l1.val;
            int y = l2 == null ? 0 : l2.val;
            int sum = x + y + carry;
            ptr.next = new ListNode(sum % 10);
            ptr = ptr.next;
            carry = sum / 10;
            if(l1 != null) l1 = l1.next;
            if(l2 != null) l2 = l2.next;
        }
        if(carry == 1) ptr.next = new ListNode(1);
        return res.next;
    }

    @Test
    public void addTwoNumbersTest() {
        /**
         * Example 1:
         * Input: (2 -> 4 -> 3) + (5 -> 6 -> 4)
         * Output: 7 -> 0 -> 8
         * Explanation: 342 + 465 = 807.
         */
        ListNode num1 = new ListNode(2);
        num1.next = new ListNode(4);
        num1.next.next = new ListNode(3);
        ListNode num2 = new ListNode(5);
        num2.next = new ListNode(6);
        num2.next.next = new ListNode(4);
        int[] expected1 = new int[]{8, 0, 7};
        int[] actual1 = linkedListToArray(addTwoNumbers(num1, num2), 3);
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: (0 -> 1) + (0 -> 1 -> 2)
         * Output: 0 -> 2 -> 2
         * Explanation: 10 + 210 = 220.
         */
        ListNode num3 = new ListNode(0);
        num3.next = new ListNode(1);
        ListNode num4 = new ListNode(0);
        num4.next = new ListNode(1);
        num4.next.next = new ListNode(2);
        int[] expected2 = new int[]{2, 2, 0};
        int[] actual2 = linkedListToArray(addTwoNumbers(num3, num4), 3);
        assertArrayEquals(expected2, actual2);
        /**
         * Example 3:
         * Input: (1) + (9 -> 9)
         * Output: 0 -> 0 -> 1
         * Explanation: 1 + 99 = 100.
         */
        ListNode num5 = new ListNode(1);
        ListNode num6 = new ListNode(9);
        num6.next = new ListNode(9);
        int[] expected3 = new int[]{1, 0, 0};
        int[] actual3 = linkedListToArray(addTwoNumbers(num5, num6), 3);
        assertArrayEquals(expected3, actual3);
        /**
         * Example 4:
         * Input: (1) + (9 -> 8 -> 9)
         * Output: 0 -> 9 -> 9
         * Explanation: 1 + 989 = 990.
         */
        ListNode num7 = new ListNode(1);
        ListNode num8 = new ListNode(9);
        num8.next = new ListNode(8);
        num8.next.next = new ListNode(9);
        int[] expected4 = new int[]{9, 9, 0};
        int[] actual4 = linkedListToArray(addTwoNumbers(num7, num8), 3);
        assertArrayEquals(expected4, actual4);
    }

    private int[] linkedListToArray(ListNode head, int n) {
        int[] res = new int[n];
        for(int i = n - 1; i >= 0; i--) {
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
