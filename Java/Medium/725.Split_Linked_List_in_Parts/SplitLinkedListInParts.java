import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SplitLinkedListInParts {

    /**
     * Given the head of a singly linked list and an integer k, split the linked list into k consecutive linked list parts.
     * <p>
     * The length of each part should be as equal as possible: no two parts should have a size differing by more than one.
     * This may lead to some parts being null.
     * <p>
     * The parts should be in the order of occurrence in the input list, and parts occurring earlier should always have a
     * size greater than or equal to parts occurring later.
     * <p>
     * Return an array of the k parts.
     * <p>
     * Constraints:
     * <p>
     * The number of nodes in the list is in the range [0, 1000].
     * 0 <= Node.val <= 1000
     * 1 <= k <= 50
     * <p>
     * Approach: List manipulation
     * The key part of this problem is to first get the size of the input list. Then the first (size % k) sub-list will have
     * one extra nodes comparing with the rest of the list. The other important factor is since we need to disconnect the list
     * at certain position, for example, the first list has 4 nodes, then starting from the head of the list, we should stop
     * when we move the pointer 3 times. Then we can disconnect the list by setting the next pointer to null.
     * <p>
     * Time: O(n) we need to traverse the entire list twice
     * Space: O(k)
     */
    public ListNode[] splitListToParts(ListNode head, int k) {
        int size = getListSize(head);
        ListNode[] res = new ListNode[k];

        // the first (size % k) will have one extra node, the rest of the sub-list will always have size / k nodes
        int numOfNodesPerList = size / k, extraNodes = size % k;

        ListNode ptr = head;
        // the iteration will stop if we split the list into k parts
        // or reach the end of the list
        for (int i = 0; i < k && ptr != null; i++) {
            // assign the head of the list to current index
            res[i] = ptr;

            // then find the tail of the current segment
            // when extraNodes is greater than 0, we need to skip one more node
            for (int j = 0; j < numOfNodesPerList + (extraNodes > 0 ? 1 : 0) - 1; j++) {
                ptr = ptr.next;
            }

            ListNode next = ptr.next;
            // disconnect the current segment with the rest of the list
            ptr.next = null;
            ptr = next;

            // decrement extraNodes since we assign one more node to the current segment
            extraNodes--;
        }
        return res;
    }

    private int getListSize(ListNode head) {
        int size = 0;
        while (head != null) {
            size++;
            head = head.next;
        }
        return size;
    }

    @Test
    public void splitListToPartsTest() {
        /**
         * Example 1:
         * Input: head = [1,2,3], k = 5
         * Output: [[1],[2],[3],[],[]]
         * Explanation:
         * The first element output[0] has output[0].val = 1, output[0].next = null.
         * The last element output[4] is null, but its string representation as a ListNode is [].
         */
        ListNode list1 = new ListNode(1, new ListNode(2, new ListNode(3)));
        ListNode[] expected1 = new ListNode[]{new ListNode(1), new ListNode(2), new ListNode(3), null, null};
        ListNode[] actual1 = splitListToParts(list1, 5);
        assertEquals(expected1.length, actual1.length);
        for (int i = 0; i < expected1.length; i++) {
            assertEquals(convertToList(expected1[i]), convertToList(actual1[i]));
        }
        /**
         * Example 2:
         * Input: head = [1,2,3,4,5,6,7,8,9,10], k = 3
         * Output: [[1,2,3,4],[5,6,7],[8,9,10]]
         * Explanation:
         * The input has been split into consecutive parts with size difference at most 1, and earlier parts are a larger
         * size than the later parts.
         */
        ListNode list2 = new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(4, new ListNode(5,
                new ListNode(6, new ListNode(7, new ListNode(8, new ListNode(9, new ListNode(10))))))))));
        ListNode[] expected2 = new ListNode[]{new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(4)))),
                new ListNode(5, new ListNode(6, new ListNode(7))), new ListNode(8, new ListNode(9, new ListNode(10)))};
        ListNode[] actual2 = splitListToParts(list2, 3);
        assertEquals(expected2.length, actual2.length);
        for (int i = 0; i < expected2.length; i++) {
            assertEquals(convertToList(expected2[i]), convertToList(actual2[i]));
        }
    }

    private List<Integer> convertToList(ListNode head) {
        List<Integer> res = new ArrayList<>();
        while (head != null) {
            res.add(head.val);
            head = head.next;
        }
        return res;
    }

    private static class ListNode {
        int val;
        ListNode next;

        public ListNode(int x) {
            this.val = x;
        }

        public ListNode(int x, ListNode next) {
            this.val = x;
            this.next = next;
        }
    }
}
