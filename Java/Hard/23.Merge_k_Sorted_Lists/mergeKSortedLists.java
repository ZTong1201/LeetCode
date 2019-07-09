import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class mergeKSortedLists {

    /**
     * Merge k sorted linked lists and return it as one sorted list. Analyze and describe its complexity.
     *
     * Approach 1: Brute force
     * We can iterate over all the lists in the array and store values into list. We then traverse the resulting
     * list and add them into a new Linked List
     * Similarly, we can add all the values into a min heap, and remove from min heap will always give us the smallest element
     * The running time and space will be exactly the same.
     *
     * Time: O(NlogN), where N is the total number of nodes.
     *      1. Iterate over all the nodes O(N)
     *      2. Typical sorting algorithm costs O(NlogN) or likewise, remove smallest elements from a min heap
     *      3. Iterate over the value list to build a new list O(N)
     * Space: O(2N) = O(N) we need a list (or a min heap) to store all the values (N elements)
     *       and a new linked list
     */
    public ListNode mergeKListsBruteForce(ListNode[] lists) {
        List<Integer> values = new ArrayList<>();
        for(int i = 0; i < lists.length; i++) {
            ListNode list = lists[i];
            while(list != null) {
                values.add(list.val);
                list = list.next;
            }
        }
        Collections.sort(values);
        ListNode res = new ListNode(0);
        ListNode ptr = res;
        for(int i : values) {
            ptr.next = new ListNode(i);
            ptr = ptr.next;
        }
        return res.next;
    }

    /**
     * Approach 2: Compare one by one
     * We can merge these lists as the same as merge two sorted lists. For each time,
     * we find the smallest value among all the head values, then we move one step further ONLY for that list.
     * When all lists reach the end (null), break out of the loop and return the final list
     *
     * Time: O(kN) where k is the number of lists, N is the total number of nodes. Almost every selection of node in final linked list
     *      cost O(k) time, there are N nodes in total.
     * Space: O(N), we need a new output linked list. O(1) if we do it in-place
     */
    public ListNode mergeKListsCompareOneByOne(ListNode[] lists) {
        ListNode res = new ListNode(0);
        ListNode ptr = res;
        int minIndex = 0;
        while(true) {
            boolean isBreak = true;
            int minValue = Integer.MAX_VALUE;
            for(int i = 0; i < lists.length; i++) {
                if(lists[i] != null) {
                    if(lists[i].val < minValue) {
                        minValue = lists[i].val;
                        minIndex = i;
                    }
                    isBreak = false;
                }
            }

            if(isBreak) break;
            ptr.next = new ListNode(minValue);
            ptr = ptr.next;
            lists[minIndex] = lists[minIndex].next;
        }
        return res.next;
    }

    /**
     * Approach 3: Using Priority Queue
     * As stated in approach 2, we always remove the smallest element at each time, which cost O(k) time, then add a new one.
     * We can use a priority queue to remove the smallest, the runtime will be reduced to O(logk) for removal.
     *
     * Time: O(Nlogk), where k is the number of lists, N is the total number of nodes. Removing smallest element from a priority queue
     *      cost O(logk), we need to add N nodes into a final list.
     * Space: O(N) for a new linked list, O(k) if we do it in-place, however we still need k positions for a priority queue.
     */
    public ListNode mergeKListsPriorityQueue(ListNode[] lists) {
        Comparator<ListNode> listNodeComparator = (ListNode l1, ListNode l2) -> { return l1.val - l2.val; };
        PriorityQueue<ListNode> minPQ = new PriorityQueue<>(listNodeComparator);
        for(ListNode list: lists) {
            if(list != null) minPQ.add(list);
        }
        ListNode res = new ListNode(0);
        ListNode ptr = res;
        while(!minPQ.isEmpty()) {
            ListNode smallestNode = minPQ.poll(); // 1 -> 4 -> 5
            ptr.next = smallestNode; // res: 0 -> 1 -> 4 -> 5
            ptr = ptr.next; // ptr: 1 -> 4 -> 5
            if(ptr.next != null) minPQ.add(ptr.next); // check whether ptr.next is null, in this case 4 -> 5, add to minPQ
        }
        return res.next;
    }

    /**
     * Approach 4: merge lists one by one
     * We can merge two lists at a time, the total number of merge is simply (k - 1).
     *
     * Time: O(kN), where k is the number of lists, N is the total number of nodes. We can assume all the lists have the same length,
     *      hence each list has length N/k. The first time we merge two lists cost N/k + N/k, second time: 2N/k + N/k, ....
     *      The total number of merge is sum i from 1 to k - 1: i * (N/k) + N/k, treat N/k as a constant, the overall runtime is
     *      approximate to O(kN)
     * Space: O(1), we can merge two lists without extra space
     */
    private ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        if(l1 == null) return l2;
        if(l2 == null) return l1;
        if(l1.val <= l2.val) {
            l1.next = mergeTwoLists(l1.next, l2);
            return l1;
        } else {
            l2.next = mergeTwoLists(l1, l2.next);
            return l2;
        }
    }

    public ListNode mergeKListsOneByOne(ListNode[] lists) {
        ListNode res = null;
        res = lists.length == 0 ? null : mergeTwoLists(res, lists[0]);
        for(int i = 1; i < lists.length; i++) {
            res = mergeTwoLists(res, lists[i]);
        }
        return res;
    }

    /**
     * Approach 5: Merge with Divide and Conquer
     * We actually don't need to merge (k - 1) times to obtain a final list. Based on divide and conquer thought, we can actually
     * ONLY merge logk times.
     *
     * Time: O(Nlogk)
     * Space: O(1) we can merge two lists without extra space
     */
    public ListNode mergeKListsDivideAndConquer(ListNode[] lists) {
        int interval = 1;
        while(interval < lists.length) {
            for(int i = 0; i + interval < lists.length; i += interval*2) {
                lists[i] = mergeTwoLists(lists[i], lists[i + interval]);
            }
            interval *= 2;
        }
        return lists.length > 0 ? lists[0] : null;
    }

    @Test
    public void mergeKListsBruteForceTest() {
        /**
         * Example:
         * Input:
         * [
         *   1->4->5,
         *   1->3->4,
         *   2->6
         * ]
         * Output: 1->1->2->3->4->4->5->6
         */
        ListNode list1 = new ListNode(1);
        list1.next = new ListNode(4);
        list1.next.next = new ListNode(5);
        ListNode list2 = new ListNode(1);
        list2.next = new ListNode(3);
        list2.next.next = new ListNode(4);
        ListNode list3 = new ListNode(2);
        list3.next = new ListNode(6);
        ListNode[] lists = new ListNode[]{list1, list2, list3};
        ListNode actual = mergeKListsBruteForce(lists);
        int[] expected = new int[]{1, 1, 2, 3, 4, 4, 5, 6};
        assertArrayEquals(expected, linkedListToArray(actual, 8));
    }

    @Test
    public void mergeKListsCompareOneByOneTest() {
        /**
         * Example:
         * Input:
         * [
         *   1->4->5,
         *   1->3->4,
         *   2->6
         * ]
         * Output: 1->1->2->3->4->4->5->6
         */
        ListNode list1 = new ListNode(1);
        list1.next = new ListNode(4);
        list1.next.next = new ListNode(5);
        ListNode list2 = new ListNode(1);
        list2.next = new ListNode(3);
        list2.next.next = new ListNode(4);
        ListNode list3 = new ListNode(2);
        list3.next = new ListNode(6);
        ListNode[] lists = new ListNode[]{list1, list2, list3};
        ListNode actual = mergeKListsCompareOneByOne(lists);
        int[] expected = new int[]{1, 1, 2, 3, 4, 4, 5, 6};
        assertArrayEquals(expected, linkedListToArray(actual, 8));
    }

    @Test
    public void mergeKListsPriorityQueueTest() {
        /**
         * Example:
         * Input:
         * [
         *   1->4->5,
         *   1->3->4,
         *   2->6
         * ]
         * Output: 1->1->2->3->4->4->5->6
         */
        ListNode list1 = new ListNode(1);
        list1.next = new ListNode(4);
        list1.next.next = new ListNode(5);
        ListNode list2 = new ListNode(1);
        list2.next = new ListNode(3);
        list2.next.next = new ListNode(4);
        ListNode list3 = new ListNode(2);
        list3.next = new ListNode(6);
        ListNode[] lists = new ListNode[]{list1, list2, list3};
        ListNode actual = mergeKListsPriorityQueue(lists);
        int[] expected = new int[]{1, 1, 2, 3, 4, 4, 5, 6};
        assertArrayEquals(expected, linkedListToArray(actual, 8));
    }

    @Test
    public void mergeKListsOneByOneTest() {
        /**
         * Example:
         * Input:
         * [
         *   1->4->5,
         *   1->3->4,
         *   2->6
         * ]
         * Output: 1->1->2->3->4->4->5->6
         */
        ListNode list1 = new ListNode(1);
        list1.next = new ListNode(4);
        list1.next.next = new ListNode(5);
        ListNode list2 = new ListNode(1);
        list2.next = new ListNode(3);
        list2.next.next = new ListNode(4);
        ListNode list3 = new ListNode(2);
        list3.next = new ListNode(6);
        ListNode[] lists = new ListNode[]{list1, list2, list3};
        ListNode actual = mergeKListsOneByOne(lists);
        int[] expected = new int[]{1, 1, 2, 3, 4, 4, 5, 6};
        assertArrayEquals(expected, linkedListToArray(actual, 8));
    }

    @Test
    public void mergeKListsDivideAndConquerTest() {
        /**
         * Example:
         * Input:
         * [
         *   1->4->5,
         *   1->3->4,
         *   2->6
         * ]
         * Output: 1->1->2->3->4->4->5->6
         */
        ListNode list1 = new ListNode(1);
        list1.next = new ListNode(4);
        list1.next.next = new ListNode(5);
        ListNode list2 = new ListNode(1);
        list2.next = new ListNode(3);
        list2.next.next = new ListNode(4);
        ListNode list3 = new ListNode(2);
        list3.next = new ListNode(6);
        ListNode[] lists = new ListNode[]{list1, list2, list3};
        ListNode actual = mergeKListsDivideAndConquer(lists);
        int[] expected = new int[]{1, 1, 2, 3, 4, 4, 5, 6};
        assertArrayEquals(expected, linkedListToArray(actual, 8));
    }

    private int[] linkedListToArray(ListNode head, int n) {
        int[] res = new int[n];
        for(int i = 0; i < res.length; i++) {
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
