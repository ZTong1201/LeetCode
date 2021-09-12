import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MaximizeDistanceToClosestPerson {

    /**
     * You are given an array representing a row of seats where seats[i] = 1 represents a person sitting in the ith seat,
     * and seats[i] = 0 represents that the ith seat is empty (0-indexed).
     * <p>
     * There is at least one empty seat, and at least one person sitting.
     * <p>
     * Alex wants to sit in the seat such that the distance between him and the closest person to him is maximized.
     * <p>
     * Return that maximum distance to the closest person.
     * <p>
     * Constraints:
     * <p>
     * 2 <= seats.length <= 2 * 10^4
     * seats[i] is 0 or 1.
     * At least one seat is empty.
     * At least one seat is occupied.
     * <p>
     * Approach: Two pointers
     * This is a similar question to LeetCode 855: https://leetcode.com/problems/exam-room/
     * We could reuse the same idea to find the maximum. Essentially, we need to find the index (i) in which the first person
     * sits. Then moving to another occupied seat (say index j), the maximum distance if sitting in between would be (j - i) / 2.
     * We keep traversing until the last person, and find the maximum distance on the fly. Note that, there are two edge cases
     * 1. The first seat (index 0) is not occupied, and sitting at index 0 will give the maximum distance. Hence, we can initialize
     * the final distance max(1, first occupied seat (index i) - 0)
     * 2. The final seat (index n - 1) is not occupied, and sitting there will give the maximum. After reaching the final occupied
     * seat, we add one last check to cover this corner case
     * <p>
     * Time: O(n) need to traverse the entire array
     * Space: O(1)
     */
    public int maxDistToClosest(int[] seats) {
        int n = seats.length;
        int firstPersonIndex = 0;
        // find the first occupied seat
        for (int i = 0; i < n; i++) {
            if (seats[i] == 1) {
                firstPersonIndex = i;
                break;
            }
        }

        // initialize the maximum distance
        // take the corner case in which sitting at index 0 gives the maximum distance into account
        int maxDistance = Math.max(1, firstPersonIndex);
        int prev = firstPersonIndex;

        for (int i = firstPersonIndex + 1; i < n; i++) {
            // find next occupied seat
            if (seats[i] == 1) {
                // update the maximum distance
                maxDistance = Math.max(maxDistance, (i - prev) / 2);
                // also move the previous occupied seat pointer
                prev = i;
            }
        }

        // check the second corner case in which sitting at index n - 1 gives the maxmium distance
        maxDistance = Math.max(maxDistance, n - 1 - prev);
        return maxDistance;
    }

    @Test
    public void maxDistToClosestTest() {
        /**
         * Example 1:
         * Input: seats = [1,0,0,0,1,0,1]
         * Output: 2
         * Explanation:
         * If Alex sits in the second open seat (i.e. seats[2]), then the closest person has distance 2.
         * If Alex sits in any other open seat, the closest person has distance 1.
         * Thus, the maximum distance to the closest person is 2.
         */
        assertEquals(2, maxDistToClosest(new int[]{1, 0, 0, 0, 1, 0, 1}));
        /**
         * Example 2:
         * Input: seats = [1,0,0,0]
         * Output: 3
         * Explanation:
         * If Alex sits in the last seat (i.e. seats[3]), the closest person is 3 seats away.
         * This is the maximum distance possible, so the answer is 3.
         */
        assertEquals(3, maxDistToClosest(new int[]{1, 0, 0, 0}));
        /**
         * Example 3:
         * Input: seats = [0,1]
         * Output: 1
         */
        assertEquals(1, maxDistToClosest(new int[]{0, 1}));
    }
}
