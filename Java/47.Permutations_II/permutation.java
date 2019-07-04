import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class permutation {

    /**
     * Given a collection of numbers that might contain duplicates, return all possible unique permutations.
     *
     * Backtrack:
     * We can implement the exact algorithm as in 46.permutation. Using swap to find all the permutations and store them in a hashset to
     * avoid duplicates.
     *
     * Time: O(∑P(N,k))
     * Space: O(N!) in the worst case if all elements are distinct, otherwise we'll expect less than N! final permutations
     */
    public List<List<Integer>> permuteUnique1(int[] nums) {
        Set<List<Integer>> res = new HashSet<>(); // use a set to store final results
        List<Integer> permutation = new ArrayList<>();
        for(int num : nums) permutation.add(num);
        int length = nums.length;
        backtrackNaive(length, res, permutation, 0);
        return new ArrayList<>(res);
    }

    private void backtrackNaive(int length, Set<List<Integer>> res, List<Integer> nums, int first) {
        if(first == length) res.add(new ArrayList<>(nums));
        for(int i = first; i < length; i++) {
            Collections.swap(nums, first, i);
            backtrackNaive(length, res, nums, first + 1);
            Collections.swap(nums, first, i);
        }
    }

    /**
     * We can actually avoid duplicates while we running the algorithm. At each state, we build a hash set to contain current visited
     * elements for that state. We only swap elements, backtrack, reswap when we find a unique value for that stage. By doing so, we reduce
     * the redundant backtrack process to avoid duplicates.
     *
     * Time: O(∑P(N,k)) in the worst case. On average, it will totally depend on the input array, i.e. how many distinct values
     * Space: O(N!) in the worst case if all elements are distinct
     */
    public List<List<Integer>> permuteUnique2(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        List<Integer> permutation = new ArrayList<>();
        for(int num : nums) permutation.add(num);
        int length = nums.length;
        backtrack(length, res, permutation, 0);
        return res;
    }

    private void backtrack(int length, List<List<Integer>> res, List<Integer> nums, int first) {
        if(first == length) res.add(new ArrayList<>(nums));
        Set<Integer> used = new HashSet<>();
        for(int i = first; i < length; i++) {
            if(used.add(nums.get(i))) { //return true if the added value is not in the set
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
        for(List<Integer> aList : expected1) {
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
        for(List<Integer> aList : expected2) {
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
        for(List<Integer> aList : expected1) {
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
        for(List<Integer> aList : expected2) {
            assertTrue(actual2.contains(aList));
        }
    }

}
