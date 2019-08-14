import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Stack;

public class constructTreePreorderInorder {
    /**
     * Given preorder and inorder traversal of a tree, construct the binary tree.
     *
     * Note:
     * You may assume that duplicates do not exist in the tree.
     *
     * Approach 1: Recursion
     *此题的本质是，preorder的第一个节点，一定是root节点，而在inorder中，该节点将array两侧分为左子树和右子树。因此，算法的过程是
     * 1.找到preorder的当前值，构建一个节点。然后找到inorder中这个值对应的index
     * 2.inorder数组中0到index - 1应该在最终结果的左子树，index + 1到最后应该在最终结果的右子树
     * 3.传入inorder的前半部分，递归构建根节点的左子树，同时将preorder的index向前移动一位
     * 4.传入inorder的后半部分，递归构建根节点的右子树，需要注意的是，此时inIndex - inStart为左子树中节点的个数。那么右子树的根节点需要从
     *   preStart + (inIndex - inStart) + 1开始
     *
     * Time: O(N) 每个节点遍历一遍，构建整个二叉树即可
     * Space: O(N) call stack取决于构建的二叉树的高度，最坏情况下为O(N)
     */
    public TreeNode buildTreeRecursive(int[] preorder, int[] inorder) {
        return helper(0, 0, inorder.length - 1, preorder, inorder);
    }

    private TreeNode helper(int preStart, int inStart, int inEnd, int[] preorder, int[] inorder) {
        //base case，若preorder遍历结束，某节点无左子树或右子树（即inStart和inEnd中不夹着任何元素），返回一个空节点
        if(preStart >= preorder.length || inStart > inEnd) {
            return null;
        }
        //以当前preorder的index构建一个根节点
        TreeNode res = new TreeNode(preorder[preStart]);
        //然后再inorder中找到该节点的index，该index将inorder数组分成前后两个子树
        int inIndex = inStart;
        while(inIndex < inorder.length && inorder[inIndex] != preorder[preStart]) {
            inIndex++;
        }
        //以inIndex为界限，将前后两数组分别递归传入函数，构建当前节点的左右子树
        //左子树较为容易，preStart只需跳过刚才已建好的节点即可
        res.left = helper(preStart + 1, inStart, inIndex - 1, preorder, inorder);
        //右子树需要preStart跳过左子树中所有的元素，左子树中元素的个数为inIndex - inStart
        res.right = helper(preStart + inIndex - inStart + 1, inIndex + 1, inEnd, preorder, inorder);
        return res;
    }


    @Test
    public void buildTreeRecursiveTest() {
        /**
         * Example:
         * Given
         * preorder = [3,9,20,15,7]
         * inorder = [9,3,15,20,7]
         * Return the following binary tree:
         *
         *     3
         *    / \
         *   9  20
         *     /  \
         *    15   7
         */
        int[] preorder = new int[]{3, 9, 20, 15, 7};
        int[] inorder = new int[]{9, 3, 15, 20, 7};
        TreeNode actual = buildTreeRecursive(preorder, inorder);
        TreeNode expected = new TreeNode(3);
        expected.left = new TreeNode(9);
        expected.right = new TreeNode(20);
        expected.right.left = new TreeNode(15);
        expected.right.right = new TreeNode(7);
        assertTrue(isSameTree(expected, actual));
    }

    /**
     * Approach 2:
     * 可以将方法1转化为iteration来构建整个树。本质思想仍然是，遍历整个preorder数组，先以preorder的数值建立节点。然后再inorder数组中找到该值的index，
     * 若递归调用，则可以将数组分成两半，继续调用函数，若用iteration，则在inorder数组中找到正确index之前所遇到的数字都应该是左子树中的节点，因此只要判断
     * inorder数组中的值与当前栈顶的值是否相等，即可判断是否找到正确index，若尚未到达index，就构建一个节点，把它连接为栈顶节点的左孩子，然后将当前节点也
     * 压栈（意味着视新节点为根节点，在inorder重新找它的位置）。若找到正确的index，则需要把栈顶节点不断退栈，意味着该节点的左子树已全部建立完毕。直到栈顶
     * 节点不等于inorder中当前index的值。最后出栈的节点是现在需要考虑的新节点，该节点的左子树已构建完毕，此时要以preorder当前index的值建立节点，作为当前
     * 节点的右孩子。然后继续将该新节点压栈（视新节点为根节点，建立其左右子树）。当preorder遍历完毕，二叉树建立完成
     *
     * Time: O(N) 遍历每个节点来构建整个二叉树
     * Space: O(N) 需要一个栈来代替recursion中的call stack，最坏情况下需要O(N)空间
     */
    public TreeNode buildTreeIterative(int[] preorder, int[] inorder) {
        if(preorder.length == 0) return null;
        //preorder第一个元素即为根节点，先建立根节点
        TreeNode res = new TreeNode(preorder[0]);
        Stack<TreeNode> stack = new Stack<>();
        stack.push(res);
        for(int pre = 1, in = 0; pre < preorder.length; pre++) {
            if(!stack.isEmpty() && stack.peek().val != inorder[in]) {
                //若栈顶节点元素不等于inorder现在index的元素，
                //则此时经过的所有节点都应该在栈顶元素的左子树中
                TreeNode left = new TreeNode(preorder[pre]);
                //将其连接成栈顶节点的左孩子
                stack.peek().left = left;
                //将当前节点压栈，视其为新的根节点
                stack.push(left);
            } else {
                //若栈顶节点元素与inorder现在index相等，则要将栈顶退栈，然后记下最后退栈的节点
                //为其安排右子树（if any）
                TreeNode curr = null;
                while(!stack.isEmpty() && stack.peek().val == inorder[in]) {
                    curr = stack.pop();
                    in++;
                }
                if(curr != null) {
                    //若当前节点不为空，意味着栈里有元素，所以为当前节点安排右子树
                    TreeNode right = new TreeNode(preorder[pre]);
                    curr.right = right;
                    //然后将此有节点压栈，视其位新的根节点
                    stack.push(right);
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
         * preorder = [3,9,20,15,7]
         * inorder = [9,3,15,20,7]
         * Return the following binary tree:
         *
         *     3
         *    / \
         *   9  20
         *     /  \
         *    15   7
         */
        int[] preorder = new int[]{3, 9, 20, 15, 7};
        int[] inorder = new int[]{9, 3, 15, 20, 7};
        TreeNode actual = buildTreeIterative(preorder, inorder);
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
