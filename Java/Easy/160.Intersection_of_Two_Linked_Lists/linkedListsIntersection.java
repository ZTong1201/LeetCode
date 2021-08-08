import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class linkedListsIntersection {

    /**
     * Write a program to find the node at which the intersection of two singly linked lists begins.
     * For example, the following two linked lists:
     * A:         a1 -> a2
     *                    \
     *                    c1 -> c2 -> c3
     *                    /
     * B:  b1 -> b2 -> b3
     * begin to intersect at node c1.
     * <p>
     * Notes:
     * <p>
     * If the two linked lists have no intersection at all, return null.
     * The linked lists must retain their original structure after the function returns.
     * You may assume there are no cycles anywhere in the entire linked structure.
     * <p>
     * Approach 1: Hash Set
     * A naive approach is just iterate over one of the linked list (say headB) and add nodes in a hash set. Then we traverse through
     * another list (headA), If at any time, we reach a node has seen before. We return that node. Otherwise, return null.
     * <p>
     * Time: O(max(m, n)), the overall runtime is determined by the longest linked list
     * Space: O(m) or O(n), we need a hash set to record node seen so far in headA
     */
    public ListNode getIntersectionNodeHashSet(ListNode headA, ListNode headB) {
        Set<ListNode> seen = new HashSet<>();
        while (headB != null) {
            seen.add(headB);
            headB = headB.next;
        }

        while (headA != null) {
            if (seen.contains(headA)) return headA;
            headA = headA.next;
        }
        return null;
    }

    /**
     * Approach 2: Two Pointers
     * Assume the intersection is of length L (if two lists don't have an intersection, then the length is 0), hence the
     * length of list A is x + L (m) and length of list B is y + L (n). We can keep traversing across two lists, i.e. if
     * the traversal on list A is done - then we start traversing list B. Then when there is actually an intersection between
     * two lists. The two pointers will meet at the intersection point because now two pointers both travelled x + y + L.
     * If there is no intersection, these two pointers will hit null pointer at the same time. In other words, L is 0, and
     * both pointers have travelled x + y.
     * <p>
     * Time: O(m + n)
     * Space: O(1), we only assign pointers, it requires a constant size
     */
    public ListNode getIntersectionNodeTwoPointers(ListNode headA, ListNode headB) {
        ListNode ptr1 = headA, ptr2 = headB;

        // keep traversing as long as two pointers don't meet
        while (ptr1 != ptr2) {
            // if list 1 has been completely traversed - move  to list 2
            ptr1 = ptr1 == null ? headB : ptr1.next;
            // same thing happens for pointer 2
            ptr2 = ptr2 == null ? headA : ptr2.next;
        }
        // return the intersection (or it will be null if there is no intersection at all)
        return ptr1;
    }


    @Test
    public void getIntersectionNodeTwoPointersTest() {
        /**
         * Example 1:
         * Input: intersectVal = 8, listA = [4,1,8,4,5], listB = [5,0,1,8,4,5], skipA = 2, skipB = 3
         * Output: Reference of the node with value = 8
         * Input Explanation: The intersected node's value is 8 (note that this must not be 0 if the two lists intersect).
         * From the head of A, it reads as [4,1,8,4,5]. From the head of B, it reads as [5,0,1,8,4,5].
         * There are 2 nodes before the intersected node in A; There are 3 nodes before the intersected node in B.
         */
        ListNode intersection1 = new ListNode(8);
        intersection1.next = new ListNode(4);
        intersection1.next.next = new ListNode(5);
        ListNode listA1 = new ListNode(4);
        listA1.next = new ListNode(1);
        listA1.next.next = intersection1;
        ListNode expectedA1 = listA1;
        ListNode listB1 = new ListNode(5);
        listB1.next = new ListNode(0);
        listB1.next.next = new ListNode(1);
        listB1.next.next.next = intersection1;
        ListNode expectedB1 = listB1;
        ListNode actual1 = getIntersectionNodeTwoPointers(listA1, listB1);
        assertEquals(intersection1, actual1);
        //test non-destructive, listA and listB will remain unchanged after finding intersection
        assertEquals(expectedA1, listA1);
        assertEquals(expectedB1, listB1);
        /**
         * Example 2:
         * Input: intersectVal = 2, listA = [0,9,1,2,4], listB = [3,2,4], skipA = 3, skipB = 1
         * Output: Reference of the node with value = 2
         * Input Explanation: The intersected node's value is 2 (note that this must not be 0 if the two lists intersect).
         * From the head of A, it reads as [0,9,1,2,4]. From the head of B, it reads as [3,2,4].
         * There are 3 nodes before the intersected node in A; There are 1 node before the intersected node in B.
         */
        ListNode intersection2 = new ListNode(2);
        intersection2.next = new ListNode(4);
        ListNode listA2 = new ListNode(0);
        listA2.next = new ListNode(9);
        listA2.next.next = new ListNode(1);
        listA2.next.next.next = intersection2;
        ListNode expectedA2 = listA2;
        ListNode listB2 = new ListNode(3);
        listB2.next = intersection2;
        ListNode expectedB2 = listB2;
        ListNode actual2 = getIntersectionNodeTwoPointers(listA2, listB2);
        assertEquals(intersection2, actual2);
        //test non-destructive, listA and listB will remain unchanged after finding intersection
        assertEquals(expectedA2, listA2);
        assertEquals(expectedB2, listB2);
        /**
         * Example 3:
         * Input: intersectVal = 0, listA = [2,6,4], listB = [1,5], skipA = 3, skipB = 2
         * Output: null
         * Input Explanation: From the head of A, it reads as [2,6,4]. From the head of B, it reads as [1,5].
         * Since the two lists do not intersect, intersectVal must be 0, while skipA and skipB can be arbitrary values.
         * Explanation: The two lists do not intersect, so return null.
         */
        ListNode listA3 = new ListNode(2);
        listA3.next = new ListNode(6);
        listA3.next.next = new ListNode(4);
        ListNode expectedA3 = listA3;
        ListNode listB3 = new ListNode(1);
        listB3.next = new ListNode(5);
        ListNode expectedB3 = listB3;
        ListNode actual3 = getIntersectionNodeTwoPointers(listA3, listB3);
        assertEquals(null, actual3);
        //test non-destructive, listA and listB will remain unchanged after finding intersection
        assertEquals(expectedA3, listA3);
        assertEquals(expectedB3, listB3);
    }


    @Test
    public void getIntersectionNodeHashSetTest() {
        /**
         * Example 1:
         * Input: intersectVal = 8, listA = [4,1,8,4,5], listB = [5,0,1,8,4,5], skipA = 2, skipB = 3
         * Output: Reference of the node with value = 8
         * Input Explanation: The intersected node's value is 8 (note that this must not be 0 if the two lists intersect).
         * From the head of A, it reads as [4,1,8,4,5]. From the head of B, it reads as [5,0,1,8,4,5].
         * There are 2 nodes before the intersected node in A; There are 3 nodes before the intersected node in B.
         */
        ListNode intersection1 = new ListNode(8);
        intersection1.next = new ListNode(4);
        intersection1.next.next = new ListNode(5);
        ListNode listA1 = new ListNode(4);
        listA1.next = new ListNode(1);
        listA1.next.next = intersection1;
        ListNode expectedA1 = listA1;
        ListNode listB1 = new ListNode(5);
        listB1.next = new ListNode(0);
        listB1.next.next = new ListNode(1);
        listB1.next.next.next = intersection1;
        ListNode expectedB1 = listB1;
        ListNode actual1 = getIntersectionNodeHashSet(listA1, listB1);
        assertEquals(intersection1, actual1);
        //test non-destructive, listA and listB will remain unchanged after finding intersection
        assertEquals(expectedA1, listA1);
        assertEquals(expectedB1, listB1);
        /**
         * Example 2:
         * Input: intersectVal = 2, listA = [0,9,1,2,4], listB = [3,2,4], skipA = 3, skipB = 1
         * Output: Reference of the node with value = 2
         * Input Explanation: The intersected node's value is 2 (note that this must not be 0 if the two lists intersect).
         * From the head of A, it reads as [0,9,1,2,4]. From the head of B, it reads as [3,2,4].
         * There are 3 nodes before the intersected node in A; There are 1 node before the intersected node in B.
         */
        ListNode intersection2 = new ListNode(2);
        intersection2.next = new ListNode(4);
        ListNode listA2 = new ListNode(0);
        listA2.next = new ListNode(9);
        listA2.next.next = new ListNode(1);
        listA2.next.next.next = intersection2;
        ListNode expectedA2 = listA2;
        ListNode listB2 = new ListNode(3);
        listB2.next = intersection2;
        ListNode expectedB2 = listB2;
        ListNode actual2 = getIntersectionNodeHashSet(listA2, listB2);
        assertEquals(intersection2, actual2);
        //test non-destructive, listA and listB will remain unchanged after finding intersection
        assertEquals(expectedA2, listA2);
        assertEquals(expectedB2, listB2);
        /**
         * Example 3:
         * Input: intersectVal = 0, listA = [2,6,4], listB = [1,5], skipA = 3, skipB = 2
         * Output: null
         * Input Explanation: From the head of A, it reads as [2,6,4]. From the head of B, it reads as [1,5].
         * Since the two lists do not intersect, intersectVal must be 0, while skipA and skipB can be arbitrary values.
         * Explanation: The two lists do not intersect, so return null.
         */
        ListNode listA3 = new ListNode(2);
        listA3.next = new ListNode(6);
        listA3.next.next = new ListNode(4);
        ListNode expectedA3 = listA3;
        ListNode listB3 = new ListNode(1);
        listB3.next = new ListNode(5);
        ListNode expectedB3 = listB3;
        ListNode actual3 = getIntersectionNodeHashSet(listA3, listB3);
        assertEquals(null, actual3);
        //test non-destructive, listA and listB will remain unchanged after finding intersection
        assertEquals(expectedA3, listA3);
        assertEquals(expectedB3, listB3);
    }

    private static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            this.val = x;
        }
    }
}
