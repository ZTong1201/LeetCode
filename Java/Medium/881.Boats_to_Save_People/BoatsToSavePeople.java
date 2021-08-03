import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class BoatsToSavePeople {

    /**
     * You are given an array people where people[i] is the weight of the ith person, and an infinite number of boats where
     * each boat can carry a maximum weight of limit. Each boat carries at most two people at the same time, provided the
     * sum of the weight of those people is at most limit.
     * <p>
     * Return the minimum number of boats to carry every given person.
     * <p>
     * Constraints:
     * <p>
     * 1 <= people.length <= 5 * 10^4
     * 1 <= people[i] <= limit <= 3 * 10^4
     * <p>
     * Approach: Greedy (two pointers)
     * According to the problem constraints, the lightest person can possibly share the boat with the heaviest person. If
     * they cannot share, then the heaviest person should have used a single boat. Always try to pair the lightest and the
     * heaviest people will ultimately reduce the number of boats used.
     * <p>
     * Time: O(nlogn) the sorting time will dominate the performance
     * Space: O(n) or O(1) depends on which sorting algorithm is used
     */
    public int numRescueBoats(int[] people, int limit) {
        Arrays.sort(people);
        int left = 0, right = people.length - 1;
        int res = 0;
        while (left <= right) {
            // need a new boat
            res++;
            // check whether the lightest person and the heaviest person can share a boat
            // if yes, this pair has been rescued
            // update the lightest person for next check
            if (people[left] + people[right] <= limit) left++;
            // if no, the heaviest person will always need a standalone boat
            right--;
        }
        return res;
    }

    @Test
    public void numRescueBoatsTest() {
        /**
         * Example 1:
         * Input: people = [1,2], limit = 3
         * Output: 1
         * Explanation: 1 boat (1, 2)
         */
        assertEquals(1, numRescueBoats(new int[]{1, 2}, 3));
        /**
         * Example 2:
         * Input: people = [3,2,2,1], limit = 3
         * Output: 3
         * Explanation: 3 boats (1, 2), (2) and (3)
         */
        assertEquals(3, numRescueBoats(new int[]{3, 2, 2, 1}, 3));
        /**
         * Example 3:
         * Input: people = [3,5,3,4], limit = 5
         * Output: 4
         * Explanation: 4 boats (3), (3), (4), (5)
         */
        assertEquals(4, numRescueBoats(new int[]{3, 5, 3, 4}, 5));
    }
}
