import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class Combinations {

    /**
     * Given two integers n and k, return all possible combinations of k numbers out of the range [1, n].
     * <p>
     * You may return the answer in any order.
     * <p>
     * Constraints:
     * <p>
     * 1 <= n <= 20
     * 1 <= k <= n
     * <p>
     * Approach: backtrack
     * <p>
     * Time: O(k * (n, k)) the only time-consuming part is to append (n, k) of length K combination list to the result list
     * Space: O((n, k)) the list of all combinations is of size (n, k)
     */
    private List<List<Integer>> res;

    public List<List<Integer>> combine(int n, int k) {
        res = new ArrayList<>();
        backtrack(n, k, 1, new ArrayList<>());
        return res;
    }

    private void backtrack(int n, int k, int start, List<Integer> combination) {
        if (combination.size() == k) {
            res.add(new ArrayList<>(combination));
        }
        for (int i = start; i <= n; i++) {
            combination.add(i);
            backtrack(n, k, i + 1, combination);
            combination.remove(combination.size() - 1);
        }
    }

    @Test
    public void combineTest() {
        /**
         * Example 1:
         * Input: n = 4, k = 2
         * Output:
         * [
         *   [2,4],
         *   [3,4],
         *   [2,3],
         *   [1,2],
         *   [1,3],
         *   [1,4],
         * ]
         */
        List<List<Integer>> actual = combine(4, 2);
        List<List<Integer>> expected = new ArrayList<>();
        expected.add(List.of(1, 2));
        expected.add(List.of(1, 3));
        expected.add(List.of(1, 4));
        expected.add(List.of(2, 3));
        expected.add(List.of(2, 4));
        expected.add(List.of(3, 4));
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).get(0), actual.get(i).get(0));
            assertEquals(expected.get(i).get(1), actual.get(i).get(1));
        }
    }
}
