import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class ThreeSum {

    /**
     * Given an array nums of n integers, are there elements a, b, c in nums such that a + b + c = 0?
     * Find all unique triplets in the array which gives the sum of zero.
     * <p>
     * Note: The solution set must not contain duplicate triplets.
     * <p>
     * The brute force approach which finds all the triplets in the array and check whether their sum equals 0 will cost O(N^3)
     * However, we can sort the array initially (O(NlogN) runtime) and then treat this problem as two sum.
     * The runtime can be reduced to O(N^2) using this method
     * <p>
     * Time: O(N^2)
     * Space: O(N), in worst case we will store all the values in a set
     */
    public List<List<Integer>> threeSumSorting(int[] nums) {
        Arrays.sort(nums);
        Set<List<Integer>> resSet = new HashSet<>();
        int target = Integer.MIN_VALUE;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == (-1) * target) continue;
            else {
                target = (-1) * nums[i];
                Set<Integer> targetMinusNum = new HashSet<>();
                for (int j = i + 1; j < nums.length; j++) {
                    if (targetMinusNum.contains(nums[j])) {
                        List<Integer> threeSumPair = new LinkedList<>();
                        threeSumPair.add((-1) * target);
                        threeSumPair.add(nums[j]);
                        threeSumPair.add(target - nums[j]);
                        resSet.add(threeSumPair);
                    } else {
                        targetMinusNum.add(target - nums[j]);
                    }
                }
            }
        }
        return new LinkedList<>(resSet);
    }

    /**
     * BEST!!
     * We can take advantage of sorting by using two pointers to reduce the extra space to store numbers has been seen
     * <p>
     * In this case,
     * Time: still O(N^2)
     * Space: Depending on the sorting algorithm, can be O(1) in the best case.
     */
    public List<List<Integer>> threeSumSortingWithTwoPointers(int[] nums) {
        // sort the array first to take advantage of the non-decreasing property
        Arrays.sort(nums);
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            // skip duplicates
            if (i > 0 && nums[i] == nums[i - 1]) continue;
            int left = i + 1, right = nums.length - 1;
            while (left < right) {
                // found a triplet whose sum equals to 0
                if (nums[left] + nums[right] == -nums[i]) {
                    res.add(List.of(nums[i], nums[left], nums[right]));
                    left++;
                    right--;
                    // skip duplicates from both sides
                    while (left < right && nums[left] == nums[left - 1]) left++;
                    while (left < right && nums[right] == nums[right + 1]) right--;
                } else if (nums[left] + nums[right] < -nums[i]) left++;
                else right--;
            }
        }
        return res;
    }

    /**
     * Actually, we don't even need sorting. However, we will require a lot of extra space to contain previous information.
     * <p>
     * Time: O(N^2)
     * Space: O(N)
     */
    public List<List<Integer>> threeSumWithoutSorting(int[] nums) {
        Map<Integer, Integer> lastIndexMap = getLastIndex(nums);
        Set<List<Integer>> resSet = new HashSet<>();

        Set<Integer> set1 = new HashSet<>();
        for (int i = 0; i < nums.length; i++) { //first loop, avoid duplicates using set1
            if (!set1.add(nums[i])) continue;

            Set<Integer> set2 = new HashSet<>();
            for (int j = i + 1; j < nums.length; j++) {
                if (!set2.add(nums[j])) continue; // second loop, avoid duplicates using set2

                if (lastIndexMap.getOrDefault(-nums[i] - nums[j], 0) > j) {
                    List<Integer> triplet = Arrays.asList(nums[i], nums[j], -nums[i] - nums[j]);
                    Collections.sort(triplet);
                    resSet.add(triplet);
                }
            }
        }
        return new ArrayList<>(resSet);
    }


    private Map<Integer, Integer> getLastIndex(int[] nums) {
        Map<Integer, Integer> indexMap = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            indexMap.put(nums[i], i);
        }
        return indexMap;
    }

    @Test
    public void threeSumSortingWithTwoPointersTest() {
        /**
         * Given array nums = [-1, 0, 1, 2, -1, -4],
         *
         * A solution set is:
         * [
         *   [-1, 0, 1],
         *   [-1, -1, 2]
         * ]
         */
        int[] test1 = new int[]{-1, 0, 1, 2, -1, -4};
        List<List<Integer>> expected1 = new LinkedList<>();
        expected1.add(new LinkedList<>(Arrays.asList(-1, -1, 2)));
        expected1.add(new LinkedList<>(Arrays.asList(-1, 0, 1)));
        List<List<Integer>> actual1 = threeSumSortingWithTwoPointers(test1);
        for (int i = 0; i < expected1.size(); i++) {
            assertEquals(new HashSet<>(expected1.get(i)), new HashSet<>(actual1.get(i)));
        }
        /**
         * Given array nums = [0, 0],
         *
         * A solution set is: []
         */
        int[] test2 = new int[]{0, 0};
        List<List<Integer>> expected2 = new LinkedList<>();
        assertEquals(expected2, threeSumSortingWithTwoPointers(test2));
        /**
         * Given array nums = [0, 0, 0, 0],
         *
         * A solution set is:
         * [
         *   [0, 0, 0]
         * ]
         */
        int[] test3 = new int[]{0, 0, 0, 0};
        List<List<Integer>> expected3 = new LinkedList<>();
        expected3.add(new LinkedList<>(Arrays.asList(0, 0, 0)));
        assertEquals(expected3, threeSumSortingWithTwoPointers(test3));
    }


    @Test
    public void threeSumSortingTest() {
        /**
         * Given array nums = [-1, 0, 1, 2, -1, -4],
         *
         * A solution set is:
         * [
         *   [-1, 0, 1],
         *   [-1, -1, 2]
         * ]
         */
        int[] test1 = new int[]{-1, 0, 1, 2, -1, -4};
        List<List<Integer>> expected1 = new LinkedList<>();
        expected1.add(new LinkedList<>(Arrays.asList(-1, -1, 2)));
        expected1.add(new LinkedList<>(Arrays.asList(-1, 0, 1)));
        List<List<Integer>> actual1 = threeSumSorting(test1);
        for (int i = 0; i < expected1.size(); i++) {
            assertEquals(new HashSet<>(expected1.get(i)), new HashSet<>(actual1.get(i)));
        }
        /**
         * Given array nums = [0, 0],
         *
         * A solution set is: []
         */
        int[] test2 = new int[]{0, 0};
        List<List<Integer>> expected2 = new LinkedList<>();
        assertEquals(expected2, threeSumSorting(test2));
        /**
         * Given array nums = [0, 0, 0, 0],
         *
         * A solution set is:
         * [
         *   [0, 0, 0]
         * ]
         */
        int[] test3 = new int[]{0, 0, 0, 0};
        List<List<Integer>> expected3 = new LinkedList<>();
        expected3.add(new LinkedList<>(Arrays.asList(0, 0, 0)));
        assertEquals(expected3, threeSumSorting(test3));
    }

    @Test
    public void threeSumWithoutSortingTest() {
        /**
         * Given array nums = [-1, 0, 1, 2, -1, -4],
         *
         * A solution set is:
         * [
         *   [-1, 0, 1],
         *   [-1, -1, 2]
         * ]
         */
        int[] test1 = new int[]{-1, 0, 1, 2, -1, -4};
        List<List<Integer>> expected1 = new LinkedList<>();
        expected1.add(new LinkedList<>(Arrays.asList(-1, -1, 2)));
        expected1.add(new LinkedList<>(Arrays.asList(-1, 0, 1)));
        List<List<Integer>> actual1 = threeSumWithoutSorting(test1);
        for (int i = 0; i < expected1.size(); i++) {
            assertEquals(new HashSet<>(expected1.get(i)), new HashSet<>(actual1.get(i)));
        }
        /**
         * Given array nums = [0, 0],
         *
         * A solution set is: []
         */
        int[] test2 = new int[]{0, 0};
        List<List<Integer>> expected2 = new LinkedList<>();
        assertEquals(expected2, threeSumWithoutSorting(test2));
        /**
         * Given array nums = [0, 0, 0, 0],
         *
         * A solution set is:
         * [
         *   [0, 0, 0]
         * ]
         */
        int[] test3 = new int[]{0, 0, 0, 0};
        List<List<Integer>> expected3 = new LinkedList<>();
        expected3.add(new LinkedList<>(Arrays.asList(0, 0, 0)));
        assertEquals(expected3, threeSumWithoutSorting(test3));
    }

}
