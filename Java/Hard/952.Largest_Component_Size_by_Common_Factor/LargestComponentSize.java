import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class LargestComponentSize {

    /**
     * You are given an integer array of unique positive integers nums. Consider the following graph:
     * <p>
     * There are nums.length nodes, labeled nums[0] to nums[nums.length - 1],
     * There is an undirected edge between nums[i] and nums[j] if nums[i] and nums[j] share a common factor greater than 1.
     * Return the size of the largest connected component in the graph.
     * <p>
     * Constraints:
     * <p>
     * 1 <= nums.length <= 2 * 10^4
     * 1 <= nums[i] <= 10^5
     * All the values of nums are unique.
     * <p>
     * Approach: Union via factors
     * For a given number, we can union it with all the factors (except 1). For example, 6 will be unioned with 2 and 3,
     * and 4 will be unioned with 2 and 4. In this case, 2, 3, 4, 6 will be treated as one group. After the union operations
     * are done, we can reiterate the num array add count the number of each group to get the largest size.
     * <p>
     * Time: O(N * sqrt(M)) where M is the largest number in the array. The number of factors will be bounded by O(sqrt(M)),
     * for each number, we need list all factors and make the union. - union takes approximately O(1) runtime in practice.
     * Space: O(N + M) we need M + 1 cells in the union find data structure, and in the end we need to compute the size of
     * each group. In the worst case, we might have N distinct groups.
     */
    public int largestComponentSize(int[] nums) {
        int maxValue = findMax(nums);
        // since we need a cell for the maxValue, we should increment the size by 1
        // to avoid index out bound
        UnionFind uf = new UnionFind(maxValue + 1);
        // union each number with its factors
        for (int num : nums) {
            for (int factor = 2; factor <= (int) Math.sqrt(num) + 1; factor++) {
                if ((num % factor) == 0) {
                    uf.union(num, factor);
                    // also, union num with the complement factor
                    // however, we should never union num with 1
                    if (num / factor != 1) uf.union(num, num / factor);
                }
            }
        }
        // now everything is connected together
        // count the size of each group
        int res = 0;
        Map<Integer, Integer> groupSize = new HashMap<>();
        for (int num : nums) {
            // always find the root node of the group
            int group = uf.find(num);
            int count = groupSize.getOrDefault(group, 0);
            groupSize.put(group, count + 1);
            // update the largest size
            res = Math.max(res, count + 1);
        }
        return res;
    }

    private int findMax(int[] nums) {
        int res = 0;
        for (int num : nums) {
            res = Math.max(res, num);
        }
        return res;
    }

    @Test
    public void largestComponentSizeTest() {
        /**
         * Example 1:
         * Input: nums = [4,6,15,35]
         * Output: 4
         */
        assertEquals(4, largestComponentSize(new int[]{4, 6, 15, 35}));
        /**
         * Example 2:
         * Input: nums = [20,50,9,63]
         * Output: 2
         */
        assertEquals(2, largestComponentSize(new int[]{20, 50, 9, 63}));
        /**
         * Example 3:
         * Input: nums = [1,2,3,4,5,6,7,8,9]
         * Output: 6
         */
        assertEquals(6, largestComponentSize(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9}));
    }

    private static class UnionFind {
        private final int[] parent;
        private final int[] size;

        public UnionFind(int n) {
            this.parent = new int[n];
            this.size = new int[n];
            Arrays.fill(this.size, 1);
            for (int i = 0; i < n; i++) {
                this.parent[i] = i;
            }
        }

        public int find(int i) {
            while (i != parent[i]) {
                parent[i] = parent[parent[i]];
                i = parent[i];
            }
            return i;
        }

        public void union(int i, int j) {
            int p = find(i);
            int q = find(j);
            if (p == q) return;
            if (size[p] >= size[q]) {
                parent[q] = p;
                size[p] += size[q];
            } else {
                parent[p] = q;
                size[q] += size[p];
            }
        }

        public int largestSize() {
            int res = 0;
            for (int componentSize : size) {
                res = Math.max(res, componentSize);
            }
            return res;
        }
    }
}
