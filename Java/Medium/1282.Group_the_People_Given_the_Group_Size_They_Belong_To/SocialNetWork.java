import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class SocialNetWork {

    /**
     * There are n people that are split into some unknown number of groups. Each person is labeled with a unique ID from
     * 0 to n - 1.
     * <p>
     * You are given an integer array groupSizes, where groupSizes[i] is the size of the group that person i is in.
     * For example, if groupSizes[1] = 3, then person 1 must be in a group of size 3.
     * <p>
     * Return a list of groups such that each person i is in a group of size groupSizes[i].
     * <p>
     * Each person should appear in exactly one group, and every person must be in a group. If there are multiple answers,
     * return any of them. It is guaranteed that there will be at least one valid solution for the given input.
     * <p>
     * Constraints:
     * <p>
     * groupSizes.length == n
     * 1 <= n <= 500
     * 1 <= groupSizes[i] <= n
     * <p>
     * Approach: Greedy
     * Always assign people together who has the same group size. Once the entire group has been filled, add it to the final
     * result and remove it from the mapping.
     * <p>
     * Time: O(n)
     * Space: O(n)
     */
    public List<List<Integer>> groupThePeople(int[] groupSizes) {
        // group size -> a list of member ID
        Map<Integer, List<Integer>> groups = new HashMap<>();
        List<List<Integer>> res = new ArrayList<>();

        for (int memberId = 0; memberId < groupSizes.length; memberId++) {
            int currSize = groupSizes[memberId];
            // get the current group of people based on the group size
            List<Integer> group = groups.getOrDefault(currSize, new ArrayList<>());
            // add new people to the same group
            group.add(memberId);
            // update the mapping
            groups.put(currSize, group);
            // if the group has been filled
            // add it to the final list and remove the mapping
            if (group.size() == groupSizes[memberId]) {
                res.add(group);
                groups.remove(currSize);
            }
        }
        return res;
    }

    @Test
    public void groupThePeopleTest() {
        /**
         * Example 1:
         * Input: groupSizes = [3,3,3,3,3,1,3]
         * Output: [[5],[0,1,2],[3,4,6]]
         * Explanation:
         * The first group is [5]. The size is 1, and groupSizes[5] = 1.
         * The second group is [0,1,2]. The size is 3, and groupSizes[0] = groupSizes[1] = groupSizes[2] = 3.
         * The third group is [3,4,6]. The size is 3, and groupSizes[3] = groupSizes[4] = groupSizes[6] = 3.
         * Other possible solutions are [[2,1,6],[5],[0,4,3]] and [[5],[0,6,2],[4,3,1]].
         */
        List<List<Integer>> expected1 = List.of(List.of(0, 1, 2), List.of(5), List.of(3, 4, 6));
        List<List<Integer>> actual1 = groupThePeople(new int[]{3, 3, 3, 3, 3, 1, 3});
        assertEquals(expected1.size(), actual1.size());
        for (int i = 0; i < expected1.size(); i++) {
            assertEquals(expected1.get(i).size(), actual1.get(i).size());
            for (int j = 0; j < expected1.get(i).size(); j++) {
                assertEquals(expected1.get(i).get(j), actual1.get(i).get(j));
            }
        }
        /**
         * Example 2:
         * Input: groupSizes = [2,1,3,3,3,2]
         * Output: [[1],[0,5],[2,3,4]]
         */
        List<List<Integer>> expected2 = List.of(List.of(1), List.of(2, 3, 4), List.of(0, 5));
        List<List<Integer>> actual2 = groupThePeople(new int[]{2, 1, 3, 3, 3, 2});
        assertEquals(expected2.size(), actual2.size());
        for (int i = 0; i < expected2.size(); i++) {
            assertEquals(expected2.get(i).size(), actual2.get(i).size());
            for (int j = 0; j < expected2.get(i).size(); j++) {
                assertEquals(expected2.get(i).get(j), actual2.get(i).get(j));
            }
        }
    }
}
