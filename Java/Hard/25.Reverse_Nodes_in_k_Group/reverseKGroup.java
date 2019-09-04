import org.junit.Test;
import static org.junit.Assert.*;

public class reverseKGroup {

    /**
     * Given a linked list, reverse the nodes of a linked list k at a time and return its modified list.
     *
     * k is a positive integer and is less than or equal to the length of the linked list. If the number of nodes is not a multiple of
     * k then left-out nodes in the end should remain as it is.
     *
     * Example:
     *
     * Given this linked list: 1->2->3->4->5
     *
     * For k = 2, you should return: 2->1->4->3->5
     *
     * For k = 3, you should return: 3->2->1->4->5
     *
     * Note:
     *
     * Only constant extra memory is allowed.
     * You may not alter the values in the list's nodes, only nodes itself may be changed.
     *
     * Approach 1: Recursion
     * 递归方法的主要思想是将链表先从正确的位置断开，即前半部分是k个节点，后半部分为剩余链表，对于前半部分，只需要对整个链表进行反转即可。对于后半部分，需要递归
     * 地对链表进行再分割，直到后半部分链表不足k个节点，则不需要再进行任何操作。之后再将前半段反转后的链表与后半段链表不断链接起来即可。
     *
     * Time: O(n) 需要遍历整个链表
     * Space: O(n/k) 递归的call stack需要额外空间存储n/k个小段
     */
    public ListNode reverseKGroupRecursive(ListNode head, int k) {
        if(head == null || head.next == null || k == 1) return head;
        ListNode node = head;
        ListNode remainNode = null;
        //因为要将k个节点从链表断开，需要先经过k - 1个节点，找到断开位置
        for(int i = 0; i < k - 1; i++) {
            node = node.next;
            //如果循环过程中node为null，说明余下部分已不足k个节点，不需要再额外分割，直接返回当前的链表head即可
            if(node == null) {
                return head;
            }
            //当遍历了k - 1个节点后
            if(i == k - 2) {
                //首先判断后续是否还有剩余节点
                if(node.next != null) {
                    remainNode = node.next;
                }
                //然后将前后节点断开
                node.next = null;
            }
        }
        //将前半段链表进行反转
        ListNode newHead = reverseRecursive(head);
        //后半段链表进行继续分割，然后再继续反转
        ListNode remainHead = reverseKGroupRecursive(remainNode, k);
        //然后将前后两段连接起来即可
        head.next = remainHead;
        //返回最开始的反转后链表的头部
        return newHead;
    }

    private ListNode reverseRecursive(ListNode head) {
        if(head == null || head.next == null) return head;
        ListNode res = reverseRecursive(head.next);
        head.next.next = head;
        head.next = null;
        return res;
    }

    @Test
    public void reverseKGroupRecursiveTest() {
        /**
         * Example 1:
         * Input: 1->2->3->4->5, k = 2
         * Output: 2->1->4->3->5
         */
        ListNode list1 = new ListNode(1);
        list1.next = new ListNode(2);
        list1.next.next = new ListNode(3);
        list1.next.next.next = new ListNode(4);
        list1.next.next.next.next = new ListNode(5);
        ListNode res1 = reverseKGroupRecursive(list1, 2);
        int[] expected1 = new int[]{2, 1, 4, 3, 5};
        int[] actual1 = listToArray(res1, 5);
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: 1->2->3->4->5, k = 3
         * Output: 3->2->1->4->5
         */
        ListNode list2 = new ListNode(1);
        list2.next = new ListNode(2);
        list2.next.next = new ListNode(3);
        list2.next.next.next = new ListNode(4);
        list2.next.next.next.next = new ListNode(5);
        ListNode res2 = reverseKGroupRecursive(list2, 3);
        int[] expected2 = new int[]{3, 2, 1, 4, 5};
        int[] actual2 = listToArray(res2, 5);
        assertArrayEquals(expected2, actual2);
    }

    /**
     * Approach 2: Iteration
     * 递归方法因为call stack会占用额外空间，严格意义上并不算是O(1)时间，因此考虑用循环方法。循环的算法中，同样需要找到本次要反转的链表的起始和终止位置。在
     * 反转前记录下终止位置很重要（因为反转后的新的起始位置就是上一次的终止位置），然后保持起始位置之前和终止位置之后的链表不动，将中间链表反转，然后再连接
     * 回去即可。
     *
     * Time: O(n)
     * Space: O(1)
     */
    public ListNode reverseKGroupIterative(ListNode head, int k) {
        if(head == null || head.next == null || k == 1) return head;
        //需要一个dummy节点放在初始链表的最前端
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode ptr = dummy;
        //因为同样要经过k - 1个节点，找到正确的剩余链表位置，count需要从1开始
        int count = 1;
        while(head != null) {
            //若经过了k - 1个节点，那么此时head所在的下一个位置就是终止位置
            if(count == k) {
                //此时需要重置count值
                count = 1;
                //然后将起始位置和终止位置之间的链表反转，并保持在这之前和之后的链表都不受影响
                //注意reverseBetween返回的是此次反转的链表的起始位置
                ptr = reverseBetween(ptr, head.next);
                //将head重新移动到此次反转链表的起始位置，因为后续要继续从这里跳过k - 1个节点
                head = ptr.next;
            } else {
                //在未经过k - 1个节点前，不断向前遍历即可
                count++;
                head = head.next;
            }
        }
        return dummy.next;
    }

    private ListNode reverseBetween(ListNode start, ListNode end) {
        //由于dummy节点存在，输入的链表头部的下一个节点才是真正的起始位置，同时记录下后半段的剩余链表，在反转后，仍需要连在最后
        ListNode curr = start.next, prev = end;
        //因为反转后仍需要返回此次反转的起始位置，以便后续跳过k - 1个节点，因此需要先将该位置记下
        ListNode res = start.next;
        //将中间链表进行反转
        while(curr != end) {
            ListNode temp = curr.next;
            curr.next = prev;
            prev = curr;
            curr = temp;
        }
        //此时prev记录的是反转链表的头部，并且原始的后半段链表仍然连接在反转的链表之后，只需将起始位置指向prev即可
        start.next = prev;
        //同时返回该起始位置以便后续跳过k - 1个节点
        return res;
    }

    @Test
    public void reverseKGroupIterativeTest() {
        /**
         * Example 1:
         * Input: 1->2->3->4->5, k = 2
         * Output: 2->1->4->3->5
         */
        ListNode list1 = new ListNode(1);
        list1.next = new ListNode(2);
        list1.next.next = new ListNode(3);
        list1.next.next.next = new ListNode(4);
        list1.next.next.next.next = new ListNode(5);
        ListNode res1 = reverseKGroupIterative(list1, 2);
        int[] expected1 = new int[]{2, 1, 4, 3, 5};
        int[] actual1 = listToArray(res1, 5);
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: 1->2->3->4->5, k = 3
         * Output: 3->2->1->4->5
         */
        ListNode list2 = new ListNode(1);
        list2.next = new ListNode(2);
        list2.next.next = new ListNode(3);
        list2.next.next.next = new ListNode(4);
        list2.next.next.next.next = new ListNode(5);
        ListNode res2 = reverseKGroupIterative(list2, 3);
        int[] expected2 = new int[]{3, 2, 1, 4, 5};
        int[] actual2 = listToArray(res2, 5);
        assertArrayEquals(expected2, actual2);
    }

    private int[] listToArray(ListNode head, int len) {
        int[] res = new int[len];
        for(int i = 0; i < len; i++) {
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
