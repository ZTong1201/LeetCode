import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class permutation {

    /**
     * Given a collection of numbers that might contain duplicates, return all possible unique permutations.
     * <p>
     * Backtrack:
     * We can implement the exact algorithm as in 46.permutation. However, since there are duplicate values in the nums array,
     * we don't want to start permutation at the exact same value. To avoid duplicates, we can
     * 1. sort the array beforehand
     * 2. skip duplicated current value when (1) it's not the first value (2) the previous value hasn't been visited
     * (3) it equals to its previous value.
     * <p>
     * Time: O(∑P(N,k))
     * Space: O(N!) in the worst case if all elements are distinct, otherwise we'll expect less than N! final permutations
     */
    private List<List<Integer>> res;

    public List<List<Integer>> permuteUnique1(int[] nums) {
        res = new ArrayList<>();
        boolean[] visited = new boolean[nums.length];
        backtrack(nums, visited, new ArrayList<>());
        return res;
    }

    private void backtrack(int[] nums, boolean[] visited, List<Integer> permutation) {
        if (permutation.size() == nums.length) res.add(new ArrayList<>(permutation));
        // always starts from the very beginning
        for (int i = 0; i < nums.length; i++) {
            // skip visited values + duplicate starting point
            if (visited[i] || (i > 0 && !visited[i - 1] && nums[i - 1] == nums[i])) continue;
            visited[i] = true;
            permutation.add(nums[i]);
            // keep searching
            backtrack(nums, visited, permutation);
            // backtrack
            visited[i] = false;
            permutation.remove(permutation.size() - 1);
        }
    }

    /**
     * We can actually avoid duplicates while running the algorithm. At each state, we build a hash set to contain current visited
     * elements for that state. We only swap elements, backtrack, reswap when we find a unique value for that stage. By doing so, we reduce
     * the redundant backtrack process to avoid duplicates.
     * <p>
     * Time: O(∑P(N,k)) in the worst case. On average, it will totally depend on the input array, i.e. how many distinct values
     * Space: O(N!) in the worst case if all elements are distinct
     */
    public List<List<Integer>> permuteUnique2(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        List<Integer> permutation = new ArrayList<>();
        for (int num : nums) permutation.add(num);
        int length = nums.length;
        backtrack(length, res, permutation, 0);
        return res;
    }

    private void backtrack(int length, List<List<Integer>> res, List<Integer> nums, int first) {
        if (first == length) res.add(new ArrayList<>(nums));
        Set<Integer> used = new HashSet<>();
        for (int i = first; i < length; i++) {
            if (used.add(nums.get(i))) { //return true if the added value is not in the set
                Collections.swap(nums, first, i);
                backtrack(length, res, nums, first + 1);
                Collections.swap(nums, first, i);
            }
        }
    }

    @Test
    public void permuteUnique1Test() {
        int[] nums1 = new int[]{1, 1, 2};
        Set<List<Integer>> actual1 = new HashSet<>(permuteUnique1(nums1));
        Set<List<Integer>> expected1 = new HashSet<>();
        expected1.add(new ArrayList<>(Arrays.asList(1, 1, 2)));
        expected1.add(new ArrayList<>(Arrays.asList(1, 2, 1)));
        expected1.add(new ArrayList<>(Arrays.asList(2, 1, 1)));
        assertEquals(expected1.size(), actual1.size());
        for (List<Integer> aList : expected1) {
            assertTrue(actual1.contains(aList));
        }
        int[] nums2 = new int[]{2, 2, 1, 1};
        Set<List<Integer>> actual2 = new HashSet<>(permuteUnique1(nums2));
        Set<List<Integer>> expected2 = new HashSet<>();
        expected2.add(new ArrayList<>(Arrays.asList(2, 2, 1, 1)));
        expected2.add(new ArrayList<>(Arrays.asList(2, 1, 2, 1)));
        expected2.add(new ArrayList<>(Arrays.asList(2, 1, 1, 2)));
        expected2.add(new ArrayList<>(Arrays.asList(1, 1, 2, 2)));
        expected2.add(new ArrayList<>(Arrays.asList(1, 2, 1, 2)));
        expected2.add(new ArrayList<>(Arrays.asList(1, 2, 2, 1)));
        assertEquals(expected2.size(), actual2.size());
        for (List<Integer> aList : expected2) {
            assertTrue(actual2.contains(aList));
        }
    }

    @Test
    public void permuteUnique2Test() {
        int[] nums1 = new int[]{1, 1, 2};
        Set<List<Integer>> actual1 = new HashSet<>(permuteUnique2(nums1));
        Set<List<Integer>> expected1 = new HashSet<>();
        expected1.add(new ArrayList<>(Arrays.asList(1, 1, 2)));
        expected1.add(new ArrayList<>(Arrays.asList(1, 2, 1)));
        expected1.add(new ArrayList<>(Arrays.asList(2, 1, 1)));
        assertEquals(expected1.size(), actual1.size());
        for (List<Integer> aList : expected1) {
            assertTrue(actual1.contains(aList));
        }
        int[] nums2 = new int[]{2, 2, 1, 1};
        Set<List<Integer>> actual2 = new HashSet<>(permuteUnique2(nums2));
        Set<List<Integer>> expected2 = new HashSet<>();
        expected2.add(new ArrayList<>(Arrays.asList(2, 2, 1, 1)));
        expected2.add(new ArrayList<>(Arrays.asList(2, 1, 2, 1)));
        expected2.add(new ArrayList<>(Arrays.asList(2, 1, 1, 2)));
        expected2.add(new ArrayList<>(Arrays.asList(1, 1, 2, 2)));
        expected2.add(new ArrayList<>(Arrays.asList(1, 2, 1, 2)));
        expected2.add(new ArrayList<>(Arrays.asList(1, 2, 2, 1)));
        assertEquals(expected2.size(), actual2.size());
        for (List<Integer> aList : expected2) {
            assertTrue(actual2.contains(aList));
        }
    }

}
