import org.junit.Test;
import static org.junit.Assert.*;

public class partitionList {

    /**
     * Given a linked list and a value x, partition it such that all nodes less than x come before nodes greater than or equal to x.
     *
     * You should preserve the original relative order of the nodes in each of the two partitions.
     *
     * Approach: Two Pointers
     * 本质上，此题是要将整个链表reform，在保留原有顺序的基础上，使得小于x的节点在前，大于等于x的节点在后。因此可以考虑是将原链表分割为两个链表，然后将前后两个
     * 链表在进行连接即可。
     * 例如，对于输入链表1 -> 4 -> 3 -> 2 -> 5 -> 2
     * 需要将原链表分割成
     * first = 1 -> 2 -> 2
     * second = 4 -> 3 -> 5
     * 再将前后连接起来即可。为了不开辟额外空间，可以只利用指针不断分配原链表的节点（意味着该节点之后的所有节点也会随之跟着一起分配），因此在此例中，最后
     * first的结果不变，second的真实结果为4 -> 3 -> 5 -> 2，此时需要将节点5之后的所有节点舍去即可。同时对于其他情况下，first后面可能也有多有节点，此时只需要
     * 再将first.next与second连接起来即可。为了不需要额外考虑edge case，可以先将first和second前设置一个dummy节点，最后返回first.next即可（即跳过dummy节点）
     *
     * Time: O(n) 只遍历了整个链表一次
     * Space: O(1) 只是在分配原链表的节点，无需额外空间
     */
    public ListNode partition(ListNode head, int x) {
        //先建立两个dummy节点，用于记录前后段节点
        ListNode first = new ListNode(-1);
        ListNode second = new ListNode(-1);
        //分别设置两个指针指向两个dummy节点的头部
        ListNode p1 = first, p2 = second;

        //遍历整个链表，按节点值大小分配进first和second
        while (head != null) {
            if (head.val < x) {
                p1.next = head;
                p1 = p1.next;
            } else {
                p2.next = head;
                p2 = p2.next;
            }
            head = head.next;
        }
        //分配完毕后，两个分割的链表后面可能有多余节点，先将second的多余节点移除
        p2.next = null;
        //然后将first和second连接起来，将first中的多余不符合条件节点移除
        p1.next = second.next;
        //由于dummy节点的存在，需要返回first.next
        return first.next;
    }


    @Test
    public void partitionTest() {
        /**
         * Example:
         * Input: head = 1->4->3->2->5->2, x = 3
         * Output: 1->2->2->4->3->5
         */
        ListNode head = new ListNode(1);
        head.next = new ListNode(4);
        head.next.next = new ListNode(3);
        head.next.next.next = new ListNode(2);
        head.next.next.next.next = new ListNode(5);
        head.next.next.next.next.next = new ListNode(2);
        ListNode res = partition(head, 3);
        int[] actual = listToArray(res, 6);
        int[] expected = new int[]{1, 2, 2, 4, 3, 5};
        assertArrayEquals(expected, actual);
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
