import org.junit.Test;
import static org.junit.Assert.*;

public class reorderList {

    /**
     * Given a singly linked list L: L0→L1→…→Ln-1→Ln,
     * reorder it to: L0→Ln→L1→Ln-1→L2→Ln-2→…
     *
     * You may not modify the values in the list's nodes, only nodes itself may be changed.
     *
     * Approach 1: Brute Force
     * 可以直接按照要求进行重排，对于每一个节点，一直遍历到链表末尾，然后将此节点从链表中断开，插入到当前节点之后。然后当前节点越过新的next节点，到再下一个
     * 节点，继续重复上述过程。直到base case
     * base case有三个，当前节点为null，当前节点的下一个节点为null，当前节点越过下一个节点的节点为null，如上三种情况都不需要再做任何chuli
     * e.g. null, 1 -> null, 1 -> 2 -> null
     * 值得注意的是，为了将末尾节点从链表中断开，在找寻末尾节点的是否，需要一直记录前一个节点，在找到末尾节点之后，将其前一个节点指向null，即可断开节点
     *
     * Time: O(n^2) 对于第一个节点需要遍历n - 1个剩余节点，第二个节点需要n - 3个剩余节点...最终总共需要遍历(n - 1)*(n / 2) / 2
     * Space: O(1) 只在循环过程中assign指针，无需额外空间
     */
    public void reorderListBruteForce(ListNode head) {
        //base case，这种情况无需重排
        if(head == null || head.next == null) return;
        //记录当前需重排节点
        ListNode curr = head;
        //考虑三种base case，任意一种情况下都无需继续重排
        while(curr != null && curr.next != null && curr.next.next != null) {
            //用两个指针找寻末尾节点以及末尾节点的上一个节点，用于disconnect链表
            ListNode prev = curr;
            ListNode ptr = curr.next;
            while(ptr != null && ptr.next != null) {
                prev = prev.next;
                ptr = ptr.next;
            }
            //找到末尾节点后，首先断掉链表
            prev.next = null;
            //然后将当前节点的下一个节点指向末尾节点，同时再把末尾节点指向后续链表
            //要用一个temp指针把后续链表记录下来
            ListNode temp = curr.next;
            curr.next = ptr;
            ptr.next = temp;
            //然后当前节点跳过其下一个节点，继续重排后续链表
            curr = curr.next.next;
        }
    }

    @Test
    public void reorderListBruteForceTest() {
        /**
         * Example 1:
         * Given 1->2->3->4, reorder it to 1->4->2->3.
         */
        ListNode list1 = new ListNode(1);
        list1.next = new ListNode(2);
        list1.next.next = new ListNode(3);
        list1.next.next.next = new ListNode(4);
        int[] expected1 = new int[]{1, 4, 2, 3};
        reorderListBruteForce(list1);
        int[] actual1 = listToArray(list1, 4);
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Given 1->2->3->4->5, reorder it to 1->5->2->4->3.
         */
        ListNode list2 = new ListNode(1);
        list2.next = new ListNode(2);
        list2.next.next = new ListNode(3);
        list2.next.next.next = new ListNode(4);
        list2.next.next.next.next = new ListNode(5);
        int[] expected2 = new int[]{1, 5, 2, 4, 3};
        reorderListBruteForce(list2);
        int[] actual2 = listToArray(list2, 5);
        assertArrayEquals(expected2, actual2);
    }

    /**
     * Approach 2: Find the middle + reverse the second half + merge two lists
     * 第一种方法时间复杂度为O(N), 可以通过将原始问题拆成三个小问题将runtime降为O(N)。通过观察发现，最终返回的结果就是将原始list拆分成前后两部分，再将后半
     * 部分倒置，然后一个一个插入进前半部分的两两节点之间。所以算法分为三个部分
     * 1.one-pass找寻中点（快慢指针），然后将链表断为前后两个部分    O(N)
     * 2.将后半部分链表reverse                        O(N)
     * 3.将两个链表merge起来，总是第一个链表的节点在前    O(N)
     * 因为算法的每一步都是O(N)，所以总的时间也是O(N)。在每一步中都只是简单的assign pointer，所以需要常数空间。
     * 值得注意的是，当链表长度为偶数时，快指针最后指向null，而慢指针指向需要断链表的起始点
     * e.g. 1 -> 2 -> 3 -> 4 -> null 最后slow会指向3，fast指向null。然而需要将从3开始的链表都断掉，所以还需要一个指针记录slow指针的前一个node
     * 当链表长度为奇数，快指针指向链表的最后一个元素，慢指针指向中点。
     * e.g. 1 -> 2 -> 3 -> 4 -> 5 -> null，结束时快指针指向5，慢指针指向3，所以需要将3之后的链表断掉即可。
     *
     * Time: O(N)
     * Space: O(1)
     */
    public void reorderListThreeSteps(ListNode head) {
        if(head == null || head.next == null) return;
        //第一步，寻找链表中点和断链表位置
        ListNode prev = null;
        ListNode slow = head;
        ListNode fast = head;
        while(fast != null && fast.next != null) {
            prev = slow;
            slow = slow.next;
            fast = fast.next.next;
        }
        //如果链表长度为偶数，则快指针最后指向null
        //此时慢指针指向的就是后半段链表的头部，但为了断掉链表，需要将prev指向null
        if(fast == null) {
            prev.next = null;
        } else { //若链表长度为奇数，则慢指针刚好指向中点，链表需要从中间断掉
            prev = prev.next;
            slow = slow.next;
            prev.next = null;
        }
        //第二步，将后半段链表reverse
        ListNode second = reverse(slow);
        //第三步，将前后两个链表merge起来，此时head中存的即是前半部分链表
        merge(head, second);
    }

    private void merge(ListNode l1, ListNode l2) {
        ListNode ptr = l1;
        while(ptr != null && l2 != null) {
            //要将后半段链表插入l1中，所以需要将l2的每个元素指向l1当前元素的下一个元素
            //然后将l2更新为其下一个元素，再更新l1，因为若assign了l2的下一个元素，原来的l2.next就不存在了
            //所以需要一个temp指针记录原来的l2.next
            ListNode temp = l2.next;
            l2.next = l1.next;
            l1.next = l2;
            l2 = temp;
            //l1需要跳过刚刚插入的元素
            l1 = l1.next.next;
        }
    }

    private ListNode reverse(ListNode head) {
        if(head == null || head.next == null) return head;
        ListNode prev = null;
        ListNode curr = head;
        while(curr != null) {
            ListNode temp = curr.next;
            curr.next = prev;
            prev = curr;
            curr = temp;
        }
        return prev;
    }

    @Test
    public void reorderListThreeStepsTest() {
        /**
         * Example 1:
         * Given 1->2->3->4, reorder it to 1->4->2->3.
         */
        ListNode list1 = new ListNode(1);
        list1.next = new ListNode(2);
        list1.next.next = new ListNode(3);
        list1.next.next.next = new ListNode(4);
        int[] expected1 = new int[]{1, 4, 2, 3};
        reorderListThreeSteps(list1);
        int[] actual1 = listToArray(list1, 4);
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Given 1->2->3->4->5, reorder it to 1->5->2->4->3.
         */
        ListNode list2 = new ListNode(1);
        list2.next = new ListNode(2);
        list2.next.next = new ListNode(3);
        list2.next.next.next = new ListNode(4);
        list2.next.next.next.next = new ListNode(5);
        int[] expected2 = new int[]{1, 5, 2, 4, 3};
        reorderListThreeSteps(list2);
        int[] actual2 = listToArray(list2, 5);
        assertArrayEquals(expected2, actual2);
    }

    private int[] listToArray(ListNode list1, int length) {
        int[] res = new int[length];
        for(int i = 0; i < length; i++) {
            res[i] = list1.val;
            list1 = list1.next;
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
