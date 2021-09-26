import java.util.LinkedList;
import java.util.Queue;

public class CBTInserter {

    /**
     * A complete binary tree is a binary tree in which every level, except possibly the last, is completely filled, and
     * all nodes are as far left as possible.
     * <p>
     * Design an algorithm to insert a new node to a complete binary tree keeping it complete after the insertion.
     * <p>
     * Implement the CBTInserter class:
     * <p>
     * CBTInserter(TreeNode root) Initializes the data structure with the root of the complete binary tree.
     * int insert(int v) Inserts a TreeNode into the tree with value Node.val == val so that the tree remains complete,
     * and returns the value of the parent of the inserted TreeNode.
     * TreeNode get_root() Returns the root node of the tree.
     * <p>
     * Constraints:
     * <p>
     * The number of nodes in the tree will be in the range [1, 1000].
     * 0 <= Node.val <= 5000
     * root is a complete binary tree.
     * 0 <= val <= 5000
     * At most 10^4 calls will be made to insert and get_root.
     * <p>
     * Approach: Level Order Traversal
     * In order to make the binary tree complete, we need to fill the current level before inserting into the next level.
     * Therefore, a level order traversal preprocessing will be needed during initialization. Basically, we can use a queue
     * to keep track of the parent node for next insertion position. As long as one of the children (either left or child)
     * is not filled, it should be inserted into the candidate queue. Since we're doing level order traversal, the order of
     * position is also correct. During insertion, we get the top node from the queue, and if its left child is null (then
     * it must have been a leaf node), we assign a new node to its left child and add this new node to the queue, it will
     * be visited later. If only the right child is null, then after inserting a new node, it has to be popped from the queue
     * because the parent becomes complete after adding the new child. Still, we need to add this new node into the queue
     * for further insertion.
     * <p>
     * Time: O(n) we need to do the preprocessing to add appropriate parent nodes into the queue, it takes O(n) time since
     * we need to visit the existing tree. insert() and get_root() will both take O(1) time.
     * Space: O(n), for level order traversal, we need to at least keep track of the last level of the tree, which will
     * have approximately n / 2 nodes, hence the queue will require O(n)
     */
    private final TreeNode root;
    private final Queue<TreeNode> nextInsertionParent;

    public CBTInserter(TreeNode root) {
        this.root = root;
        nextInsertionParent = new LinkedList<>();
        // do the preprocessing
        levelOrderTraversal();
    }

    public int insert(int val) {
        // we always need to insert a new node to the top node of the queue
        TreeNode parent = nextInsertionParent.peek();
        TreeNode newNode = new TreeNode(val);
        // if the left child is null, this parent hasn't done yet
        // insert the left child
        if (parent.left == null) {
            parent.left = newNode;
        } else {
            // otherwise, both children will be filled after current insertion
            // remove the top node from the queue since it's complete
            parent.right = newNode;
            nextInsertionParent.poll();
        }
        // always need to put the new child into the queue for future visit
        nextInsertionParent.add(newNode);
        // return the value of the parent node
        return parent.val;
    }

    public TreeNode get_root() {
        return root;
    }

    private void levelOrderTraversal() {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            TreeNode curr = queue.poll();

            // as long as we have at least one position for insertion
            // add the current node into the queue
            if (curr.left == null || curr.right == null) {
                nextInsertionParent.add(curr);
            }

            // keep traversing the next level
            if (curr.left != null) {
                queue.add(curr.left);
            }
            if (curr.right != null) {
                queue.add(curr.right);
            }
        }
    }

    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        public TreeNode(int x) {
            this.val = x;
        }
    }
}
