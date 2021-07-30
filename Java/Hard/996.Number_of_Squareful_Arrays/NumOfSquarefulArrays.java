import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class NumOfSquarefulArrays {

    /**
     * Given an array nums of non-negative integers, the array is squareful if for every pair of adjacent elements, their
     * sum is a perfect square.
     * <p>
     * Return the number of permutations of nums that are squareful.  Two permutations perm1 and perm2 differ if and only if
     * there is some index i such that perm1[i] != perm2[i].
     * <p>
     * Note:
     * <p>
     * 1 <= nums.length <= 12
     * 0 <= nums[i] <= 10^9
     * <p>
     * Approach 1: DFS (backtrack)
     * Enumerate all permutations with pruning.
     * Same approach as LeetCode 47: https://leetcode.com/problems/permutations-ii/
     * Sort the array first to avoid duplicate permutations. List all the permutations using backtrack, however, we can terminate
     * the search early if the current sum is not squareful
     * <p>
     * Time: O(n!) in the worst case we have n! permutations to be searched
     * Space: O(n) check whether the permutation array is squareful on the fly - hence the list is of size n at most
     */
    private int count;

    public int numSquarefulPermsDFS(int[] nums) {
        // remember to sort the array first to avoid duplicate permutations
        Arrays.sort(nums);
        count = 0;
        boolean[] visited = new boolean[nums.length];
        permute(nums, visited, new ArrayList<>());
        return count;
    }

    /**
     * Enumerate all the possible permutations and check whether the array is squareful on the fly.
     *
     * @param nums        input array
     * @param visited     a boolean array to record whether a certain index has been used or not
     * @param permutation the permuted array
     */
    private void permute(int[] nums, boolean[] visited, List<Integer> permutation) {
        // if a squareful permutation has been found - increment the count
        if (permutation.size() == nums.length) {
            count++;
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            // skip visited values or duplicate starting values
            if (visited[i] || (i > 0 && !visited[i - 1] && nums[i - 1] == nums[i])) continue;
            // check whether the current sub-array is squareful on the fly
            // only keep searching when it's already squareful
            if (permutation.size() == 0 || isSquare(permutation.get(permutation.size() - 1) + nums[i])) {
                visited[i] = true;
                permutation.add(nums[i]);
                permute(nums, visited, permutation);
                visited[i] = false;
                permutation.remove(permutation.size() - 1);
            }
        }
    }

    private boolean isSquare(int val) {
        double res = Math.sqrt(val);
        return (res - Math.floor(res)) == 0;
    }

    @Test
    public void numSquarefulPermsTest() {
        /**
         * Example 1:
         * Input: nums = [1,17,8]
         * Output: 2
         * Explanation:
         * [1,8,17] and [17,8,1] are the valid permutations.
         */
        assertEquals(2, numSquarefulPermsDFS(new int[]{1, 17, 8}));
        /**
         * Example 2:
         * Input: nums = [2,2,2]
         * Output: 1
         */
        assertEquals(1, numSquarefulPermsDFS(new int[]{2, 2, 2}));
        /**
         * Example 3:
         * Input: nums = [1,1,8,1,8]
         * Output: 1
         * Explanation: only [1,8,1,8,1] is valid
         */
        assertEquals(1, numSquarefulPermsDFS(new int[]{1, 1, 8, 1, 8}));
    }
}
