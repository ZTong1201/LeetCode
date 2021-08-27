import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class LongestConsecutiveSeq {

    /**
     * Given an unsorted array of integers, find the length of the longest consecutive elements sequence.
     * Your algorithm should run in O(n) complexity.
     * <p>
     * Approach 1: Hash Set + find smallest start
     * 本质上，为了找到最长的sequence，就要先找到该sequence中的第一个元素，即最小的元素，然后从该元素num开始，依次判断num + 1是否也在数组中，直到
     * num + 1不在数组中，此时的sequence就是以该最小元素为起点的一个最长sequence，为了在O(1)时间确定某元素是否在数组中，先将数组元素都存在set中
     * <p>
     * Time: O(n), 每个元素至多会被遍历两边，第一遍判断该元素是否为此次sequence的开头，第二次为从sequence的最小元素开始，一直判断num + 1是否在set中
     * Space: O(n) 需要将所有unique元素放入set中
     */
    public int longestConsecutiveSet(int[] nums) {
        Set<Integer> seen = new HashSet<>();
        for (int num : nums) {
            seen.add(num);
        }
        //先将最大值设为0，因为若数组为空，则最大长度是0
        int longestLength = 0;
        for (int num : seen) {
            //先判断当前元素是不是当前sequence的最小元素，即num - 1不在set中
            if (!seen.contains(num - 1)) {
                //那么就以当前元素为起点，寻找可能的最长sequ
                int currVal = num, currLongest = 1;

                while (seen.contains(currVal + 1)) {
                    //若当前元素 + 1也在set中，就继续判断能否形成更长的sequence，直到找到该sequence的最后一个元素
                    currVal += 1;
                    currLongest += 1;
                }
                //更新最大值
                longestLength = Math.max(longestLength, currLongest);
            }
        }
        return longestLength;
    }


    /**
     * Approach 2: Union Find
     * 此题也可以用union find来解，即对于某元素num，若其num - 1或num + 1也在数组中，就将其union起来。这样可以确保所有consecutive的sequence元素都在
     * 一个connected component中，只需要找到所有connected component的size的最大值，即为最终结果。
     * <p>
     * Time: O(n*a(n)) 要遍历整个数组，将相邻元素union起来，union的时间复杂度为O(a(n))，可以近似认为成O(1)，因此总的运行时间约为O(n)
     * Space: O(n) 建立union find最多需要n个节点，同时还要记录每个节点的size。同时还需要一个map记录元素和index的对应关系
     */
    public int longestConsecutiveUnionFind(int[] nums) {
        //edge case，若数组为空，返回0
        if (nums == null || nums.length == 0) return 0;
        unionFind uf = new unionFind(nums.length);
        //用hash map记录每个元素和union find中节点（即nums中的index）的关系，同时可以在O(1)时间判断某元素是否在数组中
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int num = nums[i];
            //数组中可能存在重复元素，若元素已被使用过，直接跳过
            if (map.containsKey(num)) continue;
            map.put(num, i);
            //然后判断num - 1和num + 1是否在数组中，若存在，就将两个元素union起来
            if (map.containsKey(num - 1)) {
                uf.union(i, map.get(num - 1));
            }
            if (map.containsKey(num + 1)) {
                uf.union(i, map.get(num + 1));
            }
        }
        //返回所有connected component的最大size
        return uf.maxSize();
    }

    private static class unionFind {
        private final int[] parents;
        private final int[] size;
        //max记录所有connected component中的size最大值，可以在union的时候更新
        private int max;

        public unionFind(int n) {
            this.parents = new int[n];
            this.size = new int[n];
            for (int i = 0; i < n; i++) {
                this.parents[i] = i;
            }
            Arrays.fill(this.size, 1);
            this.max = 1;
        }

        private int parent(int i) {
            while (i != parents[i]) {
                parents[i] = parents[parents[i]];
                i = parents[i];
            }
            return i;
        }

        public void union(int i, int j) {
            int p = parent(i);
            int q = parent(j);
            if (p == q) return;
            if (size[p] >= size[q]) {
                size[p] += size[q];
                parents[q] = p;
                max = Math.max(max, size[p]);
            } else {
                size[q] += size[p];
                parents[p] = q;
                max = Math.max(max, size[q]);
            }
        }

        public int maxSize() {
            return this.max;
        }
    }


    @Test
    public void longestConsecutiveTest() {
        /**
         * Example 1:
         * Input: [100, 4, 200, 1, 3, 2]
         * Output: 4
         * Explanation: The longest consecutive elements sequence is [1, 2, 3, 4]. Therefore its length is 4.
         */
        int[] nums1 = new int[]{100, 4, 200, 1, 3, 2};
        assertEquals(4, longestConsecutiveSet(nums1));
        assertEquals(4, longestConsecutiveUnionFind(nums1));
        /**
         * Example 2:
         * Input: [0, 0, -1]
         * Output: 2
         * Explanation: The longest consecutive elements sequence is [-1, 0]. Therefore its length is 2.
         */
        int[] nums2 = new int[]{0, 0, -1};
        assertEquals(2, longestConsecutiveSet(nums2));
        assertEquals(2, longestConsecutiveUnionFind(nums2));
    }
}
