import org.junit.Test;
import static org.junit.Assert.*;

public class monotonicArray {

    /**
     * An array is monotonic if it is either monotone increasing or monotone decreasing.
     *
     * An array A is monotone increasing if for all i <= j, A[i] <= A[j].  An array A is monotone decreasing if for all i <= j, A[i] >= A[j].
     *
     * Return true if and only if the given array A is monotonic.
     *
     * Approach 1: Two Pass
     * Since a monotonic array can be either increasing or decreasing, we can simply iterate over the entire array twice to check
     * whether the array is increasing or decreasing. If the array is neither increasing nor decreasing, we return false. Otherwise,
     * return true;
     *
     * Time: O(2N) = O(N), since we iterate over the array twice
     * Space: O(1) no extra space required
     */
    public boolean isMonotonicTwoPass(int[] A) {
        return increasing(A) || decreasing(A);
    }

    //check whether the given array is increasing
    private boolean increasing(int[] A) {
        for(int i = 0; i < A.length - 1; i++) {
            //since we assume the array is increasing, if at any time we found A[i] > A[i + 1], it is not increasing
            if(A[i] > A[i + 1]) return false;
        }
        return true;
    }

    //check whether the given array is decreasing
    private boolean decreasing(int[] A) {
        for(int i = 0; i < A.length - 1; i++) {
            //since we assume the array is decreasing, if at any time we found A[i] < A[i + 1], it is not decreasing
            if(A[i] < A[i + 1]) return false;
        }
        return true;
    }

    /**
     * Approach 2: One pass
     * We can actually assign a flag value to record whether the current array is increasing or decreasing. Using set {-1, 0, 1} to
     * represent smaller than, equal, and larger than. If the array is increasing, we would only see values of {0, 1}. If the array is
     * decreasing, the set values will be {-1, 0}. If we see other values, the array is not monotonic
     *
     * Time: O(n), since we only iterate over the array once
     * Space: O(1), only assign a flag value and the compare result of two values in the array
     */
    public boolean isMonotonicOnePass(int[] A) {
        int flag = 0;
        for(int i = 0; i < A.length - 1; i++) {
            int c = Integer.compare(A[i], A[i + 1]);  //return {-1, 0, 1}
            //if two values are not equal, either increasing or decreasing
            if(c != 0) {
                //if flag is not 0, and c != flag means the current tendency is not matched with previous tendency, return false
                if(c != flag && flag != 0) return false;
                //otherwise, record the current tendency
                flag = c;
            }
        }
        return true;
    }

    /**
     * Approach 3: One pass with simple variants
     * We can combine the first two approaches together. We may assume the array is increasing and decreasing at the very beginning
     * (However, we know that cannot be true). During one simple pass, we can check whether the increasing or the decreasing tendency holds
     * for the array. If in the end, neither of these two tendencies are false, the array is not monotonic
     *
     * Time: O(n), still one pass
     * Space: O(1), only assign two boolean variants
     */
    public boolean isMonotonicOnePassSimple(int[] A) {
        boolean increasing = true;
        boolean decreasing = true;
        for(int i = 0; i < A.length - 1; i++) {
            if(A[i] > A[i + 1]) increasing = false;
            if(A[i] < A[i + 1]) decreasing = false;
        }
        return increasing || decreasing;
    }

    @Test
    public void isMonotonicOnePassTest() {
        /**
         * Example 1:
         * Input: [1,2,2,3]
         * Output: true
         */
        int[] A1 = new int[]{1, 2, 2, 3};
        assertTrue(isMonotonicOnePass(A1));
        /**
         * Example 2:
         * Input: [6,5,4,4]
         * Output: true
         */
        int[] A2 = new int[]{6, 5, 4, 4};
        assertTrue(isMonotonicOnePass(A2));
        /**
         * Example 3:
         * Input: [1,3,2]
         * Output: false
         */
        int[] A3 = new int[]{1, 3, 2};
        assertFalse(isMonotonicOnePass(A3));
        /**
         * Example 4:
         * Input: [1,2,4,5]
         * Output: true
         */
        int[] A4 = new int[]{1, 2, 4, 5};
        assertTrue(isMonotonicOnePass(A4));
        /**
         * Example 5:
         * Input: [1,1,1]
         * Output: true
         */
        int[] A5 = new int[]{1, 1, 1};
        assertTrue(isMonotonicOnePass(A5));
    }

    @Test
    public void isMonotonicOnePassSimpleTest() {
        /**
         * Example 1:
         * Input: [1,2,2,3]
         * Output: true
         */
        int[] A1 = new int[]{1, 2, 2, 3};
        assertTrue(isMonotonicOnePassSimple(A1));
        /**
         * Example 2:
         * Input: [6,5,4,4]
         * Output: true
         */
        int[] A2 = new int[]{6, 5, 4, 4};
        assertTrue(isMonotonicOnePassSimple(A2));
        /**
         * Example 3:
         * Input: [1,3,2]
         * Output: false
         */
        int[] A3 = new int[]{1, 3, 2};
        assertFalse(isMonotonicOnePassSimple(A3));
        /**
         * Example 4:
         * Input: [1,2,4,5]
         * Output: true
         */
        int[] A4 = new int[]{1, 2, 4, 5};
        assertTrue(isMonotonicOnePassSimple(A4));
        /**
         * Example 5:
         * Input: [1,1,1]
         * Output: true
         */
        int[] A5 = new int[]{1, 1, 1};
        assertTrue(isMonotonicOnePassSimple(A5));
    }

    @Test
    public void isMonotonicTwoPassTest() {
        /**
         * Example 1:
         * Input: [1,2,2,3]
         * Output: true
         */
        int[] A1 = new int[]{1, 2, 2, 3};
        assertTrue(isMonotonicTwoPass(A1));
        /**
         * Example 2:
         * Input: [6,5,4,4]
         * Output: true
         */
        int[] A2 = new int[]{6, 5, 4, 4};
        assertTrue(isMonotonicTwoPass(A2));
        /**
         * Example 3:
         * Input: [1,3,2]
         * Output: false
         */
        int[] A3 = new int[]{1, 3, 2};
        assertFalse(isMonotonicTwoPass(A3));
        /**
         * Example 4:
         * Input: [1,2,4,5]
         * Output: true
         */
        int[] A4 = new int[]{1, 2, 4, 5};
        assertTrue(isMonotonicTwoPass(A4));
        /**
         * Example 5:
         * Input: [1,1,1]
         * Output: true
         */
        int[] A5 = new int[]{1, 1, 1};
        assertTrue(isMonotonicTwoPass(A5));
    }
}
