import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class populateNextRightPointerII {

    /**
     * You are given a perfect binary tree where all leaves are on the same level, and every parent has two children.
     * The binary tree has the following definition:
     *
     * struct Node {
     *   int val;
     *   Node *left;
     *   Node *right;
     *   Node *next;
     * }
     * Populate each next pointer to point to its next right node. If there is no next right node, the next pointer should be set to NULL.
     *
     * Initially, all next pointers are set to NULL.
     *
     * Note:
     *
     * You may only use constant extra space.
     * Recursive approach is fine, implicit stack space does not count as extra space for this problem.
     *
     * Approach 1: Using Queue (BFS)
     * 用queue将每个level的节点存下来，从左至右assign next指针即可
     *
     * Time: O(N) 遍历每个节点
     * Space: O(N) queue最大的时候会存储所有leaf节点，最坏情况下，需要O(N) space
     */
    public Node connectQueue(Node root) {
        if(root == null) return null;
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        while(!queue.isEmpty()) {
            int size = queue.size();
            Node prev = queue.poll();
            if(prev.left != null) queue.add(prev.left);
            if(prev.right != null) queue.add(prev.right);

            for(int i = 0; i < size - 1; i++) {
                Node curr = queue.poll();
                prev.next = curr;
                prev = curr;
                if(curr.left != null) queue.add(curr.left);
                if(curr.right != null) queue.add(curr.right);
            }
        }
        return root;
    }

    @Test
    public void connectQueueTest() {
        /**
         * Example:
         * Input:             1
         *                  /   \
         *                 2    3
         *                / \    \
         *               4  5    7
         * Output:            1  --> null
         *                  /  \
         *                 2 -> 3  --> null
         *                / \    \
         *               4->5 --> 7 --> null
         * Input: {"$id":"1","left":{"$id":"2","left":{"$id":"3","left":null,"next":null,"right":null,"val":4},"next":null,
         * "right":{"$id":"4","left":null,"next":null,"right":null,"val":5},"val":2},"next":null,"right":{"$id":"5",
         * "left":null,"next":null,"right":{"$id":"6","left":null,"next":null,"right":null,"val":7},"val":3},"val":1}
         *
         * Output: {"$id":"1","left":{"$id":"2","left":{"$id":"3","left":null,"next":{"$id":"4","left":null,"next":{"$id":"5",
         * "left":null,"next":null,"right":null,"val":7},"right":null,"val":5},"right":null,"val":4},"next":{"$id":"6","left":null,
         * "next":null,"right":{"$ref":"5"},"val":3},"right":{"$ref":"4"},"val":2},"next":null,"right":{"$ref":"6"},"val":1}
         *
         * Explanation: Given the above binary tree (Figure A), your function should populate each next pointer to point to its next
         * right node, just like in Figure B.
         */
        Node tree = new Node(1, new Node(2, new Node(4, null, null, null),
                new Node(5, null, null, null), null), new Node(3, null,
                new Node(7, null, null, null), null), null);
        Node actual = connectQueue(tree);
        assertEquals(null, actual.next);
        assertEquals(actual.right, actual.left.next);
        assertEquals(actual.left.right, actual.left.left.next);
        assertEquals(actual.right.right, actual.left.right.next);
        assertEquals(null, actual.right.right.next);
    }

    /**
     * Approach 2: Without extra space (Recursion)
     *
     * 此题的本质仍是BFS，在当前level，把它的下一层level全部排好即可。需要注意的是。可以把当前层的所有元素看成一个链表，将其从左至右连接起来。在每一层的
     * 最前端建一个dummy node更好操作。若当前节点存在左孩子和右孩子，就把他们按顺序连接在dummy node之后，然后遍历当前level的所有节点，直到null。即可将
     * 当前level的下一level排好，下一循环应该从下一level的最左侧节点开始，此节点存储在dummy node之后，可以快速定位。
     * 用Recursion，在连接下一层level之前定义好dummy node，当重新递归调用函数时，会建立新的dummy node。
     *
     * Time: O(N)
     * Space: O(1)
     */
    public Node connectRecursive(Node root) {
        helper(root);
        return root;
    }

    private void helper(Node root) {
        if(root == null) return;
        //建立dummy node
        Node temp = new Node();
        Node curr = temp;
        //遍历当前level，将其下一个level的节点连接好
        while(root != null) {
            //查看当前节点的左右孩子，若任意孩子存在，将其连接在当前level的链表上
            if(root.left != null) {
                curr.next = root.left;
                curr = curr.next;
            }
            if(root.right != null) {
                curr.next = root.right;
                curr = curr.next;
            }
            //移动到当前level的下一个节点
            root = root.next;
        }
        //递归调用函数，下一层的开始节点为下一level的最左侧节点
        //此节点保存在dummy node的下一节点
        helper(temp.next);
    }

    @Test
    public void connectRecursiveTest() {
        /**
         * Example:
         * Input:             1
         *                  /   \
         *                 2    3
         *                / \    \
         *               4  5    7
         * Output:            1  --> null
         *                  /  \
         *                 2 -> 3  --> null
         *                / \    \
         *               4->5 --> 7 --> null
         * Input: {"$id":"1","left":{"$id":"2","left":{"$id":"3","left":null,"next":null,"right":null,"val":4},"next":null,
         * "right":{"$id":"4","left":null,"next":null,"right":null,"val":5},"val":2},"next":null,"right":{"$id":"5",
         * "left":null,"next":null,"right":{"$id":"6","left":null,"next":null,"right":null,"val":7},"val":3},"val":1}
         *
         * Output: {"$id":"1","left":{"$id":"2","left":{"$id":"3","left":null,"next":{"$id":"4","left":null,"next":{"$id":"5",
         * "left":null,"next":null,"right":null,"val":7},"right":null,"val":5},"right":null,"val":4},"next":{"$id":"6","left":null,
         * "next":null,"right":{"$ref":"5"},"val":3},"right":{"$ref":"4"},"val":2},"next":null,"right":{"$ref":"6"},"val":1}
         *
         * Explanation: Given the above binary tree (Figure A), your function should populate each next pointer to point to its next
         * right node, just like in Figure B.
         */
        Node tree = new Node(1, new Node(2, new Node(4, null, null, null),
                new Node(5, null, null, null), null), new Node(3, null,
                new Node(7, null, null, null), null), null);
        Node actual = connectRecursive(tree);
        assertEquals(null, actual.next);
        assertEquals(actual.right, actual.left.next);
        assertEquals(actual.left.right, actual.left.left.next);
        assertEquals(actual.right.right, actual.left.right.next);
        assertEquals(null, actual.right.right.next);
    }

    /**
     * Approach 3: Without extra space (Iterative)
     *
     * Iteration方法同理，只是将上述recursion变为iteration。因为不会重复调用函数，所以在开始排布下一level时，不会新生成一个dummy node，所以需要将
     * 上一层的dummy node与后续节点断开，重新开始排布再下一层节点即可。
     *
     * Time: O(N)
     * Space: O(1)
     */
    public Node connectIterative(Node root) {
        if(root == null) return null;
        //需要三个指针，curr - 遍历整个树，记录当前节点
        //temp - 一个dummy node，当前level链表的头部节点, prev - 遍历当前level的链表，将新节点加在其后
        Node curr = root;
        Node temp = new Node();
        Node prev = temp;
        //与recursion一样，要遍历完所有节点
        while(curr != null) {
            if(curr.left != null) {
                prev.next = curr.left;
                prev = prev.next;
            }
            if(curr.right != null) {
                prev.next = curr.right;
                prev = prev.next;
            }
            curr = curr.next;
            //上述过程与recursion完全相同，后续需要手动设置新的起始节点来代替递归调用函数
            //若curr为null，说明当前层已被遍历完毕，需要遍历下一层的开始节点
            if(curr == null) {
                //下一层的开始节点存储在dummy node之后
                curr = temp.next;
                //将dummy node断开，重新串联下一层节点
                prev = temp;
                temp.next = null;
            }
        }
        return root;
    }

    @Test
    public void connectIterativeTest() {
        /**
         * Example:
         * Input:             1
         *                  /   \
         *                 2    3
         *                / \    \
         *               4  5    7
         * Output:            1  --> null
         *                  /  \
         *                 2 -> 3  --> null
         *                / \    \
         *               4->5 --> 7 --> null
         * Input: {"$id":"1","left":{"$id":"2","left":{"$id":"3","left":null,"next":null,"right":null,"val":4},"next":null,
         * "right":{"$id":"4","left":null,"next":null,"right":null,"val":5},"val":2},"next":null,"right":{"$id":"5",
         * "left":null,"next":null,"right":{"$id":"6","left":null,"next":null,"right":null,"val":7},"val":3},"val":1}
         *
         * Output: {"$id":"1","left":{"$id":"2","left":{"$id":"3","left":null,"next":{"$id":"4","left":null,"next":{"$id":"5",
         * "left":null,"next":null,"right":null,"val":7},"right":null,"val":5},"right":null,"val":4},"next":{"$id":"6","left":null,
         * "next":null,"right":{"$ref":"5"},"val":3},"right":{"$ref":"4"},"val":2},"next":null,"right":{"$ref":"6"},"val":1}
         *
         * Explanation: Given the above binary tree (Figure A), your function should populate each next pointer to point to its next
         * right node, just like in Figure B.
         */
        Node tree = new Node(1, new Node(2, new Node(4, null, null, null),
                new Node(5, null, null, null), null), new Node(3, null,
                new Node(7, null, null, null), null), null);
        Node actual = connectIterative(tree);
        assertEquals(null, actual.next);
        assertEquals(actual.right, actual.left.next);
        assertEquals(actual.left.right, actual.left.left.next);
        assertEquals(actual.right.right, actual.left.right.next);
        assertEquals(null, actual.right.right.next);
    }


    private class Node {
        int val;
        Node left;
        Node right;
        Node next;

        public Node() {}

        public Node(int x, Node _left, Node _right, Node _next) {
            this.val = x;
            this.left = _left;
            this.right = _right;
            this.next = _next;
        }
    }
}
