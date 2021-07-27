import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class sortList {

    /**
     * Sort a linked list in O(n log n) time using constant space complexity.
     * <p>
     * Approach 1: Quick Sort
     * 与对数组进行quick sort类似，每次选择第一个元素作为pivot value，然后一个fast指针从head.next扫到tail，若遇到大于pivot的节点，就跳过，找寻下一个
     * 小于等于pivot的节点，然后将两位置互换。最后节点会停在大于和小于pivot的交界处，然后将pivot与当前位置再次进行互换，pivot就处在正确的位置。然后递归
     * 调用前半段和后半段，继续排序。
     * <p>
     * Time: O(nlogn)
     * Space: O(1) 如果不考虑call stack，quicksort不需要额外空间
     */
    public ListNode sortListQuickSort(ListNode head) {
        quickSort(head, null);
        return head;
    }

    private void quickSort(ListNode head, ListNode tail) {
        //base case, 当只有一个元素，或链表为null，不需要继续排序
        if (head == tail) {
            return;
        }

        ListNode slow = head, fast = head.next;
        int pivot = head.val;
        while (fast != tail) {
            //从head.next到tail之间，将小于和大于pivot的node排好
            if (fast.val <= pivot) {
                slow = slow.next;
                swap(slow, fast);
            }
            //当遇到大于pivot的节点，继续寻找下一个小于pivot的节点，然后交换两位置
            fast = fast.next;
        }
        //最后将pivot value交换到正确的位置
        swap(head, slow);
        quickSort(head, slow);
        quickSort(slow.next, tail);
    }

    private void swap(ListNode node1, ListNode node2) {
        int temp = node1.val;
        node1.val = node2.val;
        node2.val = temp;
    }


    @Test
    public void sortListQuickSortTest() {
        /**
         * Example 1:
         * Input: 4->2->1->3
         * Output: 1->2->3->4
         */
        ListNode list1 = new ListNode(4);
        list1.next = new ListNode(2);
        list1.next.next = new ListNode(1);
        list1.next.next.next = new ListNode(3);
        ListNode res1 = sortListQuickSort(list1);
        int[] expected1 = new int[]{1, 2, 3, 4};
        int[] actual1 = listToArray(res1, 4);
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: -1->5->3->4->0
         * Output: -1->0->3->4->5
         */
        ListNode list2 = new ListNode(-1);
        list2.next = new ListNode(5);
        list2.next.next = new ListNode(3);
        list2.next.next.next = new ListNode(4);
        list2.next.next.next.next = new ListNode(0);
        ListNode res2 = sortListQuickSort(list2);
        int[] expected2 = new int[]{-1, 0, 3, 4, 5};
        int[] actual2 = listToArray(res2, 5);
        assertArrayEquals(expected2, actual2);
    }

    private int[] listToArray(ListNode list, int length) {
        int[] res = new int[length];
        for (int i = 0; i < length; i++) {
            res[i] = list.val;
            list = list.next;
        }
        return res;
    }

    /**
     * Approach 2: Merge Sort (top down)
     * 或者可以用merge sort。merge sort的本质是每次找寻链表中点，然后断开前后两段，然后递归调用函数，不断找寻两段的中点，直到链表中只有一个元素。
     * 最后一步即是merge两个已经排好序的链表即可
     * <p>
     * Time: O(nlogn)
     * Space: O(logn) call stack is of size logn
     */

    public ListNode sortListMergeSort(ListNode head) {
        //此函数的目的是递归地找到链表的中点，然后断掉前后两段，直到链表中仅剩一个元素
        if (head == null || head.next == null) return head;
        ListNode slow = head, fast = head.next;
        // 找到链表中点
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        //断掉链表
        ListNode secondHalf = slow.next;
        slow.next = null;
        ListNode first = sortListMergeSort(head);
        ListNode second = sortListMergeSort(secondHalf);
        return mergeIterative(first, second);
    }

    //merge两个sorted链表可以用iteration也可以用recursion
    private ListNode mergeRecursive(ListNode l1, ListNode l2) {
        ListNode res;
        if (l1 == null) return l2;
        if (l2 == null) return l1;
        if (l1.val <= l2.val) {
            res = l1;
            res.next = mergeRecursive(l1.next, l2);
        } else {
            res = l2;
            res.next = mergeRecursive(l1, l2.next);
        }
        return res;
    }

    private ListNode mergeIterative(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode ptr = dummy;
        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) {
                ptr.next = l1;
                l1 = l1.next;
            } else {
                ptr.next = l2;
                l2 = l2.next;
            }
            ptr = ptr.next;
        }
        ptr.next = l1 == null ? l2 : l1;
        return dummy.next;
    }

    @Test
    public void sortListMergeSortTest() {
        /**
         * Example 1:
         * Input: 4->2->1->3
         * Output: 1->2->3->4
         */
        ListNode list1 = new ListNode(4);
        list1.next = new ListNode(2);
        list1.next.next = new ListNode(1);
        list1.next.next.next = new ListNode(3);
        ListNode res1 = sortListMergeSort(list1);
        int[] expected1 = new int[]{1, 2, 3, 4};
        int[] actual1 = listToArray(res1, 4);
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: -1->5->3->4->0
         * Output: -1->0->3->4->5
         */
        ListNode list2 = new ListNode(-1);
        list2.next = new ListNode(5);
        list2.next.next = new ListNode(3);
        list2.next.next.next = new ListNode(4);
        list2.next.next.next.next = new ListNode(0);
        ListNode res2 = sortListMergeSort(list2);
        int[] expected2 = new int[]{-1, 0, 3, 4, 5};
        int[] actual2 = listToArray(res2, 5);
        assertArrayEquals(expected2, actual2);
    }

    /**
     * Approach 3 Merge sort (with extra space)
     * <p>
     * Time: O(nlogn)
     * Space: O(n)
     */
    public ListNode sortListMergeSortExtraSpace(ListNode head) {
        return mergeSort(head, null);
    }

    // find midpoint of current list segment
    private ListNode findMidpoint(ListNode head, ListNode end) {
        ListNode slow = head, fast = head;
        while (fast != end && fast.next != end) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    private ListNode mergeSort(ListNode start, ListNode end) {
        // to mimic the merge sort behavior - return a new listNode (or null) in the base case
        if (start == end) return start == null ? null : new ListNode(start.val);
        // find midpoint of current list
        ListNode mid = findMidpoint(start, end);
        // divide and conquer
        ListNode left = mergeSort(start, mid);
        ListNode right = mergeSort(mid.next, end);
        // reuse the merge two sorted list capability
        return mergeIterative(left, right);
    }


    @Test
    public void sortListMergeSortExtraSpaceTest() {
        /**
         * Example 1:
         * Input: 4->2->1->3
         * Output: 1->2->3->4
         */
        ListNode list1 = new ListNode(4);
        list1.next = new ListNode(2);
        list1.next.next = new ListNode(1);
        list1.next.next.next = new ListNode(3);
        ListNode res1 = sortListMergeSortExtraSpace(list1);
        int[] expected1 = new int[]{1, 2, 3, 4};
        int[] actual1 = listToArray(res1, 4);
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: -1->5->3->4->0
         * Output: -1->0->3->4->5
         */
        ListNode list2 = new ListNode(-1);
        list2.next = new ListNode(5);
        list2.next.next = new ListNode(3);
        list2.next.next.next = new ListNode(4);
        list2.next.next.next.next = new ListNode(0);
        ListNode res2 = sortListMergeSortExtraSpace(list2);
        int[] expected2 = new int[]{-1, 0, 3, 4, 5};
        int[] actual2 = listToArray(res2, 5);
        assertArrayEquals(expected2, actual2);
    }

    /**
     * Approach 4: Merge Sort (bottom up)
     * <p>
     * In order to omit the implicit space usage of recursion stack, we can use iteration to realize bottom up merge sort.
     * Essentially, it keeps merging sorted lists of size 1, 2, 4, ..., n / 2 and the final merged list is sorted.
     * <p>
     * The algorithm looks like this:
     * 1. Keep splitting two lists of size n out of the current list, ALWAYS disconnect the sub-list from the original list
     * 2. Merge two sorted list and append it to the previous (partially) sorted list, update the new tail of sorted list,
     * hence the next merged list can be appended
     * 3. Increase the size by multiplying 2 and redo the first two steps until the whole is sorted
     * <p>
     * Time: O(nlogn)
     * Space: O(1) use iteration to achieve bottom up merge sort, the recursion call stack is not needed anymore
     */
    public ListNode sortListMergeSortBottomUp(ListNode head) {
        if (head == null || head.next == null) return head;
        // traverse the entire list to get the length - O(n)
        // hence we know when the bottom up algorithm would stop
        int length = 1;
        ListNode ptr = head;
        while (ptr != null) {
            length++;
            ptr = ptr.next;
        }

        // create a dummy node such that sorted list can be appended afterwards
        ListNode dummy = new ListNode();
        // first append the original list to the dummy node
        dummy.next = head;

        // bottom up merge sort - iteration
        for (int i = 1; i < length; i *= 2) { // i *= 2 guarantees the loop would end in logn times
            // the sort always starts at the head of the entire list
            ListNode curr = dummy.next;
            // tail.next will always be the starting point of next round of merge
            // sort hasn't started yet, hence the tail will point to the dummy node
            ListNode tail = dummy;
            // this iteration will be complete when the entire list has been merged sorted with size i sub-lists
            while (curr != null) {
                ListNode l1 = curr;
                // split the first i nodes from the current list
                // l2 is the rest of the list now
                ListNode l2 = split(curr, i);
                // split another i nodes from l2
                // the rest of list will be the starting point for next merge
                // hence assigning the rest to the curr variable
                curr = split(l2, i);
                // merge list 1 and list 2 to get the merged list and the tail of merged list
                ListNode[] merged = merge(l1, l2);
                // append merged list to the tail of previous merged list
                tail.next = merged[0];
                // update tail to be the end of merged list hence newly merged list can be appended later
                tail = merged[1];
            }
        }
        // once the entire bottom up merge sort is done, the sorted list will be appended to the dummy node
        return dummy.next;
    }

    /**
     * Split current list by skipping first n nodes and return the head of the rest list.
     *
     * @param head the head of list to be split
     * @param n    nodes to be skipped
     * @return the rest of list after split
     */
    private ListNode split(ListNode head, int n) {
        // by skipping n nodes, we actually want to skip first n - 1 nodes
        // then assign n-th node.next to null such that it's detached from the original list
        while (n > 1 && head != null) {
            head = head.next;
            n--;
        }
        // return the rest of list (if any)
        ListNode rest = head == null ? null : head.next;
        // detach the first n nodes from the list (if possible)
        if (head != null) head.next = null;
        // return the rest of list
        return rest;
    }

    /**
     * Merge two sorted list iteratively and return an array of ListNodes, where the first item is the head of
     * merged list and the second item is the tail of merged node
     *
     * @param l1 sort list 1
     * @param l2 sort list 2
     * @return [head of merged list, tail of merged list]
     */
    private ListNode[] merge(ListNode l1, ListNode l2) {
        // create a dummy node to connect two sorted lists
        ListNode dummy = new ListNode();
        ListNode ptr = dummy;
        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) {
                ptr.next = l1;
                l1 = l1.next;
            } else {
                ptr.next = l2;
                l2 = l2.next;
            }
            ptr = ptr.next;
        }
        ptr.next = (l1 == null) ? l2 : l1;
        // keep traversing the list until the last node (if any)
        while (ptr.next != null) ptr = ptr.next;
        // return the head and the tail of merged list
        return new ListNode[]{dummy.next, ptr};
    }

    @Test
    public void sortListMergeSortBottomUpTest() {
        /**
         * Example 1:
         * Input: 4->2->1->3
         * Output: 1->2->3->4
         */
        ListNode list1 = new ListNode(4);
        list1.next = new ListNode(2);
        list1.next.next = new ListNode(1);
        list1.next.next.next = new ListNode(3);
        ListNode res1 = sortListMergeSortBottomUp(list1);
        int[] expected1 = new int[]{1, 2, 3, 4};
        int[] actual1 = listToArray(res1, 4);
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: -1->5->3->4->0
         * Output: -1->0->3->4->5
         */
        ListNode list2 = new ListNode(-1);
        list2.next = new ListNode(5);
        list2.next.next = new ListNode(3);
        list2.next.next.next = new ListNode(4);
        list2.next.next.next.next = new ListNode(0);
        ListNode res2 = sortListMergeSortBottomUp(list2);
        int[] expected2 = new int[]{-1, 0, 3, 4, 5};
        int[] actual2 = listToArray(res2, 5);
        assertArrayEquals(expected2, actual2);
    }

    private static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int x) {
            this.val = x;
        }
    }
}
