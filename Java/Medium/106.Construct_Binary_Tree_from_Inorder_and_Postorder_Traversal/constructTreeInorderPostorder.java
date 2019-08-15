import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Stack;

public class constructTreeInorderPostorder {

    /**
     * Given inorder and postorder traversal of a tree, construct the binary tree.
     *
     * Approach 1: Recursion
     * 此题与105基本思想完全相同。首先在postorder中找到根节点，然后在inorder数组中定位该节点位置，然后将前后两数组分开，分别递归构建左右子树。和105相同的是
     * 在inorder找到index后，前半部分是左子树，后半部分为右子树。
     * 但和105不同的地方是，此时最后才会访问根节点，即postorder数组中最后一个元素才是根节点。同时postorder遵从先左子树后右子树的顺序，也就是当从后往前遍历
     * postorder数组的时候，会先经过最终二叉树的右子树。因此为了快速定位左子树的index，需要将postorder中的index减去右子树的节点个数。（这点与105正好相反）
     * 其余步骤均与105类似
     *
     * Time: O(n) 遍历所有节点构建二叉树
     * Space: O(n) 当树不平衡时，递归深度最坏情况下为O(N)
     */
    public TreeNode buildTreeRecursive(int[] inorder, int[] postorder) {
        return helper(postorder.length - 1, 0, inorder.length - 1, inorder, postorder);
    }

    private TreeNode helper(int postStart, int inStart, int inEnd, int[] inorder, int[] postorder) {
        //base case，因为是倒序遍历postorder数组，当数组溢出左边界，或inorder中没有元素可以构建节点，返回空节点
        if(postStart < 0 || inStart > inEnd) {
            return null;
        }
        //此时根节点是postorder数组中最后一个元素
        TreeNode res = new TreeNode(postorder[postStart]);
        //同样，要在inorder数组中定位该根节点位置，然后分成两半继续递归
        int inIndex = inStart;
        while(inIndex < inEnd && inorder[inIndex] != postorder[postStart]) {
            inIndex++;
        }
        //递归构建左右子树，注意从后往前会先经过右子树，后到左子树，所以要从postStart上减去右子树的节点个数，才能定位到左子树位置
        res.left = helper(postStart - (inEnd - inIndex) - 1, inStart, inIndex - 1, inorder, postorder);
        res.right = helper(postStart - 1, inIndex + 1, inEnd, inorder, postorder);
        return res;
    }

    @Test
    public void buildTreeRecursiveTest() {
        /**
         * Example:
         * Given
         * inorder = [9,3,15,20,7]
         * postorder = [9,15,7,20,3]
         * Return the following binary tree:
         *
         *     3
         *    / \
         *   9  20
         *     /  \
         *    15   7
         */
        int[] inorder = new int[]{9, 3, 15, 20, 7};
        int[] postorder = new int[]{9, 15, 7, 20, 3};
        TreeNode actual = buildTreeRecursive(inorder, postorder);
        TreeNode expected = new TreeNode(3);
        expected.left = new TreeNode(9);
        expected.right = new TreeNode(20);
        expected.right.left = new TreeNode(15);
        expected.right.right = new TreeNode(7);
        assertTrue(isSameTree(expected, actual));
    }

    /**
     * Approach 2: Iteration
     * 同理，iteration也和105大体相同。只是对于postorder要从后往前遍历，因为根节点在最后，同时在遍历的过程中，右子树会先于左子树遍历。因此，在inorder数组找
     * 找寻对应根节点时，也应该以倒序遍历。换言之，此题是先构造根节点的右子树，再构造左子树。
     *
     * Time: O(N) 遍历所有节点来构造二叉树
     * Space: O(N) 用stack来代替递归深度，最坏情况下需要O(N)空间
     */
    public TreeNode buildTreeIterative(int[] inorder, int[] postorder) {
        if(postorder.length == 0) return null;
        TreeNode res = new TreeNode(postorder[postorder.length - 1]);
        Stack<TreeNode> stack = new Stack<>();
        stack.push(res);
        //与105相反，需要先构建右子树再构建左子树，因为倒序经过postorder数组时，顺序为根节点 -> 右子树 -> 左子树
        for(int post = postorder.length - 2, in = inorder.length - 1; post >= 0; post--) {
            if(!stack.isEmpty() && stack.peek().val != inorder[in]) {
                TreeNode right = new TreeNode(postorder[post]);
                stack.peek().right = right;
                stack.push(right);
            } else {
                TreeNode curr = null;
                while(!stack.isEmpty() && stack.peek().val == inorder[in]) {
                    curr = stack.pop();
                    in--;
                }
                if(curr != null) {
                    TreeNode left = new TreeNode(postorder[post]);
                    curr.left = left;
                    stack.push(left);
                }
            }
        }
        return res;
    }

    @Test
    public void buildTreeIterativeTest() {
        /**
         * Example:
         * Given
         * inorder = [9,3,15,20,7]
         * postorder = [9,15,7,20,3]
         * Return the following binary tree:
         *
         *     3
         *    / \
         *   9  20
         *     /  \
         *    15   7
         */
        int[] inorder = new int[]{9, 3, 15, 20, 7};
        int[] postorder = new int[]{9, 15, 7, 20, 3};
        TreeNode actual = buildTreeIterative(inorder, postorder);
        TreeNode expected = new TreeNode(3);
        expected.left = new TreeNode(9);
        expected.right = new TreeNode(20);
        expected.right.left = new TreeNode(15);
        expected.right.right = new TreeNode(7);
        assertTrue(isSameTree(expected, actual));
    }


    private boolean isSameTree(TreeNode p, TreeNode q) {
        if(p == null && q == null) return true;
        if(p == null || q == null) return false;
        if(p.val != q.val) return false;
        return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
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
