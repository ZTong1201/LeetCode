import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class closestBSTValue {

    /**
     * Given a non-empty binary search tree and a target value, find the value in the BST that is closest to the target.
     *
     * Note:
     *
     * Given target value is a floating point.
     * You are guaranteed to have only one unique value in the BST that is closest to the target.
     *
     * Approach 1: Iterative inorder
     * 本质上是要对BST做inorder traversal，最后target会落在两个值之间，比较target和两个值的差值，差值更小的即为结果。
     * inorder的遍历可以通过iteration来进行，先遍历左子树，一直到树的leftmost节点，在此期间，将所有经过节点全部压栈，然后开始出栈，若能找到某个区间
     * 使得target落在区间内，则比较右边界和target的差值，与左边界和target之间的差值，返回差值更小的那个。
     * 如不能找到该区间，则将更新左边界为当前节点值，然后继续遍历当前节点的右子树。
     *
     * 注意刚开始时，要将左边界定义成一个非常小的值。
     *
     * Time: O(k)，k为closest value在inorder traversal之后的index，本质上，该inorder traversal遍历直到找到某区间结束。并没有遍历完全
     * Space: O(N)，最坏情况下，对极端不平衡的树，栈中可能记录了所有节点
     */
    public int closestValueInorder(TreeNode root, double target) {
        //先将左边界设置为一个非常小的数
        long left = Long.MIN_VALUE;
        Stack<TreeNode> stack = new Stack<>();

        while(!stack.isEmpty() || root != null) {
            //因为要进行inorder遍历，所以要先遍历完所有左子树节点
            while(root != null) {
                stack.push(root);
                root = root.left;
            }

            root = stack.pop();
            //若找到某个区间使得target加在中间，则返回差值更小的那个，注意将long转换回int
            if(left <= target && target < root.val) {
                return Math.abs(left - target) > Math.abs(root.val - target) ? root.val : (int) left;
            }
            //否则的话就该继续遍历右子树，并注意将左边界更新为当前节点值，即在sorted array中移动到下一个index
            left = root.val;
            root = root.right;
        }
        return (int) left;
    }

    /**
     * Approach 2: Binary Search
     * 本质上其实是在一个sorted array中做binary search。如果target小于当前root值，则只可能在root的左子树中找到最接近的值，反之则在右子树中。
     * 在binary search过程中，若当前root值和target的差值小于之前记录的closest与target差值，则需要不断更新closest值。最终当binary search结束，即
     * 左右边界交叉时（在这题中，就是遍历到null节点），返回记录的closest
     *
     * Time: O(H) 因为每次会舍弃掉一半的子树，所以最后只遍历了和树的高度相同的节点
     * Space: O(1) 直接对树本身进行binary search，不需要额外空间
     */
    public int cloestValueBinarySearch(TreeNode root, double target) {
        //刚开始可以把val设为任意值
        int val = 0;
        int closest = root.val;
        //当root不为null，则说明binary search的左右边界尚未交叉，可以继续遍历
        while(root != null) {
            val = root.val;
            //在二分过程中，不断更新closest值
            closest = Math.abs(val - target) > Math.abs(closest - target) ? closest : val;
            //根据当前节点值，决定该舍弃掉哪一半子树
            root = val > target ? root.left : root.right;
        }
        return closest;
    }

    @Test
    public void closestValueTest() {
        /**
         * Example:ss
         * Input: root = [4,2,5,1,3], target = 3.714286
         *
         *     4
         *    / \
         *   2   5
         *  / \
         * 1   3
         *
         * Output: 4
         */
        TreeNode tree = new TreeNode(4);
        tree.left = new TreeNode(2);
        tree.right = new TreeNode(5);
        tree.left.left = new TreeNode(1);
        tree.left.right = new TreeNode(3);
        assertEquals(4, closestValueInorder(tree, 3.714286));
        assertEquals(4, cloestValueBinarySearch(tree, 3.714286));
    }


    private static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            this.val = x;
        }
    }
}
