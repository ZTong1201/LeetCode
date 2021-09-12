import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;

import static org.junit.Assert.assertEquals;

public class populateNextRightPointer {

    /**
     * You are given a perfect binary tree where all leaves are on the same level, and every parent has two children.
     * The binary tree has the following definition:
     * <p>
     * struct Node {
     * int val;
     * Node *left;
     * Node *right;
     * Node *next;
     * }
     * Populate each next pointer to point to its next right node. If there is no next right node, the next pointer should be set to NULL.
     * <p>
     * Initially, all next pointers are set to NULL.
     * <p>
     * Note:
     * <p>
     * You may only use constant extra space.
     * Recursive approach is fine, implicit stack space does not count as extra space for this problem.
     * <p>
     * Approach 1: Use Queue
     * 利用BFS，将每一层的节点存在queue里，从左到右遍历这一层的节点，将前一个节点的next指向下一个，若到了这一层的最后边节点。将该节点推出队列即可，无需后续
     * 操作
     * <p>
     * Time: O(N) 每个节点遍历一遍
     * Space: O(N) queue最大时候会将所有leaf节点存在queue中，leaf节点有N/2个，所以空间为O(N)
     */
    public Node connectQueue(Node root) {
        if (root == null) return null;

        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                Node curr = queue.poll();
                if (curr.left != null) {
                    queue.add(curr.left);
                }
                if (curr.right != null) {
                    queue.add(curr.right);
                }
                if (i == size - 1) break;
                curr.next = queue.peek();
            }
        }
        return root;
    }


    @Test
    public void connectQueueTest() {
        /**
         * Example:
         * Input:            1
         *                 /   \
         *                2     3
         *               / \   / \
         *              4  5  6  7
         * Output:            1   --> null
         *                  /   \
         *                 2 --> 3 --> null
         *                / \   / \
         *               4->5->6->7 --> null
         * Input: {"$id":"1","left":{"$id":"2","left":{"$id":"3","left":null,"next":null,"right":null,"val":4},"next":null,"right":
         * {"$id":"4","left":null,"next":null,"right":null,"val":5},"val":2},"next":null,"right":{"$id":"5","left":{"$id":"6","left":null,
         * "next":null,"right":null,"val":6},"next":null,"right":{"$id":"7","left":null,"next":null,"right":null,"val":7},"val":3},"val":1}
         *
         * Output: {"$id":"1","left":{"$id":"2","left":{"$id":"3","left":null,"next":{"$id":"4","left":null,"next":{"$id":"5","left":null,
         * "next":{"$id":"6","left":null,"next":null,"right":null,"val":7},"right":null,"val":6},"right":null,"val":5},"right":null,
         * "val":4},"next":{"$id":"7","left":{"$ref":"5"},"next":null,"right":{"$ref":"6"},"val":3},"right":{"$ref":"4"},"val":2},
         * "next":null,"right":{"$ref":"7"},"val":1}
         *
         * Explanation: Given the above perfect binary tree (Figure A), your function should populate each next pointer to point to
         * its next right node, just like in Figure B.
         */
        Node tree = new Node(1, new Node(2, new Node(4, null, null, null),
                new Node(5, null, null, null), null), new Node(3,
                new Node(6, null, null, null), new Node(7, null, null, null), null), null);
        Node actual = connectQueue(tree);
        assertEquals(null, actual.next);   // 1 --> null
        assertEquals(actual.right, actual.left.next); // 2 --> 3
        assertEquals(null, actual.right.next); // 3 --> null
        assertEquals(actual.left.right, actual.left.left.next); // 4 --> 5
        assertEquals(actual.right.left, actual.left.right.next); // 5 --> 6
        assertEquals(actual.right.right, actual.right.left.next); // 6 --> 7
        assertEquals(null, actual.right.right.next);  // 7 --> null
    }

    /**
     * Approach 2: Without extra space (Recursion)
     * 根据BFS思想，需要在当前层，处理其两个孩子节点。若当前节点不为leaf节点，即其存在node.left不为null，则node.left.next就是当前节点的右孩子（因为输入
     * 是perfect binary tree，若当前节点有左孩子，则一定有右孩子）。当前节点的右孩子的next节点要指向当前节点的next节点的左孩子（前提是当前节点的next节点
     * 不是null)
     * e.g.      1
     * /  \
     * 2    3
     * /  \  / \
     * 4   5 6   7
     * 在节点1时，只能将2指向3，因为1的next为null，所以其右孩子（3节点）的next节点无需分配。在节点2时，可以将4指向5，同时2的next节点为节点3，不为null，因此
     * 应将其右孩子（节点5）指向当前节点（节点2）的next节点（节点3）的左孩子（节点6）即可。递归调用每一层，并做分配，base case为，当输入节点为null时，返回null
     * <p>
     * Time: O(N) 遍历所有节点
     * Space: O(1) 若call stack不算空间，则只在当前节点考虑其两个孩子，无需额外空间
     */
    public Node connectRecursive(Node root) {
        //base case
        if (root == null) {
            return null;
        }

        //若当前节点的左孩子不为null（即不是leaf节点），将其左孩子的next指向其右孩子
        if (root.left != null) {
            root.left.next = root.right;
            //同时若当前节点的next节点不是null，可以同时更新其右孩子的next节点
            if (root.next != null) {
                root.right.next = root.next.left;
            }
        }

        //递归调用至下一层
        root.left = connectRecursive(root.left);
        root.right = connectRecursive(root.right);
        //最后返回修改好的二叉树
        return root;
    }

    @Test
    public void connectRecursiveTest() {
        /**
         * Example:
         * Input:            1
         *                 /   \
         *                2     3
         *               / \   / \
         *              4  5  6  7
         * Output:            1   --> null
         *                  /   \
         *                 2 --> 3 --> null
         *                / \   / \
         *               4->5->6->7 --> null
         * Input: {"$id":"1","left":{"$id":"2","left":{"$id":"3","left":null,"next":null,"right":null,"val":4},"next":null,"right":
         * {"$id":"4","left":null,"next":null,"right":null,"val":5},"val":2},"next":null,"right":{"$id":"5","left":{"$id":"6","left":null,
         * "next":null,"right":null,"val":6},"next":null,"right":{"$id":"7","left":null,"next":null,"right":null,"val":7},"val":3},"val":1}
         *
         * Output: {"$id":"1","left":{"$id":"2","left":{"$id":"3","left":null,"next":{"$id":"4","left":null,"next":{"$id":"5","left":null,
         * "next":{"$id":"6","left":null,"next":null,"right":null,"val":7},"right":null,"val":6},"right":null,"val":5},"right":null,
         * "val":4},"next":{"$id":"7","left":{"$ref":"5"},"next":null,"right":{"$ref":"6"},"val":3},"right":{"$ref":"4"},"val":2},
         * "next":null,"right":{"$ref":"7"},"val":1}
         *
         * Explanation: Given the above perfect binary tree (Figure A), your function should populate each next pointer to point to
         * its next right node, just like in Figure B.
         */
        Node tree = new Node(1, new Node(2, new Node(4, null, null, null),
                new Node(5, null, null, null), null), new Node(3,
                new Node(6, null, null, null), new Node(7, null, null, null), null), null);
        Node actual = connectRecursive(tree);
        assertEquals(null, actual.next);   // 1 --> null
        assertEquals(actual.right, actual.left.next); // 2 --> 3
        assertEquals(null, actual.right.next); // 3 --> null
        assertEquals(actual.left.right, actual.left.left.next); // 4 --> 5
        assertEquals(actual.right.left, actual.left.right.next); // 5 --> 6
        assertEquals(actual.right.right, actual.right.left.next); // 6 --> 7
        assertEquals(null, actual.right.right.next);  // 7 --> null
    }

    /**
     * Approach 3: Without extra space (Iterative)
     * 也可以将方法二转化为iteration。本质上，BFS就是遍历当前层，将下一层的节点连接起来，然后移动到下一层的最左侧起点。因此，可以在循环这一层开始前，将
     * 最左侧节点记录下来，下一层的遍历即是从最左侧节点的左孩子开始。循环当前这一层，需要把curr节点沿着next继续下去直到null即可
     * <p>
     * Time: O(N)
     * Space: O(1)
     */
    public Node connectIterative(Node root) {
        if (root == null) return null;
        //记录当前层的最左侧节点，下一次循环即是从prev.left开始
        Node prev = root;
        //循环每一层直到叶子节点
        while (prev.left != null) {
            //用curr指针遍历当前层的所有节点
            Node curr = prev;
            while (curr != null) {
                //注意在最开始判断时，已经排除了循环叶子节点层的可能，因此curr.left必然不为null，可以直接将curr.left的next指向curr.right
                curr.left.next = curr.right;
                //根据curr是否有下一个节点，决定curr的右孩子的next节点是否需要分配
                if (curr.next != null) {
                    curr.right.next = curr.next.left;
                }
                //继续更新当前层的下一个节点
                curr = curr.next;
            }
            //当前层循环结束后，遍历下一层
            prev = prev.left;
        }
        return root;
    }

    @Test
    public void connectIterativeTest() {
        /**
         * Example:
         * Input:            1
         *                 /   \
         *                2     3
         *               / \   / \
         *              4  5  6  7
         * Output:            1   --> null
         *                  /   \
         *                 2 --> 3 --> null
         *                / \   / \
         *               4->5->6->7 --> null
         * Input: {"$id":"1","left":{"$id":"2","left":{"$id":"3","left":null,"next":null,"right":null,"val":4},"next":null,"right":
         * {"$id":"4","left":null,"next":null,"right":null,"val":5},"val":2},"next":null,"right":{"$id":"5","left":{"$id":"6","left":null,
         * "next":null,"right":null,"val":6},"next":null,"right":{"$id":"7","left":null,"next":null,"right":null,"val":7},"val":3},"val":1}
         *
         * Output: {"$id":"1","left":{"$id":"2","left":{"$id":"3","left":null,"next":{"$id":"4","left":null,"next":{"$id":"5","left":null,
         * "next":{"$id":"6","left":null,"next":null,"right":null,"val":7},"right":null,"val":6},"right":null,"val":5},"right":null,
         * "val":4},"next":{"$id":"7","left":{"$ref":"5"},"next":null,"right":{"$ref":"6"},"val":3},"right":{"$ref":"4"},"val":2},
         * "next":null,"right":{"$ref":"7"},"val":1}
         *
         * Explanation: Given the above perfect binary tree (Figure A), your function should populate each next pointer to point to
         * its next right node, just like in Figure B.
         */
        Node tree = new Node(1, new Node(2, new Node(4, null, null, null),
                new Node(5, null, null, null), null), new Node(3,
                new Node(6, null, null, null), new Node(7, null, null, null), null), null);
        Node actual = connectIterative(tree);
        assertEquals(null, actual.next);   // 1 --> null
        assertEquals(actual.right, actual.left.next); // 2 --> 3
        assertEquals(null, actual.right.next); // 3 --> null
        assertEquals(actual.left.right, actual.left.left.next); // 4 --> 5
        assertEquals(actual.right.left, actual.left.right.next); // 5 --> 6
        assertEquals(actual.right.right, actual.right.left.next); // 6 --> 7
        assertEquals(null, actual.right.right.next);  // 7 --> null
    }

    private static class Node {
        int val;
        Node left;
        Node right;
        Node next;

        public Node(int x, Node _left, Node _right, Node _next) {
            this.val = x;
            this.left = _left;
            this.right = _right;
            this.next = _next;
        }
    }
}
