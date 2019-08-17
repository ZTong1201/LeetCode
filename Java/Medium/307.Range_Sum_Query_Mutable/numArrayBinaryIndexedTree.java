public class numArrayBinaryIndexedTree {

    /**
     * Approach 2: Fenwick Tree (Binary Indexed Tree)
     * 此题也可以利用fenwick tree来解。即与303相比，因为数组中元素可以改变，因此如果只记住prefix sum在update时仍需要O(n)时间进行更新，然后求得range sum。
     * Fenwick tree就是用一个类似树的结构，将一些partial sum存在每个节点中。两个节点的父子关系由当前节点index的lowbit决定。lowbit是least significant bit，
     * e.g. 例如5的binary表达式为0101，那么-5的表达式为(~5) + 1 = 1010 + 0001 = 1011，那么5的lowbit = 5 & (-5) = 0001，那么5的下一个节点为
     * 0101 + 0001 = 0110 = 6。所以由此可以确定每个节点的parent节点。
     *
     * Fenwick Tree其实并不真正需要树的结构，只需要用一个数组，隐式的找到index之间的关系即可。树的更新和查询有不同的结构
     * 1.update tree:
     * 如果开始某节点（节点5）值为5，想将其更新为7，即变化量delta = 2，可以从起始节点5开始，将其值加上delta，然后沿着树自底向上（将当前节点加上其lowbit），
     * 找到其所有parent节点，将每个节点的值都加上delta即可。某个节点的值代表的是其所有孩子的值得和。
     * 2.query tree:
     * 与prefix sum类似，根据已经记录下来的partial sum，可以准确计算0到每个节点的值。此时需要从起始节点开始，每次减去其lowbit，找到其反序的parent节点，
     * 将所有经过的节点值相加，即可得到0到该index的值。
     *
     * 几点注意：
     * 1.因为要计算lowbit，所以Fenwick tree的节点要从1开始算起，因此初始化数组size为n + 1
     * 2.与利用prefix sum时类似，在query range(i, j)的sum时需要用query(j + 1) - query(i)才能得到正确结果
     * 3.因为要计算delta值，所以要将输入数组进行copy，然后每次更新都需要将copy的数组值进行更新
     *
     * Time & Space:
     * 初始化：初始化本质上需要将从1到n + 1的每个节点值更新，因此总的时间复杂度为sum(ilogi)，其upper bound为O(nlogn)。所需空间为O(n)，因为需要将原数组进行
     *        copy，同时Fenwick tree需要size为n + 1的数组
     * update：每次update都需要O(logn)时间，所以call M次的时间为O(Mlogn)
     * query: 每次query range sum都需要两个query，时间为O(2logn)，call M次的时间为O(Mlogn)
     */
    private FenwickTree root;
    private int[] nums;   //需要对数组进行copy，在更新值得时候也更新数组的值，方便计算变化量delta

    public numArrayBinaryIndexedTree(int[] nums) {
        //注意edge case，若数组为空，则不需要建树
        if(nums.length != 0) {
            this.root = new FenwickTree(nums);
            this.nums = nums;
        }
    }

    public void update(int i, int val) {
        //更新树中元素，记得更新时要去变化量，因此要取新元素和原数组中元素的差
        this.root.updateTree(i, val - nums[i]);
        //记得更新数组中的值，方便下次计算变化量
        this.nums[i] = val;
    }


    public int sumRange(int i, int j) {
        //要计算range sum，分别计算从0到两个index的值，然后去两者的差
        return this.root.querySum(j + 1) - this.root.querySum(i);
    }


    private class FenwickTree {
        private int[] root;

        public FenwickTree(int[] nums) {
            //用数组来存储树节点，size为n + 1
            this.root = new int[nums.length + 1];
            //初始化整个树，就是对树的每个节点进行更新
            for(int i = 0; i < nums.length; i++) {
                updateTree(i, nums[i]);
            }
        }

        //注意在更新时，要在原位置基础上，加上一个变化值
        public void updateTree(int i, int delta) {
            //注意Fenwick tree的index从1开始，所以需要对原始index + 1
            i += 1;
            while(i < root.length) {
                root[i] += delta;
                //将当前index加上其lowbit，得到其parent节点，直到index超出范围
                i += lowbit(i);
            }
        }

        //计算从0开始到当前节点的sum
        public int querySum(int i) {
            int res = 0;
            while(i > 0) {
                res += root[i];
                //此时需要将当前index减去其lowbit，得到下一个节点，直到index为1
                i -= lowbit(i);
            }
            return res;
        }


        //计算当前节点index的lowbit，用于更新下一个节点
        private int lowbit(int x) {
            return x & (-x);
        }
    }
}
