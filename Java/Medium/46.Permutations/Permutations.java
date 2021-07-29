import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Permutations {

    /**
     * Given a collection of *distinct* integers, return all possible permutations.
     * <p>
     * Backtracking:
     * We use a set to record visited numbers so far. If the current solution candidate turns out NOT to be a solution, we simply discard
     * the current stage and get back to the previous stage, i.e. backtracks and then try again.
     * <p>
     * Time: O(∑P(N,k)): it is like a tree structure, we have to visit all the nodes to get our final result
     * k=1
     * <p>
     * e.g. [1, 2, 3]
     *                                      root
     *                                    /  |  \
     *                                  [1] [2] [3]              P(3, 1) = 3
     *                                 / \  / \ /  \
     *                            [1,2][1,3][2,1][2,3][3,1][3,2]   P(3, 2) = 3*2
     *                            /  \
     *                       [1,2,3][1,3,2].....................    P(3, 3) = 3*2*1
     * <p>
     * Hence we have to sum them all up to get final number of nodes visited
     * <p>
     * O(N*N!) < O(∑P(N,k)) < O(N!)
     * <p>
     * Space: O(N!) to contain final solution, and O(N) to store numbers visited
     */
    private List<List<Integer>> res;

    public List<List<Integer>> permute1(int[] nums) {
        res = new ArrayList<>();
        boolean[] visited = new boolean[nums.length];
        backtrack(nums, visited, new ArrayList<>());
        return res;
    }

    private void backtrack(int[] nums, boolean[] visited, List<Integer> permutation) {
        // since the nums array only contains unique values
        // add current permutation to the result list once the length equals to nums length
        if (permutation.size() == nums.length) res.add(new ArrayList<>(permutation));
        // always starts from 0 index - since it's a permutation
        for (int i = 0; i < nums.length; i++) {
            // skip visited elements
            if (visited[i]) continue;
            visited[i] = true;
            permutation.add(nums[i]);
            // keep searching
            backtrack(nums, visited, permutation);
            // backtrack
            permutation.remove(permutation.size() - 1);
            visited[i] = false;
        }
    }

    /**
     * We don't actually need a set to record visited numbers to backtrack. Besides, for each time, it is also unnecessary to start from
     * the very beginning of the array. We can simply keep swapping between where we start and where we end, if our pointer at any time
     * reaches the end of the array, we find a solution. After that, we swap back the elements, i.e. backtracks and try again.
     * <p>
     * Time: O(∑P(N,k))
     * Space: O(N!)
     */
    public List<List<Integer>> permute2(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        List<Integer> permutation = new ArrayList<>();
        for (int num : nums) {
            permutation.add(num);
        }
        int length = nums.length;
        backtrackSwap(length, res, permutation, 0);
        return res;
    }

    private void backtrackSwap(int length, List<List<Integer>> res, List<Integer> nums, int first) {
        //if all integers are used up
        if (first == length) res.add(new ArrayList<>(nums));
        for (int i = first; i < length; i++) {
            //place i-th integer first in the current permutation
            Collections.swap(nums, first, i);
            //use next integers to complete the permutation
            backtrackSwap(length, res, nums, first + 1);
            //backtrack
            Collections.swap(nums, first, i);
        }
    }

    @Test
    public void permute1Test() {
        /**
         * Example 1:
         * Input: [1,2,3]
         * Output:
         * [
         *   [1,2,3],
         *   [1,3,2],
         *   [2,1,3],
         *   [2,3,1],
         *   [3,1,2],
         *   [3,2,1]
         * ]
         */
        int[] nums1 = new int[]{1, 2, 3};
        Set<List<Integer>> actual1 = new HashSet<>(permute1(nums1));
        Set<List<Integer>> expected1 = new HashSet<>();
        expected1.add(new ArrayList<>(Arrays.asList(1, 2, 3)));
        expected1.add(new ArrayList<>(Arrays.asList(1, 3, 2)));
        expected1.add(new ArrayList<>(Arrays.asList(2, 1, 3)));
        expected1.add(new ArrayList<>(Arrays.asList(2, 3, 1)));
        expected1.add(new ArrayList<>(Arrays.asList(3, 1, 2)));
        expected1.add(new ArrayList<>(Arrays.asList(3, 2, 1)));
        assertEquals(expected1.size(), actual1.size());
        for (List<Integer> aList : expected1) {
            assertTrue(actual1.contains(aList));
        }
        /**
         * Example 2:
         * Input: [1,2,3,4]
         * Output:
         * [
         *   [1,2,3,4],
         *   [1,2,4,3],
         *   [1,3,2,4],
         *   [1,3,4,2],
         *   [1,4,2,3],
         *   [1,4,3,2],
         *   [2,1,3,4],
         *   [2,1,4,3],
         *   [2,3,1,4],
         *   [2,3,4,1],
         *   [2,4,1,3],
         *   [2,4,3,1],
         *   [3,1,2,4],
         *   [3,1,4,2],
         *   [3,2,1,4],
         *   [3,2,4,1],
         *   [3,4,1,2],
         *   [3,4,2,1],
         *   [4,1,2,3],
         *   [4,1,3,2],
         *   [4,2,1,3],
         *   [4,2,3,1],
         *   [4,3,1,2],
         *   [4,3,2,1],
         * ]
         */
        int[] nums2 = new int[]{1, 2, 3, 4};
        Set<List<Integer>> actual2 = new HashSet<>(permute1(nums2));
        Set<List<Integer>> expected2 = new HashSet<>();
        expected2.add(new ArrayList<>(Arrays.asList(1, 2, 3, 4)));
        expected2.add(new ArrayList<>(Arrays.asList(1, 2, 4, 3)));
        expected2.add(new ArrayList<>(Arrays.asList(1, 3, 2, 4)));
        expected2.add(new ArrayList<>(Arrays.asList(1, 3, 4, 2)));
        expected2.add(new ArrayList<>(Arrays.asList(1, 4, 2, 3)));
        expected2.add(new ArrayList<>(Arrays.asList(1, 4, 3, 2)));
        expected2.add(new ArrayList<>(Arrays.asList(2, 1, 3, 4)));
        expected2.add(new ArrayList<>(Arrays.asList(2, 1, 4, 3)));
        expected2.add(new ArrayList<>(Arrays.asList(2, 3, 1, 4)));
        expected2.add(new ArrayList<>(Arrays.asList(2, 3, 4, 1)));
        expected2.add(new ArrayList<>(Arrays.asList(2, 4, 1, 3)));
        expected2.add(new ArrayList<>(Arrays.asList(2, 4, 3, 1)));
        expected2.add(new ArrayList<>(Arrays.asList(3, 1, 2, 4)));
        expected2.add(new ArrayList<>(Arrays.asList(3, 1, 4, 2)));
        expected2.add(new ArrayList<>(Arrays.asList(3, 2, 1, 4)));
        expected2.add(new ArrayList<>(Arrays.asList(3, 2, 4, 1)));
        expected2.add(new ArrayList<>(Arrays.asList(3, 4, 1, 2)));
        expected2.add(new ArrayList<>(Arrays.asList(3, 4, 2, 1)));
        expected2.add(new ArrayList<>(Arrays.asList(4, 1, 2, 3)));
        expected2.add(new ArrayList<>(Arrays.asList(4, 1, 3, 2)));
        expected2.add(new ArrayList<>(Arrays.asList(4, 2, 1, 3)));
        expected2.add(new ArrayList<>(Arrays.asList(4, 2, 3, 1)));
        expected2.add(new ArrayList<>(Arrays.asList(4, 3, 1, 2)));
        expected2.add(new ArrayList<>(Arrays.asList(4, 3, 2, 1)));
        assertEquals(expected2.size(), actual2.size());
        for (List<Integer> aList : expected2) {
            assertTrue(actual2.contains(aList));
        }
    }

    @Test
    public void permute2Test() {
        /**
         * Example 1:
         * Input: [1,2,3]
         * Output:
         * [
         *   [1,2,3],
         *   [1,3,2],
         *   [2,1,3],
         *   [2,3,1],
         *   [3,1,2],
         *   [3,2,1]
         * ]
         */
        int[] nums1 = new int[]{1, 2, 3};
        Set<List<Integer>> actual1 = new HashSet<>(permute2(nums1));
        Set<List<Integer>> expected1 = new HashSet<>();
        expected1.add(new ArrayList<>(Arrays.asList(1, 2, 3)));
        expected1.add(new ArrayList<>(Arrays.asList(1, 3, 2)));
        expected1.add(new ArrayList<>(Arrays.asList(2, 1, 3)));
        expected1.add(new ArrayList<>(Arrays.asList(2, 3, 1)));
        expected1.add(new ArrayList<>(Arrays.asList(3, 1, 2)));
        expected1.add(new ArrayList<>(Arrays.asList(3, 2, 1)));
        assertEquals(expected1.size(), actual1.size());
        for (List<Integer> aList : expected1) {
            assertTrue(actual1.contains(aList));
        }
        /**
         * Example 2:
         * Input: [1,2,3,4]
         * Output:
         * [
         *   [1,2,3,4],
         *   [1,2,4,3],
         *   [1,3,2,4],
         *   [1,3,4,2],
         *   [1,4,2,3],
         *   [1,4,3,2],
         *   [2,1,3,4],
         *   [2,1,4,3],
         *   [2,3,1,4],
         *   [2,3,4,1],
         *   [2,4,1,3],
         *   [2,4,3,1],
         *   [3,1,2,4],
         *   [3,1,4,2],
         *   [3,2,1,4],
         *   [3,2,4,1],
         *   [3,4,1,2],
         *   [3,4,2,1],
         *   [4,1,2,3],
         *   [4,1,3,2],
         *   [4,2,1,3],
         *   [4,2,3,1],
         *   [4,3,1,2],
         *   [4,3,2,1],
         * ]
         */
        int[] nums2 = new int[]{1, 2, 3, 4};
        Set<List<Integer>> actual2 = new HashSet<>(permute2(nums2));
        Set<List<Integer>> expected2 = new HashSet<>();
        expected2.add(new ArrayList<>(Arrays.asList(1, 2, 3, 4)));
        expected2.add(new ArrayList<>(Arrays.asList(1, 2, 4, 3)));
        expected2.add(new ArrayList<>(Arrays.asList(1, 3, 2, 4)));
        expected2.add(new ArrayList<>(Arrays.asList(1, 3, 4, 2)));
        expected2.add(new ArrayList<>(Arrays.asList(1, 4, 2, 3)));
        expected2.add(new ArrayList<>(Arrays.asList(1, 4, 3, 2)));
        expected2.add(new ArrayList<>(Arrays.asList(2, 1, 3, 4)));
        expected2.add(new ArrayList<>(Arrays.asList(2, 1, 4, 3)));
        expected2.add(new ArrayList<>(Arrays.asList(2, 3, 1, 4)));
        expected2.add(new ArrayList<>(Arrays.asList(2, 3, 4, 1)));
        expected2.add(new ArrayList<>(Arrays.asList(2, 4, 1, 3)));
        expected2.add(new ArrayList<>(Arrays.asList(2, 4, 3, 1)));
        expected2.add(new ArrayList<>(Arrays.asList(3, 1, 2, 4)));
        expected2.add(new ArrayList<>(Arrays.asList(3, 1, 4, 2)));
        expected2.add(new ArrayList<>(Arrays.asList(3, 2, 1, 4)));
        expected2.add(new ArrayList<>(Arrays.asList(3, 2, 4, 1)));
        expected2.add(new ArrayList<>(Arrays.asList(3, 4, 1, 2)));
        expected2.add(new ArrayList<>(Arrays.asList(3, 4, 2, 1)));
        expected2.add(new ArrayList<>(Arrays.asList(4, 1, 2, 3)));
        expected2.add(new ArrayList<>(Arrays.asList(4, 1, 3, 2)));
        expected2.add(new ArrayList<>(Arrays.asList(4, 2, 1, 3)));
        expected2.add(new ArrayList<>(Arrays.asList(4, 2, 3, 1)));
        expected2.add(new ArrayList<>(Arrays.asList(4, 3, 1, 2)));
        expected2.add(new ArrayList<>(Arrays.asList(4, 3, 2, 1)));
        assertEquals(expected2.size(), actual2.size());
        for (List<Integer> aList : expected2) {
            assertTrue(actual2.contains(aList));
        }
    }
}
