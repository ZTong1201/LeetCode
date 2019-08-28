import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class countSmallerBST {

    /**
     * You are given an integer array nums and you have to return a new counts array. The counts array has the property where counts[i]
     * is the number of smaller elements to the right of nums[i]
     *
     * Approach 1: Binary Search Tree
     * 本质上，从左往右寻找在当前index之后比当前元素小的元素，与从右往左搜寻在当前index之前更小的元素会得到一样的结果。但倒序查找会产生一个累加结果。例如输入
     * [5, 2, 6, 1]，从右往左搜寻，若知道小于2的元素数量为1，且知道5小于2，那么可以得到小于5的元素个数为2个。
     * 为了更容易地得到比当前元素小的其他元素，考虑利用BST，同时每个BST节点上要记录3个值
     * 1.val - 当前元素
     * 2.count - 表示当前元素到目前为止出现了几次（因可能出现重复元素）
     * 3.leftCount - 记录其左子树的节点数量（即比当前元素小的元素个数），利用该值可以直接直到其左子树节点总数，不用再继续遍历整个子树
     *
     * 算法大致如下：
     * 1.初始化一个空的list，先插入一个0（因为最后一个元素之后不会有比它更小的元素，同时倒序查找，0应该最先插入），然后以数组最后一个元素建立一个根节点
     * 2.从右往左遍历整个数组，根据BST性质和当前元素的值，不断遍历到正确的分支，将该值插入。插入过程有3中可能，需要不同处理，同时插入函数返回值为比到目前为止
     *   比当前元素小的元素的个数，然后将其插入result list
     *   1)当前元素等于根节点值，说明该值在此之前曾出现过，那么该节点的count值加1，同时返回该节点的leftCount
     *   2)当前元素小于根节点值，因此需要遍历其左孩子，若左孩子为空，那么就以当前元素建立一个新的左孩子，同时返回0.（因为其左子树没有任何值），
     *     若不为空，就继续递归地遍历下去直到发现该元素或找到合适的插入位置，因其在根节点的左子树中，所以最后返回值为最后节点的leftCount，（即若为新建立
     *     节点，则返回0，表示没有元素比它小，或找到一个之前建立好的节点，返回其leftCount）
     *   3)当前元素大于根节点值，因此需要遍历其右孩子，若右孩子为空，就建立一个新节点，返回位置为根节点的count + leftCount，因为其在右子树中，所以根节点的
     *     所有左孩子以及根节点本身都小于当前节点。若右孩子不为空，就继续递归访问，最后的返回值要将遍历过程中产生的结果累加起来。
     * 3.最后只需要将result list反转，返回即可
     *
     *
     * Time: O(nlogn), worst - O(n^2)，对于数组中所有元素，要在BST中找到其节点或合适的插入位置，因为没有做任何balance处理，因此最坏情况下树的高度为O(n),
     *       平均情况下为O(logn)
     * Space: O(n) 最坏情况下数组所有元素都不同，需要建立n个节点
     *
     */
    public List<Integer> countSmallerBST(int[] nums) {
        List<Integer> res = new ArrayList<>();
        if(nums == null || nums.length == 0) return res;
        //先插入一个0，表示最后一个元素之后没有元素比它小
        res.add(0);
        //然后一个最后一个元素为值，建立一个根节点
        BSTNode root = new BSTNode(nums[nums.length - 1]);
        //然后从右往左遍历整个数组，找到当前BST中比该元素小的节点数目
        for(int i = nums.length - 2; i >= 0; i--) {
            res.add(insert(root, nums[i]));
        }
        //因为是反序插入，最后要把结果再反转
        Collections.reverse(res);
        return res;
    }

    //将当前val插入BST合适位置，同时返回当前BST中比val小的节点个数
    private int insert(BSTNode root, int val) {
        //如果当前根节点等于val
        if(root.val == val) {
            //更新该val出现的次数
            root.count++;
            //同时返回已经记录好的其左子树节点总数即可
            return root.leftCount;
        } else if(root.val > val) { //如果当前根节点大于val，则需要遍历其左子树
            //因为在当前根节点左子树中插入新值，因此根节点的leftCount需要加1
            root.leftCount++;
            //若其左子树为空，则建立一个新节点
            if(root.left == null) {
                root.left = new BSTNode(val);
                //同时因为其为新节点，没有左子树，因此返回0即可
                return 0;
            }
            //否则就向下移动到其左孩子，然后递归地访问子树，找到合适的位置
            return insert(root.left, val);
        } else { //最后一种情况，遍历当前根节点的右子树
            //如果右子树为空，就建立一个新节点
            if(root.right == null) {
                root.right = new BSTNode(val);
                //但是因其在右子树，所以它的根节点和根节点的所有左子树节点都比当前节点小
                //需要返回根节点的less_or_equal()
                return root.less_or_eqaul();
            }
            //否则就向下移动到其右孩子，继续递归地寻找当前节点（或插入当前节点）
            //最终返回值要将所有经过的节点值累加起来
            return root.less_or_eqaul() + insert(root.right, val);
        }
    }


    private class BSTNode {
        int val;
        int count;
        int leftCount;
        BSTNode left;
        BSTNode right;

        public BSTNode(int x) {
            this.val = x;
            //新建立节点为第一次出现，所以count为1，同时其左孩子为空，leftCount为0
            this.count = 1;
            this.leftCount = 0;
        }

        //为了方便得到小于等于当前节点值的节点数量，构建一个函数
        public int less_or_eqaul() {
            return count + leftCount;
        }
    }


    @Test
    public void countSmallerBSTTest() {
        /**
         * Input: [5,2,6,1]
         * Output: [2,1,1,0]
         * Explanation:
         * To the right of 5 there are 2 smaller elements (2 and 1).
         * To the right of 2 there is only 1 smaller element (1).
         * To the right of 6 there is 1 smaller element (1).
         * To the right of 1 there is 0 smaller element.
         */
        int[] nums = new int[]{5, 2, 6, 1};
        List<Integer> actual = countSmallerBST(nums);
        List<Integer> expected = new ArrayList<>(Arrays.asList(2, 1, 1, 0));
        assertEquals(expected.size(), actual.size());
        for(int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }
}
