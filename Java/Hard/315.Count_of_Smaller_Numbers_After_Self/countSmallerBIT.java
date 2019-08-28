import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class countSmallerBIT {

    /**
     * Approach 2: Fenwick Tree (Binary Indexed Tree)
     * 想利用Fenwick Tree解决问题，主要是考虑如何把问题转化成前缀和（prefix sum）的形式。如方法一讨论的一致，若从右往左倒序记录小于当前元素的值，就可以将
     * 先前记录的值累加起来。此时可以用Fenwick Tree实现在O(logn)时间内查找prefix sum和update节点值。同时还需要一步转化，即Fenwick Tree相当于一个
     * frequency map，记录每个元素出现的次数，当某个元素重复出现，就update对应节点值，而比元素小的元素数量总和就是在fenwick tree小于它的节点的prefix sum
     * 因为输入数组的每个元素的值可能很大，会出现很多redundant节点，因此先将数组进行排序，然后将每个元素和它的rank对应起来。因此对于一个长度为n的数组，其
     * rank的取值为1 - k，k是数组中unique元素的个数。最坏情况下为O(n)。
     *
     * Time: O(nlogn) 因为使用Fenwick tree，所以update和query的时间确定为O(logn)，同时因为希望对数组进行排序，要使用treeSet，treeSet中查找每个元素的
     *      时间也为O(logn)
     * Space: O(n), 需要将每个元素和对应的rank对应起来，同时Fenwick tree中的节点个数为unique元素的数量，两者最坏情况都为O(n)空间
     */
    public List<Integer> countSmallerBIT(int[] nums) {
        Set<Integer> sorted = new TreeSet<>();
        //因为要建立元素和rank之间的映射，所以需要用sorted set
        for(int num : nums) {
            sorted.add(num);
        }
        //tree的节点数量为unique元素的数量
        FenwickTree tree = new FenwickTree(sorted.size());
        Map<Integer, Integer> ranks = new HashMap<>();
        int rank = 0;
        //将sorted set中的元素和rank建立起映射关系
        for(int num : sorted) {
            ranks.put(num, ++rank);
        }
        List<Integer> res = new ArrayList<>();
        //然后倒序的访问数组的每一个元素
        for(int i = nums.length - 1; i >= 0; i--) {
            //将小于当前元素的所有元素的数量加起来即为结果
            //因为利用了元素值和rank之间的映射，所以只要求从1到rank - 1的prefix sum即可
            res.add(tree.query(ranks.get(nums[i]) - 1));
            //同时要更新当前元素所在节点的值（+1），同时fenwick tree会自动更新后续节点
            tree.update(ranks.get(nums[i]), 1);
        }
        //因为依旧是倒序遍历，所以需要将result list反转
        Collections.reverse(res);
        return res;
    }

    private class FenwickTree {
        private int[] sum;

        public FenwickTree(int size) {
            //因为Fenwick tree节点从1开始，需要size + 1个位置
            this.sum = new int[size + 1];
        }

        //更新节点值，同时更新所有可能的比它大的节点的值
        //注意更新值用的是变化量delta
        public void update(int i, int delta) {
            while(i < sum.length) {
                sum[i] += delta;
                i += lowbit(i);
            }
        }

        //查找小于当前节点的元素个数，即prefix sum
        //从当前节点寻找比它小的所有可能节点，将所有值累加起来
        public int query(int i) {
            int res = 0;
            while(i > 0) {
                res += sum[i];
                i -= lowbit(i);
            }
            return res;
        }


        private int lowbit(int x) {
            return x & (-x);
        }
    }

    @Test
    public void countSmallerBITTest() {
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
        List<Integer> actual = countSmallerBIT(nums);
        List<Integer> expected = new ArrayList<>(Arrays.asList(2, 1, 1, 0));
        assertEquals(expected.size(), actual.size());
        for(int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }
}
