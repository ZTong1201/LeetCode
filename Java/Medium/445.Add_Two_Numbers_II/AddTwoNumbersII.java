import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AddTwoNumbersII {

    /**
     * You are given two non-empty linked lists representing two non-negative integers. The most significant digit comes
     * first and each of their nodes contains a single digit. Add the two numbers and return the sum as a linked list.
     * <p>
     * You may assume the two numbers do not contain any leading zero, except the number 0 itself.
     * <p>
     * Constraints:
     * <p>
     * The number of nodes in each linked list is in the range [1, 100].
     * 0 <= Node.val <= 9
     * It is guaranteed that the list represents a number that does not have leading zeros.
     * <p>
     * 7 -> 2 -> 4 -> 3 -> null
     * 5 -> 6 -> 4 -> null
     * -------------------------
     * 7 -> 8 -> 0 -> 7 -> null
     * <p>
     * Approach 1: Reverse the list + construct from the output list
     * Reverse the input lists to make the least significant digits come first, while adding numbers on the fly, we need
     * to keep adding the newest node to the front of current list.
     * <p>
     * Time: O(n1 + n2)
     * Space: O(1) if the output list doesn't take into account
     */
    public ListNode addTwoNumbersReverse(ListNode l1, ListNode l2) {
        l1 = reverse(l1);
        l2 = reverse(l2);
        // always insert the newly created node to the front of output list
        // initialize the output list as an empty list first
        ListNode head = null;
        int carry = 0;
        while (l1 != null || l2 != null) {
            int x = l1 == null ? 0 : l1.val;
            int y = l2 == null ? 0 : l2.val;
            int sum = x + y + carry;
            carry = sum / 10;
            // create a new list node with desired value
            ListNode curr = new ListNode(sum % 10);
            // insert it to the front of current list
            curr.next = head;
            // update the head of current list
            head = curr;
            l1 = l1 == null ? null : l1.next;
            l2 = l2 == null ? null : l2.next;
        }
        // if there is still an outstanding carry value
        // append it to the front of the output too
        if (carry != 0) {
            ListNode curr = new ListNode(carry);
            curr.next = head;
            head = curr;
        }
        // return the head of output list
        return head;
    }

    private ListNode reverse(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode p = reverse(head.next);
        head.next.next = head;
        head.next = null;
        return p;
    }

    @Test
    public void addTwoNumbersReverseTest() {
        /**
         * Example 1:
         * Input: l1 = [7,2,4,3], l2 = [5,6,4]
         * Output: [7,8,0,7]
         */
        ListNode list1 = new ListNode(7, new ListNode(2, new ListNode(4, new ListNode(3))));
        ListNode list2 = new ListNode(5, new ListNode(6, new ListNode(4)));
        ListNode expected1 = new ListNode(7, new ListNode(8, new ListNode(0, new ListNode(7))));
        ListNode actual1 = addTwoNumbersReverse(list1, list2);
        while (expected1 != null) {
            assertEquals(expected1.val, actual1.val);
            expected1 = expected1.next;
            actual1 = actual1.next;
        }
        /**
         * Example 2:
         * Input: l1 = [2,4,3], l2 = [5,6,4]
         * Output: [8,0,7]
         */
        ListNode list3 = new ListNode(2, new ListNode(4, new ListNode(3)));
        ListNode list4 = new ListNode(5, new ListNode(6, new ListNode(4)));
        ListNode expected2 = new ListNode(8, new ListNode(0, new ListNode(7)));
        ListNode actual2 = addTwoNumbersReverse(list3, list4);
        while (expected2 != null) {
            assertEquals(expected2.val, actual2.val);
            expected2 = expected2.next;
            actual2 = actual2.next;
        }
        /**
         * Example 3:
         * Input: l1 = [0], l2 = [0]
         * Output: [0]
         */
        ListNode list5 = new ListNode(0);
        ListNode list6 = new ListNode(0);
        ListNode expected3 = new ListNode(0);
        ListNode actual3 = addTwoNumbersReverse(list5, list6);
        while (expected3 != null) {
            assertEquals(expected3.val, actual3.val);
            expected3 = expected3.next;
            actual3 = actual3.next;
        }
    }

    /**
     * Follow up: Could you solve it without reversing the input lists?
     * <p>
     * Approach 2: Do not reverse input lists
     * <p>
     * Time: O(n1 + n2)
     * Space: O(1)
     */
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        // first need to compute the lengths of two lists
        // such that we know when it's necessary to add values from both lists
        int len1 = 0, len2 = 0;
        ListNode ptr1 = l1, ptr2 = l2;
        while (ptr1 != null) {
            len1++;
            ptr1 = ptr1.next;
        }
        while (ptr2 != null) {
            len2++;
            ptr2 = ptr2.next;
        }

        // first compute the sum of two nodes which are at the same position
        // without taking care of the carry value
        // since the carry value will be handled later - we need to construct the result list
        // in the reverse way now - the carry value can be passed to next value in the next traversal
        ListNode head = null;
        ptr1 = l1;
        ptr2 = l2;
        // example:
        // 3 -> 3 -> 3 + 7 -> 7 => 3 -> 10 -> 10 => 10 -> 10 -> 3
        while (len1 != 0 && len2 != 0) {
            // compute the value at current node position
            int val = 0;
            // if l1 is longer than l2, then its value would have been carried to this position
            if (len1 >= len2) {
                val += ptr1.val;
                ptr1 = ptr1.next;
                // decrement len1
                len1--;
            }
            // if l2 is longer than l1, then its value should also be taken into account;
            if (len2 > len1) {
                val += ptr2.val;
                ptr2 = ptr2.next;
                // decrement len2
                len2--;
            }
            // construct a new list node and insert it to the front
            ListNode curr = new ListNode(val);
            curr.next = head;
            head = curr;
        }

        // now we need to take care of the carry value
        // since in the previous round, the output list was computed in the reverse order
        // this time it's necessary to reverse it back
        ptr1 = head;
        head = null;
        int carry = 0;
        while (ptr1 != null) {
            // construct a new list node by considering the carry value this time
            ListNode curr = new ListNode((ptr1.val + carry) % 10);
            carry = (ptr1.val + carry) / 10;
            // insert to the front of the output list
            curr.next = head;
            head = curr;
            // keep traversing the result list in the previous round
            ptr1 = ptr1.next;
        }
        // remember to handle outstanding carry value
        if (carry != 0) {
            ListNode curr = new ListNode(carry);
            curr.next = head;
            head = curr;
        }
        return head;
    }

    @Test
    public void addTwoNumbersTest() {
        /**
         * Example 1:
         * Input: l1 = [7,2,4,3], l2 = [5,6,4]
         * Output: [7,8,0,7]
         */
        ListNode list1 = new ListNode(7, new ListNode(2, new ListNode(4, new ListNode(3))));
        ListNode list2 = new ListNode(5, new ListNode(6, new ListNode(4)));
        ListNode expected1 = new ListNode(7, new ListNode(8, new ListNode(0, new ListNode(7))));
        ListNode actual1 = addTwoNumbers(list1, list2);
        while (expected1 != null) {
            assertEquals(expected1.val, actual1.val);
            expected1 = expected1.next;
            actual1 = actual1.next;
        }
        /**
         * Example 2:
         * Input: l1 = [2,4,3], l2 = [5,6,4]
         * Output: [8,0,7]
         */
        ListNode list3 = new ListNode(2, new ListNode(4, new ListNode(3)));
        ListNode list4 = new ListNode(5, new ListNode(6, new ListNode(4)));
        ListNode expected2 = new ListNode(8, new ListNode(0, new ListNode(7)));
        ListNode actual2 = addTwoNumbers(list3, list4);
        while (expected2 != null) {
            assertEquals(expected2.val, actual2.val);
            expected2 = expected2.next;
            actual2 = actual2.next;
        }
        /**
         * Example 3:
         * Input: l1 = [0], l2 = [0]
         * Output: [0]
         */
        ListNode list5 = new ListNode(0);
        ListNode list6 = new ListNode(0);
        ListNode expected3 = new ListNode(0);
        ListNode actual3 = addTwoNumbers(list5, list6);
        while (expected3 != null) {
            assertEquals(expected3.val, actual3.val);
            expected3 = expected3.next;
            actual3 = actual3.next;
        }
    }

    private static class ListNode {
        int val;
        ListNode next;

        ListNode(int x, ListNode next) {
            this.val = x;
            this.next = next;
        }

        ListNode(int x) {
            this.val = x;
        }
    }
}
