import org.junit.Test;
import static org.junit.Assert.*;

public class countCompleteNodes {

    /**
     * Given a complete binary tree, count the number of nodes.
     *
     * Note:
     *
     * Definition of a complete binary tree from Wikipedia:
     * In a complete binary tree every level, except possibly the last, is completely filled, and all nodes in the last level are as far
     * left as possible. It can have between 1 and 2h nodes inclusive at the last level h.
     *
     * Approach 1: Recursion
     * 利用recursion可以简单求解。base case为root为null，返回0，对于不为null的节点，其包含的节点数为其左子树的节点数 + 右子树的节点数 + 1（该节点本身）
     *
     * Time: O(n) 需要遍历所有节点
     * Space: O(logn) 因为树是complete的，所以一定是平衡的，树的高度为O(logn)
     */
    public int countNodesRecursive(TreeNode root) {
        return root == null ? 0 : 1 + countNodesRecursive(root.left) + countNodesRecursive(root.right);
    }

    /**
     * Approach 2: Binary Search
     * 因为输入的树是complete的，所以除了最后一个level外，其余所有的level都应该有2^k个节点。若输入的树的深度为d，那么前d - 1个level的节点的总个数为
     * 2^0 + 2^1 + 2^2 + ... + 2^(d - 1) = 1 * (1 - 2^d) / (1 - 2) = 2^d - 1。因此只要知道了最后一个level的节点数目，加上2^d - 1即为最终结果。
     * 而最后一个level的节点数取值范围为0 - 2^d，假设最后一行的节点都0 - 2^d的值所标记，可以用binary search来判断某个节点是否存在。即先令left = 0，
     * right = 2^d，每次取中间节点值，若中间节点值存在，说明前面的节点也一定存在，那么就需要继续search给定范围的右半部分，直到binary search结束，left所在
     * 位置就是在后一个level存在的最后一个节点的index。
     *
     * 同时，根据binary search，每次也可以在输入的树上选择走left还是走right，即能在树中走到对应index的正确位置，若该位置为null，说明该节点并不存在，若不为
     * null，则该节点存在。
     *
     * e.g.假设数的高度为3，那么最后一个level的节点数会在0 - 8之间，假设8个节点全部存在，那么将所有节点从左至右一次标记为0 - 7，值得注意的是，前四个节点，
     * 即0 - 3节点在根节点的左子树中，后四个节点，即4 - 7节点在根节点的右子树中。因此当取中值时，mid = 4，需要判断节点4是否存在，需要从根节点走到右子树，
     * 然后因为4，5节点在该节点的左子树中，因此需要走到其左子树。以此类推，直到走到最后一个level，节点4的正确位置。判断其是否为null
     *
     * Time: O((logn)^2) 对于每一个需要判断的节点，都需要在树中进行一次binary search判断该节点是否为null，需要O(logn)时间，同时需要对0 - 2^d之间的数
     *      进行binary search，判断最后一个level中的节点实际数量，这个过程也需要进行O(logn)次
     * Space: O(1)
     */
    public int countNodesBinarySearch(TreeNode root) {
        if(root == null) return 0;
        //先计算树的深度
        int d = depth(root);
        //如果只有一个根节点，深度为0，此时返回1即可
        if(d == 0) return 1;
        //用binary search判断最后一个level有多少个节点
        int left = 0, right = (int) Math.pow(2, d);
        while(left < right) {
            int mid = left + (right - left) / 2;
            //若当前节点存在于最后一个level，说明最后一个level有更多节点数
            //搜索右半部分
            if(exist(mid, d, root)) {
                left = mid + 1;
            } else {
                //否则说明有更少的节点，搜索左半部分
                right = mid;
            }
        }
        //最终返回2^d - 1加上最后一个level的节点数即可
        return (int) Math.pow(2, d) - 1 + left;
    }


    //判断某index（节点）是否在树的最后一个level
    private boolean exist(int idx, int d, TreeNode root) {
        int left = 0, right = (int) Math.pow(2, d) - 1;
        //需要遍历每一层，根据此时的中点值，判断在树中向左走还是向右走
        for(int i = 0; i < d; i++) {
            int mid = left + (right - left) / 2;
            //如果要找的节点小于等于range的中值，说明其在左子树中
            if(idx <= mid) {
                root = root.left;
                right = mid;
            } else {
                //否则在右子树中
                root = root.right;
                left = mid + 1;
            }
        }
        //判断所在位置是否为null即可
        return root != null;
    }


    private int depth(TreeNode root) {
        int res = 0;
        //因为树是complete的，因此只要一直走到leftmost节点即可
        while(root.left != null) {
            res++;
            root = root.left;
        }
        return res;
    }

    @Test
    public void countNodesTest() {
        /**
         * Example:
         * Input:
         *     1
         *    / \
         *   2   3
         *  / \  /
         * 4  5 6
         *
         * Output: 6
         */
        TreeNode tree = new TreeNode(1);
        tree.left = new TreeNode(2);
        tree.right = new TreeNode(3);
        tree.left.left = new TreeNode(4);
        tree.left.right = new TreeNode(5);
        tree.right.left = new TreeNode(6);
        assertEquals(6, countNodesRecursive(tree));
        assertEquals(6, countNodesBinarySearch(tree));
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
