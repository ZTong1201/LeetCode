import org.junit.Test;
import static org.junit.Assert.*;

public class paintHouseII {

    /**
     * There are a row of n houses, each house can be painted with one of the k colors. The cost of painting each house with a
     * certain color is different. You have to paint all the houses such that no two adjacent houses have the same color.
     *
     * The cost of painting each house with a certain color is represented by a n x k cost matrix. For example, costs[0][0] is the
     * cost of painting house 0 with color 0; costs[1][2] is the cost of painting house 1 with color 2, and so on...
     * Find the minimum cost to paint all houses.
     *
     * Note:
     * All costs are positive integers.
     *
     * Approach 1: Dynamic Programming
     * 与paint house I一样，到当前房子为止，将当前房子漆成某种颜色的最小cost为，到上一个房子为止，漆成非当前颜色的其他所有可能的最小值 + 将当前房子漆成
     * 当前颜色的cost。
     * 即 dp[i][j] = (k != j) min (dp[i - 1][k] + costs[i][j]
     * 因为之前计算过的cost之后不会再用，因此可以在原数组上进行操作，无需额外空间。最终结果为最后一行里的最小值
     *
     * Time: O(nk^2) 对于n*k的数组，每个格子都要遍历一遍，同时每个格子要看它上一行的其余k - 1个格子，因此总的运行时间为O(nk^2)
     * Space: O(1) 无需额外空间
     */
    public int minCostIIDP1(int[][] costs) {
        int n = costs.length;
        //注意edge case，可能输入为空数组
        if(n == 0) return 0;
        int k = costs[0].length;
        for(int i = 1; i < n; i++) {
            for(int j = 0; j < k; j++) {
                int min = Integer.MAX_VALUE;
                for(int t = 0; t < k; t++) {
                    if(t != j) {
                        min = Math.min(costs[i - 1][t], min);
                    }
                }
                costs[i][j] += min;
            }
        }
        int res = costs[n - 1][0];
        for(int i = 1; i < k; i++) {
            res = Math.min(res, costs[n - 1][i]);
        }
        return res;
    }

    /**
     * Follow up:
     * Could you solve it in O(nk) runtime?
     * Approach 2: Better DP
     * 本质上，在更新某一行的每个格子的值时，绝大多数时间加上的都是上一行的最小值，唯一的例外是当最小值与当前格子处于同一列时，此时需要加上其他列中的最小值。
     * 因此，在更新每一行的时候，只需要记住这一行前两小的元素，以及最小元素对应的列index即可。当遍历当前行时，若当前格子的列index与最小元素的列index不等是，
     * 都加上最小元素，反之则加上次小元素。同时在每一行更新过程中，继续更新当前行的前两小元素和最小元素的列index即可。最终返回的元素就是最后计算出的最小元素，
     * 因为它代表了最后一行的最小元素。
     *
     * Time: O(nk) 需要遍历每一个格子，在遍历每一个格子更新值的时候，不在需要遍历上一行的k - 1个元素，而是在两个元素中取一个，因此为O(1)时间
     * Space: O(1) 只分配了上一行和这一行的前两小元素和最小元素列index，无需额外空间
     */
    public int minCostIIDP2(int[][] costs) {
        //因为数组中元素一定都是正数，所以可以先将上一行最小值设为0，不影响第一行结果
        int preMin1 = 0, preMin2 = 0;
        //最小元素的列index，初始化为-1
        int preIndex = -1;
        //遍历每一行，更新当前行的前两小元素和最小元素列index
        for(int[] cost : costs) {
            int currMin1 = Integer.MAX_VALUE, currMin2 = Integer.MAX_VALUE;
            int currIndex = -1;
            //遍历当前行的每一个元素，更新值和index
            for(int i = 0; i < cost.length; i++) {
                int curr = cost[i];
                //若当前列index与最小值列index相等，就加上次小值
                if(i == preIndex) {
                    curr += preMin2;
                } else {
                    //否则加上最小值
                    curr += preMin1;
                }

                //接下来更新这一行的两个最小值和列index
                if(curr < currMin1) {
                    currIndex = i;
                    currMin2 = currMin1;
                    currMin1 = curr;
                } else if(curr < currMin2){
                    currMin2 = curr;
                }
            }
            //当前行遍历结束后，更新新的最小值和index
            preMin1 = currMin1;
            preMin2 = currMin2;
            preIndex = currIndex;
        }
        return preMin1;
    }

    @Test
    public void minCostIITest() {
        /**
         * Example:
         * Input: [[1,5,3],[2,9,4]]
         * Output: 5
         * Explanation: Paint house 0 into color 0, paint house 1 into color 2. Minimum cost: 1 + 4 = 5;
         *              Or paint house 0 into color 2, paint house 1 into color 0. Minimum cost: 3 + 2 = 5.
         */
        int[][] costs1 = new int[][]{{1, 5, 3}, {2, 9, 4}};
        assertEquals(5, minCostIIDP1(costs1));
        int[][] costs2 = new int[][]{{1, 5, 3}, {2, 9, 4}};
        assertEquals(5, minCostIIDP2(costs2));
    }
}
