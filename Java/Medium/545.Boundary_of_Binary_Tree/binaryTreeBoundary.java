import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class binaryTreeBoundary {

    /**
     * Given a binary tree, return the values of its boundary in anti-clockwise direction starting from root. Boundary includes left
     * boundary, leaves, and right boundary in order without duplicate nodes.  (The values of the nodes may still be duplicates.)
     *
     * Left boundary is defined as the path from root to the left-most node. Right boundary is defined as the path from root to the
     * right-most node. If the root doesn't have left subtree or right subtree, then the root itself is left boundary or right boundary.
     * Note this definition only applies to the input binary tree, and not applies to any subtrees.
     *
     * The left-most node is defined as a leaf node you could reach when you always firstly travel to the left subtree if exists.
     * If not, travel to the right subtree. Repeat until you reach a leaf node.
     *
     * The right-most node is also defined by the same way with left and right exchanged.
     *
     * Approach 1: Three sub-problems
     * We can split the entire problem into 3 sub-problems. Namely,
     * 1. add the non-leaf nodes on the left boundary
     * 2. add all the leaf nodes
     * 3. add the non-leaf nodes on the right boundary in the reverse order.
     * The first two problems can be solved easily, we build another helper function to check whether a node is a leaf or not. First
     * traverse until the left most node and add all non-leaf nodes. If the node doesn't have a left child, however, has a right child.
     * This right child is also on the left boundary, we move to the right child and keep searching for the leftmost nodes. After left
     * boundary is done, we simply traverse the entire tree, add all the leaf nodes.
     *
     * For the right boundary, since we need to add them in a reverse order. Instead of directly adding them into the final list, we use
     * a stack first, and then add the top element of the stack to our final list.
     *
     * Time: O(n) one complete traversal to add leaf nodes, two traversals will require up to the height of tree runtime to add left and
     *      right boundaries
     * Space: O(n), we need a final result list and a stack, which will require up to O(n) space. Meanwhile, the call stack will require
     *       up to O(n) space in the worst case as well.
     */
    public List<Integer> boundaryOfBinaryTreeThreeTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if(root == null) {
            return res;
        }
        //to avoid count the root node twice if it is a single root node
        //we will first check whether it is a leaf node
        if(!isLeaf(root)) {
            res.add(root.val);
        }
        TreeNode curr = root.left;
        while(curr != null) {
            //add all non-leaf nodes on the left boundary
            if(!isLeaf(curr)) {
                res.add(curr.val);
            }
            //as long as the current node has a left child, we keep searching until the leftmost node
            if(curr.left != null) {
                curr = curr.left;
            } else {
                //otherwise, move to the right child (since it is also on the left boundary)
                curr = curr.right;
            }
        }
        //then add all leaf nodes in the final list
        addLeaves(res, root);
        //finally, add the right boundary in a stack
        Stack<Integer> stack = new Stack<>();
        curr = root.right;
        while(curr != null) {
            //remember to add all non-leaf nodes
            if(!isLeaf(curr)) {
                stack.add(curr.val);
            }
            //likewise, we search for the rightmost node on the right boundary
            //as long as the current node has a right child
            if(curr.right != null) {
                curr = curr.right;
            } else {
                //otherwise, we move to the left child (since it is also on the right boundary) and restart searching
                curr = curr.left;
            }
        }
        //pop the top element out of the stack and hence we add the right boundary in a reverse order
        while(!stack.isEmpty()) {
            res.add(stack.pop());
        }
        return res;
    }

    private boolean isLeaf(TreeNode root) {
        return (root.left == null) && (root.right == null);
    }

    private void addLeaves(List<Integer> res, TreeNode root) {
        //if we reach a leaf node, add the node value to the final list
        if(isLeaf(root)) {
            res.add(root.val);
        } else { //otherwise, we'll search its left and right children (if exists)
            if(root.left != null) {
                addLeaves(res, root.left);
            }
            if(root.right != null) {
                addLeaves(res, root.right);
            }
        }
    }

    @Test
    public void boundaryOfBinaryTreeThreeTraversalTest() {
        /**
         * Example 1:
         * Input:
         *   1
         *    \
         *     2
         *    / \
         *   3   4
         *
         * Ouput:
         * [1, 3, 4, 2]
         *
         * Explanation:
         * The root doesn't have left subtree, so the root itself is left boundary.
         * The leaves are node 3 and 4.
         * The right boundary are node 1,2,4. Note the anti-clockwise direction means you should output reversed right boundary.
         * So order them in anti-clockwise without duplicates and we have [1,3,4,2].
         */
        TreeNode tree1 = new TreeNode(1);
        tree1.right = new TreeNode(2);
        tree1.right.left = new TreeNode(3);
        tree1.right.right = new TreeNode(4);
        List<Integer> actual1 = boundaryOfBinaryTreeThreeTraversal(tree1);
        List<Integer> expected1 = new ArrayList<>(Arrays.asList(1, 3, 4, 2));
        assertEquals(expected1.size(), actual1.size());
        for(int i = 0; i < expected1.size(); i++) {
            assertEquals(expected1.get(i), actual1.get(i));
        }
        /**
         * Example 2:
         * Input:
         *     ____1_____
         *    /          \
         *   2            3
         *  / \          /
         * 4   5        6
         *    / \      / \
         *   7   8    9  10
         *
         * Ouput:
         * [1,2,4,7,8,9,10,6,3]
         *
         * Explanation:
         * The left boundary are node 1,2,4. (4 is the left-most node according to definition)
         * The leaves are node 4,7,8,9,10.
         * The right boundary are node 1,3,6,10. (10 is the right-most node).
         * So order them in anti-clockwise without duplicate nodes we have [1,2,4,7,8,9,10,6,3].
         */
        TreeNode tree2 = new TreeNode(1);
        tree2.left = new TreeNode(2);
        tree2.right = new TreeNode(3);
        tree2.left.left = new TreeNode(4);
        tree2.left.right = new TreeNode(5);
        tree2.left.right.left = new TreeNode(7);
        tree2.left.right.right = new TreeNode(8);
        tree2.right.left = new TreeNode(6);
        tree2.right.left.left = new TreeNode(9);
        tree2.right.left.right = new TreeNode(10);
        List<Integer> actual2 = boundaryOfBinaryTreeThreeTraversal(tree2);
        List<Integer> expected2 = new ArrayList<>(Arrays.asList(1, 2, 4, 7, 8, 9, 10, 6, 3));
        assertEquals(actual2.size(), expected2.size());
        for(int i = 0; i < expected2.size(); i++) {
            assertEquals(expected2.get(i), actual2.get(i));
        }
    }

    /**
     * Approach 2: Preorder Traversal
     *
     * We can actually only traverse the tree once. The order of the boundary is very much like the preorder traversal except the right
     * boundary (since we need a reverse order). Other than that, we have to omit some middle points. Hence, we can make a flag value
     * to denote whether the current node is on the left boundary, on the right boundary, a root node or just a middle node.
     * Namely,
     * flag = 0 is a root node
     * flag = 1 is on the left boundary
     * flag = 2 is on the right boundary
     * flag = 3 is a middle node
     * Besides, we also implement a function to check whether it is a leaf node or not.
     * Therefore, we make three separate list to store values on the left boundary, leaf nodes and on the right boundary. At each node
     * we check the flag value of that node and add it to the proper list. Note that if it is on the right boundary, we have to add it
     * at the FRONT of the list to guarantee it is in a reverse order. Meanwhile, we need to check the flag value of its left and
     * right children (if exist). We have such cases:
     *
     * To check the left child
     * 1. If current node is a root node or on the left boundary, its left child must be on the left boundary, return 1
     * 2. If current node is on the right boundary, however, its right child is null. Consequently, its left child must be on the right
     *    boundary, return 2.
     * 3. Otherwise, return 3.
     *
     * To check the right child
     * 1. If current node is a root node or on the right boundary, its right child must be on the right boundary, return 2.
     * 2. If current node is on the left boundary, however, its left child is null. Thus, its right child must be on the right boundary,
     *    return 1.
     * 3. Otherwise, return 3.
     *
     * Time: O(n) one complete traversal is enough
     * Space: O(n) even thought we have three lists, they are mutually exclusive. They will at most contain all n nodes. Besides, the call
     *       stack requires O(n) space in the worst case
     */
    public List<Integer> boundaryOfBinaryTreeOneTraversal(TreeNode root) {
        //use linked list, we can both add at the front and the end in O(1) time
        LinkedList<Integer> leftBoundary = new LinkedList<>();
        LinkedList<Integer> rightBoundary = new LinkedList<>();
        LinkedList<Integer> leaves = new LinkedList<>();
        //the first flag value should be 0, since we pass in a root node
        preorder(root, leftBoundary, leaves, rightBoundary, 0);
        //add all leaf values
        leftBoundary.addAll(leaves);
        //then add all right boundary values
        leftBoundary.addAll(rightBoundary);
        return leftBoundary;
    }

    //helper functions to check whether current node is a root, a leaf, on the left boundary or on the right boundary

    private boolean isRoot(int flag) {
        return flag == 0;
    }

    private boolean isLeftBoundary(int flag) {
        return flag == 1;
    }

    private boolean isRightBoundary(int flag) {
        return flag == 2;
    }

    //get the flag value of the left child of current node
    private int leftChildFlag(TreeNode curr, int flag) {
        //If current node is a root node or on the left boundary, its left child must be on the left boundary, return 1
        if(isLeftBoundary(flag) || isRoot(flag)) {
            return 1;
        } else if(isRightBoundary(flag) && curr.right == null) {
            //If current node is on the right boundary, however, its right child is null. Consequently, its left child must be on the right
            //boundary, return 2.
            return 2;
        } else return 3;
    }

    //get the flag value of the right child of current node
    private int rightChildFlag(TreeNode curr, int flag) {
        if(isRightBoundary(flag) || isRoot(flag)) {
            return 2;
        } else if(isLeftBoundary(flag) && curr.left == null) {
            return 1;
        } else return 3;
    }

    //preorder traverse the tree and add node values to appropriate list
    private void preorder(TreeNode root, LinkedList<Integer> left, LinkedList<Integer> leaves, LinkedList<Integer> right, int flag) {
        //base case, if an empty tree, return nothing
        if(root == null) return;
        //if it is a root node or on the left boundary
        //add it to the left boundary
        if(isLeftBoundary(flag) || isRoot(flag)) {
            left.addLast(root.val);
        } else if(isRightBoundary(flag)) {
            //similarly, add to the right boundary
            //remember to add values at the front of the list (since we need a reverse order)
            right.addFirst(root.val);
        } else if(isLeaf(root)) {
            //or current node is a leaf node, add it to the leaf list
            leaves.addLast(root.val);
        }
        //preorder traverse the tree and update the flag value of current node's left and right child (if exist)
        preorder(root.left, left, leaves, right, leftChildFlag(root, flag));
        preorder(root.right, left, leaves, right, rightChildFlag(root, flag));
    }

    @Test
    public void boundaryOfBinaryTreeOneTraversalTest() {
        /**
         * Example 1:
         * Input:
         *   1
         *    \
         *     2
         *    / \
         *   3   4
         *
         * Ouput:
         * [1, 3, 4, 2]
         *
         * Explanation:
         * The root doesn't have left subtree, so the root itself is left boundary.
         * The leaves are node 3 and 4.
         * The right boundary are node 1,2,4. Note the anti-clockwise direction means you should output reversed right boundary.
         * So order them in anti-clockwise without duplicates and we have [1,3,4,2].
         */
        TreeNode tree1 = new TreeNode(1);
        tree1.right = new TreeNode(2);
        tree1.right.left = new TreeNode(3);
        tree1.right.right = new TreeNode(4);
        List<Integer> actual1 = boundaryOfBinaryTreeOneTraversal(tree1);
        List<Integer> expected1 = new ArrayList<>(Arrays.asList(1, 3, 4, 2));
        assertEquals(expected1.size(), actual1.size());
        for(int i = 0; i < expected1.size(); i++) {
            assertEquals(expected1.get(i), actual1.get(i));
        }
        /**
         * Example 2:
         * Input:
         *     ____1_____
         *    /          \
         *   2            3
         *  / \          /
         * 4   5        6
         *    / \      / \
         *   7   8    9  10
         *
         * Ouput:
         * [1,2,4,7,8,9,10,6,3]
         *
         * Explanation:
         * The left boundary are node 1,2,4. (4 is the left-most node according to definition)
         * The leaves are node 4,7,8,9,10.
         * The right boundary are node 1,3,6,10. (10 is the right-most node).
         * So order them in anti-clockwise without duplicate nodes we have [1,2,4,7,8,9,10,6,3].
         */
        TreeNode tree2 = new TreeNode(1);
        tree2.left = new TreeNode(2);
        tree2.right = new TreeNode(3);
        tree2.left.left = new TreeNode(4);
        tree2.left.right = new TreeNode(5);
        tree2.left.right.left = new TreeNode(7);
        tree2.left.right.right = new TreeNode(8);
        tree2.right.left = new TreeNode(6);
        tree2.right.left.left = new TreeNode(9);
        tree2.right.left.right = new TreeNode(10);
        List<Integer> actual2 = boundaryOfBinaryTreeOneTraversal(tree2);
        List<Integer> expected2 = new ArrayList<>(Arrays.asList(1, 2, 4, 7, 8, 9, 10, 6, 3));
        assertEquals(actual2.size(), expected2.size());
        for(int i = 0; i < expected2.size(); i++) {
            assertEquals(expected2.get(i), actual2.get(i));
        }
    }

    private class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            this.val = x;
        }
    }
}
