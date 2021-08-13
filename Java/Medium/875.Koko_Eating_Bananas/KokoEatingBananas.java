import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class KokoEatingBananas {

    /**
     * Koko loves to eat bananas. There are n piles of bananas, the ith pile has piles[i] bananas. The guards have gone
     * and will come back in h hours.
     * <p>
     * Koko can decide her bananas-per-hour eating speed of k. Each hour, she chooses some pile of bananas and eats k bananas
     * from that pile. If the pile has less than k bananas, she eats all of them instead and will not eat any more bananas
     * during this hour.
     * Koko likes to eat slowly but still wants to finish eating all the bananas before the guards return.
     * <p>
     * Return the minimum integer k such that she can eat all the bananas within h hours.
     * <p>
     * Constraints:
     * <p>
     * 1 <= piles.length <= 10^4
     * piles.length <= h <= 10^9
     * 1 <= piles[i] <= 10^9
     * <p>
     * Approach: Binary Search
     * This problem is find a minimum possible solution K, where when x < K, f(x) = false, when x >= K, f(x) = true. Hence we
     * can use binary search to find this point. We first traverse the entire array to find the maximum value W. Then the
     * candidate values would be 1-W. Then we execute the binary search process, as long as the current speed s cannot fulfill
     * the requirement, we move to the right half to search for a higher speed, otherwise we move the left.
     * <p>
     * Time: O(NlogW) N is the size of piles array, W is the maximum number in piles
     * Space: O(1)
     */
    public int minEatingSpeed(int[] piles, int h) {
        // the minimum speed will always be 1 and the upper bound is the maximum value in the array
        int left = 1, right = findMax(piles);
        // 使用左闭右开区间，若区间内所有值都不满足条件，则最终会返回该array的最大值
        while (left < right) {
            int mid = (right - left) / 2 + left;
            // if current speed cannot finish eating all bananas
            // search the right half for a larger speed
            if (!possible(piles, h, mid)) {
                left = mid + 1;
            } else {
                // otherwise, search the left half for a smaller value
                right = mid;
            }
        }
        return left;
    }

    private boolean possible(int[] piles, int h, int speed) {
        int hours = 0;
        for (int pile : piles) {
            // plus the number of hours to finish current pile with designated speed
            hours += (pile - 1) / speed + 1;
            if (hours > h) return false;
        }
        return true;
    }

    private int findMax(int[] piles) {
        // the value cannot be less than 0
        int res = 0;
        for (int pile : piles) {
            res = Math.max(pile, res);
        }
        return res;
    }

    @Test
    public void minEatingSpeedTest() {
        /**
         * Example 1:
         * Input: piles = [3,6,7,11], h = 8
         * Output: 4
         */
        assertEquals(4, minEatingSpeed(new int[]{3, 6, 7, 11}, 8));
        /**
         * Example 2:
         * Input: piles = [30,11,23,4,20], h = 5
         * Output: 30
         */
        assertEquals(30, minEatingSpeed(new int[]{30, 11, 23, 4, 20}, 5));
        /**
         * Example 3:
         * Input: piles = [30,11,23,4,20], h = 6
         * Output: 23
         */
        assertEquals(23, minEatingSpeed(new int[]{30, 11, 23, 4, 20}, 6));
    }
}
