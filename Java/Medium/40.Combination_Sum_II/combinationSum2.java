import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class combinationSum2 {

    /**
     * Given a collection of candidate numbers (candidates) and a target number (target), find all unique combinations in candidates
     * where the candidate numbers sums to target.
     *
     * Each number in candidates may only be used once in the combination.
     *
     * Note:
     *
     * All numbers (including target) will be positive integers.
     * The solution set must not contain duplicate combinations.
     *
     * Approach 1: Backtrack (With For Loop)
     *
     * The difference compared with combination sum I is just we have duplicates in the array and we can only use each value once. To avoid
     * duplicate usage of the same value, we can always increment the index as we search for the result. On the other hand, to avoid
     * same combinations in the final list, we can initially sort the array. When we done all the search for a given value, we will skip
     * all the subsequent duplicates and restart searching on a different value.
     *
     * Time: O(n*target)
     * Space: O(min(n,target))
     */
    public List<List<Integer>> combinationSum2Backtrack(int[] candidates, int target) {
        List<List<Integer>> res = new ArrayList<>();
        List<Integer> combo = new ArrayList<>();
        //Sorting will make handling duplicates easier
        Arrays.sort(candidates);
        backtrack(candidates, target, res, combo, 0);
        return res;
    }

    private void backtrack(int[] candidates, int target, List<List<Integer>> res, List<Integer> combo, int index) {
        //if target left over is 0, add current combination to the final list
        if(target == 0) {
            res.add(new ArrayList<>(combo));
            return;
        }
        //always start searching from the new index
        for(int i = index; i < candidates.length; i++) {
            //if the target left over less that current value, we will always exceed when adding subsequent values, return directly
            if(target < candidates[i]) {
                return;
            }
            //handle duplicates, if we find a same starting value except the starting index, we skip that value to find a new value
            if(i != index && candidates[i] == candidates[i - 1]) {
                continue;
            }
            //otherwise, add current element to the sub list
            combo.add(candidates[i]);
            //keep search by incrementing the index and subtracting current value from the target
            backtrack(candidates, target - candidates[i], res, combo, i + 1);
            //backtrack to the previous stage
            combo.remove(combo.size() - 1);
        }
    }

    @Test
    public void combinationSum2BacktrackTest() {
        /**
         * Example 1:
         * Input: candidates = [10,1,2,7,6,1,5], target = 8,
         * A solution set is:
         * [
         *   [1, 7],
         *   [1, 2, 5],
         *   [2, 6],
         *   [1, 1, 6]
         * ]
         */
        int[] candidates1 = new int[]{10, 1, 2, 7, 6, 1, 5};
        List<List<Integer>> actual1 = combinationSum2Backtrack(candidates1, 8);
        List<List<Integer>> expected1 = new ArrayList<>();
        expected1.add(new ArrayList<>(Arrays.asList(1, 1, 6)));
        expected1.add(new ArrayList<>(Arrays.asList(1, 2, 5)));
        expected1.add(new ArrayList<>(Arrays.asList(1, 7)));
        expected1.add(new ArrayList<>(Arrays.asList(2, 6)));
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
         * Input: candidates = [2,5,2,1,2], target = 5,
         * A solution set is:
         * [
         *   [1,2,2],
         *   [5]
         * ]
         */
        int[] candidates2 = new int[]{2, 5, 2, 1, 2};
        List<List<Integer>> actual2 = combinationSum2Backtrack(candidates2, 5);
        List<List<Integer>> expected2 = new ArrayList<>();
        expected2.add(new ArrayList<>(Arrays.asList(1, 2, 2)));
        expected2.add(new ArrayList<>(Arrays.asList(5)));
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
     *
     * Just as we did in combination sum I, we can avoid using for loop to keep searching. Just add one more recursion call at the end
     * of the function by incrementing the searching index whereas maintaining the searching value.
     * Similarly, we have to skip duplicates before we actually start searching
     *
     * Time: O(n*target)
     * Space: O(n*target)
     */
    public List<List<Integer>> combinationSum2WithoutLoop(int[] candidates, int target) {
        List<Integer> combo = new ArrayList<>();
        List<List<Integer>> res = new ArrayList<>();
        Arrays.sort(candidates);
        backtrackWithoutLoop(candidates, target, combo, res, 0);
        return res;
    }

    private void backtrackWithoutLoop(int[] candidates, int target, List<Integer> combo, List<List<Integer>> res, int index) {
        //if target left over is 0, add current combination to the final list
        if(target == 0) {
            res.add(new ArrayList<>(combo));
            return;
        }
        //if current index is out of boundary, simply return
        if(index >= candidates.length) {
            return;
        }
        //if the target left over is >= the current value
        //which means we have something to search
        if(target >= candidates[index]) {
            //add current element to the combo list
            combo.add(candidates[index]);
            //keep searching by subtracting current value and incrementing the current index
            backtrackWithoutLoop(candidates, target - candidates[index], combo, res, index + 1);
            //backtrack to the previous stage
            combo.remove(combo.size() - 1);
        }
        //before we restart searching, we need to skip duplicates of the previous searched value
        while(index + 1 < candidates.length && candidates[index] == candidates[index + 1]) {
            index++;
        }
        //restarting searching by maintaining current target value and incrementing the current index
        backtrackWithoutLoop(candidates, target, combo, res, index + 1);
    }

    @Test
    public void combinationSum2WithoutLoopTest() {
        /**
         * Example 1:
         * Input: candidates = [10,1,2,7,6,1,5], target = 8,
         * A solution set is:
         * [
         *   [1, 7],
         *   [1, 2, 5],
         *   [2, 6],
         *   [1, 1, 6]
         * ]
         */
        int[] candidates1 = new int[]{10, 1, 2, 7, 6, 1, 5};
        List<List<Integer>> actual1 = combinationSum2WithoutLoop(candidates1, 8);
        List<List<Integer>> expected1 = new ArrayList<>();
        expected1.add(new ArrayList<>(Arrays.asList(1, 1, 6)));
        expected1.add(new ArrayList<>(Arrays.asList(1, 2, 5)));
        expected1.add(new ArrayList<>(Arrays.asList(1, 7)));
        expected1.add(new ArrayList<>(Arrays.asList(2, 6)));
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
         * Input: candidates = [2,5,2,1,2], target = 5,
         * A solution set is:
         * [
         *   [1,2,2],
         *   [5]
         * ]
         */
        int[] candidates2 = new int[]{2, 5, 2, 1, 2};
        List<List<Integer>> actual2 = combinationSum2WithoutLoop(candidates2, 5);
        List<List<Integer>> expected2 = new ArrayList<>();
        expected2.add(new ArrayList<>(Arrays.asList(1, 2, 2)));
        expected2.add(new ArrayList<>(Arrays.asList(5)));
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
