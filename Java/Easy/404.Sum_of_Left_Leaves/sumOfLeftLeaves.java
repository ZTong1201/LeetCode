import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Stack;

public class sumOfLeftLeaves {

    /**
     * Find the sum of all left leaves in a given binary tree.
     *
     * Approach 1: Recursion with Global Variable
     * 此题最重要的一步为，在当前节点，若其存在左孩子，则要看其左孩子的左右孩子是否为null（都为null即为leaf节点），此leaf节点必为left leaf，然后直接递归
     * 访问当前节点的右孩子即可。用一个global variable记录所有左孩子的sum
     *
     * 另外值得注意的是，单独一个根节点既不是left leaf也不是right leaf。
     *
     *
     * Time: O(N) 遍历所有节点
     * Space: O(N) 因为使用递归方法，call stack最多需要O(H)的空间，即树的高度。最坏情况下为O(N)
     */
    private int res;

    public int sumOfLeftLeavesRecursive1(TreeNode root) {
        this.res = 0;
        dfs(root);
        return this.res;
    }

    private void dfs(TreeNode root) {
        //base case，若树为空，则无需继续遍历
        if(root == null) return;
        //只有当当前节点的左孩子不为空时，才可能判断该左孩子的左右孩子是否为空，来判断其是否为left leaf
        if(root.left != null) {
            if(root.left.left == null && root.left.right == null) {
                this.res += root.left.val;
            }
            //否则的话，继续遍历到更深的左孩子
            dfs(root.left);
        }
        if(root.right != null) {
            //同理，当前节点的右孩子里也有可能包含left leaf，所以若其右孩子存在，则以其右孩子为根节点，继续寻找left leaf
            dfs(root.right);
        }
    }

    /**
     * Approach 2: Recursion without global variable
     * 本质上，所有left leaves的和即是所有左子树left leaf的和 + 右子树left leaf的和
     * 在任意节点，该节点的left leaves的和为，若其左孩子为left leaf，则为其左孩子的节点val + 其右孩子中所有left leaves的和
     *
     * base case即为，若当前节点为空，则返回0
     */
    public int sumOfLeftLeavesRecursive2(TreeNode root) {
        //base case，若树为空，则返回0
        if(root == null) {
            return 0;
        }
        if(root.left != null && root.left.left == null && root.left.right == null) {
            //当前节点的left leaves的和为（若其左孩子为left leaf，其左孩子的值） + （右孩子中所有left leaves的和）
            return root.left.val + sumOfLeftLeavesRecursive2(root.right);
        }
        //整个数的和为左子树的left leaves和 + 右子树的right leaves和
        return sumOfLeftLeavesRecursive2(root.left) + sumOfLeftLeavesRecursive2(root.right);
    }

    /**
     * Approach 3: Iteration
     * 上述过程可以利用stack转化成iteration
     *
     * Time: O(N)
     * Space: O(N) 需要一个stack存tree node，最坏情况下需要O(N)
     *
     */
    public int sumOfLeftLeavesIterative(TreeNode root) {
        int res = 0;
        if(root == null) return res;
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        while(!stack.isEmpty()) {
            TreeNode curr = stack.pop();
            if(curr.left != null) {
                if(curr.left.left == null && curr.left.right == null) {
                    res += curr.left.val;
                }
                stack.push(curr.left);
            }
            if(curr.right != null) {
                stack.push(curr.right);
            }
        }
        return res;
    }

    /**
     * Approach 4: Iteration (Morris Algorithm)
     * Morris算法主要是利用常数空间对树进行遍历。Morris algorithm的本质是，若当前节点存在左孩子，则找到其左孩子的predecessor（rightmost节点），将其右孩子
     * 设置为当前节点。当再次访问到该predecessor时，将其右孩子断开，回归为null。若当前节点不存在左孩子，则说明已遍历到该节点的leftmost节点，改为访问其右孩子。
     *
     * Time: O(N)
     * Space: O(1)
     */
    public int sumOfLeftLeavesMorris(TreeNode root) {
        int res = 0;
        while(root != null) {
            if(root.left == null) {
                root = root.right;
            } else {
                TreeNode predecessor = root.left;
                while(predecessor.right != null && predecessor.right != root) {
                    //找到rightmost节点
                    predecessor = predecessor.right;
                }
                //若predecessor的右孩子为null，说明第一次遍历到此节点，将其和root节点相连，构成环，然后继续访问root的左孩子
                if(predecessor.right == null) {
                    predecessor.right = root;
                    root = root.left;
                } else {
                    //否则说明第二次找到该predecessor，该节点被遍历，因此需要断开其右孩子
                    predecessor.right = null;
                    //根据此题需要，需要判断当前root节点的左孩子是否存在，以及是否为left leaf，若是，则加上该节点值
                    if(root.left != null && root.left.left == null && root.left.right == null) {
                        res += root.left.val;
                    }
                    root = root.right;
                }
            }
        }
        return res;
    }

    @Test
    public void sumOfLeftLeavesTest() {
        /**
         * Example:
         *     3
         *    / \
         *   9  20
         *     /  \
         *    15   7
         *
         * There are two left leaves in the binary tree, with values 9 and 15 respectively. Return 24.
         */
        TreeNode tree = new TreeNode(3);
        tree.left = new TreeNode(9);
        tree.right = new TreeNode(20);
        tree.right.left = new TreeNode(15);
        tree.right.right = new TreeNode(7);
        assertEquals(24, sumOfLeftLeavesRecursive1(tree));
        assertEquals(24, sumOfLeftLeavesRecursive2(tree));
        assertEquals(24, sumOfLeftLeavesIterative(tree));
        assertEquals(24, sumOfLeftLeavesMorris(tree));
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
