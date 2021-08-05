import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class Triangle {

    /**
     * Given a triangle array, return the minimum path sum from top to bottom.
     * <p>
     * For each step, you may move to an adjacent number of the row below. More formally, if you are on index i on the
     * current row, you may move to either index i or index i + 1 on the next row
     * <p>
     * Constraints:
     * <p>
     * 1 <= triangle.length <= 200
     * triangle[0].length == 1
     * triangle[i].length == triangle[i - 1].length + 1
     * -10^4 <= triangle[i][j] <= 10^4
     * <p>
     * Approach 1: Bottom up DP - 1D
     * <p>
     * Time: O(n^2) for triangle with n rows, there are 1 + 2 + ... + n = n*(n + 1)/2 cells to be visited
     * Space: O(n) we need an array of size n
     */
    public int minimumTotal1D(List<List<Integer>> triangle) {
        int rows = triangle.size();
        int[] dp = new int[rows];
        // since it's a bottom up dp, fill the array with the numbers in the last row
        for (int i = 0; i < rows; i++) {
            dp[i] = triangle.get(rows - 1).get(i);
        }
        // starting from the second last row
        for (int i = rows - 2; i >= 0; i--) {
            int size = triangle.get(i).size();
            for (int j = 0; j < size; j++) {
                dp[j] = Math.min(dp[j], dp[j + 1]) + triangle.get(i).get(j);
            }
        }
        return dp[0];
    }

    /**
     * Approach 2: Bottom up DP - in place
     * We can update on the input list directly.
     * <p>
     * Time: O(n^2)
     * Space: O(1)
     */
    public int minimumTotalInplace(List<List<Integer>> triangle) {
        int rows = triangle.size();
        // still starting from the second last row
        for (int i = rows - 2; i >= 0; i--) {
            int size = triangle.get(i).size();
            for (int j = 0; j < size; j++) {
                // use set to update input list on the fly
                int minTotal = Math.min(triangle.get(i + 1).get(j), triangle.get(i + 1).get(j + 1)) + triangle.get(i).get(j);
                triangle.get(i).set(j, minTotal);
            }
        }
        return triangle.get(0).get(0);
    }

    @Test
    public void minimumTotalTest() {
        /**
         * Example 1:
         * Input: triangle = [[2],[3,4],[6,5,7],[4,1,8,3]]
         * Output: 11
         * Explanation: The triangle looks like:
         *    2
         *   3 4
         *  6 5 7
         * 4 1 8 3
         * The minimum path sum from top to bottom is 2 + 3 + 5 + 1 = 11.
         */
        List<List<Integer>> triangle1 = new ArrayList<>();
        triangle1.add(Arrays.asList(2));
        triangle1.add(Arrays.asList(3, 4));
        triangle1.add(Arrays.asList(6, 5, 7));
        triangle1.add(Arrays.asList(4, 1, 8, 3));
        assertEquals(11, minimumTotal1D(triangle1));
        assertEquals(11, minimumTotalInplace(triangle1));
        /**
         * Example 2:
         * Input: triangle = [[-10]]
         * Output: -10
         */
        List<List<Integer>> triangle2 = new ArrayList<>();
        triangle2.add(Arrays.asList(-10));
        assertEquals(-10, minimumTotal1D(triangle2));
        assertEquals(-10, minimumTotalInplace(triangle2));
    }
}
