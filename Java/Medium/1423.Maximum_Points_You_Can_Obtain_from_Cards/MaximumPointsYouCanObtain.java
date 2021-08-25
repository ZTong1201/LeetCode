import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MaximumPointsYouCanObtain {

    /**
     * There are several cards arranged in a row, and each card has an associated number of points. The points are given
     * in the integer array cardPoints.
     * <p>
     * In one step, you can take one card from the beginning or from the end of the row. You have to take exactly k cards.
     * <p>
     * Your score is the sum of the points of the cards you have taken.
     * <p>
     * Given the integer array cardPoints and the integer k, return the maximum score you can obtain.
     * <p>
     * Constraints:
     * <p>
     * 1 <= cardPoints.length <= 10^5
     * 1 <= cardPoints[i] <= 10^4
     * 1 <= k <= cardPoints.length
     * <p>
     * Approach 1: prefix/suffix sum + DP
     * Basically, we can list all the possible solutions by removing i in [0, k] cards from the front and removing (k - i)
     * cards from the rear of the array to make the maximum points. In order to optimize the runtime when getting the sum
     * of first i elements or sum of last (k - i) elements, we can use 2 1-D arrays to store prefix and suffix sum.
     * <p>
     * Time: O(n)
     * Space: O(n)
     */
    public int maxScoreDPExtraSpace(int[] cardPoints, int k) {
        int length = cardPoints.length;
        int[] prefixSum = new int[length + 1], suffixSum = new int[length + 1];
        prefixSum[0] = 0;
        suffixSum[length] = 0;
        // compute prefix and suffix sum
        for (int i = 1; i <= length; i++) {
            prefixSum[i] = prefixSum[i - 1] + cardPoints[i - 1];
            suffixSum[length - i] = suffixSum[length + 1 - i] + cardPoints[length - i];
        }

        // list all combinations and find the maximum
        int res = 0;
        for (int i = 0; i <= k; i++) {
            res = Math.max(res, prefixSum[i] + suffixSum[length - (k - i)]);
        }
        return res;
    }

    /**
     * Approach 2: DP (space optimized)
     * Actually, we don't need all the prefix & suffix sum since we only care about at most k elements from left and right.
     * Plus, the prefix and suffix sum could be updated on the fly while enumerating all the combinations. Essentially,
     * we now only need two variables called frontScore and rearScore. We initialize frontScore as the sum of first k
     * elements in the array (meaning we remove k cards from the left but remove nothing from the right). Then we remove
     * 1 element from the front score at a time, and compute the rearScore on the fly. Then we can update the maximum result
     * by comparing the previously recorded result and new sum (frontScore + rearScore)
     * <p>
     * Time: O(k) we visit first k elements in the array to initialize frontScore, then we enumerate k possible combinations
     * to find the maximum. In the worst case, k is comparable to the length of the array, the time complexity is O(n)
     * Space: O(1)
     */
    public int maxScoreDPWithoutSpace(int[] cardPoints, int k) {
        // initialize front score
        int frontScore = 0;
        for (int i = 0; i < k; i++) {
            frontScore += cardPoints[i];
        }
        // initialize rearScore as 0 since we're going to compute it on the fly
        // also initialize the final result as frontScore
        int rearScore = 0, res = frontScore;
        // enumerate all k possible combinations
        for (int i = 0; i < k; i++) {
            // remove one element from the front score;
            frontScore -= cardPoints[k - 1 - i];
            // accumulate rear score
            rearScore += cardPoints[cardPoints.length - 1 - i];
            // update the final result
            res = Math.max(res, frontScore + rearScore);
        }
        return res;
    }

    /**
     * Approach 3: Sliding Window
     * The problem can also be solved as removing a sequence of (n - k) elements which has the smallest sum. Since the total
     * points of the entire array is fixed, if we can remove a window of (n - k) sub-array with the smallest sum, the rest
     * of k cards would've had the largest sum.
     * <p>
     * The algorithm looks like this:
     * 1. compute the total points by iterating over the entire array.
     * 2. maintain a window of size (n - k)
     * 3. keep sliding to the right and find the minimum sum within the window
     * 4. return total point - minimum sum
     * <p>
     * Time: O(n)
     * Space: O(1)
     */
    public int maxScoreSlidingWindow(int[] cardPoints, int k) {
        int totalPoints = 0;
        for (int point : cardPoints) {
            totalPoints += point;
        }

        // compute the minimum sum within the window of size (n - k)
        int minPointsRemoved = 0, windowSum = 0;
        for (int left = 0, right = 0; right < cardPoints.length; right++) {
            // keep expanding the window while its size is less than n - k
            if (right < cardPoints.length - k) {
                // update window sum
                windowSum += cardPoints[right];
                minPointsRemoved = windowSum;
            } else {
                // we now have a window of size n - k
                // keep inserting one element from the right
                // and removing one from the left to find the minimum window sum
                windowSum = windowSum - cardPoints[left] + cardPoints[right];
                minPointsRemoved = Math.min(minPointsRemoved, windowSum);
                // remember to move the left bound to maintain the size
                left++;
            }
        }
        return totalPoints - minPointsRemoved;
    }

    @Test
    public void maxScoreTest() {
        /**
         * Example 1:
         * Input: cardPoints = [1,2,3,4,5,6,1], k = 3
         * Output: 12
         * Explanation: After the first step, your score will always be 1. However, choosing the rightmost card first will
         * maximize your total score. The optimal strategy is to take the three cards on the right, giving a final score
         * of 1 + 6 + 5 = 12.
         */
        int[] cardPoints1 = new int[]{1, 2, 3, 4, 5, 6, 1};
        assertEquals(12, maxScoreDPExtraSpace(cardPoints1, 3));
        assertEquals(12, maxScoreDPWithoutSpace(cardPoints1, 3));
        assertEquals(12, maxScoreSlidingWindow(cardPoints1, 3));
        /**
         * Example 2:
         * Input: cardPoints = [2,2,2], k = 2
         * Output: 4
         * Explanation: Regardless of which two cards you take, your score will always be 4.
         */
        int[] cardPoints2 = new int[]{2, 2, 2};
        assertEquals(4, maxScoreDPExtraSpace(cardPoints2, 2));
        assertEquals(4, maxScoreDPWithoutSpace(cardPoints2, 2));
        assertEquals(4, maxScoreSlidingWindow(cardPoints2, 2));
        /**
         * Example 3:
         * Input: cardPoints = [9,7,7,9,7,7,9], k = 7
         * Output: 55
         * Explanation: You have to take all the cards. Your score is the sum of points of all cards.
         */
        int[] cardPoints3 = new int[]{9, 7, 7, 9, 7, 7, 9};
        assertEquals(55, maxScoreDPExtraSpace(cardPoints3, 7));
        assertEquals(55, maxScoreDPWithoutSpace(cardPoints3, 7));
        assertEquals(55, maxScoreSlidingWindow(cardPoints3, 7));
        /**
         * Example 4:
         * Input: cardPoints = [1,1000,1], k = 1
         * Output: 1
         * Explanation: You cannot take the card in the middle. Your best score is 1.
         */
        int[] cardPoints4 = new int[]{1, 1000, 1};
        assertEquals(1, maxScoreDPExtraSpace(cardPoints4, 1));
        assertEquals(1, maxScoreDPWithoutSpace(cardPoints4, 1));
        assertEquals(1, maxScoreSlidingWindow(cardPoints4, 1));
        /**
         * Example 5:
         * Input: cardPoints = [1,79,80,1,1,1,200,1], k = 3
         * Output: 202
         */
        int[] cardPoints5 = new int[]{1, 79, 80, 1, 1, 1, 200, 1};
        assertEquals(202, maxScoreDPExtraSpace(cardPoints5, 3));
        assertEquals(202, maxScoreDPWithoutSpace(cardPoints5, 3));
        assertEquals(202, maxScoreSlidingWindow(cardPoints5, 3));
    }
}
