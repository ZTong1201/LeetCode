import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class flipBinaryTree {

    /**
     * For a binary tree T, we can define a flip operation as follows: choose any node, and swap the left and right child subtrees.
     *
     * A binary tree X is flip equivalent to a binary tree Y if and only if we can make X equal to Y after some number of flip operations.
     *
     * Write a function that determines whether two binary trees are flip equivalent.  The trees are given by root nodes root1 and root2.
     *
     * Note:
     *
     * Each tree will have at most 100 nodes.
     * Each value in each tree will be a unique integer in the range [0, 99].
     *
     * Approach 1: Recursion
     * 可以用简单的DFS来判断当前节点是否符合条件。有如下几个case：
     * 1. 如果当前两节点都是null，返回true
     * 2. 如果只有一个节点为null，或者当前两节点都不为null，但是节点数值不相等，返回false
     * 3. 如果当前两节点值相等，可以继续向更深的节点判断。
     *    对于当前的节点，只有两种可能，即该节点的左右子树没有进行flip，或进行了flip。所以判断其当前节点是否flip equivalent时，可以判断两节点的
     *    左子树 = 左子树，右子树 = 右子树（即没有进行flip），或判断左子树 = 右子树， 右子树 = 左子树（即两子树进行了flip即可）
     *
     * Time: O(min(N1, N2)), 若两个数大小不等，最坏情况下，遍历完更小的子树后就可以判断出两子树是否相等
     * Space: O(min(H1, H2))， 因为进行递归调用，所以call stack需要O(H)空间，同样的，当递归遍历到深度更小的子树的叶子节点时，就无法再继续更深地遍历
     */
    public boolean flipEquivRecursive(TreeNode root1, TreeNode root2) {
        if(root1 == null && root2 == null) return true;
        if(root1 == null || root2 == null || root1.val != root2.val) return false;
        return (flipEquivRecursive(root1.left, root2.left) && flipEquivRecursive(root1.right, root2.right)) ||
                (flipEquivRecursive(root1.left, root2.right) && flipEquivRecursive(root1.right, root2.left));
    }

    /**
     * Approach 2: Canonical Traversal
     * 在遍历时，若左节点的数值大于右节点的数值，可以人为的反转遍历（即先遍历右子树，再遍历左子树），按此遍历得到的结果称为canonical representation，
     * 若两个树可以是翻转等价的，那么按此canonical traversal得到的结果一定相同。
     *
     * Time: O(max(N1, N2))， 为了得到canonical representation，需要对每个树进行单独遍历，因此runtime由更大的树决定
     * Space: O(N1 + N2), 需要将所有的节点存在单独的list里
     */
    public boolean flipEquivCanonical(TreeNode root1, TreeNode root2) {
        List<Integer> vals1 = new ArrayList<>();
        List<Integer> vals2 = new ArrayList<>();
        dfs(root1, vals1);
        dfs(root2, vals2);
        return vals1.equals(vals2);
    }

    private void dfs(TreeNode root, List<Integer> res) {
        if (root != null) {
            res.add(root.val);
            int L = root.left == null ? -1 : root.left.val;
            int R = root.right == null ? -1 : root.right.val;

            if (L < R) {
                dfs(root.left, res);
                dfs(root.right, res);
            } else {
                dfs(root.right, res);
                dfs(root.left, res);
            }
        }
    }

    @Test
    public void flipEquivTest() {
        /**
         * Input: root1 = [1,2,3,4,5,6,null,null,null,7,8], root2 = [1,3,2,null,6,4,5,null,null,null,null,8,7]
         * Output: true
         * Explanation: We flipped at nodes with values 1, 3, and 5.
         */
        TreeNode root1 = new TreeNode(1);
        root1.left = new TreeNode(2);
        root1.right = new TreeNode(3);
        root1.left.left = new TreeNode(4);
        root1.left.right = new TreeNode(5);
        root1.right.left = new TreeNode(6);
        root1.left.right.left = new TreeNode(7);
        root1.left.right.right = new TreeNode(8);
        TreeNode root2 = new TreeNode(1);
        root2.left = new TreeNode(3);
        root2.right = new TreeNode(2);
        root2.left.right = new TreeNode(6);
        root2.right.left = new TreeNode(4);
        root2.right.right = new TreeNode(5);
        root2.right.right.left = new TreeNode(8);
        root2.right.right.right = new TreeNode(7);
        assertTrue(flipEquivRecursive(root1, root2));
        assertTrue(flipEquivCanonical(root1, root2));
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
