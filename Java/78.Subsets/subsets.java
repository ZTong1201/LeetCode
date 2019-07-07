import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class subsets {

    /**
     * Given a set of distinct integers, nums, return all possible subsets (the power set).
     *
     * Note: The solution set must not contain duplicate subsets.
     *
     * Approach 1: Depth-first search (Backtrack)
     * We can use backtrack to solve this problem easily. Keep searching for subset, and if we have reached all the states for current
     * starting element, we backtrack to its previous stage and run it again. There are two main tricks here:
     *
     * 1. An empty set is also a subset. Hence we need to add the subset to the final result list from previous call at the very beginning
     * 2. We can pass in a index value to record the current stage, and increment it at each function call
     *
     * Time: O(2^n) since we have 2^n subsets to search for
     * Space: O(n) if the final result list doesn't count. The implicit call stack will require O(n) since we need the search all the way
     *        to the whole set to start to backtrack.
     */
    public List<List<Integer>> subsetsDFS(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        List<Integer> subset = new ArrayList<>();
        backtrack(res, subset, 0, nums);
        return res;
    }

    private void  backtrack(List<List<Integer>> res, List<Integer> subset, int start, int[] nums) {
        res.add(new ArrayList<>(subset));  // add the generated subset from previous call
        for(int i = start; i < nums.length; i++) {
            subset.add(nums[i]);                          //add new element
            backtrack(res, subset, i + 1, nums);    //keep searching for results
            subset.remove(subset.size() - 1);      //backtrack
        }
    }

    /**
     * Approach 2: Breadth-first search
     * We can also use a BFS to solve this problem. The trick here is to search all the subsets already in the list, and add current element
     * to each of them and obtain new subsets whose number equals to the size of current list.
     * For example, if we have [ [], [1] ] in the list, when the current element is 2, we have [ [], [1], [2], [1,2] ] after BFS
     *
     * Time: O(2^n) Since we still have 2^n subsets to search for
     * Space: O(n) we need to construct a new subset for each subset already in the list. The largest length of this new subset is O(n)
     */
    public List<List<Integer>> subsetsBFS(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        res.add(new ArrayList<>());   //add an empty subset

        for(int num : nums) {  //search for each element
            int size = res.size();

            for(int i = 0; i < size; i++) {  //iterate over all the current subsets
                List<Integer> subset = new ArrayList<>(res.get(i));   //we need to construct new subsets, hence we use new keyword
                subset.add(num);
                res.add(subset);
            }
        }
        return res;
    }


    @Test
    public void subsetsDFSTest() {
        /**
         * Input: nums = [1,2,3]
         * Output:
         * [
         *   [3],
         *   [1],
         *   [2],
         *   [1,2,3],
         *   [1,3],
         *   [2,3],
         *   [1,2],
         *   []
         * ]
         */
        int[] nums = new int[]{1, 2, 3};
        Set<List<Integer>> expected = new HashSet<>();
        expected.add(new ArrayList<>());
        expected.add(new ArrayList<>(Arrays.asList(1)));
        expected.add(new ArrayList<>(Arrays.asList(2)));
        expected.add(new ArrayList<>(Arrays.asList(3)));
        expected.add(new ArrayList<>(Arrays.asList(1, 2)));
        expected.add(new ArrayList<>(Arrays.asList(1, 3)));
        expected.add(new ArrayList<>(Arrays.asList(2, 3)));
        expected.add(new ArrayList<>(Arrays.asList(1 ,2 ,3)));
        Set<List<Integer>> actual = new HashSet<>(subsetsDFS(nums));
        assertEquals(expected.size(), actual.size());
        for(List<Integer> subset : expected) {
            assertTrue(actual.contains(subset));
        }
    }


    @Test
    public void subsetsBFSTest() {
        /**
         * Input: nums = [1,2,3]
         * Output:
         * [
         *   [3],
         *   [1],
         *   [2],
         *   [1,2,3],
         *   [1,3],
         *   [2,3],
         *   [1,2],
         *   []
         * ]
         */
        int[] nums = new int[]{1, 2, 3};
        Set<List<Integer>> expected = new HashSet<>();
        expected.add(new ArrayList<>());
        expected.add(new ArrayList<>(Arrays.asList(1)));
        expected.add(new ArrayList<>(Arrays.asList(2)));
        expected.add(new ArrayList<>(Arrays.asList(3)));
        expected.add(new ArrayList<>(Arrays.asList(1, 2)));
        expected.add(new ArrayList<>(Arrays.asList(1, 3)));
        expected.add(new ArrayList<>(Arrays.asList(2, 3)));
        expected.add(new ArrayList<>(Arrays.asList(1 ,2 ,3)));
        Set<List<Integer>> actual = new HashSet<>(subsetsBFS(nums));
        assertEquals(expected.size(), actual.size());
        for(List<Integer> subset : expected) {
            assertTrue(actual.contains(subset));
        }
    }
}
