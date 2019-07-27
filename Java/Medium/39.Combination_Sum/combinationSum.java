import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class combinationSum {

    /**
     * Given a set of candidate numbers (candidates) (without duplicates) and a target number (target), find all unique combinations in
     * candidates where the candidate numbers sums to target.
     *
     * The same repeated number may be chosen from candidates unlimited number of times.
     *
     * Note:
     * All numbers (including target) will be positive integers.
     * The solution set must not contain duplicate combinations.
     *
     * Approach 1: Backtracking (With For Loop)
     * We can iterate over the array and keep adding elements in the sub list as long as the we don't reach the target value. If at any
     * time we sum up to the target, we add the current list to the final list. If the sum exceeds the target, we backtrack to the previous
     * stage and search for the next value. The only thing we need to care about is that since the result allows duplicates, when searching
     * for a new element, we will start from the previous stage.
     *
     * Time: O(n*target) since all elements are positive numbers (1 is the smallest). If we have solutions, and in the worst case, all the
     *       numbers are 1. For each 1, we might search target times to find a result
     * Space: O(min(target, n)) in order to search for a result, we either iterate it until the end of the array or stop when the current
     *       sum is larger than or equal to the target.
     */
    public List<List<Integer>> combinationSumBacktracking(int[] candidates, int target) {
        List<List<Integer>> res = new ArrayList<>();
        List<Integer> combo = new ArrayList<>();
        backtrack(candidates, target, res, combo, 0);
        return res;
    }

    private void backtrack(int[] candidates, int target, List<List<Integer>> res, List<Integer> combo, int begin) {
        //if the current sum exceeds the target, simply backtrack to the previous stage
        if(target < 0) {
            return;
        }
        //if the current sum equals to the target, we find a solution, add it into the final list and backtrack
        if(target == 0) {
            res.add(new ArrayList<>(combo));
            return;
        }
        //we keep adding elements in the combo list always from the previous starting point to allow duplicates
        for(int i = begin; i < candidates.length; i++) {
            //add an element
            combo.add(candidates[i]);
            //keep searching from the current position
            //remember to subtract the current value from the target
            backtrack(candidates, target - candidates[i], res, combo, i);
            //if we found a solution or we cannot found a solution, backtrack to the previous stage
            combo.remove(combo.size() - 1);
        }
    }

    @Test
    public void combinationSumBacktrackingTest() {
        /**
         * Example 1:
         * Input: candidates = [2,3,6,7], target = 7,
         * A solution set is:
         * [
         *   [7],
         *   [2,2,3]
         * ]
         */
        int[] candidates1 = new int[]{2, 3, 6, 7};
        List<List<Integer>> actual1 = combinationSumBacktracking(candidates1, 7);
        List<List<Integer>> expected1 = new ArrayList<>();
        expected1.add(new ArrayList<>(Arrays.asList(2, 2, 3)));
        expected1.add(new ArrayList<>(Arrays.asList(7)));
        assertEquals(expected1.size(), actual1.size());
        for(int i = 0; i < expected1.size(); i++) {
            assertEquals(expected1.get(i).size(), actual1.get(i).size());
            Set<Integer> expectedSet1 = new HashSet<>(expected1.get(i));
            Set<Integer> actualSet1 = new HashSet<>(actual1.get(i));
            for(int num : expectedSet1) {
                assertTrue(actualSet1.contains(num));
            }
        }
        /**
         * Example 2:
         * Input: candidates = [2,3,5], target = 8,
         * A solution set is:
         * [
         *   [2,2,2,2],
         *   [2,3,3],
         *   [3,5]
         * ]
         */
        int[] candidates2 = new int[]{2, 3, 5};
        List<List<Integer>> actual2 = combinationSumBacktracking(candidates2, 8);
        List<List<Integer>> expected2 = new ArrayList<>();
        expected2.add(new ArrayList<>(Arrays.asList(2, 2, 2, 2)));
        expected2.add(new ArrayList<>(Arrays.asList(2, 3, 3)));
        expected2.add(new ArrayList<>(Arrays.asList(3, 5)));
        assertEquals(expected2.size(), actual2.size());
        for(int i = 0; i < expected2.size(); i++) {
            assertEquals(expected2.get(i).size(), actual2.get(i).size());
            Set<Integer> expectedSet2 = new HashSet<>(expected2.get(i));
            Set<Integer> actualSet2 = new HashSet<>(actual2.get(i));
            for(int num : expectedSet2) {
                assertTrue(actualSet2.contains(num));
            }
        }
    }

    /**
     * Approach 2: Backtrack (Without For Loop)
     * Same idea as above without using a for loop. Since after backtracking, we need to keep searching the next position. We just
     * implement it by adding another recursion call by maintaining current target value yet incrementing the index to its next position
     *
     * Time: O(n*target) since all elements are positive numbers (1 is the smallest). If we have solutions, and in the worst case, all the
     *      numbers are 1. For each 1, we might search target times to find a result
     * Space: O(min(target, n)) in order to search for a result, we either iterate it until the end of the array or stop when the current
     *       sum is larger than or equal to the target.
     */
    public List<List<Integer>> combinationSumWithoutLoop(int[] candidates, int target) {
        List<List<Integer>> res = new ArrayList<>();
        List<Integer> combo = new ArrayList<>();
        backtrackWithoutLoop(candidates, target, res, combo, 0);
        return res;
    }

    private void backtrackWithoutLoop(int[] candidates, int target, List<List<Integer>> res, List<Integer> combo, int index) {
        //if target reaches 0, we found a solution, add it to final list
        if(target == 0) {
            res.add(new ArrayList<>(combo));
            return;
        }
        //if current index is out of boundary, simply return
        if(index >= candidates.length) {
            return;
        }
        //if the target left over is < current value to be search, we will exceed the target if adding it
        //hence we will only search when the target is larger
        if(target >= candidates[index]) {
            //adding element
            combo.add(candidates[index]);
            //keep search and update the target. Since allowing duplicates, we still search from the current index
            backtrackWithoutLoop(candidates, target - candidates[index], res, combo, index);
            combo.remove(combo.size() - 1);
        }

        //if we backtracked, which means adding element at current index cannot form a solution
        //we need to increment the index using a same target
        backtrackWithoutLoop(candidates, target, res, combo, index + 1);
    }

    @Test
    public void combinationSumWithoutLoopTest() {
        /**
         * Example 1:
         * Input: candidates = [2,3,6,7], target = 7,
         * A solution set is:
         * [
         *   [7],
         *   [2,2,3]
         * ]
         */
        int[] candidates1 = new int[]{2, 3, 6, 7};
        List<List<Integer>> actual1 = combinationSumWithoutLoop(candidates1, 7);
        List<List<Integer>> expected1 = new ArrayList<>();
        expected1.add(new ArrayList<>(Arrays.asList(2, 2, 3)));
        expected1.add(new ArrayList<>(Arrays.asList(7)));
        assertEquals(expected1.size(), actual1.size());
        for(int i = 0; i < expected1.size(); i++) {
            assertEquals(expected1.get(i).size(), actual1.get(i).size());
            Set<Integer> expectedSet1 = new HashSet<>(expected1.get(i));
            Set<Integer> actualSet1 = new HashSet<>(actual1.get(i));
            for(int num : expectedSet1) {
                assertTrue(actualSet1.contains(num));
            }
        }
        /**
         * Example 2:
         * Input: candidates = [2,3,5], target = 8,
         * A solution set is:
         * [
         *   [2,2,2,2],
         *   [2,3,3],
         *   [3,5]
         * ]
         */
        int[] candidates2 = new int[]{2, 3, 5};
        List<List<Integer>> actual2 = combinationSumWithoutLoop(candidates2, 8);
        List<List<Integer>> expected2 = new ArrayList<>();
        expected2.add(new ArrayList<>(Arrays.asList(2, 2, 2, 2)));
        expected2.add(new ArrayList<>(Arrays.asList(2, 3, 3)));
        expected2.add(new ArrayList<>(Arrays.asList(3, 5)));
        assertEquals(expected2.size(), actual2.size());
        for(int i = 0; i < expected2.size(); i++) {
            assertEquals(expected2.get(i).size(), actual2.get(i).size());
            Set<Integer> expectedSet2 = new HashSet<>(expected2.get(i));
            Set<Integer> actualSet2 = new HashSet<>(actual2.get(i));
            for(int num : expectedSet2) {
                assertTrue(actualSet2.contains(num));
            }
        }
    }
}
