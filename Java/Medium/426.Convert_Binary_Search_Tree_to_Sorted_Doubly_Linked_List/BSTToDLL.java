import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class BSTToDLL {
    /**
     * Convert a BST to a sorted circular doubly-linked list in-place.
     * Think of the left and right pointers as synonymous to the previous and next pointers in a doubly-linked list.
     * <p>
     * Let's take the following BST as an example, it may help you understand the problem better:
     *            4
     *          /  \
     *         2    5
     *        / \
     *       1   3
     * We want to transform this BST into a circular doubly linked list. Each node in a doubly linked list has a predecessor and successor.
     * For a circular doubly linked list, the predecessor of the first element is the last element,
     * and the successor of the last element is the first element.
     * <p>
     * The figure below shows the circular doubly linked list for the BST above.
     * The "head" symbol means the node it points to is the smallest element of the linked list.
     * <p>
     * head --
     * |
     * 1 -->  2 --> 3 --> 4 --> 5
     * ^ <--    <--   <--   <-- ^
     * |                        |
     * ------------------------
     * Specifically, we want to do the transformation in place. After the transformation, the left pointer of the tree node should
     * point to its predecessor, and the right pointer should point to its successor.
     * We should return the pointer to the first element of the linked list.
     * <p>
     * Approach 1: Inorder + New Node
     * In order to construct a sorted data structure from a BST, we need to do an inorder traversal. We can construct a new Doubly-linked
     * list (the Node class in this problem), and assign a pointer to the head of the DLL, as we inorder traverse the entire tree. We
     * insert new node to the DLL, and adjust the left and right node
     * <p>
     * Time: O(n), we need to traverse all the tree nodes
     * Space: O(H + n), we construct a new DLL, and the call stack require up to the height of tree space. In the worst case, it is O(n),
     * and O(logn) on average.
     */
    private Node head = new Node(0, null, null);   //a dummy node of the head of DLL
    private Node ptr = head;   //assign a pointer to the DLL

    public Node treeToDoublyListNewNode(Node root) {
        if (root == null) return null;
        treeToDoublyListNewNode(root.left);
        insertToDLL(root.val);
        treeToDoublyListNewNode(root.right);
        return head.right;
    }

    private void insertToDLL(int val) {
        //we need a new node first
        Node newNode = new Node(val, null, null);
        //insert the node into the current DLL
        //if ptr.right is null, which means we have nothing in our final DLL, we simply treat the new node as the start of DLL
        //otherwise, simply insert it at the end of the DLL
        newNode.left = ptr == head ? ptr.right : ptr;
        newNode.right = head.right;
        ptr.right = newNode;
        head.right.left = newNode;
        //we need to move the pointer one step ahead in order to point to the tail of the current DLL
        ptr = ptr.right;
    }

    /**
     * Approach 2: Inorder + In-place
     * Since the problem requires to convert the BST to a DLL in-place. We don't actually need a new DLL. Since the DLL and the tree
     * has the same definition in this case, as we inorder traversing the entire tree, we can keep track of the tail of the DLL ONLY
     * to insert new node into the DLL. Meanwhile, we record the head of the DLL. After the traversal completed, we link the head and the
     * tail together to obtain the desired result. The algorithm looks like this:
     * 1. Initialize the first and the last node as null
     * 2. As we inorder traverse the BST, if the last node is null (which means there is nothing in our final DLL), we assign the first
     * node to it, and never change the first node, since it records the head of our DLL
     * 3. If the last node is not null, we simply link the new node with our last node (a.k.a. the current tail of our DLL)
     * 4. Always move the last node one step ahead to keep track of the tail of the DLL
     * 5. When the traversal is done, link the first and the last node together
     * <p>
     * Time: O(n) visit all the nodes once
     * Space: O(H), where H is the height of the tree. Since we do it in-place, no extra space for the result DLL. However, the call stack
     * requires O(H) space to traverse the entire tree. It will be degenerated to O(N) in the worst case, and will be O(logn) on average
     */
    private Node first;
    private Node last;

    public Node treeToDoublyListInPlace(Node root) {
        if (root == null) return null;
        first = null;
        last = null;
        helper(root);
        first.left = last;
        last.right = first;
        return first;
    }

    private void helper(Node root) {
        if (root == null) return;
        //traverse the left
        helper(root.left);

        if (last != null) {
            //which means we have at least one node in our final DLL, link the new node with the last node
            last.right = root;
            root.left = last;
        } else {
            //otherwise, there is nothing in the DLL already, simply assign first node to it
            first = root;
        }
        last = root;    //always assign the current node to the last node, i.e. keep track of the tail of the DLL

        //then, traverse the right
        helper(root.right);
    }

    @Test
    public void treeToDoublyListInPlaceTest() {
        /**
         * Example:
         * Input: root = [4,2,5,1,3]
         * Output: [1,2,3,4,5]
         *
         * Explanation: The figure below shows the transformed BST. The solid line indicates the successor relationship,
         * while the dashed line means the predecessor relationship.
         *                          4
         *                         / \
         *                        2   5
         *                       / \
         *                      1   3
         * =>
         * 1 -> 2 -> 3 -> 4 -> 5
         * | <-   <-   <-   <- |
         * |___________________|
         */
        Node bst = new Node(4, null, null);
        bst.left = new Node(2, null, null);
        bst.left.left = new Node(1, null, null);
        bst.left.right = new Node(3, null, null);
        bst.right = new Node(5, null, null);
        Node DLL = treeToDoublyListInPlace(bst);
        int[] expected = new int[]{1, 2, 3, 4, 5};
        int[] actual = DLLToArray(DLL, 5);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void treeToDoublyListNewNodeTest() {
        /**
         * Example:
         * Input: root = [4,2,5,1,3]
         * Output: [1,2,3,4,5]
         */
        Node bst = new Node(4, null, null);
        bst.left = new Node(2, null, null);
        bst.left.left = new Node(1, null, null);
        bst.left.right = new Node(3, null, null);
        bst.right = new Node(5, null, null);
        Node DLL = treeToDoublyListNewNode(bst);
        int[] expected = new int[]{1, 2, 3, 4, 5};
        int[] actual = DLLToArray(DLL, 5);
        assertArrayEquals(expected, actual);
    }

    private int[] DLLToArray(Node root, int length) {
        int[] res = new int[length];
        for (int i = 0; i < length; i++) {
            res[i] = root.val;
            root = root.right;
        }
        return res;
    }


    private static class Node {
        int val;
        Node left;
        Node right;

        public Node(int _val, Node _left, Node _right) {
            this.val = _val;
            this.left = _left;
            this.right = _right;
        }
    }
}
