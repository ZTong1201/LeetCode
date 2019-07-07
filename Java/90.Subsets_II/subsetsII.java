import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class subsetsII {

    /**
     * Given a collection of integers that might contain duplicates, nums, return all possible subsets (the power set).
     *
     * Note: The solution set must not contain duplicate subsets.
     *
     * Approach 1: Use a set
     * We can repeat all the process as we did in subsets I. The only difference is to add all the subsets in a hash set, the
     * hash set will handle duplicates. However, say we have input as [1, 4, 1], we will have [1, 4], [4, 1] as different subsets.
     * To solve this problem, we must initially SORT the array to make sure that we won't have [4, 1] in the final result list.
     *
     * Time: O(2^n) we still need to search all the subsets
     * Space: O(n), the implicit call stack requires O(n)
     */
    public List<List<Integer>> subsetsWithDupHashSet(int[] nums) {
        Set<List<Integer>> res = new HashSet<>();
        List<Integer> subset = new ArrayList<>();
        backtrack(res, subset, nums, 0);
        return new ArrayList<>(res);
    }

    private void backtrack(Set<List<Integer>> res, List<Integer> subset, int[] nums, int start) {
        res.add(new ArrayList<>(subset));

        for(int i = start; i < nums.length; i++) {
            subset.add(nums[i]);
            backtrack(res, subset, nums, i + 1);
            subset.remove(subset.size() - 1);
        }
    }

    /**
     * Approach 2: skip duplicates
     * Using a hash set is convenient, however, it is not neat. Since we have sorted the array, we know that all the duplicates
     * are clustered together. Our problem is when we pop element at the end of the subset, we don't want to add duplicates once again
     * back to our subset. For example, if our input is [1, 1, 4] (sorted)
     * we will have these subset
     * []   index = 0
     * [1]
     * [1, 1]
     * [1, 1, 4]
     * [1, 4]
     * [1] after we pop all the elements out of the subset, the search will start at index = 1, but we actually want to skip it.
     *
     * As a result, we need to skip duplicates manually when we pop elements and want to add a new element
     *
     * Time: O(2^n) in the worst case, it will still be O(2^n) just like subset I
     * Space: O(n) the implicit call stack requires O(n) space
     */

    public List<List<Integer>> subsetWithDupSkipDuplicates(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        List<Integer> subset = new ArrayList<>();
        Arrays.sort(nums);      //The most important part for this algorithm. SORT IT!!!
        backtrackSkipDup(res, subset, nums, 0);
        return res;
    }

    private void backtrackSkipDup(List<List<Integer>> res, List<Integer> subset, int[] nums, int start) {
        res.add(new ArrayList<>(subset));

        for(int i = start; i < nums.length; i++) {
            subset.add(nums[i]);
            backtrackSkipDup(res, subset, nums, i + 1);
            subset.remove(subset.size() - 1);
            //While we are in the array bound, and we meet duplicates. We keep skipping duplicates until there is a new value
            while(i < nums.length - 1 && nums[i] == nums[i + 1]) i += 1;
        }
    }

    @Test
    public void subsetsWithDupHashSetTest() {
        int[] nums = new int[]{1, 2, 2};
        Set<List<Integer>> expected = new HashSet<>();
        expected.add(new ArrayList<>());
        expected.add(new ArrayList<>(Arrays.asList(1)));
        expected.add(new ArrayList<>(Arrays.asList(1, 2)));
        expected.add(new ArrayList<>(Arrays.asList(1, 2, 2)));
        expected.add(new ArrayList<>(Arrays.asList(2)));
        expected.add(new ArrayList<>(Arrays.asList(2, 2)));
        Set<List<Integer>> actual = new HashSet<>(subsetsWithDupHashSet(nums));
        assertEquals(expected.size(), actual.size());
        for(List<Integer> subset : expected) {
            assertTrue(actual.contains(subset));
        }
    }

    @Test
    public void subsetsWithDupSkipDuplicatesTest() {
        int[] nums = new int[]{1, 2, 2};
        Set<List<Integer>> expected = new HashSet<>();
        expected.add(new ArrayList<>());
        expected.add(new ArrayList<>(Arrays.asList(1)));
        expected.add(new ArrayList<>(Arrays.asList(1, 2)));
        expected.add(new ArrayList<>(Arrays.asList(1, 2, 2)));
        expected.add(new ArrayList<>(Arrays.asList(2)));
        expected.add(new ArrayList<>(Arrays.asList(2, 2)));
        Set<List<Integer>> actual = new HashSet<>(subsetWithDupSkipDuplicates(nums));
        assertEquals(expected.size(), actual.size());
        for(List<Integer> subset : expected) {
            assertTrue(actual.contains(subset));
        }
    }
}
