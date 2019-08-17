public class numArraySegmentTree {

    /**
     * Given an integer array nums, find the sum of the elements between indices i and j (i ≤ j), inclusive.
     *
     * The update(i, val) function modifies nums by updating the element at index i to val.
     *
     * Note:
     *
     * The array is only modifiable by the update function.
     * You may assume the number of calls to update and sumRange function is distributed evenly.
     *
     * Approach 1: Segment Tree
     * 构建线段树来处理此类问题。线段树的构建是自底向上的，数的每个叶子节点代表range(i,i)即，数组中的第i个元素，然后两两向上merge，叶子节点的根节点为两个
     * 叶子节点之和。每个根节点相当于把当前节点的range一分为二
     * e.g. 给定一个数组[-2, 3, 0, 1, 5]
     * 构建好的线段树：
     *                     7 ([0-4])
     *                   /   \
     *                 1      6
     *              ([0-2]) ([3-4])
     *            /      \   /    \
     *          1        0  1      5
     *       ([0-1])([2-2])([3-3]))[4-4])
     *     /        \
     *   -2         3
     *  ([0-0])   ([1-1])
     *  所以想当于将原始的range不断地一分为二，然后当前节点即为现在这个range的sum，根节点为每个index的值。
     *
     * Time & Space:
     * 1.构建整个index大概需要遍历2n个节点，因为所有节点个数为1个range为n的节点，2个range为n/2的节点，4个range为n/4的节点，。。。，一直到n个range为1的节点
     * 即1 + 2 + 4 + ... + 2^logn（总共logn + 1项），和为2n - 1。所以时间，空间复杂度均为O(n)，
     * 2.在update的时候，只需要找到对应的index是在左子树还是右子树即可，在每个节点都可以舍弃掉一半的元素，因此运行时间为O(logn)
     * 3.在query的时候，若当前range被某节点完全覆盖，则可以进行直接query，当query的range跨左右子树时，就需要同时访问左右子树才能得到最终结果。因为树是balanced
     * 所以query的时间大致为O(logn)
     *
     */
    private segmentTree root;

    public numArraySegmentTree(int[] nums) {
        //要注意edge case，若输入为空数组，则无法构建树
        if(nums.length != 0) {
            this.root = buildTree(nums, 0, nums.length - 1);
        }
    }

    public void update(int i, int val) {
        updateTree(this.root, i, val);
    }

    public int sumRange(int i, int j) {
        return querySum(this.root, i, j);
    }

    //给定一个数组，返回由该数组建好的segment tree，同时要给定当前数组的运行范围
    private segmentTree buildTree(int[] nums, int low, int high) {
        //若low = high，说明只考虑当前一个节点，即数组中的某个元素
        if(low == high) {
            return new segmentTree(low, high, nums[low]);
        }
        //否则，需要找到当前range的中点，然后分别构建左右子树
        int mid = low + (high - low) / 2;
        segmentTree left = buildTree(nums, low, mid);
        segmentTree right = buildTree(nums, mid + 1, high);
        //最后返回以left和right分别为左右子树的节点
        //注意此时的根节点的值为左右子树值的和
        return new segmentTree(low, high, left.sum + right.sum, left, right);
    }

    private void updateTree(segmentTree root, int i, int val) {
        if(root.start == i && root.end == i) {
            root.sum = val;
            return;
        }
        int mid = root.start + (root.end - root.start) / 2;
        if(i <= mid) {
            updateTree(root.left, i, val);
        } else {
            updateTree(root.right, i, val);
        }
        //当更新了叶子节点的值，记得自底向上，将所有父节点的值也更新
        root.sum = root.left.sum + root.right.sum;
    }

    private int querySum(segmentTree root, int i, int j) {
        //若当前节点就包含了query的range，可以直接返回节点值
        if(root.start == i && root.end == j) {
            return root.sum;
        }
        int mid = root.start + (root.end - root.start) / 2;
        //否则，若range的右边界也小于当前节点的中点，说明该sum在当前节点的左子树中
        //只访问左子树
        if(j <= mid) {
            return querySum(root.left, i, j);
        } else if(i > mid) {
            //若range的左边界也大于当前节点的中点，说明该sum在当前节点的右子树中
            //只访问右子树
            return querySum(root.right, i, j);
        } else {
            //最后一种情况是，该range落在左右子树中，因此需要同时访问左右子树，然后返回两子树结果的和
            return querySum(root.left, i, mid) + querySum(root.right, mid + 1, j);
        }
    }


    private class segmentTree {
        private int start;   //表示当前节点的开始index
        private int end;     //表示当前节点的结束index
        private int sum;     //表示当前range的和
        private segmentTree left;
        private segmentTree right;


        //需要两种constructor，因为可能给出当前节点的左右节点，也可能不给出
        public segmentTree(int _start, int _end, int _val) {
            this.start = _start;
            this.end = _end;
            this.sum = _val;
        }


        public segmentTree(int _start, int _end, int _val, segmentTree _left, segmentTree _right) {
            this.start = _start;
            this.end = _end;
            this.sum = _val;
            this.left = _left;
            this.right = _right;
        }
    }
}
