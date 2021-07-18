import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CombinationSumIII {

    /**
     * Find all valid combinations of k numbers that sum up to n such that the following conditions are true:
     * <p>
     * Only numbers 1 through 9 are used.
     * Each number is used at most once.
     * Return a list of all possible valid combinations. The list must not contain the same combination twice,
     * and the combinations may be returned in any order
     * <p>
     * Approach: backtrack
     * <p>
     * Time: O(k * (9, k)) the total number of combinations is (9, k), in the worst case, all of the combinations should
     * be appended to the result list (which is impossible), appending (9, k) lists with the size of k requires
     * O(k * (9, k)) time
     * Space: O(k) if the final result list is not taken into consideration. The intermediate combination list is of size
     * k at maximum. The recursion call stack will start returning results when the size hits k as well.
     */
    private List<List<Integer>> res;

    public List<List<Integer>> combinationSum3(int k, int n) {
        res = new ArrayList<>();
        backtrack(k, n, 1, new ArrayList<>());
        return res;
    }

    private void backtrack(int k, int n, int start, List<Integer> combination) {
        // base case
        // when the size of current combination equals to k and the sum equals to n
        // add current combination (deep copy) to the result list
        if (combination.size() == k && n == 0) {
            res.add(new ArrayList<>(combination));
            // backtrack
            return;
        }
        for (int i = start; i <= 9; i++) {
            combination.add(i);
            // subtract n by i to update the remainder
            // increment i since each element can only be used once
            backtrack(k, n - i, i + 1, combination);
            combination.remove(combination.size() - 1);
        }
    }

    @Test
    public void combinationSum3Test() {
        /**
         * Example 1:
         * Input: k = 3, n = 7
         * Output: [[1,2,4]]
         * Explanation:
         * 1 + 2 + 4 = 7
         * There are no other valid combinations.
         */
        List<List<Integer>> expected1 = List.of(List.of(1, 2, 4));
        List<List<Integer>> actual1 = combinationSum3(3, 7);
        assertEquals(expected1.size(), actual1.size());
        for (int i = 0; i < expected1.size(); i++) {
            assertEquals(expected1.get(i).size(), actual1.get(i).size());
            for (int j = 0; j < expected1.get(i).size(); j++) {
                assertEquals(expected1.get(i).get(j), actual1.get(i).get(j));
            }
        }
        /**
         * Example 2:
         * Input: k = 3, n = 9
         * Output: [[1,2,6],[1,3,5],[2,3,4]]
         * Explanation:
         * 1 + 2 + 6 = 9
         * 1 + 3 + 5 = 9
         * 2 + 3 + 4 = 9
         * There are no other valid combinations.
         */
        List<List<Integer>> expected2 = List.of(List.of(1, 2, 6), List.of(1, 3, 5), List.of(2, 3, 4));
        List<List<Integer>> actual2 = combinationSum3(3, 9);
        assertEquals(expected2.size(), actual2.size());
        for (int i = 0; i < expected2.size(); i++) {
            assertEquals(expected2.get(i).size(), actual2.get(i).size());
            for (int j = 0; j < expected2.get(i).size(); j++) {
                assertEquals(expected2.get(i).get(j), actual2.get(i).get(j));
            }
        }
    }
}
