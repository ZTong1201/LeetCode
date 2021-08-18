import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class sortedListToBST {

    /**
     * Given a singly linked list where elements are sorted in ascending order, convert it to a height balanced BST.
     * <p>
     * For this problem, a height-balanced binary tree is defined as a binary tree in which the depth of the two subtrees of every
     * node never differ by more than 1.
     * <p>
     * Approach 1: Recursion
     * 为了构建平衡二叉树，需要每次取到list的终点，对于list去中点，可以用两个快慢指针，快指针每次移动两步，慢指针每次移动一步，当快指针指向链表最后一个元素时，
     * 慢指针指向链表终点。（当链表长度为偶数时，中间两元素的任意一个元素都可以做根节点）。然后只需recursively将链表的前半部分和后半部分带入function，继续
     * 搜索中点。因此递归的关键是要在中点前将链表断开。所以需要另一个prev指针，prev指针永远指向slow的前一个节点。当slow到达中点时，若prev不为空，则可将
     * prev.next赋成null，将前后链表断开，递归调用。
     * <p>
     * 递归的base case为，链表中只剩一个元素，即链表的中点就是链表本身，返回已经构造好的BST即可
     * <p>
     * Time: O(NlogN) 对于整个链表，找中点需要N/2 steps, 随后链表分割为两个长度为N/2的链表，找中点各需要N/4 steps, 以此往下分割，直到链表长度为1，总共
     * 需要分割logN次，所以overall runtime为 N/2 + 2 * N/4 + 4 * N/8 + 8 * N/16 ... = sum(from 1 to logN) 2^(i -1) * N / 2^i
     * = sum(from 1 to logN) N / 2 = N / 2 * logN = NlogN
     * Space: O(logN) 递归调用函数的call stack需要O(logN)空间，因为二叉树是平衡的
     */
    public TreeNode sortedListToBSTRecursive(ListNode head) {
        if (head == null) return null;
        //第一步，先找到链表中点，以此节点建立根节点
        ListNode mid = findMiddleNode(head);
        TreeNode res = new TreeNode(mid.val);

        //base case：判断此时链表中是否只剩下一个元素
        //若中点即为输入链表自身，二叉树已构建完毕，直接返回
        if (mid == head) {
            return res;
        }
        //递归建立左子树和右子树，此时链表已被mid分成两部分，前半部分链表的头仍为head，后半部分的链表头部为mid.next
        res.left = sortedListToBSTRecursive(head);
        res.right = sortedListToBSTRecursive(mid.next);
        return res;
    }

    private ListNode findMiddleNode(ListNode head) {
        //prev指针用来定位中点的前一个节点，用来disconnect链表
        ListNode prev = null, slow = head, fast = head;

        while (fast != null && fast.next != null) {
            prev = slow;
            slow = slow.next;
            fast = fast.next.next;
        }

        if (prev != null) {
            //若prev不为空指针，则将其下一个节点变为null即可disconnect前后链表
            prev.next = null;
        }
        //slow指向链表中点
        return slow;
    }

    @Test
    public void sortedListToBSTRecursiveTest() {
        /**
         * Example:
         * Given the sorted linked list: [-10,-3,0,5,9],
         *
         * One possible answer is: [0,-3,9,-10,null,5], which represents the following height balanced BST:
         *
         *       0
         *      / \
         *    -3   9
         *    /   /
         *  -10  5
         */
        ListNode list = new ListNode(-10);
        list.next = new ListNode(-3);
        list.next.next = new ListNode(0);
        list.next.next.next = new ListNode(5);
        list.next.next.next.next = new ListNode(9);
        TreeNode res = sortedListToBSTRecursive(list);
        List<Integer> expected = new ArrayList<>(Arrays.asList(0, -3, -10, 9, 5));
        List<Integer> actual = new ArrayList<>();
        bstToList(actual, res);
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    /**
     * Approach 2: Convert list node to array
     * 如果将链表存入一个arraylist，可以直接知道list的size，这样在寻找中点的时候只需要O(1)runtime即可，因此可以将方法1的时间减少为O(N)
     * <p>
     * Time: O(N) 只需遍历所有节点构建二叉树即可
     * Space: O(N) call stack还是需要O(logN)，但是在构建二叉树之前将所有的元素存入arraylist，需要额外的O(N)空间
     */
    public TreeNode sortedListToBSTToArray(ListNode head) {
        List<Integer> arr = new ArrayList<>();
        listToArray(arr, head);
        return buildTree(arr, 0, arr.size() - 1);
    }

    private TreeNode buildTree(List<Integer> arr, int left, int right) {
        if (left > right) return null;
        int mid = left + (right - left) / 2;
        TreeNode res = new TreeNode(arr.get(mid));
        res.left = buildTree(arr, left, mid - 1);
        res.right = buildTree(arr, mid + 1, right);
        return res;
    }

    private void listToArray(List<Integer> arr, ListNode head) {
        while (head != null) {
            arr.add(head.val);
            head = head.next;
        }
    }

    @Test
    public void sortedListToBSTToArrayTest() {
        /**
         * Example:
         * Given the sorted linked list: [-10,-3,0,5,9],
         *
         * One possible answer is: [0,-3,9,-10,null,5], which represents the following height balanced BST:
         *
         *       0                 0
         *      / \               / \
         *    -3   9    or     -10  5
         *    /   /              \   \
         *  -10  5              -3   9
         */
        ListNode list = new ListNode(-10);
        list.next = new ListNode(-3);
        list.next.next = new ListNode(0);
        list.next.next.next = new ListNode(5);
        list.next.next.next.next = new ListNode(9);
        TreeNode res = sortedListToBSTToArray(list);
        List<Integer> expected = new ArrayList<>(Arrays.asList(0, -10, -3, 5, 9));
        List<Integer> actual = new ArrayList<>();
        bstToList(actual, res);
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    /**
     * Approach 3: Inorder Simulation
     * 对BST进行中序遍历会得到一个sorted list。现在已有一个sorted list，可以将这个过程逆推回去。即链表的第一个元素对应BST的leftmost节点，第二个元素为
     * 该节点的根节点，以此类推。跟中序遍历类似，需要首先直到整个链表的长度，但是不用以链表中点首先建立根节点。而是继续递归调用直到链表的只剩一个元素。（跟
     * 中序遍历过程类似，需要一直遍历左子树，直到叶子节点），将该节点加入BST中。同时移动链表头部至下一个元素，再继续递归建立右子树
     * <p>
     * Time: O(N) 每个节点至遍历一次
     * Space: O(logN) 递归建立BST的call stack需要O(logN)空间
     */
    private ListNode node;

    public TreeNode sortedListToBSTInorder(ListNode head) {
        this.node = head;
        int size = findSize(head);
        return inorder(0, size - 1);
    }

    private TreeNode inorder(int low, int high) {
        if (low > high) return null;
        int mid = low + (high - low) / 2;
        //模拟中序遍历，需要最先建立leftmost节点
        TreeNode left = inorder(low, mid - 1);
        TreeNode res = new TreeNode(this.node.val);
        res.left = left;
        //移动链表至下一个节点
        this.node = this.node.next;
        res.right = inorder(mid + 1, high);
        return res;
    }

    private int findSize(ListNode head) {
        int res = 0;
        while (head != null) {
            res += 1;
            head = head.next;
        }
        return res;
    }

    @Test
    public void sortedListToBSTInorderTest() {
        /**
         * Example:
         * Given the sorted linked list: [-10,-3,0,5,9],
         *
         * One possible answer is: [0,-3,9,-10,null,5], which represents the following height balanced BST:
         *
         *       0                 0
         *      / \               / \
         *    -3   9    or     -10  5
         *    /   /              \   \
         *  -10  5              -3   9
         */
        ListNode list = new ListNode(-10);
        list.next = new ListNode(-3);
        list.next.next = new ListNode(0);
        list.next.next.next = new ListNode(5);
        list.next.next.next.next = new ListNode(9);
        TreeNode res = sortedListToBSTInorder(list);
        List<Integer> expected = new ArrayList<>(Arrays.asList(0, -10, -3, 5, 9));
        List<Integer> actual = new ArrayList<>();
        bstToList(actual, res);
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }


    private void bstToList(List<Integer> list, TreeNode tree) {
        if (tree == null) {
            return;
        }
        list.add(tree.val);
        bstToList(list, tree.left);
        bstToList(list, tree.right);
    }


    private class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            this.val = x;
        }
    }

    private class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            this.val = x;
        }
    }
}
