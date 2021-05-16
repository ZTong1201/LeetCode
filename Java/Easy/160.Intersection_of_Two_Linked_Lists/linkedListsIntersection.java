import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class linkedListsIntersection {

    /**
     * Write a program to find the node at which the intersection of two singly linked lists begins.
     * For example, the following two linked lists:
     * A:         a1 -> a2
     *                    \
     *                    c1 -> c2 -> c3
     *                   /
     * B:  b1 -> b2 -> b3
     * begin to intersect at node c1.
     *
     * Notes:
     *
     * If the two linked lists have no intersection at all, return null.
     * The linked lists must retain their original structure after the function returns.
     * You may assume there are no cycles anywhere in the entire linked structure.
     *
     * Approach 1: Hash Set
     * A naive approach is just iterate over one of the linked list (say headA) and add nodes in a hash set. Then we traverse through
     * another list (headB), If at any time, we reach a node has seen before. We return that node. Otherwise, return null.
     *
     * Time: O(max(m, n)), the overall runtime is determined by the longest linked list
     * Space: O(m) or O(n), we need a hash set to record node seen so far in headA
     */
    public ListNode getIntersectionNodeHashSet(ListNode headA, ListNode headB) {
        ListNode ptrA = headA;
        ListNode ptrB = headB;
        Set<ListNode> nodeSeen = new HashSet<>();
        while(ptrA != null) {
            nodeSeen.add(ptrA);
            ptrA = ptrA.next;
        }
        while(ptrB != null) {
            if(nodeSeen.contains(ptrB)) return ptrB;
            ptrB = ptrB.next;
        }
        return null;
    }

    /**
     * Approach 2: Two Pointers
     * We can find the intersection without extra space by using two pointers. In this case, we need two passes.
     * The first pass, we iterate over two lists separately to record the length of each list, and check whether they have different tails
     * If the tails are different, it indicates that they don't have an intersection, return null directly. Otherwise, we traverse a second
     * pass to find the intersection.
     * Ideally, if we have two lists with the same length, we move from the head one step a time, by the time they reach the same node,
     * that is exactly the intersection. In other cases, we make the longer list skip the first abs(lenA- lenB) nodes so that they have
     * the same length. We can move one step at a time now to find the intersection.
     *
     * Time: O(max(m, n))
     *      First pass: we traverse two lists separately, the runtime is determined by the longest list
     *      Skip nodes: the longest list will skip the first abs(lenA - lenB) nodes
     *      Second pass: Two lists will iterate over the same steps. In the worst case, it equals to the smallest length of two lists. O(min(m, n))
     *      Overall, it is O(max(m, n))
     * Space: O(1), we only assign pointers, it requires a constant size
     */
    public ListNode getIntersectionNodeTwoPointers(ListNode headA, ListNode headB) {
        if(headA == null || headB == null) return null; //check corner case
        ListNode ptrA = headA;
        ListNode ptrB = headB;
        int lenA = 1;
        int lenB = 1;
        /**
         * First pass: we will know
         * 1. The lengths of two lists
         * 2. The tail nodes for each list
         * 3. If the tail nodes are different, we simply return null.
         */
        while(ptrA.next != null) {
            ptrA = ptrA.next;
            lenA += 1;
        }
        while(ptrB.next != null) {
            ptrB = ptrB.next;
            lenB += 1;
        }
        if(ptrA != ptrB) return null;

        int skip = Math.abs(lenA - lenB); //determine skip steps for the longer list
        //reassign pointers for second pass
        ptrA = headA;
        ptrB = headB;
        if(lenA > lenB) {
            while(skip > 0) {
                ptrA = ptrA.next;
                skip -= 1;
            }
        } else {
            while(skip > 0) {
                ptrB = ptrB.next;
                skip -= 1;
            }
        }

        while(ptrA != ptrB) {
            ptrA = ptrA.next;
            ptrB = ptrB.next;
        }
        return ptrA;
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
